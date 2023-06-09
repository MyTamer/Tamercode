package com.xmultra.processor.nitf;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xmultra.log.Console;
import com.xmultra.util.InitMapHolder;

/**
 * Any empty implementation. Use as starting point for a new NitfXformer subclass.
 *
 * @author      Wayne W. Weber
 * @version     $Revision: #1 $
 * @since       1.4
 */
class Nxf_Substitutions extends NitfXformer {

    /** Used to separate Head and body in substitutions over the whole document. */
    private static final String HEAD_BODY_SEPARATOR = "      ";

    private List substitutionList = new ArrayList();

    /**
     * Does a setup be creating and getting references to utilities.
     *
     * @param xformerEl           The configuration element associated with this Xformer.
     *
     * @param nitfProcessorConfig Holds the NitfProcessorConfig data & methods.
     *
     * @param imh                 Holds references to utility and log objects.
     *
     * @param nitfXformerUtils    Has utility methods shared by Xformers.
     *
     * @return True if initialization is successful.
     */
    boolean init(Element substitutionsEl, NitfProcessorConfig nitfProcessorConfig, InitMapHolder imh, NitfXformerUtils nitfXformerUtils) {
        super.init(substitutionsEl, nitfProcessorConfig, imh, nitfXformerUtils);
        this.buildSubstitutionsLists(substitutionsEl);
        return true;
    }

    /**
     * Applies a transform or process to an Nitf document.
     *
     */
    boolean xform(NitfDoc nitfDoc) {
        doSubstitutions(nitfDoc);
        return true;
    }

    /**
     * Resets any attributes set as the result of processing a doc.
     */
    void reset() {
        this.testPrintOutput = null;
    }

    /**
     * Searches through the doc and applies the regular expressions in the
     * children of the following element (found in the NewsPreParserConfig
     * file: <P>
     *
     * /NitfProcessorConfig/NitfXformers/Substitutions <P>
     *
     * @param nitfDoc Contains the document to search/replace.
     *
     */
    private void doSubstitutions(NitfDoc nitfDoc) {
        String head = nitfDoc.getHead();
        String body = nitfDoc.getBody();
        String headAndBody = null;
        SubstitutionDataHolder substDataHolder = null;
        if (this.substitutionList.size() == 0) {
            return;
        }
        for (int i = 0; i < this.substitutionList.size(); i++) {
            substDataHolder = (SubstitutionDataHolder) this.substitutionList.get(i);
            if (substDataHolder.substDataHolderArray != null) {
                nitfDoc.setHead(head);
                nitfDoc.setBody(body);
                doMatchedSubstitution(nitfDoc, substDataHolder);
                head = nitfDoc.getHead();
                body = nitfDoc.getBody();
                continue;
            }
            if (substDataHolder.whereToApply.equals(NitfProcessorConfig.HEAD)) {
                String before = head;
                head = strings.substituteWithOptions(substDataHolder.searchFor, substDataHolder.substituteWith, head, substDataHolder.options);
                printSubstitutionResults(before, head, substDataHolder);
            }
            if (substDataHolder.whereToApply.equals(NitfProcessorConfig.BODY)) {
                String before = body;
                body = strings.substituteWithOptions(substDataHolder.searchFor, substDataHolder.substituteWith, body, substDataHolder.options);
                printSubstitutionResults(before, body, substDataHolder);
            }
            if (substDataHolder.whereToApply.equals(NitfProcessorConfig.HEAD_AND_BODY)) {
                String before = head + body;
                headAndBody = head + HEAD_BODY_SEPARATOR + body;
                headAndBody = strings.substituteWithOptions(substDataHolder.searchFor, substDataHolder.substituteWith, headAndBody, substDataHolder.options);
                head = headAndBody.substring(0, headAndBody.indexOf(HEAD_BODY_SEPARATOR));
                body = headAndBody.substring(headAndBody.indexOf(HEAD_BODY_SEPARATOR) + HEAD_BODY_SEPARATOR.length(), headAndBody.length());
                printSubstitutionResults(before, head + body, substDataHolder);
            }
        }
        nitfDoc.setHead(head);
        nitfDoc.setBody(body);
    }

