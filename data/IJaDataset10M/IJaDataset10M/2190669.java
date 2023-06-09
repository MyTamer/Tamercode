package org.apache.harmony.jpda.tests.jdwp.ThreadReference;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for ThreadReference.Status command for the Thread waiting UNDEFINITELY.
 */
public class Status004Test extends JDWPSyncTestCase {

    static final int testStatusPassed = 0;

    static final int testStatusFailed = -1;

    protected String getDebuggeeClassName() {
        return "org.apache.harmony.jpda.tests.jdwp.ThreadReference.Status004Debuggee";
    }

    String getThreadSuspendStatusName(int suspendStatus) {
        String result = JDWPConstants.SuspendStatus.getName(suspendStatus);
        if (result.equals("")) {
            result = "NOT_SUSPENDED";
        }
        return result;
    }

    /**
     * This testcase exercises ThreadReference.Status command for the Thread waiting UNDEFINITELY.
     * <BR>At first the test starts Status004Debuggee which runs
     * the tested thread and blocks it in invocation of
     * the 'Object.wait()' method. 
     * <BR> Then the tests performs the ThreadReference.Status command 
     * for tested thread. 
     * <BR>It is expected that:
     * <BR>&nbsp;&nbsp; - returned thread status is WAIT status;
     * <BR>&nbsp;&nbsp; - returned suspend status is not SUSPEND_STATUS_SUSPENDED status;
     */
    public void testStatus005() {
        String thisTestName = "testStatus005";
        logWriter.println("==> " + thisTestName + " for ThreadReference.Status command: START...");
        logWriter.println("==> This " + thisTestName + " checks command for WAITING Thread: which is waiting UNDEFINITELY in Object.wait() method...");
        String checkedThreadName = synchronizer.receiveMessage();
        logWriter.println("=> checkedThreadName = " + checkedThreadName);
        long checkedThreadID = debuggeeWrapper.vmMirror.getThreadID(checkedThreadName);
        logWriter.println("=> checkedThreadID = " + checkedThreadID);
        logWriter.println("=> Send ThreadReference.Status command for checked Thread and check reply...");
        CommandPacket checkedCommand = new CommandPacket(JDWPCommands.ThreadReferenceCommandSet.CommandSetID, JDWPCommands.ThreadReferenceCommandSet.StatusCommand);
        checkedCommand.setNextValueAsThreadID(checkedThreadID);
        ReplyPacket checkedReply = debuggeeWrapper.vmMirror.performCommand(checkedCommand);
        checkReplyPacket(checkedReply, "ThreadReference.Status command");
        int threadStatus = checkedReply.getNextValueAsInt();
        int suspendStatus = checkedReply.getNextValueAsInt();
        logWriter.println("\n=> Returned thread status = 0x" + Integer.toHexString(threadStatus) + "(" + JDWPConstants.ThreadStatus.getName(threadStatus) + ")");
        if (threadStatus != JDWPConstants.ThreadStatus.WAIT) {
            finalSyncMessage = JPDADebuggeeSynchronizer.SGNL_CONTINUE;
            printErrorAndFail("Unexpected thread status is returned:" + "\n Expected thread status = 0x" + Integer.toHexString(JDWPConstants.ThreadStatus.WAIT) + "(" + JDWPConstants.ThreadStatus.getName(JDWPConstants.ThreadStatus.WAIT) + ")");
        } else {
            logWriter.println("=> OK - Expected thread status is returned");
        }
        logWriter.println("\n=> Returned thread suspend status = 0x" + Integer.toHexString(suspendStatus) + "(" + getThreadSuspendStatusName(suspendStatus) + ")");
        if (suspendStatus == JDWPConstants.SuspendStatus.SUSPEND_STATUS_SUSPENDED) {
            finalSyncMessage = JPDADebuggeeSynchronizer.SGNL_CONTINUE;
            printErrorAndFail("Unexpected thread status is returned:" + "\n Expected thread status = 0x" + Integer.toHexString(0) + "(" + getThreadSuspendStatusName(0) + ")");
        } else {
            logWriter.println("=> OK - Expected thread suspend status is returned");
        }
        logWriter.println("=> Send to Debuggee signal to funish ...");
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
        logWriter.println("==> " + thisTestName + " for ThreadReference.Status command: FINISH...");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Status004Test.class);
    }
}
