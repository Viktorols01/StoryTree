package gui.serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiStoryFolder extends GuiBox implements ConnectableInput<GuiBox>, ConnectableOutput<GuiBox> {

    private GuiStoryFolder parentFolder;
    private List<GuiStoryFolder> childrenFolders;

    private GuiEntryBox entryBox;
    private GuiExitBox exitBox;

    private List<GuiBox> inputs;
    private GuiBox output;
    private List<GuiStoryNode> nodes;

    public GuiStoryFolder(int size) {
        super(0, 0, size, size);

        this.parentFolder = null;
        this.childrenFolders = new ArrayList<GuiStoryFolder>();

        this.entryBox = new GuiEntryBox(0, 0, size / 2, size / 2);
        this.exitBox = null;

        this.nodes = new ArrayList<GuiStoryNode>();
    }

    public GuiStoryFolder(GuiStoryFolder parent, int size) {
        super(0, 0, size, size);

        this.parentFolder = parent;
        this.childrenFolders = new ArrayList<GuiStoryFolder>();

        this.entryBox = new GuiEntryBox(0, 0, size / 2, size / 2);
        this.exitBox = new GuiExitBox(0, size, size / 2, size / 2);

        this.nodes = new ArrayList<GuiStoryNode>();
    }

    public List<GuiStoryNode> getNodes() {
        return nodes;
    }

    public GuiStoryFolder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(GuiStoryFolder parent) {
        this.parentFolder = parent;
    }

    public List<GuiStoryFolder> getChildrenFolders() {
        return childrenFolders;
    }

    public void setChildrenFolders(List<GuiStoryFolder> children) {
        this.childrenFolders = children;
    }

    public GuiEntryBox getEntryBox() {
        return entryBox;
    }

    public void setEntryBox(GuiEntryBox entry) {
        this.entryBox = entry;
    }

    public GuiExitBox getExitBox() {
        return exitBox;
    }

    public void setExitBox(GuiExitBox exit) {
        this.exitBox = exit;
    }

    public void setNodes(List<GuiStoryNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void connectOutput(GuiBox connectable) {
        this.output = connectable;
    }

    @Override
    public void disconnectOutput(GuiBox connectable) {
        this.output = null;
    }

    @Override
    public void disconnectOutputs() {
        this.output = null;
    }

    @Override
    public Iterable<GuiBox> getOutputs() {
        return Collections.singleton(output);
    }

    @Override
    public void connectInput(GuiBox connectable) {
        inputs.add(connectable);
    }

    @Override
    public void disconnectInput(GuiBox connectable) {
        inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        inputs.clear();
    }

    @Override
    public Iterable<GuiBox> getInputs() {
        return inputs;
    }

}
