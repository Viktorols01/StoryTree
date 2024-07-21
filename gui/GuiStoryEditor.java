package gui;

import javax.swing.JOptionPane;

import gui.serializable.GuiStoryNode;
import gui.serializable.GuiStoryOption;
import gui.serializable.GuiTextBox;
import tools.Gui;

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
        this.guiContainer.loadRoot(root);
    }

    @Override
    protected void render(Graphics g) {
        g.setColor(GuiStyle.COLOR_BACKGROUND);
        g.fillRect(0, 0, guiContainer.getCamera().getRelativeWidth(), guiContainer.getCamera().getRelativeHeight());
        Graphics2D g2d = (Graphics2D) g;
        guiContainer.getCamera().transform(g2d);

        if (binding) {
            Point2D absPos = getAbsoluteMousePosition();
            GuiStyle.renderLine(g2d,
                    (int) (bindMovable.getX() + bindMovable.getW() / 2),
                    (int) (bindMovable.getY() + bindMovable.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        if (guiContainer.getNodes().isEmpty()) {
            return;
        }

        for (GuiStoryNode node : guiContainer.getNodes()) {
            GuiStyle.renderOutOptionLines(g2d, node);
        }
        for (GuiStoryNode node : guiContainer.getNodes()) {
            GuiStyle.renderOutoptions(g2d, node);
        }
        for (GuiStoryNode node : guiContainer.getNodes()) {
            if (node == guiContainer.getRoot()) {
                continue;
            }
            GuiStyle.renderStoryNode(g2d, node, false);
        }
        GuiStyle.renderStoryNode(g2d, guiContainer.getRoot(), true);
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
                String nodeInput = getTextFromPromt("Adding node...", "");
                if (nodeInput != null) {
                    addStoryNode(nodeInput);
                }
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
        this.repaint();
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
        this.guiContainer.getCamera().setZoom(this.guiContainer.getCamera().getZoom() * Math.pow(1.15, -rotations));
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                deleteTextBox();
                break;
        }
        this.repaint();
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
        Point2D absPos = guiContainer.getCamera().inverseTransform(relPos);
        return absPos;
    }

    private void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : guiContainer.getNodes()) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    if (node == guiContainer.getRoot()) {
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
                    -relPos.getX() / guiContainer.getCamera().getZoom() - guiContainer.getCamera().getX(),
                    -relPos.getY() / guiContainer.getCamera().getZoom() - guiContainer.getCamera().getY());
            draggedMovable = null;
        }
    }

    private void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (dragging) {
            if (draggedMovable == null) {
                guiContainer.getCamera()
                        .setX(-relPos.getX() / guiContainer.getCamera().getZoom() - draggedDelta.getX());
                guiContainer.getCamera()
                        .setY(-relPos.getY() / guiContainer.getCamera().getZoom() - draggedDelta.getY());
            } else {
                draggedMovable.setPosition((int) (absPos.getX() - draggedDelta.getX()),
                        (int) (absPos.getY() - draggedDelta.getY()));
                GuiStoryContainer.updateOptionPositions(getGraphics().getFontMetrics(), draggedMovable);
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
            for (GuiStoryNode node : guiContainer.getNodes()) {
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
                    bindMovable.setText(input);
                    GuiStoryContainer.updateSize(getGraphics().getFontMetrics(), bindMovable);
                    bindMovable = null;
                }
                return;
            } else {
                for (GuiStoryNode node : guiContainer.getNodes()) {
                    if (node.isInside(absPos.getX(), absPos.getY())) {
                        if (node == guiContainer.getRoot()) {
                            bindMovable = null;
                            return;
                        }

                        String input = getTextFromPromt("Add option", bindMovable.getText());
                        if (input != null) {
                            addStoryOption(input, bindMovable, node);
                            bindMovable = null;
                            return;
                        } else {
                            bindMovable = null;
                            return;
                        }
                    }
                }
                String optionInput = getTextFromPromt("Adding option...", bindMovable.getText());
                String nodeInput = getTextFromPromt("Adding node...", "");
                if (optionInput != null && nodeInput != null) {
                    addStoryOption(optionInput, bindMovable, addStoryNode(nodeInput));
                    bindMovable = null;
                    return;
                } else {
                    bindMovable = null;
                    return;
                }
            }
        }
    }

    private GuiStoryOption addStoryOption(String optionText, GuiStoryNode parent, GuiStoryNode child) {
        GuiStoryOption option = new GuiStoryOption(optionText, parent, child);
        GuiStoryContainer.updateSize(getGraphics().getFontMetrics(), option);
        parent.getOutOptions().add(option);
        child.getInOptions().add(option);
        GuiStoryContainer.updateOptionPositions(getGraphics().getFontMetrics(), parent);
        return option;
    }

    private GuiStoryNode addStoryNode(String text) {
        Point2D absPos = getAbsoluteMousePosition();
        GuiStoryNode node = new GuiStoryNode(text, (int) absPos.getX(), (int) absPos.getY());
        GuiStoryContainer.updateSize(getGraphics().getFontMetrics(), node);

        if (guiContainer.getNodes().isEmpty()) {
            guiContainer.loadRoot(node);
        }
        guiContainer.getNodes().add(node);
        return node;
    }

    private GuiTextBox deleteTextBox() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : guiContainer.getNodes()) {
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
        if (node == guiContainer.getRoot()) {
            return null;
        }
        for (int i = node.getInOptions().size() - 1; i >= 0; i--) {
            GuiStoryOption option = node.getInOptions().get(i);
            deleteStoryOption(option);
        }
        for (int i = node.getOutOptions().size() - 1; i >= 0; i--) {
            GuiStoryOption option = node.getInOptions().get(i);
            deleteStoryOption(option);
        }
        guiContainer.getNodes().remove(node);
        return node;
    }

    private GuiStoryOption deleteStoryOption(GuiStoryOption option) {
        option.getParent().getOutOptions().remove(option);
        option.getChild().getInOptions().remove(option);
        return option;
    }
}
