package org.eulerdiagrams.vennom.graph.views;

import java.awt.event.*;
import java.io.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;



/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class GraphViewShowEdgeLabel extends GraphView implements Serializable {

/** Trivial constructor. */
	public GraphViewShowEdgeLabel() {
		super(KeyEvent.VK_L,"Toggle Edge Label Display",KeyEvent.VK_L);
	}


/** Trivial constructor. */
	public GraphViewShowEdgeLabel(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphViewShowEdgeLabel(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {

		APCirclePanel panel = getGraphPanel();

		if(panel.getShowEdgeLabel()) {
			panel.setShowEdgeLabel(false);
		} else {
			panel.setShowEdgeLabel(true);
		}

	}
}

