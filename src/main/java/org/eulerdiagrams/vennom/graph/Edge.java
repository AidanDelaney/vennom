package org.eulerdiagrams.vennom.graph;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;

/**
 * This is a simple edge, connecting two nodes together in a graph.
 * It can be treateded as a directed or undirected edge.
 * <p>
 * It contains an additional field for helping when writing algorithms, the
 * boolean {@link #visited}, a flag for indicating that an edge has been traversed
 *
 * @see Graph
 * @see Node
 * @author Peter Rodgers
 */

public class Edge implements Serializable {

/** Source node. Must be assigned.*/
	protected Node from;
/** Target node. Must be assigned.*/
	protected Node to;
/** Optional label.*/
	protected String label = "";
/** Optional weight value.*/
	protected double weight = 0.0;
/** Node type, must be present. */
	protected EdgeType type = Graph.DEFAULT_EDGE_TYPE;
/** This flag can be set if the edge has been traversed during a graph algorithm. */
	protected boolean visited = false;
/** This value can be used to score edges during a graph algorithm. */
	protected double score = 0.0;
/**
 * A variable for use in graph algorithms. General Use, but can be used for
 * pointing at nodes in matching algorithms.
 */
	protected Object match = null;
/** The last generated shape object for this edge */
	Shape shape = null;
/** The location of edge bends. This is a list of Points.*/
	protected ArrayList<Point> bends = new ArrayList<Point>();


/**
 * Pass the two nodes that the edge should connect to this constructor.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo) {
		setFromTo(inFrom, inTo);
	}

/**
 * Constructor initialising label.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo, String inLabel) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
	}

/**
 * Constructor initialising weight.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo, double inWeight) {
		setFromTo(inFrom, inTo);
		setWeight(inWeight);
	}

/**
 * Constructor initialising type.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo, EdgeType inType) {
		setFromTo(inFrom, inTo);
		setType(inType);
	}

/**
 * Constructor initialising label and weight.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo, String inLabel, double inWeight) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
		setWeight(inWeight);
	}

/**
 * Constructor initialising label, weight and type.
 * It creates an edge between the two nodes.
 */
	public Edge(Node inFrom, Node inTo, String inLabel, double inWeight, EdgeType inType) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
		setWeight(inWeight);
		setType(inType);
	}


/** Trival accessor. */
	public Node getFrom() {return from;}
/** Trival accessor. */
	public Node getTo() {return to;}
/** Trival accessor. */
	public String getLabel() {return label;}
/** Trival accessor. */
	public double getWeight() {return weight;}
/** Trival accessor. */
	public EdgeType getType() {return type;}
/** Trival accessor. */
	public boolean getVisited() {return visited;}
/** Trival accessor. */
	public double getScore() {return score;}
/** Trival accessor. */
	public Object getMatch() {return match;}
/** Trival accessor. */
	public ArrayList<Point> getBends() {return bends;}
/** Trivial modifier. */
	public void setLabel(String inLabel) {label = inLabel;}
/** Trivial modifier. */
	public void setWeight(double inWeight) {weight = inWeight;}
/** Trivial modifier. */
	public void setType(EdgeType inType) {type=inType;}
/** Trivial modifier. */
	public void setVisited(boolean inVisited) {visited = inVisited;}
/** Trivial modifier. */
	public void setScore(double inScore) {score = inScore;}
/** Trivial modifier. */
	public void setMatch(Object inMatch) {match = inMatch;}
/** Trivial modifier. */
	public void setBends(ArrayList<Point> inEdgeBends) {bends = inEdgeBends;}

/** Adds an edge bend to the end of the edge bend list. */
	public void addBend(Point p) {
		getBends().add(p);
	}

/** Removes all edge bends by setting the bend list to a new, empty list. */
	public void removeAllBends() {
		bends = new ArrayList<Point>();
	}

/** find out if the edge connects to the same node at each end. */
	public boolean selfSourcing() {
		if(from == to) {
			return(true);
		}
		return(false);
	}


/**
 * Gives the other end of the edge to the argument node
 * @return the node at the other end of the edge, or null if the passed node is not connected to the edge
 */
	public Node getOppositeEnd(Node n) {

		Node ret = null;
		if (getFrom() == n) {
			ret = getTo();
		}
		if (getTo() == n) {
			ret = getFrom();
		}

		return(ret);
	}


