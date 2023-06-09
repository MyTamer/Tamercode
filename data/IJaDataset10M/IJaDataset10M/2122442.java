package org.obo.util;

import java.util.Collection;
import java.util.LinkedList;
import org.bbop.util.VectorFilter;
import org.obo.datamodel.Link;
import org.obo.datamodel.LinkedObject;
import org.obo.datamodel.Namespace;
import org.obo.datamodel.OBOProperty;
import org.obo.datamodel.OBORestriction;
import org.obo.datamodel.OBOSession;
import org.obo.datamodel.Synonym;
import org.obo.datamodel.SynonymType;
import org.obo.datamodel.SynonymedObject;
import org.obo.datamodel.TermSubset;
import org.obo.history.HistoryItem;
import org.obo.history.HistoryList;
import org.obo.history.StringRelationship;
import org.obo.history.TermMacroHistoryItem;
import org.apache.log4j.*;

public class HistoryUtil {

    protected static final Logger logger = Logger.getLogger(HistoryUtil.class);

    /**
	 * Does a depth-first search of a {@link TermMacroHistoryItem} to find
	 * {@link HistoryItem}s that match the provided {@link VectorFilter}.
	 * 
	 * @param mitem
	 * @param filter
	 * @return a list of {@link HistoryItem}s that match the filter
	 */
    public static Collection findMatchingItems(TermMacroHistoryItem mitem, VectorFilter filter) {
        LinkedList out = new LinkedList();
        for (int i = 0; i < mitem.size(); i++) {
            HistoryItem item = mitem.getItemAt(i);
            if (filter.satisfies(item)) out.add(item);
            if (item instanceof TermMacroHistoryItem) out.addAll(findMatchingItems((TermMacroHistoryItem) item, filter));
        }
        return out;
    }

    /**
	 * Does a depth-first search of a {@link HistoryList} to find
	 * {@link HistoryItem}s that match the provided {@link VectorFilter}.
	 * 
	 * @param list
	 * @param filter
	 * @return a list of {@link HistoryItem}s that match the filter
	 */
    public static Collection findMatchingItems(HistoryList list, VectorFilter filter) {
        LinkedList<HistoryItem> out = new LinkedList<HistoryItem>();
        for (HistoryItem item : list.getHistoryItems()) {
            if (filter.satisfies(item)) out.add(item);
            if (item instanceof TermMacroHistoryItem) {
                out.addAll(findMatchingItems((TermMacroHistoryItem) item, filter));
            }
        }
        return out;
    }

    public static Link createRealRel(OBOSession session, StringRelationship tr) {
        if (tr == null) return null;
        LinkedObject parent = (LinkedObject) session.getObject(tr.getParent());
        LinkedObject child = (LinkedObject) session.getObject(tr.getChild());
        OBOProperty type = (OBOProperty) session.getObject(tr.getType());
        Namespace ns = session.getNamespace(tr.getNamespace());
        OBORestriction realtr = session.getObjectFactory().createOBORestriction(child, type, parent, false);
        realtr.setNamespace(ns);
        realtr.setNecessarilyTrue(tr.isNecessary());
        realtr.setInverseNecessarilyTrue(tr.isInverseNecessary());
        realtr.setCompletes(tr.completes());
        realtr.setMinCardinality(tr.getMinCardinality());
        realtr.setMaxCardinality(tr.getMaxCardinality());
        realtr.setCardinality(tr.getCardinality());
        return realtr;
    }

    public static Link getRealRel(OBOSession session, StringRelationship tr) {
        if (tr == null) return null;
        LinkedObject child = (LinkedObject) session.getObject(tr.getChild());
        OBOProperty type = (OBOProperty) session.getObject(tr.getType());
        LinkedObject parent = (LinkedObject) session.getObject(tr.getParent());
        if (child == null) return null;
        for (Link link : child.getParents()) {
            if (link.getType().equals(type) && link.getParent().equals(parent)) {
                return link;
            }
        }
        return null;
    }

    /**
	 * Returns the {@link TermSubset} with the given name in the given
	 * {@link OBOSession}, or null if no such category exists.
	 */
    public static TermSubset findCategory(String name, OBOSession session) {
        for (TermSubset cat : session.getSubsets()) {
            if (cat.getName().equals(name)) return cat;
        }
        return null;
    }

    /**
	 * Finds the {@link TermSubset} in the given {@link OBOSession} that has
	 * the same name as the given {@link TermSubset}. If no matching category
	 * can be found, null is returned.
	 */
    public static TermSubset findCategory(TermSubset cat, OBOSession session) {
        return findCategory(cat.getName(), session);
    }

    /**
	 * given a link tr, and an object t, find the child link of t that matches tr
	 * 
	 * if t is null return tr
	 * 
	 * This presumably is here to prevent the duplication of link objects..? [CJM]
	 * @param tr
	 * @param t
	 * @return
	 */
    public static Link findChildRel(Link tr, LinkedObject t) {
        if (t == null) if (tr.getParent() == null) return tr; else {
            return null;
        }
        for (Link ptr : t.getChildren()) {
            if (TermUtil.equals(ptr, tr)) return ptr;
        }
        return null;
    }

    /**
	 * As for findChildRel, but ignore the intersection status of the link
	 * 
	 * Why do this? Consider deprecating -- CJM
	 * @param tr
	 * @param t
	 * @return
	 */
    @Deprecated
    public static Link findChildRelNoIntersection(Link tr, LinkedObject t) {
        if (t == null) if (tr.getParent() == null) return tr; else return null;
        for (Link ptr : t.getChildren()) {
            if (TermUtil.equalsWithoutIntersection(ptr, tr)) return ptr;
        }
        return null;
    }

    public static Link findParentRel(Link tr, LinkedObject t) {
        for (Link ctr : t.getParents()) {
            if (TermUtil.equals(ctr, tr)) return ctr;
        }
        return null;
    }

    @Deprecated
    public static Link findParentRelNoIntersection(Link tr, LinkedObject t) {
        for (Link ctr : t.getParents()) {
            if (TermUtil.equalsWithoutIntersection(ctr, tr)) return ctr;
        }
        return null;
    }

    public static Namespace findNamespace(Namespace ns, OBOSession session) {
        return HistoryUtil.findNamespace(ns.getID(), session);
    }

    public static Namespace findNamespace(String nsid, OBOSession session) {
        for (Namespace ns : session.getNamespaces()) {
            if (ns.getID().equals(nsid)) return ns;
        }
        return null;
    }

    public static Synonym findSynonym(SynonymedObject t, String stext) {
        for (Synonym s : t.getSynonyms()) {
            if (s.getText().equals(stext)) return s;
        }
        return null;
    }

    public static Synonym findSynonym(SynonymedObject t, Synonym s) {
        return findSynonym(t, s.getText());
    }

    public static SynonymType findSynonymCategory(String cat_id, OBOSession session) {
        for (SynonymType cat : session.getSynonymTypes()) {
            if (cat.getID().equals(cat_id)) return cat;
        }
        return null;
    }

    public static SynonymType findSynonymCategory(SynonymType cat, OBOSession session) {
        return findSynonymCategory(cat.getID(), session);
    }

    public static boolean hasChild(LinkedObject t, Link tr) {
        if (tr.getType() == null) return false;
        for (Link tra : t.getChildren()) {
            if (tr.getChild().equals(tra.getChild()) && tr.getType().equals(tra.getType())) {
                if ((tr instanceof OBORestriction) && (tra instanceof OBORestriction)) {
                    return ((OBORestriction) tr).getCompletes() == ((OBORestriction) tra).getCompletes();
                }
                return true;
            }
        }
        return false;
    }
}
