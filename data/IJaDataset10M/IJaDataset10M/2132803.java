package de.beas.explicanto.jaxb.impl;

public class UnitElementImpl extends de.beas.explicanto.jaxb.impl.UnitElementTypeImpl implements de.beas.explicanto.jaxb.UnitElement, java.io.Serializable, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, de.beas.explicanto.jaxb.impl.runtime.UnmarshallableObject, de.beas.explicanto.jaxb.impl.runtime.XMLSerializable, de.beas.explicanto.jaxb.impl.runtime.ValidatableObject {

    private static final long serialVersionUID = 12343L;

    public static final java.lang.Class version = (de.beas.explicanto.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (de.beas.explicanto.jaxb.UnitElement.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "UnitElement";
    }

    public de.beas.explicanto.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(de.beas.explicanto.jaxb.impl.runtime.UnmarshallingContext context) {
        return new de.beas.explicanto.jaxb.impl.UnitElementImpl.Unmarshaller(context);
    }

    public void serializeBody(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "UnitElement");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (de.beas.explicanto.jaxb.UnitElement.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~ ppsq ~ pps" + "q ~  pp sq ~ ppsr com.sun.msv.grammar.DataExp        L " + "dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet " + "Lcom/sun/msv/util/StringPair;xq ~ ppsr !com.sun.msv.dataty" + "pe.xsd.LongType         xr +com.sun.msv.datatype.xsd.Integ" + "erDerivedTypeñ]&6k¾ L \nbaseFacetst )Lcom/sun/msv/datatyp" + "e/xsd/XSDatatypeImpl;xr *com.sun.msv.datatype.xsd.BuiltinAto" + "micType         xr %com.sun.msv.datatype.xsd.ConcreteType " + "        xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl       " + " L \fnamespaceUrit Ljava/lang/String;L \btypeNameq ~ L \nw" + "hiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xp" + "t  http://www.w3.org/2001/XMLSchemat longsr 5com.sun.msv.da" + "tatype.xsd.WhiteSpaceProcessor$Collapse         xr ,com.su" + "n.msv.datatype.xsd.WhiteSpaceProcessor         xpsr *com.s" + "un.msv.datatype.xsd.MaxInclusiveFacet         xr #com.sun." + "msv.datatype.xsd.RangeFacet        L \nlimitValuet Ljava/" + "lang/Object;xr 9com.sun.msv.datatype.xsd.DataTypeWithValueCo" + "nstraintFacet\"§RoÊÇT  xr *com.sun.msv.datatype.xsd.DataTyp" + "eWithFacet        Z \fisFacetFixedZ needValueCheckFlagL \b" + "baseTypeq ~ L \fconcreteTypet \'Lcom/sun/msv/datatype/xsd/Con" + "creteType;L \tfacetNameq ~ xq ~ ppq ~  sr *com.sun.msv.da" + "tatype.xsd.MinInclusiveFacet         xq ~ !ppq ~   sr $co" + "m.sun.msv.datatype.xsd.IntegerType         xq ~ q ~ t i" + "ntegerq ~ sr ,com.sun.msv.datatype.xsd.FractionDigitsFacet " + "       I scalexr ;com.sun.msv.datatype.xsd.DataTypeWithL" + "exicalConstraintFacetT>zbê  xq ~ $ppq ~  sr #com.sun.m" + "sv.datatype.xsd.NumberType         xq ~ q ~ t decimalq " + "~ q ~ 0t fractionDigits    q ~ *t \fminInclusivesr java.la" + "ng.Long;äÌ#ß J valuexr java.lang.Number¬à  xp" + "       q ~ *t \fmaxInclusivesq ~ 4ÿÿÿÿÿÿÿsr 0com.sun.msv.gra" + "mmar.Expression$NullSetExpression         xq ~ ppsr com." + "sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamesp" + "aceURIq ~ xpq ~ q ~ sr com.sun.msv.grammar.ChoiceExp    " + "     xq ~ \bppsr  com.sun.msv.grammar.AttributeExp       " + " L expq ~ L \tnameClassq ~ xq ~ sr java.lang.BooleanÍ r" + "Õúî Z valuexp psq ~ ppsr \"com.sun.msv.datatype.xsd.Qna" + "meType         xq ~ q ~ t QNameq ~ q ~ :sq ~ ;q ~ Fq ~" + " sr #com.sun.msv.grammar.SimpleNameClass        L \tlocal" + "Nameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameCla" + "ss         xpt typet )http://www.w3.org/2001/XMLSchema-in" + "stancesr 0com.sun.msv.grammar.Expression$EpsilonExpression  " + "       xq ~ sq ~ Aq ~ Nsq ~ Ht uidt %http://www.bea-ser" + "vices.de/explicantosq ~  pp sq ~ ppsq ~ ppsr #com.sun.msv." + "datatype.xsd.StringType        Z \risAlwaysValidxq ~ q ~ " + "t stringsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$P" + "reserve         xq ~ q ~ :sq ~ ;q ~ Xq ~ sq ~ =ppsq ~ ?" + "q ~ Bpq ~ Cq ~ Jq ~ Nsq ~ Ht \n_unitTitleq ~ Rsq ~ =ppsr  com" + ".sun.msv.grammar.OneOrMoreExp         xr com.sun.msv.gram" + "mar.UnaryExp        L expq ~ xq ~ q ~ Bpsq ~  q ~ Bp s" + "q ~ ppsq ~  pp sq ~ =ppsq ~ aq ~ Bpsq ~ ?q ~ Bpsr 2com.sun." + "msv.grammar.Expression$AnyStringExpression         xq ~ q" + " ~ Oq ~ ksr  com.sun.msv.grammar.AnyNameClass         xq ~" + " Iq ~ Nsq ~ Ht  de.beas.explicanto.jaxb.UnitItemt +http://ja" + "va.sun.com/jaxb/xjc/dummy-elementssq ~ =ppsq ~ ?q ~ Bpq ~ Cq" + " ~ Jq ~ Nsq ~ Ht \n_unitItemsq ~ Rq ~ Nsq ~ =ppsq ~ ?q ~ Bpq " + "~ Cq ~ Jq ~ Nsq ~ Ht UnitElementt  sr \"com.sun.msv.grammar." + "ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/" + "ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.Expressi" + "onPool$ClosedHash×jÐNïèí I countB \rstreamVersionL paren" + "tt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ \tq ~ \rq " + "~ cq ~ hq ~ >q ~ \\q ~ qq ~ uq ~ Tq ~ gq ~ eq ~ `q ~ \nq ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends de.beas.explicanto.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(de.beas.explicanto.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(de.beas.explicanto.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return de.beas.explicanto.jaxb.impl.UnitElementImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        if (("uid" == ___local) && ("http://www.bea-services.de/explicanto" == ___uri)) {
                            spawnHandlerFromEnterElement((((de.beas.explicanto.jaxb.impl.UnitElementTypeImpl) de.beas.explicanto.jaxb.impl.UnitElementImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("UnitElement" == ___local) && ("" == ___uri)) {
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
                    case 2:
                        if (("UnitElement" == ___local) && ("" == ___uri)) {
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
