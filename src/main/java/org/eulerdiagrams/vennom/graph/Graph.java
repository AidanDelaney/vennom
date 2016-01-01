package org.eulerdiagrams.vennom.graph;

import java.util.*;
import java.io.*;
import java.awt.*;

import org.eulerdiagrams.vennom.apCircles.*;
import org.eulerdiagrams.vennom.graph.comparators.*;


/**
 * This is a graph containing nodes and edges.
 * <p>
 * The graph can be used for both directed and undirected graphs.
 * The current interpretation of undirected edges in the
 * algorithms is being able to traverse a single edge in either
 * direction. This is reflected in the graph, node and edge access functions,
 * which provide for this form of undirected access.
 * <p>
 * The consistency of the graph takes several forms:
 * all nodes and edges attach to items in the graph;
 * unique nodes and edges in the collections (no duplicate objects);
 * edges having a connection at each end; and
 * edge to and from corresponding to node to and from.
 * <p>
 * Consistency is maintained through the operations in graph, so
 * that its not possible to add an edge without the nodes being
 * in the graph.
 * Also, the node delete removes any connecting edges from the graph
 * <p>
 * Note that it is still possible to create an inconsistent graph by
 * accessing fields directly, or via poor use of Node or Edge methods.
 *
 * @see Node
 * @see Edge
 * @author Peter Rodgers
 *
 */
public class Graph implements Serializable {

/** The default edge type. */
	public static EdgeType DEFAULT_EDGE_TYPE = new EdgeType("defaultEdgeType",1000);
/** The default node type. */
	public static NodeType DEFAULT_NODE_TYPE = new NodeType("defaultNodeType");


/** Collection of nodes. */
	private ArrayList<Node> nodes = new ArrayList<Node>();
/** Collection of edges. */
	private ArrayList<Edge> edges = new ArrayList<Edge>();
/** Graph label, can be empty. */
	private String label = "";

/** Separator between nodes in an adjacency list file */
	public final char ADJACENCYSEPARATOR = ':';
	public final char WEIGHTEDSEPARATOR = ' ';
	public final char SIMPLEFILESEPARATOR = ' ';
	public final char FILESEPARATOR = '|';
	public final char XYSEPARATOR = ',';
	public final char COORDINATESEPARATOR = ';';
	public final String FILESTARTNODES = "NODES";
	public final String FILESTARTEDGES = "EDGES";
	public final String FILESTARTNODETYPES = "NODETYPES";
	public final String FILESTARTEDGETYPES = "EDGETYPES";


/** Minimal constructor. It creates an empty unlabeled graph*/
	public Graph() {}
/** Trivial constructor. It creates an empty labeled graph */
	public Graph(String inLabel) {setLabel(inLabel);}


	public String getLabel() {return label;}

	public void setLabel(String inLabel) {label=inLabel;}


/**
 * Adds a node to the graph.
 * @return true if the node is added, false if it fails because it already there.
 */
	public boolean addNode(Node n) {

		if (containsNode(n)) {
			return(false);
		}
		return(nodes.add(n));
	}

/**
 * Adds an edge to the graph.
 * @return true if the node is added, false if it fails because the edge is
 * already there, or either node is not in the graph.
 */
	public boolean addEdge(Edge e) {

		if(!containsNode(e.getTo())) {
			return(false);
		}
		if(!containsNode(e.getFrom())) {
			return(false);
		}

		if(containsEdge(e)) {
			return(false);
		}

		return(edges.add(e));
	}


/** Trivial accessor */
	public ArrayList<Node> getNodes() {return nodes;}

/** Trivial accessor */
	public ArrayList<Edge> getEdges() {return edges;}


/**
 * Set the visited fields of all the nodes in the graph to false.
 * this, or the one argument alternative should be called at the
 * start of an algorithm which uses the visited flags of nodes.
 */
	public void setNodesVisited() {
		setNodesVisited(getNodes(),false);
	}


/**
 * Set the visited fields of all the nodes in the graph with the argument.
 */
	public void setNodesVisited(boolean inVisited) {
		setNodesVisited(getNodes(),inVisited);
	}


/**
 * Set the visited fields of the given nodes with the argument.
 */
	public void setNodesVisited(Collection<Node> inNodes, boolean inVisited) {
		for(Node n : inNodes) {
			n.setVisited(inVisited);
		}
	}



/**
 * Set the score fields of all the nodes in the graph with the argument.
 */
	public void setNodesScores(double inScore) {
		setNodesScores(nodes, inScore);
	}


/**
 * Set the score fields of the given nodes with the argument.
 */
	public void setNodesScores(Collection<Node> inNodes, double inScore) {
		for(Node n : inNodes) {
			n.setScore(inScore);
		}
	}

/**
 * Set the match fields of all the nodes in the graph to the given value.
 */
	public void setNodesMatches(Object inMatch) {
		setNodesMatches(nodes,inMatch);
	}


/**
 * Set the match fields of the given node collection to the given value.
 */
	private void setNodesMatches(Collection<Node> inNodes, Object inMatch) {
		for(Node n : inNodes) {
			n.setMatch(inMatch);
		}
	}



/**
 * Set the visited values of the edges to false,
 * this should be called before using the
 * visited flags of nodes.
 */
	private void setEdgesVisited() {
		setEdgesVisited(getEdges(),false);
	}


/**
 * Set the visited values of all the edges in the graph to the argument.
 */
	public void setEdgesVisited(boolean inVisited) {
		setEdgesVisited(getEdges(),inVisited);
	}


/**
 * Set the visited values of the given edges to the argument.
 */
	private void setEdgesVisited(Collection<Edge> inEdges, boolean inVisited) {
		for(Edge e : inEdges) {
			e.setVisited(inVisited);
		}
	}


/**
 * Set the score fields of all the edges in the graph with the argument.
 */
	public void setEdgesScores(double inScore) {
		setEdgesScores(getEdges(),inScore);
	}


/**
 * Set the score fields of the given edges with the argument.
 */
	public void setEdgesScores(Collection<Edge> inEdges, double inScore) {
		for(Edge e : inEdges) {
			e.setScore(inScore);
		}
	}



/**
 * Set the weight fields of all the edges in the graph with the argument.
 */
	public void setEdgesWeights(double inWeight) {
		setEdgesWeights(getEdges(),inWeight);
	}



/**
 * Set the weight fields of the given edges with the argument.
 */
	private void setEdgesWeights(Collection<Edge> inEdges, double inWeight) {
		for(Edge e : inEdges) {
			e.setWeight(inWeight);
		}
	}


/**
 * Set the match fields of all the edges in the graph to the given value.
 */
	public void setEdgesMatches(Object inMatch) {
		setEdgesMatches(edges,inMatch);
	}


/**
 * Set the match fields of the given edge collection to the given value.
 */
	private void setEdgesMatches(Collection<Edge> inEdges, Object inMatch) {
		for(Edge e : inEdges) {
			e.setMatch(inMatch);
		}
	}



/** Gives the nodes that are not marked as visited. */
	public HashSet<Node> unvisitedNodes() {
		HashSet<Node> ret = new HashSet<Node>();

		for(Node n : getNodes()) {
			if(n.getVisited() == false) {
				ret.add(n);
			}
		}

		return(ret);
	}


/** Gives the nodes that are marked as visited. */
	public HashSet<Node> visitedNodes() {
		HashSet<Node> ret = new HashSet<Node>();

		for(Node n : getNodes()) {
			if(n.getVisited() != false) {
				ret.add(n);
			}
		}

		return(ret);
	}


/** Gives the edges that are not marked as visited. */
	public HashSet<Edge> unvisitedEdges() {
		HashSet<Edge> ret = new HashSet<Edge>();

		for(Edge e : getEdges()) {
			if(e.getVisited() == false) {
				ret.add(e);
			}
		}

		return(ret);
	}



/** Gives the edges that are marked as visited. */
	public HashSet<Edge> visitedEdges() {
		HashSet<Edge> ret = new HashSet<Edge>();

		for(Edge e : getEdges()) {
			if(e.getVisited() != false) {
				ret.add(e);
			}
		}

		return(ret);
	}


/**
 * Sum all the edge weights in the graph.
 */
	private double sumEdgeWeights() {
		return(sumEdgeWeights(getEdges()));
	}



/**
 * Finds if the node is already present in the graph.
 */
	private boolean containsNode(Node n) {
		for(Node next : getNodes()) {
			if(n == next) {
				return(true);
			}
		}
		return(false);
	}



/**
 * Finds if the edge is already present in the graph.
 */
	private boolean containsEdge(Edge e) {
		for(Edge next : getEdges()) {
			if(e == next) {
				return(true);
			}
		}
		return(false);
	}



/**
 * Sum the edge weights of edges in the given collection.
 */
	public double sumEdgeWeights(Collection<Edge> edgeCollection) {

		double sum = 0.0;
		for(Edge e : edgeCollection) {
			sum += e.getWeight();
		}
		return(sum);
	}



/**
 * Undirected graph connectedness test by breadth first search. A Breadth First Search
 * is made through the graph starting at any node. After the BFS if there
 * are any nodes that have not been visited then the graph is not connected.
 * This is detected by counting the number of nodes that have appeared
 * in the queue, if is the same as the number in the graph the graph is
 * connected. Here we implement a queue using an ArrayList.
 * @return true if the graph is connected, false if it is unconnected.
 */
	public boolean connected() {

		setNodesVisited();
		int totalNodeNumber = getNodes().size();

		Iterator<Node> ni = getNodes().iterator();
		if(!ni.hasNext()) {
			return(true);
		}
		Node first = (Node)ni.next();
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(first);
		first.setVisited(true);
		int numberTested = 0;
		while(!queue.isEmpty()) {

			Node head = queue.get(0);
			queue.remove(0);

			numberTested++;

			HashSet<Node> neighbours = head.unvisitedConnectingNodes();
			setNodesVisited(neighbours,true);
			queue.addAll(neighbours);
		}

		setNodesVisited();

		if (numberTested == totalNodeNumber) {
			return(true);
		}

		return(false);
	}


