package org.matsim.pt.counts;

import java.util.List;
import org.junit.Test;
import org.matsim.counts.CountSimComparison;
import org.matsim.testcases.MatsimTestCase;

public class PtAlightCountsComparisonAlgorithmTest extends MatsimTestCase {

    @Test
    public void testCompare() {
        PtCountsFixture fixture = new PtAlightCountsFixture();
        fixture.setUp();
        PtCountsComparisonAlgorithm cca = fixture.getCCA();
        cca.run();
        List<CountSimComparison> csc_list = cca.getComparison();
        int cnt = 0;
        for (CountSimComparison csc : csc_list) {
            if (cnt != 8 && cnt != 32) {
                assertEquals("Wrong sim value set", 0d, csc.getSimulationValue(), 0d);
            } else if (cnt == 8) {
                assertEquals("Wrong sim value set", 500d, csc.getSimulationValue(), 0d);
            } else {
                assertEquals("Wrong sim value set", 150d, csc.getSimulationValue(), 0d);
            }
            cnt++;
        }
    }

    @Test
    public void testDistanceFilter() {
        PtCountsFixture fixture = new PtAlightCountsFixture();
        fixture.setUp();
        PtCountsComparisonAlgorithm cca = fixture.getCCA();
        cca.setDistanceFilter(Double.valueOf(4000), "11");
        cca.run();
        List<CountSimComparison> csc_list = cca.getComparison();
        assertEquals("Distance filter not working", 24, csc_list.size());
    }
}
