package editor.serializable;

import java.util.ArrayList;
import java.util.List;

import editor.serializable.interfaces.TextInteractible;
import storyclasses.serializable.StoryKey;

public class EditorOption extends Box implements TextInteractible {

    private String text;
    private List<StoryKey> unlockingKeys;
    private List<StoryKey> lockingKeys;
    private boolean forced;

    public EditorOption(String text) {
        super(0, 0, 0, 0);
        this.text = text;
        this.unlockingKeys = new ArrayList<StoryKey>();
        this.lockingKeys = new ArrayList<StoryKey>();
        this.forced = false;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public List<StoryKey> getUnlockingKeys() {
        return unlockingKeys;
    }

    public List<StoryKey> getLockingKeys() {
        return lockingKeys;
    }

    public void setUnlockingKeys(List<StoryKey> unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public void setLockingKeys(List<StoryKey> lockingKeys) {
        this.lockingKeys = lockingKeys;
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }
}