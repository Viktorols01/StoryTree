package gui;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gui.serializable.ConnectableInput;
import gui.serializable.ConnectableOutput;
import gui.serializable.GuiBox;
import gui.serializable.GuiInputBox;
import gui.serializable.GuiEntryBox;
import gui.serializable.GuiExitBox;
import gui.serializable.GuiStoryFolder;
import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import storyclasses.serializable.StoryKey;
import storyclasses.serializable.StoryNode;
import storyclasses.serializable.StoryOption;
import storyclasses.serializable.StoryTree;
import tools.Camera;
import tools.Gui.Input;

public class GuiMechanics {

    private Camera camera;
    private Input input;
    private FontMetrics fontMetrics;

    private GuiBox draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private GuiInputBox connectBox = null;
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
                    (int) (connectBox.getX() + connectBox.getW() / 2),
                    (int) (connectBox.getY() + connectBox.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        GuiStyle.renderOutputLine(g2d, guiFolder.getEntryBox());

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
                    GuiStyle.updateOptionPositions(fontMetrics,
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
                    connectBox = node;
                    return;
                }
                for (GuiStoryNode.OptionPair pair : node.getOptionPairs()) {
                    GuiStoryOption option = pair.getOption();
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        GuiStyle.updateOptionSize(fontMetrics, option);
                        GuiStyle.updateOptionPositions(fontMetrics, node);
                        return;
                    }
                }
            }

            GuiEntryBox entryBox = guiFolder.getEntryBox();
            if (entryBox.isInside(absPos.getX(), absPos.getY())) {
                connecting = true;
                connectBox = entryBox;
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

            if (connectBox.isInside(absPos.getX(), absPos.getY())) {
                if (connectBox instanceof GuiStoryNode) {
                    GuiStoryNode node = (GuiStoryNode) connectBox;
                    UserInputGetter.modifyNode(node);
                    GuiStyle.updateOptionPositions(fontMetrics, node);
                    connectBox = null;
                    return;
                }
            }

            for (GuiStoryNode node : guiFolder.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    if (connectBox instanceof GuiEntryBox) {
                        GuiEntryBox entryBox = (GuiEntryBox) connectBox;
                        entryBox.connectOutput(node);
                        node.connectInput(connectBox);
                        connectBox = null;
                        return;
                    }

                    String input = UserInputGetter.getTextFromPromt("Add option", "");
                    if (input != null) {
                        if (connectBox instanceof GuiStoryNode) {
                            GuiStoryNode bindNode = (GuiStoryNode) connectBox;
                            addStoryOption(input, bindNode, node);
                            connectBox = null;
                            return;
                        }
                    } else {
                        connectBox = null;
                        return;
                    }
                }
            }

            GuiExitBox exitBox = guiFolder.getExitBox();
            if (exitBox != null) {
                if (exitBox.isInside(absPos.getX(), absPos.getY())) {
                    connectBox.connectOutput(exitBox);
                    exitBox.connectInput(connectBox);
                    return;
                }
            }

            if (connectBox instanceof GuiStoryNode) {
                GuiStoryNode bindNode = (GuiStoryNode) connectBox;
                String optionInput = UserInputGetter.getTextFromPromt("Adding option...", "");
                if (optionInput != null) {
                    String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
                    if (nodeInput != null) {
                        addStoryOption(optionInput, bindNode, addStoryNode(nodeInput));
                        connectBox = null;
                        return;
                    }
                }
            }
            connectBox = null;
            return;
        }
    }

    public void zoom(int rotations) {
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    public GuiStoryNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        GuiStoryNode node = new GuiStoryNode(text, (int) absPos.getX(), (int) absPos.getY());

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
            for (GuiStoryNode.OptionPair pair : node.getOptionPairs()) {
                GuiStoryOption option = pair.getOption();
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    deleteStoryOption(option);
                    return option;
                }
            }
        }
        return null;
    }

    private GuiStoryNode deleteStoryNode(GuiStoryNode node) {
        for (int i = node.getInputs().size() - 1; i >= 0; i--) {
            GuiBox input = node.getInputs().get(i);
            if (input instanceof GuiStoryOption) {
                GuiStoryOption option = (GuiStoryOption) input;
                deleteStoryOption(option);
            }
        }
        for (int i = node.getOutputs().size() - 1; i >= 0; i--) {
            GuiInputBox output = node.getInputs().get(i);
            GuiStoryOption option = (GuiStoryOption) output;
            deleteStoryOption(option);
        }
        guiFolder.getNodes().remove(node);
        return node;
    }

    private GuiStoryOption deleteStoryOption(GuiStoryOption option) {
        option.getInputs().get(0).disconnectOutput(option);
        option.getOutputs().get(0).disconnectInput(option);
        return option;
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
