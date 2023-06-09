package de.beas.explicanto.model.jaxb.impl;

public class TopImpl implements de.beas.explicanto.model.jaxb.Top, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, de.beas.explicanto.model.jaxb.impl.runtime.UnmarshallableObject, de.beas.explicanto.model.jaxb.impl.runtime.XMLSerializable, de.beas.explicanto.model.jaxb.impl.runtime.ValidatableObject {

    protected boolean has_Value;

    protected int _Value;

    public static final java.lang.Class version = (de.beas.explicanto.model.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    public TopImpl() {
    }

    public TopImpl(int value) {
        _Value = value;
        has_Value = true;
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (de.beas.explicanto.model.jaxb.Top.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://www.explicanto.de/DesignModel";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "top";
    }

    public int getValue() {
        return _Value;
    }

    public void setValue(int value) {
        _Value = value;
        has_Value = true;
    }

    public de.beas.explicanto.model.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(de.beas.explicanto.model.jaxb.impl.runtime.UnmarshallingContext context) {
        return new de.beas.explicanto.model.jaxb.impl.TopImpl.Unmarshaller(context);
    }

    public void serializeBody(de.beas.explicanto.model.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
        context.startElement("http://www.explicanto.de/DesignModel", "top");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Value)), "Value");
        } catch (java.lang.Exception e) {
            de.beas.explicanto.model.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(de.beas.explicanto.model.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
    }

    public void serializeURIs(de.beas.explicanto.model.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Value) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Value"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (de.beas.explicanto.model.jaxb.Top.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr com.sun.msv.g" + "rammar.DataExp        L dtt Lorg/relaxng/datatype/Datat" + "ype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq " + "~ ppsr  com.sun.msv.datatype.xsd.IntType         xr +com." + "sun.msv.datatype.xsd.IntegerDerivedTypeñ]&6k¾ L \nbaseFac" + "etst )Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xr *com.sun.m" + "sv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv." + "datatype.xsd.ConcreteType         xr \'com.sun.msv.datatype" + ".xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/S" + "tring;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/" + "xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchem" + "at intsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Coll" + "apse         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProces" + "sor         xpsr *com.sun.msv.datatype.xsd.MaxInclusiveFac" + "et         xr #com.sun.msv.datatype.xsd.RangeFacet       " + " L \nlimitValuet Ljava/lang/Object;xr 9com.sun.msv.datatyp" + "e.xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT  xr *com.sun" + ".msv.datatype.xsd.DataTypeWithFacet        Z \fisFacetFixe" + "dZ needValueCheckFlagL \bbaseTypeq ~ L \fconcreteTypet \'Lcom" + "/sun/msv/datatype/xsd/ConcreteType;L \tfacetNameq ~ xq ~ pp" + "q ~  sr *com.sun.msv.datatype.xsd.MinInclusiveFacet       " + "  xq ~ ppq ~   sr !com.sun.msv.datatype.xsd.LongType    " + "     xq ~ q ~ t longq ~ sq ~ ppq ~  sq ~ #ppq ~   " + "sr $com.sun.msv.datatype.xsd.IntegerType         xq ~ q ~" + " t integerq ~ sr ,com.sun.msv.datatype.xsd.FractionDigits" + "Facet        I scalexr ;com.sun.msv.datatype.xsd.DataTyp" + "eWithLexicalConstraintFacetT>zbê  xq ~  ppq ~  sr #com" + ".sun.msv.datatype.xsd.NumberType         xq ~ q ~ t dec" + "imalq ~ q ~ 1t fractionDigits    q ~ +t \fminInclusivesr j" + "ava.lang.Long;äÌ#ß J valuexr java.lang.Number¬à" + "  xp       q ~ +t \fmaxInclusivesq ~ 5ÿÿÿÿÿÿÿq ~ &q ~ 4sr " + "java.lang.Integerâ ¤÷8 I valuexq ~ 6   q ~ &q ~ 8sq " + "~ :ÿÿÿsr 0com.sun.msv.grammar.Expression$NullSetExpression " + "        xq ~ ppsr com.sun.msv.util.StringPairÐtjB  " + "L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr com.sun" + ".msv.grammar.ChoiceExp         xq ~ \bppsr  com.sun.msv.gra" + "mmar.AttributeExp        L expq ~ L \tnameClassq ~ xq ~" + " sr java.lang.BooleanÍ rÕúî Z valuexp psq ~ \nppsr \"co" + "m.sun.msv.datatype.xsd.QnameType         xq ~ q ~ t QNa" + "meq ~ q ~ >sq ~ ?q ~ Jq ~ sr #com.sun.msv.grammar.SimpleNa" + "meClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr c" + "om.sun.msv.grammar.NameClass         xpt typet )http://ww" + "w.w3.org/2001/XMLSchema-instancesr 0com.sun.msv.grammar.Expr" + "ession$EpsilonExpression         xq ~ sq ~ Epsq ~ Lt to" + "pt $http://www.explicanto.de/DesignModelsr \"com.sun.msv.gram" + "mar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/gram" + "mar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.Expr" + "essionPool$ClosedHash×jÐNïèí I countB \rstreamVersionL p" + "arentt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ Bq ~" + " \tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends de.beas.explicanto.model.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(de.beas.explicanto.model.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(de.beas.explicanto.model.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return de.beas.explicanto.model.jaxb.impl.TopImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        if (("top" == ___local) && ("http://www.explicanto.de/DesignModel" == ___uri)) {
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
                        if (("top" == ___local) && ("http://www.explicanto.de/DesignModel" == ___uri)) {
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
                            eatText1(value);
                            state = 2;
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
                _Value = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Value = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
