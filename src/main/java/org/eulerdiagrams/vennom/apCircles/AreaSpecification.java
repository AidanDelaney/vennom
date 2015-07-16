package org.eulerdiagrams.vennom.apCircles;

import java.awt.Point;
import java.util.*;


import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.graph.*;

public class AreaSpecification {

	protected HashMap<String,Double> specification;
	protected AbstractDiagram abstractDiagram;
	/** Dividing all specifications by this makes the total area 1.0 */
	protected double normalizationFactor = 0.0;
	
	public AreaSpecification(AbstractDiagram abstractDiagram) {
		this.abstractDiagram = abstractDiagram;
		specification = new HashMap<String, Double>();
		setAllZoneAreas(0.0);
		findNormalizationFactor();
	}

	public AreaSpecification(AbstractDiagram abstractDiagram, HashMap<String,Double> specification) {
		this.abstractDiagram = abstractDiagram;
		this.specification = specification;
		findNormalizationFactor();
	}

	public AreaSpecification(String s) {
		this.abstractDiagram = new AbstractDiagram("");
		specification = new HashMap<String, Double>();
		fromString(s);
		findNormalizationFactor();
	}
	

	protected double findNormalizationFactor() {
		double totalArea = 0.0;
		for(String s : specification.keySet()) {
			totalArea += specification.get(s);
		}
		return totalArea;
	}
	
	
	public double normalizedArea(String zone) {
		if(normalizationFactor == 0.0) {
			normalizationFactor = findNormalizationFactor();
		}
		double area = specification.get(zone);
		return area/normalizationFactor;
	}
	
	/**
	 * Sets all the zone area specifications to the argument.
	 */
	public void setAllZoneAreas(double area) {
		for(String zone : abstractDiagram.getZoneList()) {
			setZoneArea(zone, area);
		}
	}
	
	/**
	 * Sets the area for the zone. Returns false if the
	 * zone is not in the diagram.
	 */
	public boolean setZoneArea(String zone, double area) {
		if(!abstractDiagram.getZoneList().contains(zone)) {
			return false;
		}
		specification.put(zone, area);
		return true;
	}

	/**
	 * Returns the area for the zone, or -1.0 if the zone is not
	 * in the diagram
	 */
	public double getZoneArea(String zone) {
		if(!abstractDiagram.getZoneList().contains(zone)) {
			return -1.0;
		}
		return specification.get(zone);
	}

	public AbstractDiagram getAbstractDiagram() {
		return abstractDiagram;
	}
	

