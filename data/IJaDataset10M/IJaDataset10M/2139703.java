package org.fit.cssbox.layout;

import java.awt.Graphics2D;
import java.net.URL;
import java.util.ListIterator;
import java.util.Vector;
import org.fit.cssbox.css.DOMAnalyzer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.Selector;
import cz.vutbr.web.css.Term;
import cz.vutbr.web.css.TermFunction;
import cz.vutbr.web.css.TermIdent;
import cz.vutbr.web.css.TermList;
import cz.vutbr.web.css.TermString;
import cz.vutbr.web.css.TermURI;
import cz.vutbr.web.css.Selector.PseudoDeclaration;

/**
 * A factory for creating the box tree. The usual way of creating the box tree is creating the viewport using the 
 * {@link BoxFactory#createViewportTree(Element, Graphics2D, VisualContext, int, int)}. However, the factory can be used for creating
 * the individual nodes or subtrees.
 * 
 * <p>Usually, a single factory is created using the constructor. The last created factory is then accessible using
 * the {@link #getInstance()} method.
 * 
 * @author burgetr
 */
public class BoxFactory {

    private static BoxFactory instance = null;

    /** whether to use HTML */
    private boolean useHTML = true;

    protected DOMAnalyzer decoder;

    protected URL baseurl;

    protected Viewport viewport;

    protected int next_order;

    /**
     * Create a new factory. From this point, the new factory will be accessible using the {@link #getInstance()} method.
     * @param decoder The CSS decoder used for obtaining the DOM styles.
     * @param baseurl Base URL used for completing the relative URLs in the document.
     */
    public BoxFactory(DOMAnalyzer decoder, URL baseurl) {
        this.decoder = decoder;
        this.baseurl = baseurl;
        this.next_order = 0;
        instance = this;
    }

    /**
     * Get the latest created instance of the factory.
     * @return A box factory object or <code>null</code> when no factory has been created yet.
     */
    public static BoxFactory getInstance() {
        return instance;
    }

    /**
     * Sets whether the engine should use the HTML extensions or not. Currently, the HTML
     * extensions include following:
     * <ul>
     * <li>Creating replaced boxes for <code>&lt;img&gt;</code> elements
     * <li>Using the <code>&lt;body&gt;</code> element background for the whole canvas according to the HTML specification
     * </ul> 
     * @param useHTML <code>false</code> if the extensions should be switched off (default is on)
     */
    public void setUseHTML(boolean useHTML) {
        this.useHTML = useHTML;
    }

    /**
     * Checks if the HTML extensions are enabled for the factory.
     * @return <code>true</code> if the HTML extensions are enabled
     * @see #setUseHTML(boolean) 
     */
    public boolean getUseHTML() {
        return useHTML;
    }

    /**
     * Reset the factory for creating a new tree.
     */
    public void reset() {
        next_order = 0;
    }

    /**
     * Create the viewport and the underlying box tree from a DOM tree.
     * 
     * @param root the root element of the source DOM tree.
     * @param g the root graphic context. Copies of this context will be used for the individual boxes. 
     * @param ctx the visual context (computed style). Copies of this context will be used for the individual boxes.
     * @param width preferred viewport width.
     * @param height preferred viewport height.
     * @return the created viewport box with the corresponding box subtrees.
     */
    public Viewport createViewportTree(Element root, Graphics2D g, VisualContext ctx, int width, int height) {
        Element vp = createAnonymousElement(root.getOwnerDocument(), "Xdiv", "block");
        viewport = new Viewport(vp, g, ctx, this, root, width, height);
        BoxTreeCreationStatus stat = new BoxTreeCreationStatus(viewport);
        createSubtree(root, stat);
        System.out.println("Root box is: " + viewport.getRootBox());
        return viewport;
    }

