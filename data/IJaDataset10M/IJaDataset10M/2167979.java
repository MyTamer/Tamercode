package org.ourgrid.system.units;

import static java.io.File.separator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.ourgrid.broker.BrokerComponent;
import org.ourgrid.broker.BrokerComponentContextFactory;
import org.ourgrid.broker.BrokerConfiguration;
import org.ourgrid.broker.BrokerConstants;
import org.ourgrid.broker.status.JobStatusInfo;
import org.ourgrid.broker.status.PeerStatusInfo;
import org.ourgrid.broker.status.WorkerStatusInfo;
import org.ourgrid.broker.ui.sync.BrokerSyncApplicationClient;
import org.ourgrid.broker.ui.sync.BrokerSyncManagerClient;
import org.ourgrid.broker.ui.sync.command.BrokerStatusCommand;
import org.ourgrid.common.interfaces.management.BrokerManager;
import org.ourgrid.common.interfaces.to.BrokerCompleteStatus;
import org.ourgrid.common.job.JobCounter;
import org.ourgrid.common.job.PersistentJobCounter;
import org.ourgrid.common.job.SimpleJobCounter;
import org.ourgrid.common.spec.job.JobSpec;
import org.ourgrid.common.spec.peer.PeerSpec;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.contextfactory.PropertiesFileParser;
import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncApplicationClient;
import br.edu.ufcg.lsd.commune.network.xmpp.CommuneNetworkException;
import br.edu.ufcg.lsd.commune.network.xmpp.XMPPProperties;
import br.edu.ufcg.lsd.commune.processor.ProcessorStartException;

public class BrokerUnit extends AbstractUnit {

    private JobCounter jobCounter;

    private int maxReplicas;

    private int maxFails;

    private int maxBlFails;

    private int numREs;

    private BrokerSyncApplicationClient uiManager;

    public static final String BROKER_PROPERTIES_FILENAME = "test" + separator + "system" + separator + "broker.properties";

    public BrokerUnit(String propertiesFile, Integer maxReplicas) throws Exception {
        super(BrokerConstants.MODULE_NAME, propertiesFile);
        initData(new SimpleJobCounter(), maxReplicas, 3, 10, 2);
        getUIManager();
        this.uiManager.start();
    }

    public BrokerUnit(String propertiesFile, int maxReplicas, int maxFails, int maxBlFails, int numREs) throws Exception {
        super(BrokerConstants.MODULE_NAME, propertiesFile);
        initData(new SimpleJobCounter(), maxReplicas, maxFails, maxBlFails, numREs);
        getUIManager();
        this.uiManager.start();
    }

    protected BrokerUnit() throws Exception {
        this(new SimpleJobCounter(), 1, 3, 10, 2);
    }

    protected BrokerUnit(JobCounter jobCounter, int maxReplicas, int maxFails, int maxBlFails, int numREs) throws Exception {
        super(BrokerConstants.MODULE_NAME);
        initData(jobCounter, maxReplicas, maxFails, maxBlFails, numREs);
        getUIManager();
        this.uiManager.start();
    }

    private void initData(JobCounter jobCounter, int maxReplicas, int maxFails, int maxBlFails, int numREs) throws Exception {
        this.jobCounter = jobCounter;
        this.maxReplicas = maxReplicas;
        this.maxFails = maxFails;
        this.maxBlFails = maxBlFails;
        this.numREs = numREs;
        setContext(createContext());
        contextCreated();
    }

    public int addJob(JobSpec job) throws Exception {
        checkIfUnitIsRunning();
        ControlOperationResult result = uiManager.addJob(job);
        return (Integer) result.getResult();
    }

    public void cleanJob(int jobID) throws Exception {
        checkIfUnitIsRunning();
        uiManager.cleanFinishedJob(jobID);
    }

    public void cleanAllFinishedJobs() throws Exception {
        checkIfUnitIsRunning();
        uiManager.cleanAllFinishedJobs();
    }

    public Collection<PeerStatusInfo> getPeers() throws Exception {
        checkIfUnitIsRunning();
        BrokerCompleteStatus brokerStatus = uiManager.getBrokerCompleteStatus();
        return brokerStatus.getPeersPackage().getPeers();
    }

    public JobStatusInfo getJob(int jobid) throws Exception {
        checkIfUnitIsRunning();
        BrokerCompleteStatus brokerStatus = uiManager.getBrokerCompleteStatus();
        return brokerStatus.getJobsPackage().getJobs().get(jobid);
    }

