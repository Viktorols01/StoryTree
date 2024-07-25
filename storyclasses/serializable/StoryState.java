package storyclasses.serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StoryState implements Serializable {

    private StoryTree tree;
    private int storyNodeIndex;
    private Map<String, Integer> keys;

    public StoryState(StoryTree tree, int storyNodeIndex, Map<String, Integer> unlockedKeys) {
        this.tree = tree;
        this.storyNodeIndex = storyNodeIndex;
        this.keys = unlockedKeys;
    }

    public StoryState(StoryTree tree) {
        this.tree = tree;
        this.storyNodeIndex = 0;
        this.keys = new HashMap<String, Integer>();
    }

    public StoryNode getCurrentNode() {
        return this.tree.getNode(storyNodeIndex);
    }

    public void setStoryNodeIndex(int storyNodeIndex) {
        this.storyNodeIndex = storyNodeIndex;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }
}
