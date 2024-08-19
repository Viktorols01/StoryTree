package gui;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gui.serializable.ConnectableInput;
import gui.serializable.GuiBox;
import gui.serializable.GuiEntryBox;
import gui.serializable.GuiExitBox;
import gui.serializable.GuiStoryFolder;
import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import gui.serializable.GuiTextBox;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;
import tools.Camera;
import tools.Gui.Input;

public class GuiMechanics {

    private Camera camera;
    private Input input;
    private FontMetrics fontMetrics;

    private GuiBox draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private GuiBox connectBox = null;
    private boolean connecting = false;

    private GuiStoryFolder guiFolder;

    public GuiMechanics(Input input, int width, int height, FontMetrics fontMetrics) {
        this.camera = new Camera(width, height);
        this.input = input;
        this.fontMetrics = fontMetrics;
        this.guiFolder = new GuiStoryFolder(200);
    }

    public GuiStoryFolder getGuiFolder() {
        return guiFolder;
    }

    public void setGuiFolder(GuiStoryFolder guiFolder) {
        this.guiFolder = guiFolder;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(GuiStyle.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
        camera.transform(g2d);

        if (connecting) {
            Point2D absPos = getAbsoluteMousePosition();
            GuiStyle.renderLine(g2d,
                    (int) (connectBox.getX() + connectBox.getW() / 2),
                    (int) (connectBox.getY() + connectBox.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        GuiStyle.renderOutputs(g2d, guiFolder.getEntryBox());

        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderOutputs(g2d, node);
        }
        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderOptionPairs(g2d, node);
        }
        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderStoryNode(g2d, node, false);
        }

        GuiStyle.renderEntryBox(g2d, guiFolder.getEntryBox());
        if (guiFolder.getExitBox() != null) {
            GuiStyle.renderExitBox(g2d, guiFolder.getExitBox());
        }
    }

    public void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedBox = node;
                    return;
                }
            }

            GuiExitBox exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - exitBox.getX(), absPos.getY() - exitBox.getY());
                    draggedBox = exitBox;
                    return;
                }
            }

            dragging = true;
            draggedDelta = new Point2D.Double(
                    -relPos.getX() / camera.getZoom() - camera.getX(),
                    -relPos.getY() / camera.getZoom() - camera.getY());
            draggedBox = null;
        }
    }

    public void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (dragging) {
            if (draggedBox == null) {
                camera
                        .setX(-relPos.getX() / camera.getZoom() - draggedDelta.getX());
                camera
                        .setY(-relPos.getY() / camera.getZoom() - draggedDelta.getY());
            } else {
                draggedBox.setPosition((int) (absPos.getX() - draggedDelta.getX()),
                        (int) (absPos.getY() - draggedDelta.getY()));
                if (draggedBox instanceof GuiStoryNode) {
                    GuiStyle.updateOptionPositions(fontMetrics,
                            (GuiStoryNode) draggedBox);
                }
            }
        }
    }

    public void endDragging() {
        if (dragging) {
            dragging = false;
            draggedBox = null;
        }
    }

    public void startConnecting() {
        Point2D absPos = getAbsoluteMousePosition();
        if (!connecting) {
            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connecting = true;
                    connectBox = node;
                    return;
                }
                for (GuiConnectableBox output : node.getOutputs()) {
                    GuiStoryOption option = (GuiStoryOption) output;
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        GuiStyle.updateSize(fontMetrics, option);
                        GuiStyle.update(fontMetrics, node);
                        return;
                    }
                }
            }

            GuiEntryBox entryBox = guiFolder.getEntryBox();
            if (entryBox.isInside(absPos.getX(), absPos.getY())) {
                connecting = true;
                connectBox = entryBox;
                return;
            }
        }
    }

    public void connecting() {
    }

    public void endConnecting() {
        Point2D absPos = getAbsoluteMousePosition();

        if (connecting) {
            connecting = false;

            if (connectBox.isInside(absPos.getX(), absPos.getY())) {
                if (connectBox instanceof GuiStoryNode) {
                    GuiStoryNode bindNode = (GuiStoryNode) connectBox;
                    UserInputGetter.modifyNode(bindNode);
                    GuiStyle.update(fontMetrics, bindNode);
                    connectBox = null;
                    return;
                }
            }

            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    if (connectBox instanceof GuiEntryBox) {
                        connectBox.connectOutput(node);
                        node.connectInput(connectBox);
                        connectBox = null;
                        return;
                    }

                    String input = UserInputGetter.getTextFromPromt("Add option", "");
                    if (input != null) {
                        if (connectBox instanceof GuiStoryNode) {
                            GuiStoryNode bindNode = (GuiStoryNode) connectBox;
                            addStoryOption(input, bindNode, node);
                            connectBox = null;
                            return;
                        }
                    } else {
                        connectBox = null;
                        return;
                    }
                }
            }

            GuiExitBox exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectBox.connectOutput(exitBox);
                    exitBox.connectInput(connectBox);
                    return;
                }
            }

            if (connectBox instanceof GuiStoryNode) {
                GuiStoryNode bindNode = (GuiStoryNode) connectBox;
                String optionInput = UserInputGetter.getTextFromPromt("Adding option...", "");
                if (optionInput != null) {
                    String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
                    if (nodeInput != null) {
                        addStoryOption(optionInput, bindNode, addStoryNode(nodeInput));
                        connectBox = null;
                        return;
                    }
                }
            }
            connectBox = null;
            return;
        }
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public GuiStoryOption addStoryOption(String optionText, GuiStoryNode parent, GuiStoryNode child) {
        GuiStoryOption option = new GuiStoryOption(optionText);
        GuiStyle.updateSize(fontMetrics, option);
        parent.connectOutput(option);
        option.connectInput(parent);
        option.connectOutput(child);
        child.connectInput(option);
        GuiStyle.updateOptionPositions(fontMetrics, parent);
        return option;
    }

    public GuiStoryNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        GuiStoryNode node = new GuiStoryNode(text, (int) absPos.getX(), (int) absPos.getY());
        GuiStyle.updateSize(fontMetrics, node);

        guiFolder.getNodes().add(node);
        return node;
    }

    public GuiTextBox deleteTextBox() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : guiFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return node;
            }
            for (GuiConnectableBox output : node.getOutputs()) {
                GuiStoryOption option = (GuiStoryOption) output;
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    deleteStoryOption(option);
                    return option;
                }
            }
        }
        return null;
    }

    private GuiStoryNode deleteStoryNode(GuiStoryNode node) {
        for (int i = node.getInputs().size() - 1; i >= 0; i--) {
            GuiConnectableBox input = node.getInputs().get(i);
            if (input instanceof GuiStoryOption) {
                GuiStoryOption option = (GuiStoryOption) input;
                deleteStoryOption(option);
            }
        }
        for (int i = node.getOutputs().size() - 1; i >= 0; i--) {
            GuiConnectableBox output = node.getInputs().get(i);
            GuiStoryOption option = (GuiStoryOption) output;
            deleteStoryOption(option);
        }
        guiFolder.getNodes().remove(node);
        return node;
    }

    private GuiStoryOption deleteStoryOption(GuiStoryOption option) {
        option.getInputs().get(0).disconnectOutput(option);
        option.getOutputs().get(0).disconnectInput(option);
        return option;
    }

    public Point2D getRelativeMousePosition() {
        Point2D relPos = input.getMousePosition(0).getLocation();
        return relPos;
    }

    public Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = camera.inverseTransform(relPos);
        return absPos;
    }

    public StoryTree toStoryTree() {
        NodePairList addedNodes = new NodePairList();

        appendStoryNodesToList(addedNodes);

        connectStoryNodes(addedNodes);

        StoryNode[] storyArray = new StoryNode[addedNodes.serializedNodes.size()];
        addedNodes.serializedNodes.toArray(storyArray);
        return new StoryTree(storyArray);
    }

    private void appendStoryNodesToList(NodePairList list) {
        appendStoryNodesToList(guiFolder.getEntryBox(), list);
    }

    private void appendStoryNodesToList(GuiConnectableBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            list.addAndSerializeGuiNode(guiNode);
        }

        for (GuiConnectableBox subBox : box.getOutputs()) {
            appendStoryNodesToList(subBox, list);
        }
    }

    private void connectStoryNodes(NodePairList list) {
        connectStoryNodes(guiFolder.getEntryBox(), list);
    }

    private void connectStoryNodes(GuiConnectableBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            int index = list.indexOf(guiNode);
            StoryNode serializedNode = list.get(index);
            int optionIndex = 0;
            for (GuiConnectableBox subBox : guiNode.getOutputs()) {
                GuiStoryOption option = (GuiStoryOption) subBox;
                String optionText = option.getText();
                StoryKey[] unlockingKeys = new StoryKey[option.getUnlockingKeys().size()];
                option.getUnlockingKeys().toArray(unlockingKeys);
                StoryKey[] lockingKeys = new StoryKey[option.getLockingKeys().size()];
                option.getLockingKeys().toArray(lockingKeys);
                boolean forced = option.isForced();
                int nextIndex = nextStoryNodeIndex(subBox.getOutputs().get(0), list);
                serializedNode.getStoryOptions()[optionIndex] = new StoryOption(optionText,
                        nextIndex, unlockingKeys,
                        lockingKeys, forced);
                optionIndex++;
            }
        }

        for (GuiConnectableBox subBox : box.getOutputs()) {
            connectStoryNodes(subBox, list);
        }
    }

    private int nextStoryNodeIndex(GuiConnectableBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            return list.indexOf(guiNode);
        }

        return nextStoryNodeIndex(box.getOutputs().get(0), list);
    }

    class NodePairList {
        public List<GuiStoryNode> guiNodes;
        public List<StoryNode> serializedNodes;

        public NodePairList() {
            this.guiNodes = new ArrayList<GuiStoryNode>();
            this.serializedNodes = new ArrayList<StoryNode>();
        }

        public void addAndSerializeGuiNode(GuiStoryNode guiNode) {
            guiNodes.add(guiNode);

            StoryKey[] addedKeys = new StoryKey[guiNode.getAddedKeys().size()];
            guiNode.getAddedKeys().toArray(addedKeys);
            StoryKey[] removedKeys = new StoryKey[guiNode.getRemovedKeys().size()];
            guiNode.getRemovedKeys().toArray(removedKeys);
            StoryNode serializedNode = new StoryNode(guiNode.getText(), new StoryOption[guiNode.getOutputs().size()],
                    addedKeys,
                    removedKeys);
            serializedNodes.add(serializedNode);
        }

        public int indexOf(GuiStoryNode guiNode) {
            return guiNodes.indexOf(guiNode);
        }

        public StoryNode get(int index) {
            return this.serializedNodes.get(index);
        }
    }

}
