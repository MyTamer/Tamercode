package freemind.controller.actions.generated.instance.impl;

public class RemoveLastIconXmlActionTypeImpl extends freemind.controller.actions.generated.instance.impl.NodeActionImpl implements freemind.controller.actions.generated.instance.RemoveLastIconXmlActionType, com.sun.xml.bind.JAXBObject, freemind.controller.actions.generated.instance.impl.runtime.UnmarshallableObject, freemind.controller.actions.generated.instance.impl.runtime.XMLSerializable, freemind.controller.actions.generated.instance.impl.runtime.ValidatableObject {

    public static final java.lang.Class version = (freemind.controller.actions.generated.instance.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (freemind.controller.actions.generated.instance.RemoveLastIconXmlActionType.class);
    }

    public freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingEventHandler createUnmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
        return new freemind.controller.actions.generated.instance.impl.RemoveLastIconXmlActionTypeImpl.Unmarshaller(context);
    }

    public void serializeElementBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeElementBody(context);
    }

    public void serializeAttributes(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributes(context);
    }

    public void serializeAttributeBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributeBody(context);
    }

    public void serializeURIs(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (freemind.controller.actions.generated.instance.RemoveLastIconXmlActionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr  com.sun.msv.grammar.AttributeExp        L expt  " + "Lcom/sun/msv/grammar/Expression;L \tnameClasst Lcom/sun/msv/" + "grammar/NameClass;xr com.sun.msv.grammar.ExpressionøèN5~O" + " I cachedHashCodeL epsilonReducibilityt Ljava/lang/Bool" + "ean;L expandedExpq ~ xpJ+ppsr com.sun.msv.grammar.DataE" + "xp        L dtt Lorg/relaxng/datatype/Datatype;L excep" + "tq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ dÞËppsr " + "#com.sun.msv.datatype.xsd.StringType        Z \risAlwaysVa" + "lidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType         " + "xr %com.sun.msv.datatype.xsd.ConcreteType         xr \'com." + "sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUr" + "it Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/s" + "un/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.o" + "rg/2001/XMLSchemat stringsr 5com.sun.msv.datatype.xsd.White" + "SpaceProcessor$Preserve         xr ,com.sun.msv.datatype.x" + "sd.WhiteSpaceProcessor         xpsr 0com.sun.msv.grammar." + "Expression$NullSetExpression         xq ~    \nppsr com.s" + "un.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespa" + "ceURIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleNameClas" + "s        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun" + ".msv.grammar.NameClass         xpt nodet  sr \"com.sun.msv" + ".grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv" + "/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar" + ".ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL " + "parentq ~  [ tablet ![Lcom/sun/msv/grammar/Expression;xp   " + "    9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "ppppppppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends freemind.controller.actions.generated.instance.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
            super(context, "--");
        }

        protected Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return freemind.controller.actions.generated.instance.impl.RemoveLastIconXmlActionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 1:
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
                    case 0:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 1:
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
                        if (("node" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((freemind.controller.actions.generated.instance.impl.NodeActionImpl) freemind.controller.actions.generated.instance.impl.RemoveLastIconXmlActionTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 1:
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
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 1:
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
                            attIdx = context.getAttribute("", "node");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                        case 1:
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
