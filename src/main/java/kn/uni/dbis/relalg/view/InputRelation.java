package kn.uni.dbis.relalg.view;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;

public final class InputRelation extends OperatorView {
    private static final long serialVersionUID = 3846183356295838062L;
    private File input = null;

    public InputRelation(final Controller ctrl, final ViewObserver view, final int index) {
        super(ctrl, view, 0, index);
    }

    @Override
    protected void paintSymbol(final Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int w = 60;
        final int h = 50;
        final int px = (width - w) / 2;
        final int py = (height - h) / 2;
        g.draw(new Line2D.Double(px, py, px, py + h));
        g.draw(new Line2D.Double(px + w, py, px + w, py + h));
        g.draw(new Ellipse2D.Double(px, py - 10, w, 20));
        final Ellipse2D.Double e2 = new Ellipse2D.Double(px, py + h - 10, w, 20);
        final Shape clip = g.getClip();
        g.setClip(new Rectangle2D.Double(0, py + h, width, py));
        g.draw(e2);
        g.setClip(clip);
    }
}
