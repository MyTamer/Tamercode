package org.deft.artifacttypesupport.codefile.java.parser;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;

public class CStyleCommentsLexer extends Lexer {

    public static final int Stringliteral = 8;

    public static final int Newlinecharacter = 4;

    public static final int Singlelinecomment = 7;

    public static final int Newline = 5;

    public static final int Delimitedcomment = 6;

    public static final int EOF = -1;

    public CStyleCommentsLexer() {
        ;
    }

    public CStyleCommentsLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public CStyleCommentsLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "CStyleCommentsLexer.g";
    }

    public Token nextToken() {
        while (true) {
            if (input.LA(1) == CharStream.EOF) {
                return Token.EOF_TOKEN;
            }
            state.token = null;
            state.channel = Token.DEFAULT_CHANNEL;
            state.tokenStartCharIndex = input.index();
            state.tokenStartCharPositionInLine = input.getCharPositionInLine();
            state.tokenStartLine = input.getLine();
            state.text = null;
            try {
                int m = input.mark();
                state.backtracking = 1;
                state.failed = false;
                mTokens();
                state.backtracking = 0;
                if (state.failed) {
                    input.rewind(m);
                    input.consume();
                } else {
                    emit();
                    return state.token;
                }
            } catch (RecognitionException re) {
                reportError(re);
                recover(re);
            }
        }
    }

    public void memoize(IntStream input, int ruleIndex, int ruleStartIndex) {
        if (state.backtracking > 1) super.memoize(input, ruleIndex, ruleStartIndex);
    }

    public boolean alreadyParsedRule(IntStream input, int ruleIndex) {
        if (state.backtracking > 1) return super.alreadyParsedRule(input, ruleIndex);
        return false;
    }

    public final void mNewline() throws RecognitionException {
        try {
            int _type = Newline;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                mNewlinecharacter();
                if (state.failed) return;
                if (state.backtracking == 1) {
                    _channel = HIDDEN;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNewlinecharacter() throws RecognitionException {
        try {
            int alt1 = 6;
            switch(input.LA(1)) {
                case '\r':
                    {
                        int LA1_1 = input.LA(2);
                        if ((LA1_1 == '\n')) {
                            alt1 = 1;
                        } else {
                            alt1 = 2;
                        }
                    }
                    break;
                case '\n':
                    {
                        alt1 = 3;
                    }
                    break;
                case '':
                    {
                        alt1 = 4;
                    }
                    break;
                case ' ':
                    {
                        alt1 = 5;
                    }
                    break;
                case ' ':
                    {
                        alt1 = 6;
                    }
                    break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae = new NoViableAltException("", 1, 0, input);
                    throw nvae;
            }
            switch(alt1) {
                case 1:
                    {
                        match("\r\n");
                        if (state.failed) return;
                    }
                    break;
                case 2:
                    {
                        match('\r');
                        if (state.failed) return;
                    }
                    break;
                case 3:
                    {
                        match('\n');
                        if (state.failed) return;
                    }
                    break;
                case 4:
                    {
                        match('');
                        if (state.failed) return;
                    }
                    break;
                case 5:
                    {
                        match(' ');
                        if (state.failed) return;
                    }
                    break;
                case 6:
                    {
                        match(' ');
                        if (state.failed) return;
                    }
                    break;
            }
        } finally {
        }
    }

    public final void mDelimitedcomment() throws RecognitionException {
        try {
            int _type = Delimitedcomment;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("/*");
                if (state.failed) return;
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if ((LA2_0 == '*')) {
                        int LA2_1 = input.LA(2);
                        if ((LA2_1 == '/')) {
                            alt2 = 2;
                        } else if (((LA2_1 >= ' ' && LA2_1 <= '.') || (LA2_1 >= '0' && LA2_1 <= '￿'))) {
                            alt2 = 1;
                        }
                    } else if (((LA2_0 >= ' ' && LA2_0 <= ')') || (LA2_0 >= '+' && LA2_0 <= '￿'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                matchAny();
                                if (state.failed) return;
                            }
                            break;
                        default:
                            break loop2;
                    }
                } while (true);
                match("*/");
                if (state.failed) return;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSinglelinecomment() throws RecognitionException {
        try {
            int _type = Singlelinecomment;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match("//");
                if (state.failed) return;
                loop3: do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);
                    if (((LA3_0 >= ' ' && LA3_0 <= '\t') || (LA3_0 >= '' && LA3_0 <= '\f') || (LA3_0 >= '' && LA3_0 <= '') || (LA3_0 >= '' && LA3_0 <= '‧') || (LA3_0 >= '‪' && LA3_0 <= '￿'))) {
                        alt3 = 1;
                    }
                    switch(alt3) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '\t') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '') || (input.LA(1) >= '' && input.LA(1) <= '‧') || (input.LA(1) >= '‪' && input.LA(1) <= '￿')) {
                                    input.consume();
                                    state.failed = false;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return;
                                    }
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop3;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mStringliteral() throws RecognitionException {
        try {
            int _type = Stringliteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('\"');
                if (state.failed) return;
                loop4: do {
                    int alt4 = 2;
                    int LA4_0 = input.LA(1);
                    if ((LA4_0 == '\"')) {
                        alt4 = 2;
                    } else if (((LA4_0 >= ' ' && LA4_0 <= '\t') || (LA4_0 >= '' && LA4_0 <= '\f') || (LA4_0 >= '' && LA4_0 <= '!') || (LA4_0 >= '#' && LA4_0 <= '') || (LA4_0 >= '' && LA4_0 <= '‧') || (LA4_0 >= '‪' && LA4_0 <= '￿'))) {
                        alt4 = 1;
                    }
                    switch(alt4) {
                        case 1:
                            {
                                if ((input.LA(1) >= ' ' && input.LA(1) <= '\t') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '') || (input.LA(1) >= '' && input.LA(1) <= '‧') || (input.LA(1) >= '‪' && input.LA(1) <= '￿')) {
                                    input.consume();
                                    state.failed = false;
                                } else {
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return;
                                    }
                                    MismatchedSetException mse = new MismatchedSetException(null, input);
                                    recover(mse);
                                    throw mse;
                                }
                            }
                            break;
                        default:
                            break loop4;
                    }
                } while (true);
                match('\"');
                if (state.failed) return;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt5 = 4;
        switch(input.LA(1)) {
            case '\n':
            case '\r':
            case '':
            case ' ':
            case ' ':
                {
                    alt5 = 1;
                }
                break;
            case '\"':
                {
                    alt5 = 4;
                }
                break;
            case '/':
                {
                    int LA5_3 = input.LA(2);
                    if ((synpred2_CStyleCommentsLexer())) {
                        alt5 = 2;
                    } else if ((synpred3_CStyleCommentsLexer())) {
                        alt5 = 3;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae = new NoViableAltException("", 5, 3, input);
                        throw nvae;
                    }
                }
                break;
            default:
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae = new NoViableAltException("", 5, 0, input);
                throw nvae;
        }
        switch(alt5) {
            case 1:
                {
                    mNewline();
                    if (state.failed) return;
                }
                break;
            case 2:
                {
                    mDelimitedcomment();
                    if (state.failed) return;
                }
                break;
            case 3:
                {
                    mSinglelinecomment();
                    if (state.failed) return;
                }
                break;
            case 4:
                {
                    mStringliteral();
                    if (state.failed) return;
                }
                break;
        }
    }

    public final void synpred2_CStyleCommentsLexer_fragment() throws RecognitionException {
        {
            mDelimitedcomment();
            if (state.failed) return;
        }
    }

    public final void synpred3_CStyleCommentsLexer_fragment() throws RecognitionException {
        {
            mSinglelinecomment();
            if (state.failed) return;
        }
    }

    public final boolean synpred2_CStyleCommentsLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_CStyleCommentsLexer_fragment();
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred3_CStyleCommentsLexer() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred3_CStyleCommentsLexer_fragment();
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }
}
