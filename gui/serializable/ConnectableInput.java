package gui.serializable;

public interface ConnectableInput<T> {

    public abstract void connectInput(T connectable);

    public abstract void disconnectInput(T connectable);

    public abstract void disconnectInputs();

    public abstract Iterable<T> getInputs();

}
