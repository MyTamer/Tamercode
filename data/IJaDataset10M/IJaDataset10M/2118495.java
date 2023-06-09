package net.sf.saxon.expr;

import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.Item;
import net.sf.saxon.trans.XPathException;

/**
* Iterator that concatenates the results of two supplied iterators
*/
public class AppendIterator implements SequenceIterator {

    private SequenceIterator first;

    private SequenceIterable second;

    private XPathContext context;

    private SequenceIterator currentIterator;

    private int position = 0;

    /**
     * This form of constructor is designed to delay getting an iterator for the second
     * expression until it is actually needed. This gives savings in cases where the
     * iteration is aborted prematurely.
     * @param first Iterator over the first operand
     * @param second The second operand
     * @param context The dynamic context for evaluation of the second operand
     */
    public AppendIterator(SequenceIterator first, SequenceIterable second, XPathContext context) {
        this.first = first;
        this.second = second;
        this.context = context;
        this.currentIterator = first;
    }

    public Item next() throws XPathException {
        Item n = currentIterator.next();
        if (n == null && currentIterator == first) {
            currentIterator = second.iterate(context);
            n = currentIterator.next();
        }
        if (n == null) {
            position = -1;
        } else {
            position++;
        }
        return n;
    }

    public Item current() {
        return currentIterator.current();
    }

    public int position() {
        return position;
    }

    public void close() {
    }

    public SequenceIterator getAnother() throws XPathException {
        return new AppendIterator(first.getAnother(), second, context);
    }

    /**
     * Get properties of this iterator, as a bit-significant integer.
     *
     * @return the properties of this iterator. This will be some combination of
     *         properties such as {@link SequenceIterator#GROUNDED}, {@link SequenceIterator#LAST_POSITION_FINDER},
     *         and {@link SequenceIterator#LOOKAHEAD}. It is always
     *         acceptable to return the value zero, indicating that there are no known special properties.
     *         It is acceptable for the properties of the iterator to change depending on its state.
     */
    public int getProperties() {
        return 0;
    }
}
