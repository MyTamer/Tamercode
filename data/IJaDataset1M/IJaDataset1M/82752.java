package org.chmuk.bindings.kml.v22;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ScreenOverlayType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScreenOverlayType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}AbstractOverlayType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}overlayXY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}screenXY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}rotationXY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}size" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}rotation" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}ScreenOverlaySimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}ScreenOverlayObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScreenOverlayType", propOrder = { "overlayXY", "screenXY", "rotationXY", "size", "rotation", "screenOverlaySimpleExtensionGroups", "screenOverlayObjectExtensionGroups" })
public class ScreenOverlayType extends AbstractOverlayType {

    protected Vec2Type overlayXY;

    protected Vec2Type screenXY;

    protected Vec2Type rotationXY;

    protected Vec2Type size;

    @XmlElement(defaultValue = "0.0")
    protected Double rotation;

    @XmlElement(name = "ScreenOverlaySimpleExtensionGroup")
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> screenOverlaySimpleExtensionGroups;

    @XmlElement(name = "ScreenOverlayObjectExtensionGroup")
    protected List<AbstractObjectType> screenOverlayObjectExtensionGroups;

    /**
     * Gets the value of the overlayXY property.
     * 
     * @return
     *     possible object is
     *     {@link Vec2Type }
     *     
     */
    public Vec2Type getOverlayXY() {
        return overlayXY;
    }

    /**
     * Sets the value of the overlayXY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vec2Type }
     *     
     */
    public void setOverlayXY(Vec2Type value) {
        this.overlayXY = value;
    }

    /**
     * Gets the value of the screenXY property.
     * 
     * @return
     *     possible object is
     *     {@link Vec2Type }
     *     
     */
    public Vec2Type getScreenXY() {
        return screenXY;
    }

    /**
     * Sets the value of the screenXY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vec2Type }
     *     
     */
    public void setScreenXY(Vec2Type value) {
        this.screenXY = value;
    }

    /**
     * Gets the value of the rotationXY property.
     * 
     * @return
     *     possible object is
     *     {@link Vec2Type }
     *     
     */
    public Vec2Type getRotationXY() {
        return rotationXY;
    }

    /**
     * Sets the value of the rotationXY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vec2Type }
     *     
     */
    public void setRotationXY(Vec2Type value) {
        this.rotationXY = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Vec2Type }
     *     
     */
    public Vec2Type getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vec2Type }
     *     
     */
    public void setSize(Vec2Type value) {
        this.size = value;
    }

    /**
     * Gets the value of the rotation property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRotation() {
        return rotation;
    }

    /**
     * Sets the value of the rotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRotation(Double value) {
        this.rotation = value;
    }

    /**
     * Gets the value of the screenOverlaySimpleExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the screenOverlaySimpleExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScreenOverlaySimpleExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getScreenOverlaySimpleExtensionGroups() {
        if (screenOverlaySimpleExtensionGroups == null) {
            screenOverlaySimpleExtensionGroups = new ArrayList<Object>();
        }
        return this.screenOverlaySimpleExtensionGroups;
    }

    /**
     * Gets the value of the screenOverlayObjectExtensionGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the screenOverlayObjectExtensionGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScreenOverlayObjectExtensionGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AbstractObjectType }
     * 
     * 
     */
    public List<AbstractObjectType> getScreenOverlayObjectExtensionGroups() {
        if (screenOverlayObjectExtensionGroups == null) {
            screenOverlayObjectExtensionGroups = new ArrayList<AbstractObjectType>();
        }
        return this.screenOverlayObjectExtensionGroups;
    }
}
