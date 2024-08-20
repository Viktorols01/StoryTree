package editor.serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EditorFolder extends Box implements IOInteractible {

    private EditorFolder parentFolder;
    private List<EditorFolder> childrenFolders;

    private EditorFolderEntry entryBox;
    private EditorFolderExit exitBox;

    private List<OutputInteractible> inputs;
    private InputInteractible output;
    private List<EditorNode> nodes;

    public EditorFolder(int size) {
        super(0, 0, size, size);

        this.parentFolder = null;
        this.childrenFolders = new ArrayList<EditorFolder>();

        this.entryBox = new EditorFolderEntry(0, 0, size / 2, size / 2);
        this.exitBox = null;

        this.nodes = new ArrayList<EditorNode>();
    }

    public EditorFolder(EditorFolder parent, int size) {
        super(0, 0, size, size);

        this.parentFolder = parent;
        this.childrenFolders = new ArrayList<EditorFolder>();

        this.entryBox = new EditorFolderEntry(0, 0, size / 2, size / 2);
        this.exitBox = new EditorFolderExit(0, size, size / 2, size / 2);

        this.nodes = new ArrayList<EditorNode>();
    }

    public List<EditorNode> getNodes() {
        return nodes;
    }

    public EditorFolder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(EditorFolder parent) {
        this.parentFolder = parent;
    }

    public List<EditorFolder> getChildrenFolders() {
        return childrenFolders;
    }

    public void setChildrenFolders(List<EditorFolder> children) {
        this.childrenFolders = children;
    }

    public EditorFolderEntry getEntryBox() {
        return entryBox;
    }

    public void setEntryBox(EditorFolderEntry entry) {
        this.entryBox = entry;
    }

    public EditorFolderExit getExitBox() {
        return exitBox;
    }

    public void setExitBox(EditorFolderExit exit) {
        this.exitBox = exit;
    }

    public void setNodes(List<EditorNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void connectOutput(InputInteractible connectable) {
        this.output = connectable;
    }

    @Override
    public void disconnectOutput(InputInteractible connectable) {
        this.output = null;
    }

    @Override
    public void disconnectOutputs() {
        this.output = null;
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

    public InputInteractible getOutput() {
        return output;
    }

    public Collection<OutputInteractible> getInputs() {
        return inputs;
    }

}
