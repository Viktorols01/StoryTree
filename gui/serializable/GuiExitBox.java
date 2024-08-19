package gui.serializable;

import java.util.ArrayList;
import java.util.List;

public class GuiExitBox extends GuiBox implements InputInteractible {

    private List<OutputInteractible> inputs;

    public GuiExitBox(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.inputs = new ArrayList<OutputInteractible>();
    }

    @Override
    public void connectInput(OutputInteractible connectable) {
        inputs.add(connectable);
    }

    @Override
    public void disconnectInput(OutputInteractible connectable) {
        inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        inputs.clear();
    }

    public List<OutputInteractible> getInputs() {
        return inputs;
    }
}
