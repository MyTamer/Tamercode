package eu.more.triggerservice.generated.jaxb.impl;

public class DataConditionFulfiledResponseImpl implements eu.more.triggerservice.generated.jaxb.DataConditionFulfiledResponse, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    protected boolean has_Value;

    protected boolean _Value;

    public static final java.lang.Class version = (eu.more.triggerservice.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    public DataConditionFulfiledResponseImpl() {
    }

    public DataConditionFulfiledResponseImpl(boolean value) {
        _Value = value;
        has_Value = true;
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.triggerservice.generated.jaxb.DataConditionFulfiledResponse.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://www.ist-more.org/TriggerService/";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "DataConditionFulfiledResponse";
    }

    public boolean isValue() {
        return _Value;
    }

    public void setValue(boolean value) {
        _Value = value;
        has_Value = true;
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.triggerservice.generated.jaxb.impl.DataConditionFulfiledResponseImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
        context.startElement("http://www.ist-more.org/TriggerService/", "DataConditionFulfiledResponse");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Value)), "Value");
        } catch (java.lang.Exception e) {
            org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.triggerservice.generated.jaxb.DataConditionFulfiledResponse.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr com.sun.msv.g" + "rammar.DataExp        L dtt Lorg/relaxng/datatype/Datat" + "ype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq " + "~ ppsr $com.sun.msv.datatype.xsd.BooleanType         xr *" + "com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %com" + ".sun.msv.datatype.xsd.ConcreteType         xr \'com.sun.msv" + ".datatype.xsd.XSDatatypeImpl        L \fnamespaceUrit Lja" + "va/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/" + "datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001" + "/XMLSchemat booleansr 5com.sun.msv.datatype.xsd.WhiteSpaceP" + "rocessor$Collapse         xr ,com.sun.msv.datatype.xsd.Whi" + "teSpaceProcessor         xpsr 0com.sun.msv.grammar.Express" + "ion$NullSetExpression         xq ~ ppsr com.sun.msv.util" + ".StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ x" + "pq ~ q ~ sr com.sun.msv.grammar.ChoiceExp         xq ~ " + "\bppsr  com.sun.msv.grammar.AttributeExp        L expq ~ " + "L \tnameClassq ~ xq ~ sr java.lang.BooleanÍ rÕúî Z v" + "aluexp psq ~ \nppsr \"com.sun.msv.datatype.xsd.QnameType      " + "   xq ~ q ~ t QNameq ~ q ~ sq ~ q ~ \'q ~ sr #com.su" + "n.msv.grammar.SimpleNameClass        L \tlocalNameq ~ L \f" + "namespaceURIq ~ xr com.sun.msv.grammar.NameClass        " + " xpt typet )http://www.w3.org/2001/XMLSchema-instancesr 0co" + "m.sun.msv.grammar.Expression$EpsilonExpression         xq " + "~ sq ~ \"psq ~ )t DataConditionFulfiledResponset \'http://w" + "ww.ist-more.org/TriggerService/sr \"com.sun.msv.grammar.Expre" + "ssionPool        L \bexpTablet /Lcom/sun/msv/grammar/Expre" + "ssionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPoo" + "l$ClosedHash×jÐNïèí I countB \rstreamVersionL parentt $L" + "com/sun/msv/grammar/ExpressionPool;xp   pq ~ \tq ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends org.soda.jaxb.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return eu.more.triggerservice.generated.jaxb.impl.DataConditionFulfiledResponseImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("DataConditionFulfiledResponse" == ___local) && ("http://www.ist-more.org/TriggerService/" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 2:
                        if (("DataConditionFulfiledResponse" == ___local) && ("http://www.ist-more.org/TriggerService/" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
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
                        case 3:
                            revertToParentFromText(value);
                            return;
                        case 1:
                            state = 2;
                            eatText1(value);
                            return;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Value = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Value = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
