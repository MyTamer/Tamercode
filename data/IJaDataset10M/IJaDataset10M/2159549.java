package eu.more.localstorage.generated.impl;

public class CopyAllDataToOtherFolderResponseTypeImpl implements eu.more.localstorage.generated.CopyAllDataToOtherFolderResponseType, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    protected boolean has_Num;

    protected int _Num;

    public static final java.lang.Class version = (eu.more.localstorage.generated.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.localstorage.generated.CopyAllDataToOtherFolderResponseType.class);
    }

    public int getNum() {
        return _Num;
    }

    public void setNum(int value) {
        _Num = value;
        has_Num = true;
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderResponseTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Num) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Num"));
        }
        context.startElement("http://www.all.hu/webservices/MoreLocalStorage/", "Num");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Num)), "Num");
        } catch (java.lang.Exception e) {
            org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Num) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Num"));
        }
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (!has_Num) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Num"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.localstorage.generated.CopyAllDataToOtherFolderResponseType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr com.sun.msv.g" + "rammar.DataExp        L dtt Lorg/relaxng/datatype/Datat" + "ype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq " + "~ ppsr  com.sun.msv.datatype.xsd.IntType         xr +com." + "sun.msv.datatype.xsd.IntegerDerivedTypeñ]&6k¾ L \nbaseFac" + "etst )Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xr *com.sun.m" + "sv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv." + "datatype.xsd.ConcreteType         xr \'com.sun.msv.datatype" + ".xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/S" + "tring;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/" + "xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchem" + "at intsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Coll" + "apse         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProces" + "sor         xpsr *com.sun.msv.datatype.xsd.MaxInclusiveFac" + "et         xr #com.sun.msv.datatype.xsd.RangeFacet       " + " L \nlimitValuet Ljava/lang/Object;xr 9com.sun.msv.datatyp" + "e.xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT  xr *com.sun" + ".msv.datatype.xsd.DataTypeWithFacet        Z \fisFacetFixe" + "dZ needValueCheckFlagL \bbaseTypeq ~ L \fconcreteTypet \'Lcom" + "/sun/msv/datatype/xsd/ConcreteType;L \tfacetNameq ~ xq ~ pp" + "q ~  sr *com.sun.msv.datatype.xsd.MinInclusiveFacet       " + "  xq ~ ppq ~   sr !com.sun.msv.datatype.xsd.LongType    " + "     xq ~ q ~ t longq ~ sq ~ ppq ~  sq ~ #ppq ~   " + "sr $com.sun.msv.datatype.xsd.IntegerType         xq ~ q ~" + " t integerq ~ sr ,com.sun.msv.datatype.xsd.FractionDigits" + "Facet        I scalexr ;com.sun.msv.datatype.xsd.DataTyp" + "eWithLexicalConstraintFacetT>zbê  xq ~  ppq ~  sr #com" + ".sun.msv.datatype.xsd.NumberType         xq ~ q ~ t dec" + "imalq ~ q ~ 1t fractionDigits    q ~ +t \fminInclusivesr j" + "ava.lang.Long;äÌ#ß J valuexr java.lang.Number¬à" + "  xp       q ~ +t \fmaxInclusivesq ~ 5ÿÿÿÿÿÿÿq ~ &q ~ 4sr " + "java.lang.Integerâ ¤÷8 I valuexq ~ 6   q ~ &q ~ 8sq " + "~ :ÿÿÿsr 0com.sun.msv.grammar.Expression$NullSetExpression " + "        xq ~ ppsr com.sun.msv.util.StringPairÐtjB  " + "L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr com.sun" + ".msv.grammar.ChoiceExp         xq ~ \bppsr  com.sun.msv.gra" + "mmar.AttributeExp        L expq ~ L \tnameClassq ~ xq ~" + " sr java.lang.BooleanÍ rÕúî Z valuexp psq ~ \nppsr \"co" + "m.sun.msv.datatype.xsd.QnameType         xq ~ q ~ t QNa" + "meq ~ q ~ >sq ~ ?q ~ Jq ~ sr #com.sun.msv.grammar.SimpleNa" + "meClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr c" + "om.sun.msv.grammar.NameClass         xpt typet )http://ww" + "w.w3.org/2001/XMLSchema-instancesr 0com.sun.msv.grammar.Expr" + "ession$EpsilonExpression         xq ~ sq ~ Eq ~ Rsq ~ Lt" + " Numt /http://www.all.hu/webservices/MoreLocalStorage/sr \"c" + "om.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lc" + "om/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.m" + "sv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countB \rst" + "reamVersionL parentt $Lcom/sun/msv/grammar/ExpressionPool;x" + "p   pq ~ Bq ~ \tx"));
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
            return eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderResponseTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        if (("Num" == ___local) && ("http://www.all.hu/webservices/MoreLocalStorage/" == ___uri)) {
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
                        if (("Num" == ___local) && ("http://www.all.hu/webservices/MoreLocalStorage/" == ___uri)) {
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
                _Num = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Num = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
