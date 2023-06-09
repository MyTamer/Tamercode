package codec.gen.configuration.impl;

public class ParameterImpl implements codec.gen.configuration.Parameter, com.sun.xml.bind.JAXBObject, codec.gen.configuration.impl.runtime.UnmarshallableObject, codec.gen.configuration.impl.runtime.XMLSerializable, codec.gen.configuration.impl.runtime.ValidatableObject {

    protected java.lang.String _Name;

    protected com.sun.xml.bind.util.ListImpl _Content;

    protected java.lang.String _File;

    public static final java.lang.Class version = (codec.gen.configuration.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (codec.gen.configuration.Parameter.class);
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getContent() {
        if (_Content == null) {
            _Content = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Content;
    }

    public java.util.List getContent() {
        return _getContent();
    }

    public java.lang.String getFile() {
        return _File;
    }

    public void setFile(java.lang.String value) {
        _File = value;
    }

    public codec.gen.configuration.impl.runtime.UnmarshallingEventHandler createUnmarshaller(codec.gen.configuration.impl.runtime.UnmarshallingContext context) {
        return new codec.gen.configuration.impl.ParameterImpl.Unmarshaller(context);
    }

    public void serializeBody(codec.gen.configuration.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_Content == null) ? 0 : _Content.size());
        while (idx2 != len2) {
            try {
                context.text(((java.lang.String) _Content.get(idx2++)), "Content");
            } catch (java.lang.Exception e) {
                codec.gen.configuration.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public void serializeAttributes(codec.gen.configuration.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_Content == null) ? 0 : _Content.size());
        if (_File != null) {
            context.startAttribute("", "file");
            try {
                context.text(((java.lang.String) _File), "File");
            } catch (java.lang.Exception e) {
                codec.gen.configuration.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "name");
        try {
            context.text(((java.lang.String) _Name), "Name");
        } catch (java.lang.Exception e) {
            codec.gen.configuration.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        while (idx2 != len2) {
            try {
                idx2 += 1;
            } catch (java.lang.Exception e) {
                codec.gen.configuration.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public void serializeURIs(codec.gen.configuration.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx2 = 0;
        final int len2 = ((_Content == null) ? 0 : _Content.size());
        while (idx2 != len2) {
            try {
                idx2 += 1;
            } catch (java.lang.Exception e) {
                codec.gen.configuration.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (codec.gen.configuration.Parameter.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr com.sun.msv.grammar.SequenceExp         xr com.su" + "n.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/gra" + "mmar/Expression;L exp2q ~ xr com.sun.msv.grammar.Expressi" + "onøèN5~O L epsilonReducibilityt Ljava/lang/Boolean;L " + "expandedExpq ~ xpppsq ~  ppsr 2com.sun.msv.grammar.Expressi" + "on$AnyStringExpression         xq ~ sr java.lang.Boolean" + "Í rÕúî Z valuexppsr com.sun.msv.grammar.ChoiceExp    " + "     xq ~ ppsr  com.sun.msv.grammar.AttributeExp       " + " L expq ~ L \tnameClasst Lcom/sun/msv/grammar/NameClass;x" + "q ~ sq ~ \t psr com.sun.msv.grammar.DataExp        L dt" + "t Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lc" + "om/sun/msv/util/StringPair;xq ~ q ~ psr #com.sun.msv.datat" + "ype.xsd.StringType        Z \risAlwaysValidxr *com.sun.msv" + ".datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.da" + "tatype.xsd.ConcreteType         xr \'com.sun.msv.datatype.x" + "sd.XSDatatypeImpl        L \fnamespaceUrit Ljava/lang/Str" + "ing;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xs" + "d/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat" + " stringsr 5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Pre" + "serve         xr ,com.sun.msv.datatype.xsd.WhiteSpaceProce" + "ssor         xpsr 0com.sun.msv.grammar.Expression$NullSet" + "Expression         xq ~ ppsr com.sun.msv.util.StringPair" + "ÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ q ~ " + "sr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNa" + "meq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass" + "         xpt filet  sr 0com.sun.msv.grammar.Expression$Ep" + "silonExpression         xq ~ q ~ \npsq ~ \rppq ~ sq ~ %t " + "nameq ~ )sr \"com.sun.msv.grammar.ExpressionPool        L " + "\bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;x" + "psr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí " + "I countB \rstreamVersionL parentt $Lcom/sun/msv/grammar/Ex" + "pressionPool;xp   pq ~ q ~ q ~ \fx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends codec.gen.configuration.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(codec.gen.configuration.impl.runtime.UnmarshallingContext context) {
            super(context, "--------");
        }

        protected Unmarshaller(codec.gen.configuration.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return codec.gen.configuration.impl.ParameterImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        state = 7;
                        continue outer;
                    case 0:
                        attIdx = context.getAttribute("", "file");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 7:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 3:
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _File = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _Name = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 6:
                        state = 7;
                        continue outer;
                    case 0:
                        attIdx = context.getAttribute("", "file");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 7:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 3:
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
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
                    case 6:
                        state = 7;
                        continue outer;
                    case 0:
                        if (("file" == ___local) && ("" == ___uri)) {
                            state = 1;
                            return;
                        }
                        state = 3;
                        continue outer;
                    case 7:
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 3:
                        if (("name" == ___local) && ("" == ___uri)) {
                            state = 4;
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
                    case 2:
                        if (("file" == ___local) && ("" == ___uri)) {
                            state = 3;
                            return;
                        }
                        break;
                    case 6:
                        state = 7;
                        continue outer;
                    case 0:
                        attIdx = context.getAttribute("", "file");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case 7:
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return;
                    case 3:
                        attIdx = context.getAttribute("", "name");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case 5:
                        if (("name" == ___local) && ("" == ___uri)) {
                            state = 6;
                            return;
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
                        case 6:
                            eatText3(value);
                            state = 7;
                            return;
                        case 1:
                            eatText1(value);
                            state = 2;
                            return;
                        case 0:
                            attIdx = context.getAttribute("", "file");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case 7:
                            eatText3(value);
                            state = 7;
                            return;
                        case 3:
                            attIdx = context.getAttribute("", "name");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 6;
                                continue outer;
                            }
                            break;
                        case 4:
                            eatText2(value);
                            state = 5;
                            return;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText3(final java.lang.String value) throws org.xml.sax.SAXException {
            try {
                _getContent().add(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
