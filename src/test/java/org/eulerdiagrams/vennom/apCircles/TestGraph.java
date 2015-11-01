package org.eulerdiagrams.vennom.apCircles;
import java.awt.Point;
import java.util.ArrayList;

import org.eulerdiagrams.vennom.apCircles.VennomLayout;
import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestGraph {

    @Test
    public void testVenn2() {
        // Venn2 area spec
    	String task = "a 100.0\nb 100.0\nab 500";
        AreaSpecification as = new AreaSpecification(task);
        VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
        Graph g = vl.layout();

        System.out.println("task is\n"+task);
        for(Node n: g.getNodes()) {
            assertThat(n, is(not(nullValue())));
            System.out.println(n.getContour() + ", " + n.getPreciseRadius() + ", " 
                             + n.getCentre().x + ", " + n.getCentre().y);
        }
        
        ArrayList<Node> nodes = g.getNodes();
        assertEquals(nodes.size(), 2);
        
        Node n1 = nodes.get(0);
        assertTrue(n1.getContour().equals("a")); // will break if the graph nodes get reordered
        Point p1 = n1.getCentre();
        assertEquals(p1.x, 235);
        assertEquals(p1.y, 238);
        double r1 = n1.getPreciseRadius();
        assertEquals(r1, 13.8, 0.1);
        
        Node n2 = nodes.get(1);
        assertTrue(n2.getContour().equals("b")); // will break if the graph nodes get reordered
        Point p2 = n2.getCentre();
        assertEquals(p2.x, 297); 
        assertEquals(p2.y, 363);
        double r2 = n2.getPreciseRadius();
        assertEquals(r2, 13.8, 0.1);
        
        // TODO either make this checking code easy to auto-generate
        // when an example changes, or don't check for actual geometry
        // but do check for validity and consistency of result
        // e.g. calculate an error and assert that the error doesn't grow.
        
    }
}
