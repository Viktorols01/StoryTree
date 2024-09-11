package storyclasses.serializable;

import java.io.Serializable;
import java.util.HashMap;

public class StoryState implements Serializable, Cloneable {

    private final int storyNodeIndex;
    private final HashMap<String, Integer> keys;
    private final StoryState prev;

    public StoryState(int storyNodeIndex) {
        this.storyNodeIndex = storyNodeIndex;
        this.keys = new HashMap<String, Integer>();
        this.prev = null;
    }

    public StoryState(int storyNodeIndex, HashMap<String, Integer> keys, StoryState oldState) {
        this.storyNodeIndex = storyNodeIndex;
        this.keys = keys;
        this.prev = oldState;
    }

    public int getIndex() {
        return storyNodeIndex;
    }

    public HashMap<String, Integer> getKeys() {
        return keys;
    }

    public boolean hasPrevious() {
        return prev != null;
    }

    public StoryState getPrevious() {
        return prev;
    }

    @Override
    public StoryState clone() {
        HashMap<String, Integer> cloneKeys = new HashMap<String, Integer>();
        cloneKeys.putAll(keys);
        return new StoryState(storyNodeIndex, cloneKeys, prev);
    }
}