	/**
	 * Tree test by breadth first search.
	 * @return true if the graph is a tree, false if it is unconnected.
	 */
	public boolean isTree() {

		if(nodes.size() == 0) {
			return true;
		}

		if(!connected()) {
			return false;
		}

		setNodesVisited(false);
		setEdgesVisited(false);

		Node n = nodes.get(0);
		n.setVisited(true);

		ArrayList<Node> queue = new ArrayList<Node>();

		queue.add(n);

		while(queue.size() != 0) {
			Node head = queue.get(0);
			queue.remove(head);
			HashSet<Edge> connectingEdges = head.connectingEdges();
			for(Edge e : connectingEdges) {
				if(e.getVisited() == false) {
					e.setVisited(true);
					Node neighbour = e.getOppositeEnd(head);
					if(neighbour.getVisited() == true) {
						return false;
					}
					neighbour.setVisited(true);
					queue.add(neighbour);
				}
			}

		}

		return true;
	}


/**
 * Removes the node from the graph and deletes any connecting edges. Accounts
 * for redundant data connecting nodes.
 * @return the success of the operation, failure is due to node not being in the graph.
 */
	public boolean removeNode(Node removeNode) {

		if(!containsNode(removeNode)) {
			return(false);
		}

		HashSet<Edge> edgeCollection = removeNode.connectingEdges();
		for(Edge e : edgeCollection) {
			removeEdge(e);
		}

		nodes.remove(removeNode);

		return(true);
	}


/**
 * Removes the nodes with the given label from the graph and removes
 * any connecting edges. Accounts for redundant data connecting nodes.
 * @return the success of the operation, false if there is no node
 * with the label in the graph.
 */
	public boolean removeNode(String removeString) {

		boolean ret = false;

		boolean delete = true;
		while(delete) {
			delete = false;
			Iterator<Node> ni = nodes.iterator();
			while (ni.hasNext() && !delete) {
				Node n = ni.next();
				if (removeString.equals(n.getLabel())) {
					removeNode(n);
					delete = true;
					ret = true;
				}
			}
		}

		return(ret);
	}


/**
 * Removes the given node collection. Does nothing if the elements
 * in the collection are not nodes in the graph.
 */
	public void removeNodes(Collection<Node> c) {
		for(Node n : c) {
			removeNode(n);
		}
	}


/**
 * Removes the given edge collection. Does nothing if the elements
 * in the collection are not edges in the graph.
 */
	public void removeEdges(Collection<Edge> c) {
		for(Edge e : c) {
			removeEdge(e);
		}
	}


/**
 * Tests the number of edges connecting to all nodes.
 * @return true if all nodes have an even number of connecting edges,
 * false if any are odd.
 */
	private boolean nodesHaveEvenDegree() {

		for(Node n : getNodes()) {
			if(n.degree()%2 != 0) {
				return(false);
			}
		}
		return(true);
	}


/**
 * Removes the edge from the graph. Accounts
 * for redundant data connecting nodes.
 * @return the success of the operation, failure is due to edge not being in the graph.
 */
	public  boolean removeEdge(Edge removeEdge) {

		if(!containsEdge(removeEdge)) {
			return(false);
		}

// this is the best I can do - set the source and target of the
// connecting edge to null, if there are any external references
// to the edge, then their attempt to access the nodes at the ends
// may crash.

		removeEdge.setFromTo(null,null);
		edges.remove(removeEdge);

		return(true);
	}


/**
 * Moves the given node to the end of the list.
 * @return true if the node is moved, false if it is not present.
 */
	public boolean moveNodeToEnd(Node n) {
		if (!containsNode(n)) {
			return(false);
		}
// need to remove the node directly from the list
// to avoid connecting edge removal
		nodes.remove(n);
		nodes.add(n);
		return(true);
	}



/**
 * Moves the given edge to the end of the list.
 * @return true if the edge is moved, false if it is not present.
 */
	public boolean moveEdgeToEnd(Edge e) {
		if (!containsEdge(e)) {
			return(false);
		}
		edges.remove(e);
		edges.add(e);
		return(true);
	}



/**
 * Removes the nodes and edges from the graph, leaving an empty graph.
 * This is a destructive clear as nodes are removed from the graph, so
 * loosing their edge connections.
 */
	public void clear() {
		Iterator<Node> ni = nodes.iterator();
		while(ni.hasNext()) {
			Node n = ni.next();
			removeNode(n);
			ni = nodes.iterator();
		}

	}


/**
 * Saves most of the graph node, edge and associated type information.
 * All edge data saved, but does not save deep node information: node
 * match and node path attributes.
 */
	public boolean saveAll(File file) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

// save the node types
			b.write(FILESTARTNODETYPES);
			b.newLine();

			for(NodeType nt : NodeType.getExistingTypes()) {

				StringBuffer outNodeType = new StringBuffer("");
				outNodeType.append(nt.getLabel());
				outNodeType.append(FILESEPARATOR);
				if(nt.getParent() != null) {
					outNodeType.append(nt.getParent().getLabel());
				}
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getWidth());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getHeight());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getShapeString());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getFillColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getBorderColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getTextColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getStroke().getLineWidth());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedFillColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedBorderColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedTextColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedStroke().getLineWidth());

				b.write(outNodeType.toString());
				b.newLine();
			}


// save the edge types
			b.write(FILESTARTEDGETYPES);
			b.newLine();

			for(EdgeType et : EdgeType.getExistingTypes()) {

				StringBuffer outEdgeType = new StringBuffer("");
				outEdgeType.append(et.getLabel());
				outEdgeType.append(FILESEPARATOR);
				if(et.getParent() != null) {
					outEdgeType.append(et.getParent().getLabel());
				}
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getDirected());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getLineColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getStroke().getLineWidth());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedLineColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedStroke().getLineWidth());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getTextColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedTextColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getPriority());

				b.write(outEdgeType.toString());
				b.newLine();
			}


// save the nodes with generated ids
			b.write(FILESTARTNODES);
			b.newLine();
// use match to save the index, as match cannot be saved
			ArrayList<Node> nodeList  = new ArrayList<Node>(getNodes());
			int index = 0;
			while (index < nodeList.size()){
				Node n = nodeList.get(index);
				n.setMatch(new Integer(index));

				StringBuffer outNode = new StringBuffer("");
				outNode.append(index);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getLabel());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getType().getLabel());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getCentre().x);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getCentre().y);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getVisited());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getScore());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getContour());

				b.write(outNode.toString());
				b.newLine();
				index++;
			}


