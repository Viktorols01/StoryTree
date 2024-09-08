package editor.serializable;

import storyclasses.serializable.StoryKey;

import java.util.ArrayList;
import java.util.List;

import editor.serializable.interfaces.TextInteractible;

public class EditorExtraNode extends Box implements TextInteractible {
    private String text;
    private List<StoryKey> unlockingKeys;
    private List<StoryKey> lockingKeys;

    private EditorExtraNode extraNode;

    public EditorExtraNode(String text) {
        super(0, 0, 0, 0);
        this.text = text;
        this.unlockingKeys = new ArrayList<StoryKey>();
        this.lockingKeys = new ArrayList<StoryKey>();
    }

    public EditorExtraNode(String text, List<StoryKey> unlockingKeys,
            List<StoryKey> lockingKeys) {
        super(0, 0, 0, 0);
        this.text = text;
        this.unlockingKeys = unlockingKeys;
        this.lockingKeys = lockingKeys;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<StoryKey> getUnlockingKeys() {
        return unlockingKeys;
    }

    public void setUnlockingKeys(List<StoryKey> unlockingKeys) {
        this.unlockingKeys = unlockingKeys;
    }

    public List<StoryKey> getLockingKeys() {
        return lockingKeys;
    }

    public void setLockingKeys(List<StoryKey> lockingKeys) {
        this.lockingKeys = lockingKeys;
    }

    public EditorExtraNode getExtraNode() {
        return extraNode;
    }

    public void setExtraNode(EditorExtraNode extraNode) {
        this.extraNode = extraNode;
    }

}
