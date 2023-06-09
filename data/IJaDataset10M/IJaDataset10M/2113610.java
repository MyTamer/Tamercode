package org.jaffa.patterns.library.domain_creator_1_1.domain.impl;

public class CommentImpl implements org.jaffa.patterns.library.domain_creator_1_1.domain.Comment, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    protected java.lang.String _Style;

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr com.sun.msv.grammar.ChoiceExp         xr com.sun.msv.grammar.BinaryExp        L exp1t  Lcom/sun/msv/grammar/Expression;L exp2q ~ xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp½ppsr  com.sun.msv.grammar.AttributeExp        L expq ~ L \tnameClasst Lcom/sun/msv/grammar/NameClass;xq ~ ²sr java.lang.BooleanÍ rÕúî Z valuexp psr com.sun.msv.grammar.DataExp        L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ &ø7ppsr )com.sun.msv.datatype.xsd.EnumerationFacet        L valuest Ljava/util/Set;xr 9com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"§RoÊÇT  xr *com.sun.msv.datatype.xsd.DataTypeWithFacet        Z \fisFacetFixedZ needValueCheckFlagL \bbaseTypet )Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L \fconcreteTypet \'Lcom/sun/msv/datatype/xsd/ConcreteType;L \tfacetNamet Ljava/lang/String;xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl        L \fnamespaceUriq ~ L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  psr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1JMoIÛ¤G  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xp  sr #com.sun.msv.datatype.xsd.StringType        Z \risAlwaysValidxr *com.sun.msv.datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.datatype.xsd.ConcreteType         xq ~ t  http://www.w3.org/2001/XMLSchemat stringq ~ q ~  t enumerationsr java.util.HashSetºD¸·4  xpw\f   ?@     t fifot plaint lifoxsr 0com.sun.msv.grammar.Expression$NullSetExpression         xq ~    \nppsr com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpt string-derivedq ~ sr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass         xpt Styleq ~ sr 0com.sun.msv.grammar.Expression$EpsilonExpression         xq ~    \tsq ~ \tpsr \"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ 6[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppq ~ ppppppppppppppppppppppp");

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return org.jaffa.patterns.library.domain_creator_1_1.domain.Comment.class;
    }

    public java.lang.String getStyle() {
        if (_Style == null) {
            return "plain";
        } else {
            return _Style;
        }
    }

    public void setStyle(java.lang.String value) {
        _Style = value;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new org.jaffa.patterns.library.domain_creator_1_1.domain.impl.CommentImpl.Unmarshaller(context);
    }

    public java.lang.Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS();
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        if (_Style != null) {
            context.startAttribute("", "Style");
            try {
                context.text(((java.lang.String) _Style));
            } catch (java.lang.Exception e) {
                com.sun.xml.bind.marshaller.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.jaffa.patterns.library.domain_creator_1_1.domain.Comment.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "---");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return org.jaffa.patterns.library.domain_creator_1_1.domain.impl.CommentImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    if (("" == ___uri) && ("Style" == ___local)) {
                        state = 1;
                        return;
                    }
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 2:
                    if (("" == ___uri) && ("Style" == ___local)) {
                        goto0();
                        return;
                    }
                    break;
                case 0:
                    revertToParentFromLeaveAttribute(___uri, ___local);
                    return;
            }
            super.leaveAttribute(___uri, ___local);
        }

        public void text(java.lang.String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 0:
                        revertToParentFromText(value);
                        return;
                    case 1:
                        try {
                            _Style = value;
                        } catch (java.lang.Exception e) {
                            handleParseConversionException(e);
                        }
                        state = 2;
                        return;
                }
            } catch (java.lang.RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }

        private void goto0() throws com.sun.xml.bind.unmarshaller.UnreportedException {
            int idx;
            state = 0;
            idx = context.getAttribute("", "Style");
            if (idx >= 0) {
                context.consumeAttribute(idx);
                return;
            }
        }
    }
}
