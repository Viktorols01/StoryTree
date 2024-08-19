package gui;

import java.util.ArrayList;
import java.util.List;

import gui.serializable.GuiStoryFolder;
import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;

// implementera visitor pattern!!!!
public class GuiSerializer {
    public static StoryTree toStoryTree(GuiStoryFolder folder) {
        NodePairList addedNodes = new NodePairList();

        appendStoryNodesToList(folder, addedNodes);

        connectStoryNodes(addedNodes);

        StoryNode[] storyArray = new StoryNode[addedNodes.serializedNodes.size()];
        addedNodes.serializedNodes.toArray(storyArray);
        return new StoryTree(storyArray);
    }

    private static void appendStoryNodesToList(GuiStoryFolder folder, NodePairList list) {
        appendStoryNodesToList(folder.getEntryBox(), list);
    }

    private static void appendStoryNodesToList(GuiInputBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            list.addAndSerializeGuiNode(guiNode);
        }

        for (GuiInputBox subBox : box.getOutputs()) {
            appendStoryNodesToList(subBox, list);
        }
    }

    private static void connectStoryNodes(NodePairList list) {
        connectStoryNodes(guiFolder.getEntryBox(), list);
    }

    private static void connectStoryNodes(GuiInputBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            int index = list.indexOf(guiNode);
            StoryNode serializedNode = list.get(index);
            int optionIndex = 0;
            for (GuiInputBox subBox : guiNode.getOutputs()) {
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

        for (GuiInputBox subBox : box.getOutputs()) {
            connectStoryNodes(subBox, list);
        }
    }

    private static int nextStoryNodeIndex(GuiInputBox box, NodePairList list) {
        if (box instanceof GuiStoryNode) {
            GuiStoryNode guiNode = (GuiStoryNode) box;
            return list.indexOf(guiNode);
        }

        return nextStoryNodeIndex(box.getOutputs().get(0), list);
    }

    private static class NodePairList {
        List<GuiStoryNode> guiNodes;
        List<StoryNode> serializedNodes;

        NodePairList() {
            this.guiNodes = new ArrayList<GuiStoryNode>();
            this.serializedNodes = new ArrayList<StoryNode>();
        }

        void addAndSerializeGuiNode(GuiStoryNode guiNode) {
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

        int indexOf(GuiStoryNode guiNode) {
            return guiNodes.indexOf(guiNode);
        }

        StoryNode get(int index) {
            return this.serializedNodes.get(index);
        }
    }

}
