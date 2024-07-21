package storyclasses.serializable;

import java.io.Serializable;

public final class StoryOption implements Serializable {
    private final String text;
    private final StoryKeys[] unlockingKeys;
    private final StoryKeys[] lockingKeys;
    private final boolean forced;
    private final int storyNodeIndex;

    public StoryOption(final String text, final int storyNodeIndex, final StoryKeys[] unlockingKeys, final StoryKeys[] lockingKeys,
            final boolean forced) {
        this.text = text;
        this.storyNodeIndex = storyNodeIndex;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
        this.forced = forced;
    }

    public StoryOption(final String text, final int storyNodeIndex) {
        this.text = text;
        this.storyNodeIndex = storyNodeIndex;
        this.unlockingKeys = new StoryKeys[0];
        this.lockingKeys = new StoryKeys[0];
        this.forced = false;
    }

    public String getText() {
        return text;
    }

    public int getStoryNodeIndex() {
        return storyNodeIndex;
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