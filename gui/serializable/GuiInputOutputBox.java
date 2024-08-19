package gui.serializable;

public abstract class GuiInputOutputBox extends GuiBox implements InputSocket, OutputSocket {
    public GuiInputOutputBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }
}
