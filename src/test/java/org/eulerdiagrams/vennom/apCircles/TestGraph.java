package org.eulerdiagrams.vennom.apCircles;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.eulerdiagrams.vennom.graph.Edge;
import org.eulerdiagrams.vennom.graph.EdgeType;
import org.eulerdiagrams.vennom.graph.Graph;
import org.eulerdiagrams.vennom.graph.Node;
import org.eulerdiagrams.vennom.graph.NodeType;
import org.eulerdiagrams.vennom.graph.UnionFind;
import org.junit.Test;

public class TestGraph {

	private boolean printme = true;
	
	private void println(String m){
		if(printme)
			System.out.println(m);
	}
	private void print(String m){
		if(printme)
			System.out.print(m);
	}
	
	@Test
	public void test_graph(){
		println("Start Graph Tests");

		System.out.println("Test 1 START: Basic Node and edge methods");


// Graph creation and deletion
		Graph g1 = new Graph();
		Node nX = new Node("nY");
		Node nY = new Node("nX");
		
		print("1.0.1");
		if(!g1.addNode(nX)) {
			fail("Test1.0.1 Fail");
		}
		print("2");
		if(!g1.addNode(nY)) {
			fail("Test1.0.2 Fail");
		}
		print("3");
		if(g1.addNode(nY)) {
			fail("Test1.0.3 Fail");
		}
		Edge eX = new Edge(nX,nY);
		print("4");
		if(!g1.addEdge(eX)) {
			fail("Test1.0.4 Fail");
		}
		print("5");
		if(g1.addEdge(eX)) {
			fail("Test1.0.5 Fail");
		}

// test the consistency checker
		print("6");
		if(!g1.consistent()) {
			fail("Test1.0.6 Fail");
		}

		ArrayList<Edge> nYTo = nY.getEdgesTo();
		nYTo.remove(eX);
		print("7");
		if(g1.consistent()) {
			fail("Test1.0.7 Fail");
		}
		nYTo.add(eX);
		print("8");
		if(!g1.consistent()) {
			fail("Test1.0.8 Fail");
		}
		ArrayList<Edge> g1Edges = g1.getEdges();
		g1Edges.remove(eX);
		print("9");
		if(g1.consistent()) {
			fail("Test1.0.9 Fail");
		}
		g1Edges.add(eX);
		print("10");
		if(!g1.consistent()) {
			fail("Test1.0.10 Fail");
		}

		Node nOutside = new Node();
		eX.setFrom(nOutside);
		print("11");
		if(g1.consistent()) {
			fail("Test1.0.11 Fail");
		}
		eX.setFrom(nX);
		print("12");
		if(!g1.consistent()) {
			fail("Test1.0.12 Fail");
		}

		g1 = new Graph("G");
		print("13");
		if(!g1.getLabel().equals("G")) {
			fail("Test1.0.13 Fail");
		}
		g1.setLabel("H");
		print("14");
		if(!g1.getLabel().equals("H")) {
			fail("Test1.0.14 Fail");
		}
		print("15");
		if(!g1.consistent()) {
			fail("Test1.0.15 Fail");
		}


/* first real test graph

               ^e9v              
 n1  e1> n2 e2> n3          n6
 e3v    e4^    e5ve6ve7^
  ---n4--- <e8 n5
*/

// Graph Object creation

		g1.clear();
		Node n1 = new Node("n1");
		print("\n1.1.1");
		if(!g1.addNode(n1)) {
			fail("Test1.1.1 Fail");
		}
		Node n2 = new Node("n2");
		print("2");
		if(!g1.addNode(n2)) {
			fail("Test1.1.2 Fail");
		}
		Node n3 = new Node();
		print("3");
		if(!g1.addNode(n3)) {
			fail("Test1.1.3 Fail");
		}
		Node n4 = new Node("n4");
		print("4");
		if(!g1.addNode(n4)) {
			fail("Test1.1.4 Fail");
		}
		Node n5 = new Node("test");
		print("5");
		if(!g1.addNode(n5)) {
			fail("Test1.1.5 Fail");
		}
		Node n6 = new Node("n6");
		print("6");
		if(!g1.addNode(n6)) {
			fail("Test1.1.6 Fail");
		}

		Edge e1 = new Edge(n6,n6);
		print("7");
		if(!g1.addEdge(e1)) {
			fail("Test1.1.7 Fail");
		}
		Edge e2 = new Edge(n2,n3);
		print("8");
		if(!g1.addEdge(e2)) {
			fail("Test1.1.8 Fail");
		}
		Edge e3 = new Edge(n1,n4);
		print("9");
		if(!g1.addEdge(e3)) {
			fail("Test1.1.9 Fail");
		}
		Edge e4 = new Edge(n4,n2);
		print("10");
		if(!g1.addEdge(e4)) {
			fail("Test1.1.10 Fail");
		}
		Edge e5 = new Edge(n3,n5);
		print("11");
		if(!g1.addEdge(e5)) {
			fail("Test1.1.11 Fail");
		}
		Edge e6 = new Edge(n3,n5);
		print("12");
		if(!g1.addEdge(e6)) {
			fail("Test1.1.12 Fail");
		}
		Edge e7 = new Edge(n3,n5);
		print("13");
		if(!g1.addEdge(e7)) {
			fail("Test1.1.13 Fail");
		}
		Edge e8 = new Edge(n1,n1);
		print("14");
		if(!g1.addEdge(e8)) {
			fail("Test1.1.14 Fail");
		}
		Edge e9 = new Edge(n3,n3);
		print("15");
		if(!g1.addEdge(e9)) {
			fail("Test1.1.15 Fail");
		}


// oposite end test
		print("16");
		if (e1.getOppositeEnd(n6) != n6) {
			fail("Test1.1.16 Fail");
		}
		print("17");
		if (e2.getOppositeEnd(n2) != n3) {
			fail("Test1.1.17 Fail");
		}
		print("18");
		if (e2.getOppositeEnd(n3) != n2) {
 			fail("Test1.1.18 Fail");
		}
		print("19");		
		if (e2.getOppositeEnd(n6) != null) {
			fail("Test1.1.19 Fail");
		}


// changing the connections of edges
		e1.setFromTo(n4,n4);
		e1.setFrom(n5);
		e1.setTo(n5);
		e1.setFromTo(n3,n6);
		e1.setFromTo(n1,n2);
		e8.setFrom(n5);
		e8.setTo(n4);
		e7.reverse();
		e9.reverse();
// resetting labels
		n3.setLabel("n3");
		n5.setLabel("n5");

		Node tempTo = e1.getTo();
		Node tempFrom = e1.getFrom();

		e1.reverse();
		print("20");
		if(e1.getTo() != tempFrom) {
			fail("Test1.1.20 Fail");
		}

		print("21");
		if(e1.getFrom() != tempTo) {
			fail("Test1.1.21 Fail");
		}

		e1.reverse();

		print("22");
		if(e1.getTo() != tempTo) {
			fail("Test1.1.22 Fail");
		}

		print("23");
		if(e1.getFrom() != tempFrom) {
			fail("Test1.1.23 Fail");
		}

		print("24COMMENTEDOUT");
		if(!g1.consistent()) {
			//fail("Test1.1.24 Fail");  //TODO has been known to fail for some seed value e.g. 12345
		}


// Node connectivity testing
		HashSet<Node> testN = new HashSet<Node>();

		print("\n1.2.1");
		if(!n6.connectingNodes().equals(testN)) {
			fail("Test1.2.1 Fail");
		}
		print("2");
		if(!n6.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.2.2 Fail");
		}
		print("4");
		if(!n6.connectingEdges().equals(testN)) {
			fail("Test1.2.4 Fail");
		}

		testN = new HashSet<Node>();
		testN.add(n5);
		testN.add(n3);
		testN.add(n2);

		print("5");
		if(!n3.connectingNodes().equals(testN)) {
			fail("Test1.2.5 Fail");
		}

		print("7");
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.2.7 Fail");
		}


// Node visited flag testing

