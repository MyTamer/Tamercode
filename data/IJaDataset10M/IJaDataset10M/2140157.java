package com.amazon.carbonado.qe;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.filter.Filter;
import com.amazon.carbonado.filter.PropertyFilter;
import com.amazon.carbonado.filter.RelOp;
import com.amazon.carbonado.info.ChainedProperty;
import com.amazon.carbonado.info.Direction;
import static com.amazon.carbonado.info.Direction.*;
import com.amazon.carbonado.info.OrderedProperty;
import com.amazon.carbonado.info.StorableIndex;

/**
 * Evaluates an index for how well it matches a query's desired ordering. An
 * ordering score is not a single absolute value – instead it has a relative
 * weight when compared to other scores.
 *
 * <p>An index matches a desired ordering if the arrangement of properties
 * matches. Not all properties of the index need to be used, however. Also,
 * gaps in the arrangement are allowed if a property identity filter
 * matches. A property identity filter is of the form {@code "a = ?"}.
 *
 * <p>An OrderingScore measures the number of ordering properties that are
 * matched and the number that are remaining. If there are remainder
 * properties, then the user of the evaluated index will need to perform a
 * post-sort operation to achieve the desired results.
 *
 * <p>In general, an OrderingScore is better than another if it has more
 * matched properties and fewer remainder properties. Index clustering,
 * property count, and natural order is also considered.
 *
 * @author Brian S O'Neill
 * @see FilteringScore
 * @see CompositeScore
 */
public class OrderingScore<S extends Storable> {

    /**
     * Evaluates the given index for its ordering capabilities against the
     * given filter and order-by properties.
     *
     * @param index index to evaluate
     * @param filter optional filter which cannot contain any logical 'or' operations.
     * @param ordering optional properties which define desired ordering
     * @throws IllegalArgumentException if index is null or filter is not supported
     */
    public static <S extends Storable> OrderingScore<S> evaluate(StorableIndex<S> index, Filter<S> filter, OrderingList<S> ordering) {
        if (index == null) {
            throw new IllegalArgumentException("Index required");
        }
        return evaluate(index.getOrderedProperties(), index.isUnique(), index.isClustered(), filter, ordering);
    }

    /**
     * Evaluates the given index properties for its ordering capabilities
     * against the given filter and order-by properties.
     *
     * @param indexProperties index properties to evaluate
     * @param unique true if index is unique
     * @param clustered true if index is clustered
     * @param filter optional filter which cannot contain any logical 'or' operations.
     * @param ordering optional properties which define desired ordering
     * @throws IllegalArgumentException if index is null or filter is not supported
     */
    public static <S extends Storable> OrderingScore<S> evaluate(OrderedProperty<S>[] indexProperties, boolean unique, boolean clustered, Filter<S> filter, OrderingList<S> ordering) {
        if (indexProperties == null) {
            throw new IllegalArgumentException("Index properties required");
        }
        List<PropertyFilter<S>> filterList = PropertyFilterList.get(filter);
        if (ordering == null) {
            ordering = OrderingList.emptyList();
        }
        Set<ChainedProperty<S>> identityPropSet = new HashSet<ChainedProperty<S>>(filterList.size());
        for (PropertyFilter<S> propFilter : filterList) {
            if (propFilter.getOperator() == RelOp.EQ) {
                identityPropSet.add(propFilter.getChainedProperty());
            }
        }
        OrderingList<S> handledOrdering = OrderingList.emptyList();
        OrderingList<S> remainderOrdering = OrderingList.emptyList();
        OrderingList<S> freeOrdering = OrderingList.emptyList();
        OrderingList<S> unusedOrdering = OrderingList.emptyList();
        for (int i = 0; i < indexProperties.length; i++) {
            OrderedProperty<S> indexProp = indexProperties[i];
            ChainedProperty<S> indexChained = indexProp.getChainedProperty();
            if (identityPropSet.contains(indexChained)) {
                unusedOrdering = unusedOrdering.concat(indexProp.direction(UNSPECIFIED));
            }
        }
        uniquelyCheck: if (unique) {
            for (int i = 0; i < indexProperties.length; i++) {
                ChainedProperty<S> indexChained = indexProperties[i].getChainedProperty();
                if (!identityPropSet.contains(indexChained)) {
                    break uniquelyCheck;
                }
            }
            return new OrderingScore<S>(indexProperties, clustered, handledOrdering, remainderOrdering, false, freeOrdering, unusedOrdering);
        }
        Boolean shouldReverseOrder = null;
        Set<ChainedProperty<S>> seen = new HashSet<ChainedProperty<S>>();
        boolean gap = false;
        int indexPos = 0;
        calcScore: for (int i = 0; i < ordering.size(); i++) {
            OrderedProperty<S> property = ordering.get(i);
            ChainedProperty<S> chained = property.getChainedProperty();
            if (seen.contains(chained)) {
                continue calcScore;
            }
            seen.add(chained);
            if (identityPropSet.contains(chained)) {
                continue calcScore;
            }
            indexPosMatch: while (!gap && indexPos < indexProperties.length) {
                OrderedProperty<S> indexProp = indexProperties[indexPos];
                ChainedProperty<S> indexChained = indexProp.getChainedProperty();
                if (chained.equals(indexChained)) {
                    Direction indexDir = indexProp.getDirection();
                    if (indexDir == UNSPECIFIED) {
                        indexDir = ASCENDING;
                    }
                    if (shouldReverseOrder != null && shouldReverseOrder) {
                        indexDir = indexDir.reverse();
                    }
                    if (property.getDirection() == UNSPECIFIED) {
                        property = property.direction(indexDir);
                    } else if (shouldReverseOrder == null) {
                        shouldReverseOrder = indexDir != property.getDirection();
                        if (shouldReverseOrder) {
                            handledOrdering = handledOrdering.reverseDirections();
                        }
                    } else if (indexDir != property.getDirection()) {
                        break indexPosMatch;
                    }
                    handledOrdering = handledOrdering.concat(property);
                    indexPos++;
                    continue calcScore;
                }
                if (identityPropSet.contains(indexChained)) {
                    indexPos++;
                    continue indexPosMatch;
                }
                break indexPosMatch;
            }
            remainderOrdering = remainderOrdering.concat(property);
            gap = true;
        }
        while (indexPos < indexProperties.length) {
            OrderedProperty<S> freeProp = indexProperties[indexPos];
            ChainedProperty<S> freeChained = freeProp.getChainedProperty();
            if (!identityPropSet.contains(freeChained)) {
                if (shouldReverseOrder == null) {
                    freeProp = freeProp.direction(UNSPECIFIED);
                } else {
                    Direction freePropDir = freeProp.getDirection();
                    if (freePropDir == UNSPECIFIED) {
                        freePropDir = ASCENDING;
                    }
                    if (shouldReverseOrder) {
                        freeProp = freeProp.direction(freePropDir.reverse());
                    }
                }
                freeOrdering = freeOrdering.concat(freeProp);
            }
            indexPos++;
        }
        if (shouldReverseOrder == null) {
            shouldReverseOrder = false;
        }
        return new OrderingScore<S>(indexProperties, clustered, handledOrdering, remainderOrdering, shouldReverseOrder, freeOrdering, unusedOrdering);
    }

