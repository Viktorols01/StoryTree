package editor;

import java.awt.FontMetrics;

import editor.serializable.EditorFolder;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;
import editor.serializable.interfaces.TextInteractible;

public class EditorFunctions {
    public static void deleteInputReferences(OutputInteractible node) {
        for (int i = node.getOutputs().size() - 1; i >= 0; i--) {
            InputInteractible component = node.getOutputs().get(i);
            component.disconnectInput(node);
        }
    }

    public static void deleteOutputReferences(InputInteractible node) {
        for (int i = node.getInputs().size() - 1; i >= 0; i--) {
            OutputInteractible component = node.getInputs().get(i);
            component.disconnectOutput(node);
        }
    }

    public static void deleteFolderReference(EditorNode interactible, EditorFolder folder) {
        folder.getNodes().remove(interactible);
    }

    public static void deleteFolderReference(EditorFolder childFolder, EditorFolder folder) {
        folder.getChildrenFolders().remove(childFolder);
    }

    public static void connect(OutputInteractible output, InputInteractible input) {
        output.connectOutput(input);
        input.connectInput(output);
    }

     public static void updateSize(FontMetrics fontMetrics, TextInteractible textInteractible) {
        int width = EditorUtility.getTextWidth(textInteractible.getText(), fontMetrics);
        int height = EditorUtility.getTextHeight(textInteractible.getText(), fontMetrics);
        textInteractible.setSize(width + 2 * EditorConstants.ARC_DIAMETER, height + 2 * EditorConstants.ARC_DIAMETER);
    }

    public static void updateOptions(FontMetrics fontMetrics, EditorNode node) {
        final int margin = 10;
        int totalWidth = 0;

        sortOptions(node);

        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            updateSize(fontMetrics, option);
        }

        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            totalWidth += EditorUtility.getTextWidth(option.getText(), fontMetrics) + EditorConstants.ARC_DIAMETER * 2
                    + margin;
        }
        totalWidth -= margin;

        int x = node.getX() + node.getW() / 2 - totalWidth / 2;
        int y = node.getY() + node.getH() + margin;
        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            int width = EditorUtility.getTextWidth(option.getText(), fontMetrics);
            option.setPosition(x, y);
            x += width + EditorConstants.ARC_DIAMETER * 2 + margin;
        }
    }

    public static void sortOptions(EditorNode node) {
        node.getOptionPairs().sort((a, b) -> a.getOutput().getX() - b.getOutput().getX());
    }
}
