package kn.uni.dbis.relalg.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import kn.uni.dbis.relalg.Controller;
import kn.uni.dbis.relalg.model.DataModel;

public final class GUI extends JFrame {
    private static final long serialVersionUID = 3284549757487005171L;
    private final ViewPanel mainPanel;

    public GUI(final Controller ctrl, final DataModel model) {
        super("Relational Algebra Playground");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.mainPanel = new ViewPanel(ctrl, new Dimension(1280, 800));
        this.setContentPane(this.mainPanel);
        model.register(this.mainPanel);
        this.pack();
    }

    public void operatorPositionChanged(final OperatorView operatorView) {
        this.mainPanel.repaint();
    }

    public void computeLayout() {
        this.mainPanel.computeLayout();
    }
}
