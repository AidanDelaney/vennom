package org.eulerdiagrams.display.apCircles;

import java.awt.*;

import org.eulerdiagrams.display.graph.EdgeDisplayType;
import org.eulerdiagrams.vennom.graph.*;


/** Example of using Graph Panel */
public class APCircleDisplay {
	
	public static void setupEdgeNodeTypes(){
		
		EdgeType.setupEdgeTypes();
		EdgeDisplayType.setupDisplayTypes();
		
		Graph.DEFAULT_NODE_TYPE.setHeight(20);
		Graph.DEFAULT_NODE_TYPE.setWidth(20);
		Graph.DEFAULT_NODE_TYPE.setBorderColor(Color.WHITE);
		Graph.DEFAULT_NODE_TYPE.setTextColor(Color.BLUE);
		}
	
	
	public static void main(String[] args) {
		
		
/*
		NodeType top = new NodeType("centre");
		top.setShapeString("Ellipse");
		top.setFillColor(Color.red);
		top.setBorderColor(Color.blue);
		top.setWidth(20);
		top.setHeight(20);

		NodeType small = new NodeType("small");
		small.setShapeString("Rectangle");
		small.setWidth(15);
		small.setHeight(15);
*/
		setupEdgeNodeTypes();

		Graph.DEFAULT_NODE_TYPE.setHeight(20);
		Graph.DEFAULT_NODE_TYPE.setWidth(20);
		Graph.DEFAULT_NODE_TYPE.setBorderColor(Color.WHITE);
		Graph.DEFAULT_NODE_TYPE.setTextColor(Color.BLUE);
		Graph.DEFAULT_EDGE_TYPE = EdgeDisplayType.FIXEDDISPLAY.edgeType;		
		
		new APCircleWindow();
	}

}



