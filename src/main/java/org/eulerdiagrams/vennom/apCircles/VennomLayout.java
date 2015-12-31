package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.display.apCircles.APCircleDisplay; // for EdgeTypes - needs splitting into display/non-display

import org.eulerdiagrams.vennom.apCircles.drawers.GeneralAPForceModelSolver;
import org.eulerdiagrams.vennom.graph.Graph;

import java.awt.*;

public class VennomLayout {
		
	static{
		APCircleDisplay.setupEdgeNodeTypes();

		Graph.DEFAULT_EDGE_TYPE = APCircleDisplay.IDEAL;
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

