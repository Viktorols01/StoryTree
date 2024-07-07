package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GuiStoryNode extends GuiTextBox {
    private List<GuiStoryOptionPointer> inPointers;
    private List<GuiStoryOptionPointer> outPointers;

    public GuiStoryNode(double x, double y) {
        super(null, x, y, 10);
        this.inPointers = new ArrayList<GuiStoryOptionPointer>();
        this.outPointers = new ArrayList<GuiStoryOptionPointer>();

        determineColor();
    }

    public void renderOutPointerLines(Graphics2D g2d) {
        for (GuiStoryOptionPointer pointer : outPointers) {
            GuiStoryNode child = pointer.getChild();
            GuiRenderFunctions.renderLine(g2d,
                    (int) (this.getX() + this.getW() / 2),
                    (int) (this.getY() + this.getH() / 2),
                    (int) (child.getX() + child.getW() / 2),
                    (int) (child.getY() + child.getH() / 2));
        }
    }

    public void renderOutPointers(Graphics2D g2d) {
        for (GuiStoryOptionPointer pointer : outPointers) {
            pointer.render(g2d);
        }
    }

    public void renderAsRoot(Graphics2D g2d) {
        super.render(g2d);
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawRoundRect((int) getX(), (int) getY(), (int) getW(), (int) getH(), (int) getPadding(),
                (int) getPadding());
        g2d.drawString("Root", (int) getX(), (int) getY() - 2);
    }

    public GuiStoryOptionPointer addPointer(GuiStoryNode node) {
        GuiStoryOptionPointer pointer = new GuiStoryOptionPointer(this, node);
        this.outPointers.add(pointer);
        node.inPointers.add(pointer);
        determineColor();
        node.determineColor();
        pointer.updateColor(outPointers.size() < 2);
        return pointer;
    }

    public void removePointer(GuiStoryOptionPointer pointer) {
        this.inPointers.remove(pointer);
        this.outPointers.remove(pointer);
        determineColor();
    }

    public List<GuiStoryOptionPointer> getInPointers() {
        return this.inPointers;
    }

    public List<GuiStoryOptionPointer> getOutPointers() {
        return this.outPointers;
    }

    public void updatePositions() {

        for (GuiStoryOptionPointer pointer : inPointers) {
            updatePosition(pointer);
        }
        for (GuiStoryOptionPointer pointer : outPointers) {
            updatePosition(pointer);
        }
    }

    private void updatePosition(GuiStoryOptionPointer pointer) {
        double interpolateFraction = 1.0 / 4;
        GuiStoryNode parent = pointer.getParent();
        GuiStoryNode child = pointer.getChild();
        double x = interpolate(parent.getX() + parent.getW() / 2, child.getX() + child.getW() / 2,
                interpolateFraction) - pointer.getW() / 2;
        double y = interpolate(parent.getY() + parent.getH() / 2, child.getY() + child.getH() / 2,
                interpolateFraction) - pointer.getH() / 2;
        pointer.setPosition(x, y);
    }

    private void determineColor() {
        int inCount = this.inPointers.size();
        int outCount = this.outPointers.size();
        Color color;
        if (inCount == 0) {
            if (outCount == 0) {
                color = new Color(25, 25, 25);
            } else {
                color = new Color(0, 155, 0);
            }
        } else {
            if (outCount == 0) {
                color = new Color(155, 0, 0);
            } else {
                color = new Color(155, 0, 155);
            }
        }
        setColor(color);
    }

    private static double interpolate(double x1, double x2, double t) {
        return x1 * (1 - t) + x2 * t;
    }
}