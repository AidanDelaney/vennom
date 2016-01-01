package org.eulerdiagrams.display.graph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;

import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.EdgeType;

public class EdgeDisplayType {

	public EdgeType edgeType;

	static HashMap<EdgeType, EdgeDisplayType> displayMap = new HashMap<EdgeType, EdgeDisplayType>();
	public static EdgeDisplayType getDisplay(EdgeType et) {
		return displayMap.get(et);
	}

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

		ATTRACTORDISPLAY = new EdgeDisplayType(EdgeType.ATTRACTOR);
		ATTRACTORDISPLAY.setLineColor(Color.green);
		ATTRACTORDISPLAY.setTextColor(Color.green);
		ATTRACTORDISPLAY.setSelectedLineColor(Color.gray);

		REPULSORDISPLAY = new EdgeDisplayType(EdgeType.REPULSOR);
		REPULSORDISPLAY.setLineColor(Color.red);
		REPULSORDISPLAY.setTextColor(Color.red);
		REPULSORDISPLAY.setSelectedLineColor(Color.gray);

		SEPARATORDISPLAY = new EdgeDisplayType(EdgeType.SEPARATOR);
		SEPARATORDISPLAY.setLineColor(Color.cyan);
		SEPARATORDISPLAY.setTextColor(Color.cyan);
		SEPARATORDISPLAY.setSelectedLineColor(Color.gray);

		IDEALDISPLAY = new EdgeDisplayType(EdgeType.IDEAL);
		IDEALDISPLAY.setLineColor(Color.blue);
		IDEALDISPLAY.setTextColor(Color.blue);
		IDEALDISPLAY.setSelectedLineColor(Color.gray);

		CONTAINMENTDISPLAY = new EdgeDisplayType(EdgeType.CONTAINMENT);
		CONTAINMENTDISPLAY.setLineColor(Color.magenta);
		CONTAINMENTDISPLAY.setTextColor(Color.magenta);
		CONTAINMENTDISPLAY.setSelectedLineColor(Color.magenta);		
	}	
	private Color lineColor = Color.black;
	private Color selectedLineColor = Color.blue;
	private BasicStroke stroke = new BasicStroke(2.0f);
	private BasicStroke selectedStroke = new BasicStroke(2.0f);
	private Color textColor = Color.black;
	private Color selectedTextColor = Color.blue;

	public EdgeDisplayType(EdgeType et){
		edgeType = et;
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
