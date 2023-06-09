package edu.harvard.hul.ois.jhove.module.pdf;

import edu.harvard.hul.ois.jhove.module.*;
import java.util.*;

/**
 *  Class encapsulating a PDF page tree node.
 *  The page tree is built such that callers can walk through
 *  it by calling startWalk and then calling nextDocNode
 *  (for all nodes) or nextPageObject (for pages only) repeatedly.
 */
public class PageTreeNode extends DocNode {

    private List _descendants;

    private ListIterator _descendantsIter;

    private DocNode _currentDescendant;

    private boolean _walkFirst;

    /**
     *  Superclass constructor.
     *  @param module     The PDFModule under which we're operating
     *  @param parent     The parent node in the document tree;
     *                    may be null only for the root node
     *  @param dict       The dictionary object on which this node
     *                    is based
     */
    public PageTreeNode(PdfModule module, PageTreeNode parent, PdfDictionary dict) {
        super(module, parent, dict);
        _pageObjectFlag = false;
        _descendants = new ArrayList(1);
    }

    /**
     *  Builds the subtree of descendants of this node, using
     *  the Kids entry in the dictionary.
     */
    public void buildSubtree(boolean toplevel, int recGuard) throws PdfException {
        buildSubtree(toplevel, recGuard, -1, -1);
    }

    /**
     *  Builds the subtree of descendants of this node, using
     *  the Kids entry in the dictionary.
     */
    public void buildSubtree(boolean toplevel, int recGuard, int objNumber, int genNumber) throws PdfException {
        if (recGuard <= 0) {
            throw new PdfMalformedException("Excessive depth or infinite recursion in page tree structure");
        }
        PdfArray kids = null;
        try {
            PdfObject obj = _dict.get("Kids");
            if (obj instanceof PdfIndirectObj) {
                kids = (PdfArray) (((PdfIndirectObj) obj).getObject());
            } else {
                kids = (PdfArray) obj;
            }
            if (toplevel && kids == null) {
                PdfSimpleObject type = (PdfSimpleObject) _dict.get("Type");
                if (type != null && "Page".equals(type.getStringValue())) {
                    PageObject pageObj = new PageObject(_module, this, _dict);
                    _descendants = new ArrayList(1);
                    _descendants.add(pageObj);
                }
            } else {
                Vector kidsVec = kids.getContent();
                _descendants = new ArrayList(kidsVec.size());
                for (int i = 0; i < kidsVec.size(); i++) {
                    PdfIndirectObj kidRef = (PdfIndirectObj) kidsVec.elementAt(i);
                    PdfDictionary kid = (PdfDictionary) _module.resolveIndirectObject(kidRef);
                    PdfSimpleObject kidtype = (PdfSimpleObject) kid.get("Type");
                    String kidtypeStr = kidtype.getStringValue();
                    if (kidtypeStr.equals("Page")) {
                        PageObject pageObj = new PageObject(_module, this, kid);
                        pageObj.loadContent(_module);
                        _descendants.add(pageObj);
                    } else if (kidtypeStr.equals("Pages")) {
                        PageTreeNode nodeObj = new PageTreeNode(_module, this, kid);
                        nodeObj.buildSubtree(false, recGuard - 1);
                        _descendants.add(nodeObj);
                    }
                }
            }
        } catch (PdfException ee) {
            throw ee;
        } catch (Exception e) {
            throw new PdfInvalidException("Invalid page tree node");
        }
    }

    /**
     *  Initialize an iterator through the descendants of this node.
     */
    public void startWalk() {
        _descendantsIter = _descendants.listIterator();
        _currentDescendant = null;
        _walkFirst = true;
        _walkFinished = false;
    }

    /**
     *   Get the next PageObject which is under this node.  This function
     *   is designed such that calling startWalk() and then repeatedly
     *   calling nextPageObject() will return all the PageObjects in the tree
     *   under this node, and finally will return null when there are no more.
     */
    public PageObject nextPageObject() {
        if (_walkFinished) {
            return null;
        }
        if (_currentDescendant == null) {
            if (!_descendantsIter.hasNext()) {
                _walkFinished = true;
                return null;
            }
            _currentDescendant = (DocNode) _descendantsIter.next();
            _currentDescendant.startWalk();
        }
        PageObject retval = _currentDescendant.nextPageObject();
        if (retval == null) {
            if (_descendantsIter.hasNext()) {
                _currentDescendant = (DocNode) _descendantsIter.next();
                _currentDescendant.startWalk();
                return _currentDescendant.nextPageObject();
            } else {
                _walkFinished = true;
                return null;
            }
        } else {
            return retval;
        }
    }

    /**
     *   Get the next DocNode which is under this node.  This function
     *   is designed such that calling startWalk() and then repeatedly
     *   calling nextPageObject() will return first this node,
     *   then all the DocNodes in the tree
     *   under this node. It finally will return null when there 
     *   are no more.
     */
    public DocNode nextDocNode() {
        if (_walkFinished) {
            return null;
        }
        if (_walkFirst) {
            _walkFirst = false;
            return this;
        }
        if (_currentDescendant == null) {
            if (!_descendantsIter.hasNext()) {
                _walkFinished = true;
                return null;
            }
            _currentDescendant = (DocNode) _descendantsIter.next();
            _currentDescendant.startWalk();
        }
        DocNode retval = _currentDescendant.nextDocNode();
        if (retval == null) {
            if (_descendantsIter.hasNext()) {
                _currentDescendant = (DocNode) _descendantsIter.next();
                _currentDescendant.startWalk();
                return _currentDescendant.nextDocNode();
            } else {
                _walkFinished = true;
                return null;
            }
        } else return retval;
    }
}
