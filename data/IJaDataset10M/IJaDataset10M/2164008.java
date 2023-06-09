package com.google.checkout.schema._2.impl;

public class AlternateTaxRuleImpl implements com.google.checkout.schema._2.AlternateTaxRule, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

    protected com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType _TaxArea;

    protected boolean has_Rate;

    protected double _Rate;

    public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.google.checkout.schema._2.AlternateTaxRule.class);
    }

    public com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType getTaxArea() {
        return _TaxArea;
    }

    public void setTaxArea(com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType value) {
        _TaxArea = value;
    }

    public double getRate() {
        return _Rate;
    }

    public void setRate(double value) {
        _Rate = value;
        has_Rate = true;
    }

    public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
        return new com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.Unmarshaller(context);
    }

    public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Rate) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Rate"));
        }
        context.startElement("http://checkout.google.com/schema/2", "rate");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDouble(((double) _Rate)), "Rate");
        } catch (java.lang.Exception e) {
            com.google.checkout.schema._2.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://checkout.google.com/schema/2", "tax-area");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _TaxArea), "TaxArea");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _TaxArea), "TaxArea");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _TaxArea), "TaxArea");
        context.endElement();
    }

    public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Rate) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Rate"));
        }
    }

    public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Rate) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Rate"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.google.checkout.schema._2.AlternateTaxRule.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr !com.sun.msv.grammar.InterleaveExp         xr com." + "sun.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/g" + "rammar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expres" + "sionøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L" + " expandedExpq ~ xpppsr \'com.sun.msv.grammar.trex.ElementPa" + "ttern        L \tnameClasst Lcom/sun/msv/grammar/NameClas" + "s;xr com.sun.msv.grammar.ElementExp        Z ignoreUnde" + "claredAttributesL \fcontentModelq ~ xq ~ pp sr com.sun.msv" + ".grammar.SequenceExp         xq ~ ppsr com.sun.msv.gramm" + "ar.DataExp        L dtt Lorg/relaxng/datatype/Datatype;" + "L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ p" + "psr #com.sun.msv.datatype.xsd.DoubleType         xr +com.s" + "un.msv.datatype.xsd.FloatingNumberTypeüã¶¨|à  xr *com.sun" + ".msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.ms" + "v.datatype.xsd.ConcreteType         xr \'com.sun.msv.dataty" + "pe.xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang" + "/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatyp" + "e/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSch" + "emat doublesr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor" + "$Collapse         xr ,com.sun.msv.datatype.xsd.WhiteSpaceP" + "rocessor         xpsr 0com.sun.msv.grammar.Expression$Null" + "SetExpression         xq ~ ppsr com.sun.msv.util.StringP" + "airÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q " + "~ sr com.sun.msv.grammar.ChoiceExp         xq ~ ppsr  c" + "om.sun.msv.grammar.AttributeExp        L expq ~ L \tname" + "Classq ~ xq ~ sr java.lang.BooleanÍ rÕúî Z valuexp p" + "sq ~ \fppsr \"com.sun.msv.datatype.xsd.QnameType         xq " + "~ q ~ t QNameq ~ q ~ sq ~ q ~ *q ~ sr #com.sun.msv.gr" + "ammar.SimpleNameClass        L \tlocalNameq ~ L \fnamespac" + "eURIq ~ xr com.sun.msv.grammar.NameClass         xpt ty" + "pet )http://www.w3.org/2001/XMLSchema-instancesr 0com.sun.ms" + "v.grammar.Expression$EpsilonExpression         xq ~ sq ~ " + "%q ~ 2sq ~ ,t ratet #http://checkout.google.com/schema/2sq" + " ~ pp sq ~ \nppsq ~ pp sq ~ !ppsr  com.sun.msv.grammar.OneO" + "rMoreExp         xr com.sun.msv.grammar.UnaryExp       " + " L expq ~ xq ~ q ~ &psq ~ #q ~ &psr 2com.sun.msv.grammar" + ".Expression$AnyStringExpression         xq ~ q ~ 3q ~ @sr" + "  com.sun.msv.grammar.AnyNameClass         xq ~ -q ~ 2sq ~" + " ,t :com.google.checkout.schema._2.AlternateTaxRule.TaxAreaT" + "ypet +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ !ppsq " + "~ #q ~ &pq ~ \'q ~ .q ~ 2sq ~ ,t \btax-areaq ~ 6sr \"com.sun.ms" + "v.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/ms" + "v/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.gramma" + "r.ExpressionPool$ClosedHash×jÐNïèí I countB \rstreamVersi" + "onL parentt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq " + "~ 8q ~ q ~ \"q ~ Fq ~ =q ~ :q ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class TaxAreaTypeImpl implements com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType, com.sun.xml.bind.JAXBObject, com.google.checkout.schema._2.impl.runtime.UnmarshallableObject, com.google.checkout.schema._2.impl.runtime.XMLSerializable, com.google.checkout.schema._2.impl.runtime.ValidatableObject {

        protected com.google.checkout.schema._2.USZipArea _UsZipArea;

        protected com.google.checkout.schema._2.USCountryArea _UsCountryArea;

        protected com.google.checkout.schema._2.USStateArea _UsStateArea;

        public static final java.lang.Class version = (com.google.checkout.schema._2.impl.JAXBVersion.class);

        private static com.sun.msv.grammar.Grammar schemaFragment;

        private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType.class);
        }

        public com.google.checkout.schema._2.USZipArea getUsZipArea() {
            return _UsZipArea;
        }

        public void setUsZipArea(com.google.checkout.schema._2.USZipArea value) {
            _UsZipArea = value;
        }

        public com.google.checkout.schema._2.USCountryArea getUsCountryArea() {
            return _UsCountryArea;
        }

        public void setUsCountryArea(com.google.checkout.schema._2.USCountryArea value) {
            _UsCountryArea = value;
        }

        public com.google.checkout.schema._2.USStateArea getUsStateArea() {
            return _UsStateArea;
        }

        public void setUsStateArea(com.google.checkout.schema._2.USStateArea value) {
            _UsStateArea = value;
        }

        public com.google.checkout.schema._2.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
            return new com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl.Unmarshaller(context);
        }

        public void serializeBody(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
            if (((_UsStateArea != null) && (_UsCountryArea == null)) && (_UsZipArea == null)) {
                context.startElement("http://checkout.google.com/schema/2", "us-state-area");
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UsStateArea), "UsStateArea");
                context.endNamespaceDecls();
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UsStateArea), "UsStateArea");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _UsStateArea), "UsStateArea");
                context.endElement();
            } else {
                if (((_UsStateArea == null) && (_UsCountryArea == null)) && (_UsZipArea != null)) {
                    context.startElement("http://checkout.google.com/schema/2", "us-zip-area");
                    context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UsZipArea), "UsZipArea");
                    context.endNamespaceDecls();
                    context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UsZipArea), "UsZipArea");
                    context.endAttributes();
                    context.childAsBody(((com.sun.xml.bind.JAXBObject) _UsZipArea), "UsZipArea");
                    context.endElement();
                } else {
                    if (((_UsStateArea == null) && (_UsCountryArea != null)) && (_UsZipArea == null)) {
                        context.startElement("http://checkout.google.com/schema/2", "us-country-area");
                        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UsCountryArea), "UsCountryArea");
                        context.endNamespaceDecls();
                        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UsCountryArea), "UsCountryArea");
                        context.endAttributes();
                        context.childAsBody(((com.sun.xml.bind.JAXBObject) _UsCountryArea), "UsCountryArea");
                        context.endElement();
                    }
                }
            }
        }

        public void serializeAttributes(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        }

        public void serializeURIs(com.google.checkout.schema._2.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        }

        public java.lang.Class getPrimaryInterface() {
            return (com.google.checkout.schema._2.AlternateTaxRule.TaxAreaType.class);
        }

        public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
            if (schemaFragment == null) {
                schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.ChoiceExp         xr com.sun." + "msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gramm" + "ar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expression" + "øèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L ex" + "pandedExpq ~ xpppsq ~  ppsr \'com.sun.msv.grammar.trex.Eleme" + "ntPattern        L \tnameClasst Lcom/sun/msv/grammar/Name" + "Class;xr com.sun.msv.grammar.ElementExp        Z ignore" + "UndeclaredAttributesL \fcontentModelq ~ xq ~ pp sr com.sun" + ".msv.grammar.SequenceExp         xq ~ ppsq ~ pp sq ~  pp" + "sr  com.sun.msv.grammar.OneOrMoreExp         xr com.sun.m" + "sv.grammar.UnaryExp        L expq ~ xq ~ sr java.lang" + ".BooleanÍ rÕúî Z valuexp psr  com.sun.msv.grammar.Attri" + "buteExp        L expq ~ L \tnameClassq ~ \bxq ~ q ~ psr" + " 2com.sun.msv.grammar.Expression$AnyStringExpression       " + "  xq ~ sq ~ q ~ sr  com.sun.msv.grammar.AnyNameClass   " + "      xr com.sun.msv.grammar.NameClass         xpsr 0co" + "m.sun.msv.grammar.Expression$EpsilonExpression         xq " + "~ q ~ q ~ sr #com.sun.msv.grammar.SimpleNameClass       " + " L \tlocalNamet Ljava/lang/String;L \fnamespaceURIq ~ xq ~" + " t )com.google.checkout.schema._2.USStateAreat +http://java" + ".sun.com/jaxb/xjc/dummy-elementssq ~  ppsq ~ q ~ psr com." + "sun.msv.grammar.DataExp        L dtt Lorg/relaxng/datat" + "ype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/Strin" + "gPair;xq ~ ppsr \"com.sun.msv.datatype.xsd.QnameType       " + "  xr *com.sun.msv.datatype.xsd.BuiltinAtomicType         " + "xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'com." + "sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUr" + "iq ~ L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/" + "xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchem" + "at QNamesr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Co" + "llapse         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProc" + "essor         xpsr 0com.sun.msv.grammar.Expression$NullSet" + "Expression         xq ~ ppsr com.sun.msv.util.StringPair" + "ÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ 0q ~ /" + "sq ~ t typet )http://www.w3.org/2001/XMLSchema-instanceq ~" + " sq ~ t \rus-state-areat #http://checkout.google.com/schema" + "/2sq ~ pp sq ~ ppsq ~ pp sq ~  ppsq ~ q ~ psq ~ q ~ p" + "q ~ q ~ q ~ sq ~ t \'com.google.checkout.schema._2.USZipA" + "reaq ~ \"sq ~  ppsq ~ q ~ pq ~ (q ~ 8q ~ sq ~ t us-zip-a" + "reaq ~ =sq ~ pp sq ~ ppsq ~ pp sq ~  ppsq ~ q ~ psq ~ " + "q ~ pq ~ q ~ q ~ sq ~ t +com.google.checkout.schema._2." + "USCountryAreaq ~ \"sq ~  ppsq ~ q ~ pq ~ (q ~ 8q ~ sq ~ t" + " us-country-areaq ~ =sr \"com.sun.msv.grammar.ExpressionPool" + "        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool" + "$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedH" + "ash×jÐNïèí I countB \rstreamVersionL parentt $Lcom/sun/m" + "sv/grammar/ExpressionPool;xp   pq ~ \fq ~ ?q ~ Kq ~ q ~ #q" + " ~ Fq ~ Rq ~ q ~ Bq ~ Nq ~ q ~ Aq ~ Mq ~ x"));
            }
            return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
        }

        public class Unmarshaller extends com.google.checkout.schema._2.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

            public Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
                super(context, "--------");
            }

            protected Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context, int startState) {
                this(context);
                state = startState;
            }

            public java.lang.Object owner() {
                return com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl.this;
            }

            public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
                int attIdx;
                outer: while (true) {
                    switch(state) {
                        case 3:
                            revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                            return;
                        case 1:
                            attIdx = context.getAttribute("", "country-area");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return;
                            }
                            break;
                        case 4:
                            if (("state" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                _UsStateArea = ((com.google.checkout.schema._2.impl.USStateAreaImpl) spawnChildFromEnterElement((com.google.checkout.schema._2.impl.USStateAreaImpl.class), 5, ___uri, ___local, ___qname, __atts));
                                return;
                            }
                            break;
                        case 6:
                            if (("zip-pattern" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                _UsZipArea = ((com.google.checkout.schema._2.impl.USZipAreaImpl) spawnChildFromEnterElement((com.google.checkout.schema._2.impl.USZipAreaImpl.class), 7, ___uri, ___local, ___qname, __atts));
                                return;
                            }
                            break;
                        case 0:
                            if (("us-state-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                context.pushAttributes(__atts, false);
                                state = 4;
                                return;
                            }
                            if (("us-zip-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                context.pushAttributes(__atts, false);
                                state = 6;
                                return;
                            }
                            if (("us-country-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                context.pushAttributes(__atts, false);
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
                            if (("us-country-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                context.popAttributes();
                                state = 3;
                                return;
                            }
                            break;
                        case 1:
                            attIdx = context.getAttribute("", "country-area");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return;
                            }
                            break;
                        case 5:
                            if (("us-state-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                                context.popAttributes();
                                state = 3;
                                return;
                            }
                            break;
                        case 7:
                            if (("us-zip-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
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
                        case 1:
                            if (("country-area" == ___local) && ("" == ___uri)) {
                                _UsCountryArea = ((com.google.checkout.schema._2.impl.USCountryAreaImpl) spawnChildFromEnterAttribute((com.google.checkout.schema._2.impl.USCountryAreaImpl.class), 2, ___uri, ___local, ___qname));
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
                        case 1:
                            attIdx = context.getAttribute("", "country-area");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
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
                            case 3:
                                revertToParentFromText(value);
                                return;
                            case 1:
                                attIdx = context.getAttribute("", "country-area");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return;
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

    public class Unmarshaller extends com.google.checkout.schema._2.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context) {
            super(context, "-----");
        }

        protected Unmarshaller(com.google.checkout.schema._2.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        if (("tax-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return;
                        }
                        if (("rate" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 3;
                            return;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 1:
                        if (("us-state-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            _TaxArea = ((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl) spawnChildFromEnterElement((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        if (("us-zip-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            _TaxArea = ((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl) spawnChildFromEnterElement((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        if (("us-country-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            _TaxArea = ((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl) spawnChildFromEnterElement((com.google.checkout.schema._2.impl.AlternateTaxRuleImpl.TaxAreaTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
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
                    case 4:
                        if (("rate" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.popAttributes();
                            state = 0;
                            return;
                        }
                        break;
                    case 2:
                        if (("tax-area" == ___local) && ("http://checkout.google.com/schema/2" == ___uri)) {
                            context.popAttributes();
                            state = 0;
                            return;
                        }
                        break;
                    case 0:
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
                    case 0:
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
                    case 0:
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
                        case 0:
                            revertToParentFromText(value);
                            return;
                        case 3:
                            state = 4;
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
                _Rate = javax.xml.bind.DatatypeConverter.parseDouble(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Rate = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
