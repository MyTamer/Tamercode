package com.amazon.carbonado.repo.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import com.amazon.carbonado.Cursor;
import com.amazon.carbonado.FetchException;
import com.amazon.carbonado.FetchInterruptedException;
import com.amazon.carbonado.FetchTimeoutException;
import com.amazon.carbonado.PersistException;
import com.amazon.carbonado.PersistInterruptedException;
import com.amazon.carbonado.PersistTimeoutException;
import com.amazon.carbonado.Query;
import com.amazon.carbonado.Repository;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storable;
import com.amazon.carbonado.Storage;
import com.amazon.carbonado.SupportException;
import com.amazon.carbonado.Trigger;
import com.amazon.carbonado.capability.IndexInfo;
import com.amazon.carbonado.cursor.ArraySortBuffer;
import com.amazon.carbonado.cursor.ControllerCursor;
import com.amazon.carbonado.cursor.EmptyCursor;
import com.amazon.carbonado.cursor.FilteredCursor;
import com.amazon.carbonado.cursor.SingletonCursor;
import com.amazon.carbonado.cursor.SortBuffer;
import com.amazon.carbonado.cursor.SortedCursor;
import com.amazon.carbonado.filter.Filter;
import com.amazon.carbonado.filter.FilterValues;
import com.amazon.carbonado.filter.RelOp;
import com.amazon.carbonado.sequence.SequenceValueProducer;
import com.amazon.carbonado.gen.DelegateStorableGenerator;
import com.amazon.carbonado.gen.DelegateSupport;
import com.amazon.carbonado.gen.MasterFeature;
import com.amazon.carbonado.util.QuickConstructorGenerator;
import com.amazon.carbonado.info.Direction;
import com.amazon.carbonado.info.OrderedProperty;
import com.amazon.carbonado.info.StorableIndex;
import com.amazon.carbonado.info.StorableInfo;
import com.amazon.carbonado.info.StorableIntrospector;
import com.amazon.carbonado.qe.BoundaryType;
import com.amazon.carbonado.qe.QueryExecutorFactory;
import com.amazon.carbonado.qe.QueryEngine;
import com.amazon.carbonado.qe.StorageAccess;
import com.amazon.carbonado.spi.IndexInfoImpl;
import com.amazon.carbonado.spi.LobEngine;
import com.amazon.carbonado.spi.TriggerManager;
import com.amazon.carbonado.txn.TransactionScope;

/**
 * 
 *
 * @author Brian S O'Neill
 */
class MapStorage<S extends Storable> implements Storage<S>, DelegateSupport<S>, StorageAccess<S> {

    private static final int DEFAULT_LOB_BLOCK_SIZE = 1000;

    private static final Object[] NO_VALUES = new Object[0];

    private final MapRepository mRepo;

    private final StorableInfo<S> mInfo;

    private final TriggerManager<S> mTriggers;

    private final InstanceFactory mInstanceFactory;

    private final StorableIndex<S> mPrimaryKeyIndex;

    private final QueryEngine<S> mQueryEngine;

    private final int mLockTimeout;

    private final TimeUnit mLockTimeoutUnit;

    private final ConcurrentNavigableMap<Key<S>, S> mMap;

    private final Comparator<S> mFullComparator;

    private final Comparator<S>[] mSearchComparators;

    private final Key.Assigner<S> mKeyAssigner;

    /**
     * Simple lock which is reentrant for transactions, but auto-commit does not
     * need to support reentrancy. Read lock requests in transactions can starve
     * write lock requests, but auto-commit cannot cause starvation. In practice
     * starvation is not possible since transactions always lock for upgrade.
     */
    final UpgradableLock<Object> mLock = new UpgradableLock<Object>() {

        @Override
        protected boolean isReadLockHeld(Object locker) {
            return locker instanceof MapTransaction;
        }
    };

