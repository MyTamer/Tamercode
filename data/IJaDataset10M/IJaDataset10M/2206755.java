package de.pangaea.metadataportal.search;

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import de.pangaea.metadataportal.config.*;
import de.pangaea.metadataportal.utils.IndexConstants;
import de.pangaea.metadataportal.utils.LRUMap;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.document.*;

/**
 * Implementation of the caching algorithm behind the <b>panFMP</b> search engine.
 * This class is for internal use only.
 * <p>To configure the cache use the following search properties in your config file <em>(these are the defaults):</em></p>
 *<pre>{@literal
 *<cacheMaxAge>300</cacheMaxAge>
 *<cacheMaxSessions>30</cacheMaxSessions>
 *<indexChangeCheckInterval>30</indexChangeCheckInterval>
 *<reloadIndexIfChangedAfter>60</reloadIndexIfChangedAfter>
 *<keepOldReaderAlive>60</keepOldReaderAlive>
 *<maxStoredQueries>200</maxStoredQueries>
 *}</pre>
 * @author Uwe Schindler
 */
public final class LuceneCache {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(LuceneCache.class);

    private LuceneCache(Config config) {
        this.config = config;
        int maxStoredQueries = Integer.parseInt(config.searchProperties.getProperty("maxStoredQueries", Integer.toString(DEFAULT_MAX_STORED_QUERIES)));
        final Map<UUID, Query> storedQueries = new LRUMap<UUID, Query>(maxStoredQueries);
        this.storedQueries = Collections.synchronizedMap(storedQueries);
        int cacheMaxSessions = Integer.parseInt(config.searchProperties.getProperty("cacheMaxSessions", Integer.toString(DEFAULT_CACHE_MAX_SESSIONS)));
        final Map<String, Session> sessions = new LRUMap<String, Session>(cacheMaxSessions);
        this.sessions = sessions;
        cacheMaxAge = Integer.parseInt(config.searchProperties.getProperty("cacheMaxAge", Integer.toString(DEFAULT_CACHE_MAX_AGE)));
        reloadIndexIfChangedAfter = Integer.parseInt(config.searchProperties.getProperty("reloadIndexIfChangedAfter", Integer.toString(DEFAULT_RELOAD_AFTER)));
        keepOldReaderAlive = Integer.parseInt(config.searchProperties.getProperty("keepOldReaderAlive", Integer.toString(DEFAULT_KEEP_OLD_READER_ALIVE)));
        fetchFactor = Integer.parseInt(config.searchProperties.getProperty("fetchFactor", Integer.toString(DEFAULT_FETCH_FACTOR)));
        warmer = new IndexReaderWarmerImpl(config);
        final long indexChangeCheckInterval = 1000L * (long) (Integer.parseInt(config.searchProperties.getProperty("indexChangeCheckInterval", Integer.toString(DEFAULT_INDEX_CHANGE_CHECK_INTERVAL))));
        timer.schedule(new TimerTask() {

            public void run() {
                try {
                    cleanupCacheTask();
                } catch (IOException ioe) {
                    log.error("Error in timer task: ", ioe);
                }
            }
        }, indexChangeCheckInterval, indexChangeCheckInterval);
    }

    private static final Map<String, LuceneCache> instances = new HashMap<String, LuceneCache>();

    private static final Timer timer = new Timer("LuceneCache Maintenance Tasks", true);

    public static synchronized LuceneCache getInstance(String cfgFile) throws Exception {
        LuceneCache instance = instances.get(cfgFile);
        if (instance == null) {
            Config cfg = new Config(cfgFile, Config.ConfigMode.SEARCH);
            instance = new LuceneCache(cfg);
            instances.put(cfgFile, instance);
            log.info("New LuceneCache instance for config file '" + cfgFile + "' created.");
        }
        return instance;
    }

    private static final String UUID_GEN_PREFIX = "urn:java:" + LuceneCache.class.getName() + '#';

    public UUID storeQuery(final Query query) {
        try {
            final UUID uuid = UUID.nameUUIDFromBytes((UUID_GEN_PREFIX + query.toString(IndexConstants.FIELDNAME_CONTENT)).getBytes("UTF-8"));
            storedQueries.put(uuid, query);
            return uuid;
        } catch (UnsupportedEncodingException ue) {
            throw new RuntimeException(ue);
        }
    }

    public Query readStoredQuery(final UUID uuid) {
        return storedQueries.get(uuid);
    }

    public Session getSession(IndexConfig index, Query query, Sort sort) throws IOException {
        String id = "index=" + index.id + "\000query=" + query.toString(IndexConstants.FIELDNAME_CONTENT) + "\000sort=" + sort;
        Session sess;
        synchronized (sessions) {
            sess = sessions.get(id);
            if (sess == null) {
                sess = new Session(this, index.newSearcher(), query, sort);
                sessions.put(id, sess);
                log.info("Created session: index={" + index.id + "}; query={" + query.toString(IndexConstants.FIELDNAME_CONTENT) + "}; sorting={" + sort + "}");
            } else {
                sess.logAccess();
            }
        }
        return sess;
    }

    private volatile boolean indexChanged = false;

    private volatile TimerTask releaseOldSharedReaderTask = null;

