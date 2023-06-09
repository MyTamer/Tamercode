package org.dllearner.algorithm.tbsl.sem.drs.reader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dllearner.algorithm.tbsl.sem.drs.*;
import org.dllearner.algorithm.tbsl.sem.util.Label;

public class DRSParser implements DRSParserConstants {

    /** Main entry point. */
    public static void main(String args[]) throws ParseException {
        DRSParser parser = new DRSParser(System.in);
        parser.Input();
    }

    /** Root production. */
    public final void Input() throws ParseException {
        DRS();
        jj_consume_token(0);
    }

    /** DRS */
    public final DRS DRS() throws ParseException {
        Set<DiscourseReferent> dr_set = null;
        Set<DRS_Condition> conditions = null;
        DRS drs;
        Token label = null;
        if (jj_2_1(2)) {
            label = jj_consume_token(LABEL);
            jj_consume_token(1);
        } else {
            ;
        }
        jj_consume_token(2);
        if (jj_2_2(2)) {
            dr_set = DR_Set();
        } else {
            ;
        }
        jj_consume_token(3);
        if (jj_2_3(2)) {
            conditions = Condition_List();
        } else {
            ;
        }
        jj_consume_token(4);
        if (dr_set == null) {
            dr_set = new HashSet<DiscourseReferent>();
        }
        drs = new DRS();
        if (label != null) {
            drs.setLabel(label.toString());
        }
        drs.setDiscourseReferents(dr_set);
        if (conditions != null) {
            drs.setDRSConditions(conditions);
        }
        {
            if (true) return drs;
        }
        throw new Error("Missing return statement in function");
    }

    /** DR_Set*/
    public final Set<DiscourseReferent> DR_Set() throws ParseException {
        Token dr;
        Set<DiscourseReferent> dr_set = null;
        dr = dr();
        if (jj_2_4(2)) {
            jj_consume_token(5);
            dr_set = DR_Set();
        } else {
            ;
        }
        if (dr_set == null) {
            dr_set = new HashSet<DiscourseReferent>();
        }
        if (dr.toString().startsWith("?")) {
            dr_set.add(new DiscourseReferent(dr.toString().substring(1), true, false));
        } else if (dr.toString().startsWith("!")) {
            dr_set.add(new DiscourseReferent(dr.toString().substring(1), false, true));
        } else {
            dr_set.add(new DiscourseReferent(dr.toString(), false, false));
        }
        {
            if (true) return dr_set;
        }
        throw new Error("Missing return statement in function");
    }

    public final Set<DRS_Condition> Condition_List() throws ParseException {
        DRS_Condition condition = null;
        Set<DRS_Condition> conditions = null;
        condition = Condition();
        if (jj_2_5(2)) {
            jj_consume_token(5);
            conditions = Condition_List();
        } else {
            ;
        }
        if (conditions == null) {
            conditions = new HashSet<DRS_Condition>();
        }
        conditions.add(condition);
        {
            if (true) return conditions;
        }
        throw new Error("Missing return statement in function");
    }

