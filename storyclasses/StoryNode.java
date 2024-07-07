package storyclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoryNode implements Serializable {
    private String text;
    private List<String> options;
    private List<StoryNode> subNodes;

    public StoryNode(String text) {
        this.text = text;
        this.options = new ArrayList<String>();
        this.subNodes = new ArrayList<StoryNode>();
    }

    public void AddNode(String option, StoryNode node) {
        options.add(option);
        subNodes.add(node);
    }

    public void RemoveNode(StoryNode node) {
        int index = subNodes.indexOf(node);
        if (index == -1) {
            return;
        } 
        options.remove(index);
        subNodes.remove(index);
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<StoryNode> getSubNodes() {
        return subNodes;
    }
}