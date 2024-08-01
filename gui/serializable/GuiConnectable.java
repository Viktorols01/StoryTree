package gui.serializable;

public interface GuiConnectable {
    public void connect(GuiConnectable bindable);
    public void disconnect(GuiConnectable bindable);
}
