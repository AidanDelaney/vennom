package org.eulerdiagrams.vennom.graph;

import java.util.ArrayList;

public class EulerTiming {

	/**
	 * Test the performance of SMK Euler algorithms.
	 */
	public static void main(String[] args) {
		ArrayList<Node> tour = new ArrayList<Node>();
		Graph eg = new Graph();
		int testRuns = 2;
		int maxNodes = 100000;
		int nodeIncrement = 100;
		int numberOfAlgorithmCalls = 100;
		System.out.println("NON RECURSIVE");
		for(int k = 1; k <=testRuns; k++) {
			for(int i = nodeIncrement; i <= maxNodes; i += nodeIncrement) {
				int nodes = i;
				int edges = nodes*2;
				eg.generateRandomEulerGraph(nodes,edges);
				long start = System.currentTimeMillis();
				for(int j = 1; j < numberOfAlgorithmCalls; j++) {
					tour = eg.eulerSMK(false);
				}
				long end =  System.currentTimeMillis();
				System.out.println("nodes " +nodes+" iteration\t"+k+" time\t"+(end-start));
				if (!eg.eulerTourInGraph(tour)) {
					System.out.println("Test 9.X Failed with tour "+tour +"\nand graph\n"+eg);
				}
			}
		}
		System.out.println("RECURSIVE");
		for(int k = 1; k <testRuns; k++) {
			for(int i = nodeIncrement; i <= maxNodes; i += nodeIncrement) {
				int nodes = i;
				int edges = nodes*2;
				eg.generateRandomEulerGraph(nodes,edges);
				long start = System.currentTimeMillis();
				for(int j = 1; j < numberOfAlgorithmCalls; j++) {
					tour = eg.eulerSMK(true);
				}
				long end =  System.currentTimeMillis();
				System.out.println("nodes " +nodes+" iteration\t"+k+" time\t"+(end-start));
				if (!eg.eulerTourInGraph(tour)) {
					System.out.println("Test 9.X Failed with tour "+tour +"\nand graph\n"+eg);
				}
			}
		}
	}

}
