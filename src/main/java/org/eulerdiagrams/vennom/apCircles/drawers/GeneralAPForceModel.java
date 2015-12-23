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
 * A metric SE for area proportional circle EDs. The nodes
 * are the centre of the circles, the SEPARATE edges are
 * labelled with a minimum separations, the IDEAL edges
 * are labelled with an ideal separation.
 *
 * @author Peter Rodgers
 */
public class GeneralAPForceModel extends GraphDrawer implements Serializable, 
		UpdateRecipient {

	private static final long serialVersionUID = 1L;

	/** Set to randomize the graph before spring embedding */
	protected boolean randomize = false;
	
	/** The GeneralAPForceModel has some GUI-related content.  It delegates
	    the non-GUI work to the solver class.*/
	private GeneralAPForceModelSolver solver;

	// Solver settings which the ForceModel client may change
	public void setIdealMultiplier(double val){
		solver.setIdealMultiplier(val);
	}
	public void setSeparatorMultiplier(double val){
		solver.setSeparatorMultiplier(val);
	}
	public void setContainmentMultiplier(double val){
		solver.setContainmentMultiplier(val);
	}
	public void setF(double val){
		solver.setF(val);
	}

	/** Limit the number of allowed iterations performed during the solve */
	public void setIterations(int inIterations) {
		solver.setMaxIterations( inIterations);
	}

	/** Set to redraw on each iteration */
	protected boolean animateFlag = true;

	/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
	
	/** Trivial constructor. */
	public GeneralAPForceModel() {
		super(KeyEvent.VK_Z,"AP General Spring Embedder");
		solver = new GeneralAPForceModelSolver();
	}
	
	/** Trivial constructor. */
	public GeneralAPForceModel(int key, String s) {
		super(key,s);
		solver = new GeneralAPForceModelSolver();
	}
	
	/** Constructor. */
	public GeneralAPForceModel(int key, String s, boolean inRandomize) {
		super(key,s);
		randomize = inRandomize;
		solver = new GeneralAPForceModelSolver();
	}
	
	/** Full constructor. */
	public GeneralAPForceModel(int key, String s, int mnemomic, boolean inRandomize) {
		super(key,s,mnemomic);
		randomize = inRandomize;
		solver = new GeneralAPForceModelSolver();
	}

	/** time taken to complete the preceding solve */
	public long getTime() {return time;}
	
	/** whether to allow animation */
	public boolean getAnimateFlag() {return animateFlag;}
	/** whether to allow animation */
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
	
	/** whether to randomize the graph before solving */
	public void setRandomize(boolean flag) {randomize = flag;}


	/** Draws the graph. */
	public void layout() {
		
		// prepare graph before solve
		if(randomize) {
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
		}

		// set limits on graph
		solver.setLimits( getGraphPanel().getWidth(), getGraphPanel().getHeight() );

		// request update calls at each iteration step
		solver.setUpdateRecipient(this);

		// do work
		solver.layout( getGraph() );

		// refresh view
		if(getGraphPanel() != null) {
			getGraphPanel().repaint();
			getGraphPanel().update(getGraphPanel().getGraphics());
		}		
	}
	
	/** when to update the view */
	public void update(){
		if(animateFlag && getGraphPanel() != null) {
			for(Node n : getGraph().getNodes()) {
				Point2D.Double newCentre = solver.currentNodeCentre(n);
				Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
				n.setPreciseCentre(newCentre);
				n.setCentre(centreInt);
			}
			getGraphPanel().update(getGraphPanel().getGraphics());
		}		
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
				double x = solver.currentNodeCentre(node).x;
				double y = solver.currentNodeCentre(node).y;
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
			solver.moveNodes(getGraph(), moveX, moveY);
		}
}