    private void cleanupCacheTask() throws IOException {
        log.debug("cleanupCacheTask() started.");
        boolean changed = false;
        if (!indexChanged) {
            for (IndexConfig cfg : config.indexes.values()) {
                if (cfg instanceof SingleIndexConfig && !cfg.isSharedIndexCurrent()) {
                    changed = true;
                    break;
                }
            }
            indexChanged = changed;
        }
        if (changed) {
            log.info("Detected change in one of the configured indexes. Preparing for reload in " + reloadIndexIfChangedAfter + "s.");
            timer.schedule(new TimerTask() {

                public void run() {
                    synchronized (sessions) {
                        try {
                            for (IndexConfig cfg : config.indexes.values()) {
                                cfg.reopenSharedIndex();
                            }
                        } catch (IOException ioe) {
                            log.error("Failed to reopen shared index readers.", ioe);
                        }
                        indexChanged = false;
                        sessions.clear();
                    }
                    if (releaseOldSharedReaderTask != null) releaseOldSharedReaderTask.cancel();
                    timer.schedule(releaseOldSharedReaderTask = new TimerTask() {

                        public void run() {
                            try {
                                for (IndexConfig cfg : config.indexes.values()) {
                                    cfg.releaseOldSharedReader();
                                }
                            } catch (IOException ioe) {
                                log.error("Failed to release unused shared index readers.", ioe);
                            }
                        }
                    }, ((long) keepOldReaderAlive) * 1000L);
                }
            }, ((long) reloadIndexIfChangedAfter) * 1000L);
        }
        long now = System.currentTimeMillis();
        synchronized (sessions) {
            for (Iterator<Session> entries = sessions.values().iterator(); entries.hasNext(); ) {
                Session e = entries.next();
                if (now - e.lastAccess > ((long) cacheMaxAge) * 1000L) entries.remove();
            }
        }
        log.debug("cleanupCacheTask() finished.");
    }

    public FieldSelector getFieldSelector(boolean loadXml, Collection<String> fieldsToLoad) {
        HashSet<String> set = new HashSet<String>(loadXml ? FIELDS_XML : FIELDS_DEFAULT);
        if (fieldsToLoad == null) {
            for (FieldConfig f : config.fields.values()) {
                if (f.storage != Field.Store.NO) set.add(f.name);
            }
        } else {
            for (String fieldName : fieldsToLoad) {
                FieldConfig f = config.fields.get(fieldName);
                if (f == null) throw new IllegalFieldConfigException("Field name '" + fieldName + "' is unknown!");
                if (f.storage == Field.Store.NO) throw new IllegalFieldConfigException("Field '" + fieldName + "' is not a stored field!");
            }
            set.addAll(fieldsToLoad);
        }
        return new SetBasedFieldSelector(set, Collections.<String>emptySet());
    }

    private int cacheMaxAge = DEFAULT_CACHE_MAX_AGE;

    private int keepOldReaderAlive = DEFAULT_KEEP_OLD_READER_ALIVE;

    private int reloadIndexIfChangedAfter = DEFAULT_RELOAD_AFTER;

    private int fetchFactor = DEFAULT_FETCH_FACTOR;

    private final Map<UUID, Query> storedQueries;

    private final Map<String, Session> sessions;

    public final IndexReaderWarmerImpl warmer;

    public final Config config;

    public static final int DEFAULT_CACHE_MAX_AGE = 5 * 60;

    public static final int DEFAULT_INDEX_CHANGE_CHECK_INTERVAL = 30;

    public static final int DEFAULT_RELOAD_AFTER = 1 * 60;

    public static final int DEFAULT_KEEP_OLD_READER_ALIVE = 1 * 60;

    public static final int DEFAULT_MAX_STORED_QUERIES = 200;

    public static final int DEFAULT_CACHE_MAX_SESSIONS = 30;

    public static final int DEFAULT_FETCH_FACTOR = 250;

    private static final Set<String> FIELDS_DEFAULT = Collections.singleton(IndexConstants.FIELDNAME_IDENTIFIER);

    private static final Set<String> FIELDS_XML = Collections.unmodifiableSet(new TreeSet<String>(Arrays.asList(IndexConstants.FIELDNAME_IDENTIFIER, IndexConstants.FIELDNAME_XML)));

    /**
	 * Implementation of a cache entry.
	 */
    protected static final class Session {

        private Session(LuceneCache parent, Searcher searcher, Query query, Sort sort) {
            this.parent = parent;
            this.searcher = searcher;
            this.query = query;
            this.sort = sort;
            lastAccess = System.currentTimeMillis();
        }

        protected synchronized void ensureFetchable(int neededDoc) throws IOException {
            if (topDocs == null || neededDoc >= fetchedCount) {
                int count;
                if (neededDoc >= Integer.MAX_VALUE / 2) {
                    count = Integer.MAX_VALUE;
                } else {
                    count = (fetchedCount == 0) ? parent.fetchFactor : fetchedCount;
                    while (neededDoc >= count) count *= 2;
                }
                log.debug("Fetching " + count + " top docs...");
                long start = System.currentTimeMillis();
                topDocs = (sort != null) ? searcher.search(query, (Filter) null, count, sort) : searcher.search(query, (Filter) null, count);
                queryTime = System.currentTimeMillis() - start;
                fetchedCount = count;
                parent.warmer.addLifeQuery(query, sort);
            }
        }

        protected synchronized void logAccess() {
            lastAccess = System.currentTimeMillis();
        }

        protected SearchResultList getSearchResultList(boolean loadXml, Collection<String> fieldsToLoad) {
            return new SearchResultList(this, parent.getFieldSelector(loadXml, fieldsToLoad));
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                searcher.close();
                searcher = null;
            } finally {
                super.finalize();
            }
        }

        protected final LuceneCache parent;

        private final Query query;

        private final Sort sort;

        protected Searcher searcher;

        protected long lastAccess;

        protected long queryTime = 0L;

        protected TopDocs topDocs = null;

        protected int fetchedCount = 0;
    }
}
