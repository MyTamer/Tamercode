package com.emc.atmos.sync.plugins;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import com.emc.atmos.sync.TaskNode;
import com.emc.atmos.sync.TaskResult;
import com.emc.atmos.sync.util.AtmosMetadata;
import com.emc.atmos.sync.util.CountingInputStream;
import com.emc.esu.api.DirectoryEntry;
import com.emc.esu.api.EsuApi;
import com.emc.esu.api.EsuException;
import com.emc.esu.api.Identifier;
import com.emc.esu.api.ListOptions;
import com.emc.esu.api.ObjectId;
import com.emc.esu.api.ObjectMetadata;
import com.emc.esu.api.ObjectPath;
import com.emc.esu.api.ServiceInformation;
import com.emc.esu.api.rest.LBEsuRestApi;

/**
 * Reads objects from an Atmos system.
 * @author cwikj
 */
public class AtmosSource extends MultithreadedSource implements InitializingBean {

    private static final Logger l4j = Logger.getLogger(AtmosSource.class);

    /**
	 * This pattern is used to activate this plugin.
	 */
    public static final String URI_PATTERN = "^(http|https)://([a-zA-Z0-9/\\-]+):([a-zA-Z0-9\\+/=]+)@([^/]*?)(:[0-9]+)?(?:/)?$";

    public static final String SOURCE_NAMESPACE_OPTION = "source-namespace";

    public static final String SOURCE_NAMESPACE_DESC = "The source within the Atmos namespace.  Note that a directory must end with a trailing slash (e.g. /dir1/dir2/) otherwise it will be interpreted as a single file.  Not compatible with source-oid-list.";

    public static final String SOURCE_NAMESPACE_ARG_NAME = "atmos-path";

    public static final String SOURCE_OIDLIST_OPTION = "source-oid-list";

    public static final String SOURCE_OIDLIST_DESC = "The file containing the list of OIDs to copy (newline separated).  Use - to read the list from standard input.  Not compatible with source-namespace";

    public static final String SOURCE_OIDLIST_ARG_NAME = "filename";

    public static final String SOURCE_NAMELIST_OPTION = "source-name-list";

    public static final String SOURCE_NAMELIST_DESC = "The file containing the list of namespace paths to copy (newline separated).  Use - to read the list from standard input.  Not compatible with source-namespace";

    public static final String SOURCE_NAMELIST_ARG_NAME = "filename";

    public static final String SOURCE_QUERY_OPTION = "source-query";

    public static final String SOURCE_QUERY_DESC = "The xquery string to use to select the OIDs to copy.  Note that the XQuery service must be enabled on your Atmos system for this to work.  Not compatible with source-namespace or source-oidlist";

    public static final String SOURCE_QUERY_ARG_NAME = "xquery-string";

    private List<String> hosts;

    private String protocol;

    private int port;

    private String uid;

    private String secret;

    private EsuApi atmos;

    private String namespaceRoot;

    private String oidFile;

    private String query;

    private String nameFile;

    public AtmosSource() {
    }

    /**
	 * @see com.emc.atmos.sync.plugins.SyncPlugin#getOptions()
	 */
    @SuppressWarnings("static-access")
    @Override
    public Options getOptions() {
        Options opts = new Options();
        opts.addOption(OptionBuilder.withDescription(SOURCE_NAMESPACE_DESC).withLongOpt(SOURCE_NAMESPACE_OPTION).hasArg().withArgName(SOURCE_NAMESPACE_ARG_NAME).create());
        opts.addOption(OptionBuilder.withDescription(SOURCE_OIDLIST_DESC).withLongOpt(SOURCE_OIDLIST_OPTION).hasArg().withArgName(SOURCE_OIDLIST_ARG_NAME).create());
        opts.addOption(OptionBuilder.withDescription(SOURCE_QUERY_DESC).withLongOpt(SOURCE_QUERY_OPTION).hasArg().withArgName(SOURCE_QUERY_ARG_NAME).create());
        opts.addOption(OptionBuilder.withDescription(SOURCE_NAMELIST_DESC).withLongOpt(SOURCE_NAMELIST_OPTION).hasArg().withArgName(SOURCE_NAMELIST_ARG_NAME).create());
        return opts;
    }

