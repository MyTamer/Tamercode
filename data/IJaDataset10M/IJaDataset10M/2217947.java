package xades4j.xml.bind.xades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for QualifyingPropertiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QualifyingPropertiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SignedProperties" type="{http://uri.etsi.org/01903/v1.3.2#}SignedPropertiesType" minOccurs="0"/>
 *         &lt;element name="UnsignedProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedPropertiesType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Target" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QualifyingPropertiesType", propOrder = { "signedProperties", "unsignedProperties" })
public class XmlQualifyingPropertiesType {

    @XmlElement(name = "SignedProperties")
    protected XmlSignedPropertiesType signedProperties;

    @XmlElement(name = "UnsignedProperties")
    protected XmlUnsignedPropertiesType unsignedProperties;

    @XmlAttribute(name = "Target", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String target;

    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the signedProperties property.
     * 
     * @return
     *     possible object is
     *     {@link XmlSignedPropertiesType }
     *     
     */
    public XmlSignedPropertiesType getSignedProperties() {
        return signedProperties;
    }

    /**
     * Sets the value of the signedProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSignedPropertiesType }
     *     
     */
    public void setSignedProperties(XmlSignedPropertiesType value) {
        this.signedProperties = value;
    }

    /**
     * Gets the value of the unsignedProperties property.
     * 
     * @return
     *     possible object is
     *     {@link XmlUnsignedPropertiesType }
     *     
     */
    public XmlUnsignedPropertiesType getUnsignedProperties() {
        return unsignedProperties;
    }

    /**
     * Sets the value of the unsignedProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlUnsignedPropertiesType }
     *     
     */
    public void setUnsignedProperties(XmlUnsignedPropertiesType value) {
        this.unsignedProperties = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }
}