// save the edges
			b.write(FILESTARTEDGES);
			b.newLine();
			for(Edge e : getEdges()) {
				StringBuffer outEdge = new StringBuffer("");
				outEdge.append(e.getFrom().getMatch().toString());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getTo().getMatch().toString());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getLabel());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getWeight());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getType().getLabel());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getVisited());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getScore());

				outEdge.append(FILESEPARATOR);
				Iterator<Point> bi = e.getBends().iterator();
				while(bi.hasNext()) {
					Point point = (Point)bi.next();
					outEdge.append(point.x);
					outEdge.append(XYSEPARATOR);
					outEdge.append(point.y);
					if(bi.hasNext()) {
						outEdge.append(COORDINATESEPARATOR);
					}
				}

				b.write(outEdge.toString());
				b.newLine();
			}
			b.close();
		}
		catch(IOException e) {
			System.out.println("An IO exception occured when executing saveAll("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;
	}


/**
 * Saves only the node locations and label and the edge labels, the edges
 * linked by node lables, rather than ids, hence the node labels must be unique
 */
	public boolean saveSimple(File file) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

// save the nodes
			b.write(FILESTARTNODES);
			b.newLine();
			for(Node n : getNodes()) {
				StringBuffer outNode = new StringBuffer("");
				outNode.append(n.getLabel());
				outNode.append(SIMPLEFILESEPARATOR);
				outNode.append(n.getCentre().x);
				outNode.append(SIMPLEFILESEPARATOR);
				outNode.append(n.getCentre().y);
				b.write(outNode.toString());
				b.newLine();
			}


// save the edges
			b.write(FILESTARTEDGES);
			b.newLine();
			for(Edge e : getEdges()) {
				StringBuffer outEdge = new StringBuffer("");
				outEdge.append(e.getFrom().getLabel());
				outEdge.append(SIMPLEFILESEPARATOR);
				outEdge.append(e.getTo().getLabel());
				outEdge.append(SIMPLEFILESEPARATOR);
				outEdge.append(e.getLabel());
				b.write(outEdge.toString());
				b.newLine();
			}
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveSimple("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;
	}


/** loads all the graph information and associated types */
	public boolean loadAll(File file) {

		int contourCount = 0;

		clear();

		try {
			Character c = new Character(FILESEPARATOR);
			String separatorString = new String(c.toString());

			c = new Character(COORDINATESEPARATOR);
			String coordinateString = new String(c.toString());

			c = new Character(XYSEPARATOR);
			String xyString = new String(c.toString());

			boolean readingNodeTypes = false;
			boolean readingEdgeTypes = false;
			boolean readingEdges = false;
			boolean readingNodes = false;

			ArrayList<Node> nodeList = new ArrayList<Node>();

			BufferedReader b = new BufferedReader(new FileReader(file));
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}

				if(readingNodeTypes && !line.equals(FILESTARTEDGETYPES)) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;
// get label
					separatorInd = parseLine.indexOf(separatorString);
					String nodeLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
//check for current node type
					NodeType nt = NodeType.withLabel(nodeLabel);
					if (nt == null) {
// if not already in graph- node type label can only be set by constructor
						nt = new NodeType(nodeLabel);
					}

// get parent
					separatorInd = parseLine.indexOf(separatorString);
					String parentLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
//check if parent exists
					if(!parentLabel.equals("")) {
						NodeType pt = NodeType.withLabel(parentLabel);
						if (pt == null) {
// if parent is not already in graph- create a new node type, if the type is loaded
// later then the default settings should be overridden
							pt = new NodeType(parentLabel);
						}
						nt.setParent(pt);
					}

					Integer rgb = null;
					Integer size = null;
					Float width = null;

// get node type width
					separatorInd = parseLine.indexOf(separatorString);
					size = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setWidth(size.intValue());
					parseLine.delete(0,separatorInd+1);

// get node type height
					separatorInd = parseLine.indexOf(separatorString);
					size = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setHeight(size.intValue());
					parseLine.delete(0,separatorInd+1);

// get node type shape
					separatorInd = parseLine.indexOf(separatorString);
					String shapeString = parseLine.substring(0,separatorInd);
					nt.setShapeString(shapeString);
					parseLine.delete(0,separatorInd+1);

// get node type fill color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setFillColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type border color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setBorderColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					nt.setStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

// get node type selected fill color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedFillColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type selected border color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedBorderColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type selected text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get node type selected stroke, the last attribute
					width = new Float(Float.parseFloat(parseLine.toString()));
					nt.setSelectedStroke(new BasicStroke(width.floatValue()));


				}

				if(readingEdgeTypes && !line.equals(FILESTARTNODES)) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

// get edge type label
					separatorInd = parseLine.indexOf(separatorString);
					String edgeLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
//check for current edge type
					EdgeType et = EdgeType.withLabel(edgeLabel);
					if (et == null) {
// if not already in graph- edge type label can only be set by constructor
						et = new EdgeType(edgeLabel);
					}

// get edge type parent
					separatorInd = parseLine.indexOf(separatorString);
					String parentLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
//check if parent exists
					if(!parentLabel.equals("")) {
						EdgeType pt = EdgeType.withLabel(parentLabel);
						if (pt == null) {
// if parent is not already in graph- create a new edge type, if the type is loaded
// later then the default settings should be overridden
							pt = new EdgeType(parentLabel);
						}
						et.setParent(pt);
					}

					Integer rgb = null;
					Float width = null;


// get edge type directed value
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						et.setDirected(true);
					} else {
						et.setDirected(false);
					}
					parseLine.delete(0,separatorInd+1);

// get edge type line color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setLineColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get edge type stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					et.setStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

// get edge type selected line color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setSelectedLineColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// get edge type selected stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					et.setSelectedStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

// edge type text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

// edge type selected text color
					separatorInd = parseLine.indexOf(separatorString);
					if(separatorInd == -1) {
						rgb = new Integer(Integer.parseInt(parseLine.toString()));
						et.setSelectedTextColor(new Color(rgb.intValue()));
					} else {
						rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
						et.setSelectedTextColor(new Color(rgb.intValue()));
						parseLine.delete(0,separatorInd+1);

						Integer priority = new Integer(Integer.parseInt(parseLine.toString()));
						if(priority.intValue() != -1) {
							et.setPriority(priority.intValue());
						}
					}

				}

				if(readingNodes && !line.equals(FILESTARTEDGES)) {
					Node n = new Node();
					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

// get node id used for edges later
					separatorInd = parseLine.indexOf(separatorString);
					Integer index = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setMatch(index);
					parseLine.delete(0,separatorInd+1);

// get node label
					separatorInd = parseLine.indexOf(separatorString);
					String nodeLabel = parseLine.substring(0,separatorInd);
					n.setLabel(nodeLabel);
					parseLine.delete(0,separatorInd+1);


// get node type and find it in the node type list
					separatorInd = parseLine.indexOf(separatorString);
					String typeLabel = parseLine.substring(0,separatorInd);
					NodeType nt = NodeType.withLabel(typeLabel);
					if(nt != null) {
						n.setType(nt);
					}
					parseLine.delete(0,separatorInd+1);


// get node x coord
					separatorInd = parseLine.indexOf(separatorString);
					Integer x = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setX(x.intValue());
					parseLine.delete(0,separatorInd+1);

// get node y coord
					separatorInd = parseLine.indexOf(separatorString);
					Integer y = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setY(y.intValue());
					parseLine.delete(0,separatorInd+1);

// get node visited flag
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						n.setVisited(true);
					} else {
						n.setVisited(false);
					}
					parseLine.delete(0,separatorInd+1);

// get node score, might be last attribute in old files
					separatorInd = parseLine.indexOf(separatorString);
					if(separatorInd == -1) {
						Double s = new Double(Double.parseDouble(parseLine.toString()));
						n.setScore(s.doubleValue());
						String contour = Character.toString((char)('a'+contourCount));
						n.setContour(contour);
						contourCount++;
					} else {
						Double s = new Double(Double.parseDouble(parseLine.substring(0,separatorInd)));
						n.setScore(s.doubleValue());
						parseLine.delete(0,separatorInd+1);

						String contour = parseLine.toString();
						n.setContour(contour);
					}

// get node contour


					addNode(n);

					int indexInt = index.intValue();

					while(nodeList.size() <= indexInt) {
						nodeList.add(null);
					}

					nodeList.set(indexInt,n);
				}

				if(readingEdges) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

// get id of first node
					separatorInd = parseLine.indexOf(separatorString);
					Integer i1 = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					parseLine.delete(0,separatorInd+1);

// get id of second node
					separatorInd = parseLine.indexOf(separatorString);
					Integer i2 = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					parseLine.delete(0,separatorInd+1);

					Edge e = new Edge(nodeList.get(i1.intValue()),nodeList.get(i2.intValue()));

// get edge label
					separatorInd = parseLine.indexOf(separatorString);
					String edgeLabel = parseLine.substring(0,separatorInd);
					e.setLabel(edgeLabel);
					parseLine.delete(0,separatorInd+1);

// get edge weight
					separatorInd = parseLine.indexOf(separatorString);
					Double w = new Double(Double.parseDouble(parseLine.substring(0,separatorInd)));
					e.setWeight(w.doubleValue());
					parseLine.delete(0,separatorInd+1);

// get edge type and find it in the edge type list
					separatorInd = parseLine.indexOf(separatorString);
					String typeLabel = parseLine.substring(0,separatorInd);
					EdgeType et = EdgeType.withLabel(typeLabel);
					if(et != null) {
						e.setType(et);
					}
					parseLine.delete(0,separatorInd+1);

// get edge visited flag
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						e.setVisited(true);
					} else {
						e.setVisited(false);
					}
					parseLine.delete(0,separatorInd+1);

// get edge score, may be the last attribute
					separatorInd = parseLine.indexOf(separatorString);
					if(separatorInd == -1) {
						String scoreString = parseLine.toString();
						Double s = new Double(Double.parseDouble(scoreString));
						e.setScore(s.doubleValue());
					} else {
						String scoreString = parseLine.substring(0,separatorInd);
						Double s = new Double(Double.parseDouble(scoreString));
						e.setScore(s.doubleValue());
						parseLine.delete(0,separatorInd+1);

// if edge score is not the last attribute, then the edge bends are all that are left in parseLine

						while(parseLine.length() != 0) {
							separatorInd = parseLine.indexOf(coordinateString);
							String pairString = null;
							if(separatorInd == -1) {
								separatorInd = parseLine.length();
							}
							pairString = parseLine.substring(0,separatorInd);
							parseLine.delete(0,separatorInd+1);
							int xySeparatorInd = pairString.indexOf(xyString);
							String xString = pairString.substring(0,xySeparatorInd);
							String yString = pairString.substring(xySeparatorInd+1);

							Integer ix = new Integer(Integer.parseInt(xString));
							Integer iy = new Integer(Integer.parseInt(yString));
							Point bend = new Point(ix.intValue(),iy.intValue());
							e.addBend(bend);
						}
					}


					addEdge(e);

				}

				if(line.equals(FILESTARTNODETYPES)) {
					readingNodeTypes = true;
					readingEdgeTypes = false;
					readingEdges = false;
					readingNodes = false;
				}
				if(line.equals(FILESTARTEDGETYPES)) {
					readingNodeTypes = false;
					readingEdgeTypes = true;
					readingEdges = false;
					readingNodes = false;
				}
				if(line.equals(FILESTARTEDGES)) {
					readingNodeTypes = false;
					readingEdgeTypes = false;
					readingEdges = true;
					readingNodes = false;
				}
				if(line.equals(FILESTARTNODES)) {
					readingNodeTypes = false;
					readingEdgeTypes = false;
					readingEdges = false;
					readingNodes = true;
				}
				line = b.readLine();

			}
			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAll("+file+") in Graph.java: "+e+"\n");
			return false;
		}

		return true;


	}




