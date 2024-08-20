package editor.serializable;

public interface OutputInteractible extends Interactible {
    public abstract void connectOutput(InputInteractible connectable);

    public abstract void disconnectOutput(InputInteractible connectable);

    public abstract void disconnectOutputs();
}