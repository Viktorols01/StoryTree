package gui.serializable;

public interface InputSocket {
    public abstract void connectInput(OutputSocket connectable);

    public abstract void disconnectInput(OutputSocket connectable);

    public abstract void disconnectInputs();
}
