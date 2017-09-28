package kn.uni.dbis.relalg.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.OperatorType;
import kn.uni.dbis.relalg.ViewObserver;
import kn.uni.dbis.relalg.model.JoinModel;
import kn.uni.dbis.relalg.model.OperatorModel;

public final class ViewPanel extends JPanel implements ViewObserver {
    /** Serial version UID. */
    private static final long serialVersionUID = 3555137048451441567L;
    private final Controller controller;
    private final List<OperatorView> operators = new ArrayList<>();

    ViewPanel(final Controller ctrl, final Dimension size) {
        super(null);
        this.controller = ctrl;
        this.setPreferredSize(size);
        this.setBackground(new Color(0xEEEEEE));
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                super.mousePressed(e);
                if (e.isPopupTrigger()) {
                    ViewPanel.this.showPopUp(e);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                super.mouseReleased(e);
                if (e.isPopupTrigger()) {
                    ViewPanel.this.showPopUp(e);
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(final KeyEvent e) {
                if (e.getKeyChar() == 'l') {
                    ViewPanel.this.computeLayout();
                }
            }
        });
    }

    @Override
    public void operatorPositionChanged(final OperatorView operatorView) {
        this.repaint();
    }

    @Override
    public void operatorAdded(final OperatorModel model, final Point pos) {
        final OperatorView view = model.createView(this.controller);
        view.setLocation(pos);
        this.operators.add(view);
        this.add(view);
        this.repaint();
    }

    @Override
    public void connectionAdded(final int parIdx, final int pos, final int childIdx) {
        this.operators.get(parIdx).setInput(pos, this.operators.get(childIdx));
        this.repaint();
    }

    @Override
    public void connectionRemoved(final int parIdx, final int pos) {
        this.operators.get(parIdx).setInput(pos, null);
        this.repaint();
    }

    @Override
    public void typeChanged(final int index, final JoinModel.Type type) {
        ((Join) this.operators.get(index)).typeChanged(type);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (final OperatorView op : this.operators) {
            final int k = op.getNumInputs();
            if (k > 0) {
                final Rectangle bounds = op.getBounds();
                final double gap = bounds.getWidth() / k;
                for (int i = 0; i < k; i++) {
                    final OperatorView input = op.getInput(i);
                    if (input != null) {
                        final Rectangle bounds2 = input.getBounds();
                        final int startX = (int) (bounds.getX() + (i + 0.5) * gap);
                        g2d.draw(new Line2D.Double(startX, bounds.y + bounds.height,
                                bounds2.x + bounds2.width / 2, bounds2.y));
                    }
                }
            }
        }
    }

    protected void showPopUp(final MouseEvent e) {
        final JPopupMenu menu = new JPopupMenu("Add Operator");
        final Point createPos = (Point) e.getPoint().clone();
        createPos.translate(-50, -50);
        menu.add(new JMenuItem("Add Relation")).addActionListener(
                e2 -> this.controller.addOperator(OperatorType.INPUT_RELATION, createPos));
        menu.add(new JMenuItem("Add Selection")).addActionListener(
                e2 -> this.controller.addOperator(OperatorType.SELECTION, createPos));
        menu.add(new JMenuItem("Add Projection")).addActionListener(
                e2 -> this.controller.addOperator(OperatorType.PROJECTION, createPos));
        menu.add(new JMenuItem("Add Join")).addActionListener(
                e2 -> this.controller.addOperator(OperatorType.JOIN, createPos));
        menu.show(this, e.getX(), e.getY());
    }

    public void computeLayout() {
        final BitSet rootIdxs = new BitSet();
        rootIdxs.set(0, this.operators.size());
        for (final OperatorView op : this.operators) {
            for (int i = op.getNumInputs(); --i >= 0;) {
                final OperatorView in = op.getInput(i);
                if (in != null) {
                    rootIdxs.clear(in.getIndex());
                }
            }
        }
        final List<LayoutResult> roots = rootIdxs.stream()
                .mapToObj(this.operators::get)
                .map(this::layout)
                .collect(Collectors.toList());
        int x = 50;
        final int y = 50;
        for (final LayoutResult res : roots) {
            x += res.leftWidth;
            this.assignPositions(res.root, x, y);
            x += res.root.op.getWidth() + 50;
        }
    }

