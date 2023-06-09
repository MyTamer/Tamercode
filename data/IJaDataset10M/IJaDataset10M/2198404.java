package org.dbe.composer.wfengine.bpel;

import javax.xml.namespace.QName;

/**
 * Interface for process events.
 */
public interface ISdlProcessEvent extends ISdlBaseProcessEvent {

    /** Inactive activity process state. */
    public static final int INACTIVE = -1;

    /** Activity is ready to execute. */
    public static final int READY_TO_EXECUTE = 0;

    /** Activity is executing. */
    public static final int EXECUTING = 1;

    /** Activity finished executing with success. */
    public static final int EXECUTE_COMPLETE = 2;

    /** Activity finished executing with a fault. */
    public static final int EXECUTE_FAULT = 3;

    /** A link has been evaluated. */
    public static final int LINK_STATUS = 4;

    /** A bpel object is now a dead path meaning it will never execute. */
    public static final int DEAD_PATH_STATUS = 5;

    /** A bpel object has been terminated */
    public static final int TERMINATED = 6;

    /** A process has been migrated. */
    public static final int MIGRATED = 12;

    /**
    * Returns the QName of the process this event was generated by.
    */
    public QName getQName();
}
