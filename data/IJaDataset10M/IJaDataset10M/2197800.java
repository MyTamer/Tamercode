package edu.clemson.cs.r2jt.proving;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.clemson.cs.r2jt.absyn.Exp;
import edu.clemson.cs.r2jt.analysis.MathExpTypeResolver;
import edu.clemson.cs.r2jt.proving.absyn.PExp;
import edu.clemson.cs.r2jt.proving.absyn.PSymbol;
import edu.clemson.cs.r2jt.proving.immutableadts.EmptyImmutableList;
import edu.clemson.cs.r2jt.proving.immutableadts.ImmutableList;
import edu.clemson.cs.r2jt.proving.immutableadts.SimpleImmutableList;

public class ImmutableConjuncts implements Iterable<PExp> {

    public static final ImmutableConjuncts EMPTY = new ImmutableConjuncts();

    /**
	 * <p><strong>Invariant:</strong> 
	 * Every <code>Exp</code> in this <code>List</code> has been
	 * run through <code>defensiveCopy()</code>.  It is therefore safe to use
	 * <code>copy()</code> directly when taking things <em>out</em> of this
	 * list, just not when putting things <em>into</em> this list.</p>
	 */
    private final SimpleImmutableList<PExp> myConjuncts;

    private final int myConjunctsSize;

    private Set<String> myCachedSymbolNames;

    private Set<PSymbol> myCachedQuantifiedVariables;

    private List<PExp> myCachedFunctionApplications;

    /**
	 * <p>Creates a new <code>ImmutableConjuncts</code> whose conjuncts are
	 * deep copies of the top-level conjuncts of <code>e</code>.</p>
	 * 
	 * @param e The <code>Exp</code> to break into top-level conjuncts.
	 */
    public ImmutableConjuncts(Exp e, MathExpTypeResolver typer) {
        this(PExp.buildPExp(e, typer).splitIntoConjuncts());
    }

    /**
	 * <p>Creates a new <code>ImmutableConjuncts</code> whose conjuncts are
	 * the top-level conjuncts of <code>e</code>.</p>
	 * 
	 * @param e The <code>PExp</code> to break into top-level conjuncts.
	 */
    public ImmutableConjuncts(PExp e) {
        this(e.splitIntoConjuncts());
        myCachedSymbolNames = e.getSymbolNames();
        myCachedQuantifiedVariables = e.getQuantifiedVariables();
        myCachedFunctionApplications = e.getFunctionApplications();
    }

    /**
	 * <p>Creates a new <code>ImmutableConjuncts</code> whose conjuncts are
	 * deep copies of the <code>Exp</code>s in <code>exps</code>.</p>
	 * 
	 * @param exps A list of <code>Exp</code>s to make the conjuncts of 
	 *             the newly-created <code>ImmutableConjuncts</code>.
	 */
    public ImmutableConjuncts(Iterable<PExp> exps) {
        if (exps instanceof ImmutableConjuncts) {
            ImmutableConjuncts expsAsImmutableConjuncts = (ImmutableConjuncts) exps;
            myConjuncts = expsAsImmutableConjuncts.myConjuncts;
            myCachedSymbolNames = expsAsImmutableConjuncts.myCachedSymbolNames;
            myCachedQuantifiedVariables = expsAsImmutableConjuncts.myCachedQuantifiedVariables;
            myCachedFunctionApplications = expsAsImmutableConjuncts.myCachedFunctionApplications;
        } else {
            List<PExp> newExps = new LinkedList<PExp>();
            for (PExp e : exps) {
                newExps.add(e);
            }
            myConjuncts = new ImmutableList<PExp>(newExps);
        }
        myConjunctsSize = myConjuncts.size();
    }

    /**
	 * <p>Private constructor for making a "blank" 
	 * <code>ImmutableConjuncts</code> for creating singleton subtypes.</p>
	 */
    protected ImmutableConjuncts() {
        List<PExp> empty = Collections.emptyList();
        myConjuncts = new ImmutableList<PExp>(empty);
        myConjunctsSize = 0;
    }