    /**
     * Creates the box subtrees for all the child nodes of the DOM node corresponding to the box creatin status. Recursively creates the child boxes 
     * from the child nodes.
     * @param stat current tree creation status used for determining the parents
     */
    public void createBoxTree(BoxTreeCreationStatus stat) {
        boolean generated = false;
        do {
            if (stat.parent.isDisplayed()) {
                if (stat.parent.preadd != null) addToTree(stat.parent.preadd, stat);
                if (stat.parent.previousTwin == null) {
                    Node n = createPseudoElement(stat.parent, PseudoDeclaration.BEFORE);
                    if (n != null && (n.getNodeType() == Node.ELEMENT_NODE || n.getNodeType() == Node.TEXT_NODE)) {
                        stat.curchild = -1;
                        createSubtree(n, stat);
                    }
                }
                NodeList children = stat.parent.getElement().getChildNodes();
                for (int child = stat.parent.firstDOMChild; child < stat.parent.lastDOMChild; child++) {
                    Node n = children.item(child);
                    if (n.getNodeType() == Node.ELEMENT_NODE || n.getNodeType() == Node.TEXT_NODE) {
                        stat.curchild = child;
                        createSubtree(n, stat);
                    }
                }
                if (stat.parent.nextTwin == null) {
                    Node n = createPseudoElement(stat.parent, PseudoDeclaration.AFTER);
                    if (n != null && (n.getNodeType() == Node.ELEMENT_NODE || n.getNodeType() == Node.TEXT_NODE)) {
                        stat.curchild = children.getLength();
                        createSubtree(n, stat);
                    }
                }
                normalizeBox(stat.parent);
            }
            if (stat.parent.nextTwin != null) {
                stat.parent = stat.parent.nextTwin;
                generated = true;
            } else generated = false;
        } while (generated);
    }

    /**
     * Creates a subtree of a parent box that corresponds to a single child DOM node of this box and adds the subtree to the complete tree.
     * 
     * @param n the root DOM node of the subtree being created
     * @param stat curent box creation status for obtaining the containing boxes 
     */
    private void createSubtree(Node n, BoxTreeCreationStatus stat) {
        stat.parent.curstat = new BoxTreeCreationStatus(stat);
        Box newbox;
        boolean istext = false;
        if (n.getNodeType() == Node.TEXT_NODE) {
            newbox = createTextBox((Text) n, stat);
            istext = true;
        } else newbox = createElementBox((Element) n, stat);
        if (!istext) {
            BoxTreeCreationStatus newstat = new BoxTreeCreationStatus(stat);
            newstat.parent = (ElementBox) newbox;
            if (((ElementBox) newbox).mayContainBlocks()) {
                BlockBox block = (BlockBox) newbox;
                if (block.position == BlockBox.POS_ABSOLUTE || block.position == BlockBox.POS_RELATIVE || block.position == BlockBox.POS_FIXED) newstat.absbox = block;
                newstat.contbox = block;
                if (block.overflow == BlockBox.OVERFLOW_HIDDEN) newstat.clipbox = block;
                newstat.lastinflow = null;
                createBoxTree(newstat);
                removeTrailingWhitespaces(block);
            } else createBoxTree(newstat);
        }
        addToTree(newbox, stat);
    }

    /**
     * Adds a bew box to the tree according to its type and the tree creation status.
     * @param newbox the box to be added
     * @param stat current box tree creation status used for determining the appropriate parent boxes
     */
    private void addToTree(Box newbox, BoxTreeCreationStatus stat) {
        if (newbox.isBlock()) {
            if (!((BlockBox) newbox).isPositioned()) {
                if (stat.parent.mayContainBlocks()) {
                    stat.parent.addSubBox(newbox);
                    stat.lastinflow = newbox;
                } else {
                    ElementBox iparent = null;
                    ElementBox grandpa = stat.parent;
                    ElementBox prev = null;
                    do {
                        iparent = grandpa;
                        grandpa = iparent.getParent();
                        int lastchild = iparent.lastDOMChild;
                        iparent.lastDOMChild = iparent.curstat.curchild;
                        if (iparent.curstat.curchild + 1 < lastchild || prev != null) {
                            ElementBox newparent = iparent.copyBox();
                            newparent.removeAllSubBoxes();
                            newparent.firstDOMChild = iparent.curstat.curchild + 1;
                            iparent.nextTwin = newparent;
                            newparent.previousTwin = iparent;
                            if (prev != null) newparent.preadd = prev;
                            prev = newparent;
                        }
                    } while (grandpa != null && !grandpa.mayContainBlocks());
                    if (grandpa != null) {
                        iparent.postadd = new Vector<Box>(2);
                        iparent.postadd.add(newbox);
                        if (iparent.nextTwin != null) iparent.postadd.add(iparent.nextTwin);
                    } else System.err.println("BoxFactory: warning: grandpa is missing for " + newbox);
                }
            } else {
                newbox.getContainingBlock().addSubBox(newbox);
                ((BlockBox) newbox).absReference = stat.lastinflow;
            }
        } else {
            boolean lastwhite = (stat.lastinflow == null) || stat.lastinflow.isBlock() || (stat.lastinflow.endsWithWhitespace() && stat.lastinflow.collapsesSpaces());
            boolean collapse = lastwhite && newbox.isWhitespace() && newbox.collapsesSpaces();
            if (!collapse) {
                stat.parent.addSubBox(newbox);
                stat.lastinflow = newbox;
            }
        }
        if (newbox instanceof ElementBox && ((ElementBox) newbox).postadd != null) {
            for (Box box : ((ElementBox) newbox).postadd) addToTree(box, stat);
        }
    }

