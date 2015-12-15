package org.eulerdiagrams.vennom.graph;

import java.util.*;
import java.io.*;
import java.awt.*;


/**
 * The type of a node in a graph. Utilises the tree structure of GraphType.
 * Allows matching to use a type heirarchy and associates various display
 * information with collections of Nodes.
 *
 * @see Node
 * @author Peter Rodgers
 */
public class NodeType extends ItemType implements Serializable {

@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((borderColor == null) ? 0 : borderColor.hashCode());
        result = prime * result
                + ((fillColor == null) ? 0 : fillColor.hashCode());
        result = prime * result + height;
        result = prime * result + ((selectedBorderColor == null) ? 0
                : selectedBorderColor.hashCode());
        result = prime * result + ((selectedFillColor == null) ? 0
                : selectedFillColor.hashCode());
        result = prime * result
                + ((selectedStroke == null) ? 0 : selectedStroke.hashCode());
        result = prime * result + ((selectedTextColor == null) ? 0
                : selectedTextColor.hashCode());
        result = prime * result
                + ((shapeString == null) ? 0 : shapeString.hashCode());
        result = prime * result + ((stroke == null) ? 0 : stroke.hashCode());
        result = prime * result
                + ((textColor == null) ? 0 : textColor.hashCode());
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeType other = (NodeType) obj;
        if (borderColor == null) {
            if (other.borderColor != null)
                return false;
        } else if (!borderColor.equals(other.borderColor))
            return false;
        if (fillColor == null) {
            if (other.fillColor != null)
                return false;
        } else if (!fillColor.equals(other.fillColor))
            return false;
        if (height != other.height)
            return false;
        if (selectedBorderColor == null) {
            if (other.selectedBorderColor != null)
                return false;
        } else if (!selectedBorderColor.equals(other.selectedBorderColor))
            return false;
        if (selectedFillColor == null) {
            if (other.selectedFillColor != null)
                return false;
        } else if (!selectedFillColor.equals(other.selectedFillColor))
            return false;
        if (selectedStroke == null) {
            if (other.selectedStroke != null)
                return false;
        } else if (!selectedStroke.equals(other.selectedStroke))
            return false;
        if (selectedTextColor == null) {
            if (other.selectedTextColor != null)
                return false;
        } else if (!selectedTextColor.equals(other.selectedTextColor))
            return false;
        if (shapeString == null) {
            if (other.shapeString != null)
                return false;
        } else if (!shapeString.equals(other.shapeString))
            return false;
        if (stroke == null) {
            if (other.stroke != null)
                return false;
        } else if (!stroke.equals(other.stroke))
            return false;
        if (textColor == null) {
            if (other.textColor != null)
                return false;
        } else if (!textColor.equals(other.textColor))
            return false;
        if (width != other.width)
            return false;
        return true;
    }

/** Height of a node, top to bottom. */
	protected int height = 30;
/** Width of a node, leftmost to rightmost. */
	protected int width = 30;

/** This can be Rectangle or Ellipse */
    protected String shapeString = new String("Ellipse");
	protected Color fillColor = Color.white;
	protected Color borderColor = Color.black;
	protected Color textColor = Color.black;
    protected BasicStroke stroke = new BasicStroke(2.0f);
	protected Color selectedFillColor = Color.black;
	protected Color selectedBorderColor = Color.black;
	protected Color selectedTextColor = Color.white;
    protected BasicStroke selectedStroke = new BasicStroke(2.0f);
/** A list of all the node types */
    protected static ArrayList<NodeType> existingTypes = new ArrayList<NodeType>();

/** Trivial constructor */
	public NodeType(String inLabel) {
		super(inLabel);
		existingTypes.add(this);
	}

/** trivial accessor */
	public int getHeight() {return height;}
/** trivial accessor */
	public int getWidth() {return width;}
/** trivial accessor */
	public String getShapeString() {return shapeString;}
/** trivial accessor */
	public Color getFillColor() {return fillColor;}
/** trivial accessor */
	public Color getBorderColor() {return borderColor;}
/** trivial accessor */
	public Color getTextColor() {return textColor;}
/** trivial accessor */
	public BasicStroke getStroke() {return stroke;}
/** trivial accessor */
	public Color getSelectedFillColor() {return selectedFillColor;}
/** trivial accessor */
	public Color getSelectedBorderColor() {return selectedBorderColor;}
/** trivial accessor */
	public Color getSelectedTextColor() {return selectedTextColor;}
/** trivial accessor */
	public BasicStroke getSelectedStroke() {return selectedStroke;}
/** trivial accessor */
	public static ArrayList<NodeType> getExistingTypes() {return existingTypes;}

/** trivial mutator */
	public void setHeight(int h) {height = h;}
/** trivial mutator */
	public void setWidth(int w) {width = w;}
/** trivial mutator */
	public void setShapeString(String s) {shapeString = s;}
/** trivial mutator */
	public void setFillColor(Color c) {fillColor = c;}
/** trivial mutator */
	public void setBorderColor(Color c) {borderColor = c;}
/** trivial mutator */
	public void setTextColor(Color c) {textColor = c;}
/** trivial mutator */
	public void setStroke(BasicStroke s) {stroke = s;}
/** trivial mutator */
	public void setSelectedFillColor(Color c) {selectedFillColor = c;}
/** trivial mutator */
	public void setSelectedBorderColor(Color c) {selectedBorderColor = c;}
/** trivial mutator */
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}
/** trivial mutator */
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}


/** Returns the edge type with the given label or if there is no such type, returns null. */
	public static NodeType withLabel(String label) {
		NodeType nt = null;
		for(NodeType nt2 : existingTypes){
			if(nt2.getLabel().equals(label)) {
				nt = nt2;
			}
		}
		return nt;
	}

/** Returns a list of all the roots in the type heirarchy removing duplicates */
	public static ArrayList<NodeType> allRoots() {

		ArrayList<NodeType> ret = new ArrayList<NodeType>();
		for(NodeType t : existingTypes){
			NodeType root = (NodeType)t.root();
			if(!ret.contains(root)) {
				ret.add(root);
			}
		}
		return ret;
	}

/**
* Outputs the content of the node type for debugging purposes.
*/
	public String toString() {
		return(getLabel());
	}

}



