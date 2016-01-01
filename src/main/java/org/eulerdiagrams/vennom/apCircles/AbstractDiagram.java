package org.eulerdiagrams.vennom.apCircles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import org.eulerdiagrams.vennom.apCircles.comparators.ContourValueMapComparator;
import org.eulerdiagrams.vennom.apCircles.comparators.ZoneStringComparator;
import org.eulerdiagrams.vennom.apCircles.enumerate.Enumerate;
import org.eulerdiagrams.vennom.apCircles.enumerate.IsomorphismInvariants;


/**
 * 
 * @author Peter Rodgers
 * @author Leishi Zhang
 */

public class AbstractDiagram implements Comparable<AbstractDiagram>, Cloneable {


	protected ArrayList<String> zoneList = null;

	public static Random random = new Random(System.currentTimeMillis());
	

	/** measures the usage of the brute force part of the isomorphism test */
	private long bruteForceCount = 0;	

	private HashMap<String,String> contourLabelMap = new HashMap<String,String>();

private static long timer1 = 0;
private static long timer2 = 0;
private static long timer3 = 0;
private static long timer4 = 0;
	
	public long getBruteForceCount() {return bruteForceCount;}
	
	
	/**
	 * Takes a space delimited string and produces the abstract graph. Note
	 * that "0" is a special character that gives the empty zone.
	 */
	public AbstractDiagram(String zoneListString) {

		zoneList = findZoneList(zoneListString);
	}
	
	/**
	 * Takes a list of zones and produces the abstract graph. Note
	 * that "0" is a special character that gives the empty zone.
	 */
	public AbstractDiagram(ArrayList<String> zones) {

		zoneList = constructZoneList(zones);
	}
	
	
	/**
	 * Produces a duplicate of the given abstract diagram by copying the zonelist.
	 */
	public AbstractDiagram(AbstractDiagram ad) {
//		diagramString = ad.getDiagramString();
		zoneList = new ArrayList<String>(ad.zoneList);
	}	
	
	
	public ArrayList<String> getZoneList() {return zoneList;}
	
	public void setZoneList(ArrayList<String> zoneList) {this.zoneList = zoneList;}
	
	private static AbstractDiagram VennFactory(int numberOfContours) {
		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		StringBuffer adZones = new StringBuffer();
		adZones.append("0");
		for(String z: zones) {
			adZones.append(" "+z);
		}
		AbstractDiagram ad = new AbstractDiagram(adZones.toString());
		//ad.addZone("");
		return ad;
	}
	
	
	public static AbstractDiagram randomDiagramFactory(int numberOfContours) {
		return randomDiagramFactory(numberOfContours, true,0.5);
	}
	
	
	public static AbstractDiagram randomDiagramFactory(int numberOfContours, boolean includeNull, double chanceOfZoneAddition, long seed) {

		random = new Random(seed);
		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		StringBuffer adZones = new StringBuffer();
		for(String z: zones) {
			if(random.nextDouble() < chanceOfZoneAddition) {
				adZones.append(" "+z);
			}
		}
		
		AbstractDiagram ad = new AbstractDiagram(adZones.toString());
		if(includeNull)
			ad.addZone("");
		return ad;
	}
	
	private static AbstractDiagram randomDiagramFactory(int numberOfContours, boolean includeNull, double chanceOfZoneAddition) {

		long seed = System.currentTimeMillis();
		AbstractDiagram ad = randomDiagramFactory(numberOfContours, includeNull, chanceOfZoneAddition, seed);
		return ad;
	}
	
	
	/**
	 * Takes a space delimited string and produces a sorted arraylist
	 * of strings. "0" is treated as the empty zone, "". Sorts each string
	 * lexographically. Returns null if a contour is specified more than once
	 * in any single zone or if there are duplicate zones.
	 */
	private static ArrayList<String> findZoneList(String diagramString) {
		ArrayList<String> ret = new ArrayList<String>();
		String[] splitString = diagramString.split(" ");
		
		for(int i = 0; i< splitString.length; i++) {
			String zoneString = splitString[i];
			if(zoneString.length() > 0) {
				if(AbstractDiagram.isOutsideZone(zoneString)) { // deal with "0" being the empty set
					zoneString = "";
				}
				String orderedZone = AbstractDiagram.orderZone(zoneString);
				if(orderedZone == null) {
					System.out.println("findZoneList("+diagramString+"): duplicate contour in zone "+zoneString);
					return null;
				}
				ret.add(orderedZone);
			}
		}
		
		// sort zones
		if(!sortZoneList(ret)) {
			System.out.println("findZoneList("+diagramString+"): duplicate zone found");
			return null;
		}
				
		return ret;
	}
	
	
	/**
	 * return a sorted list. Sort each element lexographically, then sort the list of strings.
	 */
	private ArrayList<String> constructZoneList(ArrayList<String> zones) {
		
		ArrayList<String> ret = new ArrayList<String>(zones.size());
		
		for(String zoneString : zones) {
			if(zoneString.equals("0")) { // deal with "0" being the empty set
				zoneString = "";
			}
			String orderedZone = AbstractDiagram.orderZone(zoneString);
			if(orderedZone == null) {
				System.out.println("construcZoneList("+zones+"): duplicate contour in zone "+zoneString);
				return null;
			}
			ret.add(orderedZone);
		}
		
		// sort zones
		if(!sortZoneList(ret)) {
			System.out.println("constructZoneList("+zones+"): duplicate zone found");
			return null;
		}
				
		return ret;
	}
	
	
	/** 
	 * Orders the zone string and detects duplicates. Returns null on duplicate.
	 */
	public static String orderZone(String zoneString) {
		
		ArrayList<String> splitZoneList = findContourList(zoneString);
		Collections.sort(splitZoneList);
		// check for duplicates
		if(hasDuplicatesInSortedList(splitZoneList)) {
			return null;
		}		
		// rebuild the string
		StringBuffer sortedZoneStringBuffer = new StringBuffer();
		for(String s: splitZoneList) {
			sortedZoneStringBuffer.append(s);
		}

		return sortedZoneStringBuffer.toString();
	}	
	
