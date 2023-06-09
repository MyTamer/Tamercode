package lights.exceptions;

import java.io.*;

/** The superclass of every error generated by this package. *  @author <a href="mailto:alm@cs.wustl.edu">Amy L. Murphy</a> *  @author <a href="mailto:picco@cs.wustl.edu">Gian Pietro Picco</a> *  @version 1.0 alpha                                */
public class TupleSpaceError extends Error {

    private Throwable internal = null;

    private boolean internalFlag = false;

    /** Constructs a TupleSpaceError with no detail message. */
    public TupleSpaceError() {
        super();
    }

    /** Constructs a TupleSpaceError with the specified detail message.    *   * @param s the detail message. */
    public TupleSpaceError(String s) {
        super(s);
    }

    TupleSpaceError(Exception internal) {
        this();
        setInternal(internal);
    }

    TupleSpaceError(Exception internal, String s) {
        super(s);
        setInternal(internal);
    }

    public boolean hasInternal() {
        return internalFlag;
    }

    /**   * Returns the internal exception associated with this object.   *   * @return the internal exception, <code>null</code> if there is none.    */
    public Throwable getInternal() {
        return internal;
    }

    public String toString() {
        String r = super.toString();
        if (internalFlag) r += ": " + internal.toString();
        return r;
    }

    /** Prints this exception and its backtrace to the standard error stream. */
    public void printStackTrace() {
        if (internalFlag) {
            System.err.println(super.toString());
            internal.printStackTrace();
        } else super.printStackTrace();
    }

    /** Prints this exception and its backtrace to the specified print stream. */
    public void printStackTrace(PrintStream s) {
        if (internalFlag) {
            s.println(super.toString());
            internal.printStackTrace(s);
        } else super.printStackTrace(s);
    }

    /** Prints this exception and its backtrace to the specified print writer. */
    public void printStackTrace(PrintWriter s) {
        if (internalFlag) {
            s.println(super.toString());
            internal.printStackTrace(s);
        } else super.printStackTrace(s);
    }

    private void setInternal(Exception internal) {
        this.internal = internal;
        internalFlag = true;
    }
}
