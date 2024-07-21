package main;

import storyclasses.readers.ConsoleStoryReader;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

class ReaderProgram {
    public static void main(String[] args) {
        StoryTree tree = FileHandler.loadObject("files/stories", "StoryTree files", "st");

        if (tree != null) {
            ConsoleStoryReader reader = new ConsoleStoryReader(tree);
            reader.read();
        }
    }
}