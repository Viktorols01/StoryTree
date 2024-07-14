package gui;

import java.io.Serializable;

public class GuiBox implements Serializable {
    private double x;
    private double y;
    private double w;
    private double h;

    public GuiBox(final double x, final double y, final double w, final double h) {
        setPosition(x, y);
        setSize(w, h);
    }

    public void setPosition(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(final double w, final double h) {
        this.w = w;
        this.h = h;
    }

    public boolean isInside(final double x, final double y) {
        return x >= this.x && x <= this.x + this.w && y >= this.y && y <= this.y + this.h;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }
}
