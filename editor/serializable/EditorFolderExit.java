package editor.serializable;

import java.util.ArrayList;
import java.util.List;

import editor.Constants;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;

public class EditorFolderExit extends Box implements InputInteractible {

    private EditorFolder parentFolder;
    private List<OutputInteractible> inputs;

    public EditorFolderExit(int x, int y, EditorFolder parentFolder) {
        super(x, y, Constants.STANDARD_SIZE, Constants.STANDARD_SIZE);
        this.parentFolder = parentFolder;
        this.inputs = new ArrayList<OutputInteractible>();
    }

    public EditorFolder getParentFolder() {
        return parentFolder;
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
