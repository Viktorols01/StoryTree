package editor;

import java.awt.FontMetrics;
import java.awt.geom.Point2D;

import editor.serializable.EditorFolder;
import editor.serializable.EditorFolderEntry;
import editor.serializable.EditorFolderExit;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.Interactible;
import editor.serializable.interfaces.OutputInteractible;
import tools.Camera;
import tools.InterfacePanel.Input;


// contains COMMANDS without parameters
public class EditorContext {
    private Camera camera;
    private Input input;
    private FontMetrics fontMetrics;
    
    private Interactible draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private OutputInteractible connectingComponent = null;
    private boolean connecting = false;

    private EditorContainer container;

    public EditorContext(int width, int height, Input input, FontMetrics fontMetrics) {
        this.camera = new Camera(width, height);
        this.input = input;
        this.container = new EditorContainer(fontMetrics);
        this.fontMetrics = fontMetrics;
    }

    public EditorFolder getEditorFolder() {
        return container.getEditorFolder();
    }

    public void setEditorFolder(EditorFolder guiFolder) {
        container.setEditorFolder(guiFolder);
    }

    public void startDragging() {
        Point2D absPos = getAbsoluteMousePosition();
        Point2D relPos = getRelativeMousePosition();
        if (!dragging) {
            for (EditorNode interactible : getEditorFolder().getNodes()) {
                if (interactible.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - interactible.getX(),
                            absPos.getY() - interactible.getY());
                    draggedBox = interactible;
                    return;
                }
            }

            EditorFolderExit exitBox = getEditorFolder().getExitBox();
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

    public void updateDragging() {
        Point2D absPos = getAbsoluteMousePosition();
        Point2D relPos = getRelativeMousePosition();
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

    public void endDragging() {
        if (dragging) {
            dragging = false;
            draggedBox = null;
        }
    }

    public void startConnecting() {
        Point2D absPos = getAbsoluteMousePosition();
        if (!connecting) {
            for (EditorNode node : getEditorFolder().getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connecting = true;
                    connectingComponent = node;
                    return;
                }
                for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                    EditorOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        EditorFunctions.updateSize(fontMetrics, option);
                        EditorFunctions.updateOptions(fontMetrics, node);
                        return;
                    }
                }
            }

            EditorFolderEntry entryBox = getEditorFolder().getEntryBox();
            if (entryBox.isInside(absPos.getX(), absPos.getY())) {
                connecting = true;
                connectingComponent = entryBox;
                return;
            }
        }
    }

    public void updateConnecting() {
    }

    public void endConnecting() {
        Point2D absPos = getAbsoluteMousePosition();
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

            for (EditorNode node : getEditorFolder().getNodes()) {
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

            EditorFolderExit exitBox = getEditorFolder().getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectingComponent.connectOutput(exitBox);
                    exitBox.connectInput(connectingComponent);
                    return;
                }
            }

            EditorNode newNode = container.addStoryNode(getAbsoluteMousePosition(), "");
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

    public void addStoryNode() {
        String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
        if (nodeInput != null) {
            container.addStoryNode(getAbsoluteMousePosition(), nodeInput);
        }
    }

    public void deleteInteractible() {
        container.deleteInteractible(getAbsoluteMousePosition());
    }

    public boolean isConnecting() {
        return connecting;
    }

    public OutputInteractible getConnectingComponent() {
        return connectingComponent;
    }

    private Point2D getRelativeMousePosition() {
        Point2D relPos = input.getMousePosition(0).getLocation();
        return relPos;
    }

    public Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = camera.inverseTransform(relPos);
        return absPos;
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public Camera getCamera() {
        return camera;
    }
}
