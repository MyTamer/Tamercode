package org.bbop.util;

import java.util.Comparator;
import java.util.PriorityQueue;
import org.apache.log4j.*;

public class NBestPriorityQueue<T> extends PriorityQueue<T> {

    protected static final Logger logger = Logger.getLogger(NBestPriorityQueue.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1117073236905007271L;

    protected int maxSize;

    public NBestPriorityQueue(Comparator<T> comparator, int maxSize) {
        this(comparator, maxSize, maxSize);
    }

    public NBestPriorityQueue(Comparator<T> comparator, int maxSize, int initialSize) {
        super(initialSize, comparator);
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public boolean add(T o) {
        if (shouldBeAdded(o)) return super.add(o); else return false;
    }

    @Override
    public boolean offer(T o) {
        if (shouldBeAdded(o)) return super.offer(o); else return false;
    }

    protected boolean shouldBeAdded(T o) {
        if (size() >= maxSize) {
            T minObj = peek();
            if (comparator().compare(minObj, o) >= 0) return false;
        }
        return true;
    }
}
