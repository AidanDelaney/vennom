package org.eulerdiagrams.vennom.apCircles.drawers;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.apCircles.AbstractDiagram;
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

/*
	public double idealMultipiler = 0.1;
	public double separatorMultiplier = 10000.0;
	public double containmentMultiplier = 0.01;
	public double f = 1.0;
*/	
/*
	public double idealMultipiler = 0.05;
	public double separatorMultiplier = 10000.0;
	public double containmentMultiplier = 0.01;
	public double f = 1.0;
*/
	
	// these values determined by experimentation
	public double idealMultipiler = 0.0475;
	public double separatorMultiplier = 19000.0;
	public double containmentMultiplier = 0.01;
	public double f = 1.0; // movement multiplier
	
	/** Extra space between circles */
	protected double separatorPadding = 20;
	
	/** Extra space for contained circles */
	protected double containmentPadding = 5;
	
/** The number of iterations */
	protected int iterations = 10000;
//protected int iterations = 1;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 2000;
/** The amount of movement below which the algorithm stops*/
	protected double movementThreshold = 0.001;
/** The maximum allowed force when no structure issues for a single repulsion or attraction */
	protected double forceThreshold = 50;
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
			
			if(animateFlag && getGraphPanel() != null) {
				for(Node n : getGraph().getNodes()) {
					Point2D.Double newCentre = currentNodeCentres.get(n);
					Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
					n.setPreciseCentre(newCentre);
					n.setCentre(centreInt);
				}
				getGraphPanel().update(getGraphPanel().getGraphics());
			}

			maxMovement = findMaximumMovement();
			
			if(i >= iterations) {
//				System.out.println("General AP - Exit due to iterations limit "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
				break;
			}
		}
		
		if(maxMovement-movementThreshold <= 0) {
//			System.out.println("General AP - Exit due to under movement threshold "+(System.currentTimeMillis() - startTime)+" milliseconds and "+i+" iterations");
		
		}
