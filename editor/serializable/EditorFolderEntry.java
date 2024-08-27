package editor.serializable;

import java.util.List;

import editor.EditorConstants;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;

public class EditorFolderEntry extends Box implements OutputInteractible  {

    private EditorFolder parentFolder;
    private InputInteractible output;

    public EditorFolderEntry(int x, int y, EditorFolder parentFolder) {
        super(x, y, EditorConstants.STANDARD_SIZE, EditorConstants.STANDARD_SIZE);
        this.parentFolder = parentFolder;
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

    public EditorFolder getParentFolder() {
        return parentFolder;
    }

    @Override
    public List<InputInteractible> getOutputs() {
        return output == null ? List.of() : List.of(output);
    }

}
