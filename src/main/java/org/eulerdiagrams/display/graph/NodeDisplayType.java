package org.eulerdiagrams.display.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;
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
	
	private static HashMap<Node, Shape> shapeMap = new HashMap<Node, Shape>();
	/**
	 * Gives a new shape object representing the node. At the moment
	 * rectangles and ellipses are supported
	 */
	public static Shape generateShape(Node n) {
		
		Shape shape = null;

		NodeType nt = n.getType();		
		int height = nt.getHeight();
		int width = nt.getWidth();

		Point c = n.getCentre();
		
		if(n.getType().getShapeString().equals("Ellipse")) {
			shape = new Ellipse2D.Double(c.x-width/2,c.y-height/2,width,height);
		 } else {
			shape = new Rectangle2D.Double(c.x-width/2,c.y-height/2,width,height);
		}
		
		shapeMap.put(n, shape);

		return(shape);
	}


	/**
	 * Returns the shape object representing the node, or if
	 * there is none, generate a new shape.
	 */
	public static Shape shape(Node n) {
		Shape shape = shapeMap.get(n);
		if(shape != null)
			return shape;
		shape =	generateShape(n);
		return shape;
	}	

	/**
	 * Finds a node within the passed point, or returns null
	 * if the argument point is not over a node. The padding
	 * refers to the distance the point can be from the
	 * node, and must be greater than 0. If there is more
	 * than one node, it finds the
	 * last one in the collection, which hopefully should
	 * be the one on top of the display.
	 */
	public static Node getNodeNearPoint(Graph g, Point p, int padding) {

		Node returnNode = null;

		for(Node n : g.getNodes()) {
			Shape nodeShape = shape(n);
			Rectangle r = new Rectangle(p.x-padding,p.y-padding,padding*2,padding*2);
			if(nodeShape.intersects(r)) {
				returnNode = n;
			}
		}
		return(returnNode);
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