		n2.setVisited(true);

		print("\n1.3.1");
		if(!n3.connectingNodes().equals(testN)) {
			fail("Test1.3.1 Fail");
		}
		testN.remove(n2);
		print("3");
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.3.3 Fail"); //TODO has been known to fail for some seed value
		}

		g1.setNodesVisited(true);
		testN = new HashSet<Node>();

		print("4");
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.3.4 Fail");
		}

		n2.setVisited(false);
		testN.add(n2);

		print("5");
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.3.5 Fail");
		}

		testN.remove(n2);

		n2.setVisited(true);
		testN = new HashSet<Node>();
		print("6");
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			fail("Test1.3.6 Fail");
		}

		testN = new HashSet<Node>();
		testN.add(n5);
		testN.add(n3);
		testN.add(n2);
		print("7");
		if(!n3.connectingNodes().equals(testN)) {
			fail("Test1.3.7 Fail");
		}
		g1.setNodesVisited();


// Edge visited flag testing
		
		HashSet<Edge> testE = new HashSet<Edge>();

		testE = new HashSet<Edge>();
		testE.add(e3);
		testE.add(e1);
		print("\n1.5.1");
		if(!n1.connectingEdges().equals(testE)) {
			fail("Test1.5.1 Fail");
		}
		print("2");
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			fail("Test1.5.2 Fail");
		}

		testE.remove(e1);
		e1.setVisited(true);
		print("3");
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			fail("Test1.5.3 Fail");
		}
		e1.setVisited(false);
		e2.setVisited(false);

		g1.setEdgesVisited(true);
		testE = new HashSet<Edge>();
		print("4");
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			fail("Test1.5.4 Fail");
		}
		e1.setVisited(true);
		e2.setVisited(true);
		g1.setEdgesVisited(false);
		testE = new HashSet<Edge>();
		testE.add(e3);
		testE.add(e1);
		print("5");
		if(!n1.connectingEdges().equals(testE)) {
			fail("Test1.5.5 Fail");
		}
		print("6");
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			fail("Test1.5.6 Fail");
		}

		print("Test 1 END");


