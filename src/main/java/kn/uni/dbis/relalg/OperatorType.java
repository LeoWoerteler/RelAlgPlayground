package kn.uni.dbis.relalg;

import kn.uni.dbis.relalg.model.JoinModel;
import kn.uni.dbis.relalg.model.OperatorModel;
import kn.uni.dbis.relalg.model.ProjectionModel;
import kn.uni.dbis.relalg.model.RelationModel;
import kn.uni.dbis.relalg.model.SelectionModel;

public enum OperatorType {
    JOIN {
        @Override
        public OperatorModel newInstance(final ViewObserver observer, final int index) {
            return new JoinModel(observer, index, JoinModel.Type.INNER_JOIN);
        }
    },

    INPUT_RELATION {
        @Override
        public OperatorModel newInstance(final ViewObserver observer, final int index) {
            return new RelationModel(observer, index);
        }
    },

    SELECTION {
        @Override
        public OperatorModel newInstance(final ViewObserver observer, final int index) {
            return new SelectionModel(observer, index);
        }
    },

    PROJECTION {
        @Override
        public OperatorModel newInstance(final ViewObserver observer, final int index) {
            return new ProjectionModel(observer, index);
        }
    };

    public abstract OperatorModel newInstance(ViewObserver observer, int index);
}
