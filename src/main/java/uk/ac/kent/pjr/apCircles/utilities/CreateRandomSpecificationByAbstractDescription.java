package uk.ac.kent.pjr.apCircles.utilities;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import pjr.apCircles.AbstractDiagram;
import pjr.apCircles.AreaSpecification;
import pjr.apCircles.Util;
import pjr.apCircles.display.APCircleDisplay;
import pjr.graph.*;
import pjr.graph.utilities.GraphUtility;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class CreateRandomSpecificationByAbstractDescription extends GraphUtility implements Serializable {


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

	public int numberOfCircles = 5;
	public int zoneAreaMin = 100;
	public int zoneAreaMax = 10000;
	public double zoneChance = 0.3; // chances of a movable edge being an attractor
	public static Random r = new Random(System.currentTimeMillis());

	
/** Trivial constructor. */
	public CreateRandomSpecificationByAbstractDescription() {
		super(KeyEvent.VK_R,"Create Random Diagram by Abstract Description");
	}

/** Trivial constructor. */
	public CreateRandomSpecificationByAbstractDescription(int key, String s) {
		super(key,s);
	}
	

	public void apply() {
		
		
		Graph g = null;

		int i = 0;
		while(g == null) {
			AreaSpecification as = createRandomAreaSpecification();
			g = as.generateAugmentedIntersectionGraph();
			i++;
System.out.println("Attempt "+i+"\n"+as);
		}

		getGraphPanel().setGraph(g);

		getGraph().centreOnPoint(CENTREX, CENTREY);

		
	}


	public AreaSpecification createRandomAreaSpecification() {
		
		AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(numberOfCircles,false,zoneChance);

		HashMap<String,Double> mapping = new HashMap<String,Double>();
		for(String zone : ad.getZoneList()) {
			double area = r.nextInt(1+zoneAreaMax-zoneAreaMin);
			mapping.put(zone,area);
		}
	

		AreaSpecification as = new AreaSpecification(ad,mapping);
		
		return as;
	}


	

}
