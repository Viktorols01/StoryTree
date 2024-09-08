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

    private Font font;
    private EditorContext context;

    public EditorPanel(int width, int height) {
        super(width, height);
        this.font = new Font("Arial", Font.PLAIN, Constants.FONT_SIZE);
        this.context = new EditorContext(width, height, getInput(), getFontMetrics(font));
        setFont(font);
    }

    public EditorFolder getGuiFolder() {
        return context.getEditorFolder();
    }

    public void setGuiFolder(EditorFolder guiFolder) {
        context.setEditorFolder(guiFolder);
    }

    @Override
    protected void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderUtility.renderEditorContext(g2d, context);
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
                context.addEditorNode();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                context.startDragging();
                break;
            case 3:
                context.startConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                context.endDragging();
                break;
            case 3:
                context.endConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        context.updateDragging();
        context.updateConnecting();
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
        context.zoom(rotations);
        this.repaint();
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                context.deleteInteractible();
                break;
            case KeyEvent.VK_F:
                context.addEditorFolder();
                break;
            case KeyEvent.VK_I:
                context.enterEditorFolder();
                break;
            case KeyEvent.VK_O:
                context.exitEditorFolder();
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
}
