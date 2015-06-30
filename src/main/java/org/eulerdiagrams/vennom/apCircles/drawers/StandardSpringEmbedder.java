package org.eulerdiagrams.vennom.apCircles.drawers;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import org.eulerdiagrams.vennom.apCircles.Util;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.GraphDrawer;

/**
 * A version of Eades spring embedder for laying out graphs
 * Selected nodes are not moved, but still participate in the
 * force calculation
 *
 * @author Peter Rodgers
 */
public class StandardSpringEmbedder extends GraphDrawer implements Serializable {

	private static final long serialVersionUID = 1L;
/** The strength of a spring */
	protected double k = 0.1;
/** The strength of the repulsive force */
	protected double r = 20000.0;
/** The amount of movement on each iteration */
	protected double f = 1.0;
	/** The number of iterations */
	protected int iterations = 10000;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 1000;
/** The amount of max force below which the algorithm stops*/
	protected double forceThreshold = 0.1;
/** The maximum force applied on one iteration */
	protected double maxAllowedMovement = 40.0;
/** The maximum force applied on one iteration */
	protected double maxForce;
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
	public StandardSpringEmbedder() {
		super(KeyEvent.VK_E,"Spring Embedder");
	}

/** Trivial constructor. */
	public StandardSpringEmbedder(int key, String s) {
		super(key,s);
	}
/** Constructor. */
	public StandardSpringEmbedder(int key, String s, boolean inRandomize) {
		super(key,s);
		randomize = inRandomize;
	}
/** Full constructor. */
	public StandardSpringEmbedder(int key, String s, int mnemomic, boolean inRandomize) {
		super(key,s,mnemomic);
		randomize = inRandomize;
	}


	public long getTime() {return time;}
	public double getK() {return k;}
	public double getR() {return r;}
	public double getF() {return f;}
	public int getIterations() {return iterations;}
	public boolean getAnimateFlag() {return animateFlag;}

	public void setK(double inK) {k = inK;}
	public void setR(double inR) {r = inR;}
	public void setF(double inF) {f = inF;}
	public void setIterations(int inIterations) {iterations = inIterations;}
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
	public void setRandomize(boolean flag) {randomize = flag;}


/** Draws the graph. */
	public void layout() {
		
		if(randomize) {
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
		}
	
		maxForce = Double.MAX_VALUE;

		currentNodeCentres.clear();
		for(Node n : getGraph().getNodes()) {
			currentNodeCentres.put(n, new Point2D.Double(n.getCentre().x,n.getCentre().y));
		}

		
		int i = 0;
		long startTime = System.currentTimeMillis();
		while(maxForce-forceThreshold > 0) {
			

			//set up the node centres storage
			oldNodeCentres.clear();
			for(Node n : getGraph().getNodes()) {
				Point2D.Double currentCentre = currentNodeCentres.get(n);
				oldNodeCentres.put(n, new Point2D.Double(currentCentre.x,currentCentre.y));
			}
			
			i++;
			maxForce = 0.0;

			for(Node n : getGraph().getNodes()) {
				Point2D.Double newPos = findForceOnNode(n);
				currentNodeCentres.put(n,newPos);
			}


			
			if(animateFlag && getGraphPanel() != null) {
				for(Node n : getGraph().getNodes()) {
					Point2D.Double newCentre = currentNodeCentres.get(n);
					Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
					n.setCentre(centreInt);
				}
				getGraphPanel().update(getGraphPanel().getGraphics());
			}
			if((System.currentTimeMillis() - startTime) > timeLimit) {
				System.out.println("Exit due to time expiry after "+timeLimit+" milliseconds and "+i+" iterations");
				break;
			}
		}
System.out.println("Iterations: "+i+", max force: "+maxForce+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));

		for(Node n : getGraph().getNodes()) {
			Point2D.Double newCentre = currentNodeCentres.get(n);
			Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
			n.setCentre(centreInt);
		}

		if(!animateFlag && getGraphPanel() != null) {
			getGraphPanel().update(getGraphPanel().getGraphics());
		}
		
	}





/**
 * Finds the new location of a node.
 */
	public Point2D.Double findForceOnNode(Node n) {
		
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

			double distance = p.distance(nextP);
			double xDistance = p.x - nextP.x;
			double yDistance = p.y - nextP.y;
			double absDistance = Math.abs(distance);
			double absXDistance = Math.abs(xDistance);
			double absYDistance = Math.abs(yDistance);

			double xForceShare = absXDistance/(absXDistance+absYDistance);
			double yForceShare = absYDistance/(absXDistance+absYDistance);

			if (n.connectingNodes().contains(nextN)) {
				if(xDistance > 0) {
					xAttractive -= k*xForceShare*absDistance;
				} else {
					if(xDistance < 0) {
						xAttractive += k*xForceShare*absDistance;
					}
				}

				if(yDistance > 0) {
					yAttractive -= k*yForceShare*absDistance;
				} else {
					if(yDistance < 0) {
						yAttractive += k*yForceShare*absDistance;
					}
				}
			}
			
			double repulsiveForce = r / (distance * distance);

			if(xDistance > 0) {
				xRepulsive += repulsiveForce*xForceShare;
			} else {
				if(xDistance < 0) {
					xRepulsive -= repulsiveForce*xForceShare;
				}
			}

			if(yDistance > 0) {
				yRepulsive += repulsiveForce*yForceShare;
			} else {
				if(yDistance < 0) {
					yRepulsive -= repulsiveForce*yForceShare;
				}
			}

		}

		double totalXForce = f*(xRepulsive + xAttractive);
		double totalYForce = f*(yRepulsive + yAttractive);
		
		double totalForce = Math.sqrt(totalXForce*totalXForce+totalYForce*totalYForce);
		
		if(totalForce > maxAllowedMovement) {
			double fraction = maxAllowedMovement/totalForce;
			totalXForce = totalXForce*fraction;
			totalYForce = totalYForce*fraction;
			totalForce = Math.sqrt(totalXForce*totalXForce+totalYForce*totalYForce);
		}
		
		if(totalForce > maxForce) {
			maxForce = totalForce;
		}

		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return ret;
	}

}
