package eu.more.gms.generated.jaxb.impl;

public class AddQueryMemberToGroupImpl extends eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl implements eu.more.gms.generated.jaxb.AddQueryMemberToGroup, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, org.soda.jaxb.runtime.UnmarshallableObject, org.soda.jaxb.runtime.XMLSerializable, org.soda.jaxb.runtime.ValidatableObject {

    public static final java.lang.Class version = (eu.more.gms.generated.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (eu.more.gms.generated.jaxb.AddQueryMemberToGroup.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://www.ist-more.org/GroupManagement/";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "addQueryMemberToGroup";
    }

    public org.soda.jaxb.runtime.UnmarshallingEventHandler createUnmarshaller(org.soda.jaxb.runtime.UnmarshallingContext context) {
        return new eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.Unmarshaller(context);
    }

    public void serializeBody(org.soda.jaxb.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.ist-more.org/GroupManagement/", "addQueryMemberToGroup");
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
        return (eu.more.gms.generated.jaxb.AddQueryMemberToGroup.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~ ppsq ~ pps" + "q ~ ppsq ~ ppsr com.sun.msv.grammar.ChoiceExp         x" + "q ~ \bppsr  com.sun.msv.grammar.OneOrMoreExp         xr co" + "m.sun.msv.grammar.UnaryExp        L expq ~ xq ~ sr ja" + "va.lang.BooleanÍ rÕúî Z valuexp psq ~  q ~ p sq ~ pps" + "q ~  pp sq ~ ppsq ~ q ~ psr  com.sun.msv.grammar.Attribut" + "eExp        L expq ~ L \tnameClassq ~ xq ~ q ~ psr 2c" + "om.sun.msv.grammar.Expression$AnyStringExpression         " + "xq ~ sq ~ q ~ sr  com.sun.msv.grammar.AnyNameClass      " + "   xr com.sun.msv.grammar.NameClass         xpsr 0com.s" + "un.msv.grammar.Expression$EpsilonExpression         xq ~ " + "q ~ q ~ #sr #com.sun.msv.grammar.SimpleNameClass        " + "L \tlocalNamet Ljava/lang/String;L \fnamespaceURIq ~ %xq ~  t" + " (eu.more.gms.generated.jaxb.MemberDetailst +http://java.sun" + ".com/jaxb/xjc/dummy-elementssq ~ ppsq ~ q ~ psr com.sun." + "msv.grammar.DataExp        L dtt Lorg/relaxng/datatype/" + "Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPai" + "r;xq ~ ppsr \"com.sun.msv.datatype.xsd.QnameType         x" + "r *com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %" + "com.sun.msv.datatype.xsd.ConcreteType         xr \'com.sun." + "msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUriq ~" + " %L \btypeNameq ~ %L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/" + "WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat " + "QNamesr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collap" + "se         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcesso" + "r         xpsr 0com.sun.msv.grammar.Expression$NullSetExpr" + "ession         xq ~ ppsr com.sun.msv.util.StringPairÐtj" + "B  L \tlocalNameq ~ %L \fnamespaceURIq ~ %xpq ~ 6q ~ 5sq ~" + " $t typet )http://www.w3.org/2001/XMLSchema-instanceq ~ #sq" + " ~ $t \rmemberDetailst  q ~ #sq ~ ppsq ~ q ~ psq ~ +ppsr #" + "com.sun.msv.datatype.xsd.StringType        Z \risAlwaysVal" + "idxq ~ 0q ~ 5t stringsr 5com.sun.msv.datatype.xsd.WhiteSpac" + "eProcessor$Preserve         xq ~ 8q ~ ;sq ~ <q ~ Iq ~ 5sq" + " ~ $t behaviorPolicyq ~ Cq ~ #sq ~ ppsq ~ q ~ pq ~ Fsq ~" + " $t configurationPolicyq ~ Cq ~ #sq ~ ppsq ~ q ~ pq ~ Fs" + "q ~ $t groupIdentifierq ~ Cq ~ #sq ~ ppsq ~ q ~ pq ~ Fsq" + " ~ $t \tgroupNameq ~ Cq ~ #sq ~ ppsq ~ q ~ pq ~ .q ~ >q ~ " + "#sq ~ $t addQueryMemberToGroupt (http://www.ist-more.org/Gr" + "oupManagement/sr \"com.sun.msv.grammar.ExpressionPool       " + " L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedH" + "ash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNï" + "èí I countB \rstreamVersionL parentt $Lcom/sun/msv/gramm" + "ar/ExpressionPool;xp   pq ~ q ~ q ~ Oq ~ q ~ \fq ~ q ~ " + "q ~ Wq ~ Dq ~ \rq ~ q ~ )q ~ \nq ~ [q ~ Sq ~ \tx"));
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
            return eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("memberDetails" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        spawnHandlerFromEnterElement((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("addQueryMemberToGroup" == ___local) && ("http://www.ist-more.org/GroupManagement/" == ___uri)) {
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
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        spawnHandlerFromLeaveElement((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return;
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 2:
                        if (("addQueryMemberToGroup" == ___local) && ("http://www.ist-more.org/GroupManagement/" == ___uri)) {
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
                        if (("behaviorPolicy" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        if (("configurationPolicy" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        if (("groupIdentifier" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        if (("groupName" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return;
                        }
                        spawnHandlerFromEnterAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return;
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
                        attIdx = context.getAttribute("", "behaviorPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "configurationPolicy");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupIdentifier");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        attIdx = context.getAttribute("", "groupName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        spawnHandlerFromLeaveAttribute((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return;
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
                            attIdx = context.getAttribute("", "behaviorPolicy");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "configurationPolicy");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "groupIdentifier");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            attIdx = context.getAttribute("", "groupName");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            spawnHandlerFromText((((eu.more.gms.generated.jaxb.impl.GroupQueryDetailsImpl) eu.more.gms.generated.jaxb.impl.AddQueryMemberToGroupImpl.this).new Unmarshaller(context)), 2, value);
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
    }
}
