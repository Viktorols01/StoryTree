package editor.serializable;

import java.util.List;

import editor.EditorConstants;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;

public class EditorFolderEntry extends Box implements OutputInteractible  {

    private InputInteractible output;

    public EditorFolderEntry(int x, int y) {
        super(x, y, EditorConstants.STANDARD_SIZE, EditorConstants.STANDARD_SIZE);
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

    @Override
    public List<InputInteractible> getOutputs() {
        return List.of(output);
    }

}
