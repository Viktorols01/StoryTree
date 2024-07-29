package gui;

import java.awt.geom.Point2D;

import gui.serializable.GuiBox;
import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import gui.serializable.GuiTextBox;

public class GuiMechanics {

    private GuiBox draggedBox = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private GuiBox bindBox = null;
    private boolean binding = false;

    private GuiStoryEditor guiStoryEditor;

    public GuiMechanics(GuiStoryEditor guiStoryEditor) {
        this.guiStoryEditor = guiStoryEditor;
    }

    public void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : guiStoryEditor.getGuiFolder().getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedBox = node;
                    return;
                }
            }
            dragging = true;
            draggedDelta = new Point2D.Double(
                    -relPos.getX() / guiStoryEditor.getCamera().getZoom() - guiStoryEditor.getCamera().getX(),
                    -relPos.getY() / guiStoryEditor.getCamera().getZoom() - guiStoryEditor.getCamera().getY());
            draggedBox = null;
        }
    }

    public void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (dragging) {
            if (draggedBox == null) {
                guiStoryEditor.getCamera()
                        .setX(-relPos.getX() / guiStoryEditor.getCamera().getZoom() - draggedDelta.getX());
                guiStoryEditor.getCamera()
                        .setY(-relPos.getY() / guiStoryEditor.getCamera().getZoom() - draggedDelta.getY());
            } else {
                draggedBox.setPosition((int) (absPos.getX() - draggedDelta.getX()),
                        (int) (absPos.getY() - draggedDelta.getY()));
                if (draggedBox instanceof GuiStoryNode) {
                    GuiStyle.updateOptionPositions(guiStoryEditor.getGraphics().getFontMetrics(),
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

    public void startBinding() {
        Point2D absPos = getAbsoluteMousePosition();
        if (!binding) {
            for (GuiStoryNode node : guiStoryEditor.getGuiFolder().getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    binding = true;
                    bindBox = node;
                    return;
                }
                for (GuiStoryOption option : node.getOutOptions()) {
                    if (option.isInside(absPos.getX(), absPos.getY())) {
                        UserInputGetter.modifyOption(option);
                        GuiStyle.updateSize(guiStoryEditor.getGraphics().getFontMetrics(), option);
                        GuiStyle.update(guiStoryEditor.getGraphics().getFontMetrics(), node);
                        return;
                    }
                }
            }
        }
    }

    public void binding() {
    }

    public void endBinding() {
        Point2D absPos = getAbsoluteMousePosition();

        if (binding) {
            binding = false;

            if (bindBox.isInside(absPos.getX(), absPos.getY())) {
                if (bindBox instanceof GuiStoryNode) {
                    GuiStoryNode bindNode = (GuiStoryNode) bindBox;
                    UserInputGetter.modifyNode(bindNode);
                    GuiStyle.update(guiStoryEditor.getGraphics().getFontMetrics(), bindNode);
                    bindBox = null;
                    return;
                }
            } else {
                for (GuiStoryNode node : guiStoryEditor.getGuiFolder().getNodes()) {
                    if (node.isInside(absPos.getX(), absPos.getY())) {
                        String input = UserInputGetter.getTextFromPromt("Add option", "");
                        if (input != null) {
                            if (bindBox instanceof GuiStoryNode) {
                                GuiStoryNode bindNode = (GuiStoryNode) bindBox;
                                addStoryOption(input, bindNode, node);
                                bindBox = null;
                                return;
                            }
                        } else {
                            bindBox = null;
                            return;
                        }
                    }
                }
                String optionInput = UserInputGetter.getTextFromPromt("Adding option...", "");
                if (optionInput != null) {
                    String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
                    if (nodeInput != null) {
                        if (bindBox instanceof GuiStoryNode) {
                            GuiStoryNode bindNode = (GuiStoryNode) bindBox;
                            addStoryOption(optionInput, bindNode, addStoryNode(nodeInput));
                            bindBox = null;
                            return;
                        }
                    }
                } else {
                    bindBox = null;
                    return;
                }
            }
        }
    }

    public GuiStoryOption addStoryOption(String optionText, GuiStoryNode parent, GuiStoryNode child) {
        GuiStoryOption option = new GuiStoryOption(optionText, parent, child);
        GuiStyle.updateSize(guiStoryEditor.getGraphics().getFontMetrics(), option);
        parent.getOutOptions().add(option);
        child.getInOptions().add(option);
        GuiStyle.updateOptionPositions(guiStoryEditor.getGraphics().getFontMetrics(), parent);
        return option;
    }

    public GuiStoryNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        GuiStoryNode node = new GuiStoryNode(text, (int) absPos.getX(), (int) absPos.getY());
        GuiStyle.updateSize(guiStoryEditor.getGraphics().getFontMetrics(), node);

        guiStoryEditor.getGuiFolder().getNodes().add(node);
        return node;
    }

    public GuiTextBox deleteTextBox() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : guiStoryEditor.getGuiFolder().getNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return node;
            }
            for (GuiStoryOption option : node.getOutOptions()) {
                if (option.isInside(absPos.getX(), absPos.getY())) {
                    deleteStoryOption(option);
                    return option;
                }
            }
        }
        return null;
    }

    private GuiStoryNode deleteStoryNode(GuiStoryNode node) {
        for (int i = node.getInOptions().size() - 1; i >= 0; i--) {
            GuiStoryOption option = node.getInOptions().get(i);
            deleteStoryOption(option);
        }
        for (int i = node.getOutOptions().size() - 1; i >= 0; i--) {
            GuiStoryOption option = node.getInOptions().get(i);
            deleteStoryOption(option);
        }
        guiStoryEditor.getGuiFolder().getNodes().remove(node);
        return node;
    }

    private GuiStoryOption deleteStoryOption(GuiStoryOption option) {
        option.getParent().getOutOptions().remove(option);
        option.getChild().getInOptions().remove(option);
        return option;
    }

    public Point2D getRelativeMousePosition() {
        Point2D relPos = guiStoryEditor.getInput().getMousePosition(0).getLocation();
        return relPos;
    }

    public Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = guiStoryEditor.getCamera().inverseTransform(relPos);
        return absPos;
    }

    public GuiBox getDraggedBox() {
        return draggedBox;
    }

    public boolean isDragging() {
        return dragging;
    }

    public Point2D getDraggedDelta() {
        return draggedDelta;
    }

    public GuiBox getBindBox() {
        return bindBox;
    }

    public boolean isBinding() {
        return binding;
    }

}
