package kn.uni.dbis.relalg.model;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.view.InputRelation;
import kn.uni.dbis.relalg.view.OperatorView;

public class RelationModel extends OperatorModel {
    public RelationModel(final ViewObserver observer, final int index) {
        super(observer, index, 0);
    }

    @Override
    public OperatorView createView(final Controller ctrl) {
        return new InputRelation(ctrl, this.getObserver(), this.getIndex());
    }
}
