package main;

import storyclasses.readers.FrameStoryReader;
import storyclasses.readers.StoryReader;
import storyclasses.serializable.StoryTree;
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