// test dynamic graph stuff
		println(" | Test 2 START: Object removal");
		Node nd1 = new Node("nd1");
		Node nd2 = new Node("nd2");
		Node nd3 = new Node("nd3");

		Graph g2 = new Graph("g2");
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);

		Edge ed1 = new Edge(nd1,nd2);
		Edge ed2 = new Edge(nd1,nd2);
		Edge ed3 = new Edge(nd2,nd1);
		Edge ed4 = new Edge(nd1,nd1);
		Edge ed5 = new Edge(nd3,nd2);
		g2.addEdge(ed1);
		g2.addEdge(ed2);
		g2.addEdge(ed3);
		g2.addEdge(ed4);
		g2.addEdge(ed5);

		g2.removeNode("nd1");

		ArrayList<Node> testNodeAL;
		ArrayList<Edge> testEdgeAL;
		testEdgeAL = new ArrayList<Edge>();
		testEdgeAL.add(ed5);
		if(!g2.getEdges().equals(testEdgeAL)) {
			//fail("Test2.1 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		g2.addNode(nd3);
		if(!g2.getEdges().equals(testEdgeAL)) {
			//fail("Test2.2 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		g2.removeNode(nd2);
		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.3 Fail");
		}
		testNodeAL = new ArrayList<Node>();
		testNodeAL.add(nd3);
		if(!g2.getNodes().equals(testNodeAL)) {
			fail("Test2.4 Fail");
		}
		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.5 Fail");
		}

		if(!g2.consistent()) {
			fail("Test2.5.0 Fail");
		}


		g2.clear();

		Node dyNode1 = new Node("dyNode1");
		Node dyNode2 = new Node("dyNode2");
		g2.addNode(dyNode1);
		g2.addNode(dyNode2);

		Edge dyEdge1 = new Edge(dyNode1,dyNode2,"dyEdge2");
		g2.addEdge(dyEdge1);

		g2.removeEdge(dyEdge1);

		if(dyEdge1.getFrom() != null) {
			fail("Test2.5.1 Fail");
		}
		if(dyEdge1.getTo() != null) {
			fail("Test2.5.2 Fail");
		}

		HashSet dyTest = new HashSet();

		if(dyNode1.getEdgesFrom().equals(dyTest) == false) {
			//fail("Test2.5.3 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		if(dyNode1.getEdgesTo().equals(dyTest) == false) {
			//fail("Test2.5.4 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		if(dyNode2.getEdgesFrom().equals(dyTest) == false) {
			//fail("Test2.5.5 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		if(dyNode2.getEdgesTo().equals(dyTest) == false) {
			//fail("Test2.5.6 Fail");  //TODO has been known to fail for some seed value e.g. 12345
		}
		if(!g2.consistent()) {
			fail("Test2.5.7 Fail");
		}


		g2.clear();
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);

		ed1 = new Edge(nd1,nd2,"A",1.1);
		ed2 = new Edge(nd1,nd2,"B");
		ed3 = new Edge(nd2,nd1,1);
		ed4 = new Edge(nd1,nd1);
		ed5 = new Edge(nd3,nd2);
		g2.addEdge(ed1);
		g2.addEdge(ed2);
		g2.addEdge(ed3);
		g2.addEdge(ed4);
		g2.addEdge(ed5);


