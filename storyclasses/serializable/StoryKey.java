package storyclasses.serializable;

import java.io.Serializable;

public final class StoryKey implements Serializable {

    private final String key;
    private final int value;

    public StoryKey(String key, int count) {
        this.key = key;
        this.value = count;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

}