    /**
     * Removes the block box trailing inline whitespace child boxes if allowed by the white-space values. 
     * @param block the block box to be processed
     */
    private void removeTrailingWhitespaces(ElementBox block) {
        if (block.collapsesSpaces()) {
            for (ListIterator<Box> it = block.getSubBoxList().listIterator(block.getSubBoxNumber()); it.hasPrevious(); ) {
                Box subbox = it.previous();
                if (subbox.isInFlow()) {
                    if (!subbox.isBlock() && subbox.collapsesSpaces()) {
                        if (subbox.isWhitespace()) it.remove(); else if (subbox instanceof ElementBox) {
                            removeTrailingWhitespaces((ElementBox) subbox);
                            break;
                        } else if (subbox instanceof TextBox) {
                            ((TextBox) subbox).removeTrailingWhitespaces();
                            break;
                        }
                    } else break;
                }
            }
            block.setEndChild(block.getSubBoxList().size());
        }
    }

    /**
     * Creates a new box for an element node and sets the containing boxes accordingly.
     * @param n The element node
     * @param stat The box tree creation status used for obtaining the containing boxes
     * @return the newly created element box
     */
    public ElementBox createElementBox(Element n, BoxTreeCreationStatus stat) {
        ElementBox ret = createBox(stat.parent, (Element) n, null);
        ret.setClipBlock(stat.clipbox);
        if (ret.isBlock()) {
            BlockBox block = (BlockBox) ret;
            if (block.position == BlockBox.POS_ABSOLUTE || block.position == BlockBox.POS_FIXED) ret.setContainingBlock(stat.absbox); else ret.setContainingBlock(stat.contbox);
        } else ret.setContainingBlock(stat.contbox);
        return ret;
    }

    /**
     * Creates a new box for a text node and sets the containing boxes accordingly.
     * @param n The element node
     * @param stat Current box tree creation status for obtaining the containing boxes
     * @return the newly created text box
     */
    private TextBox createTextBox(Text n, BoxTreeCreationStatus stat) {
        TextBox text = new TextBox(n, (Graphics2D) stat.parent.getGraphics().create(), stat.parent.getVisualContext().create());
        text.setOrder(next_order++);
        text.setContainingBlock(stat.contbox);
        text.setClipBlock(stat.clipbox);
        text.setViewport(viewport);
        text.setBase(baseurl);
        return text;
    }

    /**
     * Checks the newly created box and creates anonymous block boxes above the children if necessary.
     * @param root the box to be checked
     * @return the modified root box
     */
    private ElementBox normalizeBox(ElementBox root) {
        if (root.mayContainBlocks() && ((BlockBox) root).containsBlocks()) createAnonymousBlocks((BlockBox) root); else if (root.containsMixedContent()) createAnonymousInline(root);
        createAnonymousBoxes(root, ElementBox.DISPLAY_TABLE_CELL, ElementBox.DISPLAY_TABLE_ROW, ElementBox.DISPLAY_ANY, ElementBox.DISPLAY_ANY, "tr", "table-row");
        createAnonymousBoxes(root, ElementBox.DISPLAY_TABLE_ROW, ElementBox.DISPLAY_TABLE_ROW_GROUP, ElementBox.DISPLAY_TABLE_HEADER_GROUP, ElementBox.DISPLAY_TABLE_FOOTER_GROUP, "tbody", "table-row-group");
        createAnonymousBoxes(root, ElementBox.DISPLAY_TABLE_COLUMN, ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_TABLE_COLUMN_GROUP, ElementBox.DISPLAY_ANY, "table", "table");
        createAnonymousBoxes(root, ElementBox.DISPLAY_TABLE_ROW_GROUP, ElementBox.DISPLAY_TABLE, ElementBox.DISPLAY_ANY, ElementBox.DISPLAY_ANY, "table", "table");
        return root;
    }

