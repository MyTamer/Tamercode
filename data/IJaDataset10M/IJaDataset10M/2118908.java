package eu.more.compressionservice.test.generated.jaxb.impl;

public class LocalTestTypeImpl implements eu.more.compressionservice.test.generated.jaxb.LocalTestType, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    protected java.lang.String _In;

    public static final java.lang.Class version = (eu.more.compressionservice.test.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.compressionservice.test.generated.jaxb.LocalTestType.class);
    }

    public java.lang.String getIn() {
        return _In;
    }

    public void setIn(java.lang.String value) {
        _In = value;
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.compressionservice.test.generated.jaxb.impl.LocalTestTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.ist-more.org/CompressionTest/", "in");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _In), "In");
        } catch (java.lang.Exception e) {
            org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.compressionservice.test.generated.jaxb.LocalTestType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr com.sun.msv.g" + "rammar.DataExp        L dtt Lorg/relaxng/datatype/Datat" + "ype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq " + "~ ppsr #com.sun.msv.datatype.xsd.StringType        Z \ris" + "AlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType   " + "      xr %com.sun.msv.datatype.xsd.ConcreteType         " + "xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnam" + "espaceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet" + " .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://" + "www.w3.org/2001/XMLSchemat stringsr 5com.sun.msv.datatype.x" + "sd.WhiteSpaceProcessor$Preserve         xr ,com.sun.msv.da" + "tatype.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv." + "grammar.Expression$NullSetExpression         xq ~ ppsr c" + "om.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnam" + "espaceURIq ~ xpq ~ q ~ sr com.sun.msv.grammar.ChoiceExp " + "        xq ~ \bppsr  com.sun.msv.grammar.AttributeExp      " + "  L expq ~ L \tnameClassq ~ xq ~ sr java.lang.Boolean" + "Í rÕúî Z valuexp psq ~ \nppsr \"com.sun.msv.datatype.xsd." + "QnameType         xq ~ q ~ t QNamesr 5com.sun.msv.datat" + "ype.xsd.WhiteSpaceProcessor$Collapse         xq ~ q ~ sq" + " ~ q ~ \'q ~ sr #com.sun.msv.grammar.SimpleNameClass       " + " L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.gra" + "mmar.NameClass         xpt typet )http://www.w3.org/2001/" + "XMLSchema-instancesr 0com.sun.msv.grammar.Expression$Epsilon" + "Expression         xq ~ sq ~ \"psq ~ +t int (http://www." + "ist-more.org/CompressionTest/sr \"com.sun.msv.grammar.Express" + "ionPool        L \bexpTablet /Lcom/sun/msv/grammar/Express" + "ionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$" + "ClosedHash×jÐNïèí I countB \rstreamVersionL parentt $Lco" + "m/sun/msv/grammar/ExpressionPool;xp   pq ~ q ~ \tx"));
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
            return eu.more.compressionservice.test.generated.jaxb.impl.LocalTestTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        if (("in" == ___local) && ("http://www.ist-more.org/CompressionTest/" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 2:
                        if (("in" == ___local) && ("http://www.ist-more.org/CompressionTest/" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
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
                        case 1:
                            state = 2;
                            eatText1(value);
                            return;
                        case 3:
                            revertToParentFromText(value);
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
                _In = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
