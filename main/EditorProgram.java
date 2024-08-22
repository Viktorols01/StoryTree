package main;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JFrame;
import javax.swing.JPanel;

import editor.EditorPanel;
import editor.EditorSerializer;
import editor.serializable.EditorFolder;
import storyclasses.readers.ConsoleStoryReader;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

public class EditorProgram {

    public static void main(String[] args) {
        JFrame frame = new JFrame("StoryTree editor");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        EditorPanel gui = new EditorPanel(1000, 800);
        panel.add(gui, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();

        Button playButton = new Button("Play story");
        playButton.addActionListener((a) -> {
            StoryTree tree = EditorSerializer.toStoryTree(gui.getGuiFolder());
            if (tree != null) {
                ConsoleStoryReader reader = new ConsoleStoryReader(tree);
                reader.read();
            }
        });
        buttonPanel.add(playButton);

        Button savePlayableButton = new Button("Save playable story");
        savePlayableButton.addActionListener((a) -> {
            StoryTree tree = EditorSerializer.toStoryTree(gui.getGuiFolder());
            if (tree != null) {
                FileHandler.saveObject(tree, "files/stories", "Story files", "st");
            }
        });
        buttonPanel.add(savePlayableButton);

        Button saveButton = new Button("Save story");
        saveButton.addActionListener((a) -> {
            EditorFolder folder = gui.getGuiFolder();
            if (folder != null) {
                FileHandler.saveObject(folder, "files/guistories", "GUI story files", "gst");
            }
        });
        buttonPanel.add(saveButton);

        Button loadButton = new Button("Load story");
        loadButton.addActionListener((a) -> {
            EditorFolder folder = FileHandler.loadObject("files/guistories", "GUI story files", "gst");
            if (folder != null) {
                gui.setGuiFolder(folder);
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