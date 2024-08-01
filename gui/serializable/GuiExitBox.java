package gui.serializable;

public class GuiExitBox extends GuiConnectableBox {

    public GuiExitBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void connectOutput(GuiConnectableBox bindable) {
        return;
    }

    @Override
    public void connectInput(GuiConnectableBox bindable) {
        if (bindable instanceof GuiStoryOption) {

        }
        this.inputs.add(bindable);
    }

    @Override
    public void disconnectOutput(GuiConnectableBox bindable) {
        return;
    }

    @Override
    public void disconnectInput(GuiConnectableBox bindable) {
        this.inputs.remove(bindable);
    }

}