    MapStorage(MapRepository repo, Class<S> type, int lockTimeout, TimeUnit lockTimeoutUnit) throws SupportException {
        mRepo = repo;
        mInfo = StorableIntrospector.examine(type);
        mTriggers = new TriggerManager<S>();
        EnumSet<MasterFeature> features;
        if (repo.isMaster()) {
            features = EnumSet.of(MasterFeature.INSERT_CHECK_REQUIRED, MasterFeature.NORMALIZE, MasterFeature.VERSIONING, MasterFeature.INSERT_SEQUENCES);
        } else {
            features = EnumSet.of(MasterFeature.INSERT_CHECK_REQUIRED, MasterFeature.NORMALIZE);
        }
        Class<? extends S> delegateStorableClass = DelegateStorableGenerator.getDelegateClass(type, features);
        mInstanceFactory = QuickConstructorGenerator.getInstance(delegateStorableClass, InstanceFactory.class);
        mPrimaryKeyIndex = new StorableIndex<S>(mInfo.getPrimaryKey(), Direction.ASCENDING).clustered(true);
        mQueryEngine = new QueryEngine<S>(type, repo);
        mLockTimeout = lockTimeout;
        mLockTimeoutUnit = lockTimeoutUnit;
        mMap = new ConcurrentSkipListMap<Key<S>, S>();
        List<OrderedProperty<S>> propList = createPkPropList();
        mFullComparator = SortedCursor.createComparator(propList);
        mSearchComparators = new Comparator[propList.size() + 1];
        mSearchComparators[propList.size()] = mFullComparator;
        mKeyAssigner = Key.getAssigner(type);
        try {
            if (LobEngine.hasLobs(type)) {
                Trigger<S> lobTrigger = repo.getLobEngine().getSupportTrigger(type, DEFAULT_LOB_BLOCK_SIZE);
                addTrigger(lobTrigger);
            }
            mTriggers.addTriggers(type, repo.mTriggerFactories);
        } catch (SupportException e) {
            throw e;
        } catch (RepositoryException e) {
            throw new SupportException(e);
        }
    }

    public Class<S> getStorableType() {
        return mInfo.getStorableType();
    }

    public S prepare() {
        return (S) mInstanceFactory.instantiate(this);
    }

    public Query<S> query() throws FetchException {
        return mQueryEngine.query();
    }

    public Query<S> query(String filter) throws FetchException {
        return mQueryEngine.query(filter);
    }

    public Query<S> query(Filter<S> filter) throws FetchException {
        return mQueryEngine.query(filter);
    }

