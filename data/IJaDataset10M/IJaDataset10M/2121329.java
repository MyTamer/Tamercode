package org.ikasan.connector.base.outbound;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;
import org.apache.log4j.Logger;
import org.ikasan.common.CommonXMLParser;
import org.ikasan.connector.ConnectorContext;
import org.ikasan.connector.ConnectorEnvironment;
import org.ikasan.connector.ConnectorXMLParser;
import org.ikasan.connector.ConnectorXMLTransformer;
import org.ikasan.connector.ConnectorXSLTransformer;
import org.ikasan.connector.ResourceLoader;

/**
 * This is the abstract factory class which extends the ManagedConnectionFactory
 * for obtaining real physical connections to the associated EIS.
 * 
 * This class is central to the creation of ConnectionFactories and
 * ManagedConnections.
 * 
 * This (and extensions thereof) is the first class that the application server
 * will instantiate.
 * 
 * All the other classes in the resource adapter are instantiated, directly or
 * indirectly, by this one.
 * 
 * @author Ikasan Development Team
 */
public abstract class EISManagedConnectionFactory implements ManagedConnectionFactory, Serializable {

    /** Logger */
    private static Logger logger = Logger.getLogger(EISManagedConnectionFactory.class);

    /** default serial version uid */
    private static final long serialVersionUID = 1L;

    /** Print writer */
    private PrintWriter writer = null;

    /** All sessions require a clientID */
    protected String clientID;

    /** non transactional session factory */
    protected String sessionFactoryName;

    /** local transaction session factory */
    protected String localSessionFactoryName;

    /** XA transaction session factory */
    protected String xaSessionFactoryName;

    /** Connector Context */
    protected ConnectorContext connectorContext = ResourceLoader.getInstance().newContext();

    /** Connector XML Parser */
    protected ConnectorXMLParser xmlParser = ResourceLoader.getInstance().newXMLParser();

    /** Connector XML Transformer */
    protected ConnectorXMLTransformer xmltransformer = ResourceLoader.getInstance().newXMLTransformer();

    /** Connector XSL Transformer */
    protected ConnectorXSLTransformer xsltransformer = ResourceLoader.getInstance().newXSLTransformer();

    /** Connector Environment */
    protected ConnectorEnvironment connectorEnv = ResourceLoader.getInstance().newEnvironment();

    /**
     * The application server calls this method to create a new EIS-specific
     * ConnectionFactory.
     * 
     * The extending class must implement this for the specific EIS.
     * 
     * @param connectionManager - The connection manager to use for this connection factory
     * @return ConnectionFactory - The connection factory
     * @throws ResourceException - Exception if creation fails
     */
    public abstract Object createConnectionFactory(final ConnectionManager connectionManager) throws ResourceException;

    /**
     * The application server or stand-alone client calls this method to create
     * a new EIS-specific ConnectionFactory.
     * 
     * The extending class must implement this for the specific EIS.
     */
    public abstract Object createConnectionFactory() throws ResourceException;

    /**
     * The application server calls this method to create a new physical (real)
     * connection to the EIS.
     * 
     * The extending class must implement this for the specific EIS.
     */
    public abstract ManagedConnection createManagedConnection(final Subject subject, final ConnectionRequestInfo cri) throws ResourceException;

    /**
     * hashCode() uses specific instance properties to generate a hashcode which
     * can be used to uniquely identify this class instance.
     * 
     * The extending class must implement this for the specific EIS.
     */
    @Override
    public abstract int hashCode();

    /**
     * equals() is used to determine whether two instances of this class are set
     * with the same configuration properties.
     * 
     * The extending class must implement this for the specific EIS.
     */
    @Override
    public abstract boolean equals(final Object object);

    /**
     * This method is called by the application server when the client asks for
     * a new connection. The application server passes in a Set of all the
     * active virtual connections, and this object must pick one that is
     * currently handling a physical connection that can be shared to support
     * the new client request. Typically this sharing will be allowed if the
     * security attributes and properties of the new request match an existing
     * physical connection.
     * 
     * If nothing is available, the method must return null, so that the
     * application server knows it has to create a new physical connection.
     */
    public abstract ManagedConnection matchManagedConnections(final Set connections, final Subject subject, final ConnectionRequestInfo cri);

    /**
     * Standard getter for the logWriter
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
     */
    public PrintWriter getLogWriter() {
        logger.debug("Getting logWriter...");
        return this.writer;
    }

    /**
     * Standard setter for the logWriter
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter writer) {
        this.writer = writer;
        logger.debug("Setting logWriter...");
    }

    /**
     * Standard getter for the connectorContext
     * 
     * @return ConnectorContext
     */
    public ConnectorContext getConnectorContext() {
        logger.debug("Getting connectorContext...");
        return this.connectorContext;
    }

