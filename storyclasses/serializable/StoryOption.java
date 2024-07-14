package storyclasses.serializable;

import java.io.Serializable;

public final class StoryOption implements Serializable {
    private final String text;
    private final StoryKeys[] unlockingKeys;
    private final StoryKeys[] lockingKeys;
    private final boolean forced;
    private final StoryNode storyNode;

    public StoryOption(final String text, final StoryNode storyNode, final StoryKeys[] unlockingKeys, final StoryKeys[] lockingKeys,
            final boolean forced) {
        this.text = text;
        this.storyNode = storyNode;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
        this.forced = forced;
    }

    public StoryOption(final String text, final StoryNode storyNode) {
        this.text = text;
        this.storyNode = storyNode;
        this.unlockingKeys = new StoryKeys[0];
        this.lockingKeys = new StoryKeys[0];
        this.forced = false;
    }

    public String getText() {
        return text;
    }

    public StoryNode getStoryNode() {
        return storyNode;
    }

    public StoryKeys[] getUnlockingKeys() {
        return unlockingKeys;
    }

    public StoryKeys[] getLockingKeys() {
        return lockingKeys;
    }

    public boolean isForced() {
        return forced;
    }

}