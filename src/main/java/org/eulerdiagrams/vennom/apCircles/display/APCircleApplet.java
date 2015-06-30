package org.eulerdiagrams.vennom.apCircles.display;

import javax.swing.*;
import javax.swing.border.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.drawers.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Spring Embedder and Edge Length Applet.
 *
 * @author Peter Rodgers
 */
public class APCircleApplet extends Applet {

	public final int GRAPH_PANEL_WIDTH = 400;
	public final int GRAPH_PANEL_HEIGHT = 400;
	
	protected APCirclePanel graphPanel;
	protected Graph graph;
	
	protected GraphDrawerEdgeLength metricSE;

	public void init() {

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		
		Border etchedBorder = BorderFactory.createEtchedBorder();
		Border spaceBorder1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border spaceBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder,spaceBorder1);
		Border panelBorder = BorderFactory.createCompoundBorder(spaceBorder2,compoundBorder);

		graphPanel = new APCirclePanel(this);
		
		graphPanel.setBorder(lineBorder);
		
		metricSE = new GraphDrawerEdgeLength(KeyEvent.VK_M,"Metric Spring Embedder",KeyEvent.VK_M,false,true);
		metricSE.setIterations(1000000);
		metricSE.setTimeLimit(5000);
		graphPanel.addGraphDrawer(metricSE);
		
		GraphDrawerEdgeLength gdel = new GraphDrawerEdgeLength(KeyEvent.VK_L,"Spring Embedder - edge length version",KeyEvent.VK_L,false,true);
		graphPanel.addGraphDrawer(gdel);

		BasicSpringEmbedder bse = new BasicSpringEmbedder(KeyEvent.VK_S,"Quick Start Spring Embedder, no animation",KeyEvent.VK_E);
		graphPanel.addGraphDrawer(bse);
		
		graphPanel.setPreferredSize(new Dimension(GRAPH_PANEL_WIDTH,GRAPH_PANEL_HEIGHT));
		
		JPanel drawingButtonPanel = new JPanel();
		
		setupDrawingButtonPanel(drawingButtonPanel);
		
		drawingButtonPanel.setBorder(panelBorder);

		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(graphPanel,c);
		add(graphPanel);
		
		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(drawingButtonPanel,c);
		add(drawingButtonPanel);
		
		setVisible(true);

		randomGraph(5,10);

		graphPanel.requestFocus();

	}
	
	public String getAppletInfo() {
	    return "Force Directed Edge Length Graph Drawing Algorithms by Peter Rodgers and Paul Mutton";
	}
	
	protected void setupDrawingButtonPanel(JPanel panel) {

		JButton button;
		
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = externalPadding;

		button = new JButton("Spring Embed");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				springEmbed();
			}
		});
		
		gridbag.setConstraints(button,c);
		panel.add(button);
		
		
	}
	
	protected void springEmbed() {
		metricSE.drawGraph();
	}

	protected void randomGraph(int numberOfNodes, int numberOfEdges) {
		
		final int PADX = 50;
		final int PADY = 50;
		final int MAXCOUNT =100;
		
		int count =1;
		graph.generateRandomGraph(numberOfNodes,numberOfEdges,false,false);
		while(!graph.connected()) {
			if (count >= MAXCOUNT) {
				break;
			}
			count++;
			graph.generateRandomGraphExact(numberOfNodes,numberOfEdges,false);
		}
		
		Point topLeft = new Point(PADX,PADY);
		int width = GRAPH_PANEL_WIDTH - PADX*2;
		int height = GRAPH_PANEL_HEIGHT - PADY*2;
		
		if (width < 0 || height < 0) {
			topLeft = new Point(0,0);
			width = GRAPH_PANEL_WIDTH+1;
			height = GRAPH_PANEL_HEIGHT+1;
		}
		
System.out.println(topLeft+" "+width+" "+height);

		graph.randomizeNodePoints(topLeft,width,height);

		graphPanel.update(graphPanel.getGraphics());
		graphPanel.requestFocus();
	}

}



