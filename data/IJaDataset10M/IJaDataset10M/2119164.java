package org.codecover.instrumentation.measurement.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.codecover.instrumentation.measurement.CoverageResultLog;
import org.codecover.instrumentation.measurement.NullCoverageLog;
import org.codecover.model.utils.StringUtil;
import static org.codecover.instrumentation.measurement.MeasurementConstants.CHARSET;

@SuppressWarnings("all")
public class CoverageLogParser implements CoverageLogParserConstants {

    public enum ParseMode {

        CHECK_UID, DO_NOT_CHECK_UID, GET_UID, UNKNOWN
    }

    private String testSessionContainerUID = null;

    private ParseMode parseMode = ParseMode.UNKNOWN;

    public CoverageLogParser(File source, Charset charset) throws IOException {
        this(new BufferedReader(new InputStreamReader(new FileInputStream(source), charset)));
    }

    public CoverageLogParser(File source) throws IOException {
        this(source, CHARSET);
    }

    /**
     * Gets the first TestSessionContainerUID of this coverage log file.<br>
     * <br>
     * <b>Do not reuse this CoverageLogParser after this check!</b>
     * @return the first TestSessionContainerUID, <code>null</code> if no 
     * TestSessionContainerUID is found
     * 
     * @throws TokenMgrError: This is a {@link TokenMgrError}, that is produced
     * by javacc stating, that the input could not be transformed into tokens.
     */
    public String getUID() throws ParseException, TokenMgrError {
        try {
            this.parseMode = ParseMode.GET_UID;
            CompilationUnit(NullCoverageLog.INSTANCE, null);
            return null;
        } catch (UIDFoundException e) {
            return this.testSessionContainerUID;
        }
    }

