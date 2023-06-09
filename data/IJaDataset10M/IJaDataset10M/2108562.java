package org.signserver.client.cli.validationservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.signserver.cli.CommandLineInterface;
import org.signserver.cli.spi.UnexpectedCommandFailureException;
import org.signserver.client.cli.spi.ClientCommandFactory;

/**
 * Main class for the Validation CLI in case anyone wants to run this standalone 
 * (ie for debugging).
 *
 * @author Markus Kilås
 * @version $Id: ValidationCLI.java 2097 2012-02-07 15:14:11Z netmackan $
 */
public class ValidationCLI extends CommandLineInterface {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(ValidationCLI.class);

    public ValidationCLI() {
        super(ClientCommandFactory.class, getCLIProperties());
    }

    /**
     * Main
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws UnexpectedCommandFailureException {
        ValidationCLI adminCLI = new ValidationCLI();
        System.exit(adminCLI.execute(args));
    }

    private static Properties getCLIProperties() {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = ValidationCLI.class.getResourceAsStream("/signserver_cli.properties");
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException ex) {
            LOG.error("Could not load configuration: " + ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    LOG.error("Failed to close configuration", ex);
                }
            }
        }
        return properties;
    }
}
