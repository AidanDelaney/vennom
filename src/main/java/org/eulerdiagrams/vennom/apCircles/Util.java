package org.eulerdiagrams.vennom.apCircles;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;


/**
 * Utility Methods for the Convex package.
 *@author Peter Rodgers
 */
public class Util {
	
	public static final double NEARLY_ZERO = 0.00001;
	public static final long MAX_DRAWING_TIME = 1000; // milliseconds, for stopping searches within reasonable time

	public static boolean ChrismasHackOn = false;
	public static boolean TEST_FIX = true;
    public static int debug_level = 1; 
	// 1 = test framework, 2 = method in/out, 3 = detail, 4 = desperation
	
	public static String[] zoneIndex = {"Not Used","A","B","AB","C","AC","BC","ABC"};
	public static String[] contourIndex = {"A","B","C"};

	
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
	public static double lineAngle(Point p1, Point p2) {
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		double ret = lineAngle(pd1,pd2);
		return ret;
	}
	
	/** Gets the angle of the line between two points */
	public static double lineAbsoluteAngle(Point p1, Point p2) {
		double rise = p2.y - p1.y;
    	double run = p2.x - p1.x;
       	return -Math.atan2(rise, run);
       	
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
	
	
	/**
	 * Returns the angle formed by p2 between p1 and p3 with respect to the reference point.
	 * If the points are on a polygon and the reference point is inside the polygon, this
	 * will indicate if the point is a convex one on the polygon if less than PI radians,
	 * otherwise it is non-convex.
	 * @return a value between 0 and 2*PI radians
	 */
	public static double angleWithReference(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double reference) {
		double angle = Util.angle(p1,p2,p3);
		double p2Distance = Util.distance(p2,reference);
		Point2D.Double intersection = intersectionPointOfTwoLines(p1, p3, reference, p2);
		double intersectionDistance = distance(intersection,reference);
		if(p2Distance < intersectionDistance) {
			angle = 2*Math.PI-angle;
		}
		return angle;
	}

	

  	/** Get the distance between two points. */
	public static double distance(int x1, int y1, int x2, int y2){
		Point2D.Double pd1 = new Point2D.Double(x1,y1);
		Point2D.Double pd2 = new Point2D.Double(x2,y2);
		double ret = distance(pd1,pd2);
		return ret;
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
	
	
	/** Get a point half way between the given points */
	public static Point midPoint(Point p1, Point p2) {
		return new Point(p1.x+(p2.x-p1.x)/2,p1.y+(p2.y-p1.y)/2);
	}

	/** Get a point half way between the given points */
	public static Point2D.Double midPoint(Point2D p1, Point2D p2) {
		return new Point2D.Double(p1.getX()+(p2.getX()-p1.getX())/2,p1.getY()+(p2.getY()-p1.getY())/2);
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
	 * Get the angle p2 forms between p1 and p3 relative to relativePoint.
	 * The angle is greater than PI radians if the angle looks convex when
	 * viewed from relativePoint.
	 * @return a value between 0 and PI*2 radians
	 */
	public static double getRelativeAngle(Point p1, Point p2, Point p3, Point relativePoint) {
		
		double angle = angle(p1,p2,p3);
		
		double relativeP1Angle = angle(p1,p2,relativePoint);
		double relativeP3Angle = angle(p3,p2,relativePoint);
		
		double totalRelativeAngle = relativeP1Angle +relativeP3Angle;

		if(totalRelativeAngle > angle) {
			angle = Math.PI*2 - angle;
		}

		return angle;
	}
	
	

	

	/**
	 * Intersection point of two lines, first line given by p1 and
	 * p2, second line given by p3 and p4.
	 * */
	public static Point intersectionPointOfTwoLines(Point p1, Point p2, Point p3, Point p4) {
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		Point2D.Double pd3 = new Point2D.Double(p3.x,p3.y);
		Point2D.Double pd4 = new Point2D.Double(p4.x,p4.y);
		Point2D.Double retd = intersectionPointOfTwoLines(pd1,pd2,pd3,pd4);
		if(retd == null) {
			return null;
		}
		Point ret = new Point((int)retd.x,(int)retd.y);
		return ret;
	}

	public static Point2D.Double intersectionPointOfPolygonAndLine(Polygon polygon, Line2D.Double line){
	 
		if(polygon!=null){
			int nPoints = polygon.npoints;
			Point2D.Double p3 = new Point2D.Double(line.getX1(),line.getY1());
			Point2D.Double p4 = new Point2D.Double(line.getX2(),line.getY2());				
				if(nPoints>2){
					for(int i = 0 ; i < nPoints -1; i++){					
			
						double x1 = polygon.xpoints[i];
						double y1 = polygon.ypoints[i];
						double x2 = polygon.xpoints[i+1];
						double y2 = polygon.ypoints[i+1];
						Point2D.Double p1 = new Point2D.Double(x1,y1);
						Point2D.Double p2 = new Point2D.Double(x2,y2);
						Line2D.Double l = new Line2D.Double(x1,y1,x2,y2);
						
						if (l.intersectsLine(line)){
							return  intersectionPointOfTwoLines(p1,p2,p3,p4);
						}
								
					}		
					double xs = polygon.xpoints[0];
					double ys = polygon.ypoints[0];
					double xe = polygon.xpoints[nPoints -1];
					double ye = polygon.ypoints[nPoints -1];
					Line2D.Double l0 = new Line2D.Double(xe,ye,xs,ys);
					Point2D.Double p1 = new Point2D.Double(xs,ys);
					Point2D.Double p2 = new Point2D.Double(xe,ye);				
					if (l0.intersectsLine(line)){
						return  intersectionPointOfTwoLines(p1,p2,p3,p4);
					}
				}
			}
		return null;
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

	
	public static Line2D.Double parallelLine(Line2D.Double l, double perpendicularDistance){
	
		Point2D.Double p1 = new Point2D.Double(l.getX1(),l.getY1());
		Point2D.Double p2 = new Point2D.Double(l.getX2(),l.getY2());

		double lineAngle = lineAngle(p1,p2);
		double lineAngleDegrees = Math.toDegrees(lineAngle);
		double perpendicularAngleDegrees = lineAngleDegrees+90;
		if(perpendicularDistance > 0) {
			perpendicularAngleDegrees = lineAngleDegrees-90;
		}
		while(perpendicularAngleDegrees > 360) {
			perpendicularAngleDegrees -= 360;
		}
		while(perpendicularAngleDegrees < 0) {
			perpendicularAngleDegrees += 360;
		}

		Point2D.Double moveP1 = movePointOnScreen(p1,perpendicularDistance,perpendicularAngleDegrees);
		Point2D.Double moveP2 = movePointOnScreen(p2,perpendicularDistance,perpendicularAngleDegrees);

		Line2D.Double line = new Line2D.Double(moveP1,moveP2);	
		return line;
	}
	
	
	/**
	 * given a current value, a centre to scale from and a scale multiplier
	 * this method calculates the new value.
	 */
	public static int scaleCoordinate(int value, int centre, double multiplier) {
		return convertToInteger(scaleCoordinate((double)value,(double)centre,multiplier));
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
	
	
	/**
	 * Given a polygon, this method finds the point on the polygon closest
	 * to the origin.
	 */
	public static Point2D.Double getTopLeftMostPolygonPoint(Polygon p) {
		if(p == null) {
			return null;
		}
		
		Point2D.Double origin = new Point2D.Double(0,0);
		double points[] = new double[2];
		
		Point2D.Double ret = null;
		double distance = Double.MAX_VALUE;
		
		PathIterator pit = p.getPathIterator(null);
		while(!pit.isDone()) {
			pit.currentSegment(points);
			Point2D.Double nextPoint = new Point2D.Double(points[0],points[1]);
			double nextDistance = distance(origin,nextPoint);

			if(nextDistance < distance) {
				ret = nextPoint;
				distance = nextDistance;
			}
			
			pit.next();
		}
		return ret;
	}
	
	
	public static Point getExtendedPoint(Point2D p1, Point2D p2, double dis){
		
		Point ret;

		double x, y, x1,x2,y1,y2;
		x1 = p2.getX()-dis;
		x2 = p2.getX()+dis;
		y1 = p2.getY()-dis;
		y2 = p2.getY()+dis;
		
		if(p1.getX()>=p2.getX())
			x = x1;
		else
			x = x2;
		
		if(p1.getY()>=p2.getY())
			y = y1;
		else
			y = y2;	
		
		ret = new Point((int)x,(int)y);
		return ret;
	}
	
	


	public static boolean sameSign(double a, double b)
	{
		if (a < 0 && b < 0)
			return true;
		else if (a >= 0 && b >= 0)
			return true;
		else
			return false;
	}

	

	
	/** takes the point and returns a new one moved by the distance and angle. */
	public static Point2D.Double movePointOnScreen(Point2D.Double startPoint, double distance, double degrees) {
		double newX = startPoint.getX() + distance * Math.cos(Math.toRadians(degrees));
		double newY = startPoint.getY() - distance * Math.sin(Math.toRadians(degrees));
		return new Point2D.Double(newX, newY);
	}


	
	/** Calculates the angle in degrees between the lines x1 to y1 and x2 to y2. */
	public static double calculateAngle(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		double angle=0.0;
 
		// Calculate angle
		if (dx == 0.0) {
			if (dy == 0.0) {
				angle = 0.0;
			} else if (dy > 0.0) {
				angle = Math.PI / 2.0;
			} else
				angle = Math.PI * 3.0 / 2.0;
		} else if (dy == 0.0) {
			if (dx > 0.0) {
				angle = 0.0;
			} else {
				angle = Math.PI;
			}
		} else {
			if  (dx < 0) {
				angle = Math.atan(dy/dx) + Math.PI;
			} else if (dy < 0.0) {
				angle = Math.atan(dy/dx) + (2*Math.PI);
			} else {
				angle = Math.atan(dy/dx);
			}
		}
 
		// Convert to degrees
		angle = angle * 180 / Math.PI;
				
		return angle;
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
	
	
	/**
	 * Finds out if the lines p1 to p2 and q1 to q2 cross. Touching lines are considered to cross.
	 */
	public static boolean linesCross(Point p1, Point p2, Point q1, Point q2) {
		return java.awt.geom.Line2D.linesIntersect(p1.x, p1.y, p2.x, p2.y, q1.x, q1.y, q2.x, q2.y);
	}

	/**
	 * Finds out if the lines p1 to p2 and q1 to q2 cross. Touching lines are considered to cross.
	 */
	public static boolean linesCross(Point2D.Double p1, Point2D.Double p2, Point2D.Double q1, Point2D.Double q2) {
		return java.awt.geom.Line2D.linesIntersect(p1.x, p1.y, p2.x, p2.y, q1.x, q1.y, q2.x, q2.y);
	}

	/**
	 * Finds out if the lines p1 to p2 and q1 to q2 are parallel.
	 */
	public static boolean linesParallel(Point p1, Point p2, Point q1, Point q2) {
		
		Point2D.Double pd1 = new Point2D.Double(p1.x,p1.y);
		Point2D.Double pd2 = new Point2D.Double(p2.x,p2.y);
		Point2D.Double qd1 = new Point2D.Double(q1.x,q1.y);
		Point2D.Double qd2 = new Point2D.Double(q2.x,q2.y);
		return linesParallel(pd1,pd2,qd1,qd2);
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
	public static boolean linesNearlyParallel(Point p1, Point p2, Point q1, Point q2, double fudgeDegrees) {
		
		Point2D.Double p1d = new Point2D.Double(p1.x,p1.y);
		Point2D.Double p2d = new Point2D.Double(p2.x,p2.y);
		Point2D.Double q1d = new Point2D.Double(q1.x,q1.y);
		Point2D.Double q2d = new Point2D.Double(q2.x,q2.y);
		
		return linesNearlyParallel(p1d,p2d,q1d,q2d,fudgeDegrees);
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
	

	public static String reverseString(String s) {
		String[] splitString = s.split("");
		StringBuffer reverse = new StringBuffer("");
		for(int i = splitString.length-1; i >= 0 ; i--) {
			reverse.append(splitString[i]);
		}
		return reverse.toString();
	}

	public static Point getLineLineIntersection(Point p1, Point p2, Point p3, Point p4){
		Point ret = new Point();
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double x3 = p3.getX();
		double y3 = p3.getY();
		double x4 = p4.getX();
		double y4 = p4.getY();
		
		double x = det(det(x1, y1, x2, y2), x1 - x2,det(x3, y3, x4, y4), x3 - x4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
		double y = det(det(x1, y1, x2, y2), y1 - y2,det(x3, y3, x4, y4), y3 - y4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
		ret.setLocation(x, y);
	
		return ret;
	}

	
	
	public static Point2D.Double getLineLineIntersection(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double p4){
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		double x3 = p3.getX();
		double y3 = p3.getY();
		double x4 = p4.getX();
		double y4 = p4.getY();
		
		double x = det(det(x1, y1, x2, y2), x1 - x2,det(x3, y3, x4, y4), x3 - x4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
		double y = det(det(x1, y1, x2, y2), y1 - y2,det(x3, y3, x4, y4), y3 - y4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);

		Point2D.Double ret = new Point2D.Double(x, y);
		return ret;
	}

	public static double det(double a, double b, double c, double d)
	{
		return a * d - b * c;
	}

	/** 
	 * Find the distance from p to the line created from p1 p2.
	 * If the perpendicular from po to the line is not on the line
	 * then the distance is to the end of the line, either p1 or p2
	 */
	public static double pointLineDistance(Point p, Point p1, Point p2) {
		
		Point perpPoint = perpendicularPoint(p, p1, p2);
		if(pointIsWithinBounds(perpPoint, p1, p2)) {
			double distance = distance(p,perpPoint);
			return distance;
		}
		
		double distance1 = distance(p,p1);
		double distance2 = distance(p,p2);
		
		if(distance1 < distance2) {
			return distance1;
		}
		

		return distance2;
	}

	public static double pointLineDistance(Point2D.Double p, Point2D.Double p1, Point2D.Double p2) {
		
		Point2D.Double perpPoint = perpendicularPoint(p, p1, p2);
		if(pointIsWithinBounds(perpPoint, p1, p2)) {
			double distance = distance(p,perpPoint);
			return distance;
		}
		
		double distance1 = distance(p,p1);
		double distance2 = distance(p,p2);
		
		if(distance1 < distance2) {
			return distance1;
		}
		

		return distance2;
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

	public static double computePolygonArea(ArrayList<Point2D.Double> ps) {
		double area = 0.0;
		for (int i = 0; i < ps.size() - 1; i++) {
			area += (ps.get(i).x * ps.get(i+1).y) - (ps.get(i+1).x * ps.get(i).y);
		}
		area += (ps.get(ps.size()-1).x * ps.get(0).y) - (ps.get(0).x * ps.get(ps.size()-1).y);  

		area *= 0.5;
		
		if(area<0) {
			area =-area;
		}

		return area;
	}


	/**
	 * Area of triangle from corner points.
	 */
	public static double computeTriangleArea(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		Point2D.Double[] ps = new Point2D.Double[3];
		ps[0] = p1;
		ps[1] = p2;
		ps[2] = p3;
		return computePolygonArea(ps);
	}

	
	
	public static Point2D.Double computePolygonCentroid(Polygon p) {
		double cx = 0.0;
		double cy = 0.0;

		for(int i = 0 ; i < p.npoints-1; i++) {
			double a = p.xpoints[i] * p.ypoints[i+1] - p.xpoints[i+1] * p.ypoints[i];
			cx += (p.xpoints[i] + p.xpoints[i+1]) * a;
			cy += (p.ypoints[i] + p.ypoints[i+1]) * a;
		}
		double a = p.xpoints[p.npoints-1] * p.ypoints[0] - p.xpoints[0] * p.ypoints[p.npoints-1];
		cx += (p.xpoints[p.npoints-1] + p.xpoints[0]) * a;
		cy += (p.ypoints[p.npoints-1] + p.ypoints[0]) * a;
		  
		double area = computePolygonArea(p);

		cx /= 6 * area;
		cy /= 6 * area;	
		
		return new Point2D.Double(cx, cy);
	}  
	
	
	public static Point2D.Double computePolygonAveragePoint(ArrayList<Point2D.Double> p) {
		double cx = 0.0;
		double cy = 0.0;

		for(Point2D.Double point : p) {
			cx += point.x;
			cy += point.y;
		}
		
		double retx = cx/p.size();
		double rety = cy/p.size();
		
		return new Point2D.Double(retx, rety);
	}  
	
	

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
					int x = convertToInteger(coords[0]);
					int y = convertToInteger(coords[1]);
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
					int x = convertToInteger(coords[0]);
					int y = convertToInteger(coords[1]);
					p.addPoint(x, y);
				}
			}
			if (coordType == PathIterator.SEG_LINETO) {
				int x = convertToInteger(coords[0]);
				int y = convertToInteger(coords[1]);
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



	public static Polygon squarePolygonAroundPoint(Point2D.Double p, int width) {
		Point pInt = new Point(convertToInteger(p.x),convertToInteger(p.y));
		return squarePolygonAroundPoint(pInt, width);
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

	/** Safe String to double conversion */
	public static double safeParseDouble(String doubleString) {
		double ret = 0.0;
		try {
			ret = Double.parseDouble(doubleString);
		} catch(Exception e) {
			System.out.println("Could not parse double from String: "+doubleString);
//			e.printStackTrace();
			return -1.0;
		}
		return ret;
	}

	public static void debug(int i, String string) {
		if(i <= debug_level){
			System.out.println(string);
		}
	}

	
	
	public static double computeMinX(ArrayList<Point2D.Double> polygon) {
		if(polygon.size() == 0) {
			return Double.NaN;
		}
		double ret = Double.MAX_VALUE;
		for(Point2D.Double p : polygon) {
			if(p.x < ret) {
				ret = p.x;
			}
		}
		return ret;
	}
	
	
	public static double computeMaxX(ArrayList<Point2D.Double> polygon) {
		if(polygon.size() == 0) {
			return Double.NaN;
		}
		Double ret = null;
		for(Point2D.Double p : polygon) {
			if(ret == null) {
				ret = p.x;
				continue;
			}
			if(p.x > ret) {
				ret = p.x;
			}
		}
		return ret;
	}
	
	
	public static double computeMinY(ArrayList<Point2D.Double> polygon) {
		if(polygon.size() == 0) {
			return Double.NaN;
		}
		Double ret = null;
		for(Point2D.Double p : polygon) {
			if(ret == null) {
				ret = p.y;
				continue;
			}
			if(p.y < ret) {
				ret = p.y;
			}
		}
		return ret;
	}
	
	
	public static double computeMaxY(ArrayList<Point2D.Double> polygon) {
		if(polygon.size() == 0) {
			return Double.NaN;
		}
		Double ret = null;
		for(Point2D.Double p : polygon) {
			if(ret == null) {
				ret = p.y;
				continue;
			}
			if(p.y > ret) {
				ret = p.y;
			}
		}
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
	
	public static double equilateralHeightFromArea(double area) {
		return equilateralHeightFromSide(equilateralSideFromArea(area));
	}
	
	public static double equilateralAreaFromHeight(double height) {
		return equilateralAreaFromSide(equilateralSideFromHeight(height));
	}

	
	public static boolean pointsNearlyEqual(Point2D.Double p1, Point2D.Double p2) {
		if(Math.abs(p1.x-p2.x) > NEARLY_ZERO) {return false;}
		if(Math.abs(p1.y-p2.y) > NEARLY_ZERO) {return false;}
		return true;
	}




	public static Polygon diamondPolygonAroundPoint(Point2D.Double p, int width) {
		Point pInt = new Point(convertToInteger(p.x),convertToInteger(p.y));
		return diamondPolygonAroundPoint(pInt, width);
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


	/**
	 * Square rotated 45 degrees. Order of points is left, bottom, right, top.
	 */
	public static ArrayList<Point2D.Double> diamondPolygonAroundPoint(Point2D.Double p, double width) {
		ArrayList<Point2D.Double> ret = new ArrayList<Point2D.Double>();
		double d = width/2;
		ret.add(new Point2D.Double(p.x-d, p.y));
		ret.add(new Point2D.Double(p.x, p.y+d));
		ret.add(new Point2D.Double(p.x+d, p.y));
		ret.add(new Point2D.Double(p.x, p.y-d));
		return ret;
	}

	public static double diamondPolygonWidthFromArea(double area) {

		double squareSide = Math.sqrt(area);
		double width = Math.sqrt(2*squareSide*squareSide);
		
		return width;
		
	}




}
