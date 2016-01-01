package org.eulerdiagrams.display.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashMap;

import org.eulerdiagrams.vennom.graph.NodeType;

public class NodeDisplayType {
	
	public NodeType nodeType;
	
	public NodeDisplayType(NodeType nt){
		nodeType = nt;
		displayMap.put(nodeType, this);
	}
	
	private static HashMap<NodeType, NodeDisplayType> displayMap = new HashMap<NodeType, NodeDisplayType>();
	public static NodeDisplayType getDisplay(NodeType et) {
		return displayMap.get(et);
	}

	protected Color fillColor = Color.white;
	protected Color borderColor = Color.black;
	protected Color textColor = Color.black;
    protected BasicStroke stroke = new BasicStroke(2.0f);
	protected Color selectedFillColor = Color.black;
	protected Color selectedBorderColor = Color.black;
	protected Color selectedTextColor = Color.white;
    protected BasicStroke selectedStroke = new BasicStroke(2.0f);

	public Color getFillColor() {return fillColor;}
	public Color getBorderColor() {return borderColor;}
	public Color getTextColor() {return textColor;}
	public BasicStroke getStroke() {return stroke;}
	public Color getSelectedFillColor() {return selectedFillColor;}
	public Color getSelectedBorderColor() {return selectedBorderColor;}
	public Color getSelectedTextColor() {return selectedTextColor;}
	public BasicStroke getSelectedStroke() {return selectedStroke;}

	public void setFillColor(Color c) {fillColor = c;}
	public void setBorderColor(Color c) {borderColor = c;}
	public void setTextColor(Color c) {textColor = c;}
	public void setStroke(BasicStroke s) {stroke = s;}
	public void setSelectedFillColor(Color c) {selectedFillColor = c;}
	public void setSelectedBorderColor(Color c) {selectedBorderColor = c;}
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}	
}
