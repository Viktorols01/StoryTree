package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryOption extends GuiTextBox {

    private GuiStoryNode parent;
    private List<StoryKey> unlockingKeys;
    private List<StoryKey> lockingKeys;
    private boolean forced;
    private GuiStoryNode child;

    public GuiStoryOption(String text, GuiStoryNode parent, GuiStoryNode child) {
        super(text, 0, 0, 10);
        this.parent = parent;
        this.unlockingKeys = new ArrayList<StoryKey>();
        this.lockingKeys = new ArrayList<StoryKey>();
        this.forced = false;
        this.child = child;
    }

    public List<StoryKey> getUnlockingKeys() {
        return unlockingKeys;
    }

    public List<StoryKey> getLockingKeys() {
        return lockingKeys;
    }

    public void setUnlockingKeys(List<StoryKey> unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public void setLockingKeys(List<StoryKey> lockingKeys) {
        this.lockingKeys = lockingKeys;
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public GuiStoryNode getChild() {
        return this.child;
    }

    public GuiStoryNode getParent() {
        return this.parent;
    }

}