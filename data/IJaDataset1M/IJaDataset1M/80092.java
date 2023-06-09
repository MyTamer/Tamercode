package statechum.analysis.learning.rpnicore;

import static org.junit.Assert.assertTrue;
import static statechum.analysis.learning.rpnicore.FsmParser.buildGraph;
import static statechum.analysis.learning.rpnicore.FsmParser.buildLearnerGraph;
import static statechum.analysis.learning.rpnicore.FsmParser.buildLearnerGraphND;
import statechum.DeterministicDirectedSparseGraph.CmpVertex;
import statechum.DeterministicDirectedSparseGraph.VertexID;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import statechum.Configuration;
import statechum.JUConstants;
import statechum.StringVertex;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.utils.UserData;
import static statechum.Helper.checkForCorrectException;
import static statechum.Helper.whatToRun;
import static statechum.analysis.learning.rpnicore.TestEqualityComparisonAndHashCode.equalityTestingHelper;

@RunWith(Parameterized.class)
public class TestGraphConstructionWithDifferentConf {

    public TestGraphConstructionWithDifferentConf(Configuration conf) {
        mainConfiguration = conf;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Configuration.configurationsForTesting();
    }

    /** Given a test configuration, returns a textual description of its purpose. 
	 * 
	 * @param config configuration to consider
	 * @return description.
	 */
    public static String parametersToString(Configuration config) {
        return Configuration.parametersToString(config);
    }

    /** Make sure that whatever changes a test have made to the 
	 * configuration, next test is not affected.
	 */
    @Before
    public void beforeTest() {
        config = mainConfiguration.copy();
        config.setAllowedToCloneNonCmpVertex(true);
        differentA = buildLearnerGraph("Q-a->A-b->B", "testFSMStructureEquals2", config);
        differentB = buildLearnerGraph("A-b->A-a->B", "testFSMStructureEquals2", config);
    }

    /** The configuration to use when running tests. */
    Configuration config = null, mainConfiguration = null;

    /** Used as arguments to equalityTestingHelper. */
    private LearnerGraph differentA = null, differentB = null;

