package editor;

import java.awt.FontMetrics;
import java.util.Iterator;
import java.util.List;

import editor.serializable.EditorExtraNode;
import editor.serializable.EditorFolder;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;
import editor.serializable.interfaces.TextInteractible;
import storyclasses.serializable.StoryKey;

public class Utility {
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

    public static void updateSize(FontMetrics fontMetrics, TextInteractible textInteractible, int arcDiameter) {
        int width = getTextWidth(textInteractible.getText(), fontMetrics);
        width = (width < arcDiameter) ? arcDiameter : width;
        int height = getTextHeight(textInteractible.getText(), fontMetrics);
        height = (height < arcDiameter) ? arcDiameter : height;
        textInteractible.setSize(width + 2 * arcDiameter, height + 2 * arcDiameter);
    }

    public static void updateOptionsAndExtraNodes(FontMetrics fontMetrics, EditorNode node) {
        updateOptions(fontMetrics, node);
        updateExtraNodes(fontMetrics, node);
    }

    public static void updateExtraNodes(FontMetrics fontMetrics, EditorNode node) {
        int x = node.getX() + node.getW() + Constants.MARGIN;
        int y = node.getY() - Constants.MARGIN;

        EditorExtraNode extraNode = node.getExtraNode();
        while (extraNode != null) {
            Utility.updateSize(fontMetrics, extraNode, Constants.ARC_DIAMETER_OPTION);
            extraNode.setPosition(x, y - extraNode.getH());
            x += extraNode.getW() + Constants.MARGIN;
            extraNode = extraNode.getExtraNode();
        }
    }

    public static void updateOptions(FontMetrics fontMetrics, EditorNode node) {

        sortOptions(node);

        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            updateSize(fontMetrics, option, Constants.ARC_DIAMETER_OPTION);
        }

        int totalWidth = 0;
        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            totalWidth += option.getW() + Constants.MARGIN;
        }
        totalWidth -= Constants.MARGIN;

        int x = node.getX() + node.getW() / 2 - totalWidth / 2;
        int y = node.getY() + node.getH() + Constants.MARGIN;
        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            option.setPosition(x, y);
            x += option.getW() + Constants.MARGIN;
        }
    }

    public static void sortOptions(EditorNode node) {
        node.getOptionPairs().sort((a, b) -> a.getOutput().getX() - b.getOutput().getX());
    }

    public static int getTextWidth(String text, FontMetrics fontMetrics) {
        if (text == null) {
            return 0;
        }
        Iterator<String> iterable = text.lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }

        return width;
    }

    public static int getLineHeight(FontMetrics fontMetrics) {
        int lineHeight = fontMetrics.getHeight();
        return lineHeight;
    }

    public static int getTextHeight(String text, FontMetrics fontMetrics) {
        if (text == null) {
            return 0;
        }
        int height = (int) (getLineHeight(fontMetrics) * text.lines().count());
        return height;
    }

    public static String getFolderLocation(EditorFolder folder) {
        if (folder.getParentFolder() == null) {
            return folder.getText();
        } else {
            return getFolderLocation(folder.getParentFolder()) + ", " + folder.getText();
        }
    }

    public static String keyListToString(List<StoryKey> list) {
        Iterator<StoryKey> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            StoryKey key = iterator.next();
            sb.append(key.getKey() + ": " + key.getValue());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