    /**
     * Performs a substitution within a match. All text that matches a
     * regex in a &ltMatchedSubstitution&gt; element can be processed
     * by a group of substitutions, defined in a &ltSubstitutionInMatch&gt;
     * elements.
     *
     * @param doc             The document that matched substitution will be
     *                        applied to.
     *
     * @param substDataHolder A holder for the matched substitution (and
     *                        an array of substitutions).
     */
    private void doMatchedSubstitution(NitfDoc nitfDoc, SubstitutionDataHolder substDataHolder) {
        boolean applyInHead = false;
        boolean applyInBody = false;
        boolean applyInHeadAndBody = false;
        boolean ignoreCase = false;
        boolean matchAllOccurrences = false;
        String head = nitfDoc.getHead();
        String body = nitfDoc.getBody();
        String data = "";
        int matchGroup = substDataHolder.matchGroupNumber;
        if (substDataHolder.options.indexOf("i") != -1) {
            ignoreCase = true;
        }
        if (substDataHolder.options.indexOf("g") != -1) {
            matchAllOccurrences = true;
        }
        if (substDataHolder.whereToApply.equals(NitfProcessorConfig.HEAD)) {
            data = head;
            applyInHead = true;
        } else if (substDataHolder.whereToApply.equals(NitfProcessorConfig.BODY)) {
            data = body;
            applyInBody = true;
        } else {
            data = nitfDoc.getHead() + HEAD_BODY_SEPARATOR + nitfDoc.getBody();
            applyInHeadAndBody = true;
        }
        StringBuffer sb = new StringBuffer();
        String after = data;
        while (strings.matches(substDataHolder.searchFor, after, ignoreCase)) {
            String preMatch = strings.getPreMatch();
            if (preMatch == null) {
                preMatch = "";
            }
            String wholeMatch = strings.getGroup(0);
            if (wholeMatch == null) {
                wholeMatch = "";
            }
            after = strings.getPostMatch();
            if (after == null) {
                after = "";
            }
            String groupMatch = strings.getGroup(matchGroup);
            if (groupMatch == null) {
                groupMatch = "";
            }
            int endOffset = strings.getEndOffset(matchGroup);
            int matchLength = groupMatch.length();
            String preMatchAndMatch = preMatch + wholeMatch;
            String preGroupMatch = "";
            String postGroupMatch = "";
            int groupMatchIndex = endOffset - matchLength;
            preGroupMatch = preMatchAndMatch.substring(0, groupMatchIndex);
            postGroupMatch = preMatchAndMatch.substring(groupMatchIndex + groupMatch.length(), preMatchAndMatch.length());
            sb.append(preGroupMatch);
            for (int i = 0; i < substDataHolder.substDataHolderArray.length; i++) {
                String originalGroupMatch = groupMatch;
                groupMatch = strings.substituteWithOptions(substDataHolder.substDataHolderArray[i].searchFor, substDataHolder.substDataHolderArray[i].substituteWith, groupMatch, substDataHolder.substDataHolderArray[i].options);
                if (data == null) {
                    break;
                }
                printSubstitutionResults(originalGroupMatch, groupMatch, substDataHolder.substDataHolderArray[i]);
            }
            sb.append(groupMatch);
            if (postGroupMatch != null) {
                sb.append(postGroupMatch);
            }
            if (!matchAllOccurrences) {
                break;
            }
        }
        if (!after.equals(data)) {
            sb.append(after);
            data = sb.toString();
        }
        String newHead = "";
        String newBody = "";
        if (applyInHeadAndBody) {
            newHead = data.substring(0, data.indexOf(HEAD_BODY_SEPARATOR));
            newBody = data.substring(data.indexOf(HEAD_BODY_SEPARATOR) + HEAD_BODY_SEPARATOR.length(), data.length());
        }
        if (applyInHead) {
            printSubstitutionResults(head, data, substDataHolder);
            nitfDoc.setHead(data);
        } else if (applyInBody) {
            printSubstitutionResults(body, data, substDataHolder);
            nitfDoc.setBody(data);
        } else if (applyInHeadAndBody) {
            printSubstitutionResults(head + body, newHead + newBody, substDataHolder);
            nitfDoc.setHead(newHead);
            nitfDoc.setBody(newBody);
        }
    }

