package org.astm.ccr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for VehicleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VehicleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:astm-org:CCR}Description" minOccurs="0"/>
 *         &lt;element name="Product" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ProductName" type="{urn:astm-org:CCR}CodedDescriptionType"/>
 *                   &lt;element name="BrandName" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                   &lt;element name="Strength" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:astm-org:CCR}MeasureType">
 *                           &lt;sequence>
 *                             &lt;element name="StrengthSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                             &lt;element name="VariableStrengthModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Form" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
 *                           &lt;sequence>
 *                             &lt;element name="FormSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                             &lt;element name="MultipleFormModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Concentration" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:astm-org:CCR}MeasureType">
 *                           &lt;sequence>
 *                             &lt;element name="ConcentrationSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                             &lt;element name="VariableConcentrationModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Size" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
 *                           &lt;sequence>
 *                             &lt;element name="SizeSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                             &lt;element name="VariableSizeModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ProductSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                   &lt;element name="MultipleProductModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Manufacturer" type="{urn:astm-org:CCR}ActorReferenceType" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}IDs" minOccurs="0"/>
 *         &lt;element name="Quantity" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{urn:astm-org:CCR}MeasureType">
 *                 &lt;sequence>
 *                   &lt;element name="QuantitySequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                   &lt;element name="VariableQuantityModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="VehicleSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="MultipleVehicleModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
 *         &lt;element ref="{urn:astm-org:CCR}InternalCCRLink" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VehicleType", propOrder = { "description", "product", "manufacturer", "iDs", "quantity", "vehicleSequencePosition", "multipleVehicleModifier", "internalCCRLink" })
public class VehicleType {

    @XmlElement(name = "Description")
    protected CodedDescriptionType description;

    @XmlElement(name = "Product")
    protected List<VehicleType.Product> product;

    @XmlElement(name = "Manufacturer")
    protected ActorReferenceType manufacturer;

    @XmlElement(name = "IDs")
    protected IDType iDs;

    @XmlElement(name = "Quantity")
    protected List<VehicleType.Quantity> quantity;

    @XmlElement(name = "VehicleSequencePosition")
    protected BigInteger vehicleSequencePosition;

    @XmlElement(name = "MultipleVehicleModifier")
    protected CodedDescriptionType multipleVehicleModifier;

