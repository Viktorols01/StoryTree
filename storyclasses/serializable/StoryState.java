package storyclasses.serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StoryState implements Serializable {

    private StoryNode currentStoryNode;
    private Map<String, Integer> keys;

    public StoryState(StoryNode storyNode, Map<String, Integer> unlockedKeys) {
        this.currentStoryNode = storyNode;
        this.keys = unlockedKeys;
    }

    public StoryState(StoryNode storyNode) {
        this.currentStoryNode = storyNode;
        this.keys = new HashMap<String, Integer>();
    }

    public StoryNode getCurrentNode() {
        return this.currentStoryNode;
    }

    public void setCurrentStoryNode(StoryNode currentStoryNode) {
        this.currentStoryNode = currentStoryNode;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }
}
