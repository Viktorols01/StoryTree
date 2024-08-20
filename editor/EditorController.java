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
import editor.serializable.InputInteractible;
import editor.serializable.OutputInteractible;
import editor.serializable.EditorNode.OptionPair;
import tools.Camera;
import tools.InterfacePanel.Input;

public class EditorController {

    private Camera camera;
    private Input input;
    private FontMetrics fontMetrics;

    private Box draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private OutputInteractible connectComponent = null;
    private boolean connecting = false;

    private EditorFolder guiFolder;

    public EditorController(Input input, int width, int height, FontMetrics fontMetrics) {
        this.camera = new Camera(width, height);
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

    public void render(Graphics2D g2d) {
        g2d.setColor(EditorStyle.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
        camera.transform(g2d);

        if (connecting) {
            Point2D absPos = getAbsoluteMousePosition();
            EditorStyle.renderLine(g2d,
                    (int) (connectComponent.getX() + connectComponent.getW() / 2),
                    (int) (connectComponent.getY() + connectComponent.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        if (guiFolder.getEntryBox().getOutput() != null) {
            EditorStyle.renderOutputLine(g2d, guiFolder.getEntryBox());
        }

        for (EditorNode node : guiFolder.getNodes()) {
            EditorStyle.renderOptionPairs(g2d, node);
        }
        for (EditorNode node : guiFolder.getNodes()) {
            EditorStyle.renderStoryNode(g2d, node, false);
        }

        EditorStyle.renderEntryBox(g2d, guiFolder.getEntryBox());
        if (guiFolder.getExitBox() != null) {
            EditorStyle.renderExitBox(g2d, guiFolder.getExitBox());
        }
    }

    public void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (EditorNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedBox = node;
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

    public void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
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
                            EditorStyle.updateOptions(fontMetrics, (EditorNode) output);
                        }
                    }
                    if (draggedBox instanceof EditorNode) {
                        EditorStyle.updateOptions(fontMetrics,
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
            for (EditorNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connecting = true;
                    connectComponent = node;
                    return;
                }
                for (EditorNode.OptionPair pair : node.getOptionPairs()) {
                    EditorOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        EditorStyle.updateSize(fontMetrics, option);
                        EditorStyle.updateOptions(fontMetrics, node);
                        return;
                    }
                }
            }

            EditorFolderEntry entryBox = guiFolder.getEntryBox();
            if (entryBox.isInside(absPos.getX(), absPos.getY())) {
                connecting = true;
                connectComponent = entryBox;
                return;
            }
        }
    }

    public void connecting() {
    }

    public void endConnecting() {
        Point2D absPos = getAbsoluteMousePosition();

        if (connecting) {
            connecting = false;

            if (connectComponent instanceof EditorNode) {
                if (connectComponent.isInside(absPos.getX(), absPos.getY())) {
                    EditorNode node = (EditorNode) connectComponent;
                    UserInputGetter.modifyNode(node);
                    EditorStyle.updateSize(fontMetrics, node);
                    EditorStyle.updateOptions(fontMetrics, node);
                    connectComponent = null;
                    return;
                }
            }

            for (EditorNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connectComponent.connectOutput(node);
                    node.connectInput(connectComponent);

                    if (connectComponent instanceof EditorNode) {
                        EditorNode connectNode = (EditorNode) connectComponent;
                        EditorStyle.updateOptions(fontMetrics, connectNode);
                    }

                    connectComponent = null;
                    return;
                }
            }

            EditorFolderExit exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectComponent.connectOutput(exitBox);
                    exitBox.connectInput(connectComponent);
                    return;
                }
            }

            EditorNode newNode = addStoryNode("");
            connectComponent.connectOutput(newNode);
            newNode.connectInput(connectComponent);

            String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
            if (nodeInput != null) {
                newNode.setText(nodeInput);
                EditorStyle.updateSize(fontMetrics, newNode);
            }

            if (connectComponent instanceof EditorNode) {
                EditorStyle.updateOptions(fontMetrics, (EditorNode) connectComponent);
            }

            connectComponent = null;
            return;

        }
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public EditorNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        EditorNode node = new EditorNode(text, (int) absPos.getX(), (int) absPos.getY());
        EditorStyle.updateSize(fontMetrics, node);

        guiFolder.getNodes().add(node);
        return node;
    }

    public Box deleteBox() {
        Point2D absPos = getAbsoluteMousePosition();
        for (EditorNode node : guiFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return node;
            }
            for (int i = node.getOptionPairs().size() - 1; i >= 0; i--) {
                OptionPair pair = node.getOptionPairs().get(i);
                EditorOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    node.getOptionPairs().remove(i);
                    return option;
                }
            }
        }
        return null;
    }

    private EditorNode deleteStoryNode(EditorNode node) {
        for (int i = node.getInputs().size() - 1; i >= 0; i--) {
            OutputInteractible component = node.getInputs().get(i);
            component.disconnectOutput(node);
        }
        for (int i = node.getOptionPairs().size() - 1; i >= 0; i--) {
            InputInteractible component = node.getOptionPairs().get(i).getOutput();
            component.disconnectInput(node);
        }
        guiFolder.getNodes().remove(node);
        return node;
    }

    public Point2D getRelativeMousePosition() {
        Point2D relPos = input.getMousePosition(0).getLocation();
        return relPos;
    }

    public Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = camera.inverseTransform(relPos);
        return absPos;
    }
}
