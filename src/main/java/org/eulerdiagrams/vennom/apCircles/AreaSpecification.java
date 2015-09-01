package org.eulerdiagrams.vennom.apCircles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.graph.*;

public class AreaSpecification {

	private HashMap<String,Double> specification;
	protected AbstractDiagram abstractDiagram;
	/** Dividing all specifications by this makes the total area 1.0 */
	protected double normalizationFactor = 0.0;
	
	public static float scalingFactor = -1;
	public static int polygonResolution = 1000;
	
	public AreaSpecification(AbstractDiagram abstractDiagram) {
		this.abstractDiagram = abstractDiagram;
		setSpecification(new HashMap<String, Double>());
		setAllZoneAreas(0.0);
		findNormalizationFactor();
	}

	public AreaSpecification(AbstractDiagram abstractDiagram, HashMap<String,Double> specification) {
		this.abstractDiagram = abstractDiagram;
		this.setSpecification(specification);
		findNormalizationFactor();
	}

	public AreaSpecification(String s) {
		this.abstractDiagram = new AbstractDiagram("");
		setSpecification(new HashMap<String, Double>());
		fromString(s);
		findNormalizationFactor();
	}
	
	
	public void setSpecification(HashMap<String,Double> specification) {this.specification = specification;}

	public HashMap<String,Double> getSpecification() {return specification;}

	


	protected double findNormalizationFactor() {
		double totalArea = 0.0;
		for(String s : getSpecification().keySet()) {
			totalArea += getSpecification().get(s);
		}
		return totalArea;
	}
	
	
	public double normalizedArea(String zone) {
		if(normalizationFactor == 0.0) {
			normalizationFactor = findNormalizationFactor();
		}
		double area = getSpecification().get(zone);
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
		getSpecification().put(zone, area);
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
		return getSpecification().get(zone);
	}

	public AbstractDiagram getAbstractDiagram() {
		return abstractDiagram;
	}
	


