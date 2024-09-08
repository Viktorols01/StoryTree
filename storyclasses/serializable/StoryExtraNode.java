package storyclasses.serializable;

import java.io.Serializable;

public final class StoryExtraNode implements Serializable {

    private String text;
    private StoryKey[] unlockingKeys;
    private StoryKey[] lockingKeys;

    private StoryExtraNode extraNode;

    public StoryExtraNode(String text, StoryKey[] unlockingKeys, StoryKey[] lockingKeys) {
        this.text = text;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
    }

    public StoryExtraNode(String text) {
        this.text = text;
        this.unlockingKeys = new StoryKey[0];
        this.lockingKeys = new StoryKey[0];
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StoryKey[] getUnlockingKeys() {
        return unlockingKeys;
    }

    public void setUnlockingKeys(StoryKey[] unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public StoryKey[] getLockingKeys() {
        return lockingKeys;
    }

    public void setLockingKeys(StoryKey[] lockingKeys) {
        this.lockingKeys = lockingKeys;
    }

    public StoryExtraNode getExtraNode() {
        return extraNode;
    }

    public void setExtraNode(StoryExtraNode extraNode) {
        this.extraNode = extraNode;
    }

    
}
