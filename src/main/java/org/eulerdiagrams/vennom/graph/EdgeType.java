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

	public static EdgeType REPULSOR;
	public static EdgeType ATTRACTOR;
	public static EdgeType FIXED;
	public static EdgeType SEPARATOR;
	public static EdgeType IDEAL;
	public static EdgeType CONTAINMENT;

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
	
@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (directed ? 1231 : 1237);
        result = prime * result + priority;
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
        EdgeType other = (EdgeType) obj;
        if (directed != other.directed)
            return false;
        if (priority != other.priority)
            return false;
        return true;
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



