package org.chmuk.bindings.kml.v22;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for LinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinkType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}BasicLinkType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}refreshMode" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}refreshInterval" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}viewRefreshMode" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}viewRefreshTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}viewBoundScale" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}viewFormat" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}httpQuery" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}LinkSimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}LinkObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkType", propOrder = { "refreshMode", "refreshInterval", "viewRefreshMode", "viewRefreshTime", "viewBoundScale", "viewFormat", "httpQuery", "linkSimpleExtensionGroups", "linkObjectExtensionGroups" })
public class LinkType extends BasicLinkType {

    @XmlElement(defaultValue = "onChange")
    protected RefreshModeEnumType refreshMode;

    @XmlElement(defaultValue = "4.0")
    protected Double refreshInterval;

    @XmlElement(defaultValue = "never")
    protected ViewRefreshModeEnumType viewRefreshMode;

    @XmlElement(defaultValue = "4.0")
    protected Double viewRefreshTime;

    @XmlElement(defaultValue = "1.0")
    protected Double viewBoundScale;

    protected String viewFormat;

    protected String httpQuery;

    @XmlElement(name = "LinkSimpleExtensionGroup")
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> linkSimpleExtensionGroups;

    @XmlElement(name = "LinkObjectExtensionGroup")
    protected List<AbstractObjectType> linkObjectExtensionGroups;

    /**
     * Gets the value of the refreshMode property.
     * 
     * @return
     *     possible object is
     *     {@link RefreshModeEnumType }
     *     
     */
    public RefreshModeEnumType getRefreshMode() {
        return refreshMode;
    }

    /**
     * Sets the value of the refreshMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefreshModeEnumType }
     *     
     */
    public void setRefreshMode(RefreshModeEnumType value) {
        this.refreshMode = value;
    }

    /**
     * Gets the value of the refreshInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * Sets the value of the refreshInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRefreshInterval(Double value) {
        this.refreshInterval = value;
    }

    /**
     * Gets the value of the viewRefreshMode property.
     * 
     * @return
     *     possible object is
     *     {@link ViewRefreshModeEnumType }
     *     
     */
    public ViewRefreshModeEnumType getViewRefreshMode() {
        return viewRefreshMode;
    }

    /**
     * Sets the value of the viewRefreshMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViewRefreshModeEnumType }
     *     
     */
    public void setViewRefreshMode(ViewRefreshModeEnumType value) {
        this.viewRefreshMode = value;
    }

    /**
     * Gets the value of the viewRefreshTime property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getViewRefreshTime() {
        return viewRefreshTime;
    }

    /**
     * Sets the value of the viewRefreshTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setViewRefreshTime(Double value) {
        this.viewRefreshTime = value;
    }

    /**
     * Gets the value of the viewBoundScale property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getViewBoundScale() {
        return viewBoundScale;
    }

    /**
     * Sets the value of the viewBoundScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setViewBoundScale(Double value) {
        this.viewBoundScale = value;
    }

    /**
     * Gets the value of the viewFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewFormat() {
        return viewFormat;
    }

    /**
     * Sets the value of the viewFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewFormat(String value) {
        this.viewFormat = value;
    }

    /**
     * Gets the value of the httpQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHttpQuery() {
        return httpQuery;
    }

    /**
     * Sets the value of the httpQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHttpQuery(String value) {
        this.httpQuery = value;
    }

    /**
     * Gets the value of the linkSimpleExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkSimpleExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkSimpleExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getLinkSimpleExtensionGroups() {
        if (linkSimpleExtensionGroups == null) {
            linkSimpleExtensionGroups = new ArrayList<Object>();
        }
        return this.linkSimpleExtensionGroups;
    }

    /**
     * Gets the value of the linkObjectExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkObjectExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkObjectExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractObjectType }
     * 
     * 
     */
    public List<AbstractObjectType> getLinkObjectExtensionGroups() {
        if (linkObjectExtensionGroups == null) {
            linkObjectExtensionGroups = new ArrayList<AbstractObjectType>();
        }
        return this.linkObjectExtensionGroups;
    }
}
