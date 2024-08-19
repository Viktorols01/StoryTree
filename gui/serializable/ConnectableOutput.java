package gui.serializable;

public interface ConnectableOutput<T> {

    public abstract void connectOutput(T connectable);

    public abstract void disconnectOutput(T connectable);

    public abstract void disconnectOutputs();

    public abstract Iterable<T> getOutputs();
}
