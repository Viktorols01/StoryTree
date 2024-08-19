package gui.serializable;

import java.util.ArrayList;
import java.util.List;

import storyclasses.serializable.StoryKey;

public class GuiStoryNode extends GuiInputOutputBox {
    private List<OutputSocket> inputs;
    private List<OptionPair> optionPairs;

    private String text;
    private List<StoryKey> addedKeys;
    private List<StoryKey> removedKeys;

    public GuiStoryNode(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
        this.addedKeys = new ArrayList<StoryKey>();
        this.removedKeys = new ArrayList<StoryKey>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<OptionPair> getOptionPairs() {
        return optionPairs;
    }

    public List<OutputSocket> getInputs() {
        return inputs;
    }

    public class OptionPair {
        private GuiStoryOption option;
        private InputSocket output;

        private OptionPair(InputSocket output) {
            this.option = new GuiStoryOption("");
            this.output = output;
        }

        public GuiStoryOption getOption() {
            return option;
        }

        public InputSocket getOutput() {
            return output;
        }

    }

    @Override
    public void connectOutput(InputSocket connectable) {
        this.optionPairs.add(new OptionPair(connectable));
    }

    @Override
    public void disconnectOutput(InputSocket connectable) {
        this.optionPairs.removeIf(pair -> pair.getOutput().equals(connectable));
    }

    @Override
    public void disconnectOutputs() {
        this.optionPairs.clear();
    }

    @Override
    public void connectInput(OutputSocket connectable) {
        this.inputs.add(connectable);
    }

    @Override
    public void disconnectInput(OutputSocket connectable) {
        this.inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        this.inputs.clear();
    }
}