    @Test
    public final void testFSMStructureEquals1a() {
        LearnerGraph a = new LearnerGraph(config), b = new LearnerGraph(config);
        equalityTestingHelper(a, b, differentA, differentB);
        Assert.assertFalse(a.equals(null));
        Assert.assertFalse(a.equals("hello"));
        config.setDefaultInitialPTAName("B");
        b = new LearnerGraph(config);
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public final void testFSMStructureEquals2a() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals2a", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals2a", config);
        equalityTestingHelper(a, b, differentA, differentB);
    }

    /** Tests that reject states do not mess up anything. */
    @Test
    public final void testFSMStructureEquals2b() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c-#C\nB-b->B", "testFSMStructureEquals2b", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c-#C\nB-b->B", "testFSMStructureEquals2b", config);
        equalityTestingHelper(a, b, differentA, differentB);
    }

    /** Tests that different types of states make a difference on the outcome of a comparison. */
    @Test
    public final void testFSMStructureEquals2c() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2c", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c-#C\nB-b->B", "testFSMStructureEquals2c", config);
        equalityTestingHelper(a, a, b, differentB);
    }

    /** Tests that state colour affects a comparison. */
    @Test
    public final void testFSMStructureEquals2d() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c-#C\nB-b->B", "testFSMStructureEquals2d", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c-#C\nB-b->B", "testFSMStructureEquals2d", config);
        a.findVertex("B").setColour(JUConstants.RED);
        a.findVertex("A").setHighlight(true);
        equalityTestingHelper(a, a, b, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2e1() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        a.addToCompatibility(a.findVertex(VertexID.parseID("A")), a.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        b.addToCompatibility(b.findVertex(VertexID.parseID("A")), b.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        c.addToCompatibility(c.findVertex(VertexID.parseID("C")), c.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2e2() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / C = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2f1() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        a.addToCompatibility(a.findVertex(VertexID.parseID("A")), a.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.MERGED);
        b.addToCompatibility(b.findVertex(VertexID.parseID("A")), b.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.MERGED);
        c.addToCompatibility(c.findVertex(VertexID.parseID("C")), c.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2f2() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = MERGED = B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = MERGED = B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / C = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2g1() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        a.addToCompatibility(a.findVertex(VertexID.parseID("A")), a.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        b.addToCompatibility(b.findVertex(VertexID.parseID("A")), b.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        c.addToCompatibility(c.findVertex(VertexID.parseID("A")), c.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.MERGED);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2g2() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / A = MERGED = B", "testFSMStructureEquals2e", config);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2h1() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B", "testFSMStructureEquals2e", config);
        a.addToCompatibility(a.findVertex(VertexID.parseID("A")), a.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        a.addToCompatibility(a.findVertex(VertexID.parseID("C")), a.findVertex(VertexID.parseID("A")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        b.addToCompatibility(b.findVertex(VertexID.parseID("A")), b.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        b.addToCompatibility(b.findVertex(VertexID.parseID("C")), b.findVertex(VertexID.parseID("A")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        c.addToCompatibility(c.findVertex(VertexID.parseID("A")), c.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        c.addToCompatibility(c.findVertex(VertexID.parseID("C")), c.findVertex(VertexID.parseID("B")), JUConstants.PAIRCOMPATIBILITY.INCOMPATIBLE);
        equalityTestingHelper(a, b, c, differentB);
    }

    /** Tests that different collections of incompatible states affect the comparison. */
    @Test
    public final void testFSMStructureEquals2h2() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / C = INCOMPATIBLE = A / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / C = INCOMPATIBLE = A / B = INCOMPATIBLE = A", "testFSMStructureEquals2e", config);
        LearnerGraph c = buildLearnerGraph("A-a->A-b->B\nA-c->C\nB-b->B / C = INCOMPATIBLE = B / A = INCOMPATIBLE = B", "testFSMStructureEquals2e", config);
        equalityTestingHelper(a, b, c, differentB);
    }

    @Test
    public final void testFSMStructureEquals3() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals3", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals3", config);
        b.initEmpty();
        equalityTestingHelper(a, a, b, differentB);
    }

    @Test
    public final void testFSMStructureEquals4() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals4", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals4", config);
        b.initPTA();
        equalityTestingHelper(a, a, b, differentB);
    }

    @Test
    public final void testFSMStructureEquals5() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals6", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureEquals6", config);
        b.setInit(new StringVertex("B"));
        equalityTestingHelper(a, a, b, differentB);
    }

    /** Tests that clones are faithful replicas. */
    @Test
    public final void testFSMStructureClone1() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureClone1", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureClone1", config);
        LearnerGraph bClone = new LearnerGraph(b, b.config);
        equalityTestingHelper(a, bClone, differentA, differentB);
        equalityTestingHelper(b, bClone, differentA, differentB);
        bClone.initPTA();
        equalityTestingHelper(a, b, bClone, differentB);
    }

    /** Tests that clones are faithful replicas. */
    @Test
    public final void testFSMStructureClone2() {
        LearnerGraph a = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureClone2", config);
        LearnerGraph b = buildLearnerGraph("A-a->A-b->B\nB-b->B", "testFSMStructureClone2", config);
        LearnerGraph bClone = new LearnerGraph(b, b.config);
        b.initPTA();
        equalityTestingHelper(a, bClone, b, differentB);
    }

    @Test
    public void testGraphConstructionFail1a() {
        boolean exceptionThrown = false;
        try {
            buildGraph("A--a-->B<-b-CONFL\nA-b->A-c->A\nB-d->B-p-#CONFL", "testGraphConstructionFail1a", config);
        } catch (IllegalArgumentException e) {
            assertTrue("correct exception not thrown", e.getMessage().contains("conflicting") && e.getMessage().contains("CONFL"));
            exceptionThrown = true;
        }
        assertTrue("exception not thrown", exceptionThrown);
    }

    @Test
    public void testGraphConstructionFail1b() {
        boolean exceptionThrown = false;
        try {
            buildGraph("A--a-->CONFL-b-#CONFL", "testGraphConstructionFail1b", config);
        } catch (IllegalArgumentException e) {
            assertTrue("correct exception not thrown", e.getMessage().contains("conflicting") && e.getMessage().contains("CONFL"));
            exceptionThrown = true;
        }
        assertTrue("exception not thrown", exceptionThrown);
    }

    /** Checks if adding a vertex to a graph causes an exception to be thrown. */
    public void checkWithVertex(Vertex v, String expectedExceptionString, String testName) {
        final DirectedSparseGraph g = buildGraph("A--a-->B<-b-CONFL\nA-b->A-c->A\nB-d->B-p->CONFL", testName, config);
        new LearnerGraph(g, config);
        g.addVertex(v);
        checkForCorrectException(new whatToRun() {

            @Override
            public void run() {
                new LearnerGraph(g, config);
            }
        }, IllegalArgumentException.class, expectedExceptionString);
    }

    @Test
    public void testGraphConstructionFail2() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("B"), UserData.SHARED);
        checkWithVertex(v, "multiple states with the same name", "testGraphConstructionFail2");
    }

    @Test
    public void testGraphConstructionFail3() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("CONFL"), UserData.SHARED);
        checkWithVertex(v, "multiple states with the same name", "testGraphConstructionFail3");
    }

    @Test
    public void testGraphConstructionFail4a() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("Q"), UserData.SHARED);
        v.addUserDatum(JUConstants.INITIAL, true, UserData.SHARED);
        checkWithVertex(v, "both labelled as initial states", "testGraphConstructionFail4a");
    }

    @Test
    public void testGraphConstructionFail4b() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("Q"), UserData.SHARED);
        v.addUserDatum(JUConstants.INITIAL, new VertexID("aa"), UserData.SHARED);
        checkWithVertex(v, "invalid init property", "testGraphConstructionFail4b");
    }

    @Test
    public void testGraphConstructionFail5a() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("Q"), UserData.SHARED);
        v.addUserDatum(JUConstants.INITIAL, "aa", UserData.SHARED);
        checkWithVertex(v, "invalid init property", "testGraphConstructionFail5a");
    }

    @Test
    public void testGraphConstructionFail5b() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        v.addUserDatum(JUConstants.LABEL, new VertexID("Q"), UserData.SHARED);
        v.addUserDatum(JUConstants.INITIAL, false, UserData.SHARED);
        checkWithVertex(v, "invalid init property", "testGraphConstructionFail5b");
    }

    /** missing initial state in an empty graph. */
    @Test
    public void testGraphConstructionFail6() {
        checkForCorrectException(new whatToRun() {

            @Override
            public void run() {
                new LearnerGraph(new DirectedSparseGraph(), config);
            }
        }, IllegalArgumentException.class, "missing initial");
    }

    /** Unlabelled states. */
    @Test
    public final void testGraphConstructionFail7() {
        DirectedSparseVertex v = new DirectedSparseVertex();
        v.addUserDatum(JUConstants.ACCEPTED, true, UserData.SHARED);
        checkWithVertex(v, "is not labelled", "testGraphConstructionFail7");
    }

    /** Non-determinism is ok for non-deterministic matrix. */
    @Test
    public final void testGraphConstruction_nondet_1a() {
        LearnerGraphND graph = buildLearnerGraphND("A-a->B-b->C\nB-b->D", "testGraphConstruction_nondet_1a", Configuration.getDefaultConfiguration());
        Set<CmpVertex> targets_a = new TreeSet<CmpVertex>();
        targets_a.add(graph.findVertex("B"));
        Set<CmpVertex> targets_b = new TreeSet<CmpVertex>();
        targets_b.add(graph.findVertex("C"));
        targets_b.add(graph.findVertex("D"));
        Set<CmpVertex> actual_a = new TreeSet<CmpVertex>();
        actual_a.addAll(graph.transitionMatrix.get(graph.findVertex("A")).get(AbstractLearnerGraph.generateNewLabel("a", graph.config)));
        Assert.assertTrue(targets_a.equals(actual_a));
        Set<CmpVertex> actual_b = new TreeSet<CmpVertex>();
        actual_b.addAll(graph.transitionMatrix.get(graph.findVertex("B")).get(AbstractLearnerGraph.generateNewLabel("b", graph.config)));
        Assert.assertTrue(targets_b.equals(actual_b));
        Assert.assertTrue(graph.transitionMatrix.get(graph.findVertex("C")).isEmpty());
        Assert.assertTrue(graph.transitionMatrix.get(graph.findVertex("D")).isEmpty());
    }

    /** Non-determinism is bad for a deterministic graph. */
    @Test
    public final void testGraphConstruction_nondet_1b() {
        checkForCorrectException(new whatToRun() {

            @Override
            public void run() {
                buildLearnerGraph("A-a->B-b->C\nB-b->D", "testGraphConstruction_nondet_1a", Configuration.getDefaultConfiguration());
            }
        }, IllegalArgumentException.class, "non-determinism");
    }
}
