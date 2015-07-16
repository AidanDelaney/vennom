package org.eulerdiagrams.vennom.apCircles;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * 
 * @author Leishi Zhang
 *
 */

public class RegularPolygon {
	/**
	 * Creates a regular polygon with the required number of corners
	 * and outer radius. The polygon is centred
	 * on the origin.
	 */
 	public static Polygon generateRegularPolygon(int centre_x, int centre_y,
 			double outerRadius, int numOfCorners) {
 		double[] x = new double[numOfCorners];
 		double[] y = new double[numOfCorners];
	
	    // centre is starting point of drawing, translation to the correct
 		// position will happen in the end
 		x[0] = centre_x;
 		y[0] = centre_y;
 		
 		// length of one side
 		double sideLength = 2 * outerRadius * Math.sin(Math.PI / numOfCorners);
	
	    // outer angle
	    double angle = (2 * Math.PI) / numOfCorners;
	
	    // second corner
	    x[1] = x[0] + (Math.cos((Math.PI - angle) / 2) * sideLength);
	    y[1] = y[0] + (Math.sin((Math.PI - angle) / 2) * sideLength);
	
	     // Direction vector
	     double[] vec = new double[2];
	     vec[0] = x[1] - x[0];
	     vec[1] = y[1] - y[0];
	
	     // helper variables
	     double x_afterRot, y_afterRot;
	
	     for (int i = 2; i < numOfCorners; i++) { 
	    	 // translation vector calculation
	         x_afterRot = vec[0] * Math.cos(-angle) - vec[1] * Math.sin(-angle);
	         y_afterRot = vec[1] * Math.cos(-angle) + vec[0] * Math.sin(-angle);
	         vec[0] = x_afterRot;
	         vec[1] = y_afterRot;
	
	         // new corner = old corner + translation vector after rotation
	         x[i] = x[i - 1] + vec[0];
	         y[i] = y[i - 1] + vec[1];
	
	         // new direction vector
	         vec[0] = x[i] - x[i - 1];
	         vec[1] = y[i] - y[i - 1];
	    }
	
	   // Double to integer to match Polygon constructor
	   int[] final_x = new int[numOfCorners];
	   int[] final_y = new int[numOfCorners];
	   
	   for (int i = 0; i < numOfCorners; i++) {
		   // translation to centre of circle
	       final_x[i] = (int) (Math.round(x[i]) - outerRadius) ;
	       final_y[i] = (int) Math.round(y[i]);
	   }
	
	   Polygon pol = new Polygon(final_x, final_y, numOfCorners);
	   return pol;
	 }
	
	/**
	 * draw a maximised polygon in a given rectangle. 
	 * This does not change the aspect ratio of the graph, 
	 * it is either maximised in x or y directions.
	 */
	public static Polygon generateRegularPolygonInsideRectangle(
	       int numOfCorners, Rectangle rectangle){
			
		int outerRadius = 0;
		int width = (int)rectangle.getWidth();
		int height = (int)rectangle.getHeight();
		int centre_x = (int)rectangle.getCenterX();
		int centre_y = (int)rectangle.getCenterY();
		
		//outerRadius equals the smaller one between width and height
		if(width > height)
			outerRadius = (int)(height/2);
		else
			outerRadius = (int)(width/2);	
			Polygon pol = generateRegularPolygon(centre_x, centre_y, outerRadius, numOfCorners);
			return pol;
	}	
	/**
	 * draw a maximised polygon outside a given rectangle. 
	 * This does not change the aspect ratio of the graph, 
	 * it is either maximised in x or y directions.
	 */
	public static Polygon generateRegularPolygonOutsideRectangle(
	             int numOfCorners, Rectangle rectangle){
		int centre_x = (int)rectangle.getCenterX();
		int centre_y = (int)rectangle.getCenterY();
		int radius = (int)(Math.sqrt(rectangle.getWidth()*rectangle.getWidth() + rectangle.getHeight()*rectangle.getHeight())/2+1);
		Polygon pol = generateRegularPolygon(centre_x, centre_y, radius, numOfCorners);
		return pol;
	}	
		
