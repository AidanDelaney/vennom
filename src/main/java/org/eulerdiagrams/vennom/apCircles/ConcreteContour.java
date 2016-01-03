package org.eulerdiagrams.vennom.apCircles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class ConcreteContour {

	protected String abstractContour;
	protected Polygon polygon;
	protected Area area;
	protected Polygon maxArea;
	protected Polygon minArea;
	protected boolean isCircle =false;
	protected String concurrentLabels= "";
	protected boolean isConcurrent = false;
	
	public ConcreteContour() {
	
	}

	public ConcreteContour(String abstractContour, Polygon polygon) {
		this.abstractContour = abstractContour;
		this.polygon = polygon;
		resetArea();
	}
	public String getConcurrentLabels(){
		return concurrentLabels;
	}
	public void setConcurrent(boolean concurrent){
		isConcurrent = concurrent;
	}
	public boolean getIsConcurrent(){
		return isConcurrent;
	}

	public void setLabel(String aLabel){abstractContour=aLabel;}

	public String getAbstractContour() {
		return abstractContour;
	}
	public Polygon getPolygon() {
		return polygon;
	}

	public Area getArea() {
		return area;
	}

	private void resetArea() {
		if (polygon == null) {
			area = new Area();
		} else {
			area = new Area(polygon);
		}
		//setContourLines();
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;		
		resetArea();
	}

	/**
	 * Generate the areas for each zone
	 */
	public static HashMap<String, Area> generateZoneAreas(ArrayList<ConcreteContour> concreteContours) {
		// We could try each possible intersection
		// but that is a guaranteed 2 power n algorithm
		// Here we take each intersecting pair and
		// test if any contours can be added to it.
		// The intersecting contours are then built up.
		// Then any intersections that are wholly contained
		// in the remaining contour set are removed.

		// the zones that  still may have further intersections
		ArrayList<String> activeZones = new ArrayList<String>();
		// all the zones tried for intersection
		ArrayList<String> triedZones = new ArrayList<String>();
		// the correct zones and areas
		HashMap<String, Area> currentZoneMap = new HashMap<String, Area>();
		// all tried maps
		HashMap<String, Area> zoneAreaMap = new HashMap<String, Area>(); 

		// create all existing intersections
		// then filter for those that dont exist except in other
		// zones - eg. the diagram "0 abc", first we create a b c ab ac abc
		// then remove all but abc by testing against contours not
		// in the intersection

		// start with the outside zone
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		
		for (ConcreteContour concreteContour : concreteContours) {
			// String abstractContour = concreteContour.getAbstractContour();

			Area contourArea = new Area(concreteContour.getArea());

			Rectangle bounds = contourArea.getBounds();
			if (bounds.getX() < minX) {
				minX = bounds.getX();
			}
			if (bounds.getX() + bounds.getWidth() > maxX) {
				maxX = bounds.getX() + bounds.getWidth();
			}
			if (bounds.getY() < minY) {
				minY = bounds.getY();
			}
			if (bounds.getY() + bounds.getHeight() > maxY) {
				maxY = bounds.getY() + bounds.getHeight();
			}
		}
		if (minX > maxX) {
			minX = 0.0;
			maxX = 0.0;
			minY = 0.0;
			maxY = 0.0;
		}

		
		// use a rectangle that bounds all contours, to represent the outer zone
		int outerX1 = Util.convertToInteger(minX) - 100;
		int outerX2 = Util.convertToInteger(maxX) + 100;
		int outerY1 = Util.convertToInteger(minY) - 100;
		int outerY2 = Util.convertToInteger(maxY) + 100;
		Polygon outerPolygon = new Polygon();
		outerPolygon.addPoint(outerX1, outerY1);
		outerPolygon.addPoint(outerX2, outerY1);
		outerPolygon.addPoint(outerX2, outerY2);
		outerPolygon.addPoint(outerX1, outerY2);
		Area outerArea = new Area(outerPolygon);

		currentZoneMap.put("", outerArea);
		activeZones.add("");
		zoneAreaMap.put("", new Area(outerArea));

		
		
		
		// add the existing intersections
		while (activeZones.size() > 0) {
			while (activeZones.size() != 0) {

				String activeZone = activeZones.get(0);
				activeZones.remove(0);
				// test every contour intersection with a zone
				for (ConcreteContour concreteContour : concreteContours) {

					String abstractContour = concreteContour.getAbstractContour();
					ArrayList<ConcreteContour> duplicatedContours = new ArrayList<ConcreteContour>();
					for(ConcreteContour cc: concreteContours){
						if(cc.getAbstractContour().compareTo(abstractContour) == 0 && cc!= concreteContour)
							duplicatedContours.add(cc);
					}
					
					if (activeZone.indexOf(abstractContour) != -1) {
						// dont need to consider a zone that already contains the contour
						continue;
					}
					String testZone = activeZone + abstractContour;
					testZone = AbstractDiagram.orderZone(testZone);

					if (triedZones.contains(testZone)) {
						// don't need to consider zones that have already been attempted
						continue;
					}
					triedZones.add(testZone);
					Area intersectArea = new Area(concreteContour.getArea());								 
					Area zoneArea = zoneAreaMap.get(activeZone);				
					intersectArea.intersect(zoneArea);
					if (!intersectArea.isEmpty()) {
						currentZoneMap.put(testZone, intersectArea);
						activeZones.add(testZone);
						zoneAreaMap.put(testZone, intersectArea);
					}	
					else{						
						if(duplicatedContours.size()!=0){
							for(ConcreteContour cc: duplicatedContours){
								intersectArea = new Area(cc.getArea());								 
								zoneArea = zoneAreaMap.get(activeZone);				
								intersectArea.intersect(zoneArea);
								if (!intersectArea.isEmpty()) {
									currentZoneMap.put(testZone, intersectArea);
									activeZones.add(testZone);
									zoneAreaMap.put(testZone, intersectArea);
								}	
							}
						}
					}
				}
			}
		}
		
		// filter out the intersections that are completely contained
		// in the other contours
		HashMap<String, Area> retZoneMap = new HashMap<String, Area>();
		for (String z : currentZoneMap.keySet()) {

			Area zoneArea = new Area(zoneAreaMap.get(z));
			for (ConcreteContour concreteContour : concreteContours) {
				String abstractContour = concreteContour.getAbstractContour();
				if (z.indexOf(abstractContour) == -1) {
					Area otherContoursArea = concreteContour.getArea();
					zoneArea.subtract(otherContoursArea);
				}
			}

			if (!zoneArea.isEmpty()) {
				retZoneMap.put(z, zoneArea);
			}
		}
	 

		return retZoneMap;
	}

	/**
	 * Deals with areas that have a nearly zero size section. This returns the
	 * area without that section. This repairs problems caused by
	 * Area.intersect.
	 */
	public static ArrayList<Polygon> polygonsFromArea(Area a) {
		if (!a.isPolygonal()) {
			// cant do anything if its not a polygon
			return null;
		}

		// create polygons, add them to the returned list if their area is large
		// enough
		ArrayList<Polygon> ret = new ArrayList<Polygon>();
		Polygon p = new Polygon();
		double[] coords = new double[6];
		PathIterator pi = a.getPathIterator(null);
		while (!pi.isDone()) {
			int coordType = pi.currentSegment(coords);
			if (coordType == PathIterator.SEG_CLOSE
					|| coordType == PathIterator.SEG_MOVETO) {
				if (coordType == PathIterator.SEG_CLOSE) {
					int x = Util.convertToInteger(coords[0]);
					int y = Util.convertToInteger(coords[1]);
					p.addPoint(x, y);
				}
				if (p.npoints > 2) { // no need to deal with empty polygons
					Rectangle2D boundingRectangle = p.getBounds2D();
					double boundingArea = boundingRectangle.getWidth()
							* boundingRectangle.getHeight();
					if (boundingArea >= 1.0) { // only add polygons of decent
												// size to returned area
						ret.add(p);
					}
				}
				p = new Polygon(); // start with the next polygon
				if (coordType == PathIterator.SEG_MOVETO) {
					int x = Util.convertToInteger(coords[0]);
					int y = Util.convertToInteger(coords[1]);
					p.addPoint(x, y);
				}
			}
			if (coordType == PathIterator.SEG_LINETO) {
				int x = Util.convertToInteger(coords[0]);
				int y = Util.convertToInteger(coords[1]);
				p.addPoint(x, y);
			}
			;
			if (coordType == PathIterator.SEG_CUBICTO) {
				System.out.println("Found a PathIterator.SEG_CUBICTO");
			}
			if (coordType == PathIterator.SEG_QUADTO) {
				System.out.println("Found a PathIterator.SEG_QUADTO");
			}

			pi.next();
		}
		return ret;
	}
	
	

	/**
	 * Find any point inside the polygon. For a simple polygon there must be a
	 * triple of consecutive points that have their triangle centre in the
	 * polygon, so return that centre. Return null if no point can be found (in
	 * case of empty polygon or some non-simple polygons).
	 */
	public static Point2D findPointInsidePolygon(Polygon p) {

		double delta = 0.1;
		// keep reducing delta further until an internal point is found
		for (int reduceLoop = 1; reduceLoop < 200; reduceLoop++) { 
			for (int i1 = 0; i1 <= p.npoints - 3; i1++) {
				double x1 = p.xpoints[i1];
				double y1 = p.ypoints[i1];

				int i2 = i1 + 1;
				double x2 = p.xpoints[i2];
				double y2 = p.ypoints[i2];
				while (x2 == x1 && y2 == y1) {
					// duplicate points sometimes seen in polygons, so skip them
					i2++;
					if (i2 >= p.npoints) {
						break;
					}
					x2 = p.xpoints[i2];
					y2 = p.ypoints[i2];
				}
				if (i2 >= p.npoints) {
					break;
				}

				int i3 = i2 + 1;
				double x3 = p.xpoints[i3];
				double y3 = p.ypoints[i3];
				while (x3 == x2 && y3 == y2) {
					// duplicate points sometimes seen in polygons, so skip them
					i3++;
					if (i3 >= p.npoints) {
						break;
					}
					x3 = p.xpoints[i3];
					y3 = p.ypoints[i3];
				}
				if (i3 >= p.npoints) {
					break;
				}

				double xMiddle12 = x1 + (x2 - x1) / 2;
				double xMiddle13 = x1 + (x3 - x1) / 2;
				double xMiddle23 = x2 + (x3 - x2) / 2;

				double yMiddle12 = y1 + (y2 - y1) / 2;
				double yMiddle13 = y1 + (y3 - y1) / 2;
				double yMiddle23 = y2 + (y3 - y2) / 2;

				double xTriangleMiddle = (xMiddle12 + xMiddle13 + xMiddle23) / 3;
				double yTriangleMiddle = (yMiddle12 + yMiddle13 + yMiddle23) / 3;
				if (p.contains(xTriangleMiddle, yTriangleMiddle)) {
					// dont pick a point too close to the edge of the poly, as
					// this may not
					// be in the area, due to rounding
					if (!p.contains(xTriangleMiddle, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle, yTriangleMiddle - delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle - delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle - delta)) {
						continue;
					}

					return new Point2D.Double(xTriangleMiddle, yTriangleMiddle);
				}
			}
			delta = delta / 2;
		}

//		System.out.println("Can't find internal point for polygon");

		return null; // should never get here for simple polygons
	}

	/**
	 * Generate an sorted list of zones from the interlinking polygons.
	 * Duplicate zones are not returned.
	 */
	public static String generateAbstractDiagramFromList(
			ArrayList<ConcreteContour> concreteContours) {

		if (concreteContours == null) {
			return "0";
		}

		HashMap<String, Area> zoneMap = generateZoneAreas(concreteContours);

		ArrayList<String> zones = new ArrayList<String>(zoneMap.keySet());

		AbstractDiagram.sortZoneList(zones);

		StringBuffer zoneSB = new StringBuffer();
		Iterator<String> it = zones.iterator();
		while (it.hasNext()) {
			String z = it.next();
			if (z.equals("")) {
				z = "0";
			}
			zoneSB.append(z);
			if (it.hasNext()) {
				zoneSB.append(" ");
			}
		}

		return zoneSB.toString();
	}


}
