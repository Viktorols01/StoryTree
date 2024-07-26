package gui;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import gui.serializable.GuiTextBox;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;
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
        box.setTextWidth(width);
        int height = (int) (box.getLineHeight() * box.getText().lines().count());
        box.setSize(width + 2 * box.getPadding(), height + 2 * box.getPadding());
    }

    public static void updateOptionPositions(FontMetrics fontMetrics, GuiStoryNode node) {
        final int margin = 2;
        int totalWidth = 0;

        for (GuiStoryOption option : node.getOutOptions()) {
            totalWidth += option.getTextWidth() + option.getPadding() * 2 + margin;
        }
        totalWidth -= margin;

        int x = node.getX() + node.getW() / 2 - totalWidth / 2;
        int y = node.getY() + node.getH() + margin;
        for (GuiStoryOption option : node.getOutOptions()) {
            int width = option.getTextWidth();
            option.setPosition(x, y);
            x += width + option.getPadding() * 2 + margin;
        }
    }

    private Camera camera;

    private List<GuiStoryNode> nodes;

    public GuiStoryContainer(int width, int height) {
        this.camera = new Camera(width, height);
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
                String optionText = pointer.getOptionText();
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

    public void loadRoot(GuiStoryNode root) {
        camera.setX(root.getX());
        camera.setX(root.getY());
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
        return nodes.get(0);
    }

    public List<GuiStoryNode> getNodes() {
        return nodes;
    }
}
