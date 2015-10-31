package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;

public class TestVennomLayout {
	
	public static void main(String args[])
	{	
		String input;
		// no arg means no task
		if(args.length < 1)	{
			// hard-coded example
			input = "a 100.0 b 500.0 ab 500";
		} else {
			input = args[0];
		}
		
		// expect an argument which is a string describing a diagram
		// like this "a 100.0 b 200.0 ab 500"
		//
		// make a string describing a diagram
		// like this "a 100.0\nb 200.0\nab 500"

		String[] tokens = input.split(" ");
		String s = "";
		for(int i = 0; i<tokens.length; ++i)
		{
			s += tokens[i];
			if(i%2==0){
				// expect token[i] to be a zone label
				// add a space to allow the next token to be an area for that zone
				// expect it to be different from any preceding zone labels
				s+=" ";
			} else {
				// expect token[i] to be a double
				// add a linebreak to finish the data for that zone
				// expect the double to be > 0
				s+="\n";
			}
		}
				
		// Build an AreaSpecification
        AreaSpecification as = new AreaSpecification(s); 
        
        VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
        Graph g = vl.layout();
        
        // TODO programmatically check found values against these expected values
        // (what tolerance to use? hook into proper test system)
        System.out.println("Expected output:");
        System.out.println("contour a at Point2D.Double[235.0, 238.0] with radius 13.81976597885342");
        System.out.println("contour b at Point2D.Double[297.0, 363.0] with radius 17.841241161527712");
        System.out.println("Today's output:");
		for(Node node : g.getNodes()) {
			System.out.println("contour "+node.getContour()+" at "+node.getPreciseCentre()+
					          " with radius "+node.getPreciseRadius());
		}
	}
}
