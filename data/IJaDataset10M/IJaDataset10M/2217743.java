package com.sforce.soap._2006._04.metadata;

public class CustomTab extends com.sforce.soap._2006._04.metadata.Metadata implements java.io.Serializable {

    private java.lang.Boolean customObject;

    private java.lang.String description;

    private java.lang.Integer frameHeight;

    private java.lang.Boolean hasSidebar;

    private java.lang.String icon;

    private java.lang.String label;

    private boolean mobileReady;

    private java.lang.String motif;

    private java.lang.String page;

    private java.lang.String scontrol;

    private java.lang.String splashPageLink;

    private java.lang.String url;

    private com.sforce.soap._2006._04.metadata.Encoding urlEncodingKey;

    public CustomTab() {
    }

    public CustomTab(java.lang.String fullName, java.lang.Boolean customObject, java.lang.String description, java.lang.Integer frameHeight, java.lang.Boolean hasSidebar, java.lang.String icon, java.lang.String label, boolean mobileReady, java.lang.String motif, java.lang.String page, java.lang.String scontrol, java.lang.String splashPageLink, java.lang.String url, com.sforce.soap._2006._04.metadata.Encoding urlEncodingKey) {
        super(fullName);
        this.customObject = customObject;
        this.description = description;
        this.frameHeight = frameHeight;
        this.hasSidebar = hasSidebar;
        this.icon = icon;
        this.label = label;
        this.mobileReady = mobileReady;
        this.motif = motif;
        this.page = page;
        this.scontrol = scontrol;
        this.splashPageLink = splashPageLink;
        this.url = url;
        this.urlEncodingKey = urlEncodingKey;
    }

    /**
     * Gets the customObject value for this CustomTab.
     * 
     * @return customObject
     */
    public java.lang.Boolean getCustomObject() {
        return customObject;
    }

    /**
     * Sets the customObject value for this CustomTab.
     * 
     * @param customObject
     */
    public void setCustomObject(java.lang.Boolean customObject) {
        this.customObject = customObject;
    }

    /**
     * Gets the description value for this CustomTab.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description value for this CustomTab.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the frameHeight value for this CustomTab.
     * 
     * @return frameHeight
     */
    public java.lang.Integer getFrameHeight() {
        return frameHeight;
    }

    /**
     * Sets the frameHeight value for this CustomTab.
     * 
     * @param frameHeight
     */
    public void setFrameHeight(java.lang.Integer frameHeight) {
        this.frameHeight = frameHeight;
    }

    /**
     * Gets the hasSidebar value for this CustomTab.
     * 
     * @return hasSidebar
     */
    public java.lang.Boolean getHasSidebar() {
        return hasSidebar;
    }

    /**
     * Sets the hasSidebar value for this CustomTab.
     * 
     * @param hasSidebar
     */
    public void setHasSidebar(java.lang.Boolean hasSidebar) {
        this.hasSidebar = hasSidebar;
    }

    /**
     * Gets the icon value for this CustomTab.
     * 
     * @return icon
     */
    public java.lang.String getIcon() {
        return icon;
    }

    /**
     * Sets the icon value for this CustomTab.
     * 
     * @param icon
     */
    public void setIcon(java.lang.String icon) {
        this.icon = icon;
    }

    /**
     * Gets the label value for this CustomTab.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }

    /**
     * Sets the label value for this CustomTab.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    /**
     * Gets the mobileReady value for this CustomTab.
     * 
     * @return mobileReady
     */
    public boolean isMobileReady() {
        return mobileReady;
    }

    /**
     * Sets the mobileReady value for this CustomTab.
     * 
     * @param mobileReady
     */
    public void setMobileReady(boolean mobileReady) {
        this.mobileReady = mobileReady;
    }

    /**
     * Gets the motif value for this CustomTab.
     * 
     * @return motif
     */
    public java.lang.String getMotif() {
        return motif;
    }

    /**
     * Sets the motif value for this CustomTab.
     * 
     * @param motif
     */
    public void setMotif(java.lang.String motif) {
        this.motif = motif;
    }

    /**
     * Gets the page value for this CustomTab.
     * 
     * @return page
     */
    public java.lang.String getPage() {
        return page;
    }

    /**
     * Sets the page value for this CustomTab.
     * 
     * @param page
     */
    public void setPage(java.lang.String page) {
        this.page = page;
    }

