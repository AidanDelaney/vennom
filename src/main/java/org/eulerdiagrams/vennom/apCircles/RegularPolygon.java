package org.eulerdiagrams.vennom.apCircles;

import java.awt.Polygon;

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
	 
}


	
	
	
	
	
	
	
	
