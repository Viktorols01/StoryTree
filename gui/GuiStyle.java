package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

public class GuiStyle {
    public static final Color COLOR_BACKGROUND = new Color(55, 55, 55);
    
    public static final Color COLOR_ROOT = new Color(0, 155, 0);
    public static final Color COLOR_EXTENSION = new Color(105, 105, 105);
    public static final Color COLOR_BRANCH = new Color(155, 155, 0);
    public static final Color COLOR_END = new Color(155, 0, 0);
    public static final Color COLOR_UNUSED = new Color(25, 25, 25);

    public static final Color COLOR_OPTION = new Color(205, 205, 205);

    public static final Color COLOR_BLACK = new Color(0, 0, 0);
    public static final Color COLOR_WHITE = new Color(255, 255, 255);

    public static Color getNodeColor(GuiStoryNode node) {
        int inCount = node.getInOptions().size();
        int outCount = node.getOutOptions().size();
        Color color;
        if (inCount == 0) {
            if (outCount == 0) {
                color = COLOR_UNUSED;
            } 
            else {
                color = COLOR_ROOT;
            }
        } else {
            if (outCount == 0) {
                color = COLOR_END;
            } else if (outCount == 1) {
                color = COLOR_EXTENSION;
            } else {
                color = COLOR_BRANCH;
            }
        }
        return color;
    }

    public static void renderLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void renderTextBox(Graphics2D g2d, GuiTextBox box, Color textColor) {
        g2d.fillRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                (int) box.getPadding(), (int) box.getPadding());

        g2d.setColor(textColor);
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
        renderTextBox(g2d, node, COLOR_WHITE);
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
        for (GuiStoryOption option : node.getOutOptions()) {
            g2d.setColor(COLOR_BACKGROUND);
            renderTextBox(g2d, option, COLOR_WHITE);
            renderTextBoxOutline(g2d, option, COLOR_WHITE);
        }
    }
}
