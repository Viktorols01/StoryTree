package gui.serializable;

import java.util.ArrayList;
import java.util.List;

public class GuiExitBox extends GuiBox implements ConnectableInput<GuiBox> {

    private List<GuiBox> inputs;

    public GuiExitBox(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.inputs = new ArrayList<GuiBox>();
    }

    @Override
    public void connectInput(GuiBox connectable) {
        inputs.add(connectable);
    }

    @Override
    public void disconnectInput(GuiBox connectable) {
        inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        inputs.clear();
    }

    @Override
    public Iterable<GuiBox> getInputs() {
        return inputs;
    }

}
