package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GuiStoryNode extends GuiTextBox {
    private List<GuiStoryOption> inPointers;
    private List<GuiStoryOption> outPointers;

    public GuiStoryNode(double x, double y) {
        super(null, x, y, 10);
        this.inPointers = new ArrayList<GuiStoryOption>();
        this.outPointers = new ArrayList<GuiStoryOption>(); 
    }

    public void addPointer(Graphics2D g2d, String optionText, GuiStoryNode node) {
        if (node == null) {
            return;
        }

        GuiStoryOption newPointer = new GuiStoryOption(optionText, this, node);
        this.outPointers.add(newPointer);
        node.inPointers.add(newPointer);
    }

    public void removePointer(GuiStoryOption pointer) {
        this.inPointers.remove(pointer);
        this.outPointers.remove(pointer);
        pointer.getChild().inPointers.remove(pointer);
        pointer.getChild().inPointers.remove(pointer);
    }

    public List<GuiStoryOption> getInPointers() {
        return this.inPointers;
    }

    public List<GuiStoryOption> getOutPointers() {
        return this.outPointers;
    }
}