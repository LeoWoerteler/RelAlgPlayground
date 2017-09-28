package kn.uni.dbis.relalg.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.ViewObserver;

public abstract class OperatorView extends JPanel {
    /** Serial version UID. */
    private static final long serialVersionUID = 4890993255507309009L;
    private final OperatorView[] inputs;
    private final ViewObserver view;

    private Point movePos = null;
    private final int index;
    private final Controller controller;

    protected OperatorView(final Controller ctrl, final ViewObserver view, final int numInputs, final int index) {
        this.controller = ctrl;
        this.index = index;
        this.inputs = new OperatorView[numInputs];
        this.view = view;
        this.setBackground(new Color(0, true));
        this.setSize(new Dimension(100, 100));
        this.setPreferredSize(new Dimension(100, 100));
        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(final MouseEvent e) {
                OperatorView.this.moveStep(e);
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    OperatorView.this.showPopUp(e);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    OperatorView.this.startMove(e);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    OperatorView.this.showPopUp(e);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    OperatorView.this.endMove(e);
                }
            }
        });
    }

    public int getIndex() {
        return this.index;
    }

    private void showPopUp(final MouseEvent e) {
        final JPopupMenu menu = new JPopupMenu();
        if (this.showPopUp(menu)) {
            menu.show(this, e.getX(), e.getY());
        }
    }

    protected boolean showPopUp(final JPopupMenu menu) {
        return false;
    }

    protected void moveStep(final MouseEvent e) {
        final Point newPos = e.getLocationOnScreen();
        if (this.movePos != null) {
            final Point pos = this.getLocation();
            this.setLocation(new Point(pos.x + newPos.x - this.movePos.x,
                    pos.y + newPos.y - this.movePos.y));
            this.movePos = newPos;
        }
    }

    protected void startMove(final MouseEvent e) {
        this.movePos = e.getLocationOnScreen();
    }

    protected void endMove(final MouseEvent e) {
        this.movePos = null;
    }

    @Override
    protected final void paintComponent(final Graphics g) {
        final Rectangle bounds = this.getBounds();
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new Rectangle2D.Double(0, 0, bounds.getWidth() - 1, bounds.getHeight() - 1));
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        this.paintSymbol(g2d);
        final int r = 5;
        g2d.fill(new Ellipse2D.Double(bounds.getWidth() / 2 - r, -r, 2 * r, 2 * r));

        final int numInputs = this.inputs.length;
        if (numInputs > 0) {
            final double space = bounds.getWidth() / numInputs;
            for (int i = 0; i < numInputs; i++) {
                g2d.fill(new Ellipse2D.Double((i + 0.5) * space - r, bounds.getHeight() - r, 2 * r, 2 * r));
            }
        }
        this.view.operatorPositionChanged(this);
    }

    public int getNumInputs() {
        return this.inputs.length;
    }

    protected abstract void paintSymbol(Graphics2D g);

    public OperatorView getInput(final int i) {
        return this.inputs[i];
    }

    public void setInput(final int i, final OperatorView in) {
        this.inputs[i] = in;
    }

    Controller getController() {
        return this.controller;
    }
}
