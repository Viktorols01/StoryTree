package editor.serializable;

public class Box implements Interactible {
    private int x;
    private int y;
    private int w;
    private int h;

    public Box(final int x, final int y, final int w, final int h) {
        setPosition(x, y);
        setSize(w, h);
    }

    public void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(final int w, final int h) {
        this.w = w;
        this.h = h;
    }

    public boolean isInside(final double x, final double y) {
        return x >= this.x && x <= this.x + this.w && y >= this.y && y <= this.y + this.h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
