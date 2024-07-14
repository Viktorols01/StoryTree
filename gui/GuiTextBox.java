package gui;

public class GuiTextBox extends GuiBox {
    private String text;
    private double lineHeight;
    private double padding;

    public GuiTextBox(final String text, final double x, final double y, final double padding) {
        super(x, y, 0, 0);
        this.text = text;
        this.padding = padding;
    }

    public String getText() {
        return text;
    }

    public double getPadding() {
        return padding;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(final double lineHeight) {
        this.lineHeight = lineHeight;
    }

    public void setPadding(final double padding) {
        this.padding = padding;
    }    
}
