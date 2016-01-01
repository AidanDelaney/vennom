package org.eulerdiagrams.vennom.apCircles;

import java.awt.Polygon;
import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;


public class TestConcreteContour {
	@Test
	public void test_001(){
		ArrayList<ConcreteContour> ccs;
		Polygon p1,p2,p3;
		ConcreteContour c1,c2,c3;
		String zones;
		
		ccs = new ArrayList<ConcreteContour>();
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		assertEquals(zones.length() == 1, true);
		//System.out.println("Should be '0': "+ zones);	//TODO better assertion	
		
		p1 = new Polygon();
		p1.addPoint(0,0);
		p1.addPoint(0,100);
		p1.addPoint(100,0);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		assertEquals(zones.length(), 3); // TODO is this really the expectation? Or a bug?
		//System.out.println("Should be '0 a': "+ zones);	//TODO better assertion	

		p2 = new Polygon();
		p2.addPoint(200,200);
		p2.addPoint(200,300);
		p2.addPoint(300,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		ccs.add(c2);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		assertEquals(zones.length(), 5); // TODO is this really the expectation? Or a bug?
		//System.out.println("Should be '0 a b': "+ zones);		//TODO better assertion
		
		p3 = new Polygon();
		p3.addPoint(20,20);
		p3.addPoint(230,230);
		p3.addPoint(100,200);
		ccs = new ArrayList<ConcreteContour>();
		c1 = new ConcreteContour("a",p1);
		c2 = new ConcreteContour("b",p2);
		c3 = new ConcreteContour("c",p3);
		ccs.add(c2);
		ccs.add(c3);
		ccs.add(c1);
		zones = ConcreteContour.generateAbstractDiagramFromList(ccs);
		assertEquals(zones.length(), 13);// TODO is this really the expectation? Or a bug?
		//System.out.println("Should be '0 a b c ac bc': "+ zones);	//TODO better assertion		
		
	}
}
