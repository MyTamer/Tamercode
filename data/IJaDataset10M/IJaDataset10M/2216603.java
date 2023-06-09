package com.windsor.node.plugin.facid3.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for EnvironmentalInterestListDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnvironmentalInterestListDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.exchangenetwork.net/schema/facilityid/3}EnvironmentalInterest" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnvironmentalInterestListDataType", propOrder = { "environmentalInterest" })
public class EnvironmentalInterestListDataType {

    @XmlElement(name = "EnvironmentalInterest", required = true)
    protected List<EnvironmentalInterestDataType> environmentalInterest;

    /**
     * Gets the value of the environmentalInterest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environmentalInterest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironmentalInterest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvironmentalInterestDataType }
     * 
     * 
     */
    public List<EnvironmentalInterestDataType> getEnvironmentalInterest() {
        if (environmentalInterest == null) {
            environmentalInterest = new ArrayList<EnvironmentalInterestDataType>();
        }
        return this.environmentalInterest;
    }
}
