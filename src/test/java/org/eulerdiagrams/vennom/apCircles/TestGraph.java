package org.eulerdiagrams.vennom.apCircles;
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
        AreaSpecification as = new AreaSpecification("a 100.0\nb 100.0\nab 500");
        VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
        Graph g = vl.layout();
        for(Node n: g.getNodes()) {
            assertThat(n, is(not(nullValue())));
            // TODO programmatically check these printed values are correct
            System.out.println(n.getContour() + ", " + n.getLabel() + ", " + n.getCentre().x + ", " + n.getCentre().y);
        }
    }
}
