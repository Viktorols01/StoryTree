package gui;

import java.util.LinkedList;
import java.util.List;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import storyclasses.serializable.StoryKey;

public class UserInputGetter {

    public static String getTextFromPromt(String title, String text) {
        return JOptionPane.showInputDialog(title, text);
    }

    public static void modifyNode(GuiStoryNode node) {
        int width = 300;
        int bigHeight = 150;
        int smallHeight = 20;
        int margin = 5;

        JDialog dialog = new JDialog((JFrame) null, "Modify node", true);

        JPanel panel = new JPanel();
        List<Component> components = new LinkedList<Component>();

        JLabel textLabel = new JLabel("Node text");
        textLabel.setPreferredSize(new Dimension(width, smallHeight - margin));
        components.add(textLabel);

        JTextArea textArea = new JTextArea(node.getText(), 4, 0);
        textArea.setPreferredSize(new Dimension(width, bigHeight));
        textArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        PlainDocument keyDoc = (PlainDocument) textArea.getDocument();
        keyDoc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                node.setText(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                node.setText(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                node.setText(textArea.getText());
            }

        });
        components.add(textArea);

        JLabel addedKeysLabel = new JLabel("Added keys");
        addedKeysLabel.setPreferredSize(new Dimension(width, smallHeight));
        components.add(addedKeysLabel);

        JScrollPane addedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(node.getAddedKeys(), width - margin * 2, smallHeight, Color.green));
        addedKeysScrollPane.setPreferredSize(new Dimension(width, bigHeight));
        addedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        addedKeysScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        addedKeysScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        components.add(addedKeysScrollPane);

        JLabel removedKeysLabel = new JLabel("Removed keys");
        removedKeysLabel.setPreferredSize(new Dimension(width, smallHeight));
        components.add(removedKeysLabel);

        JScrollPane removedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(node.getRemovedKeys(), width - margin * 2, smallHeight, Color.red));
        removedKeysScrollPane.setPreferredSize(new Dimension(width, bigHeight));
        removedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        removedKeysScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        removedKeysScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        components.add(removedKeysScrollPane);

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(width, smallHeight - margin));
        submitButton.addActionListener((a) -> {
            node.setText(textArea.getText());
            dialog.dispose();
        });
        components.add(submitButton);

        stackComponents(panel, components, margin);
        dialog.add(panel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void modifyOption(GuiStoryOption option) {

    }

    private static class KeyEditPanel extends JPanel {

        private static final int numberSize = 40;
        private static final int buttonSize = 40;

        private int lineWidth;
        private int lineHeight;

        private Color color;

        private List<StoryKey> keys;

        public KeyEditPanel(List<StoryKey> keys, int lineWidth, int lineHeight, Color color) {
            this.lineWidth = lineWidth;
            this.lineHeight = lineHeight;
            this.color = color;

            this.keys = keys;

            generate();
        }

        private void generate() {
            this.removeAll();
            this.setBackground(color);

            List<Component> components = new LinkedList<Component>();

            JPanel addPanel = new JPanel();
            addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
            addPanel.setPreferredSize(new Dimension(lineWidth, lineHeight));
            JButton addButton = new JButton("+");
            addButton.addActionListener((a) -> {
                this.keys.add(new StoryKey("key", 1));
                this.generate();
            });
            addPanel.add(addButton);
            components.add(addPanel);

            for (StoryKey key : keys) {
                JPanel keyPanel = new JPanel();
                keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.X_AXIS));
                keyPanel.setPreferredSize(new Dimension(lineWidth, lineHeight));
                JTextField keyTextField = new JTextField(key.getKey());
                keyTextField.setPreferredSize(new Dimension(lineWidth - numberSize - buttonSize, lineHeight));
                PlainDocument keyDoc = (PlainDocument) keyTextField.getDocument();
                keyDoc.addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        key.setKey(keyTextField.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        key.setKey(keyTextField.getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        key.setKey(keyTextField.getText());
                    }

                });

                keyPanel.add(keyTextField);
                JTextField valueTextField = new JTextField("" + key.getValue());
                valueTextField.setPreferredSize(new Dimension(numberSize, lineHeight));
                PlainDocument valueDoc = (PlainDocument) valueTextField.getDocument();
                valueDoc.setDocumentFilter(new MyIntFilter());
                valueDoc.addDocumentListener(new DocumentListener() {

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        key.setValue(Integer.parseInt(valueTextField.getText()));
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        key.setValue(Integer.parseInt(valueTextField.getText()));
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        key.setValue(Integer.parseInt(valueTextField.getText()));
                    }

                });
                keyPanel.add(valueTextField);

                JButton removeButton = new JButton("-");
                removeButton.setPreferredSize(new Dimension(buttonSize, lineHeight));
                removeButton.addActionListener((a) -> {
                    this.keys.remove(key);
                    this.generate();
                });
                keyPanel.add(removeButton);

                components.add(keyPanel);
            }

            stackComponents(this, components, 0);
            this.revalidate();
            this.repaint();
        }
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

    // BORROWED FROM STACKOVERFLOW
    static class MyIntFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string,
                AttributeSet attr) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString())) {
                super.insertString(fb, offset, string, attr);
            } else {
                // warn the user and don't allow the insert
            }
        }

        private boolean test(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text,
                AttributeSet attrs) throws BadLocationException {

            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                // warn the user and don't allow the insert
            }

        }

        @Override
        public void remove(FilterBypass fb, int offset, int length)
                throws BadLocationException {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.delete(offset, offset + length);

            if (test(sb.toString())) {
                super.remove(fb, offset, length);
            } else {
                // warn the user and don't allow the insert
            }

        }
    }
}