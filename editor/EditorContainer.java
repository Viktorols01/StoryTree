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
        this.editorFolder = new EditorFolder("Root");
    }

    public EditorFolder getEditorFolder() {
        return editorFolder;
    }

    public void setEditorFolder(EditorFolder guiFolder) {
        this.editorFolder = guiFolder;
    }

    public EditorNode addEditorNode(Point2D absPos, String text) {
        EditorNode node = new EditorNode(text, (int) absPos.getX(), (int) absPos.getY());
        EditorFunctions.updateSize(fontMetrics, node, EditorConstants.ARC_DIAMETER_NODE);

        editorFolder.getNodes().add(node);
        return node;
    }

    public void addEditorFolder(Point2D absPos, String text) {
        editorFolder.getChildrenFolders()
                .add(new EditorFolder(text, (int) absPos.getX(), (int) absPos.getY(), editorFolder));
    }

    public boolean enterEditorFolder(Point2D absPos) {
        for (EditorFolder folder : editorFolder.getChildrenFolders()) {
            if (folder.isInside(absPos.getX(), absPos.getY())) {
                editorFolder = folder;
                return true;
            }
        }
        return false;
    }

    public boolean exitEditorFolder(Point2D absPos) {
        EditorFolder parentFolder = editorFolder.getParentFolder();
        if (parentFolder == null) {
            return false;
        }

        editorFolder = editorFolder.getParentFolder();
        return true;
    }

    public boolean deleteInteractible(Point2D absPos) {
        for (EditorNode node : editorFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                EditorFunctions.deleteInputReferences(node);
                EditorFunctions.deleteOutputReferences(node);
                EditorFunctions.deleteFolderReference(node, editorFolder);
                return true;
            }
            for (int i = node.getOptionPairs().size() - 1; i >= 0; i--) {
                OptionPair pair = node.getOptionPairs().get(i);
                EditorOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    node.getOptionPairs().remove(i);
                    return true;
                }
            }
        }

        for (EditorFolder folder : editorFolder.getChildrenFolders()) {
            if (folder.isInside(absPos.getX(), absPos.getY())) {
                EditorFunctions.deleteInputReferences(folder);
                EditorFunctions.deleteOutputReferences(folder);
                EditorFunctions.deleteFolderReference(folder, editorFolder);
                return true;
            }
        }
        return false;
    }
}
