package phex.xml.impl;

public class XJBPhexImpl implements phex.xml.XJBPhex, com.sun.xml.bind.unmarshaller.UnmarshallableObject, com.sun.xml.bind.serializer.XMLSerializable, com.sun.xml.bind.validator.ValidatableObject {

    private static final Class PRIMARY_INTERFACE_CLASS = phex.xml.XJBPhex.class;

    protected String _OldPhexVersion;

    protected phex.xml.XJBSWDownloadList _SWDownloadList;

    protected phex.xml.XJBSecurity _Security;

    protected String _PhexVersion;

    protected phex.xml.XJBSearchFilters _SearchFilters;

    protected phex.xml.XJBOldDownloadList _DownloadList;

    protected phex.xml.XJBSharedLibrary _SharedLibrary;

    protected phex.xml.XJBGUISettings _GuiSettings;

    private static final com.sun.msv.grammar.Grammar schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize("¬í sr com.sun.msv.grammar.SequenceExpUeñpó­  xr com.sun.msv.grammar.BinaryExpi<Á_· L exp1t  Lcom/sun/msv/grammar/Expression;L exp2q ~ xr com.sun.msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilonReducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp¶uppsq ~  2ßOppsq ~  iªppsq ~  ÞL4ppsq ~  ±Öppsq ~  `êppsq ~  XëEppsr com.sun.msv.grammar.ChoiceExpÆ|ì3¸ùô  xq ~ ,u ppsr \'com.sun.msv.grammar.trex.ElementPatternsuÂ L \tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv.grammar.ElementExpÓÆb\rC Z ignoreUndeclaredAttributesL \fcontentModelq ~ xq ~ ,usr java.lang.BooleanÍ rÕúî Z valuexp p sq ~ ,upp sq ~ \f,uppsr  com.sun.msv.grammar.OneOrMoreExpÌ;^»EÌÛ  xr com.sun.msv.grammar.UnaryExp\'»he^_5 L expq ~ xq ~ ,utq ~ psr  com.sun.msv.grammar.AttributeExp0\rR\nÇL\n L expq ~ L \tnameClassq ~ xq ~ ,uqq ~ psr 2com.sun.msv.grammar.Expression$AnyStringExpressionË²óÉ ¼  xq ~    \bsq ~ q ~ sr  com.sun.msv.grammar.AnyNameClassÃp¯ït½  xr com.sun.msv.grammar.NameClass}ÔkBÇ\tk  xpsr 0com.sun.msv.grammar.Expression$EpsilonExpressionvãZþxî  xq ~    \tq ~ psr #com.sun.msv.grammar.SimpleNameClassé÷«ªõ L \tlocalNamet Ljava/lang/String;L \fnamespaceURIq ~ $xq ~ t phex.xml.XJBSearchFilterst +http://java.sun.com/jaxb/xjc/dummy-elementssq ~ #t search-filterst  q ~ \"sq ~ \f,u ppsq ~ ,uq ~ p sq ~ ,upp sq ~ \f,uppsq ~ ,utq ~ psq ~ ,uqq ~ pq ~ q ~  q ~ \"sq ~ #t phex.xml.XJBGUISettingsq ~ \'sq ~ #t \fgui-settingsq ~ *q ~ \"sq ~ \f,u ppsq ~ ,uq ~ p sq ~ ,upp sq ~ \f,uppsq ~ ,utq ~ psq ~ ,uqq ~ pq ~ q ~  q ~ \"sq ~ #t phex.xml.XJBSharedLibraryq ~ \'sq ~ #t \rsharedLibraryq ~ *q ~ \"sq ~ \f,u ppsq ~ ,uq ~ p sq ~ ,upp sq ~ \f,uppsq ~ ,utq ~ psq ~ ,uqq ~ pq ~ q ~  q ~ \"sq ~ #t phex.xml.XJBOldDownloadListq ~ \'sq ~ #t \fdownloadlistq ~ *q ~ \"sq ~ \f,u ppsq ~ ,uq ~ p sq ~ ,upp sq ~ \f,uppsq ~ ,utq ~ psq ~ ,uqq ~ pq ~ q ~  q ~ \"sq ~ #t phex.xml.XJBSWDownloadListq ~ \'sq ~ #t swDownloadListq ~ *q ~ \"sq ~ \f (qppsq ~  (fq ~ p sr com.sun.msv.grammar.DataExp8õõ>{j!Ë L dtt Lorg/relaxng/datatype/Datatype;L exceptq ~ L namet Lcom/sun/msv/util/StringPair;xq ~  ([ppsr #com.sun.msv.datatype.xsd.StringTypeÀ\t©yöæ  xr *com.sun.msv.datatype.xsd.BuiltinAtomicTypeÿ10¨bRÊ  xr %com.sun.msv.datatype.xsd.ConcreteType7­sa|×Z  xr \'com.sun.msv.datatype.xsd.XSDatatypeImpl4éH.2z L \fnamespaceUriq ~ $L \btypeNameq ~ $L \nwhiteSpacet .Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt  http://www.w3.org/2001/XMLSchemat stringsr .com.sun.msv.datatype.xsd.WhiteSpaceProcessor$1JMoIÛ¤G  xr ,com.sun.msv.datatype.xsd.WhiteSpaceProcessoripÿ0CÎN  xpsr 0com.sun.msv.grammar.Expression$NullSetExpression sÏ@  xq ~    \nq ~ psr com.sun.msv.util.StringPairÐtjB  L \tlocalNameq ~ $L \fnamespaceURIq ~ $xpq ~ `q ~ _sq ~ #t \fphex-versionq ~ *q ~ \"sq ~ \f,u ppsq ~ ,uq ~ p sq ~ ,upp sq ~ \f,uppsq ~ ,utq ~ psq ~ ,uqq ~ pq ~ q ~  q ~ \"sq ~ #t phex.xml.XJBSecurityq ~ \'sq ~ #t \bsecurityq ~ *q ~ \"sq ~ \f 6ppsq ~  +q ~ pq ~ Xsq ~ #t \fphex-versionq ~ *q ~ \"sr \"com.sun.msv.grammar.ExpressionPoolåóJ;Í]^ø L \bexpTablet /Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPool$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~ y[ tablet ![Lcom/sun/msv/grammar/Expression;xp      9pur ![Lcom.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp   ¿pq ~ \bppppppppppppppppppq ~ tpq ~ pppppppppppppppq ~ \tppppppppq ~ pppppq ~ Spppppq ~ pppppppppppppppq ~ \npppppppppppppppppppppppppppppq ~ q ~ /q ~ 9q ~ Cq ~ Mq ~ npq ~ pppq ~ q ~ .q ~ 8q ~ Bq ~ Lq ~ mpppppppppppppppppppppppppppq ~ \rq ~ +q ~ 5q ~ ?q ~ Iq ~ jpppppppppppppppppppppppppppppppppppp");

