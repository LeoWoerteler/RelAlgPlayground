package kn.uni.dbis.relalg.view;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;

public final class Selection extends OperatorView {
    private static final long serialVersionUID = 5548819517967559536L;

    public Selection(final Controller ctrl, final ViewObserver view, final int index) {
        super(ctrl, view, 1, index);
        this.setFocusable(true);
    }

    @Override
    protected void paintSymbol(final Graphics2D g) {
        final double w = this.getBounds().getWidth();
        final double h = this.getBounds().getHeight();
        final Font f = new Font("SansSerif", Font.PLAIN, 100);
        final String sigma = "σ";
        final FontMetrics metrics = g.getFontMetrics(f);
        final Rectangle2D bounds = metrics.getStringBounds(sigma, g);
        final int expW = 80;
        final int expH = 80;
        final double scale = Math.min(expW / bounds.getWidth(), expH / bounds.getHeight());
        g.setFont(f.deriveFont(Font.PLAIN, (float) (scale * 100)));
        g.drawString(sigma, (float) (w / 2 - scale * bounds.getWidth() / 2),
                (float) (h / 2 - scale * bounds.getHeight() / 2 - bounds.getY() * scale));
    }
}
