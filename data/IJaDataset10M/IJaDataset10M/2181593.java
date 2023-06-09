package com.google.api.adwords.v201101.cm;

public class AuthorizationErrorReason implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected AuthorizationErrorReason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _UNABLE_TO_AUTHORIZE = "UNABLE_TO_AUTHORIZE";

    public static final java.lang.String _NO_ADWORDS_ACCOUNT_FOR_CUSTOMER = "NO_ADWORDS_ACCOUNT_FOR_CUSTOMER";

    public static final java.lang.String _USER_PERMISSION_DENIED = "USER_PERMISSION_DENIED";

    public static final java.lang.String _EFFECTIVE_USER_PERMISSION_DENIED = "EFFECTIVE_USER_PERMISSION_DENIED";

    public static final java.lang.String _USER_HAS_READONLY_PERMISSION = "USER_HAS_READONLY_PERMISSION";

    public static final java.lang.String _NO_CUSTOMER_FOUND = "NO_CUSTOMER_FOUND";

    public static final AuthorizationErrorReason UNABLE_TO_AUTHORIZE = new AuthorizationErrorReason(_UNABLE_TO_AUTHORIZE);

    public static final AuthorizationErrorReason NO_ADWORDS_ACCOUNT_FOR_CUSTOMER = new AuthorizationErrorReason(_NO_ADWORDS_ACCOUNT_FOR_CUSTOMER);

    public static final AuthorizationErrorReason USER_PERMISSION_DENIED = new AuthorizationErrorReason(_USER_PERMISSION_DENIED);

    public static final AuthorizationErrorReason EFFECTIVE_USER_PERMISSION_DENIED = new AuthorizationErrorReason(_EFFECTIVE_USER_PERMISSION_DENIED);

    public static final AuthorizationErrorReason USER_HAS_READONLY_PERMISSION = new AuthorizationErrorReason(_USER_HAS_READONLY_PERMISSION);

    public static final AuthorizationErrorReason NO_CUSTOMER_FOUND = new AuthorizationErrorReason(_NO_CUSTOMER_FOUND);

    public java.lang.String getValue() {
        return _value_;
    }

    public static AuthorizationErrorReason fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        AuthorizationErrorReason enumeration = (AuthorizationErrorReason) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static AuthorizationErrorReason fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }

    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public java.lang.String toString() {
        return _value_;
    }

    public java.lang.Object readResolve() throws java.io.ObjectStreamException {
        return fromValue(_value_);
    }

    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumSerializer(_javaType, _xmlType);
    }

    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumDeserializer(_javaType, _xmlType);
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AuthorizationErrorReason.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201101", "AuthorizationError.Reason"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}
