package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.vennom.apCircles.drawers.GeneralAPForceModelSolver;
import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.EdgeType;
import org.eulerdiagrams.vennom.graph.Graph;

// pulling this in for the static declaration of EdgeTypes
import static org.eulerdiagrams.display.apCircles.APCircleDisplay.*;

import java.awt.*;

public class VennomLayout {
	static{
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

		CONTAINMENT = new EdgeType("containment");
		CONTAINMENT.setLineColor(Color.magenta);
		CONTAINMENT.setTextColor(Color.magenta);
		CONTAINMENT.setSelectedLineColor(Color.magenta);
		CONTAINMENT.setPriority(1016);

		Graph.DEFAULT_NODE_TYPE.setHeight(20);
		Graph.DEFAULT_NODE_TYPE.setWidth(20);
		Graph.DEFAULT_NODE_TYPE.setBorderColor(Color.WHITE);
		Graph.DEFAULT_NODE_TYPE.setTextColor(Color.BLUE);
		Graph.DEFAULT_EDGE_TYPE = IDEAL;
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

