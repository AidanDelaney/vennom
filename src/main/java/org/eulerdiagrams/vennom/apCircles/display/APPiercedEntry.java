package org.eulerdiagrams.vennom.apCircles.display;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.apCircles.AbstractDiagram;
import org.eulerdiagrams.vennom.apCircles.AreaSpecification;
import org.eulerdiagrams.vennom.graph.Graph;

public class APPiercedEntry extends Frame implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton okButton;
	JButton cancelButton;
	TextArea textArea;
	JPanel panel;
	
	private static boolean first = true;
	
	APCirclePanel circlePanel;

	// only want one of these in existence at a time
	private static APPiercedEntry singleAPEntry = null;
	
	/** Node list must have at least one element */
	public APPiercedEntry(APCirclePanel circlePanel) {

		super("Enter Pierced AP Specification");
		
		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/3;
		int posY = (frameDim.height - getSize().height)/3;
		setLocation(posX, posY);


		this.circlePanel = circlePanel;
		

		panel = new JPanel();
		addWidgets(panel);
		add(panel, BorderLayout.CENTER);
		
		if(circlePanel.getGraph().getNodes().size() == 0 && first) {
			textArea.setText("a 4000\nb 6000\nab 2000\nabc 500\nac 1000\nd 2000\nad 500\ne 800\nae 700");
			first = false;
		}

		pack();
		setVisible(true);
		
		if(singleAPEntry != null) {
			singleAPEntry.dispose();
		}
		singleAPEntry = this;
	}


	protected void addWidgets(JPanel panel) {

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = externalPadding;

		textArea = new TextArea(circlePanel.getSpecification().toString(),20,20);
		gridbag.setConstraints(textArea,c);
		panel.add(textArea);
		
		textArea.selectAll();
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = externalPadding;

		okButton = new JButton("OK");
		okButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		okButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		okButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okButton(event);
			}

		});
		gridbag.setConstraints(okButton,c);
		panel.add(okButton);
		okButton.requestFocus();
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = externalPadding;

		cancelButton = new JButton("Cancel");
		cancelButton.setMinimumSize(APCirclePanel.BUTTONSIZE);
		cancelButton.setPreferredSize(APCirclePanel.BUTTONSIZE);
		cancelButton.setMaximumSize(APCirclePanel.BUTTONSIZE);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelButton(event);
			}
		});
		gridbag.setConstraints(cancelButton,c);
		panel.add(cancelButton);

	}


	public void actionPerformed(ActionEvent event) {
	}


	public void okButton(ActionEvent event) {
		if((event.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
    
// if the button has been initiated by a non button press, and the
// cancel button has the focus, redirect to the cancel 
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}

		AreaSpecification as = new AreaSpecification(new AbstractDiagram(""));
		as.fromString(textArea.getText());
		Graph g = as.generatePiercedAugmentedIntersectionGraph();
		if(g != null) {
			circlePanel.setGraph(g);
			circlePanel.setSpecification(as);
			circlePanel.update(circlePanel.getGraphics());
			circlePanel.requestFocus();

		} else {
			JOptionPane.showMessageDialog(circlePanel, "Zones do not form an atomic pierced diagram");
		}
		
	}

	public void cancelButton(ActionEvent event) {
		// if the button has been initiated by a non button press, and the
		// ok button has the focus, redirect to the ok 
		if(okButton.isFocusOwner()) {
			okButton(event);
			return;
		}
		circlePanel.requestFocus();
		dispose();
	}

} //end Help Frame

