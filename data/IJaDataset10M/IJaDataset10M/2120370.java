package eu.more.localstorage.generated.impl;

public class CopyAllDataToOtherFolderFaultImpl extends eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderFaultTypeImpl implements eu.more.localstorage.generated.CopyAllDataToOtherFolderFault, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    public static final java.lang.Class version = (eu.more.localstorage.generated.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.localstorage.generated.CopyAllDataToOtherFolderFault.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://www.all.hu/webservices/MoreLocalStorage/";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "CopyAllDataToOtherFolder_fault";
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderFaultImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.all.hu/webservices/MoreLocalStorage/", "CopyAllDataToOtherFolder_fault");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.localstorage.generated.CopyAllDataToOtherFolderFault.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~  pp sq ~ pp" + "sr com.sun.msv.grammar.DataExp        L dtt Lorg/relax" + "ng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/ut" + "il/StringPair;xq ~ ppsr #com.sun.msv.datatype.xsd.StringTyp" + "e        Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.Bui" + "ltinAtomicType         xr %com.sun.msv.datatype.xsd.Concre" + "teType         xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl" + "        L \fnamespaceUrit Ljava/lang/String;L \btypeNameq " + "~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProce" + "ssor;xpt  http://www.w3.org/2001/XMLSchemat stringsr 5com.s" + "un.msv.datatype.xsd.WhiteSpaceProcessor$Preserve         x" + "r ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xp" + "sr 0com.sun.msv.grammar.Expression$NullSetExpression       " + "  xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \tloc" + "alNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ sr com.sun.msv.g" + "rammar.ChoiceExp         xq ~ \bppsr  com.sun.msv.grammar.A" + "ttributeExp        L expq ~ L \tnameClassq ~ xq ~ sr " + "java.lang.BooleanÍ rÕúî Z valuexp psq ~ \fppsr \"com.sun." + "msv.datatype.xsd.QnameType         xq ~ q ~ t QNamesr 5" + "com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse       " + "  xq ~ q ~ sq ~ q ~ )q ~ sr #com.sun.msv.grammar.Simpl" + "eNameClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr" + " com.sun.msv.grammar.NameClass         xpt typet )http:/" + "/www.w3.org/2001/XMLSchema-instancesr 0com.sun.msv.grammar.E" + "xpression$EpsilonExpression         xq ~ sq ~ $q ~ 3sq ~" + " -t CopyAllDataToOtherFolder_faultt /http://www.all.hu/webs" + "ervices/MoreLocalStorage/sq ~  ppsq ~ \"q ~ %pq ~ &q ~ /q ~ 3" + "sq ~ -t CopyAllDataToOtherFolder_faultq ~ 7sr \"com.sun.msv." + "grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/" + "grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar." + "ExpressionPool$ClosedHash×jÐNïèí I countB \rstreamVersion" + "L parentt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ " + "\tq ~ q ~ !q ~ 8x"));
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
            return eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderFaultImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        if (("CopyAllDataToOtherFolder_fault" == ___local) && ("http://www.all.hu/webservices/MoreLocalStorage/" == ___uri)) {
                            spawnHandlerFromEnterElement((((eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderFaultTypeImpl) eu.more.localstorage.generated.impl.CopyAllDataToOtherFolderFaultImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("CopyAllDataToOtherFolder_fault" == ___local) && ("http://www.all.hu/webservices/MoreLocalStorage/" == ___uri)) {
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
                        if (("CopyAllDataToOtherFolder_fault" == ___local) && ("http://www.all.hu/webservices/MoreLocalStorage/" == ___uri)) {
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
