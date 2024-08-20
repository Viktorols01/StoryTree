package editor.serializable;

import java.io.Serializable;

public interface Interactible extends Serializable {
    public void setPosition(int x, int y);
    public void setSize(int w, int h);
    public boolean isInside(double x, double y);
    public int getX();
    public int getY();
    public int getW();
    public int getH();
}
