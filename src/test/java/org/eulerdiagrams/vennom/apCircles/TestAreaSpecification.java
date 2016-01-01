package org.eulerdiagrams.vennom.apCircles;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAreaSpecification {
	@Test
	public void test_construct(){
		AreaSpecification as = new AreaSpecification("A 10");
		
		double area = as.getZoneArea("A");
		assertEquals(area-10.0 == 0.0, true);
		
	}
}
