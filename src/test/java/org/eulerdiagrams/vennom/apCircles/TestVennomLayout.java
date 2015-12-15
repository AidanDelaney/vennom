package org.eulerdiagrams.vennom.apCircles;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eulerdiagrams.vennom.graph.Graph;
import org.junit.Test;

public class TestVennomLayout {

    @Test
    public void test() {
        AreaSpecification as = new AreaSpecification("A 100\nB 100\n");
        VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
        Graph graph = vl.layout();

        Graph notExpected = as.generateGeneralAugmentedIntersectionGraph();

        boolean b = graph.equals(notExpected);
        assertThat(graph, is(not(equalTo(notExpected))));
    }

}
