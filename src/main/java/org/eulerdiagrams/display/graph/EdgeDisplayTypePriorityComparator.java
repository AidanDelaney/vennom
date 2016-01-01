package org.eulerdiagrams.display.graph;

import org.eulerdiagrams.vennom.graph.comparators.EdgeTypePriorityComparator;

import java.util.Comparator;

/**
 * Orders edge types by their priority.
 */
public class EdgeDisplayTypePriorityComparator implements Comparator<EdgeDisplayType> {
	
	public int compare(EdgeDisplayType et1, EdgeDisplayType et2) {
		EdgeTypePriorityComparator c = new EdgeTypePriorityComparator();
		return c.compare(et1.edgeType, et2.edgeType);
	}
}
