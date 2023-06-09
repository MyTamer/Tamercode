package net.sourceforge.ondex.xten.functions;

import static net.sourceforge.ondex.xten.functions.ControledVocabularyHelper.convertConceptClasses;
import static net.sourceforge.ondex.xten.functions.ControledVocabularyHelper.convertRelationTypes;
import static net.sourceforge.ondex.xten.functions.ControledVocabularyHelper.*;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptAccession;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.GDS;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXEntity;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXGraphMetaData;
import net.sourceforge.ondex.core.ONDEXIterator;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.base.ONDEXViewImpl;
import net.sourceforge.ondex.core.util.ONDEXBitSet;
import net.sourceforge.ondex.core.util.ONDEXViewFunctions;
import net.sourceforge.ondex.core.util.SparseBitSet;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.NullValueException;

/**
 * Useful reusable functions
 * @author lysenkoa
 *
 */
public class StandardFunctions {

    public static void changeRelationDirection(ONDEXGraph graph, String originaRT, String newRT) {
        ONDEXGraphMetaData meta = graph.getMetaData();
        RelationType original = meta.getRelationType(originaRT);
        String invName = original.getInverseName();
        if (invName == null) {
            invName = newRT;
        } else {
            System.err.println("Inverse type found: " + invName);
        }
        RelationType inverse = createRT(graph, invName);
    }

    /**
	 * Converts a collection of concepts to an array of their ids
	 * @param concepts
	 * @return an array of integer ids
	 */
    public static Integer[] entitesToIds(Collection<ONDEXConcept> concepts) {
        Integer[] result = new Integer[concepts.size()];
        int i = 0;
        for (ONDEXEntity c : concepts) {
            result[i] = c.getId();
            i++;
        }
        return result;
    }

    /**
	 * Searches in a graph for concepts with accessions matching a regex
	 * 
	 * @author hindlem
	 * 
	 * @param regex a valid Java regex (a pattern.matcher($accession).matches() is done)
	 * @param graph the graph to search concepts in 
	 * @param exclusive if true then return all concepts (from the whole graph) that don't have matching accessions, else return all matching concepts
	 * @param cc the concept class of concepts to search in (can be null)
	 * @param concept_cv the cv of concepts to search in (can be null)
	 * @param accession_cv the cv of accessions to seach in (can be null)
	 */
    public static final ONDEXView<ONDEXConcept> filterConceptsOnAcessionRegex(String regex, ONDEXGraph graph, boolean exclusive, ConceptClass cc, CV concept_cv, CV accession_cv) {
        SparseBitSet conceptsFound = new SparseBitSet();
        Pattern pattern = Pattern.compile(regex);
        ONDEXView<ONDEXConcept> concepts = graph.getConcepts();
        if (cc != null) {
            ONDEXViewFunctions.and(concepts, graph.getConceptsOfConceptClass(cc));
        }
        if (concept_cv != null) {
            ONDEXViewFunctions.and(concepts, graph.getConceptsOfCV(concept_cv));
        }
        while (concepts.hasNext()) {
            ONDEXConcept concept = concepts.next();
            ONDEXIterator<ConceptAccession> accessions = concept.getConceptAccessions();
            while (accessions.hasNext()) {
                ConceptAccession accession = accessions.next();
                if (accession_cv != null && !accession.getElementOf().equals(accession_cv)) {
                    continue;
                }
                if (pattern.matcher(accession.getAccession()).matches()) {
                    conceptsFound.set(concept.getId());
                    break;
                }
            }
        }
        concepts.close();
        ONDEXView<ONDEXConcept> matchedConcepts = new ONDEXViewImpl<ONDEXConcept>(graph, ONDEXConcept.class, conceptsFound);
        if (exclusive) return ONDEXViewFunctions.andNot(graph.getConcepts(), matchedConcepts); else return matchedConcepts;
    }