	public static Polygon insideCircle(Polygon big, Polygon small){
		
		Rectangle rect1 = small.getBounds();
		Rectangle rect2 = big.getBounds();
		Polygon circle = generateRegularPolygonOutsideRectangle(20, rect1);
		
		double radius = circle.getBounds().getHeight()/2;		
		double minRadius = 0;			
		double diffSize = 0;
		double diffRadius = 0;
			
		if(rect1.getWidth()<rect1.getHeight()){
			minRadius = rect1.getHeight()/2;
			diffRadius = radius - minRadius;
			if(rect2.getWidth()<rect2.getHeight())
				diffSize = rect2.getHeight()/2 - rect1.getHeight()/2 - diffRadius;		
			else
				diffSize = rect2.getWidth()/2 - rect1.getHeight()/2 - diffRadius;
		}
		else{
			minRadius = small.getBounds().getWidth()/2;
			diffRadius = radius - minRadius;
			if(rect2.getWidth()<rect2.getHeight())
				diffSize = rect2.getHeight()/2 - rect1.getWidth()/2 - diffRadius;
			else
				diffSize = rect2.getWidth()/2 - rect1.getWidth()/2 - diffRadius;
		}
		
		double radiusRange = radius - minRadius;
		int iteration = 1;
		Polygon currentCircle = circle;
		double currentFitness =  getFintness(circle, small, big);
		
		if(currentFitness == 0){		
			return circle;
		}
		else{
			int deltaX =  1;
			int deltaY =  1;
			
			while(iteration < 20 && deltaX <= diffSize){
				
				ArrayList<Polygon> neighbours = getNeighbours(currentCircle,deltaX,deltaY);
				boolean changed = false;
				for(int i = 0; i < neighbours.size(); i++){
					Polygon neighbour = neighbours.get(i);			
					if(radiusRange > 0) {
						Polygon newCircle = neighbour;
						for(int k = 0 ; k < radiusRange +1; k++){
							if(contains(newCircle,small))
							newCircle = shrink(k, neighbour);
						
						for(int j=0; j<(int)radiusRange+1; j++){
							double tempFitness = getFintness(newCircle, small, big); 
							if(tempFitness == 0){
								return newCircle;
							}
							else if(tempFitness > currentFitness){
								changed  = true;
								currentCircle = neighbours.get(i);
								currentFitness = tempFitness;								
							}
						}								
					}
				}			
			}	
			
			if(!changed){
				return null;
			}	
			iteration++;
			deltaX++;
			deltaY++;
			}		
		}
		System.out.println("no circle can be fitted between two polygons");
		return null;
	}
	public static Polygon shrink(int shrinkSize, Polygon pol){
		Rectangle rect = pol.getBounds();
		int startX = (int)rect.getX() + shrinkSize;
		int startY = (int)rect.getY() + shrinkSize;
		Rectangle rect1 = new Rectangle(startX,startY,rect.width-shrinkSize,rect.height-shrinkSize);
	
		Polygon ret = generateRegularPolygonInsideRectangle(20,rect1);
		return ret;
	}
		
	public static ArrayList<Polygon> getNeighbours(Polygon pol, int deltaX, int deltaY){
		ArrayList<Polygon> neighbours = new ArrayList<Polygon>();
		Polygon pol1 = getCopy(pol);
		pol1.translate(deltaX, 0);
		Polygon pol2 = getCopy(pol);
		pol2.translate(-deltaX, 0);		
		Polygon pol3 = getCopy(pol);
		pol3.translate(0, deltaY);
		Polygon pol4 = getCopy(pol);
		pol4.translate(0, -deltaY);	
		Polygon pol5 = getCopy(pol);
		pol5.translate(deltaX, deltaY);
		Polygon pol6 = getCopy(pol);
		pol6.translate(-deltaX, deltaY);		
		Polygon pol7 = getCopy(pol);
		pol7.translate(deltaX, -deltaY);
		Polygon pol8 = getCopy(pol);
		pol8.translate(-deltaX, -deltaY);	
		neighbours.add(pol1);
		neighbours.add(pol2);
		neighbours.add(pol3);
		neighbours.add(pol4);	
		neighbours.add(pol5);
		neighbours.add(pol6);	
		neighbours.add(pol7);
		neighbours.add(pol8);			
		return neighbours;
	}
	public static double getFintness(Polygon circle, Polygon small, Polygon big){
		double fitness = 0;
		double smallArea = PolygonArea(small);		
		double circleArea = PolygonArea(circle);
		double smallIntersectArea = PolygonIntersect.intersectionArea(circle, small);
		double bigIntersectArea = PolygonIntersect.intersectionArea(big, circle);
		double diffSmall = Math.abs(smallIntersectArea - smallArea);
		double diffBig = Math.abs(bigIntersectArea - circleArea);
		fitness = - (diffSmall + diffBig);			
		return fitness;
	}
		