    public Map<Integer, HashSet<WorkerStatusInfo>> getWorkersByJob() throws Exception {
        checkIfUnitIsRunning();
        BrokerCompleteStatus brokerStatus = uiManager.getBrokerCompleteStatus();
        return brokerStatus.getWorkersPackage().getWorkersByJob();
    }

    public void setPeers(PeerUnit... peerUnits) throws Exception {
        checkIfUnitIsRunning();
        List<PeerSpec> specs = new ArrayList<PeerSpec>(peerUnits.length);
        for (PeerUnit unit : peerUnits) {
            final PeerSpec peerSpec = new PeerSpec();
            peerSpec.putAttribute(PeerSpec.ATT_USERNAME, unit.getJabberUserName());
            peerSpec.putAttribute(PeerSpec.ATT_SERVERNAME, unit.getJabberServerHostname());
            specs.add(peerSpec);
        }
        uiManager.setPeers(specs);
    }

    public JobCounter getJobCounter() {
        return jobCounter;
    }

    public void setJobCounter(JobCounter jobCounter) throws Exception {
        checkIfUnitIsStopped();
        this.jobCounter = jobCounter;
    }

    public int getMaxBlFails() {
        return maxBlFails;
    }

    public void setMaxBlFails(int maxBlFails) throws Exception {
        this.maxBlFails = maxBlFails;
    }

    public int getMaxFails() {
        return maxFails;
    }

    public void setMaxFails(int maxFails) throws Exception {
        this.maxFails = maxFails;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public int getNumREs() {
        return numREs;
    }

    public void setNumberOfReplicaExecutors(int numREs) throws Exception {
        checkIfUnitIsStopped();
        this.numREs = numREs;
    }

    public void cancelJob(int jobid) throws Exception {
        uiManager.cancelJob(jobid);
    }

    public void cleanUp() throws Exception {
        if (jobCounter instanceof PersistentJobCounter) {
            PersistentJobCounter pjc = (PersistentJobCounter) jobCounter;
            pjc.getCounterFile().delete();
        }
    }

    public boolean areThereJobsRunning() throws Exception {
        BrokerCompleteStatus brokerStatus = uiManager.getBrokerCompleteStatus();
        Map<Integer, JobStatusInfo> jobs = brokerStatus.getJobsPackage().getJobs();
        for (JobStatusInfo job : jobs.values()) {
            if (job.isRunning()) {
                System.out.println("BrokerUnit.areThereJobsRunning(): " + job.getJobId() + " " + job.getState());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void deploy() {
        throw new UnsupportedOperationException("Remove deployment does not work for this unit");
    }

    public void showStatus() throws Exception {
        new BrokerStatusCommand(uiManager).run(new String[] {});
    }

    @Override
    public SyncApplicationClient<BrokerManager, BrokerSyncManagerClient> getUIManager() throws Exception {
        if (this.uiManager == null) {
            ContainerContext context = getContext();
            this.uiManager = new BrokerSyncApplicationClient(context);
        }
        return this.uiManager;
    }

    @Override
    protected void createComponent() {
        ContainerContext context = getContext();
        try {
            new BrokerComponent(context);
        } catch (CommuneNetworkException e) {
            e.printStackTrace();
        } catch (ProcessorStartException e) {
            e.printStackTrace();
        }
    }

    protected ContainerContext createContext() {
        String propertiesFile = BROKER_PROPERTIES_FILENAME;
        if (this.propertiesFile != null) {
            propertiesFile = this.propertiesFile;
        }
        BrokerComponentContextFactory contextFactory = new BrokerComponentContextFactory(new PropertiesFileParser(propertiesFile));
        ContainerContext context = contextFactory.createContext();
        Map<String, String> properties = context.getProperties();
        properties.put(BrokerConfiguration.PROP_MAX_REPLICAS, String.valueOf(this.maxReplicas));
        context = new ContainerContext(properties);
        return context;
    }

    public String getLogin() {
        ContainerContext context = createContext();
        return context.getProperty(XMPPProperties.PROP_USERNAME) + "@" + context.getProperty(XMPPProperties.PROP_XMPP_SERVERNAME);
    }

    public String getPassword() {
        ContainerContext context = createContext();
        return context.getProperty(XMPPProperties.PROP_PASSWORD);
    }
}
