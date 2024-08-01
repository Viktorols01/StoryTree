package gui.serializable;

import java.util.List;

public class GuiExitBox extends GuiConnectableBox {

    private List<GuiStoryOption> inOptions;

    public GuiExitBox(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public List<GuiStoryOption> getInOptions() {
        return inOptions;
    }

    public void setInOptions(List<GuiStoryOption> inOptions) {
        this.inOptions = inOptions;
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