    @Override
    public boolean parseOptions(CommandLine line) {
        if (line.hasOption(CommonOptions.SOURCE_OPTION)) {
            Pattern p = Pattern.compile(URI_PATTERN);
            String source = line.getOptionValue(CommonOptions.SOURCE_OPTION);
            Matcher m = p.matcher(source);
            if (!m.matches()) {
                LogMF.debug(l4j, "{0} does not match {1}", source, p);
                return false;
            }
            protocol = m.group(1);
            uid = m.group(2);
            secret = m.group(3);
            String sHost = m.group(4);
            String sPort = null;
            if (m.groupCount() == 5) {
                sPort = m.group(5);
            }
            hosts = Arrays.asList(sHost.split(","));
            if (sPort != null) {
                port = Integer.parseInt(sPort.substring(1));
            } else {
                if ("https".equals(protocol)) {
                    port = 443;
                } else {
                    port = 80;
                }
            }
            boolean namespace = line.hasOption(SOURCE_NAMESPACE_OPTION);
            boolean objectlist = line.hasOption(SOURCE_OIDLIST_OPTION);
            boolean querylist = line.hasOption(SOURCE_QUERY_OPTION);
            boolean namelist = line.hasOption(SOURCE_NAMELIST_OPTION);
            if (namespace && objectlist || namespace && querylist || objectlist && querylist || namelist && namespace) {
                throw new IllegalArgumentException(MessageFormat.format("Only one of (--{0}, --{1}, --{2}, --{3}) is allowed", SOURCE_NAMESPACE_OPTION, SOURCE_OIDLIST_OPTION, SOURCE_QUERY_OPTION, SOURCE_NAMELIST_OPTION));
            }
            if (!(namespace || objectlist || querylist || namelist)) {
                throw new IllegalArgumentException(MessageFormat.format("One of (--{0}, --{1}, --{2}, --{3}) must be specified", SOURCE_NAMESPACE_OPTION, SOURCE_OIDLIST_OPTION, SOURCE_QUERY_OPTION, SOURCE_NAMELIST_OPTION));
            }
            if (namespace) {
                namespaceRoot = line.getOptionValue(SOURCE_NAMESPACE_OPTION);
            }
            if (objectlist) {
                oidFile = line.getOptionValue(SOURCE_OIDLIST_OPTION);
                if ("-".equals(oidFile)) {
                } else {
                    File f = new File(oidFile);
                    if (!f.exists()) {
                        throw new IllegalArgumentException(MessageFormat.format("The OID list file {0} does not exist", oidFile));
                    }
                }
            }
            if (querylist) {
                query = line.getOptionValue(SOURCE_QUERY_OPTION);
            }
            if (namelist) {
                nameFile = line.getOptionValue(SOURCE_NAMELIST_OPTION);
            }
            super.parseOptions(line);
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notEmpty(hosts);
        Assert.hasText(protocol);
        Assert.hasText(secret);
        Assert.hasText(uid);
    }

    /**
	 * @see com.emc.atmos.sync.plugins.SyncPlugin#validateChain(com.emc.atmos.sync.plugins.SyncPlugin)
	 */
    @Override
    public void validateChain(SyncPlugin first) {
    }

    /**
	 * @see com.emc.atmos.sync.plugins.SyncPlugin#getName()
	 */
    @Override
    public String getName() {
        return "Atmos Source";
    }

    @Override
    public String getDocumentation() {
        return "The Atmos source plugin is triggered by the source pattern:\n" + "http://uid:secret@host[:port]  or\n" + "https://uid:secret@host[:port]\n" + "Note that the uid should be the 'full token ID' including the " + "subtenant ID and the uid concatenated by a slash\n" + "If you want to software load balance across multiple hosts, " + "you can provide a comma-delimited list of hostnames or IPs " + "in the host part of the URI.\n" + "This source plugin is multithreaded and you should use the " + "--source-threads option to specify how many threads to use." + " The default thread count is one.";
    }

    public void run() {
        running = true;
        if (atmos == null) {
            atmos = new LBEsuRestApi(hosts, port, uid, secret);
            ((LBEsuRestApi) atmos).setProtocol(protocol);
        }
        ServiceInformation info = atmos.getServiceInformation();
        LogMF.info(l4j, "Connected to Atmos {0} on {1}", info.getAtmosVersion(), hosts);
        if (info.isUnicodeMetadataSupported()) {
            ((LBEsuRestApi) atmos).setUnicodeEnabled(true);
            l4j.info("Unicode metadata enabled");
        }
        if (namespaceRoot != null) {
            readNamespace();
        } else if (oidFile != null) {
            readOIDs();
        } else if (query != null) {
            readQuery();
        } else if (nameFile != null) {
            readNames();
        } else {
            throw new IllegalStateException("One of namespaceRoot, oidFile, or query must be set");
        }
    }

    private void readQuery() {
        initQueue();
        ObjectId lastItem = null;
        while (running) {
            synchronized (graph) {
                BreadthFirstIterator<TaskNode, DefaultEdge> i = new BreadthFirstIterator<TaskNode, DefaultEdge>(graph);
                while (i.hasNext()) {
                    TaskNode t = i.next();
                    if (graph.inDegreeOf(t) == 0 && !t.isQueued()) {
                        t.setQueued(true);
                        l4j.debug("Submitting " + t);
                        pool.submit(t);
                    }
                }
            }
            if (queue.size() > threadCount * 100) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                continue;
            }
            String token = "0";
            if (lastItem != null) {
                LogMF.info(l4j, "Getting more results starting with {0}", lastItem);
                token = lastItem.toString();
            }
            List<ObjectId> ids = queryItems(query, token);
            if (ids.size() == 0) {
                l4j.info("No more results");
                break;
            }
            LogMF.info(l4j, "Found {0} results", ids.size());
            for (ObjectId id : ids) {
                ReadAtmosTask task = new ReadAtmosTask(id);
                task.addToGraph(graph);
                lastItem = id;
            }
        }
        runQueue();
    }

