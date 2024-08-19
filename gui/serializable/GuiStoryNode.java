package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryNode extends GuiTextBox implements ConnectableInput<GuiBox>, ConnectableOutput<GuiBox> {
    private List<GuiBox> inputs;
    private List<OptionPair> optionPairs;

    private List<StoryKey> addedKeys;
    private List<StoryKey> removedKeys;

    public GuiStoryNode(String text, int x, int y) {
        super(text, x, y);
        this.addedKeys = new ArrayList<StoryKey>();
        this.removedKeys = new ArrayList<StoryKey>();
    }

    public List<StoryKey> getAddedKeys() {
        return addedKeys;
    }

    public List<StoryKey> getRemovedKeys() {
        return removedKeys;
    }

    public void setAddedKeys(List<StoryKey> addedKeys) {
        this.addedKeys = addedKeys;
    }

    public void setRemovedKeys(List<StoryKey> removedKeys) {
        this.removedKeys = removedKeys;
    }

    public List<OptionPair> geOptionPairs() {
        return optionPairs;
    }

    public class OptionPair {
        GuiStoryOption option;
        GuiBox connectable;

        OptionPair(GuiBox connectable) {
            this.option = new GuiStoryOption("");
            this.connectable = connectable;
        }
    }

    @Override
    public void connectOutput(GuiBox connectable) {
        this.optionPairs.add(new OptionPair(connectable));
    }

    @Override
    public void disconnectOutput(GuiBox connectable) {
        this.optionPairs.removeIf(pair -> pair.connectable.equals(connectable));
    }

    @Override
    public void disconnectOutputs() {
        this.optionPairs.clear();
    }

    @Override
    public Iterable<GuiBox> getOutputs() {
        List<GuiBox> list = new ArrayList<GuiBox>();
        for (OptionPair pair : optionPairs) {
            list.add(pair.connectable);
        }
        return list;
    }

    @Override
    public void connectInput(GuiBox connectable) {
        this.inputs.add(connectable);
    }

    @Override
    public void disconnectInput(GuiBox connectable) {
        this.inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        this.inputs.clear();
    }

    @Override
    public Iterable<GuiBox> getInputs() {
        return this.inputs;
    }
}