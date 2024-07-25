package gui;

import java.util.List;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
    private static int width = 300;
    private static int height = 800;
    private static int buttonHeight = 40;
    private static int margin = 5;

    public static String getTextFromPromt(String title, String text) {
        return JOptionPane.showInputDialog(title, text);
    }

    public static void modifyNode(GuiStoryNode node) {
        JDialog dialog = new JDialog((JFrame) null, "Custom Input Dialog", true);

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        JTextArea textArea = new JTextArea(node.getText(), 4, 0);
        textArea.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        textArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(textArea);

        JScrollPane addedKeysScrollPane = new JScrollPane(new KeyEditPanel(node.getAddedKeys()));
        addedKeysScrollPane.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        addedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(addedKeysScrollPane);

        JScrollPane removedKeysScrollPane = new JScrollPane(new KeyEditPanel(node.getRemovedKeys()));
        removedKeysScrollPane.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        removedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(removedKeysScrollPane);

        Button submitButton = new Button("Submit");
        submitButton.setPreferredSize(new Dimension(width - margin * 2, buttonHeight));
        submitButton.addActionListener((a) -> {
            node.setText(textArea.getText());
            dialog.dispose();
        });
        panel.add(submitButton);

        layout.putConstraint(SpringLayout.WEST, textArea,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, textArea,
                margin,
                SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, addedKeysScrollPane,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, addedKeysScrollPane,
                margin,
                SpringLayout.SOUTH, textArea);

        layout.putConstraint(SpringLayout.WEST, removedKeysScrollPane,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, removedKeysScrollPane,
                margin,
                SpringLayout.SOUTH, addedKeysScrollPane);

        layout.putConstraint(SpringLayout.WEST, submitButton,
                margin,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, submitButton,
                margin,
                SpringLayout.SOUTH, removedKeysScrollPane);
        dialog.add(panel);
        panel.setPreferredSize(new Dimension(width, height + buttonHeight));
        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void modifyOption(GuiStoryOption option) {

    }

    private static class KeyEditPanel extends JPanel {

        private static int lineWidth = 200;
        private static int lineHeight = 50;

        private List<StoryKey> keys;

        private SpringLayout layout;

        public KeyEditPanel(List<StoryKey> keys) {
            this.keys = keys;
            this.layout = new SpringLayout();
            this.setLayout(layout);

            this.setBackground(new Color(255, 0, 0));

            this.keys.add(new StoryKey("test", 5));

            generate();
        }

        private void generate() {
            this.removeAll();
            this.setPreferredSize(new Dimension(lineWidth, lineHeight * (1 + keys.size())));

            Component previous = null;
            for (StoryKey key : keys) {
                JPanel keyPanel = new JPanel();
                keyPanel.setBackground(new Color(0, 0, 255));
                keyPanel.setPreferredSize(new Dimension(lineWidth, lineHeight));
                JTextField keyTextField = new JTextField(key.getKey());
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

                this.add(keyPanel);
                this.layout.putConstraint(SpringLayout.WEST, keyPanel, 5, SpringLayout.WEST,
                        this);
                this.layout.putConstraint(SpringLayout.NORTH, keyPanel, 5,
                        previous == null ? SpringLayout.NORTH : SpringLayout.SOUTH,
                        previous == null ? this : previous);

                previous = keyPanel;
            }
        }
    }

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