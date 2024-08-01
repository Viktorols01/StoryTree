package gui.serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiConnectableBox extends GuiBox {

    protected List<GuiConnectableBox> inputs;
    protected List<GuiConnectableBox> outputs;

    public GuiConnectableBox(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.inputs = new ArrayList<GuiConnectableBox>();
        this.outputs = new ArrayList<GuiConnectableBox>();
    }

    public abstract void connectOutput(GuiConnectableBox bindable);

    public abstract void connectInput(GuiConnectableBox bindable);

    public abstract void disconnectOutput(GuiConnectableBox bindable);

    public abstract void disconnectInput(GuiConnectableBox bindable);

    public List<GuiConnectableBox> getInputs() {
        return inputs;
    }

    public void setInputs(List<GuiConnectableBox> inputs) {
        this.inputs = inputs;
    }

    public List<GuiConnectableBox> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<GuiConnectableBox> outputs) {
        this.outputs = outputs;
    }

}
