package storyclasses.serializable;

import java.io.Serializable;

public final class StoryNode implements Serializable {
    private final String text;
    private final StoryKey[] addedKeys;
    private final StoryKey[] removedKeys;
    private final StoryOption[] storyOptions;

    public StoryNode(String text, StoryOption[] storyOptions, StoryKey[] addedKeys, StoryKey[] removedKeys) {
        this.text = text;
        this.storyOptions = storyOptions;
        this.addedKeys = addedKeys;
        this.removedKeys = removedKeys;
    }

    public StoryNode(String text, StoryOption[] storyOptions) {
        this.text = text;
        this.storyOptions = storyOptions;
        this.addedKeys = new StoryKey[0];
        this.removedKeys = new StoryKey[0];
    }

    public String getText() {
        return text;
    }

    public StoryOption[] getStoryOptions() {
        return storyOptions;
    }

    public StoryKey[] getAddedKeys() {
        return addedKeys;
    }

    public StoryKey[] getRemovedKeys() {
        return removedKeys;
    }

}