    public final DRS_Condition Condition() throws ParseException {
        List<DiscourseReferent> dr_list;
        Token dr1;
        Token dr2;
        Token dr;
        Token predicate;
        Token quantifier;
        DRS drs1;
        DRS drs2;
        if (jj_2_15(2)) {
            predicate = jj_consume_token(WORD);
            jj_consume_token(6);
            dr_list = DR_List();
            jj_consume_token(7);
            Simple_DRS_Condition condition;
            condition = new Simple_DRS_Condition();
            condition.setPredicate(predicate.toString());
            condition.setArguments(dr_list);
            {
                if (true) return condition;
            }
        } else if (jj_2_16(2)) {
            dr1 = dr();
            jj_consume_token(8);
            dr2 = dr();
            Simple_DRS_Condition condition;
            condition = new Simple_DRS_Condition();
            condition.setPredicate("equal");
            condition.addArgument(new DiscourseReferent(dr1.toString()));
            condition.addArgument(new DiscourseReferent(dr2.toString()));
            {
                if (true) return condition;
            }
        } else if (jj_2_17(2)) {
            jj_consume_token(9);
            drs1 = DRS();
            Negated_DRS drs = new Negated_DRS();
            drs.setDRS(drs1);
            {
                if (true) return drs;
            }
        } else if (jj_2_18(2)) {
            drs1 = DRS();
            if (jj_2_6(2)) {
                quantifier = jj_consume_token(EVERY);
            } else if (jj_2_7(2)) {
                quantifier = jj_consume_token(SOME);
            } else if (jj_2_8(2)) {
                quantifier = jj_consume_token(AFEW);
            } else if (jj_2_9(2)) {
                quantifier = jj_consume_token(MOST);
            } else if (jj_2_10(2)) {
                quantifier = jj_consume_token(THEMOST);
            } else if (jj_2_11(2)) {
                quantifier = jj_consume_token(THELEAST);
            } else if (jj_2_12(2)) {
                quantifier = jj_consume_token(HOWMANY);
            } else if (jj_2_13(2)) {
                quantifier = jj_consume_token(MANY);
            } else if (jj_2_14(2)) {
                quantifier = jj_consume_token(NO);
            } else {
                jj_consume_token(-1);
                throw new ParseException();
            }
            dr = dr();
            drs2 = DRS();
            Complex_DRS_Condition drs;
            drs = new Complex_DRS_Condition();
            drs.setRestrictor(drs1);
            drs.setScope(drs2);
            drs.setReferent(new DiscourseReferent(dr.toString()));
            if (quantifier.toString().equals("EVERY")) {
                drs.setQuantifier(DRS_Quantifier.EVERY);
            }
            if (quantifier.toString().equals("SOME")) {
                drs.setQuantifier(DRS_Quantifier.SOME);
            }
            if (quantifier.toString().equals("MOST")) {
                drs.setQuantifier(DRS_Quantifier.MOST);
            }
            if (quantifier.toString().equals("THEMOST")) {
                drs.setQuantifier(DRS_Quantifier.THEMOST);
            }
            if (quantifier.toString().equals("THELEAST")) {
                drs.setQuantifier(DRS_Quantifier.THELEAST);
            }
            if (quantifier.toString().equals("AFEW")) {
                drs.setQuantifier(DRS_Quantifier.FEW);
            }
            if (quantifier.toString().equals("MANY")) {
                drs.setQuantifier(DRS_Quantifier.MANY);
            }
            if (quantifier.toString().equals("HOWMANY")) {
                drs.setQuantifier(DRS_Quantifier.HOWMANY);
            }
            if (quantifier.toString().equals("NO")) {
                drs.setQuantifier(DRS_Quantifier.NO);
            }
            {
                if (true) return drs;
            }
        } else {
            jj_consume_token(-1);
            throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    /** DR_List*/
    public final List<DiscourseReferent> DR_List() throws ParseException {
        Token dr;
        List<DiscourseReferent> dr_list = null;
        dr = dr();
        if (jj_2_19(2)) {
            jj_consume_token(5);
            dr_list = DR_List();
        } else {
            ;
        }
        if (dr_list == null) {
            dr_list = new ArrayList<DiscourseReferent>();
        }
        if (dr.toString().startsWith("?")) {
            dr_list.add(0, new DiscourseReferent(dr.toString().substring(1), true, false));
        } else if (dr.toString().startsWith("?")) {
            dr_list.add(0, new DiscourseReferent(dr.toString().substring(1), false, true));
        } else {
            dr_list.add(0, new DiscourseReferent(dr.toString(), false, false));
        }
        {
            if (true) return dr_list;
        }
        throw new Error("Missing return statement in function");
    }

    public final Token dr() throws ParseException {
        Token t;
        if (jj_2_20(2)) {
            t = jj_consume_token(WORD);
        } else if (jj_2_21(2)) {
            t = jj_consume_token(DR);
        } else if (jj_2_22(2)) {
            t = jj_consume_token(QUOTED_STRING);
        } else {
            jj_consume_token(-1);
            throw new ParseException();
        }
        {
            if (true) return t;
        }
        throw new Error("Missing return statement in function");
    }

    private boolean jj_2_1(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_1();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(0, xla);
        }
    }

    private boolean jj_2_2(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_2();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(1, xla);
        }
    }

