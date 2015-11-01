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
    // A symmetric Venn2
    public void testVenn2_001() {
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
        
        // generate test code by switching this on
        // then we see code we can put into the test
        // TODO this is only a manageable system for a handful of tests
        // we we expect to be pretty geometrically stable
        boolean regenerate_geometry_checks = false;
        if(regenerate_geometry_checks){
            ArrayList<Node> nodes = g.getNodes();

            System.out.println("ArrayList<Node> nodes = g.getNodes();");
            System.out.println("assertEquals(nodes.size(), "+nodes.size()+");");
            for(int i=0; i<nodes.size();++i){
	            System.out.println("Node n"+i+" = nodes.get("+i+");");
	            Node n = nodes.get(i);
	            System.out.println("assertTrue(n"+i+".getContour().equals(\""+n.getContour()+"\"));"
	                              +" // will break if the graph nodes get reordered");
	            System.out.println("Point p"+i+" = n"+i+".getCentre();");
	            Point p = n.getCentre();
	        	System.out.println("assertEquals(p"+i+".x, "+p.x+");");
	        	System.out.println("assertEquals(p"+i+".y, "+p.y+");");
	            System.out.println("double r"+i+" = n"+i+".getPreciseRadius();");
	            System.out.println("assertEquals(r"+i+", "+n.getPreciseRadius()+", 0.1);");
            }
        }
            
        ArrayList<Node> nodes = g.getNodes();
        assertEquals(nodes.size(), 2);
        Node n0 = nodes.get(0);
        assertTrue(n0.getContour().equals("a")); // will break if the graph nodes get reordered
        Point p0 = n0.getCentre();
        assertEquals(p0.x, 235);
        assertEquals(p0.y, 238);
        double r0 = n0.getPreciseRadius();
        assertEquals(r0, 13.81976597885342, 0.1);
        Node n1 = nodes.get(1);
        assertTrue(n1.getContour().equals("b")); // will break if the graph nodes get reordered
        Point p1 = n1.getCentre();
        assertEquals(p1.x, 297);
        assertEquals(p1.y, 363);
        double r1 = n1.getPreciseRadius();
        assertEquals(r1, 13.81976597885342, 0.1);
        
        // TODO either make this checking code easy to auto-generate
        // when an example changes, or don't check for actual geometry
        // but do check for validity and consistency of result
        // e.g. calculate an error and assert that the error doesn't grow.
        
        // TODO write a function which takes the String task
        // and the graph and an error tolerance and validates the result.
        // Does this code already exist?
        
    }
    
    @Test
    // A non-symmetric Venn2
    public void testVenn2_002() {
        // Venn2 area spec
    	String task = "a 100.0\nb 200.0\nab 500";
        AreaSpecification as = new AreaSpecification(task);
        VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
        Graph g = vl.layout();

        System.out.println("task is\n"+task);
        for(Node n: g.getNodes()) {
            assertThat(n, is(not(nullValue())));
            System.out.println(n.getContour() + ", " + n.getPreciseRadius() + ", " 
                             + n.getCentre().x + ", " + n.getCentre().y);
        }
        
        // generate test code by switching this on
        // then we see code we can put into the test
        // TODO this is only a manageable system for a handful of tests
        // we we expect to be pretty geometrically stable
        boolean regenerate_geometry_checks = false;
        if(regenerate_geometry_checks){
            ArrayList<Node> nodes = g.getNodes();

            System.out.println("ArrayList<Node> nodes = g.getNodes();");
            System.out.println("assertEquals(nodes.size(), "+nodes.size()+");");
            for(int i=0; i<nodes.size();++i){
	            System.out.println("Node n"+i+" = nodes.get("+i+");");
	            Node n = nodes.get(i);
	            System.out.println("assertTrue(n"+i+".getContour().equals(\""+n.getContour()+"\"));"
	                              +" // will break if the graph nodes get reordered");
	            System.out.println("Point p"+i+" = n"+i+".getCentre();");
	            Point p = n.getCentre();
	        	System.out.println("assertEquals(p"+i+".x, "+p.x+");");
	        	System.out.println("assertEquals(p"+i+".y, "+p.y+");");
	            System.out.println("double r"+i+" = n"+i+".getPreciseRadius();");
	            System.out.println("assertEquals(r"+i+", "+n.getPreciseRadius()+", 0.1);");
            }
        }
            
        ArrayList<Node> nodes = g.getNodes();
        assertEquals(nodes.size(), 2);
        Node n0 = nodes.get(0);
        assertTrue(n0.getContour().equals("a")); // will break if the graph nodes get reordered
        Point p0 = n0.getCentre();
        assertEquals(p0.x, 235);
        assertEquals(p0.y, 238);
        double r0 = n0.getPreciseRadius();
        assertEquals(r0, 13.81976597885342, 0.1);
        Node n1 = nodes.get(1);
        assertTrue(n1.getContour().equals("b")); // will break if the graph nodes get reordered
        Point p1 = n1.getCentre();
        assertEquals(p1.x, 297);
        assertEquals(p1.y, 363);
        double r1 = n1.getPreciseRadius();
        assertEquals(r1, 14.927053303604616, 0.1);        
    }
}
