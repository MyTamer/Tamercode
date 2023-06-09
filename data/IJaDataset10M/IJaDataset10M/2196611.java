package eu.more.gms.generated.jaxb.impl;

public class GroupQueryDetailsImpl implements eu.more.gms.generated.jaxb.GroupQueryDetails, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    protected java.lang.String _BehaviorPolicy;

    protected com.sun.xml.bind.util.ListImpl _MemberDetails;

    protected java.lang.String _GroupName;

    protected java.lang.String _ConfigurationPolicy;

    protected java.lang.String _GroupIdentifier;

    public static final java.lang.Class version = (eu.more.gms.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.gms.generated.jaxb.GroupQueryDetails.class);
    }

    public java.lang.String getBehaviorPolicy() {
        return _BehaviorPolicy;
    }

    public void setBehaviorPolicy(java.lang.String value) {
        _BehaviorPolicy = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getMemberDetails() {
        if (_MemberDetails == null) {
            _MemberDetails = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _MemberDetails;
    }

    public java.util.List getMemberDetails() {
        return _getMemberDetails();
    }

    public java.lang.String getGroupName() {
        return _GroupName;
    }

    public void setGroupName(java.lang.String value) {
        _GroupName = value;
    }

    public java.lang.String getConfigurationPolicy() {
        return _ConfigurationPolicy;
    }

    public void setConfigurationPolicy(java.lang.String value) {
        _ConfigurationPolicy = value;
    }

    public java.lang.String getGroupIdentifier() {
        return _GroupIdentifier;
    }

    public void setGroupIdentifier(java.lang.String value) {
        _GroupIdentifier = value;
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_MemberDetails == null) ? 0 : _MemberDetails.size());
        while (idx2 != len2) {
            context.startElement("", "memberDetails");
            int idx_0 = idx2;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _MemberDetails.get(idx_0++)), "MemberDetails");
            context.endNamespaceDecls();
            int idx_1 = idx2;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _MemberDetails.get(idx_1++)), "MemberDetails");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _MemberDetails.get(idx2++)), "MemberDetails");
            context.endElement();
        }
    }

    public void serializeAttributes(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_MemberDetails == null) ? 0 : _MemberDetails.size());
        if (_BehaviorPolicy != null) {
            context.startAttribute("", "behaviorPolicy");
            try {
                context.text(((java.lang.String) _BehaviorPolicy), "BehaviorPolicy");
            } catch (java.lang.Exception e) {
                org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_ConfigurationPolicy != null) {
            context.startAttribute("", "configurationPolicy");
            try {
                context.text(((java.lang.String) _ConfigurationPolicy), "ConfigurationPolicy");
            } catch (java.lang.Exception e) {
                org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_GroupIdentifier != null) {
            context.startAttribute("", "groupIdentifier");
            try {
                context.text(((java.lang.String) _GroupIdentifier), "GroupIdentifier");
            } catch (java.lang.Exception e) {
                org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_GroupName != null) {
            context.startAttribute("", "groupName");
            try {
                context.text(((java.lang.String) _GroupName), "GroupName");
            } catch (java.lang.Exception e) {
                org.soda.jaxb.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public void serializeURIs(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_MemberDetails == null) ? 0 : _MemberDetails.size());
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (eu.more.gms.generated.jaxb.GroupQueryDetails.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsq ~  ppsq ~  ppsq ~  ppsr com.sun.msv." + "grammar.ChoiceExp         xq ~ ppsr  com.sun.msv.grammar." + "OneOrMoreExp         xr com.sun.msv.grammar.UnaryExp     " + "   L expq ~ xq ~ sr java.lang.BooleanÍ rÕúî Z va" + "luexp psr \'com.sun.msv.grammar.trex.ElementPattern        " + "L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.m" + "sv.grammar.ElementExp        Z ignoreUndeclaredAttribute" + "sL \fcontentModelq ~ xq ~ q ~ p sq ~  ppsq ~ pp sq ~ \tpps" + "q ~ q ~ psr  com.sun.msv.grammar.AttributeExp        L " + "expq ~ L \tnameClassq ~ xq ~ q ~ psr 2com.sun.msv.gramma" + "r.Expression$AnyStringExpression         xq ~ sq ~ q ~ " + "sr  com.sun.msv.grammar.AnyNameClass         xr com.sun." + "msv.grammar.NameClass         xpsr 0com.sun.msv.grammar.Ex" + "pression$EpsilonExpression         xq ~ q ~ q ~ !sr #com" + ".sun.msv.grammar.SimpleNameClass        L \tlocalNamet Lj" + "ava/lang/String;L \fnamespaceURIq ~ #xq ~ t (eu.more.gms.gen" + "erated.jaxb.MemberDetailst +http://java.sun.com/jaxb/xjc/dum" + "my-elementssq ~ \tppsq ~ q ~ psr com.sun.msv.grammar.DataE" + "xp        L dtt Lorg/relaxng/datatype/Datatype;L excep" + "tq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ ppsr \"com" + ".sun.msv.datatype.xsd.QnameType         xr *com.sun.msv.da" + "tatype.xsd.BuiltinAtomicType         xr %com.sun.msv.datat" + "ype.xsd.ConcreteType         xr \'com.sun.msv.datatype.xsd." + "XSDatatypeImpl        L \fnamespaceUriq ~ #L \btypeNameq ~ " + "#L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcess" + "or;xpt  http://www.w3.org/2001/XMLSchemat QNamesr 5com.sun." + "msv.datatype.xsd.WhiteSpaceProcessor$Collapse         xr ," + "com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xpsr " + "0com.sun.msv.grammar.Expression$NullSetExpression         " + "xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \tlocalNa" + "meq ~ #L \fnamespaceURIq ~ #xpq ~ 4q ~ 3sq ~ \"t typet )http:" + "//www.w3.org/2001/XMLSchema-instanceq ~ !sq ~ \"t \rmemberDeta" + "ilst  q ~ !sq ~ \tppsq ~ q ~ psq ~ )ppsr #com.sun.msv.datat" + "ype.xsd.StringType        Z \risAlwaysValidxq ~ .q ~ 3t s" + "tringsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preser" + "ve         xq ~ 6q ~ 9sq ~ :q ~ Gq ~ 3sq ~ \"t behaviorPo" + "licyq ~ Aq ~ !sq ~ \tppsq ~ q ~ pq ~ Dsq ~ \"t configuratio" + "nPolicyq ~ Aq ~ !sq ~ \tppsq ~ q ~ pq ~ Dsq ~ \"t groupIden" + "tifierq ~ Aq ~ !sq ~ \tppsq ~ q ~ pq ~ Dsq ~ \"t \tgroupNameq" + " ~ Aq ~ !sr \"com.sun.msv.grammar.ExpressionPool        L " + "\bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;x" + "psr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí " + "I countB \rstreamVersionL parentt $Lcom/sun/msv/grammar/Ex" + "pressionPool;xp   pq ~ q ~ \nq ~ Mq ~ q ~ q ~ q ~ q ~ " + "Uq ~ Bq ~ \bq ~ \rq ~ \'q ~ q ~ Qx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends org.soda.jaxb.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 9;
                            eatText1(v);
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case 13:
                        if (("queryCharacteristics" == ___local) && ("" == ___uri)) {
                            _getMemberDetails().add(((eu.more.gms.generated.jaxb.impl.MemberDetailsImpl) spawnChildFromEnterElement((eu.more.gms.generated.jaxb.impl.MemberDetailsImpl.class), 14, ___uri, ___local, ___qname, __atts)));
                            return;
                        }
                        break;
                    case 12:
                        if (("memberDetails" == ___local) && ("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return;
                        }
                        state = 15;
                        continue outer;
                    case 0:
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText2(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText3(v);
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case 9:
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 12;
                            eatText4(v);
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 15:
                        if (("memberDetails" == ___local) && ("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _GroupIdentifier = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _BehaviorPolicy = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _ConfigurationPolicy = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _GroupName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 9;
                            eatText1(v);
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case 12:
                        state = 15;
                        continue outer;
                    case 0:
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText2(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText3(v);
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case 9:
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 12;
                            eatText4(v);
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 15:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 14:
                        if (("memberDetails" == ___local) && ("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
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
                        if (("groupIdentifier" == ___local) && ("" == ___uri)) {
                            state = 7;
                            return;
                        }
                        state = 9;
                        continue outer;
                    case 12:
                        state = 15;
                        continue outer;
                    case 0:
                        if (("behaviorPolicy" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        if (("configurationPolicy" == ___local) && ("" == ___uri)) {
                            state = 4;
                            return;
                        }
                        state = 6;
                        continue outer;
                    case 9:
                        if (("groupName" == ___local) && ("" == ___uri)) {
                            state = 10;
                            return;
                        }
                        state = 12;
                        continue outer;
                    case 15:
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
                    case 6:
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 9;
                            eatText1(v);
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case 11:
                        if (("groupName" == ___local) && ("" == ___uri)) {
                            state = 12;
                            return;
                        }
                        break;
                    case 12:
                        state = 15;
                        continue outer;
                    case 5:
                        if (("configurationPolicy" == ___local) && ("" == ___uri)) {
                            state = 6;
                            return;
                        }
                        break;
                    case 0:
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText2(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 3:
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText3(v);
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case 8:
                        if (("groupIdentifier" == ___local) && ("" == ___uri)) {
                            state = 9;
                            return;
                        }
                        break;
                    case 9:
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 12;
                            eatText4(v);
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case 2:
                        if (("behaviorPolicy" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 15:
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
                            eatText2(value);
                            return;
                        case 6:
                            attIdx = context.getAttribute("", "groupIdentifier");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 9;
                                eatText1(v);
                                continue outer;
                            }
                            state = 9;
                            continue outer;
                        case 12:
                            state = 15;
                            continue outer;
                        case 4:
                            state = 5;
                            eatText3(value);
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "behaviorPolicy");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText2(v);
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case 3:
                            attIdx = context.getAttribute("", "configurationPolicy");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 6;
                                eatText3(v);
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                        case 7:
                            state = 8;
                            eatText1(value);
                            return;
                        case 9:
                            attIdx = context.getAttribute("", "groupName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 12;
                                eatText4(v);
                                continue outer;
                            }
                            state = 12;
                            continue outer;
                        case 10:
                            state = 11;
                            eatText4(value);
                            return;
                        case 15:
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