    /**
     * Used for diagnostic printing to see the results of a substitution.
     *
     * @param dataBefore      The head, body, or head/body over which a
     *                        substitution was done.
     *
     * @param dataAfter       The head, body, or head/body over which a
     *                        substitution was done.
     *
     * @param substDataHolder An object which holds all parameters for
     *                        performing a substitution.
     */
    private void printSubstitutionResults(String dataBefore, String dataAfter, SubstitutionDataHolder substDataHolder) {
        boolean foundPatternBefore = false;
        boolean foundPatternAfter = false;
        String substNumber = substDataHolder.substNumber;
        if (!Console.getConsoleMode("a")) {
            return;
        }
        if (substDataHolder.testPrint.equals("No")) {
            return;
        }
        if (substDataHolder.testPrint.indexOf("Pattern") != -1) {
            if (substDataHolder.testPrintPattern == null || substDataHolder.testPrintPattern.equals("")) {
                return;
            }
            if (strings.matches(substDataHolder.testPrintPattern, dataBefore)) {
                foundPatternBefore = true;
            }
            if (strings.matches(substDataHolder.testPrintPattern, dataAfter)) {
                foundPatternAfter = true;
            }
            if (foundPatternBefore == foundPatternAfter && !substNumber.equals("0")) {
                return;
            }
            if (substDataHolder.testPrint.equals("WhenPatternFirstMatched") && !foundPatternAfter) {
                return;
            }
            if (substDataHolder.testPrint.equals("WhenPatternFirstNotMatched") && foundPatternAfter) {
                return;
            }
        }
        String data = dataBefore;
        String DATA_DESC_BEFORE = "Before: ";
        String DATA_DESC_AFTER = "After:  ";
        String dataDesc = DATA_DESC_BEFORE;
        if (this.testPrintOutput == null) {
            this.testPrintOutput = new StringBuffer();
        }
        for (int i = 0; i < 2; i++) {
            data = "SearchFor=" + substDataHolder.searchFor + "\n" + "SubstituteWith=" + substDataHolder.substituteWith + "\n" + "WhereToApply=" + substDataHolder.whereToApply + "         " + "Options=" + substDataHolder.options + "\n" + Console.DOTS + "\n" + "Comment=" + substDataHolder.comment + "\n" + Console.DOTS + "\n" + data;
            String output = Console.print("Inside: Substitutions (Nxf #" + this.getXformerSequenceNumber() + ") - " + dataDesc + "Substitution #" + substNumber, data, true, false);
            this.testPrintOutput.append(output);
            dataDesc = DATA_DESC_AFTER;
            data = dataAfter;
        }
    }

    /**
     * Builds two Lists of Substitutions composed of
     * SubstitutionDataHolder objects. One list will be performed before
     * any document processing has begun and the other will be performed
     * after all document processing is done.
     *
     * @param substitutionNodes   All the Substitution or MatchedSubstituion
     *                            elements in a NodeList.
     */
    private void buildSubstitutionsLists(Element substitutionsEl) {
        int substNumber = 0;
        if (substitutionsEl == null) {
            return;
        }
        NodeList substitutionsNodes = substitutionsEl.getChildNodes();
        for (int i = 0; i < substitutionsNodes.getLength(); i++) {
            Node substitutionNode = substitutionsNodes.item(i);
            if (substitutionNode.getLocalName() == null) {
                continue;
            }
            if (!substitutionNode.getLocalName().equals(NitfProcessorConfig.SUBSTITUTION) && !substitutionNode.getLocalName().equals(NitfProcessorConfig.MATCHED_SUBSTITUTION)) {
                continue;
            }
            substNumber++;
            Element substitutionElem = null;
            try {
                substitutionElem = (Element) substitutionNode;
            } catch (ClassCastException e) {
                continue;
            }
            SubstitutionDataHolder substDataHolder = new SubstitutionDataHolder();
            substDataHolder.parseSubstitutionElement(substitutionElem, substNumber + "");
            this.substitutionList.add(substDataHolder);
        }
    }

    /**
     * Holds the data for a substitution or a matched substitution.
     *
     * @author Wayne Weber
     * @since 1.2
     */
    private class SubstitutionDataHolder {

        String name = "";

        String searchFor = "";

        String substituteWith = "";

        String options = "";

        String whereToApply = "";

        int matchGroupNumber = 0;

        String testPrint = "";

        String testPrintPattern = "";

        String comment = "";

        String substNumber = "0";

        SubstitutionDataHolder[] substDataHolderArray = null;

