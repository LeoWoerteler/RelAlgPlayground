package kn.uni.dbis.relalg.model;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import kn.uni.dbis.relalg.OperatorType;
import kn.uni.dbis.relalg.ViewObserver;

public final class DataModel {
    private final List<OperatorModel> operators = new ArrayList<>();
    private ViewObserver view = null;

    public OperatorModel addOperator(final OperatorType type, final Point pos) {
        final int idx = this.operators.size();
        final OperatorModel model = type.newInstance(this.view, idx);
        operators.add(model);
        this.view.operatorAdded(model, pos);
        return model;
    }

    public boolean addConnection(final OperatorModel consumer, final int pos, final OperatorModel input) {
        if (this.isConnected(input, pos, consumer)) {
            return false;
        }
        final OperatorModel oldInput = consumer.getInput(pos);
        if (oldInput != null) {
            this.removeConnection(consumer, pos, oldInput);
        }
        final OperatorModel oldConsumer = input.getConsumer();
        if (oldConsumer != null) {
            this.removeConnection(oldConsumer, input.getConsumerPos(), input);
        }
        consumer.setInput(pos, input);
        input.setConsumer(consumer, pos);
        this.view.connectionAdded(consumer.getIndex(), pos, input.getIndex());
        return true;
    }

    private void removeConnection(final OperatorModel consumer, final int pos, final OperatorModel input) {
        input.setConsumer(null, -1);
        consumer.setInput(pos, null);
        this.view.connectionRemoved(consumer.getIndex(), pos);
    }

    private boolean isConnected(final OperatorModel input, final int pos, final OperatorModel consumer) {
        final int target = consumer.getIndex();
        final BitSet seen = new BitSet();
        final Deque<OperatorModel> stack = new ArrayDeque<>(Collections.singleton(input));
        seen.set(input.getIndex());

        while (!stack.isEmpty()) {
            final OperatorModel curr = stack.pop();
            final int idx = curr.getIndex();
            if (idx == target) {
                return true;
            }
            for (int i = curr.getNumInputs(); --i >= 0;) {
                final OperatorModel sub = curr.getInput(i);
                if (sub != null) {
                    final int sidx = sub.getIndex();
                    if (seen.get(sidx)) {
                        continue;
                    }
                    seen.set(sidx);
                    stack.push(sub);
                }
            }
        }
        return false;
    }

    public void register(final ViewObserver observer) {
        this.view = observer;
    }

    public void setJoinType(final int index, final JoinModel.Type type) {
        final OperatorModel model = this.operators.get(index);
        if (!(model instanceof JoinModel)) {
            throw new IllegalStateException("Expexted join model, found " + model);
        }
        ((JoinModel) model).setType(type);
    }
}
