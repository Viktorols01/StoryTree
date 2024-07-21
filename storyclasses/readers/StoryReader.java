package storyclasses.readers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import storyclasses.serializable.StoryKeys;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryState;
import storyclasses.serializable.StoryTree;

public abstract class StoryReader {

    private StoryState storyState;

    public StoryReader(StoryTree tree) {
        this.storyState = new StoryState(tree);
    }

    public StoryReader(StoryState state) {
        this.storyState = state;
    }

    public void read() {
        acquireKeys();
        displayTextToUser(this.storyState.getCurrentNode().getText());
        interact();
    }

    public void interact() {
        List<StoryOption> availableOptions = getAvailableStoryOptions();
        String[] stringAvailableOptions = toOptionStrings(availableOptions);
        if (stringAvailableOptions.length == 0) {
            return;
        } else {
            displayOptionsToUser(stringAvailableOptions);

            String selectedOption = getOptionFromUser(stringAvailableOptions);

            for (StoryOption option : availableOptions) {
                if (option.getText().equals(selectedOption)) {
                    this.storyState.setStoryNodeIndex(option.getStoryNodeIndex());
                    read();
                    return;
                }
            }
            interact();
        }
    }

    private void acquireKeys() {
        for (StoryKeys addedKey : storyState.getCurrentNode().getAddedKeys()) {
            addKey(addedKey);
        }
        for (StoryKeys removedKey : storyState.getCurrentNode().getRemovedKeys()) {
            removeKey(removedKey);
        }
    }

    private void addKey(StoryKeys addedKey) {
        if (storyState.getKeys().containsKey(addedKey.getKey())) {
            int currentValue = storyState.getKeys().get(addedKey.getKey());
            storyState.getKeys().put(addedKey.getKey(), currentValue + addedKey.getValue());
        } else {
            storyState.getKeys().put(addedKey.getKey(), addedKey.getValue());
        }
    }

    private void removeKey(StoryKeys removedKey) {
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
        outer:
        for (StoryOption storyOption : getAllStoryOptions()) {
            for (StoryKeys unlockingKey : storyOption.getUnlockingKeys()) {
                if (this.storyState.getKeys().get(unlockingKey.getKey()) < unlockingKey.getValue()) {
                    continue outer;
                }
            }
            for (StoryKeys lockingKey : storyOption.getLockingKeys()) {
                if (this.storyState.getKeys().get(lockingKey.getKey()) >= lockingKey.getValue()) {
                    continue outer;
                }
            }
            storyOptionList.add(storyOption);
        }

        for (StoryOption option : storyOptionList) {
            if (option.isForced()) {
                List<StoryOption> forcedList = new LinkedList<StoryOption>();
                forcedList.add(option);
                return forcedList; 
            }
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

    protected abstract void displayTextToUser(String text);

    protected abstract void displayOptionsToUser(String[] optionStrings);

    protected abstract String getOptionFromUser(String[] optionStrings);

    public StoryState getStoryState() {
        return this.storyState;
    }
}
