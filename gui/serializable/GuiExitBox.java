package gui.serializable;

import java.util.ArrayList;
import java.util.List;

public class GuiExitBox extends GuiInputBox {

    private List<OutputSocket> inputs;

    public GuiExitBox(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.inputs = new ArrayList<OutputSocket>();
    }

    @Override
    public void connectInput(OutputSocket connectable) {
        inputs.add(connectable);
    }

    @Override
    public void disconnectInput(OutputSocket connectable) {
        inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        inputs.clear();
    }

    public List<OutputSocket> getInputs() {
        return inputs;
    }
}
