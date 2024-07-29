package gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;

public class GuiStoryFolder implements Serializable {
    // TODO: entry, exit, parent, position, folders
    // getRoot ska ge Entry
    // serialization ska behandla folders, entry och exit

    private List<GuiStoryNode> nodes;

    public GuiStoryFolder() {
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
        return nodes.get(0); // must be replaced with the node connected to entry...
    }
}
