package kn.uni.dbis.relalg;

import java.awt.Point;

import kn.uni.dbis.relalg.model.JoinModel;
import kn.uni.dbis.relalg.model.OperatorModel;
import kn.uni.dbis.relalg.view.OperatorView;

public interface ViewObserver {

    void connectionRemoved(int parIdx, int pos);

    void connectionAdded(int parIdx, int pos, int childIdx);

    void operatorAdded(OperatorModel model, Point pos);

    void operatorPositionChanged(OperatorView operatorView);

    void typeChanged(int index, JoinModel.Type type);
}
