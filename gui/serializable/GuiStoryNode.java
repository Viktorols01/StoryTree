package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKeys;

public class GuiStoryNode extends GuiTextBox {
    private List<GuiStoryOption> inOptions;
    private List<GuiStoryOption> outOptions;
    private List<StoryKeys> addedKeys;
    private List<StoryKeys> removedKeys;

    public GuiStoryNode(String text, int x, int y) {
        super(text, x, y, 10);
        this.inOptions = new ArrayList<GuiStoryOption>();
        this.outOptions = new ArrayList<GuiStoryOption>();
        this.addedKeys = new ArrayList<StoryKeys>();
        this.removedKeys = new ArrayList<StoryKeys>();
    }

    public List<GuiStoryOption> getInOptions() {
        return this.inOptions;
    }

    public List<GuiStoryOption> getOutOptions() {
        return this.outOptions;
    }

    public List<StoryKeys> getAddedKeys() {
        return addedKeys;
    }

    public List<StoryKeys> getRemovedKeys() {
        return removedKeys;
    }
}