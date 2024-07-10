package tests;

import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StorySerializer;

class SerializerTest {
    public static void main(String[] args) {
        StoryNode root;
        StoryNode death = new StoryNode("You died!", new StoryOption[0]);
        StoryNode life = new StoryNode("You lived!", new StoryOption[0]);
        StoryNode broke = new StoryNode("YOU'RE BROKE! YOU'RE FUCKING POOR!", new StoryOption[0], new StoryKey[0], new StoryKey[] {new StoryKey("pengar", 50)});

        StoryOption left = new StoryOption("Go to the left", death);
        StoryOption right = new StoryOption("Go to the right", life);
        StoryOption porsche = new StoryOption("Buy a porsche", broke, new StoryKey[]{new StoryKey("pengar", 50)}, new StoryKey[0], false);
    

        root = new StoryNode("Welcome to the dungeon!", new StoryOption[]{left, right, porsche}, new StoryKey[]{new StoryKey("pengar", 50)}, new StoryKey[]{});

        System.out.println(StorySerializer.toString(root));
    }
}