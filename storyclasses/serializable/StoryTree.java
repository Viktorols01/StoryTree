package storyclasses.serializable;

import java.io.Serializable;

public class StoryTree implements Serializable {
    StoryNode[] nodes;

    public StoryTree(StoryNode[] nodes) {
        this.nodes = nodes;
    }

    public StoryNode getRoot() {
        return nodes[0];
    }

    public StoryNode getNode(int index) {
        return nodes[index];
    }

    public StoryNode[] getNodes() {
        return nodes;
    }
}