    public String getOldPhexVersion() {
        return _OldPhexVersion;
    }

    public void setOldPhexVersion(String value) {
        _OldPhexVersion = value;
    }

    public phex.xml.XJBSWDownloadList getSWDownloadList() {
        return _SWDownloadList;
    }

    public void setSWDownloadList(phex.xml.XJBSWDownloadList value) {
        _SWDownloadList = value;
    }

    public phex.xml.XJBSecurity getSecurity() {
        return _Security;
    }

    public void setSecurity(phex.xml.XJBSecurity value) {
        _Security = value;
    }

    public String getPhexVersion() {
        return _PhexVersion;
    }

    public void setPhexVersion(String value) {
        _PhexVersion = value;
    }

    public phex.xml.XJBSearchFilters getSearchFilters() {
        return _SearchFilters;
    }

    public void setSearchFilters(phex.xml.XJBSearchFilters value) {
        _SearchFilters = value;
    }

    public phex.xml.XJBOldDownloadList getDownloadList() {
        return _DownloadList;
    }

    public void setDownloadList(phex.xml.XJBOldDownloadList value) {
        _DownloadList = value;
    }

    public phex.xml.XJBSharedLibrary getSharedLibrary() {
        return _SharedLibrary;
    }

    public void setSharedLibrary(phex.xml.XJBSharedLibrary value) {
        _SharedLibrary = value;
    }

    public phex.xml.XJBGUISettings getGuiSettings() {
        return _GuiSettings;
    }

    public void setGuiSettings(phex.xml.XJBGUISettings value) {
        _GuiSettings = value;
    }