    /**
	 * <p>Private constructor for making an <code>ImmutableConjuncts</code> 
	 * given an immutable list of conjuncts.  Note that expressions in the
	 * immutable list must be considered immutable and have been run through
	 * defensiveCopy!  Just a little performance hack.</p>
	 */
    protected ImmutableConjuncts(SimpleImmutableList<PExp> l) {
        myConjuncts = l;
        myConjunctsSize = myConjuncts.size();
    }

    /**
	 * <p>Private constructor for making an <code>ImmutableConjuncts</code> 
	 * given an immutable list of conjuncts.  Note that expressions in the
	 * immutable list must be considered immutable and have been run through
	 * defensiveCopy!  Just a little performance hack.</p>
	 */
    protected ImmutableConjuncts(PExp[] exps, int length) {
        myConjuncts = new ImmutableList<PExp>(exps, length);
        myConjunctsSize = length;
    }

    /**
	 * <p>Returns a copy of this set of conjuncts with very obviously 
	 * <code>true</code> conjuncts removed.  Examples are the actual "true" 
	 * value and equalities with the same thing on the left and right side.</p>
	 *  
	 * @param expressions The expressions to process.
	 */
    public ImmutableConjuncts eliminateObviousConjuncts() {
        PExp[] workingSpace = new PExp[myConjuncts.size()];
        int curIndex = 0;
        PExp curExp;
        Iterator<PExp> iter = myConjuncts.iterator();
        while (iter.hasNext()) {
            curExp = iter.next();
            if (!curExp.isObviouslyTrue()) {
                workingSpace[curIndex] = curExp;
                curIndex++;
            }
        }
        return new ImmutableConjuncts(workingSpace, curIndex);
    }

    /**
	 * <p>Returns a copy of this set of conjuncts with redundant conjuncts
	 * collapsed.</p>
	 * 
	 * @return A copy of <code>this</code> with redundant conjuncts removed.
	 */
    public ImmutableConjuncts eliminateRedundantConjuncts() {
        int runStart = 0, runLength = 0;
        SimpleImmutableList<PExp> newConjuncts = new EmptyImmutableList<PExp>();
        HashSet<PExp> hashedConjuncts = new HashSet<PExp>();
        Iterator<PExp> conjunctsIter = myConjuncts.iterator();
        PExp curConjunct;
        boolean unique;
        while (conjunctsIter.hasNext()) {
            curConjunct = conjunctsIter.next();
            unique = hashedConjuncts.add(curConjunct);
            if (unique) {
                runLength++;
            } else {
                newConjuncts = appendConjuncts(newConjuncts, runStart, runLength);
                runStart += runLength + 1;
                runLength = 0;
            }
        }
        newConjuncts = appendConjuncts(newConjuncts, runStart, runLength);
        return new ImmutableConjuncts(newConjuncts);
    }

    private SimpleImmutableList<PExp> appendConjuncts(SimpleImmutableList<PExp> original, int startIndex, int length) {
        SimpleImmutableList<PExp> retval;
        switch(length) {
            case 0:
                retval = original;
                break;
            case 1:
                retval = original.appended(myConjuncts.get(startIndex));
                break;
            default:
                retval = original.appended(myConjuncts.subList(startIndex, length));
        }
        return retval;
    }

    /**
	 * <p>Treats the provided List of <code>Exp</code>s as a boolean expression
	 * that is the <em>and</em> of each of the elements in that iterable object.
	 * Tests if that boolean expression contains equal conjuncts in the same 
	 * order as <code>this</code>.</p>
	 * 
	 * @param otherConjuncts The set of conjuncts to test for ordered 
	 * 						 equality.
	 * 
	 * @return True <strong>iff</strong> the given expression is order-equal.
	 */
    public boolean orderEqual(Iterable<PExp> otherConjuncts) {
        boolean retval = true;
        Iterator<PExp> elements = myConjuncts.iterator();
        Iterator<PExp> otherElements = otherConjuncts.iterator();
        while (retval && elements.hasNext() && otherElements.hasNext()) {
            retval = elements.next().equals(otherElements.next());
        }
        return retval && !elements.hasNext() && !otherElements.hasNext();
    }

