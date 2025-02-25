package editor.serializable.interfaces;

import java.util.List;

public interface OutputInteractible extends Interactible {
    public abstract void connectOutput(InputInteractible connectable);
    public abstract void disconnectOutput(InputInteractible connectable);
    public abstract void disconnectOutputs();
    public abstract List<InputInteractible> getOutputs();
}