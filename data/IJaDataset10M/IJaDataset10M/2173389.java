package org.ourgrid.common.executor.gateway;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FileUtils;
import org.ourgrid.common.executor.Executor;
import org.ourgrid.common.executor.ExecutorException;
import org.ourgrid.common.executor.ExecutorHandle;
import org.ourgrid.common.executor.ExecutorResult;
import org.ourgrid.common.executor.IntegerExecutorHandle;
import org.ourgrid.common.executor.config.ExecutorConfiguration;
import org.ourgrid.common.executor.config.GatewayExecutorConfiguration;
import org.ourgrid.common.util.StringUtil;
import org.ourgrid.gateway.wssubmitter.GatewayBridgeSubmitter;
import org.ourgrid.gateway.wssubmitter.client.GatewayBridgeSubmitterClient;
import org.ourgrid.gateway.wssubmitter.client.Job;
import org.ourgrid.gateway.wssubmitter.client.JobIDList;
import org.ourgrid.gateway.wssubmitter.client.JobList;
import org.ourgrid.gateway.wssubmitter.client.JobOutput;
import org.ourgrid.gateway.wssubmitter.client.JobStatus;
import org.ourgrid.gateway.wssubmitter.client.LogicalFile;
import org.ourgrid.gateway.wssubmitter.client.OutputList;
import org.ourgrid.gateway.wssubmitter.client.StatusList;
import org.ourgrid.worker.WorkerConfiguration;
import org.ourgrid.worker.WorkerConstants;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;

public class GatewayExecutor implements Executor {

    private static final int GET_RESULT_INTERVAL = 10000;

    private static final long serialVersionUID = 6385184394295862967L;

    private String destinationGrid;

    private String publicDirPath;

    private String publicDirURL;

    private String gatewayServiceAddress;

    private GatewayBridgeSubmitter bridgeSubmitter;

    private Map<ExecutorHandle, GatewayJob> jobsIds = new TreeMap<ExecutorHandle, GatewayJob>();

    private final transient CommuneLogger logger;

    private int nextHandle = 0;

    public GatewayExecutor(CommuneLogger logger) {
        this.logger = logger;
    }

    public void beginAllocation() throws ExecutorException {
    }

    public void chmod(File file, String perm) throws ExecutorException {
    }

    public ExecutorHandle execute(String dirName, String command, Map<String, String> envVars) throws ExecutorException {
        JobList jobList = new JobList();
        String[] splitCommand = command.split(" ");
        Job job = new Job();
        job.setAlg(splitCommand[0]);
        if (splitCommand.length > 1) {
            job.setArgs(command.substring(splitCommand[0].length()).trim());
        }
        logger.debug("Setting destination grid for execution: " + destinationGrid);
        job.setGrid(destinationGrid);
        String outputVars = envVars.get(WorkerConfiguration.ATT_OUTPUTFILES);
        String[] outputFiles = outputVars == null ? new String[] {} : outputVars.split(WorkerConfiguration.SEPARATOR_CHAR);
        Map<String, String> resolvedOutputs = new HashMap<String, String>();
        for (String outputFile : outputFiles) {
            job.getOutputs().add(outputFile);
            resolvedOutputs.put(outputFile, resolveLogicalName(envVars, outputFile));
        }
        String inputVars = envVars.get(WorkerConfiguration.ATT_INPUTFILES);
        String[] inputFiles = inputVars == null ? new String[] {} : inputVars.split(WorkerConfiguration.SEPARATOR_CHAR);
        for (String inputFile : inputFiles) {
            String resolvedFileName = resolveLogicalName(envVars, inputFile);
            LogicalFile logicalFile = new LogicalFile();
            logicalFile.setURL(copyFileToPublicDir(inputFile, resolvedFileName, new File(envVars.get(WorkerConstants.ENV_PLAYPEN)).getName()));
            logicalFile.setLogicalName(inputFile);
            job.getInputs().add(logicalFile);
        }
        jobList.getJob().add(job);
        logger.debug("Invoking gateway webservice to submit job: " + job.getAlg());
        JobIDList jobIDList = getBridgeSubmitter().submit(jobList);
        IntegerExecutorHandle handle = new IntegerExecutorHandle(nextHandle++);
        String jobID = jobIDList.getJobid().iterator().next();
        logger.debug("Job submitted to gateway with id: " + jobID);
        GatewayJob gJob = new GatewayJob(jobID, job, resolvedOutputs);
        jobsIds.put(handle, gJob);
        return handle;
    }

