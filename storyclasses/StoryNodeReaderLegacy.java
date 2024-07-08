package storyclasses;

import java.util.Scanner;

import storyclasses.serializable.StoryNode;

public class StoryNodeReaderLegacy {
    private Scanner scanner;

    public StoryNodeReaderLegacy() {
        this.scanner = new Scanner(System.in);
    }

    public void read(StoryNode node) {
        printText(node.getText());
        int optionCount = node.getOptions().size();
        printOptions(node, optionCount);
        int option = getOption(optionCount);

        if (option == -1) {
            return;
        } else {
            read(node.getSubNodes().get(option));
        }
    }

    private void printOptions(StoryNode node, int optionCount) {
        if (optionCount < 2) {
            return;
        }
        for (int i = 0; i < optionCount; i++) {
            printOption(i, node.getOptions().get(i));
        }
    }

    private int getOption(int optionCount) {
        if (optionCount == 0) {
            getInput("Press enter to complete story");
            return -1;
        }
        if (optionCount == 1) {
            getInput("Press enter to continue");
            return 0;
        }

        try {
            int input = Integer.parseInt(getInput("Input"));

            if (input >= 0 && input < optionCount) {
                return input;
            } else {
                printError("Input is not in valid range.");
                return getOption(optionCount);
            }
        } catch (NumberFormatException e) {
            printError("Input could not be parsed as an integer.");
            return getOption(optionCount);
        }
    }

    private String getInput(String inputMessage) {
        System.out.print("\u001b[33m");
        System.out.print(inputMessage + ": ");
        String input = scanner.nextLine();
        System.out.print("\u001b[0m");
        return input;
    }

    private void printText(String text) {
        System.out.print("\u001b[1m");
        System.out.println(text);
        System.out.print("\u001b[0m");
    }

    private void printOption(int i, String option) {
        System.out.print("\u001b[3m");
        System.out.println(i + ": " + option);
        System.out.print("\u001b[0m");
    }

    private void printError(String errorMessage) {
        System.out.print("\u001b[31m");
        System.out.println(errorMessage);
        System.out.print("\u001b[0m");
    }
}