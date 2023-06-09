package com.movilnet.clom.web.dhl.trackingResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DestinationServiceArea complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DestinationServiceArea">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceAreaCode" type="{http://www.dhl.com/datatypes}ServiceAreaCode" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FacilityCode" type="{http://www.dhl.com/datatypes}FacilityCode" minOccurs="0"/>
 *         &lt;element name="InboundSortCode" type="{http://www.dhl.com/datatypes}InboundSortCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DestinationServiceArea", propOrder = { "serviceAreaCode", "description", "facilityCode", "inboundSortCode" })
public class DestinationServiceArea {

    @XmlElement(name = "ServiceAreaCode")
    protected String serviceAreaCode;

    @XmlElement(name = "Description")
    protected String description;

    @XmlElement(name = "FacilityCode")
    protected String facilityCode;

    @XmlElement(name = "InboundSortCode")
    protected String inboundSortCode;

    /**
     * Gets the value of the serviceAreaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceAreaCode() {
        return serviceAreaCode;
    }

    /**
     * Sets the value of the serviceAreaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceAreaCode(String value) {
        this.serviceAreaCode = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the facilityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFacilityCode() {
        return facilityCode;
    }

    /**
     * Sets the value of the facilityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFacilityCode(String value) {
        this.facilityCode = value;
    }

    /**
     * Gets the value of the inboundSortCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundSortCode() {
        return inboundSortCode;
    }

    /**
     * Sets the value of the inboundSortCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundSortCode(String value) {
        this.inboundSortCode = value;
    }
}
