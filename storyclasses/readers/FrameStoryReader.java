package storyclasses.readers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import storyclasses.serializable.StoryTree;

// ADD SCROLLING

public class FrameStoryReader implements StoryReader {

    private StoryIterator storyIterator;

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JPanel questionPanel;

    public FrameStoryReader(StoryTree tree) {
        this.storyIterator = new StoryIterator(tree);
    }

    public void createVisuals() {
        int width = 600;
        int bigHeight = 400;
        int smallHeight = 200;
        int margin = 5;

        frame = new JFrame("FrameReader");

        panel = new JPanel();
        List<Component> components = new LinkedList<Component>();

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 17));
        textArea.setEditable(false);

        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setPreferredSize(new Dimension(width, bigHeight));
        textScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        textScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        components.add(textScrollPane);

        questionPanel = new JPanel();
        questionPanel.setPreferredSize(new Dimension(width, smallHeight));
        components.add(questionPanel);

        stackComponents(panel, components, margin);

        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.repaint();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void updateVisuals() {
        String text = storyIterator.getCurrentText();
        String[] options = storyIterator.getCurrentOptions();

        textArea.setText(text);

        questionPanel.removeAll();
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener((a) -> {
                storyIterator.selectOption(button.getText());
                updateVisuals();
            });
            questionPanel.add(button);
        }

        if (storyIterator.canGoBack()) {
            JButton button = new JButton("Go back");
            button.addActionListener((a) -> {
                storyIterator.goBack();
                updateVisuals();
            });
            questionPanel.add(button);

        }

        questionPanel.revalidate();
        questionPanel.repaint();
    }

    private static void stackComponents(JPanel panel, List<Component> components, int margin) {
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        int totalWidth = 0;
        int totalHeight = 0;
        for (Component component : components) {
            int width = (int) component.getPreferredSize().getWidth();
            int height = (int) component.getPreferredSize().getHeight();

            if (width > totalWidth) {
                totalWidth = width;
            }

            totalHeight += height;
        }
        panel.setPreferredSize(new Dimension(totalWidth + 2 * margin, totalHeight + (1 + components.size()) * margin));

        Component previous = null;
        for (Component component : components) {
            panel.add(component);
            layout.putConstraint(SpringLayout.WEST, component, margin, SpringLayout.WEST,
                    panel);
            layout.putConstraint(SpringLayout.NORTH, component, margin,
                    previous == null ? SpringLayout.NORTH : SpringLayout.SOUTH,
                    previous == null ? panel : previous);

            previous = component;
        }
    }

    @Override
    public void read() {
        createVisuals();
        updateVisuals();
    }

}
