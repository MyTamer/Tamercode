package org.zkforge.javax.el;

/**
 * Represents any of the exception conditions that can arise during expression
 * evaluation.
 * 
 * @since 2.1
 */
public class ELException extends RuntimeException {

    /**
	 * Creates an ELException with no detail message
	 */
    public ELException() {
        super();
    }

    /**
	 * Creates an ELException with the provided detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
    public ELException(String message) {
        super(message);
    }

    /**
	 * Creates an ELException with the given detail message and root cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the originating cause of this exception
	 */
    public ELException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Creates an ELException with the given cause
	 * 
	 * @param cause
	 *            the originating cause of this exception
	 */
    public ELException(Throwable cause) {
        super(cause);
    }
}