/**
 * This saves an adjacency list graph file. The file is in the form of a
 * simple adjacency list, each line
 * of the file is an edge, with nodes separated by the {@link #ADJACENCYSEPARATOR}
 * character. Any empty line
 * or line with more than one colon is ignored.
 * Nodes are assumed to have unique labels for the purpose of this file writer
 * if there are duplicates, they will be merged to the same node.
 */
	public void saveAdjacencyFile(String fileName) {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(fileName));
			HashSet<Node> remainingNodes = new HashSet<Node>(getNodes());
			StringBuffer s = new StringBuffer();
			for(Edge e : getEdges()){
				s.setLength(0);
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				s.append(n1.getLabel()).append(ADJACENCYSEPARATOR).append(n2.getLabel());
				b.write(s.toString());
				b.newLine();

				remainingNodes.remove(n1);
				remainingNodes.remove(n2);
			}
//deal with singleton nodes
			for(Node n : remainingNodes) {
				b.write(n.toString());
				b.newLine();
			}

			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveAdjacencyFile("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}
	}



/**
 * This loads the given adjacency list graph file into the graph, deleting any
 * current nodes and edges. The file is in the form of a simple adjacency list, each line
 * of the file is an edge, with nodes separated by the {@link #ADJACENCYSEPARATOR}
 * character. Any empty line
 * or line with more than one colon is ignored.
 * Nodes are assumed to have unique labels for the purpose of this loader.
 * may appear in each line. Any non empty line without a colon is
 * considered to be a node with no connecting edges.
 * @return true if successful, false if there was a formatting problem
 */
	public boolean loadAdjacencyFile(String fileName) {

		clear();
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line = b.readLine();
			while(line != null) {

				int separatorInd = line.indexOf(ADJACENCYSEPARATOR);

// ignore any empty lines and lines with more than 1 separator
				if(line.length() > 0 && separatorInd == line.lastIndexOf(ADJACENCYSEPARATOR)) {
					if(separatorInd >= 0) {
						String n1 = line.substring(0,separatorInd);
						String n2 = line.substring(separatorInd+1);
						addAdjacencyEdge(n1,n2);
					} else {
// no separator, so just add a singleton node
						addAdjacencyNode(line);
					}
				}
				line = b.readLine();
			}

			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAdjacencyFile("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}

		return(true);
	}


/**
 * Creates an edge between the nodes with the given labels. This is the unweighted
 * unlabelled edge version of the method.
 * @return the created edge, or null if was not created.
 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel) {

		return(addAdjacencyEdge(fromLabel, toLabel, "", 0.0));
	}


/**
 * Creates an edge between the nodes with the given labels. This is the
 * unlabeled edge version of the method.
 * @return the created edge, or null if was not created.
 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel, double edgeWeight) {

		return(addAdjacencyEdge(fromLabel, toLabel, "", edgeWeight));
	}



/**
 * Creates an edge between the nodes with the given labels, creating the nodes if needed.
 * If a node label is empty, the node is not created. If one or both the labels are empty, the
 * edge is not created. Self sourcing and parallel edges are allowed. Third argument is the
 * edge label, fourth is the edge weight.
 * @return the created edge, or null if was not created.
 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel, String edgeLabel, double edgeWeight) {

		Node fromNode = null;
		Node toNode = null;

		if(!fromLabel.equals("")) {
			fromNode = addAdjacencyNode(fromLabel);
		}

		if(!toLabel.equals("")) {
			toNode = addAdjacencyNode(toLabel);
		}

		if(fromNode != null && toNode != null) {

			Edge e = new Edge(fromNode, toNode, edgeLabel, edgeWeight);
			addEdge(e);
			return(e);
		}

		return(null);
	}


/**
 * Either returns an existing node with the label, or if there is none
 * creates a node with the label and adds it to the graph.
 * @return the found or created node, or null if there is more than one node with the label.
 */
	public Node addAdjacencyNode(String nodeLabel) {

		Node returnNode = null;
		for(Node n : getNodes()) {
			if(nodeLabel.equals(n.getLabel())) {
				if(returnNode != null) {
					return(null);
				}
				returnNode = n;
			}
		}
		if (returnNode != null) {
			return(returnNode);
		}

		Node newNode = new Node(nodeLabel);
		addNode(newNode);

		return(newNode);
	}


/**
 * Finds the list of Nodes with the given label.
 */
	public ArrayList<Node> findNodesWithLabel(String nodeLabel) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : getNodes()) {
			if(nodeLabel.equals(n.getLabel())) {
				ret.add(n);
			}
		}
		return(ret);
	}

/**
 * Finds the list of Nodes with the given label.
 */
	public ArrayList<Node> findNodesWithScore(double score) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : getNodes()) {
			if(score == n.getScore()) {
				ret.add(n);
			}
		}
		return(ret);
	}
	
/**
 * Generates a complete graph of the size of the number of nodes passed.
 * The old graph gets deleted. The nodes are labeled 1 to the size of the
 * graph, the edges are not labeled.
 */
	public void generateCompleteGraph(int nodeNumber) {

		clear();
		if(nodeNumber==1) {
			addAdjacencyNode("1");
		}
		for(int i = 1; i <= nodeNumber; i++) {
			for(int j = 1; j <= nodeNumber; j++) {
				if(i<j) {
					Integer nodei = new Integer(i);
					Integer nodej = new Integer(j);
					addAdjacencyEdge(nodei.toString(),nodej.toString());
				}
			}
		}
	}



	/**
	 * Check to see if two nodes with the given labels are already connected in
	 * the graph.
	 */
	private boolean nodeLabelsAlreadyConnected(String label1, String label2) {
		Node n1 = null;
		for(Node n : getNodes()) {
			if(label1.equals(n.getLabel())) {
				n1 = n;
				break;
			}
		}

		if(n1 != null) {
			for(Node connectedN : n1.connectingNodes()) {
				if(label2.equals(connectedN.getLabel())) {
					return true;

				}
			}
		}
		return false;
	}

/**
 * Generates a random graph of approximately the size of nodes and edges
 * passed. It does this by generating edges between random nodes, which
 * are labelled with the relevant numbers. Only these attached nodes get
 * created, so that the number of nodes may be less than that passed.
 * The old graph gets deleted.
 */
	public void generateRandomGraph( int nodeNumber, 
			                         int edgeNumber, 
			                         long seed,
			                         boolean selfSourcing, 
			                         boolean parallel) {

		clear();
		Random r = new Random(seed);
		for(int i = 0; i < edgeNumber; i++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!parallel) {
				if(nodeLabelsAlreadyConnected(node1.toString(),node2.toString())) {
					continue;
				}
			}
			if(!node1.equals(node2) || selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			}
		}
	}

	public void generateRandomGraph(int nodeNumber, int edgeNumber, long seed) {
		generateRandomGraph(nodeNumber,edgeNumber, seed, false,true);
	}

/**
 * Generates a random graph of exactly the size of nodes and edges
 * passed, by creating singleton nodes.
 */
	public void generateRandomGraphExact(int nodeNumber, int edgeNumber, boolean selfSourcing) {

		clear();
		for(int i = 0; i < nodeNumber; i++) {
			Integer node = new Integer(i);
			addNode(new Node(node.toString()));
		}
		Random r = new Random();
		for(int i = 0; i < edgeNumber; i++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!node1.equals(node2) || selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			}
		}
	}


/**
 * Randomizes the graph node locations to be within the rectangle defined
 * by the parameters.
 */
	public void randomizeNodePoints(Point topleft, int width, int height, long seed) {

		Random r = new Random(seed);
		for(Node n : getNodes()) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			n.setCentre(new Point(topleft.x+x,topleft.y+y));
		}
		return;
	}

/**
 * Randomizes the graph node locations to be within the rectangle defined
 * by the parameters.
 */
	public void randomizeNodePoints(Point topleft, int width, int height) {
		long seed = System.currentTimeMillis();
		randomizeNodePoints(topleft,width,height,seed);
	}

/**
 * Randomizes the graph node locations to be within the rectangle defined
 * by the parameters.
 */
	public void randomizeNodePoints(Point topleft, int width, int height, int randomSeed) {
		Random r = new Random(randomSeed);
		for(Node n : getNodes()) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			n.setCentre(new Point(topleft.x+x,topleft.y+y));
		}
		return;
	}



