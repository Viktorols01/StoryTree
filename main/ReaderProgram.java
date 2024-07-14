package main;

import storyclasses.readers.ConsoleStoryReader;
import storyclasses.serializable.StoryKeys;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;

class ReaderProgram {
    public static void main(String[] args) {
        StoryNode root;
        StoryNode death = new StoryNode("You died!", new StoryOption[0]);
        StoryNode life = new StoryNode("You lived!", new StoryOption[0]);
        StoryNode broke = new StoryNode("YOU'RE BROKE! YOU'RE FUCKING POOR!", new StoryOption[0], new StoryKeys[0], new StoryKeys[] {new StoryKeys("pengar", 50)});

        StoryOption left = new StoryOption("Go to the left", death);
        StoryOption right = new StoryOption("Go to the right", life);
        StoryOption porsche = new StoryOption("Buy a porsche", broke, new StoryKeys[]{new StoryKeys("pengar", 50)}, new StoryKeys[0], false);
    

        root = new StoryNode("Welcome to the dungeon!", new StoryOption[]{left, right, porsche}, new StoryKeys[]{new StoryKeys("pengar", 50)}, new StoryKeys[]{});

        if (root != null) {
            ConsoleStoryReader reader = new ConsoleStoryReader(root);
            reader.read();
        }
    }
}