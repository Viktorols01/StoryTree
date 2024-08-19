package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryOption extends GuiBox {

    private String text;
    private List<StoryKey> unlockingKeys;
    private List<StoryKey> lockingKeys;
    private boolean forced;

    public GuiStoryOption(String text) {
        super(0, 0, 0, 0);
        this.text = text;
        this.unlockingKeys = new ArrayList<StoryKey>();
        this.lockingKeys = new ArrayList<StoryKey>();
        this.forced = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}