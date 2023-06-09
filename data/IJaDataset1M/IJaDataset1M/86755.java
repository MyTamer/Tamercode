package nz.ac.massey.xmldad.bookquery.impl;

public class ResponseTypeImpl implements nz.ac.massey.xmldad.bookquery.ResponseType, com.sun.xml.bind.JAXBObject, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallableObject, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializable, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.ValidatableObject {

    protected java.lang.String _Status;

    protected java.lang.String _Message;

    public static final java.lang.Class version = (nz.ac.massey.xmldad.bookquery.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (nz.ac.massey.xmldad.bookquery.ResponseType.class);
    }

    public java.lang.String getStatus() {
        return _Status;
    }

    public void setStatus(java.lang.String value) {
        _Status = value;
    }

    public java.lang.String getMessage() {
        return _Message;
    }

    public void setMessage(java.lang.String value) {
        _Message = value;
    }

    public nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingEventHandler createUnmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context) {
        return new nz.ac.massey.xmldad.bookquery.impl.ResponseTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://xmldad.massey.ac.nz/bookQuery", "status");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Status), "Status");
        } catch (java.lang.Exception e) {
            nz.ac.massey.xmldad.bookcatalogue.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_Message != null) {
            context.startElement("http://xmldad.massey.ac.nz/bookQuery", "message");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Message), "Message");
            } catch (java.lang.Exception e) {
                nz.ac.massey.xmldad.bookcatalogue.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (nz.ac.massey.xmldad.bookquery.ResponseType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsr \'com.sun.msv.grammar.trex.ElementPatt" + "ern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;" + "xr com.sun.msv.grammar.ElementExp        Z ignoreUndecl" + "aredAttributesL \fcontentModelq ~ xq ~ pp sq ~  ppsr com.s" + "un.msv.grammar.DataExp        L dtt Lorg/relaxng/dataty" + "pe/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/String" + "Pair;xq ~ ppsr )com.sun.msv.datatype.xsd.EnumerationFacet  " + "      L valuest Ljava/util/Set;xr 9com.sun.msv.datatype" + ".xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT  xr *com.sun." + "msv.datatype.xsd.DataTypeWithFacet        Z \fisFacetFixed" + "Z needValueCheckFlagL \bbaseTypet )Lcom/sun/msv/datatype/xsd" + "/XSDatatypeImpl;L \fconcreteTypet \'Lcom/sun/msv/datatype/xsd/" + "ConcreteType;L \tfacetNamet Ljava/lang/String;xr \'com.sun.ms" + "v.datatype.xsd.XSDatatypeImpl        L \fnamespaceUriq ~ " + "L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/Wh" + "iteSpaceProcessor;xpt $http://xmldad.massey.ac.nz/bookQueryp" + "sr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve   " + "      xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor    " + "     xp  sr #com.sun.msv.datatype.xsd.StringType        " + "Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicT" + "ype         xr %com.sun.msv.datatype.xsd.ConcreteType     " + "    xq ~ t  http://www.w3.org/2001/XMLSchemat stringq ~ " + "q ~  t enumerationsr java.util.HashSetºD¸·4  xpw\f   " + "?@     t okt errorxsr 0com.sun.msv.grammar.Expression$Nu" + "llSetExpression         xq ~ ppsr com.sun.msv.util.Strin" + "gPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpt st" + "ring-derivedq ~ sr com.sun.msv.grammar.ChoiceExp        " + " xq ~ ppsr  com.sun.msv.grammar.AttributeExp        L e" + "xpq ~ L \tnameClassq ~ xq ~ sr java.lang.BooleanÍ rÕúî" + " Z valuexp psq ~ ppsr \"com.sun.msv.datatype.xsd.QnameType" + "         xq ~ q ~ !t QNamesr 5com.sun.msv.datatype.xsd.W" + "hiteSpaceProcessor$Collapse         xq ~ q ~ )sq ~ *q ~ 6" + "q ~ !sr #com.sun.msv.grammar.SimpleNameClass        L \tlo" + "calNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.Name" + "Class         xpt typet )http://www.w3.org/2001/XMLSchema" + "-instancesr 0com.sun.msv.grammar.Expression$EpsilonExpressio" + "n         xq ~ sq ~ 1q ~ @sq ~ :t statusq ~ sq ~ -ppsq" + " ~ q ~ 2p sq ~  ppsq ~ ppq ~  q ~ )sq ~ *q ~ \"q ~ !sq ~ -p" + "psq ~ /q ~ 2pq ~ 3q ~ <q ~ @sq ~ :t messageq ~ q ~ @sr \"co" + "m.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lco" + "m/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.ms" + "v.grammar.ExpressionPool$ClosedHash×jÐNïèí I countB \rstr" + "eamVersionL parentt $Lcom/sun/msv/grammar/ExpressionPool;xp" + "   pq ~ Fq ~ \nq ~ q ~ Dq ~ .q ~ Ix"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends nz.ac.massey.xmldad.bookcatalogue.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return nz.ac.massey.xmldad.bookquery.impl.ResponseTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 3:
                        if (("message" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return;
                        }
                        state = 6;
                        continue outer;
                    case 0:
                        if (("status" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
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
                    case 6:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 3:
                        state = 6;
                        continue outer;
                    case 2:
                        if (("status" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
                        }
                        break;
                    case 5:
                        if (("message" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
                            context.popAttributes();
                            state = 6;
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
                    case 6:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 3:
                        state = 6;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 3:
                        state = 6;
                        continue outer;
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
                            state = 5;
                            eatText1(value);
                            return;
                        case 6:
                            revertToParentFromText(value);
                            return;
                        case 3:
                            state = 6;
                            continue outer;
                        case 1:
                            state = 2;
                            eatText2(value);
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
                _Message = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Status = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
