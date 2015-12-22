package org.eulerdiagrams.vennom.apCircles;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestAbstractDiagram {

	
    @Test
    // different ways to build equivalent abstract diagrams
    public void test_construct1(){
    	AbstractDiagram ad1 = new AbstractDiagram("a b ");

    	assertEquals(ad1.getContours().size(), 2);
    	assertEquals(ad1.getContours().contains("a"), true);
    	assertEquals(ad1.getContours().contains("c"), false);
    	assertEquals(ad1.getZoneList().size(), 2); //nb no outside zone
    	
    	AbstractDiagram ad2 = new AbstractDiagram(ad1);
    	ArrayList<String> labels = new ArrayList<String>();
    	labels.add("a");
    	labels.add("b");
    	AbstractDiagram ad3 = new AbstractDiagram(labels);
    	AbstractDiagram ad4 = ad1.clone();
    	
    	assertEquals(ad1.compareTo(ad2), 0);
    	assertEquals(ad1.compareTo(ad3), 0);
    	assertEquals(ad1.compareTo(ad4), 0);
	}
    public void test_construct2(){
    	AbstractDiagram ad1 = new AbstractDiagram("a b ");
    	ad1.addZone("");

    	assertEquals(ad1.getContours().size(), 2);
    	assertEquals(ad1.getContours().contains("a"), true);
    	assertEquals(ad1.getContours().contains("c"), false);
    	assertEquals(ad1.getZoneList().size(), 3); //nb no outside zone
    	
    	AbstractDiagram ad2 = new AbstractDiagram(ad1);
    	ArrayList<String> labels = new ArrayList<String>();
    	labels.add("a");
    	labels.add("b");
    	labels.add("");
    	AbstractDiagram ad3 = new AbstractDiagram(labels);
    	AbstractDiagram ad4 = ad1.clone();
    	
    	assertEquals(ad1.compareTo(ad2), 0);
    	assertEquals(ad1.compareTo(ad3), 0);
    	assertEquals(ad1.compareTo(ad4), 0);
	}
    @Test
    public void test_addZone(){
    	AbstractDiagram ad1 = new AbstractDiagram("a b ab");
    	AbstractDiagram ad2 = new AbstractDiagram("a b");
    	assertNotEquals(ad1.compareTo(ad2), 0);
    	
    	ad2.addZone("ab");

    	assertEquals(ad1.compareTo(ad2), 0);
    }

    @Test
    public void test_removeCurve(){
    	AbstractDiagram ad1 = new AbstractDiagram("a b ab");
    	AbstractDiagram ad2 = new AbstractDiagram("a");
    	assertNotEquals(ad1.compareTo(ad2), 0);
    	
    	AbstractDiagram ad3 = ad1.removeCurve("b");

    	assertNotEquals(ad1.compareTo(ad2), 0);
    	assertEquals(ad3.compareTo(ad2), 0);
    }
    @Test
    public void test_isOutsideZone(){
    	assertEquals(AbstractDiagram.isOutsideZone("a"), false);
    	assertEquals(AbstractDiagram.isOutsideZone("" ), true);
    	assertEquals(AbstractDiagram.isOutsideZone("0"), true);
    	assertEquals(AbstractDiagram.isOutsideZone("O"), true);
    }
    @Test
    public void test_findContourList(){
    	ArrayList<String> conts;
    	ArrayList<String> expectedConts = new ArrayList<String>();

    	conts = AbstractDiagram.findContourList("");
    	assertEquals(conts.equals(expectedConts), true);

    	expectedConts.add("a");

    	conts = AbstractDiagram.findContourList("a");
    	assertEquals(conts.equals(expectedConts), true);

    	expectedConts.add("b");

    	conts = AbstractDiagram.findContourList("ab");
    	assertEquals(conts.equals(expectedConts), true);
    }
    @Test
    public void test_findContoursFromZones(){
    	ArrayList<String> conts;
    	ArrayList<String> zones = new ArrayList<String>();    	
    	ArrayList<String> expectedConts = new ArrayList<String>();

    	conts = AbstractDiagram.findContoursFromZones(zones);
    	assertEquals(conts.equals(expectedConts), true);
    	
    	zones.add("ab");
    	expectedConts.add("a");
    	expectedConts.add("b");

    	conts = AbstractDiagram.findContoursFromZones(zones);
    	assertEquals(conts.equals(expectedConts), true);

    	zones.add("a");

    	conts = AbstractDiagram.findContoursFromZones(zones);
    	assertEquals(conts.equals(expectedConts), true);
    }
    
    @Test
    public void test_orderZone(){
    	String result;

    	String expectedResult = "ab";
    	result = AbstractDiagram.orderZone("ab");
    	assertEquals(result.equals(expectedResult), true);
    	
    	result = AbstractDiagram.orderZone("ba");
    	assertEquals(result.equals(expectedResult), true);

    	expectedResult = "a";
    	result = AbstractDiagram.orderZone("a");
    	assertEquals(result.equals(expectedResult), true);

    	result = AbstractDiagram.orderZone("aa");
    	assertEquals(result == null, true);

    	result = AbstractDiagram.orderZone("aba");
    	assertEquals(result == null, true);
    }
    @Test
    public void test_randomDiagramFactory(){
    	AbstractDiagram ad1 = AbstractDiagram.randomDiagramFactory(0);
    	AbstractDiagram ad2 = new AbstractDiagram("");
    	ad2.addZone(""); 

    	assertEquals(ad1.compareTo(ad2), 0);

    	ad1 = AbstractDiagram.randomDiagramFactory(1);
    	//assertEquals(ad1.getZoneList().size(), 1); // this will randomly fail!
    	assertEquals(ad1.getZoneList().size() < 3, true); 

    	ad1 = AbstractDiagram.randomDiagramFactory(2);
    	//assertEquals(ad1.getZoneList().size(), 1); // this will randomly fail!
    	assertEquals(ad1.getZoneList().size() < 5, true); 

    	boolean includeNull = true;
    	double chancesOfInclude = 0.5;
    	long seed = 12345;
    	
    	ad1 = AbstractDiagram.randomDiagramFactory(2, includeNull, chancesOfInclude, seed);
    	assertEquals(ad1.getZoneList().size(), 2); 
    
    	includeNull = false;

    	ad1 = AbstractDiagram.randomDiagramFactory(2, includeNull, chancesOfInclude, seed);
    	assertEquals(ad1.getZoneList().size(), 1); 
    
    	chancesOfInclude = 0.0;// guarantees zones will not be included

    	ad1 = AbstractDiagram.randomDiagramFactory(2, includeNull, chancesOfInclude, seed);
    	assertEquals(ad1.getZoneList().size(), 0); 

    	chancesOfInclude = 1.00001; // greater than 1 guarantees zone

    	ad1 = AbstractDiagram.randomDiagramFactory(2, includeNull, chancesOfInclude, seed);
    	assertEquals(ad1.getZoneList().size(), 3); 
    }
    @Test
    public void test_sortZoneList(){
    	ArrayList<String> zones = new ArrayList<String>();
    	zones.add("z");
    	zones.add("ab");
    	zones.add("a");
    	ArrayList<String> expected_sorted_zones = new ArrayList<String>();
    	expected_sorted_zones.add("a");
    	expected_sorted_zones.add("z"); // sort zones with fewer contours earlier
    	expected_sorted_zones.add("ab");

    	// not in same order
    	assertNotEquals(zones, expected_sorted_zones);
    	
    	// sort
    	assertEquals(AbstractDiagram.sortZoneList(zones), true);
    	
    	// now in same order
    	assertEquals(zones, expected_sorted_zones);

    	zones.add("a");

    	// returns fals for duplicates
    	assertEquals(AbstractDiagram.sortZoneList(zones), false); // because of the duplicate
    	
    	// sorts but does not remove duplicate
    	assertNotEquals(zones, expected_sorted_zones);

    	expected_sorted_zones.add(0, "a");
    	assertEquals(zones, expected_sorted_zones);
    }
}
    
 	//	AbstractDiagram ad1,ad2;
	/*	ad1 = new AbstractDiagram("a b ");
		ad2 = new AbstractDiagram("c");
		System.out.println(ad1.isomorphicTo(ad2));
		System.out.println(failOnZoneSizeCount);*/
 		
 		
 		//ad1 = new AbstractDiagram("a b ab abc ac d bd e ae");
 		//ad1 = new AbstractDiagram("a b ab abc bc bcd bd");
 		//AbstractDiagram ad1 = new AbstractDiagram("a b ab abc bc bd bcd be bce abcf abf");
 		//ad1 = new AbstractDiagram("a b ba");
 		//ad1 = new AbstractDiagram("a b ab bc abc bcd abcd bcde bce bf f g bg bh bgh bghi bhi bgj gj");
 		
 		//ad1=new AbstractDiagram("0 a b c d f ab ac ad af"); 
 		//ad1.checkInductivePiercingDiagram(); 		
