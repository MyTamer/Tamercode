package org.yawlfoundation.yawl.worklet.support;

import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/** 
 *  An event log file implementation.
 * 
 *  @author Michael Adams
 *  v0.8, 04-09/2006
 */
public class EventLogger {

    public static final String eCheckOut = "CheckOutWorkItem";

    public static final String eUndoCheckOut = "UndoCheckOutWorkItem";

    public static final String eLaunch = "WorkletLaunched";

    public static final String eCheckIn = "CheckInWorkItem";

    public static final String eCancel = "WorkletCancelled";

    public static final String eComplete = "WorkletCompleted";

    private static final String _logPath = Library.wsLogsDir + "eventLog.csv";

    private static SimpleDateFormat _sdfe = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss:SS");

    private static Logger elog = Logger.getLogger("org.yawlfoundation.yawl.worklet.support.EventLogger");

    /**
     *  writes an event to the event log
     *  @param event - the type of event to log
     *  @param caseId - the case that caused the event
     *  @param specId - the specification id of the case
     *  @param taskId - the id of the task the workletwas subbed for
     *  @param parentCaseId - the case id of the original workitem
     *  @param xType - the reason for raising a worklet case (maps to WorkletService.XTYPE)
     */
    public static void log(DBManager mgr, String event, String caseId, String specId, String taskId, String parentCaseId, int xType) {
        if (mgr != null) {
            WorkletEvent we = new WorkletEvent(event, caseId, specId, taskId, parentCaseId, xType);
            mgr.persist(we, DBManager.DB_INSERT);
        } else {
            logToCSV(event, caseId, specId, taskId, parentCaseId, xType);
        }
    }

    /** this version is used to log to a CSV file when persistence is OFF */
    private static void logToCSV(String event, String caseId, String specId, String taskId, String parentCaseId, int xType) {
        StringBuffer s = new StringBuffer();
        s.append(_sdfe.format(new Date()));
        s.append(",");
        s.append(event);
        s.append(",");
        s.append(caseId);
        s.append(",");
        s.append(specId);
        s.append(",");
        s.append(taskId);
        s.append(",");
        s.append(parentCaseId);
        s.append(",");
        s.append(xType);
        try {
            PrintWriter pLog = new PrintWriter(new FileWriter(_logPath, true));
            pLog.println(s.toString());
            pLog.close();
        } catch (IOException e) {
            elog.error("Exception writing to CSV EventLog", e);
        }
    }

    /**
     *  writes an event to the event log
     *  @param event - the type of event to log
     *  @param wir - the workitem that triggered the event
     */
    public static void log(DBManager mgr, String event, WorkItemRecord wir, int xType) {
        log(mgr, event, wir.getCaseID(), wir.getSpecificationID(), wir.getTaskID(), "", xType);
    }
}