/**
 * Sets or moves both ends of the edge.
 * @return the success of the operation, if fail, then the old state
 * should be preserved.
 */
	public boolean setFromTo(Node inFrom, Node inTo) {

		Node oldFrom=from;		
		if(!setFrom(inFrom)) {
			return(false);
		}
		if(!setTo(inTo)) {
			setFrom(oldFrom);
			return(false);
		}
		return(true);
	}


/**
 * Sets or moves the source of the edge. Accounts for the
 * redundant data in the node. Also deals with nulls, either
 * currently attached to edges, or passed to this method.
 * @return the success of the operation.
 */
	public boolean setFrom(Node inFrom) {
		if(from != null) {
			if(!from.removeEdgeFrom(this)) {
				return(false);
			}
		}
		if(inFrom != null) {
			inFrom.addEdgeFrom(this);
		}
		from = inFrom;
		return(true);
	}


/**
* Sets or moves the target of the edge. Accounts for the
* redundant data in the node. Also deals with nulls, either
* currently attached to edges, or passed to this method.
* @return the success of the operation.
*/
	public boolean setTo(Node inTo) {
		if(to != null) {
			if(!to.removeEdgeTo(this)) {
				return(false);
			}
		}
		if(inTo != null) {
			inTo.addEdgeTo(this);
		}
		to = inTo;
		return(true);
	}


	/**
	 * This method reverses the ends of the edge. It also works for undirected edges,
	 * reversing the from and to values, although in this case no difference should
	 * be apparent.
	 */
		public void reverse() {
			
			Node oldFrom = from;
			setFrom(to);
			setTo(oldFrom);
		}


/**
 * Checks to see if the two edges intersect, only works
 * if there are no edge bends in either edge.
 */
	public boolean straightLineIntersects(Edge e) {
		
		// if the edges connect to the same node there is no crossing
		if(getFrom() == e.getFrom()) {return false;}
		if(getFrom() == e.getTo()) {return false;}
		if(getTo() == e.getFrom()) {return false;}
		if(getTo() == e.getTo()) {return false;}
		
		int x1 = getFrom().getX();
		int y1 = getFrom().getY();
		int x2 = getTo().getX();
		int y2 = getTo().getY();
		int x3 = e.getFrom().getX();
		int y3 = e.getFrom().getY();
		int x4 = e.getTo().getX();
		int y4 = e.getTo().getY();


		return Line2D.linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4);
	}


/**
 * Gives a new shape object representing the edge. At the moment
 * only undirected straight lines are supported.
 */
	public Shape generateShape(Point offset) {

		Point fromPoint = new Point(from.getCentre());
		Point toPoint = new Point(to.getCentre());

		fromPoint.x += offset.x;
		fromPoint.y += offset.y;
		toPoint.x += offset.x;
		toPoint.y += offset.y;

		GeneralPath path = new GeneralPath();
		path.moveTo(fromPoint.x+offset.x, fromPoint.y+offset.y);

		for(Point p : bends) {
			path.lineTo(p.x+offset.x, p.y+offset.y);
		}

		path.lineTo(toPoint.x+offset.x, toPoint.y+offset.y);

		shape = path;

		return(shape);
	}


/**
 * Returns the shape object representing the edge, or if
 * there is none, generate a new shape. The generation is
 * performed using a zero offset, which may cause difficulties
 * if parallel edges are being separated, however generation
 * due to the call of this method will be extremely rare, and
 * no known situation where generation is performed is known.
 */
	public Shape shape() {
		if(shape == null) {
			generateShape(APCirclePanel.ZEROOFFSET);
		}
		return(shape);
	}



/**
 * Checks that the connecting nodes have consistent redundant data. Used principally
 * by {@link Graph#consistent}.
 */
	public boolean consistent(Graph g) {
		if(!getFrom().getEdgesFrom().contains(this)) {
			return(false);
		}
		if(!getTo().getEdgesTo().contains(this)) {
			return(false);
		}
		if(!g.getNodes().contains(getFrom())) {
			return(false);
		}
		if(!g.getNodes().contains(getTo())) {
			return(false);
		}

		return(true);
	}


/** Gives the connecting nodes strings in a pair, plus the edge label and weight. */
	public String toString() {
		return("("+getFrom()+":"+getTo()+","+getLabel()+","+getWeight()+")");
	}


}

 