	/**
	 * Returns the complete intersection graph, or null if the description is not
	 * a single piercing.
	 */
	public Graph generatePeircedAugmentedIntersectionGraph() {

		if(abstractDiagram.getZoneList().size() <= 1) {
			return new Graph();
		}
		
		HashMap<String,Node> circleNodeMap = new HashMap<String,Node>();
		
		Graph graph = new Graph();
		// add the nodes
		for(String circle : abstractDiagram.getContours()) {
			double circleArea = 0.0;
			for(String zone : abstractDiagram.getZoneList()) {
				ArrayList<String> zoneList = AbstractDiagram.findContourList(zone);
				if(zoneList.contains(circle)) {
					double zoneArea = specification.get(zone);
					circleArea += zoneArea;
				}
			}
			
			double circleRadius = Math.sqrt(circleArea/Math.PI);
			double labelDouble = Util.round(circleRadius,2);
			
			Node n = new Node(Double.toString(labelDouble));
			n.setContour(circle);
			graph.addNode(n);
			circleNodeMap.put(circle,n);
		}
		graph.randomizeNodePoints(new Point(50,50),400,400);

		ArrayList<String> remainingZones = new ArrayList<String>(abstractDiagram.getZoneList());
		ArrayList<String> circles = abstractDiagram.getContours();
		
		remainingZones.remove("");
		remainingZones.remove("0");
		remainingZones.remove("O");
		
		while(remainingZones.size() != 1) {
			String piercingCircle = null;

			for(String circle : circles) {
	
				ArrayList<String> containingZones = new ArrayList<String>();
				for(String z : remainingZones) {
					ArrayList<String> zList = AbstractDiagram.findContourList(z);
					if(zList.contains(circle)) {
						containingZones.add(z);
					}
				}
				if(containingZones.size() != 2) {
					continue;
				}
				String z1 = containingZones.get(0);
				String z2 = containingZones.get(1);
				
				String z1Minusz2 = AbstractDiagram.zoneMinus(z1, z2);
				String z2Minusz1 = AbstractDiagram.zoneMinus(z2, z1);
				
				String splitCircle = null;
				String containment = null;
				if(z1Minusz2.length() == 1 && z2Minusz1.length() == 0) {
					splitCircle = z1Minusz2;
					containment = AbstractDiagram.zoneIntersection(z1,z2);
					containment = AbstractDiagram.zoneMinus(containment,circle);
				}
				if(z1Minusz2.length() == 0 && z2Minusz1.length() == 1) {
					splitCircle = z2Minusz1;
					containment = AbstractDiagram.zoneIntersection(z1,z2);
					containment = AbstractDiagram.zoneMinus(containment,circle);
				}

				if(splitCircle == null) {
					continue;
				}
				
				piercingCircle = circle;

				remainingZones.remove(z1);
				remainingZones.remove(z2);
				String z1Reduced = AbstractDiagram.zoneMinus(z1,piercingCircle);
				String z2Reduced = AbstractDiagram.zoneMinus(z2,piercingCircle);
				if(z1Reduced.length() != 0 && !remainingZones.contains(z1Reduced)) {
					remainingZones.add(z1Reduced);
				}
				if(z2Reduced.length() != 0 && !remainingZones.contains(z2Reduced)) {
					remainingZones.add(z2Reduced);
				}

				// add fixed edges
				Node currentCircleNode = graph.firstNodeWithContour(piercingCircle);
				Edge fixed = new Edge(currentCircleNode,graph.firstNodeWithContour(splitCircle));
				fixed.setType(APCircleDisplay.FIXED);
				graph.addEdge(fixed);
/*				
System.out.println("NEXT PIERCING CURVE");
System.out.println("new circle "+piercingCircle);
System.out.println("split circle "+splitCircle);
System.out.println("contained "+containment);
*/			
				// add attractor edges
				ArrayList<String> containmentList = AbstractDiagram.findContourList(containment);
				for(String containedCircle : containmentList) {
					Edge attractor = new Edge(currentCircleNode,graph.firstNodeWithContour(containedCircle));
					attractor.setType(APCircleDisplay.ATTRACTOR);
					graph.addEdge(attractor);
				}
				
				// rest are repulsor edges
				for(Node n : graph.getNodes()) {
					if(n == currentCircleNode) {
						continue;
					}

					Edge existingEdge = graph.findEdgeBetween(n, currentCircleNode);
					
					if(existingEdge == null) {
						Edge repulsor = new Edge(n, currentCircleNode);
						repulsor.setType(APCircleDisplay.REPULSOR);
						graph.addEdge(repulsor);
					}
				}
			} // end circle iteration
			
			if(piercingCircle == null) {
				// if no piercing found, not a pierced diagram so return null
				return null;
			}
		
		} // end outside loop

		// remainingZones has to be size one, no need to do anything with the last circle, its already fully connected
		String lastZone = remainingZones.get(0);
		if(lastZone.length() != 1) {
			return null;
		}
		
		
		// find the edge labels. Do this last to save time if the description is not pierced
		for(Edge e : graph.getEdges()) {
			if(e.getType().equals(APCircleDisplay.REPULSOR)) {
				e.setLabel(findRepulsorLabel(e));
			}
			if(e.getType().equals(APCircleDisplay.ATTRACTOR)) {
				e.setLabel(findAttractorLabel(e));
			}
			if(e.getType().equals(APCircleDisplay.FIXED)) {
				e.setLabel(Double.toString(findIdealNodeSeparation(graph,e)));
			}

		}
		
		return graph;
	}

