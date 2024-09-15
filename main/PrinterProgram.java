package main;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

import storyclasses.serializable.StoryExtraNode;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;
import tools.FileHandler;

public class PrinterProgram {
    public static void main(String[] args) {
        StoryTree tree = FileHandler.loadObject("files/stories", "StoryTree files", "st");
        
        if (tree != null) {
            StringBuilder sb = new StringBuilder();

            for (StoryNode node : tree.getNodes()) {
                sb.append("\n" + node.getText());
                sb.append("\n" + "Extra-nodes:");
                StoryExtraNode extraNode = node.getExtraNode();
                while (extraNode != null) {
                    sb.append("\n\t" + extraNode.getText());
                    extraNode = extraNode.getExtraNode();
                }
                sb.append("\n" + "Options:");
                for (StoryOption option : node.getStoryOptions()) {
                    sb.append("\n\t" + option.getText());
                }
                sb.append("\n");
            }

            try {
                File file = new File("files/printed.txt");
                FileOutputStream stream = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF8");
                writer.write(sb.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
