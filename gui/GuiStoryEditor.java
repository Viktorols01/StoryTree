package gui;

import javax.swing.JOptionPane;

import tools.Gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public class GuiStoryEditor extends Gui {

    private GuiStoryNode draggedMovable = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private GuiStoryNode bindMovable = null;
    private boolean binding = false;

    private GuiStoryContainer guiContainer;

    public GuiStoryEditor(int width, int height) {
        super(width, height);
        this.guiContainer = new GuiStoryContainer(width, height);

        setFont(new Font("Arial", Font.PLAIN, 10));
    }

    public GuiStoryContainer getGuiContainer() {
        return this.guiContainer;
    }

    public void setGuiContainer(GuiStoryContainer guiContainer) {
        this.guiContainer = guiContainer;
    }

    public void setGuiRoot(GuiStoryNode root) {
        this.guiContainer = new GuiStoryContainer(getWidth(), getHeight());
        this.guiContainer.setGuiRoot(root);
    }

    @Override
    protected void render(Graphics g) {
        g.setColor(new Color(55, 55, 55));
        g.fillRect(0, 0, guiContainer.getCam().getRelativeWidth(), guiContainer.getCam().getRelativeHeight());
        Graphics2D g2d = (Graphics2D) g;
        guiContainer.getCam().transform(g2d);

        if (binding) {
            Point2D absPos = getAbsoluteMousePosition();
            GuiStoryContainer.renderLine(g2d,
                    (int) (bindMovable.getX() + bindMovable.getW() / 2),
                    (int) (bindMovable.getY() + bindMovable.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        if (guiContainer.getGuiNodes().isEmpty()) {
            return;
        }

        for (GuiStoryNode node : guiContainer.getGuiNodes()) {
            GuiStoryContainer.renderOutPointerLines(g2d, node);
        }
        for (GuiStoryNode node : guiContainer.getGuiNodes()) {
            GuiStoryContainer.renderOutPointers(g2d, node);
        }
        for (GuiStoryNode node : guiContainer.getGuiNodes()) {
            if (node == guiContainer.getGuiRoot()) {
                continue;
            }
            GuiStoryContainer.renderGuiTextBox(g2d, node);
        }
        GuiStoryContainer.renderGuiTextBoxAsRoot(g2d, guiContainer.getGuiRoot());
    }

    @Override
    protected void onMouseMoved(MouseEvent e) {
        this.requestFocus();
        this.repaint();
    }

    @Override
    protected void onMouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case 3:
                addStoryNode();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                startDragging();
                break;
            case 3:
                startBinding();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                endDragging();
                break;
            case 3:
                endBinding();
                break;
        }
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        dragging();
        binding();
        this.repaint();
    }

    @Override
    protected void onMouseExited(MouseEvent e) {
    }

    @Override
    protected void onMouseEntered(MouseEvent e) {
    }

    @Override
    protected void onMouseWheelMoved(MouseWheelEvent e) {
        int rotations = e.getWheelRotation();
        this.guiContainer.getCam().setZoom(this.guiContainer.getCam().getZoom() * Math.pow(1.15, -rotations));
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                deleteObject();
                break;
        }
    }

    @Override
    protected void onKeyReleased(KeyEvent e) {
    }

    @Override
    protected void onKeyTyped(KeyEvent e) {
    }

    private String getTextFromPromt(String title, String text) {
        return JOptionPane.showInputDialog(title, text);
    }

    private Point2D getRelativeMousePosition() {
        Point2D relPos = getInput().getMousePosition(0).getLocation();
        return relPos;
    }

    private Point2D getAbsoluteMousePosition() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = guiContainer.getCam().inverseTransform(relPos);
        return absPos;
    }

    private GuiStoryNode addStoryNode() {
        Point2D absPos = getAbsoluteMousePosition();
        String input = getTextFromPromt("Add node", "");
        if (input != null) {
            GuiStoryNode node = new GuiStoryNode(absPos.getX(), absPos.getY());
            node.setText((Graphics2D) getGraphics(), input);

            if (guiContainer.getGuiNodes().isEmpty()) {
                guiContainer.setGuiRoot(node);
            }
            guiContainer.getGuiNodes().add(node);
            return node;
        }
        return null;
    }

    private void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : guiContainer.getGuiNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    if (node == guiContainer.getGuiRoot()) {
                        return;
                    }
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedMovable = node;
                    return;
                }
            }
            dragging = true;
            draggedDelta = new Point2D.Double(
                    -relPos.getX() / guiContainer.getCam().getZoom() - guiContainer.getCam().getX(),
                    -relPos.getY() / guiContainer.getCam().getZoom() - guiContainer.getCam().getY());
            draggedMovable = null;
        }
    }

    private void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (dragging) {
            if (draggedMovable == null) {
                guiContainer.getCam().setX(-relPos.getX() / guiContainer.getCam().getZoom() - draggedDelta.getX());
                guiContainer.getCam().setY(-relPos.getY() / guiContainer.getCam().getZoom() - draggedDelta.getY());
            } else {
                draggedMovable.setPosition(absPos.getX() - draggedDelta.getX(), absPos.getY() - draggedDelta.getY());
            }
        }
    }

    private void endDragging() {
        if (dragging) {
            dragging = false;
            draggedMovable = null;
        }
    }

    private void startBinding() {
        Point2D absPos = getAbsoluteMousePosition();
        if (!binding) {
            for (GuiStoryNode node : guiContainer.getGuiNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    binding = true;
                    bindMovable = node;
                    return;
                }
            }
        }
    }

    private void binding() {
    }

    private void endBinding() {
        Point2D absPos = getAbsoluteMousePosition();

        if (binding) {
            binding = false;

            if (bindMovable.isInside(absPos.getX(), absPos.getY())) {
                String input = getTextFromPromt("Edit text", bindMovable.getText());
                if (input != null) {
                    bindMovable.setText((Graphics2D) getGraphics(), input);
                    bindMovable = null;
                }
                return;
            }

            String optionText = getTextFromPromt("Add option", "");
            if (optionText != null) {
                GuiStoryNode addNode = null;

                for (GuiStoryNode node : guiContainer.getGuiNodes()) {
                    if (node.isInside(absPos.getX(), absPos.getY())) {
                        addNode = node;
                    }
                }

                if (addNode == null) {
                    addNode = addStoryNode();
                }

                bindMovable.addPointer((Graphics2D) getGraphics(), optionText, addNode);
                bindMovable = null;
            }
        }
    }

    private void deleteObject() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : guiContainer.getGuiNodes()) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return;
            }
            for (GuiStoryOption pointer : node.getOutPointers()) {
                if (pointer.isInside(absPos.getX(), absPos.getY())) {
                    pointer.getParent().removePointer(pointer);
                    return;
                }
            }
        }
    }

    private void deleteStoryNode(GuiStoryNode node) {
        if (node == guiContainer.getGuiRoot()) {
            return;
        }
        for (int i = node.getInPointers().size() - 1; i >= 0; i--) {
            GuiStoryOption pointer = node.getInPointers().get(i);
            pointer.getParent().removePointer(pointer);
        }
        for (int i = node.getOutPointers().size() - 1; i >= 0; i--) {
            GuiStoryOption pointer = node.getOutPointers().get(i);
            pointer.getParent().removePointer(pointer);
        }
        guiContainer.getGuiNodes().remove(node);
    }
}
