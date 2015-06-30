package org.eulerdiagrams.vennom.apCircles.views;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;
import org.eulerdiagrams.vennom.graph.*;
import org.eulerdiagrams.vennom.graph.views.GraphView;

public class CircleViewAPToggleShow extends GraphView {
	
	int count = 0;


	public CircleViewAPToggleShow(String inMenuText) {
		super(inMenuText);
	}

	public CircleViewAPToggleShow(int inAccelerator, String inMenuText) {
		super(inAccelerator, inMenuText);
	}

	public CircleViewAPToggleShow(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}

	@Override
	public void view() {

		APCirclePanel gp = getGraphPanel();
		
		if(count == 0) {
			gp.setShowCirclesFlag(false);
			gp.setShowNodesFlag(true);
			gp.setShowMovableEdgesFlag(true);
			gp.setShowFixedEdgesFlag(true);
		} else if(count == 1) {
			gp.setShowCirclesFlag(true);
			gp.setShowNodesFlag(false);
			gp.setShowMovableEdgesFlag(true);
			gp.setShowFixedEdgesFlag(true);
		} else if(count == 2) {
			gp.setShowCirclesFlag(false);
			gp.setShowNodesFlag(false);
			gp.setShowMovableEdgesFlag(true);
			gp.setShowFixedEdgesFlag(true);
		} else if(count == 3) {
			gp.setShowCirclesFlag(true);
			gp.setShowNodesFlag(false);
			gp.setShowMovableEdgesFlag(false);
			gp.setShowFixedEdgesFlag(true);
		} else if(count == 4) {
			gp.setShowCirclesFlag(true);
			gp.setShowNodesFlag(false);
			gp.setShowMovableEdgesFlag(false);
			gp.setShowFixedEdgesFlag(false);
		} else if(count == 5) {
			gp.setShowCirclesFlag(true);
			gp.setShowNodesFlag(true);
			gp.setShowMovableEdgesFlag(true);
			gp.setShowFixedEdgesFlag(true);
			count = -1;
		}

		count++;
		
	}

}