    /**
	 * <p>Tests if each expression in the given <code>ImmutableConjuncts</code>
	 * has an equal expression in <code>this</code>, and <em>visa versa</em>.
	 * </p>
	 * 
	 * @param otherConjuncts The set of conjuncts to test against 
	 *                       <code>this</code>.
	 * @return True <strong>iff</strong> the given set of conjuncts is equal.
	 */
    public boolean equals(Object o) {
        boolean retval = (o instanceof Iterable<?>);
        if (retval) {
            Iterable<?> conjuncts = (Iterable<?>) o;
            retval = oneWayEquals(conjuncts, this) && oneWayEquals(this, conjuncts);
        }
        return retval;
    }

    /**
	 * <p>Returns a <code>List</code> of the conjuncts in <code>this</code>.</p>
	 * 
	 * @return A mutable copy of <code>this</code>.
	 */
    public List<PExp> getMutableCopy() {
        List<PExp> retval = new LinkedList<PExp>();
        Iterator<PExp> conjuncts = myConjuncts.iterator();
        PExp e;
        while (conjuncts.hasNext()) {
            e = conjuncts.next();
            retval.add(e);
        }
        return retval;
    }

    /**
	 * <p>Answers the question, "Does each <code>Exp</code> in 
	 * <code>query</code> have an equal <code>Exp</code> in <code>base</code>?"
	 * </p>
	 * 
	 * @param query The expressions to test.
	 * @param base The expressions to test against.
	 * 
	 * @return The answer.
	 */
    private static boolean oneWayEquals(Iterable<?> query, Iterable<?> base) {
        boolean retval = true;
        Iterator<?> queryIterator = query.iterator();
        Object curQueryExp;
        while (retval && queryIterator.hasNext()) {
            curQueryExp = queryIterator.next();
            retval = containsEqual(base, curQueryExp);
        }
        return retval;
    }

    /**
	 * <p>Returns <code>true</code> <strong>iff</strong> at least one of the
	 * conjuncts in <code>source</code> is equal to <code>e</code>.</p> 
	 * 
	 * @param source A list of conjuncts to test against.
	 * @param e The <code>Exp</code> to test for equality.
	 * 
	 * @return True <strong>iff</strong> <code>source</code> contains an
	 *         equal conjunct.
	 *         
	 * @throws NullPointerException If <code>source</code> or <code>e</code> is 
	 *                              <code>null</code>.
	 */
    private static boolean containsEqual(Iterable<?> source, Object e) {
        boolean retval = false;
        Iterator<?> sourceIterator = source.iterator();
        while (!retval && sourceIterator.hasNext()) {
            retval = sourceIterator.next().equals(e);
        }
        return retval;
    }

    /**
	 * <p>Returns <code>true</code> <strong>iff</strong> at least one of the
	 * conjuncts in <code>this</code> is equal to <code>e</code>.</p> 
	 * 
	 * @param e The <code>Exp</code> to test for equality.
	 * 
	 * @return True <strong>iff</strong> <code>this</code> contains an
	 *         equal conjunct.
	 *         
	 * @throws NullPointerException If <code>e</code> is <code>null</code>.
	 */
    public boolean containsEqual(PExp e) {
        return containsEqual(this, e);
    }

    public String toString() {
        String retval = "";
        boolean first = true;
        for (PExp e : this) {
            if (!first) {
                retval += " and \n";
            }
            retval += (e.toString());
            first = false;
        }
        if (retval == "") {
            retval = "True";
        }
        retval += "\n";
        return retval;
    }

    @Override
    public Iterator<PExp> iterator() {
        return myConjuncts.iterator();
    }