/** Compares the graph based on node labels and the node labels that edges
 * connect to, it works as a way of comparing graphs when nodes have
 * unique labels and no edge labels. An isomorphism test is required where
 * duplicate labels are present or only the topology is of interest.
 * @return true if the graphs are equal by node label and edge connections,
 * false otherwise.
 */
	public boolean equalsByNodeLabel(Graph g) {

		String n1[] = new String[getNodes().size()];

// first test that the two graphs have the same number of node labels
		int i1 = 0;
		for(Node n : this.getNodes()) {
			n1[i1] = n.getLabel();
			i1++;
		}

		String n2[] = new String[g.getNodes().size()];

		int i2 = 0;
		for(Node n : g.getNodes()) {
			n2[i2] = n.getLabel();
			i2++;
		}

		Arrays.sort(n1);
		Arrays.sort(n2);

		if (!Arrays.equals(n1,n2)) {
			return(false);
		}

// next test that there are the same number of edges going to and from nodes
// with the same labels
		String e1[] = new String[this.getEdges().size()];

		int j1 = 0;
		for(Edge e : this.getEdges()) {
			e1[j1] = new String(e.getFrom().getLabel()+":"+e.getTo().getLabel());
			j1++;
		}

		String e2[] = new String[g.getEdges().size()];

		int j2 = 0;
		for(Edge e : g.getEdges()) {
			e2[j2] = new String(e.getFrom().getLabel()+":"+e.getTo().getLabel());
			j2++;
		}

		Arrays.sort(e1);
		Arrays.sort(e2);

		if (!Arrays.equals(e1,e2)) {
			return(false);
		}

		return(true);
	}


	/**
	 * Finds the first node in the graph with the given label.
	 * @return null if there is no such node, or if there is more than one.
	 */
	public Node firstNodeWithLabel(String nodeLabel) {

		Node returnNode = null;
		boolean found = false;

		Iterator<Node> ni = getNodes().iterator();
		while(ni.hasNext() && !found) {
			Node n = (Node)ni.next();
			if(nodeLabel.equals(n.getLabel())) {
				returnNode = n;
				found = true;
			}
		}

		return(returnNode);

	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ADJACENCYSEPARATOR;
        result = prime * result + COORDINATESEPARATOR;
        result = prime * result + FILESEPARATOR;
        result = prime * result
                + ((FILESTARTEDGES == null) ? 0 : FILESTARTEDGES.hashCode());
        result = prime * result + ((FILESTARTEDGETYPES == null) ? 0
                : FILESTARTEDGETYPES.hashCode());
        result = prime * result
                + ((FILESTARTNODES == null) ? 0 : FILESTARTNODES.hashCode());
        result = prime * result + ((FILESTARTNODETYPES == null) ? 0
                : FILESTARTNODETYPES.hashCode());
        result = prime * result + SIMPLEFILESEPARATOR;
        result = prime * result + WEIGHTEDSEPARATOR;
        result = prime * result + XYSEPARATOR;
        long temp;
        temp = Double.doubleToLongBits(bestCount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((bestPath == null) ? 0 : bestPath.hashCode());
        result = prime * result + ((edges == null) ? 0 : edges.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
        result = prime * result + Arrays.hashCode(stack);
        result = prime * result + stackp;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + Arrays.hashCode(trail);
        result = prime * result + trailp;
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
        Graph other = (Graph) obj;
        if (ADJACENCYSEPARATOR != other.ADJACENCYSEPARATOR)
            return false;
        if (COORDINATESEPARATOR != other.COORDINATESEPARATOR)
            return false;
        if (FILESEPARATOR != other.FILESEPARATOR)
            return false;
        if (FILESTARTEDGES == null) {
            if (other.FILESTARTEDGES != null)
                return false;
        } else if (!FILESTARTEDGES.equals(other.FILESTARTEDGES))
            return false;
        if (FILESTARTEDGETYPES == null) {
            if (other.FILESTARTEDGETYPES != null)
                return false;
        } else if (!FILESTARTEDGETYPES.equals(other.FILESTARTEDGETYPES))
            return false;
        if (FILESTARTNODES == null) {
            if (other.FILESTARTNODES != null)
                return false;
        } else if (!FILESTARTNODES.equals(other.FILESTARTNODES))
            return false;
        if (FILESTARTNODETYPES == null) {
            if (other.FILESTARTNODETYPES != null)
                return false;
        } else if (!FILESTARTNODETYPES.equals(other.FILESTARTNODETYPES))
            return false;
        if (SIMPLEFILESEPARATOR != other.SIMPLEFILESEPARATOR)
            return false;
        if (WEIGHTEDSEPARATOR != other.WEIGHTEDSEPARATOR)
            return false;
        if (XYSEPARATOR != other.XYSEPARATOR)
            return false;
        if (Double.doubleToLongBits(bestCount) != Double
                .doubleToLongBits(other.bestCount))
            return false;
        if (bestPath == null) {
            if (other.bestPath != null)
                return false;
        } else if (!bestPath.equals(other.bestPath))
            return false;
        if (edges == null) {
            if (other.edges != null)
                return false;
        } else if (!edges.equals(other.edges))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (nodes == null) {
            if (other.nodes != null)
                return false;
        } else if (!nodes.equals(other.nodes))
            return false;
        if (!Arrays.equals(stack, other.stack))
            return false;
        if (stackp != other.stackp)
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (!Arrays.equals(trail, other.trail))
            return false;
        if (trailp != other.trailp)
            return false;
        return true;
    }
    /**
	 * Finds the first node in the graph with the given contour.
	 * @return null if there is no such node, or if there is more than one.
	 */
	public Node firstNodeWithContour(String contour) {

		Node returnNode = null;
		boolean found = false;

		Iterator<Node> ni = getNodes().iterator();
		while(ni.hasNext() && !found) {
			Node n = (Node)ni.next();
			if(contour.equals(n.getContour())) {
				returnNode = n;
				found = true;
			}
		}

		return(returnNode);

	}


/**
 * Find the shortest path between two nodes. Treats the graph as undirected and
 * unweighted.
 * <p>
 * This is a breadth first search through the graph. A queue is used for this,
 * FIFO - First In First Out. Add the start node to the queue, then keep taking
 * the head of the queue and adding the heads neighbours until the end node
 * has been found. In this case we also have to keep track of the shortest paths to
 * the nodes we have detected using the path facility provided in the node class.
 * <p>
 * There is no actual Queue class or interface in Java, but anything which
 * implements 'List' will do, as its possible to add to the end of the list, and
 * treat the head of the list as index 0, and use the remove methods.
 * Here we are using a ArrayList, which implements 'List'.
 *
 * @return null if there is no path between the nodes, if there is a path it returns
 * a list of nodes representing the path, including both end nodes.
 */
	public ArrayList<Node> unweightedShortest(Node startNode, Node goal) {

		Node head = null;
		ArrayList<Node> path = new ArrayList<Node>();
		ArrayList<Node> queue = new ArrayList<Node>();
		HashMap<Node,ArrayList<Node>> paths = new HashMap<Node,ArrayList<Node>>();
		for(Node n : getNodes()) {
			paths.put(n,null);
		}

		queue.add(startNode);
		paths.put(startNode,path);

		while(!queue.isEmpty() && head != goal) {
//System.out.println("paths "+paths);
//System.out.println("queue "+queue);
			head = queue.remove(0);

			ArrayList<Node> headPath = paths.get(head);
			path = new ArrayList<Node>(headPath);
			path.add(head);

			HashSet<Node> neighbours = head.connectingNodes();
			for(Node n : neighbours) {
				if(paths.get(n) == null) {
					paths.put(n,path);
					queue.add(n);
				}
			}
		}

		if (head == goal) {
			ArrayList<Node> result = new ArrayList<Node>(paths.get(goal));
			result.add(goal);

//System.out.println(result);
			return(result);
		}

		return(null);

	}



/**
 * Find a Euler circuit in the graph, by repeatedly tracing closed trails in the graph
 * using {@link #trace}
 * @return a list of nodes which indicates a circuit, or null if no trail is present.
 */
	public ArrayList<Node> euler() {

		if (!eulerian()) {
			return(null);
		}

		setEdgesVisited(false);

// get any node
		Node n;
		Iterator<Node> ni = getNodes().iterator();
		if(ni.hasNext()) {
			n = (Node)ni.next();
			if(!ni.hasNext() && getEdges().size() == 0) {
// one node and no edges so return the empty list
				return(new ArrayList<Node>());
			}
		} else {
// no nodes so return the empty list
			return(new ArrayList<Node>());
		}

		ArrayList<Node> K = trace(n);

		int currentIndex = 0;
		while (currentIndex < K.size()) {

			Node currentNode = K.get(currentIndex);
			if(currentNode.unvisitedConnectingEdges().size() > 0) {
				ArrayList<Node> C = trace(currentNode);
				K.remove(currentIndex);
				K.addAll(currentIndex,C);
			}
			currentIndex++;
		}

		return(K);
	}




	private int trailp = 0;
	private Edge trail[] = null;
	private int stackp = 0;
	private Edge[] stack = null;
/**
 * Find a Euler circuit in the graph, using SMK algorithm
 * @return a list of nodes which indicates a circuit, or null if no trail is present.
 */
	public ArrayList<Node> eulerSMK(boolean recursiveVersion) {

		if (!eulerian()) {
			return(null);
		}

		setEdgesVisited(false);

// get any node
		Node startVertex;
		Iterator<Node> ni = getNodes().iterator();
		if(ni.hasNext()) {
			startVertex = ni.next();
			if(!ni.hasNext() && getEdges().size() == 0) {
				return(new ArrayList<Node>());
			}
		} else {
			return(new ArrayList<Node>());
		}

		trailp = 0;
		trail = new Edge[edges.size()];
		stackp = 0;
		stack = new Edge[edges.size()];

		if(recursiveVersion) {
			findEulerTrailSMK(startVertex);
		} else {
			findEulerTrailSMK2(startVertex);
		}

		// this stuff converts from an array of edges
		// to a list of nodes
		ArrayList<Node> ret = new ArrayList<Node>();
		if(trail.length == 1) {
			Edge singleEdge = trail[0];
			ret.add(singleEdge.getFrom());
			ret.add(singleEdge.getTo());
			return ret;
		}


		// try it twice, as the from and to nodes as the start node.
		Edge e = trail[0];
		Node n = e.getFrom();
		boolean success = true;
		for(int i = 0; i < trail.length; i++) {
			e = trail[i];
			ret.add(n);
			if(e.getFrom() == n) {
				n = e.getTo();
			} else if(e.getTo() == n) {
				n = e.getFrom();
			} else {
				// cant complete a full trail, must have the other node as start node
				success = false;
				break;
			}
		}
		if(!success) {
			// try it starting at the to node
			ret = new ArrayList<Node>();
			e = trail[0];
			n = e.getTo();
			for(int i = 0; i < trail.length; i++) {
				e = trail[i];
				ret.add(n);
				if(e.getFrom() == n) {
					n = e.getTo();
				} else if(e.getTo() == n) {
					n = e.getFrom();
				}
			}
		}
		ret.add(n);
/*
for(int i = 0; i < trail.length; i++) {
	e = trail[i];
	System.out.print(e+" ");
}
System.out.println();
System.out.println("node list "+ret);
*/
		return ret;
	}


	private Edge findEdgeSMK(Node node) {
		for(Edge e : node.unvisitedConnectingEdges()) {
			e.setVisited(true);
			return e;
		}
		return null;
	}

	/** recursive version */
	private void findEulerTrailSMK(Node vertexA) {

		Edge edge = findEdgeSMK(vertexA);
		if(edge == null) {
			return;
		}
		Node vertexB = edge.getOppositeEnd(vertexA);
		findEulerTrailSMK(vertexB);
		trail[trailp++] = edge;
		findEulerTrailSMK(vertexA);

		return;
	}


	/** Non recursive version */
    public void findEulerTrailSMK2(Node node) {
        Edge e;
        for(;;){
            e=findEdgeSMK(node);
            if (e != null) {
        		node = e.getOppositeEnd(node);
                stack[stackp++]=e;
            } else if (stackp==0) return;
            else {
                trail[trailp++]=e=stack[--stackp];
        		node = e.getOppositeEnd(node);
            }
        }
    }

/**
 * Returns a list of nodes that form a circuit. In this case it is
 * a loop that does not use the same edge twice and where the edge has
 * not already been visited. It finds a circuit that includes all the
 * unvisited edges from the argument node by only finishing when the
 * argument node has no more unconnected edges. It maintains the
 * visited edges, but not the nodes  as it goes. It does not reset
 * the visited edges at the start of the method because it is expected
 * to be called several times to build up the tour as designed for use
 * by a Euler circuit method. The graph must be eulerian.
 * @return a list of nodes which indicates a circuit.
 */
	public ArrayList<Node> trace(Node startNode) {

		ArrayList<Node> C = new ArrayList<Node>();
		Node currentNode = startNode;
		C.add(currentNode);
		boolean moreEdges = true;
		while(moreEdges) {
			Iterator<Edge> ei = currentNode.unvisitedConnectingEdges().iterator();
			if(!ei.hasNext()) {
				moreEdges = false;
			} else {

				Edge e = (Edge)ei.next();
				e.setVisited(true);
				currentNode = e.getOppositeEnd(currentNode);
				C.add(currentNode);
			}
		}

		return(C);
	}



/**
 * Test for the graph having a euler tour. Graph must be connected, and all
 * nodes have even degree.
 */
	public boolean eulerian() {

		if(!nodesHaveEvenDegree()) {
			return(false);
		}
		if(!connected()) {
			return(false);
		}
		return(true);
	}



/**
 * Loads in a tour in list string format and returns the equivalent ArrayList
 * If the file contains the string "Fail" then the tour is set to null.
 */
	public ArrayList<Node> loadTour(String fileName) {

		String tourString = "";
		ArrayList<Node> tour = new ArrayList<Node>();

		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line = b.readLine();
			while(line != null) {

				if(line !=null && !line.equals("")) {
					tourString = line;
				}

				line = b.readLine();
			}

			b.close();
			StringTokenizer st = new StringTokenizer(tourString,"	 ,[]");

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if(token.compareToIgnoreCase("FAIL") == 0) {
					tour = null;
					break;
				} else {
// this bit finds a node with the appropriate label
					boolean fail = true;
					for(Node n : getNodes()) {
						if(token.equals(n.getLabel())) {
							tour.add(n);
							fail = false;
							break;
						}
					}
					if (fail) {
						System.out.println("ERROR WHEN LOADING TOUR, node "+token+" not found in graph");
						tour = null;
					}
				}
			}

		} catch(IOException e){
			System.out.println("An IO exception occured when executing EulerTest.loadTour("+fileName+") in EulerTest.java: "+e+"\n");
			System.exit(1);
		}

		return(tour);
	}



