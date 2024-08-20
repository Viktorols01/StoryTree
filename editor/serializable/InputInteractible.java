package editor.serializable;

import java.util.Collection;

public interface InputInteractible extends Interactible {
    public abstract void connectInput(OutputInteractible connectable);

    public abstract void disconnectInput(OutputInteractible connectable);

    public abstract void disconnectInputs();

    public abstract Collection<OutputInteractible> getInputs();
}