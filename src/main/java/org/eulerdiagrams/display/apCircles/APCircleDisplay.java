package org.eulerdiagrams.display.apCircles;

import java.awt.*;

import org.eulerdiagrams.display.graph.EdgeDisplayType;
import org.eulerdiagrams.display.graph.NodeDisplayType;
import org.eulerdiagrams.vennom.graph.*;


/** Example of using Graph Panel */
public class APCircleDisplay {
	
	public static void setupEdgeNodeTypes(){
		
		EdgeType.setupEdgeTypes();
		EdgeDisplayType.setupDisplayTypes();
		
		Graph.DEFAULT_NODE_TYPE.setHeight(20);
		Graph.DEFAULT_NODE_TYPE.setWidth(20);
		
		NodeDisplayType defaultDisplayType = new NodeDisplayType(Graph.DEFAULT_NODE_TYPE);		
		defaultDisplayType.setBorderColor(Color.WHITE);
		defaultDisplayType.setTextColor(Color.BLUE);
		}
	
	
	public static void main(String[] args) {
		
		setupEdgeNodeTypes();

		Graph.DEFAULT_EDGE_TYPE = EdgeDisplayType.FIXEDDISPLAY.edgeType;		
		
		new APCircleWindow();
	}

}



