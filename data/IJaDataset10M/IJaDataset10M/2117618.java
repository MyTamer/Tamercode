package org.jaffa.patterns.library.object_viewer_meta_2_0.domain.impl;

public class CriteriaFieldImpl implements org.jaffa.patterns.library.object_viewer_meta_2_0.domain.CriteriaField, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    protected java.lang.String _DataType;

    protected java.lang.String _DomainField;

    protected java.lang.String _Name;

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr com.sun.msv.grammar.SequenceExp         xr com.sun.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/grammar/Expression;L exp2q ~ xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xpá5ppsq ~  3ppsr \'com.sun.msv.grammar.trex.ElementPattern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExp        Z ignoreUndeclaredAttributesL \fcontentModelq ~ xq ~  ®3pp sr com.sun.msv.grammar.DataExp        L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~  ®3tppsr #com.sun.msv.datatype.xsd.StringType        Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat stringsr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1JMoIÛ¤G  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv.grammar.Expression$NullSetExpression         xq ~    \nppsr com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass         xpt Namet  sq ~  Îpp sq ~  Îppsr )com.sun.msv.datatype.xsd.EnumerationFacet        L valuest Ljava/util/Set;xr 9com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT  xr *com.sun.msv.datatype.xsd.DataTypeWithFacet        Z \fisFacetFixedZ needValueCheckFlagL \bbaseTypet )Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L \fconcreteTypet \'Lcom/sun/msv/datatype/xsd/ConcreteType;L \tfacetNameq ~ xq ~ q ~ #t \tdataTypesq ~   q ~ q ~ t enumerationsr java.util.HashSetºD¸·4  xpw\f   @?@     *t LongRawt \bDateOnlyt Rawt clobt \bdateonlyt \tDATE_ONLYt \bcurrencyt longrawt BLOBt LONGRAWt \tdate_timet \tDate_Timet \bCurrencyt \nLongStringt booleant STRINGt \bDateTimet decimalt stringt Blobt \nlongstringt DECIMALt Decimalt Clobt Stringt blobt INTEGERt CLOBt \tdate_onlyt BOOLEANt integert \bCURRENCYt \bDATETIMEt \tDATE_TIMEt rawt Integert \tDate_Onlyt RAWt \nLONGSTRINGt Booleant \bdatetimet \bDATEONLYxq ~ sq ~ q ~ -q ~ #sq ~ t \bDataTypeq ~ #sq ~  ®3pp q ~ sq ~ t DomainFieldq ~ #sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ b[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppq ~ q ~ pppppppppppppppppppppppppppppppppppppppp");

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return org.jaffa.patterns.library.object_viewer_meta_2_0.domain.CriteriaField.class;
    }

    public java.lang.String getDataType() {
        return _DataType;
    }

    public void setDataType(java.lang.String value) {
        _DataType = value;
    }

    public java.lang.String getDomainField() {
        return _DomainField;
    }

    public void setDomainField(java.lang.String value) {
        _DomainField = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new org.jaffa.patterns.library.object_viewer_meta_2_0.domain.impl.CriteriaFieldImpl.Unmarshaller(context);
    }

    public java.lang.Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS();
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "Name");
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Name));
        } catch (java.lang.Exception e) {
            com.sun.xml.bind.marshaller.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "DataType");
        context.endAttributes();
        try {
            context.text(((java.lang.String) _DataType));
        } catch (java.lang.Exception e) {
            com.sun.xml.bind.marshaller.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "DomainField");
        context.endAttributes();
        try {
            context.text(((java.lang.String) _DomainField));
        } catch (java.lang.Exception e) {
            com.sun.xml.bind.marshaller.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.jaffa.patterns.library.object_viewer_meta_2_0.domain.CriteriaField.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return org.jaffa.patterns.library.object_viewer_meta_2_0.domain.impl.CriteriaFieldImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
                case 3:
                    if (("" == ___uri) && ("DataType" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 4;
                        return;
                    }
                    break;
                case 6:
                    if (("" == ___uri) && ("DomainField" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 7;
                        return;
                    }
                    break;
                case 0:
                    if (("" == ___uri) && ("Name" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    break;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
                case 8:
                    if (("" == ___uri) && ("DomainField" == ___local)) {
                        context.popAttributes();
                        state = 9;
                        return;
                    }
                    break;
                case 5:
                    if (("" == ___uri) && ("DataType" == ___local)) {
                        context.popAttributes();
                        state = 6;
                        return;
                    }
                    break;
                case 2:
                    if (("" == ___uri) && ("Name" == ___local)) {
                        context.popAttributes();
                        state = 3;
                        return;
                    }
                    break;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    revertToParentFromLeaveAttribute(___uri, ___local);
                    return;
            }
            super.leaveAttribute(___uri, ___local);
        }

        public void text(java.lang.String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 9:
                        revertToParentFromText(value);
                        return;
                    case 4:
                        try {
                            _DataType = value;
                        } catch (java.lang.Exception e) {
                            handleParseConversionException(e);
                        }
                        state = 5;
                        return;
                    case 1:
                        try {
                            _Name = value;
                        } catch (java.lang.Exception e) {
                            handleParseConversionException(e);
                        }
                        state = 2;
                        return;
                    case 7:
                        try {
                            _DomainField = value;
                        } catch (java.lang.Exception e) {
                            handleParseConversionException(e);
                        }
                        state = 8;
                        return;
                }
            } catch (java.lang.RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }
    }
}
