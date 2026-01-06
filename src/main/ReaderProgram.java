package src.main;

import src.reader.serializable.StoryTree;
import src.reader.swing.FrameStoryReader;
import src.reader.swing.StoryReader;
import src.tools.FileHandler;

class ReaderProgram {
    public static void main(String[] args) {
        StoryTree tree = FileHandler.loadObject("files/stories", "StoryTree files", "st");

        if (tree != null) {
            StoryReader reader = new FrameStoryReader(tree);
            reader.read();
        }
    }
}