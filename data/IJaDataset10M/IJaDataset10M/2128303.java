package com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl;

public class SecurityConstraintTypeImpl implements com.c2b2.ipoint.presentation.portlets.jsr168.dd.SecurityConstraintType, com.sun.xml.bind.JAXBObject, com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.UnmarshallableObject, com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.XMLSerializable, com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.ValidatableObject {

    protected com.sun.xml.bind.util.ListImpl _DisplayName;

    protected com.c2b2.ipoint.presentation.portlets.jsr168.dd.PortletCollectionType _PortletCollection;

    protected com.c2b2.ipoint.presentation.portlets.jsr168.dd.UserDataConstraintType _UserDataConstraint;

    protected java.lang.String _Id;

    public static final java.lang.Class version = (com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.c2b2.ipoint.presentation.portlets.jsr168.dd.SecurityConstraintType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getDisplayName() {
        if (_DisplayName == null) {
            _DisplayName = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _DisplayName;
    }

    public java.util.List getDisplayName() {
        return _getDisplayName();
    }

    public com.c2b2.ipoint.presentation.portlets.jsr168.dd.PortletCollectionType getPortletCollection() {
        return _PortletCollection;
    }

    public void setPortletCollection(com.c2b2.ipoint.presentation.portlets.jsr168.dd.PortletCollectionType value) {
        _PortletCollection = value;
    }

    public com.c2b2.ipoint.presentation.portlets.jsr168.dd.UserDataConstraintType getUserDataConstraint() {
        return _UserDataConstraint;
    }

    public void setUserDataConstraint(com.c2b2.ipoint.presentation.portlets.jsr168.dd.UserDataConstraintType value) {
        _UserDataConstraint = value;
    }

    public java.lang.String getId() {
        return _Id;
    }

    public void setId(java.lang.String value) {
        _Id = value;
    }

    public com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.UnmarshallingContext context) {
        return new com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.SecurityConstraintTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = ((_DisplayName == null) ? 0 : _DisplayName.size());
        while (idx1 != len1) {
            context.startElement("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd", "display-name");
            int idx_0 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _DisplayName.get(idx_0++)), "DisplayName");
            context.endNamespaceDecls();
            int idx_1 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _DisplayName.get(idx_1++)), "DisplayName");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _DisplayName.get(idx1++)), "DisplayName");
            context.endElement();
        }
        context.startElement("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd", "portlet-collection");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PortletCollection), "PortletCollection");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PortletCollection), "PortletCollection");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _PortletCollection), "PortletCollection");
        context.endElement();
        context.startElement("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd", "user-data-constraint");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UserDataConstraint), "UserDataConstraint");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UserDataConstraint), "UserDataConstraint");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _UserDataConstraint), "UserDataConstraint");
        context.endElement();
    }

    public void serializeAttributes(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = ((_DisplayName == null) ? 0 : _DisplayName.size());
        if (_Id != null) {
            context.startAttribute("", "id");
            try {
                context.text(((java.lang.String) _Id), "Id");
            } catch (java.lang.Exception e) {
                com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = ((_DisplayName == null) ? 0 : _DisplayName.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.c2b2.ipoint.presentation.portlets.jsr168.dd.SecurityConstraintType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsq ~  ppsq ~  ppsr com.sun.msv.grammar." + "ChoiceExp         xq ~ ppsr  com.sun.msv.grammar.OneOrMor" + "eExp         xr com.sun.msv.grammar.UnaryExp        L " + "expq ~ xq ~ sr java.lang.BooleanÍ rÕúî Z valuexp ps" + "r \'com.sun.msv.grammar.trex.ElementPattern        L \tname" + "Classt Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.gramm" + "ar.ElementExp        Z ignoreUndeclaredAttributesL \fcont" + "entModelq ~ xq ~ q ~ p sq ~  ppsq ~ pp sq ~ \bppsq ~ \nq ~" + " psr  com.sun.msv.grammar.AttributeExp        L expq ~ " + "L \tnameClassq ~ xq ~ q ~ psr 2com.sun.msv.grammar.Expres" + "sion$AnyStringExpression         xq ~ sq ~ \rq ~ sr  com" + ".sun.msv.grammar.AnyNameClass         xr com.sun.msv.gram" + "mar.NameClass         xpsr 0com.sun.msv.grammar.Expression" + "$EpsilonExpression         xq ~ q ~ q ~  sr #com.sun.msv" + ".grammar.SimpleNameClass        L \tlocalNamet Ljava/lang" + "/String;L \fnamespaceURIq ~ \"xq ~ t ?com.c2b2.ipoint.present" + "ation.portlets.jsr168.dd.DisplayNameTypet +http://java.sun.c" + "om/jaxb/xjc/dummy-elementssq ~ \bppsq ~ q ~ psr com.sun.ms" + "v.grammar.DataExp        L dtt Lorg/relaxng/datatype/Da" + "tatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;" + "xq ~ ppsr \"com.sun.msv.datatype.xsd.QnameType         xr " + "*com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %co" + "m.sun.msv.datatype.xsd.ConcreteType         xr \'com.sun.ms" + "v.datatype.xsd.XSDatatypeImpl        L \fnamespaceUriq ~ \"" + "L \btypeNameq ~ \"L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/Wh" + "iteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat QN" + "amesr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse" + "         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor " + "        xpsr 0com.sun.msv.grammar.Expression$NullSetExpres" + "sion         xq ~ ppsr com.sun.msv.util.StringPairÐtjB" + "  L \tlocalNameq ~ \"L \fnamespaceURIq ~ \"xpq ~ 3q ~ 2sq ~ !" + "t typet )http://www.w3.org/2001/XMLSchema-instanceq ~  sq ~" + " !t \fdisplay-namet 6http://java.sun.com/xml/ns/portlet/portl" + "et-app_1_0.xsdq ~  sq ~ pp sq ~  ppsq ~ pp sq ~ \bppsq ~ \nq" + " ~ psq ~ q ~ pq ~ q ~ q ~  sq ~ !t Ecom.c2b2.ipoint.pre" + "sentation.portlets.jsr168.dd.PortletCollectionTypeq ~ %sq ~ " + "\bppsq ~ q ~ pq ~ +q ~ ;q ~  sq ~ !t portlet-collectionq ~" + " @sq ~ pp sq ~  ppsq ~ pp sq ~ \bppsq ~ \nq ~ psq ~ q ~ p" + "q ~ q ~ q ~  sq ~ !t Fcom.c2b2.ipoint.presentation.portlet" + "s.jsr168.dd.UserDataConstraintTypeq ~ %sq ~ \bppsq ~ q ~ pq" + " ~ +q ~ ;q ~  sq ~ !t user-data-constraintq ~ @sq ~ \bppsq ~" + " q ~ psq ~ (ppsr #com.sun.msv.datatype.xsd.StringType     " + "   Z \risAlwaysValidxq ~ -q ~ 2t stringsr 5com.sun.msv.da" + "tatype.xsd.WhiteSpaceProcessor$Preserve         xq ~ 5q ~" + " 8sq ~ 9q ~ ^q ~ 2sq ~ !t idt  q ~  sr \"com.sun.msv.grammar" + ".ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar" + "/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.Express" + "ionPool$ClosedHash×jÐNïèí I countB \rstreamVersionL pare" + "ntt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ \fq ~ Yq" + " ~ q ~ q ~ Eq ~ Qq ~ q ~ q ~ \tq ~ Dq ~ Pq ~ q ~ &q ~ Iq" + " ~ Uq ~ q ~ Bq ~ Nx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.SecurityConstraintTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 12:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 10:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("description" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            _UserDataConstraint = ((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl) spawnChildFromEnterElement((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        if (("transport-guarantee" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            _UserDataConstraint = ((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl) spawnChildFromEnterElement((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        break;
                    case 6:
                        if (("display-name" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return;
                        }
                        if (("portlet-collection" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 4:
                        attIdx = context.getAttribute("http://www.w3.org/XML/1998/namespace", "lang");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 7:
                        if (("portlet-name" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            _PortletCollection = ((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.PortletCollectionTypeImpl) spawnChildFromEnterElement((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.PortletCollectionTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        break;
                    case 9:
                        if (("user-data-constraint" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return;
                        }
                        break;
                    case 3:
                        if (("display-name" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return;
                        }
                        state = 6;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Id = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 11:
                        if (("user-data-constraint" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return;
                        }
                        break;
                    case 12:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 10:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 4:
                        attIdx = context.getAttribute("http://www.w3.org/XML/1998/namespace", "lang");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 5:
                        if (("display-name" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return;
                        }
                        break;
                    case 8:
                        if (("portlet-collection" == ___local) && ("http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return;
                        }
                        break;
                    case 3:
                        state = 6;
                        continue outer;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 12:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 10:
                        if (("id" == ___local) && ("" == ___uri)) {
                            _UserDataConstraint = ((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl) spawnChildFromEnterAttribute((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.UserDataConstraintTypeImpl.class), 11, ___uri, ___local, ___qname));
                            return;
                        }
                        break;
                    case 0:
                        if (("id" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        state = 3;
                        continue outer;
                    case 4:
                        if (("lang" == ___local) && ("http://www.w3.org/XML/1998/namespace" == ___uri)) {
                            _getDisplayName().add(((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.DisplayNameTypeImpl) spawnChildFromEnterAttribute((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.DisplayNameTypeImpl.class), 5, ___uri, ___local, ___qname)));
                            return;
                        }
                        break;
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
                    case 12:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 10:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 4:
                        attIdx = context.getAttribute("http://www.w3.org/XML/1998/namespace", "lang");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 2:
                        if (("id" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
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
                        case 12:
                            revertToParentFromText(value);
                            return;
                        case 1:
                            eatText1(value);
                            state = 2;
                            return;
                        case 10:
                            attIdx = context.getAttribute("", "id");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                        case 0:
                            attIdx = context.getAttribute("", "id");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case 4:
                            attIdx = context.getAttribute("http://www.w3.org/XML/1998/namespace", "lang");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            _getDisplayName().add(((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.DisplayNameTypeImpl) spawnChildFromText((com.c2b2.ipoint.presentation.portlets.jsr168.dd.impl.DisplayNameTypeImpl.class), 5, value)));
                            return;
                        case 3:
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
