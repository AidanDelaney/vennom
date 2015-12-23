package org.eulerdiagrams.vennom.apCircles;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import org.eulerdiagrams.vennom.graph.Graph;


/**
 * Utility Methods for the Convex package.
 *@author Peter Rodgers
 */
public class Util {
	
	public static final double NEARLY_ZERO = 0.00001;
    public static int debug_level = 1; 
	// 1 = test framework, 2 = method in/out, 3 = detail, 4 = desperation
	
	
	/**
	 * Round to the given number of decimal places
	 */
	public static double round(double inAmount,int decimalPlaces) {

		long divider = 1;
		for(int i = 1; i<= decimalPlaces; i++) {
			divider *= 10;
		}
		double largeAmount = Math.rint(inAmount*divider);
		return(largeAmount/divider);
	}

	/**
	 * Convert double to integer
	 */
	public static int convertToInteger(double inAmount) {
		
		double noDecimals = round(inAmount,0);
		return (int)noDecimals;
	}


	/** Gets the angle of the line between two points */
	public static double lineAngle(Point2D.Double p1, Point2D.Double p2) {
    	double rise = p2.y - p1.y;
    	double run = p2.x - p1.x;
       	double angle = -Math.atan2(rise, run);
       	if(angle < 0){
       		angle = 2*Math.PI + angle;
       	}
       	return angle;
	}

	/**
	 * Returns the angle formed by p2 between p1 and p3
	 * @return a value between 0 and PI radians
	 */
	public static double angle(Point p1, Point p2, Point p3) {
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		Point2D.Double pd3 = new Point2D.Double(p3.x,p3.y);
		double ret = angle(pd1,pd2,pd3);
		return ret;
	}

	/**
	 * Returns the angle formed by p2 between p1 and p3.
	 * @return a value between 0 and PI radians
	 */
	public static double angle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		  
		double angle = 0.0;
		  
		double angle1 = lineAngle(p2,p1);
		double angle2 = lineAngle(p2,p3);
		  
		angle = Math.abs(angle1 - angle2);
	  