    /**
     * Gets the scontrol value for this CustomTab.
     * 
     * @return scontrol
     */
    public java.lang.String getScontrol() {
        return scontrol;
    }

    /**
     * Sets the scontrol value for this CustomTab.
     * 
     * @param scontrol
     */
    public void setScontrol(java.lang.String scontrol) {
        this.scontrol = scontrol;
    }

    /**
     * Gets the splashPageLink value for this CustomTab.
     * 
     * @return splashPageLink
     */
    public java.lang.String getSplashPageLink() {
        return splashPageLink;
    }

    /**
     * Sets the splashPageLink value for this CustomTab.
     * 
     * @param splashPageLink
     */
    public void setSplashPageLink(java.lang.String splashPageLink) {
        this.splashPageLink = splashPageLink;
    }

    /**
     * Gets the url value for this CustomTab.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }

    /**
     * Sets the url value for this CustomTab.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    /**
     * Gets the urlEncodingKey value for this CustomTab.
     * 
     * @return urlEncodingKey
     */
    public com.sforce.soap._2006._04.metadata.Encoding getUrlEncodingKey() {
        return urlEncodingKey;
    }

    /**
     * Sets the urlEncodingKey value for this CustomTab.
     * 
     * @param urlEncodingKey
     */
    public void setUrlEncodingKey(com.sforce.soap._2006._04.metadata.Encoding urlEncodingKey) {
        this.urlEncodingKey = urlEncodingKey;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CustomTab)) return false;
        CustomTab other = (CustomTab) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.customObject == null && other.getCustomObject() == null) || (this.customObject != null && this.customObject.equals(other.getCustomObject()))) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && ((this.frameHeight == null && other.getFrameHeight() == null) || (this.frameHeight != null && this.frameHeight.equals(other.getFrameHeight()))) && ((this.hasSidebar == null && other.getHasSidebar() == null) || (this.hasSidebar != null && this.hasSidebar.equals(other.getHasSidebar()))) && ((this.icon == null && other.getIcon() == null) || (this.icon != null && this.icon.equals(other.getIcon()))) && ((this.label == null && other.getLabel() == null) || (this.label != null && this.label.equals(other.getLabel()))) && this.mobileReady == other.isMobileReady() && ((this.motif == null && other.getMotif() == null) || (this.motif != null && this.motif.equals(other.getMotif()))) && ((this.page == null && other.getPage() == null) || (this.page != null && this.page.equals(other.getPage()))) && ((this.scontrol == null && other.getScontrol() == null) || (this.scontrol != null && this.scontrol.equals(other.getScontrol()))) && ((this.splashPageLink == null && other.getSplashPageLink() == null) || (this.splashPageLink != null && this.splashPageLink.equals(other.getSplashPageLink()))) && ((this.url == null && other.getUrl() == null) || (this.url != null && this.url.equals(other.getUrl()))) && ((this.urlEncodingKey == null && other.getUrlEncodingKey() == null) || (this.urlEncodingKey != null && this.urlEncodingKey.equals(other.getUrlEncodingKey())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCustomObject() != null) {
            _hashCode += getCustomObject().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getFrameHeight() != null) {
            _hashCode += getFrameHeight().hashCode();
        }
        if (getHasSidebar() != null) {
            _hashCode += getHasSidebar().hashCode();
        }
        if (getIcon() != null) {
            _hashCode += getIcon().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        _hashCode += (isMobileReady() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMotif() != null) {
            _hashCode += getMotif().hashCode();
        }
        if (getPage() != null) {
            _hashCode += getPage().hashCode();
        }
        if (getScontrol() != null) {
            _hashCode += getScontrol().hashCode();
        }
        if (getSplashPageLink() != null) {
            _hashCode += getSplashPageLink().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getUrlEncodingKey() != null) {
            _hashCode += getUrlEncodingKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(CustomTab.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "CustomTab"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customObject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "customObject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("frameHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "frameHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasSidebar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "hasSidebar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icon");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "icon"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileReady");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "mobileReady"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motif");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "motif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("page");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "page"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scontrol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "scontrol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("splashPageLink");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "splashPageLink"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlEncodingKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "urlEncodingKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://soap.sforce.com/2006/04/metadata", "Encoding"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}
