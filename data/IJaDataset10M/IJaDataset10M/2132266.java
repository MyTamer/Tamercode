package net.seagis.ows.v100;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * This type is adapted from the EnvelopeType of GML 3.1, with modified contents and documentation for encoding a MINIMUM size box SURROUNDING all associated data. 
 * 
 * <p>Java class for BoundingBoxType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BoundingBoxType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LowerCorner" type="{http://www.opengis.net/ows}PositionType"/>
 *         &lt;element name="UpperCorner" type="{http://www.opengis.net/ows}PositionType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="crs" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="dimensions" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoundingBoxType", propOrder = { "lowerCorner", "upperCorner" })
@XmlSeeAlso({ WGS84BoundingBoxType.class })
public class BoundingBoxType {

    @XmlList
    @XmlElement(name = "LowerCorner", type = Double.class)
    private List<Double> lowerCorner = new ArrayList<Double>();

    @XmlList
    @XmlElement(name = "UpperCorner", type = Double.class)
    private List<Double> upperCorner = new ArrayList<Double>();

    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    private String crs;

    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    private BigInteger dimensions;

    public BoundingBoxType() {
    }

    /**
     * Build a 2 dimension boundingBox.
     * 
     * @param crs
     * @param maxx
     * @param maxy
     * @param minx
     * @param miny
     */
    public BoundingBoxType(String crs, double minx, double miny, double maxx, double maxy) {
        this.dimensions = new BigInteger("2");
        this.lowerCorner.add(minx);
        this.lowerCorner.add(miny);
        this.upperCorner.add(maxx);
        this.upperCorner.add(maxy);
        this.crs = crs;
    }

    /**
     * Gets the value of the lowerCorner property.
     * (unmodifiable)
     */
    public List<Double> getLowerCorner() {
        return Collections.unmodifiableList(lowerCorner);
    }

    /**
     * Set the lower corner list.
     */
    public void setLowerCorner(List<Double> lowerCorner) {
        this.lowerCorner = lowerCorner;
    }

    /**
     * Set the upper corner list.
     */
    public void setUpperCorner(List<Double> upperCorner) {
        this.upperCorner = upperCorner;
    }

    /**
     * add a point to the lower corner list.
     */
    public void setLowerCorner(Double point) {
        this.lowerCorner.add(point);
    }

    /**
     * add a point to the upper corner list.
     */
    public void setUpperCorner(Double point) {
        this.upperCorner.add(point);
    }

    /**
     * Gets the value of the upperCorner property.
     * (unmodifiable)
     */
    public List<Double> getUpperCorner() {
        return Collections.unmodifiableList(upperCorner);
    }

    /**
     * Gets the value of the crs property.
     * 
     */
    public String getCrs() {
        return crs;
    }

    /**
     * set the crs value
     */
    public void setCrs(String crs) {
        this.crs = crs;
    }

    /**
     * Gets the value of the dimensions property.
     */
    public BigInteger getDimensions() {
        return dimensions;
    }
}
