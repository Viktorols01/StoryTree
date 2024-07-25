package storyclasses.serializable;

import java.io.Serializable;

public final class StoryOption implements Serializable {
    private String text;
    private StoryKey[] unlockingKeys;
    private StoryKey[] lockingKeys;
    private boolean forced;
    private int storyNodeIndex;

    public StoryOption(String text, int storyNodeIndex, StoryKey[] unlockingKeys, StoryKey[] lockingKeys,
            boolean forced) {
        this.text = text;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
        this.forced = forced;
        this.storyNodeIndex = storyNodeIndex;
    }

    public StoryOption(String text, int storyNodeIndex) {
        this.text = text;
        this.unlockingKeys = new StoryKey[0];
        this.lockingKeys = new StoryKey[0];
        this.forced = false;
        this.storyNodeIndex = storyNodeIndex;
    }

    public String getText() {
        return text;
    }

    public StoryKey[] getUnlockingKeys() {
        return unlockingKeys;
    }

    public StoryKey[] getLockingKeys() {
        return lockingKeys;
    }

    public boolean isForced() {
        return forced;
    }

    public int getStoryNodeIndex() {
        return storyNodeIndex;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUnlockingKeys(StoryKey[] unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public void setLockingKeys(StoryKey[] lockingKeys) {
        this.lockingKeys = lockingKeys;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public void setStoryNodeIndex(int storyNodeIndex) {
        this.storyNodeIndex = storyNodeIndex;
    }

    

}