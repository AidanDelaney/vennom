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
public class EdgeType implements Serializable {

	public static EdgeType REPULSOR;
	public static EdgeType ATTRACTOR;
	public static EdgeType FIXED;
	public static EdgeType SEPARATOR;
	public static EdgeType IDEAL;
	public static EdgeType CONTAINMENT;
	
	private String label;

	public static void setupEdgeTypes(){
		FIXED = new EdgeType("fixed");
		ATTRACTOR = new EdgeType("attractor");
		REPULSOR = new EdgeType("repulsor");
		SEPARATOR = new EdgeType("separator");
		IDEAL = new EdgeType("ideal");
		CONTAINMENT = new EdgeType("containment");
	}
	
/** Indicates if the edge is directed. */
	protected boolean directed = false;
/** A list of all the edge types */
    protected static ArrayList<EdgeType> existingTypes = new ArrayList<EdgeType>();

/** Trivial constructor. */
	public EdgeType(String inLabel) {
		label = inLabel;
		existingTypes.add(this);
	}

/** trivial accessor */
	public boolean getDirected() {return directed;}
/** trivial accessor */
	public static ArrayList<EdgeType> getExistingTypes() {return existingTypes;}

	/** trivial mutator */
	public void setDirected(boolean d) {directed = d;}


/** Returns the edge type with the given label or if there is no such type, returns null. */
	public static EdgeType withLabel(String label) {
		EdgeType et = null;
		for(EdgeType et2 : EdgeType.getExistingTypes()) {
			if(et2.label.equals(label)) {
				et = et2;
			}
		}
		return et;
	}

/**
* Outputs the content of the type.
*/
	public String toString() {
		return label;
	}

	public String getLabel(){
		return label;
	}
}



