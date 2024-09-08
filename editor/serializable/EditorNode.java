package editor.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.OutputInteractible;
import editor.serializable.interfaces.TextInteractible;
import storyclasses.serializable.StoryKey;

public class EditorNode extends Box implements InputInteractible, OutputInteractible, TextInteractible {
    private List<OutputInteractible> inputs;
    private List<OptionPair> optionPairs;
    private EditorExtraNode extraNode;

    private String text;
    private List<StoryKey> addedKeys;
    private List<StoryKey> removedKeys;

    public EditorNode(String text, int x, int y) {
        super(x, y, 0, 0);
        this.text = text;
        this.addedKeys = new ArrayList<StoryKey>();
        this.removedKeys = new ArrayList<StoryKey>();

        this.inputs = new ArrayList<OutputInteractible>();
        this.optionPairs = new ArrayList<OptionPair>();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
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

    public EditorExtraNode getExtraNode() {
        return extraNode;
    }

    public void setExtraNode(EditorExtraNode extraNode) {
        this.extraNode = extraNode;
    }

    public class OptionPair implements Serializable {
        private EditorOption option;
        private InputInteractible output;

        private OptionPair(EditorOption option, InputInteractible output) {
            this.option = option;
            this.output = output;
        }

        public EditorOption getOption() {
            return option;
        }

        public InputInteractible getOutput() {
            return output;
        }

    }

    @Override
    public void connectOutput(InputInteractible connectable) {
        optionPairs.add(new OptionPair(new EditorOption(""), connectable));
    }

    @Override
    public void disconnectOutput(InputInteractible connectable) {
        optionPairs.removeIf(pair -> pair.getOutput().equals(connectable));
    }

    @Override
    public void disconnectOutputs() {
        optionPairs.clear();
    }

    @Override
    public void connectInput(OutputInteractible connectable) {
        inputs.add(connectable);
    }

    @Override
    public void disconnectInput(OutputInteractible connectable) {
        inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        inputs.clear();
    }

    @Override
    public List<OutputInteractible> getInputs() {
        return inputs;
    }

    @Override
    public List<InputInteractible> getOutputs() {
        List<InputInteractible> outputs = new ArrayList<InputInteractible>();
        for (OptionPair pair : optionPairs) {
            outputs.add(pair.getOutput());
        }
        return outputs;
    }
}