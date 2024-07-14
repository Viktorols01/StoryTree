package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import tools.Camera;

public class GuiStoryContainer implements Serializable {
    public static void renderLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void renderGuiTextBox(Graphics2D g2d, GuiTextBox box) {
        g2d.setColor(box.getColor());
        g2d.fillRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                (int) box.getPadding(), (int) box.getPadding());

        g2d.setColor(new Color(255, 255, 255));
        Iterator<String> iterable = box.getText().lines().iterator();
        int i = 0;
        while (iterable.hasNext()) {
            g2d.drawString(iterable.next(), (int) (box.getX() + box.getPadding()),
                    (int) (box.getY() + box.getPadding() / 2 + (i + 1) * box.getLineHeight()));
            i++;
        }
    }

    public static void renderGuiTextBoxOutline(Graphics2D g2d, GuiTextBox box, Color color) {
        g2d.setColor(color);
        g2d.drawRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                (int) box.getPadding(),
                (int) box.getPadding());
    }

    public static void renderGuiTextBoxAsRoot(Graphics2D g2d, GuiTextBox box) {
        renderGuiTextBox(g2d, box);
        renderGuiTextBoxOutline(g2d, box, new Color(255, 255, 255));
        g2d.drawString("Root", (int) box.getX(), (int) box.getY() - 2);
    }

    public static void renderOutPointerLines(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getOutPointers()) {
            GuiStoryNode child = pointer.getChild();
            GuiStoryContainer.renderLine(g2d,
                    (int) (node.getX() + node.getW() / 2),
                    (int) (node.getY() + node.getH() / 2),
                    (int) (child.getX() + child.getW() / 2),
                    (int) (child.getY() + child.getH() / 2));
        }
    }

    public static void renderOutPointers(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getOutPointers()) {
            renderGuiTextBox(g2d, pointer);
        }
    }

    private Camera cam;
    private GuiStoryNode guiRoot;
    private List<GuiStoryNode> guiNodes;

    public GuiStoryContainer(int width, int height) {
        this.cam = new Camera(width, height);
        this.guiNodes = new ArrayList<GuiStoryNode>();
    }

    public StoryNode toStoryTree() {
        StoryNode serializedRoot = null;
        List<StoryNode> serializedNodes = new ArrayList<StoryNode>();
        for (GuiStoryNode guiNode : guiNodes) {
            StoryNode serializedNode = new StoryNode(guiNode.getText(),
                    new StoryOption[guiNode.getOutPointers().size()]);
            if (guiNode == guiRoot) {
                serializedRoot = serializedNode;
            }
            serializedNodes.add(serializedNode);
        }

        for (int i = 0; i < guiNodes.size(); i++) {
            GuiStoryNode guiNode = guiNodes.get(i);
            StoryNode serializedNode = serializedNodes.get(i);

            int optionIndex = 0;
            for (GuiStoryOption pointer : guiNode.getOutPointers()) {
                String optionText = pointer.getOptionText();
                GuiStoryNode optionNode = pointer.getChild();
                int nodeIndex = guiNodes.indexOf(optionNode);
                serializedNode.getStoryOptions()[optionIndex] = new StoryOption(optionText, serializedNodes.get(nodeIndex));
                optionIndex++;
            }
        }
        return serializedRoot;
    }

    public void setGuiRoot(GuiStoryNode guiRoot) {
        this.guiRoot = guiRoot;
        loadGuiStoryNode(guiRoot);
    }

    public void setGuiNodes(List<GuiStoryNode> guiNodes) {
        this.guiNodes = guiNodes;
    }

    public Camera getCam() {
        return cam;
    }

    public GuiStoryNode getGuiRoot() {
        return guiRoot;
    }

    public List<GuiStoryNode> getGuiNodes() {
        return guiNodes;
    }

    private void loadGuiStoryNode(GuiStoryNode node) {
        this.getGuiNodes().add(node);
        for (GuiStoryOption pointer : node.getOutPointers()) {
            GuiStoryNode child = pointer.getChild();
            if (!this.getGuiNodes().contains(child)) {
                loadGuiStoryNode(child);
            }
        }
    }

    public void packGuiTextBox(Graphics2D g2d, GuiTextBox box) {
        box.setLineHeight(g2d.getFontMetrics().getHeight());

        Iterator<String> iterable = box.getText().lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = g2d.getFontMetrics().stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int height = (int) (box.getLineHeight() * box.getText().lines().count());
        box.setSize(width + 2 * box.getPadding(), height + 2 * box.getPadding());
    }

    public void updatePositions(GuiStoryNode node) {

        for (GuiStoryOption pointer : node.getInPointers()) {
            updatePosition(pointer);
        }
        for (GuiStoryOption pointer : node.getOutPointers()) {
            updatePosition(pointer);
        }
    }

    private void updatePosition(GuiStoryOption pointer) {
        double interpolateFraction = 1.0 / 4;
        GuiStoryNode parent = pointer.getParent();
        GuiStoryNode child = pointer.getChild();
        double x = interpolate(parent.getX() + parent.getW() / 2, child.getX() + child.getW() / 2,
                interpolateFraction) - pointer.getW() / 2;
        double y = interpolate(parent.getY() + parent.getH() / 2, child.getY() + child.getH() / 2,
                interpolateFraction) - pointer.getH() / 2;
        pointer.setPosition(x, y);
    }

    public void updateColor(GuiStoryNode node) {
        int inCount = node.getInPointers().size();
        int outCount = node.getOutPointers().size();
        Color color;
        if (inCount == 0) {
            if (outCount == 0) {
                color = new Color(25, 25, 25);
            } else if (outCount == 1) {
                color = new Color(155, 155, 155);
            } else {
                color = new Color(155, 155, 0);
            }
        } else {
            if (outCount == 0) {
                color = new Color(155, 0, 0);
            } else {
                color = new Color(155, 0, 155);
            }
        }
        node.setColor(color);
    }

    private static double interpolate(double x1, double x2, double t) {
        return x1 * (1 - t) + x2 * t;
    }
}