	/**
	 * Returns the complete intersection graph, or null if the description is not
	 * a single piercing.
	 */
	public Graph generateAugmentedIntersectionGraph() {

		if(abstractDiagram.getZoneList().size() <= 1) {
			return new Graph();
		}
		
		HashMap<String,Node> circleNodeMap = new HashMap<String,Node>();
		
		Graph graph = new Graph();
		// add the nodes
		for(String circle : abstractDiagram.getContours()) {
			double circleArea = 0.0;
			for(String zone : abstractDiagram.getZoneList()) {
				ArrayList<String> zoneList = AbstractDiagram.findContourList(zone);
				if(zoneList.contains(circle)) {
					double zoneArea = specification.get(zone);
					circleArea += zoneArea;
				}
			}
			double circleRadius = Math.sqrt(circleArea/Math.PI);
			
			Node n = new Node(Double.toString(circleRadius));
			n.setContour(circle);
			graph.addNode(n);
			circleNodeMap.put(circle,n);
		}
		
		graph.randomizeNodePoints(new Point(50,50),400,400);

		ArrayList<String> zones = new ArrayList<String>(abstractDiagram.getZoneList());
		ArrayList<String> circles = abstractDiagram.getContours();
		
		zones.remove("");
		zones.remove("0");
		zones.remove("O");

		for(int i = 0; i < circles.size(); i++) {
			String c1 = circles.get(i);
			
			for(int j = i+1; j < circles.size(); j++) {
				String c2 = circles.get(j);

				boolean intersect = false;
				
				for(String zone : zones) {
					if(zone.contains(c1) && zone.contains(c2)) {
						intersect = true;
						break;
					}
				}

				Node n1 = circleNodeMap.get(c1);
				Node n2 = circleNodeMap.get(c2);
				if(intersect) {
					Edge attractor = new Edge(n1,n2);
					attractor.setLabel(Double.toString(findIdealNodeSeparation(graph,attractor)));

					attractor.setType(APCircleDisplay.ATTRACTOR);
					graph.addEdge(attractor);
System.out.println("Attractor "+c1+" "+c2+" "+attractor.getLabel());
				} else {
					Edge repulsor = new Edge(n1,n2);
					repulsor.setType(APCircleDisplay.REPULSOR);
					repulsor.setLabel(findRepulsorLabel(repulsor));
					graph.addEdge(repulsor);
System.out.println("Repulsor "+c1+" "+c2+" "+repulsor.getLabel());					
				}
				
			}
		}
		
/*		
	
		while(remainingZones.size() != 1) {
			String piercingCircle = null;

			for(String circle : circles) {
	
				ArrayList<String> containingZones = new ArrayList<String>();
				for(String z : remainingZones) {
					ArrayList<String> zList = AbstractDiagram.findContourList(z);
					if(zList.contains(circle)) {
						containingZones.add(z);
					}
				}
System.out.println(circle+" "+containingZones);

				String z1 = containingZones.get(0);
				String z2 = containingZones.get(1);
				
				String z1Minusz2 = AbstractDiagram.zoneMinus(z1, z2);
				String z2Minusz1 = AbstractDiagram.zoneMinus(z2, z1);
				
				String splitCircle = null;
				String containment = null;
				if(z1Minusz2.length() == 1 && z2Minusz1.length() == 0) {
					splitCircle = z1Minusz2;
					containment = AbstractDiagram.zoneIntersection(z1,z2);
					containment = AbstractDiagram.zoneMinus(containment,circle);
				}
				if(z1Minusz2.length() == 0 && z2Minusz1.length() == 1) {
					splitCircle = z2Minusz1;
					containment = AbstractDiagram.zoneIntersection(z1,z2);
					containment = AbstractDiagram.zoneMinus(containment,circle);
				}


				if(splitCircle == null) {
					continue;
				}
				
				piercingCircle = circle;

				remainingZones.remove(z1);
				remainingZones.remove(z2);
				String z1Reduced = AbstractDiagram.zoneMinus(z1,piercingCircle);
				String z2Reduced = AbstractDiagram.zoneMinus(z2,piercingCircle);
				if(z1Reduced.length() != 0 && !remainingZones.contains(z1Reduced)) {
					remainingZones.add(z1Reduced);
				}
				if(z2Reduced.length() != 0 && !remainingZones.contains(z2Reduced)) {
					remainingZones.add(z2Reduced);
				}

				// add fixed edges
				Node currentCircleNode = graph.firstNodeWithContour(piercingCircle);
				Edge fixed = new Edge(currentCircleNode,graph.firstNodeWithContour(splitCircle));
				fixed.setType(APCircleDisplay.FIXED);
				graph.addEdge(fixed);

		
				// add attractor edges
				ArrayList<String> containmentList = AbstractDiagram.findContourList(containment);
				for(String containedCircle : containmentList) {
					Edge attractor = new Edge(currentCircleNode,graph.firstNodeWithContour(containedCircle));
					attractor.setType(APCircleDisplay.ATTRACTOR);
					graph.addEdge(attractor);
				}
				
				// rest are repulsor edges
				for(Node n : graph.getNodes()) {
					if(n == currentCircleNode) {
						continue;
					}

					Edge existingEdge = graph.findEdgeBetween(n, currentCircleNode);
					
					if(existingEdge == null) {
						Edge repulsor = new Edge(n, currentCircleNode);
						repulsor.setType(APCircleDisplay.REPULSOR);
						graph.addEdge(repulsor);
					}
				}
			} // end circle iteration
			
			if(piercingCircle == null) {
				// if no piercing found, not a pierced diagram so return null
				return null;
			}
		
		} // end outside loop

		// remainingZones has to be size one, no need to do anything with the last circle, its already fully connected
		String lastZone = remainingZones.get(0);
		if(lastZone.length() != 1) {
			return null;
		}

		
		// find the edge labels. Do this last to save time if the description is not pierced
		for(Edge e : graph.getEdges()) {
			if(e.getType().equals(APCircleDisplay.REPULSOR)) {
				e.setLabel(findRepulsorLabel(e));
			}
			if(e.getType().equals(APCircleDisplay.ATTRACTOR)) {
				e.setLabel(findAttractorLabel(e));
			}
			if(e.getType().equals(APCircleDisplay.FIXED)) {
				e.setLabel(findFixedLabel(graph,e));
			}

		}
*/		
		return graph;
	}



