package editor;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import editor.serializable.Box;
import editor.serializable.EditorFolder;
import editor.serializable.EditorFolderEntry;
import editor.serializable.EditorFolderExit;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.EditorNode.OptionPair;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.Interactible;
import editor.serializable.interfaces.OutputInteractible;
import tools.Camera;
import tools.InterfacePanel.Input;

public class EditorContainer {

    private Input input;
    private FontMetrics fontMetrics;

    private EditorNode draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private OutputInteractible connectingComponent = null;
    private boolean connecting = false;

    private EditorFolder guiFolder;

    public EditorContainer(Input input, int width, int height, FontMetrics fontMetrics) {
        this.input = input;
        this.fontMetrics = fontMetrics;
        this.guiFolder = new EditorFolder(200);
    }

    public EditorFolder getGuiFolder() {
        return guiFolder;
    }

    public void setGuiFolder(EditorFolder guiFolder) {
        this.guiFolder = guiFolder;
    }

    public void startDragging(Point2D absPos, Point2D relPos) {
        if (!dragging) {
            for (EditorNode interactible : guiFolder.getNodes()) {
                if (interactible.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - interactible.getX(),
                            absPos.getY() - interactible.getY());
                    draggedBox = interactible;
                    return;
                }
            }

            EditorFolderExit exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - exitBox.getX(), absPos.getY() - exitBox.getY());
                    draggedBox = exitBox;
                    return;
                }
            }

            dragging = true;
            draggedDelta = new Point2D.Double(
                    -relPos.getX() / camera.getZoom() - camera.getX(),
                    -relPos.getY() / camera.getZoom() - camera.getY());
            draggedBox = null;
        }
    }

    public void updateDragging(Point2D absPos, Point2D relPos) {
        if (dragging) {
            if (draggedBox == null) {
                camera
                        .setX(-relPos.getX() / camera.getZoom() - draggedDelta.getX());
                camera
                        .setY(-relPos.getY() / camera.getZoom() - draggedDelta.getY());
            } else {
                draggedBox.setPosition((int) (absPos.getX() - draggedDelta.getX()),
                        (int) (absPos.getY() - draggedDelta.getY()));

                if (draggedBox instanceof InputInteractible) {
                    for (OutputInteractible output : ((InputInteractible) draggedBox).getInputs()) {
                        if (output instanceof EditorNode) {
                            EditorFunctions.updateOptions(fontMetrics, (EditorNode) output);
                        }
                    }
                    if (draggedBox instanceof EditorNode) {
                        EditorFunctions.updateOptions(fontMetrics,
                                (EditorNode) draggedBox);
                    }
                }
            }
        }
    }

    public void endDragging(Point2D absPos, Point2D relPos) {
        if (dragging) {
            dragging = false;
            draggedBox = null;
        }
    }

    public void startConnecting(Point2D absPos, Point2D relPos) {
        if (!connecting) {
            for (EditorNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connecting = true;
                    connectingComponent = node;
                    return;
                }
                for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                    EditorOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        EditorRender.updateSize(fontMetrics, option);
                        EditorRender.updateOptions(fontMetrics, node);
                        return;
                    }
                }
            }

            EditorFolderEntry entryBox = guiFolder.getEntryBox();
            if (entryBox.isInside(absPos.getX(), absPos.getY())) {
                connecting = true;
                connectingComponent = entryBox;
                return;
            }
        }
    }

    public void updateConnecting(Point2D absPos, Point2D relPos) {
    }

    public void endConnecting(Point2D absPos, Point2D relPos) {
        if (connecting) {
            connecting = false;

            if (connectingComponent instanceof EditorNode) {
                if (connectingComponent.isInside(absPos.getX(), absPos.getY())) {
                    EditorNode node = (EditorNode) connectingComponent;
                    UserInputGetter.modifyNode(node);
                    EditorFunctions.updateSize(fontMetrics, node);
                    EditorFunctions.updateOptions(fontMetrics, node);
                    connectingComponent = null;
                    return;
                }
            }

            for (EditorNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connectingComponent.connectOutput(node);
                    node.connectInput(connectingComponent);

                    if (connectingComponent instanceof EditorNode) {
                        EditorNode connectNode = (EditorNode) connectingComponent;
                        EditorFunctions.updateOptions(fontMetrics, connectNode);
                    }

                    connectingComponent = null;
                    return;
                }
            }

            EditorFolderExit exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectingComponent.connectOutput(exitBox);
                    exitBox.connectInput(connectingComponent);
                    return;
                }
            }

            EditorNode newNode = addStoryNode("");
            connectingComponent.connectOutput(newNode);
            newNode.connectInput(connectingComponent);

            String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
            if (nodeInput != null) {
                newNode.setText(nodeInput);
                EditorFunctions.updateSize(fontMetrics, newNode);
            }

            if (connectingComponent instanceof EditorNode) {
                EditorFunctions.updateOptions(fontMetrics, (EditorNode) connectingComponent);
            }

            connectingComponent = null;
            return;

        }
    }

    public boolean hasDraggedBox() {
        return draggedBox != null;
    }

    public boolean isConnecting() {
        return connecting;
    }

    public OutputInteractible getConnectingComponent() {
        return connectingComponent;
    }

    public EditorNode addStoryNode(Point2D absPos, String text) {
        EditorNode node = new EditorNode(text, (int) absPos.getX(), (int) absPos.getY());
        EditorFunctions.updateSize(fontMetrics, node);

        guiFolder.getNodes().add(node);
        return node;
    }

    public void deleteInteractible(Point2D absPos) {
        for (EditorNode node : guiFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                EditorFunctions.deleteInputReferences(node);
                EditorFunctions.deleteOutputReferences(node);
                EditorFunctions.deleteFolderReference(node, guiFolder);
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