/**
 * Saves the given tour in the List string format.
 */
	public void saveTour(String fileName, AbstractList<Node> tour) {
		saveTour(fileName, tour, true);
	}


/**
 * Saves the given tour in the List string format, if the boolean is false
 * only the string "Fail" is written.
 */
	public void saveTour(String fileName, AbstractList<Node> tour, boolean succeed) {

		try {
			String outString;
			if (succeed == false) {
				outString = "Fail";
			} else {
				outString = tour.toString();
			}
			BufferedWriter b = new BufferedWriter(new FileWriter(fileName));
			b.write(outString);
			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveTour("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}
	}



/**
 * Checks for existence of tour, array list must just be null if there
 * is no tour.
 *
 * NOTE BUG - [A] can be given as a correct tour for the graph with
 * just the node A and no edges. The correct result is only [].
 *
 * @return true if the tour if it is in the graph, false if it is not.
 */
	public boolean eulerTourInGraph(ArrayList<Node> tour) {

		if (!eulerian()) {
// not a eulerian graph
			if (tour == null) {
				return(true);
			} else {
				return(false);
			}
		}

		if(tour == null) {
// no tour given, but graph is eulerian
			return(false);
		}

		setEdgesVisited();

		Node first = null;
		Node prev = null;
		Node next = null;

		HashSet<Edge> edgeSet;

		Iterator<Node> ti = tour.iterator();
		if(!ti.hasNext() && (getNodes().size() > 1)) {
// Tour is empty, but the graph has a node
			return(false);
		}

		while(ti.hasNext()) {
			prev = next;
			next = (Node)ti.next();
			if(!containsNode(next)) {
// Tour node not found in the graph
				return(false);
			}

			if(prev == null) {
				first = next;
			} else {
				edgeSet = prev.unvisitedConnectingEdges();
				Edge connectingEdge = null;
				for(Edge e : edgeSet) {
					if(e.getOppositeEnd(prev) == next) {

						e.setVisited(true);
						connectingEdge = e;
						break;
					}
				}
				if(connectingEdge == null) {
// No more unvisited edges connecting the two nodes
					return(false);
				}
			}
		}

		for(Edge edge : getEdges()) {
			if(edge.getVisited() == false) {
// not all edges have been visited
				return(false);
			}
		}

		if (first != next) {
// start node not last node
			return(false);
		}

 		return(true);
	}



/**
 * Generate Eulerian graph by randomly generating a graph of the size given,
 * then adding and removing edges till its eularian. The delete and add is random,
 * but attempts to be a bit clever, tending towards nodes of even degree. Clearly
 * too small number of edges might cause problems, but it will
 * get there in the end. The selfSourcing parameter indicates if edges sourced and
 * targetted at the same node are allowed. The parallel parameter indicates if
 * parallel edges are required. However, it must be noted that to make the graph
 * Eulerian, some parallel edges may be added.
 */
	public void generateRandomEulerGraph(int nodeNumber, 
			                             int edgeNumber, 
			                             long seed, 
			                             boolean selfSourcing, 
			                             boolean parallel) {

		clear();

		Node newNode = null;
		StringBuffer newString = new StringBuffer();
		for(int i = 0; i < nodeNumber; i++) {
			newString.setLength(0);
			newString.append(i);
			newNode = new Node(newString.toString());
			addNode(newNode);
		}

		Random r = new Random(seed);
		for(int j = 0; j < edgeNumber; j++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!parallel) {
				if(nodeLabelsAlreadyConnected(node1.toString(),node2.toString())) {
					continue;
				}
			}
			if(selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			} else {
				if(!node1.equals(node2)) {
					addAdjacencyEdge(node1.toString(),node2.toString());
				}
			}
		}

// tidy up the graph to ensure the graph is connected and that the
// nodes have even degree

// make connected
		setNodesVisited();

		boolean connected = false;
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(newNode);
		Node head = null;

		while(!connected) {
			while(!queue.isEmpty()) {
				head = queue.remove(0);
				head.setVisited(true);
				HashSet<Node> neighbours = head.unvisitedConnectingNodes();
				queue.addAll(neighbours);
			}

			HashSet<Node> unvisited = unvisitedNodes();
			if (unvisited.size() == 0) {
				connected = true;
			} else {
				Iterator<Node> ni = unvisited.iterator();
				Node n = ni.next();
				addEdge(new Edge(head,n));
				queue.add(n);
			}
		}

// add edges to nodes with odd degree
		Node source = null;
		for(Node n : getNodes()) {
			int degree = n.degree();

			if(degree%2 != 0) {
				if(source == null) {
					source = n;
				} else {
					addEdge(new Edge(source,n));
					source = null;
				}
			}
		}
	}

    public void generateRandomEulerGraph(int nodeNumber, int edgeNumber, long seed) {
	    generateRandomEulerGraph(nodeNumber, edgeNumber, seed, false, true);
    }

/**
 * Adds random weights to the edges of a graph, value is between
 * (inclusive) the arguments.
 */
	public void randomlyWeightGraph(int from, int to) {
		Random r = new Random();
		for(Edge e : getEdges()) {
			int value = from+r.nextInt(to-from);
			e.setWeight(value);
		}
	}



/**
 * If the passed node2 is a match for node1, taking into account
 * the current matched state of neighbouring nodes.
 * then this method returns true, false if it is not a possible match.
 * Accounts for current connections to matched nodes, both must have
 * the same number of self sourcing edges. This method takes
 * no account of node or edge lables or edge direction.
 */
	protected boolean isAnUndirectedMatch(Node node1, Node node2) {

		if (node1.getMatch() != null) {
			return(false);
		}
		if (node2.getMatch() != null) {
			return(false);
		}

// this should not be needed given a reasonably efficient
// filtering before the backtracking
		if(node1.connectingNodes().size() != node2.connectingNodes().size()) {
			return(false);
		}

		HashSet<Node> matched1 = setNodesNeighboursScoresForIsomorphism(node1);
		HashSet<Node> matched2 = setNodesNeighboursScoresForIsomorphism(node2);

// check current matches are valid, i.e. same number and connect to non
// conflicting nodes compare the scores of the two sets
// this deals with self sourcing edges as well as
// normal matches

		boolean ret = true;
		for(Node matchedNode : matched1) {
			Node oppositeNode = (Node)matchedNode.getMatch();

			if (matchedNode.getScore() != oppositeNode.getScore()) {
				ret = false;
			}

		}

		setNodesScores(matched1, 0.0);
		setNodesScores(matched2, 0.0);

		return(ret);
	}


/**
 * This method uses the score attribute to find the number of connecting
 * edges to each matched node. It returns a set of the nodes which have
 * had scores set (the matched nodes). This collects a repeated bit of
 * code used within the isomorphism algorithm.
 */
	protected HashSet<Node> setNodesNeighboursScoresForIsomorphism(Node node) {

		HashSet<Node> neighbours = node.connectingNodes();
		HashSet<Edge> edgeCollection = node.connectingEdges();

// cope with self sourcing edges
		neighbours.add(node);

		setNodesScores(neighbours,0.0);

		HashSet<Node> matched = new HashSet<Node>();
		for(Edge theEdge : edgeCollection) {
			Node theNode = theEdge.getOppositeEnd(node);

			if(theNode.getMatch() != null) {
// set up matched attributes and return true
				double score = theNode.getScore();
				theNode.setScore(score+1);
// add the node to the collection of matched nodes
				matched.add(theNode);
			}
		}

		return(matched);
	}


/**
 * Prints the match pairs for nodes
 */

	public String nodeMatchesToString() {

		String ret = new String("[");

		Iterator<Node> ni = nodes.iterator();
		while(ni.hasNext()) {

			Node n = ni.next();
			ret = ret+"<"+n.toString()+","+n.getMatch()+">";

			if(ni.hasNext()) {
				ret = ret+";";
			}

		}

		ret = ret+"]";

		return(ret);
	}


/**
 * Adds unique identifiers to the edges of a graph, integers from 1.
 * Can be used before calling TSP in order to cope with parallel edges.
 */
	public void identifyEdgeLabels() {
		int count = 1;
		for(Edge e : getEdges()) {
			Integer i = new Integer(count);
			e.setLabel(i.toString());
			count++;
		}
	}


/** Find a Minimum Spanning Tree using Kruskals algorithm. Outline:
 * sort the edges by weight, and add them to the MST if a loop is not formed.
 * uses {@link UnionFind} as the loop detector.
 * Deals with parallel and self sourcing edges.
 * @return The edges in the MST, or null if there is no MST.
 */
	public ArrayList<Edge> kruskal() {

		EdgeWeightComparator eComp = new EdgeWeightComparator();

		ArrayList<Node> ns = new ArrayList<Node>(getNodes());
		ArrayList<Edge> es = new ArrayList<Edge>(getEdges());
		Collections.sort(es,eComp);
		ArrayList<Edge> mst = new ArrayList<Edge>();

		UnionFind uf = new UnionFind(ns.size());

		Iterator<Edge> ei = es.iterator();
		while((mst.size() < ns.size()-1) && ei.hasNext()) {
			Edge e = ei.next();
			int nFrom = ns.indexOf(e.getFrom());
			int nTo = ns.indexOf(e.getTo());
			if(!uf.find(nFrom,nTo)) {
				mst.add(e);
				uf.union(nFrom,nTo);
			}
		}

		if(mst.size() < ns.size()-1) {
			return(null);
		}

		return(mst);
	}


/** Find a Minimum Spanning Tree using Prims algorithm. Outline:
 * pick the nearest node in the PQ to the current nodes in the
 * mst, add unseen neighbouring nodes (those with a score of -1.0)
 * to the PQ, change the priority of seen neighbouring nodes in the
 * pq. The nodes in the PQ are those with the visited flag set to true.
 * Uses the visited field of nodes to indicate whether a node is in
 * mst - if it is false and the score is not -1.0 then it is in the mst.
 * Deals with parallel and self sourcing edges.
 * Current pq implementation is a HashSet to easily cope with parallel edges.
 * This is a bit slower than optimal- a sorted collection might be better.
 * @return the edges in the MST, or null if there is no MST
 */
	public ArrayList<Edge> prim() {

		if(getNodes().size() == 0) {
			return(new ArrayList<Edge>());
		}

		NodeScoreComparator nComp = new NodeScoreComparator();

		int mstMaxSize = getNodes().size()-1;

		setNodesScores(-1.0);
		setNodesVisited(false);

// get any node to start from
		Node startNode = null;
		Iterator<Node> niStart = getNodes().iterator();
		if(niStart.hasNext()) {
			startNode = niStart.next();
		}
		startNode.setScore(0.0);

		ArrayList<Edge> mst = new ArrayList<Edge>();
		HashSet<Node> pq = new HashSet<Node>();

		for(Edge e : startNode.connectingEdges()) {
			Node n = e.getOppositeEnd(startNode);
			if(n != startNode) {
				if(n.getScore() == -1) {
					n.setScore(e.getWeight());
				} else {
// in case of parallel edges, get the cheapest
					if(n.getScore() > e.getWeight()) {
						n.setScore(e.getWeight());
					}
				}
				n.setVisited(true);
				pq.add(n);
			}
		}

		while(pq.size() > 0) {

// pq head removal here. This is a bit inefficient, the pq is iterated
// through in linear manner twice. Could be improved with a sorted collection
			Node currentNode = Collections.min(pq,nComp);
			pq.remove(currentNode);
			currentNode.setVisited(false);

			boolean addedToMST = false;
			for(Edge e : currentNode.connectingEdges()) {
// this bit finds the minimum edge and adds it to the pq
// also check that this edge is not self sourcing and connects to the MST
				if((!addedToMST) && e.getWeight() == currentNode.getScore()) {
					Node otherEnd = e.getOppositeEnd(currentNode);
					if(otherEnd != currentNode && !otherEnd.getVisited()) {
						mst.add(e);
						addedToMST = true;
					}
				}

				Node n = e.getOppositeEnd(currentNode);
				if(n != currentNode) {
					if(n.getScore()==-1.0) {
						n.setScore(e.getWeight());
						n.setVisited(true);
						pq.add(n);
					} else {

						if(n.getVisited() && n.getScore() > e.getWeight()) {
							n.setScore(e.getWeight());
// if changing to a sorted PQ, change the order of n here
						}
					}
				}
			}
		}

// case where the pq was empty, but the mst was not complete - must be
// due to a unconnected graph
		if(mst.size() < mstMaxSize) {
			mst = null;
		}

		return(mst);
	}



/** Used by tsp. */
private double bestCount = 0.0;
/** Used by tsp. */
private ArrayList<Edge> bestPath = null;
/** Used by tsp. */
private Node start = null;

/**
 * The Travelling Salesman Problem optimal solution buy brute force. This is a
 * liberal interpretation of the tsp, where edges may be traversed more than once.
 * Works with parallel edges only if the edges have unique labels.
 * @return a list containing the edges in the tsp, or null if the graph has no
 * tsp solution.
 */
	public ArrayList<Edge> tsp() {

		if(!connected()) {
			return(null);
		}
		bestCount = sumEdgeWeights()*2;
		bestPath = new ArrayList<Edge>();

		Iterator<Node> ni = nodes.iterator();
		if(ni.hasNext()) {
			start = ni.next();
		} else {
			return(bestPath);
		}

		tspRec(new ArrayList<Edge>(),0.0,start);
		return(bestPath);
	}



/** Recursive tsp finder. */
	private void tspRec(ArrayList<Edge> edgePath, double count, Node node) {

		for(Edge e : node.connectingEdges()) {
			double nextCount = count + e.getWeight();

// only go down the edge there is a chance of improvement
			if((nextCount <= bestCount) && !e.selfSourcing()) {

				Node nextNode = e.getOppositeEnd(node);

				ArrayList<Edge> nextEdgePath = new ArrayList<Edge>(edgePath);
				nextEdgePath.add(e);

// if this is a full tour, record it as the best, else go on to the next iteration
				if(nextNode == start && containsAllNodes(nextEdgePath,getNodes())) {
					bestCount = nextCount;
					bestPath = nextEdgePath;
				} else {

//edge may have already been traversed once this tour
					if(!containsConnections(edgePath,e,2)) {
						tspRec(nextEdgePath,nextCount,nextNode);
					}
				}
			}
		}
	}



/**
 * Used in tsp. Checks that all the nodes are in the edge list (i.e. if the
 * edge collection is a tour, check if it is a tour of all the nodes).
 */
	protected boolean containsAllNodes(Collection<Edge> edgeCollection, Collection<Node> nodeCollection) {
		ArrayList<Node> nodeList = new ArrayList<Node>(nodeCollection);
		for(Edge e : edgeCollection) {
			nodeList.remove(e.getFrom());
			nodeList.remove(e.getTo());
		}
		if(nodeList.size() != 0) {
			return(false);
		}
		return(true);
	}



/**
 * Used in tsp. Finds an edge, based on equality of
 * connections (directed) and label.
 */
	protected boolean containsConnections(ArrayList<Edge> track, Edge e, int number) {

		int count = 0;
		for(Edge trackE : track) {
			if((trackE.getFrom()==e.getFrom() && trackE.getTo()==e.getTo()) || (trackE.getFrom()==e.getTo() && trackE.getTo()==e.getFrom())) {
				if(trackE.getLabel() == e.getLabel()) {
					count++;
				}
			}
		}
		if(count < number) {
			return(false);
		}
		return(true);
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
	public Node getNodeNearPoint(Point p, int padding) {

		Node returnNode = null;

		for(Node n : getNodes()) {
			Shape nodeShape = n.shape();
			Rectangle r = new Rectangle(p.x-padding,p.y-padding,padding*2,padding*2);
			if(nodeShape.intersects(r)) {
				returnNode = n;
			}
		}
		return(returnNode);
	}


/**
* Finds the closest node to the point, or returns null
* if there are no nodes in the graph.
*/
	public Node getClosestNode(Point p) {

		Node returnNode = null;

		double closestDistance = -1;

		for(Node n : getNodes()) {
			Point centre = n.getCentre();
			double distance = centre.distance(p);

// no current return node
			if(closestDistance == -1) {
				returnNode = n;
				closestDistance = distance;
			}
			if(distance < closestDistance) {
				returnNode = n;
				closestDistance = distance;
			}
		}
		return(returnNode);
	}


/**
* Finds the centre of the graph, based on forming a rectangle
* around the limiting nodes in the graph.
*/
	public Point getCentre() {

		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;

		for(Node node : getNodes()) {
			if(node.getX() > maxX) {
				maxX = node.getX();
			}
			if(node.getX() < minX) {
				minX = node.getX();
			}
			if(node.getY() > maxY) {
				maxY = node.getY();
			}
			if(node.getY() < minY) {
				minY = node.getY();
			}
		}

		int x = minX + (maxX - minX)/2;
		int y = minY + (maxY - minY)/2;

		Point ret = new Point(x,y);
		return ret;
	}

/**
 * Snaps the nodes and edge bends to the given grid. Does not deal with
 * overlapping items.
 */
	public void snapToGrid(int gridX,int gridY) {

		for(Node node : getNodes()) {

			node.setX(getNearestGridPoint(node.getX(),gridX));
			node.setY(getNearestGridPoint(node.getY(),gridY));
		}

		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = getNearestGridPoint(point.x,gridX);
				point.y = getNearestGridPoint(point.y,gridY);
			}
		}
	}

	static public int getNearestGridPoint(int coordinate,int grid) {

		int remainder = coordinate%grid;
		int ret = coordinate - remainder;
		if(remainder > grid/2) {
			ret = ret+ grid;
		}
		return ret;
	}

	/** returns the list of nodes with degree equal to or less than the given parameter */
	public int edgeIntersections() {
		int count = 0;
		for(Edge e1 : getEdges()) {
			for(Edge e2 : getEdges()) {
				if(e1 == e2) {
					continue;
				}
				if(e1.straightLineIntersects(e2)) {
					count++;
				}
			}
		}
		return count/2;
	}


