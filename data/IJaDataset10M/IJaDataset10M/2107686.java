package org.deveel.csharpcc.cstree;

import org.deveel.csharpcc.parser.Options;

public class CSTreeParserTokenManager implements CSTreeParserConstants {

    public java.io.PrintStream debugStream = System.out;

    public void setDebugStream(java.io.PrintStream ds) {
        debugStream = ds;
    }

    private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2) {
        switch(pos) {
            case 0:
                if ((active0 & 0x140000L) != 0L || (active2 & 0x40400L) != 0L) return 2;
                if ((active1 & 0x80000000000000L) != 0L) return 8;
                if ((active0 & 0xfffffffffe001ffeL) != 0L || (active1 & 0xfffffffffL) != 0L || (active2 & 0x3000000L) != 0L) {
                    jjmatchedKind = 108;
                    return 32;
                }
                return -1;
            case 1:
                if ((active0 & 0x100000L) != 0L) return 0;
                if ((active0 & 0xc17ff9fffa001ffeL) != 0L || (active1 & 0xfffffffffL) != 0L || (active2 & 0x3000000L) != 0L) {
                    if (jjmatchedPos != 1) {
                        jjmatchedKind = 108;
                        jjmatchedPos = 1;
                    }
                    return 32;
                }
                if ((active0 & 0x3e80060004000000L) != 0L) return 32;
                return -1;
            case 2:
                if ((active0 & 0x1c30000000001000L) != 0L || (active1 & 0x1001022L) != 0L) return 32;
                if ((active0 & 0xc14ffdfffa000ffeL) != 0L || (active1 & 0xffeffefddL) != 0L || (active2 & 0x3000000L) != 0L) {
                    if (jjmatchedPos != 2) {
                        jjmatchedKind = 108;
                        jjmatchedPos = 2;
                    }
                    return 32;
                }
                return -1;
            case 3:
                if ((active0 & 0x192fe5fb220009feL) != 0L || (active1 & 0xdfa5fefd9L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 3;
                    return 32;
                }
                if ((active0 & 0xc0401804d8000600L) != 0L || (active1 & 0x204a00004L) != 0L) return 32;
                return -1;
            case 4:
                if ((active0 & 0x1922c5e20200017eL) != 0L || (active1 & 0x5721eafd9L) != 0L) {
                    if (jjmatchedPos != 4) {
                        jjmatchedKind = 108;
                        jjmatchedPos = 4;
                    }
                    return 32;
                }
                if ((active0 & 0xd201920000880L) != 0L || (active1 & 0x888414000L) != 0L) return 32;
                return -1;
            case 5:
                if ((active0 & 0x840000000000L) != 0L || (active1 & 0x621ea488L) != 0L) return 32;
                if ((active0 & 0x192241e20200097eL) != 0L || (active1 & 0x510000b51L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 5;
                    return 32;
                }
                return -1;
            case 6:
                if ((active0 & 0x190041200200097cL) != 0L || (active1 & 0x410000a51L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 6;
                    return 32;
                }
                if ((active0 & 0x2200c200000002L) != 0L || (active1 & 0x100000100L) != 0L) return 32;
                return -1;
            case 7:
                if ((active0 & 0x80000000000097cL) != 0L || (active1 & 0x10000201L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 7;
                    return 32;
                }
                if ((active0 & 0x1100412002000000L) != 0L || (active1 & 0x400000850L) != 0L) return 32;
                return -1;
            case 8:
                if ((active0 & 0x978L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 8;
                    return 32;
                }
                if ((active0 & 0x800000000000004L) != 0L || (active1 & 0x10000201L) != 0L) return 32;
                return -1;
            case 9:
                if ((active0 & 0x918L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 9;
                    return 32;
                }
                if ((active0 & 0x60L) != 0L) return 32;
                return -1;
            case 10:
                if ((active0 & 0x910L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 10;
                    return 32;
                }
                if ((active0 & 0x8L) != 0L) return 32;
                return -1;
            case 11:
                if ((active0 & 0x900L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 11;
                    return 32;
                }
                if ((active0 & 0x10L) != 0L) return 32;
                return -1;
            case 12:
                if ((active0 & 0x800L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 12;
                    return 32;
                }
                if ((active0 & 0x100L) != 0L) return 32;
                return -1;
            case 13:
                if ((active0 & 0x800L) != 0L) {
                    jjmatchedKind = 108;
                    jjmatchedPos = 13;
                    return 32;
                }
                return -1;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0, long active1, long active2) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
    }

    private final int jjStopAtPos(int pos, int kind) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        return pos + 1;
    }

    private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
        jjmatchedKind = kind;
        jjmatchedPos = pos;
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return pos + 1;
        }
        return jjMoveNfa_0(state, pos + 1);
    }

    private final int jjMoveStringLiteralDfa0_0() {
        switch(curChar) {
            case 9:
                return jjStopAtPos(0, 14);
            case 10:
                return jjStopAtPos(0, 15);
            case 12:
                return jjStopAtPos(0, 17);
            case 13:
                return jjStopAtPos(0, 16);
            case 32:
                return jjStopAtPos(0, 13);
            case 33:
                jjmatchedKind = 123;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x4L);
            case 35:
                return jjStopAtPos(0, 151);
            case 37:
                jjmatchedKind = 142;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x400000L);
            case 38:
                jjmatchedKind = 139;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x80010L);
            case 40:
                return jjStopAtPos(0, 111);
            case 41:
                return jjStopAtPos(0, 112);
            case 42:
                jjmatchedKind = 137;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x20000L);
            case 43:
                jjmatchedKind = 135;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x8020L);
            case 44:
                return jjStopAtPos(0, 118);
            case 45:
                jjmatchedKind = 136;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x10040L);
            case 46:
                return jjStartNfaWithStates_0(0, 119, 8);
            case 47:
                jjmatchedKind = 138;
                return jjMoveStringLiteralDfa1_0(0x140000L, 0x0L, 0x40000L);
            case 58:
                return jjStopAtPos(0, 126);
            case 59:
                return jjStopAtPos(0, 117);
            case 60:
                jjmatchedKind = 122;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x1L);
            case 61:
                jjmatchedKind = 120;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x8000000000000000L, 0x0L);
            case 62:
                jjmatchedKind = 121;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x2L);
            case 63:
                return jjStopAtPos(0, 125);
            case 67:
                return jjMoveStringLiteralDfa1_0(0x40L, 0x0L, 0x0L);
            case 69:
                return jjMoveStringLiteralDfa1_0(0x1000L, 0x0L, 0x0L);
            case 73:
                return jjMoveStringLiteralDfa1_0(0x8L, 0x0L, 0x0L);
            case 76:
                return jjMoveStringLiteralDfa1_0(0x4L, 0x0L, 0x0L);
            case 77:
                return jjMoveStringLiteralDfa1_0(0x200L, 0x0L, 0x0L);
            case 80:
                return jjMoveStringLiteralDfa1_0(0x30L, 0x0L, 0x0L);
            case 83:
                return jjMoveStringLiteralDfa1_0(0x500L, 0x0L, 0x0L);
            case 84:
                return jjMoveStringLiteralDfa1_0(0x880L, 0x0L, 0x0L);
            case 91:
                return jjStopAtPos(0, 115);
            case 93:
                return jjStopAtPos(0, 116);
            case 94:
                jjmatchedKind = 141;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x200000L);
            case 97:
                return jjMoveStringLiteralDfa1_0(0x6000000L, 0x0L, 0x0L);
            case 98:
                return jjMoveStringLiteralDfa1_0(0x78000000L, 0x0L, 0x0L);
            case 99:
                return jjMoveStringLiteralDfa1_0(0x3f80000000L, 0x0L, 0x0L);
            case 100:
                return jjMoveStringLiteralDfa1_0(0x7c000000000L, 0x0L, 0x0L);
            case 101:
                return jjMoveStringLiteralDfa1_0(0xf80000000000L, 0x0L, 0x0L);
            case 102:
                return jjMoveStringLiteralDfa1_0(0x3f000000000000L, 0x0L, 0x0L);
            case 103:
                return jjMoveStringLiteralDfa1_0(0x40000000000000L, 0x0L, 0x1000000L);
            case 105:
                return jjMoveStringLiteralDfa1_0(0x3f80000000000000L, 0x0L, 0x0L);
            case 108:
                return jjMoveStringLiteralDfa1_0(0xc000000000000000L, 0x0L, 0x0L);
            case 110:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x7L, 0x0L);
            case 111:
                return jjMoveStringLiteralDfa1_0(0x2L, 0x78L, 0x0L);
            case 112:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x780L, 0x0L);
            case 114:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x3800L, 0x0L);
            case 115:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x1fc000L, 0x2000000L);
            case 116:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x3e00000L, 0x0L);
            case 117:
                return jjMoveStringLiteralDfa1_0(0x0L, 0xfc000000L, 0x0L);
            case 118:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x700000000L, 0x0L);
            case 119:
                return jjMoveStringLiteralDfa1_0(0x0L, 0x800000000L, 0x0L);
            case 123:
                return jjStopAtPos(0, 113);
            case 124:
                jjmatchedKind = 140;
                return jjMoveStringLiteralDfa1_0(0x0L, 0x0L, 0x100008L);
            case 125:
                return jjStopAtPos(0, 114);
            case 126:
                return jjStopAtPos(0, 124);
            default:
                return jjMoveNfa_0(3, 0);
        }
    }

    private final int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(0, active0, active1, active2);
            return 1;
        }
        switch(curChar) {
            case 38:
                if ((active2 & 0x10L) != 0L) return jjStopAtPos(1, 132);
                break;
            case 42:
                if ((active0 & 0x100000L) != 0L) return jjStartNfaWithStates_0(1, 20, 0);
                break;
            case 43:
                if ((active2 & 0x20L) != 0L) return jjStopAtPos(1, 133);
                break;
            case 45:
                if ((active2 & 0x40L) != 0L) return jjStopAtPos(1, 134);
                break;
            case 47:
                if ((active0 & 0x40000L) != 0L) return jjStopAtPos(1, 18);
                break;
            case 61:
                if ((active1 & 0x8000000000000000L) != 0L) return jjStopAtPos(1, 127); else if ((active2 & 0x1L) != 0L) return jjStopAtPos(1, 128); else if ((active2 & 0x2L) != 0L) return jjStopAtPos(1, 129); else if ((active2 & 0x4L) != 0L) return jjStopAtPos(1, 130); else if ((active2 & 0x8000L) != 0L) return jjStopAtPos(1, 143); else if ((active2 & 0x10000L) != 0L) return jjStopAtPos(1, 144); else if ((active2 & 0x20000L) != 0L) return jjStopAtPos(1, 145); else if ((active2 & 0x40000L) != 0L) return jjStopAtPos(1, 146); else if ((active2 & 0x80000L) != 0L) return jjStopAtPos(1, 147); else if ((active2 & 0x100000L) != 0L) return jjStopAtPos(1, 148); else if ((active2 & 0x200000L) != 0L) return jjStopAtPos(1, 149); else if ((active2 & 0x400000L) != 0L) return jjStopAtPos(1, 150);
                break;
            case 65:
                return jjMoveStringLiteralDfa2_0(active0, 0x30L, active1, 0L, active2, 0L);
            case 71:
                return jjMoveStringLiteralDfa2_0(active0, 0x8L, active1, 0L, active2, 0L);
            case 75:
                return jjMoveStringLiteralDfa2_0(active0, 0x400L, active1, 0L, active2, 0L);
            case 79:
                return jjMoveStringLiteralDfa2_0(active0, 0x1a84L, active1, 0L, active2, 0L);
            case 80:
                return jjMoveStringLiteralDfa2_0(active0, 0x100L, active1, 0L, active2, 0L);
            case 83:
                return jjMoveStringLiteralDfa2_0(active0, 0x40L, active1, 0L, active2, 0L);
            case 97:
                return jjMoveStringLiteralDfa2_0(active0, 0x1000188000000L, active1, 0x81L, active2, 0L);
            case 98:
                return jjMoveStringLiteralDfa2_0(active0, 0x2000000L, active1, 0x4008L, active2, 0L);
            case 101:
                return jjMoveStringLiteralDfa2_0(active0, 0x1c000000000L, active1, 0xb802L, active2, 0x3000000L);
            case 102:
                if ((active0 & 0x80000000000000L) != 0L) return jjStartNfaWithStates_0(1, 55, 32);
                break;
            case 104:
                return jjMoveStringLiteralDfa2_0(active0, 0x600000000L, active1, 0x800610000L, active2, 0L);
            case 105:
                return jjMoveStringLiteralDfa2_0(active0, 0x6000000000000L, active1, 0x104000000L, active2, 0L);
            case 108:
                return jjMoveStringLiteralDfa2_0(active0, 0x8080800000000L, active1, 0x8000000L, active2, 0L);
            case 109:
                return jjMoveStringLiteralDfa2_0(active0, 0x100000000000000L, active1, 0L, active2, 0L);
            case 110:
                if ((active0 & 0x200000000000000L) != 0L) {
                    jjmatchedKind = 57;
                    jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0x1c00100000000000L, active1, 0x30000000L, active2, 0L);
            case 111:
                if ((active0 & 0x20000000000L) != 0L) {
                    jjmatchedKind = 41;
                    jjmatchedPos = 1;
                }
                return jjMoveStringLiteralDfa2_0(active0, 0xc070043010000000L, active1, 0x600000000L, active2, 0L);
            case 112:
                return jjMoveStringLiteralDfa2_0(active0, 0x2L, active1, 0x10L, active2, 0L);
            case 114:
                return jjMoveStringLiteralDfa2_0(active0, 0x20000000L, active1, 0x1800300L, active2, 0L);
            case 115:
                if ((active0 & 0x4000000L) != 0L) return jjStartNfaWithStates_0(1, 26, 32); else if ((active0 & 0x2000000000000000L) != 0L) return jjStartNfaWithStates_0(1, 61, 32);
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0xc0000000L, active2, 0L);
            case 116:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0xe0000L, active2, 0L);
            case 117:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x424L, active2, 0L);
            case 118:
                return jjMoveStringLiteralDfa2_0(active0, 0x200000000000L, active1, 0x40L, active2, 0L);
            case 119:
                return jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0x100000L, active2, 0L);
            case 120:
                return jjMoveStringLiteralDfa2_0(active0, 0xc00000000000L, active1, 0L, active2, 0L);
            case 121:
                return jjMoveStringLiteralDfa2_0(active0, 0x40000000L, active1, 0x2000000L, active2, 0L);
            case 124:
                if ((active2 & 0x8L) != 0L) return jjStopAtPos(1, 131);
                break;
            default:
                break;
        }
        return jjStartNfa_0(0, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L) return jjStartNfa_0(0, old0, old1, old2);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(1, active0, active1, active2);
            return 2;
        }
        switch(curChar) {
            case 69:
                return jjMoveStringLiteralDfa3_0(active0, 0x100L, active1, 0L, active2, 0L);
            case 70:
                if ((active0 & 0x1000L) != 0L) return jjStartNfaWithStates_0(2, 12, 32);
                break;
            case 72:
                return jjMoveStringLiteralDfa3_0(active0, 0x40L, active1, 0L, active2, 0L);
            case 73:
                return jjMoveStringLiteralDfa3_0(active0, 0x400L, active1, 0L, active2, 0L);
            case 75:
                return jjMoveStringLiteralDfa3_0(active0, 0x880L, active1, 0L, active2, 0L);
            case 78:
                return jjMoveStringLiteralDfa3_0(active0, 0x8L, active1, 0L, active2, 0L);
            case 79:
                return jjMoveStringLiteralDfa3_0(active0, 0x4L, active1, 0L, active2, 0L);
            case 82:
                return jjMoveStringLiteralDfa3_0(active0, 0x230L, active1, 0L, active2, 0L);
            case 97:
                return jjMoveStringLiteralDfa3_0(active0, 0xc00000000L, active1, 0x28800L, active2, 0L);
            case 98:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x400L, active2, 0L);
            case 99:
                return jjMoveStringLiteralDfa3_0(active0, 0x4000004000000000L, active1, 0x10000000L, active2, 0L);
            case 101:
                return jjMoveStringLiteralDfa3_0(active0, 0x200220000000L, active1, 0x50L, active2, 0L);
            case 102:
                if ((active1 & 0x1000L) != 0L) return jjStartNfaWithStates_0(2, 76, 32);
                return jjMoveStringLiteralDfa3_0(active0, 0x8000000000L, active1, 0L, active2, 0L);
            case 104:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x40000000L, active2, 0L);
            case 105:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0xa80300100L, active2, 0L);
            case 106:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x8L, active2, 0L);
            case 108:
                return jjMoveStringLiteralDfa3_0(active0, 0x1010000000000L, active1, 0x400000004L, active2, 0L);
            case 109:
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x1L, active2, 0L);
            case 110:
                return jjMoveStringLiteralDfa3_0(active0, 0x8002003000000000L, active1, 0x4000000L, active2, 0L);
            case 111:
                return jjMoveStringLiteralDfa3_0(active0, 0x8000010000000L, active1, 0x8010200L, active2, 0L);
            case 112:
                return jjMoveStringLiteralDfa3_0(active0, 0x100400000000000L, active1, 0x2000000L, active2, 0L);
            case 114:
                if ((active0 & 0x10000000000000L) != 0L) {
                    jjmatchedKind = 52;
                    jjmatchedPos = 2;
                }
                return jjMoveStringLiteralDfa3_0(active0, 0x20000000000000L, active1, 0x1004c0080L, active2, 0L);
            case 115:
                return jjMoveStringLiteralDfa3_0(active0, 0x8008a000000L, active1, 0x20000000L, active2, 0L);
            case 116:
                if ((active0 & 0x400000000000000L) != 0L) {
                    jjmatchedKind = 58;
                    jjmatchedPos = 2;
                } else if ((active1 & 0x20L) != 0L) return jjStartNfaWithStates_0(2, 69, 32); else if ((active2 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(2, 108, 32); else if ((active2 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(2, 108, 32);
                return jjMoveStringLiteralDfa3_0(active0, 0x1840800140000002L, active1, 0x2000L, active2, 0L);
            case 117:
                return jjMoveStringLiteralDfa3_0(active0, 0x140000000000L, active1, 0x800000L, active2, 0L);
            case 119:
                if ((active1 & 0x2L) != 0L) return jjStartNfaWithStates_0(2, 65, 32);
                break;
            case 120:
                return jjMoveStringLiteralDfa3_0(active0, 0x4000000000000L, active1, 0L, active2, 0L);
            case 121:
                if ((active1 & 0x1000000L) != 0L) return jjStartNfaWithStates_0(2, 88, 32);
                return jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0x4000L, active2, 0L);
            default:
                break;
        }
        return jjStartNfa_0(1, active0, active1, active2);
    }

    private final int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2) {
        if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L) return jjStartNfa_0(1, old0, old1, old2);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(2, active0, active1, 0L);
            return 3;
        }
        switch(curChar) {
            case 65:
                return jjMoveStringLiteralDfa4_0(active0, 0x40L, active1, 0L);
            case 67:
                return jjMoveStringLiteralDfa4_0(active0, 0x100L, active1, 0L);
            case 69:
                if ((active0 & 0x200L) != 0L) return jjStartNfaWithStates_0(3, 9, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0x880L, active1, 0L);
            case 75:
                return jjMoveStringLiteralDfa4_0(active0, 0x4L, active1, 0L);
            case 79:
                return jjMoveStringLiteralDfa4_0(active0, 0x8L, active1, 0L);
            case 80:
                if ((active0 & 0x400L) != 0L) return jjStartNfaWithStates_0(3, 10, 32);
                break;
            case 83:
                return jjMoveStringLiteralDfa4_0(active0, 0x30L, active1, 0L);
            case 97:
                return jjMoveStringLiteralDfa4_0(active0, 0xa008020000000L, active1, 0x420000080L);
            case 98:
                return jjMoveStringLiteralDfa4_0(active0, 0x40000000000L, active1, 0L);
            case 99:
                return jjMoveStringLiteralDfa4_0(active0, 0x300000000L, active1, 0L);
            case 100:
                if ((active1 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(3, 97, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x800L);
            case 101:
                if ((active0 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(3, 27, 32); else if ((active0 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(3, 30, 32); else if ((active0 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(3, 31, 32); else if ((active0 & 0x80000000000L) != 0L) return jjStartNfaWithStates_0(3, 43, 32); else if ((active1 & 0x800000L) != 0L) return jjStartNfaWithStates_0(3, 87, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0x1824810000000000L, active1, 0x2000009L);
            case 103:
                if ((active0 & 0x8000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 63, 32);
                break;
            case 104:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x10000000L);
            case 105:
                return jjMoveStringLiteralDfa4_0(active0, 0x4000000002L, active1, 0x40000L);
            case 107:
                if ((active0 & 0x4000000000000000L) != 0L) return jjStartNfaWithStates_0(3, 62, 32);
                break;
            case 108:
                if ((active0 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(3, 28, 32); else if ((active1 & 0x4L) != 0L) return jjStartNfaWithStates_0(3, 66, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0x100400000000000L, active1, 0x800008400L);
            case 109:
                if ((active0 & 0x100000000000L) != 0L) return jjStartNfaWithStates_0(3, 44, 32);
                break;
            case 110:
                return jjMoveStringLiteralDfa4_0(active0, 0x200000000000L, active1, 0x88000000L);
            case 111:
                if ((active0 & 0x40000000000000L) != 0L) return jjStartNfaWithStates_0(3, 54, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x40400000L);
            case 114:
                if ((active0 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(3, 34, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x10050L);
            case 115:
                if ((active1 & 0x200000L) != 0L) return jjStartNfaWithStates_0(3, 85, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0x1001800000000L, active1, 0L);
            case 116:
                if ((active1 & 0x4000000L) != 0L) return jjStartNfaWithStates_0(3, 90, 32);
                return jjMoveStringLiteralDfa4_0(active0, 0x2002000000L, active1, 0x100124200L);
            case 117:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x82000L);
            case 118:
                return jjMoveStringLiteralDfa4_0(active0, 0L, active1, 0x100L);
            default:
                break;
        }
        return jjStartNfa_0(2, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(2, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(3, active0, active1, 0L);
            return 4;
        }
        switch(curChar) {
            case 65:
                return jjMoveStringLiteralDfa5_0(active0, 0x4L, active1, 0L);
            case 69:
                return jjMoveStringLiteralDfa5_0(active0, 0x30L, active1, 0L);
            case 73:
                return jjMoveStringLiteralDfa5_0(active0, 0x100L, active1, 0L);
            case 78:
                if ((active0 & 0x80L) != 0L) {
                    jjmatchedKind = 7;
                    jjmatchedPos = 4;
                }
                return jjMoveStringLiteralDfa5_0(active0, 0x800L, active1, 0L);
            case 82:
                return jjMoveStringLiteralDfa5_0(active0, 0x48L, active1, 0L);
            case 97:
                return jjMoveStringLiteralDfa5_0(active0, 0x20000000000000L, active1, 0x110L);
            case 99:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x180008L);
            case 100:
                if ((active0 & 0x4000000000000L) != 0L) return jjStartNfaWithStates_0(4, 50, 32);
                break;
            case 101:
                if ((active0 & 0x1000000000000L) != 0L) return jjStartNfaWithStates_0(4, 48, 32); else if ((active1 & 0x4000L) != 0L) return jjStartNfaWithStates_0(4, 78, 32); else if ((active1 & 0x800000000L) != 0L) return jjStartNfaWithStates_0(4, 99, 32);
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x10008200L);
            case 102:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x20000000L);
            case 103:
                if ((active1 & 0x8000000L) != 0L) return jjStartNfaWithStates_0(4, 91, 32); else if ((active1 & 0x80000000L) != 0L) return jjStartNfaWithStates_0(4, 95, 32);
                return jjMoveStringLiteralDfa5_0(active0, 0x10000000000L, active1, 0L);
            case 104:
                if ((active0 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(4, 32, 32);
                break;
            case 105:
                return jjMoveStringLiteralDfa5_0(active0, 0x100402000000000L, active1, 0x20400L);
            case 107:
                if ((active0 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(4, 29, 32);
                return jjMoveStringLiteralDfa5_0(active0, 0x200000000L, active1, 0L);
            case 108:
                return jjMoveStringLiteralDfa5_0(active0, 0x2040000000000L, active1, 0L);
            case 109:
                return jjMoveStringLiteralDfa5_0(active0, 0x4000000000L, active1, 0x80L);
            case 110:
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x40000L);
            case 111:
                return jjMoveStringLiteralDfa5_0(active0, 0x2L, active1, 0x2000800L);
            case 114:
                return jjMoveStringLiteralDfa5_0(active0, 0x1800800002000000L, active1, 0x40002040L);
            case 115:
                if ((active0 & 0x800000000L) != 0L) return jjStartNfaWithStates_0(4, 35, 32);
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x1L);
            case 116:
                if ((active0 & 0x1000000000L) != 0L) return jjStartNfaWithStates_0(4, 36, 32); else if ((active0 & 0x200000000000L) != 0L) return jjStartNfaWithStates_0(4, 45, 32); else if ((active0 & 0x8000000000000L) != 0L) return jjStartNfaWithStates_0(4, 51, 32); else if ((active1 & 0x10000L) != 0L) return jjStartNfaWithStates_0(4, 80, 32);
                return jjMoveStringLiteralDfa5_0(active0, 0L, active1, 0x400000000L);
            case 117:
                return jjMoveStringLiteralDfa5_0(active0, 0x8000000000L, active1, 0x100000000L);
            case 119:
                if ((active1 & 0x400000L) != 0L) return jjStartNfaWithStates_0(4, 86, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(3, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(3, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(4, active0, active1, 0L);
            return 5;
        }
        switch(curChar) {
            case 65:
                return jjMoveStringLiteralDfa6_0(active0, 0x100L, active1, 0L);
            case 69:
                return jjMoveStringLiteralDfa6_0(active0, 0x8L, active1, 0L);
            case 72:
                return jjMoveStringLiteralDfa6_0(active0, 0x4L, active1, 0L);
            case 80:
                return jjMoveStringLiteralDfa6_0(active0, 0x40L, active1, 0L);
            case 82:
                return jjMoveStringLiteralDfa6_0(active0, 0x30L, active1, 0L);
            case 95:
                return jjMoveStringLiteralDfa6_0(active0, 0x800L, active1, 0L);
            case 97:
                return jjMoveStringLiteralDfa6_0(active0, 0x14002000000L, active1, 0x100000000L);
            case 99:
                if ((active1 & 0x400L) != 0L) return jjStartNfaWithStates_0(5, 74, 32); else if ((active1 & 0x20000L) != 0L) return jjStartNfaWithStates_0(5, 81, 32);
                return jjMoveStringLiteralDfa6_0(active0, 0x120400000000000L, active1, 0x10000200L);
            case 100:
                if ((active1 & 0x8000L) != 0L) return jjStartNfaWithStates_0(5, 79, 32);
                break;
            case 101:
                if ((active0 & 0x40000000000L) != 0L) return jjStartNfaWithStates_0(5, 42, 32); else if ((active1 & 0x20000000L) != 0L) return jjStartNfaWithStates_0(5, 93, 32);
                return jjMoveStringLiteralDfa6_0(active0, 0x200000000L, active1, 0L);
            case 102:
                if ((active1 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(5, 89, 32);
                return jjMoveStringLiteralDfa6_0(active0, 0x800000000000000L, active1, 0L);
            case 103:
                if ((active1 & 0x40000L) != 0L) return jjStartNfaWithStates_0(5, 82, 32);
                break;
            case 104:
                if ((active1 & 0x100000L) != 0L) return jjStartNfaWithStates_0(5, 84, 32);
                break;
            case 105:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x400000040L);
            case 108:
                return jjMoveStringLiteralDfa6_0(active0, 0x2008000000000L, active1, 0L);
            case 110:
                if ((active0 & 0x800000000000L) != 0L) return jjStartNfaWithStates_0(5, 47, 32); else if ((active1 & 0x2000L) != 0L) return jjStartNfaWithStates_0(5, 77, 32);
                return jjMoveStringLiteralDfa6_0(active0, 0x1000002000000002L, active1, 0x800L);
            case 112:
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x1L);
            case 115:
                if ((active1 & 0x80L) != 0L) return jjStartNfaWithStates_0(5, 71, 32);
                break;
            case 116:
                if ((active1 & 0x8L) != 0L) return jjStartNfaWithStates_0(5, 67, 32); else if ((active1 & 0x80000L) != 0L) return jjStartNfaWithStates_0(5, 83, 32); else if ((active1 & 0x40000000L) != 0L) return jjStartNfaWithStates_0(5, 94, 32);
                return jjMoveStringLiteralDfa6_0(active0, 0L, active1, 0x110L);
            default:
                break;
        }
        return jjStartNfa_0(4, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(4, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(5, active0, active1, 0L);
            return 6;
        }
        switch(curChar) {
            case 67:
                return jjMoveStringLiteralDfa7_0(active0, 0x40L, active1, 0L);
            case 69:
                return jjMoveStringLiteralDfa7_0(active0, 0x4L, active1, 0L);
            case 76:
                return jjMoveStringLiteralDfa7_0(active0, 0x100L, active1, 0L);
            case 77:
                return jjMoveStringLiteralDfa7_0(active0, 0x800L, active1, 0L);
            case 95:
                return jjMoveStringLiteralDfa7_0(active0, 0x38L, active1, 0L);
            case 97:
                return jjMoveStringLiteralDfa7_0(active0, 0x1800000000000000L, active1, 0x1L);
            case 99:
                return jjMoveStringLiteralDfa7_0(active0, 0x2000000L, active1, 0L);
            case 100:
                if ((active0 & 0x200000000L) != 0L) return jjStartNfaWithStates_0(6, 33, 32);
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x40L);
            case 101:
                if ((active1 & 0x100L) != 0L) return jjStartNfaWithStates_0(6, 72, 32);
                break;
            case 104:
                if ((active0 & 0x20000000000000L) != 0L) return jjStartNfaWithStates_0(6, 53, 32);
                break;
            case 105:
                return jjMoveStringLiteralDfa7_0(active0, 0x100400000000000L, active1, 0L);
            case 107:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x10000000L);
            case 108:
                if ((active0 & 0x4000000000L) != 0L) return jjStartNfaWithStates_0(6, 38, 32); else if ((active1 & 0x100000000L) != 0L) return jjStartNfaWithStates_0(6, 96, 32);
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x400000800L);
            case 111:
                return jjMoveStringLiteralDfa7_0(active0, 0L, active1, 0x10L);
            case 115:
                if ((active0 & 0x2L) != 0L) return jjStartNfaWithStates_0(6, 1, 32);
                break;
            case 116:
                if ((active0 & 0x8000000000L) != 0L) return jjStartNfaWithStates_0(6, 39, 32);
                return jjMoveStringLiteralDfa7_0(active0, 0x10000000000L, active1, 0x200L);
            case 117:
                return jjMoveStringLiteralDfa7_0(active0, 0x2000000000L, active1, 0L);
            case 121:
                if ((active0 & 0x2000000000000L) != 0L) return jjStartNfaWithStates_0(6, 49, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(5, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(5, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(6, active0, active1, 0L);
            return 7;
        }
        switch(curChar) {
            case 65:
                return jjMoveStringLiteralDfa8_0(active0, 0x4L, active1, 0L);
            case 66:
                return jjMoveStringLiteralDfa8_0(active0, 0x10L, active1, 0L);
            case 67:
                return jjMoveStringLiteralDfa8_0(active0, 0x8L, active1, 0L);
            case 69:
                return jjMoveStringLiteralDfa8_0(active0, 0x20L, active1, 0L);
            case 71:
                return jjMoveStringLiteralDfa8_0(active0, 0x800L, active1, 0L);
            case 79:
                return jjMoveStringLiteralDfa8_0(active0, 0x40L, active1, 0L);
            case 95:
                return jjMoveStringLiteralDfa8_0(active0, 0x100L, active1, 0L);
            case 99:
                return jjMoveStringLiteralDfa8_0(active0, 0x800000000000000L, active1, 0x1L);
            case 101:
                if ((active0 & 0x2000000000L) != 0L) return jjStartNfaWithStates_0(7, 37, 32); else if ((active0 & 0x10000000000L) != 0L) return jjStartNfaWithStates_0(7, 40, 32); else if ((active1 & 0x40L) != 0L) return jjStartNfaWithStates_0(7, 70, 32); else if ((active1 & 0x400000000L) != 0L) return jjStartNfaWithStates_0(7, 98, 32);
                return jjMoveStringLiteralDfa8_0(active0, 0L, active1, 0x10000200L);
            case 108:
                if ((active0 & 0x1000000000000000L) != 0L) return jjStartNfaWithStates_0(7, 60, 32);
                break;
            case 114:
                if ((active1 & 0x10L) != 0L) return jjStartNfaWithStates_0(7, 68, 32);
                break;
            case 116:
                if ((active0 & 0x2000000L) != 0L) return jjStartNfaWithStates_0(7, 25, 32); else if ((active0 & 0x400000000000L) != 0L) return jjStartNfaWithStates_0(7, 46, 32); else if ((active0 & 0x100000000000000L) != 0L) return jjStartNfaWithStates_0(7, 56, 32);
                break;
            case 121:
                if ((active1 & 0x800L) != 0L) return jjStartNfaWithStates_0(7, 75, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(6, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(6, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(7, active0, active1, 0L);
            return 8;
        }
        switch(curChar) {
            case 65:
                return jjMoveStringLiteralDfa9_0(active0, 0x8L, active1, 0L);
            case 68:
                if ((active0 & 0x4L) != 0L) return jjStartNfaWithStates_0(8, 2, 32);
                return jjMoveStringLiteralDfa9_0(active0, 0x40L, active1, 0L);
            case 69:
                return jjMoveStringLiteralDfa9_0(active0, 0x10L, active1, 0L);
            case 78:
                return jjMoveStringLiteralDfa9_0(active0, 0x20L, active1, 0L);
            case 82:
                return jjMoveStringLiteralDfa9_0(active0, 0x800L, active1, 0L);
            case 84:
                return jjMoveStringLiteralDfa9_0(active0, 0x100L, active1, 0L);
            case 100:
                if ((active1 & 0x200L) != 0L) return jjStartNfaWithStates_0(8, 73, 32); else if ((active1 & 0x10000000L) != 0L) return jjStartNfaWithStates_0(8, 92, 32);
                break;
            case 101:
                if ((active0 & 0x800000000000000L) != 0L) return jjStartNfaWithStates_0(8, 59, 32); else if ((active1 & 0x1L) != 0L) return jjStartNfaWithStates_0(8, 64, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(7, active0, active1, 0L);
    }

    private final int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1) {
        if (((active0 &= old0) | (active1 &= old1)) == 0L) return jjStartNfa_0(7, old0, old1, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(8, active0, 0L, 0L);
            return 9;
        }
        switch(curChar) {
            case 68:
                if ((active0 & 0x20L) != 0L) return jjStartNfaWithStates_0(9, 5, 32);
                break;
            case 69:
                if ((active0 & 0x40L) != 0L) return jjStartNfaWithStates_0(9, 6, 32);
                break;
            case 71:
                return jjMoveStringLiteralDfa10_0(active0, 0x10L);
            case 79:
                return jjMoveStringLiteralDfa10_0(active0, 0x100L);
            case 83:
                return jjMoveStringLiteralDfa10_0(active0, 0x8L);
            case 95:
                return jjMoveStringLiteralDfa10_0(active0, 0x800L);
            default:
                break;
        }
        return jjStartNfa_0(8, active0, 0L, 0L);
    }

    private final int jjMoveStringLiteralDfa10_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(8, old0, 0L, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(9, active0, 0L, 0L);
            return 10;
        }
        switch(curChar) {
            case 68:
                return jjMoveStringLiteralDfa11_0(active0, 0x800L);
            case 69:
                if ((active0 & 0x8L) != 0L) return jjStartNfaWithStates_0(10, 3, 32);
                break;
            case 73:
                return jjMoveStringLiteralDfa11_0(active0, 0x10L);
            case 75:
                return jjMoveStringLiteralDfa11_0(active0, 0x100L);
            default:
                break;
        }
        return jjStartNfa_0(9, active0, 0L, 0L);
    }

    private final int jjMoveStringLiteralDfa11_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(9, old0, 0L, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(10, active0, 0L, 0L);
            return 11;
        }
        switch(curChar) {
            case 69:
                return jjMoveStringLiteralDfa12_0(active0, 0x900L);
            case 78:
                if ((active0 & 0x10L) != 0L) return jjStartNfaWithStates_0(11, 4, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(10, active0, 0L, 0L);
    }

    private final int jjMoveStringLiteralDfa12_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(10, old0, 0L, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(11, active0, 0L, 0L);
            return 12;
        }
        switch(curChar) {
            case 67:
                return jjMoveStringLiteralDfa13_0(active0, 0x800L);
            case 78:
                if ((active0 & 0x100L) != 0L) return jjStartNfaWithStates_0(12, 8, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(11, active0, 0L, 0L);
    }

    private final int jjMoveStringLiteralDfa13_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(11, old0, 0L, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(12, active0, 0L, 0L);
            return 13;
        }
        switch(curChar) {
            case 76:
                return jjMoveStringLiteralDfa14_0(active0, 0x800L);
            default:
                break;
        }
        return jjStartNfa_0(12, active0, 0L, 0L);
    }

    private final int jjMoveStringLiteralDfa14_0(long old0, long active0) {
        if (((active0 &= old0)) == 0L) return jjStartNfa_0(12, old0, 0L, 0L);
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            jjStopStringLiteralDfa_0(13, active0, 0L, 0L);
            return 14;
        }
        switch(curChar) {
            case 83:
                if ((active0 & 0x800L) != 0L) return jjStartNfaWithStates_0(14, 11, 32);
                break;
            default:
                break;
        }
        return jjStartNfa_0(13, active0, 0L, 0L);
    }

    private final void jjCheckNAdd(int state) {
        if (jjrounds[state] != jjround) {
            jjstateSet[jjnewStateCnt++] = state;
            jjrounds[state] = jjround;
        }
    }

    private final void jjAddStates(int start, int end) {
        do {
            jjstateSet[jjnewStateCnt++] = jjnextStates[start];
        } while (start++ != end);
    }

    private final void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private final void jjCheckNAddStates(int start, int end) {
        do {
            jjCheckNAdd(jjnextStates[start]);
        } while (start++ != end);
    }

    private final void jjCheckNAddStates(int start) {
        jjCheckNAdd(jjnextStates[start]);
        jjCheckNAdd(jjnextStates[start + 1]);
    }

    static final long[] jjbitVec0 = { 0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL };

    static final long[] jjbitVec2 = { 0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL };

    static final long[] jjbitVec3 = { 0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L };

    static final long[] jjbitVec4 = { 0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL };

    static final long[] jjbitVec5 = { 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL };

    static final long[] jjbitVec6 = { 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L };

    static final long[] jjbitVec7 = { 0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L };

    static final long[] jjbitVec8 = { 0x3fffffffffffL, 0x0L, 0x0L, 0x0L };

    private final int jjMoveNfa_0(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 52;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 3:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(0, 6); else if (curChar == 36) {
                                if (kind > 108) kind = 108;
                                jjCheckNAdd(32);
                            } else if (curChar == 34) jjCheckNAddStates(7, 9); else if (curChar == 39) jjAddStates(10, 11); else if (curChar == 46) jjCheckNAdd(8); else if (curChar == 47) jjstateSet[jjnewStateCnt++] = 2;
                            if ((0x3fe000000000000L & l) != 0L) {
                                if (kind > 100) kind = 100;
                                jjCheckNAddTwoStates(5, 6);
                            } else if (curChar == 48) {
                                if (kind > 100) kind = 100;
                                jjCheckNAddStates(12, 14);
                            }
                            break;
                        case 0:
                            if (curChar == 42) jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 1:
                            if ((0xffff7fffffffffffL & l) != 0L && kind > 19) kind = 19;
                            break;
                        case 2:
                            if (curChar == 42) jjstateSet[jjnewStateCnt++] = 0;
                            break;
                        case 4:
                            if ((0x3fe000000000000L & l) == 0L) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 5:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddTwoStates(5, 6);
                            break;
                        case 7:
                            if (curChar == 46) jjCheckNAdd(8);
                            break;
                        case 8:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddStates(15, 17);
                            break;
                        case 10:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(11);
                            break;
                        case 11:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddTwoStates(11, 12);
                            break;
                        case 13:
                            if (curChar == 39) jjAddStates(10, 11);
                            break;
                        case 14:
                            if ((0xffffff7fffffdbffL & l) != 0L) jjCheckNAdd(15);
                            break;
                        case 15:
                            if (curChar == 39 && kind > 106) kind = 106;
                            break;
                        case 17:
                            if ((0x8400000000L & l) != 0L) jjCheckNAdd(15);
                            break;
                        case 18:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAddTwoStates(19, 15);
                            break;
                        case 19:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAdd(15);
                            break;
                        case 20:
                            if ((0xf000000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 21;
                            break;
                        case 21:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAdd(19);
                            break;
                        case 22:
                            if (curChar == 34) jjCheckNAddStates(7, 9);
                            break;
                        case 23:
                            if ((0xfffffffbffffdbffL & l) != 0L) jjCheckNAddStates(7, 9);
                            break;
                        case 25:
                            if ((0x8400000000L & l) != 0L) jjCheckNAddStates(7, 9);
                            break;
                        case 26:
                            if (curChar == 34 && kind > 107) kind = 107;
                            break;
                        case 27:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAddStates(18, 21);
                            break;
                        case 28:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAddStates(7, 9);
                            break;
                        case 29:
                            if ((0xf000000000000L & l) != 0L) jjstateSet[jjnewStateCnt++] = 30;
                            break;
                        case 30:
                            if ((0xff000000000000L & l) != 0L) jjCheckNAdd(28);
                            break;
                        case 31:
                            if (curChar != 36) break;
                            if (kind > 108) kind = 108;
                            jjCheckNAdd(32);
                            break;
                        case 32:
                            if ((0x3ff001000000000L & l) == 0L) break;
                            if (kind > 108) kind = 108;
                            jjCheckNAdd(32);
                            break;
                        case 33:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(0, 6);
                            break;
                        case 34:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(34, 35);
                            break;
                        case 35:
                            if (curChar != 46) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddStates(22, 24);
                            break;
                        case 36:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddStates(22, 24);
                            break;
                        case 38:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(39);
                            break;
                        case 39:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddTwoStates(39, 12);
                            break;
                        case 40:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(40, 41);
                            break;
                        case 42:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(43);
                            break;
                        case 43:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 104) kind = 104;
                            jjCheckNAddTwoStates(43, 12);
                            break;
                        case 44:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddStates(25, 27);
                            break;
                        case 46:
                            if ((0x280000000000L & l) != 0L) jjCheckNAdd(47);
                            break;
                        case 47:
                            if ((0x3ff000000000000L & l) != 0L) jjCheckNAddTwoStates(47, 12);
                            break;
                        case 48:
                            if (curChar != 48) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddStates(12, 14);
                            break;
                        case 50:
                            if ((0x3ff000000000000L & l) == 0L) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddTwoStates(50, 6);
                            break;
                        case 51:
                            if ((0xff000000000000L & l) == 0L) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddTwoStates(51, 6);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 3:
                        case 32:
                            if ((0x7fffffe87fffffeL & l) == 0L) break;
                            if (kind > 108) kind = 108;
                            jjCheckNAdd(32);
                            break;
                        case 1:
                            if (kind > 19) kind = 19;
                            break;
                        case 6:
                            if ((0x100000001000L & l) != 0L && kind > 100) kind = 100;
                            break;
                        case 9:
                            if ((0x2000000020L & l) != 0L) jjAddStates(28, 29);
                            break;
                        case 12:
                            if ((0x5000000050L & l) != 0L && kind > 104) kind = 104;
                            break;
                        case 14:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAdd(15);
                            break;
                        case 16:
                            if (curChar == 92) jjAddStates(30, 32);
                            break;
                        case 17:
                            if ((0x14404410000000L & l) != 0L) jjCheckNAdd(15);
                            break;
                        case 23:
                            if ((0xffffffffefffffffL & l) != 0L) jjCheckNAddStates(7, 9);
                            break;
                        case 24:
                            if (curChar == 92) jjAddStates(33, 35);
                            break;
                        case 25:
                            if ((0x14404410000000L & l) != 0L) jjCheckNAddStates(7, 9);
                            break;
                        case 37:
                            if ((0x2000000020L & l) != 0L) jjAddStates(36, 37);
                            break;
                        case 41:
                            if ((0x2000000020L & l) != 0L) jjAddStates(38, 39);
                            break;
                        case 45:
                            if ((0x2000000020L & l) != 0L) jjAddStates(40, 41);
                            break;
                        case 49:
                            if ((0x100000001000000L & l) != 0L) jjCheckNAdd(50);
                            break;
                        case 50:
                            if ((0x7e0000007eL & l) == 0L) break;
                            if (kind > 100) kind = 100;
                            jjCheckNAddTwoStates(50, 6);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = (int) (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 3:
                        case 32:
                            if (!jjCanMove_1(hiByte, i1, i2, l1, l2)) break;
                            if (kind > 108) kind = 108;
                            jjCheckNAdd(32);
                            break;
                        case 1:
                            if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 19) kind = 19;
                            break;
                        case 14:
                            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) jjstateSet[jjnewStateCnt++] = 15;
                            break;
                        case 23:
                            if (jjCanMove_0(hiByte, i1, i2, l1, l2)) jjAddStates(7, 9);
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 52 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjMoveStringLiteralDfa0_3() {
        switch(curChar) {
            case 42:
                return jjMoveStringLiteralDfa1_3(0x800000L);
            default:
                return 1;
        }
    }

    private final int jjMoveStringLiteralDfa1_3(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return 1;
        }
        switch(curChar) {
            case 47:
                if ((active0 & 0x800000L) != 0L) return jjStopAtPos(1, 23);
                break;
            default:
                return 2;
        }
        return 2;
    }

    private final int jjMoveStringLiteralDfa0_1() {
        return jjMoveNfa_1(0, 0);
    }

    private final int jjMoveNfa_1(int startState, int curPos) {
        int[] nextStates;
        int startsAt = 0;
        jjnewStateCnt = 3;
        int i = 1;
        jjstateSet[0] = startState;
        int j, kind = 0x7fffffff;
        for (; ; ) {
            if (++jjround == 0x7fffffff) ReInitRounds();
            if (curChar < 64) {
                long l = 1L << curChar;
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        case 0:
                            if ((0x2400L & l) != 0L) {
                                if (kind > 21) kind = 21;
                            }
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        case 1:
                            if (curChar == 10 && kind > 21) kind = 21;
                            break;
                        case 2:
                            if (curChar == 13) jjstateSet[jjnewStateCnt++] = 1;
                            break;
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else if (curChar < 128) {
                long l = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            } else {
                int hiByte = (int) (curChar >> 8);
                int i1 = hiByte >> 6;
                long l1 = 1L << (hiByte & 077);
                int i2 = (curChar & 0xff) >> 6;
                long l2 = 1L << (curChar & 077);
                MatchLoop: do {
                    switch(jjstateSet[--i]) {
                        default:
                            break;
                    }
                } while (i != startsAt);
            }
            if (kind != 0x7fffffff) {
                jjmatchedKind = kind;
                jjmatchedPos = curPos;
                kind = 0x7fffffff;
            }
            ++curPos;
            if ((i = jjnewStateCnt) == (startsAt = 3 - (jjnewStateCnt = startsAt))) return curPos;
            try {
                curChar = input_stream.readChar();
            } catch (java.io.IOException e) {
                return curPos;
            }
        }
    }

    private final int jjMoveStringLiteralDfa0_2() {
        switch(curChar) {
            case 42:
                return jjMoveStringLiteralDfa1_2(0x400000L);
            default:
                return 1;
        }
    }

    private final int jjMoveStringLiteralDfa1_2(long active0) {
        try {
            curChar = input_stream.readChar();
        } catch (java.io.IOException e) {
            return 1;
        }
        switch(curChar) {
            case 47:
                if ((active0 & 0x400000L) != 0L) return jjStopAtPos(1, 22);
                break;
            default:
                return 2;
        }
        return 2;
    }

    static final int[] jjnextStates = { 34, 35, 40, 41, 44, 45, 12, 23, 24, 26, 14, 16, 49, 51, 6, 8, 9, 12, 23, 24, 28, 26, 36, 37, 12, 44, 45, 12, 10, 11, 17, 18, 20, 25, 27, 29, 38, 39, 42, 43, 46, 47 };

    private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
        switch(hiByte) {
            case 0:
                return ((jjbitVec2[i2] & l2) != 0L);
            default:
                if ((jjbitVec0[i1] & l1) != 0L) return true;
                return false;
        }
    }

    private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
        switch(hiByte) {
            case 0:
                return ((jjbitVec4[i2] & l2) != 0L);
            case 48:
                return ((jjbitVec5[i2] & l2) != 0L);
            case 49:
                return ((jjbitVec6[i2] & l2) != 0L);
            case 51:
                return ((jjbitVec7[i2] & l2) != 0L);
            case 61:
                return ((jjbitVec8[i2] & l2) != 0L);
            default:
                if ((jjbitVec3[i1] & l1) != 0L) return true;
                return false;
        }
    }

    public static final String[] jjstrLiteralImages = { "", "\157\160\164\151\157\156\163", "\114\117\117\113\101\110\105\101\104", "\111\107\116\117\122\105\137\103\101\123\105", "\120\101\122\123\105\122\137\102\105\107\111\116", "\120\101\122\123\105\122\137\105\116\104", "\103\123\110\101\122\120\103\117\104\105", "\124\117\113\105\116", "\123\120\105\103\111\101\114\137\124\117\113\105\116", "\115\117\122\105", "\123\113\111\120", "\124\117\113\105\116\137\115\107\122\137\104\105\103\114\123", "\105\117\106", null, null, null, null, null, null, null, null, null, null, null, null, "\141\142\163\164\162\141\143\164", "\141\163", "\142\141\163\145", "\142\157\157\154", "\142\162\145\141\153", "\142\171\164\145", "\143\141\163\145", "\143\141\164\143\150", "\143\150\145\143\153\145\144", "\143\150\141\162", "\143\154\141\163\163", "\143\157\156\163\164", "\143\157\156\164\151\156\165\145", "\144\145\143\151\155\141\154", "\144\145\146\141\165\154\164", "\144\145\154\145\147\141\164\145", "\144\157", "\144\157\165\142\154\145", "\145\154\163\145", "\145\156\165\155", "\145\166\145\156\164", "\145\170\160\154\151\143\151\164", "\145\170\164\145\162\156", "\146\141\154\163\145", "\146\151\156\141\154\154\171", "\146\151\170\145\144", "\146\154\157\141\164", "\146\157\162", "\146\157\162\145\141\143\150", "\147\157\164\157", "\151\146", "\151\155\160\154\151\143\151\164", "\151\156", "\151\156\164", "\151\156\164\145\162\146\141\143\145", "\151\156\164\145\162\156\141\154", "\151\163", "\154\157\143\153", "\154\157\156\147", "\156\141\155\145\163\160\141\143\145", "\156\145\167", "\156\165\154\154", "\157\142\152\145\143\164", "\157\160\145\162\141\164\157\162", "\157\165\164", "\157\166\145\162\162\151\144\145", "\160\141\162\141\155\163", "\160\162\151\166\141\164\145", "\160\162\157\164\145\143\164\145\144", "\160\165\142\154\151\143", "\162\145\141\144\157\156\154\171", "\162\145\146", "\162\145\164\165\162\156", "\163\142\171\164\145", "\163\145\141\154\145\144", "\163\150\157\162\164", "\163\164\141\164\151\143", "\163\164\162\151\156\147", "\163\164\162\165\143\164", "\163\167\151\164\143\150", "\164\150\151\163", "\164\150\162\157\167", "\164\162\165\145", "\164\162\171", "\164\171\160\145\157\146", "\165\151\156\164", "\165\154\157\156\147", "\165\156\143\150\145\143\153\145\144", "\165\156\163\141\146\145", "\165\163\150\157\162\164", "\165\163\151\156\147", "\166\151\162\164\165\141\154", "\166\157\151\144", "\166\157\154\141\164\151\154\145", "\167\150\151\154\145", null, null, null, null, null, null, null, null, null, null, null, "\50", "\51", "\173", "\175", "\133", "\135", "\73", "\54", "\56", "\75", "\76", "\74", "\41", "\176", "\77", "\72", "\75\75", "\74\75", "\76\75", "\41\75", "\174\174", "\46\46", "\53\53", "\55\55", "\53", "\55", "\52", "\57", "\46", "\174", "\136", "\45", "\53\75", "\55\75", "\52\75", "\57\75", "\46\75", "\174\75", "\136\75", "\45\75", "\43", "\147\145\164", "\163\145\164" };

    public static final String[] lexStateNames = { "DEFAULT", "IN_SINGLE_LINE_COMMENT", "IN_FORMAL_COMMENT", "IN_MULTI_LINE_COMMENT" };

    public static final int[] jjnewLexState = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 2, 3, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

    static final long[] jjtoToken = { 0xfffffffffe001fffL, 0xffff9d1fffffffffL, 0x3ffffffL };

    static final long[] jjtoSkip = { 0xe3e000L, 0x0L, 0x0L };

    static final long[] jjtoSpecial = { 0xe3e000L, 0x0L, 0x0L };

    static final long[] jjtoMore = { 0x11c0000L, 0x0L, 0x0L };

    protected JavaCharStream input_stream;

    private final int[] jjrounds = new int[52];

    private final int[] jjstateSet = new int[104];

    StringBuffer image;

    int jjimageLen;

    int lengthOfMatch;

    protected char curChar;

    public CSTreeParserTokenManager(JavaCharStream stream) {
        if (JavaCharStream.staticFlag) throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
        input_stream = stream;
    }

    public CSTreeParserTokenManager(JavaCharStream stream, int lexState) {
        this(stream);
        SwitchTo(lexState);
    }

    public void ReInit(JavaCharStream stream) {
        jjmatchedPos = jjnewStateCnt = 0;
        curLexState = defaultLexState;
        input_stream = stream;
        ReInitRounds();
    }

    private final void ReInitRounds() {
        int i;
        jjround = 0x80000001;
        for (i = 52; i-- > 0; ) jjrounds[i] = 0x80000000;
    }

    public void ReInit(JavaCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 4 || lexState < 0) throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE); else curLexState = lexState;
    }

    protected Token jjFillToken() {
        Token t = Token.newToken(jjmatchedKind);
        t.kind = jjmatchedKind;
        String im = jjstrLiteralImages[jjmatchedKind];
        t.image = (im == null) ? input_stream.GetImage() : im;
        t.beginLine = input_stream.getBeginLine();
        t.beginColumn = input_stream.getBeginColumn();
        t.endLine = input_stream.getEndLine();
        t.endColumn = input_stream.getEndColumn();
        return t;
    }

    int curLexState = 0;

    int defaultLexState = 0;

    int jjnewStateCnt;

    int jjround;

    int jjmatchedPos;

    int jjmatchedKind;

    public Token getNextToken() {
        int kind;
        Token specialToken = null;
        Token matchedToken;
        int curPos = 0;
        EOFLoop: for (; ; ) {
            try {
                curChar = input_stream.BeginToken();
            } catch (java.io.IOException e) {
                jjmatchedKind = 0;
                matchedToken = jjFillToken();
                matchedToken.specialToken = specialToken;
                return matchedToken;
            }
            image = null;
            jjimageLen = 0;
            for (; ; ) {
                switch(curLexState) {
                    case 0:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_0();
                        break;
                    case 1:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_1();
                        if (jjmatchedPos == 0 && jjmatchedKind > 24) {
                            jjmatchedKind = 24;
                        }
                        break;
                    case 2:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_2();
                        if (jjmatchedPos == 0 && jjmatchedKind > 24) {
                            jjmatchedKind = 24;
                        }
                        break;
                    case 3:
                        jjmatchedKind = 0x7fffffff;
                        jjmatchedPos = 0;
                        curPos = jjMoveStringLiteralDfa0_3();
                        if (jjmatchedPos == 0 && jjmatchedKind > 24) {
                            jjmatchedKind = 24;
                        }
                        break;
                }
                if (jjmatchedKind != 0x7fffffff) {
                    if (jjmatchedPos + 1 < curPos) input_stream.backup(curPos - jjmatchedPos - 1);
                    if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        matchedToken = jjFillToken();
                        matchedToken.specialToken = specialToken;
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        return matchedToken;
                    } else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                        if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
                            matchedToken = jjFillToken();
                            if (specialToken == null) specialToken = matchedToken; else {
                                matchedToken.specialToken = specialToken;
                                specialToken = (specialToken.next = matchedToken);
                            }
                            SkipLexicalActions(matchedToken);
                        } else SkipLexicalActions(null);
                        if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                        continue EOFLoop;
                    }
                    MoreLexicalActions();
                    if (jjnewLexState[jjmatchedKind] != -1) curLexState = jjnewLexState[jjmatchedKind];
                    curPos = 0;
                    jjmatchedKind = 0x7fffffff;
                    try {
                        curChar = input_stream.readChar();
                        continue;
                    } catch (java.io.IOException e1) {
                    }
                }
                int error_line = input_stream.getEndLine();
                int error_column = input_stream.getEndColumn();
                String error_after = null;
                boolean EOFSeen = false;
                try {
                    input_stream.readChar();
                    input_stream.backup(1);
                } catch (java.io.IOException e1) {
                    EOFSeen = true;
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                    if (curChar == '\n' || curChar == '\r') {
                        error_line++;
                        error_column = 0;
                    } else error_column++;
                }
                if (!EOFSeen) {
                    input_stream.backup(1);
                    error_after = curPos <= 1 ? "" : input_stream.GetImage();
                }
                throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
            }
        }
    }

    void SkipLexicalActions(Token matchedToken) {
        switch(jjmatchedKind) {
            default:
                break;
        }
    }

    void MoreLexicalActions() {
        jjimageLen += (lengthOfMatch = jjmatchedPos + 1);
        switch(jjmatchedKind) {
            case 19:
                if (image == null) image = new StringBuffer(new String(input_stream.GetSuffix(jjimageLen))); else image.append(input_stream.GetSuffix(jjimageLen));
                jjimageLen = 0;
                input_stream.backup(1);
                break;
            default:
                break;
        }
    }
}