    /**
     * Creates anonymous inline boxes if the a block box contains both the inline
     * and the text child boxes. The child boxes of the specified root
     * are processed and the text boxes are grouped in a newly created
     * anonymous <code>span</code> boxes.
     * @param root the root box
     */
    private void createAnonymousInline(ElementBox root) {
        Vector<Box> nest = new Vector<Box>();
        for (int i = 0; i < root.getSubBoxNumber(); i++) {
            Box sub = root.getSubBox(i);
            if (sub instanceof ElementBox) nest.add(sub); else {
                ElementBox anbox = createAnonymousBox(root, sub, false);
                anbox.addSubBox(sub);
                nest.add(anbox);
            }
        }
        root.nested = nest;
        root.endChild = nest.size();
    }

    /**
     * Creates anonymous block boxes if the a block box contains both the inline
     * and the block child boxes. The child boxes of the specified root
     * are processed and the inline boxes are grouped in a newly created
     * anonymous <code>div</code> boxes.
     * @param root the root box
     */
    private void createAnonymousBlocks(BlockBox root) {
        Vector<Box> nest = new Vector<Box>();
        ElementBox adiv = null;
        for (int i = 0; i < root.getSubBoxNumber(); i++) {
            Box sub = root.getSubBox(i);
            if (sub.isBlock()) {
                if (adiv != null && !adiv.isempty) {
                    normalizeBox(adiv);
                    removeTrailingWhitespaces(adiv);
                }
                adiv = null;
                nest.add(sub);
            } else if (adiv != null || !sub.isWhitespace()) {
                if (adiv == null) {
                    adiv = createAnonymousBox(root, sub, true);
                    nest.add(adiv);
                }
                if (sub.isDisplayed() && !sub.isEmpty()) {
                    adiv.isempty = false;
                    adiv.displayed = true;
                }
                adiv.addSubBox(sub);
            }
        }
        if (adiv != null && !adiv.isempty) {
            normalizeBox(adiv);
            removeTrailingWhitespaces(adiv);
        }
        root.nested = nest;
        root.endChild = nest.size();
    }

    /**
     * Checks the child boxes of the specified root box wheter they require creating an anonymous
     * parent box.
     * @param root the box whose child boxes are checked
     * @param type the required display type of the child boxes. The remaining child boxes are skipped.
     * @param reqtype1 the first required display type of the root. If the root type doesn't correspond
     *      to any of the required types, an anonymous parent is created for the selected children.
     * @param reqtype2 the second required display type of the root.
     * @param reqtype3 the third required display type of the root.
     * @param name the element name of the created anonymous box
     * @param display the display type of the created anonymous box
     */
    private void createAnonymousBoxes(ElementBox root, CSSProperty.Display type, CSSProperty.Display reqtype1, CSSProperty.Display reqtype2, CSSProperty.Display reqtype3, String name, String display) {
        if (root.getDisplay() != reqtype1 && root.getDisplay() != reqtype2 && root.getDisplay() != reqtype3) {
            Vector<Box> nest = new Vector<Box>();
            ElementBox adiv = null;
            for (int i = 0; i < root.getSubBoxNumber(); i++) {
                Box sub = root.getSubBox(i);
                if (sub instanceof ElementBox) {
                    ElementBox subel = (ElementBox) sub;
                    if (subel.getDisplay() != type) {
                        adiv = null;
                        nest.add(sub);
                    } else {
                        if (adiv == null) {
                            Element elem = createAnonymousElement(root.getElement().getOwnerDocument(), name, display);
                            adiv = createBox(root, elem, display);
                            adiv.isblock = true;
                            adiv.isempty = true;
                            adiv.setContainingBlock(sub.getContainingBlock());
                            adiv.setClipBlock(sub.getClipBlock());
                            nest.add(adiv);
                        }
                        if (sub.isDisplayed() && !sub.isEmpty()) {
                            adiv.isempty = false;
                            adiv.displayed = true;
                        }
                        sub.setParent(adiv);
                        sub.setContainingBlock((BlockBox) adiv);
                        adiv.addSubBox(sub);
                    }
                } else return;
            }
            root.nested = nest;
            root.endChild = nest.size();
        }
    }

