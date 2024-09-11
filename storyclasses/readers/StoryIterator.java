package storyclasses.readers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import storyclasses.serializable.StoryExtraNode;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryState;
import storyclasses.serializable.StoryTree;

public class StoryIterator {

    private StoryTree tree;
    private StoryState storyState;

    private String currentText;
    private List<StoryOption> availableOptionsList;

    public StoryIterator(StoryTree tree) {
        this.tree = tree;
        visitNode(0);
    }

    public StoryIterator(StoryTree tree, StoryState state) {
        this.tree = tree;
        visitNode(state.getIndex());
    }

    public String getCurrentText() {
        return currentText;
    }

    public String[] getCurrentOptions() {
        return toOptionStrings(availableOptionsList);
    }

    public boolean hasNext() {
        return availableOptionsList.size() > 0;
    }

    public void selectOption(String selectedOption) {
        for (StoryOption option : availableOptionsList) {
            if (option.getText().equals(selectedOption)) {
                visitNode(option.getStoryNodeIndex());
            }
        }
    }

    private void visitNode(int i) {
        if (storyState == null) {
            setStoryState(new StoryState(i));
        } else {
            StoryState clone = storyState == null ? null : storyState.clone();
            setStoryState(new StoryState(i, storyState.getKeys(), clone));
        }
        acquireKeysFromNode(storyState, tree.getNode(i));
        getInfo();
    }

    private void getInfo() {
        this.currentText = getFullText(tree.getNode(storyState.getIndex()));
        this.availableOptionsList = getAvailableStoryOptions();
    }

    private String getFullText(StoryNode node) {
        StringBuilder sb = new StringBuilder(node.getText());
        StoryExtraNode extraNode = node.getExtraNode();
        while (extraNode != null) {
            if (isUnlocked(extraNode.getUnlockingKeys(), extraNode.getLockingKeys())) {
                sb.append("\n");
                sb.append(extraNode.getText());
            }
            extraNode = extraNode.getExtraNode();
        }
        return sb.toString();
    }

    private static void acquireKeysFromNode(StoryState state, StoryNode node) {
        for (StoryKey addedKey : node.getAddedKeys()) {
            addKey(state, addedKey);
        }
        for (StoryKey removedKey : node.getRemovedKeys()) {
            removeKey(state, removedKey);
        }
    }

    private static void addKey(StoryState state, StoryKey addedKey) {
        if (state.getKeys().containsKey(addedKey.getKey())) {
            int currentValue = state.getKeys().get(addedKey.getKey());
            state.getKeys().put(addedKey.getKey(), currentValue + addedKey.getValue());
        } else {
            state.getKeys().put(addedKey.getKey(), addedKey.getValue());
        }
    }

    private static void removeKey(StoryState state, StoryKey removedKey) {
        if (state.getKeys().containsKey(removedKey.getKey())) {
            int currentValue = state.getKeys().get(removedKey.getKey());
            state.getKeys().put(removedKey.getKey(),
                    (currentValue - removedKey.getValue() < 0) ? 0 : currentValue - removedKey.getValue());
        }
    }

    private StoryOption[] getAllStoryOptions() {
        return tree.getNode(storyState.getIndex()).getStoryOptions();
    }

    private List<StoryOption> getAvailableStoryOptions() {
        List<StoryOption> storyOptionList = new LinkedList<StoryOption>();
        for (StoryOption storyOption : getAllStoryOptions()) {
            if (isUnlocked(storyOption.getUnlockingKeys(), storyOption.getLockingKeys())) {
                storyOptionList.add(storyOption);
            }
        }

        List<StoryOption> forcedList = new LinkedList<StoryOption>();
        for (StoryOption option : storyOptionList) {
            if (option.isForced()) {
                forcedList.add(option);
            }
        }
        if (!forcedList.isEmpty()) {
            return forcedList;
        }

        return storyOptionList;
    }

    private boolean isUnlocked(StoryKey[] unlockingKeys, StoryKey[] lockingKeys) {
        for (StoryKey unlockingKey : unlockingKeys) {
            if (!this.storyState.getKeys().containsKey(unlockingKey.getKey())
                    || this.storyState.getKeys().get(unlockingKey.getKey()) < unlockingKey.getValue()) {
                return false;
            }
        }
        for (StoryKey lockingKey : lockingKeys) {
            if (this.storyState.getKeys().containsKey(lockingKey.getKey())
                    && this.storyState.getKeys().get(lockingKey.getKey()) >= lockingKey.getValue()) {
                return false;
            }
        }
        return true;
    }

    private static String[] toOptionStrings(List<StoryOption> storyOptionList) {
        String[] stringOptions = new String[storyOptionList.size()];
        Iterator<StoryOption> iterator = storyOptionList.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            stringOptions[i] = iterator.next().getText();
            i++;
        }
        return stringOptions;
    }

    public StoryState getStoryState() {
        return this.storyState;
    }

    public void setStoryState(StoryState state) {
        this.storyState = state;
    }

    public boolean canGoBack() {
        return storyState.hasPrevious();
    }

    public void goBack() {
        setStoryState(storyState.getPrevious());
        getInfo();
    }
}
