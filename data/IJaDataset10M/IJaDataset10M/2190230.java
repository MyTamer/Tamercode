package org.yawlfoundation.yawl.editor.analyser;

import org.yawlfoundation.yawl.elements.YNet;
import org.yawlfoundation.yawl.elements.YExternalNetElement;
import org.yawlfoundation.yawl.elements.YCondition;
import org.yawlfoundation.yawl.elements.YInputCondition;
import org.yawlfoundation.yawl.elements.YOutputCondition;
import org.yawlfoundation.yawl.elements.YTask;
import org.yawlfoundation.yawl.elements.YNetElement;
import org.yawlfoundation.yawl.elements.state.YMarking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;

/**
 *  A Reset net formalisation of a YAWL net.
 *
 **/
public final class ResetWFNet {

    private Map _Transitions = new HashMap(100);

    private Map _Places = new HashMap(100);

    private Map _YOJ = new HashMap();

    private Set alreadyConsideredMarkings = new HashSet(100);

    private Set _Conditions = new HashSet(100);

    private Set _Tasks = new HashSet(100);

    private RPlace inputPlace;

    private RPlace outputPlace;

    private String _ID;

    private RSetOfMarkings endMarkings = new RSetOfMarkings();

    private AnalysisDialog messageDlg;

    public void setAnalysisDialog(AnalysisDialog dlg) {
        messageDlg = dlg;
    }

    int maxNumMarkings = 5000;

    /**
     * Constructor for Reset net.
     *
     */
    public ResetWFNet(YNet yNet) {
        _ID = yNet.getID();
        ConvertToResetNet(yNet.getNetElements());
    }

    public ResetWFNet(ResetWFNet rNet) {
        _Transitions = new HashMap(rNet._Transitions);
        _Places = new HashMap(rNet._Places);
        _YOJ = new HashMap(rNet._YOJ);
        _ID = rNet.getID();
        inputPlace = (RPlace) _Places.get(rNet.inputPlace.getID());
        outputPlace = (RPlace) _Places.get(rNet.outputPlace.getID());
        _Conditions = new HashSet(rNet._Conditions);
        _Tasks = new HashSet(rNet._Tasks);
    }

    public String getID() {
        return _ID;
    }

    public Map getNetElements() {
        Map allElements = new HashMap();
        allElements.putAll(new HashMap(_Transitions));
        allElements.putAll(new HashMap(_Places));
        return allElements;
    }

