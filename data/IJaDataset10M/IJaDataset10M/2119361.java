package com.google.checkout.schema._2.impl;

public class PickupImpl implements com.google.checkout.schema._2.Pickup, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

    protected com.google.checkout.schema._2.Money _Price;

    protected java.lang.String _Name;

    public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.google.checkout.schema._2.Pickup.class);
    }

    public com.google.checkout.schema._2.Money getPrice() {
        return _Price;
    }

    public void setPrice(com.google.checkout.schema._2.Money value) {
        _Price = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
        return new com.google.checkout.schema._2.impl.PickupImpl.Unmarshaller(context);
    }

    public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://checkout.google.com/schema/2", "price");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Price), "Price");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Price), "Price");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Price), "Price");
        context.endElement();
    }

    public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startAttribute("", "name");
        try {
            context.text(((java.lang.String) _Name), "Name");
        } catch (java.lang.Exception e) {
            com.google.checkout.schema._2.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.google.checkout.schema._2.Pickup.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsr \'com.sun.msv.grammar.trex.ElementPatt" + "ern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;" + "xr com.sun.msv.grammar.ElementExp        Z ignoreUndecl" + "aredAttributesL \fcontentModelq ~ xq ~ pp sq ~  ppsq ~ pp " + "sr com.sun.msv.grammar.ChoiceExp         xq ~ ppsr  com." + "sun.msv.grammar.OneOrMoreExp         xr com.sun.msv.gramm" + "ar.UnaryExp        L expq ~ xq ~ sr java.lang.Boolean" + "Í rÕúî Z valuexp psr  com.sun.msv.grammar.AttributeExp " + "       L expq ~ L \tnameClassq ~ xq ~ q ~ psr 2com.su" + "n.msv.grammar.Expression$AnyStringExpression         xq ~ " + "sq ~ q ~ sr  com.sun.msv.grammar.AnyNameClass         " + "xr com.sun.msv.grammar.NameClass         xpsr 0com.sun.ms" + "v.grammar.Expression$EpsilonExpression         xq ~ q ~ " + "q ~ sr #com.sun.msv.grammar.SimpleNameClass        L \tlo" + "calNamet Ljava/lang/String;L \fnamespaceURIq ~ xq ~ t #com" + ".google.checkout.schema._2.Moneyt +http://java.sun.com/jaxb/" + "xjc/dummy-elementssq ~ \fppsq ~ q ~ psr com.sun.msv.gramma" + "r.DataExp        L dtt Lorg/relaxng/datatype/Datatype;L" + " exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ pp" + "sr \"com.sun.msv.datatype.xsd.QnameType         xr *com.sun" + ".msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.ms" + "v.datatype.xsd.ConcreteType         xr \'com.sun.msv.dataty" + "pe.xsd.XSDatatypeImpl        L \fnamespaceUriq ~ L \btypeN" + "ameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpace" + "Processor;xpt  http://www.w3.org/2001/XMLSchemat QNamesr 5c" + "om.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse       " + "  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor       " + "  xpsr 0com.sun.msv.grammar.Expression$NullSetExpression    " + "     xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \t" + "localNameq ~ L \fnamespaceURIq ~ xpq ~ /q ~ .sq ~ t typet" + " )http://www.w3.org/2001/XMLSchema-instanceq ~ sq ~ t pri" + "cet #http://checkout.google.com/schema/2sq ~ ppsq ~ $ppsr #" + "com.sun.msv.datatype.xsd.StringType        Z \risAlwaysVal" + "idxq ~ )q ~ .t stringsr 5com.sun.msv.datatype.xsd.WhiteSpac" + "eProcessor$Preserve         xq ~ 1q ~ 4sq ~ 5q ~ Aq ~ .sq" + " ~ t namet  sr \"com.sun.msv.grammar.ExpressionPool       " + " L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedH" + "ash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNï" + "èí I countB \rstreamVersionL parentt $Lcom/sun/msv/gramm" + "ar/ExpressionPool;xp   pq ~ \nq ~ \"q ~ q ~ q ~ \rx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.google.checkout.schema._2.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.google.checkout.schema._2.impl.PickupImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 4:
                        attIdx = context.getAttribute("", "currency");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 6:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 3:
                        if (("price" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "name");
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
                _Name = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 5:
                        if (("price" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return;
                        }
                        break;
                    case 4:
                        attIdx = context.getAttribute("", "currency");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 6:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "name");
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
                    case 4:
                        if (("currency" == ___local) && ("" == ___uri)) {
                            _Price = ((com.google.checkout.schema._2.impl.MoneyImpl) spawnChildFromEnterAttribute((com.google.checkout.schema._2.impl.MoneyImpl.class), 5, ___uri, ___local, ___qname));
                            return;
                        }
                        break;
                    case 6:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 0:
                        if (("name" == ___local) && ("" == ___uri)) {
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
                    case 4:
                        attIdx = context.getAttribute("", "currency");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 2:
                        if (("name" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 6:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
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
                        case 4:
                            attIdx = context.getAttribute("", "currency");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                        case 1:
                            state = 2;
                            eatText1(value);
                            return;
                        case 6:
                            revertToParentFromText(value);
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "name");
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