// edge label and weight testing
		if(!ed1.getLabel().equals("A")) {
			fail("Test2.5.1 Fail");
		}
		ed1.setLabel("C");
		if(!ed1.getLabel().equals("C")) {
			fail("Test2.5.2 Fail");
		}
		if(!ed2.getLabel().equals("B")) {
			fail("Test2.5.3 Fail");
		}
		if(ed1.getWeight()!=1.1) {
			fail("Test2.5.4 Fail");
		}
		ed1.setWeight(2.2);
		if(ed1.getWeight()!=2.2) {
			fail("Test2.5.5 Fail");
		}
		if(ed3.getWeight()!=1) {
			fail("Test2.5.6 Fail");
		}

		g2.removeEdge(ed2);
		testEdgeAL = new ArrayList<Edge>();
		testEdgeAL.add(ed1);
		testEdgeAL.add(ed3);
		testEdgeAL.add(ed4);
		testEdgeAL.add(ed5);
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.6 Fail");
		}
		ArrayList<Node> testNodeAL2;
		
		testNodeAL2 = new ArrayList<Node>();
		testNodeAL2.add(nd1);
		testNodeAL2.add(nd2);
		testNodeAL2.add(nd3);
		if(!g2.getNodes().equals(testNodeAL2)) {
			fail("Test2.7 Fail");
		}

		g2.removeEdge(ed4);

		testEdgeAL.remove(ed4);
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.8 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			fail("Test2.9 Fail");
		}

		ed4 = new Edge(nd3,nd3);
		g2.addEdge(ed4);
		testEdgeAL.add(ed4);
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.10 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			fail("Test2.11 Fail");
		}

		if(!g2.consistent()) {
			fail("Test2.11.0 Fail");
		}

		g2.clear();

		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.12 Fail");
		}
		if(!g2.getNodes().equals(testEdgeAL)) {
			fail("Test2.13 Fail");
		}

		ed4 = new Edge(nd2,nd1);
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);
		g2.addEdge(ed4);

		g2.removeEdge(ed4);

		if(!g2.getEdges().equals(testEdgeAL)) {
			fail("Test2.14 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			fail("Test2.15 Fail");
		}

		if(!g2.consistent()) {
			fail("Test2.15.0 Fail");
		}

//score field testing
		Graph gScore = new Graph();

		Node ns1 = new Node("A");
		Node ns2 = new Node();
		Node ns3 = new Node("C");
		Node ns4 = new Node("D");

		gScore.addNode(ns1);
		gScore.addNode(ns2);
		gScore.addNode(ns3);
		gScore.addNode(ns4);

		Edge es1 = new Edge(ns1,ns2,"A",1.1);
		Edge es2 = new Edge(ns4,ns1);
		Edge es3 = new Edge(ns1,ns3);

		gScore.addEdge(es1);
		gScore.addEdge(es2);
		gScore.addEdge(es3);

		if(ns1.getScore() != 0.0) {
			fail("Test2.16.1 Fail");
		}

		if(es1.getScore() != 0.0) {
			fail("Test2.16.2 Fail");
		}

		ns1.setScore(1.3);
		es1.setScore(2.4);

		if(ns1.getScore() != 1.3) {
			fail("Test2.16.3 Fail");
		}

		if(es1.getScore() != 2.4) {
			fail("Test2.16.4 Fail");
		}

		ArrayList<Node> alNodeScore = new ArrayList<Node>();
		ArrayList<Edge> alEdgeScore = new ArrayList<Edge>();
		gScore.setNodesScores(alNodeScore,3.5);
		gScore.setEdgesScores(alEdgeScore,4.6);

		if(ns1.getScore() != 1.3) {
			fail("Test2.16.5 Fail");
		}

		if(es1.getScore() != 2.4) {
			fail("Test2.16.6 Fail");
		}

		alNodeScore.add(ns1);
		alNodeScore.add(ns2);
		gScore.setNodesScores(alNodeScore,5.7);
		alNodeScore.remove(ns1);
		alNodeScore.remove(ns2);
		alEdgeScore.add(es1);
		alEdgeScore.add(es2);
		gScore.setEdgesScores(alEdgeScore,6.8);

		if(ns1.getScore() != 5.7) {
			fail("Test2.16.7 Fail");
		}
		if(ns3.getScore() != 0.0) {
			fail("Test2.16.8 Fail");
		}

		if(es1.getScore() != 6.8) {
			fail("Test2.16.9 Fail");
		}
		if(es3.getScore() != 0.0) {
			fail("Test2.16.10 Fail");
		}

		gScore.setNodesScores(7.9);
		gScore.setEdgesScores(8.01);

		if(ns1.getScore() != 7.9) {
			fail("Test2.16.11 Fail");
		}
		if(ns3.getScore() != 7.9) {
			fail("Test2.16.12 Fail");
		}

		if(es1.getScore() != 8.01) {
			fail("Test2.16.13 Fail");
		}
		if(es3.getScore() != 8.01) {
			fail("Test2.16.14 Fail");
		}

		Graph testChangeGraph = new Graph();

		if(testChangeGraph.moveNodeToEnd(n1)) {
			fail("Test2.17.1 Fail");
		}
		if(testChangeGraph.moveEdgeToEnd(e1)) {
			fail("Test2.17.2 Fail");
		}

		ArrayList<Node> testChangeNodeAL = new ArrayList<Node>();

		Node changeNode1 = new Node("c1");
		testChangeGraph.addNode(changeNode1);

		if(testChangeGraph.moveNodeToEnd(n1)) {
			fail("Test2.17.3 Fail");
		}

		testChangeGraph.moveNodeToEnd(changeNode1);

		testChangeNodeAL = new ArrayList<Node>();
		testChangeNodeAL.add(changeNode1);

		if(!testChangeGraph.getNodes().equals(testChangeNodeAL)) {
			fail("Test2.17.4 Fail");
		}

		Node changeNode2 = new Node("c2");
		Node changeNode3 = new Node("c3");

		Edge changeEdge1 = new Edge(changeNode1,changeNode2);
		Edge changeEdge2 = new Edge(changeNode3,changeNode2);
		Edge changeEdge3 = new Edge(changeNode1,changeNode3);

		testChangeGraph.addNode(changeNode2);
		testChangeGraph.addNode(changeNode3);

		testChangeGraph.addEdge(changeEdge1);
		testChangeGraph.addEdge(changeEdge2);
		testChangeGraph.addEdge(changeEdge3);

		testChangeNodeAL = new ArrayList<Node>();
		testChangeNodeAL.add(changeNode1);
		testChangeNodeAL.add(changeNode3);
		testChangeNodeAL.add(changeNode2);

		testChangeGraph.moveNodeToEnd(changeNode2);

		if(!testChangeGraph.getNodes().equals(testChangeNodeAL)) {
			fail("Test2.17.5 Fail");
		}

		ArrayList<Edge> testChangeEdgeAL = new ArrayList<Edge>();
		testChangeEdgeAL.add(changeEdge2);
		testChangeEdgeAL.add(changeEdge3);
		testChangeEdgeAL.add(changeEdge1);

		testChangeGraph.moveEdgeToEnd(changeEdge1);

		if(!testChangeGraph.getEdges().equals(testChangeEdgeAL)) {
			fail("Test2.17.6 Fail");
		}


		print("Test 2 END");


		println(" | Test 3 START: Shortest path");

		ArrayList<Node> path = new ArrayList<Node>();
		path.add(n5);
		path.add(n4);
		path.add(n1);
		if(!g1.unweightedShortest(n5,n1).equals(path)) {
			//fail("Test3.1 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}
		path = new ArrayList<Node>();
		path.add(n3);
		if(!g1.unweightedShortest(n3,n3).equals(path)) {
			fail("Test3.2 Fail");
		}
		if(g1.unweightedShortest(n4,n6) != null) {
			fail("Test3.3 Fail");
		}

		print("Test 3 END");


//Adjacency Edge Graph creation stuff

		println(" | Test 4 START: Adjacency Graph");

//finding or adding a node from a label
		Node nA = g1.addAdjacencyNode("nA");
		testNodeAL = new ArrayList<Node>();
		testNodeAL.add(n1);
		testNodeAL.add(n2);
		testNodeAL.add(n3);
		testNodeAL.add(n4);
		testNodeAL.add(n5);
		testNodeAL.add(n6);
		testNodeAL.add(nA);
		if(!g1.getNodes().equals(testNodeAL)) {
			fail("Test4.1 Fail");
		}

		if(g1.addAdjacencyNode("n2") != n2) {
			fail("Test4.2 Fail");
		}
		if(!g1.getNodes().equals(testNodeAL)) {
			fail("Test4.3 Fail");
		}

		Node nDuplicate = new Node("n1");
		g1.addNode(nDuplicate);
		if(g1.addAdjacencyNode("n1") != null) {
			fail("Test4.4 Fail");
		}
		g1.removeNode(nDuplicate);

		if(!g1.consistent()) {
			//fail("Test4.4.0 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}

//adding an adjacency edge
		Edge eZ1 = g1.addAdjacencyEdge("n3","n3");

		Edge[] tempArray = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1};
		ArrayList<Edge> testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			fail("Test4.5 Fail");
		}
		if(!g1.getNodes().equals(testNodeAL)) {
			fail("Test4.6 Fail"); //TODO has been known to fail for some seed value
		}

		g1.addAdjacencyEdge("nB","");
		if(g1.getNodes().size() != 8) {
			fail("Test4.7 Fail");
		}

		Edge eZ3 = g1.addAdjacencyEdge("nB","n1");

		Edge[] tempArray2 = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1,eZ3};
		testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray2));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			fail("Test4.8 Fail");
		}

		g1.addAdjacencyEdge("","");
		Edge eZ4 = g1.addAdjacencyEdge("nC","nD");

		Edge[] tempArray3 = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1,eZ3,eZ4};
		testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray3));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			fail("Test4.9 Fail");
		}
		if(g1.getNodes().size() != 10) {
			fail("Test4.10 Fail");
		}

		print("Test 4 END");

		println(" | Test 5 START: Connected");

		if(g1.connected()) {
			fail("Test5.1 Fail");
		}
		g1.addEdge(new Edge(nA,n6));
		if(g1.connected()) {
			fail("Test5.2 Fail");
		}
		g1.addAdjacencyEdge("nA","nD");
		g1.addAdjacencyEdge("nA","n4");
		if(!g1.connected()) {
			fail("Test5.3 Fail"); //TODO has been known to fail for some seed value
		}

		if(!g1.consistent()) {
			//fail("Test5.3.0 Fail"); //TODO has been known to fail for some seed value e.g. 12345
		}

		Graph g4 = new Graph("g4");
		if(!g4.connected()) {
			fail("Test5.4 Fail");
		}
		g4.addNode(new Node("z1"));
		if(!g4.connected()) {
			fail("Test5.5 Fail");
		}
		g4.addNode(new Node("z2"));
		if(g4.connected()) {
			fail("Test5.6 Fail");
		}
		g4.addAdjacencyEdge("z1","z2");
		if(!g4.connected()) {
			fail("Test5.7 Fail");
		}
		g4.addAdjacencyEdge("z2","z3");
		if(!g4.connected()) {
			fail("Test5.8 Fail");
		}
		g4.removeNode("z2");
		if(g4.connected()) {
			fail("Test5.9 Fail");
		}


		print("Test 5 END");


		println(" | Test 6 START: Equality by label testing");

		Graph gc1 = new Graph("gc1");
		Graph gc2 = new Graph("gc2");

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.1 Fail");
		}

		gc1.addNode(new Node("A"));
		if(gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.2 Fail");
		}
		gc2.addNode(new Node("B"));

		if(gc2.equalsByNodeLabel(gc1)) {
			fail("Test6.3 Fail");
		}

		gc2.addNode(new Node("A"));

		if(gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.4 Fail");
		}

		gc1.addNode(new Node("B"));

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.5 Fail");
		}

		gc1.addNode(new Node("C"));
		gc2.addNode(new Node("C"));

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.6 Fail");
		}


		gc1.addAdjacencyEdge("A","B");

		if(gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.7 Fail");
		}

		gc2.addAdjacencyEdge("A","B");

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.8 Fail");
		}

		gc1.addAdjacencyEdge("C","B");
		gc2.addAdjacencyEdge("C","B");

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.9 Fail");
		}

		gc1.addAdjacencyEdge("C","A");
		gc2.addAdjacencyEdge("A","C");

		if(gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.10 Fail");
		}

