package gui;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKeys;

public class GuiStoryOption extends GuiTextBox {

    private GuiStoryNode parent;
    private String optionText;
    private List<StoryKeys> unlockingKeys;
    private List<StoryKeys> lockingKeys;
    private boolean forced;
    private GuiStoryNode child;

    public GuiStoryOption(String text, GuiStoryNode parent, GuiStoryNode child) {
        super(text, 0, 0, 5);
        this.parent = parent;
        this.optionText = text;
        this.unlockingKeys = new ArrayList<StoryKeys>();
        this.lockingKeys = new ArrayList<StoryKeys>();
        this.forced = false;
        this.child = child;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public List<StoryKeys> getUnlockingKeys() {
        return unlockingKeys;
    }

    public void setUnlockingKeys(List<StoryKeys> unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public List<StoryKeys> getLockingKeys() {
        return lockingKeys;
    }

    public void setLockingKeys(List<StoryKeys> lockingKeys) {
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