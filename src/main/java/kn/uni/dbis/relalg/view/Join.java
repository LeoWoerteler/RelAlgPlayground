package kn.uni.dbis.relalg.view;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.model.JoinModel;
import kn.uni.dbis.relalg.model.JoinModel.Type;

public class Join extends OperatorView {

    private static final int LEFT_BAR = 1 << 0;
    private static final int RIGHT_BAR = 1 << 1;
    private static final int LEFT_OUT = 1 << 2;
    private static final int RIGHT_OUT = 1 << 3;
    private static final int ANTI = 1 << 4;

    private JoinModel.Type joinType;

    public Join(final Controller ctrl, final ViewObserver view, final int index, final JoinModel.Type type) {
        super(ctrl, view, 2, index);
        this.joinType = type;
    }

    private static final long serialVersionUID = -9051350956524547313L;

    @Override
    protected void paintSymbol(final Graphics2D g) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int w = 50;
        final int h = 50;
        final int px = (width - w) / 2;
        final int py = (height - h) / 2;
        final int out = 10;

        // end points of the central "X"
        final Point2D ul = new Point2D.Double(px, py);
        final Point2D ur = new Point2D.Double(px + w, py);
        final Point2D ll = new Point2D.Double(px, py + h);
        final Point2D lr = new Point2D.Double(px + w, py + h);

        // end points of the whiskers
        final Point2D oul = new Point2D.Double(px - out, py);
        final Point2D our = new Point2D.Double(px + w + out, py);
        final Point2D oll = new Point2D.Double(px - out, py + h);
        final Point2D olr = new Point2D.Double(px + w + out, py + h);

        // end points of the top bar
        final Point2D al = new Point2D.Double(px, py - out);
        final Point2D ar = new Point2D.Double(px + w, py - out);

        // always draw the "X"
        g.draw(new Line2D.Double(ll, ur));
        g.draw(new Line2D.Double(lr, ul));

        final int parts = this.getParts();
        if ((parts & LEFT_BAR) != 0) {
            g.draw(new Line2D.Double(ll, ul));
        }
        if ((parts & RIGHT_BAR) != 0) {
            g.draw(new Line2D.Double(lr, ur));
        }
        if ((parts & LEFT_OUT) != 0) {
            g.draw(new Line2D.Double(oul, ul));
            g.draw(new Line2D.Double(oll, ll));
        }
        if ((parts & RIGHT_OUT) != 0) {
            g.draw(new Line2D.Double(ur, our));
            g.draw(new Line2D.Double(lr, olr));
        }
        if ((parts & ANTI) != 0) {
            g.draw(new Line2D.Double(al, ar));
        }
    }

    private int getParts() {
        switch (this.joinType) {
            case CROSS_JOIN:
                return 0;
            case FULL_OUTER_JOIN:
                return LEFT_BAR | RIGHT_BAR | LEFT_OUT | RIGHT_OUT;
            case INNER_JOIN:
                return LEFT_BAR | RIGHT_BAR;
            case LEFT_ANTIJOIN:
                return LEFT_BAR | ANTI;
            case LEFT_OUTER_JOIN:
                return LEFT_BAR | RIGHT_BAR | LEFT_OUT;
            case LEFT_SEMIJOIN:
                return LEFT_BAR;
            case RIGHT_ANTIJOIN:
                return RIGHT_BAR | ANTI;
            case RIGHT_OUTER_JOIN:
                return LEFT_BAR | RIGHT_BAR | RIGHT_OUT;
            case RIGHT_SEMIJOIN:
                return RIGHT_BAR;
            default:
                throw new IllegalStateException("Unknown join type: " + this.joinType);
        }
    }

    @Override
    protected boolean showPopUp(final JPopupMenu menu) {
        final ButtonGroup joinType = new ButtonGroup();
        for (final JoinModel.Type type : JoinModel.Type.values()) {
            final JRadioButtonMenuItem item = new JRadioButtonMenuItem(type.toString());
            if (type == this.joinType) {
                item.setSelected(true);
            }
            item.addActionListener(ev -> this.getController().setJoinType(this.getIndex(), type));
            joinType.add(item);
            menu.add(item);
        }
        return true;
    }

    public void typeChanged(final Type type) {
        this.joinType = type;
        this.repaint();
    }
}
