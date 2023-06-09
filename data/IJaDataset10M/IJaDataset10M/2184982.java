package org.primordion.user.app.English2French.rule;

import org.primordion.xholon.base.IXholon;

/**
 * <p>Used with "est-ce que" to make changes to a following verb.</p>
 * <p>See _QUESTION lexemes in English lexicon.</p>
 * <p>See WordRule.doRule_Question() and WordPass.handleQuestion(wordRuleResult).</p>
 * <p>See example of V_QUE_D: Will the train arrive on time.</p>
 * @author Ken Webb
 *
 */
public class Rule_V_QUE extends Rule_Flag {

    public String getRuleId() {
        switch(this.getXhcId()) {
            case V_QUE_ACE:
                return "V_QUE_A";
            case V_QUE_BCE:
                return "V_QUE_B";
            case V_QUE_CCE:
                return "V_QUE_C";
            case V_QUE_DCE:
                return "V_QUE_D";
            case V_QUE_ECE:
                return "V_QUE_E";
            case V_QUE_FCE:
                return "V_QUE_F";
            default:
                return null;
        }
    }

    public void doRule() {
        switch(this.getXhcId()) {
            case V_QUE_ACE:
                {
                    IXholon fourLetterCode = getNextFourLetterCode();
                    if (fourLetterCode == null) {
                        return;
                    }
                    String codeStr = fourLetterCode.getVal_String();
                    if ((codeStr == null) || (codeStr.length() != 4)) {
                        return;
                    }
                    if (codeStr.endsWith("xr")) {
                        fourLetterCode.setVal(codeStr.substring(0, 3) + "m");
                    }
                    break;
                }
            case V_QUE_BCE:
                {
                    IXholon fourLetterCode = getNextFourLetterCode();
                    if (fourLetterCode == null) {
                        return;
                    }
                    String codeStr = fourLetterCode.getVal_String();
                    if ((codeStr == null) || (codeStr.length() != 4)) {
                        return;
                    }
                    if (codeStr.endsWith("xr")) {
                        fourLetterCode.setVal(codeStr.substring(0, 3) + "m");
                    } else {
                        return;
                    }
                    IXholon closedBracket = fourLetterCode.getParentNode().insertAfter("ClosedBracket", null, "org.primordion.user.app.English2French.ClosedBracket");
                    closedBracket.appendChild("VerbClass", null).setVal(2);
                    closedBracket.appendChild("Lemma", null).setVal("�tre");
                    closedBracket.insertAfter("CM_vc", null);
                    break;
                }
            case V_QUE_CCE:
                {
                    IXholon fourLetterCode = getNextFourLetterCode();
                    if (fourLetterCode == null) {
                        return;
                    }
                    String codeStr = fourLetterCode.getVal_String();
                    if ((codeStr == null) || (codeStr.length() != 4)) {
                        return;
                    }
                    if (codeStr.endsWith("xr")) {
                        IXholon closedBracket = fourLetterCode.getParentNode().insertAfter("ClosedBracket", null, "org.primordion.user.app.English2French.ClosedBracket");
                        closedBracket.appendChild("VerbClass", null).setVal(2);
                        closedBracket.appendChild("Lemma", null).setVal("�tre");
                        closedBracket.insertAfter("CM_vc", null);
                    }
                    break;
                }
            case V_QUE_DCE:
                {
                    insertBetween(4, "aller");
                    break;
                }
            case V_QUE_ECE:
                insertBetween(28, "pouvoir");
                break;
            case V_QUE_FCE:
                insertBetween(27, "devoir");
                break;
            default:
                break;
        }
    }

    /**
	 * Insert between [__xr and #____]
	 * @param verbClass An integer that identifies a type of French verb conjugation.
	 * @param verbLemma The infinitive form of a French verb.
	 */
    protected void insertBetween(int verbClass, String verbLemma) {
        IXholon fourLetterCode = getNextFourLetterCode();
        if (fourLetterCode == null) {
            return;
        }
        String codeStr = fourLetterCode.getVal_String();
        if ((codeStr == null) || (codeStr.length() != 4)) {
            return;
        }
        if (codeStr.endsWith("xr")) {
            IXholon closedBracket = fourLetterCode.getParentNode().insertAfter("ClosedBracket", null, "org.primordion.user.app.English2French.ClosedBracket");
            closedBracket.appendChild("VerbClass", null).setVal(verbClass);
            closedBracket.appendChild("Lemma", null).setVal(verbLemma);
            IXholon vv = closedBracket.insertAfter("CM_vv", null);
            IXholon newOpenBracket = vv.insertAfter("OpenBracket", null);
            newOpenBracket.appendChild("FourLetterCode", null).setVal("nfnv");
            return;
        }
    }
}
