package storyclasses.serializable;

import java.io.Serializable;

public final class StoryKey implements Serializable {

    private String key;
    private int value;

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

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(int value) {
        this.value = value;
    }

}