package storyclasses;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryState;

public abstract class StoryReader {
    
    private StoryState storyState;

    public StoryReader(StoryNode root) {
        this.storyState = new StoryState(root);
    }

    public StoryReader(StoryState state) {
        this.storyState = state;
    }

    public void read() {
        List<StoryOption> availableOptions = getAvailableStoryOptions();
        String[] stringAvailableOptions = toOptionStrings(availableOptions);
        displayInformationToUser(this.storyState.getCurrentNode().getText(), stringAvailableOptions);

        String selectedOption = getOptionFromUser();

        for (StoryOption option : availableOptions) {
            if (option.getText().equals(selectedOption)) {
                travelTo(option.getStoryNode());
            }
        }
    }

    public StoryState getStoryState() {
        return this.storyState;
    }

    private void travelTo(StoryNode node) {
        this.storyState.setCurrentStoryNode(node);
        for (StoryKey addedKey : node.getAddedKeys()) {
            addKey(addedKey);
        }
        for (StoryKey removedKey : node.getRemovedKeys()) {
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
            for (StoryKey unlockingKey : storyOption.getUnlockingKeys()) {
                if (this.storyState.getKeys().get(unlockingKey.getKey()) < unlockingKey.getValue()) {
                    continue;
                }
            }
            for (StoryKey lockingKey : storyOption.getLockingKeys()) {
                if (this.storyState.getKeys().get(lockingKey.getKey()) >= lockingKey.getValue()) {
                    continue;
                }
            }
            storyOptionList.add(storyOption);
        }
        return storyOptionList;
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

    protected abstract void displayInformationToUser(String text, String[] optionStrings);

    protected abstract String getOptionFromUser();
}
