package storyclasses.readers;

import java.util.Scanner;

import storyclasses.serializable.StoryState;
import storyclasses.serializable.StoryTree;

public class ConsoleStoryReader implements StoryReader {

    private StoryIterator storyIterator;

    private Scanner scanner;

    public ConsoleStoryReader(StoryTree tree) {
        this.storyIterator = new StoryIterator(tree);
        this.scanner = new Scanner(System.in);
    }

    public ConsoleStoryReader(StoryTree tree, StoryState storyState) {
        this.storyIterator = new StoryIterator(tree, storyState);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void read() {
        while (true) {
            displayTextToUser(storyIterator.getCurrentText());
            String[] options = storyIterator.getCurrentOptions();

            if (storyIterator.hasNext()) {
                displayOptionsToUser(options);
                storyIterator.selectOption(getOptionFromUser(options));
            } else {
                break;
            }
        }
    }

    private void displayTextToUser(String text) {
        System.out.print("\u001b[1m");
        System.out.println(text);
        System.out.print("\u001b[0m");
    }

    private void displayOptionsToUser(String[] optionStrings) {
        int length = optionStrings.length;
        for (int i = 0; i < length; i++) {
            String option = optionStrings[i];
            System.out.print("\u001b[3m");
            if (length == 1) {
                System.out.println(option);
            } else {
                System.out.println(i + ": " + option);
            }
            System.out.print("\u001b[0m");
        }
    }

    private String getOptionFromUser(String[] optionStrings) {
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
