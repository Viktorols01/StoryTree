package editor.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import editor.UserInputGetter;
import storyclasses.serializable.StoryKey;

public class EditorNode extends Box implements IOInteractible, TextInteractible {
    private List<OutputInteractible> inputs;
    private List<OptionPair> optionPairs;

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

    public List<OutputInteractible> getInputs() {
        return inputs;
    }

    public class OptionPair implements Serializable {
        private EditorOption option;
        private InputInteractible output;

        private OptionPair(InputInteractible output) {
            this.option = new EditorOption(UserInputGetter.getTextFromPromt("Adding option...", ""));
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
        this.optionPairs.add(new OptionPair(connectable));
    }

    @Override
    public void disconnectOutput(InputInteractible connectable) {
        this.optionPairs.removeIf(pair -> pair.getOutput().equals(connectable));
    }

    @Override
    public void disconnectOutputs() {
        this.optionPairs.clear();
    }

    @Override
    public void connectInput(OutputInteractible connectable) {
        this.inputs.add(connectable);
    }

    @Override
    public void disconnectInput(OutputInteractible connectable) {
        this.inputs.remove(connectable);
    }

    @Override
    public void disconnectInputs() {
        this.inputs.clear();
    }
}