	public static String findAttractorLabel(Edge e) {
		Node n1 = e.getFrom();
		Node n2 = e.getTo();
		double label1 = Double.parseDouble(n1.getLabel());
		double label2 = Double.parseDouble(n2.getLabel());
		double maxDistance = label1-label2;
		if(maxDistance < 0) {
			maxDistance = label2-label1;
		}
		double ret = Util.round(maxDistance,2);
		return Double.toString(ret);
	}
	
	
	
	public static String findRepulsorLabel(Edge e) {
		Node n1 = e.getFrom();
		Node n2 = e.getTo();
		double label1 = Double.parseDouble(n1.getLabel());
		double label2 = Double.parseDouble(n2.getLabel());
		double minDistance = label1+label2;
		double ret = Util.round(minDistance,2);
		return Double.toString(ret);
	}
	

	protected double findIdealNodeSeparation(Graph graph, Edge e) {
		
		Node n1 = e.getFrom();
		Node n2 = e.getTo();

		double label1 = 20;
		double label2 = 20;
		try {
			label1 = Double.parseDouble(n1.getLabel());
			label2 = Double.parseDouble(n2.getLabel());
		} catch (Exception exception) {
			System.out.println(exception.getStackTrace());
		}
		
		double intersectionArea = 0.0;
	
		for(String z : abstractDiagram.getZoneList()) {
			
			ArrayList<String> zList = AbstractDiagram.findContourList(z);
			if(zList.contains(n1.getContour()) && zList.contains(n2.getContour())) {
				intersectionArea += specification.get(z);
			}
		}
			
		double intersectionDistance = findCircleCircleSeparation(label1,label2,intersectionArea);

		double ret = Util.round(intersectionDistance,2);
		return ret;
	}
	
	



