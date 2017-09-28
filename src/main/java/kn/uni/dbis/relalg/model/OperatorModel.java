package kn.uni.dbis.relalg.model;

import java.util.ArrayList;
import java.util.List;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.view.OperatorView;

public abstract class OperatorModel {
    private OperatorState state = OperatorState.UNINITIALIZED;
    private final StringBuilder info = new StringBuilder();
    private final List<String[]> result = new ArrayList<>();
    private final int index;
    private final OperatorModel[] inputs;
    private OperatorModel consumer;
    private int consumerPos = -1;
    private final ViewObserver observer;

    OperatorModel(final ViewObserver observer, final int index, final int numInputs) {
        this.index = index;
        this.inputs = new OperatorModel[numInputs];
        this.observer = observer;
    }

    ViewObserver getObserver() {
        return this.observer;
    }

    public int getIndex() {
        return this.index;
    }

    public int getNumInputs() {
        return this.inputs.length;
    }

    public OperatorModel getInput(final int i) {
        return this.inputs[i];
    }

    public void setConsumer(final OperatorModel model, final int pos) {
        this.consumer = model;
        this.consumerPos = pos;
    }

    public OperatorModel getConsumer() {
        return this.consumer;
    }

    public int getConsumerPos() {
        return this.consumerPos;
    }

    public void setInput(final int pos, final OperatorModel input) {
        this.inputs[pos] = input;
    }

    public abstract OperatorView createView(Controller ctrl);
}
