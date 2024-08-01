package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryOption extends GuiTextBox {

    private List<StoryKey> unlockingKeys;
    private List<StoryKey> lockingKeys;
    private boolean forced;

    public GuiStoryOption(String text) {
        super(text, 0, 0);
        this.unlockingKeys = new ArrayList<StoryKey>();
        this.lockingKeys = new ArrayList<StoryKey>();
        this.forced = false;
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

    @Override
    public void connectOutput(GuiConnectableBox bindable) {
        this.outputs.clear();
        this.outputs.add(bindable);
    }

    @Override
    public void connectInput(GuiConnectableBox bindable) {
        if (bindable instanceof GuiStoryNode) {
            this.inputs.clear();
            this.inputs.add(bindable);
        }
    }

    @Override
    public void disconnectOutput(GuiConnectableBox bindable) {
        this.outputs.clear();
    }

    @Override
    public void disconnectInput(GuiConnectableBox bindable) {
        this.inputs.clear();
    }
}