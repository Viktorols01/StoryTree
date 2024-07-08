package main;

import storyclasses.StoryNodeReaderLegacy;
import storyclasses.serializable.StoryNode;
import tools.FileHandler;

class ReaderProgram {
    public static void main(String[] args) {
        StoryNode root;
        root = FileHandler.loadObject("files/stories", "Story files", "story");
        if (root != null) {
            StoryNodeReaderLegacy reader = new StoryNodeReaderLegacy();
            reader.read(root);
        }
    }
}