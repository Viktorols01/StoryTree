package extra;

import reader.serializable.StoryExtraNode;
import reader.serializable.StoryKey;
import reader.serializable.StoryNode;
import reader.serializable.StoryOption;
import reader.serializable.StoryTree;
import reader.swing.ConsoleStoryReader;
import tools.FileHandler;

class SerializerTest {
    public static void main(String[] args) {
        StoryNode root; // 0
        StoryNode death = new StoryNode("You died!", new StoryOption[0]); // 1
        StoryNode life = new StoryNode("You lived!", new StoryOption[0]); // 2
        StoryNode broke = new StoryNode("YOU'RE BROKE! YOU'RE FUCKING POOR!", new StoryOption[0], new StoryKey[0], new StoryKey[] {new StoryKey("pengar", 50)}); // 3

        StoryOption left = new StoryOption("Go to the left", 1);
        StoryOption right = new StoryOption("Go to the right", 2);
        StoryOption porsche = new StoryOption("Buy a porsche", 3, new StoryKey[]{new StoryKey("pengar", 50)}, new StoryKey[0], false);

        root = new StoryNode("Welcome to the dungeon!", new StoryOption[]{left, right, porsche}, new StoryKey[]{new StoryKey("pengar", 100)}, new StoryKey[]{});

        StoryExtraNode extraNode = new StoryExtraNode("Additional text", new StoryKey[0], new StoryKey[0]);
        extraNode.setExtraNode(new StoryExtraNode("Ännu mer text", new StoryKey[] {new StoryKey("omnipotence", 1)}, new StoryKey[0]));
        root.setExtraNode(extraNode);

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