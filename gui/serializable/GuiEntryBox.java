package gui.serializable;

public class GuiEntryBox extends GuiBox {

    private GuiStoryNode outNode;

    public GuiEntryBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public GuiStoryNode getOutNode() {
        return outNode;
    }

    public void setOutNode(GuiStoryNode outNode) {
        this.outNode = outNode;
    }
    
}