    /**
 * Parses a coverage log file and writes all found test cases and counters to
 * the {@link CoverageResultLog} c.
 *
 * If the <code>expectedTestSessionContainerUID</code> is not <code>null</code>, it is checked,
 * whether the TestSessionContainerUID of this coverage log file is equal or not. If there is
 * a mismatch, a {@link WrongUIDException} is thrown
 * 
 * @throws TokenMgrError: This is a {@link TokenMgrError}, that is produced
 * by javacc stating, that the input could not be transformed into tokens.
 */
    public final void CompilationUnit(CoverageResultLog c, String expectedTestSessionContainerUID) throws ParseException, TokenMgrError {
        if (this.parseMode != ParseMode.GET_UID) {
            if (expectedTestSessionContainerUID != null) {
                this.parseMode = ParseMode.CHECK_UID;
            } else {
                this.parseMode = ParseMode.DO_NOT_CHECK_UID;
            }
        }
        c.startLog();
        label_1: while (true) {
            TestCase(c, expectedTestSessionContainerUID);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case TEST_SESSION_CONTAINER:
                    ;
                    break;
                default:
                    jj_la1[0] = jj_gen;
                    break label_1;
            }
        }
        c.closeLog();
        label_2: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case EOL:
                    ;
                    break;
                default:
                    jj_la1[1] = jj_gen;
                    break label_2;
            }
            jj_consume_token(EOL);
        }
        jj_consume_token(0);
    }

    public final void TestCase(CoverageResultLog c, String expectedTestSessionContainerUID) throws ParseException {
        TestSessionContainer(c, expectedTestSessionContainerUID);
        StartTestCase(c, this.testSessionContainerUID);
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case START_SECTION:
            case COUNTER_LITERAL:
                Section(c);
                break;
            default:
                jj_la1[2] = jj_gen;
                ;
        }
        EndTestCase(c);
    }

    public final String TestSessionContainer(CoverageResultLog c, String expectedTestSessionContainerUID) throws ParseException {
        String tempUID = null;
        jj_consume_token(TEST_SESSION_CONTAINER);
        jj_consume_token(SPACECHAR);
        tempUID = TestSessionContainerUID();
        switch(this.parseMode) {
            case CHECK_UID:
                if (!tempUID.equals(expectedTestSessionContainerUID)) {
                    {
                        if (true) throw new WrongUIDException(expectedTestSessionContainerUID, tempUID);
                    }
                }
            case DO_NOT_CHECK_UID:
            case UNKNOWN:
                if (this.testSessionContainerUID != null) {
                    if (!this.testSessionContainerUID.equals(tempUID)) {
                        {
                            if (true) throw new DifferentUIDException(this.testSessionContainerUID, tempUID);
                        }
                    }
                } else {
                    this.testSessionContainerUID = tempUID;
                }
                break;
            case GET_UID:
                this.testSessionContainerUID = tempUID;
                {
                    if (true) throw new UIDFoundException();
                }
                break;
        }
        jj_consume_token(EOL);
        {
            if (true) return this.testSessionContainerUID;
        }
        throw new Error("Missing return statement in function");
    }

    public final String TestSessionContainerUID() throws ParseException {
        String testSessionContainerUID = null;
        testSessionContainerUID = StringLiteral();
        {
            if (true) return testSessionContainerUID.trim();
        }
        throw new Error("Missing return statement in function");
    }

    public final void Section(CoverageResultLog c) throws ParseException {
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case START_SECTION:
                label_3: while (true) {
                    NamedSection(c);
                    switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                        case START_SECTION:
                            ;
                            break;
                        default:
                            jj_la1[3] = jj_gen;
                            break label_3;
                    }
                }
                break;
            case COUNTER_LITERAL:
                UnnamedSection(c);
                break;
            default:
                jj_la1[4] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
    }

    public final void NamedSection(CoverageResultLog c) throws ParseException {
        StartSection(c);
        label_4: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case COUNTER_LITERAL:
                    ;
                    break;
                default:
                    jj_la1[5] = jj_gen;
                    break label_4;
            }
            Counter(c);
        }
    }

    public final void UnnamedSection(CoverageResultLog c) throws ParseException {
        label_5: while (true) {
            Counter(c);
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case COUNTER_LITERAL:
                    ;
                    break;
                default:
                    jj_la1[6] = jj_gen;
                    break label_5;
            }
        }
    }

    public final void StartTestCase(CoverageResultLog c, String foundTestSessionContainerUID) throws ParseException {
        String testCaseName = null;
        long timeStamp = -1;
        String testCaseComment = null;
        jj_consume_token(START_TEST_CASE);
        jj_consume_token(SPACECHAR);
        testCaseName = TestCaseName();
        if (jj_2_1(2)) {
            jj_consume_token(SPACECHAR);
            timeStamp = TimeStamp();
        } else {
            ;
        }
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case SPACECHAR:
                jj_consume_token(SPACECHAR);
                testCaseComment = TestCaseComment();
                break;
            default:
                jj_la1[7] = jj_gen;
                ;
        }
        c.startTestCase(foundTestSessionContainerUID, testCaseName, timeStamp, testCaseComment);
        jj_consume_token(EOL);
    }

    public final void EndTestCase(CoverageResultLog c) throws ParseException {
        String testCaseName = null;
        long timeStamp = -1;
        String resultComment = null;
        jj_consume_token(END_TEST_CASE);
        jj_consume_token(SPACECHAR);
        testCaseName = TestCaseName();
        if (jj_2_2(2)) {
            jj_consume_token(SPACECHAR);
            timeStamp = TimeStamp();
        } else {
            ;
        }
        switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case SPACECHAR:
                jj_consume_token(SPACECHAR);
                resultComment = TestCaseComment();
                break;
            default:
                jj_la1[8] = jj_gen;
                ;
        }
        c.endTestCase(testCaseName, timeStamp, resultComment);
        jj_consume_token(EOL);
    }

    public final String TestCaseName() throws ParseException {
        String testCaseName;
        testCaseName = StringLiteral();
        {
            if (true) return testCaseName.trim();
        }
        throw new Error("Missing return statement in function");
    }

    public final long TimeStamp() throws ParseException {
        long timeStamp;
        timeStamp = IntegerLiteral();
        {
            if (true) return timeStamp;
        }
        throw new Error("Missing return statement in function");
    }

    public final String TestCaseComment() throws ParseException {
        String testCaseComment;
        testCaseComment = StringLiteral();
        {
            if (true) return testCaseComment.trim();
        }
        throw new Error("Missing return statement in function");
    }

    public final void StartSection(CoverageResultLog c) throws ParseException {
        String sectionName;
        jj_consume_token(START_SECTION);
        jj_consume_token(SPACECHAR);
        sectionName = SectionName();
        c.startNamedSection(sectionName);
        jj_consume_token(EOL);
    }

    public final String SectionName() throws ParseException {
        String sectionName;
        sectionName = StringLiteral();
        {
            if (true) return sectionName.trim();
        }
        throw new Error("Missing return statement in function");
    }

    public final long IntegerLiteral() throws ParseException {
        Token integerLiteral;
        integerLiteral = jj_consume_token(INTEGER_LITERAL);
        {
            if (true) return Long.parseLong(integerLiteral.toString());
        }
        throw new Error("Missing return statement in function");
    }

    public final String StringLiteral() throws ParseException {
        Token stringLiteral;
        stringLiteral = jj_consume_token(STRING_LITERAL);
        {
            if (true) return StringUtil.parseStringLiteral(stringLiteral.toString());
        }
        throw new Error("Missing return statement in function");
    }

    public final void Counter(CoverageResultLog c) throws ParseException {
        String counterID;
        long counterValue;
        counterID = CounterID();
        jj_consume_token(SPACECHAR);
        counterValue = IntegerLiteral();
        c.passCounter(counterID.toString(), counterValue);
        jj_consume_token(EOL);
    }

    public final String CounterID() throws ParseException {
        String counterID;
        Token token;
        token = jj_consume_token(COUNTER_LITERAL);
        counterID = token.toString();
        token = jj_consume_token(INTEGER_LITERAL);
        counterID = counterID + token.toString();
        label_6: while (true) {
            switch((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
                case HYPHEN:
                    ;
                    break;
                default:
                    jj_la1[9] = jj_gen;
                    break label_6;
            }
            token = jj_consume_token(HYPHEN);
            counterID = counterID + token.toString();
            token = jj_consume_token(INTEGER_LITERAL);
            counterID = counterID + token.toString();
        }
        {
            if (true) return counterID;
        }
        throw new Error("Missing return statement in function");
    }

    private final boolean jj_2_1(int xla) {
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

    private final boolean jj_2_2(int xla) {
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

    private final boolean jj_3R_8() {
        if (jj_scan_token(INTEGER_LITERAL)) return true;
        return false;
    }

    private final boolean jj_3R_7() {
        if (jj_3R_8()) return true;
        return false;
    }

    private final boolean jj_3_1() {
        if (jj_scan_token(SPACECHAR)) return true;
        if (jj_3R_7()) return true;
        return false;
    }

    private final boolean jj_3_2() {
        if (jj_scan_token(SPACECHAR)) return true;
        if (jj_3R_7()) return true;
        return false;
    }

    public CoverageLogParserTokenManager token_source;

    SimpleCharStream jj_input_stream;

    public Token token, jj_nt;

    private int jj_ntk;

    private Token jj_scanpos, jj_lastpos;

    private int jj_la;

    public boolean lookingAhead = false;

    private boolean jj_semLA;

    private int jj_gen;

    private final int[] jj_la1 = new int[10];

    private static int[] jj_la1_0;

    static {
        jj_la1_0();
    }

    private static void jj_la1_0() {
        jj_la1_0 = new int[] { 0x40, 0x8, 0x300, 0x100, 0x300, 0x200, 0x200, 0x4, 0x4, 0x20 };
    }

    private final JJCalls[] jj_2_rtns = new JJCalls[2];

    private boolean jj_rescan = false;

    private int jj_gc = 0;

    public CoverageLogParser(java.io.InputStream stream) {
        this(stream, null);
    }

    public CoverageLogParser(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new CoverageLogParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

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
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public CoverageLogParser(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new CoverageLogParserTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public CoverageLogParser(CoverageLogParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    public void ReInit(CoverageLogParserTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 10; i++) jj_la1[i] = -1;
        for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
    }

    private final Token jj_consume_token(int kind) throws ParseException {
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

    static final class LookaheadSuccess extends java.lang.Error {
    }

    private final LookaheadSuccess jj_ls = new LookaheadSuccess();

    private final boolean jj_scan_token(int kind) {
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

    public final Token getNextToken() {
        if (token.next != null) token = token.next; else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    public final Token getToken(int index) {
        Token t = lookingAhead ? jj_scanpos : token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next; else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private final int jj_ntk() {
        if ((jj_nt = token.next) == null) return (jj_ntk = (token.next = token_source.getNextToken()).kind); else return (jj_ntk = jj_nt.kind);
    }

    private java.util.Vector jj_expentries = new java.util.Vector();

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
            boolean exists = false;
            for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements(); ) {
                int[] oldentry = (int[]) (e.nextElement());
                if (oldentry.length == jj_expentry.length) {
                    exists = true;
                    for (int i = 0; i < jj_expentry.length; i++) {
                        if (oldentry[i] != jj_expentry[i]) {
                            exists = false;
                            break;
                        }
                    }
                    if (exists) break;
                }
            }
            if (!exists) jj_expentries.addElement(jj_expentry);
            if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
        }
    }

    public ParseException generateParseException() {
        jj_expentries.removeAllElements();
        boolean[] la1tokens = new boolean[12];
        for (int i = 0; i < 12; i++) {
            la1tokens[i] = false;
        }
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 10; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.addElement(jj_expentry);
            }
        }
        jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = (int[]) jj_expentries.elementAt(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private final void jj_rescan_token() {
        jj_rescan = true;
        for (int i = 0; i < 2; i++) {
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
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess ls) {
            }
        }
        jj_rescan = false;
    }

    private final void jj_save(int index, int xla) {
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
