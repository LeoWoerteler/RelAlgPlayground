package kn.uni.dbis.relalg;

import java.awt.Point;

import javax.swing.SwingUtilities;

import kn.uni.dbis.relalg.model.DataModel;
import kn.uni.dbis.relalg.model.OperatorModel;
import kn.uni.dbis.relalg.view.GUI;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) {
        final DataModel model = new DataModel();
        final Controller ctrl = new Controller(model);
        final GUI view = new GUI(ctrl, model);
        view.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            final Point orig = new Point(50, 50);
            final OperatorModel join = model.addOperator(OperatorType.JOIN, orig);
            final OperatorModel join0 = model.addOperator(OperatorType.JOIN, orig);
            model.addConnection(join, 0, join0);
            final OperatorModel proj00 = model.addOperator(OperatorType.PROJECTION, orig);
            model.addConnection(join0, 0, proj00);
            final OperatorModel join000 = model.addOperator(OperatorType.JOIN, orig);
            model.addConnection(proj00, 0, join000);
            final OperatorModel in0000 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join000, 0, in0000);
            final OperatorModel join0001 = model.addOperator(OperatorType.JOIN, orig);
            model.addConnection(join000, 1, join0001);
            final OperatorModel in01 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join0, 1, in01);
            final OperatorModel sel1 = model.addOperator(OperatorType.SELECTION, orig);
            model.addConnection(join, 1, sel1);
            final OperatorModel join10 = model.addOperator(OperatorType.JOIN, orig);
            model.addConnection(sel1, 0, join10);
            final OperatorModel in100 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join10, 0, in100);
            final OperatorModel in101 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join10, 1, in101);
            final OperatorModel in00010 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join0001, 0, in00010);
            final OperatorModel in00011 = model.addOperator(OperatorType.INPUT_RELATION, orig);
            model.addConnection(join0001, 1, in00011);
            view.computeLayout();
        });
    }
}
