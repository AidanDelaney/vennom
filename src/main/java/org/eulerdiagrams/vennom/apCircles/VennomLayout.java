package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.vennom.apCircles.drawers.GeneralAPForceModelSolver;
import org.eulerdiagrams.vennom.graph.EdgeType;
import org.eulerdiagrams.vennom.graph.Graph;

public class VennomLayout {
		
	static{
		EdgeType.setupEdgeTypes();
		Graph.DEFAULT_EDGE_TYPE = EdgeType.IDEAL;
	}

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
	        GeneralAPForceModelSolver solver = new GeneralAPForceModelSolver();
	        solver.layout(g);
	        return g;
		}		
	}
}

