package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GuiStoryOptionPointer extends GuiTextBox {
    private List<String> optionTexts;
    private GuiStoryNode parent;
    private GuiStoryNode child;

    public GuiStoryOptionPointer(GuiStoryNode parent, GuiStoryNode child) {
        super(null, 0, 0, 10);
        this.optionTexts = new ArrayList<String>();
        this.parent = parent;
        this.child = child;
    }

    public void addOptionText(Graphics2D g2d, String optionText) {
        this.optionTexts.add(optionText);

        StringBuilder sb = new StringBuilder();
        for (String text : optionTexts) {
            sb.append(text).append("\n");
        }
        setText(g2d, sb.toString());
        updateColor(true);
    }

    public List<String> getOptionTexts() {
        return this.optionTexts;
    }

    public GuiStoryNode getChild() {
        return this.child;
    }

    public GuiStoryNode getParent() {
        return this.parent;
    }

    public void remove() {
        getParent().removePointer(this);
        getChild().removePointer(this);
    }

    public void updateColor(boolean single) {
        if (single && optionTexts.size() < 2) {
            setColor(new Color(25, 25, 25));
        } else {
            setColor(new Color(155, 155, 0));
        }
    }
}