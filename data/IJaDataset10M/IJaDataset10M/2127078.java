package onepoint.project.configuration.generated.impl;

public class OpConfigDatabasePasswordImpl implements onepoint.project.configuration.generated.OpConfigDatabasePassword, com.sun.xml.bind.JAXBObject, onepoint.project.configuration.generated.impl.runtime.UnmarshallableObject, onepoint.project.configuration.generated.impl.runtime.XMLSerializable, onepoint.project.configuration.generated.impl.runtime.ValidatableObject {

    protected java.lang.String _Value;

    protected boolean has_Encrypted;

    protected boolean _Encrypted;

    public static final java.lang.Class version = (onepoint.project.configuration.generated.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (onepoint.project.configuration.generated.OpConfigDatabasePassword.class);
    }

    public java.lang.String getValue() {
        return _Value;
    }

    public void setValue(java.lang.String value) {
        _Value = value;
    }

    public boolean isEncrypted() {
        if (!has_Encrypted) {
            return javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.DatatypeConverterImpl.installHook("true"));
        } else {
            return _Encrypted;
        }
    }

    public void setEncrypted(boolean value) {
        _Encrypted = value;
        has_Encrypted = true;
    }

    public onepoint.project.configuration.generated.impl.runtime.UnmarshallingEventHandler createUnmarshaller(onepoint.project.configuration.generated.impl.runtime.UnmarshallingContext context) {
        return new onepoint.project.configuration.generated.impl.OpConfigDatabasePasswordImpl.Unmarshaller(context);
    }

    public void serializeBody(onepoint.project.configuration.generated.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        try {
            context.text(((java.lang.String) _Value), "Value");
        } catch (java.lang.Exception e) {
            onepoint.project.configuration.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
    }

    public void serializeAttributes(onepoint.project.configuration.generated.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        if (has_Encrypted) {
            context.startAttribute("", "encrypted");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Encrypted)), "Encrypted");
            } catch (java.lang.Exception e) {
                onepoint.project.configuration.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
    }

    public void serializeURIs(onepoint.project.configuration.generated.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (onepoint.project.configuration.generated.OpConfigDatabasePassword.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsr com.sun.msv.grammar.DataExp       " + " L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L na" + "met Lcom/sun/msv/util/StringPair;xq ~ ppsr #com.sun.msv.da" + "tatype.xsd.StringType        Z \risAlwaysValidxr *com.sun." + "msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv" + ".datatype.xsd.ConcreteType         xr \'com.sun.msv.datatyp" + "e.xsd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/" + "String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype" + "/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSche" + "mat stringsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$" + "Preserve         xr ,com.sun.msv.datatype.xsd.WhiteSpacePr" + "ocessor         xpsr 0com.sun.msv.grammar.Expression$Null" + "SetExpression         xq ~ ppsr com.sun.msv.util.StringP" + "airÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q " + "~ sr com.sun.msv.grammar.ChoiceExp         xq ~ ppsr  c" + "om.sun.msv.grammar.AttributeExp        L expq ~ L \tname" + "Classt Lcom/sun/msv/grammar/NameClass;xq ~ sr java.lang.B" + "ooleanÍ rÕúî Z valuexp psq ~ ppsr $com.sun.msv.datatyp" + "e.xsd.BooleanType         xq ~ q ~ t booleansr 5com.sun" + ".msv.datatype.xsd.WhiteSpaceProcessor$Collapse         xq " + "~ q ~ sq ~ q ~ $q ~ sr #com.sun.msv.grammar.SimpleNameCl" + "ass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.s" + "un.msv.grammar.NameClass         xpt \tencryptedt  sr 0com." + "sun.msv.grammar.Expression$EpsilonExpression         xq ~ " + "sq ~ q ~ .sr \"com.sun.msv.grammar.ExpressionPool       " + " L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHa" + "sh;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïè" + "í I countB \rstreamVersionL parentt $Lcom/sun/msv/gramma" + "r/ExpressionPool;xp   pq ~ q ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends onepoint.project.configuration.generated.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(onepoint.project.configuration.generated.impl.runtime.UnmarshallingContext context) {
            super(context, "-----");
        }

        protected Unmarshaller(onepoint.project.configuration.generated.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return onepoint.project.configuration.generated.impl.OpConfigDatabasePasswordImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        attIdx = context.getAttribute("", "encrypted");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 4:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Encrypted = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Encrypted = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        attIdx = context.getAttribute("", "encrypted");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 4:
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
                        if (("encrypted" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        state = 3;
                        continue outer;
                    case 4:
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
                        attIdx = context.getAttribute("", "encrypted");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 2:
                        if (("encrypted" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 4:
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
                            attIdx = context.getAttribute("", "encrypted");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText1(v);
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case 4:
                            revertToParentFromText(value);
                            return;
                        case 1:
                            state = 2;
                            eatText1(value);
                            return;
                        case 3:
                            state = 4;
                            eatText2(value);
                            return;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText2(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Value = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