    @XmlElement(name = "InternalCCRLink")
    protected InternalCCRLink internalCCRLink;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link CodedDescriptionType }
     *     
     */
    public CodedDescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodedDescriptionType }
     *     
     */
    public void setDescription(CodedDescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VehicleType.Product }
     * 
     * 
     */
    public List<VehicleType.Product> getProduct() {
        if (product == null) {
            product = new ArrayList<VehicleType.Product>();
        }
        return this.product;
    }

    /**
     * Gets the value of the manufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link ActorReferenceType }
     *     
     */
    public ActorReferenceType getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActorReferenceType }
     *     
     */
    public void setManufacturer(ActorReferenceType value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the iDs property.
     * 
     * @return
     *     possible object is
     *     {@link IDType }
     *     
     */
    public IDType getIDs() {
        return iDs;
    }

    /**
     * Sets the value of the iDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDType }
     *     
     */
    public void setIDs(IDType value) {
        this.iDs = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the quantity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuantity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VehicleType.Quantity }
     * 
     * 
     */
    public List<VehicleType.Quantity> getQuantity() {
        if (quantity == null) {
            quantity = new ArrayList<VehicleType.Quantity>();
        }
        return this.quantity;
    }

    /**
     * Gets the value of the vehicleSequencePosition property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVehicleSequencePosition() {
        return vehicleSequencePosition;
    }

    /**
     * Sets the value of the vehicleSequencePosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVehicleSequencePosition(BigInteger value) {
        this.vehicleSequencePosition = value;
    }

    /**
     * Gets the value of the multipleVehicleModifier property.
     * 
     * @return
     *     possible object is
     *     {@link CodedDescriptionType }
     *     
     */
    public CodedDescriptionType getMultipleVehicleModifier() {
        return multipleVehicleModifier;
    }

    /**
     * Sets the value of the multipleVehicleModifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodedDescriptionType }
     *     
     */
    public void setMultipleVehicleModifier(CodedDescriptionType value) {
        this.multipleVehicleModifier = value;
    }

    /**
     * Gets the value of the internalCCRLink property.
     * 
     * @return
     *     possible object is
     *     {@link InternalCCRLink }
     *     
     */
    public InternalCCRLink getInternalCCRLink() {
        return internalCCRLink;
    }

    /**
     * Sets the value of the internalCCRLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternalCCRLink }
     *     
     */
    public void setInternalCCRLink(InternalCCRLink value) {
        this.internalCCRLink = value;
    }

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
     *         &lt;element name="ProductName" type="{urn:astm-org:CCR}CodedDescriptionType"/>
     *         &lt;element name="BrandName" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *         &lt;element name="Strength" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{urn:astm-org:CCR}MeasureType">
     *                 &lt;sequence>
     *                   &lt;element name="StrengthSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *                   &lt;element name="VariableStrengthModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Form" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
     *                 &lt;sequence>
     *                   &lt;element name="FormSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *                   &lt;element name="MultipleFormModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Concentration" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{urn:astm-org:CCR}MeasureType">
     *                 &lt;sequence>
     *                   &lt;element name="ConcentrationSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *                   &lt;element name="VariableConcentrationModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Size" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
     *                 &lt;sequence>
     *                   &lt;element name="SizeSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *                   &lt;element name="VariableSizeModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ProductSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *         &lt;element name="MultipleProductModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "productName", "brandName", "strength", "form", "concentration", "size", "productSequencePosition", "multipleProductModifier" })
    public static class Product {

        @XmlElement(name = "ProductName", required = true)
        protected CodedDescriptionType productName;

        @XmlElement(name = "BrandName")
        protected CodedDescriptionType brandName;

        @XmlElement(name = "Strength")
        protected List<VehicleType.Product.Strength> strength;

        @XmlElement(name = "Form")
        protected List<VehicleType.Product.Form> form;

        @XmlElement(name = "Concentration")
        protected List<VehicleType.Product.Concentration> concentration;

        @XmlElement(name = "Size")
        protected List<VehicleType.Product.Size> size;

        @XmlElement(name = "ProductSequencePosition")
        protected BigInteger productSequencePosition;

        @XmlElement(name = "MultipleProductModifier")
        protected CodedDescriptionType multipleProductModifier;

        /**
         * Gets the value of the productName property.
         * 
         * @return
         *     possible object is
         *     {@link CodedDescriptionType }
         *     
         */
        public CodedDescriptionType getProductName() {
            return productName;
        }

        /**
         * Sets the value of the productName property.
         * 
         * @param value
         *     allowed object is
         *     {@link CodedDescriptionType }
         *     
         */
        public void setProductName(CodedDescriptionType value) {
            this.productName = value;
        }

        /**
         * Gets the value of the brandName property.
         * 
         * @return
         *     possible object is
         *     {@link CodedDescriptionType }
         *     
         */
        public CodedDescriptionType getBrandName() {
            return brandName;
        }

        /**
         * Sets the value of the brandName property.
         * 
         * @param value
         *     allowed object is
         *     {@link CodedDescriptionType }
         *     
         */
        public void setBrandName(CodedDescriptionType value) {
            this.brandName = value;
        }

        /**
         * Gets the value of the strength property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the strength property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getStrength().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VehicleType.Product.Strength }
         * 
         * 
         */
        public List<VehicleType.Product.Strength> getStrength() {
            if (strength == null) {
                strength = new ArrayList<VehicleType.Product.Strength>();
            }
            return this.strength;
        }

        /**
         * Gets the value of the form property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the form property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getForm().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VehicleType.Product.Form }
         * 
         * 
         */
        public List<VehicleType.Product.Form> getForm() {
            if (form == null) {
                form = new ArrayList<VehicleType.Product.Form>();
            }
            return this.form;
        }

        /**
         * Gets the value of the concentration property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the concentration property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConcentration().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VehicleType.Product.Concentration }
         * 
         * 
         */
        public List<VehicleType.Product.Concentration> getConcentration() {
            if (concentration == null) {
                concentration = new ArrayList<VehicleType.Product.Concentration>();
            }
            return this.concentration;
        }

        /**
         * Gets the value of the size property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the size property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSize().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VehicleType.Product.Size }
         * 
         * 
         */
        public List<VehicleType.Product.Size> getSize() {
            if (size == null) {
                size = new ArrayList<VehicleType.Product.Size>();
            }
            return this.size;
        }

        /**
         * Gets the value of the productSequencePosition property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getProductSequencePosition() {
            return productSequencePosition;
        }

        /**
         * Sets the value of the productSequencePosition property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setProductSequencePosition(BigInteger value) {
            this.productSequencePosition = value;
        }

        /**
         * Gets the value of the multipleProductModifier property.
         * 
         * @return
         *     possible object is
         *     {@link CodedDescriptionType }
         *     
         */
        public CodedDescriptionType getMultipleProductModifier() {
            return multipleProductModifier;
        }

        /**
         * Sets the value of the multipleProductModifier property.
         * 
         * @param value
         *     allowed object is
         *     {@link CodedDescriptionType }
         *     
         */
        public void setMultipleProductModifier(CodedDescriptionType value) {
            this.multipleProductModifier = value;
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{urn:astm-org:CCR}MeasureType">
         *       &lt;sequence>
         *         &lt;element name="ConcentrationSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="VariableConcentrationModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "concentrationSequencePosition", "variableConcentrationModifier" })
        public static class Concentration extends MeasureType {

            @XmlElement(name = "ConcentrationSequencePosition")
            protected BigInteger concentrationSequencePosition;

            @XmlElement(name = "VariableConcentrationModifier")
            protected CodedDescriptionType variableConcentrationModifier;

            /**
             * Gets the value of the concentrationSequencePosition property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getConcentrationSequencePosition() {
                return concentrationSequencePosition;
            }

            /**
             * Sets the value of the concentrationSequencePosition property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setConcentrationSequencePosition(BigInteger value) {
                this.concentrationSequencePosition = value;
            }

            /**
             * Gets the value of the variableConcentrationModifier property.
             * 
             * @return
             *     possible object is
             *     {@link CodedDescriptionType }
             *     
             */
            public CodedDescriptionType getVariableConcentrationModifier() {
                return variableConcentrationModifier;
            }

            /**
             * Sets the value of the variableConcentrationModifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link CodedDescriptionType }
             *     
             */
            public void setVariableConcentrationModifier(CodedDescriptionType value) {
                this.variableConcentrationModifier = value;
            }
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
         *       &lt;sequence>
         *         &lt;element name="FormSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="MultipleFormModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "formSequencePosition", "multipleFormModifier" })
        public static class Form extends CodedDescriptionType {

            @XmlElement(name = "FormSequencePosition")
            protected BigInteger formSequencePosition;

            @XmlElement(name = "MultipleFormModifier")
            protected CodedDescriptionType multipleFormModifier;

            /**
             * Gets the value of the formSequencePosition property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getFormSequencePosition() {
                return formSequencePosition;
            }

            /**
             * Sets the value of the formSequencePosition property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setFormSequencePosition(BigInteger value) {
                this.formSequencePosition = value;
            }

            /**
             * Gets the value of the multipleFormModifier property.
             * 
             * @return
             *     possible object is
             *     {@link CodedDescriptionType }
             *     
             */
            public CodedDescriptionType getMultipleFormModifier() {
                return multipleFormModifier;
            }

            /**
             * Sets the value of the multipleFormModifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link CodedDescriptionType }
             *     
             */
            public void setMultipleFormModifier(CodedDescriptionType value) {
                this.multipleFormModifier = value;
            }
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{urn:astm-org:CCR}CodedDescriptionType">
         *       &lt;sequence>
         *         &lt;element name="SizeSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="VariableSizeModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "sizeSequencePosition", "variableSizeModifier" })
        public static class Size extends CodedDescriptionType {

            @XmlElement(name = "SizeSequencePosition")
            protected BigInteger sizeSequencePosition;

            @XmlElement(name = "VariableSizeModifier")
            protected CodedDescriptionType variableSizeModifier;

            /**
             * Gets the value of the sizeSequencePosition property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getSizeSequencePosition() {
                return sizeSequencePosition;
            }

            /**
             * Sets the value of the sizeSequencePosition property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setSizeSequencePosition(BigInteger value) {
                this.sizeSequencePosition = value;
            }

            /**
             * Gets the value of the variableSizeModifier property.
             * 
             * @return
             *     possible object is
             *     {@link CodedDescriptionType }
             *     
             */
            public CodedDescriptionType getVariableSizeModifier() {
                return variableSizeModifier;
            }

            /**
             * Sets the value of the variableSizeModifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link CodedDescriptionType }
             *     
             */
            public void setVariableSizeModifier(CodedDescriptionType value) {
                this.variableSizeModifier = value;
            }
        }

        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{urn:astm-org:CCR}MeasureType">
         *       &lt;sequence>
         *         &lt;element name="StrengthSequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="VariableStrengthModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "strengthSequencePosition", "variableStrengthModifier" })
        public static class Strength extends MeasureType {

            @XmlElement(name = "StrengthSequencePosition")
            protected BigInteger strengthSequencePosition;

            @XmlElement(name = "VariableStrengthModifier")
            protected CodedDescriptionType variableStrengthModifier;

            /**
             * Gets the value of the strengthSequencePosition property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getStrengthSequencePosition() {
                return strengthSequencePosition;
            }

            /**
             * Sets the value of the strengthSequencePosition property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setStrengthSequencePosition(BigInteger value) {
                this.strengthSequencePosition = value;
            }

            /**
             * Gets the value of the variableStrengthModifier property.
             * 
             * @return
             *     possible object is
             *     {@link CodedDescriptionType }
             *     
             */
            public CodedDescriptionType getVariableStrengthModifier() {
                return variableStrengthModifier;
            }

            /**
             * Sets the value of the variableStrengthModifier property.
             * 
             * @param value
             *     allowed object is
             *     {@link CodedDescriptionType }
             *     
             */
            public void setVariableStrengthModifier(CodedDescriptionType value) {
                this.variableStrengthModifier = value;
            }
        }
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{urn:astm-org:CCR}MeasureType">
     *       &lt;sequence>
     *         &lt;element name="QuantitySequencePosition" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *         &lt;element name="VariableQuantityModifier" type="{urn:astm-org:CCR}CodedDescriptionType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "quantitySequencePosition", "variableQuantityModifier" })
    public static class Quantity extends MeasureType {

        @XmlElement(name = "QuantitySequencePosition")
        protected BigInteger quantitySequencePosition;

        @XmlElement(name = "VariableQuantityModifier")
        protected CodedDescriptionType variableQuantityModifier;

        /**
         * Gets the value of the quantitySequencePosition property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getQuantitySequencePosition() {
            return quantitySequencePosition;
        }

        /**
         * Sets the value of the quantitySequencePosition property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setQuantitySequencePosition(BigInteger value) {
            this.quantitySequencePosition = value;
        }

        /**
         * Gets the value of the variableQuantityModifier property.
         * 
         * @return
         *     possible object is
         *     {@link CodedDescriptionType }
         *     
         */
        public CodedDescriptionType getVariableQuantityModifier() {
            return variableQuantityModifier;
        }

        /**
         * Sets the value of the variableQuantityModifier property.
         * 
         * @param value
         *     allowed object is
         *     {@link CodedDescriptionType }
         *     
         */
        public void setVariableQuantityModifier(CodedDescriptionType value) {
            this.variableQuantityModifier = value;
        }
    }
}