    /**
     * Returns a comparator which determines which OrderingScores are
     * better. It does not matter if the scores were evaluated for different
     * indexes or storable types. The comparator returns {@code <0} if first
     * score is better, {@code 0} if equal, or {@code >0} if second is better.
     */
    public static Comparator<OrderingScore<?>> fullComparator() {
        return Full.INSTANCE;
    }

    private final OrderedProperty<S>[] mIndexProperties;

    private final boolean mIndexClustered;

    private final OrderingList<S> mHandledOrdering;

    private final OrderingList<S> mRemainderOrdering;

    private final boolean mShouldReverseOrder;

    private final OrderingList<S> mFreeOrdering;

    private final OrderingList<S> mUnusedOrdering;

    private OrderingScore(OrderedProperty<S>[] indexProperties, boolean indexClustered, OrderingList<S> handledOrdering, OrderingList<S> remainderOrdering, boolean shouldReverseOrder, OrderingList<S> freeOrdering, OrderingList<S> unusedOrdering) {
        mIndexProperties = indexProperties;
        mIndexClustered = indexClustered;
        mHandledOrdering = handledOrdering;
        mRemainderOrdering = remainderOrdering;
        mShouldReverseOrder = shouldReverseOrder;
        mFreeOrdering = freeOrdering;
        mUnusedOrdering = unusedOrdering;
    }

    private OrderingScore(OrderingScore<S> score, OrderingList<S> remainderOrdering) {
        mIndexProperties = score.mIndexProperties;
        mIndexClustered = score.mIndexClustered;
        mHandledOrdering = score.mHandledOrdering;
        mRemainderOrdering = remainderOrdering;
        mShouldReverseOrder = score.mShouldReverseOrder;
        mFreeOrdering = score.mFreeOrdering;
        mUnusedOrdering = score.mUnusedOrdering;
    }

    /**
     * Returns true if evaluated index is clustered. Scans of clustered indexes
     * are generally faster.
     */
    public boolean isIndexClustered() {
        return mIndexClustered;
    }

    /**
     * Returns the amount of properties in the evaluated index.
     */
    public int getIndexPropertyCount() {
        return mIndexProperties.length;
    }

    /**
     * Returns the number of desired orderings the evaluated index
     * supports. The number of orderings is reduced to eliminate redundancies.
     */
    public int getHandledCount() {
        return mHandledOrdering.size();
    }

    /**
     * Returns the ordering properties that the evaluated index supports. The
     * list of orderings is reduced to eliminate redundancies. If any handled
     * ordering properties originally had an unspecified direction, the correct
     * direction is specified in this list.
     *
     * @return handled orderings, never null
     */
    public OrderingList<S> getHandledOrdering() {
        return mHandledOrdering;
    }

    /**
     * Returns the number of desired orderings the evaluated index does not
     * support. The number of orderings is reduced to eliminate redundancies.
     * When the remainder count is non-zero, a query plan which uses the
     * evaluated index must perform a sort.
     */
    public int getRemainderCount() {
        return mRemainderOrdering.size();
    }