    private List<ObjectId> queryItems(String query, String token) {
        query += "|||token:" + token;
        LogMF.info(l4j, "Executing query: {0}", query);
        List<ObjectId> ids = null;
        try {
            ids = atmos.queryObjects(query);
        } catch (EsuException e) {
            if (e.getAtmosCode() == 1009) {
                throw new RuntimeException("Your Atmos is not configured to accept queries.  Please call support.");
            } else {
                throw e;
            }
        }
        return ids;
    }

    private void readOIDs() {
        initQueue();
        BufferedReader br;
        try {
            if ("-".equals(oidFile)) {
                br = new BufferedReader(new InputStreamReader(System.in));
            } else {
                br = new BufferedReader(new FileReader(new File(oidFile)));
            }
            while (running) {
                synchronized (graph) {
                    BreadthFirstIterator<TaskNode, DefaultEdge> i = new BreadthFirstIterator<TaskNode, DefaultEdge>(graph);
                    while (i.hasNext()) {
                        TaskNode t = i.next();
                        if (graph.inDegreeOf(t) == 0 && !t.isQueued()) {
                            t.setQueued(true);
                            l4j.debug("Submitting " + t);
                            pool.submit(t);
                        }
                    }
                }
                if (queue.size() > threadCount * 10) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                ObjectId id = new ObjectId(line.trim());
                ReadAtmosTask task = new ReadAtmosTask(id);
                task.addToGraph(graph);
            }
            runQueue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(5);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(6);
        }
    }

    private void readNames() {
        initQueue();
        BufferedReader br;
        try {
            if ("-".equals(nameFile)) {
                br = new BufferedReader(new InputStreamReader(System.in));
            } else {
                br = new BufferedReader(new FileReader(new File(nameFile)));
            }
            while (running) {
                synchronized (graph) {
                    BreadthFirstIterator<TaskNode, DefaultEdge> i = new BreadthFirstIterator<TaskNode, DefaultEdge>(graph);
                    while (i.hasNext()) {
                        TaskNode t = i.next();
                        if (graph.inDegreeOf(t) == 0 && !t.isQueued()) {
                            t.setQueued(true);
                            l4j.debug("Submitting " + t);
                            pool.submit(t);
                        }
                    }
                }
                if (queue.size() > threadCount * 10) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                ObjectPath op = new ObjectPath(line.trim());
                ReadAtmosTask task = new ReadAtmosTask(op);
                task.addToGraph(graph);
            }
            runQueue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(5);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(6);
        }
    }

    private void readNamespace() {
        initQueue();
        ReadAtmosTask rootTask = new ReadAtmosTask(new ObjectPath(namespaceRoot));
        rootTask.addToGraph(graph);
        runQueue();
        if (!running) {
            pool.shutdownNow();
        }
    }

