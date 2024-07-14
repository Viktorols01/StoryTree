package gui;

import java.util.ArrayList;
import java.util.List;

public class GuiStoryNode extends GuiTextBox {
    private List<GuiStoryOption> inOptions;
    private List<GuiStoryOption> outOptions;

    public GuiStoryNode(String text, double x, double y) {
        super(text, x, y, 10);
        this.inOptions = new ArrayList<GuiStoryOption>();
        this.outOptions = new ArrayList<GuiStoryOption>(); 
    }

    public List<GuiStoryOption> getInOptions() {
        return this.inOptions;
    }

    public List<GuiStoryOption> getOutOptions() {
        return this.outOptions;
    }
}