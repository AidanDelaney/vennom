package org.eulerdiagrams.vennom.apCircles.utilities;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.eulerdiagrams.vennom.apCircles.AbstractDiagram;
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

public class CreateRandomPiercedSpecificationByAbstractDescription extends GraphUtility implements Serializable {


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
	public static Random random = new Random();

	
/** Trivial constructor. */
	public CreateRandomPiercedSpecificationByAbstractDescription() {
		super(KeyEvent.VK_R,"Create Random Diagram by Abstract Description");
	}

/** Trivial constructor. */
	public CreateRandomPiercedSpecificationByAbstractDescription(int key, String s) {
		super(key,s);
	}
	

	public void apply() {
		
		
		Graph g = null;

		int i = 0;
		while(g == null) {
			AreaSpecification as = createRandomAreaSpecification(numberOfCircles, System.currentTimeMillis());
//			AreaSpecification as = createRandomAreaSpecification(numberOfCircles, i);
			g = as.generatePiercedAugmentedIntersectionGraph();
			i++;
		}

		getGraphPanel().setGraph(g);

		getGraph().centreOnPoint(CENTREX, CENTREY);

		
	}


	public AreaSpecification createRandomAreaSpecification(int circleCount, long seed) {
		
		AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(circleCount,false,zoneChance,seed);

		random = new Random(seed+1);
		HashMap<String,Double> mapping = new HashMap<String,Double>();
		for(String zone : ad.getZoneList()) {
			double area = random.nextInt(1+zoneAreaMax-zoneAreaMin);
			mapping.put(zone,area);
		}
	

		AreaSpecification as = new AreaSpecification(ad,mapping);
		
		return as;
	}


	

}
