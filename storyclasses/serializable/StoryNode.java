package storyclasses.serializable;

import java.io.Serializable;

public final class StoryNode implements Serializable {
    private final String text;
    private final StoryKeys[] addedKeys;
    private final StoryKeys[] removedKeys;
    private final StoryOption[] storyOptions;

    public StoryNode(final String text, final StoryOption[] storyOptions, final StoryKeys[] addedKeys, final StoryKeys[] removedKeys) {
        this.text = text;
        this.storyOptions = storyOptions;
        this.addedKeys = addedKeys;
        this.removedKeys = removedKeys;
    }

    public StoryNode(final String text, final StoryOption[] storyOptions) {
        this.text = text;
        this.storyOptions = storyOptions;
        this.addedKeys = new StoryKeys[0];
        this.removedKeys = new StoryKeys[0];
    }

    public String getText() {
        return text;
    }

    public StoryOption[] getStoryOptions() {
        return storyOptions;
    }

    public StoryKeys[] getAddedKeys() {
        return addedKeys;
    }

    public StoryKeys[] getRemovedKeys() {
        return removedKeys;
    }

}