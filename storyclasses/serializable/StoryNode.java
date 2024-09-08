package storyclasses.serializable;

import java.io.Serializable;

public final class StoryNode implements Serializable {
    private String text;
    private StoryKey[] addedKeys;
    private StoryKey[] removedKeys;
    private StoryOption[] storyOptions;
    private StoryExtraNode extraNode;

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

    public void setText(String text) {
        this.text = text;
    }

    public void setStoryOptions(StoryOption[] storyOptions) {
        this.storyOptions = storyOptions;
    }

    public void setAddedKeys(StoryKey[] addedKeys) {
        this.addedKeys = addedKeys;
    }

    public void setRemovedKeys(StoryKey[] removedKeys) {
        this.removedKeys = removedKeys;
    }

    public StoryExtraNode getExtraNode() {
        return extraNode;
    }

    public void setExtraNode(StoryExtraNode extraNode) {
        this.extraNode = extraNode;
    }

}