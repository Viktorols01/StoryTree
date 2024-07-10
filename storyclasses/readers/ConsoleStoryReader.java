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
    protected String getOptionFromUser() {
        // ATT GÖRA: BEGRÄNSA OCH SLÄNG FEL TILLS DET ÄR VETTIGT!
        return scanner.nextLine();
    }

}
