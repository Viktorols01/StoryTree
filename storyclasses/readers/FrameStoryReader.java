package storyclasses.readers;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import storyclasses.serializable.StoryTree;

public class FrameStoryReader extends StoryReader {

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;

    public FrameStoryReader(StoryTree tree) {
        super(tree);
        this.frame = new JFrame("FrameReader");
        this.panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 800));
        this.textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(500, 500));

        panel.add(textArea);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    protected void displayTextToUser(String text) {
        textArea.setText(text);
        frame.pack();
    }

    @Override
    protected void displayOptionsToUser(String[] optionStrings) {
    }

    @Override
    protected String getOptionFromUser(String[] optionStrings) {
        return "";
    }
    
}
