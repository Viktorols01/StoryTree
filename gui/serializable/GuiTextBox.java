package gui.serializable;

public class GuiTextBox extends GuiBox {
    private String text;
    private int lineHeight;
    private int textWidth;

    public GuiTextBox(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
    }
}
