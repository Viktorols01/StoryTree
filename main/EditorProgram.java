package main;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.GuiStoryEditor;
import gui.serializable.GuiStoryNode;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

public class EditorProgram {

    public static void main(String[] args) {
        JFrame frame = new JFrame("StoryTree editor");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        GuiStoryEditor gui = new GuiStoryEditor(1000, 800);
        panel.add(gui, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        Button savePlayableButton = new Button("Save playable story");
        savePlayableButton.addActionListener((a) -> {
            StoryTree tree = gui.getGuiContainer().toStoryTree();
            if (tree != null) {
                FileHandler.saveObject(tree, "files/stories", "Story files", "st");
            }
        });
        buttonPanel.add(savePlayableButton);

        Button saveButton = new Button("Save story");
        saveButton.addActionListener((a) -> {
            GuiStoryNode root = gui.getGuiContainer().getRoot();
            if (root != null) {
                FileHandler.saveObject(root, "files/guistories", "GUI story files", "gst");
            }
        });
        buttonPanel.add(saveButton);

        Button loadButton = new Button("Load story");
        loadButton.addActionListener((a) -> {
            GuiStoryNode root = FileHandler.loadObject("files/guistories", "GUI story files", "gst");
            if (root != null) {
                gui.getGuiContainer().loadRoot(root);;
            }
        });
        buttonPanel.add(loadButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
}