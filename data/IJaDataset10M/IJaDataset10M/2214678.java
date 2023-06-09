package gov.nasa.worldwind.formats.gpx;

import gov.nasa.worldwind.tracks.Track;
import gov.nasa.worldwind.tracks.TrackPoint;
import gov.nasa.worldwind.tracks.TrackSegment;
import gov.nasa.worldwind.util.Logging;
import java.util.Arrays;
import java.util.List;

/**
 * @author tag
 * @version $Id: GpxRoute.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class GpxRoute extends gov.nasa.worldwind.formats.gpx.ElementParser implements Track, TrackSegment {

    private String name;

    private java.util.List<TrackPoint> points = new java.util.ArrayList<TrackPoint>();

    @SuppressWarnings({ "UnusedDeclaration" })
    public GpxRoute(String uri, String lname, String qname, org.xml.sax.Attributes attributes) {
        super("rte");
    }

    public List<TrackSegment> getSegments() {
        return Arrays.asList((TrackSegment) this);
    }

    public String getName() {
        return this.name;
    }

    public int getNumPoints() {
        return this.points.size();
    }

    public java.util.List<TrackPoint> getPoints() {
        return this.points;
    }

    @Override
    public void doStartElement(String uri, String lname, String qname, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        if (lname == null) {
            String msg = Logging.getMessage("nullValue.LNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (uri == null) {
            String msg = Logging.getMessage("nullValue.URIIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (qname == null) {
            String msg = Logging.getMessage("nullValue.QNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (attributes == null) {
            String msg = Logging.getMessage("nullValue.AttributesIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (lname.equalsIgnoreCase("rtept")) {
            this.currentElement = new GpxRoutePoint(uri, lname, qname, attributes);
            this.points.add((TrackPoint) this.currentElement);
        }
    }

    @Override
    public void doEndElement(String uri, String lname, String qname) throws org.xml.sax.SAXException {
        if (lname == null) {
            String msg = Logging.getMessage("nullValue.LNameIsNull");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        if (lname.equalsIgnoreCase("name")) {
            this.name = this.currentCharacters;
        }
    }
}
