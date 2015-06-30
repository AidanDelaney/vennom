package org.eulerdiagrams.vennom.graph.views;

import java.io.*;
import java.awt.event.*;

import org.eulerdiagrams.vennom.apCircles.APCirclePanel;

/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class GraphViewSeparateEdges extends GraphView implements Serializable {

/** Trivial constructor. */
	public GraphViewSeparateEdges() {
		super(KeyEvent.VK_P,"Toggle Parallel Edge Separation",KeyEvent.VK_P);
	}


/** Trivial constructor. */
	public GraphViewSeparateEdges(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphViewSeparateEdges(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {
		APCirclePanel panel = getGraphPanel();
		if(panel.getSeparateParallel()) {
			panel.setSeparateParallel(false);
		} else {
			panel.setSeparateParallel(true);
		}
	}
}

