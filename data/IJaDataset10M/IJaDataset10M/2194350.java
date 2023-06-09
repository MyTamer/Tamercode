package uk.ac.ed.ph.jacomax;

/**
 * Runtime Exception thrown to indicate a problem with the configuration of
 * Maxima, such as a bad path or environment.
 * 
 * @author  David McKain
 * @version $Revision: 5 $
 */
public final class JacomaxConfigurationException extends JacomaxRuntimeException {

    private static final long serialVersionUID = 7100573731627419599L;

    public JacomaxConfigurationException(String message) {
        super(message);
    }

    public JacomaxConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
