package com.google.api.adwords.v201101.o;

/**
 * Basic information about a webpage.
 */
public class WebpageDescriptor implements java.io.Serializable {

    private java.lang.String url;

    private java.lang.String title;

    public WebpageDescriptor() {
    }

    public WebpageDescriptor(java.lang.String url, java.lang.String title) {
        this.url = url;
        this.title = title;
    }

    /**
     * Gets the url value for this WebpageDescriptor.
     * 
     * @return url   * The URL of the webpage.
     */
    public java.lang.String getUrl() {
        return url;
    }

    /**
     * Sets the url value for this WebpageDescriptor.
     * 
     * @param url   * The URL of the webpage.
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    /**
     * Gets the title value for this WebpageDescriptor.
     * 
     * @return title   * The title of the webpage.
     */
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Sets the title value for this WebpageDescriptor.
     * 
     * @param title   * The title of the webpage.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WebpageDescriptor)) return false;
        WebpageDescriptor other = (WebpageDescriptor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.url == null && other.getUrl() == null) || (this.url != null && this.url.equals(other.getUrl()))) && ((this.title == null && other.getTitle() == null) || (this.title != null && this.title.equals(other.getTitle())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(WebpageDescriptor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201101", "WebpageDescriptor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201101", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201101", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
