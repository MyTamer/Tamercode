package org.apache.commons.math;

/**
 * Exception thrown when an error occurs evaluating a function.
 * <p>
 * Maintains an <code>argument</code> property holding the input value that
 * caused the function evaluation to fail.
 * 
 * @version $Revision: 620312 $ $Date: 2008-02-10 12:28:59 -0700 (Sun, 10 Feb 2008) $
 */
public class FunctionEvaluationException extends MathException {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -7619974756160279127L;

    /** Argument causing function evaluation failure */
    private double argument = Double.NaN;

    /**
     * Construct an exception indicating the argument value
     * that caused the function evaluation to fail.
     * 
     * @param argument  the failing function argument 
     */
    public FunctionEvaluationException(double argument) {
        super("Evaluation failed for argument = {0}", new Object[] { new Double(argument) });
        this.argument = argument;
    }

    /**
     * Construct an exception using the given argument and message
     * text.
     * 
     * @param argument  the failing function argument 
     * @param message  the exception message text
     * @deprecated as of 1.2, replaced by {@link #FunctionEvaluationException(double, String, Object[])}
     */
    public FunctionEvaluationException(double argument, String message) {
        super(message);
        this.argument = argument;
    }

    /**
     * Constructs an exception with specified formatted detail message.
     * Message formatting is delegated to {@link java.text.MessageFormat}.
     * @param argument  the failing function argument 
     * @param pattern format specifier
     * @param arguments format arguments
     * @since 1.2
     */
    public FunctionEvaluationException(double argument, String pattern, Object[] arguments) {
        super(pattern, arguments);
        this.argument = argument;
    }

    /**
     * Construct an exception with the given argument, message and root cause.
     * 
     * @param argument  the failing function argument 
     * @param message descriptive error message
     * @param cause root cause.
     * @deprecated as of 1.2, replaced by {@link #FunctionEvaluationException(double, String, Object[], Throwable)}
     */
    public FunctionEvaluationException(double argument, String message, Throwable cause) {
        super(message, cause);
        this.argument = argument;
    }

    /**
     * Constructs an exception with specified root cause.
     * Message formatting is delegated to {@link java.text.MessageFormat}.
     * @param argument  the failing function argument 
     * @param cause  the exception or error that caused this exception to be thrown
     * @since 1.2
     */
    public FunctionEvaluationException(double argument, Throwable cause) {
        super(cause);
        this.argument = argument;
    }

    /**
     * Constructs an exception with specified formatted detail message and root cause.
     * Message formatting is delegated to {@link java.text.MessageFormat}.
     * @param argument  the failing function argument 
     * @param pattern format specifier
     * @param arguments format arguments
     * @param cause  the exception or error that caused this exception to be thrown
     * @since 1.2
     */
    public FunctionEvaluationException(double argument, String pattern, Object[] arguments, Throwable cause) {
        super(pattern, arguments, cause);
        this.argument = argument;
    }

    /**
     * Returns the function argument that caused this exception.
     * 
     * @return  argument that caused function evaluation to fail
     */
    public double getArgument() {
        return this.argument;
    }
}
