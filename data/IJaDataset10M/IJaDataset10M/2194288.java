package org.processmining.framework.models.pdm;

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.processmining.framework.log.*;
import org.processmining.framework.models.pdm.*;
import org.processmining.framework.models.recommendation.*;
import org.processmining.framework.ui.*;
import org.processmining.framework.models.*;

public class PDMStateSpace extends ModelGraph {

    PDMModel model;

    HashSet states = new HashSet();

    HashSet edges = new HashSet();

    PDMDataElement root;

    boolean colored;

    private HashSet paths = new HashSet();

    Vector D = new Vector();

    Vector V = new Vector();

    Vector strategy;

    public PDMStateSpace() {
        super("PDM state space");
    }

    public PDMStateSpace(PDMModel model, boolean colored) {
        super("PDM state space");
        this.root = model.getRootElement();
        this.model = model;
        this.colored = colored;
    }

    /**
	 * Adds a state to the statespace
	 * 
	 * @param state
	 *            PDMState
	 */
    public void addState(PDMState state) {
        states.add(state);
    }

    /**
	 * Adds an edge to the statespace
	 * 
	 * @param edge
	 *            PDMStateEdge
	 */
    public void addEdge(PDMStateEdge edge) {
        edges.add(edge);
    }

    /**
	 * Returns a hashset with all states in this statespace
	 * 
	 * @return HashSet
	 */
    public HashSet getStates() {
        return states;
    }

    /**
	 * Returns a hashset with set of states that follow upon state 'state' (i.e.
	 * the outgoing edges of 'state' point to these states)
	 * 
	 * @param state
	 *            PDMState
	 * @return HashSet
	 */
    public HashSet getFollowingStates(PDMState state) {
        HashSet result = new HashSet();
        Iterator it = edges.iterator();
        while (it.hasNext()) {
            PDMStateEdge edge = (PDMStateEdge) it.next();
            if (edge.getSource().equals(state)) {
                result.add(edge.getDestination());
            }
        }
        return result;
    }

    /**
	 * Checks whether there is a state following on this state that contains the
	 * root element in the set of available data elements. Thus, it checks
	 * whether it is possible to succesfully complete the process from this
	 * state.
	 * 
	 * @param state
	 *            PDMState
	 * @return boolean
	 */
    public boolean completionPossible(PDMState state) {
        boolean result = false;
        HashSet following = getFollowingStates(state);
        Iterator it = following.iterator();
        while (it.hasNext() && (!result)) {
            PDMState state1 = (PDMState) it.next();
            if (state1.getDataElementSet().contains(root)) {
                result = true;
            } else if (!state1.getDataElementSet().contains(root)) {
                result = completionPossible(state1);
            }
        }
        return result;
    }

    /**
	 * Returns the Product Data Model (PDM) to which this statespace belongs to.
	 * 
	 * @return PDMModel
	 */
    public PDMModel getPDMModel() {
        return model;
    }

    /**
	 * Writes the model to DOT.
	 * 
	 * @param bw
	 *            The writer
	 * @throws IOException
	 *             If writing fails
	 */
    public void writeToDot(Writer bw) throws IOException {
        bw.write("digraph G {ranksep=\".3\"; fontsize=\"8\"; remincross=true; margin=\"0.0,0.0\"; rankdir=TB; ");
        bw.write("fontname=\"Arial\"; \n");
        bw.write("edge [arrowsize=\"0.5\"];\n");
        bw.write("node [fontname=\"Arial\",fontsize=\"8\"];\n");
        if (colored) {
            Iterator it2 = states.iterator();
            while (it2.hasNext()) {
                PDMState state = (PDMState) it2.next();
                if (state instanceof PDMState) {
                    HashSet failed = state.failedOperations;
                    if (failed.isEmpty()) {
                        state.setStatus(1);
                    } else if (state.getDataElementSet().contains(root)) {
                        state.setStatus(1);
                    } else if (!failed.isEmpty()) {
                        boolean complPos = completionPossible(state);
                        if (complPos) {
                            state.setStatus(2);
                        } else {
                            state.setStatus(3);
                        }
                    }
                }
            }
        }
        Iterator it = states.iterator();
        while (it.hasNext()) {
            PDMState state = (PDMState) it.next();
            if (state instanceof PDMState) {
                state.writeToDot(bw);
            }
        }
        it = edges.iterator();
        while (it.hasNext()) {
            PDMStateEdge edge = (PDMStateEdge) it.next();
            if (edge instanceof PDMStateEdge) {
                edge.writeToDot(bw);
            }
        }
        bw.write("\n}\n");
    }

