package org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl;

public class CodedValueTypeImpl implements org.openhealthexchange.openpixpdq.ihe.audit.jaxb.CodedValueType, com.sun.xml.bind.JAXBObject, org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.ValidatableObject {

    protected java.lang.String _DisplayName;

    protected java.lang.String _OriginalText;

    protected java.lang.String _Code;

    protected java.lang.String _CodeSystemName;

    protected java.lang.String _CodeSystem;

    public static final java.lang.Class version = (org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthexchange.openpixpdq.ihe.audit.jaxb.CodedValueType.class);
    }

    public java.lang.String getDisplayName() {
        return _DisplayName;
    }

    public void setDisplayName(java.lang.String value) {
        _DisplayName = value;
    }

    public java.lang.String getOriginalText() {
        return _OriginalText;
    }

    public void setOriginalText(java.lang.String value) {
        _OriginalText = value;
    }

    public java.lang.String getCode() {
        return _Code;
    }

    public void setCode(java.lang.String value) {
        _Code = value;
    }

    public java.lang.String getCodeSystemName() {
        return _CodeSystemName;
    }

    public void setCodeSystemName(java.lang.String value) {
        _CodeSystemName = value;
    }

    public java.lang.String getCodeSystem() {
        return _CodeSystem;
    }

    public void setCodeSystem(java.lang.String value) {
        _CodeSystem = value;
    }

    public org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.CodedValueTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributes(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (_CodeSystem != null) {
            context.startAttribute("", "codeSystem");
            try {
                context.text(((java.lang.String) _CodeSystem), "CodeSystem");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_CodeSystemName != null) {
            context.startAttribute("", "codeSystemName");
            try {
                context.text(((java.lang.String) _CodeSystemName), "CodeSystemName");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "code");
        try {
            context.text(((java.lang.String) _Code), "Code");
        } catch (java.lang.Exception e) {
            org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (_DisplayName != null) {
            context.startAttribute("", "displayName");
            try {
                context.text(((java.lang.String) _DisplayName), "DisplayName");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_OriginalText != null) {
            context.startAttribute("", "originalText");
            try {
                context.text(((java.lang.String) _OriginalText), "OriginalText");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
    }

    public void serializeURIs(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthexchange.openpixpdq.ihe.audit.jaxb.CodedValueType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsq ~  ppsq ~  ppsq ~  ppsr  com.sun.msv." + "grammar.AttributeExp        L expq ~ L \tnameClasst Lco" + "m/sun/msv/grammar/NameClass;xq ~ ppsr com.sun.msv.grammar." + "DataExp        L dtt Lorg/relaxng/datatype/Datatype;L " + "exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ ppsr" + " #com.sun.msv.datatype.xsd.StringType        Z \risAlwaysV" + "alidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType        " + " xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'com" + ".sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceU" + "rit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/" + "sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3." + "org/2001/XMLSchemat stringsr 5com.sun.msv.datatype.xsd.Whit" + "eSpaceProcessor$Preserve         xr ,com.sun.msv.datatype." + "xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv.grammar" + ".Expression$NullSetExpression         xq ~ ppsr com.sun." + "msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceU" + "RIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleNameClass  " + "      L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.ms" + "v.grammar.NameClass         xpt codet  sr com.sun.msv.gr" + "ammar.ChoiceExp         xq ~ ppsq ~ \tsr java.lang.Boolea" + "nÍ rÕúî Z valuexp pq ~ sq ~  t displayNameq ~ $sr 0co" + "m.sun.msv.grammar.Expression$EpsilonExpression         xq " + "~ sq ~ (q ~ -sq ~ %ppsq ~ \tq ~ )pq ~ sq ~  t \foriginalTex" + "tq ~ $q ~ -sq ~ %ppsq ~ \tq ~ )psq ~ \fppsr (com.sun.msv.datat" + "ype.xsd.WhiteSpaceFacet         xr *com.sun.msv.datatype.x" + "sd.DataTypeWithFacet        Z \fisFacetFixedZ needValueCh" + "eckFlagL \bbaseTypet )Lcom/sun/msv/datatype/xsd/XSDatatypeImp" + "l;L \fconcreteTypet \'Lcom/sun/msv/datatype/xsd/ConcreteType;L" + " \tfacetNameq ~ xq ~ q ~ $t OIDsr 5com.sun.msv.datatype.xs" + "d.WhiteSpaceProcessor$Collapse         xq ~   q ~ q ~ t" + " \nwhiteSpaceq ~ sq ~ q ~ ;q ~ $sq ~  t \ncodeSystemq ~ $q ~" + " -sq ~ %ppsq ~ \tq ~ )pq ~ sq ~  t codeSystemNameq ~ $q ~ -" + "sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTable" + "t /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com." + "sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I count" + "B \rstreamVersionL parentt $Lcom/sun/msv/grammar/ExpressionP" + "ool;xp   \bpq ~ Bq ~ &q ~ q ~ \bq ~ q ~ q ~ 3q ~ /x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthexchange.openpixpdq.ihe.audit.jaxb.impl.CodedValueTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 9:
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 6:
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case 12:
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case 15:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _DisplayName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Code = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _OriginalText = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _CodeSystem = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _CodeSystemName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 9:
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 6:
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case 12:
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case 15:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 9:
                        if (("displayName" == ___local) && ("" == ___uri)) {
                            state = 10;
                            return;
                        }
                        state = 12;
                        continue outer;
                    case 6:
                        if (("code" == ___local) && ("" == ___uri)) {
                            state = 7;
                            return;
                        }
                        break;
                    case 12:
                        if (("originalText" == ___local) && ("" == ___uri)) {
                            state = 13;
                            return;
                        }
                        state = 15;
                        continue outer;
                    case 15:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 0:
                        if (("codeSystem" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        if (("codeSystemName" == ___local) && ("" == ___uri)) {
                            state = 4;
                            return;
                        }
                        state = 6;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 9:
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 14:
                        if (("originalText" == ___local) && ("" == ___uri)) {
                            state = 15;
                            return;
                        }
                        break;
                    case 6:
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case 12:
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case 11:
                        if (("displayName" == ___local) && ("" == ___uri)) {
                            state = 12;
                            return;
                        }
                        break;
                    case 15:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 8:
                        if (("code" == ___local) && ("" == ___uri)) {
                            state = 9;
                            return;
                        }
                        break;
                    case 2:
                        if (("codeSystem" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 5:
                        if (("codeSystemName" == ___local) && ("" == ___uri)) {
                            state = 6;
                            return;
                        }
                        break;
                    case 3:
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                try {
                    switch(state) {
                        case 9:
                            attIdx = context.getAttribute("", "displayName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 12;
                                continue outer;
                            }
                            state = 12;
                            continue outer;
                        case 4:
                            eatText5(value);
                            state = 5;
                            return;
                        case 10:
                            eatText1(value);
                            state = 11;
                            return;
                        case 7:
                            eatText2(value);
                            state = 8;
                            return;
                        case 6:
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 9;
                                continue outer;
                            }
                            break;
                        case 12:
                            attIdx = context.getAttribute("", "originalText");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 15;
                                continue outer;
                            }
                            state = 15;
                            continue outer;
                        case 13:
                            eatText3(value);
                            state = 14;
                            return;
                        case 15:
                            revertToParentFromText(value);
                            return;
                        case 1:
                            eatText4(value);
                            state = 2;
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case 3:
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText5(v);
                                state = 6;
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
