package org.blipkit.test;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Hashtable;
import org.bbop.io.AuditedPrintStream;
import org.blipkit.datamodel.impl.DatalogBackedMutableLinkDatabase;
import org.obo.datamodel.impl.DefaultLinkDatabase;
import org.obo.reasoner.impl.ForwardChainingReasoner;
import org.obo.test.AbstractOBOTest;
import jpl.Atom;
import jpl.Compound;
import jpl.Query;
import jpl.Term;
import jpl.Variable;
import junit.framework.*;

public class DatalogTest extends AbstractOBOTest {

    public DatalogTest(String name) {
        super(name);
    }

    public Collection<String> getFilesToLoad() {
        String[] files = { "nucleus.obo" };
        return Arrays.asList(files);
    }

    public void setUp() throws Exception {
        System.out.println("foo");
        System.out.println("Setting up: " + this);
        assertFact("zyb:foo", "a", "b");
        new Query("use_module(bio(io))").allSolutions();
        new Query("load_bioresource(go)").allSolutions();
        System.out.println("asserted");
    }

    public void testHasLoaded() {
        Variable X = new Variable("X");
        Term iq = new Compound("foo", new Term[] { new Atom("a"), X });
        Query q = new Query(":", new Term[] { new Atom("zyb"), iq });
        for (Hashtable h : q.allSolutions()) {
            System.out.println(q + "// " + X.toString() + " soln= " + h.get(X.toString()));
        }
        for (Hashtable h : new Query("ontol_db:subclassT(X,'GO:0005634')").allSolutions()) {
            System.out.println(h.get("X"));
        }
        for (Hashtable h : new Query("ontol_db:subclassT(X,'GO:0005634'),ontol_db:class(X,XN)").allSolutions()) {
            System.out.println(h.get("X") + " ! " + h.get("XN"));
        }
        assertTrue(true);
    }

    protected int assertFact(String pred, Object... args) {
        Atom[] atomA = new Atom[args.length];
        int i = 0;
        for (Object a : args) {
            atomA[i] = new Atom((String) a);
            i++;
        }
        Compound term = new Compound(pred, atomA);
        Query q = new Query("assert", term);
        for (Hashtable h : q.allSolutions()) {
            System.err.println("hmm " + h);
        }
        return 0;
    }
}
