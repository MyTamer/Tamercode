package coordinator.wsaddressing;

public class AttributedQName implements java.io.Serializable, org.apache.axis.encoding.SimpleType {

    private javax.xml.namespace.QName _value;

    public AttributedQName() {
    }

    public AttributedQName(javax.xml.namespace.QName _value) {
        this._value = _value;
    }

    public AttributedQName(java.lang.String _value) {
        this._value = new javax.xml.namespace.QName(_value);
    }

    public java.lang.String toString() {
        return _value == null ? null : _value.toString();
    }

    /**
     * Gets the _value value for this AttributedQName.
     * 
     * @return _value
     */
    public javax.xml.namespace.QName get_value() {
        return _value;
    }

    /**
     * Sets the _value value for this AttributedQName.
     * 
     * @param _value
     */
    public void set_value(javax.xml.namespace.QName _value) {
        this._value = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributedQName)) return false;
        AttributedQName other = (AttributedQName) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this._value == null && other.get_value() == null) || (this._value != null && this._value.equals(other.get_value())));
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
        if (get_value() != null) {
            _hashCode += get_value().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AttributedQName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "AttributedQName"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "QName"));
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
        return new org.apache.axis.encoding.ser.SimpleSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.SimpleDeserializer(_javaType, _xmlType, typeDesc);
    }
}
