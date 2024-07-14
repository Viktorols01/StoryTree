package main;

import storyclasses.readers.ConsoleStoryReader;
import storyclasses.serializable.StoryNode;
import tools.FileHandler;

class ReaderProgram {
    public static void main(String[] args) {
        StoryNode root = FileHandler.loadObject("files/stories", "Stories", ".story");

        if (root != null) {
            ConsoleStoryReader reader = new ConsoleStoryReader(root);
            reader.read();
        }
    }
}