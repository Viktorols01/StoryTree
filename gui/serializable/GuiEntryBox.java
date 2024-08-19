package gui.serializable;

public class GuiEntryBox extends GuiBox implements OutputInteractible  {

    private InputInteractible output;

    public GuiEntryBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void connectOutput(InputInteractible connectable) {
        output = connectable;
    }

    @Override
    public void disconnectOutput(InputInteractible connectable) {
        output = null;
    }

    @Override
    public void disconnectOutputs() {
        output = null;
    }

    public InputInteractible getOutput() {
        return output;
    }

}