		if(angle > Math.PI) {
			angle = Math.PI*2 - angle;
		}
		return angle;
	}
	

	
  	/** Get the distance between two points. */
	public static double distance(double x1, double y1, double x2, double y2){
		Point2D.Double pd1 = new Point2D.Double(x1,y1);
		Point2D.Double pd2 = new Point2D.Double(x2,y2);
		double ret = distance(pd1,pd2);
		return ret;
	}
	
  	/** Get the distance between two points. */
	public static double distance(Point p1, Point p2){
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		double ret = distance(pd1,pd2);
		return ret;
	}
	
	
  	/** Get the distance between two points. */
	public static double distance(Point2D.Double p1, Point2D.Double p2){
			double rise = p1.y - p2.y;
			double run = p1.x - p2.x;
			double distance = Math.sqrt(Math.pow(rise, 2)+ Math.pow(run, 2));
			return distance;
	}
	
	
	/** Get a point that is the given fraction between the given points */
	public static Point betweenPoints(Point p1, Point p2, double fraction) {
		return new Point(convertToInteger(p1.x+(p2.x-p1.x)*fraction),convertToInteger(p1.y+(p2.y-p1.y)*fraction));
	}

	/** Get a point that is the given fraction between the given points */
	public static Point2D.Double betweenPoints(Point2D p1, Point2D p2, double fraction) {
		return new Point2D.Double(p1.getX()+(p2.getX()-p1.getX())*fraction,p1.getY()+(p2.getY()-p1.getY())*fraction);
	}

	
	/**
	 * Intersection point of two lines, first line given by p1 and
	 * p2, second line given by p3 and p4.
	 * @return the intersection point, or null if the lines are parallel.
	 */
	public static Point2D.Double intersectionPointOfTwoLines(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double p4) {

		double x1 = p1.x;
		double y1 = p1.y;
		double x2 = p2.x;
		double y2 = p2.y;
		double x3 = p3.x;
		double y3 = p3.y;
		double x4 = p4.x;
		double y4 = p4.y;

		double x = x1+ (x2-x1)*(((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/((y4-y3)*(x2-x1)-(x4-x3)*(y2-y1)));
		double y = y1+ (y2-y1)*(((y4-y3)*(x1-x3)-(x4-x3)*(y1-y3))/((x4-x3)*(y2-y1)-(y4-y3)*(x2-x1)));
		
		if(Double.isNaN(x)) {return null;}
		if(Double.isNaN(y)) {return null;}
		if(Double.isInfinite(x)) {return null;}
		if(Double.isInfinite(y)) {return null;}

		return new Point2D.Double(x,y);
	}
	
	/**
	 * Get the point on the line between p1 and p2 formed by the
	 * perpendicular from p0.
	 */
	public static Point perpendicularPoint(Point p0, Point p1, Point p2) {
		Point2D.Double pd0 = new Point2D.Double(p0.x,p0.y);
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		Point2D.Double retd = perpendicularPoint(pd0,pd1,pd2);
		Point ret = new Point((int)retd.x,(int)retd.y);
		return ret;
	}
	
	/**
	 * Get the point on the line between p1 and p2 formed by the
	 * perpendicular from p0.
	 */
	public static Point2D.Double perpendicularPoint(Point2D.Double p0, Point2D.Double p1, Point2D.Double p2) {
		
		// vector from p1 to p2
		double base_vec_x = p2.x - p1.x; 
		double base_vec_y = p2.y - p1.y; 
		double base_len = Math.sqrt(base_vec_x * base_vec_x + base_vec_y*base_vec_y);
		double base_uvec_x = base_vec_x / base_len; 
		double base_uvec_y = base_vec_y / base_len; 
		
		// vector from p1 to p0
		double step_x = (p0.x-p1.x);
		double step_y = (p0.y-p1.y);
		
		double dot_prod = step_x*base_uvec_x + step_y*base_uvec_y;
		
		double result_x = p1.x + dot_prod*base_uvec_x; 
		double result_y = p1.y + dot_prod*base_uvec_y; 
		Point2D.Double result = new Point2D.Double(result_x, result_y);
		return result;
	}

	/**
	 * given a current value, a centre to scale from and a scale multiplier
	 * this method calculates the new value.
	 */
	public static double scaleCoordinate(double value, double centre, double multiplier) {

		double ret = value;
		ret = ret - centre;
		ret = ret * multiplier;
		ret = ret + centre;
		return ret;

	}
	
	/** takes the point and returns a new one moved by the distance and angle. */
	public static Point2D.Double movePointOnScreen(Point2D.Double startPoint, double distance, double degrees) {
		double newX = startPoint.getX() + distance * Math.cos(Math.toRadians(degrees));
		double newY = startPoint.getY() - distance * Math.sin(Math.toRadians(degrees));
		return new Point2D.Double(newX, newY);
	}



	/**
	 * Checks to see if the p is within the rectangle given
	 * by p1 and p2. Can be used to see if a point is on a line
	 */
	public static boolean pointIsWithinBounds(Point p, Point p1, Point p2) {
		int left = p1.x;
		int right = p2.x;
		if(p1.x > p2.x) {
			left = p2.x;
			right = p1.x;
		}
		int top = p1.y;
		int bottom = p2.y;
		if(p1.y > p2.y) {
			top = p2.y;
			bottom = p1.y;
		}
		
		if(p.x < left) {return false;}
		if(p.x > right) {return false;}
		if(p.y < top) {return false;}
		if(p.y > bottom) {return false;}
		
		return true;

	}
	
	public static boolean pointIsWithinBounds(Point2D.Double p, Point2D.Double p1, Point2D.Double p2) {
		double left = p1.x;
		double right = p2.x;
		if(p1.x > p2.x) {
			left = p2.x;
			right = p1.x;
		}
		double top = p1.y;
		double bottom = p2.y;
		if(p1.y > p2.y) {
			top = p2.y;
			bottom = p1.y;
		}
		
		if(p.x < left) {return false;}
		if(p.x > right) {return false;}
		if(p.y < top) {return false;}
		if(p.y > bottom) {return false;}
		
		return true;

	}


	
	public static boolean linesParallel(Point2D.Double p1, Point2D.Double p2, Point2D.Double q1, Point2D.Double q2) {
		
		final int DECIMAL_PLACES = 6;
		
		double angleP = round(lineAngle(p1, p2),DECIMAL_PLACES);
		double angleQ = round(lineAngle(q1, q2),DECIMAL_PLACES);
		if(angleP == angleQ) {
			return true;
		}
		if(angleP == round(lineAngle(q1, q2)-Math.PI,DECIMAL_PLACES)) {
			return true;
		}
		if(round(lineAngle(p1, p2)-Math.PI,DECIMAL_PLACES) == angleQ) {
			return true;
		}
		return false;
	}

	
	/**
	 * Finds out if the lines p1 to p2 and q1 to q2 are close to parallel.
	 */
	public static boolean linesNearlyParallel(Point2D.Double p1, Point.Double p2, Point.Double q1, Point.Double q2, double fudgeDegrees) {
		
		final double fudgeAngle = Math.toRadians(fudgeDegrees);
		
		final int DECIMAL_PLACES = 6;
		
		double angleP = round(lineAngle(p1, p2),DECIMAL_PLACES);
		double angleQ = round(lineAngle(q1, q2),DECIMAL_PLACES);
		if(Math.abs(angleP-angleQ) < fudgeAngle) {
			return true;
		}
		
		if(Math.abs(angleP-round(lineAngle(q1, q2)-Math.PI,DECIMAL_PLACES)) < fudgeAngle) {
			return true;
		}
		
		if(Math.abs(angleQ-round(lineAngle(p1, p2)-Math.PI,DECIMAL_PLACES)) < fudgeAngle) {
			return true;
		}

		
		return false;
	}



	public static double det(double a, double b, double c, double d)
	{
		return a * d - b * c;
	}

	
	
	public static double computePolygonArea (Polygon p) {

		double area = 0.0;
		for (int i = 0; i < p.npoints - 1; i++) {
			area += (p.xpoints[i] * p.ypoints[i+1]) - (p.xpoints[i+1] * p.ypoints[i]);
		}
		area += (p.xpoints[p.npoints-1] * p.ypoints[0]) - (p.xpoints[0] * p.ypoints[p.npoints-1]);  

		area *= 0.5;
		
		if(area<0) {
			area =-area;
		}

		return area;
	}

	
	
	public static float computePolygonAreaFloat (Polygon p) {

		float area = 0.0f;
		for (int i = 0; i < p.npoints - 1; i++) {
			area += (p.xpoints[i] * p.ypoints[i+1]) - (p.xpoints[i+1] * p.ypoints[i]);
		}
		area += (p.xpoints[p.npoints-1] * p.ypoints[0]) - (p.xpoints[0] * p.ypoints[p.npoints-1]);  

		area *= 0.5f;
		
		if(area<0) {
			area =-area;
		}

		return area;
	}

	public static double computePolygonArea(Point2D.Double[] ps) {
		double area = 0.0;
		for (int i = 0; i < ps.length - 1; i++) {
			area += (ps[i].x * ps[i+1].y) - (ps[i+1].x * ps[i].y);
		}
		area += (ps[ps.length-1].x * ps[0].y) - (ps[0].x * ps[ps.length-1].y);  

		area *= 0.5;
		
		if(area<0) {
			area =-area;
		}

		return area;
	}


	

	
	
	public static Polygon squarePolygonAroundPoint(Point p, int width) {
		Polygon ret = new Polygon();
		int d = width/2;
		ret.addPoint(p.x-d, p.y-d);
		ret.addPoint(p.x-d, p.y+d);
		ret.addPoint(p.x+d, p.y+d);
		ret.addPoint(p.x+d, p.y-d);
		return ret;
	}

	
	public static double equilateralSideFromArea(double area) {
		return Math.sqrt(area*4/Math.sqrt(3));
	}
	
	public static double equilateralAreaFromSide(double side) {
		return Math.sqrt(side*side*Math.sqrt(3)/4);
	}
	
	public static double equilateralHeightFromSide(double side) {
		return side*Math.sqrt(3)/2;
	}
	
	public static double equilateralSideFromHeight(double height) {
		return height*2/Math.sqrt(3);
	}

	
	public static Polygon diamondPolygonAroundPoint(Point p, int width) {
		Polygon ret = new Polygon();
		int d = width/2;
		ret.addPoint(p.x-d, p.y);
		ret.addPoint(p.x, p.y+d);
		ret.addPoint(p.x+d, p.y);
		ret.addPoint(p.x, p.y-d);
		return ret;
	}





		
	public static void findErrors(AreaSpecification as, 
			                      Graph g,
			                      HashMap<String,Double> errorMap,
			                      ArrayList<String> missingZones,
			                      ArrayList<String> additionalZones) {
		int polygonResolution = 10000;

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
				double polygonArea= scaledPolygonArea/(AreaSpecification.scalingFactor*AreaSpecification.scalingFactor);
				totalPolygonArea += polygonArea;
			}

			currentValuesMap.put(zone,totalPolygonArea);
		}

		
		double currentTotal = 0.0;
		for(String zone : currentValuesMap.keySet()) {
			double value = currentValuesMap.get(zone);
			currentTotal += value;
		}

		HashSet<String> allZones = new HashSet<String>(currentValuesMap.keySet());
		
		allZones.addAll(as.getSpecification().keySet());

		double requiredTotal = 0.0;
		for(String zone : as.getSpecification().keySet()) {
			double value = as.getSpecification().get(zone);
			requiredTotal += value;
		}

		
		ArrayList<String> allZonesList = new ArrayList<String>(allZones);
		boolean unique = AbstractDiagram.sortZoneList(allZonesList);
		if(!unique) {
			System.out.println("DUPLICATE ZONES");
		}
		
//System.out.println("all"+ allZonesList);
//System.out.println("current "+currentValuesMap.keySet());
//System.out.println("required "+as.getAbstractDiagram().getZoneList());

		for(String zone : allZonesList) {
			if(zone.equals("")) {
				continue;
			}
			Double actual = currentValuesMap.get(zone);
			if(actual == null) {
				actual = 0.0;
			}
			actual = actual/currentTotal;

			Double required = as.getSpecification().get(zone);
			if(required == null) {
				required = 0.0;
			}
			required = required/requiredTotal;
//System.out.print(zone+":"+required+":"+actual+" ");

			double errorFraction = 1.0;
			if(required != 0.0) {
				errorFraction = Math.abs((required-actual)/required);
			}
			
//System.out.println("|"+zone+"| actual "+actual+" required "+required+" error "+errorFraction);
			errorMap.put(zone,errorFraction);

		}
//System.out.println();
//System.out.println(actualCheck +" "+requiredCheck+" both should be 1");		

		for(String zone : as.getAbstractDiagram().getZoneList()) {
			if(zone == "") {
				continue;
			}
			if(currentValuesMap.get(zone) == null) {
				missingZones.add(zone);
			}
		}
		
		for(String zone : currentValuesMap.keySet()) {
			if(zone == "") {
				continue;
			}
			if(!as.getAbstractDiagram().getZoneList().contains(zone)) {
				additionalZones.add(zone);
			}
		}
		
		
	}
	/*
	public static String report(AreaSpecification ad, Graph g) {
		findErrors(ad,g);
		String ret = "";
		ret += "\t"+(missingZones.size()+additionalZones.size());
//System.out.println("missing "+missingZones);
//System.out.println("additional "+additionalZones);

//System.out.println("errorTotal "+errorTotal);
		double errorTotal = 0.0;
		for(String zone : errorMap.keySet()) {
			errorTotal += errorMap.get(zone);
		}
		
		ret += "\t"+errorTotal;
		
		return ret;
	}
	*/

}
