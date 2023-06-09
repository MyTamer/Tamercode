package phex.xml.impl;

public class XJBIPAccessRuleImpl extends phex.xml.impl.XJBSecurityRuleImpl implements phex.xml.XJBIPAccessRule, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    private static final Class PRIMARY_INTERFACE_CLASS = phex.xml.XJBIPAccessRule.class;

    protected byte[] _CompareIP;

    protected byte[] _Ip;

    protected boolean has_AddressType;

    protected byte _AddressType;

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr com.sun.msv.grammar.SequenceExpUeñpó­  xr com.sun.msv.grammar.BinaryExpi<Á_· L exp1t  Lcom/sun/msv/grammar/Expression;L exp2q ~ xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp\n\f:ppsq ~  \b8ð}ppsq ~  e¦pppsq ~  Nppsq ~  `reppsq ~  àñØppsq ~  ò×ppsq ~  cµppsq ~  @ppsr com.sun.msv.grammar.ChoiceExpÆ|ì3¸ùô  xq ~  (qppsr \'com.sun.msv.grammar.trex.ElementPatternsuÂ L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExpÓÆb\rC Z ignoreUndeclaredAttributesL \fcontentModelq ~ xq ~  (fsr java.lang.BooleanÍ rÕúî Z valuexp p sr com.sun.msv.grammar.DataExp8õõ>{j!Ë L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~  ([ppsr #com.sun.msv.datatype.xsd.StringTypeÀ\t©yöæ  xr *com.sun.msv.datatype.xsd.BuiltinAtomicTypeÿ10¨bRÊ  xr %com.sun.msv.datatype.xsd.ConcreteType7­sa|×Z  xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl4éH.2z L \fnamespaceUrit Ljava/lang/String;L \btypeNameq ~ L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat stringsr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1JMoIÛ¤G  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessoripÿ0CÎN  xpsr 0com.sun.msv.grammar.Expression$NullSetExpression sÏ@  xq ~    \nq ~ psr com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ L \fnamespaceURIq ~ xpq ~ \"q ~ !sr #com.sun.msv.grammar.SimpleNameClassé÷«ªõ L \tlocalNameq ~ L \fnamespaceURIq ~ xr com.sun.msv.grammar.NameClass}ÔkBÇ\tk  xpt descriptiont  sr 0com.sun.msv.grammar.Expression$EpsilonExpressionvãZþxî  xq ~    \tsq ~ psq ~  î#ppsq ~  î#q ~ p sq ~  î#ppsr $com.sun.msv.datatype.xsd.BooleanTypeÈ}  xq ~ q ~ !t booleansr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$2z9îø,N  xq ~ $q ~ \'sq ~ (q ~ 7q ~ !sq ~ *t \risDenyingRuleq ~ .q ~ 0sq ~  î#ppsq ~  î#q ~ p q ~ 4sq ~ *t \nisDisabledq ~ .q ~ 0sq ~  î#ppsq ~  î#q ~ p q ~ 4sq ~ *t \fisSystemRuleq ~ .q ~ 0sq ~  îjüppsq ~  îjñq ~ p sq ~  îjæppsr  com.sun.msv.datatype.xsd.IntType¾l¶°ä  xr +com.sun.msv.datatype.xsd.IntegerDerivedTypeñ]&6k¾  xq ~ q ~ !t intq ~ 9q ~ \'sq ~ (q ~ Kq ~ !sq ~ *t \ftriggerCountq ~ .q ~ 0sq ~  ppsq ~  }q ~ p sq ~  rppsr !com.sun.msv.datatype.xsd.LongTypeèAü>J¡Á  xq ~ Iq ~ !t longq ~ 9q ~ \'sq ~ (q ~ Tq ~ !sq ~ *t \nexpiryDateq ~ .q ~ 0sq ~  î#ppsq ~  î#q ~ p q ~ 4sq ~ *t isDeletedOnExpiryq ~ .q ~ 0sq ~ äppsq ~ Ùq ~ p sq ~ Îppsr !com.sun.msv.datatype.xsd.ByteType¨n4XRÏHÕ  xq ~ Iq ~ !t byteq ~ 9q ~ \'sq ~ (q ~ aq ~ !sq ~ *t addressTypeq ~ .q ~ 0sq ~ ÓJ\bppsq ~ ÓIýq ~ p sq ~ ÓIòppsr &com.sun.msv.datatype.xsd.HexBinaryType«pPyl  xr \'com.sun.msv.datatype.xsd.BinaryBaseType§Î^¯W  xq ~ q ~ !t \thexBinaryq ~ 9q ~ \'sq ~ (q ~ kq ~ !sq ~ *t ipq ~ .q ~ 0sq ~ ÓJ\bppsq ~ ÓIýq ~ p q ~ gsq ~ *t \tcompareIPq ~ .q ~ 0sr \"com.sun.msv.grammar.ExpressionPoolåóJ;Í]^ø L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ t[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿pppppq ~ \rppppq ~ Epppppppppppppppppppppppq ~ ppq ~ \tpq ~ pppppq ~ \\pppppppq ~ ppppppppppppppppppppppppppppppppppq ~ eq ~ oppppppppppq ~ pppppppq ~ Oppppppq ~ \nppppppppppppppppq ~ pppppq ~ 2q ~ =q ~ Aq ~ Xppppppq ~ \fpppppppppppppppppppppppppppppppq ~ \bpppppppppp");

    public byte[] getCompareIP() {
        return _CompareIP;
    }

    public void setCompareIP(byte[] value) {
        _CompareIP = value;
    }

    public byte[] getIp() {
        return _Ip;
    }

    public void setIp(byte[] value) {
        _Ip = value;
    }

    public byte getAddressType() {
        return _AddressType;
    }

    public void setAddressType(byte value) {
        _AddressType = value;
        has_AddressType = true;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new phex.xml.impl.XJBIPAccessRuleImpl.Unmarshaller(context);
    }

    public Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS;
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeElements(context);
        if (has_AddressType) {
            context.startElement("", "addressType");
            context.endAttributes();
            context.text(com.sun.msv.datatype.xsd.ByteType.save(((Byte) new java.lang.Byte(_AddressType))));
            context.endElement();
        }
        if (_Ip != null) {
            context.startElement("", "ip");
            context.endAttributes();
            context.text(com.sun.msv.datatype.xsd.HexBinaryType.save(((byte[]) _Ip)));
            context.endElement();
        }
        if (_CompareIP != null) {
            context.startElement("", "compareIP");
            context.endAttributes();
            context.text(com.sun.msv.datatype.xsd.HexBinaryType.save(((byte[]) _CompareIP)));
            context.endElement();
        }
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributes(context);
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        super.serializeAttributeBodies(context);
    }

    public Class getPrimaryInterface() {
        return (phex.xml.XJBIPAccessRule.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "--------");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return phex.xml.impl.XJBIPAccessRuleImpl.this;
        }

        public void enterElement(String ___uri, String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    if ("".equals(___uri) && "isSystemRule".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "addressType".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "isDisabled".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "triggerCount".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "compareIP".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "expiryDate".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "isDeletedOnExpiry".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "ip".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "description".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    if ("".equals(___uri) && "isDenyingRule".equals(___local)) {
                        spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                        return;
                    }
                    spawnSuperClassFromEnterElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local, __atts);
                    return;
                case 1:
                    if ("".equals(___uri) && "addressType".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 6;
                        return;
                    }
                    if ("".equals(___uri) && "compareIP".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 4;
                        return;
                    }
                    if ("".equals(___uri) && "ip".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 2;
                        return;
                    }
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 5:
                    if ("".equals(___uri) && "compareIP".equals(___local)) {
                        context.popAttributes();
                        state = 1;
                        return;
                    }
                    break;
                case 0:
                    spawnSuperClassFromLeaveElement((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local);
                    return;
                case 1:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
                case 3:
                    if ("".equals(___uri) && "ip".equals(___local)) {
                        context.popAttributes();
                        state = 1;
                        return;
                    }
                    break;
                case 7:
                    if ("".equals(___uri) && "addressType".equals(___local)) {
                        context.popAttributes();
                        state = 1;
                        return;
                    }
                    break;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    spawnSuperClassFromEnterAttribute((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local);
                    return;
                case 1:
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    spawnSuperClassFromLeaveAttribute((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, ___uri, ___local);
                    return;
                case 1:
                    revertToParentFromLeaveAttribute(___uri, ___local);
                    return;
            }
            super.leaveAttribute(___uri, ___local);
        }

        public void text(String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 0:
                        spawnSuperClassFromText((new phex.xml.impl.XJBSecurityRuleImpl.Unmarshaller(context)), 1, value);
                        return;
                    case 1:
                        revertToParentFromText(value);
                        return;
                    case 2:
                        _Ip = com.sun.msv.datatype.xsd.HexBinaryType.load(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                        state = 3;
                        return;
                    case 6:
                        _AddressType = com.sun.msv.datatype.xsd.ByteType.load(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value)).byteValue();
                        has_AddressType = true;
                        state = 7;
                        return;
                    case 4:
                        _CompareIP = com.sun.msv.datatype.xsd.HexBinaryType.load(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                        state = 5;
                        return;
                }
            } catch (RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }

        public void leaveChild(int nextState) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(nextState) {
                case 1:
                    state = 1;
                    return;
            }
            super.leaveChild(nextState);
        }
    }
}