        /**
         * Parses data from Substitution and MatchedSubstitution
         * elements and inserts it into this holder.
         *
         * @param substitutionElem The element from the config file to parse out.
         *
         * @param substNumber      The substitution number (used in TestPrint).
         */
        void parseSubstitutionElement(Element substitutionElem, String substNumber) {
            this.searchFor = substitutionElem.getAttribute(NitfProcessorConfig.SEARCH_FOR);
            this.searchFor = strings.substitute("&lt;", "<", searchFor);
            this.searchFor = strings.convertStringWithEscapedCode(searchFor);
            this.substituteWith = substitutionElem.getAttribute(NitfProcessorConfig.SUBSTITUTE_WITH);
            this.substituteWith = strings.substitute("&lt;", "<", substituteWith);
            this.substituteWith = strings.substitute("&lt&#59;", "&lt;", substituteWith);
            this.substituteWith = strings.convertStringWithEscapedCode(substituteWith);
            this.options = "m";
            String ignoreCase = substitutionElem.getAttribute(NitfProcessorConfig.IGNORE_CASE);
            if (ignoreCase.equals("Yes")) {
                this.options += "i";
            }
            String range = substitutionElem.getAttribute(NitfProcessorConfig.RANGE);
            if (range.equals(NitfProcessorConfig.RANGE_ALL_OCCURRENCES)) {
                this.options += "g";
            }
            this.whereToApply = substitutionElem.getAttribute(NitfProcessorConfig.WHERE_TO_APPLY);
            this.substNumber = substNumber;
            this.processTestPrint(substitutionElem);
            this.comment = substitutionElem.getAttribute(NitfProcessorConfig.COMMENT);
            if (substitutionElem.getLocalName().equals(NitfProcessorConfig.MATCHED_SUBSTITUTION)) {
                String matchGroup = substitutionElem.getAttribute(NitfProcessorConfig.MATCH_GROUP);
                this.matchGroupNumber = Integer.parseInt(matchGroup);
                NodeList matchSubstChildNodeList = substitutionElem.getChildNodes();
                this.substDataHolderArray = new SubstitutionDataHolder[matchSubstChildNodeList.getLength()];
                for (int i = 0; i < matchSubstChildNodeList.getLength(); i++) {
                    Element matchSubstChildElem = (Element) matchSubstChildNodeList.item(i);
                    SubstitutionDataHolder substDataHolder = new SubstitutionDataHolder();
                    substDataHolder.parseSubstitutionElement(matchSubstChildElem, substNumber + "." + (i + 1));
                    substDataHolderArray[i] = substDataHolder;
                }
            }
        }

        /**
         * Parses out and inserts the "TestPrint" value into the
         * holder.
         *
         * @param substDataHolder
         *               The holder which gets the TestPrint value inserted.
         *
         * @param substitutionElem
         *               The element in the config file with the TestPrint value.
         *
         * @return The holder.
         */
        void processTestPrint(Element substitutionElem) {
            this.testPrint = "No";
            String testPrint = substitutionElem.getAttribute(NitfProcessorConfig.TEST_PRINT);
            String xformersTestPrint = nitfProcessorConfig.getTestPrint();
            if (xformersTestPrint.equals("Enabled") && testPrint.equals("Yes")) {
                this.testPrint = "Yes";
            }
            if (xformersTestPrint.equals("All")) {
                this.testPrint = "Yes";
            }
            if (xformersTestPrint.indexOf("Pattern") != -1) {
                this.testPrint = xformersTestPrint;
                String testPrintPattern = nitfProcessorConfig.getTestPrintPattern();
                testPrintPattern = strings.substitute("&lt;", "<", testPrintPattern);
                testPrintPattern = strings.convertStringWithEscapedCode(testPrintPattern);
                this.testPrintPattern = testPrintPattern;
            }
        }

        /**
         * Used for testing. Shows the content of object.
         *
         * @return String representing content of object.
         */
        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (!name.equals("")) {
                sb.append("[name=").append(name).append(", ");
            } else {
                sb.append("[");
            }
            sb.append("searchFor=").append(searchFor);
            sb.append(", substituteWith=").append(substituteWith);
            sb.append(", options=").append(options);
            if (!whereToApply.equals("")) {
                sb.append(", whereToApply=").append(whereToApply);
            }
            if (!testPrint.equals("No")) {
                sb.append(", testPrint=").append(testPrint);
                sb.append(", testPrintPattern=").append(testPrintPattern);
            }
            sb.append(", substNumber=").append(substNumber);
            sb.append(", comment=").append(comment);
            return sb.append("]").toString();
        }
    }
}
