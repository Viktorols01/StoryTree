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
    private static int buttonHeight = 20;
    private static int margin = 5;

    public static String getTextFromPromt(String title, String text) {
        return JOptionPane.showInputDialog(title, text);
    }

    public static void modifyNode(GuiStoryNode node) {
        JDialog dialog = new JDialog((JFrame) null, "Modify node", true);

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        JTextArea textArea = new JTextArea(node.getText(), 4, 0);
        textArea.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        textArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(textArea);

        JScrollPane addedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(dialog, node.getAddedKeys(), width - margin * 2, buttonHeight));
        addedKeysScrollPane.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        addedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(addedKeysScrollPane);

        JScrollPane removedKeysScrollPane = new JScrollPane(
                new KeyEditPanel(dialog, node.getRemovedKeys(), width - margin * 2, buttonHeight));
        removedKeysScrollPane.setPreferredSize(new Dimension(width - margin * 2, height / 3 - margin * 2));
        removedKeysScrollPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panel.add(removedKeysScrollPane);

        JButton submitButton = new JButton("Submit");
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

        private int lineWidth;
        private int lineHeight;

        private Component parent;

        private List<StoryKey> keys;

        private SpringLayout layout;

        public KeyEditPanel(Component parent, List<StoryKey> keys, int lineWidth, int lineHeight) {
            this.parent = parent;
            this.lineWidth = lineWidth;
            this.lineHeight = lineHeight;

            this.keys = keys;

            generate();
        }

        private void generate() {
            this.removeAll();

            System.out.println("Regenerating...");
            System.out.println(keys.toString());

            this.setBackground(new Color(255, 0, 0));
            this.layout = new SpringLayout();
            this.setLayout(layout);

            JPanel modifyPanel = new JPanel();
            modifyPanel.setLayout(new BoxLayout(modifyPanel, BoxLayout.X_AXIS));
            modifyPanel.setPreferredSize(new Dimension(lineWidth, lineHeight));
            JButton addButton = new JButton("+");
            addButton.setPreferredSize(new Dimension(lineWidth, lineHeight));
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
                keyTextField.setPreferredSize(new Dimension((int) (lineWidth * 0.8), lineHeight));
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
                valueTextField.setPreferredSize(new Dimension((int) (lineWidth * 0.2), lineHeight));
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
                this.layout.putConstraint(SpringLayout.WEST, keyPanel, 0, SpringLayout.WEST,
                        this);
                this.layout.putConstraint(SpringLayout.NORTH, keyPanel, 0,
                        previous == null ? SpringLayout.NORTH : SpringLayout.SOUTH,
                        previous == null ? this : previous);

                previous = keyPanel;
            }
            this.parent.repaint();
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