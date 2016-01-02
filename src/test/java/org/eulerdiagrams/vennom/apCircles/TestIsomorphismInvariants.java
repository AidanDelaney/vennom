package org.eulerdiagrams.vennom.apCircles;

import org.eulerdiagrams.vennom.apCircles.enumerate.IsomorphismInvariants;
import org.junit.Test;

public class TestIsomorphismInvariants {
	@Test
	public void test_001() {

		IsomorphismInvariants.outputLabelSizeCombinations(1);
		IsomorphismInvariants.outputZoneSizeCombinations(1);
		IsomorphismInvariants.outputZonePartitionCombinations(1);
		IsomorphismInvariants.outputLabelPartitionCombinations(1);
		IsomorphismInvariants.outputlabelZoneSequenceCombinations(1);
		
		IsomorphismInvariants.outputLabelSizeCombinations(2);
		IsomorphismInvariants.outputZoneSizeCombinations(2);
		IsomorphismInvariants.outputZonePartitionCombinations(2);
		IsomorphismInvariants.outputLabelPartitionCombinations(2);
		IsomorphismInvariants.outputlabelZoneSequenceCombinations(2);
		
		IsomorphismInvariants.outputLabelSizeCombinations(3);
		IsomorphismInvariants.outputZoneSizeCombinations(3);
		IsomorphismInvariants.outputZonePartitionCombinations(3);
		IsomorphismInvariants.outputLabelPartitionCombinations(3);
		IsomorphismInvariants.outputlabelZoneSequenceCombinations(3);

		// removed this because it is too slow ... TODO is this a bug?
		//IsomorphismInvariants.outputLabelSizeCombinations(4);
		//IsomorphismInvariants.outputZoneSizeCombinations(4);
		//IsomorphismInvariants.outputZonePartitionCombinations(4);
		//IsomorphismInvariants.outputLabelPartitionCombinations(4);
		//IsomorphismInvariants.outputlabelZoneSequenceCombinations(4);
			
		// TODO assertions and checking this does something correct
		
	}
}