/** Centre the graph on the given point */
	public void centreOnPoint(int centreX, int centreY) {
		Point graphCentre = getCentre();

		int moveX = centreX - graphCentre.x;
		int moveY = centreY - graphCentre.y;

		moveGraph(moveX,moveY);
	}

/** Move all the nodes and edge bends by the values given */
	public void moveGraph(int moveX, int moveY) {

		for(Node node : getNodes()) {
			node.setX(node.getX()+moveX);
			node.setY(node.getY()+moveY);
		}

		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = point.x+moveX;
				point.y = point.y+moveY;
			}
		}
	}

/**
 * Check graph consistency by ensuring internal data structures are correct.
 * That is: all redundant connection data is correct and check all connecting
 * nodes and edges are in the graph.
 * This method is a useful test to ensure that internal methods that
 * change the graph leave a valid graph.
 */
	public boolean consistent() {

// go though nodes checking parent and all to and from vectors
// correspond to connecting edges
		for(Node n : getNodes()) {
			if(!n.consistent(this)) {return false;}
		}

// go through edges checking parent and all connecting ids are
// in to and from vectors
		for(Edge e : getEdges()) {
			if(!e.consistent(this)) {return false;}
		}

		return(true);
	}


	/**
	 * Finds the first edge between the two nodes, or returns null
	 */
	public Edge findEdgeBetween(Node n1, Node n2) {

		for(Edge e : edges) {
			if(e.getTo() == n1 && e.getFrom() == n2) {
				return e;
			}
			if(e.getFrom() == n1 && e.getTo() == n2) {
				return e;
			}
		}
		return null;

	}


	/**
	 * Finds the first node in the graph containing the argument
	 * as a substring.
	 * @return null if there is no such node.
	 */
	public Node firstNodeContainingLabel(String subLabel) {

		Iterator<Node> ni = getNodes().iterator();
		while(ni.hasNext()) {
			Node n = ni.next();
			if(n.getLabel().contains(subLabel)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Add edge between nodes if there is not already one present.
	 * @return the edge, or null if no edge was created.
	 */
	public Edge addUniqueEdge(Node from, Node to) {

		ArrayList<Node> nodes = getNodes();
		if(!nodes.contains(from)) {
			return null;
		}
		if(!nodes.contains(to)) {
			return null;
		}
		for(Edge e : from.getEdgesFrom()) {
			if(e.getTo() == to) {
				return null;
			}
		}
		Edge newEdge = new Edge(from,to);
		addEdge(newEdge);
		return newEdge;
	}


	/**
	 * Returns the difference in characters between the two labels, the
	 * returned String is ordered.
	 */
	public static String findLabelDifferences(String label1, String label2) {

		ArrayList<String> labelList1 = AbstractDiagram.findContourList(label1);
		ArrayList<String> labelList2 = AbstractDiagram.findContourList(label2);

		ArrayList<String> oldList1 = new ArrayList<String>(labelList1);
		for(String contour1: oldList1) {
			if(labelList2.contains(contour1)) {
				labelList1.remove(contour1);
				labelList2.remove(contour1);
			}
		}

		labelList1.addAll(labelList2);

		Collections.sort(labelList1);
		StringBuffer ret = new StringBuffer();
		for(String l: labelList1) {
			ret.append(l);
		}

		return ret.toString();
	}


	/**
	 * Returns the intersection in characters between the two labels, the
	 * returned String is ordered.
	 */
	public static String findLabelIntersection(String label1, String label2) {

		ArrayList<String> labelList1 = AbstractDiagram.findContourList(label1);
		ArrayList<String> labelList2 = AbstractDiagram.findContourList(label2);

		ArrayList<String> retList = new ArrayList<String>();

		for(String contour: labelList1) {
			if(labelList2.contains(contour)) {
				retList.add(contour);
			}
		}

		Collections.sort(retList);
		StringBuffer ret = new StringBuffer();
		for(String l: retList) {
			ret.append(l);
		}

		return ret.toString();
	}




/**
 * Outputs the node and edge lists in a string.
 */
	public String toString() {
		return(getLabel()+"\nNodes:"+getNodes().toString()+"\nEdges:"+getEdges().toString());
	}

}



