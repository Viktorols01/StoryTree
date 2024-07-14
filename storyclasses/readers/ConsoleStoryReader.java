package storyclasses.readers;

import java.util.Scanner;

import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryState;

public class ConsoleStoryReader extends StoryReader {

    private Scanner scanner;

    public ConsoleStoryReader(StoryNode root) {
        super(root);
        this.scanner = new Scanner(System.in);
    }

    public ConsoleStoryReader(StoryState storyState) {
        super(storyState);
    }

    @Override
    protected void displayTextToUser(String text) {
        System.out.print("\u001b[1m");
        System.out.println(text);
        System.out.print("\u001b[0m");

        System.out.println("Keys: " + getStoryState().getKeys().toString());
    }

    @Override
    protected void displayOptionsToUser(String[] optionStrings) {
        for (int i = 0; i < optionStrings.length; i++) {
            String option = optionStrings[i];
            System.out.print("\u001b[3m");
            System.out.println(i + ": " + option);
            System.out.print("\u001b[0m");
        }
    }

    @Override
    protected String getOptionFromUser(String[] optionStrings) {
        int optionIndex = -1;
        if (optionStrings.length == 1) {
            System.out.print("\u001b[33m");
            System.out.print("Press enter to continue:");
            System.out.print("\u001b[m");
            scanner.nextLine();
            optionIndex = 0; 
        } else {
            do {
                System.out.print("\u001b[33m");
                    System.out.print("Input: ");
                    String input = scanner.nextLine();
                    System.out.print("\u001b[m");
                try {
                    optionIndex = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    optionIndex = -1;
                }
            } while (optionIndex < 0 || optionIndex >= optionStrings.length);
        }
        return optionStrings[optionIndex];
    }

}