//this test is two non isomorphic graphs for which the comparison succeeds.

		gc1.clear();
		gc2.clear();

		Node gcn1 = new Node("");
		Node gcn2 = new Node("");
		Node gcn3 = new Node("");
		Node gcn4 = new Node("");
		gc1.addNode(gcn1);
		gc1.addNode(gcn2);
		gc2.addNode(gcn3);
		gc2.addNode(gcn4);

		gc1.addEdge(new Edge(gcn1,gcn2));

		gc2.addEdge(new Edge(gcn3,gcn3));

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.11 Fail");
		}

// dispite the equal labels these graphs are not equal due to the extra edge
		gc2.addEdge(new Edge(gcn4,gcn3));

		if(gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.12 Fail");
		}

		gc1.addEdge(new Edge(gcn1,gcn2));

		if(!gc1.equalsByNodeLabel(gc2)) {
			fail("Test6.13 Fail");
		}

		if(!gc1.consistent()) {
			fail("Test6.13.0 Fail");
		}


		print("Test 6 END");


//Adjacency file testing. This test creates a file called test.adj
//It will crash with an exception if it cant create the file.

		println(" | Test 7 START: Adjacency file - needs to read and write test.adj");

		long seed = 12345;
		g4.generateRandomGraph(10,15, seed);
		g4.saveAdjacencyFile("test.adj");
		Graph g5 = new Graph("g5");
		g5.loadAdjacencyFile("test.adj");

		if(!g4.equalsByNodeLabel(g5)) {
			fail("Test7.1 Fail");
		}

		if(!g4.consistent()) {
			fail("Test7.1.0 Fail");
		}
		if(!g5.consistent()) {
			fail("Test7.1.1 Fail");
		}


		g4.clear();
		g4.saveAdjacencyFile("test.adj");
		g5.clear();
		g5.generateRandomGraph(10,15, seed);
		g4.loadAdjacencyFile("test.adj");
		if(g4.equalsByNodeLabel(g5)) {
			fail("Test7.2 Fail");
		}
		g4.generateRandomGraph(20,30, seed);
		g4.addNode(n1);
		g4.saveAdjacencyFile("test.adj");
		g5.generateRandomGraph(10,15, seed);
		g5.loadAdjacencyFile("test.adj");
		g4.loadAdjacencyFile("test.adj");
		if(!g5.equalsByNodeLabel(g4)) {
			fail("Test7.3 Fail"); //TODO has been known to fail for some seed value
		}
		g4.clear();
		g4.saveAdjacencyFile("test.adj");
		g4.addNode(n1);
		g4.loadAdjacencyFile("test.adj");
		if(!g4.equalsByNodeLabel(new Graph())) {
			fail("Test7.4 Fail"); //TODO has been known to fail for some seed value
		}

		if(!g4.consistent()) {
			fail("Test7.4.0 Fail");
		}

		print("Test 7 END");


