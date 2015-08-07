package org.eulerdiagrams.vennom.apCircles.drawers;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import org.eulerdiagrams.vennom.apCircles.Util;
import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.apCircles.utilities.CreateRandomPiercedSpecificationByGraph;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.GraphDrawer;


/**
 * A version of Eades spring embedder for laying out graphs
 * Selected nodes are not moved, but still participate in the
 * force calculation
 *
 * @author Peter Rodgers
 */
public class PiercedAPForceModel extends GraphDrawer implements Serializable {

	private static final long serialVersionUID = 1L;
	/** The strength of the attraction when applied to fixed edges*/
	protected double fixedMultipiler = 10;
	/** The strength of the attraction when applied to attractor edges*/
	protected double attractorMultipiler = 20000.0;
	/** The strength of the repulsive force */
	protected double repulsorMultiplier = 400000.0;
	
/** The amount of movement on each iteration */
	protected double f = 0.05;
/** Iterations between fixed edge rectification */
	protected int rectificationFrequency = 10;
/** The number of iterations */
	protected int iterations = 10000;
//protected int iterations = 1;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 2000;
/** The amount of movement below which the algorithm stops*/
	protected double movementThreshold = 0.01;
/** The maximum allowed force when no structure issues for a single repulsion or attraction */
	protected double forceThreshold = 100;
/** The maximum force applied on one iteration */
	protected double maxMovement;
/** This holds copies of current node locations for double precision*/
	protected HashMap<Node,Point2D.Double> currentNodeCentres = new HashMap<Node,Point2D.Double>();
/** This holds copies of old node locations */
	protected HashMap<Node,Point2D.Double> oldNodeCentres = new HashMap<Node,Point2D.Double>();
/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
/** Set to randomize the graph before spring embedding */
	protected boolean randomize = false;
/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
	/** x limit, set on layout start */
	protected int limitX = 600;
/** y limit, set set on layout start */
	protected int limitY = 600;

/** Trivial constructor. */
	public PiercedAPForceModel() {
		super(KeyEvent.VK_S,"AP Pierced Spring Embedder");
	}
	

/** Trivial constructor. */
	public PiercedAPForceModel(int key, String s) {
		super(key,s);
	}
/** Constructor. */
	public PiercedAPForceModel(int key, String s, boolean inRandomize) {
		super(key,s);
		randomize = inRandomize;
	}
/** Full constructor. */
	public PiercedAPForceModel(int key, String s, int mnemomic, boolean inRandomize) {
		super(key,s,mnemomic);
		randomize = inRandomize;
	}


	public long getTime() {return time;}
	public int getIterations() {return iterations;}
	public boolean getAnimateFlag() {return animateFlag;}

