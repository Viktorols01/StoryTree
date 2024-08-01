package gui.serializable;

public class GuiEntryBox extends GuiConnectableBox {

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

    @Override
    public void connect(GuiConnectable bindable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connect'");
    }

    @Override
    public void disconnect(GuiConnectable bindable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }
    
}
