package gui;

import gui.serializable.GuiStoryFolder;
import gui.serializable.GuiStoryNode;
import tools.Camera;
import tools.Gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public class GuiStoryEditor extends Gui {

    private Camera camera;

    private GuiMechanics guiMechanics;

    private GuiStoryFolder guiFolder;

    public GuiStoryEditor(int width, int height) {
        super(width, height);
        this.camera = new Camera(width, height);
        this.guiMechanics = new GuiMechanics(this);
        this.guiFolder = new GuiStoryFolder(200);

        setFont(new Font("Arial", Font.PLAIN, 50));
    }

    public GuiStoryFolder getGuiFolder() {
        return this.guiFolder;
    }

    public void setGuiFolder(GuiStoryFolder guiFolder) {
        this.guiFolder = guiFolder;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    protected void render(Graphics g) {
        g.setColor(GuiStyle.COLOR_BACKGROUND);
        g.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
        Graphics2D g2d = (Graphics2D) g;
        camera.transform(g2d);

        if (guiMechanics.isBinding()) {
            Point2D absPos = guiMechanics.getAbsoluteMousePosition();
            GuiStyle.renderLine(g2d,
                    (int) (guiMechanics.getBindBox().getX() + guiMechanics.getBindBox().getW() / 2),
                    (int) (guiMechanics.getBindBox().getY() + guiMechanics.getBindBox().getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderOutOptionLines(g2d, node);
        }
        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderOutoptions(g2d, node);
        }
        for (GuiStoryNode node : guiFolder.getNodes()) {
            GuiStyle.renderStoryNode(g2d, node, false);
        }

        GuiStyle.renderEntryBox(g2d, guiFolder.getEntryBox());
        if (guiFolder.getExitBox() != null) {
            GuiStyle.renderExitBox(g2d, guiFolder.getExitBox());
        }
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
                    guiMechanics.addStoryNode(nodeInput);
                }
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                guiMechanics.startDragging();
                break;
            case 3:
                guiMechanics.startBinding();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case 1:
                guiMechanics.endDragging();
                break;
            case 3:
                guiMechanics.endBinding();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        guiMechanics.dragging();
        guiMechanics.binding();
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
        camera.setZoom(camera.getZoom() * Math.pow(1.15, -rotations));
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                guiMechanics.deleteTextBox();
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
