package org.matsim.jaxb.signalsystems20;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signalSystem" type="{http://www.matsim.org/files/dtd}signalSystemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "signalSystem" })
@XmlRootElement(name = "signalSystems")
public class XMLSignalSystems {

    protected List<XMLSignalSystemType> signalSystem;

    /**
     * Gets the value of the signalSystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signalSystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignalSystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLSignalSystemType }
     * 
     * 
     */
    public List<XMLSignalSystemType> getSignalSystem() {
        if (signalSystem == null) {
            signalSystem = new ArrayList<XMLSignalSystemType>();
        }
        return this.signalSystem;
    }
}
