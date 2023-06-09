package org.collada.colladaschema;

import javolution.util.FastTable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * 			Create a new, named param object in the GLES Runtime, assign it a type, an initial value, and additional attributes at declaration time.
 * 			
 * 
 * <p>Java class for gles_newparam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gles_newparam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="semantic" type="{http://www.w3.org/2001/XMLSchema}NCName" minOccurs="0"/>
 *         &lt;element name="modifier" type="{http://www.collada.org/2005/11/COLLADASchema}fx_modifier_enum_common" minOccurs="0"/>
 *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_basic_type_common"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gles_newparam", propOrder = { "annotates", "semantic", "modifier", "float3X4", "bool4", "float4X2", "samplerState", "float1X4", "bool2", "_enum", "float4X4", "float2", "float1X1", "float2X3", "float3X2", "bool", "float3X1", "float4X1", "int2", "_int", "float4", "_float", "float3", "texturePipeline", "float2X2", "float4X3", "float2X4", "textureUnit", "int4", "float1X2", "int3", "float3X3", "float2X1", "bool3", "float1X3", "surface" })
public class GlesNewparam {

    @XmlElement(name = "annotate")
    protected List<FxAnnotateCommon> annotates;

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String semantic;

    protected FxModifierEnumCommon modifier;

    @XmlList
    @XmlElement(name = "float3x4", type = Double.class)
    protected List<Double> float3X4;

    @XmlList
    @XmlElement(type = Boolean.class)
    protected List<Boolean> bool4;

    @XmlList
    @XmlElement(name = "float4x2", type = Double.class)
    protected List<Double> float4X2;

    @XmlElement(name = "sampler_state")
    protected GlesSamplerState samplerState;

    @XmlList
    @XmlElement(name = "float1x4", type = Double.class)
    protected List<Double> float1X4;

    @XmlList
    @XmlElement(type = Boolean.class)
    protected List<Boolean> bool2;

    @XmlElement(name = "enum")
    protected String _enum;

    @XmlList
    @XmlElement(name = "float4x4", type = Double.class)
    protected List<Double> float4X4;

    @XmlList
    @XmlElement(type = Double.class)
    protected List<Double> float2;

    @XmlElement(name = "float1x1")
    protected Double float1X1;

    @XmlList
    @XmlElement(name = "float2x3", type = Double.class)
    protected List<Double> float2X3;

    @XmlList
    @XmlElement(name = "float3x2", type = Double.class)
    protected List<Double> float3X2;

    protected Boolean bool;

    @XmlList
    @XmlElement(name = "float3x1", type = Double.class)
    protected List<Double> float3X1;

    @XmlList
    @XmlElement(name = "float4x1", type = Double.class)
    protected List<Double> float4X1;

    @XmlList
    @XmlElement(type = Long.class)
    protected List<Long> int2;

    @XmlElement(name = "int")
    protected Long _int;

    @XmlList
    @XmlElement(type = Double.class)
    protected List<Double> float4;

    @XmlElement(name = "float")
    protected Double _float;

    @XmlList
    @XmlElement(type = Double.class)
    protected List<Double> float3;

    @XmlElement(name = "texture_pipeline")
    protected GlesTexturePipeline texturePipeline;

    @XmlList
    @XmlElement(name = "float2x2", type = Double.class)
    protected List<Double> float2X2;

    @XmlList
    @XmlElement(name = "float4x3", type = Double.class)
    protected List<Double> float4X3;

    @XmlList
    @XmlElement(name = "float2x4", type = Double.class)
    protected List<Double> float2X4;

    @XmlElement(name = "texture_unit")
    protected GlesTextureUnit textureUnit;

    @XmlList
    @XmlElement(type = Long.class)
    protected List<Long> int4;

    @XmlList
    @XmlElement(name = "float1x2", type = Double.class)
    protected List<Double> float1X2;

    @XmlList
    @XmlElement(type = Long.class)
    protected List<Long> int3;

    @XmlList
    @XmlElement(name = "float3x3", type = Double.class)
    protected List<Double> float3X3;

