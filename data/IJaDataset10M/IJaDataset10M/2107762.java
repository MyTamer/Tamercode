package freemind.controller.actions.generated.instance.impl;

public class ItalicNodeActionTypeImpl extends freemind.controller.actions.generated.instance.impl.FormatNodeActionImpl implements freemind.controller.actions.generated.instance.ItalicNodeActionType, com.sun.xml.bind.JAXBObject, freemind.controller.actions.generated.instance.impl.runtime.UnmarshallableObject, freemind.controller.actions.generated.instance.impl.runtime.XMLSerializable, freemind.controller.actions.generated.instance.impl.runtime.ValidatableObject {

    protected boolean has_Italic;

    protected boolean _Italic;

    public static final java.lang.Class version = (freemind.controller.actions.generated.instance.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (freemind.controller.actions.generated.instance.ItalicNodeActionType.class);
    }

    public boolean isItalic() {
        return _Italic;
    }

    public void setItalic(boolean value) {
        _Italic = value;
        has_Italic = true;
    }

    public freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingEventHandler createUnmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
        return new freemind.controller.actions.generated.instance.impl.ItalicNodeActionTypeImpl.Unmarshaller(context);
    }

    public void serializeElementBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeElementBody(context);
    }

    public void serializeAttributes(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startAttribute("", "italic");
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Italic)));
        } catch (java.lang.Exception e) {
            freemind.controller.actions.generated.instance.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        super.serializeAttributes(context);
    }

    public void serializeAttributeBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributeBody(context);
    }

    public void serializeURIs(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (freemind.controller.actions.generated.instance.ItalicNodeActionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava" + "/lang/Boolean;L expandedExpq ~ xpúºppsr  com.sun.msv.gra" + "mmar.AttributeExp        L expq ~ L \tnameClasst Lcom/s" + "un/msv/grammar/NameClass;xq ~ J+ppsr com.sun.msv.grammar" + ".DataExp        L dtt Lorg/relaxng/datatype/Datatype;L " + "exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ dÞ" + "Ëppsr #com.sun.msv.datatype.xsd.StringType        Z \risAl" + "waysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType     " + "    xr %com.sun.msv.datatype.xsd.ConcreteType         xr" + " \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnames" + "paceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet ." + "Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://ww" + "w.w3.org/2001/XMLSchemat stringsr 5com.sun.msv.datatype.xsd" + ".WhiteSpaceProcessor$Preserve         xr ,com.sun.msv.data" + "type.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv.gr" + "ammar.Expression$NullSetExpression         xq ~    \nppsr " + "com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fn" + "amespaceURIq ~ xpq ~ q ~ sr #com.sun.msv.grammar.SimpleNa" + "meClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr c" + "om.sun.msv.grammar.NameClass         xpt nodet  sq ~ ¯û" + "ppsq ~ \t Qï^ppsr $com.sun.msv.datatype.xsd.BooleanType     " + "    xq ~ q ~ t booleansr 5com.sun.msv.datatype.xsd.Whit" + "eSpaceProcessor$Collapse         xq ~ q ~ sq ~ q ~ &q ~" + " sq ~ t italicq ~ !sr \"com.sun.msv.grammar.ExpressionPool" + "        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool" + "$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedH" + "ash×jÐNïèí I countI \tthresholdL parentq ~ -[ tablet ![" + "Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv" + ".grammar.Expression;Ö8DÃ]­§\n  xp   ¿ppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "ppppppppppppppppppppppppppppppppppppppppppppq ~ ppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends freemind.controller.actions.generated.instance.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
            super(context, "-----");
        }

        protected Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return freemind.controller.actions.generated.instance.impl.ItalicNodeActionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return;
                        }
                        break;
                    case 4:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "italic");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText0(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Italic = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Italic = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 4:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "italic");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
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
                        if (("node" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((freemind.controller.actions.generated.instance.impl.FormatNodeActionImpl) freemind.controller.actions.generated.instance.impl.ItalicNodeActionTypeImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 4:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 0:
                        if (("italic" == ___local) && ("" == ___uri)) {
                            state = 1;
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
                        attIdx = context.getAttribute("", "node");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return;
                        }
                        break;
                    case 2:
                        if (("italic" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 4:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 0:
                        attIdx = context.getAttribute("", "italic");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText0(v);
                            state = 3;
                            continue outer;
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
                            attIdx = context.getAttribute("", "node");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return;
                            }
                            break;
                        case 4:
                            revertToParentFromText(value);
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "italic");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText0(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case 1:
                            eatText0(value);
                            state = 2;
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
