package main;

import storyclasses.StoryNode;
import storyclasses.StoryNodeReader;

class Reader {
    public static void main(String[] args) {
        StoryNode root;
        root = FileHandler.loadObject("files/stories", "Story files", "story");
        if (root != null) {
            StoryNodeReader reader = new StoryNodeReader();
            reader.read(root);
        }
    }
}