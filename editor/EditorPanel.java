package editor;

//import storyclasses.serializable.StoryTree;
import tools.InterfacePanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.serializable.EditorFolder;

public class EditorPanel extends InterfacePanel {

    private EditorController controller;
    private Font font;

    public EditorPanel(int width, int height) {
        super(width, height);
        this.font = new Font("Arial", Font.PLAIN, 50);
        setFont(font);
        this.controller = new EditorController(getInput(), width, height, this.getFontMetrics(font));
    }

    public EditorFolder getGuiFolder() {
        return this.controller.getGuiFolder();
    }

    public void setGuiFolder(EditorFolder guiFolder) {
        this.controller.setGuiFolder(guiFolder);
    }

    @Override
    protected void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        controller.render(g2d);
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
                String nodeInput = UserInputGetter.getTextFromPromt("Adding node...", "");
                if (nodeInput != null) {
                    controller.addStoryNode(nodeInput);
                }
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                controller.startDragging();
                break;
            case 3:
                controller.startConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                controller.endDragging();
                break;
            case 3:
                controller.endConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        controller.dragging();
        controller.connecting();
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
        controller.zoom(rotations);
        this.repaint();
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                controller.deleteBox();
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

    // public StoryTree toStoryTree() {
    //     return GuiSerializer.toStoryTree(guiMechanics.getGuiFolder());
    // }
}
