package com.google.checkout.schema._2.impl;

public class RequestReceivedElementImpl extends com.google.checkout.schema._2.impl.RequestReceivedResponseImpl implements com.google.checkout.schema._2.RequestReceivedElement, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

    public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.google.checkout.schema._2.RequestReceivedElement.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://checkout.google.com/schema/2";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "request-received";
    }

    public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
        return new com.google.checkout.schema._2.impl.RequestReceivedElementImpl.Unmarshaller(context);
    }

    public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://checkout.google.com/schema/2", "request-received");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.google.checkout.schema._2.RequestReceivedElement.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr  com.sun.msv.g" + "rammar.AttributeExp        L expq ~ L \tnameClassq ~ xq" + " ~ ppsr com.sun.msv.grammar.DataExp        L dtt Lorg" + "/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/" + "msv/util/StringPair;xq ~ ppsr #com.sun.msv.datatype.xsd.Str" + "ingType        Z \risAlwaysValidxr *com.sun.msv.datatype.x" + "sd.BuiltinAtomicType         xr %com.sun.msv.datatype.xsd." + "ConcreteType         xr \'com.sun.msv.datatype.xsd.XSDataty" + "peImpl        L \fnamespaceUrit Ljava/lang/String;L \btype" + "Nameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpac" + "eProcessor;xpt  http://www.w3.org/2001/XMLSchemat stringsr " + "5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve      " + "   xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor       " + "  xpsr 0com.sun.msv.grammar.Expression$NullSetExpression " + "        xq ~ ppsr com.sun.msv.util.StringPairÐtjB  " + "L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr #com.sun" + ".msv.grammar.SimpleNameClass        L \tlocalNameq ~ L \fn" + "amespaceURIq ~ xr com.sun.msv.grammar.NameClass         " + "xpt \rserial-numbert  sr com.sun.msv.grammar.ChoiceExp      " + "   xq ~ \bppsq ~ \nsr java.lang.BooleanÍ rÕúî Z valuex" + "p psq ~ \fppsr \"com.sun.msv.datatype.xsd.QnameType         " + "xq ~ q ~ t QNamesr 5com.sun.msv.datatype.xsd.WhiteSpacePr" + "ocessor$Collapse         xq ~ q ~ sq ~ q ~ -q ~ sq ~  " + "t typet )http://www.w3.org/2001/XMLSchema-instancesr 0com.s" + "un.msv.grammar.Expression$EpsilonExpression         xq ~ " + "sq ~ (q ~ 5sq ~  t request-receivedt #http://checkout.goog" + "le.com/schema/2sr \"com.sun.msv.grammar.ExpressionPool       " + " L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$Closed" + "Hash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐN" + "ïèí I countB \rstreamVersionL parentt $Lcom/sun/msv/gram" + "mar/ExpressionPool;xp   pq ~ \tq ~ &x"));
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
            return com.google.checkout.schema._2.impl.RequestReceivedElementImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 0:
                        if (("request-received" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.pushAttributes(__atts, false);
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
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 2:
                        if (("request-received" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
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
                    case 1:
                        if (("serial-number" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((com.google.checkout.schema._2.impl.RequestReceivedResponseImpl) com.google.checkout.schema._2.impl.RequestReceivedElementImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                    case 1:
                        attIdx = context.getAttribute("", "serial-number");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
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
                            attIdx = context.getAttribute("", "serial-number");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
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
    }
}
