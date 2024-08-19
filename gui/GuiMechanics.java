package gui;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import gui.serializable.GuiBox;
import gui.serializable.InputInteractible;
import gui.serializable.OutputInteractible;
import gui.serializable.GuiEntryBox;
import gui.serializable.GuiExitBox;
import gui.serializable.GuiStoryFolder;
import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryNode.OptionPair;
import gui.serializable.GuiStoryOption;

import tools.Camera;
import tools.Gui.Input;

public class GuiMechanics {

    private Camera camera;
    private Input input;
    private FontMetrics fontMetrics;

    private GuiBox draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private OutputInteractible connectComponent = null;
    private boolean connecting = false;

    private GuiStoryFolder guiFolder;

    public GuiMechanics(Input input, int width, int height, FontMetrics fontMetrics) {
        this.camera = new Camera(width, height);
        this.input = input;
        this.fontMetrics = fontMetrics;
        this.guiFolder = new GuiStoryFolder(200);
    }

    public GuiStoryFolder getGuiFolder() {
        return guiFolder;
    }

    public void setGuiFolder(GuiStoryFolder guiFolder) {
        this.guiFolder = guiFolder;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(GuiStyle.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
        camera.transform(g2d);

        if (connecting) {
            Point2D absPos = getAbsoluteMousePosition();
            GuiStyle.renderLine(g2d,
                    (int) (connectComponent.getX() + connectComponent.getW() / 2),
                    (int) (connectComponent.getY() + connectComponent.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        if (guiFolder.getEntryBox().getOutput() != null) {
            GuiStyle.renderOutputLine(g2d, guiFolder.getEntryBox());
        }

        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderOptionPairs(g2d, node);
        }
        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderStoryNode(g2d, node, false);
        }

        GuiStyle.renderEntryBox(g2d, guiFolder.getEntryBox());
        if (guiFolder.getExitBox() != null) {
            GuiStyle.renderExitBox(g2d, guiFolder.getExitBox());
        }
    }

    public void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedBox = node;
                    return;
                }
            }

            GuiExitBox exitBox = guiFolder.getExitBox();
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
                if (draggedBox instanceof GuiStoryNode) {
                    GuiStyle.updateOptions(fontMetrics,
                            (GuiStoryNode) draggedBox);
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
            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connecting = true;
                    connectComponent = node;
                    return;
                }
                for (GuiStoryNode.OptionPair pair : node.getOptionPairs()) {
                    GuiStoryOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        GuiStyle.updateSize(fontMetrics, option);
                        GuiStyle.updateOptions(fontMetrics, node);
                        return;
                    }
                }
            }

            GuiEntryBox entryBox = guiFolder.getEntryBox();
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

            if (connectComponent instanceof GuiStoryNode) {
                if (connectComponent.isInside(absPos.getX(), absPos.getY())) {
                    GuiStoryNode node = (GuiStoryNode) connectComponent;
                    UserInputGetter.modifyNode(node);
                    GuiStyle.updateSize(fontMetrics, node);
                    GuiStyle.updateOptions(fontMetrics, node);
                    connectComponent = null;
                    return;
                }
            }

            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    connectComponent.connectOutput(node);
                    node.connectInput(connectComponent);

                    if (connectComponent instanceof GuiStoryNode) {
                        GuiStoryNode connectNode = (GuiStoryNode) connectComponent;
                        GuiStyle.updateOptions(fontMetrics, connectNode);
                    }

                    connectComponent = null;
                    return;
                }
            }

            GuiExitBox exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectComponent.connectOutput(exitBox);
                    exitBox.connectInput(connectComponent);
                    return;
                }
            }

            GuiStoryNode newNode = addStoryNode("");
            connectComponent.connectOutput(newNode);
            newNode.connectInput(connectComponent);

            String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
            if (nodeInput != null) {
                newNode.setText(nodeInput);
                GuiStyle.updateSize(fontMetrics, newNode);
            }

            if (connectComponent instanceof GuiStoryNode) {
                GuiStyle.updateOptions(fontMetrics, (GuiStoryNode) connectComponent);
            }

            connectComponent = null;
            return;

        }
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public GuiStoryNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        GuiStoryNode node = new GuiStoryNode(text, (int) absPos.getX(), (int) absPos.getY());
        GuiStyle.updateSize(fontMetrics, node);

        guiFolder.getNodes().add(node);
        return node;
    }

    public GuiBox deleteBox() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : guiFolder.getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return node;
            }
            for (int i = node.getOptionPairs().size() - 1; i >= 0; i--) {
                OptionPair pair = node.getOptionPairs().get(i);
                GuiStoryOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    node.getOptionPairs().remove(i);
                    return option;
                }
            }
        }
        return null;
    }

    private GuiStoryNode deleteStoryNode(GuiStoryNode node) {
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
