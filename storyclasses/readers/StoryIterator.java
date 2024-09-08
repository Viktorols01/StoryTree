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

    private StoryState storyState;

    private String currentText;
    private List<StoryOption> availableOptionsList;

    public StoryIterator(StoryTree tree) {
        this.storyState = new StoryState(tree);
        visitNode();
    }

    public StoryIterator(StoryState state) {
        this.storyState = state;
        visitNode();
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
                this.storyState.setStoryNodeIndex(option.getStoryNodeIndex());
                visitNode();
            }
        }
    }

    private void visitNode() {
        acquireKeys();
        this.currentText = getFullText(storyState.getCurrentNode());
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

    private void acquireKeys() {
        for (StoryKey addedKey : storyState.getCurrentNode().getAddedKeys()) {
            addKey(addedKey);
        }
        for (StoryKey removedKey : storyState.getCurrentNode().getRemovedKeys()) {
            removeKey(removedKey);
        }
    }

    private void addKey(StoryKey addedKey) {
        if (storyState.getKeys().containsKey(addedKey.getKey())) {
            int currentValue = storyState.getKeys().get(addedKey.getKey());
            storyState.getKeys().put(addedKey.getKey(), currentValue + addedKey.getValue());
        } else {
            storyState.getKeys().put(addedKey.getKey(), addedKey.getValue());
        }
    }

    private void removeKey(StoryKey removedKey) {
        if (storyState.getKeys().containsKey(removedKey.getKey())) {
            int currentValue = storyState.getKeys().get(removedKey.getKey());
            storyState.getKeys().put(removedKey.getKey(),
                    (currentValue - removedKey.getValue() < 0) ? 0 : currentValue - removedKey.getValue());
        }
    }

    private StoryOption[] getAllStoryOptions() {
        return storyState.getCurrentNode().getStoryOptions();
    }

    private List<StoryOption> getAvailableStoryOptions() {
        List<StoryOption> storyOptionList = new LinkedList<StoryOption>();
        for (StoryOption storyOption : getAllStoryOptions()) {
            if (isUnlocked(storyOption.getUnlockingKeys(), storyOption.getLockingKeys())) {
                storyOptionList.add(storyOption);
            } else {
                System.out.println(storyOption.getText() + " blev nekad");
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
}