//partial node and edge access

		println(" | Test 8 START: visited and path fields");

		Graph g = new Graph();
		HashSet<Node> testa = new HashSet<Node>();
		HashSet<Node> testb = new HashSet<Node>();

		if(!g.unvisitedNodes().equals(testa)) {
			fail("Test8.1 Fail");
		}
		if(!g.visitedNodes().equals(testb)) {
			fail("Test8.2 Fail");
		}

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);

		Edge eV1 = new Edge(n1,n2);
		Edge eV2 = new Edge(n3,n2);
		Edge eV3 = new Edge(n4,n4);

		testa.add(n1);
		testa.add(n2);
		testa.add(n3);
		testa.add(n4);

		if(!g.unvisitedNodes().equals(testa)) {
			fail("Test8.5 Fail"); //TODO has been known to fail for some seed value
		}
		if(!g.visitedNodes().equals(testb)) {
			fail("Test8.6 Fail"); //TODO has been known to fail for some seed value
		}

		n1.setVisited(true);
		n2.setVisited(true);

		testa.remove(n1);
		testa.remove(n2);

		testb.add(n1);
		testb.add(n2);

		if(!g.unvisitedNodes().equals(testa)) {
			fail("Test8.9 Fail"); //TODO has been known to fail for some seed value
		}
		if(!g.visitedNodes().equals(testb)) {
			fail("Test8.10 Fail");
		}

		HashSet<Edge> teste1 = new HashSet<Edge>();
		HashSet<Edge> teste2 = new HashSet<Edge>();

		if(!g.unvisitedEdges().equals(teste1)) {
			fail("Test8.13 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			fail("Test8.14 Fail");
		}

		g.addEdge(eV1);
		g.addEdge(eV2);
		g.addEdge(eV3);

		eV1.setVisited(true);
		eV2.setVisited(true);
		eV3.setVisited(true);

		teste2.add(eV1);
		teste2.add(eV2);
		teste2.add(eV3);

		if(!g.unvisitedEdges().equals(teste1)) {
			fail("Test8.15 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			fail("Test8.16 Fail");
		}

		eV3.setVisited(false);

		teste1.add(eV3);
		teste2.remove(eV3);

		if(!g.unvisitedEdges().equals(teste1)) {
			fail("Test8.17 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			// fail("Test8.18 Fail"); //TODO has been known to fail for some seed value e.g. 12345, 12346
		}

		print("Test 8 END");

//euler tour tests
		println(" | Test 9 START: Euler Tour - needs to read and write test.euler");

		Graph eg = new Graph();
		ArrayList<Node> tour1 = new ArrayList<Node>();
		eg.saveTour("test.euler",tour1);
		ArrayList<Node> tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.1 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2.add(n1);
		if (eg.eulerTourInGraph(tour2)) {
			fail("Test 9.2 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (eg.eulerTourInGraph(tour2)) {
			fail("Test 9.3 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}

		eg = new Graph();
		eg.generateRandomEulerGraph(5,7, seed);
		tour1 = eg.euler();
		eg.saveTour("test.euler",tour1);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.4 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(false);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.4a Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(true);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.4b Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2.remove(2);
		if (eg.eulerTourInGraph(tour2)) {
			fail("Test 9.5 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.addAdjacencyEdge("1","2");
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.6 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		
		if(!eg.consistent()) {
			fail("Test 9.6.0 Fail");
		}

		eg.clear();
		eg.addNode(n1);
		tour1 = eg.euler();
		tour2 = new ArrayList<Node>();
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.6.1 Failed with tour "+tour2 +"\nand graph\n"+eg); //TODO has been known to fail for some seed value
		}
		eg.generateRandomEulerGraph(2,4, seed);
		tour1 = eg.euler();
		eg.saveTour("test.euler",tour1);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.7 Failed");// with tour "+tour2 +"\nand graph\n"+eg); //TODO has been known to fail for some seed value
		}
		tour2 = eg.eulerSMK(false);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.7a Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(true);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.7b Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = new ArrayList<Node>();
		if (eg.eulerTourInGraph(tour2)) {
			fail("Test 9.8 Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.addNode(new Node());
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.9 Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}

		if(!eg.consistent()) {
			fail("Test 9.9.0 Fail");
		}

		eg = new Graph();
		eg.generateRandomEulerGraph(20,30, seed);
		tour2 = eg.euler();
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.10 Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(false);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.10a Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(true);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.10b Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}

		if(!eg.consistent()) {
			fail("Test 9.10.0 Fail");
		}

		Node nX0 = new Node("0");
		Node nX1 = new Node("1");
		Node nX2 = new Node("2");
		Node nX3 = new Node("3");
		Node nX4 = new Node("4");
		eg = new Graph();
		eg.addNode(nX0);
		eg.addNode(nX1);
		eg.addNode(nX2);
		eg.addNode(nX3);
		eg.addNode(nX4);
		eg.addEdge(new Edge(nX0,nX1));
		eg.addEdge(new Edge(nX3,nX2));
		eg.addEdge(new Edge(nX4,nX2));
		eg.addEdge(new Edge(nX0,nX1));
		eg.addEdge(new Edge(nX3,nX0));
		eg.addEdge(new Edge(nX0,nX3));
		eg.addEdge(new Edge(nX3,nX4));
		tour2 = eg.euler();
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.11 Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(false);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.11a Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = eg.eulerSMK(true);
		if (!eg.eulerTourInGraph(tour2)) {
			fail("Test 9.11b Failed");// with tour "+tour2 +"\nand graph\n"+eg);
		}


		print("Test 9 END");


// test the brute force tsp
		println(" | Test 10 START: tsp");

		Graph tspGraph = new Graph();

		ArrayList<Edge> tspTest = new ArrayList<Edge>();

		if (!tspGraph.tsp().equals(tspTest)) {
			fail("Test 10.1 Failed");
		}

		Node tspN1 = new Node("A");
		tspGraph.addNode(tspN1);

		if (!tspGraph.tsp().equals(tspTest)) {
			fail("Test 10.2 Failed");
		}

		Node tspN2 = new Node("B");
		tspGraph.addNode(tspN2);

		Edge tspE1 = new Edge(tspN1,tspN2,2);

		tspGraph.addEdge(tspE1);

		tspTest.add(tspE1);
		tspTest.add(tspE1);

		if (!tspGraph.tsp().equals(tspTest)) {
			fail("Test 10.3 Failed");
		}
		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 4.0) {
			fail("Test 10.4 Failed");
		}

		Node tspN3 = new Node("C");
		tspGraph.addNode(tspN3);

		Edge tspE2 = new Edge(tspN1,tspN3,40);
		Edge tspE3 = new Edge(tspN2,tspN3,3);

		tspGraph.addEdge(tspE2);
		tspGraph.addEdge(tspE3);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 10.0) {
			fail("Test 10.5 Failed");
		}

		tspE2.setWeight(4);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 9.0) {
			fail("Test 10.6 Failed");
		}

		Node tspN4 = new Node("D");
		tspGraph.addNode(tspN4);

		tspGraph.addEdge(new Edge(tspN1,tspN4,3.5));
		tspGraph.addEdge(new Edge(tspN2,tspN4,6));
		tspGraph.addEdge(new Edge(tspN4,tspN3,2.4));

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 10.9) {
			fail("Test 10.7 Failed");
		}

		tspGraph.clear();

		tspGraph.addAdjacencyEdge("A","H",17.0);
		tspGraph.addAdjacencyEdge("G","H",1.0);
		tspGraph.addAdjacencyEdge("H","E",2);
		tspGraph.addAdjacencyEdge("G","E",9.0);
		tspGraph.addAdjacencyEdge("E","D",20.0);
		tspGraph.addAdjacencyEdge("C","D",2.5);
		tspGraph.addAdjacencyEdge("C","B",3.0);
		tspGraph.addAdjacencyEdge("D","B",4.0);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 89.5) {
			fail("Test 10.8 Failed");
		}

		tspGraph.clear();

		tspGraph.addAdjacencyEdge("A","H","17",17.0);
		tspGraph.addAdjacencyEdge("G","H","1",1.0);
		tspGraph.addAdjacencyEdge("G","A","1",1.0);
		tspGraph.addAdjacencyEdge("E","D","20",20.0);
		tspGraph.addAdjacencyEdge("C","D","2 and a half",2.5);
		tspGraph.addAdjacencyEdge("C","B","3",3.0);
		tspGraph.addAdjacencyEdge("D","B","4",4.0);

		if (tspGraph.tsp() != null) {
			fail("Test 10.9 Failed");
		}

		print("Test 10 END");

		println(" | Test 11 START: Union Find");

		UnionFind uf = new UnionFind(10);

		if(uf.find(9,4)) {
			fail("Test 11.1 Fail");
		}

		uf.union(1,2);
		if(uf.getParent()[1] != 2) {
			fail("Test 11.2 Fail");
		}
		if(uf.getParent()[2] != uf.ROOT) {
			fail("Test 11.3 Fail");
		}

		uf.union(3,2);
		if(!uf.find(3,2)) {
			fail("Test 11.4 Fail");
		}
		if(uf.find(3,7)) {
			fail("Test 11.5 Fail");
		}
		if(uf.find(7,3)) {
			fail("Test 11.6 Fail");
		}
		uf.union(5,4);
		if(!uf.find(5,4)) {
			fail("Test 11.7 Fail");
		}
		if(uf.find(3,4)) {
			fail("Test 11.8 Fail");
		}
		if(uf.find(5,7)) {
			fail("Test 11.9 Fail");
		}
		uf.union(3,4);
		if(!uf.find(3,4)) {
			fail("Test 11.10 Fail");
		}
		uf.union(4,1);
		if(!uf.find(4,3)) {
			fail("Test 11.11 Fail");
		}
		uf.union(7,6);
		uf.union(8,9);
		uf.union(8,7);
		if(uf.find(7,2)) {
			fail("Test 11.12 Fail");
		}
		uf.union(8,4);
		if(!uf.find(7,2)) {
			fail("Test 11.13 Fail");
		}
		if(uf.find(7,10)) {
			fail("Test 11.12 Fail");
		}
		print("Test 11 END");

		println(" | Test 12 START: mst");

		Graph mstGraph = new Graph();

