package editor.serializable;

import java.util.ArrayList;
import java.util.List;

import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;

public class EditorFolderExit extends Box implements InputInteractible {

    private List<OutputInteractible> inputs;

    public EditorFolderExit(int x, int y, int w, int h) {
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
