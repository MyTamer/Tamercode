package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.NoSuchElementException;

/** An iterator which returns RDF nodes.
 *
 * <p>RDF iterators are standard Java iterators, except that they
 *    have an extra method that returns specifically typed objects,
 *    in this case RDF nodes, and have a <CODE>close()</CODE> method. 
 *    thatshould be called to free resources if the application does
 *    not complete the iteration.</p>
 * @author bwm
 * @version Release='$Name:  $' Revision='$Revision: 1.9 $' Date='$Date: 2006/03/22 13:53:12 $'
 */
public interface NodeIterator extends ExtendedIterator {

    /** Determine if there any more values in the iteration.
     .
     * @return true if and only if there are more values available
     * from the iteration.
     */
    public boolean hasNext();

    /** Return the next RDFNode of the iteration.
     * @throws NoSuchElementException if there are no more nodes to be returned.
     .
     * @return The next RDFNode from the iteration.
     */
    public Object next() throws NoSuchElementException;

    /** Return the next RDFNode of the iteration.
     * @throws NoSuchElementException if there are no more nodes to be returned.
     .
     * @return The next RDFNode from the iteration.
     */
    public RDFNode nextNode() throws NoSuchElementException;

    /** Unsupported Operation.
     * @throws NoSuchElementException
     */
    public void remove() throws NoSuchElementException;

    /** Terminate the iteration and free up resources.
     *
     * <p>Some implementations, e.g. on relational databases, hold resources while
     * the iterator still exists.  These will normally be freed when the iteration
     * completes.  However, if an application wishes to ensure they are freed without
     * completing the iteration, this method should be called.</p>
     .
     */
    public void close();
}