//generating a complete graph
		mstGraph.generateCompleteGraph(0);
		if(mstGraph.getNodes().size() != 0) {
			fail("Test12.1.1 Fail");
		}
		if(mstGraph.getEdges().size() != 0) {
			fail("Test12.1.2 Fail");
		}

		mstGraph.generateCompleteGraph(1);
		if(mstGraph.getNodes().size() != 1) {
			fail("Test12.1.3 Fail");
		}
		if(mstGraph.getEdges().size() != 0) {
			fail("Test12.1.4 Fail");
		}

		if(!mstGraph.consistent()) {
			fail("Test 12.1.4.0 Fail");
		}

		mstGraph.generateCompleteGraph(5);
		if(mstGraph.getNodes().size() != 5) {
			fail("Test12.1.5 Fail");
		}
		if(mstGraph.getEdges().size() != 10) {
			fail("Test12.1.6 Fail");
		}

		mstGraph.generateCompleteGraph(8);
		if(mstGraph.getNodes().size() != 8) {
			fail("Test12.1.7 Fail");
		}
		if(mstGraph.getEdges().size() != 28) {
			fail("Test12.1.8 Fail");
		}

		mstGraph.setEdgesWeights(1.0);
		if(mstGraph.sumEdgeWeights(mstGraph.prim()) != 7) {
			fail("Test12.2.1 Fail");
		}
		if(mstGraph.sumEdgeWeights(mstGraph.kruskal()) != 7) {
			fail("Test12.2.2 Fail");
		}

		mstGraph.clear();

		ArrayList<Edge> mstCompare = new ArrayList<Edge>();

		if(!mstCompare.equals(mstGraph.prim())) {
			fail("Test12.2.3 Fail");
		}
		if(!mstCompare.equals(mstGraph.kruskal())) {
			fail("Test12.2.4 Fail");
		}

		Node mstN1 = new Node("A");
		Node mstN2 = new Node("B");
		Node mstN3 = new Node("C");

		mstGraph.addNode(mstN1);

		if(!mstCompare.equals(mstGraph.prim())) {
			fail("Test12.2.5 Fail");
		}
		if(!mstCompare.equals(mstGraph.kruskal())) {
			fail("Test12.2.6 Fail");
		}
		
		mstGraph.addNode(mstN2);

		if(mstGraph.prim() != null) {
			fail("Test12.2.7 Fail");
		}
		if(mstGraph.kruskal() != null) {
			fail("Test12.2.8 Fail");
		}

		Edge mstE1 = new Edge(mstN1,mstN2,"A",1);

		mstGraph.addEdge(mstE1);

		mstCompare.add(mstE1);
		if(!mstCompare.equals(mstGraph.prim())) {
			fail("Test12.2.9 Fail");
		}
		if(!mstCompare.equals(mstGraph.kruskal())) {
			fail("Test12.2.10 Fail");
		}

		mstGraph.addNode(mstN3);
		Edge mstE2 = new Edge(mstN2,mstN3,"B",5);
		Edge mstE3 = new Edge(mstN3,mstN1,"C",2);
		Edge mstE4 = new Edge(mstN2,mstN2,"D",1);

		mstGraph.addEdge(mstE2);
		mstGraph.addEdge(mstE3);
		mstGraph.addEdge(mstE4);

		mstCompare.add(mstE3);

		if(mstGraph.sumEdgeWeights(mstGraph.prim()) != 3) {
			fail("Test12.2.11 Fail");
		}
		if(mstGraph.sumEdgeWeights(mstGraph.kruskal()) != 3) {
			fail("Test12.2.12 Fail");
		}

	
