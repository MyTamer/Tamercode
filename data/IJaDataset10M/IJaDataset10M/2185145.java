package de.beas.explicanto.jaxb.impl;

public class QuizComponentImpl implements de.beas.explicanto.jaxb.QuizComponent, java.io.Serializable, com.sun.xml.bind.JAXBObject, de.beas.explicanto.jaxb.impl.runtime.UnmarshallableObject, de.beas.explicanto.jaxb.impl.runtime.XMLSerializable, de.beas.explicanto.jaxb.impl.runtime.ValidatableObject {

    private static final long serialVersionUID = 12343L;

    protected java.lang.String _QuizDescription;

    public static final java.lang.Class version = (de.beas.explicanto.jaxb.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (de.beas.explicanto.jaxb.QuizComponent.class);
    }

    public java.lang.String getQuizDescription() {
        return _QuizDescription;
    }

    public void setQuizDescription(java.lang.String value) {
        _QuizDescription = value;
    }

    public de.beas.explicanto.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(de.beas.explicanto.jaxb.impl.runtime.UnmarshallingContext context) {
        return new de.beas.explicanto.jaxb.impl.QuizComponentImpl.Unmarshaller(context);
    }

    public void serializeBody(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://www.bea-services.de/explicanto", "_quizDescription");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _QuizDescription), "QuizDescription");
        } catch (java.lang.Exception e) {
            de.beas.explicanto.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(de.beas.explicanto.jaxb.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (de.beas.explicanto.jaxb.QuizComponent.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsr com.sun.msv.g" + "rammar.DataExp        L dtt Lorg/relaxng/datatype/Datat" + "ype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq " + "~ ppsr #com.sun.msv.datatype.xsd.StringType        Z \ris" + "AlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType   " + "      xr %com.sun.msv.datatype.xsd.ConcreteType         " + "xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnam" + "espaceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet" + " .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://" + "www.w3.org/2001/XMLSchemat stringsr 5com.sun.msv.datatype.x" + "sd.WhiteSpaceProcessor$Preserve         xr ,com.sun.msv.da" + "tatype.xsd.WhiteSpaceProcessor         xpsr 0com.sun.msv." + "grammar.Expression$NullSetExpression         xq ~ ppsr c" + "om.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnam" + "espaceURIq ~ xpq ~ q ~ sr com.sun.msv.grammar.ChoiceExp " + "        xq ~ \bppsr  com.sun.msv.grammar.AttributeExp      " + "  L expq ~ L \tnameClassq ~ xq ~ sr java.lang.Boolean" + "Í rÕúî Z valuexp psq ~ \nppsr \"com.sun.msv.datatype.xsd." + "QnameType         xq ~ q ~ t QNamesr 5com.sun.msv.datat" + "ype.xsd.WhiteSpaceProcessor$Collapse         xq ~ q ~ sq" + " ~ q ~ \'q ~ sr #com.sun.msv.grammar.SimpleNameClass       " + " L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.gra" + "mmar.NameClass         xpt typet )http://www.w3.org/2001/" + "XMLSchema-instancesr 0com.sun.msv.grammar.Expression$Epsilon" + "Expression         xq ~ sq ~ \"q ~ 1sq ~ +t _quizDescrip" + "tiont %http://www.bea-services.de/explicantosr \"com.sun.msv." + "grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/" + "grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar." + "ExpressionPool$ClosedHash×jÐNïèí I countB \rstreamVersion" + "L parentt $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ " + "q ~ \tx"));
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
            return de.beas.explicanto.jaxb.impl.QuizComponentImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 0:
                        if (("_quizDescription" == ___local) && ("http://www.bea-services.de/explicanto" == ___uri)) {
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
                    case 2:
                        if (("_quizDescription" == ___local) && ("http://www.bea-services.de/explicanto" == ___uri)) {
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
                        case 1:
                            eatText1(value);
                            state = 2;
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
                _QuizDescription = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }
    }
}
