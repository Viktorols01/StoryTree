package main;

import java.util.Scanner;

import storyclasses.StoryReader;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryState;

public class ConsoleStoryReader extends StoryReader {

    private Scanner scanner;

    public ConsoleStoryReader(StoryNode root) {
        super(root);
    }

    public ConsoleStoryReader(StoryState storyState) {
        super(storyState);
    }

    @Override
    protected void displayInformationToUser(String text, String[] optionStrings) {
        System.out.print("\u001b[1m");
        System.out.println(text);
        System.out.print("\u001b[0m");

        for (int i = 0; i < optionStrings.length; i++) {
            String option = optionStrings[i];
            System.out.print("\u001b[3m");
            System.out.println(i + ": " + option);
            System.out.print("\u001b[0m");
        }
    }

    @Override
    protected String getOptionFromUser() {
        // ATT GÖRA: BEGRÄNSA OCH SLÄNG FEL TILLS DET ÄR VETTIGT!
        return scanner.nextLine();
    }

}
