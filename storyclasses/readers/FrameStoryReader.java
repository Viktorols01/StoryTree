package storyclasses.readers;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import storyclasses.serializable.StoryTree;

public class FrameStoryReader implements StoryReader {

    private StoryIterator storyIterator;

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JPanel questionPanel;

    public FrameStoryReader(StoryTree tree) {
        this.storyIterator = new StoryIterator(tree);

        this.frame = new JFrame("FrameReader");
        this.panel = new JPanel();
        this.textArea = new JTextArea();
        this.questionPanel = new JPanel();

        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(textArea);
        panel.add(questionPanel);

        frame.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void updateVisuals() {
        String text = storyIterator.getCurrentText();
        String[] options = storyIterator.getCurrentOptions();

        textArea.setText(text);
        textArea.setPreferredSize(new Dimension(400, 400));

        questionPanel.removeAll();
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener((a) -> {
                storyIterator.selectOption(button.getText());
                updateVisuals();
            });
            questionPanel.add(button);
        }
        questionPanel.setPreferredSize(new Dimension(400, 200));
        frame.pack();
        frame.repaint();
        frame.setVisible(true);
    }

    @Override
    public void read() {
        updateVisuals();
    }

}
