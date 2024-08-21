package editor;

import tools.Camera;
//import storyclasses.serializable.StoryTree;
import tools.InterfacePanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import editor.serializable.EditorFolder;

public class EditorPanel extends InterfacePanel {

    private EditorContainer container;
    private Camera camera;
    private Font font;

    public EditorPanel(int width, int height) {
        super(width, height);
        this.font = new Font("Arial", Font.PLAIN, 50);
        setFont(font);
        this.container = new EditorContainer(getInput(), width, height, this.getFontMetrics(font));
    }

    public EditorFolder getGuiFolder() {
        return this.container.getGuiFolder();
    }

    public void setGuiFolder(EditorFolder guiFolder) {
        this.container.setGuiFolder(guiFolder);
    }

    @Override
    protected void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(EditorConstants.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
        camera.transform(g2d);
        EditorRender.renderEditorContainer(g2d, container);
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
                    container.addStoryNode(nodeInput);
                }
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                container.startDragging();
                break;
            case 3:
                container.startConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                container.endDragging();
                break;
            case 3:
                container.endConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        container.updateDragging();
        container.updateConnecting();
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
        zoom(rotations);
        this.repaint();
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                container.deleteInteractible();
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

    public Point2D getRelativeMousePosition() {
        Point2D relPos = getInput().getMousePosition(0).getLocation();
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
}
