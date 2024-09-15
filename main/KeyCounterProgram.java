package main;

import java.util.HashSet;

import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

public class KeyCounterProgram {
    public static void main(String[] args) {
        StoryTree tree = FileHandler.loadObject("files/stories", "StoryTree files", "st");

        HashSet<String> set = new HashSet<String>();

        if (tree != null) {
            for (StoryNode node : tree.getNodes()) {
                for (StoryKey key : node.getAddedKeys()) {
                    set.add(key.getKey());
                }
                for (StoryKey key : node.getRemovedKeys()) {
                    set.add(key.getKey());
                }
            }
            
            set.forEach((s) -> {
                System.out.println(s);
            });
        }
    }
}