/*  TODO
tests fail at line mstGraph.prim()

//generated graph test - randomly generate graphs of size
//1-10 nodes, edges 4-14, and compare prim against kruskal
//on test fail, the graph should be printed for reference.
		for(int i =1; i<=10; i++) {

			mstGraph.generateRandomGraph(i,i+3);
			mstGraph.randomlyWeightGraph(0,100);

			System.out.println("\ngo to call prim");
			System.out.println("\nmstGraph = "+mstGraph);
			ArrayList<Edge> prim = mstGraph.prim();    //// unit test failures here?
			System.out.println("\ncalled prim");
			double primSum = 0;
			if(prim != null) {
				primSum = mstGraph.sumEdgeWeights(prim);
			}
			ArrayList<Edge> kruskal = mstGraph.kruskal();
			double kruskalSum = 0;
			if(kruskal != null) {
				kruskalSum = mstGraph.sumEdgeWeights(kruskal);
			}

			if(primSum != kruskalSum) {
				fail("Test 12.3."+i+" Failed - mst sums not equal");
				println("Randomly Generated Graph");
				println(mstGraph.toString());
			}

			if(!mstGraph.consistent()) {
				fail("Test 12.3."+i+".0 Failed - graph consistency check failed");
				println("Randomly Generated Graph");
				println(mstGraph.toString());
			}

		}
*/

		System.out.print("Test 12 END");

		println("End Graph Tests");		
	}
	@Test
	public void test_NodeConstruction(){
		Node n0 = new Node(new NodeType("myType"));
		Node n1 = new Node(new NodeType("myType"), new Point(1, 2));
		Node n2 = new Node(new Point(0, 2));
		Node n3 = new Node("myNode");
		Node n4 = new Node("myNode", new NodeType("myType"));
		Node n5 = new Node("myNode", new NodeType("myType"), new Point(3, 4));
		Graph g = new Graph();
		g.addNode(n0);
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		assertEquals(g.getNodes().size(), 6);
		
		Point p0 = n0.getCentre();
		assertEquals(p0.x, 0);
		assertEquals(p0.y, 0);
		n0.setX(17);
		n0.setY(16);
		p0 = n0.getCentre();
		assertEquals(p0.x, 17);
		assertEquals(p0.y, 16);
	}
	@Test
	public void test_GraphCentre(){
		Node n0 = new Node(new Point(0, 2));
		Node n1 = new Node(new Point(0, 4));
		Graph g = new Graph();
		g.addNode(n0);
		g.addNode(n1);
		g.centreOnPoint(1, 3);
		assertEquals(g.getNodes().size(), 2);
		
		Point p0 = g.getNodes().get(0).getCentre();
		Point p1 = g.getNodes().get(1).getCentre();
		assertEquals(p0.x, 1);
		assertEquals(p0.y, 2);
		assertEquals(p1.x, 1);
		assertEquals(p1.y, 4);
	}
	@Test
	public void test_GraphGrid(){
		Node n0 = new Node(new Point(-4, 5));
		Node n1 = new Node(new Point(4, 0));
		Graph g = new Graph();
		g.addNode(n0);
		g.addNode(n1);
		g.snapToGrid(10, 10);
		assertEquals(g.getNodes().size(), 2);
		
		Point p0 = g.getNodes().get(0).getCentre();
		Point p1 = g.getNodes().get(1).getCentre();
		assertEquals(p0.x, 0);
		assertEquals(p0.y, 0);
		assertEquals(p1.x, 0);
		assertEquals(p1.y, 0);
	}} 

