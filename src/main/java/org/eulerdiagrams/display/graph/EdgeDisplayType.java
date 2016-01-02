package org.eulerdiagrams.display.graph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;

import org.eulerdiagrams.vennom.graph.EdgeType;
import org.eulerdiagrams.vennom.graph.Graph;

public class EdgeDisplayType {

	public EdgeType edgeType;

	private static HashMap<EdgeType, EdgeDisplayType> displayMap = new HashMap<EdgeType, EdgeDisplayType>();
	public static EdgeDisplayType getDisplay(EdgeType et) {
		return displayMap.get(et);
	}

	public static EdgeDisplayType DEFAULT_EDGE_DISPLAY_TYPE = new EdgeDisplayType(Graph.DEFAULT_EDGE_TYPE,1000);
	
	public static EdgeDisplayType REPULSORDISPLAY;
	public static EdgeDisplayType ATTRACTORDISPLAY;
	public static EdgeDisplayType FIXEDDISPLAY;
	public static EdgeDisplayType SEPARATORDISPLAY;
	public static EdgeDisplayType IDEALDISPLAY;
	public static EdgeDisplayType CONTAINMENTDISPLAY;

	public static void setupDisplayTypes() {
				
		FIXEDDISPLAY = new EdgeDisplayType(EdgeType.FIXED);
		FIXEDDISPLAY.setLineColor(Color.black);
		FIXEDDISPLAY.setTextColor(Color.black);
		FIXEDDISPLAY.setSelectedLineColor(Color.gray);
		FIXEDDISPLAY.setPriority(1020);

		ATTRACTORDISPLAY = new EdgeDisplayType(EdgeType.ATTRACTOR);
		ATTRACTORDISPLAY.setLineColor(Color.green);
		ATTRACTORDISPLAY.setTextColor(Color.green);
		ATTRACTORDISPLAY.setSelectedLineColor(Color.gray);
		ATTRACTORDISPLAY.setPriority(1019);

		REPULSORDISPLAY = new EdgeDisplayType(EdgeType.REPULSOR);
		REPULSORDISPLAY.setLineColor(Color.red);
		REPULSORDISPLAY.setTextColor(Color.red);
		REPULSORDISPLAY.setSelectedLineColor(Color.gray);
		REPULSORDISPLAY.setPriority(1018);

		SEPARATORDISPLAY = new EdgeDisplayType(EdgeType.SEPARATOR);
		SEPARATORDISPLAY.setLineColor(Color.cyan);
		SEPARATORDISPLAY.setTextColor(Color.cyan);
		SEPARATORDISPLAY.setSelectedLineColor(Color.gray);
		SEPARATORDISPLAY.setPriority(1017);

		IDEALDISPLAY = new EdgeDisplayType(EdgeType.IDEAL);
		IDEALDISPLAY.setLineColor(Color.blue);
		IDEALDISPLAY.setTextColor(Color.blue);
		IDEALDISPLAY.setSelectedLineColor(Color.gray);
		IDEALDISPLAY.setPriority(1016);

		CONTAINMENTDISPLAY = new EdgeDisplayType(EdgeType.CONTAINMENT);
		CONTAINMENTDISPLAY.setLineColor(Color.magenta);
		CONTAINMENTDISPLAY.setTextColor(Color.magenta);
		CONTAINMENTDISPLAY.setSelectedLineColor(Color.magenta);		
		CONTAINMENTDISPLAY.setPriority(1016);			
		}	
	private Color lineColor = Color.black;
	private Color selectedLineColor = Color.blue;
	private BasicStroke stroke = new BasicStroke(2.0f);
	private BasicStroke selectedStroke = new BasicStroke(2.0f);
	private Color textColor = Color.black;
	private Color selectedTextColor = Color.blue;
	protected int priority = -1;
	/** Holds the current least priority */
	protected static int lowestPriority = Integer.MAX_VALUE;
	/** trivial accessor */
	public int getPriority() {return priority;}
	
/** This mutator may alter the current lowestPriority */
	public void setPriority(int p) {
		priority = p;
		if (p<lowestPriority) {
			lowestPriority = p-1;
		}
	}	
	/** Set the priority of this edge type to be the least */
	public void setPriorityToLowest() {
		priority = lowestPriority;
		lowestPriority--;
	}

	public EdgeDisplayType(EdgeType et){
		edgeType = et;
		displayMap.put(edgeType, this);
	}	
	public EdgeDisplayType(EdgeType et, int priority){
		edgeType = et;
		setPriority(priority);
		displayMap.put(edgeType, this);
	}	
	public EdgeDisplayType(String label) {
		edgeType = new EdgeType(label);
		displayMap.put(edgeType,  this);
	}

	public void setLineColor(Color c){lineColor = c;}
	public void setSelectedLineColor(Color c) {selectedLineColor = c;}
	public void setStroke(BasicStroke s) {stroke = s;}
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}
	public void setTextColor(Color c) {textColor = c;}
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}

	public Color getLineColor(){return lineColor;}
	public Color getSelectedLineColor() {return selectedLineColor;}
	public BasicStroke getStroke() {return stroke;}
	public BasicStroke getSelectedStroke() {return selectedStroke;}
	public Color getTextColor() {return textColor;}
	public Color getSelectedTextColor() {return selectedTextColor;}

	
}