    public com.sun.xml.bind.unmarshaller.ContentHandlerEx getUnmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
        return new phex.xml.impl.XJBPhexImpl.Unmarshaller(context);
    }

    public Class getPrimaryInterfaceClass() {
        return PRIMARY_INTERFACE_CLASS;
    }

    public void serializeElements(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        if (_SearchFilters != null) {
            context.startElement("", "search-filters");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _SearchFilters));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _SearchFilters));
            context.endElement();
        }
        if (_GuiSettings != null) {
            context.startElement("", "gui-settings");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _GuiSettings));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _GuiSettings));
            context.endElement();
        }
        if (_SharedLibrary != null) {
            context.startElement("", "sharedLibrary");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _SharedLibrary));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _SharedLibrary));
            context.endElement();
        }
        if (_DownloadList != null) {
            context.startElement("", "downloadlist");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _DownloadList));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _DownloadList));
            context.endElement();
        }
        if (_SWDownloadList != null) {
            context.startElement("", "swDownloadList");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _SWDownloadList));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _SWDownloadList));
            context.endElement();
        }
        if (_OldPhexVersion != null) {
            context.startElement("", "phex-version");
            context.endAttributes();
            context.text(((String) _OldPhexVersion));
            context.endElement();
        }
        if (_Security != null) {
            context.startElement("", "security");
            context.childAsAttributes(((com.sun.xml.bind.serializer.XMLSerializable) _Security));
            context.endAttributes();
            context.childAsElements(((com.sun.xml.bind.serializer.XMLSerializable) _Security));
            context.endElement();
        }
    }

    public void serializeAttributes(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
        if (_PhexVersion != null) {
            context.startAttribute("", "phex-version");
            context.text(((String) _PhexVersion));
            context.endAttribute();
        }
    }

    public void serializeAttributeBodies(com.sun.xml.bind.serializer.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public Class getPrimaryInterface() {
        return (phex.xml.XJBPhex.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends com.sun.xml.bind.unmarshaller.ContentHandlerEx {

        public Unmarshaller(com.sun.xml.bind.unmarshaller.UnmarshallingContext context) {
            super(context, "-----------------");
        }

        protected com.sun.xml.bind.unmarshaller.UnmarshallableObject owner() {
            return phex.xml.impl.XJBPhexImpl.this;
        }

        public void enterElement(String ___uri, String ___local, org.xml.sax.Attributes __atts) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    if ("".equals(___uri) && "downloadfile".equals(___local)) {
                        _DownloadList = ((phex.xml.impl.XJBOldDownloadListImpl) spawnChildFromEnterElement((phex.xml.impl.XJBOldDownloadListImpl.class), 10, ___uri, ___local, __atts));
                        return;
                    }
                    break;
                case 0:
                    if ("".equals(___uri) && "downloadlist".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 9;
                        return;
                    }
                    if ("".equals(___uri) && "search-filters".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 1;
                        return;
                    }
                    if ("".equals(___uri) && "phex-version".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 13;
                        return;
                    }
                    if ("".equals(___uri) && "sharedLibrary".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 15;
                        return;
                    }
                    if ("".equals(___uri) && "swDownloadList".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 3;
                        return;
                    }
                    if ("".equals(___uri) && "gui-settings".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 11;
                        return;
                    }
                    if ("".equals(___uri) && "security".equals(___local)) {
                        context.pushAttributes(__atts);
                        state = 7;
                        return;
                    }
                    revertToParentFromEnterElement(___uri, ___local, __atts);
                    return;
                case 7:
                    if ("".equals(___uri) && "ip-access-rule".equals(___local)) {
                        _Security = ((phex.xml.impl.XJBSecurityImpl) spawnChildFromEnterElement((phex.xml.impl.XJBSecurityImpl.class), 8, ___uri, ___local, __atts));
                        return;
                    }
                    break;
                case 3:
                    if ("".equals(___uri) && "swDownloadFile".equals(___local)) {
                        _SWDownloadList = ((phex.xml.impl.XJBSWDownloadListImpl) spawnChildFromEnterElement((phex.xml.impl.XJBSWDownloadListImpl.class), 4, ___uri, ___local, __atts));
                        return;
                    }
                    break;
                case 15:
                    if ("".equals(___uri) && "SF".equals(___local)) {
                        _SharedLibrary = ((phex.xml.impl.XJBSharedLibraryImpl) spawnChildFromEnterElement((phex.xml.impl.XJBSharedLibraryImpl.class), 16, ___uri, ___local, __atts));
                        return;
                    }
                    break;
                case 11:
                    if ("".equals(___uri) && "tab".equals(___local)) {
                        _GuiSettings = ((phex.xml.impl.XJBGUISettingsImpl) spawnChildFromEnterElement((phex.xml.impl.XJBGUISettingsImpl.class), 12, ___uri, ___local, __atts));
                        return;
                    }
                    if ("".equals(___uri) && "table-list".equals(___local)) {
                        _GuiSettings = ((phex.xml.impl.XJBGUISettingsImpl) spawnChildFromEnterElement((phex.xml.impl.XJBGUISettingsImpl.class), 12, ___uri, ___local, __atts));
                        return;
                    }
                    break;
                case 1:
                    if ("".equals(___uri) && "search-filter".equals(___local)) {
                        _SearchFilters = ((phex.xml.impl.XJBSearchFiltersImpl) spawnChildFromEnterElement((phex.xml.impl.XJBSearchFiltersImpl.class), 2, ___uri, ___local, __atts));
                        return;
                    }
                    break;
            }
            super.enterElement(___uri, ___local, __atts);
        }

        public void leaveElement(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 9:
                    if ("".equals(___uri) && "downloadlist".equals(___local)) {
                        _DownloadList = ((phex.xml.impl.XJBOldDownloadListImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBOldDownloadListImpl.class), 10, ___uri, ___local));
                        return;
                    }
                    break;
                case 0:
                    revertToParentFromLeaveElement(___uri, ___local);
                    return;
                case 7:
                    if ("".equals(___uri) && "security".equals(___local)) {
                        _Security = ((phex.xml.impl.XJBSecurityImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBSecurityImpl.class), 8, ___uri, ___local));
                        return;
                    }
                    break;
                case 3:
                    if ("".equals(___uri) && "swDownloadList".equals(___local)) {
                        _SWDownloadList = ((phex.xml.impl.XJBSWDownloadListImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBSWDownloadListImpl.class), 4, ___uri, ___local));
                        return;
                    }
                    break;
                case 4:
                    if ("".equals(___uri) && "swDownloadList".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 2:
                    if ("".equals(___uri) && "search-filters".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 15:
                    if ("".equals(___uri) && "sharedLibrary".equals(___local)) {
                        _SharedLibrary = ((phex.xml.impl.XJBSharedLibraryImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBSharedLibraryImpl.class), 16, ___uri, ___local));
                        return;
                    }
                    break;
                case 11:
                    if ("".equals(___uri) && "gui-settings".equals(___local)) {
                        _GuiSettings = ((phex.xml.impl.XJBGUISettingsImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBGUISettingsImpl.class), 12, ___uri, ___local));
                        return;
                    }
                    break;
                case 12:
                    if ("".equals(___uri) && "gui-settings".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 14:
                    if ("".equals(___uri) && "phex-version".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 10:
                    if ("".equals(___uri) && "downloadlist".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 16:
                    if ("".equals(___uri) && "sharedLibrary".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
                case 1:
                    if ("".equals(___uri) && "search-filters".equals(___local)) {
                        _SearchFilters = ((phex.xml.impl.XJBSearchFiltersImpl) spawnChildFromLeaveElement((phex.xml.impl.XJBSearchFiltersImpl.class), 2, ___uri, ___local));
                        return;
                    }
                    break;
                case 8:
                    if ("".equals(___uri) && "security".equals(___local)) {
                        context.popAttributes();
                        goto0();
                        return;
                    }
                    break;
            }
            super.leaveElement(___uri, ___local);
        }

        public void enterAttribute(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 0:
                    if ("".equals(___uri) && "phex-version".equals(___local)) {
                        state = 5;
                        return;
                    }
                    revertToParentFromEnterAttribute(___uri, ___local);
                    return;
            }
            super.enterAttribute(___uri, ___local);
        }

        public void leaveAttribute(String ___uri, String ___local) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(state) {
                case 6:
                    if ("".equals(___uri) && "phex-version".equals(___local)) {
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

        public void text(String value) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            try {
                switch(state) {
                    case 0:
                        revertToParentFromText(value);
                        return;
                    case 5:
                        _PhexVersion = value;
                        state = 6;
                        return;
                    case 13:
                        _OldPhexVersion = value;
                        state = 14;
                        return;
                }
            } catch (RuntimeException e) {
                handleUnexpectedTextException(value, e);
            }
        }

        public void leaveChild(int nextState) throws com.sun.xml.bind.unmarshaller.UnreportedException {
            switch(nextState) {
                case 4:
                    state = 4;
                    return;
                case 2:
                    state = 2;
                    return;
                case 12:
                    state = 12;
                    return;
                case 16:
                    state = 16;
                    return;
                case 10:
                    state = 10;
                    return;
                case 8:
                    state = 8;
                    return;
            }
            super.leaveChild(nextState);
        }

        private void goto0() throws com.sun.xml.bind.unmarshaller.UnreportedException {
            int idx;
            state = 0;
            idx = context.getAttribute("", "phex-version");
            if (idx >= 0) {
                context.consumeAttribute(idx);
                return;
            }
        }
    }
}