    @XmlList
    @XmlElement(name = "float2x1", type = Double.class)
    protected List<Double> float2X1;

    @XmlList
    @XmlElement(type = Boolean.class)
    protected List<Boolean> bool3;

    @XmlList
    @XmlElement(name = "float1x3", type = Double.class)
    protected List<Double> float1X3;

    protected FxSurfaceCommon surface;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sid;

    /**
     * Gets the value of the annotates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FxAnnotateCommon }
     * 
     * 
     */
    public List<FxAnnotateCommon> getAnnotates() {
        if (annotates == null) {
            annotates = new FastTable<FxAnnotateCommon>();
        }
        return this.annotates;
    }

    /**
     * Gets the value of the semantic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemantic() {
        return semantic;
    }

    /**
     * Sets the value of the semantic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemantic(String value) {
        this.semantic = value;
    }

    /**
     * Gets the value of the modifier property.
     * 
     * @return
     *     possible object is
     *     {@link FxModifierEnumCommon }
     *     
     */
    public FxModifierEnumCommon getModifier() {
        return modifier;
    }

    /**
     * Sets the value of the modifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxModifierEnumCommon }
     *     
     */
    public void setModifier(FxModifierEnumCommon value) {
        this.modifier = value;
    }

    /**
     * Gets the value of the float3X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3X4() {
        if (float3X4 == null) {
            float3X4 = new FastTable<Double>();
        }
        return this.float3X4;
    }

    /**
     * Gets the value of the bool4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool4() {
        if (bool4 == null) {
            bool4 = new FastTable<Boolean>();
        }
        return this.bool4;
    }

    /**
     * Gets the value of the float4X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4X2() {
        if (float4X2 == null) {
            float4X2 = new FastTable<Double>();
        }
        return this.float4X2;
    }

    /**
     * Gets the value of the samplerState property.
     * 
     * @return
     *     possible object is
     *     {@link GlesSamplerState }
     *     
     */
    public GlesSamplerState getSamplerState() {
        return samplerState;
    }

    /**
     * Sets the value of the samplerState property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesSamplerState }
     *     
     */
    public void setSamplerState(GlesSamplerState value) {
        this.samplerState = value;
    }

    /**
     * Gets the value of the float1X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat1X4() {
        if (float1X4 == null) {
            float1X4 = new FastTable<Double>();
        }
        return this.float1X4;
    }

    /**
     * Gets the value of the bool2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool2() {
        if (bool2 == null) {
            bool2 = new FastTable<Boolean>();
        }
        return this.bool2;
    }

    /**
     * Gets the value of the enum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnum() {
        return _enum;
    }

    /**
     * Sets the value of the enum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnum(String value) {
        this._enum = value;
    }

    /**
     * Gets the value of the float4X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4X4() {
        if (float4X4 == null) {
            float4X4 = new FastTable<Double>();
        }
        return this.float4X4;
    }

    /**
     * Gets the value of the float2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2() {
        if (float2 == null) {
            float2 = new FastTable<Double>();
        }
        return this.float2;
    }

    /**
     * Gets the value of the float1X1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFloat1X1() {
        return float1X1;
    }

    /**
     * Sets the value of the float1X1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFloat1X1(Double value) {
        this.float1X1 = value;
    }

    /**
     * Gets the value of the float2X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2X3() {
        if (float2X3 == null) {
            float2X3 = new FastTable<Double>();
        }
        return this.float2X3;
    }

    /**
     * Gets the value of the float3X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3X2() {
        if (float3X2 == null) {
            float3X2 = new FastTable<Double>();
        }
        return this.float3X2;
    }

    /**
     * Gets the value of the bool property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBool() {
        return bool;
    }

    /**
     * Sets the value of the bool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBool(Boolean value) {
        this.bool = value;
    }

    /**
     * Gets the value of the float3X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3X1() {
        if (float3X1 == null) {
            float3X1 = new FastTable<Double>();
        }
        return this.float3X1;
    }

    /**
     * Gets the value of the float4X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4X1() {
        if (float4X1 == null) {
            float4X1 = new FastTable<Double>();
        }
        return this.float4X1;
    }

    /**
     * Gets the value of the int2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getInt2() {
        if (int2 == null) {
            int2 = new FastTable<Long>();
        }
        return this.int2;
    }

    /**
     * Gets the value of the int property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getInt() {
        return _int;
    }

    /**
     * Sets the value of the int property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setInt(Long value) {
        this._int = value;
    }

    /**
     * Gets the value of the float4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4() {
        if (float4 == null) {
            float4 = new FastTable<Double>();
        }
        return this.float4;
    }

    /**
     * Gets the value of the float property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFloat() {
        return _float;
    }

    /**
     * Sets the value of the float property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFloat(Double value) {
        this._float = value;
    }

    /**
     * Gets the value of the float3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3() {
        if (float3 == null) {
            float3 = new FastTable<Double>();
        }
        return this.float3;
    }

    /**
     * Gets the value of the texturePipeline property.
     * 
     * @return
     *     possible object is
     *     {@link GlesTexturePipeline }
     *     
     */
    public GlesTexturePipeline getTexturePipeline() {
        return texturePipeline;
    }

