package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;

public class GuiStoryFolder extends GuiConnectableBox {

    private GuiStoryFolder parent;
    private List<GuiStoryFolder> children;

    private GuiEntryBox entryBox;
    private GuiExitBox exitBox;

    private List<GuiStoryNode> nodes;

    public GuiStoryFolder(int size) {
        super(0, 0, size, size);

        this.parent = null;
        this.children = new ArrayList<GuiStoryFolder>();

        this.entryBox = new GuiEntryBox(0, 0, size / 2, size / 2);
        this.exitBox = null;

        this.nodes = new ArrayList<GuiStoryNode>();
    }

    public GuiStoryFolder(GuiStoryFolder parent, int size) {
        super(0, 0, size, size);

        this.parent = parent;
        this.children = new ArrayList<GuiStoryFolder>();

        this.entryBox = new GuiEntryBox(0, 0, size / 2, size / 2);
        this.exitBox = new GuiExitBox(0, size, size / 2, size / 2);

        this.nodes = new ArrayList<GuiStoryNode>();
    }

    public StoryTree toStoryTree() {
        List<StoryNode> serializedNodes = new ArrayList<StoryNode>();
        StoryNode serializedRoot = serializeNode(getRoot());
        serializedNodes.add(serializedRoot);

        for (GuiStoryNode guiNode : nodes) {
            if (guiNode == getRoot()) {
                continue;
            }
            StoryNode serializedNode = serializeNode(guiNode);
            serializedNodes.add(serializedNode);
        }

        for (int i = 0; i < nodes.size(); i++) {
            GuiStoryNode guiNode = nodes.get(i);
            StoryNode serializedNode = serializedNodes.get(i);

            int optionIndex = 0;
            for (GuiStoryOption pointer : guiNode.getOutOptions()) {
                String optionText = pointer.getText();
                StoryKey[] unlockingKeys = new StoryKey[pointer.getUnlockingKeys().size()];
                pointer.getUnlockingKeys().toArray(unlockingKeys);
                StoryKey[] lockingKeys = new StoryKey[pointer.getLockingKeys().size()];
                pointer.getLockingKeys().toArray(lockingKeys);
                boolean forced = pointer.isForced();
                GuiStoryNode optionNode = pointer.getChild();
                int nodeIndex = nodes.indexOf(optionNode);
                serializedNode.getStoryOptions()[optionIndex] = new StoryOption(optionText, nodeIndex, unlockingKeys,
                        lockingKeys, forced);
                optionIndex++;
            }
        }

        StoryNode[] storyArray = new StoryNode[serializedNodes.size()];
        serializedNodes.toArray(storyArray);
        return new StoryTree(storyArray);
    }

    private StoryNode serializeNode(GuiStoryNode guiNode) {
        StoryKey[] addedKeys = new StoryKey[guiNode.getAddedKeys().size()];
        guiNode.getAddedKeys().toArray(addedKeys);
        StoryKey[] removedKeys = new StoryKey[guiNode.getRemovedKeys().size()];
        guiNode.getRemovedKeys().toArray(removedKeys);
        return new StoryNode(guiNode.getText(), new StoryOption[guiNode.getOutOptions().size()], addedKeys,
                removedKeys);
    }

    public List<GuiStoryNode> getNodes() {
        return nodes;
    }

    public GuiStoryNode getRoot() {
        return entryBox.getOutNode();
    }

    public GuiStoryFolder getParent() {
        return parent;
    }

    public void setParent(GuiStoryFolder parent) {
        this.parent = parent;
    }

    public List<GuiStoryFolder> getChildren() {
        return children;
    }

    public void setChildren(List<GuiStoryFolder> children) {
        this.children = children;
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
    public void connect(GuiConnectable bindable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attach'");
    }

    @Override
    public void disconnect(GuiConnectable bindable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'detach'");
    }

}