    /**
	 * <p>Returns a copy of <code>this</code> with all sub-expressions
	 * equal to some key in <code>mapping</code> replaced with the corresponding 
	 * value.</p>
	 * 
	 * @param mapping The mapping to use for substituting.
	 * 
	 * @return A new <code>ImmutableConjuncts</code> with the mapping applied.
	 */
    public ImmutableConjuncts substitute(Map<PExp, PExp> mapping) {
        List<PExp> retvalConjuncts = new LinkedList<PExp>();
        Iterator<PExp> conjuncts = myConjuncts.iterator();
        PExp c;
        while (conjuncts.hasNext()) {
            c = conjuncts.next();
            retvalConjuncts.add(c.substitute(mapping));
        }
        return new ImmutableConjuncts(retvalConjuncts);
    }

    public ImmutableConjuncts overwritten(int index, PExp newConjunct) {
        return new ImmutableConjuncts(myConjuncts.set(index, newConjunct));
    }

    public ImmutableConjuncts inserted(int index, ImmutableConjuncts c) {
        return new ImmutableConjuncts(myConjuncts.insert(index, c.myConjuncts));
    }

    /**
	 * <p>Returns a copy of <code>this</code> with <code>e</code>s appended at
	 * the end of the list of conjuncts.</p>
	 * 
	 * @param e The expression to append.
	 * 
	 * @return An <code>ImmutableConjuncts</code> with size <code>this.size() +
	 *         1</code> whose first <code>this.size()</code> conjuncts are
	 *         copies of <code>this</code>'s conjuncts, and whose last conjunct
	 *         is a copy of <code>e</code>.
	 */
    public ImmutableConjuncts appended(PExp e) {
        return new ImmutableConjuncts(myConjuncts.appended(e));
    }

    /**
	 * <p>Returns a copy of <code>this</code> with the <code>Exp</code>s from
	 * <code>i</code> appended at the end of the list of conjuncts.</p>
	 * 
	 * @param i The expressions to append.
	 * 
	 * @return An <code>ImmutableConjuncts</code> whose first 
	 *         <code>this.size()</code> conjuncts are copies of 
	 *         <code>this</code>'s conjuncts, and whose other conjuncts are
	 *         copies of the <code>Exp</code>s in <code>i</code>, in order. 
	 */
    public ImmutableConjuncts appended(Iterable<PExp> i) {
        ImmutableConjuncts retval;
        if (i instanceof ImmutableConjuncts) {
            SimpleImmutableList<PExp> iConjuncts = ((ImmutableConjuncts) i).myConjuncts;
            if (iConjuncts.size() == 0) {
                retval = this;
            } else {
                SimpleImmutableList<PExp> newExps = myConjuncts.appended(iConjuncts);
                retval = new ImmutableConjuncts(newExps);
            }
        } else {
            Iterator<PExp> iIterator = i.iterator();
            if (iIterator.hasNext()) {
                retval = new ImmutableConjuncts(i);
            } else {
                retval = this;
            }
        }
        return retval;
    }

    /**
	 * <p>Returns a new <code>ImmutableConjuncts</code> derived from this one,
	 * whose conjuncts are all the conjuncts of <code>this</code> except the
	 * <code>index</code>th.</p>
	 *
	 * @param indexToRemove The index to remove.
	 * 
	 * @return A new <code>ImmutableConjuncts</code> without the 
	 *         <code>index</code>th one.
	 *         
	 * @throws IndexOutOfBoundsException If the provided index is out of bounds.
	 */
    public ImmutableConjuncts removed(int indexToRemove) {
        return new ImmutableConjuncts(myConjuncts.removed(indexToRemove));
    }

