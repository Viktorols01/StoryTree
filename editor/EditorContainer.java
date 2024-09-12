package editor;

import java.awt.FontMetrics;
import java.awt.geom.Point2D;

import editor.serializable.EditorExtraNode;
import editor.serializable.EditorFolder;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.EditorNode.OptionPair;
import editor.serializable.interfaces.Interactible;

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

    public EditorNode addNode(Point2D absPos, String text) {
        EditorNode node = new EditorNode(text, (int) absPos.getX(), (int) absPos.getY());
        Utility.updateSize(fontMetrics, node, Constants.ARC_DIAMETER_NODE);

        editorFolder.getNodes().add(node);
        return node;
    }

    public void addFolder(Point2D absPos, String text) {
        editorFolder.getChildrenFolders()
                .add(new EditorFolder(text, (int) absPos.getX(), (int) absPos.getY(), editorFolder));
    }

    public boolean enterFolder(Point2D absPos) {
        for (EditorFolder folder : editorFolder.getChildrenFolders()) {
            if (folder.isInside(absPos.getX(), absPos.getY())) {
                editorFolder = folder;
                return true;
            }
        }
        return false;
    }

    public boolean exitFolder(Point2D absPos) {
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
                Utility.deleteInputReferences(node);
                Utility.deleteOutputReferences(node);
                Utility.deleteFolderReference(node, editorFolder);
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
            Interactible prev = node;
            EditorExtraNode extraNode = node.getExtraNode();
            while (extraNode != null) {
                if (extraNode.isInside(absPos.getX(), absPos.getY())) {
                    if (prev instanceof EditorNode) {
                        ((EditorNode) prev).setExtraNode(extraNode.getExtraNode());
                    }
                    if (prev instanceof EditorExtraNode) {
                        ((EditorExtraNode) prev).setExtraNode(extraNode.getExtraNode());
                    }
                    Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                    return true;
                }
                prev = extraNode;
                extraNode = extraNode.getExtraNode();
            }
        }

        for (EditorFolder folder : editorFolder.getChildrenFolders()) {
            if (folder.isInside(absPos.getX(), absPos.getY())) {
                Utility.deleteInputReferences(folder);
                Utility.deleteOutputReferences(folder);
                Utility.deleteFolderReference(folder, editorFolder);
                return true;
            }
        }
        return false;
    }

    public boolean addExtraNode(Point2D absPos, String titleInput) {
        for (EditorNode node : editorFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                EditorExtraNode existingExtraNode = node.getExtraNode();
                EditorExtraNode newExtraNode = new EditorExtraNode(titleInput);
                node.setExtraNode(newExtraNode);
                newExtraNode.setExtraNode(existingExtraNode);
                Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                return true;
            }
            EditorExtraNode extraNode = node.getExtraNode();
            while (extraNode != null) {
                if (extraNode.isInside(absPos.getX(), absPos.getY())) {
                    EditorExtraNode existingExtraNode = extraNode.getExtraNode();
                    EditorExtraNode newExtraNode = new EditorExtraNode(titleInput);
                    extraNode.setExtraNode(newExtraNode);
                    newExtraNode.setExtraNode(existingExtraNode);
                    Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                    return true;
                }
                extraNode = extraNode.getExtraNode();
            }
        }
        return false;
    }
}