    public void truncate() throws PersistException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForWrite(scope);
                try {
                    mMap.clear();
                } finally {
                    mLock.unlockFromWrite(scope);
                }
            } else {
                txn.lockForWrite(mLock);
                mMap.clear();
            }
        } catch (PersistException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    public boolean addTrigger(Trigger<? super S> trigger) {
        return mTriggers.addTrigger(trigger);
    }

    public boolean removeTrigger(Trigger<? super S> trigger) {
        return mTriggers.removeTrigger(trigger);
    }

    public IndexInfo[] getIndexInfo() {
        StorableIndex<S> pkIndex = mPrimaryKeyIndex;
        if (pkIndex == null) {
            return new IndexInfo[0];
        }
        int i = pkIndex.getPropertyCount();
        String[] propertyNames = new String[i];
        Direction[] directions = new Direction[i];
        while (--i >= 0) {
            propertyNames[i] = pkIndex.getProperty(i).getName();
            directions[i] = pkIndex.getPropertyDirection(i);
        }
        return new IndexInfo[] { new IndexInfoImpl(getStorableType().getName(), true, true, propertyNames, directions) };
    }

    public boolean doTryLoad(S storable) throws FetchException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForRead(scope);
                try {
                    return doTryLoadNoLock(storable);
                } finally {
                    mLock.unlockFromRead(scope);
                }
            } else {
                final boolean isForUpdate = scope.isForUpdate();
                txn.lockForUpgrade(mLock, isForUpdate);
                try {
                    return doTryLoadNoLock(storable);
                } finally {
                    txn.unlockFromUpgrade(mLock, isForUpdate);
                }
            }
        } catch (FetchException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchException(e);
        }
    }

    boolean doTryLoadNoLock(S storable) {
        S existing = mMap.get(new Key<S>(storable, mFullComparator));
        if (existing == null) {
            return false;
        } else {
            storable.markAllPropertiesDirty();
            existing.copyAllProperties(storable);
            storable.markAllPropertiesClean();
            return true;
        }
    }

    public boolean doTryInsert(S storable) throws PersistException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForUpgrade(scope);
                try {
                    return doTryInsertNoLock(storable);
                } finally {
                    mLock.unlockFromUpgrade(scope);
                }
            } else {
                txn.lockForWrite(mLock);
                if (doTryInsertNoLock(storable)) {
                    txn.inserted(this, storable);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (PersistException e) {
            throw e;
        } catch (FetchException e) {
            throw e.toPersistException();
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    private boolean doTryInsertNoLock(S storable) {
        S copy = (S) storable.prepare();
        storable.copyAllProperties(copy);
        copy.markAllPropertiesClean();
        Key<S> key = new Key<S>(copy, mFullComparator);
        S existing = mMap.get(key);
        if (existing != null) {
            return false;
        }
        mMap.put(key, copy);
        storable.markAllPropertiesClean();
        return true;
    }

    public boolean doTryUpdate(S storable) throws PersistException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForWrite(scope);
                try {
                    return doTryUpdateNoLock(storable);
                } finally {
                    mLock.unlockFromWrite(scope);
                }
            } else {
                txn.lockForWrite(mLock);
                S existing = mMap.get(new Key<S>(storable, mFullComparator));
                if (existing == null) {
                    return false;
                } else {
                    txn.updated(this, (S) existing.copy());
                    existing.markAllPropertiesDirty();
                    storable.copyDirtyProperties(existing);
                    existing.markAllPropertiesClean();
                    storable.markAllPropertiesDirty();
                    existing.copyAllProperties(storable);
                    storable.markAllPropertiesClean();
                    return true;
                }
            }
        } catch (PersistException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    private boolean doTryUpdateNoLock(S storable) {
        S existing = mMap.get(new Key<S>(storable, mFullComparator));
        if (existing == null) {
            return false;
        } else {
            existing.markAllPropertiesDirty();
            storable.copyDirtyProperties(existing);
            existing.markAllPropertiesClean();
            storable.markAllPropertiesDirty();
            existing.copyAllProperties(storable);
            storable.markAllPropertiesClean();
            return true;
        }
    }

    public boolean doTryDelete(S storable) throws PersistException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForUpgrade(scope);
                try {
                    return doTryDeleteNoLock(storable);
                } finally {
                    mLock.unlockFromUpgrade(scope);
                }
            } else {
                txn.lockForWrite(mLock);
                S existing = mMap.remove(new Key<S>(storable, mFullComparator));
                if (existing == null) {
                    return false;
                } else {
                    txn.deleted(this, existing);
                    return true;
                }
            }
        } catch (PersistException e) {
            throw e;
        } catch (FetchException e) {
            throw e.toPersistException();
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    private boolean doTryDeleteNoLock(S storable) {
        return mMap.remove(new Key<S>(storable, mFullComparator)) != null;
    }

    void mapPut(S storable) {
        mMap.put(new Key<S>(storable, mFullComparator), storable);
    }

    void mapRemove(S storable) {
        mMap.remove(new Key<S>(storable, mFullComparator));
    }

    private void doLockForRead(Object locker) throws FetchException {
        try {
            if (!mLock.tryLockForRead(locker, mLockTimeout, mLockTimeoutUnit)) {
                throw new FetchTimeoutException("" + mLockTimeout + ' ' + mLockTimeoutUnit.toString().toLowerCase());
            }
        } catch (InterruptedException e) {
            throw new FetchInterruptedException(e);
        }
    }

    private void doLockForUpgrade(Object locker) throws FetchException {
        try {
            if (!mLock.tryLockForUpgrade(locker, mLockTimeout, mLockTimeoutUnit)) {
                throw new FetchTimeoutException("" + mLockTimeout + ' ' + mLockTimeoutUnit.toString().toLowerCase());
            }
        } catch (InterruptedException e) {
            throw new FetchInterruptedException(e);
        }
    }

    private void doLockForWrite(Object locker) throws PersistException {
        try {
            if (!mLock.tryLockForWrite(locker, mLockTimeout, mLockTimeoutUnit)) {
                throw new PersistTimeoutException("" + mLockTimeout + ' ' + mLockTimeoutUnit.toString().toLowerCase());
            }
        } catch (InterruptedException e) {
            throw new PersistInterruptedException(e);
        }
    }

    public Repository getRootRepository() {
        return mRepo.getRootRepository();
    }

    public boolean isPropertySupported(String propertyName) {
        return mInfo.getAllProperties().containsKey(propertyName);
    }

    public Trigger<? super S> getInsertTrigger() {
        return mTriggers.getInsertTrigger();
    }

    public Trigger<? super S> getUpdateTrigger() {
        return mTriggers.getUpdateTrigger();
    }

    public Trigger<? super S> getDeleteTrigger() {
        return mTriggers.getDeleteTrigger();
    }

    public Trigger<? super S> getLoadTrigger() {
        return mTriggers.getLoadTrigger();
    }

    public void locallyDisableLoadTrigger() {
        mTriggers.locallyDisableLoad();
    }

    public void locallyEnableLoadTrigger() {
        mTriggers.locallyEnableLoad();
    }

    public SequenceValueProducer getSequenceValueProducer(String name) throws PersistException {
        try {
            return mRepo.getSequenceValueProducer(name);
        } catch (RepositoryException e) {
            throw e.toPersistException();
        }
    }

    public QueryExecutorFactory<S> getQueryExecutorFactory() {
        return mQueryEngine;
    }

    public Collection<StorableIndex<S>> getAllIndexes() {
        return Collections.singletonList(mPrimaryKeyIndex);
    }

    public Storage<S> storageDelegate(StorableIndex<S> index) {
        return null;
    }

    public long countAll() throws FetchException {
        return countAll(null);
    }

    public long countAll(Query.Controller controller) throws FetchException {
        try {
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForRead(scope);
                try {
                    return mMap.size();
                } finally {
                    mLock.unlockFromRead(scope);
                }
            } else {
                final boolean isForUpdate = scope.isForUpdate();
                txn.lockForUpgrade(mLock, isForUpdate);
                try {
                    return mMap.size();
                } finally {
                    txn.unlockFromUpgrade(mLock, isForUpdate);
                }
            }
        } catch (FetchException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchException(e);
        }
    }

    public Cursor<S> fetchAll() throws FetchException {
        try {
            return new MapCursor<S>(this, mRepo.localTransactionScope(), mMap.values());
        } catch (FetchException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchException(e);
        }
    }

    public Cursor<S> fetchAll(Query.Controller controller) throws FetchException {
        return ControllerCursor.apply(fetchAll(), controller);
    }

    public Cursor<S> fetchOne(StorableIndex<S> index, Object[] identityValues) throws FetchException {
        return fetchOne(index, identityValues, null);
    }

    public Cursor<S> fetchOne(StorableIndex<S> index, Object[] identityValues, Query.Controller controller) throws FetchException {
        try {
            S key = prepare();
            for (int i = 0; i < identityValues.length; i++) {
                key.setPropertyValue(index.getProperty(i).getName(), identityValues[i]);
            }
            TransactionScope<MapTransaction> scope = mRepo.localTransactionScope();
            MapTransaction txn = scope.getTxn();
            if (txn == null) {
                doLockForRead(scope);
                try {
                    S value = mMap.get(new Key<S>(key, mFullComparator));
                    if (value == null) {
                        return EmptyCursor.the();
                    } else {
                        return new SingletonCursor<S>(copyAndFireLoadTrigger(value));
                    }
                } finally {
                    mLock.unlockFromRead(scope);
                }
            } else {
                final boolean isForUpdate = scope.isForUpdate();
                txn.lockForUpgrade(mLock, isForUpdate);
                try {
                    S value = mMap.get(new Key<S>(key, mFullComparator));
                    if (value == null) {
                        return EmptyCursor.the();
                    } else {
                        return new SingletonCursor<S>(copyAndFireLoadTrigger(value));
                    }
                } finally {
                    txn.unlockFromUpgrade(mLock, isForUpdate);
                }
            }
        } catch (FetchException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchException(e);
        }
    }

    S copyAndFireLoadTrigger(S storable) throws FetchException {
        storable = (S) storable.copy();
        Trigger<? super S> trigger = getLoadTrigger();
        if (trigger != null) {
            trigger.afterLoad(storable);
            storable.markAllPropertiesClean();
        }
        return storable;
    }

    public Query<?> indexEntryQuery(StorableIndex<S> index) {
        return null;
    }

    public Cursor<S> fetchFromIndexEntryQuery(StorableIndex<S> index, Query<?> indexEntryQuery) {
        return null;
    }

    public Cursor<S> fetchFromIndexEntryQuery(StorableIndex<S> index, Query<?> indexEntryQuery, Query.Controller controller) {
        return null;
    }

    public Cursor<S> fetchSubset(StorableIndex<S> index, Object[] identityValues, BoundaryType rangeStartBoundary, Object rangeStartValue, BoundaryType rangeEndBoundary, Object rangeEndValue, boolean reverseRange, boolean reverseOrder) throws FetchException {
        if (identityValues == null) {
            identityValues = NO_VALUES;
        }
        NavigableMap<Key<S>, S> map = mMap;
        int tieBreaker = 1;
        if (reverseOrder) {
            map = map.descendingMap();
            reverseRange = !reverseRange;
            tieBreaker = -tieBreaker;
        }
        if (reverseRange) {
            BoundaryType t1 = rangeStartBoundary;
            rangeStartBoundary = rangeEndBoundary;
            rangeEndBoundary = t1;
            Object t2 = rangeStartValue;
            rangeStartValue = rangeEndValue;
            rangeEndValue = t2;
        }
        tail: {
            Key<S> startKey;
            switch(rangeStartBoundary) {
                case OPEN:
                default:
                    if (identityValues.length == 0) {
                        break tail;
                    } else {
                        startKey = searchKey(-tieBreaker, identityValues);
                    }
                    break;
                case INCLUSIVE:
                    startKey = searchKey(-tieBreaker, identityValues, rangeStartValue);
                    break;
                case EXCLUSIVE:
                    startKey = searchKey(tieBreaker, identityValues, rangeStartValue);
                    break;
            }
            Key<S> ceilingKey = map.ceilingKey(startKey);
            if (ceilingKey == null) {
                return EmptyCursor.the();
            }
            map = map.tailMap(ceilingKey, true);
        }
        Cursor<S> cursor;
        try {
            cursor = new MapCursor<S>(this, mRepo.localTransactionScope(), map.values());
        } catch (FetchException e) {
            throw e;
        } catch (Exception e) {
            throw new FetchException(e);
        }
        Filter<S> filter;
        FilterValues<S> filterValues;
        if (rangeEndBoundary == BoundaryType.OPEN) {
            if (identityValues.length == 0) {
                filter = null;
                filterValues = null;
            } else {
                filter = Filter.getOpenFilter(getStorableType());
                for (int i = 0; i < identityValues.length; i++) {
                    filter = filter.and(index.getProperty(i).getName(), RelOp.EQ);
                }
                filterValues = filter.initialFilterValues();
                for (int i = 0; i < identityValues.length; i++) {
                    filterValues = filterValues.with(identityValues[i]);
                }
            }
        } else {
            filter = Filter.getOpenFilter(getStorableType());
            int i = 0;
            for (; i < identityValues.length; i++) {
                filter = filter.and(index.getProperty(i).getName(), RelOp.EQ);
            }
            RelOp rangeOp;
            if (reverseRange) {
                rangeOp = rangeEndBoundary == BoundaryType.INCLUSIVE ? RelOp.GE : RelOp.GT;
            } else {
                rangeOp = rangeEndBoundary == BoundaryType.INCLUSIVE ? RelOp.LE : RelOp.LT;
            }
            filter = filter.and(index.getProperty(i).getName(), rangeOp);
            filterValues = filter.initialFilterValues();
            for (i = 0; i < identityValues.length; i++) {
                filterValues = filterValues.with(identityValues[i]);
            }
            filterValues = filterValues.with(rangeEndValue);
        }
        if (filter != null) {
            cursor = FilteredCursor.applyFilter(filter, filterValues, cursor);
        }
        return cursor;
    }

    public Cursor<S> fetchSubset(StorableIndex<S> index, Object[] identityValues, BoundaryType rangeStartBoundary, Object rangeStartValue, BoundaryType rangeEndBoundary, Object rangeEndValue, boolean reverseRange, boolean reverseOrder, Query.Controller controller) throws FetchException {
        return ControllerCursor.apply(fetchSubset(index, identityValues, rangeStartBoundary, rangeStartValue, rangeEndBoundary, rangeEndValue, reverseRange, reverseOrder), controller);
    }

    private List<OrderedProperty<S>> createPkPropList() {
        return new ArrayList<OrderedProperty<S>>(mInfo.getPrimaryKey().getProperties());
    }

    private Key<S> searchKey(int tieBreaker, Object[] identityValues) {
        S storable = prepare();
        mKeyAssigner.setKeyValues(storable, identityValues);
        Comparator<S> c = getSearchComparator(identityValues.length);
        return new SearchKey<S>(tieBreaker, storable, c);
    }

    private Key<S> searchKey(int tieBreaker, Object[] identityValues, Object rangeValue) {
        S storable = prepare();
        mKeyAssigner.setKeyValues(storable, identityValues, rangeValue);
        Comparator<S> c = getSearchComparator(identityValues.length + 1);
        return new SearchKey<S>(tieBreaker, storable, c);
    }

    private Comparator<S> getSearchComparator(int propertyCount) {
        Comparator<S> comparator = mSearchComparators[propertyCount];
        if (comparator == null) {
            List<OrderedProperty<S>> propList = createPkPropList().subList(0, propertyCount);
            if (propList.size() > 0) {
                comparator = SortedCursor.createComparator(propList);
            } else {
                comparator = SortedCursor.createComparator(getStorableType());
            }
            mSearchComparators[propertyCount] = comparator;
        }
        return comparator;
    }

    public SortBuffer<S> createSortBuffer() {
        return new ArraySortBuffer<S>();
    }

    public SortBuffer<S> createSortBuffer(Query.Controller controller) {
        return new ArraySortBuffer<S>();
    }

    public static interface InstanceFactory {

        Storable instantiate(DelegateSupport support);
    }

    private static class SearchKey<S extends Storable> extends Key<S> {

        private final int mTieBreaker;

        SearchKey(int tieBreaker, S storable, Comparator<S> comparator) {
            super(storable, comparator);
            mTieBreaker = tieBreaker;
        }

        @Override
        protected int tieBreaker() {
            return mTieBreaker;
        }

        @Override
        public String toString() {
            return super.toString() + ", tieBreaker=" + mTieBreaker;
        }
    }
}
