package gui.serializable;

public class GuiEntryBox extends GuiOutputBox {

    private InputSocket output;

    public GuiEntryBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void connectOutput(InputSocket connectable) {
        output = connectable;
    }

    @Override
    public void disconnectOutput(InputSocket connectable) {
        output = null;
    }

    @Override
    public void disconnectOutputs() {
        output = null;
    }

    public InputSocket getOutput() {
        return output;
    }

}
