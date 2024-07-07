package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

public class GuiTextBox extends GuiBox {
    private String text;

    private Color color;
    private double lineHeight;
    private double padding;

    public GuiTextBox(Color color, double x, double y, double padding) {
        super(x, y, 0, 0);

        this.color = color;
        this.padding = padding;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillRoundRect((int) getX(), (int) getY(), (int) getW(), (int) getH(), (int) padding, (int) padding);

        g2d.setColor(new Color(255, 255, 255));
        Iterator<String> iterable = this.text.lines().iterator();
        int i = 0;
        while (iterable.hasNext()) {
            g2d.drawString(iterable.next(), (int) (getX() + padding),
                    (int) (getY() + padding / 2 + (i + 1) * this.lineHeight));
            i++;
        }
    }

    private void pack(Graphics2D g2d) {
        this.lineHeight = g2d.getFontMetrics().getHeight();

        Iterator<String> iterable = this.text.lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = g2d.getFontMetrics().stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int height = (int) (lineHeight * text.lines().count());
        setSize(width + 2 * padding, height + 2 * padding);
    }

    public String getText() {
        return text;
    }

    public void setText(Graphics2D g2d, String text) {
        this.text = text;
        pack(g2d);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getPadding() {
        return padding;
    }
}
