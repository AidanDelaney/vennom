package org.eulerdiagrams.vennom.apCircles.utilities;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.eulerdiagrams.vennom.apCircles.AreaSpecification;
import org.eulerdiagrams.vennom.apCircles.Util;
import org.eulerdiagrams.vennom.apCircles.display.APCircleDisplay;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.utilities.GraphUtility;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class CreateRandomPiercedSpecificationByGraph extends GraphUtility implements Serializable {


	private static final long serialVersionUID = 1L;

	public static final int CENTREX = 300;
	public static final int CENTREY = 300;

	JFrame frame;
	JPanel panel;
	JTextField nodeField;
	JTextField edgeField;
	JLabel nodeLabel;
	JLabel edgeLabel;
	JButton okButton;

	public static Random r = new Random(System.currentTimeMillis());

	public int numberOfNodes = 5;
	public int radiusMinSize = 20;
	public int radiusMaxSize = 180;
	public double insideChance = 0.2; // chances of a movable edge being an attractor
	
	
//public double insideChance = 0.1; // chances of a movable edge being an attractor

/** Trivial constructor. */
	public CreateRandomPiercedSpecificationByGraph() {
		super(KeyEvent.VK_G,"Create Random Graph");
	}

/** Trivial constructor. */
	public CreateRandomPiercedSpecificationByGraph(int key, String s) {
		super(key,s);
	}
	
	
	public void apply() {
		
		createRandomTree(getGraph(),numberOfNodes,radiusMinSize,radiusMaxSize);
		createMovableEdges(getGraph(),insideChance);

		getGraph().centreOnPoint(CENTREX, CENTREY);

		
	}


	public static void createRandomTree(Graph g, int nodes,int minVal,int maxVal) {
		
		g.clear();
		
		for(int i = 0;i < nodes; i++) {
			int x = r.nextInt(300)+100;
			int y = r.nextInt(300)+100;

			int labelVal = r.nextInt(1+maxVal-minVal);
			labelVal += minVal;
			String label = Integer.toString(labelVal);
			
			Node n = new Node(label,new Point(x,y));
			n.setContour(Character.toString((char)('a'+i)));
			
			g.addNode(n);
			
		}

		
		// form the fixed tree
		while(!g.connected()) {
			ArrayList<Node> connectedSet = g.connectedSet(g.getNodes().get(0));
			
			int randomIndex = r.nextInt(connectedSet.size());
			Node n1 = connectedSet.get(randomIndex);

			for(Node n2 : g.getNodes()) {
				if(!connectedSet.contains(n2)) {
					double radius1 = -1;
					double radius2 = -1;
					try {
						radius1 = Double.parseDouble(n1.getLabel());
						radius2 = Double.parseDouble(n2.getLabel());
					} catch (Exception exception) {
						radius1 = -1;
						radius2 = -1;
					}
					int maxDistance = Util.convertToInteger(radius1+radius2);
					int minDistance = Util.convertToInteger(radius1-radius2);
					if(minDistance < 0) {
						minDistance = Util.convertToInteger(radius2-radius1);
					}
					double labelDistance = r.nextInt(1+maxDistance-minDistance);
					labelDistance += minDistance;
					String edgeLabel = Double.toString(labelDistance);
					Edge e = new Edge(n1,n2,edgeLabel);
					g.addEdge(e);
					break;
				}
			}
		}
	}
	
	
	protected static void createMovableEdges(Graph g, double attractionChance) {
		
		// fully connect the graph
		for(Node n1 : g.getNodes()) {
			for(Node n2 : g.getNodes()) {
				if(n1 == n2) {
					continue;
				}
				if(n1.connectingNodes().contains(n2)) {
					continue;
				}

				double rand = r.nextDouble();
				if(rand < attractionChance) {
					
					Edge e = new Edge(n1,n2,APCircleDisplay.ATTRACTOR);
					g.addEdge(e);
					e.setLabel(Double.toString(AreaSpecification.findAttractionValue(e)));
				} else {
					Edge e = new Edge(n1,n2,APCircleDisplay.REPULSOR);
					g.addEdge(e);
					e.setLabel(Double.toString(AreaSpecification.findMinimumSeparation(e)));
				}
			}
		}		
		
	}


	

}
