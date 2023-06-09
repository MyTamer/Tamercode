package gov.nasa.worldwind.layers.Earth;

import gov.nasa.worldwind.layers.BasicTiledImageLayer;
import gov.nasa.worldwind.util.WWXML;
import org.w3c.dom.Document;

/**
 * @author tag
 * @version $Id: USGSDigitalOrtho.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class USGSDigitalOrtho extends BasicTiledImageLayer {

    public USGSDigitalOrtho() {
        super(getConfigurationDocument(), null);
    }

    protected static Document getConfigurationDocument() {
        return WWXML.openDocumentFile("config/Earth/USGSDigitalOrthoLayer.xml", null);
    }
}
