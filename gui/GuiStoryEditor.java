package gui;

import gui.serializable.GuiStoryFolder;
//import storyclasses.serializable.StoryTree;
import tools.Gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GuiStoryEditor extends Gui {

    private GuiMechanics guiMechanics;
    private Font font;

    public GuiStoryEditor(int width, int height) {
        super(width, height);
        this.font = new Font("Arial", Font.PLAIN, 50);
        setFont(font);
        this.guiMechanics = new GuiMechanics(getInput(), width, height, this.getFontMetrics(font));
    }

    public GuiStoryFolder getGuiFolder() {
        return this.guiMechanics.getGuiFolder();
    }

    public void setGuiFolder(GuiStoryFolder guiFolder) {
        this.guiMechanics.setGuiFolder(guiFolder);
    }

    @Override
    protected void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        guiMechanics.render(g2d);
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
                guiMechanics.startConnecting();
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
                guiMechanics.endConnecting();
                break;
        }
        this.repaint();
    }

    @Override
    protected void onMouseDragged(MouseEvent e) {
        guiMechanics.dragging();
        guiMechanics.connecting();
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
        guiMechanics.zoom(rotations);
    }

    @Override
    protected void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                guiMechanics.deleteBox();
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
