package net.seagis.wcs.v100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Defines the properties (categories, measures, or values) assigned to each location in the domain. Any such property may be a scalar (numeric or text) value, such as population density, or a compound (vector or tensor) value, such as incomes by race, or radiances by wavelength. The semantic of the range set is typically an observable and is referenced by a URI. A rangeSet also has a reference system that is reffered by the URI in the refSys attribute. The refSys is either qualitative (classification) or quantitative (uom). The three attributes can be included either here and in each axisDescription. If included in both places, the values in the axisDescription over-ride those included in the RangeSet. 
 * 
 * <p>Java class for RangeSetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RangeSetType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/wcs}AbstractDescriptionType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/wcs}axisDescription" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nullValues" type="{http://www.opengis.net/wcs}valueEnumType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.opengis.net/wcs}semantic"/>
 *       &lt;attribute name="refSys" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="refSysLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author Guilhem Legal 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RangeSetType")
public class RangeSetType extends AbstractDescriptionType {

    @XmlAttribute(namespace = "http://www.opengis.net/wcs")
    @XmlSchemaType(name = "anyURI")
    private String semantic;

    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    private String refSys;

    @XmlAttribute
    private String refSysLabel;

    /**
     * Empty constructor used by JAXB.
     */
    RangeSetType() {
    }

    /**
     * Build a new range set.
     */
    public RangeSetType(List<MetadataLinkType> metadataLink, String name, String label, String description, String semantic, String refSys, String refSysLabel) {
        super(metadataLink, name, label, description);
        this.semantic = semantic;
        this.refSys = refSys;
        this.refSysLabel = refSysLabel;
    }

    /**
     * Gets the value of the semantic property.
     */
    public String getSemantic() {
        return semantic;
    }

    /**
     * Gets the value of the refSys property.
     * 
     */
    public String getRefSys() {
        return refSys;
    }

    /**
     * Gets the value of the refSysLabel property.
     */
    public String getRefSysLabel() {
        return refSysLabel;
    }
}
