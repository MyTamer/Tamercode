package freemind.controller.actions.generated.instance.impl;

public class CompoundActionImpl extends freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl implements freemind.controller.actions.generated.instance.CompoundAction, com.sun.xml.bind.JAXBObject, com.sun.xml.bind.RIElement, freemind.controller.actions.generated.instance.impl.runtime.UnmarshallableObject, freemind.controller.actions.generated.instance.impl.runtime.XMLSerializable, freemind.controller.actions.generated.instance.impl.runtime.ValidatableObject {

    public static final java.lang.Class version = (freemind.controller.actions.generated.instance.impl.JAXBVersion.class);

    private static com.sun.msv.grammar.Grammar schemaFragment;

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "compound_action";
    }

    private static final java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (freemind.controller.actions.generated.instance.CompoundAction.class);
    }

    public freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingEventHandler createUnmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
        return new freemind.controller.actions.generated.instance.impl.CompoundActionImpl.Unmarshaller(context);
    }

    public void serializeElementBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "compound_action");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeElementBody(context);
        context.endElement();
    }

    public void serializeAttributes(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public void serializeAttributeBody(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
        context.startElement("", "compound_action");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeElementBody(context);
        context.endElement();
    }

    public void serializeURIs(freemind.controller.actions.generated.instance.impl.runtime.XMLSerializer context) throws org.xml.sax.SAXException {
    }

    public java.lang.Class getPrimaryInterface() {
        return (freemind.controller.actions.generated.instance.CompoundAction.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize(("¬í sr \'com.sun.msv.grammar.trex.ElementPattern        L " + "\tnameClasst Lcom/sun/msv/grammar/NameClass;xr com.sun.msv." + "grammar.ElementExp        Z ignoreUndeclaredAttributesL " + "\fcontentModelt  Lcom/sun/msv/grammar/Expression;xr com.sun." + "msv.grammar.ExpressionøèN5~O I cachedHashCodeL epsilon" + "Reducibilityt Ljava/lang/Boolean;L expandedExpq ~ xp\t\b{p" + "p sr com.sun.msv.grammar.ChoiceExp         xr com.sun.ms" + "v.grammar.BinaryExp        L exp1q ~ L exp2q ~ xq ~ " + "\t\bpppsr  com.sun.msv.grammar.OneOrMoreExp         xr com" + ".sun.msv.grammar.UnaryExp        L expq ~ xq ~ \t\besr " + "java.lang.BooleanÍ rÕúî Z valuexp psq ~ \t\bbq ~ psq " + "~ \bÄÐq ~ psq ~ \b>q ~ psq ~ \b<¬q ~ psq ~ øq ~ " + "psq ~ ´q ~ psq ~ pöq ~ psq ~ ,dq ~ psq ~ èÒq" + " ~ psq ~ ¤@q ~ psq ~ `\r®q ~ psq ~ \rq ~ psq ~ " + "Ø\fq ~ psq ~ øq ~ psq ~ Pfq ~ psq ~ \f\nÔq ~ psq " + "~ È\nBq ~ psq ~ \t°q ~ psq ~ @\tq ~ psq ~ ü\bq ~ " + "psq ~ ¸úq ~ psq ~ thq ~ psq ~ 0Öq ~ psq ~ ìDq" + " ~ psq ~ ¨²q ~ psq ~ d q ~ psq ~  q ~ psq ~ " + "Üüq ~ psq ~ jq ~ psq ~ TØq ~ psq ~ Fq ~ psq " + "~  Ì´q ~ psq ~  \"q ~ psq ~   D q ~ p sq ~  D ppsq" + " ~ \n D zq ~ psr  com.sun.msv.grammar.AttributeExp        " + "L expq ~ L \tnameClassq ~ xq ~  D wq ~ psr 2com.sun.msv" + ".grammar.Expression$AnyStringExpression         xq ~    \b" + "sq ~ \rpsr  com.sun.msv.grammar.AnyNameClass         xr c" + "om.sun.msv.grammar.NameClass         xpsr 0com.sun.msv.gra" + "mmar.Expression$EpsilonExpression         xq ~    \tq ~ 7q" + " ~ <sr #com.sun.msv.grammar.SimpleNameClass        L \tloc" + "alNamet Ljava/lang/String;L \fnamespaceURIq ~ >xq ~ 9t =free" + "mind.controller.actions.generated.instance.CompoundActiont +" + "http://java.sun.com/jaxb/xjc/dummy-elementssq ~   D q ~ p " + "sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <s" + "q ~ =t ?freemind.controller.actions.generated.instance.Selec" + "tNodeActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ " + "psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t <freemind.controller" + ".actions.generated.instance.CutNodeActionq ~ Asq ~   D q ~ " + "p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~" + " <sq ~ =t >freemind.controller.actions.generated.instance.Pa" + "steNodeActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~" + " psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t >freemind.controll" + "er.actions.generated.instance.RevertXmlActionq ~ Asq ~   D " + "q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ " + ":q ~ <sq ~ =t =freemind.controller.actions.generated.instanc" + "e.BoldNodeActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D z" + "q ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t ?freemind.contr" + "oller.actions.generated.instance.ItalicNodeActionq ~ Asq ~  " + " D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6" + "q ~ :q ~ <sq ~ =t Cfreemind.controller.actions.generated.ins" + "tance.UnderlinedNodeActionq ~ Asq ~   D q ~ p sq ~  D pp" + "sq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Afree" + "mind.controller.actions.generated.instance.FontSizeNodeActio" + "nq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D " + "wq ~ pq ~ 6q ~ :q ~ <sq ~ =t =freemind.controller.actions.g" + "enerated.instance.FontNodeActionq ~ Asq ~   D q ~ p sq ~ " + " D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t" + " Dfreemind.controller.actions.generated.instance.NodeColorFo" + "rmatActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ p" + "sq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Nfreemind.controller." + "actions.generated.instance.NodeBackgroundColorFormatActionq " + "~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq " + "~ pq ~ 6q ~ :q ~ <sq ~ =t Dfreemind.controller.actions.gene" + "rated.instance.NodeStyleFormatActionq ~ Asq ~   D q ~ p sq" + " ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq " + "~ =t Dfreemind.controller.actions.generated.instance.EdgeCol" + "orFormatActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq " + "~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Dfreemind.control" + "ler.actions.generated.instance.EdgeWidthFormatActionq ~ Asq " + "~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq " + "~ 6q ~ :q ~ <sq ~ =t Dfreemind.controller.actions.generated." + "instance.EdgeStyleFormatActionq ~ Asq ~   D q ~ p sq ~  D" + " ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t ?" + "freemind.controller.actions.generated.instance.DeleteNodeAct" + "ionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 " + "D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t =freemind.controller.actions" + ".generated.instance.EditNodeActionq ~ Asq ~   D q ~ p sq ~" + "  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ " + "=t <freemind.controller.actions.generated.instance.NewNodeAc" + "tionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3" + " D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t <freemind.controller.action" + "s.generated.instance.UndoXmlActionq ~ Asq ~   D q ~ p sq ~" + "  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ " + "=t 9freemind.controller.actions.generated.instance.FoldActio" + "nq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D " + "wq ~ pq ~ 6q ~ :q ~ <sq ~ =t >freemind.controller.actions.g" + "enerated.instance.MoveNodesActionq ~ Asq ~   D q ~ p sq ~ " + " D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =" + "t =freemind.controller.actions.generated.instance.HookNodeAc" + "tionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3" + " D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t <freemind.controller.action" + "s.generated.instance.AddIconActionq ~ Asq ~   D q ~ p sq ~" + "  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ " + "=t Ffreemind.controller.actions.generated.instance.RemoveLas" + "tIconXmlActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq " + "~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Ffreemind.control" + "ler.actions.generated.instance.RemoveAllIconsXmlActionq ~ As" + "q ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ p" + "q ~ 6q ~ :q ~ <sq ~ =t @freemind.controller.actions.generate" + "d.instance.MoveNodeXmlActionq ~ Asq ~   D q ~ p sq ~  D " + "ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t @fr" + "eemind.controller.actions.generated.instance.AddCloudXmlActi" + "onq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D" + " wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Dfreemind.controller.actions." + "generated.instance.AddArrowLinkXmlActionq ~ Asq ~   D q ~ " + "p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ " + "<sq ~ =t ?freemind.controller.actions.generated.instance.Add" + "LinkXmlActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq ~" + " psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Gfreemind.controll" + "er.actions.generated.instance.RemoveArrowLinkXmlActionq ~ As" + "q ~   D q ~ p sq ~  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ p" + "q ~ 6q ~ :q ~ <sq ~ =t Ffreemind.controller.actions.generate" + "d.instance.ArrowLinkColorXmlActionq ~ Asq ~   D q ~ p sq ~" + "  D ppsq ~ \n D zq ~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ " + "=t Ffreemind.controller.actions.generated.instance.ArrowLink" + "ArrowXmlActionq ~ Asq ~   D q ~ p sq ~  D ppsq ~ \n D zq " + "~ psq ~ 3 D wq ~ pq ~ 6q ~ :q ~ <sq ~ =t Ffreemind.control" + "ler.actions.generated.instance.ArrowLinkPointXmlActionq ~ Aq" + " ~ <sq ~ =t compound_actiont  sr \"com.sun.msv.grammar.Expre" + "ssionPool        L \bexpTablet /Lcom/sun/msv/grammar/Expre" + "ssionPool$ClosedHash;xpsr -com.sun.msv.grammar.ExpressionPoo" + "l$ClosedHash×jÐNïèí I countI \tthresholdL parentq ~\f[ " + "tablet ![Lcom/sun/msv/grammar/Expression;xp   g   rpur ![Lco" + "m.sun.msv.grammar.Expression;Ö8DÃ]­§\n  xp  q ~ gq ~ aq ~ " + "[q ~ Uq ~ Oq ~ Iq ~ Cq ~ 1q ~ Vq ~ Pq ~ /q ~ Jq ~ Dq ~ 2q ~ " + "°q ~ ©q ~ .q ~ ªq ~ £q ~ ¤q ~ q ~ q ~ -q ~ q ~ ¯q ~ ¶q ~ " + "µq ~ ¼q ~ ,q ~ »q ~ Âq ~ Áq ~ Èq ~ Çq ~ +q ~ Îq ~ Íq ~ Ôq ~ " + "Óq ~ Úq ~ *q ~ Ùq ~ àq ~ ßq ~ æq ~ åq ~ )q ~ ìq ~ ëq ~ òq ~ " + "ñq ~ øq ~ (q ~ ÷q ~ þq ~ ýq ~q ~q ~ \'pppppq ~ &pppppq ~ " + "%pppppq ~ $pppppq ~ #pppppq ~ \"pppppq ~ !pppppq ~  pppppq ~ " + "pppppq ~ pppppq ~ pppppq ~ pppppq ~ pppppq ~ pppppq ~ " + "pppppq ~ pppppq ~ pppppq ~ pppppq ~ pppppq ~ pppppq ~ " + "pppppq ~ pppppq ~ pppppq ~ pppppq ~ ppq ~ \fppppppppppq " + "~ \tppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp" + "pppppppppppppppppppppppppppppppq ~ q ~ q ~ q ~ q ~ q ~ " + "zq ~ tq ~ nq ~ hq ~ bq ~ \\q ~ q ~ q ~ q ~ q ~ yq ~ sq ~ " + "m"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller extends freemind.controller.actions.generated.instance.impl.runtime.AbstractUnmarshallingEventHandlerImpl {

        public Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(freemind.controller.actions.generated.instance.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts) throws org.xml.sax.SAXException {
            int attIdx;
            outer: while (true) {
                switch(state) {
                    case 0:
                        if (("compound_action" == ___local) && ("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return;
                        }
                        break;
                    case 3:
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return;
                    case 1:
                        if (("compound_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("select_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("cut_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("paste_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("revert_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("bold_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("italic_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("underlined_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("font_size_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("font_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("node_color_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("node_background_color_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("node_style_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("edge_color_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("edge_width_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("edge_style_format_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("delete_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("edit_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("new_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("undo_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("fold_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("move_nodes_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("hook_node_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("add_icon_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("remove_last_icon_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("remove_all_icons_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("move_node_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("add_cloud_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("add_arrow_link_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("add_link_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("remove_arrow_link_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("arrow_link_color_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("arrow_link_arrow_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        if (("arrow_link_point_xml_action" == ___local) && ("" == ___uri)) {
                            spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return;
                        }
                        spawnHandlerFromEnterElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                    case 3:
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return;
                    case 1:
                        spawnHandlerFromLeaveElement((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return;
                    case 2:
                        if (("compound_action" == ___local) && ("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return;
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
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return;
                    case 1:
                        spawnHandlerFromEnterAttribute((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case 1:
                        spawnHandlerFromLeaveAttribute((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                            spawnHandlerFromText((((freemind.controller.actions.generated.instance.impl.CompoundActionTypeImpl) freemind.controller.actions.generated.instance.impl.CompoundActionImpl.this).new Unmarshaller(context)), 2, value);
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
