package com.google.checkout.schema._2.impl;

public class ArchiveOrderRequestImpl implements com.google.checkout.schema._2.ArchiveOrderRequest, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

    protected java.lang.String _GoogleOrderNumber;

    public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.google.checkout.schema._2.ArchiveOrderRequest.class);
    }

    public java.lang.String getGoogleOrderNumber() {
        return _GoogleOrderNumber;
    }

    public void setGoogleOrderNumber(java.lang.String value) {
        _GoogleOrderNumber = value;
    }

    public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
        return new com.google.checkout.schema._2.impl.ArchiveOrderRequestImpl.Unmarshaller(context);
    }

    public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startAttribute("", "google-order-number");
        try {
            context.text(((java.lang.String) _GoogleOrderNumber), "GoogleOrderNumber");
        } catch (java.lang.Exception e) {
            com.google.checkout.schema._2.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.google.checkout.schema._2.ArchiveOrderRequest.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr  com.sun.msv.grammar.AttributeExp        L expt  " + "Lcom/sun/msv/grammar/Expression;L \tnameClasst Lcom/sun/msv/" + "grammar/NameClass;xr com.sun.msv.grammar.ExpressionøèN5~O" + " L epsilonReducibilityt Ljava/lang/Boolean;L expandedEx" + "pq ~ xpppsr com.sun.msv.grammar.DataExp        L dtt " + "Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/" + "sun/msv/util/StringPair;xq ~ ppsr \"com.sun.msv.datatype.xsd" + ".TokenType         xr #com.sun.msv.datatype.xsd.StringType" + "        Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.Buil" + "tinAtomicType         xr %com.sun.msv.datatype.xsd.Concret" + "eType         xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl " + "       L \fnamespaceUrit Ljava/lang/String;L \btypeNameq ~" + " L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProces" + "sor;xpt  http://www.w3.org/2001/XMLSchemat tokensr 5com.sun" + ".msv.datatype.xsd.WhiteSpaceProcessor$Collapse         xr " + ",com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xps" + "r 0com.sun.msv.grammar.Expression$NullSetExpression       " + "  xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \tlocal" + "Nameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr #com.sun.msv.gra" + "mmar.SimpleNameClass        L \tlocalNameq ~ L \fnamespace" + "URIq ~ xr com.sun.msv.grammar.NameClass         xpt goo" + "gle-order-numbert  sr \"com.sun.msv.grammar.ExpressionPool   " + "     L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$Cl" + "osedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash" + "×jÐNïèí I countB \rstreamVersionL parentt $Lcom/sun/msv/" + "grammar/ExpressionPool;xp    px"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.google.checkout.schema._2.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.google.checkout.schema._2.impl.ArchiveOrderRequestImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "google-order-number");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _GoogleOrderNumber = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "google-order-number");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
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
                    case 0:
                        if (("google-order-number" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        break;
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
                    case 0:
                        attIdx = context.getAttribute("", "google-order-number");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case 2:
                        if (("google-order-number" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
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
                        case 0:
                            attIdx = context.getAttribute("", "google-order-number");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText1(v);
                                continue outer;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
