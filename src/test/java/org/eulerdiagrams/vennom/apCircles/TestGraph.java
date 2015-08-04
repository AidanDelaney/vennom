package org.eulerdiagrams.vennom.apCircles;

import static org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay.*;

import org.eulerdiagrams.vennom.apCircles.drawers.GeneralAPForceModel;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.GraphDrawer;

import java.awt.Color;

import javax.swing.JFrame;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestGraph {

	static {
        /*
         * The lines until the <<< marker are junk that is necessary to set before you can use the VenNom graph drawer.
         * It's not pretty, but without setting properties of the EdgeTypes you get NullPointerExceptions from the
         * VenNom code.
         */
        FIXED = new EdgeType("fixed");
        FIXED.setLineColor(Color.black);
        FIXED.setTextColor(Color.black);
        FIXED.setSelectedLineColor(Color.gray);
        FIXED.setPriority(1020);

        ATTRACTOR = new EdgeType("attractor");
        ATTRACTOR.setLineColor(Color.green);
        ATTRACTOR.setTextColor(Color.green);
        ATTRACTOR.setSelectedLineColor(Color.gray);
        ATTRACTOR.setPriority(1019);

        REPULSOR = new EdgeType("repulsor");
        REPULSOR.setLineColor(Color.red);
        REPULSOR.setTextColor(Color.red);
        REPULSOR.setSelectedLineColor(Color.gray);
        REPULSOR.setPriority(1018);

        SEPARATOR = new EdgeType("separator");
        SEPARATOR.setLineColor(Color.cyan);
        SEPARATOR.setTextColor(Color.cyan);
        SEPARATOR.setSelectedLineColor(Color.gray);
        SEPARATOR.setPriority(1017);

        IDEAL = new EdgeType("ideal");
        IDEAL.setLineColor(Color.blue);
        IDEAL.setTextColor(Color.blue);
        IDEAL.setSelectedLineColor(Color.gray);
        IDEAL.setPriority(1016);

        Graph.DEFAULT_NODE_TYPE.setHeight(20);
        Graph.DEFAULT_NODE_TYPE.setWidth(20);
        Graph.DEFAULT_NODE_TYPE.setBorderColor(Color.WHITE);
        Graph.DEFAULT_NODE_TYPE.setTextColor(Color.BLUE);
        Graph.DEFAULT_EDGE_TYPE = FIXED;
        // <<<
    }

    @Test
    public void testVenn2() {
        // Venn2 area spec
        AreaSpecification as = new AreaSpecification("a 100.0\nb 100.0\nab 500");
        JFrame frame = new JFrame();
        GraphDrawer gd = new GeneralAPForceModel();
        APCirclePanel apc = new APCirclePanel(frame);
        apc.setSpecification(as);
        apc.addGraphDrawer(gd);

        apc.setGraph(as.generateGeneralAugmentedIntersectionGraph());
        gd.layout();

        for(Node n: gd.getGraph().getNodes()) {
            assertThat(n, is(not(nullValue())));
            System.out.println(n.getContour() + ", " + n.getLabel() + ", " + n.getCentre().x + ", " + n.getCentre().y);
        }
    }
}
