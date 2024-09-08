package editor;

import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.geom.Point2D;

import editor.serializable.EditorExtraNode;
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

    public EditorFolder getFolder() {
        return container.getEditorFolder();
    }

    public void setEditorFolder(EditorFolder guiFolder) {
        container.setEditorFolder(guiFolder);
    }

    public void startDragging() {
        if (!dragging) {
            Point2D absPos = getAbsoluteMousePosition();

            Interactible hoveredComponent = getHoveredComponent();

            if (hoveredComponent instanceof EditorNode || hoveredComponent instanceof EditorFolder
                    || hoveredComponent instanceof EditorFolderExit) {
                dragging = true;
                draggedDelta = new Point2D.Double(absPos.getX() - hoveredComponent.getX(),
                        absPos.getY() - hoveredComponent.getY());
                draggedBox = hoveredComponent;
                return;
            } else {
                Point2D relPos = getRelativeMousePosition();
                dragging = true;
                draggedDelta = new Point2D.Double(
                        -relPos.getX() / camera.getZoom() - camera.getX(),
                        -relPos.getY() / camera.getZoom() - camera.getY());
                draggedBox = null;
            }
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
                            Utility.updateOptionsAndExtraNodes(fontMetrics, (EditorNode) output);
                        }
                    }
                    if (draggedBox instanceof EditorNode) {
                        Utility.updateOptionsAndExtraNodes(fontMetrics,
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
        if (!connecting) {
            Point2D absPos = getAbsoluteMousePosition();

            Interactible hoveredComponent = getHoveredComponent();

            if (hoveredComponent instanceof EditorNode) {
                connecting = true;
                connectingComponent = (EditorNode) hoveredComponent;
                return;
            }

            if (hoveredComponent instanceof EditorFolder) {
                connecting = true;
                connectingComponent = (EditorFolder) hoveredComponent;
                return;
            }

            if (hoveredComponent instanceof EditorFolderEntry) {
                connecting = true;
                connectingComponent = (EditorFolderEntry) hoveredComponent;
                return;
            }

            for (EditorNode node : getFolder().getNodes()) {
                for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                    EditorOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        Utility.updateSize(fontMetrics, option, Constants.ARC_DIAMETER_OPTION);
                        Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                        return;
                    }
                }
                EditorExtraNode extraNode = node.getExtraNode();
                while (extraNode != null) {
                    if (extraNode.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyExtraNode(extraNode);
                        Utility.updateSize(fontMetrics, extraNode, Constants.ARC_DIAMETER_OPTION);
                        Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                        return;
                    }
                    extraNode = extraNode.getExtraNode();
                }
            }
        }
    }

    public void updateConnecting() {
    }

    public void endConnecting() {
        if (connecting) {
            connecting = false;

            Interactible hoveredComponent = getHoveredComponent();

            if (hoveredComponent == connectingComponent) {
                if (connectingComponent instanceof EditorNode) {
                    EditorNode node = (EditorNode) connectingComponent;
                    UserInputGetter.modifyNode(node);
                    Utility.updateSize(fontMetrics, node, Constants.ARC_DIAMETER_NODE);
                    Utility.updateOptionsAndExtraNodes(fontMetrics, node);
                }
                if (connectingComponent instanceof EditorFolder) {
                    EditorFolder folder = (EditorFolder) connectingComponent;
                    String folderInput = UserInputGetter.getTextFromPromt("Renaming folder...", folder.getText());
                    if (folderInput != null) {
                        folder.setText(folderInput);
                    }
                }
                connectingComponent = null;
                return;
            }

            if (hoveredComponent instanceof EditorNode) {
                connectConnectingComponent((EditorNode) hoveredComponent);
                return;
            }

            if (hoveredComponent instanceof EditorFolder) {
                connectConnectingComponent((EditorFolder) hoveredComponent);
                return;
            }

            if (hoveredComponent instanceof EditorFolderExit) {
                connectConnectingComponent((EditorFolderExit) hoveredComponent);
                return;
            }

            EditorNode newNode = container.addNode(getAbsoluteMousePosition(), "");
            connectConnectingComponent(newNode);
            String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
            newNode.setText(nodeInput == null ? "..." : nodeInput);
            Utility.updateSize(fontMetrics, newNode, Constants.ARC_DIAMETER_NODE);
        }
    }

    private void connectConnectingComponent(InputInteractible input) {
        if (connectingComponent instanceof EditorNode) {
            EditorNode connectNode = (EditorNode) connectingComponent;

            String optionInput = UserInputGetter.getTextFromPromt("Adding option...", "");
            if (optionInput == null) {
                Utility.connect(connectingComponent, input);
            } else {
                connectNode.connectOutput(input, optionInput);
                input.connectInput(connectNode);
                Utility.updateOptionsAndExtraNodes(fontMetrics, connectNode);
            }
        } else {
            Utility.connect(connectingComponent, input);
        }
        connectingComponent = null;
    }

    public void addNode() {
        String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
        if (nodeInput != null) {
            container.addNode(getAbsoluteMousePosition(), nodeInput);
        }
    }

    public void addFolder() {
        String titleInput = UserInputGetter.getTextFromPromt("Adding folder...", "");
        if (titleInput != null) {
            container.addFolder(getAbsoluteMousePosition(), titleInput);
        }
    }

    public void addExtraNode() {
        String titleInput = UserInputGetter.getTextFromPromt("Adding extra node...", "");
        if (titleInput != null) {
            container.addExtraNode(getAbsoluteMousePosition(), titleInput);
        }
    }

    public void enterFolder() {
        if (container.enterFolder(getAbsoluteMousePosition())) {
            camera.setX(container.getEditorFolder().getEntryBox().getX());
            camera.setY(container.getEditorFolder().getEntryBox().getY());
        }
    }

    public void exitFolder() {
        EditorFolder previousFolder = getFolder();
        if (container.exitFolder(getAbsoluteMousePosition())) {
            camera.setX(previousFolder.getX());
            camera.setY(previousFolder.getY());
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
        Point relPos = input.getMousePosition(0);
        if (relPos == null) {
            return new Point2D.Double(0, 0);
        }
        return relPos.getLocation();
    }

    public Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = camera.inverseTransform(relPos);
        return absPos;
    }

    public Interactible getHoveredComponent() {
        Point2D absPos = getAbsoluteMousePosition();

        for (EditorNode node : getFolder().getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                return node;
            }

            EditorExtraNode extraNode = node.getExtraNode();
            while (extraNode != null) {
                if (extraNode.isInside(absPos.getX(), absPos.getY())) {
                    return extraNode;
                }
                extraNode = extraNode.getExtraNode();
            }

            for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                EditorOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    return option;
                }
            }
        }

        for (EditorFolder childrenFolder : getFolder().getChildrenFolders()) {
            if (childrenFolder.isInside(absPos.getX(), absPos.getY())) {
                return childrenFolder;
            }
        }

        EditorFolderExit exitBox = getFolder().getExitBox();
        if (exitBox != null) {
            if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                return exitBox;
            }
        }

        EditorFolderEntry entryBox = getFolder().getEntryBox();
        if (entryBox.isInside(absPos.getX(), absPos.getY())) {
            return entryBox;
        }

        return null;
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public Camera getCamera() {
        return camera;
    }
}
