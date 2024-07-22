package gui;

import java.awt.Button;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;

public class UserInputGetter {
    public static String getTextFromPromt(String title, String text) {
        return JOptionPane.showInputDialog(title, text);
    }

    public static void modifyNode(GuiStoryNode node) {
        JDialog dialog = new JDialog((JFrame) null, "Custom Input Dialog", true);
        dialog.setSize(400, 300);

        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        JTextArea textArea = new JTextArea(node.getText(), 4, 0);
        panel.add(textArea);
        Button submitButton = new Button("Submit");
        submitButton.addActionListener((a) -> {
            node.setText(textArea.getText());
            dialog.dispose();
        });
        panel.add(submitButton);

        layout.putConstraint(SpringLayout.WEST, submitButton,
                5,
                SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, submitButton,
                5,
                SpringLayout.SOUTH, textArea);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void modifyOption(GuiStoryOption option) {

    }
}
