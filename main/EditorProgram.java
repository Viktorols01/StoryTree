package main;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.GuiStoryContainer;
import gui.GuiStoryEditor;
import storyclasses.serializable.StoryNode;
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
            StoryNode root = gui.getGuiContainer().toStoryTree();
            if (root != null) {
                FileHandler.saveObject(root, "files/stories", "Story files", "story");
            }
        });
        buttonPanel.add(savePlayableButton);

        Button saveButton = new Button("Save story");
        saveButton.addActionListener((a) -> {
            GuiStoryContainer container = gui.getGuiContainer();
            if (container != null) {
                FileHandler.saveObject(container, "files/guistories", "GUI story containers", "gcontainer");
            }
        });
        buttonPanel.add(saveButton);

        Button loadButton = new Button("Load story");
        loadButton.addActionListener((a) -> {
            GuiStoryContainer container = FileHandler.loadObject("files/guistories", "GUI story containers", "gcontainer");
            if (container != null) {
                gui.setGuiContainer(container);
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