    @Override
    public void terminate() {
        running = false;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public EsuApi getAtmos() {
        return atmos;
    }

    public void setAtmos(EsuApi atmos) {
        this.atmos = atmos;
    }

    /**
	 * This is the core task node that executes inside the thread pool.  It
	 * handles the dependencies when enumerating directories.
	 */
    class ReadAtmosTask extends TaskNode {

        private Identifier id;

        public ReadAtmosTask(Identifier id) {
            this.id = id;
        }

        @Override
        protected TaskResult execute() throws Exception {
            if (id instanceof ObjectPath) {
                ObjectPath op = (ObjectPath) id;
                if (op.toString().equals("/apache/")) {
                    return new TaskResult(true);
                }
                if (op.isDirectory()) {
                    listDirectory(op);
                }
            }
            AtmosSyncObject obj = new AtmosSyncObject(id);
            try {
                getNext().filter(obj);
                complete(obj);
            } catch (Exception e) {
                failed(obj, e);
                return TaskResult.FAILURE;
            }
            return TaskResult.SUCCESS;
        }

        /**
		 * Lists the namespace directory and enqueues task nodes for each
		 * child.
		 */
        private void listDirectory(ObjectPath op) {
            ListOptions options = new ListOptions();
            List<DirectoryEntry> ents = atmos.listDirectory(op, options);
            if (options.getToken() != null) {
                ents.addAll(atmos.listDirectory(op, options));
            }
            for (DirectoryEntry ent : ents) {
                ReadAtmosTask child = new ReadAtmosTask(ent.getPath());
                child.addParent(this);
                child.addToGraph(graph);
            }
        }
    }

    /**
	 * Encapsulates the information needed for reading from Atmos and does
	 * some lazy loading of data.
	 */
    class AtmosSyncObject extends SyncObject {

        private Identifier sourceId;

        private CountingInputStream in;

        private String relativePath;

        private boolean metadataLoaded;

        public AtmosSyncObject(Identifier sourceId) throws URISyntaxException {
            this.sourceId = sourceId;
            SourceAtmosId ann = new SourceAtmosId();
            if (sourceId instanceof ObjectPath) {
                setSourceURI(new URI(protocol, uid + ":" + secret, hosts.get(0), port, sourceId.toString(), null, null));
                if (((ObjectPath) sourceId).isDirectory()) {
                    setDirectory(true);
                }
                ann.setPath((ObjectPath) sourceId);
                String sourcePath = sourceId.toString();
                if (namespaceRoot != null) {
                    if (sourcePath.startsWith(namespaceRoot)) {
                        sourcePath = sourcePath.substring(namespaceRoot.length());
                    }
                }
                if (sourcePath.startsWith("/")) {
                    sourcePath = sourcePath.substring(1);
                }
                relativePath = sourcePath;
            } else {
                setSourceURI(new URI(protocol, uid + ":" + secret, hosts.get(0), port, "/" + sourceId.toString(), null, null));
                ann.setId((ObjectId) sourceId);
                relativePath = sourceId.toString();
            }
            metadataLoaded = false;
            this.addAnnotation(ann);
        }

        @Override
        public InputStream getInputStream() {
            if (in == null) {
                if (sourceId instanceof ObjectPath && ((ObjectPath) sourceId).isDirectory()) {
                    in = new CountingInputStream(new ByteArrayInputStream(new byte[0]));
                    setSize(0);
                } else {
                    try {
                        in = new CountingInputStream(atmos.readObjectStream(sourceId, null));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to get input " + "stream for " + sourceId + ": " + e.getMessage(), e);
                    }
                }
            }
            return in;
        }

        public long getBytesRead() {
            if (in != null) {
                return in.getBytesRead();
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "AtmosSyncObject: " + sourceId;
        }

        @Override
        public long getSize() {
            if (!metadataLoaded) {
                getMeta();
            }
            return super.getSize();
        }

        @Override
        public AtmosMetadata getMetadata() {
            if (!metadataLoaded) {
                getMeta();
            }
            return super.getMetadata();
        }

        /**
		 * Query Atmos for the object's metadata.
		 */
        private void getMeta() {
            if (sourceId instanceof ObjectPath && ((ObjectPath) sourceId).isDirectory()) {
                AtmosMetadata am = new AtmosMetadata();
                setSize(0);
                am.setContentType(null);
                am.setMetadata(atmos.getUserMetadata(sourceId, null));
                am.setSystemMetadata(atmos.getSystemMetadata(sourceId, null));
                setMetadata(am);
            } else {
                ObjectMetadata meta = atmos.getAllMetadata(sourceId);
                AtmosMetadata am = AtmosMetadata.fromObjectMetadata(meta);
                setMetadata(am);
                if (am.getSystemMetadata().getMetadata("size") != null) {
                    setSize(Long.parseLong(am.getSystemMetadata().getMetadata("size").getValue()));
                }
            }
            metadataLoaded = true;
        }

        @Override
        public String getRelativePath() {
            return relativePath;
        }
    }

    /**
	 * @return the namespaceRoot
	 */
    public String getNamespaceRoot() {
        return namespaceRoot;
    }

    /**
	 * @param namespaceRoot the namespaceRoot to set
	 */
    public void setNamespaceRoot(String namespaceRoot) {
        this.namespaceRoot = namespaceRoot;
    }

    /**
	 * @return the oidFile
	 */
    public String getOidFile() {
        return oidFile;
    }

    /**
	 * @param oidFile the oidFile to set
	 */
    public void setOidFile(String oidFile) {
        this.oidFile = oidFile;
    }

    /**
	 * @return the query
	 */
    public String getQuery() {
        return query;
    }

    /**
	 * @param query the query to set
	 */
    public void setQuery(String query) {
        this.query = query;
    }
}
