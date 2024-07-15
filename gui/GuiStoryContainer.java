package gui;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import tools.Camera;

public class GuiStoryContainer {
    public static void updateSize(FontMetrics fontMetrics, GuiTextBox box) {
        box.setLineHeight(fontMetrics.getHeight());

        Iterator<String> iterable = box.getText().lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int height = (int) (box.getLineHeight() * box.getText().lines().count());
        box.setSize(width + 2 * box.getPadding(), height + 2 * box.getPadding());
    }

    public static void updatePositions(GuiStoryNode node) {
        for (GuiStoryOption pointer : node.getInOptions()) {
            updatePosition(pointer);
        }
        for (GuiStoryOption pointer : node.getOutOptions()) {
            updatePosition(pointer);
        }
    }
    private static double interpolate(double x1, double x2, double t) {
        return x1 * (1 - t) + x2 * t;
    }

    private static void updatePosition(GuiStoryOption pointer) {
        double interpolateFraction = 1.0 / 4;
        GuiStoryNode parent = pointer.getParent();
        GuiStoryNode child = pointer.getChild();
        double x = interpolate(parent.getX() + parent.getW() / 2, child.getX() + child.getW() / 2,
                interpolateFraction) - pointer.getW() / 2;
        double y = interpolate(parent.getY() + parent.getH() / 2, child.getY() + child.getH() / 2,
                interpolateFraction) - pointer.getH() / 2;
        pointer.setPosition(x, y);
    }

    private Camera camera;

    private GuiStoryNode root;

    private List<GuiStoryNode> nodes;

    public GuiStoryContainer(int width, int height) {
        this.camera = new Camera(width, height);
        this.nodes = new ArrayList<GuiStoryNode>();
    }

    public StoryNode toStoryRoot() {
        StoryNode serializedRoot = null;
        List<StoryNode> serializedNodes = new ArrayList<StoryNode>();
        for (GuiStoryNode guiNode : nodes) {
            StoryNode serializedNode = new StoryNode(guiNode.getText(),
                    new StoryOption[guiNode.getOutOptions().size()]);
            if (guiNode == root) {
                serializedRoot = serializedNode;
            }
            serializedNodes.add(serializedNode);
        }

        for (int i = 0; i < nodes.size(); i++) {
            GuiStoryNode guiNode = nodes.get(i);
            StoryNode serializedNode = serializedNodes.get(i);

            int optionIndex = 0;
            for (GuiStoryOption pointer : guiNode.getOutOptions()) {
                String optionText = pointer.getOptionText();
                GuiStoryNode optionNode = pointer.getChild();
                int nodeIndex = nodes.indexOf(optionNode);
                serializedNode.getStoryOptions()[optionIndex] = new StoryOption(optionText,
                        serializedNodes.get(nodeIndex));
                optionIndex++;
            }
        }
        return serializedRoot;
    }

    public void loadRoot(GuiStoryNode root) {
        camera.setX(root.getX());
        camera.setX(root.getY());
        this.root = root;
        this.nodes = new ArrayList<GuiStoryNode>();
        this.nodes.add(root);
        loadNode(root);
    }

    private void loadNode(GuiStoryNode node) {
        for (GuiStoryOption option : node.getOutOptions()) {
            GuiStoryNode optionNode = option.getChild();
            if (!this.nodes.contains(optionNode)) {
                this.nodes.add(optionNode);
                loadNode(optionNode);
            }
        }
    }


    public Camera getCamera() {
        return camera;
    }

    public GuiStoryNode getRoot() {
        return root;
    }

    public List<GuiStoryNode> getNodes() {
        return nodes;
    }
}