	public static Polygon getCopy(Polygon pol){
		Polygon copy = new Polygon();
		for(int i = 0 ; i < pol.npoints; i++){
			int x = pol.xpoints[i];
			int y = pol.ypoints[i];
			copy.addPoint(x, y);	
		}		
		return copy;
	}
	public static int PolygonArea(Polygon pol){
		Point2D[] polyPoints = new Point2D[pol.npoints];
		for(int i = 0 ; i < pol.npoints; i++){
			polyPoints[i] = new Point2D.Double(pol.xpoints[i], pol.ypoints[i]);
		}
		float area = (float)PolygonArea(polyPoints);
		return Math.round(area);
	}	
	public static double PolygonArea(Point2D[] polyPoints) {
		int i, j, n = polyPoints.length;
		double area = 0;
		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			area += polyPoints[i].getX() * polyPoints[j].getY();
			area -= polyPoints[j].getX() * polyPoints[i].getY();
		}
		area /= 2.0;
		return Math.abs(area);
	}
	/**
	 * generate two half polygons 
	 * @param centre_x
	 * @param centre_y
	 * @param outerRadius
	 * @param numOfCorners
	 * @return two half "circles" 
	 */
	public static Polygon[] halfPolygons(int centre_x, int centre_y,
			int outerRadius, int numOfCorners){
		
		Polygon pol = generateRegularPolygon(centre_x, centre_y, outerRadius, numOfCorners);
		Polygon[] ret = new Polygon[2];
		int halfPoint =	pol.npoints/2;
		for(int i = 0 ; i <= halfPoint; i++){
			ret[0].addPoint(pol.xpoints[i], pol.ypoints[i]);			
		}
		for(int j = halfPoint; j< pol.npoints;j++){
			ret[1].addPoint(pol.xpoints[j],pol.ypoints[j]);
		}
		ret[1].addPoint(pol.xpoints[0], pol.ypoints[0]);
		 return ret;
	}
	/**
	 * split a polygon into two half polygons
	 * @param pol
	 * @return
	 */
	public static Polygon[] halfPolygons(Polygon pol){
		Polygon[] ret = new Polygon[2];
		ret[0] = new Polygon();
		ret[1] = new Polygon();
		int halfPoint =	pol.npoints/2;
		for(int i = 0 ; i <= halfPoint; i++){
			ret[0].addPoint(pol.xpoints[i], pol.ypoints[i]);			
		}
		for(int j = halfPoint; j< pol.npoints;j++){
			ret[1].addPoint(pol.xpoints[j],pol.ypoints[j]);
		}
		ret[1].addPoint(pol.xpoints[0], pol.ypoints[0]);
		 return ret;
	}	
	public RegularPolygon(int cX, int cY, int radius, int numOfCorners) {
		this.cX = cX;
		this.cY = cY;
		this.radius = radius;
		this.numOfCorners = numOfCorners;
		polygon = RegularPolygon.generateRegularPolygon(cX, cY, radius, numOfCorners);
	}
	public void print(){
		System.out.println("regular polygon:" + this.cX +" " + this.cY + " " + this.radius);
	}
	public static boolean contains(Polygon big, Polygon small){
		for(int i = 0 ; i < small.npoints; i++){
			double x = small.xpoints[i];
			double y = small.ypoints[i];
			if(!big.contains(new Point2D.Double(x,y))){
				return false;
			}
		}		
		return true;
	}
	public int getCentreX(){return cX;}
	public int getCentreY(){return cY;}
	public int getRadius(){return radius;}
	public int getNumCorners(){return numOfCorners;}
	public Polygon getPolygon(){return polygon;}
	protected int cX;
	protected int cY;
	protected int radius;
	protected int numOfCorners;
	protected Polygon polygon;
		
	 
}


	
	
	
	
	
	
	
	
