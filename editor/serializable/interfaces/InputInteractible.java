package editor.serializable.interfaces;

import java.util.List;

public interface InputInteractible extends Interactible {
    public abstract void connectInput(OutputInteractible connectable);
    public abstract void disconnectInput(OutputInteractible connectable);
    public abstract void disconnectInputs();
    public abstract List<OutputInteractible> getInputs();
}