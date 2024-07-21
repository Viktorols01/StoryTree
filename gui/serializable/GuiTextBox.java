package gui.serializable;

public class GuiTextBox extends GuiBox {
    private String text;
    private int lineHeight;
    private int textWidth;
    private int padding;

    public GuiTextBox(final String text, final int x, final int y, final int padding) {
        super(x, y, 0, 0);
        this.text = text;
        this.padding = padding;
    }

    public String getText() {
        return text;
    }

    public int getPadding() {
        return padding;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(final int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(final int textWidth) {
        this.textWidth = textWidth;
    }

    public void setPadding(final int padding) {
        this.padding = padding;
    }    
}