    /**
     * Creates an empty anonymous block or inline box that can be placed between an optional parent and its child.
     * The corresponding properties of the box are taken from the child. The child is inserted NOT as the child box of the new box. 
     * The new box is NOT inserted as a subbox of the parent. 
     * @param parent an optional parent node. When used, the parent of the new box is set to this node and the style is inherited from the parent. 
     * @param child the child node
     * @param block when set to <code>true</code>, a {@link BlockBox} is created. Otherwise, a {@link InlineBox} is created.
     * @return the new created block box
     */
    protected ElementBox createAnonymousBox(ElementBox parent, Box child, boolean block) {
        ElementBox anbox;
        if (block) {
            Element anelem = createAnonymousElement(child.getNode().getOwnerDocument(), "Xdiv", "block");
            anbox = new BlockBox(anelem, (Graphics2D) child.getGraphics().create(), child.getVisualContext().create());
            anbox.setStyle(createAnonymousStyle("block"));
            ((BlockBox) anbox).contblock = false;
            anbox.isblock = true;
        } else {
            Element anelem = createAnonymousElement(child.getNode().getOwnerDocument(), "Xspan", "inline");
            anbox = new InlineBox(anelem, (Graphics2D) child.getGraphics().create(), child.getVisualContext().create());
            anbox.setStyle(createAnonymousStyle("inline"));
            anbox.isblock = false;
        }
        if (parent != null) {
            computeInheritedStyle(anbox, parent);
            anbox.setParent(parent);
        }
        anbox.setOrder(next_order++);
        anbox.isempty = true;
        anbox.setBase(child.getBase());
        anbox.setViewport(child.getViewport());
        anbox.setContainingBlock(child.getContainingBlock());
        anbox.setClipBlock(child.getClipBlock());
        return anbox;
    }

    /**
     * Creates a single new box from an element.
     * @param n The source DOM element
     * @param display the display: property value that is used when the box style is not known (e.g. anonymous boxes)
     * @return A new box of a subclass of {@link ElementBox} based on the value of the 'display' CSS property
     */
    public ElementBox createBox(ElementBox parent, Element n, String display) {
        ElementBox root;
        NodeData style = decoder.getElementStyleInherited(n);
        if (style == null) style = createAnonymousStyle(display);
        if (useHTML && n.getNodeName().equals("img")) {
            InlineReplacedBox rbox = new InlineReplacedBox((Element) n, (Graphics2D) parent.getGraphics().create(), parent.getVisualContext().create());
            rbox.setStyle(style);
            rbox.setContentObj(new ReplacedImage(rbox, rbox.getVisualContext(), baseurl));
            root = rbox;
            if (root.isBlock()) root = new BlockReplacedBox(rbox);
        } else {
            root = createElementInstance(parent, n, style);
        }
        root.setBase(baseurl);
        root.setViewport(viewport);
        root.setParent(parent);
        root.setOrder(next_order++);
        return root;
    }

    /**
     * Creates a new box for a pseudo-element.
     * @param n The source DOM element
     * @param pseudo The pseudo element name
     * @param viewport The used viewport
     * @param parent the root element from which the style will be inherited
     * @return A new box of a subclass of ElementBox based on the value of the 'display' CSS property
     */
    private Node createPseudoElement(ElementBox box, Selector.PseudoDeclaration pseudo) {
        Element n = box.getElement();
        NodeData style = decoder.getElementStyleInherited(n, pseudo);
        if (style != null) {
            TermList cont = style.getValue(TermList.class, "content");
            if (cont != null && cont.size() > 0) {
                Element pelem = createAnonymousElement(n.getOwnerDocument(), "XPspan", "inline");
                for (Term<?> c : cont) {
                    if (c instanceof TermIdent) {
                    } else if (c instanceof TermString) {
                        Text txt = n.getOwnerDocument().createTextNode(((TermString) c).getValue());
                        pelem.appendChild(txt);
                    } else if (c instanceof TermURI) {
                    } else if (c instanceof TermFunction) {
                    }
                }
                decoder.useStyle(pelem, null, style);
                return pelem;
            } else return null;
        } else return null;
    }

