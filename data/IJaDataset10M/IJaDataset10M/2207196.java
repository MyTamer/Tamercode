package org.purl.sword.base;

/**
 * Represents information about an exception that is generated during
 * the Unmarshall process. 
 * 
 * @author Neil Taylor
 */
public class UnmarshallException extends Exception {

    /**
    * Create a new instance and store the specified message and source data. 
    * 
    * @param message The message for the exception. 
    * @param source  The original exception that lead to this exception. This
    *                can be <code>null</code>. 
    */
    public UnmarshallException(String message, Exception source) {
        super(message, source);
    }

    /**
    * Create a new instance and store the specified message. 
    * 
    * @param message The message for the exception. 
    */
    public UnmarshallException(String message) {
        super(message);
    }
}
