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
		FIXED.setPriority(1020);

		ATTRACTOR = new EdgeType("attractor");
		ATTRACTOR.setPriority(1019);

		REPULSOR = new EdgeType("repulsor");
		REPULSOR.setPriority(1018);

		SEPARATOR = new EdgeType("separator");
		SEPARATOR.setPriority(1017);

		IDEAL = new EdgeType("ideal");
		IDEAL.setPriority(1016);

		CONTAINMENT = new EdgeType("containment");
		CONTAINMENT.setPriority(1016);		
	}
	
/** Indicates if the edge is directed. */
	protected boolean directed = false;
	protected int priority = -1;
/** A list of all the edge types */
    protected static ArrayList<EdgeType> existingTypes = new ArrayList<EdgeType>();
/** Holds the current least priority */
    protected static int lowestPriority = Integer.MAX_VALUE;

/** Trivial constructor. */
	public EdgeType(String inLabel) {
		label = inLabel;
		existingTypes.add(this);
	}

/** Constructor also setting priority. */
	public EdgeType(String inLabel, int priority) {
		label = inLabel;
		existingTypes.add(this);
		setPriority(priority);
	}

/** trivial accessor */
	public boolean getDirected() {return directed;}
/** trivial accessor */
	public static ArrayList<EdgeType> getExistingTypes() {return existingTypes;}
/** trivial accessor */
	public int getPriority() {return priority;}
	
/** trivial mutator */
	public void setDirected(boolean d) {directed = d;}
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