    private boolean jj_2_3(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_3();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(2, xla);
        }
    }

    private boolean jj_2_4(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_4();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(3, xla);
        }
    }

    private boolean jj_2_5(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_5();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(4, xla);
        }
    }

    private boolean jj_2_6(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_6();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(5, xla);
        }
    }

    private boolean jj_2_7(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_7();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(6, xla);
        }
    }

    private boolean jj_2_8(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_8();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(7, xla);
        }
    }

    private boolean jj_2_9(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_9();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(8, xla);
        }
    }

    private boolean jj_2_10(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_10();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(9, xla);
        }
    }

    private boolean jj_2_11(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_11();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(10, xla);
        }
    }

    private boolean jj_2_12(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_12();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(11, xla);
        }
    }

    private boolean jj_2_13(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_13();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(12, xla);
        }
    }

    private boolean jj_2_14(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_14();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(13, xla);
        }
    }

    private boolean jj_2_15(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_15();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(14, xla);
        }
    }

    private boolean jj_2_16(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_16();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(15, xla);
        }
    }

    private boolean jj_2_17(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_17();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(16, xla);
        }
    }

    private boolean jj_2_18(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_18();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(17, xla);
        }
    }

    private boolean jj_2_19(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_19();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(18, xla);
        }
    }

    private boolean jj_2_20(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_20();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(19, xla);
        }
    }

    private boolean jj_2_21(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_21();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(20, xla);
        }
    }

    private boolean jj_2_22(int xla) {
        jj_la = xla;
        jj_lastpos = jj_scanpos = token;
        try {
            return !jj_3_22();
        } catch (LookaheadSuccess ls) {
            return true;
        } finally {
            jj_save(21, xla);
        }
    }

    private boolean jj_3_2() {
        if (jj_3R_1()) return true;
        return false;
    }

    private boolean jj_3_18() {
        if (jj_3R_4()) return true;
        return false;
    }

    private boolean jj_3_10() {
        if (jj_scan_token(THEMOST)) return true;
        return false;
    }

    private boolean jj_3_19() {
        if (jj_scan_token(5)) return true;
        if (jj_3R_5()) return true;
        return false;
    }

    private boolean jj_3_17() {
        if (jj_scan_token(9)) return true;
        if (jj_3R_4()) return true;
        return false;
    }

    private boolean jj_3_13() {
        if (jj_scan_token(MANY)) return true;
        return false;
    }

    private boolean jj_3R_2() {
        if (jj_3R_6()) return true;
        return false;
    }

    private boolean jj_3_7() {
        if (jj_scan_token(SOME)) return true;
        return false;
    }

    private boolean jj_3R_5() {
        if (jj_3R_3()) return true;
        return false;
    }

    private boolean jj_3_22() {
        if (jj_scan_token(QUOTED_STRING)) return true;
        return false;
    }

    private boolean jj_3_3() {
        if (jj_3R_2()) return true;
        return false;
    }

    private boolean jj_3_1() {
        if (jj_scan_token(LABEL)) return true;
        if (jj_scan_token(1)) return true;
        return false;
    }

    private boolean jj_3R_4() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3_1()) jj_scanpos = xsp;
        if (jj_scan_token(2)) return true;
        xsp = jj_scanpos;
        if (jj_3_2()) jj_scanpos = xsp;
        if (jj_scan_token(3)) return true;
        return false;
    }

    private boolean jj_3_9() {
        if (jj_scan_token(MOST)) return true;
        return false;
    }

    private boolean jj_3_16() {
        if (jj_3R_3()) return true;
        if (jj_scan_token(8)) return true;
        return false;
    }

    private boolean jj_3_11() {
        if (jj_scan_token(THELEAST)) return true;
        return false;
    }

    private boolean jj_3_21() {
        if (jj_scan_token(DR)) return true;
        return false;
    }

    private boolean jj_3_4() {
        if (jj_scan_token(5)) return true;
        if (jj_3R_1()) return true;
        return false;
    }

    private boolean jj_3_12() {
        if (jj_scan_token(HOWMANY)) return true;
        return false;
    }

    private boolean jj_3R_6() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3_15()) {
            jj_scanpos = xsp;
            if (jj_3_16()) {
                jj_scanpos = xsp;
                if (jj_3_17()) {
                    jj_scanpos = xsp;
                    if (jj_3_18()) return true;
                }
            }
        }
        return false;
    }

    private boolean jj_3_15() {
        if (jj_scan_token(WORD)) return true;
        if (jj_scan_token(6)) return true;
        return false;
    }

    private boolean jj_3_6() {
        if (jj_scan_token(EVERY)) return true;
        return false;
    }

    private boolean jj_3_5() {
        if (jj_scan_token(5)) return true;
        if (jj_3R_2()) return true;
        return false;
    }

    private boolean jj_3_14() {
        if (jj_scan_token(NO)) return true;
        return false;
    }

    private boolean jj_3_20() {
        if (jj_scan_token(WORD)) return true;
        return false;
    }

    private boolean jj_3R_3() {
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3_20()) {
            jj_scanpos = xsp;
            if (jj_3_21()) {
                jj_scanpos = xsp;
                if (jj_3_22()) return true;
            }
        }
        return false;
    }

    private boolean jj_3R_1() {
        if (jj_3R_3()) return true;
        Token xsp;
        xsp = jj_scanpos;
        if (jj_3_4()) jj_scanpos = xsp;
        return false;
    }

    private boolean jj_3_8() {
        if (jj_scan_token(AFEW)) return true;
        return false;
    }

    /** Generated Token Manager. */
    public DRSParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    /** Current token. */
    public Token token;

    /** Next token. */
    public Token jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    private int jj_gen;

    private final int[] jj_la1 = new int[0];

    private static int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[] {};
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[22];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    /** Constructor with InputStream. */
    public DRSParser(java.io.InputStream stream) {
        this(stream, null);
    }

    /** Constructor with InputStream and supplied encoding */
    public DRSParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new DRSParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /** Reinitialise. */
    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor. */
    public DRSParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new DRSParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Constructor with generated Token Manager. */
    public DRSParser(DRSParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    /** Reinitialise. */
    public void ReInit(DRSParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 0; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            if (++jj_gc > 100) {
                jj_gc = 0;
                for (int i = 0; i < jj_2_rtns.length; i++) {
                    JJCalls c = jj_2_rtns[i];
                    while (c != null) {
                        if (c.gen < jj_gen) c.first = null;
                        c = c.next;
                    }
                }
            }
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    private static final class LookaheadSuccess extends java.lang.Error {
    }

    private final LookaheadSuccess jj_ls = new LookaheadSuccess();

    private boolean jj_scan_token(int kind) {
        if (jj_scanpos == jj_lastpos) {
            jj_la--;
            if (jj_scanpos.next == null) {
                jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
            } else {
                jj_lastpos = jj_scanpos = jj_scanpos.next;
            }
        } else {
            jj_scanpos = jj_scanpos.next;
        }
        if (jj_rescan) {
            int i = 0;
            Token tok = token;
            while (tok != null && tok != jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) jj_add_error_token(kind, i);
        }
        if (jj_scanpos.kind != kind) return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
        return false;
    }

    /** Get the next Token. */
    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /** Get the specific Token. */
    public final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();

    private int[] jj_expentry;

    private int jj_kind = -1;

    private int[] jj_lasttokens = new int[100];

    private int jj_endpos;

    private void jj_add_error_token(int kind, int pos) {
        if (pos >= 100) return;
        if (pos == jj_endpos + 1) {
            jj_lasttokens[jj_endpos++] = kind;
        } else if (jj_endpos != 0) {
            jj_expentry = new int[jj_endpos];
            for (int i = 0; i < jj_endpos; i++) {
                jj_expentry[i] = jj_lasttokens[i];
            }
            jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext(); ) {
                int[] oldentry = (int[]) (it.next());
                if (oldentry.length == jj_expentry.length) {
                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            continue jj_entries_loop;
                        }
                    }
                    jj_expentries.add(jj_expentry);
                    break jj_entries_loop;
                }
            }
            if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
        }
    }

    /** Generate ParseException. */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[27];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 0; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 27; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /** Enable tracing. */
    public final void enable_tracing() {
    }

    /** Disable tracing. */
    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
        jj_rescan = true;
        for (int i = 0; i < 22; i++) {
            try {
                JJCalls p = jj_2_rtns[i];
                do {
                    if (p.gen > jj_gen) {
                        jj_la = p.arg;
                        jj_lastpos = jj_scanpos = p.first;
                        switch(i) {
                            case 0:
                                jj_3_1();
                                break;
                            case 1:
                                jj_3_2();
                                break;
                            case 2:
                                jj_3_3();
                                break;
                            case 3:
                                jj_3_4();
                                break;
                            case 4:
                                jj_3_5();
                                break;
                            case 5:
                                jj_3_6();
                                break;
                            case 6:
                                jj_3_7();
                                break;
                            case 7:
                                jj_3_8();
                                break;
                            case 8:
                                jj_3_9();
                                break;
                            case 9:
                                jj_3_10();
                                break;
                            case 10:
                                jj_3_11();
                                break;
                            case 11:
                                jj_3_12();
                                break;
                            case 12:
                                jj_3_13();
                                break;
                            case 13:
                                jj_3_14();
                                break;
                            case 14:
                                jj_3_15();
                                break;
                            case 15:
                                jj_3_16();
                                break;
                            case 16:
                                jj_3_17();
                                break;
                            case 17:
                                jj_3_18();
                                break;
                            case 18:
                                jj_3_19();
                                break;
                            case 19:
                                jj_3_20();
                                break;
                            case 20:
                                jj_3_21();
                                break;
                            case 21:
                                jj_3_22();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess ls) {
            }
        }
        jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p = jj_2_rtns[index];
        while (p.gen > jj_gen) {
            if (p.next == null) {
                p = p.next = new JJCalls();
                break;
            }
            p = p.next;
        }
        p.gen = jj_gen + xla - jj_la;
        p.first = token;
        p.arg = xla;
    }

    static final class JJCalls {

        int gen;

        Token first;

        int arg;

        JJCalls next;
    }
}