	/**
	 * Returns the complete intersection graph, or null if the description is not
	 * a single piercing.
	 */
	public Graph generatePiercedAugmentedIntersectionGraph() {


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
			double labelDouble = circleRadius;
//			labelDouble = Util.round(labelDouble,2);
			
			Node n = new Node(Double.toString(labelDouble));
			n.setContour(circle);
			n.setScore(labelDouble);
			graph.addNode(n);
			circleNodeMap.put(circle,n);
		}
		graph.randomizeNodePoints(new Point(50,50),400,400,1);

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
				double repulsion = findMinimumSeparation(e);
				e.setLabel(Double.toString(repulsion));
				e.setScore(repulsion);
			}
			if(e.getType().equals(APCircleDisplay.ATTRACTOR)) {
				double attraction = findAttractionValue(e);
				e.setLabel(Double.toString(attraction));
				e.setScore(attraction);
			}
			if(e.getType().equals(APCircleDisplay.FIXED)) {
				double fixed = findFixedValue(graph,e);
				e.setLabel(Double.toString(fixed));
				e.setScore(fixed);
			}

		}
		
		return graph;
	}


	public static double findAttractionValue(Edge e) {
		Node n1 = e.getFrom();
		Node n2 = e.getTo();
		double label1 = n1.getScore();
		double label2 = n2.getScore();
		double maxDistance = label1-label2;
		if(maxDistance < 0) {
			maxDistance = label2-label1;
		}
		double ret = maxDistance;
//		ret = Util.round(ret,2);
		return ret;
	}
	

	
	public static Double findMinimumSeparation(Edge e) {
		Node n1 = e.getFrom();
		Node n2 = e.getTo();
		double label1 = n1.getScore();
		double label2 = n2.getScore();
		double minDistance = label1+label2;
		double ret = minDistance;
//		ret = Util.round(ret,2);
		return ret;
	}
	

	protected double findFixedValue(Graph graph, Edge e) {
		
		if(!e.getType().equals(APCircleDisplay.FIXED)) {
			System.out.println("Non fixed node passed to labelFixed "+e);
			return -1;
		}


		Node n1 = e.getFrom();
		Node n2 = e.getTo();

		double label1 = 20;
		double label2 = 20;
		try {
			label1 = n1.getScore();
			label2 = n2.getScore();
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

		double ret = intersectionDistance;
//		ret = Util.round(ret,2);
		return ret;
	}
	


	/**
	 * Returns the complete graph for general layout.
	 */
	public Graph generateGeneralAugmentedIntersectionGraph() {
		

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
					double zoneArea = getSpecification().get(zone);
					circleArea += zoneArea;
				}
			}
			double circleRadius = Math.sqrt(circleArea/Math.PI);
			
			Node n = new Node(Double.toString(circleRadius));
			n.setScore(circleRadius);
			n.setContour(circle);
			graph.addNode(n);
			circleNodeMap.put(circle,n);
		}
		
		graph.randomizeNodePoints(new Point(50,50),400,400,1);

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
				
				boolean containment = false;


				if(abstractDiagram.contourContainment(c1,c2)) { // c2 is entirely in c1
					containment = true;
				}
				if(abstractDiagram.contourContainment(c2,c1)) { // c2 is entirely in c1
					containment = true;
				}

				Node n1 = circleNodeMap.get(c1);
				Node n2 = circleNodeMap.get(c2);
				if(containment) {
					Edge containing = new Edge(n1,n2);
					double maxSeparation = findMaxContainmentDistance(n1,n2);
					containing.setType(APCircleDisplay.CONTAINMENT);
					containing.setLabel(Double.toString(maxSeparation));
					containing.setScore(maxSeparation);
					graph.addEdge(containing);
//System.out.println("CONTAINMENT "+maxSeparation);
				} else if(intersect) {
					Edge ideal = new Edge(n1,n2);
					double idealLength = findIdealNodeSeparation(graph,ideal);
					ideal.setType(APCircleDisplay.IDEAL);
					ideal.setLabel(Double.toString(idealLength));
					ideal.setScore(idealLength);
					graph.addEdge(ideal);
//System.out.println("IDEAL "+idealLength);
				} else {
					Edge separator = new Edge(n1,n2);
					double minSeparation = findMinimumSeparation(separator);
					separator.setType(APCircleDisplay.SEPARATOR);
					separator.setLabel(Double.toString(minSeparation));
					separator.setScore(minSeparation);
					graph.addEdge(separator);
//System.out.println("SEPARATOR "+minSeparation);
				}
				
			}
		}
		

		return graph;
	}


	/**
	 * Circle for n2 entirely within n1
	 */
	protected double findMaxContainmentDistance(Node n1, Node n2) {

		double radius1 = 20;
		double radius2 = 20;
		try {
			radius1 = n1.getScore();
			radius2 = n2.getScore();
		} catch (Exception exception) {
			System.out.println(exception.getStackTrace());
		}

		double maxSeparation = Math.abs(radius1-radius2);
		
		return maxSeparation;
	}

	protected double findIdealNodeSeparation(Graph graph, Edge e) {
		
		Node n1 = e.getFrom();
		Node n2 = e.getTo();

		double label1 = 20;
		double label2 = 20;
		try {
			label1 = n1.getScore();
			label2 = n2.getScore();
		} catch (Exception exception) {
			System.out.println(exception.getStackTrace());
		}
		
		double intersectionArea = 0.0;
	
		for(String z : abstractDiagram.getZoneList()) {
			
			ArrayList<String> zList = AbstractDiagram.findContourList(z);
			if(zList.contains(n1.getContour()) && zList.contains(n2.getContour())) {
				intersectionArea += getSpecification().get(z);
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
		setSpecification(spec);
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

	

	static public ArrayList<ConcreteContour> convertCirclesToCCs(Graph g, int polygonResolution) {
		
		ArrayList<ConcreteContour> circleContours = new ArrayList<ConcreteContour>();
		
//graphPanel.polygons = new ArrayList<Polygon>();
		// scale polygons for best resolution
		float biggestRadius = -1;
		int i = 0;
		for(Node n : g.getNodes()) {
			double radius = Double.parseDouble(n.getLabel());
			if(radius > biggestRadius) {
				biggestRadius = (float)radius;
			}
			i++;
		}
		
		scalingFactor = 1.0E4f/biggestRadius; // value found by experimentation
//scalingFactor = 1;

//System.out.println("Approximation error ");
		for(Node n : g.getNodes()) {
			int scaledX = Util.convertToInteger(n.getX()*scalingFactor);
			int scaledY = Util.convertToInteger(n.getY()*scalingFactor);
			
			double radius = Double.parseDouble(n.getLabel());
			
			Polygon p = RegularPolygon.generateRegularPolygon(scaledX, scaledY, radius*scalingFactor, polygonResolution);
			
			ConcreteContour cc = new ConcreteContour(n.getContour(), p);
			circleContours.add(cc);
// graphPanel.polygons.add(p);
//report approximation error
			
			double actualArea = (float)(Math.PI*radius*radius);
//System.out.println("scaledActualArea "+(actualArea*scalingFactor*scalingFactor));
			float scaledPolygonArea = Util.computePolygonAreaFloat(p);
//System.out.println(n.getContour()+" scaledPolygonArea "+scaledPolygonArea);
			float polygonArea= scaledPolygonArea/(scalingFactor*scalingFactor);
			double percentError = 100*(polygonArea-actualArea)/actualArea;
			if(Math.abs(percentError) > 0.1) {
				System.out.println(n.getContour()+" has circle area error > 0.1% is: "+percentError+"% actual area: "+actualArea+" polygon Area: "+polygonArea);
			}

		}
		
		return circleContours;
	}

	public static AreaSpecification exactRandomDiagramFactory(int minX, int minY, int maxX, int maxY, int minRadius, int maxRadius, int circleCount, long seed, String fileName) {

		ArrayList<Integer> xList = new ArrayList<Integer>();
		ArrayList<Integer> yList = new ArrayList<Integer>();
		ArrayList<Double> radiusList = new ArrayList<Double>();
		Random random = new Random(seed);
		for(int i = 0; i < circleCount; i++) {
			int x = random.nextInt(1+maxX-minX);
			x += minX;
			int y = random.nextInt(1+maxY-minY);
			y += minY;
			double radius = random.nextInt(1+maxRadius-minRadius);
			radius += minRadius;
			
			xList.add(x);
			yList.add(y);
			radiusList.add(radius);
		}
		AreaSpecification as = findAreaSpecificationFromCircles(xList,yList,radiusList);
		
		if(fileName != null && fileName.length() > 0) {
			File file = new File(fileName);

			String svg = "<svg  width=\""+500+"\" height=\""+500+"\">\n";
			for (int i = 0; i < circleCount; i++) {
				double x = xList.get(i);
				double y = yList.get(i);
				double r = radiusList.get(i);
				svg += "\t<circle cx=\""+x+"\" cy=\""+y+"\" r=\""+r+"\" fill=\"none\" stroke=\"black\" stroke-width=\"2\" />\n";
			}
			
			svg += "</svg>";
			try {
				BufferedWriter b = new BufferedWriter(new FileWriter(file));

		// save the nodes
				b.write(svg);
				b.newLine();
				b.close();
			}
			catch(IOException e){
				System.out.println("An IO exception occured when saving svg in exactRandomDiagramFactory("+file.getName()+"\n"+e+"\n");
			}
		}
		
		return as;
	}


	public static AreaSpecification findAreaSpecificationFromCircles(ArrayList<Integer> xList, ArrayList<Integer> yList, ArrayList<Double> radiusList) {

		Graph g = new Graph();
		for(int i = 0; i < radiusList.size(); i++) {
			Double radius = radiusList.get(i);
			int x = xList.get(i);
			int y = yList.get(i);
			Node n = new Node(radius.toString());
			n.setX(x);
			n.setY(y);
			n.setScore(radius);
			Character label = (char)('a'+i);
			n.setContour(label.toString());
			g.addNode(n);
//System.out.print(label+" "+x+":"+y+":"+radius+" ");
		}
//System.out.println();
		
		ArrayList<ConcreteContour> circleConcreteContours = AreaSpecification.convertCirclesToCCs(g,polygonResolution);
		HashMap<String,Double> currentValuesMap = new HashMap<String,Double>();
		HashMap<String, Area> zoneAreaMap = ConcreteContour.generateZoneAreas(circleConcreteContours);
		for (String zone : zoneAreaMap.keySet()) {
			Area area = zoneAreaMap.get(zone);
	
			ArrayList<Polygon> polygons = ConcreteContour.polygonsFromArea(area);
			if (zone.equals("")) {
				// outer zone
				continue;
			}
	
			// remove polygons that surround holes in the zone
			// we only want polygons where the fill is the zone
			// eg. diagram "0 a b ab" where a and b go through each other
			// has two polys filled with the zone for both a and b
			// the diagram "0 a b" drawn normally has three
			// polys for 0 (including border), only one of which
			// is filled with 0.
			//
			// What about holes in holes? Does this happen with
			// simple polygons? I don't think so.
			ArrayList<Polygon> polysCopy = new ArrayList<Polygon>(polygons);
			for (Polygon polygon : polysCopy) {
				Point2D insidePoint = ConcreteContour.findPointInsidePolygon(polygon);
				if (insidePoint != null && !area.contains(insidePoint)) {
					polygons.remove(polygon);
				}
			}
	
			double totalPolygonArea = 0.0;
			for(Polygon p : polygons) {
	//graphPanel.polygons.add(p);
				double scaledPolygonArea = Util.computePolygonArea(p);
				double polygonArea= scaledPolygonArea/(scalingFactor*scalingFactor);
				totalPolygonArea += polygonArea;
			}
			currentValuesMap.put(zone,totalPolygonArea);
		}
		
		
		ArrayList<String> zoneList = new ArrayList<String>(currentValuesMap.keySet());
		AbstractDiagram.sortZoneList(zoneList);
		AbstractDiagram ad = new AbstractDiagram(zoneList);
		
		AreaSpecification ret = new AreaSpecification(ad,currentValuesMap);
		
		return ret;
	}


	
}

