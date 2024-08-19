package gui.serializable;

public interface OutputSocket {
    public abstract void connectOutput(InputSocket connectable);

    public abstract void disconnectOutput(InputSocket connectable);

    public abstract void disconnectOutputs();
}
