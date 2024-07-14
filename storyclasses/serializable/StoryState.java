package storyclasses.serializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StoryState implements Serializable {

    private StoryNode currentStoryNode;
    private final Map<String, Integer> keys;

    public StoryState(final StoryNode storyNode, final Map<String, Integer> unlockedKeys) {
        this.currentStoryNode = storyNode;
        this.keys = unlockedKeys;
    }

    public StoryState(final StoryNode storyNode) {
        this.currentStoryNode = storyNode;
        this.keys = new HashMap<String, Integer>();
    }

    public StoryNode getCurrentNode() {
        return this.currentStoryNode;
    }

    public void setCurrentStoryNode(final StoryNode currentStoryNode) {
        this.currentStoryNode = currentStoryNode;
    }

    public Map<String, Integer> getKeys() {
        return keys;
    }
}
