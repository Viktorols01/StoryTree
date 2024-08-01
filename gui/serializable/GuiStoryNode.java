package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryNode extends GuiTextBox {
    private List<StoryKey> addedKeys;
    private List<StoryKey> removedKeys;

    public GuiStoryNode(String text, int x, int y) {
        super(text, x, y);
        this.addedKeys = new ArrayList<StoryKey>();
        this.removedKeys = new ArrayList<StoryKey>();
    }

    public List<StoryKey> getAddedKeys() {
        return addedKeys;
    }

    public List<StoryKey> getRemovedKeys() {
        return removedKeys;
    }

    public void setAddedKeys(List<StoryKey> addedKeys) {
        this.addedKeys = addedKeys;
    }

    public void setRemovedKeys(List<StoryKey> removedKeys) {
        this.removedKeys = removedKeys;
    }

    @Override
    public void connectOutput(GuiConnectableBox bindable) {
        if (bindable instanceof GuiStoryOption) {
            System.out.println("connected storynode output to option");
            this.outputs.add(bindable);
        }
    }

    @Override
    public void connectInput(GuiConnectableBox bindable) {
        this.inputs.add(bindable);
    }

    @Override
    public void disconnectOutput(GuiConnectableBox bindable) {
        this.outputs.remove(bindable);
    }

    @Override
    public void disconnectInput(GuiConnectableBox bindable) {
        this.inputs.remove(bindable);
    }

}