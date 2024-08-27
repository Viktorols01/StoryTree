package editor;

import java.util.ArrayList;
import java.util.List;

import editor.serializable.EditorFolder;
import editor.serializable.EditorFolderExit;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;

public class EditorSerializer {
    public static StoryTree toStoryTree(EditorFolder folder) {
        NodePairList addedNodes = new NodePairList();

        appendStoryNodesToList(folder.getEntryBox(), addedNodes);

        connectStoryNodes(folder, addedNodes);

        StoryNode[] storyArray = new StoryNode[addedNodes.serializedNodes.size()];
        addedNodes.serializedNodes.toArray(storyArray);
        return new StoryTree(storyArray);
    }

    private static void appendStoryNodesToList(OutputInteractible box, NodePairList list) {
        if (box instanceof EditorNode) {
            EditorNode node = (EditorNode) box;
            if (list.indexOf(node) == -1) {
                list.addAndSerializeGuiNode(node);
            } else {
                return;
            }
        }

        for (InputInteractible subBox : box.getOutputs()) {

            if (subBox instanceof EditorFolder) {
                EditorFolder childFolder = (EditorFolder) subBox;
                appendStoryNodesToList(childFolder.getEntryBox(), list);
            }

            if (subBox instanceof EditorFolderExit) {
                EditorFolderExit exit = (EditorFolderExit) subBox;
                appendStoryNodesToList(exit.getParentFolder(), list);
            }

            if (subBox instanceof EditorNode) {
                appendStoryNodesToList((EditorNode) subBox, list);
            }
        }
    }

    private static void connectStoryNodes(EditorFolder folder, NodePairList list) {
        for (EditorNode node : list.guiNodes) {
            int index = list.indexOf(node);
            StoryNode serializedNode = list.get(index);
            int optionIndex = 0;
            for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                int nextIndex = nextStoryNodeIndex(pair.getOutput(), list);
                if (nextIndex == -1) {
                    continue;
                }
                EditorOption option = pair.getOption();
                String optionText = option.getText();
                StoryKey[] unlockingKeys = new StoryKey[option.getUnlockingKeys().size()];
                option.getUnlockingKeys().toArray(unlockingKeys);
                StoryKey[] lockingKeys = new StoryKey[option.getLockingKeys().size()];
                option.getLockingKeys().toArray(lockingKeys);
                boolean forced = option.isForced();
                serializedNode.getStoryOptions()[optionIndex] = new StoryOption(optionText,
                        nextIndex, unlockingKeys,
                        lockingKeys, forced);
                optionIndex++;
            }
        }
    }

    private static int nextStoryNodeIndex(InputInteractible interactible, NodePairList list) {
        if (interactible instanceof EditorNode) {
            EditorNode node = (EditorNode) interactible;
            return list.indexOf(node);
        }

        if (interactible instanceof EditorFolderExit) {
            EditorFolderExit exit = (EditorFolderExit) interactible;
            return nextStoryNodeIndex(exit.getParentFolder().getOutput(), list);
        }

        if (interactible instanceof EditorFolder) {
            EditorFolder childFolder = (EditorFolder) interactible;
            return nextStoryNodeIndex(childFolder.getEntryBox().getOutput(), list);
        }

        return -1;
    }

    private static boolean hasNextStoryNode(InputInteractible interactible) {
        if (interactible instanceof EditorNode) {
            return true;
        }

        if (interactible instanceof EditorFolderExit) {
            EditorFolderExit exit = (EditorFolderExit) interactible;
            return hasNextStoryNode(exit.getParentFolder().getOutput());
        }

        if (interactible instanceof EditorFolder) {
            EditorFolder childFolder = (EditorFolder) interactible;
            return hasNextStoryNode(childFolder.getEntryBox().getOutput());
        }

        return false;
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

            int outputSize = 0;
            for (EditorNode.OptionPair pair : guiNode.getOptionPairs()) {
                if (hasNextStoryNode(pair.getOutput())) {
                    outputSize++;
                }
            }
            StoryNode serializedNode = new StoryNode(guiNode.getText(), new StoryOption[outputSize],
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
