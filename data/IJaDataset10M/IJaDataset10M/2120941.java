package org.jaffa.patterns.library.object_viewer_meta_2_1.domain.impl;

public class SourceChainImpl implements org.jaffa.patterns.library.object_viewer_meta_2_1.domain.SourceChain, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    protected com.sun.xml.bind.util.ListImpl _Source = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr  com.sun.msv.grammar.OneOrMoreExp         xr com.sun.msv.grammar.UnaryExp        L expt  Lcom/sun/msv/grammar/Expression;xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp¿Îppsr \'com.sun.msv.grammar.trex.ElementPattern        L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExp        Z ignoreUndeclaredAttributesL \fcontentModelq ~ xq ~ ¿Ëpp sq ~ ¿Àpp sr com.sun.msv.grammar.ChoiceExp         xr com.sun.msv.grammar.BinaryExp        L exp1q ~ L exp2q ~ xq ~ ¿µppsq ~  ¿ªsr java.lang.BooleanÍ rÕúî Z valuexp psr  com.sun.msv.grammar.AttributeExp        L expq ~ L \tnameClassq ~ xq ~ ¿§q ~ psr 2com.sun.msv.grammar.Expression$AnyStringExpression         xq ~    \bsq ~ q ~ sr  com.sun.msv.grammar.AnyNameClass         xr com.sun.msv.grammar.NameClass         xpsr 0com.sun.msv.grammar.Expression$EpsilonExpression         xq ~    \tq ~ q ~ sr #com.sun.msv.grammar.SimpleNameClass        L \tlocalNamet Ljava/lang/String;L \fnamespaceURIq ~ xq ~ t ?org.jaffa.patterns.library.object_viewer_meta_2_1.domain.Sourcet +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ t Sourcet  sr \"com.sun.msv.grammar.ExpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ $[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿ppppppppq ~ ppppppppppq ~ \rppppppppppppppppppppppppq ~ pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return org.jaffa.patterns.library.object_viewer_meta_2_1.domain.SourceChain.class;
    }

    public java.util.List getSource() {
        return _Source;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new org.jaffa.patterns.library.object_viewer_meta_2_1.domain.impl.SourceChainImpl.Unmarshaller(context);
    }

    public java.lang.Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS();
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = _Source.size();
        while (idx1 != len1) {
            if (_Source.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _Source.get(idx1++)));
            } else {
                context.startElement("", "Source");
                int idx_0 = idx1;
                context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _Source.get(idx_0++)));
                context.endAttributes();
                context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _Source.get(idx1++)));
                context.endElement();
            }
        }
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = _Source.size();
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        int idx1 = 0;
        final int len1 = _Source.size();
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.jaffa.patterns.library.object_viewer_meta_2_1.domain.SourceChain.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "----");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return org.jaffa.patterns.library.object_viewer_meta_2_1.domain.impl.SourceChainImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    if (("" == ___uri) && ("Source" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
                case 1:
                    if (("" == ___uri) && ("Object" == ___local)) {
                        _Source.add(((org.jaffa.patterns.library.object_viewer_meta_2_1.domain.impl.SourceImpl) spawnChildFromEnterElement((org.jaffa.patterns.library.object_viewer_meta_2_1.domain.impl.SourceImpl.class), 2, ___uri, ___local, __atts)));
                        return;
                    }
                    break;
                case 0:
                    if (("" == ___uri) && ("Source" == ___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    break;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
                case 2:
                    if (("" == ___uri) && ("Source" == ___local)) {
                        context.popAttributes();
                        state = 3;
                        return;
                    }
                    break;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 3:
                    revertToParentFromLeaveAttribute(___uri, ___local);
                    return;
            }
            super.leaveAttribute(___uri, ___local);
        }

        public void text(java.lang.String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 3:
                        revertToParentFromText(value);
                        return;
                }
            } catch (java.lang.RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }

        public void leaveChild(int nextState) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(nextState) {
                case 2:
                    state = 2;
                    return;
            }
            super.leaveChild(nextState);
        }
    }
}