    private String copyFileToPublicDir(String logicalFileName, String inputFile, String playpenDir) throws ExecutorException {
        try {
            FileUtils.copyFileToDirectory(new File(inputFile), new File(publicDirPath + File.separator + playpenDir));
        } catch (IOException e) {
            throw new ExecutorException(e);
        }
        return publicDirURL + "/" + playpenDir + "/" + logicalFileName;
    }

    private String resolveLogicalName(Map<String, String> envVars, String inputFile) {
        String playpenDir = envVars.get(WorkerConstants.ENV_PLAYPEN);
        String storageDir = envVars.get(WorkerConstants.ENV_STORAGE);
        int indexOfPlaypenDir = inputFile.indexOf(playpenDir);
        int indexOfStorageDir = inputFile.indexOf(storageDir);
        if (indexOfPlaypenDir < 0 && indexOfStorageDir < 0) {
            return playpenDir + File.separator + inputFile;
        }
        return StringUtil.replaceVariables(inputFile, envVars);
    }

    public ExecutorHandle execute(String dirName, String command) throws ExecutorException {
        return execute(dirName, command, new HashMap<String, String>());
    }

    public void finishExecution() throws ExecutorException {
    }

    public ExecutorResult getResult(ExecutorHandle handle) throws ExecutorException {
        GatewayJob gJob = jobsIds.get(handle);
        JobIDList jobIdList = new JobIDList();
        jobIdList.getJobid().add(gJob.getJobId());
        while (true) {
            logger.debug("Getting status for job with id: " + gJob.getJobId());
            StatusList statusList = getBridgeSubmitter().getStatus(jobIdList);
            JobStatus jobStatus = statusList.getStatus().iterator().next();
            if (JobStatus.ERROR.equals(jobStatus)) {
                throw new ExecutorException("The job execution at the Grid Gateway failed.");
            } else if (JobStatus.FINISHED.equals(jobStatus)) {
                fetchOutputs(gJob);
                return new ExecutorResult(0, "", "");
            }
            try {
                Thread.sleep(GET_RESULT_INTERVAL);
            } catch (InterruptedException e) {
                throw new ExecutorException(e);
            }
        }
    }

    private void fetchOutputs(GatewayJob gJob) throws ExecutorException {
        JobIDList jobIdList = new JobIDList();
        jobIdList.getJobid().add(gJob.getJobId());
        OutputList outputList = getBridgeSubmitter().getOutput(jobIdList);
        for (JobOutput jobOutput : outputList.getOutput()) {
            List<LogicalFile> logicalFiles = jobOutput.getOutput();
            if (logicalFiles.size() != gJob.getJob().getOutputs().size()) {
                throw new ExecutorException("Outputs fetched from the bridge differs " + "from the ones specified on the job description");
            }
            for (LogicalFile logicalFile : logicalFiles) {
                wget(logicalFile, gJob.getOutputs().get(logicalFile.getLogicalName()));
            }
        }
    }

    private void wget(LogicalFile logicalFile, String fullFilePath) throws ExecutorException {
        try {
            URL url = new URL(logicalFile.getURL());
            FileUtils.copyURLToFile(url, new File(fullFilePath));
        } catch (Exception e) {
            throw new ExecutorException(e);
        }
    }

    public void kill(ExecutorHandle handle) throws ExecutorException {
    }

    public void setConfiguration(ExecutorConfiguration executorConfiguratrion) {
        this.destinationGrid = executorConfiguratrion.getProperty(WorkerConstants.PREFIX + GatewayExecutorConfiguration.PROPERTIES.DESTINATION_GRID.toString());
        this.publicDirPath = executorConfiguratrion.getProperty(WorkerConstants.PREFIX + GatewayExecutorConfiguration.PROPERTIES.PUBLIC_DIR_PATH.toString());
        this.publicDirURL = executorConfiguratrion.getProperty(WorkerConstants.PREFIX + GatewayExecutorConfiguration.PROPERTIES.PUBLIC_DIR_URL.toString());
        this.gatewayServiceAddress = executorConfiguratrion.getProperty(WorkerConstants.PREFIX + GatewayExecutorConfiguration.PROPERTIES.WS_URL.toString());
    }

    public GatewayBridgeSubmitter getBridgeSubmitter() throws ExecutorException {
        if (bridgeSubmitter == null) {
            try {
                bridgeSubmitter = new GatewayBridgeSubmitterClient(gatewayServiceAddress).getG3BridgeSubmitterPort();
            } catch (Throwable e) {
                logger.error("Cannot access gateway webservice: " + e.getMessage());
                throw new ExecutorException("Cannot access gateway webservice: " + e.getMessage());
            }
        }
        return bridgeSubmitter;
    }
}
