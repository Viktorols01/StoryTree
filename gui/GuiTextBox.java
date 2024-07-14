package gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class GuiTextBox extends GuiBox {
    private String text;

    private Color color;
    private double lineHeight;
    private double padding;

    public GuiTextBox(final Color color, final double x, final double y, final double padding) {
        super(x, y, 0, 0);

        this.color = color;
        this.padding = padding;
    }

    public String getText() {
        return text;
    }

    public void setText(final Graphics2D g2d, final String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public double getPadding() {
        return padding;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(final double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public void setPadding(final double padding) {
        this.padding = padding;
    }    
}