    public Vector calculateStrategyMinCost() {
        Vector result = new Vector();
        Vector v0 = new Vector();
        Vector d0 = new Vector();
        int numStates = states.size();
        for (int i = 0; i < numStates; i++) {
            v0.add(0.0);
            d0.add("-");
        }
        V.add(v0);
        D.add(d0);
        int depth = getDepthOfStatespace() + 1;
        for (int n = 1; n < (depth - 1); n++) {
            Vector vv = new Vector(numStates);
            Vector dd = new Vector(numStates);
            for (int m = 0; m < numStates; m++) {
                PDMState state = this.getPDMState("state" + m);
                Double cost = 10000.0;
                String dec = "";
                HashSet decisions = calculateDecisions(state);
                if (decisions.isEmpty()) {
                    cost = 0.0;
                    dec = "-";
                } else {
                    Iterator itd = decisions.iterator();
                    while (itd.hasNext()) {
                        String str = (String) itd.next();
                        PDMOperation op = model.getOperation(str);
                        double value = op.getCost();
                        Iterator ite = edges.iterator();
                        while (ite.hasNext()) {
                            PDMStateEdge edge = (PDMStateEdge) ite.next();
                            PDMState state2 = edge.getDestination();
                            if ((edge.getSource().equals(state)) && edge.getLabel().equals(str)) {
                                double prob = edge.getProbability();
                                double val = prob * getValueV(n - 1, state2.getID());
                                value = value + val;
                            }
                        }
                        if (value < cost) {
                            cost = value;
                            dec = op.getID();
                        }
                    }
                }
                int num = state.getID();
                vv.add(num, cost);
                dd.add(num, dec);
            }
            V.add(vv);
            D.add(dd);
        }
        Vector vv = new Vector(numStates);
        Vector dd = new Vector(numStates);
        Vector strat = new Vector(numStates);
        for (int m = 0; m < numStates; m++) {
            Vector decs = new Vector();
            PDMState state = this.getPDMState("state" + m);
            Double cost = 10000.0;
            String dec = "";
            HashSet decisions = calculateDecisions(state);
            if (decisions.isEmpty()) {
                cost = 0.0;
                dec = "-";
            } else {
                Iterator itd = decisions.iterator();
                while (itd.hasNext()) {
                    String str = (String) itd.next();
                    PDMOperation op = model.getOperation(str);
                    double value = op.getCost();
                    Iterator ite = edges.iterator();
                    while (ite.hasNext()) {
                        PDMStateEdge edge = (PDMStateEdge) ite.next();
                        PDMState state2 = edge.getDestination();
                        if ((edge.getSource().equals(state)) && edge.getLabel().equals(str)) {
                            double prob = edge.getProbability();
                            double val = prob * getValueV(depth - 2, state2.getID());
                            value = value + val;
                        }
                    }
                    Vector map = new Vector(2);
                    map.add(str);
                    map.add(value);
                    decs.add(map);
                    if (value < cost) {
                        cost = value;
                        dec = op.getID();
                    }
                }
            }
            int num = state.getID();
            vv.add(num, cost);
            dd.add(num, dec);
            strat.add(num, decs);
        }
        V.add(vv);
        D.add(dd);
        strategy = strat;
        result = strat;
        Iterator itv = V.iterator();
        while (itv.hasNext()) {
            Vector vvv = (Vector) itv.next();
            Iterator itvvv = vvv.iterator();
            while (itvvv.hasNext()) {
                Double ddd = (Double) itvvv.next();
                double d5 = ddd.doubleValue();
                System.out.print(d5 + ", ");
            }
            System.out.println();
        }
        return result;
    }

