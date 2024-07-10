package storyclasses.serializable;

import java.util.ArrayList;
import java.util.List;

// abandon, smarter people have done this
// problem with delimiters etc
public class StorySerializer {
    public static String toString(StoryNode node) {
        List<StoryNode> nodeList = new ArrayList<StoryNode>();
        addNodeToList(node, nodeList);
        return writeList(nodeList);

    }

    private static void addNodeToList(StoryNode node, List<StoryNode> nodeList) {
        if (nodeList.contains(node)) {
            return;
        }

        nodeList.add(node);
        for (StoryOption option : node.getStoryOptions()) {
            addNodeToList(option.getStoryNode(), nodeList);
        }
    }

    private static String writeList(List<StoryNode> nodeList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodeList.size(); i++) {
            StoryNode node = nodeList.get(i);
            sb.append(i).append("\n");
            sb.append("Text: ");
            sb.append(node.getText()).append("\n");
            sb.append("Added keys: ");
            for (StoryKey key : node.getAddedKeys()) {
                sb.append(key.getKey() + ":" + key.getValue());
                sb.append(",");
            }
            sb.append("\n");
            sb.append("Removed keys: ");
            for (StoryKey key : node.getRemovedKeys()) {
                sb.append(key.getKey() + ":" + key.getValue());
                sb.append(",");
            }
            sb.append("\n");
            sb.append("Options:");
            sb.append("\n");
            for (StoryOption option : node.getStoryOptions()) {
                sb.append("\t");
                sb.append("Text: ");
                sb.append(option.getText());
                sb.append(", Node: ");
                sb.append(nodeList.indexOf(option.getStoryNode()));
                sb.append(", Unlocking keys: ");
                for (StoryKey key : option.getUnlockingKeys()) {
                    sb.append(key.getKey() + ":" + key.getValue());
                    sb.append(",");
                }
                sb.append("Locking keys: ");
                for (StoryKey key : option.getLockingKeys()) {
                    sb.append(key.getKey() + ":" + key.getValue());
                    sb.append(",");
                }
                sb.append("Forced: ");
                sb.append(option.isForced());
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
