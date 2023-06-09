package gov.nasa.worldwind.ogc.kml;

import gov.nasa.worldwind.util.Logging;
import java.util.Map;

/**
 * Represents the KML <i>Style</i> element and provides access to its contents.
 *
 * @author tag
 * @version $Id: KMLStyle.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class KMLStyle extends KMLAbstractStyleSelector {

    /**
     * Construct an instance.
     *
     * @param namespaceURI the qualifying namespace URI. May be null to indicate no namespace qualification.
     */
    public KMLStyle(String namespaceURI) {
        super(namespaceURI);
    }

    public KMLIconStyle getIconStyle() {
        return (KMLIconStyle) this.getField(KMLConstants.ICON_STYLE_FIELD);
    }

    public KMLLabelStyle getLabelStyle() {
        return (KMLLabelStyle) this.getField(KMLConstants.LABEL_STYLE_FIELD);
    }

    public KMLLineStyle getLineStyle() {
        return (KMLLineStyle) this.getField(KMLConstants.LINE_STYLE_FIELD);
    }

    public KMLPolyStyle getPolyStyle() {
        return (KMLPolyStyle) this.getField(KMLConstants.POLY_STYLE_FIELD);
    }

    public KMLBalloonStyle getBaloonStyle() {
        return (KMLBalloonStyle) this.getField(KMLConstants.BALOON_STYLE_FIELD);
    }

    public KMLListStyle getListStyle() {
        return (KMLListStyle) this.getField(KMLConstants.LIST_STYLE_FIELD);
    }

    /**
     * Adds the sub-style fields of a specified sub-style to this one's fields if they don't already exist.
     *
     * @param subStyle the sub-style to merge with this one.
     *
     * @return the substyle passed in as the parameter.
     *
     * @throws IllegalArgumentException if the sub-style parameter is null.
     */
    public KMLAbstractSubStyle mergeSubStyle(KMLAbstractSubStyle subStyle) {
        if (subStyle == null) {
            String message = Logging.getMessage("nullValue.SymbolIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        Class subStyleClass = subStyle.getClass();
        for (Map.Entry<String, Object> field : this.getFields().getEntries()) {
            if (field.getValue() != null && field.getValue().getClass().equals(subStyleClass)) {
                this.overrideFields(subStyle, (KMLAbstractSubStyle) field.getValue());
            }
        }
        return subStyle;
    }
}
