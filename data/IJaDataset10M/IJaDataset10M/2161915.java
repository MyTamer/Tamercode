package net.seagis.gml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * A datum specifies the relationship of a coordinate system to the earth, thus creating a coordinate reference system. A datum uses a parameter or set of parameters that determine the location of the origin of the coordinate reference system. Each datum subtype can be associated with only specific types of coordinate systems. This abstract complexType shall not be used, extended, or restricted, in an Application Schema, to define a concrete subtype with a meaning equivalent to a concrete subtype specified in this document. 
 * 
 * <p>Java class for AbstractDatumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractDatumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml}AbstractDatumBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/gml}datumID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}remarks" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}anchorPoint" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}realizationEpoch" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}validArea" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/gml}scope" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractDatumType", propOrder = { "datumID", "anchorPoint", "realizationEpoch", "validArea", "scope" })
public abstract class AbstractDatumType extends AbstractDatumBaseType {

    private List<IdentifierType> datumID;

    private CodeType anchorPoint;

    @XmlSchemaType(name = "date")
    private XMLGregorianCalendar realizationEpoch;

    private ExtentType validArea;

    private String scope;

    /**
     * Set of alternative identifications of this datum.
     * The first datumID, if any, is normally the primary identification code, 
     * and any others are aliases. Gets the value of the datumID property.
     */
    public List<IdentifierType> getDatumID() {
        if (datumID == null) {
            datumID = new ArrayList<IdentifierType>();
        }
        return this.datumID;
    }

    /**
     * Gets the value of the anchorPoint property.
     */
    public CodeType getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * Gets the value of the realizationEpoch property.
     */
    public XMLGregorianCalendar getRealizationEpoch() {
        return realizationEpoch;
    }

    /**
     * Gets the value of the validArea property.
     */
    public ExtentType getValidArea() {
        return validArea;
    }

    /**
     * Gets the value of the scope property.
     */
    public String getScope() {
        return scope;
    }
}
