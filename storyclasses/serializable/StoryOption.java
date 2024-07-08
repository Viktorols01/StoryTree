package storyclasses.serializable;

import java.io.Serializable;

public final class StoryOption implements Serializable {
    private final String text;
    private final StoryNode storyNode;
    private final StoryKey[] unlockingKeys;
    private final StoryKey[] lockingKeys;
    private final boolean forced;

    public StoryOption(String text, StoryNode storyNode, StoryKey[] unlockingKeys, StoryKey[] lockingKeys,
            boolean forced) {
        this.text = text;
        this.storyNode = storyNode;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
        this.forced = forced;
    }

    public StoryOption(String text, StoryNode storyNode) {
        this.text = text;
        this.storyNode = storyNode;
        this.unlockingKeys = new StoryKey[0];
        this.lockingKeys = new StoryKey[0];
        this.forced = false;
    }

    public String getText() {
        return text;
    }

    public StoryNode getStoryNode() {
        return storyNode;
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

}