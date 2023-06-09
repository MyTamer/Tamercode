package be.fedict.eid.dss.model.mbean;

import java.security.Provider;
import java.security.Security;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jboss.ejb3.annotation.Service;
import be.fedict.eid.applet.service.signer.ooxml.OOXMLProvider;

/**
 * MBean to manage some runtime aspects like registering security providers.
 * 
 * @author Frank Cornelis
 * 
 */
@Service
public class DigitalSignatureServiceMBean implements DigitalSignatureServiceMBeanLocal {

    private static final Log LOG = LogFactory.getLog(DigitalSignatureServiceMBean.class);

    private Provider managedBouncyCastleProvider;

    private Provider managedOOXMLProvider;

    public void create() throws Exception {
        LOG.debug("create");
    }

    public void start() throws Exception {
        LOG.debug("start");
        registerBouncyCastle();
        registerOOXMLProvider();
    }

    private void registerOOXMLProvider() {
        Provider provider = Security.getProvider(OOXMLProvider.NAME);
        if (null != provider) {
            LOG.debug("we don't register OOXMLProvider");
            return;
        }
        this.managedOOXMLProvider = new OOXMLProvider();
        LOG.debug("we register OOXMLProvider");
        if (-1 == Security.addProvider(this.managedOOXMLProvider)) {
            LOG.fatal("could not register OOXMLProvider");
        }
    }

    private void registerBouncyCastle() {
        Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (null != provider) {
            LOG.debug("we don't register BouncyCastle");
            return;
        }
        this.managedBouncyCastleProvider = new BouncyCastleProvider();
        LOG.debug("we register BouncyCastle");
        if (-1 == Security.addProvider(this.managedBouncyCastleProvider)) {
            LOG.fatal("could not register BouncyCastle");
        }
    }

    public void stop() {
        LOG.debug("stop");
        unregisterBouncyCastle();
        unregisterOOXMLProvider();
    }

    private void unregisterOOXMLProvider() {
        if (null == this.managedOOXMLProvider) {
            LOG.debug("we don't unregister OOXMLProvider");
            return;
        }
        LOG.debug("we unregister OOXMLProvider");
        Security.removeProvider(OOXMLProvider.NAME);
    }

    private void unregisterBouncyCastle() {
        if (null == this.managedBouncyCastleProvider) {
            LOG.debug("we don't unregister BouncyCastle");
            return;
        }
        LOG.debug("we unregister BouncyCastle");
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

    public void destroy() {
        LOG.debug("destroy");
    }
}
