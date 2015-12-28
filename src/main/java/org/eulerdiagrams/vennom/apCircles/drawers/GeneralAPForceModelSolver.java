package org.eulerdiagrams.vennom.apCircles.drawers;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;

import org.eulerdiagrams.vennom.apCircles.Util;
import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;

public class GeneralAPForceModelSolver {

	/** The maximum number of iterations allowed */
	private int maxIterations;

	/** During iterations, we can send updates at each iteration */
	private UpdateRecipient forUpdates;
	
	/** The amount of movement below which the algorithm stops*/
    private double movementThreshold;

    /** The maximum allowed force when no structure issues for a single repulsion or attraction */
    private double forceThreshold;
    
	private double idealMultiplier;
	private double separatorMultiplier;
	private double containmentMultiplier;
	private double f; // movement multiplier

	/** x limit, set on layout start */
	private int limitX;
    /** y limit, set set on layout start */
	private int limitY;

	/** Extra space for contained circles */
	private double containmentPadding;

	/** Extra space between circles */
	private double separatorPadding;	

	/** This holds copies of current node locations for double precision*/
	private HashMap<Node,Point2D.Double> currentNodeCentres = new HashMap<Node,Point2D.Double>();

	/** This holds copies of old node locations */
	private HashMap<Node,Point2D.Double> oldNodeCentres = new HashMap<Node,Point2D.Double>();
	
	// constructor gives default values to settings
	public GeneralAPForceModelSolver(){

		maxIterations = 10000;
		
	    movementThreshold = 0.001;

	    forceThreshold = 50;

	    /*
		public double idealMultiplier = 0.1;
		public double separatorMultiplier = 10000.0;
		public double containmentMultiplier = 0.01;
		public double f = 1.0;
	     */	
	    /*
		public double idealMultiplier = 0.05;
		public double separatorMultiplier = 10000.0;
		public double containmentMultiplier = 0.01;
		public double f = 1.0;
	     */
	    
		// these values determined by experimentation
		idealMultiplier = 0.0475;
		separatorMultiplier = 19000.0;
		containmentMultiplier = 0.01;
		f = 1.0; // movement multiplier

		limitX = 600;
		limitY = 600;

		containmentPadding = 5;

		separatorPadding = 20;	
	}
	
	public Point2D.Double currentNodeCentre(Node n){
		return currentNodeCentres.get(n);
	}
	public HashMap<Node,Point2D.Double> oldNodeCentres(){
		return oldNodeCentres;
	}

	public void setMaxIterations( int max ){
		maxIterations = max;
	}
	
	public void setLimits(int limitX, int limitY){
		this.limitX = limitX;
		this.limitY = limitY;		
	}

	public void setIdealMultiplier(double val){
		this.idealMultiplier = val;
	}
	public void setSeparatorMultiplier(double val){
		this.separatorMultiplier = val;
	}
	public void setContainmentMultiplier(double val){
		this.setContainmentMultiplier(val);
	}
	public void setF(double val){
		this.f = val;
	}

	public void setUpdateRecipient( UpdateRecipient u ){
		forUpdates = u;
	}

	public void layout( Graph g ){
		currentNodeCentres.clear();
		for(Node n : g.getNodes()) {
			currentNodeCentres.put(n, new Point2D.Double(n.getCentre().x,n.getCentre().y));
		}

		// The maximum movement undergone at this iteration step
		double maxMovement = Double.MAX_VALUE;
		
		int i = 0;
		while(maxMovement-movementThreshold > 0) {
			
			i++;

			//set up the node centres storage
			//System.out.println("nodes "+g.getNodes());

			oldNodeCentres().clear();
			for(Node n : g.getNodes()) {
				Point2D.Double currentCentre = currentNodeCentre(n);
				//System.out.println("n "+n.hashCode()+" pos "+currentCentre);
				oldNodeCentres().put(n, new Point2D.Double(currentCentre.x,currentCentre.y));
			}

			for(Node n : g.getNodes()) {
				Point2D.Double newPos = findForceOnNode(g, n);
				//System.out.println("n "+n.hashCode()+" new pos "+newPos);
				currentNodeCentres.put(n,newPos);
			}
			
			if(forUpdates != null)
				forUpdates.update();

			maxMovement = findMaximumMovement(g);
			//System.out.println("i = "+i+", max movement = "+maxMovement);
			
			if(i >= maxIterations) {
				//System.out.println("General AP - Exit due to iterations limit "+i+" iterations");
				break;
			}
		}
		
		if(maxMovement-movementThreshold <= 0) {
			//System.out.println("General AP - Exit due to under movement threshold "+i+" iterations");
		
		}
		//System.out.println("General AP - Iterations: "+i+", max movement: "+maxMovement);

		
		for(Node n : g.getNodes()) {
			Point2D.Double newCentre = currentNodeCentre(n);
			Point centreInt = new Point(Util.convertToInteger(newCentre.x),Util.convertToInteger(newCentre.y));
			n.setPreciseCentre(newCentre);
			n.setCentre(centreInt);
		}

	}

	
	private double findMaximumMovement( Graph g ) {
		
		double max = 0;
		for(Node n : g.getNodes()) {
			//System.out.println("find positions for "+n.hashCode());
			Point2D.Double oldP = oldNodeCentres().get(n);
			Point2D.Double currentP = currentNodeCentre(n);
			
			//System.out.println("old pos "+oldP+" new pos "+currentP);
			double distance = Util.distance(oldP, currentP);
			if(distance > max) {
				max = distance;
			}
			
		}
		
		return max;
		
	}
	public Point2D.Double findForceOnNode(Graph g, Node n) {
		
		double radius = n.getScore();
		
		Point2D.Double p = oldNodeCentres().get(n);

		double xContainment = 0.0;
		double yContainment = 0.0;
		double xSeparator = 0.0;
		double ySeparator = 0.0;
		double xIdeal = 0.0;
		double yIdeal = 0.0;
		
		for(Node nextN : g.getNodes()) {
			if(n == nextN) {
				continue;
			}
			Point2D.Double nextP = oldNodeCentres().get(nextN);

			Edge e = g.findEdgeBetween(n,nextN);
			
			//System.out.println("edge is "+e);
			
			if(e != null) {
				
				double centreDistance = Util.distance(p,nextP);
				
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
								
				if(e.isContainmentType()) {
					
					//System.out.println("edge is containment");

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

				if(e.isSeparatorType()) {
					
					//System.out.println("edge is separator");

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

				
				if(e.isIdealType()) {

					//System.out.println("edge is ideal");

					double distanceFromIdeal = Math.abs(centreDistance-e.getScore());
					
					double idealForce = idealMultiplier*distanceFromIdeal;
					
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

	/** Move all the nodes and edge bends by the values given */
	public void moveNodes(Graph g, double moveX, double moveY) {
	
		for(Node node : g.getNodes()) {
			double x = currentNodeCentre(node).x+moveX;
			double y = currentNodeCentre(node).y+moveY;
			Point2D.Double point = new Point2D.Double(x,y);
			currentNodeCentres.put(node,point);
		}
	
	}		
}
