package main;

import reader.serializable.StoryTree;
import reader.swing.FrameStoryReader;
import reader.swing.StoryReader;
import tools.FileHandler;

class ReaderProgram {
    public static void main(String[] args) {
        StoryTree tree = FileHandler.loadObject("files/stories", "StoryTree files", "st");

        if (tree != null) {
            StoryReader reader = new FrameStoryReader(tree);
            reader.read();
        }
    }
}