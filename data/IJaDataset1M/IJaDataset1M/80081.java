package uk.ac.manchester.cs.snee.compiler.queryplan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;
import uk.ac.manchester.cs.snee.compiler.OptimizationException;
import uk.ac.manchester.cs.snee.metadata.CostParameters;
import uk.ac.manchester.cs.snee.metadata.schema.SchemaMetadataException;
import uk.ac.manchester.cs.snee.metadata.schema.TypeMappingException;
import uk.ac.manchester.cs.snee.metadata.source.sensornet.Site;
import uk.ac.manchester.cs.snee.metadata.source.sensornet.Topology;
import uk.ac.manchester.cs.snee.operators.logical.CardinalityType;
import uk.ac.manchester.cs.snee.operators.logical.DeliverOperator;
import uk.ac.manchester.cs.snee.operators.sensornet.SensornetDeliverOperator;
import uk.ac.manchester.cs.snee.operators.sensornet.SensornetExchangeOperator;
import uk.ac.manchester.cs.snee.operators.sensornet.SensornetOperator;

public class Fragment {

    /**
     * Logger for this class.
     */
    static Logger logger = Logger.getLogger(Fragment.class.getName());

    /**
     * Counter to assign unique id to different fragments.
     */
    public static int fragmentCount = 0;

    /**
     * Identifier of this fragment
     */
    protected int fragID;

    /**
     * The output exchange operator of this fragment (currently, there can only be one).
     */
    protected SensornetExchangeOperator parentExchange;

    /**
     * The input exchange operators of this fragment.
     */
    protected ArrayList<SensornetExchangeOperator> childExchanges = new ArrayList<SensornetExchangeOperator>();

    /**
     * The root operator of the fragment
     */
    protected SensornetOperator rootOperator;

    /**
     * The operators which belong to this fragment
     */
    protected HashSet<SensornetOperator> operators = new HashSet<SensornetOperator>();

    /**
     * The sensor network nodes to which this fragment has been allocated to run.
     */
    protected ArrayList<Site> sites = new ArrayList<Site>();

    /**
     * The list of sensor nodes where the operators in the fragment are required to run.
     * e.g., an Acquire operator will require certain data sources
     */
    public ArrayList<String> desiredSites = new ArrayList<String>();

    public Fragment() {
        fragmentCount++;
        this.fragID = fragmentCount;
    }

    /**
     * @return the id of the fragment
     */
    public String getID() {
        return new Integer(this.fragID).toString();
    }

    /**
     * Returns true if this is a leaf fragment
     */
    public final boolean isLeaf() {
        return (this.getChildFragments().size() == 0);
    }

    /**
     * Return the root operator of the fragment
     * (NB: The parent exchange is not considered to be within in the fragment;
     * this method returns the child of the parent exchange)
     * @return
     */
    public final SensornetOperator getRootOperator() {
        return this.rootOperator;
    }

    public final void setRootOperator(final SensornetOperator op) {
        this.rootOperator = op;
    }

    /**
     * @return	the child exchange operators of the fragment
     */
    public final ArrayList<SensornetExchangeOperator> getChildExchangeOperators() {
        return this.childExchanges;
    }

    /**
     * Get the parent fragments of this fragment
     * @return
     */
    public final Fragment getParentFragment() {
        return this.parentExchange.getDestFragment();
    }

    /**
     * Get the child fragments of this fragment
     * @return
     */
    public final ArrayList<Fragment> getChildFragments() {
        final ArrayList<Fragment> childFrags = new ArrayList<Fragment>();
        final Iterator<SensornetExchangeOperator> childExchangeIter = this.childExchanges.iterator();
        while (childExchangeIter.hasNext()) {
            final SensornetExchangeOperator exch = childExchangeIter.next();
            assert (exch.getSourceFragment() != null);
            childFrags.add(exch.getSourceFragment());
        }
        return childFrags;
    }

    /**
     * Get the sites on which all the child fragments have been placed
     * @return
     */
    public final HashSet<Site> getChildFragSites() {
        final HashSet<Site> childFragSites = new HashSet<Site>();
        final Iterator<Fragment> fragIter = this.getChildFragments().iterator();
        while (fragIter.hasNext()) {
            final Fragment f = fragIter.next();
            childFragSites.addAll(f.getSites());
        }
        return childFragSites;
    }

    /**
     * @return	the parent exchange operators of the fragment
     */
    public final SensornetExchangeOperator getParentExchangeOperator() {
        return this.parentExchange;
    }

    /**
     * Returns the ith child exchange operator
     * @param i		the positition of the child exchange operator
     * @return		the exchange operator
     */
    public final SensornetExchangeOperator getChildExchangeOperator(final int i) {
        return this.childExchanges.get(i);
    }

