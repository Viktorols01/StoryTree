package tests;

import storyclasses.readers.ConsoleStoryReader;
import storyclasses.serializable.StoryKeys;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

class SerializerTest {
    public static void main(String[] args) {
        StoryNode root; // 0
        StoryNode death = new StoryNode("You died!", new StoryOption[0]); // 1
        StoryNode life = new StoryNode("You lived!", new StoryOption[0]); // 2
        StoryNode broke = new StoryNode("YOU'RE BROKE! YOU'RE FUCKING POOR!", new StoryOption[0], new StoryKeys[0], new StoryKeys[] {new StoryKeys("pengar", 50)}); // 3

        StoryOption left = new StoryOption("Go to the left", 1);
        StoryOption right = new StoryOption("Go to the right", 2);
        StoryOption porsche = new StoryOption("Buy a porsche", 3, new StoryKeys[]{new StoryKeys("pengar", 50)}, new StoryKeys[0], true);

        root = new StoryNode("Welcome to the dungeon!", new StoryOption[]{left, right, porsche}, new StoryKeys[]{new StoryKeys("pengar", 50)}, new StoryKeys[]{});

        StoryTree tree = new StoryTree(new StoryNode[]{root, death, life, broke});
        
        StoryTree loadedTree = roundTrip(tree);

        ConsoleStoryReader reader = new ConsoleStoryReader(loadedTree);
        reader.read();
    }

    private static StoryTree roundTrip(StoryTree node) {
        FileHandler.saveObject(node, "files/stories", "StoryTree files", "st");
        return FileHandler.loadObject("files/stories", "StoryTree files", "st");
    }
}