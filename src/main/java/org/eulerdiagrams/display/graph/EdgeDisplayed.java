package org.eulerdiagrams.display.graph;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.eulerdiagrams.display.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.Graph;

public class EdgeDisplayed {
	

	/** The last generated shape object for this edge */
	private static Shape shape = null;

	/**
	 * Gives a new shape object representing the edge. At the moment
	 * only undirected straight lines are supported.
	 */
	public static Shape generateShape(Point offset, Edge e) {

		Point fromPoint = new Point(e.getFrom().getCentre());
		Point toPoint = new Point(e.getTo().getCentre());

		fromPoint.x += offset.x;
		fromPoint.y += offset.y;
		toPoint.x += offset.x;
		toPoint.y += offset.y;

		GeneralPath path = new GeneralPath();
		path.moveTo(fromPoint.x+offset.x, fromPoint.y+offset.y);

		for(Point p : e.getBends()) {
			path.lineTo(p.x+offset.x, p.y+offset.y);
		}

		path.lineTo(toPoint.x+offset.x, toPoint.y+offset.y);

		shape = path;

		return(shape);
	}


	/**
	 * Returns the shape object representing the edge, or if
	 * there is none, generate a new shape. The generation is
	 * performed using a zero offset, which may cause difficulties
	 * if parallel edges are being separated, however generation
	 * due to the call of this method will be extremely rare, and
	 * no known situation where generation is performed is known.
	 */
	public static Shape shape( Edge e ) {
		if(shape == null) {
			generateShape(APCirclePanel.ZEROOFFSET, e);
		}
		return(shape);
	}

/**
 * Finds an edge close to the passed point, or returns null
 * if the argument point is not over an edge. The padding
 * refers to the distance the point can be from the
 * edge, and must be greater than 0. If there is more
 * than one edge, it finds the
 * last one in the collection, which hopefully should
 * be the one on top of the display.
 * TBD DEAL WITH EDGE BENDS.
 */
	public static Edge getEdgeNearPoint(Graph g, Point p, int padding) {

		Edge returnEdge = null;

		for(Edge e : g.getEdges()) {
			Shape edgeShape = shape(e);
			Rectangle r = new Rectangle(p.x-padding,p.y-padding,padding*2,padding*2);
			if(edgeShape.intersects(r)) {
				returnEdge = e;
			}
		}
		return(returnEdge);
	}

}