	/**
	 * Use numerical methods to find the distance between two circles with the given areas.
	 */
	private double findCircleCircleSeparation(double radius1, double radius2, double intersectionArea) {

		
		double area1 = Math.PI*radius1*radius1;
		double area2 = Math.PI*radius2*radius2;

		
		// bisection search bit starts here
		// two starting limits when circles just touch outside and inside

		double left = radius1-radius2;
		if(left < 0) {
			left = radius2-radius1;
		}
		double right = radius1+radius2;


		double newError = Double.MAX_VALUE;
		double separation = 0.0;
		
		while(Math.abs(newError) > Util.NEARLY_ZERO) {

			if(Math.abs(left-right) < Util.NEARLY_ZERO) {
//				System.out.println("Failed to optimize. Error is "+newError);
				break;
			}
			
			separation = left+(right-left)/2;
			
//TODO precision problems finding middle area
			double newArea = findCircleCircleIntersectionArea(radius1, radius2, separation);
			newError = intersectionArea-newArea;
			if(newError >= 0) {
				right = separation;
			} else {
				left = separation;
			}
		}

		return separation;
	}
	
	
	

	/**
	 */
	public static double findCircleCircleIntersectionArea(double radius1, double radius2, double d) {

	    // If the circles are too far apart, they have no common area.
		if (d > radius1+radius2) {
			return 0;
		}

	    // If one circle is within another, the common area is given by the smaller circle.
		if (d < Math.abs(radius1-radius2)) {
			double r = Math.min(radius1, radius2);
			return Math.PI*r*r;
	    }

		return radius1*radius1*Math.acos((d*d+radius1*radius1-radius2*radius2)/(2*d*radius1))+radius2*radius2*Math.acos((d*d+radius2*radius2-radius1*radius1)/(2*d*radius2))-0.5*Math.sqrt((-1*d+radius1+radius2)*(d+radius1-radius2)*(d-radius1+radius2)*(d+radius1+radius2));
	}

	public boolean fromString(String s) {
		
		String[] specifications = s.split("\n");

		AbstractDiagram ad = new AbstractDiagram("0");
		HashMap<String,Double> spec = new HashMap<String,Double>();
		for(int i = 0; i< specifications.length; i++) {
			String pair = specifications[i].trim();
			if(pair.length() == 0) {
				// ignore empty lines
				continue;
			}
			String[] splitPair = pair.split(" ");
			if(splitPair.length == 1) {
				System.out.println("Only one element on a specification line \""+pair+"\"");
				return false;
			}
			if(splitPair.length > 2) {
				System.out.println("More than two elements on a specification line \""+pair+"\"");
				return false;
			}
			String zone = splitPair[0];
			if(ad.getZoneList().contains(zone)) {
				System.out.println("Multiple occurences of zone \""+pair+"\"");
				return false;
			}
			double area = 0.0;
			try {
				area = Double.parseDouble(splitPair[1]);
			} catch(Exception exception) {
				System.out.println("Cannot parse second element as a double \""+pair+"\"");
				return false;
			}
			
			zone = AbstractDiagram.orderZone(zone);
			ad.addZone(zone);
			spec.put(zone,area);
		}
		
		abstractDiagram = ad;
		specification = spec;
		return true;
	}

	
	public String toString() {
		String ret = "";
		Iterator<String> i = abstractDiagram.getZoneList().iterator();
		while(i.hasNext()) {
			String zone = i.next();
			if(!AbstractDiagram.isOutsideZone(zone)) {
				ret += zone+" "+getZoneArea(zone)+"\n";
			}
		}

		return ret;
	}

}