    public static ONDEXView<ONDEXConcept> filteroutUnconnected(ONDEXGraph graph, ONDEXView<ONDEXConcept> setOfConcepts, ONDEXView<ONDEXRelation> validRelations) {
        SparseBitSet resultSet = new SparseBitSet();
        ONDEXView<ONDEXConcept> setOfConceptsClone = setOfConcepts.clone();
        while (setOfConceptsClone.hasNext()) {
            ONDEXConcept c = setOfConceptsClone.next();
            if (ONDEXViewFunctions.and(graph.getRelationsOfConcept(c), validRelations).size() > 0) resultSet.set(c.getId());
        }
        ONDEXView<ONDEXConcept> result = new ONDEXViewImpl<ONDEXConcept>(graph, ONDEXConcept.class, resultSet);
        setOfConceptsClone.close();
        return result;
    }

    /**
	 * Counts the gds ranking for a given graph
	 * @param graph
	 * @param concepts
	 * @param attributeName
	 * @return
	 */
    public static final SortedMap<Integer, Object> gdsRanking(ONDEXGraph graph, ONDEXView<ONDEXConcept> concepts, String attributeName) {
        AttributeName att = graph.getMetaData().getAttributeName(attributeName);
        if (att == null) return new TreeMap<Integer, Object>();
        Map<Object, Integer> counts = new HashMap<Object, Integer>();
        while (concepts.hasNext()) {
            GDS<ONDEXConcept> gds = concepts.next().getConceptGDS(att);
            if (gds != null) {
                Integer count = counts.get(gds.getValue());
                if (count == null) count = 0;
                counts.put(gds.getValue(), ++count);
            }
        }
        concepts.close();
        SortedMap<Integer, Object> sorted = new TreeMap<Integer, Object>(new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) return 1;
                if (o1 < o2) return -1;
                return 0;
            }
        });
        for (Entry<Object, Integer> ent : counts.entrySet()) {
            sorted.put(ent.getValue(), ent.getKey());
        }
        counts = null;
        return sorted;
    }

    /**
	 * Gets the list of all accessions of a particular cv from a concpet
	 * @param c - concept with accessions
	 * @param type - type of accesions to extract
	 * @return
	 */
    public static final List<String> getAccessionsOfType(ONDEXConcept c, CV type) {
        ONDEXIterator<ConceptAccession> ca = c.getConceptAccessions();
        List<String> accs = new LinkedList<String>();
        while (ca.hasNext()) {
            ConceptAccession a = ca.next();
            if (a.getElementOf().equals(type)) {
                accs.add(a.getAccession());
            }
        }
        ca.close();
        return accs;
    }

    /**
	 * Gets all connected concepts and relations that are linked to the seed concept via valid relations
	 * @param seed
	 * @param graph
	 * @param setOfTraversableRelations
	 * @return
	 */
    public static SparseBitSet[] getAllConnected(ONDEXConcept seed, ONDEXGraph graph, ONDEXView<ONDEXRelation> setOfTraversableRelations) {
        SparseBitSet[] result = new SparseBitSet[2];
        result[0] = new SparseBitSet();
        result[0].set(seed.getId());
        result[1] = new SparseBitSet();
        SparseBitSet toProcess = new SparseBitSet();
        toProcess.set(seed.getId());
        while (toProcess.countSetBits() > 0) {
            SparseBitSet toProcessNext = new SparseBitSet();
            for (int i : toProcess) {
                SparseBitSet[] temp = getNeighbours(i, graph, setOfTraversableRelations);
                temp[0].andNot(result[0]);
                temp[1].andNot(result[1]);
                result[0].or(temp[0]);
                result[1].or(temp[1]);
                toProcessNext.or(temp[0]);
            }
            toProcess = toProcessNext;
        }
        return result;
    }

    /**
	 * @author taubertj
	 * Calculates betweenness centrality on the ONDEX graph.
	 * Result returned as a map of concept to its betweennes centrality value.
	 * @return Object2DoubleOpenHashMap<ONDEXConcept>
	 * @throws AccessDeniedException
	 * @throws NullValueException
	 */
    public static final Object2DoubleOpenHashMap<ONDEXConcept> getBetweenessCentrality(ONDEXGraph aog) throws NullValueException, AccessDeniedException {
        return getBetweenessCentrality(aog.getConcepts(), aog.getRelations(), aog);
    }

    /**
	 * @author taubertj
	 * Calculates betweenness centrality on the ONDEX graph.
	 * Result returned as a map of concept to its betweennes centrality value.
	 * @return Object2DoubleOpenHashMap<ONDEXConcept>
	 * @throws AccessDeniedException
	 * @throws NullValueException
	 */
    public static final Object2DoubleOpenHashMap<ONDEXConcept> getBetweenessCentrality(ONDEXView<ONDEXConcept> itc, ONDEXView<ONDEXRelation> incomingr, ONDEXGraph aog) throws NullValueException, AccessDeniedException {
        boolean filter = incomingr.size() != aog.getRelations().size();
        Object2DoubleOpenHashMap<ONDEXConcept> CB = new Object2DoubleOpenHashMap<ONDEXConcept>();
        CB.defaultReturnValue(0.0);
        while (itc.hasNext()) {
            ONDEXConcept concept = itc.next();
            Stack<ONDEXConcept> S = new Stack<ONDEXConcept>();
            Map<ONDEXConcept, List<ONDEXConcept>> P = new Hashtable<ONDEXConcept, List<ONDEXConcept>>();
            Object2IntOpenHashMap<ONDEXConcept> rho = new Object2IntOpenHashMap<ONDEXConcept>();
            rho.defaultReturnValue(0);
            rho.put(concept, 1);
            Object2IntOpenHashMap<ONDEXConcept> d = new Object2IntOpenHashMap<ONDEXConcept>();
            d.defaultReturnValue(-1);
            d.put(concept, 0);
            LinkedBlockingQueue<ONDEXConcept> Q = new LinkedBlockingQueue<ONDEXConcept>();
            Q.offer(concept);
            while (!Q.isEmpty()) {
                ONDEXConcept v = Q.poll();
                S.push(v);
                ONDEXView<ONDEXRelation> itr = aog.getRelationsOfConcept(v);
                if (filter) itr = ONDEXViewFunctions.and(itr, incomingr);
                while (itr.hasNext()) {
                    ONDEXRelation r = itr.next();
                    ONDEXConcept from = r.getFromConcept();
                    ONDEXConcept to = r.getToConcept();
                    ONDEXConcept w = null;
                    if (!from.equals(v)) w = from; else if (!to.equals(v)) w = to; else continue;
                    if (d.getInt(w) < 0) {
                        Q.offer(w);
                        d.put(w, d.getInt(v) + 1);
                    }
                    if (d.getInt(w) == d.getInt(v) + 1) {
                        rho.put(w, rho.getInt(w) + rho.getInt(v));
                        if (!P.containsKey(w)) P.put(w, new ArrayList<ONDEXConcept>());
                        P.get(w).add(v);
                    }
                }
                itr.close();
            }
            Object2DoubleOpenHashMap<ONDEXConcept> delta = new Object2DoubleOpenHashMap<ONDEXConcept>();
            delta.defaultReturnValue(0.0);
            while (!S.isEmpty()) {
                ONDEXConcept w = S.pop();
                if (P.containsKey(w)) {
                    for (ONDEXConcept v : P.get(w)) {
                        delta.put(v, delta.getDouble(v) + ((double) rho.getInt(v) / (double) rho.getInt(w)) * (1.0 + delta.getDouble(w)));
                    }
                }
                if (!w.equals(concept)) {
                    CB.put(w, CB.getDouble(w) + delta.getDouble(w));
                }
            }
        }
        itc.close();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        ONDEXConcept[] keys = CB.keySet().toArray(new ONDEXConcept[0]);
        for (int i = 0; i < keys.length; i++) {
            double value = CB.getDouble(keys[i]);
            if (value < min) min = value;
            if (value > max) max = value;
        }
        double diff = max - min;
        for (int i = 0; i < keys.length; i++) {
            CB.put(keys[i], (CB.getDouble(keys[i]) - min) / diff);
        }
        return CB;
    }

    /**
	 * Construct a view of the concepts that have concept class with on of the supplied ids.
	 * @param ids - string concept class ids
	 * @return ondex view of concepts
	 */
    public static ONDEXView<ONDEXConcept> getConceptsByClassId(ONDEXGraph graph, String... ids) {
        ONDEXView<ONDEXConcept> result = new ONDEXViewImpl<ONDEXConcept>(graph, ONDEXConcept.class, new SparseBitSet());
        if (ids == null) return result;
        for (int i = 0; i < ids.length; i++) {
            result = ONDEXViewFunctions.or(result, graph.getConceptsOfConceptClass(createCC(graph, ids[i])));
        }
        return result;
    }

    /**
	 * This method will return all concept classes in a given OndexView. This method is non-destructive
	 * and will operate on a copy of a view, so it can be reused afterwards, but also must be closed,
	 * if no longer needed. 
	 * 
	 * @return - a set of concept classes.
	 */
    public static final Set<ConceptClass> getContainedConceptClasses(ONDEXView<ONDEXConcept> concepts) {
        Set<ConceptClass> result = new HashSet<ConceptClass>();
        ONDEXView<ONDEXConcept> copy = concepts.clone();
        while (copy.hasNext()) {
            result.add(copy.next().getOfType());
        }
        copy.close();
        return result;
    }

    /**
	 * Calculates degree centrality on the ONDEX graph.
	 * Result returned as a map of concept to its degree centrality value.
	 * @return Object2DoubleOpenHashMap<ONDEXConcept>
	 * @throws AccessDeniedException
	 * @throws NullValueException
	 */
    public static final Object2DoubleOpenHashMap<ONDEXConcept> getDegreeCentrality(ONDEXGraph aog) throws NullValueException, AccessDeniedException {
        return getDegreeCentrality(aog.getConcepts(), aog.getRelations(), aog);
    }

    /**
	 * Calculates degree centrality on the ONDEX graph.
	 * Result returned as a map of concept to its degree centrality value.
	 * @return Object2DoubleOpenHashMap<ONDEXConcept>
	 * @throws AccessDeniedException
	 * @throws NullValueException
	 */
    public static final Object2DoubleOpenHashMap<ONDEXConcept> getDegreeCentrality(ONDEXView<ONDEXConcept> itc, ONDEXView<ONDEXRelation> itr, ONDEXGraph aog) throws NullValueException, AccessDeniedException {
        Object2DoubleOpenHashMap<ONDEXConcept> conceptToDegree = new Object2DoubleOpenHashMap<ONDEXConcept>();
        double normalisationFactor = itc.size() - 1;
        while (itc.hasNext()) {
            ONDEXConcept c = itc.next();
            if (itr.size() == aog.getRelations().size()) conceptToDegree.put(c, aog.getRelationsOfConcept(c).size() / normalisationFactor); else conceptToDegree.put(c, ONDEXViewFunctions.and(aog.getRelationsOfConcept(c), itr).size() / normalisationFactor);
        }
        itc.close();
        itr.close();
        return conceptToDegree;
    }

    /**
	 * Returns all incoming relations of concept
	 * @param graph - graph
	 * @param c - concept
	 * @param exclude - ignore retalions of these types
	 * @return - set of relations
	 */
    public static final Set<ONDEXRelation> getIncomingRelations(ONDEXGraph graph, ONDEXConcept c, RelationType... exclude) {
        ONDEXIterator<ONDEXRelation> it = graph.getRelationsOfConcept(c);
        Set<RelationType> toExclude = new HashSet<RelationType>(Arrays.asList(exclude));
        Set<ONDEXRelation> result = new HashSet<ONDEXRelation>();
        while (it.hasNext()) {
            ONDEXRelation r = it.next();
            if (r.getToConcept().equals(c) && !toExclude.contains(r.getOfType())) result.add(r);
        }
        it.close();
        return result;
    }

    /**
	 * Returns all incoming relations of conceptc that connect it to one of the concepts of selected concept class(es)
	 * @param graph - graph
	 * @param c - concept
	 * @return - set of relations
	 */
    public static final Set<ONDEXRelation> getIncomingRelationsToConceptClass(ONDEXGraph graph, ONDEXConcept c, ConceptClass... ccs) {
        ONDEXIterator<ONDEXRelation> it = graph.getRelationsOfConcept(c);
        Set<ONDEXRelation> result = new HashSet<ONDEXRelation>();
        Set<ConceptClass> test = new HashSet<ConceptClass>(Arrays.asList(ccs));
        while (it.hasNext()) {
            ONDEXRelation r = it.next();
            if (r.getToConcept().equals(c) && (test.size() == 0 || test.contains(r.getToConcept().getOfType()))) result.add(r);
        }
        it.close();
        return result;
    }

    /**
	 * Reports intersections of the two sets:
	 * result[0] - unique entries in set one
	 * result[1] - intersection of one and two
	 * result[2] - unique entries in set two
	 * @param one
	 * @param two
	 * @return
	 */
    public static final ONDEXBitSet[] getIntersection(ONDEXBitSet one, ONDEXBitSet two) {
        ONDEXBitSet[] result = new SparseBitSet[3];
        result[0] = (ONDEXBitSet) one.clone();
        result[0].andNot(two);
        result[1] = (ONDEXBitSet) one.clone();
        result[1].and(two);
        result[2] = (ONDEXBitSet) two.clone();
        result[2].andNot(one);
        return result;
    }

    /**
	 * Returns a neighbourhood of the node. Relations are included in the subgraph if
	 * if they are of the matching relation type. Concepts are included if they are of matching 
	 * concept class AND are connected via a relation of a matching relation type.
	 * 
	 * If the relation types or concept classes supplied are null, all concept classes
	 * and relation types are matched.
	 * 
	 * If either array is empty, the match will be set to match nothing; this option
	 * could be used to get just the relations, but not concepts.
	 * 
	 * The seed concept is not included in the match.
	 * 
	 * @param graph - graph to do the analysis in
	 * @param c - seed concept
	 * @param rts - relation types to match
	 * @param ccs - concept classes to match
	 * @return - matching first level neighbourhood as a subgraph
	 */
    public static Subgraph getNeighbourhood(ONDEXGraph graph, ONDEXConcept c, RelationType[] rts, ConceptClass[] ccs) {
        Subgraph result = new Subgraph(graph);
        Set<RelationType> restrictionRT = new HashSet<RelationType>();
        boolean performRTCheck = false;
        if (rts != null) {
            performRTCheck = true;
            restrictionRT.addAll(Arrays.asList(rts));
        }
        Set<ConceptClass> restrictionCC = new HashSet<ConceptClass>();
        boolean performCCCheck = false;
        if (ccs != null) {
            performCCCheck = true;
            restrictionCC.addAll(Arrays.asList(ccs));
        }
        ONDEXView<ONDEXRelation> rs = graph.getRelationsOfConcept(c);
        while (rs.hasNext()) {
            ONDEXRelation r = rs.next();
            if (!performRTCheck || restrictionRT.contains(r.getOfType())) {
                result.addRelation(r);
                ONDEXConcept other = getOtherNode(c, r);
                if (!performCCCheck || restrictionCC.contains(other.getOfType())) {
                    result.addConcept(other);
                }
            }
        }
        rs.close();
        return result;
    }

    /**
	 * Returns a bitset of neighbouring concepts at position 0 and all relations at position 1
	 * @param seed
	 * @param graph
	 * @return
	 */
    public static SparseBitSet[] getNeighbours(int conceptID, ONDEXGraph graph) {
        return getNeighbours(graph.getConcept(conceptID), graph);
    }

    public static SparseBitSet[] getNeighbours(int conceptID, ONDEXGraph graph, ONDEXView<ONDEXRelation> validRelations) {
        return getNeighbours(graph.getConcept(conceptID), graph, validRelations);
    }

    /**
	 * Returns an array of two bitset of neighbouring concepts at array position 0 and all relations at array position 1
	 * @param seed
	 * @param graph
	 * @return
	 */
    public static SparseBitSet[] getNeighbours(ONDEXConcept seed, ONDEXGraph graph) {
        SparseBitSet[] result = new SparseBitSet[2];
        result[0] = new SparseBitSet();
        result[1] = new SparseBitSet();
        ONDEXView<ONDEXRelation> rel = graph.getRelationsOfConcept(seed);
        while (rel.hasNext()) {
            ONDEXRelation r = rel.next();
            result[0].set(StandardFunctions.getOtherNode(seed, r).getId());
            result[1].set(r.getId());
        }
        rel.close();
        return result;
    }

    /**
	 * Return all neighbours for a seed concept
	 * @param seed
	 * @param graph
	 * @param validRelations
	 * @return
	 */
    public static SparseBitSet[] getNeighbours(ONDEXConcept seed, ONDEXGraph graph, ONDEXView<ONDEXRelation> validRelations) {
        SparseBitSet[] result = new SparseBitSet[2];
        result[0] = new SparseBitSet();
        result[1] = new SparseBitSet();
        ONDEXView<ONDEXRelation> rel = graph.getRelationsOfConcept(seed);
        while (rel.hasNext()) {
            ONDEXRelation r = rel.next();
            if (!validRelations.contains(r)) {
                continue;
            }
            result[0].set(StandardFunctions.getOtherNode(seed, r).getId());
            result[1].set(r.getId());
        }
        rel.close();
        return result;
    }

    /**
	 * Returns a bitset of neighbouring concepts at position 0 and all relations at position 1
	 * @param seed
	 * @param graph
	 * @return
	 */
    public static SparseBitSet[] getNeighboursAtLevel(ONDEXConcept seed, ONDEXGraph graph, int level) {
        SparseBitSet[] result = new SparseBitSet[2];
        result[0] = new SparseBitSet();
        result[1] = new SparseBitSet();
        SparseBitSet toProcess = new SparseBitSet();
        toProcess.set(seed.getId());
        while (level > 0) {
            SparseBitSet toProcessNext = new SparseBitSet();
            for (int i : toProcess) {
                SparseBitSet[] temp = getNeighbours(i, graph);
                temp[0].andNot(result[0]);
                temp[1].andNot(result[1]);
                result[0].or(temp[0]);
                result[1].or(temp[1]);
                toProcessNext.or(temp[0]);
                if (toProcessNext.countSetBits() == 0) ;
                break;
            }
            toProcess = toProcessNext;
            level--;
        }
        return result;
    }

    /**
	 * Returns a bitset of neighbouring concepts at position 0 and all relations at position 1
	 * @param seed
	 * @param graph
	 * @return
	 */
    public static SparseBitSet[] getNeighboursAtLevel(ONDEXConcept seed, ONDEXGraph graph, ONDEXView<ONDEXRelation> setOfTraversableRelations, int level) {
        SparseBitSet[] result = new SparseBitSet[2];
        result[0] = new SparseBitSet();
        result[1] = new SparseBitSet();
        SparseBitSet toProcess = new SparseBitSet();
        toProcess.set(seed.getId());
        while (level > 0) {
            SparseBitSet toProcessNext = new SparseBitSet();
            for (int i : toProcess) {
                SparseBitSet[] temp = getNeighbours(i, graph, setOfTraversableRelations);
                result[0].or(temp[0]);
                result[1].or(temp[1]);
                toProcessNext.or(temp[0]);
            }
            if (toProcessNext.countSetBits() == 0) break;
            toProcess = toProcessNext;
            level--;
        }
        return result;
    }

    /**
	 * Returns one or more concepts of specified concept class(s), that are associated
	 * with this relations 
	 * 
	 * @param r - relation
	 * @param ccs - concept classes
	 * @param ignoreQualifier - if true, do not include the qualifiers in
	 * the result
	 * 
	 * @return - an array of concepts that satisfy this condition
	 */
    public static ONDEXConcept[] getNodesByConceptClass(ONDEXRelation r, Collection<ConceptClass> ccs, boolean ignoreQualifier) {
        Set<ONDEXConcept> set = new HashSet<ONDEXConcept>();
        ONDEXConcept c = r.getFromConcept();
        if (ccs.contains(c.getOfType())) set.add(c);
        c = r.getToConcept();
        if (ccs.contains(c.getOfType())) set.add(c);
        if (!ignoreQualifier) {
            c = r.getQualifier();
            if (c != null && ccs.contains(c.getOfType())) set.add(c);
        }
        return set.toArray(new ONDEXConcept[set.size()]);
    }

    /**
	 * Returns a concept on the opposite end of the relation to the one specified.
	 * @param s - session
	 * @param c - concept on the opposite side of the relation to the one needed
	 * @param r - relation connecting them
	 * @return concept on the other end of the specified relation
	 */
    public static ONDEXConcept getOtherNode(ONDEXConcept c, ONDEXRelation r) {
        ONDEXConcept to = r.getToConcept();
        if (c.equals(to)) return r.getFromConcept();
        return to;
    }

    /**
	 * Returns all concepts that are directly connected to the one specified
	 * Optional: provide one or more relation types as a restriction. Only the nodes that are connected by the relations
	 * of the following types will be returned.
	 * @param s - session
	 * @param graph - graph
	 * @param c - seed concept
	 * @return - set of concepts directly connected to the seed concept
	 */
    public static Set<ONDEXConcept> getOtherNodes(ONDEXGraph graph, ONDEXConcept c, RelationType... types) {
        Set<RelationType> restriction = new HashSet<RelationType>();
        boolean performCheck = false;
        if (types != null && types.length > 0) {
            performCheck = true;
            restriction.addAll(Arrays.asList(types));
        }
        ONDEXView<ONDEXRelation> rs = graph.getRelationsOfConcept(c);
        Set<ONDEXConcept> result = new HashSet<ONDEXConcept>();
        while (rs.hasNext()) {
            ONDEXRelation r = rs.next();
            if (!performCheck || restriction.contains(r.getOfType())) {
                result.add(getOtherNode(c, r));
            }
        }
        rs.close();
        return result;
    }

    /**
	 * Returns all outgoing relations of concept
	 * @param graph - graph
	 * @param c - concept
	 * @param exclude - ignore retalions of these types
	 * @return - set of relations
	 */
    public static final Set<ONDEXRelation> getOutgoingRelations(ONDEXGraph graph, ONDEXConcept c, RelationType... exclude) {
        ONDEXIterator<ONDEXRelation> it = graph.getRelationsOfConcept(c);
        Set<RelationType> toExclude = new HashSet<RelationType>(Arrays.asList(exclude));
        Set<ONDEXRelation> result = new HashSet<ONDEXRelation>();
        while (it.hasNext()) {
            ONDEXRelation r = it.next();
            if (r.getFromConcept().equals(c) && !toExclude.contains(r.getOfType())) result.add(r);
        }
        it.close();
        return result;
    }

    /**
	 * Returns all outgoing relations of concept c that connect it to one of the concepts of selected concept class(es)
	 * @param graph - graph
	 * @param c - concept
	 * @return - set of relations
	 */
    public static final Set<ONDEXRelation> getOutgoingRelationsToConceptClass(ONDEXGraph graph, ONDEXConcept c, ConceptClass... ccs) {
        ONDEXIterator<ONDEXRelation> it = graph.getRelationsOfConcept(c);
        Set<ONDEXRelation> result = new HashSet<ONDEXRelation>();
        Set<ConceptClass> test = new HashSet<ConceptClass>(Arrays.asList(ccs));
        while (it.hasNext()) {
            ONDEXRelation r = it.next();
            if (r.getFromConcept().equals(c) && (test.size() == 0 || test.contains(r.getToConcept().getOfType()))) result.add(r);
        }
        it.close();
        return result;
    }

    /**
	 * Constructs the view containing all of the relations of particular 
	 * types, specified by their string ids.
	 * @param graph - graph to operate on 
	 * @param types - string ids of the relation types that will be used to 
	 * determine which relation  to include in the view.
	 * 
	 * @return - resulting view
	 */
    public static ONDEXView<ONDEXRelation> getRelationsByTypes(ONDEXGraph graph, List<String> types) {
        ONDEXView<ONDEXRelation> result = new ONDEXViewImpl<ONDEXRelation>(graph, ONDEXRelation.class, new SparseBitSet());
        ONDEXGraphMetaData meta = graph.getMetaData();
        for (String type : types) {
            RelationType rt = meta.getRelationType(type);
            if (rt != null) {
                result = ONDEXViewFunctions.or(result, graph.getRelationsOfRelationType(rt));
            }
        }
        return result;
    }

    /**
	 * Returns a list of subgraphs that match the pattern of concept classes/relation types
	 * supplied as arguments. To work correctly the set of arguments must always start with an array of string ids
	 * for concept classes. Every concept that matches the concept classes in the first array will become a seed
	 * for a subgraph returned in the result. Graph entities may be members of multiple subgraphs, if they
	 * are part of more than one path.
	 * 
	 * @return - a list of subgraphs that answer the query.
	 * 
	 */
    public static List<Subgraph> getSubgraphMatch(ONDEXGraph graph, String[]... ccsRts) {
        List<Subgraph> result = new ArrayList<Subgraph>();
        if (ccsRts == null || ccsRts.length == 0) return result;
        ONDEXView<ONDEXConcept> seeds = getConceptsByClassId(graph, ccsRts[0]);
        while (seeds.hasNext()) {
            ONDEXConcept seed = seeds.next();
            Subgraph sg = new Subgraph(graph);
            result.add(sg);
            sg.addConcept(seed);
            Set<ONDEXConcept> candidates = new HashSet<ONDEXConcept>();
            candidates.add(seed);
            for (int i = 1; i < ccsRts.length; i = i + 2) {
                String[] relationTypes = ccsRts[i];
                String[] conceptClasses = null;
                if (i + 1 < ccsRts.length) {
                    conceptClasses = ccsRts[i + 1];
                }
                Set<ONDEXConcept> newCandidates = new HashSet<ONDEXConcept>();
                for (ONDEXConcept newSeed : candidates) {
                    Subgraph temp = getNeighbourhood(graph, newSeed, convertRelationTypes(graph, relationTypes), convertConceptClasses(graph, conceptClasses));
                    newCandidates.addAll(temp.getConcepts().toCollection());
                    sg.add(temp);
                }
                candidates = newCandidates;
            }
        }
        return result;
    }

    /**
	 * Checks the one of the collections for a presence of any of the elements from another collection
	 * @param toMatch - reference collection
	 * @param toCheck - collection that will be checked for the presence of matches
	 * @return true if an element was found , false otherwise
	 */
    public static final boolean hasMatch(Collection<?> toMatch, Collection<?> toCheck) {
        for (Object o : toCheck) {
            if (toMatch.contains(o)) return true;
        }
        return false;
    }

    /**
	 * StandardFunctions.nullChecker
	 * A convenience method to check groups of arguments to intercept null values
	 * in order to avoid annoying NullValue exceptions
	 * @param objs - values to check
	 * @return true if null found, false otherwise
	 */
    public static boolean nullChecker(Object... objs) {
        for (Object o : objs) {
            if (o == null) return true;
        }
        return false;
    }

    /**
	 * Return all source concepts for a given set of relations.
	 *  If the concept is bout a source an a target it will be omitted
	 * @param rs - set of relations
	 * @return -  set of source concepts
	 */
    public static final Set<ONDEXConcept> relationsToSources(Set<ONDEXRelation> rs) {
        Set<ONDEXConcept> result = new HashSet<ONDEXConcept>();
        for (ONDEXRelation r : rs) {
            ONDEXConcept from = r.getFromConcept();
            ONDEXConcept to = r.getToConcept();
            if (to != from) result.add(from);
        }
        return result;
    }

    /**
	 * Return all target concepts for a given set of relations.
	 * If the concept is bout a source an a target it will be omitted
	 * @param rs - set of relations
	 * @return -  set of target concepts
	 */
    public static final Set<ONDEXConcept> relationsToTargets(Set<ONDEXRelation> rs) {
        Set<ONDEXConcept> result = new HashSet<ONDEXConcept>();
        for (ONDEXRelation r : rs) {
            ONDEXConcept from = r.getFromConcept();
            ONDEXConcept to = r.getToConcept();
            if (to != from) result.add(to);
        }
        return result;
    }

    /**
	 * Gets the sparse bit set from the view. The view is used up and closed when this method is called.
	 * @param view ondex view
	 * @return sparse bit set of ids
	 */
    public static ONDEXBitSet viewToBitSet(ONDEXView<ONDEXEntity> view) {
        ONDEXBitSet set = new SparseBitSet();
        while (view.hasNext()) {
            set.set(view.next().getId());
        }
        view.close();
        return set;
    }

    private StandardFunctions() {
    }
}
