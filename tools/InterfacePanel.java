package tools;

import javax.swing.JPanel;

import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import java.awt.Dimension;
import java.awt.Point;

// updated 2024-08-20
// kanske ska flytta in JPanel inuti GUI? Jobbigt att använda nu för det är så många funktioner...
public abstract class InterfacePanel extends JPanel implements MouseInputListener, MouseWheelListener, KeyListener {

    private Input input;

    private int width;
    private int height;

    public InterfacePanel(int width, int height) {
        super(true);
        this.input = new Input();
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
    }

    protected abstract void render(Graphics g);

    protected abstract void onMouseMoved(MouseEvent e);

    protected abstract void onMouseClicked(MouseEvent e);

    protected abstract void onMousePressed(MouseEvent e);

    protected abstract void onMouseReleased(MouseEvent e);

    protected abstract void onMouseDragged(MouseEvent e);

    protected abstract void onMouseExited(MouseEvent e);

    protected abstract void onMouseEntered(MouseEvent e);

    protected abstract void onMouseWheelMoved(MouseWheelEvent e);

    protected abstract void onKeyPressed(KeyEvent e);

    protected abstract void onKeyReleased(KeyEvent e);

    protected abstract void onKeyTyped(KeyEvent e);

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Input getInput() {
        return this.input;
    }

    public class Input {

        private final int mouseButtonCount = 4;
        private final MouseButton[] mouseButtons = new MouseButton[mouseButtonCount];

        private final int keyCount = 256;
        private final Key[] keys = new Key[keyCount];

        private Input() {
            for (int i = 0; i < keys.length; i++) {
                keys[i] = new Key();
            }
            for (int i = 0; i < mouseButtons.length; i++) {
                mouseButtons[i] = new MouseButton();
            }
        }

        private MouseButton getMouseButton(int button) {
            return this.mouseButtons[button < mouseButtonCount ? button : 0];
        }

        private Key getKey(int keyCode) {
            return keys[keyCode < keyCount ? keyCode : 0];
        }

        public Point getMousePosition(int button) {
            return this.getMouseButton(button).position;
        }

        private void setMousePosition(int button, Point position) {
            this.getMouseButton(button).position = position;
        }

        public boolean isMousePressed(int button) {
            return this.getMouseButton(button).pressed;
        }

        private void setMousePressed(int button, boolean pressed) {
            this.getMouseButton(button).pressed = pressed;
        }

        public boolean isKeyPressed(int keyCode) {
            return this.getKey(keyCode).pressed;
        }

        private void setKeyPressed(int keyCode, boolean pressed) {
            this.getKey(keyCode).pressed = pressed;
        }

        private class MouseButton {
            private Point position = new Point(0, 0);
            private boolean pressed = false;

            public MouseButton() {
                this.pressed = false;
            }
        }

        private class Key {
            private boolean pressed;

            public Key() {
                this.pressed = false;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        input.setMousePosition(e.getButton(), getMousePosition());
        onMouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        input.setMousePressed(e.getButton(), true);
        onMousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        input.setMousePressed(e.getButton(), false);
        onMouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        onMouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        input.setMousePressed(e.getButton(), false);
        onMouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        input.setMousePosition(e.getButton(), getMousePosition());
        onMouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        input.setMousePosition(e.getButton(), getMousePosition());
        onMouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        onMouseWheelMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        input.setKeyPressed(e.getKeyCode(), true);
        onKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        input.setKeyPressed(e.getKeyCode(), false);
        onKeyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        onKeyTyped(e);
    }
}