    /**
	 * <p>Returns a new <code>ImmutableConjuncts</code> derived from this one,
	 * whose conjuncts are the conjuncts of <code>this</code> starting at the
	 * <code>start</code>th conjunct (zero-based) and containing the following 
	 * <code>length</code> conjuncts.  If <code>start + length</code> exceeds
	 * <code>this.size()</code>, the returned <code>ImmutableConjuncts</code>
	 * will contain the following <code>size - start</code> conjuncts instead.
	 * </p>
	 * 
	 * <p>If <code>start >= this.size()</code>, will return an 
	 * <code>ImmutableConjuncts</code> with <code>size() == 0</code>.</p>
	 * 
	 * @param start The index of the first conjunct to include (zero-based).
	 * @param length The number of conjuncts from that point to include.
	 * 
	 * @return A new <code>ImmutableConjuncts</code> containing those conjuncts
	 *         starting at <code>start</code> and extending to include the next
	 *         <code>length</code> conjuncts, or all the remaining conjuncts if
	 *         <code>length</code> extends past the end of the list.
	 *         
	 * @throws IndexOutOfBoundsException If either <code>start</code> or
	 *             <code>length</code> is negative.
	 */
    public ImmutableConjuncts subConjuncts(int start, int length) {
        if (start > myConjunctsSize) {
            start = myConjunctsSize;
        }
        if (start + length > myConjunctsSize) {
            length = myConjunctsSize - start;
        }
        return new ImmutableConjuncts(myConjuncts.subList(start, length));
    }

    /**
	 * <p>Returns a deep copy of the <code>index</code>th conjunct in 
	 * <code>this</code>.</p>
	 * 
	 * @param index The index of the conjunct to retrieve, zero-based.
	 * 
	 * @return A deep copy of the <code>index</code>th conjunct in 
	 *         <code>this</code>.
	 *         
	 * @throws IndexOutOfBoundsException If the provided index is less than zero
	 *            or equal to or greater than <code>size()</code>.
	 */
    public PExp get(int index) {
        return myConjuncts.get(index);
    }

    /**
	 * <p>Returns the number of conjuncts in <code>this</code>.</p>
	 * 
	 * @return The number of conjuncts.
	 */
    public int size() {
        return myConjuncts.size();
    }

    public ImmutableConjuncts flipQuantifiers() {
        ImmutableConjuncts retval;
        PExp[] workingSpace = new PExp[myConjuncts.size()];
        boolean conjunctChanged = false;
        int curIndex = 0;
        Iterator<PExp> conjunctIter = myConjuncts.iterator();
        PExp curConjunct;
        while (conjunctIter.hasNext()) {
            curConjunct = conjunctIter.next();
            workingSpace[curIndex] = curConjunct.flipQuantifiers();
            conjunctChanged |= (workingSpace[curIndex] != curConjunct);
            curIndex++;
        }
        if (conjunctChanged) {
            retval = new ImmutableConjuncts(workingSpace, myConjunctsSize);
        } else {
            retval = this;
        }
        return retval;
    }

    public boolean containsQuantifiedVariableNotIn(ImmutableConjuncts o) {
        Set<PSymbol> oVariables = o.getQuantifiedVariables();
        Set<PSymbol> thisVariables = this.getQuantifiedVariables();
        return !oVariables.containsAll(thisVariables);
    }

    public Set<PSymbol> getQuantifiedVariables() {
        if (myCachedQuantifiedVariables == null) {
            myCachedQuantifiedVariables = new HashSet<PSymbol>();
            Iterator<PExp> conjunctsIter = myConjuncts.iterator();
            while (conjunctsIter.hasNext()) {
                myCachedQuantifiedVariables.addAll(conjunctsIter.next().getQuantifiedVariables());
            }
        }
        return myCachedQuantifiedVariables;
    }

    public List<PExp> getFunctionApplications() {
        if (myCachedFunctionApplications == null) {
            myCachedFunctionApplications = new LinkedList<PExp>();
            Iterator<PExp> conjunctsIter = myConjuncts.iterator();
            while (conjunctsIter.hasNext()) {
                myCachedFunctionApplications.addAll(conjunctsIter.next().getFunctionApplications());
            }
        }
        return myCachedFunctionApplications;
    }

    public Set<String> getSymbolNames() {
        if (myCachedSymbolNames == null) {
            myCachedSymbolNames = new HashSet<String>();
            Iterator<PExp> conjunctsIter = myConjuncts.iterator();
            while (conjunctsIter.hasNext()) {
                myCachedSymbolNames.addAll(conjunctsIter.next().getSymbolNames());
            }
        }
        return myCachedSymbolNames;
    }
}
