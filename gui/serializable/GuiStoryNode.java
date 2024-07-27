package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryNode extends GuiTextBox {
    private List<GuiStoryOption> inOptions;
    private List<GuiStoryOption> outOptions;
    private List<StoryKey> addedKeys;
    private List<StoryKey> removedKeys;

    public GuiStoryNode(String text, int x, int y) {
        super(text, x, y, 25);
        this.inOptions = new ArrayList<GuiStoryOption>();
        this.outOptions = new ArrayList<GuiStoryOption>();
        this.addedKeys = new ArrayList<StoryKey>();
        this.removedKeys = new ArrayList<StoryKey>();
    }

    public List<GuiStoryOption> getInOptions() {
        return this.inOptions;
    }

    public List<GuiStoryOption> getOutOptions() {
        return this.outOptions;
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

}