	/**
	 * Gets the contours in the diagram. This returns a sorted list.
	 */
	public ArrayList<String> getContours() {
		return findContoursFromZones(zoneList);
	}

	
	/**
	 * Takes a list of zones and returns a sorted list of contours
	 * appearing in the zones.
	 */
	public static ArrayList<String> findContoursFromZones(ArrayList<String> zones) {
		ArrayList<String> contours = new ArrayList<String>(); 
		for(String zone: zones) {
			ArrayList<String> zoneContours = AbstractDiagram.findContourList(zone);
			contours.addAll(zoneContours);
		}
		Collections.sort(contours);
		removeDuplicatesFromSortedList(contours);
		return contours;
	}
	
	
	/** Takes a string and returns the list of characters in the string */
	public static ArrayList<String> findContourList(String zoneLabel) {
		String[] zones = zoneLabel.split("");
		ArrayList<String> zoneList = new ArrayList<String>(Arrays.asList(zones));
		zoneList.remove(""); // split adds a blank entry in index 0
		return zoneList;
	}


	/**
	 * Find the groups of intersecting contours. No concurrency considered
	 */
	public ArrayList<String> findIntersectionGroups() {
		
		// first get the pairs of intersecting contours
		ArrayList<String> intersectingPairs = new ArrayList<String>();
		for(String zone : zoneList) {
			ArrayList<String> contours = AbstractDiagram.findContourList(zone);
			for(int i = 0; i< contours.size(); i++) {
				String ci = contours.get(i);
				for(int j = i+1; j< contours.size(); j++) {
					String cj = contours.get(j);
					
					if(!intersectingPairs.contains(ci+cj) && !intersectingPairs.contains(cj+ci)) {
						intersectingPairs.add(ci+cj);
					}
				}
			}
		}

		
		ArrayList<StringBuffer> intersectionGroups = new ArrayList<StringBuffer>();
		// put the single element zones into intersectionGroups
		for(String zone : zoneList) {
			if(zone.length() == 1) {
				StringBuffer newGroup = new StringBuffer(zone);
				intersectionGroups.add(newGroup);
			}
		}
		
		// now either merge groups, add contours to groups or add new groups
		for(String pair : intersectingPairs) {
			String c1 = Character.toString(pair.charAt(0));
			String c2 = Character.toString(pair.charAt(1));
			
			StringBuffer c1Group = null;
			StringBuffer c2Group = null;
			for(StringBuffer group : intersectionGroups) {
				String groupString = group.toString();
				if(groupString.contains(c1)) {
					c1Group = group;
				}
				if(groupString.contains(c2)) {
					c2Group = group;
				}
			}
			
			if(contourContainment(c1, c2) || contourContainment(c2, c1)) {
				// dont merge two contours if one contains the other
				if(c1Group == null) {
					StringBuffer newGroup = new StringBuffer(c1);
					intersectionGroups.add(newGroup);
				}
				if(c2Group == null) {
					StringBuffer newGroup = new StringBuffer(c2);
					intersectionGroups.add(newGroup);
				}
			} else {
			
				if(c1Group == null && c2Group == null) {
					// if neither contour is in a group, create a new group
					StringBuffer newGroup = new StringBuffer(pair);
					intersectionGroups.add(newGroup);
				}
				if(c1Group != null && c2Group == null) {
					// if one contour is already in a group, add the other
					c1Group.append(c2);
				}
				if(c1Group == null && c2Group != null) {
					// if one contour is already in a group, add the other
					c2Group.append(c1);
				}
				if(c1Group != null && c2Group != null && c1Group != c2Group) {
					// if both contours are already in a group, join up the two groups
					intersectionGroups.remove(c2Group);
					c1Group.append(c2Group);
				}
			}
		}


		ArrayList<String> ret = new ArrayList<String>();
		for(StringBuffer sb : intersectionGroups) {
			String group = sb.toString();
			group = AbstractDiagram.orderZone(group);
			ret.add(group);
		}
		
		sortZoneList(ret);
		
		return ret;
	}



