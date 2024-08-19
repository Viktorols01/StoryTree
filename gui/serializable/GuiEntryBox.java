package gui.serializable;

import java.util.Collection;
import java.util.Collections;

public class GuiEntryBox extends GuiBox implements ConnectableOutput<GuiBox> {

    private GuiBox output;

    public GuiEntryBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    public void connectOutput(GuiBox connectable) {
        output = connectable;
    }

    @Override
    public void disconnectOutput(GuiBox connectable) {
        output = null;
    }

    @Override
    public void disconnectOutputs() {
        output = null;
    }

    @Override
    public Collection<GuiBox> getOutputs() {
        return Collections.singleton(output);
    }

    

}
