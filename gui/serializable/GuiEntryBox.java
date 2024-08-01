package gui.serializable;

public class GuiEntryBox extends GuiConnectableBox {

    public GuiEntryBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void connectOutput(GuiConnectableBox bindable) {
        this.outputs.clear();
        this.outputs.add(bindable);
    }

    @Override
    public void connectInput(GuiConnectableBox bindable) {
        return;
    }

    @Override
    public void disconnectOutput(GuiConnectableBox bindable) {
        this.outputs.clear();
    }

    @Override
    public void disconnectInput(GuiConnectableBox bindable) {
        return;
    }

}
