package gui;

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
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        JLabel textLabel = new JLabel("Node text");
        textLabel.setPreferredSize(new Dimension(width, smallHeight - margin));
        panel.add(textLabel);

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
        panel.add(textArea);

        JLabel addedKeysLabel = new JLabel("Added keys");
        addedKeysLabel.setPreferredSize(new Dimension(width, smallHeight));
        panel.add(addedKeysLabel);

        JScrollPane addedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(node.getAddedKeys(), width - margin * 2, smallHeight, Color.green));
        addedKeysScrollPane.setPreferredSize(new Dimension(width, bigHeight));
        addedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        addedKeysScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        addedKeysScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(addedKeysScrollPane);

        JLabel removedKeysLabel = new JLabel("Removed keys");
        removedKeysLabel.setPreferredSize(new Dimension(width, smallHeight));
        panel.add(removedKeysLabel);

        JScrollPane removedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(node.getRemovedKeys(), width - margin * 2, smallHeight, Color.red));
        removedKeysScrollPane.setPreferredSize(new Dimension(width, bigHeight));
        removedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        removedKeysScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        removedKeysScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(removedKeysScrollPane);

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(width, smallHeight - margin));
        submitButton.addActionListener((a) -> {
            node.setText(textArea.getText());
            dialog.dispose();
        });
        panel.add(submitButton);

        layout.putConstraint(SpringLayout.WEST, textLabel,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, textLabel,
                margin,
                SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, textArea,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, textArea,
                margin,
                SpringLayout.SOUTH, textLabel);

        layout.putConstraint(SpringLayout.WEST, addedKeysLabel,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, addedKeysLabel,
                margin,
                SpringLayout.SOUTH, textArea);

        layout.putConstraint(SpringLayout.WEST, addedKeysScrollPane,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, addedKeysScrollPane,
                margin,
                SpringLayout.SOUTH, addedKeysLabel);

        layout.putConstraint(SpringLayout.WEST, removedKeysLabel,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, removedKeysLabel,
                margin,
                SpringLayout.SOUTH, addedKeysScrollPane);

        layout.putConstraint(SpringLayout.WEST, removedKeysScrollPane,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, removedKeysScrollPane,
                margin,
                SpringLayout.SOUTH, removedKeysLabel);

        layout.putConstraint(SpringLayout.WEST, submitButton,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, submitButton,
                margin,
                SpringLayout.SOUTH, removedKeysScrollPane);
        dialog.add(panel);
        panel.setPreferredSize(new Dimension(width + margin * 2, 3 * bigHeight + 4 * smallHeight + 6 * margin));
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

        private SpringLayout layout;

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

            this.layout = new SpringLayout();
            this.setLayout(layout);

            this.setPreferredSize(new Dimension(lineWidth, lineHeight * (1 + keys.size())));

            JPanel modifyPanel = new JPanel();
            modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.X_AXIS));
            modifyPanel.setPreferredSize(new Dimension(lineWidth, lineHeight));
            JButton addButton = new JButton("+");
            addButton.addActionListener((a) -> {
                this.keys.add(new StoryKey("key", 1));
                this.generate();
            });
            modifyPanel.add(addButton);
            this.add(modifyPanel);
            this.layout.putConstraint(SpringLayout.WEST, modifyPanel, 0, SpringLayout.WEST, this);
            this.layout.putConstraint(SpringLayout.NORTH, modifyPanel, 0, SpringLayout.NORTH, this);

            Component previous = modifyPanel;
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

                this.add(keyPanel);
                this.layout.putConstraint(SpringLayout.WEST, keyPanel, 0, SpringLayout.WEST,
                        this);
                this.layout.putConstraint(SpringLayout.NORTH, keyPanel, 0,
                        previous == null ? SpringLayout.NORTH : SpringLayout.SOUTH,
                        previous == null ? this : previous);

                previous = keyPanel;
            }
            this.revalidate();
            this.repaint();
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