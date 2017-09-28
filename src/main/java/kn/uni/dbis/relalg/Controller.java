package kn.uni.dbis.relalg;

import java.awt.Point;

import kn.uni.dbis.relalg.model.DataModel;
import kn.uni.dbis.relalg.model.JoinModel;

public class Controller {

    private final DataModel model;

    public Controller(final DataModel model) {
        this.model = model;
    }

    public void setJoinType(final int index, final JoinModel.Type type) {
        model.setJoinType(index, type);
    }

    public void addOperator(final OperatorType type, final Point pos) {
        model.addOperator(type, pos);
    }
}