    /**
     * Creates an instance of ElementBox. According to the display: property of the style, the appropriate
     * subclass of ElementBox is created (e.g. BlockBox, TableBox, etc.)
     * @param n The source DOM element
     * @param style Style definition for the node
     * @return The created instance of ElementBox
     */
    private ElementBox createElementInstance(ElementBox parent, Element n, NodeData style) {
        ElementBox root = new InlineBox((Element) n, (Graphics2D) parent.getGraphics().create(), parent.getVisualContext().create());
        root.setStyle(style);
        if (root.getDisplay() == ElementBox.DISPLAY_LIST_ITEM) root = new ListItemBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE) root = new BlockTableBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_CAPTION) root = new TableCaptionBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_ROW_GROUP || root.getDisplay() == ElementBox.DISPLAY_TABLE_HEADER_GROUP || root.getDisplay() == ElementBox.DISPLAY_TABLE_FOOTER_GROUP) root = new TableBodyBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_ROW) root = new TableRowBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_CELL) root = new TableCellBox((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_COLUMN) root = new TableColumn((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_TABLE_COLUMN_GROUP) root = new TableColumnGroup((InlineBox) root); else if (root.getDisplay() == ElementBox.DISPLAY_INLINE_BLOCK) root = new InlineBlockBox((InlineBox) root); else if (root.isBlock()) root = new BlockBox((InlineBox) root);
        return root;
    }

    /**
     * Creates a new DOM element that represents an anonymous box in a document.
     * @param doc the document
     * @param name the anonymous element name (generally arbitrary)
     * @param display the display style value for the block
     * @return the new element
     */
    public Element createAnonymousElement(Document doc, String name, String display) {
        Element div = doc.createElement(name);
        div.setAttribute("class", "Xanonymous");
        div.setAttribute("style", "display:" + display);
        return div;
    }

    /**
     * Creates the style definition for an anonymous box. It contains only the class name set to "Xanonymous"
     * and the display: property set according to the parametres.
     * @param display <code>display:</code> property value of the resulting style.
     * @return Resulting style definition
     */
    public NodeData createAnonymousStyle(String display) {
        NodeData ret = CSSFactory.createNodeData();
        Declaration cls = CSSFactory.getRuleFactory().createDeclaration();
        cls.unlock();
        cls.setProperty("class");
        cls.add(CSSFactory.getTermFactory().createString("Xanonymous"));
        ret.push(cls);
        Declaration disp = CSSFactory.getRuleFactory().createDeclaration();
        disp.unlock();
        disp.setProperty("display");
        disp.add(CSSFactory.getTermFactory().createIdent(display));
        ret.push(disp);
        return ret;
    }

    /**
     * Computes the style of a node based on its parent using the CSS inheritance.
     * @param dest the box whose style should be computed
     * @param parent the parent box
     */
    private void computeInheritedStyle(ElementBox dest, ElementBox parent) {
        NodeData newstyle = dest.getStyle().inheritFrom(parent.getStyle());
        dest.setStyle(newstyle);
    }
}

/**
 * The box tree creation status holds all the ancestor boxes that might be necessary for creating the child boxes
 * and adding them to the resulting tree
 *
 * @author burgetr
 */
class BoxTreeCreationStatus {

    /** Normal flow parent box */
    public ElementBox parent;

    /** Containing block for normal flow */
    public BlockBox contbox;

    /** Containing block for absolutely positioned boxes */
    public BlockBox absbox;

    /** Clipping box based on overflow property */
    public BlockBox clipbox;

    /** Last in-flow box */
    public Box lastinflow;

    /** The index of the DOM node within its parent node */
    int curchild;

    /** 
     * Creates a new initial creation status
     * @param viewport the root viewport box
     */
    public BoxTreeCreationStatus(Viewport viewport) {
        parent = contbox = absbox = clipbox = viewport;
        lastinflow = null;
        curchild = 0;
    }

    /** 
     * Creates a copy of the status
     * @param stat original status
     */
    public BoxTreeCreationStatus(BoxTreeCreationStatus stat) {
        this.parent = stat.parent;
        this.contbox = stat.contbox;
        this.absbox = stat.absbox;
        this.clipbox = stat.clipbox;
        this.lastinflow = stat.lastinflow;
        this.curchild = stat.curchild;
    }
}