    public static Set getPostset(Set elements) {
        Set postset = new HashSet();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            RElement ne = (RElement) iter.next();
            Set postElements = ne.getPostsetElements();
            if (!postElements.isEmpty()) {
                postset.addAll(postElements);
            }
        }
        return postset;
    }

    public static Set getPreset(Set elements) {
        Set preset = new HashSet();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            RElement ne = (RElement) iter.next();
            Set preElements = ne.getPresetElements();
            if (!preElements.isEmpty()) {
                preset.addAll(preElements);
            }
        }
        return preset;
    }

    public RPlace getInputPlace() {
        return inputPlace;
    }

    public RPlace getOutputPlace() {
        return outputPlace;
    }

    public void removeNetElement(RElement netElement) {
        Set preSet = netElement.getPresetElements();
        Iterator presetIter = preSet.iterator();
        while (presetIter.hasNext()) {
            RElement next = (RElement) presetIter.next();
            RFlow flow = new RFlow(next, netElement);
            next.removePostsetFlow(flow);
        }
        Set postSet = netElement.getPostsetElements();
        Iterator postsetIter = postSet.iterator();
        while (postsetIter.hasNext()) {
            RElement next = (RElement) postsetIter.next();
            RFlow flow = new RFlow(netElement, next);
            next.removePresetFlow(flow);
        }
        if (netElement instanceof RTransition) {
            _Transitions.remove(netElement.getID());
            RTransition t = (RTransition) netElement;
            Set cancelSet = t.getRemoveSet();
            if (!cancelSet.isEmpty()) {
                for (Iterator i = cancelSet.iterator(); i.hasNext(); ) {
                    RElement next = (RElement) i.next();
                    next.removeFromCancelledBySet(t);
                }
            }
        } else {
            _Places.remove(netElement.getID());
            Set cancelledBy = netElement.getCancelledBySet();
            if (!cancelledBy.isEmpty()) {
                for (Iterator i = cancelledBy.iterator(); i.hasNext(); ) {
                    RTransition t = (RTransition) i.next();
                    t.removeFromRemoveSet((RPlace) netElement);
                }
            }
        }
    }

    /**
     * The method converts a YAWL net into a Reset net.
     * If there are OR-joins, they are converted to XORs
     */
    private void ConvertToResetNet(Map netElements) {
        Iterator netEles = netElements.values().iterator();
        while (netEles.hasNext()) {
            YExternalNetElement nextElement = (YExternalNetElement) netEles.next();
            if (nextElement instanceof YCondition) {
                RPlace p = new RPlace(nextElement.getID());
                p.addToYawlMappings(nextElement);
                p.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                p.addToResetMappings(p);
                _Places.put(p.getID(), p);
                _Conditions.add(nextElement);
                if (nextElement instanceof YInputCondition) {
                    inputPlace = (RPlace) _Places.get(nextElement.getID());
                }
                if (nextElement instanceof YOutputCondition) {
                    outputPlace = (RPlace) _Places.get(nextElement.getID());
                }
            } else if (nextElement instanceof YTask) {
                RPlace p = new RPlace("p_" + nextElement.getID());
                p.addToYawlMappings(nextElement);
                p.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                p.addToResetMappings(p);
                _Places.put(p.getID(), p);
                _Tasks.add(nextElement);
            }
        }
        Map _StartTransitions = new HashMap();
        Map _EndTransitions = new HashMap();
        Iterator netEls = netElements.values().iterator();
        while (netEls.hasNext()) {
            YExternalNetElement next = (YExternalNetElement) netEls.next();
            if (next instanceof YTask) {
                YTask nextElement = (YTask) next;
                if (nextElement.getJoinType() == YTask._AND) {
                    RTransition t = new RTransition(nextElement.getID() + "_start");
                    t.addToYawlMappings(nextElement);
                    t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                    t.addToResetMappings(t);
                    _StartTransitions.put(t.getID(), t);
                    Set pre = nextElement.getPresetElements();
                    Iterator preEls = pre.iterator();
                    while (preEls.hasNext()) {
                        YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                        RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + nextElement.getID()));
                        t.setPostset(outflow);
                    }
                } else if (nextElement.getJoinType() == YTask._XOR) {
                    Set pre = nextElement.getPresetElements();
                    Iterator preEls = pre.iterator();
                    while (preEls.hasNext()) {
                        YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                        RTransition t = new RTransition(nextElement.getID() + "_start^" + preElement.getID());
                        t.addToYawlMappings(nextElement);
                        t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                        t.addToResetMappings(t);
                        _StartTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + nextElement.getID()));
                        t.setPostset(outflow);
                    }
                } else if (nextElement.getJoinType() == YTask._OR) {
                    Set pre = nextElement.getPresetElements();
                    Iterator preEls = pre.iterator();
                    while (preEls.hasNext()) {
                        YExternalNetElement preElement = (YExternalNetElement) preEls.next();
                        RTransition t = new RTransition(nextElement.getID() + "_start^" + preElement.getID());
                        t.addToYawlMappings(nextElement);
                        t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                        t.addToResetMappings(t);
                        _StartTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get(preElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get("p_" + nextElement.getID()));
                        t.setPostset(outflow);
                    }
                    _YOJ.put(nextElement.getID(), nextElement);
                }
                if (nextElement.getSplitType() == YTask._AND) {
                    RTransition t = new RTransition(nextElement.getID() + "_end");
                    t.addToYawlMappings(nextElement);
                    t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                    t.addToResetMappings(t);
                    _EndTransitions.put(t.getID(), t);
                    Set post = nextElement.getPostsetElements();
                    Iterator postEls = post.iterator();
                    while (postEls.hasNext()) {
                        YExternalNetElement postElement = (YExternalNetElement) postEls.next();
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                        t.setPostset(outflow);
                    }
                    Set removeSet = new HashSet(nextElement.getRemoveSet());
                    if (!removeSet.isEmpty()) {
                        addCancelSet(t, removeSet);
                    }
                } else if (nextElement.getSplitType() == YTask._XOR) {
                    Set post = nextElement.getPostsetElements();
                    Iterator postEls = post.iterator();
                    while (postEls.hasNext()) {
                        YExternalNetElement postElement = (YExternalNetElement) postEls.next();
                        RTransition t = new RTransition(nextElement.getID() + "_end^" + postElement.getID());
                        t.addToYawlMappings(nextElement);
                        t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                        t.addToResetMappings(t);
                        _EndTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                        t.setPostset(outflow);
                        Set removeSet = new HashSet(nextElement.getRemoveSet());
                        if (!removeSet.isEmpty()) {
                            addCancelSet(t, removeSet);
                        }
                    }
                } else if (nextElement.getSplitType() == YTask._OR) {
                    Set xSubSet = new HashSet();
                    Set post = nextElement.getPostsetElements();
                    for (int i = 1; i <= post.size(); i++) {
                        Set subSet = generateCombination(post, i);
                        xSubSet.addAll(subSet);
                    }
                    for (Iterator xSubSetEls = xSubSet.iterator(); xSubSetEls.hasNext(); ) {
                        Set x = (Set) xSubSetEls.next();
                        String t_id = "";
                        for (Iterator i = x.iterator(); i.hasNext(); ) {
                            YExternalNetElement postElement = (YExternalNetElement) i.next();
                            t_id += postElement.getID() + " ";
                        }
                        RTransition t = new RTransition(nextElement.getID() + "_end^{" + t_id + "}");
                        t.addToYawlMappings(nextElement);
                        t.addToYawlMappings(YAWLReachabilityUtils.convertToYawlMappings(nextElement));
                        t.addToResetMappings(t);
                        _EndTransitions.put(t.getID(), t);
                        RFlow inflow = new RFlow((RPlace) _Places.get("p_" + nextElement.getID()), t);
                        t.setPreset(inflow);
                        for (Iterator i = x.iterator(); i.hasNext(); ) {
                            YExternalNetElement postElement = (YExternalNetElement) i.next();
                            RFlow outflow = new RFlow(t, (RPlace) _Places.get(postElement.getID()));
                            t.setPostset(outflow);
                        }
                        Set removeSet = new HashSet(nextElement.getRemoveSet());
                        if (!removeSet.isEmpty()) {
                            addCancelSet(t, removeSet);
                        }
                    }
                }
            }
        }
        _Transitions.putAll(_StartTransitions);
        _Transitions.putAll(_EndTransitions);
    }

    /**
     * This method is used to generate combinations of markings for 
     * comparison. 
     */
    private Set generateCombination(Set netElements, int size) {
        Set subSets = new HashSet();
        Object[] elements = netElements.toArray();
        int[] indices;
        CombinationGenerator x = new CombinationGenerator(elements.length, size);
        while (x.hasMore()) {
            Set combsubSet = new HashSet();
            indices = x.getNext();
            for (int i = 0; i < indices.length; i++) {
                combsubSet.add(elements[indices[i]]);
            }
            subSets.add(combsubSet);
        }
        return subSets;
    }

    /**
     *This method is used to associate a cancellation set (RPlaces) with each RTransition.
     *This is the implementation of R.
     */
    private void addCancelSet(RTransition rt, Set removeSet) {
        Set removeSetT = new HashSet(removeSet);
        Set removeSetR = new HashSet();
        removeSet.retainAll(_Conditions);
        for (Iterator i = removeSet.iterator(); i.hasNext(); ) {
            YExternalNetElement c = (YExternalNetElement) i.next();
            RPlace p = (RPlace) _Places.get(c.getID());
            if (p != null) {
                removeSetR.add(p);
            }
        }
        removeSetT.removeAll(_Conditions);
        for (Iterator i = removeSetT.iterator(); i.hasNext(); ) {
            YExternalNetElement t = (YExternalNetElement) i.next();
            RPlace p = (RPlace) _Places.get("p_" + t.getID());
            if (p != null) {
                removeSetR.add(p);
            }
        }
        rt.setRemoveSet(removeSetR);
    }

    public String checkWeakSoundness() {
        String msg;
        boolean optionToComplete = true;
        boolean properCompletion = true;
        boolean noDeadTasks = true;
        optionToComplete = checkOptionToComplete();
        String optionMsg;
        if (optionToComplete) {
            if (containsORjoins()) {
                optionMsg = "The net " + _ID + " has one or more OR-join tasks and option to complete cannot be decided.";
            } else {
                optionMsg = "The net " + _ID + " has an option to complete.";
            }
        } else {
            optionMsg = "The net " + _ID + " does not have an option to complete.";
            optionToComplete = false;
        }
        msg = formatXMLMessage(optionMsg, optionToComplete);
        String deadTaskMsg = checkDeadTasks();
        if (deadTaskMsg.equals("")) {
            if (containsORjoins()) {
                deadTaskMsg = "The net " + _ID + " has one or more OR-join tasks and whether there are dead tasks cannot be decided.";
            } else {
                deadTaskMsg = "The net " + _ID + " has no dead tasks.";
            }
        } else {
            noDeadTasks = false;
            deadTaskMsg = "The net " + _ID + " has dead tasks:" + deadTaskMsg;
        }
        msg += formatXMLMessage(deadTaskMsg, noDeadTasks);
        if (noDeadTasks) {
            String properCompletionMsg = checkProperCompletion();
            if (properCompletionMsg.equals("")) {
                properCompletionMsg = "The net " + _ID + " has proper completion.";
            } else {
                if (containsORjoins()) {
                    properCompletionMsg = "The net " + _ID + " has one or more OR-join tasks and proper completion cannot be decided.";
                } else {
                    properCompletionMsg = "Tokens could be left in the following condition(s) when the net has completed:" + properCompletionMsg;
                    properCompletion = false;
                }
            }
            msg += formatXMLMessage(properCompletionMsg, properCompletion);
        }
        String smsg;
        boolean isWeakSound = true;
        if (containsORjoins()) {
            smsg = "The net " + _ID + " has one or more OR-join tasks and the weak soundness property cannot be decided.";
        } else {
            if (optionToComplete && properCompletion && noDeadTasks) {
                smsg = "The net " + _ID + " satisfies the weak soundness property.";
            } else {
                smsg = "The net " + _ID + " does not satisfy the weak soundness property.";
                isWeakSound = false;
            }
        }
        msg += formatXMLMessage(smsg, isWeakSound);
        return msg;
    }

    /**
    *  To check if a marking with one token in output condition is covered from  
    *  initial marking. 
    *
    */
    private boolean checkOptionToComplete() {
        boolean canComplete = false;
        if (inputPlace != null && outputPlace != null) {
            Integer tokenCount = new Integer(1);
            Map iMap = new HashMap();
            Map oMap = new HashMap();
            iMap.put(inputPlace.getID(), tokenCount);
            oMap.put(outputPlace.getID(), tokenCount);
            RMarking Mi = new RMarking(iMap);
            RMarking Mo = new RMarking(oMap);
            canComplete = Coverable(Mi, Mo);
        }
        return canComplete;
    }

    /**
    *  To check if there are dead tasks in the net. 
    *  Returns a list of dead tasks or an empty string.
    */
    private String checkDeadTasks() {
        Integer tokenCount = new Integer(1);
        Map iMap = new HashMap();
        Map pMap = new HashMap();
        iMap.put(inputPlace.getID(), tokenCount);
        RMarking Mi = new RMarking(iMap);
        RMarking Mp;
        String msg = "";
        HashSet DeadTasks = new HashSet();
        boolean fireableTask = true;
        for (Iterator i = _Tasks.iterator(); i.hasNext(); ) {
            YExternalNetElement t = (YExternalNetElement) i.next();
            String placeName = "p_" + t.getID();
            RElement e = findResetMapping(placeName);
            if (e != null && e instanceof RPlace) {
                RPlace p = (RPlace) e;
                pMap.put(p.getID(), tokenCount);
                Mp = new RMarking(pMap);
                fireableTask = Coverable(Mi, Mp);
                pMap.remove(p.getID());
            } else if (e != null && e instanceof RTransition) {
                Set preSet = e.getPresetElements();
                for (Iterator iterator = preSet.iterator(); iterator.hasNext(); ) {
                    RPlace prePlace = (RPlace) iterator.next();
                    pMap.put(prePlace.getID(), tokenCount);
                }
                Mp = new RMarking(pMap);
                fireableTask = Coverable(Mi, Mp);
                pMap.clear();
            }
            if (!fireableTask) {
                DeadTasks.addAll(convertToYawlMappingsForTasks(e));
            }
        }
        if (DeadTasks.size() > 0) {
            msg = DeadTasks.toString();
        }
        return msg;
    }

    public boolean containsORjoins() {
        return _YOJ.size() > 0;
    }

    /**
    *  To check if it is possible to have a token in the net, while there is 
    *  a token in the output condition. (Note: this alone cannot determine
    *  whether the net will complete or not).
    *
    */
    private String checkProperCompletion() {
        Integer tokenCount = new Integer(1);
        Map iMap = new HashMap();
        Map pMap = new HashMap();
        iMap.put(inputPlace.getID(), tokenCount);
        pMap.put(outputPlace.getID(), tokenCount);
        RMarking Mi = new RMarking(iMap);
        String msg = "";
        boolean isNotProper = false;
        for (Iterator i = _Places.values().iterator(); i.hasNext(); ) {
            RPlace p = (RPlace) i.next();
            if (p != inputPlace && p != outputPlace) {
                pMap.put(p.getID(), tokenCount);
                RMarking Mp = new RMarking(pMap);
                isNotProper = Coverable(Mi, Mp);
                if (isNotProper) {
                    msg += convertToYawlMappings(p).toString();
                }
                pMap.remove(p.getID());
            }
        }
        return msg;
    }

    /**
    *  To check if the cancellation sets are unnecessary.
    *
    */
    public String checkCancellationSets() {
        Integer tokenCount = new Integer(1);
        Map iMap = new HashMap();
        Map pMap = new HashMap();
        iMap.put(inputPlace.getID(), tokenCount);
        RMarking Mi = new RMarking(iMap);
        Set removeSet = new HashSet();
        ArrayList msgArray = new ArrayList();
        String msg = "";
        boolean tokenExists = false;
        for (Iterator i = _Transitions.values().iterator(); i.hasNext(); ) {
            RTransition t = (RTransition) i.next();
            if (t.isCancelTransition()) {
                Set preSet = t.getPresetElements();
                pMap.clear();
                for (Iterator pi = preSet.iterator(); pi.hasNext(); ) {
                    RPlace p = (RPlace) pi.next();
                    pMap.put(p.getID(), tokenCount);
                }
                removeSet = t.getRemoveSet();
                for (Iterator ri = removeSet.iterator(); ri.hasNext(); ) {
                    RPlace cp = (RPlace) ri.next();
                    if (preSet.contains(cp)) {
                        tokenCount = new Integer(2);
                    }
                    pMap.put(cp.getID(), tokenCount);
                    RMarking Mp = new RMarking(pMap);
                    tokenExists = Coverable(Mi, Mp);
                    if (!tokenExists) {
                        msgArray.add("Element(s)" + convertToYawlMappings(cp) + " should not be in the cancellation set of task(s) " + convertToYawlMappingsForTasks(t) + ".");
                    }
                    if (preSet.contains(cp)) {
                        tokenCount = new Integer(1);
                        pMap.put(cp.getID(), tokenCount);
                    } else {
                        pMap.remove(cp.getID());
                    }
                }
            }
        }
        if (msgArray.size() == 0) {
            msg = "The net " + _ID + " satisfies the irreducible cancellation regions property.";
            msg = formatXMLMessage(msg, true);
        } else {
            for (Iterator mi = msgArray.iterator(); mi.hasNext(); ) {
                String rawmessage = (String) mi.next();
                msg += formatXMLMessage(rawmessage, false);
            }
        }
        return msg;
    }

    public RMarking convertToRMarking(YMarking M) {
        Set MarkedTasks = new HashSet();
        Map RMap = new HashMap();
        LinkedList YLocations = new LinkedList(M.getLocations());
        for (Iterator i = YLocations.iterator(); i.hasNext(); ) {
            YNetElement nextElement = (YNetElement) i.next();
            if (nextElement instanceof YCondition) {
                YCondition condition = (YCondition) nextElement;
                RPlace place = (RPlace) _Places.get(condition.getID());
                if (place != null) {
                    String placename = place.getID();
                    Integer tokenCount = new Integer(1);
                    if (RMap.containsKey(placename)) {
                        Object value = RMap.get(placename);
                        Integer countString = new Integer(value.toString());
                        int count = countString.intValue();
                        count++;
                        tokenCount = new Integer(count);
                    }
                    RMap.put(placename, tokenCount);
                }
            }
            if (nextElement instanceof YTask) {
                MarkedTasks.add(nextElement);
            }
        }
        for (Iterator placeConvIter = MarkedTasks.iterator(); placeConvIter.hasNext(); ) {
            YTask task = (YTask) placeConvIter.next();
            String internalPlace = "p_" + task.getID();
            RPlace place = (RPlace) _Places.get(internalPlace);
            if (place != null) {
                String placename = place.getID();
                Integer tokenCount = new Integer(1);
                RMap.put(placename, tokenCount);
            }
        }
        return new RMarking(RMap);
    }

    /**
    * This method uses reachable markings to check soundness property.
    * Only applicable to YAWL nets without OR-joins
    *
    */
    public String checkSoundness() throws Exception {
        if (containsORjoins()) {
            return "This net has OR-joins. Soundness check not performed.";
        }
        endMarkings = new RSetOfMarkings();
        String msg = "";
        Integer tokenCount = new Integer(1);
        Map iMap = new HashMap();
        Map oMap = new HashMap();
        iMap.put(inputPlace.getID(), tokenCount);
        oMap.put(outputPlace.getID(), tokenCount);
        RMarking Mi = new RMarking(iMap);
        RMarking Mo = new RMarking(oMap);
        boolean optionToComplete = true;
        boolean properCompletion = true;
        boolean noDeadTasks = true;
        RSetOfMarkings RS = getReachableMarkings(Mi);
        String omsg = "";
        if (endMarkings.contains(Mo) && endMarkings.size() == 1) {
            omsg = "The net " + _ID + " has an option to complete.";
        } else {
            optionToComplete = false;
            if (!endMarkings.contains(Mo)) {
                omsg = " The final marking Mo is not reachable.";
            }
            String deadlockmsg = "";
            for (Iterator i = endMarkings.getMarkings().iterator(); i.hasNext(); ) {
                RMarking currentM = (RMarking) i.next();
                if (!currentM.isBiggerThanOrEqual(Mo)) {
                    deadlockmsg += printMarking(currentM) + " ";
                }
            }
            omsg = "The net " + _ID + " does not have an option to complete." + omsg;
            if (deadlockmsg != "") {
                omsg = omsg + " Potential deadlocks: " + deadlockmsg;
            }
        }
        msg += formatXMLMessage(omsg, optionToComplete);
        String pmsg = "";
        String deadlockmsg = "";
        for (Iterator i = RS.getMarkings().iterator(); i.hasNext(); ) {
            RMarking currentM = (RMarking) i.next();
            if (currentM.isBiggerThan(Mo)) {
                pmsg += printMarking(currentM) + " ";
                properCompletion = false;
            }
        }
        if (pmsg.equals("")) {
            pmsg = "The net " + _ID + " has proper completion.";
        } else {
            pmsg = "The net " + _ID + " does not have proper completion. Markings larger than Mo can be found: " + pmsg;
        }
        msg += formatXMLMessage(pmsg, properCompletion);
        String dmsg = "";
        Map cMap = new HashMap();
        HashSet DeadTasks = new HashSet();
        for (Iterator i = _Tasks.iterator(); i.hasNext(); ) {
            YTask t = (YTask) i.next();
            String internalPlace = "p_" + t.getID();
            RElement e = findResetMapping(internalPlace);
            if (e != null && e instanceof RPlace) {
                cMap.put(e.getID(), tokenCount);
                RMarking currentM = new RMarking(cMap);
                if (!RS.containsBiggerEqual(currentM)) {
                    DeadTasks.addAll(convertToYawlMappingsForTasks(e));
                    noDeadTasks = false;
                }
                cMap.remove(e.getID());
            } else if (e != null && e instanceof RTransition) {
                Set preSet = e.getPresetElements();
                for (Iterator iterator = preSet.iterator(); iterator.hasNext(); ) {
                    RPlace prePlace = (RPlace) iterator.next();
                    cMap.put(prePlace.getID(), tokenCount);
                }
                RMarking currentM = new RMarking(cMap);
                if (!RS.containsBiggerEqual(currentM)) {
                    DeadTasks.addAll(convertToYawlMappingsForTasks(e));
                    noDeadTasks = false;
                }
                cMap.clear();
            }
        }
        if (noDeadTasks) {
            dmsg = "The net " + _ID + " has no dead tasks.";
        } else {
            dmsg = "The net " + _ID + " has dead tasks:" + DeadTasks.toString();
        }
        msg += formatXMLMessage(dmsg, noDeadTasks);
        String smsg;
        boolean isSound = true;
        if (optionToComplete && properCompletion && noDeadTasks) {
            smsg = "The net " + _ID + " satisfies the soundness property.";
        } else {
            smsg = "The net " + _ID + " does not satisfy the soundness property.";
            isSound = false;
        }
        msg += formatXMLMessage(smsg, isSound);
        return msg;
    }

    /**
     * This method takes two markings s and t, and check whether t is reachable 
     * from s. Not used at the moment.
     */
    public boolean Reachable(RMarking s, RMarking t) throws Exception {
        RSetOfMarkings RS = getReachableMarkings(s);
        return RS.contains(t);
    }

    /** 
     * This method generates a set of reachable markings for a given net.
     */
    private RSetOfMarkings getReachableMarkings(RMarking M) throws Exception {
        RSetOfMarkings RS = new RSetOfMarkings();
        RSetOfMarkings visitingPS = getImmediateSuccessors(M);
        visitingPS.addMarking(M);
        while (!RS.containsAll(visitingPS.getMarkings())) {
            if (messageDlg.isCancelled()) return RS;
            RS.addAll(visitingPS);
            if (RS.size() > maxNumMarkings) {
                messageDlg.write("Reachable markings >" + maxNumMarkings + ". Possible infinite loop in the net " + _ID);
                return RS;
            }
            visitingPS = getImmediateSuccessors(visitingPS);
            messageDlg.write("Immediate successors: " + visitingPS.size());
        }
        messageDlg.write("Reachability set size: " + RS.size());
        return RS;
    }

    /**
     * return successor markings from a set of markings.
     *
     *
     */
    private RSetOfMarkings getImmediateSuccessors(RSetOfMarkings markings) {
        RSetOfMarkings successors = new RSetOfMarkings();
        for (Iterator i = markings.getMarkings().iterator(); i.hasNext(); ) {
            RMarking currentM = (RMarking) i.next();
            RSetOfMarkings post = getImmediateSuccessors(currentM);
            if (post.size() > 0) {
                successors.addAll(post);
            } else {
                endMarkings.addMarking(currentM);
            }
        }
        return successors;
    }

    /**
     * return successor markings from a marking.
     *
     *
     */
    private RSetOfMarkings getImmediateSuccessors(RMarking currentM) {
        RSetOfMarkings successors = new RSetOfMarkings();
        for (Iterator i = _Transitions.values().iterator(); i.hasNext(); ) {
            RTransition t = (RTransition) i.next();
            if (isForwardEnabled(currentM, t)) {
                RMarking postMarking = getNextRMarking(currentM, t);
                successors.addMarking(postMarking);
            }
        }
        return successors;
    }

    /**
     * Get an immediate successor marking from m by firing t.
     */
    private RMarking getNextRMarking(RMarking currentM, RTransition t) {
        Map postmarkedPlaces = new HashMap(currentM.getMarkedPlaces());
        Set postSet = new HashSet(t.getPostsetElements());
        Set preSet = new HashSet(t.getPresetElements());
        Set removeSet = new HashSet(t.getRemoveSet());
        RElement netElement;
        String netElementName;
        Integer countString, tokenCount;
        preSet.removeAll(removeSet);
        for (Iterator iterator = preSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (postmarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) postmarkedPlaces.get(netElementName);
                int count = countString.intValue();
                if (count == 1) {
                    postmarkedPlaces.remove(netElementName);
                } else if (count > 1) {
                    count = count - 1;
                    tokenCount = new Integer(count);
                    postmarkedPlaces.put(netElementName, tokenCount);
                }
            }
        }
        for (Iterator iterator = removeSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (postmarkedPlaces.containsKey(netElementName)) {
                postmarkedPlaces.remove(netElementName);
            }
        }
        tokenCount = new Integer(1);
        for (Iterator iterator = postSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (postmarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) postmarkedPlaces.get(netElementName);
                int count = countString.intValue();
                count++;
                tokenCount = new Integer(count);
            }
            postmarkedPlaces.put(netElementName, tokenCount);
        }
        RMarking nextM = new RMarking(postmarkedPlaces);
        return nextM;
    }

    private boolean isForwardEnabled(RMarking currentM, RTransition t) {
        Set preSet = t.getPresetElements();
        Map markedPlaces = currentM.getMarkedPlaces();
        for (Iterator x = preSet.iterator(); x.hasNext(); ) {
            RPlace place = (RPlace) x.next();
            String placeName = place.getID();
            if (!markedPlaces.containsKey(placeName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method takes two markings s and t, and check whether s'<= s is coverable 
     * from the predecessors of t.
     */
    private boolean Coverable(RMarking s, RMarking t) {
        alreadyConsideredMarkings = new HashSet(100);
        RSetOfMarkings tSet = new RSetOfMarkings();
        tSet.addMarking(t);
        RSetOfMarkings rm = FiniteBasisPred(tSet);
        RMarking x;
        for (Iterator iter = rm.getMarkings().iterator(); iter.hasNext(); ) {
            x = (RMarking) iter.next();
            if (x.isLessThanOrEqual(s)) {
                alreadyConsideredMarkings = null;
                return true;
            }
        }
        alreadyConsideredMarkings.clear();
        return false;
    }

    /**
     * This methods returns the FiniteBasis of the Predecessors for a set of 
     * RMarkings.
     */
    private RSetOfMarkings FiniteBasisPred(RSetOfMarkings I) {
        RSetOfMarkings K = new RSetOfMarkings();
        RSetOfMarkings Kn = new RSetOfMarkings();
        RSetOfMarkings Pred = new RSetOfMarkings();
        K.addAll(I);
        Pred.addAll(K);
        Kn = getMinimalCoveringSet(pb(K), Pred);
        while (!IsUpwardEqual(K, Kn)) {
            K.removeAll();
            K.addAll(Kn);
            Pred.removeAll();
            Pred.addAll(K);
            Kn = getMinimalCoveringSet(pb(K), Pred);
        }
        Kn = null;
        Pred = null;
        return K;
    }

    /**
     * This method checks whether the basis of the two sets of markings are equal.
     * This is the implementation of K = Kn.
     *
     */
    private boolean IsUpwardEqual(RSetOfMarkings K, RSetOfMarkings Kn) {
        return K.equals(Kn);
    }

    /**
     * This method is called with a set of RMarkings to generate
     * a set of precedessors. 
     */
    private RSetOfMarkings pb(RSetOfMarkings I) {
        RSetOfMarkings Z = new RSetOfMarkings();
        RMarking M;
        for (Iterator i = I.getMarkings().iterator(); i.hasNext(); ) {
            M = (RMarking) i.next();
            Z.addAll(pb(M));
        }
        Z = getMinimalCoveringSet2(Z);
        return Z;
    }

    /**
     * This method is called with a RMarking to generate
     * a set of precedessors of this marking. The method returns 
     * a finite basis for the predecessors set.(optimisation) 
     */
    private RSetOfMarkings pb(RMarking M) {
        RSetOfMarkings Z = new RSetOfMarkings();
        if (!alreadyConsideredMarkings.contains(M)) {
            for (Iterator i = _Transitions.values().iterator(); i.hasNext(); ) {
                RTransition t = (RTransition) i.next();
                if (isBackwardsEnabled(M, t)) {
                    RMarking preM = getPreviousRMarking(M, t);
                    if (!preM.isBiggerThanOrEqual(M)) {
                        Z.addMarking(preM);
                    }
                }
            }
            alreadyConsideredMarkings.add(M);
        }
        return Z;
    }

    /**
    * This method determines whether a transition should be backwards 
    * enabled at a given RMarking. Currently, a transition 
    * is not backwards enabled only if there are more tokens in the remove set  
    * than tokens in the postset.
    *
    */
    private boolean isBackwardsEnabled(RMarking currentM, RTransition t) {
        Set postSet = t.getPostsetElements();
        Set removeSet = t.getRemoveSet();
        Map markedPlaces = currentM.getMarkedPlaces();
        Integer count;
        if (removeSet.size() > 0) {
            for (Iterator x = removeSet.iterator(); x.hasNext(); ) {
                RPlace place = (RPlace) x.next();
                String placeName = place.getID();
                if (markedPlaces.containsKey(placeName)) {
                    if (postSet.contains(place)) {
                        count = (Integer) markedPlaces.get(placeName);
                        if (count.intValue() > 1) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private RMarking getPreviousRMarking(RMarking currentM, RTransition t) {
        Map premarkedPlaces = new HashMap(currentM.getMarkedPlaces());
        Set postSet = new HashSet(t.getPostsetElements());
        Set preSet = new HashSet(t.getPresetElements());
        Set removeSet = new HashSet(t.getRemoveSet());
        RElement netElement;
        String netElementName;
        Integer countString, tokenCount;
        postSet.removeAll(removeSet);
        for (Iterator iterator = postSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (premarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) premarkedPlaces.get(netElementName);
                int count = countString.intValue();
                if (count == 1) {
                    premarkedPlaces.remove(netElementName);
                } else if (count > 1) {
                    count = count - 1;
                    tokenCount = new Integer(count);
                    premarkedPlaces.put(netElementName, tokenCount);
                }
            }
        }
        preSet.removeAll(removeSet);
        tokenCount = new Integer(1);
        for (Iterator iterator = preSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            if (premarkedPlaces.containsKey(netElementName)) {
                countString = (Integer) premarkedPlaces.get(netElementName);
                int count = countString.intValue();
                count++;
                tokenCount = new Integer(count);
            }
            premarkedPlaces.put(netElementName, tokenCount);
        }
        removeSet.retainAll(t.getPresetElements());
        tokenCount = new Integer(1);
        for (Iterator iterator = removeSet.iterator(); iterator.hasNext(); ) {
            netElement = (RElement) iterator.next();
            netElementName = netElement.getID();
            premarkedPlaces.put(netElementName, tokenCount);
        }
        return new RMarking(premarkedPlaces);
    }

    /**
     * This method is used to generate the minimal covering set 
     * for a given set of Markings.
     */
    private RSetOfMarkings getMinimalCoveringSet2(RSetOfMarkings Z) {
        RSetOfMarkings Z_min = new RSetOfMarkings();
        Z_min.addAll(Z);
        for (Iterator z = Z.getMarkings().iterator(); z.hasNext(); ) {
            RMarking M = (RMarking) z.next();
            RSetOfMarkings Z_inner = new RSetOfMarkings();
            Z_inner.addAll(Z_min);
            Z_inner.removeMarking(M);
            for (Iterator x = Z_inner.getMarkings().iterator(); x.hasNext(); ) {
                RMarking M_i = (RMarking) x.next();
                if (M.isBiggerThanOrEqual(M_i)) {
                    Z_min.removeMarking(M);
                }
            }
        }
        return Z_min;
    }

    private RSetOfMarkings getMinimalCoveringSet(RSetOfMarkings pbZ, RSetOfMarkings Z) {
        RSetOfMarkings Z_min = new RSetOfMarkings();
        Z_min.addAll(Z);
        Z_min.addAll(pbZ);
        for (Iterator z = pbZ.getMarkings().iterator(); z.hasNext(); ) {
            RMarking M = (RMarking) z.next();
            RSetOfMarkings Z_inner = new RSetOfMarkings();
            Z_inner.addAll(Z_min);
            Z_inner.removeMarking(M);
            for (Iterator x = Z_inner.getMarkings().iterator(); x.hasNext(); ) {
                RMarking M_i = (RMarking) x.next();
                if (M.isBiggerThanOrEqual(M_i)) {
                    Z_min.removeMarking(M);
                } else if (M_i.isBiggerThanOrEqual(M)) {
                    Z_min.removeMarking(M_i);
                }
            }
        }
        return Z_min;
    }

    /**
	 * used for formatting xml messages.
	 * Message could be a warning or observation. 
	 */
    private String formatXMLMessage(String msg, boolean isObservation) {
        StringBuffer msgBuffer = new StringBuffer(200);
        if (isObservation) {
            msgBuffer.append("<observation>");
            msgBuffer.append(msg);
            msgBuffer.append("</observation>");
        } else {
            msgBuffer.append("<warning>");
            msgBuffer.append(msg);
            msgBuffer.append("</warning>");
        }
        return msgBuffer.toString();
    }

    private String printMarking(RMarking m) {
        String printM = "";
        Set mPlaces = m.getMarkedPlaces().entrySet();
        for (Iterator i = mPlaces.iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            printM += e.getValue() + "" + e.getKey() + "+";
        }
        return printM.substring(0, printM.length() - 1);
    }

    /**
    * This method is used for error messages mapping from reset
    * to yawl. If there are yawl mappings, the message returns them, otherwise 
    * it returns an empty set.
    */
    public static HashSet convertToYawlMappings(RElement e) {
        HashSet resetMappings = new HashSet(e.getResetMappings());
        HashSet yawlMappings = new HashSet(e.getYawlMappings());
        for (Iterator i = resetMappings.iterator(); i.hasNext(); ) {
            RElement innerEle = (RElement) i.next();
            yawlMappings.addAll(innerEle.getYawlMappings());
        }
        return yawlMappings;
    }

    public static HashSet convertToYawlMappingsForConditions(RElement e) {
        String msg = " ";
        HashSet mappings = convertToYawlMappings(e);
        HashSet condMappings = new HashSet();
        for (Iterator in = mappings.iterator(); in.hasNext(); ) {
            YExternalNetElement yEle = (YExternalNetElement) in.next();
            if (yEle instanceof YCondition) {
                condMappings.add(yEle);
            }
        }
        return condMappings;
    }

    public static HashSet convertToYawlMappingsForTasks(RElement e) {
        HashSet mappings = convertToYawlMappings(e);
        HashSet taskMappings = new HashSet();
        for (Iterator in = mappings.iterator(); in.hasNext(); ) {
            YExternalNetElement yEle = (YExternalNetElement) in.next();
            if (yEle instanceof YTask) {
                taskMappings.add(yEle);
            }
        }
        return taskMappings;
    }

    /**
    * @id - id of an RElement to retrieve from reset net
    * it returns the element or null
    * Changes made to make it work with reduced nets
    */
    private RElement findResetMapping(String id) {
        Map allElements = new HashMap();
        allElements.putAll(_Places);
        allElements.putAll(_Transitions);
        RElement e = (RElement) allElements.get(id);
        HashSet mappings;
        RElement mappedEle, innerEle;
        if (e == null) {
            for (Iterator i = allElements.values().iterator(); i.hasNext(); ) {
                mappedEle = (RElement) i.next();
                mappings = new HashSet(mappedEle.getResetMappings());
                for (Iterator inner = mappings.iterator(); inner.hasNext(); ) {
                    innerEle = (RElement) inner.next();
                    if (innerEle.getID().equals(id)) {
                        return mappedEle;
                    }
                }
            }
        }
        return e;
    }
}
