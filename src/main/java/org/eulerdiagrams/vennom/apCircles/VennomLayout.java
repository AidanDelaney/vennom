package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.vennom.apCircles.drawers.GeneralAPForceModelSolver;
import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.Graph;

public class VennomLayout {

	private AreaSpecification diagramSpec;
	private int layoutType;
	
	public static int FORCE_LAYOUT = 0;
	
	public VennomLayout(int layoutType, AreaSpecification as){
		diagramSpec = as;
		this.layoutType = layoutType;		
	}
	public Graph layout(){
		if(layoutType != FORCE_LAYOUT){
			// throw new NotImplementedException()?
			return null;			
		} else {
	        Graph g = diagramSpec.generateGeneralAugmentedIntersectionGraph();                
			for(Edge e : g.getEdges()) {
				e.setType(Graph.DEFAULT_EDGE_TYPE); //TODO what does this mean?
			}
	        GeneralAPForceModelSolver solver = new GeneralAPForceModelSolver();
	        solver.layout(g);
	        return g;
		}		
	}
}
