package editor;

import java.util.ArrayList;
import java.util.List;

import editor.serializable.EditorFolder;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;

// implementera visitor pattern!!!!
public class EditorSerializer {
    public static StoryTree toStoryTree(EditorFolder folder) {
        NodePairList addedNodes = new NodePairList();

        appendStoryNodesToList(folder, addedNodes);

        connectStoryNodes(addedNodes);

        StoryNode[] storyArray = new StoryNode[addedNodes.serializedNodes.size()];
        addedNodes.serializedNodes.toArray(storyArray);
        return new StoryTree(storyArray);
    }

    private static void appendStoryNodesToList(EditorFolder folder, NodePairList list) {
        appendStoryNodesToList(folder.getEntryBox(), list);
    }

    private static void appendStoryNodesToList(InputInteractible box, NodePairList list) {
        if (box instanceof EditorNode) {
            EditorNode guiNode = (EditorNode) box;
            list.addAndSerializeGuiNode(guiNode);
        }

        for (InputInteractible subBox : box.getOutputs()) {
            appendStoryNodesToList(subBox, list);
        }
    }

    private static void connectStoryNodes(NodePairList list) {
        connectStoryNodes(guiFolder.getEntryBox(), list);
    }

    private static void connectStoryNodes(InputInteractible box, NodePairList list) {
        if (box instanceof EditorNode) {
            EditorNode guiNode = (EditorNode) box;
            int index = list.indexOf(guiNode);
            StoryNode serializedNode = list.get(index);
            int optionIndex = 0;
            for (InputInteractible subBox : guiNode.getOutputs()) {
                EditorOption option = (EditorOption) subBox;
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

        for (InputInteractible subBox : box.getOutputs()) {
            connectStoryNodes(subBox, list);
        }
    }

    private static int nextStoryNodeIndex(InputInteractible box, NodePairList list) {
        if (box instanceof EditorNode) {
            EditorNode guiNode = (EditorNode) box;
            return list.indexOf(guiNode);
        }

        return nextStoryNodeIndex(box.getOutputs().get(0), list);
    }

    private static class NodePairList {
        List<EditorNode> guiNodes;
        List<StoryNode> serializedNodes;

        NodePairList() {
            this.guiNodes = new ArrayList<EditorNode>();
            this.serializedNodes = new ArrayList<StoryNode>();
        }

        void addAndSerializeGuiNode(EditorNode guiNode) {
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

        int indexOf(EditorNode guiNode) {
            return guiNodes.indexOf(guiNode);
        }

        StoryNode get(int index) {
            return this.serializedNodes.get(index);
        }
    }

}
