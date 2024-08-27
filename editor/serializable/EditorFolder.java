package editor.serializable;

import java.util.ArrayList;
import java.util.List;

import editor.EditorConstants;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;
import editor.serializable.interfaces.TextInteractible;

public class EditorFolder extends Box implements InputInteractible, OutputInteractible, TextInteractible {

    private String title;
    private EditorFolder parentFolder;
    private List<EditorFolder> childrenFolders;
    private EditorFolderEntry entryBox;
    private EditorFolderExit exitBox;
    private List<EditorNode> nodes;
    private List<OutputInteractible> inputs;
    private InputInteractible output;

    public EditorFolder(String title) {
        super(0, 0, EditorConstants.STANDARD_SIZE, EditorConstants.STANDARD_SIZE);
        this.title = title;
        this.parentFolder = null;
        this.childrenFolders = new ArrayList<EditorFolder>();
        this.entryBox = new EditorFolderEntry(0, 0);
        this.exitBox = null;
        this.nodes = new ArrayList<EditorNode>();
        this.inputs = new ArrayList<OutputInteractible>();
    }

    public EditorFolder(String title, int x, int y, EditorFolder parent) {
        super(x, y, EditorConstants.STANDARD_SIZE, EditorConstants.STANDARD_SIZE);
        this.title = title;
        this.parentFolder = parent;
        this.childrenFolders = new ArrayList<EditorFolder>();
        this.entryBox = new EditorFolderEntry(0, 0);
        this.exitBox = new EditorFolderExit(0, EditorConstants.STANDARD_SIZE);
        this.nodes = new ArrayList<EditorNode>();
        this.inputs = new ArrayList<OutputInteractible>();
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

    public List<OutputInteractible> getInputs() {
        return inputs;
    }

    @Override
    public List<InputInteractible> getOutputs() {
        return output == null ? List.of() : List.of(output);
    }

    @Override
    public String getText() {
        return title;
    }

    @Override
    public void setText(String text) {
        this.title = text;
    }

}
