package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import storyclasses.StoryNode;
import tools.Camera;
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

    private Camera cam;

    private GuiStoryNode draggedMovable = null;
    private boolean dragging = false;
    private Point2D draggedDelta = null;

    private GuiStoryNode bindMovable = null;
    private boolean binding = false;

    private GuiStoryNode root;
    private List<GuiStoryNode> nodes;

    public GuiStoryEditor(int width, int height) {
        super(width, height);
        this.cam = new Camera(width, height);
        this.nodes = new ArrayList<GuiStoryNode>();

        setFont(new Font("Arial", Font.PLAIN, 10));
    }

    public GuiStoryNode getGuiRoot() {
        return this.root;
    }

    public void loadGuiRoot(GuiStoryNode root) {
        this.cam.reset();
        this.nodes = new ArrayList<GuiStoryNode>();
        this.root = root;
        loadGuiStoryNode(root);
    }

    private void loadGuiStoryNode(GuiStoryNode node) {
        this.nodes.add(node);
        for (GuiStoryOptionPointer pointer : node.getOutPointers()) {
            GuiStoryNode child = pointer.getChild();
            if (!this.nodes.contains(child)) {
                loadGuiStoryNode(child);
            }
        }
    }

    public StoryNode getStoryTree() {
        List<StoryNode> serializedNodes = new ArrayList<StoryNode>();
        StoryNode serializedRoot = null;
        for (GuiStoryNode guiNode : nodes) {
            StoryNode serializedNode = new StoryNode(guiNode.getText());
            if (guiNode == root) {
                serializedRoot = serializedNode;
            }
            serializedNodes.add(serializedNode);
        }

        for (int i = 0; i < nodes.size(); i++) {
            GuiStoryNode guiNode = nodes.get(i);
            StoryNode serializedNode = serializedNodes.get(i);

            for (GuiStoryOptionPointer pointer : guiNode.getOutPointers()) {
                int index = nodes.indexOf(pointer.getChild());
                for (String optionText : pointer.getOptionTexts()) {
                    serializedNode.AddNode(optionText, serializedNodes.get(index));
                }
            }
        }
        return serializedRoot;
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
        Point2D absPos = cam.inverseTransform(relPos);
        return absPos;
    }

    private GuiStoryNode addStoryNode() {
        Point2D absPos = getAbsoluteMousePosition();
        String input = getTextFromPromt("Add node", "");
        if (input != null) {
            GuiStoryNode node = new GuiStoryNode(absPos.getX(), absPos.getY());
            node.setText((Graphics2D) getGraphics(), input);

            if (nodes.isEmpty()) {
                root = node;
            }
            nodes.add(node);
            return node;
        }
        return null;
    }

    private void startDragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (!dragging) {
            for (GuiStoryNode node : nodes) {
                if (node.isInside(absPos.getX(), absPos.getY())) {
                    if (node == root) {
                        return;
                    }
                    dragging = true;
                    draggedDelta = new Point2D.Double(absPos.getX() - node.getX(), absPos.getY() - node.getY());
                    draggedMovable = node;
                    return;
                }
            }
            dragging = true;
            draggedDelta = new Point2D.Double(-relPos.getX() / cam.getZoom() - cam.getX(),
                    -relPos.getY() / cam.getZoom() - cam.getY());
            draggedMovable = null;
        }
    }

    private void dragging() {
        Point2D relPos = getRelativeMousePosition();
        Point2D absPos = getAbsoluteMousePosition();
        if (dragging) {
            if (draggedMovable == null) {
                cam.setX(-relPos.getX() / cam.getZoom() - draggedDelta.getX());
                cam.setY(-relPos.getY() / cam.getZoom() - draggedDelta.getY());
            } else {
                draggedMovable.setPosition(absPos.getX() - draggedDelta.getX(), absPos.getY() - draggedDelta.getY());
                draggedMovable.updatePositions();
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
            for (GuiStoryNode node : nodes) {
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
                    bindMovable.updatePositions();
                }
                return;
            }

            String optionText = getTextFromPromt("Add option", "");
            if (optionText != null) {
                for (GuiStoryNode node : nodes) {
                    if (node.isInside(absPos.getX(), absPos.getY())) {
                        for (GuiStoryOptionPointer pointer : bindMovable.getOutPointers()) {
                            if (pointer.getChild() == node) {
                                pointer.addOptionText((Graphics2D) getGraphics(), optionText);
                                bindMovable.updatePositions();
                                bindMovable = null;
                                return;
                            }
                        }
                        GuiStoryOptionPointer pointer = bindMovable.addPointer(node);
                        pointer.addOptionText((Graphics2D) getGraphics(), optionText);
                        bindMovable.updatePositions();
                        bindMovable = null;
                        return;
                    }
                }

                GuiStoryNode node = addStoryNode();
                if (node != null) {
                    GuiStoryOptionPointer pointer = bindMovable.addPointer(node);
                    pointer.addOptionText((Graphics2D) getGraphics(), optionText);
                    bindMovable.updatePositions();
                    bindMovable = null;
                    return;
                }
            }
        }
    }

    private void deleteObject() {
        Point2D absPos = getAbsoluteMousePosition();
        for (GuiStoryNode node : nodes) {
            if (node.isInside(absPos.getX(), absPos.getY())) {
                deleteStoryNode(node);
                return;
            }
            for (GuiStoryOptionPointer pointer : node.getOutPointers()) {
                if (pointer.isInside(absPos.getX(), absPos.getY())) {
                    System.out.println("AAAAA");
                    pointer.remove();
                    return;
                }
            }
        }
    }

    private void deleteStoryNode(GuiStoryNode node) {
        if (node == root) {
            return;
        }
        System.out.println("AAAA");
        for (int i = node.getInPointers().size() - 1; i >= 0; i--) {
            GuiStoryOptionPointer pointer = node.getInPointers().get(i);
            pointer.remove();
        }
        for (int i = node.getOutPointers().size() - 1; i >= 0; i--) {
            GuiStoryOptionPointer pointer = node.getOutPointers().get(i);
            pointer.remove();
        }
        nodes.remove(node);
    }

    @Override
    protected void render(Graphics g) {
        g.setColor(new Color(55, 55, 55));
        g.fillRect(0, 0, cam.getRelativeWidth(), cam.getRelativeHeight());
        Graphics2D g2d = (Graphics2D) g;
        cam.transform(g2d);

        if (binding) {
            Point2D absPos = getAbsoluteMousePosition();
            GuiRenderFunctions.renderLine(g2d,
                    (int) (bindMovable.getX() + bindMovable.getW() / 2),
                    (int) (bindMovable.getY() + bindMovable.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        if (nodes.isEmpty()) {
            return;
        }

        for (GuiStoryNode node : nodes) {
            node.renderOutPointerLines(g2d);
        }
        for (GuiStoryNode node : nodes) {
            node.renderOutPointers(g2d);
        }
        for (GuiStoryNode node : nodes) {
            if (node == root) {
                continue;
            }
            node.render(g2d);
        }
        root.renderAsRoot(g2d);
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
        this.cam.setZoom(cam.getZoom() * Math.pow(1.15, -rotations));
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
}
