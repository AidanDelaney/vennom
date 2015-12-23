package org.eulerdiagrams.vennom.apCircles.enumerate;

import java.util.*;

import org.eulerdiagrams.vennom.apCircles.*;
import org.eulerdiagrams.vennom.apCircles.comparators.*;

public class Enumerate {

	/**
	 * Takes a combination number, which should be seen as a binary,
	 * a 1 or 0 indicating whether the zone in the zone list is in this diagram.
	 */
	public static AbstractDiagram findAbstractDiagram(long combination, ArrayList<String> zones) {
		String diagramString = "";
		long current = combination;
		for(int i = 0; i <= zones.size(); i++) {
			if(current%2 == 1) {
				diagramString += zones.get(i)+" ";
			}
			current = current/2;
		}
		AbstractDiagram ret = new AbstractDiagram(diagramString);
		return ret;
	}

	/**
	 * Returns a list of strings containing all the zone combinations for
	 * the contours, contours labelled with a single letter starting at "a".
	 * Does not return the outside contour.
	 */
	public static ArrayList<String> findAllZones(int numberOfContours) {
/*		
		if(contours == 1) {
			return new ArrayList<String>(Arrays.asList(ZONES1));
		}
		if(contours == 2) {
			return new ArrayList<String>(Arrays.asList(ZONES2));
		}
		if(contours == 3) {
			return new ArrayList<String>(Arrays.asList(ZONES3));
		}
		if(contours == 4) {
			return new ArrayList<String>(Arrays.asList(ZONES4));
		}
*/
		ArrayList<String> zoneList = new ArrayList<String>();
		
		double numberOfZones = (int)Math.pow(2,numberOfContours)-1;
		for(int zoneNumber = 1; zoneNumber <= numberOfZones; zoneNumber++) {
			String zone = findZone(zoneNumber);
			zoneList.add(zone);

		}
		ZoneStringComparator zComp = new ZoneStringComparator();
		Collections.sort(zoneList,zComp);

		return zoneList;
	}

	
	/**
	 * Takes a zone number, which should be seen as a binary,
	 * indicating whether each contour is in the zone.
	 * Contours are assumed to be labelled from "a" onwards.
	 */
	private static String findZone(int zoneNumber) {
		String zoneString = "";
		int current = zoneNumber;
		int i = 0;
		while(current != 0) {
			if(current%2 == 1) {
				char contourChar = (char)((int)'a'+i);
				zoneString += contourChar;
			}
			current = current/2;
			i++;
		}
		return zoneString;
	}

}


