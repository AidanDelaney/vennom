package org.eulerdiagrams.vennom.graph;

import java.util.*;
import java.io.*;
import java.awt.*;


/**
 * The type of an edge in a graph. Utilises the tree structure of ItemType.
 * Allows matching to use a type heirarchy and associates various display
 * information with collections of Edges.
 *
 * @see Edge
 * @author Peter Rodgers
 */
public class EdgeType extends ItemType implements Serializable {

/** Indicates if the edge is directed. */
	protected boolean directed = false;
	protected Color lineColor = Color.black;
	protected Color selectedLineColor = Color.blue;
    protected BasicStroke stroke = new BasicStroke(2.0f);
    protected BasicStroke selectedStroke = new BasicStroke(2.0f);
	protected Color textColor = Color.black;
	protected Color selectedTextColor = Color.blue;
	protected int priority = -1;
/** A list of all the edge types */
    protected static ArrayList<EdgeType> existingTypes = new ArrayList<EdgeType>();
/** Holds the current least priority */
    protected static int lowestPriority = Integer.MAX_VALUE;

/** Trivial constructor. */
	public EdgeType(String inLabel) {
		super(inLabel);
		existingTypes.add(this);
	}

/** Constructor also setting priority. */
	public EdgeType(String inLabel, int priority) {
		super(inLabel);
		existingTypes.add(this);
		setPriority(priority);
	}

/** trivial accessor */
	public boolean getDirected() {return directed;}
/** trivial accessor */
	public Color getLineColor() {return lineColor;}
/** trivial accessor */
	public Color getSelectedLineColor() {return selectedLineColor;}
/** trivial accessor */
	public BasicStroke getStroke() {return stroke;}
/** trivial accessor */
	public BasicStroke getSelectedStroke() {return selectedStroke;}
/** trivial accessor */
	public Color getTextColor() {return textColor;}
/** trivial accessor */
	public Color getSelectedTextColor() {return selectedTextColor;}
/** trivial accessor */
	public static ArrayList<EdgeType> getExistingTypes() {return existingTypes;}
/** trivial accessor */
	public int getPriority() {return priority;}

/** trivial mutator */
	public void setDirected(boolean d) {directed = d;}
/** trivial mutator */
	public void setLineColor(Color c) {lineColor = c;}
/** trivial mutator */
	public void setSelectedLineColor(Color c) {selectedLineColor = c;}
/** trivial mutator */
	public void setStroke(BasicStroke s) {stroke = s;}
/** trivial mutator */
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}
/** trivial mutator */
	public void setTextColor(Color c) {textColor = c;}
/** trivial mutator */
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}
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

/** Returns the edge type with the given label or if there is no such type, returns null. */
	public static EdgeType withLabel(String label) {
		EdgeType et = null;
		for(EdgeType et2 : EdgeType.getExistingTypes()) {
			if(et2.getLabel().equals(label)) {
				et = et2;
			}
		}
		return et;
	}


/** Returns a list of all the roots in the type heirarchy removing duplicates */
	public static ArrayList<EdgeType> allRoots() {

		ArrayList<EdgeType> ret = new ArrayList<EdgeType>();
		for(EdgeType t : EdgeType.getExistingTypes()) {
			EdgeType root = (EdgeType)t.root();
			if(!ret.contains(root)) {
				ret.add(root);
			}
		}
		return ret;
	}

/** 
* Outputs the content of the type.
*/
	public String toString() {
		return(getLabel());
	}

}



