package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import gui.serializable.GuiTextBox;

public class GuiStyle {
    public static final Color COLOR_BACKGROUND = new Color(55, 55, 55);
    
    public static final Color COLOR_ROOT_NODE = new Color(0, 155, 0);
    public static final Color COLOR_EXTENSION_NODE = new Color(105, 105, 105);
    public static final Color COLOR_BRANCH_NODE = new Color(155, 155, 0);
    public static final Color COLOR_END_NODE = new Color(155, 0, 0);
    public static final Color COLOR_UNUSED_NODE = new Color(25, 25, 25);

    public static final Color COLOR_OPTION = new Color(205, 205, 205);
    public static final Color COLOR_OPTION_FORCED = new Color(205, 105, 105);

    public static final Color COLOR_BLACK = new Color(0, 0, 0);
    public static final Color COLOR_WHITE = new Color(255, 255, 255);

    public static Color getNodeColor(GuiStoryNode node) {
        int inCount = node.getInOptions().size();
        int outCount = node.getOutOptions().size();
        Color color;
        if (inCount == 0) {
            if (outCount == 0) {
                color = COLOR_UNUSED_NODE;
            } 
            else {
                color = COLOR_ROOT_NODE;
            }
        } else {
            if (outCount == 0) {
                color = COLOR_END_NODE;
            } else if (outCount == 1) {
                color = COLOR_EXTENSION_NODE;
            } else {
                color = COLOR_BRANCH_NODE;
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

    public static void renderOption(Graphics2D g2d, GuiStoryOption option) {
        g2d.setColor(COLOR_BACKGROUND);
        renderTextBox(g2d, option, COLOR_OPTION);
        renderTextBoxOutline(g2d, option, option.isForced() ? COLOR_OPTION_FORCED: COLOR_OPTION);
    }

    public static void renderOutOptionLines(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getOutOptions()) {
            GuiStoryNode child = pointer.getChild();
            GuiStyle.renderLine(g2d,
                    (int) (pointer.getX() + pointer.getW() / 2),
                    (int) (pointer.getY() + pointer.getH() / 2),
                    (int) (child.getX() + child.getW() / 2),
                    (int) (child.getY() + child.getH() / 2));
        }
    }

    public static void renderOutoptions(Graphics2D g2d, GuiStoryNode node) {
        for (GuiStoryOption option : node.getOutOptions()) {
            renderOption(g2d, option);
        }
    }
}