	/**
	 * See if c2 is entirely inside c1.
	 * c1 and c2 must be in the diagram.
	 * Concurrent contours contain each other.
	 */
	public boolean contourContainment(String c1, String c2) {
		for(String zone : zoneList) {
			if(!zone.contains(c1) && zone.contains(c2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * See if the circle is entirely inside the zone.
	 * z and c must be in the diagram.
	 */
	public boolean zoneContainment(String z, String c) {
		boolean foundInZ = false;
		for(String zone : zoneList) {
			if(zone.equals(z)) {
				continue;
			}
			if(zone.contains(c)) {
				boolean isSubZone = true;
				for(String subC : AbstractDiagram.findContourList(z)) {
					if(!zone.contains(subC)) {
						isSubZone = false;
						break;
					}
				}
				if(!isSubZone) {
					return false;
				} else {
					foundInZ = true;
				}
			}
		}
		return foundInZ;
	}

		
	/**
	 * See if c1 shares a zone with c2.
	 * c1 and c2 must be in the diagram.
	 */
	public boolean contoursIntersect(String c1, String c2) {
		for(String zone : zoneList) {
			if(zone.contains(c1) && zone.contains(c2)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Get the contours z1 shares with z2.
	 */
	public static String zoneIntersection(String z1, String z2) {
		ArrayList<String> intersection = new ArrayList<String>();
		
		ArrayList<String> c2List = AbstractDiagram.findContourList(z2);
		
		for(String c1 : AbstractDiagram.findContourList(z1)) {
			if(c2List.contains(c1)) {
				intersection.add(c1);
			}
		}
		
		Collections.sort(intersection);
		
		String ret = "";
		for(String contour : intersection) {
			ret += contour;
		}
		
		return ret;
	}


	/**
	 * Get the contours in either z1 or z2.
	 */
	public static String zoneUnion(String z1, String z2) {
		
		ArrayList<String> union = AbstractDiagram.findContourList(z2);
		
		for(String c1 : AbstractDiagram.findContourList(z1)) {
			if(!union.contains(c1)) {
				union.add(c1);
			}
		}
		
		Collections.sort(union);
		
		String ret = "";
		for(String contour : union) {
			ret += contour;
		}
		
		return ret;
	}


	/**
	 * Get the contours in z1 that are not in z2.
	 */
	public static String zoneMinus(String z1, String z2) {
		
		ArrayList<String> c1List = AbstractDiagram.findContourList(z1);
		
		for(String c2 : AbstractDiagram.findContourList(z2)) {
			if(c1List.contains(c2)) {
				c1List.remove(c2);
			}
		}
		
		Collections.sort(c1List);
		
		String ret = "";
		for(String contour : c1List) {
			ret += contour;
		}
		
		return ret;
	}


	
	/**
	 * Removes duplicates from the sorted List, returns true
	 * if duplicates found, false if not.
	 */
	private static boolean removeDuplicatesFromSortedList(List list) {
		Object last = null;
		boolean found = false;
		ListIterator li = list.listIterator();
		while(li.hasNext()) {
			Object o = (Object)li.next();
			if(last == null) {
				// first iteration
				last = o;
			} else {
				Object current = o;
				if(last.equals(current)) {
					li.remove();
					found = true;
				}
				last = current;
			}
		}
		return found;
	}
	
	/**
	 * Finds duplicates in a the sorted List, returns true
	 * if duplicates found, false if not.
	 */
	static boolean hasDuplicatesInSortedList(List list) {
		Object last = null;
		ListIterator li = list.listIterator();
		while(li.hasNext()) {
			Object o = li.next();
			if(last == null) {
				// first iteration
				last = o;
			} else {
				Object current = o;
				if(last.equals(current)) {
					return true;
				}
				last = current;
			}
		}
		return false;
	}
	
	/**
	 * Sorts the list of strings and checks for duplicates. Returns
	 * true if there are no duplicates, false if there are. The
	 * list is still sorted on a false return.
	 */
	public static boolean sortZoneList(ArrayList<String> zoneList) {
		ZoneStringComparator zComp = new ZoneStringComparator();
		Collections.sort(zoneList,zComp);
		
		if(AbstractDiagram.hasDuplicatesInSortedList(zoneList)) {
			return false;
		}

		return true;

	}
	
	
	/**
	 * Changes the zone strings according to the map. A check
	 * is made to see if the result will lead to ambiguity
	 * so that a->b in ab ac abc will result in false being returned.
	 * a->b, b->a is dealt with.
	 */
	public boolean remapContourStrings(HashMap<String,String> map) {

		ArrayList<String> newZoneList = new ArrayList<String>();
		for(String z:zoneList) {
			
			// we need to do it like this because of maps
			// like a->b, b->a
			
			// first split the zone and convert it into a list that can be changed
			ArrayList<String> splitZoneList = AbstractDiagram.findContourList(z);

			String newZone = "";
			// find occurences of keys and replace them in the new zone
			for(String contourString: splitZoneList) {
				if(map.containsKey(contourString)) {
					newZone += map.get(contourString);
				} else {
					newZone += contourString;
				}
			}
			
			// newZone might need to be reordered
			newZone = AbstractDiagram.orderZone(newZone);
			if(newZone == null) {
				System.out.println("remapContourStrings(): duplicate contour in zone "+z+" new zone "+newZone+" caused by map");
				return false;
			}
			

			// add to the new zone list
			newZoneList.add(newZone);
		}

		// sort zones
		if(!sortZoneList(newZoneList)) {
			System.out.println("remapContourStrings(): duplicate zone found. old zone list is "+zoneList+" new zone list is "+newZoneList);
			return false;
		}
		
		zoneList = newZoneList;
		
		return true;
	}
	
	
	public String toString() {
		String ret = "";
		Iterator<String> i = zoneList.iterator();
		while(i.hasNext()) {
			String zone = i.next();
			if(!AbstractDiagram.isOutsideZone(zone)) {
				ret += zone;
			}
			if(i.hasNext()) {
				ret += " ";
			}
		}
		return ret;

	}

	public static boolean isOutsideZone(String zone) {
		if(zone.equals("")  || zone.equals("0") || zone.equals("O")) {
			return true;
		}
		return false;
	}
	
	/**
	 * The zone labels must be equal, so its not
	 * an isomorphism test. E.g. "a b ab" is not equal to "x z xz"
	 */
	public int compareTo(AbstractDiagram ad) {

		ArrayList<String> zoneList1 = zoneList;
		ArrayList<String> zoneList2 = ad.zoneList;

		// the diagram with the most zones is greatest (a b c is greater than a b)
		if(zoneList1.size() > zoneList2.size()) {
			return 1;
		}
		if(zoneList1.size() < zoneList2.size()) {
			return -1;
		}
		
		Iterator<String> zi1 = zoneList1.iterator();
		Iterator<String> zi2 = zoneList2.iterator();
		while(zi1.hasNext()) {
			String z1 = zi1.next();
			String z2 = zi2.next();
			
			// the diagram with the most outer zones is greatest (zone a is greater than ab)
			if(z1.length() < z2.length()) {
				return 1;
			}
			if(z1.length() > z2.length()) {
				return -1;
			}
			// first lexographic difference
			int lexCompare = z1.compareTo(z2);
			if(lexCompare != 0) {
				return lexCompare;
			}
		}
		
		return 0;
	}
	
	protected static long longestSuccessTime = 0;
	protected static long longestFailTime = 0;
	protected static int longestSuccessBruteForce = 0;
	protected static int longestFailBruteForce = 0;
	public static int failOnZoneSizeCount = 0;
	public static int failOnContourSizeCount = 0;
	public static int failOnZonePartitionCount = 0;
	public static int failOnContourPartitionCount = 0;
	public static int failOnContourZonePartitionCount = 0;
	public static int succeedAfterMapping = 0;
	public static int failAfterMapping = 0;
	
	
	public static void resetIsomorphismCounts() {
		longestSuccessTime = 0;
		longestFailTime = 0;

		longestSuccessBruteForce = 0;
		longestFailBruteForce = 0;

		failOnZoneSizeCount = 0;
		failOnContourSizeCount = 0;
		failOnZonePartitionCount = 0;
		failOnContourPartitionCount = 0;
		succeedAfterMapping = 0;
		failAfterMapping = 0;
	}
 	

	/**
	 * Test all contour combinations to test structural equality
	 */
	public boolean isomorphicTo(AbstractDiagram ad2) {
		
		//long startTime = System.currentTimeMillis();
		bruteForceCount = 0;

timer1 = 0;
timer2 = 0;
timer3 = 0;
timer4 = 0;
		
		AbstractDiagram ad1 = new AbstractDiagram(this);

long startTimer1 = System.currentTimeMillis();
		if(IsomorphismInvariants.zoneSizes(ad1, ad2)) {
			failOnZoneSizeCount++;
			return false;
		}
		
		if(IsomorphismInvariants.labelSizes(ad1, ad2)) {
			failOnContourSizeCount++;
			return false;
		}

		
		if(IsomorphismInvariants.zonePartition(ad1, ad2)) {
			failOnZonePartitionCount++;
timer1 = System.currentTimeMillis()-startTimer1;
			return false;
		}
		
		if(IsomorphismInvariants.labelPartition(ad1, ad2)) {
			failOnContourPartitionCount++;
timer1 = System.currentTimeMillis()-startTimer1;
			return false;
		}
timer1 = System.currentTimeMillis()-startTimer1;

long startTimer3 = System.currentTimeMillis();
		// switch to complements if there are a lot of zones
		double maxNumberOfZones = (int)Math.pow(2,ad1.getContours().size()-1)-1;
		if(ad1.getZoneList().size() > (2*maxNumberOfZones)/3) { // try complement when zone list is 2 3rds of max
			ad1 = ad1.complement();
			ad2 = ad2.complement();
		}
timer3 = System.currentTimeMillis()-startTimer3;

long startTimer2 = System.currentTimeMillis();

		// test the occurrences of contours in zones makes the diagrams equal
		// this groups contours by the number of occurrences in all zone sizes
		// so that for 0 a ab abc abd
		// results in a:[0,1,1,2], b:[0,0,1,1], c:[0,0,0,1], d:[0,0,0,1]
		// so that c and d are grouped
		HashMap<String,String> singleMaps = new HashMap<String,String>();
		ArrayList<Integer> listSizes = new ArrayList<Integer>();
		ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>> pairsForTesting = new ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>>();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps1 = ad1.findContourMaps();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps2 = ad2.findContourMaps();
		for(ArrayList<ContourZoneOccurrence> czos1: contourMaps1) {
			ContourZoneOccurrence czo1 = czos1.get(0);			
			boolean foundMatch = false;
			for(ArrayList<ContourZoneOccurrence> czos2: contourMaps2) {
				if(czos1.size() != czos2.size()) {
					continue;
				}
				ContourZoneOccurrence czo2 = czos2.get(0);
				if(czo1.compareLists(czo2) == 0) {
					foundMatch = true;
					if(czos1.size() == 1) {
						// if there is only one contour, just set the mapping
						singleMaps.put(czo2.getContour(), czo1.getContour());
					} else {
						ArrayList<ArrayList<ContourZoneOccurrence>> pair = new ArrayList<ArrayList<ContourZoneOccurrence>>();
						pair.add(czos1);
						pair.add(czos2);
						pairsForTesting.add(pair);
						listSizes.add(czos1.size());
					}

					break;
				}
			}
			if(!foundMatch) {
timer2 = System.currentTimeMillis()-startTimer2;
				failOnContourZonePartitionCount++;
				return false;
			}
			
		}
timer2 = System.currentTimeMillis()-startTimer2;
		

//for(ArrayList<ArrayList<ContourZoneOccurrence>> pair : pairsForTesting) {
//System.out.println(pair.get(0)+" -- "+pair.get(1));
//}
		
		// here there is no choice in the mapping
		if(pairsForTesting.size() == 0) {
			AbstractDiagram adCopy = new AbstractDiagram(ad2);
			adCopy.remapContourStrings(singleMaps);
			if(ad1.compareTo(adCopy) == 0) {

				succeedAfterMapping++;
				return true;
			}
			return false;
		}
		
		// now we need to try all the contour mappings to test
		// if any mapping make the diagrams equal.
		// Its only neccessary to test contour mappings between
		// contours which have the same occurrence information
		// for all zone sizes.
		
		// this tests all contour mappings in a brute force approach
		
		
		long startBruteForceTime = System.currentTimeMillis();
		
		ArrayList<GroupMap> combination = firstCombination(listSizes);
		boolean loop = true;
		while(loop) {
			
			bruteForceCount++;

			HashMap<String,String> mapping = new HashMap<String,String>(singleMaps);

			for(int i = 0; i < combination.size(); i++) {
				GroupMap gm = combination.get(i);
				int[] permutation = gm.getMapping();
				
				for (int j = 0; j < permutation.length; ++j) {
					ArrayList<ArrayList<ContourZoneOccurrence>> pair = pairsForTesting.get(i);
					ArrayList<ContourZoneOccurrence> list1 = pair.get(0);
					ArrayList<ContourZoneOccurrence> list2 = pair.get(1);
					ContourZoneOccurrence czo1 = list1.get(j);
					ContourZoneOccurrence czo2 = list2.get(permutation[j]);
					String contour1 = czo1.getContour();
					String contour2 = czo2.getContour();
					mapping.put(contour2,contour1);

				}

				
			}

			AbstractDiagram adCopy = new AbstractDiagram(ad2);
			adCopy.remapContourStrings(mapping);
			if(ad1.compareTo(adCopy) == 0) {

				succeedAfterMapping++;
				return true;
			}

			loop = nextCombination(combination);
		}				
		
		failAfterMapping++;

//System.out.println("FALSE because no mapping found");
		return false;
	}
	
	
	
	private static ArrayList<GroupMap> firstCombination(ArrayList<Integer> intList) {
		ArrayList<GroupMap> ret = new ArrayList<GroupMap>();
		for(Integer i : intList) {
			GroupMap gm = new GroupMap(i);
			ret.add(gm);
		}
		return ret;
	}
	

	/**
	 * Given a list of triples, where the first number is the quantity
	 * of combinations at that index, the second number is the current lhs
	 * index and the third number is the current rhs index, increment the
	 * combinations by one, or return false if at the end.
	 */
	private static boolean nextCombination(ArrayList<GroupMap> current) {
		
		if(current.size() == 0) {
			return false;
		}
		
		int index = 0;
		boolean overflow = true;
		while(overflow) {
			GroupMap gm = current.get(index);
			
			overflow = gm.nextMapping();
			
			if(overflow) {
				index++;
				if(index >= current.size()) {
					// last combination
					return false;
				}
			}
		}		
		return true;		
	}

	
	

	/** Group the contours in the zone list into those that cannot be disinguished */
	public ArrayList<ArrayList<ContourZoneOccurrence>> findContourMaps() {
		
		ArrayList<ArrayList<ContourZoneOccurrence>> ret = new ArrayList<ArrayList<ContourZoneOccurrence>>();
		
		ArrayList<String> zones = getZoneList();
		ArrayList<String> contours = findContoursFromZones(zones);
		
		String largestZone = "";
		HashMap<String,Integer> sizeMap = new HashMap<String,Integer>(contours.size()*10);
		for(String z : zones) {
			
			int zoneSize = z.length();
			
			ArrayList<String> splitZoneList = AbstractDiagram.findContourList(z);
			for(String c : splitZoneList) {
				String key = c+zoneSize; // key is the contour and size of zone
				Integer occurrences = sizeMap.get(key);
				if(occurrences == null) {
					occurrences = 1;
				} else {
					occurrences++;
				}
				sizeMap.put(key, occurrences);
			}
			
			// find the largest zone
			if(largestZone.length() < z.length()) {
				largestZone = z;
			}
		}

		// now build up the occurrences
		HashMap<String,ArrayList<Integer>> contourOccurrences = new HashMap<String, ArrayList<Integer>>(contours.size());
		for(String c : contours) {
			ArrayList<Integer> occurrenceList = new ArrayList<Integer>(largestZone.length()+1);
			occurrenceList.add(0); // no occurrences at size 0
			for(int i = 1; i <= largestZone.length(); i++) {
				String key = c+i;
				Integer occurrences = sizeMap.get(key);
				if(occurrences == null) {
					occurrenceList.add(0);
				} else {
					occurrenceList.add(occurrences);
				}
			}
			
			contourOccurrences.put(c,occurrenceList);
		}

		for(String contour : contourOccurrences.keySet()) {
			ArrayList<Integer> list = contourOccurrences.get(contour);
			ContourZoneOccurrence czo = new ContourZoneOccurrence(contour,list);
			boolean foundGroup = false;
			for(ArrayList<ContourZoneOccurrence> contourGroup : ret) {
				String tryContour = contourGroup.get(0).getContour();
				ArrayList<Integer> tryList = contourOccurrences.get(tryContour);
				
				if(compareOccurrences(czo.getOccurrences(),tryList)) {
					contourGroup.add(czo);
					foundGroup = true;
					break;
				}
			}

			if(!foundGroup) {
				ArrayList<ContourZoneOccurrence> newGroup = new ArrayList<ContourZoneOccurrence>();
				newGroup.add(czo);
				ret.add(newGroup);
			}
		}

		return ret;
	}
	
	
	
	
	/**
	 * Compare two lists that consist of contour occurrences at zone sizes
	 */
	private static boolean compareOccurrences(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		if(list1.size() != list2.size()) {
			return false;
		}
		Iterator<Integer> zi1 = list1.iterator();
		Iterator<Integer> zi2 = list2.iterator();
		while(zi1.hasNext()) {
			Integer i1 = zi1.next();
			Integer i2 = zi2.next();
			
			if(i1 != i2) {
				return false;
			}
		}
		
		return true;

	}
	
	
	
	/**
	 * Returns an ordered list of lists of contours, based on
	 * the ordering in the HashMap, with contours having the
	 * same order in the same list. The list must be sorted on the hashmap values.
	 */
	private static ArrayList<ArrayList<String>> groupContoursByMap(ArrayList<String> contours, HashMap<String,Integer> map) {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		if(contours.size() == 0) {
			return ret;
		}

		int addV = -1;
		boolean start = true;
		ArrayList<String> cList = new ArrayList<String>();
		for(String c: contours) {
			int v = map.get(c);
			if(start) {
				cList.add(c);
				addV = v;
				start = false;
				continue;
			}
			if(addV == v) {
				cList.add(c);
			} else {
				Collections.sort(cList);
				ret.add(cList);
				cList = new ArrayList<String>();
				cList.add(c);
				addV = v;
			}
		}
		Collections.sort(cList);
		ret.add(cList);
		
		return ret;
	}

	
	/**
	 * Find the complement diagram.
	 */
	private AbstractDiagram complement() {
		
		ArrayList<String> contours = getContours();
		ArrayList<String> zones = getZoneList();
		
		AbstractDiagram venn = VennFactory(contours.size());

		// we need to map venn to the contours in the diagram
		HashMap<String,String> mapping = new HashMap<String,String>(contours.size());
		Iterator<String> it1 = contours.iterator();
		Iterator<String> it2 = venn.getContours().iterator();
		while(it1.hasNext()) {
			// coiteration, because both should have the same number of contours
			String c1 = it1.next();
			String c2 = it2.next();
			mapping.put(c2,c1);
		}
		venn.remapContourStrings(mapping);
		
		ArrayList<String> retZones = new ArrayList<String>();
		
		for(String z : venn.getZoneList()) {
			if(!zones.contains(z)) {
				retZones.add(z);
			}
		}
		
		AbstractDiagram ret = new AbstractDiagram(retZones);
		
		return ret;
		
	}
		
		/**
		 * remap the contour labels so that they are in an order,
		 * with 'a' the highest value label in this diagram, 'b'
		 * the next and so on. The effect is that many
		 * isomorphic diagrams will have the same label ordering.
		 * The labels are compared by checking each zone size.
		 * A label occurring in a smaller size zone size gets
		 * higher priority. Where there is no difference in this
		 * then a label occurring adjacent to a higher ordered
		 * label gets highest priority.
		 */
		public void normalize() {
			
		ArrayList<String> contours = new ArrayList<String>(getContours());

		// set up the contours by the size of zones they occur in
		HashMap<Integer,ArrayList<String>> zoneSizeToContours = new HashMap<Integer,ArrayList<String>>();
		for(String zone: zoneList) {
			ArrayList<String> splitZoneList = AbstractDiagram.findContourList(zone);
			
			int zoneSize = splitZoneList.size();
			ArrayList<String> contourList = zoneSizeToContours.get(zoneSize);
			if(contourList == null) {
				contourList = new ArrayList<String>(splitZoneList);
				zoneSizeToContours.put(zoneSize,contourList);
			} else {
				contourList.addAll(splitZoneList);
				Collections.sort(contourList);
//				removeDuplicatesFromSortedList(contourList); // can't do this here because we need the duplicates later for a count
			}
		}
		

		// find the number of contour occurrences at each zone size
		// test if there are different numbers of contours at
		// different zone sizes.
		// We can iterate over just one of the sets and pull out
		// the contents of both on that key in safety, because previous
		// tests have detected if they have non existent (ie. different size)
		// zones.
		
		HashMap<String,Integer> contourValueMap = new HashMap<String,Integer>(); // stores a numeric value indicating the number of the contour based on occurences at each zone size
		Set<Integer> zoneSizeSet = zoneSizeToContours.keySet();
		for(Integer zoneSize: zoneSizeSet) {
			ArrayList<String> sizeContourList = zoneSizeToContours.get(zoneSize);
			Collections.sort(sizeContourList);
	
			int count = 0;
			String countContour = sizeContourList.get(0);
			Iterator<String> sclIt = sizeContourList.iterator();
			while(sclIt.hasNext()) {
				String contour = sclIt.next();
				if(!contour.equals(countContour)) { // if we have reached the end of a sequence of contours

					// this code is repeated after the loop
					int valueToAdd = count*(int)Math.pow(zoneList.size()-zoneSize,contours.size()-1);
					Integer currentContourValue = contourValueMap.get(countContour);
					if(currentContourValue == null) {
						contourValueMap.put(countContour,valueToAdd);
					} else {
						contourValueMap.put(countContour,currentContourValue+valueToAdd);
					}
					countContour = contour;
					count = 1;
				} else {
					count++;
				}
				
			}
			// repeat the command thats inside the loop for the last contour
			int valueToAdd = count*(int)Math.pow(zoneList.size()-zoneSize,contours.size()-1);
			Integer currentContourValue = contourValueMap.get(countContour);
			if(currentContourValue == null) {
				contourValueMap.put(countContour,valueToAdd);
			} else {
				contourValueMap.put(countContour,currentContourValue+valueToAdd);
			}
		}
		
		// sort the contours
		ContourValueMapComparator cvMapComp = new ContourValueMapComparator(contourValueMap);
		Collections.sort(contours,cvMapComp);
		Collections.reverse(contours);
		
		ArrayList<ArrayList<String>> contourOrderList = groupContoursByMap(contours,contourValueMap);

		// TODO
		// try iterating through the contourOrderList, attempting to disambiguate
		// members of each list by seeing if any neighbors at each size
		// have a different value. Keep propagating changes until no
		// more changes found.
		// HashMap<String,HashMap<Integer,ArrayList<String>>> neighbourMap = findContourNeighboursByZoneSize();
		

		contourLabelMap = new HashMap<String,String>();
		char currentContourChar = 'a';
		for(ArrayList<String> cs: contourOrderList) {
			ArrayList<String> equalContours = new ArrayList<String>(cs);
			while(equalContours.size() != 0) {
				String currentContour = Character.toString(currentContourChar);
				String originalContour = "";
				if(equalContours.contains(currentContour)) {
					originalContour = currentContour;
				} else {
					originalContour = equalContours.get(0);
				}
				contourLabelMap.put(originalContour,currentContour);
//System.out.println(originalContour+" "+currentContour);
				equalContours.remove(originalContour);
				currentContourChar++;
			}

		}
		remapContourStrings(contourLabelMap);

	}
	public	HashMap<String,String> getcontourLabelMap(){
		
		return contourLabelMap;
	}
	

	
	public boolean addZone(String z) {
		String zoneString = z;
		if(AbstractDiagram.isOutsideZone(zoneString)) { // deal with "0" being the empty set
			zoneString = "";
		}

		if(zoneList.contains(zoneString)) {
			return false;
		}
		String orderedZone = AbstractDiagram.orderZone(zoneString);
		zoneList.add(orderedZone);
		sortZoneList(zoneList);
		return true;
	}
	public boolean removeZone(String z) {
		String zoneString = z;
		if(AbstractDiagram.isOutsideZone(zoneString)) { // deal with "0" being the empty set
			zoneString = "";
		}
		
		int index = zoneList.indexOf(zoneString);
		if(index == -1) {
			return false;
		}
		zoneList.remove(index);
		return true;
	}
	
	public AbstractDiagram clone() {
		return new AbstractDiagram(this);
	}
	
	
	/** The number of times the contour occurs in the diagram */
	public int countZonesWithContour(String c) {
		int ret = 0;
		for(String z : getZoneList()) {
			if(z.contains(c)) {
				ret++;
			}
		}
		
		return ret;
	}
	
	
	public AbstractDiagram removeCurve(String s){
		ArrayList<String> temp = new ArrayList<String>();
		for(String s1: zoneList){
			if(!s1.contains(s)){
				temp.add(s1);
			}
		}
		return new AbstractDiagram(temp);		
	}
	
	
	/**
	 * Finds pairs of concurrent contours (3 concurrent contours forms 3 pairwise instances).
	 * Returned as pairs of letters.
	 */
	public ArrayList<String> findConcurrentContours() {
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<String> contours = getContours();
		for(int i = 0; i < contours.size(); i++) {
			for(int j = i+1; j < contours.size(); j++) {
				String c1 = contours.get(i);
				String c2 = contours.get(j);
				if(concurrent(c1,c2)) {
					ret.add(c1+c2);
				}
				
			}
		}
		
		return ret;
	}
	
	/**
	 * Test to see if the two contours are concurrent.
	 */
	public boolean concurrent(String c1, String c2) {

		for(String zone : getZoneList()) {
			if(zone.contains(c1) && !zone.contains(c2)) {
				return false;
			}
			if(!zone.contains(c1) && zone.contains(c2)) {
				return false;
			}
		}
		
		return true;
	}

	
}