    /**
     * Returns the ordering properties that the evaluated index does not
     * support. The list of orderings is reduced to eliminate redundancies.
     *
     * @return remainder orderings, never null
     */
    public OrderingList<S> getRemainderOrdering() {
        return mRemainderOrdering;
    }

    /**
     * Returns true if evaluated index must be iterated in reverse to achieve
     * the desired ordering.
     */
    public boolean shouldReverseOrder() {
        return mShouldReverseOrder;
    }

    /**
     * Returns potential ordering properties that the evaluated index can
     * handle, if arranged to immediately follow the handled orderings. The
     * direction of any free orderings may be UNSPECIFIED, which indicates that
     * specific order is not relevant.
     *
     * @return free orderings, never null
     */
    public OrderingList<S> getFreeOrdering() {
        return mFreeOrdering;
    }

    /**
     * Returns unused ordering properties of the evaluated index because they
     * were filtered out. The direction of each unused ordering is UNSPECIFIED
     * because specific order is not relevant.
     *
     * @return unused orderings, never null
     */
    public OrderingList<S> getUnusedOrdering() {
        return mUnusedOrdering;
    }

    /**
     * Returns true if the given score uses an index exactly the same as this
     * one. The only allowed differences are in the count of remainder
     * orderings.
     */
    public boolean canMergeRemainderOrdering(OrderingScore<S> other) {
        if (this == other || (getHandledCount() == 0 && other.getHandledCount() == 0)) {
            return true;
        }
        if (isIndexClustered() == other.isIndexClustered() && getIndexPropertyCount() == other.getIndexPropertyCount() && shouldReverseOrder() == other.shouldReverseOrder() && getHandledOrdering().equals(other.getHandledOrdering())) {
            OrderingList<S> thisRemainderOrdering = getRemainderOrdering();
            OrderingList<S> otherRemainderOrdering = other.getRemainderOrdering();
            int size = Math.min(thisRemainderOrdering.size(), otherRemainderOrdering.size());
            for (int i = 0; i < size; i++) {
                if (!thisRemainderOrdering.get(i).equals(otherRemainderOrdering.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Merges the remainder orderings of this score with the one given. Call
     * canMergeRemainderOrdering first to verify if the merge makes any sense.
     */
    public OrderingList<S> mergeRemainderOrdering(OrderingScore<S> other) {
        OrderingList<S> thisRemainderOrdering = getRemainderOrdering();
        if (this == other) {
            return thisRemainderOrdering;
        }
        OrderingList<S> otherRemainderOrdering = other.getRemainderOrdering();
        if (thisRemainderOrdering.size() == 0) {
            return otherRemainderOrdering;
        } else {
            if (otherRemainderOrdering.size() == 0) {
                return thisRemainderOrdering;
            } else if (thisRemainderOrdering.size() >= otherRemainderOrdering.size()) {
                return thisRemainderOrdering;
            } else {
                return otherRemainderOrdering;
            }
        }
    }

    /**
     * Returns a new OrderingScore with the remainder replaced. Handled count
     * is not recalculated.
     *
     * @since 1.2
     */
    public OrderingScore<S> withRemainderOrdering(OrderingList<S> ordering) {
        return new OrderingScore<S>(this, ordering);
    }

    @Override
    public String toString() {
        return "OrderingScore {handledCount=" + getHandledCount() + ", remainderCount=" + getRemainderCount() + ", shouldReverseOrder=" + shouldReverseOrder() + '}';
    }

    private static class Full implements Comparator<OrderingScore<?>> {

        static final Comparator<OrderingScore<?>> INSTANCE = new Full();

        public int compare(OrderingScore<?> first, OrderingScore<?> second) {
            if (first == second) {
                return 0;
            }
            int result = FilteringScore.nullCompare(first, second);
            if (result != 0) {
                return result;
            }
            double firstRatio, otherRatio;
            {
                int total = first.getHandledCount() + first.getRemainderCount();
                firstRatio = ((double) first.getHandledCount()) / total;
            }
            {
                int total = second.getHandledCount() + second.getRemainderCount();
                otherRatio = ((double) second.getHandledCount()) / total;
            }
            if (firstRatio > otherRatio) {
                return -1;
            } else if (firstRatio < otherRatio) {
                return 1;
            }
            if (Double.isNaN(firstRatio)) {
                if (Double.isNaN(otherRatio)) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (Double.isNaN(otherRatio)) {
                return -1;
            }
            if (first.isIndexClustered()) {
                if (!second.isIndexClustered()) {
                    return -1;
                }
            } else if (second.isIndexClustered()) {
                return 1;
            }
            if (first.getIndexPropertyCount() < second.getIndexPropertyCount()) {
                return -1;
            } else if (first.getIndexPropertyCount() > second.getIndexPropertyCount()) {
                return 1;
            }
            if (first.shouldReverseOrder()) {
                if (!second.shouldReverseOrder()) {
                    return 1;
                }
            } else if (second.shouldReverseOrder()) {
                return -1;
            }
            return 0;
        }
    }
}
