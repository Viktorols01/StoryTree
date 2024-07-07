package gui;

import java.awt.Color;
import java.awt.Graphics;

public class GuiRenderFunctions {
    public static void renderLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.setColor(new Color(255, 255, 255));
        g.drawLine(x1, y1, x2, y2);
    }
}