    private void assignPositions(final LayoutTreeNode root, final int x, final int y) {
        if (root.op == null) {
            return;
        }
        root.op.setLocation(x, y);
        final int childY = y + root.op.getHeight() + 50;
        if (root.children.length != 0) {
            int xx = x - root.firstChildOffset;
            for (final LayoutTreeNode node : root.children) {
                xx += node.distance;
                this.assignPositions(node, xx, childY);
            }
        }
    }

    private LayoutResult layout(final OperatorView op) {
        if (op == null || op.getNumInputs() == 0) {
            return new LayoutResult(new LayoutTreeNode(op, new LayoutTreeNode[0], 0), null, null, 0, 0);
        }
        final int k = op.getNumInputs();
        final LayoutTreeNode[] subTrees = new LayoutTreeNode[k];
        final LayoutResult fst = this.layout(op.getInput(0));
        final int leftWidth = fst.leftWidth;
        int rightWidth = fst.rightWidth;
        subTrees[0] = fst.root;
        final ILL left = new ILL(0, fst.leftContour);
        final ILL right = new ILL(0, fst.rightContour);
        int width = 0;
        for (int i = 1; i < k; i++) {
            final LayoutResult res = this.layout(op.getInput(i));
            rightWidth = res.rightWidth;
            ILL ll = left;
            ILL lr = right.next;
            right.next = res.rightContour;
            ILL rl = res.leftContour;
            ILL rr = right;

            int overlap = 3 * op.getWidth() / 2;
            int maxOverlap = overlap;
            int wl = 0;
            int wr = 0;
            while (lr != null && rl != null) {
                overlap += lr.value + rl.value;
                maxOverlap = Math.max(overlap, maxOverlap);
                wl += ll.next.value + lr.value;
                wr += rl.value + rr.next.value;
                ll = ll.next;
                lr = lr.next;
                rl = rl.next;
                rr = rr.next;
            }
            width += maxOverlap;
            if (lr != null) {
                lr.value -= overlap + wl;
                rr.next = lr;
            } else if (rl != null) {
                rl.value -= overlap + wr;
                ll.next = rl;
            }
            subTrees[i] = res.root;
            subTrees[i].setDistance(maxOverlap);
        }
        left.value = width / 2;
        right.value = width / 2;
        return new LayoutResult(new LayoutTreeNode(op, subTrees, left.value), left, right,
                leftWidth + left.value, right.value + rightWidth);
    }

    private static class LayoutTreeNode {
        private final OperatorView op;
        private final LayoutTreeNode[] children;
        private final int firstChildOffset;
        private int distance = 0;

        LayoutTreeNode(final OperatorView op, final LayoutTreeNode[] children,
                final int firstChildOffset) {
            this.op = op;
            this.children = children;
            this.firstChildOffset = firstChildOffset;
        }

        void setDistance(final int distance) {
            this.distance = distance;
        }
    }

    private static final class LayoutResult {
        private final LayoutTreeNode root;
        private final ILL leftContour;
        private final ILL rightContour;
        private final int leftWidth;
        private final int rightWidth;

        LayoutResult(final LayoutTreeNode root, final ILL leftContour, final ILL rightContour,
                final int leftWidth, final int rightWidth) {
            this.root = root;
            this.leftContour = leftContour;
            this.rightContour = rightContour;
            this.leftWidth = leftWidth;
            this.rightWidth = rightWidth;
        }
    }

    private static final class ILL {
        private int value;
        private ILL next;

        ILL(final int value, final ILL next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ILL[").append(this.value);
            for (ILL curr = this.next; curr != null; curr = curr.next) {
                sb.append(", ").append(curr.value);
            }
            return sb.append("]").toString();
        }
    }
}
