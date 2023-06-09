package gov.nasa.worldwind.ogc.kml;

/**
 * Represents the KML <i>LatLonBox</i> element and provides access to its contents.
 *
 * @author tag
 * @version $Id: KMLLatLonBox.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class KMLLatLonBox extends KMLAbstractLatLonBoxType {

    /**
     * Construct an instance.
     *
     * @param namespaceURI the qualifying namespace URI. May be null to indicate no namespace qualification.
     */
    public KMLLatLonBox(String namespaceURI) {
        super(namespaceURI);
    }

    public Double getRotation() {
        return (Double) this.getField("rotation");
    }
}
