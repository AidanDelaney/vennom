package org.eulerdiagrams.vennom.apCircles.display;

import java.awt.*;
import org.eulerdiagrams.vennom.graph.*;


/** Example of using Graph Panel */
public class APCircleDisplay {

	public static EdgeType REPULSOR;
	public static EdgeType ATTRACTOR;
	public static EdgeType FIXED;
	public static EdgeType SEPARATOR;
	public static EdgeType IDEAL;
	public static EdgeType CONTAINMENT;
	
	
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
		Graph.DEFAULT_EDGE_TYPE = FIXED;
		
		new APCircleWindow();
	}

}



