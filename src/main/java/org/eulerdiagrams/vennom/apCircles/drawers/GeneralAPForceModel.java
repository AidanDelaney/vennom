package org.eulerdiagrams.vennom.apCircles.drawers;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import org.eulerdiagrams.vennom.apCircles.Util;
import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.GraphDrawer;


/**
 * A metric SE for area proportional circle EDs. The nodes
 * are the centre of the circles, the SEPARATE edges are
 * labelled with a minimum separations, the IDEAL edges
 * are labelled with an ideal separation.
 *
 * @author Peter Rodgers
 */
public class GeneralAPForceModel extends GraphDrawer implements Serializable {

	private static final long serialVersionUID = 1L;
	protected double idealMultipiler = 0.1;
	protected double separatorMultiplier = 10000.0;
	
	/** Extra space between circles */
	protected double separatorPadding = 20;
	
/** The amount of movement on each iteration */
	protected double f = 1.0;
/** The number of iterations */
	protected int iterations = 10000;
//protected int iterations = 1;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 2000;
/** The amount of movement below which the algorithm stops*/
	protected double movementThreshold = 0.01;
/** The maximum allowed force when no structure issues for a single repulsion or attraction */
	protected double forceThreshold = 20;
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

/** Trivial constructor. */
	public GeneralAPForceModel() {
		super(KeyEvent.VK_Z,"AP General Spring Embedder");
	}
	

/** Trivial constructor. */
	public GeneralAPForceModel(int key, String s) {
		super(key,s);
	}
/** Constructor. */
	public GeneralAPForceModel(int key, String s, boolean inRandomize) {
		super(key,s);
		randomize = inRandomize;
	}
/** Full constructor. */
	public GeneralAPForceModel(int key, String s, int mnemomic, boolean inRandomize) {
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
		if(randomize) {
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
		}
	
		maxMovement = Double.MAX_VALUE;

		currentNodeCentres.clear();
		for(Node n : getGraph().getNodes()) {
			currentNodeCentres.put(n, new Point2D.Double(n.getCentre().x,n.getCentre().y));
		}

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

			maxMovement = findMaximumMovement();
			
			if(i >= iterations) {
				System.out.println("Exit due to iterations limit "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
				break;
			}
		}
System.out.println("Iterations: "+i+", max movement: "+maxMovement+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));

		
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
		Point2D.Double p = oldNodeCentres.get(n);

		double xSeparator = 0.0;
		double ySeparator = 0.0;
		double xIdeal = 0.0;
		double yIdeal = 0.0;
		
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
				
				double xDistance = p.x - nextP.x;
				double yDistance = p.y - nextP.y;

				double absXDistance = Math.abs(xDistance);
				double absYDistance = Math.abs(yDistance);

				double xForceShare = absXDistance/(absXDistance+absYDistance);
				double yForceShare = absYDistance/(absXDistance+absYDistance);
				if(absXDistance+absYDistance == 0) {
					xForceShare = 1;
					yForceShare = 0;
				}
				
				EdgeType et = e.getType();
				
				if(et.equals(APCircleDisplay.SEPARATOR)) {
					
					double separation = centreDistance;
					if(separation == 0) {
						separation = 0.1;
					}
					double separatorForce = separatorMultiplier/(separation*separation);
					
					if(centreDistance >= e.getScore()+separatorPadding) {
						separatorForce = 0;
					}
					
					if(separatorForce > forceThreshold) {
						separatorForce = forceThreshold;

					}
					
System.out.println("separatorForce "+separatorForce);

					// repulse the nodes
					double xForce = separatorForce*xForceShare;
					if(xDistance < 0) {
						xForce = -xForce;
					}
					xSeparator += xForce;


					double yForce = separatorForce*yForceShare;
					if(yDistance < 0) {
						yForce = -yForce;
					}
					ySeparator += yForce;
				}

				
				if(et.equals(APCircleDisplay.IDEAL)) {

					double distanceFromIdeal = Math.abs(centreDistance-e.getScore());
					
					double idealForce = idealMultipiler*distanceFromIdeal;
					
					if(idealForce > forceThreshold) {
						idealForce = forceThreshold;
					}
System.out.println("idealForce "+idealForce);
					
					double xForce = idealForce*xForceShare;
					if(xDistance > 0) {
						xForce = -xForce;
					}
					if(centreDistance-e.getScore() < 0) { // too short
						xForce = -xForce;
					}
					xIdeal += xForce;

					double yForce = idealForce*yForceShare;
					if(yDistance > 0) {
						yForce = -yForce;
					}
					if(centreDistance-e.getScore() < 0) { // too short
						yForce = -yForce;
					}
					yIdeal += yForce;
				}

			}
		}
		double totalXForce = f*(xSeparator + xIdeal);
		double totalYForce = f*(ySeparator + yIdeal);
System.out.println("totalXForce "+totalXForce);
System.out.println("totalYForce "+totalYForce);
		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return ret;
		
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

