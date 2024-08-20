package editor.serializable;

public class EditorFolderEntry extends Box implements OutputInteractible  {

    private InputInteractible output;

    public EditorFolderEntry(int x, int y, int w, int h) {
        super(x, y, w, h);
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

}
