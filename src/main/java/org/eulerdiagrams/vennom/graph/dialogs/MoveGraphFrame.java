package org.eulerdiagrams.vennom.graph.dialogs;

import javax.swing.*;
import javax.swing.border.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;

import java.util.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Creates a frame that allows the graph to be moved around.
 *
 * @author Peter Rodgers
 */
public class MoveGraphFrame extends JFrame implements ActionListener {

	protected APCirclePanel gp;
	protected Graph graph;
	protected boolean newFlag;

	protected JTextField moveField;
	protected JTextField scaleField;

	protected JPanel movePanel;
	protected JPanel scalePanel;
	protected JPanel centrePanel;
	protected JPanel buttonPanel;

	public final String MOVEDISTANCE = "20.0";
	public final String SCALEFACTOR = "2.0";
	public final int FIELDSIZE = 5;

	public MoveGraphFrame(APCirclePanel graphPanel) {

		super("Move Graph");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(graphPanel.getContainerFrame());

		gp = graphPanel;
		graph = gp.getGraph();

		movePanel = new JPanel();
		scalePanel = new JPanel();
		centrePanel = new JPanel();
		buttonPanel = new JPanel();

		Border etchedBorder = BorderFactory.createEtchedBorder();
		Border spaceBorder1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border spaceBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder,spaceBorder1);
		Border panelBorder = BorderFactory.createCompoundBorder(spaceBorder2,compoundBorder);

		movePanel.setBorder(panelBorder);
		scalePanel.setBorder(panelBorder);
		centrePanel.setBorder(panelBorder);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		getContentPane().setLayout(gridbag);

		c.ipadx = 2;
		c.ipady = 2;
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(movePanel,c);
		getContentPane().add(movePanel);

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(scalePanel,c);
		getContentPane().add(scalePanel);

		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(centrePanel,c);
		getContentPane().add(centrePanel);

		c.gridx = 0;
		c.gridy = 3;
		gridbag.setConstraints(buttonPanel,c);
		getContentPane().add(buttonPanel);

		setupMove(movePanel);
		setupScale(scalePanel);
		setupCentre(centrePanel);
		setupButtons(buttonPanel);

		pack();

		setVisible(true);
	}



	protected void setupMove(JPanel panel) {

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = externalPadding;

		JButton upButton = new JButton("Up");
		upButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		upButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		upButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph(0.0,-1.0);
			}
		});

		JButton downButton = new JButton("Down");
		downButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		downButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		downButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph(0.0,1.0);
			}
		});

		JButton leftButton = new JButton("Left");
		leftButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		leftButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		leftButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph(-1.0,0.0);
			}
		});

		JButton rightButton = new JButton("Right");
		rightButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		rightButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		rightButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph(1.0,0.0);
			}
		});

		moveField = new JTextField(MOVEDISTANCE,FIELDSIZE);

		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(upButton,c);
   		panel.add(upButton);

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(leftButton,c);
   		panel.add(leftButton);

		c.gridx = 2;
		c.gridy = 1;
		gridbag.setConstraints(rightButton,c);
   		panel.add(rightButton);

		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(downButton,c);
   		panel.add(downButton);

		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(moveField,c);
   		panel.add(moveField);

	}



	protected void moveGraph(double x, double y) {

		double multiplier = Double.parseDouble(moveField.getText());

		int moveX = (int)(x*multiplier);
		int moveY = (int)(y*multiplier);

		for(Node node : graph.getNodes()) {
			node.setX(node.getX()+moveX);
			node.setY(node.getY()+moveY);
		}

		for(Edge edge : graph.getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = point.x+moveX;
				point.y = point.y+moveY;
			}
		}
		gp.update(gp.getGraphics());

	}


	protected void setupScale(JPanel panel) {

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = externalPadding;

		JButton downButton = new JButton("Scale Down");
		downButton.setMinimumSize(APCirclePanel.LARGEBUTTONSIZE);
		downButton.setPreferredSize(APCirclePanel.LARGEBUTTONSIZE);
		downButton.setMaximumSize(APCirclePanel.LARGEBUTTONSIZE);
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				scaleGraph(false);
			}
		});

		JButton upButton = new JButton("Scale Up");
		upButton.setMinimumSize(APCirclePanel.LARGEBUTTONSIZE);
		upButton.setPreferredSize(APCirclePanel.LARGEBUTTONSIZE);
		upButton.setMaximumSize(APCirclePanel.LARGEBUTTONSIZE);
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				scaleGraph(true);
			}
		});

		scaleField = new JTextField(SCALEFACTOR,FIELDSIZE);

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(downButton,c);
   		panel.add(downButton);

		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(scaleField,c);
   		panel.add(scaleField);

		c.gridx = 2;
		c.gridy = 0;
		gridbag.setConstraints(upButton,c);
   		panel.add(upButton);

	}


	protected void scaleGraph(boolean scaleUp) {

		double multiplier = Double.parseDouble(scaleField.getText());
		if(multiplier == 0.0) {
			return;
		}
		if(!scaleUp) {
			multiplier = 1/multiplier;
		}

		int panelCentreX = gp.getX() + (gp.getWidth()/2);
		int panelCentreY = gp.getY() + (gp.getHeight()/2);

		for(Node node : graph.getNodes()) {
			node.setX(scaleCoordinate(node.getX(),panelCentreX,multiplier));
			node.setY(scaleCoordinate(node.getY(),panelCentreY,multiplier));
		}

		for(Edge edge : graph.getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = scaleCoordinate(point.x,panelCentreX,multiplier);
				point.y = scaleCoordinate(point.y,panelCentreY,multiplier);
			}
		}

		gp.update(gp.getGraphics());

	}

/**
 * given a current value, a centre to scale from and a scale multiplier
 * this method calculates the new value.
 */
	protected int scaleCoordinate(int value, int centre, double multiplier) {

		double ret = value;
		ret = ret - centre;
		ret = ret * multiplier;
		ret = ret + centre;
		return (int)ret;

	}


	protected void setupCentre(JPanel panel) {
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);

		JButton centreButton = new JButton("Centre");
		getRootPane().setDefaultButton(centreButton);
		centreButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		centreButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		centreButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		centreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				centreButton();
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(centreButton,c);
   		panel.add(centreButton);
	}


/** Moves the graph to the centre of the panel. */
	public void centreButton() {

		if(graph.getNodes().size() == 0) {
			return;
		}
		

		int panelCentreX = gp.getX() + (gp.getWidth()/2);
		int panelCentreY = gp.getY() + (gp.getHeight()/2);
		
		graph.centreOnPoint(panelCentreX,panelCentreY);

		gp.update(gp.getGraphics());
	}


	protected void setupButtons(JPanel panel) {

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);

		JButton finishedButton = new JButton("Finished");
		getRootPane().setDefaultButton(finishedButton);
		finishedButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		finishedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				finishedButton(event);
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(finishedButton,c);
   		panel.add(finishedButton);

	}



	public void actionPerformed(ActionEvent event) {
	}


	public void finishedButton(ActionEvent event) {
		dispose();
	}



}