    /**
     * Sets the value of the texturePipeline property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesTexturePipeline }
     *     
     */
    public void setTexturePipeline(GlesTexturePipeline value) {
        this.texturePipeline = value;
    }

    /**
     * Gets the value of the float2X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2X2() {
        if (float2X2 == null) {
            float2X2 = new FastTable<Double>();
        }
        return this.float2X2;
    }

    /**
     * Gets the value of the float4X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4X3() {
        if (float4X3 == null) {
            float4X3 = new FastTable<Double>();
        }
        return this.float4X3;
    }

    /**
     * Gets the value of the float2X4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2X4() {
        if (float2X4 == null) {
            float2X4 = new FastTable<Double>();
        }
        return this.float2X4;
    }

    /**
     * Gets the value of the textureUnit property.
     * 
     * @return
     *     possible object is
     *     {@link GlesTextureUnit }
     *     
     */
    public GlesTextureUnit getTextureUnit() {
        return textureUnit;
    }

    /**
     * Sets the value of the textureUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesTextureUnit }
     *     
     */
    public void setTextureUnit(GlesTextureUnit value) {
        this.textureUnit = value;
    }

    /**
     * Gets the value of the int4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getInt4() {
        if (int4 == null) {
            int4 = new FastTable<Long>();
        }
        return this.int4;
    }

    /**
     * Gets the value of the float1X2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat1X2() {
        if (float1X2 == null) {
            float1X2 = new FastTable<Double>();
        }
        return this.float1X2;
    }

    /**
     * Gets the value of the int3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the int3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInt3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getInt3() {
        if (int3 == null) {
            int3 = new FastTable<Long>();
        }
        return this.int3;
    }

    /**
     * Gets the value of the float3X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3X3() {
        if (float3X3 == null) {
            float3X3 = new FastTable<Double>();
        }
        return this.float3X3;
    }

    /**
     * Gets the value of the float2X1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2X1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2X1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2X1() {
        if (float2X1 == null) {
            float2X1 = new FastTable<Double>();
        }
        return this.float2X1;
    }

    /**
     * Gets the value of the bool3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bool3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBool3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getBool3() {
        if (bool3 == null) {
            bool3 = new FastTable<Boolean>();
        }
        return this.bool3;
    }

    /**
     * Gets the value of the float1X3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float1X3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat1X3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat1X3() {
        if (float1X3 == null) {
            float1X3 = new FastTable<Double>();
        }
        return this.float1X3;
    }

    /**
     * Gets the value of the surface property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceCommon }
     *     
     */
    public FxSurfaceCommon getSurface() {
        return surface;
    }

    /**
     * Sets the value of the surface property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceCommon }
     *     
     */
    public void setSurface(FxSurfaceCommon value) {
        this.surface = value;
    }

    /**
     * Gets the value of the sid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSid() {
        return sid;
    }

    /**
     * Sets the value of the sid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSid(String value) {
        this.sid = value;
    }
}