	public void setIterations(int inIterations) {iterations = inIterations;}
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
	public void setRandomize(boolean flag) {randomize = flag;}


/** Draws the graph. */
	public void layout() {
		
		limitX = getGraphPanel().getWidth();
		limitY = getGraphPanel().getHeight();
		
		if(randomize) {
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
		}
	
		maxMovement = Double.MAX_VALUE;

		currentNodeCentres.clear();
		for(Node n : getGraph().getNodes()) {
			currentNodeCentres.put(n, new Point2D.Double(n.getCentre().x,n.getCentre().y));
		}

		rectifyLengths();
		
		int i = 0;
		long startTime = System.currentTimeMillis();
		while(maxMovement-movementThreshold > 0) {
			
			i++;

			//set up the node centres storage
			oldNodeCentres.clear();
			for(Node n : getGraph().getNodes()) {
				Point2D.Double currentCentre = currentNodeCentres.get(n);
				oldNodeCentres.put(n, new Point2D.Double(currentCentre.x,currentCentre.y));
			}

			for(Node n : getGraph().getNodes()) {
				Point2D.Double newPos = findForceOnNode(n);
				currentNodeCentres.put(n,newPos);
			}

			if(i%rectificationFrequency == 0) {

				rectifyLengths();
				//rectifying the lengths seems to move the graph, so recentre after each iteration
				centreGraph(CreateRandomPiercedSpecificationByGraph.CENTREX, CreateRandomPiercedSpecificationByGraph.CENTREY);
				
				if(animateFlag && getGraphPanel() != null) {
					for(Node n : getGraph().getNodes()) {
						Point2D.Double newCentre = currentNodeCentres.get(n);
						Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
						n.setPreciseCentre(newCentre);
						n.setCentre(centreInt);
					}
					getGraphPanel().update(getGraphPanel().getGraphics());
				}
				if((System.currentTimeMillis() - startTime) > timeLimit) {
					System.out.println("Exit due to time expiry after "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
					break;
				}
			}
			
			maxMovement = findMaximumMovement();
			
			if(i >= iterations) {
				System.out.println("Pierced AP - Exit due to iterations limit "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
				break;
			}
		}
		if(maxMovement-movementThreshold <= 0) {
			System.out.println("Pierced AP - Exit due to under movement threshold "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
		
		}
		System.out.println("Pierced AP - Iterations: "+i+", max movement: "+maxMovement+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));



		rectifyLengths();
		centreGraph(CreateRandomPiercedSpecificationByGraph.CENTREX, CreateRandomPiercedSpecificationByGraph.CENTREY);
		
		for(Node n : getGraph().getNodes()) {
			Point2D.Double newCentre = currentNodeCentres.get(n);
			Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
			n.setCentre(centreInt);
		}

		if(getGraphPanel() != null) {
			getGraphPanel().repaint();
			getGraphPanel().update(getGraphPanel().getGraphics());
		}
	}
	

	
	private double findMaximumMovement() {
	
		double max = 0;
		
		for(Node n : getGraph().getNodes()) {
			Point2D.Double oldP = oldNodeCentres.get(n);
			Point2D.Double currentP = currentNodeCentres.get(n);
			
			double distance = Util.distance(oldP, currentP);
			if(distance > max) {
				max = distance;
			}
			
		}
		
		return max;
		
	}




/**
 * Finds the new location of a node.
 */
	public Point2D.Double findForceOnNode(Node n) {

		double radius = n.getScore();
		
		Point2D.Double p = oldNodeCentres.get(n);

		double xRepulsive = 0.0;
		double yRepulsive = 0.0;
		double xAttractive = 0.0;
		double yAttractive = 0.0;
		
		for(Node nextN : getGraph().getNodes()) {
			if(n == nextN) {
				continue;
			}
			Point2D.Double nextP = oldNodeCentres.get(nextN);

			Edge e = getGraph().findEdgeBetween(n,nextN);
			
			if(e != null) {
				
				double centreDistance = Util.distance(p,nextP);
				double radiusFrom = e.getFrom().getScore();
				double radiusTo = e.getTo().getScore();
				double largeRadius = radiusFrom;
				double smallRadius = radiusTo;
				if(radiusTo > radiusFrom) {
					largeRadius = radiusTo;
					smallRadius = radiusFrom;
				}
				double outsideBorderDistance = centreDistance-(radiusFrom+radiusTo);
				double insideBorderDifference = largeRadius-(centreDistance+smallRadius);

				double xDistance = p.x - nextP.x;
				double yDistance = p.y - nextP.y;

				double absXDistance = Math.abs(xDistance);
				double absYDistance = Math.abs(yDistance);

				double xForceShare = absXDistance/(absXDistance+absYDistance);
				double yForceShare = absYDistance/(absXDistance+absYDistance);
				
				EdgeType et = e.getType();
				
				if(et.equals(APCircleDisplay.REPULSOR)) {
					
					double repulsiveForce = repulsorMultiplier/(outsideBorderDistance*outsideBorderDistance);
					
					if(repulsiveForce > forceThreshold) {
						repulsiveForce = forceThreshold;

					}
					if(outsideBorderDistance<0) {
						// this is bad, use a big force
						repulsiveForce = forceThreshold*2;
					}
					// repulse the nodes
					double xForce = repulsiveForce*xForceShare;
					if(xDistance < 0) {
						xForce = -xForce;
					}
					xRepulsive += xForce;


					double yForce = repulsiveForce*yForceShare;
					if(yDistance < 0) {
						yForce = -yForce;
					}
					yRepulsive += yForce;
				}

				if(et.equals(APCircleDisplay.FIXED)) {

					double desiredLength = e.getScore();
					double error = centreDistance-desiredLength;
					
					double attractiveForce = fixedMultipiler*error;
					if(attractiveForce > forceThreshold) {
						attractiveForce = forceThreshold;
					}

					// repulse the nodes
					double xForce = attractiveForce*xForceShare;
					if(xDistance > 0) {
						xForce = -xForce;
					}
					xAttractive += xForce;


					double yForce = attractiveForce*yForceShare;
					if(yDistance > 0) {
						yForce = -yForce;
					}
					yAttractive += yForce;
				}
				
				if(et.equals(APCircleDisplay.ATTRACTOR)) {

					
					// this repulses the circles from their border, when they are inside, this brings them together
					//double attractiveForce = attractorMultipiler*centreDistance;
					double repulsiveForce = attractorMultipiler/(insideBorderDifference*insideBorderDifference);
					if(repulsiveForce > forceThreshold) {
						repulsiveForce = forceThreshold;
					}
					
					if(insideBorderDifference < 0) {
						// this is bad, use a big force
						repulsiveForce = forceThreshold*2;
					}
					// repulse the nodes
					double xForce = repulsiveForce*xForceShare;
					if(xDistance > 0) {
						xForce = -xForce;
					}
					xAttractive += xForce;

					double yForce = repulsiveForce*yForceShare;
					if(yDistance > 0) {
						yForce = -yForce;
					}
					yAttractive += yForce;
/*
Ellipse2D.Double ellipse = new Ellipse2D.Double(p.x+xForce*10,p.y+yForce*10,2,2);
APCirclePanel.ellipses.add(ellipse);
if(n.getLabel().equals("20")) {
	System.out.println("attractiveForce " +attractiveForce);
	System.out.println("xForce " +xForce+" yForce "+yForce+" edge "+e);
}
*/					
				}

			}
		}
		double totalXForce = f*(xRepulsive + xAttractive);
		double totalYForce = f*(yRepulsive + yAttractive);
		
		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		
		// stop the node going out of the drawable area
		if(newX < radius) {
			newX = radius;
		}
		if(newY < radius) {
			newY = radius;
		}
		if(newX > limitX - radius) {
			newX = limitX - radius;
		}
		if(newY > limitY - radius) {
			newY = limitY - radius;
		}
		
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return ret;
		
	}
	
	
	
	/**
	 * Make the edges with type "fixed" the desired length. Assumes subgraph of those edges is a tree and labelled with desired lengths
	 */
	public void rectifyLengths() {
		Graph g = getGraph();
		g.setNodesVisited(false);
		g.setEdgesVisited(false);
		
		Node root = g.getNodes().get(0);
		root.setVisited(true);
		
		ArrayList<Node> queue = new ArrayList<Node>();
		
		queue.add(root);
		
		while(queue.size() != 0) {
			Node head = queue.get(0);
			queue.remove(head);
			HashSet<Edge> connectingEdges = head.connectingEdges();
			for(Edge e : connectingEdges) {
				if(e.getType() != APCircleDisplay.FIXED) {
					continue;
				}
				if(e.getVisited() == false) {
					e.setVisited(true);
					Node neighbour = e.getOppositeEnd(head);
					moveNodeForCorrectLength(e,neighbour);
					neighbour.setVisited(true);
					queue.add(neighbour);
				}
			}
		
		}
		
/*		
for(Edge e : g.getEdges()) {
	if(e.getType() != APCircleDisplay.FIXED) {
		continue;
	}
	Node n1 = e.getFrom();
	Node n2 = e.getTo();
	double desiredLength = Integer.parseInt(e.getLabel());
	double actualLength = Util.distance(currentNodeCentres.get(e.getFrom()),currentNodeCentres.get(e.getTo()));
	double error = actualLength-desiredLength;
	if(Math.abs(actualLength-desiredLength) > 0.1) {
		System.out.println(" Edge "+n1.getLabel()+" "+n2.getLabel()+" is "+actualLength+" long. Should be "+desiredLength+". Error "+(error));
	}
}
*/		
	}


	private void moveNodeForCorrectLength(Edge e, Node n1) {

		double desiredLength = e.getScore();
		
		Node n2 = e.getOppositeEnd(n1);

		Point2D.Double p1 = currentNodeCentres.get(n1);
		Point2D.Double p2 = currentNodeCentres.get(n2);
		
		double actualLength = Util.distance(p1,p2);
		double fraction = desiredLength/actualLength;
		
		Point2D.Double newCentre = Util.betweenPoints(p2, p1, fraction);
		currentNodeCentres.put(n1,newCentre);
//Ellipse2D.Double ellipse = new Ellipse2D.Double(newCentre.x,newCentre.y,5,5);
//APCirclePanel.ellipses.add(ellipse);

	}
	
	
	/** Centre the graph on the given point */
	public void centreGraph(double centreX, double centreY) {
		Point2D.Double graphCentre = graphCentre();
	
		double moveX = centreX - graphCentre.x;
		double moveY = centreY - graphCentre.y;
		
		moveNodes(moveX,moveY);
	}
	
	
	/**
	* Finds the centre of the graph, based on forming a rectangle
	* around the limiting nodes in the graph.
	*/
		public Point2D.Double graphCentre() {

			double maxX = Double.MIN_VALUE;
			double minX = Double.MAX_VALUE;
			double maxY = Double.MIN_VALUE;
			double minY = Double.MAX_VALUE;

			for(Node node : getGraph().getNodes()) {
				double x = currentNodeCentres.get(node).x;
				double y = currentNodeCentres.get(node).y;
				if(x > maxX) {
					maxX = x;
				}
				if(x < minX) {
					minX = x;
				}
				if(y > maxY) {
					maxY = y;
				}
				if(y < minY) {
					minY = node.getY();
				}
			}

			double x = minX + (maxX - minX)/2;
			double y = minY + (maxY - minY)/2;

			Point2D.Double ret = new Point2D.Double(x,y);
			return ret;
		}
		
		
		/** Move all the nodes and edge bends by the values given */
		public void moveNodes(double moveX, double moveY) {
		
			for(Node node : getGraph().getNodes()) {
				double x = currentNodeCentres.get(node).x+moveX;
				double y = currentNodeCentres.get(node).y+moveY;
				Point2D.Double point = new Point2D.Double(x,y);
				currentNodeCentres.put(node,point);
			}
		
		}



	
}

