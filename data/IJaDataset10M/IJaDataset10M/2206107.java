package nz.ac.massey.xmldad.bookquery.impl;

public class BookSubmissionTypeImpl implements nz.ac.massey.xmldad.bookquery.BookSubmissionType, com.sun.xml.bind.JAXBObject, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallableObject, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializable, nz.ac.massey.xmldad.bookcatalogue.impl.runtime.ValidatableObject {

    protected nz.ac.massey.xmldad.bookcatalogue.Book _Book;

    public static final java.lang.Class version = (nz.ac.massey.xmldad.bookquery.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (nz.ac.massey.xmldad.bookquery.BookSubmissionType.class);
    }

    public nz.ac.massey.xmldad.bookcatalogue.Book getBook() {
        return _Book;
    }

    public void setBook(nz.ac.massey.xmldad.bookcatalogue.Book value) {
        _Book = value;
    }

    public nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingEventHandler createUnmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context) {
        return new nz.ac.massey.xmldad.bookquery.impl.BookSubmissionTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("http://xmldad.massey.ac.nz/bookQuery", "book");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Book), "Book");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Book), "Book");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Book), "Book");
        context.endElement();
    }

    public void serializeAttributes(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeURIs(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (nz.ac.massey.xmldad.bookquery.BookSubmissionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O L epsilonReducibilityt Lj" + "ava/lang/Boolean;L expandedExpq ~ xppp sr com.sun.msv.gra" + "mmar.SequenceExp         xr com.sun.msv.grammar.BinaryExp" + "        L exp1q ~ L exp2q ~ xq ~ ppsq ~  pp sr com." + "sun.msv.grammar.ChoiceExp         xq ~ \bppsr  com.sun.msv." + "grammar.OneOrMoreExp         xr com.sun.msv.grammar.Unary" + "Exp        L expq ~ xq ~ sr java.lang.BooleanÍ rÕúî" + " Z valuexp psr  com.sun.msv.grammar.AttributeExp       " + " L expq ~ L \tnameClassq ~ xq ~ q ~ psr 2com.sun.msv.gr" + "ammar.Expression$AnyStringExpression         xq ~ sq ~ " + "q ~ sr  com.sun.msv.grammar.AnyNameClass         xr com." + "sun.msv.grammar.NameClass         xpsr 0com.sun.msv.gramma" + "r.Expression$EpsilonExpression         xq ~ q ~ q ~ sr " + "#com.sun.msv.grammar.SimpleNameClass        L \tlocalNamet" + " Ljava/lang/String;L \fnamespaceURIq ~ xq ~ t &nz.ac.masse" + "y.xmldad.bookcatalogue.Bookt +http://java.sun.com/jaxb/xjc/d" + "ummy-elementssq ~ ppsq ~ q ~ psr com.sun.msv.grammar.Dat" + "aExp        L dtt Lorg/relaxng/datatype/Datatype;L exc" + "eptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~ ppsr \"c" + "om.sun.msv.datatype.xsd.QnameType         xr *com.sun.msv." + "datatype.xsd.BuiltinAtomicType         xr %com.sun.msv.dat" + "atype.xsd.ConcreteType         xr \'com.sun.msv.datatype.xs" + "d.XSDatatypeImpl        L \fnamespaceUriq ~ L \btypeNameq " + "~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProce" + "ssor;xpt  http://www.w3.org/2001/XMLSchemat QNamesr 5com.su" + "n.msv.datatype.xsd.WhiteSpaceProcessor$Collapse         xr" + " ,com.sun.msv.datatype.xsd.WhiteSpaceProcessor         xps" + "r 0com.sun.msv.grammar.Expression$NullSetExpression       " + "  xq ~ ppsr com.sun.msv.util.StringPairÐtjB  L \tlocal" + "Nameq ~ L \fnamespaceURIq ~ xpq ~ .q ~ -sq ~ t typet )htt" + "p://www.w3.org/2001/XMLSchema-instanceq ~ sq ~ t bookt $h" + "ttp://xmldad.massey.ac.nz/bookQuerysr \"com.sun.msv.grammar.E" + "xpressionPool        L \bexpTablet /Lcom/sun/msv/grammar/E" + "xpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.Expressio" + "nPool$ClosedHash×jÐNïèí I countB \rstreamVersionL parent" + "t $Lcom/sun/msv/grammar/ExpressionPool;xp   pq ~ \fq ~ \tq ~" + " !q ~ x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends nz.ac.massey.xmldad.bookcatalogue.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(nz.ac.massey.xmldad.bookcatalogue.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return nz.ac.massey.xmldad.bookquery.impl.BookSubmissionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 1:
                        if (("title" == ___local) && ("http://xmldad.massey.ac.nz/bookCatalogue" == ___uri)) {
                            _Book = ((nz.ac.massey.xmldad.bookcatalogue.impl.BookImpl) spawnChildFromEnterElement((nz.ac.massey.xmldad.bookcatalogue.impl.BookImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return;
                        }
                        break;
                    case 0:
                        if (("book" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
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
                    case 2:
                        if (("book" == ___local) && ("http://xmldad.massey.ac.nz/bookQuery" == ___uri)) {
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
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }
    }
}