    public Vector calculateStrategyMinTime() {
        Vector result = new Vector();
        Vector v0 = new Vector();
        Vector d0 = new Vector();
        int numStates = states.size();
        for (int i = 0; i < numStates; i++) {
            v0.add(0.0);
            d0.add("-");
        }
        V.add(v0);
        D.add(d0);
        int depth = getDepthOfStatespace() + 1;
        for (int n = 1; n < depth; n++) {
            Vector vv = new Vector(numStates);
            Vector dd = new Vector(numStates);
            for (int m = 0; m < numStates; m++) {
                PDMState state = this.getPDMState("state" + m);
                Double time = 10000.0;
                String dec = "";
                HashSet decisions = calculateDecisions(state);
                if (decisions.isEmpty()) {
                    time = 0.0;
                    dec = "-";
                } else {
                    Iterator itd = decisions.iterator();
                    while (itd.hasNext()) {
                        String str = (String) itd.next();
                        PDMOperation op = model.getOperation(str);
                        double value = op.getDuration();
                        Iterator ite = edges.iterator();
                        while (ite.hasNext()) {
                            PDMStateEdge edge = (PDMStateEdge) ite.next();
                            PDMState state2 = edge.getDestination();
                            if ((edge.getSource().equals(state)) && edge.getLabel().equals(str)) {
                                double prob = edge.getProbability();
                                double val = prob * getValueV(n - 1, state2.getID());
                                value = value + val;
                            }
                        }
                        if (value < time) {
                            time = value;
                            dec = op.getID();
                        }
                    }
                }
                int num = state.getID();
                vv.add(num, time);
                dd.add(num, dec);
            }
            V.add(vv);
            D.add(dd);
        }
        strategy = (Vector) D.lastElement();
        result = (Vector) D.lastElement();
        Iterator itv = V.iterator();
        while (itv.hasNext()) {
            Vector vvv = (Vector) itv.next();
            Iterator itvvv = vvv.iterator();
            while (itvvv.hasNext()) {
                Double ddd = (Double) itvvv.next();
                double d5 = ddd.doubleValue();
                System.out.print(d5 + ", ");
            }
            System.out.println();
        }
        return result;
    }

    public Vector getStrategy() {
        return strategy;
    }

    public HashSet getOutEdges(PDMState state) {
        HashSet result = new HashSet();
        Iterator it = edges.iterator();
        while (it.hasNext()) {
            PDMStateEdge edge = (PDMStateEdge) it.next();
            if (edge.getSource().equals(state)) {
                result.add(edge.getDestination());
            }
        }
        return result;
    }

    public HashSet calculateDecisions(PDMState state) {
        HashSet result = new HashSet();
        Iterator it = edges.iterator();
        while (it.hasNext()) {
            PDMStateEdge edge = (PDMStateEdge) it.next();
            if (edge.getSource().equals(state)) {
                result.add(edge.getLabel());
            }
        }
        return result;
    }

    public double getValueV(int n, int statenr) {
        double result = 0.0;
        Vector vec = (Vector) V.get(n);
        Double val = (Double) vec.get(statenr);
        result = val.doubleValue();
        return result;
    }

    /**
	 * Returns the Resource with identifier "id"
	 * 
	 * @param id
	 *            String
	 * @return PDMResource
	 */
    public PDMState getPDMState(String id) {
        PDMState result = null;
        Iterator it = states.iterator();
        while (it.hasNext()) {
            PDMState state = (PDMState) it.next();
            if (state.getIdentifier().equals(id)) {
                result = state;
            }
        }
        return result;
    }

    public PDMState getPDMState(HashSet exec, HashSet failed, HashSet data) {
        PDMState result = null;
        Iterator it = states.iterator();
        while (it.hasNext()) {
            PDMState state = (PDMState) it.next();
            HashSet data2 = state.getDataElementSet();
            HashSet exec2 = state.executedOperations;
            HashSet failed2 = state.failedOperations;
            boolean one = data2.equals(data);
            boolean two = exec2.equals(exec);
            boolean three = failed2.equals(failed);
            if (one && two && three) {
                result = state;
            }
        }
        return result;
    }

    public int getNumberOfStates() {
        return states.size();
    }

    public int getDepthOfStatespace() {
        int result = 0;
        PDMState rootState = getPDMState("state0");
        if (!(rootState == null)) {
            Vector v = new Vector();
            v.add(rootState);
            getPaths(v);
            Iterator itp = paths.iterator();
            while (itp.hasNext()) {
                Vector p = (Vector) itp.next();
                int len = p.size();
                if (len > result) {
                    result = len;
                }
            }
        } else {
            System.out.println("The PDM state space does not have a root state");
        }
        return result;
    }

    public void getPaths(Vector v) {
        PDMState state = (PDMState) v.lastElement();
        HashSet outEdges = getOutEdges(state);
        if (outEdges.size() > 0) {
            Iterator it = outEdges.iterator();
            while (it.hasNext()) {
                PDMState dest = (PDMState) it.next();
                Vector vv = (Vector) v.clone();
                vv.add(dest);
                getPaths(vv);
            }
        } else if (outEdges.size() == 0) {
            paths.add(v);
        }
    }
}
