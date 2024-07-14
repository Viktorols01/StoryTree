package storyclasses.serializable;

import java.io.Serializable;

public final class StoryKeys implements Serializable {

    private final String key;
    private final int value;

    public StoryKeys(final String key, final int count) {
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