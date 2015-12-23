package org.eulerdiagrams.vennom.apCircles;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.eulerdiagrams.vennom.graph.Graph;
import org.junit.Test;

public class TestUtil {

	@Test
	public void test_findErrors(){
    	AbstractDiagram ad = new AbstractDiagram("a b ");
		AreaSpecification as = new AreaSpecification(ad);
		as.setZoneArea("a", 200);
		as.setZoneArea("b", 200);
		
		VennomLayout vl = new VennomLayout(VennomLayout.FORCE_LAYOUT, as);
		Graph g = vl.layout();
				
		HashMap<String,Double> errorMap = new HashMap<String,Double>();
		ArrayList<String> missingZones = new ArrayList<String>();
		ArrayList<String> additionalZones = new ArrayList<String>();
	
		Util.findErrors(as, g, errorMap, missingZones, additionalZones);
		
		assertTrue(errorMap.size() == 2);
		assertTrue(errorMap.get("a") == 0.0);
		assertTrue(errorMap.get("b") == 0.0);
		assertTrue(missingZones.size() == 0);
		assertTrue(additionalZones.size() == 0);
	}
}
