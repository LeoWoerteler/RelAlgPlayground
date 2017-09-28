package kn.uni.dbis.relalg.model;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.view.OperatorView;
import kn.uni.dbis.relalg.view.Selection;

public class SelectionModel extends OperatorModel {
    public SelectionModel(final ViewObserver observer, final int index) {
        super(observer, index, 1);
    }

    @Override
    public OperatorView createView(final Controller ctrl) {
        return new Selection(ctrl, this.getObserver(), this.getIndex());
    }
}
