package editor;

import java.awt.FontMetrics;
import java.awt.geom.Point2D;

import editor.serializable.EditorFolder;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.EditorNode.OptionPair;


public class EditorContainer {

    private FontMetrics fontMetrics;
    private EditorFolder editorFolder;

    public EditorContainer(FontMetrics fontMetrics) {
        this.fontMetrics = fontMetrics;
        this.editorFolder = new EditorFolder(200);
    }

    public EditorFolder getEditorFolder() {
        return editorFolder;
    }

    public void setEditorFolder(EditorFolder guiFolder) {
        this.editorFolder = guiFolder;
    }

    public EditorNode addStoryNode(Point2D absPos, String text) {
        EditorNode node = new EditorNode(text, (int) absPos.getX(), (int) absPos.getY());
        EditorFunctions.updateSize(fontMetrics, node);

        editorFolder.getNodes().add(node);
        return node;
    }

    public void deleteInteractible(Point2D absPos) {
        for (EditorNode node : editorFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                EditorFunctions.deleteInputReferences(node);
                EditorFunctions.deleteOutputReferences(node);
                EditorFunctions.deleteFolderReference(node, editorFolder);
                return;
            }
            for (int i = node.getOptionPairs().size() - 1; i >= 0; i--) {
                OptionPair pair = node.getOptionPairs().get(i);
                EditorOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    node.getOptionPairs().remove(i);
                    return;
                }
            }
        }
    }
}
