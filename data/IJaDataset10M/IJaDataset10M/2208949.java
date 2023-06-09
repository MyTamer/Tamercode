package com.windsor.node.plugin.facid3.domain;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for FacilityDetailsDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FacilityDetailsDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.exchangenetwork.net/schema/facilityid/3}FacilityList" minOccurs="0"/>
 *         &lt;element ref="{http://www.exchangenetwork.net/schema/facilityid/3}AffiliateList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="schemaVersion" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *             &lt;pattern value="3\.\d*"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FacilityDetailsDataType", propOrder = { "facilityList", "affiliateList" })
public class FacilityDetailsDataType {

    @XmlElement(name = "FacilityList")
    protected FacilityListDataType facilityList;

    @XmlElement(name = "AffiliateList")
    protected AffiliateListDataType affiliateList;

    @XmlAttribute(required = true)
    protected String schemaVersion;

    /**
     * Gets the value of the facilityList property.
     * 
     * @return
     *     possible object is
     *     {@link FacilityListDataType }
     *     
     */
    public FacilityListDataType getFacilityList() {
        return facilityList;
    }

    /**
     * Sets the value of the facilityList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FacilityListDataType }
     *     
     */
    public void setFacilityList(FacilityListDataType value) {
        this.facilityList = value;
    }

    /**
     * Gets the value of the affiliateList property.
     * 
     * @return
     *     possible object is
     *     {@link AffiliateListDataType }
     *     
     */
    public AffiliateListDataType getAffiliateList() {
        return affiliateList;
    }

    /**
     * Sets the value of the affiliateList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AffiliateListDataType }
     *     
     */
    public void setAffiliateList(AffiliateListDataType value) {
        this.affiliateList = value;
    }

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }
}