//		System.out.println("General AP - Iterations: "+i+", max movement: "+maxMovement+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));

		
		for(Node n : getGraph().getNodes()) {
			Point2D.Double newCentre = currentNodeCentres.get(n);
			Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
			n.setPreciseCentre(newCentre);
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
	 * Find centre of zone by a point furthest from zone border, or if zone
	 * does not exist, return middle of the relevant circles. If the circles in
	 * the zone do not exist, this returns null.
	 */
	public Point2D.Double findZoneCentre(String zone) {
		
		ArrayList<Point2D.Double> pointsInZone = findPointsInZone(zone);

		Point2D.Double middle = null;
//APCirclePanel.ellipses = new ArrayList<Ellipse2D.Double>();	
		if(pointsInZone != null) {
			middle = furthestPointFromCircleBorder(pointsInZone);
		} else { // zone does not exist
			middle = circleCentres(zone);
		}
//APCirclePanel.ellipses.add(new Ellipse2D.Double(middle.x-1,middle.y-1,2,2));
//System.out.println("middle "+middle.x+" "+middle.y);

		return middle;
	}
	
	
	/**
	 * Average of circle centres
	 */
	public Point2D.Double circleCentres(String zone) {
		ArrayList<Node> zoneNodes = new ArrayList<Node>();
		for(Node n : getGraph().getNodes()) {
			if(zone.contains(n.getContour())) {
				zoneNodes.add(n);
			}
		}
		
		double retX = 0.0;
		double retY = 0.0;
		for(Node n : zoneNodes) {
			retX = retX + n.getX();
			retY = retY + n.getY();
		}
		retX = retX / zoneNodes.size();
		retY = retY / zoneNodes.size();
		
		Point2D.Double ret = new Point2D.Double(retX,retY);
		
		return ret;

	}
	
	/**
	 * Point most distant from any circle.
	 */
	public Point2D.Double furthestPointFromCircleBorder(ArrayList<Point2D.Double> points) {
		
		double maxDistance = -1;
		Point2D.Double maxPoint = null;
		for(Point2D.Double p : points) {
			double distance = distanceFromCircleBorder(p);
			if(distance > maxDistance) {
				maxDistance = distance;
				maxPoint = p;
			}
		}
		return maxPoint;
	}
	
	/**
	 * Closest distance to any circle.
	 */
	public double distanceFromCircleBorder(Point2D.Double point) {
		
		double min = Double.MAX_VALUE;
		
		for(Node n : graphPanel.getGraph().getNodes()) {
			double radius = n.getScore();
			double circleX = n.getX();
			double circleY = n.getY();
			
			double distance = Util.distance(point.x, point.y, circleX, circleY);
			double borderDistance = -1;
			if(distance > radius) {
				borderDistance = distance - radius;
			} else {
				borderDistance = radius - distance;
			}
			if(borderDistance < min) {
				min = borderDistance;
			}

		}
		return min;
	}
	
	

	public ArrayList<Point2D.Double> findPointsInZone(String zone) {
		ArrayList<Node> zoneNodes = new ArrayList<Node>();
		// find smallest circle
		Node startNode = null;
		double startRadius = Double.MAX_VALUE;
		for(Node n : getGraph().getNodes()) {
			double radius = n.getScore();
			
			if(zone.contains(n.getContour())) {
				zoneNodes.add(n);
				if(radius < startRadius) {
					startRadius = radius;
					startNode = n;
				}
			}
		}
		if(startNode == null) { // no such zone is possible - zero contours or wrong contours passed
			return null; 
		}

		final int numberOfPointsOnSide = 20;
		
		double startX = startNode.getX()-startRadius;
		double startY = startNode.getY()-startRadius;
		double endX = startNode.getX()+startRadius;
		double endY = startNode.getY()+startRadius;
		double step = (startRadius*2)/numberOfPointsOnSide;
		
		ArrayList<Point2D.Double> candidatePoints = new ArrayList<Point2D.Double>();
		for(double x = startX; x <= endX; x += step) {
			for(double y = startY; y <= endY; y += step) {
				if(isPointInZone(zone,x,y)) {
					candidatePoints.add(new Point2D.Double(x,y));
				}
			}
		}
		
		if(candidatePoints.size() == 0) {
			return null;
		}
		return candidatePoints;
	}



	/**
	 * Point must be within the radius of circles it is in and outside
	 * of the radius of circles it is not in
	 */
	public boolean isPointInZone(String zone, double x, double y) {

		for(Node n : getGraph().getNodes()) {
			double centreX = n.getX();
			double centreY = n.getY();
			double radius = n.getScore();
			
			double distance = Util.distance(x, y, centreX, centreY);
			if(zone.contains(n.getContour())) {
				if(distance > radius) { // contour in the zone
					return false;
				}
			} else { // a contour not in the zone
				if(distance <= radius) {
					return false;
				}
			}
		}
		return true;
	}


	

/**
 * Finds the new location of a node.
 */
	public Point2D.Double findForceOnNode(Node n) {
		
		double radius = n.getScore();
		
		Point2D.Double p = oldNodeCentres.get(n);

		double xContainment = 0.0;
		double yContainment = 0.0;
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
				
				if(et.equals(APCircleDisplay.CONTAINMENT)) {
					
					double separation = centreDistance;
					if(separation == 0) {
						separation = 0.1;
					}
					double containmentForce = containmentMultiplier * separation;
					
					if(centreDistance <= e.getScore()-containmentPadding) {
						containmentForce = 0;
					}
					
					if(containmentForce > forceThreshold) {
						containmentForce = forceThreshold;

					}
					
					// attract the nodes
					double xForce = containmentForce*xForceShare;
					if(xDistance > 0) {
						xForce = -xForce;
					}
					xContainment += xForce;


					double yForce = containmentForce*yForceShare;
					if(yDistance > 0) {
						yForce = -yForce;
					}
					yContainment += yForce;
				}

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
		double totalXForce = f*(xContainment + xSeparator + xIdeal);
		double totalYForce = f*(yContainment + ySeparator + yIdeal);

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

