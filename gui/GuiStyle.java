package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

public class GuiStyle {
    public static void renderLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void renderTextBox(Graphics2D g2d, GuiTextBox box) {
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

    public static void renderTextBoxOutline(Graphics2D g2d, GuiTextBox box, Color color) {
        g2d.setColor(color);
        g2d.drawRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                (int) box.getPadding(),
                (int) box.getPadding());
    }

    public static void renderStoryNode(Graphics2D g2d, GuiStoryNode node, boolean isRoot) {
        g2d.setColor(getNodeColor(node));
        renderTextBox(g2d, node);
        if (isRoot) {
            renderTextBoxOutline(g2d, node, new Color(255, 255, 255));
            g2d.drawString("Root", (int) node.getX(), (int) node.getY() - 2);
        }
    }

    public static void renderOutOptionLines(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getOutOptions()) {
            GuiStoryNode child = pointer.getChild();
            GuiStyle.renderLine(g2d,
                    (int) (node.getX() + node.getW() / 2),
                    (int) (node.getY() + node.getH() / 2),
                    (int) (child.getX() + child.getW() / 2),
                    (int) (child.getY() + child.getH() / 2));
        }
    }

    public static void renderOutoptions(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getOutOptions()) {
            renderTextBox(g2d, pointer);
        }
    }

    public static Color getNodeColor(GuiStoryNode node) {
        int inCount = node.getInOptions().size();
        int outCount = node.getOutOptions().size();
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
        return color;
    }
}
