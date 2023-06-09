package com.google.gwt.core.client.impl;

import java.io.Serializable;

/**
 * The emulated Throwable class does not serialize Throwables recursively and
 * does not serialize the stack trace.  This class is an alternative, and
 * can be used by writing a custom serializer for the class which contains a
 * Throwable. See {@link LogRecord_CustomFieldSerializer} as an example.
 *
 */
public class SerializableThrowable implements Serializable {

    private SerializableThrowable cause = null;

    private String message = null;

    private StackTraceElement[] stackTrace = null;

    private String typeName = null;

    /**
   * A subclass of Throwable that contains the serialized exception class type.
   */
    public static class ThrowableWithClassName extends Throwable {

        private String typeName;

        public ThrowableWithClassName(String message, Throwable cause, String typeName) {
            super(message, cause);
            this.typeName = typeName;
        }

        public ThrowableWithClassName(String message, String typeName) {
            super(message);
            this.typeName = typeName;
        }

        public String getExceptionClass() {
            return typeName;
        }
    }

    /**
   * Create a new SerializableThrowable from a Throwable.
   */
    public SerializableThrowable(Throwable t) {
        message = t.getMessage();
        if (t.getCause() != null && t.getCause() != t) {
            cause = new SerializableThrowable(t.getCause());
        }
        stackTrace = t.getStackTrace();
        typeName = t.getClass().getName();
    }

    protected SerializableThrowable() {
    }

    public SerializableThrowable getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    /**
   * Create a new Throwable from this SerializableThrowable.
   */
    public Throwable getThrowable() {
        Throwable t;
        if (cause != null) {
            t = new ThrowableWithClassName(message, cause.getThrowable(), typeName);
        } else {
            t = new ThrowableWithClassName(message, typeName);
        }
        t.setStackTrace(stackTrace);
        return t;
    }

    public void setCause(SerializableThrowable c) {
        cause = c;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setStackTrace(StackTraceElement[] st) {
        stackTrace = st;
    }
}