    /**
     * Returns the number of child exchange operators.
     * @return
     */
    public final int getNumChildExchangeOperators() {
        return this.childExchanges.size();
    }

    /**
     * @return the operators in the fragment
     */
    public final HashSet<SensornetOperator> getOperators() {
        return this.operators;
    }

    public final int[] getSourceNodes() {
        return this.getRootOperator().getSourceSites();
    }

    public final boolean containsOperatorType(final Class c) {
        final Iterator<SensornetOperator> i = this.operators.iterator();
        while (i.hasNext()) {
            final SensornetOperator op = i.next();
            if (c.isInstance(op)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isLocationSensitive() {
        boolean found = false;
        final Iterator<SensornetOperator> ops = this.operatorIterator(TraversalOrder.PRE_ORDER);
        while (ops.hasNext()) {
            if (ops.next().isLocationSensitive()) {
                found = true;
                break;
            }
        }
        return found;
    }

    public final boolean isRecursive() {
        boolean found = false;
        final Iterator<SensornetOperator> ops = this.operators.iterator();
        while (ops.hasNext()) {
            if (ops.next().isRecursive()) {
                found = true;
                break;
            }
        }
        return found;
    }

    public final boolean isAttributeSensitive() {
        boolean found = false;
        final Iterator<SensornetOperator> ops = this.operatorIterator(TraversalOrder.PRE_ORDER);
        while (ops.hasNext()) {
            if (ops.next().isAttributeSensitive()) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * 
     * @return True if the fragment ends in a deliver and therefor does not output tuples to a tray.
     */
    public final boolean isDeliverFragment() {
        if (this.getRootOperator() instanceof SensornetDeliverOperator) {
            return true;
        }
        return false;
    }

    /**
     * @return the sensor network nodes this fragment has been allocated to
     */
    public final ArrayList<Site> getSites() {
        return this.sites;
    }

    /**
     * Helper method to implement the operator iterator.
     * @param op The current operator being visited
     * @param opList The list of operators being generated (the result).
     * @param traversalOrder The order in which to traverse the operators.
     */
    private void doOperatorIterator(final SensornetOperator op, final ArrayList<SensornetOperator> opList, final TraversalOrder traversalOrder) {
        if (traversalOrder == TraversalOrder.PRE_ORDER) {
            opList.add(op);
        }
        for (int n = 0; n < op.getInDegree(); n++) {
            if (!(op.getInput(n) instanceof SensornetExchangeOperator)) {
                this.doOperatorIterator((SensornetOperator) op.getInput(n), opList, traversalOrder);
            }
        }
        if (traversalOrder == TraversalOrder.POST_ORDER) {
            opList.add(op);
        }
    }

    /**
     * Iterator to traverse the operator tree.
     * The structure of the routing tree may not be modified during iteration
     * @param traversalOrder (constants defined in QueryPlan class)
     * @return an iterator of the operator tree in the fragment
     */
    public final Iterator<SensornetOperator> operatorIterator(final TraversalOrder traversalOrder) {
        final ArrayList<SensornetOperator> opList = new ArrayList<SensornetOperator>();
        logger.finest("root =" + this.getRootOperator());
        this.doOperatorIterator(this.getRootOperator(), opList, traversalOrder);
        logger.finest("done");
        return opList.iterator();
    }

    /** 
     * Calculates the physical size of the state of the operators in this 
     * fragment.  Does not include the size of the exchange components 
     * including the consumers and producers.  Does not include the size 
     * of the code itself.
     * 
     * @return Sum of the cost of each of the operators
     * @throws OptimizationException 
     * @throws TypeMappingException 
     * @throws SchemaMetadataException 
     */
    public final long getDataMemoryCost(final Site node, final DAF daf) throws SchemaMetadataException, TypeMappingException, OptimizationException {
        long total = 0;
        final Iterator<SensornetOperator> ops = this.operatorIterator(TraversalOrder.PRE_ORDER);
        while (ops.hasNext()) {
            final SensornetOperator op = ops.next();
            logger.finest("op=" + op);
            total += op.getDataMemoryCost(node, daf);
        }
        logger.finest("done");
        return total;
    }

    /**
     * Calculates the time cost for a single evaluation of all the operators 
     * in this fragment.  The time cost is based on the maximum cardinality 
     * not the average cardinality.  Does not include the time of the exchange 
     * components including the consumers and producers
     * 
     * Based on the time estimates provided in the OperatorsMetaData file.
     * Includes the cost of copying the tuples to the tray.
     * 
     * @param node The site the operator has been placed on
     * @param daf The distributed-algebraic form of the corresponding 
     * operator tree.
     * @return Sum of the cost of each of the operators
     * @throws OptimizationException 
     */
    public final double getTimeCost(final Site node, final DAF daf, CostParameters costParams) throws OptimizationException {
        long total = 0;
        final Iterator<SensornetOperator> ops = this.operatorIterator(TraversalOrder.PRE_ORDER);
        while (ops.hasNext()) {
            final SensornetOperator op = ops.next();
            logger.finest("op: " + op.toString());
            final double temp = op.getTimeCost(CardinalityType.PHYSICAL_MAX, node, daf);
            logger.finest("ops TimeCost =" + temp);
            total += temp;
        }
        if (!this.isDeliverFragment()) {
            final int cardinality = this.getRootOperator().getCardinality(CardinalityType.PHYSICAL_MAX, node, daf);
            total += cardinality * costParams.getCopyTuple();
        }
        return total;
    }

    public final void setParentExchange(final SensornetExchangeOperator p) {
        this.parentExchange = p;
    }

    public final void addChildExchange(final SensornetExchangeOperator c) {
        if (!this.childExchanges.contains(c)) {
            this.childExchanges.add(c);
        }
    }

    /**
     * Adds an operator to the fragment
     * @param op
     */
    public final void addOperator(final SensornetOperator op) {
        this.operators.add(op);
    }

    /**
     * Adds a sensor network node to execute fragment
     * They are added in order (for good looking display purposes)
     * @param n
     */
    public final void addSite(final Site newSite) {
        boolean addedFlag = false;
        for (int i = 0; i < this.sites.size(); i++) {
            if (Integer.parseInt(this.sites.get(i).getID()) > Integer.parseInt(newSite.getID())) {
                this.sites.add(i, newSite);
                addedFlag = true;
                break;
            }
        }
        if (!addedFlag) {
            this.sites.add(newSite);
        }
    }

    /**
     * Resets the fragment counter (for use when a new query plan is instantiated)
     *
     */
    public static void resetFragmentCounter() {
        fragmentCount = 0;
    }

    public final void addDesiredSite(final Site n) {
        this.desiredSites.add(n.getID());
    }

    public final void addDesiredSites(final int[] nindices, final RT routingTree) {
        for (int element : nindices) {
            this.addDesiredSite((Site) routingTree.getSite(element));
        }
    }

    public final ArrayList<String> getDesiredSites() {
        return this.desiredSites;
    }

    public final String getDesiredSitesString() {
        final Iterator<String> siteIter = this.desiredSites.iterator();
        boolean first = true;
        final StringBuffer s = new StringBuffer();
        while (siteIter.hasNext()) {
            final String siteID = siteIter.next();
            s.append(siteID);
            if (first) {
                first = false;
            } else {
                s.append(",");
            }
        }
        return s.toString();
    }

    /**
     * 
     * @return
     */
    public final int getNumChildFragments() {
        return this.getChildFragments().size();
    }

    /**
     * Merges a child fragment into the current fragment.
     * This will only work if the given fragment is indeed a child fragment
     * of the current fragment, and both fragments have been assigned to the
     * same set of sites.
     * 
     * @param childFrag
     */
    public final void mergeChildFragment(Fragment childFrag) {
        assert (this == childFrag.getParentFragment());
        assert (this.getSites().equals(childFrag.getSites()) || childFrag.getNumSites() == 0);
        for (int i = 0; i < this.childExchanges.size(); i++) {
            SensornetExchangeOperator exch = this.childExchanges.get(0);
            if (exch.getSourceFragment().getID().equals(childFrag.getID()) && exch.getDestFragment().getID().equals(this.getID())) {
                this.childExchanges.remove(i);
            }
        }
        this.childExchanges.addAll(childFrag.childExchanges);
        this.operators.addAll(childFrag.operators);
        Iterator<SensornetOperator> opIter = childFrag.operatorIterator(TraversalOrder.POST_ORDER);
        while (opIter.hasNext()) {
            SensornetOperator op = opIter.next();
            op.setContainingFragment(this);
        }
        Iterator<SensornetExchangeOperator> exchOpIter = this.childExchanges.iterator();
        while (exchOpIter.hasNext()) {
            SensornetExchangeOperator exchOp = exchOpIter.next();
            exchOp.setDestinationFragment(this);
        }
    }

    /**
     * Returns the number of sites that this fragment has been assigned to.
     * @return The number of sites executing this fragment
     */
    public int getNumSites() {
        return this.sites.size();
    }

    public void removeOperator(SensornetOperator op) {
        this.operators.remove(op);
    }
}
