package kn.uni.dbis.relalg.model;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.view.Join;
import kn.uni.dbis.relalg.view.OperatorView;

public class JoinModel extends OperatorModel {

    public enum Type {
        CROSS_JOIN,
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN,
        FULL_OUTER_JOIN,
        LEFT_SEMIJOIN,
        RIGHT_SEMIJOIN,
        LEFT_ANTIJOIN,
        RIGHT_ANTIJOIN;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            final String[] parts = this.name().split("_");
            for (final String part : parts) {
                sb.append(' ').append(part.charAt(0)).append(part.substring(1).toLowerCase());
            }
            return sb.substring(1);
        }
    }

    private Type joinType;

    public JoinModel(final ViewObserver observer, final int index, final Type type) {
        super(observer, index, 2);
        this.joinType = type;
    }

    @Override
    public OperatorView createView(final Controller ctrl) {
        return new Join(ctrl, this.getObserver(), this.getIndex(), Type.INNER_JOIN);
    }

    void setType(final Type type) {
        this.joinType = type;
        this.getObserver().typeChanged(this.getIndex(), type);
    }
}
