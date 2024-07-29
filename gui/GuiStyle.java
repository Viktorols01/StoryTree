package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Iterator;

import gui.serializable.GuiBox;
import gui.serializable.GuiEntryBox;
import gui.serializable.GuiExitBox;
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
    public static final Color COLOR_OPTION_FORCED = new Color(255, 105, 105);

    public static final Color COLOR_BLACK = new Color(0, 0, 0);
    public static final Color COLOR_WHITE = new Color(255, 255, 255);
    public static final Color COLOR_GREEN = new Color(0, 255, 0);
    public static final Color COLOR_RED = new Color(255, 0, 0);

    public static final Color COLOR_KEY = new Color(205, 205, 0);

    public static final int BOX_PADDING = 25;

    public static Color getNodeColor(GuiStoryNode node) {
        int inCount = node.getInOptions().size();
        int outCount = node.getOutOptions().size();
        Color color;
        if (inCount == 0) {
            if (outCount == 0) {
                color = COLOR_UNUSED_NODE;
            } else {
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

    public static void update(FontMetrics fontMetrics, GuiStoryNode node) {
        updateSize(fontMetrics, node);
        updateOptionPositions(fontMetrics, node);
    }

    public static void updateSize(FontMetrics fontMetrics, GuiTextBox box) {
        box.setLineHeight(fontMetrics.getHeight());

        Iterator<String> iterable = box.getText().lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        box.setTextWidth(width);
        int height = (int) (box.getLineHeight() * box.getText().lines().count());
        box.setSize(width + 2 * BOX_PADDING, height + 2 * BOX_PADDING);
    }

    public static void updateOptionPositions(FontMetrics fontMetrics, GuiStoryNode node) {
        final int margin = 10;
        int totalWidth = 0;

        for (GuiStoryOption option : node.getOutOptions()) {
            totalWidth += option.getTextWidth() + BOX_PADDING * 2 + margin;
        }
        totalWidth -= margin;

        int x = node.getX() + node.getW() / 2 - totalWidth / 2;
        int y = node.getY() + node.getH() + margin;
        for (GuiStoryOption option : node.getOutOptions()) {
            int width = option.getTextWidth();
            option.setPosition(x, y);
            x += width + BOX_PADDING * 2 + margin;
        }
    }

    public static void renderLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void renderEntryBox(Graphics2D g2d, GuiEntryBox box) {
        renderBoxOutline(g2d, box, COLOR_WHITE);
        g2d.setColor(COLOR_GREEN);
        renderArrow(g2d, box.getX(), box.getY(), box.getW(), false);
    }

    public static void renderExitBox(Graphics2D g2d, GuiExitBox box) {
        renderBoxOutline(g2d, box, COLOR_WHITE);
        g2d.setColor(COLOR_RED);
        renderArrow(g2d, box.getX(), box.getY(), box.getW(), true);
    }

    public static void renderTextBox(Graphics2D g2d, GuiTextBox box, Color textColor) {
        g2d.fillRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                15, 15);

        g2d.setColor(textColor);
        Iterator<String> iterable = box.getText().lines().iterator();
        int i = 0;
        while (iterable.hasNext()) {
            g2d.drawString(iterable.next(), (int) (box.getX() + BOX_PADDING),
                    (int) (box.getY() + BOX_PADDING / 2 + (i + 1) * box.getLineHeight()));
            i++;
        }
    }

    public static void renderBoxOutline(Graphics2D g2d, GuiBox box, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                BOX_PADDING,
                BOX_PADDING);
    }

    public static void renderStoryNode(Graphics2D g2d, GuiStoryNode node, boolean isRoot) {
        g2d.setColor(getNodeColor(node));
        renderTextBox(g2d, node, COLOR_WHITE);
        if (isRoot) {
            renderBoxOutline(g2d, node, new Color(255, 255, 255));
            g2d.drawString("Root", (int) node.getX(), (int) node.getY() - 2);
        }
        if (!node.getAddedKeys().isEmpty()) {
            renderPlus(g2d, node.getX() + node.getW(), node.getY(), node.getLineHeight() / 2);
            renderKey(g2d, node.getX() + node.getW() + node.getLineHeight() / 2, node.getY(), node.getLineHeight());
        }
        if (!node.getRemovedKeys().isEmpty()) {
            renderMinus(g2d, node.getX() + node.getW(), node.getY() + node.getLineHeight(), node.getLineHeight() / 2);
            renderKey(g2d, node.getX() + node.getW() + node.getLineHeight() / 2, node.getY() + node.getLineHeight(),
                    node.getLineHeight());
        }
    }

    public static void renderOption(Graphics2D g2d, GuiStoryOption option) {
        g2d.setColor(COLOR_BACKGROUND);
        renderTextBox(g2d, option, COLOR_OPTION);
        renderBoxOutline(g2d, option, option.isForced() ? COLOR_OPTION_FORCED : COLOR_OPTION);

        if (!option.getUnlockingKeys().isEmpty() || !option.getLockingKeys().isEmpty()) {
            int lockSize = option.getLineHeight() / 2;
            renderLock(g2d, option.getX() + option.getW() / 2 - lockSize / 2,
                    option.getY() + option.getH() - lockSize / 2,
                    lockSize, BOX_PADDING / 2, option.isForced());
        }
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

    private static void renderLock(Graphics2D g2d, int x, int y, int size, int padding, boolean forced) {
        g2d.setColor(forced ? COLOR_OPTION_FORCED : COLOR_OPTION);
        g2d.fillRoundRect(x, y, size, size, padding, padding);
        g2d.setColor(COLOR_BLACK);
        {
            int w = size / 2;
            int h = size / 2;
            g2d.fillOval(x + size / 2 - w / 2, y + 2 * size / 5 - h / 2, w, h);
        }
        {
            int w = size / 4;
            int h = 2 * size / 3;
            g2d.fillRect(x + size / 2 - w / 2, y + size / 2 - h / 2, w, h);
        }
    }

    private static void renderKey(Graphics2D g2d, int x, int y, int size) {
        int[][] points = new int[16][2];
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        points[0] = new int[] { size * 4 / 7, size * 0 / 7 };
        points[1] = new int[] { size * 7 / 7, size * 3 / 7 };
        points[2] = new int[] { size * 5 / 7, size * 5 / 7 };
        points[3] = new int[] { size * 4 / 7, size * 4 / 7 };
        points[4] = new int[] { size * 3 / 7, size * 5 / 7 };
        points[5] = new int[] { size * 1 / 2, size * 11 / 14 };
        points[6] = new int[] { size * 3 / 7, size * 6 / 7 };
        points[7] = new int[] { size * 5 / 14, size * 11 / 14 };
        points[8] = new int[] { size * 2 / 7, size * 6 / 7 };
        points[9] = new int[] { size * 5 / 14, size * 13 / 14 };
        points[10] = new int[] { size * 2 / 7, size * 7 / 7 };
        points[11] = new int[] { size * 3 / 14, size * 13 / 14 };
        points[12] = new int[] { size * 1 / 7, size * 7 / 7 };
        points[13] = new int[] { size * 0 / 7, size * 6 / 7 };
        points[14] = new int[] { size * 3 / 7, size * 3 / 7 };
        points[15] = new int[] { size * 2 / 7, size * 2 / 7 };
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            xPoints[i] = x + point[0];
            yPoints[i] = y + point[1];
        }
        Polygon keyPolygon = new Polygon(xPoints, yPoints, points.length);
        g2d.setColor(COLOR_KEY);
        g2d.fillPolygon(keyPolygon);
    }

    private static void renderPlus(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(COLOR_GREEN);
        g2d.fillRect(x + size / 3, y, size / 3, size);
        g2d.fillRect(x, y + size / 3, size, size / 3);
    }

    private static void renderMinus(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(COLOR_RED);
        g2d.fillRect(x, y + size / 3, size, size / 3);
    }

    private static void renderArrow(Graphics2D g2d, int x, int y, int size, boolean inverted) {
        int[][] points = new int[7][2];
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        points[0] = new int[] { size * 1 / 4, size * 0 / 4 };
        points[1] = new int[] { size * 3 / 4, size * 0 / 4 };
        points[2] = new int[] { size * 3 / 4, size * 2 / 4 };
        points[3] = new int[] { size * 4 / 4, size * 2 / 4 };
        points[4] = new int[] { size * 2 / 4, size * 4 / 4 };
        points[5] = new int[] { size * 0 / 4, size * 2 / 4 };
        points[6] = new int[] { size * 1 / 4, size * 2 / 4 };
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            if (inverted) {
                xPoints[i] = x + point[0];
                yPoints[i] = y + size - point[1];
            } else {
                xPoints[i] = x + point[0];
                yPoints[i] = y + point[1];
            }

        }
        Polygon keyPolygon = new Polygon(xPoints, yPoints, points.length);
        g2d.fillPolygon(keyPolygon);
    }
}