    /**
     * Standard getter for the connectorEnvironment
     * 
     * @return ConnectorEnvironment
     */
    public ConnectorEnvironment getConnectorEnv() {
        logger.debug("Getting connectorEnv [" + this.connectorEnv + "]");
        return this.connectorEnv;
    }

    /**
     * Standard getter for the connectorXMLParser
     * 
     * @return ConnectorXMLParser
     */
    public CommonXMLParser getXmlParser() {
        logger.debug("Getting xmlparser [" + this.xmlParser + "]");
        return this.xmlParser;
    }

    /**
     * Standard getter for the connectorXMLTransformer
     * 
     * @return ConnectorXMLTransformer
     */
    public ConnectorXMLTransformer getXmlTransformer() {
        logger.debug("Getting xmltransformer [" + this.xmltransformer + "]");
        return this.xmltransformer;
    }

    /**
     * Standard getter for the connectorXSLTransformer
     * 
     * @return ConnectorXSLTransformer
     */
    public ConnectorXSLTransformer getXslTransformer() {
        logger.debug("Getting xsltransformer [" + this.xsltransformer + "]");
        return this.xsltransformer;
    }

    /**
     * Getter for clientID
     * 
     * @return String - clientID
     */
    public String getClientID() {
        logger.debug("Getting clientID [" + this.clientID + "]");
        return this.clientID;
    }

    /**
     * Setter for clientID.
     * 
     * @param clientID - The client id to set
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
        logger.debug("Setting clientID [" + this.clientID + "]");
    }

    /**
     * Getter for the sessionFactoryName
     * 
     * @return the sessionFactoryName
     */
    public String getSessionFactoryName() {
        logger.debug("Getting dataSource [" + this.sessionFactoryName + "]");
        return this.sessionFactoryName;
    }

    /**
     * Setter for the sessionFactoryName
     * 
     * @param sessionFactoryName the sessionFactoryName to set
     */
    public void setSessionFactoryName(String sessionFactoryName) {
        this.sessionFactoryName = sessionFactoryName;
        logger.debug("Setting sessionFactoryName [" + this.sessionFactoryName + "]");
    }

    /**
     * Getter for the localSessionFactoryName
     * 
     * @return the localSessionFactoryName
     */
    public String getLocalSessionFactoryName() {
        logger.debug("Getting localSessionFactoryName [" + this.localSessionFactoryName + "]");
        return this.localSessionFactoryName;
    }

    /**
     * Setter for the localSessionFactoryName
     * 
     * @param localSessionFactoryName the localSessionFactoryName to set
     */
    public void setLocalSessionFactoryName(String localSessionFactoryName) {
        this.localSessionFactoryName = localSessionFactoryName;
        logger.debug("Setting localSessionFactoryName [" + this.localSessionFactoryName + "]");
    }

    /**
     * Getter for the xaSessionFactoryName
     * 
     * @return the xaSessionFactoryName
     */
    public String getXASessionFactoryName() {
        logger.debug("Getting xaSessionFactoryName [" + this.xaSessionFactoryName + "]");
        return this.xaSessionFactoryName;
    }

    /**
     * Setter for the xaSessionFactoryName
     * 
     * @param xaSessionFactoryName the xaSessionFactoryName to set
     */
    public void setXASessionFactoryName(String xaSessionFactoryName) {
        this.xaSessionFactoryName = xaSessionFactoryName;
        logger.debug("Setting xaSessionFactoryName [" + this.xaSessionFactoryName + "]");
    }

    /**
     * Validate standard properties required for all connections.
     * 
     * @throws InvalidPropertyException - Exception if hte proprty is invalid
     */
    protected void validateConnectionProperties() throws InvalidPropertyException {
        if (this.clientID == null) {
            throw new InvalidPropertyException("ClientID property cannot be null.");
        }
        if (this.sessionFactoryName == null) {
            logger.warn("Connector property 'sessionFactoryName' is [" + this.sessionFactoryName + "]. " + "You will not be able to use a non-transactional data source!");
        }
        if (this.localSessionFactoryName == null) {
            logger.warn("Connector property 'localSessionFactoryName' is [" + this.localSessionFactoryName + "]. " + "You will not be able to use a local transactional data source!");
        }
        if (this.xaSessionFactoryName == null) {
            logger.warn("Connector property 'xaSessionFactoryName' is [" + this.xaSessionFactoryName + "]. " + "You will not be able to use an XA transactional data source!");
        }
    }
}
