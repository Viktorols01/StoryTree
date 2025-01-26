package storyclasses.readers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import storyclasses.serializable.StoryTree;
import tools.FileHandler;

// ADD SCROLLING

public class FrameStoryReader implements StoryReader {

    private StoryIterator storyIterator;

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JPanel questionPanel;
    private JPanel backtrackingPanel;

    private boolean savingEnabled;
    private boolean loadingEnabled;
    private boolean backtrackingEnabled;
    private boolean showKeys;

    public FrameStoryReader(StoryTree tree) {
        this.storyIterator = new StoryIterator(tree);
    }

    public void createVisuals() {
        int width = 600;
        int bigHeight = 400;
        int smallHeight = 150;
        int miniHeight = 50;
        int margin = 5;

        frame = new JFrame("FrameReader");
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener((_) -> {
            if (!savingEnabled) {
                JOptionPane.showMessageDialog(frame, "Saving is not allowed.");
            } else {
                FileHandler.saveObject(storyIterator.getStoryState(), "files/saves", "Game saves", "save");
            }
        });
        optionsMenu.add(saveItem);

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener((_) -> {
            if (!loadingEnabled) {
                JOptionPane.showMessageDialog(frame, "Loading is not allowed.");
            } else {
                storyIterator.loadStoryState(FileHandler.loadObject("files/saves", "Game saves", "save"));
            }
            updateVisuals();
        });
        optionsMenu.add(loadItem);

        JMenuItem cheatsItem = new JMenuItem("Cheat codes");
        cheatsItem.addActionListener((_) -> {
            String code = JOptionPane.showInputDialog("Enter cheat code:");
            if (code.equals("savescum")) {
                if (!savingEnabled) {
                    JOptionPane.showMessageDialog(frame, "Saving unlocked!");
                    savingEnabled = true;
                }
                if (!loadingEnabled) {
                    JOptionPane.showMessageDialog(frame, "Loading unlocked!");
                    loadingEnabled = true;
                }
            }
            if (code.equals("timetraveller")) {
                if (!backtrackingEnabled) {
                    JOptionPane.showMessageDialog(frame, "Backtracking unlocked!");
                    backtrackingEnabled = true;
                }
            }
            if (code.equals("omniscient")) {
                if (!showKeys) {
                    JOptionPane.showMessageDialog(frame, "Key printing enabled! Use cheat code: 'hidekeys' to disable.");
                    showKeys = true;
                }
            }
            if (code.equals("hidekeys")) {
                if (showKeys) {
                    JOptionPane.showMessageDialog(frame, "Key printing disabled.");
                    showKeys = false;
                }
            }
            updateVisuals();
        });
        optionsMenu.add(cheatsItem);

        menuBar.add(optionsMenu);
        frame.setJMenuBar(menuBar);

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

        backtrackingPanel = new JPanel();
        backtrackingPanel.setPreferredSize(new Dimension(width, miniHeight));
        components.add(backtrackingPanel);

        stackComponents(panel, components, margin);

        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.repaint();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void updateVisuals() {
        StringBuilder sb = new StringBuilder(storyIterator.getCurrentText());
        if (showKeys) {
            sb.append("\n");
            sb.append("\n");
            sb.append("Keys:");
            sb.append(mapToString(storyIterator.getStoryState().getKeys()));
        }
        String[] options = storyIterator.getCurrentOptions();

        textArea.setText(sb.toString());

        questionPanel.removeAll();
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener((_) -> {
                storyIterator.selectOption(button.getText());
                updateVisuals();
            });
            questionPanel.add(button);
        }

        backtrackingPanel.removeAll();
        if (storyIterator.canGoBack() && backtrackingEnabled) {
            JButton button = new JButton("<--");
            button.addActionListener((_) -> {
                storyIterator.goBack();
                updateVisuals();
            });
            backtrackingPanel.add(button);

        }

        backtrackingPanel.revalidate();
        backtrackingPanel.repaint();
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

    private static String mapToString(HashMap<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((s, i) -> {
            sb.append("\n" + "\t" + s + ": " + i);
        });
        return sb.toString();
    }

}
