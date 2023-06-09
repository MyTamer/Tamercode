package kshos.command.grammar;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class OSVM_grammarLexer extends Lexer {

    public static final int STIN = 6;

    public static final int SP = 5;

    public static final int STOUT = 7;

    public static final int CHAR = 11;

    public static final int BG = 9;

    public static final int PIPE = 8;

    public static final int ICHAR = 12;

    public static final int NL = 4;

    public static final int EOF = -1;

    public static final int STRING = 10;

    public OSVM_grammarLexer() {
        ;
    }

    public OSVM_grammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public OSVM_grammarLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String getGrammarFileName() {
        return "D:\\!OS\\gramatika\\OSVM_grammar.g";
    }

    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                int alt1 = 10;
                alt1 = dfa1.predict(input);
                switch(alt1) {
                    case 1:
                        {
                            matchRange('a', 'z');
                        }
                        break;
                    case 2:
                        {
                            matchRange('A', 'Z');
                        }
                        break;
                    case 3:
                        {
                            matchRange('0', '9');
                        }
                        break;
                    case 4:
                        {
                            match('/');
                        }
                        break;
                    case 5:
                        {
                            match('_');
                        }
                        break;
                    case 6:
                        {
                            match('-');
                        }
                        break;
                    case 7:
                        {
                            match('?');
                        }
                        break;
                    case 8:
                        {
                            match('.');
                        }
                        break;
                    case 9:
                        {
                            match("..");
                        }
                        break;
                    case 10:
                        {
                            match(':');
                        }
                        break;
                }
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                loop2: do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '-' && LA2_0 <= ':') || LA2_0 == '?' || (LA2_0 >= 'A' && LA2_0 <= 'Z') || LA2_0 == '_' || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
                        alt2 = 1;
                    }
                    switch(alt2) {
                        case 1:
                            {
                                mCHAR();
                            }
                            break;
                        default:
                            break loop2;
                    }
                } while (true);
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mICHAR() throws RecognitionException {
        try {
            int _type = ICHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                if ((input.LA(1) >= ' ' && input.LA(1) <= '\t') || (input.LA(1) >= '' && input.LA(1) <= '\f') || (input.LA(1) >= '' && input.LA(1) <= '') || (input.LA(1) >= '!' && input.LA(1) <= '%') || (input.LA(1) >= '\'' && input.LA(1) <= ';') || input.LA(1) == '=' || (input.LA(1) >= '?' && input.LA(1) <= '{') || (input.LA(1) >= '}' && input.LA(1) <= '￿')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                _channel = HIDDEN;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mPIPE() throws RecognitionException {
        try {
            int _type = PIPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('|');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSTIN() throws RecognitionException {
        try {
            int _type = STIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('<');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSTOUT() throws RecognitionException {
        try {
            int _type = STOUT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('>');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mSP() throws RecognitionException {
        try {
            int _type = SP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match(' ');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mNL() throws RecognitionException {
        try {
            int _type = NL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int alt3 = 3;
            int LA3_0 = input.LA(1);
            if ((LA3_0 == '\r')) {
                int LA3_1 = input.LA(2);
                if ((LA3_1 == '\n')) {
                    alt3 = 1;
                } else {
                    alt3 = 2;
                }
            } else if ((LA3_0 == '\n')) {
                alt3 = 3;
            } else {
                NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
                throw nvae;
            }
            switch(alt3) {
                case 1:
                    {
                        match("\r\n");
                    }
                    break;
                case 2:
                    {
                        match('\r');
                    }
                    break;
                case 3:
                    {
                        match('\n');
                    }
                    break;
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public final void mBG() throws RecognitionException {
        try {
            int _type = BG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
                match('&');
            }
            state.type = _type;
            state.channel = _channel;
        } finally {
        }
    }

    public void mTokens() throws RecognitionException {
        int alt4 = 9;
        alt4 = dfa4.predict(input);
        switch(alt4) {
            case 1:
                {
                    mCHAR();
                }
                break;
            case 2:
                {
                    mSTRING();
                }
                break;
            case 3:
                {
                    mICHAR();
                }
                break;
            case 4:
                {
                    mPIPE();
                }
                break;
            case 5:
                {
                    mSTIN();
                }
                break;
            case 6:
                {
                    mSTOUT();
                }
                break;
            case 7:
                {
                    mSP();
                }
                break;
            case 8:
                {
                    mNL();
                }
                break;
            case 9:
                {
                    mBG();
                }
                break;
        }
    }

    protected DFA1 dfa1 = new DFA1(this);

    protected DFA4 dfa4 = new DFA4(this);

    static final String DFA1_eotS = "\10￿\1\13\3￿";

    static final String DFA1_eofS = "\14￿";

    static final String DFA1_minS = "\1\55\7￿\1\56\3￿";

    static final String DFA1_maxS = "\1\172\7￿\1\56\3￿";

    static final String DFA1_acceptS = "\1￿\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1￿\1\12\1\11\1\10";

    static final String DFA1_specialS = "\14￿}>";

    static final String[] DFA1_transitionS = { "\1\6\1\10\1\4\12\3\1\11\4￿\1\7\1￿\32\2\4￿\1" + "\5\1￿\32\1", "", "", "", "", "", "", "", "\1\12", "", "", "" };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);

    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);

    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);

    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);

    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);

    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);

    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }

        public String getDescription() {
            return "107:8: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '/' | '_' | '-' | '?' | '.' | '..' | ':' )";
        }
    }

    static final String DFA4_eotS = "\1\12\11\22\11￿\1\22";

    static final String DFA4_eofS = "\24￿";

    static final String DFA4_minS = "\1\0\11\55\11￿\1\55";

    static final String DFA4_maxS = "\1￿\11\172\11￿\1\172";

    static final String DFA4_acceptS = "\12￿\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\1\1￿";

    static final String DFA4_specialS = "\1\0\23￿}>";

    static final String[] DFA4_transitionS = { "\12\13\1\20\2\13\1\20\22\13\1\17\5\13\1\21\6\13\1\6\1\10\1" + "\4\12\3\1\11\1\13\1\15\1\13\1\16\1\7\1\13\32\2\4\13\1\5\1\13" + "\32\1\1\13\1\14ﾃ\13", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "\1\12\1\23\14\12\4￿\1\12\1￿\32\12\4￿\1\12\1" + "￿\32\12", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12", "", "", "", "", "", "", "", "", "", "\16\12\4￿\1\12\1￿\32\12\4￿\1\12\1￿\32" + "\12" };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);

    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);

    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);

    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);

    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);

    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);

    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i = 0; i < numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }

        public String getDescription() {
            return "1:1: Tokens : ( CHAR | STRING | ICHAR | PIPE | STIN | STOUT | SP | NL | BG );";
        }

        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
            int _s = s;
            switch(s) {
                case 0:
                    int LA4_0 = input.LA(1);
                    s = -1;
                    if (((LA4_0 >= 'a' && LA4_0 <= 'z'))) {
                        s = 1;
                    } else if (((LA4_0 >= 'A' && LA4_0 <= 'Z'))) {
                        s = 2;
                    } else if (((LA4_0 >= '0' && LA4_0 <= '9'))) {
                        s = 3;
                    } else if ((LA4_0 == '/')) {
                        s = 4;
                    } else if ((LA4_0 == '_')) {
                        s = 5;
                    } else if ((LA4_0 == '-')) {
                        s = 6;
                    } else if ((LA4_0 == '?')) {
                        s = 7;
                    } else if ((LA4_0 == '.')) {
                        s = 8;
                    } else if ((LA4_0 == ':')) {
                        s = 9;
                    } else if (((LA4_0 >= ' ' && LA4_0 <= '\t') || (LA4_0 >= '' && LA4_0 <= '\f') || (LA4_0 >= '' && LA4_0 <= '') || (LA4_0 >= '!' && LA4_0 <= '%') || (LA4_0 >= '\'' && LA4_0 <= ',') || LA4_0 == ';' || LA4_0 == '=' || LA4_0 == '@' || (LA4_0 >= '[' && LA4_0 <= '^') || LA4_0 == '`' || LA4_0 == '{' || (LA4_0 >= '}' && LA4_0 <= '￿'))) {
                        s = 11;
                    } else if ((LA4_0 == '|')) {
                        s = 12;
                    } else if ((LA4_0 == '<')) {
                        s = 13;
                    } else if ((LA4_0 == '>')) {
                        s = 14;
                    } else if ((LA4_0 == ' ')) {
                        s = 15;
                    } else if ((LA4_0 == '\n' || LA4_0 == '\r')) {
                        s = 16;
                    } else if ((LA4_0 == '&')) {
                        s = 17;
                    } else s = 10;
                    if (s >= 0) return s;
                    break;
            }
            NoViableAltException nvae = new NoViableAltException(getDescription(), 4, _s, input);
            error(nvae);
            throw nvae;
        }
    }
}
