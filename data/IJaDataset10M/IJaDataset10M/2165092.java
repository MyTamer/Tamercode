package com.loribel.java.parser;

public interface JavaParserConstants {

    int EOF = 0;

    int SINGLE_LINE_COMMENT = 9;

    int FORMAL_COMMENT = 10;

    int MULTI_LINE_COMMENT = 11;

    int ABSTRACT = 13;

    int BOOLEAN = 14;

    int BREAK = 15;

    int BYTE = 16;

    int CASE = 17;

    int CATCH = 18;

    int CHAR = 19;

    int CLASS = 20;

    int CONST = 21;

    int CONTINUE = 22;

    int _DEFAULT = 23;

    int DO = 24;

    int DOUBLE = 25;

    int ELSE = 26;

    int EXTENDS = 27;

    int FALSE = 28;

    int FINAL = 29;

    int FINALLY = 30;

    int FLOAT = 31;

    int FOR = 32;

    int GOTO = 33;

    int IF = 34;

    int IMPLEMENTS = 35;

    int IMPORT = 36;

    int INSTANCEOF = 37;

    int INT = 38;

    int INTERFACE = 39;

    int LONG = 40;

    int NATIVE = 41;

    int NEW = 42;

    int NULL = 43;

    int PACKAGE = 44;

    int PRIVATE = 45;

    int PROTECTED = 46;

    int PUBLIC = 47;

    int RETURN = 48;

    int SHORT = 49;

    int STATIC = 50;

    int SUPER = 51;

    int SWITCH = 52;

    int SYNCHRONIZED = 53;

    int THIS = 54;

    int THROW = 55;

    int THROWS = 56;

    int TRANSIENT = 57;

    int TRUE = 58;

    int TRY = 59;

    int VOID = 60;

    int VOLATILE = 61;

    int WHILE = 62;

    int INTEGER_LITERAL = 63;

    int DECIMAL_LITERAL = 64;

    int HEX_LITERAL = 65;

    int OCTAL_LITERAL = 66;

    int FLOATING_POINT_LITERAL = 67;

    int EXPONENT = 68;

    int CHARACTER_LITERAL = 69;

    int STRING_LITERAL = 70;

    int IDENTIFIER = 71;

    int LETTER = 72;

    int DIGIT = 73;

    int LPAREN = 74;

    int RPAREN = 75;

    int LBRACE = 76;

    int RBRACE = 77;

    int LBRACKET = 78;

    int RBRACKET = 79;

    int SEMICOLON = 80;

    int COMMA = 81;

    int DOT = 82;

    int ASSIGN = 83;

    int GT = 84;

    int LT = 85;

    int BANG = 86;

    int TILDE = 87;

    int HOOK = 88;

    int COLON = 89;

    int EQ = 90;

    int LE = 91;

    int GE = 92;

    int NE = 93;

    int SC_OR = 94;

    int SC_AND = 95;

    int INCR = 96;

    int DECR = 97;

    int PLUS = 98;

    int MINUS = 99;

    int STAR = 100;

    int SLASH = 101;

    int BIT_AND = 102;

    int BIT_OR = 103;

    int XOR = 104;

    int REM = 105;

    int LSHIFT = 106;

    int RSIGNEDSHIFT = 107;

    int RUNSIGNEDSHIFT = 108;

    int PLUSASSIGN = 109;

    int MINUSASSIGN = 110;

    int STARASSIGN = 111;

    int SLASHASSIGN = 112;

    int ANDASSIGN = 113;

    int ORASSIGN = 114;

    int XORASSIGN = 115;

    int REMASSIGN = 116;

    int LSHIFTASSIGN = 117;

    int RSIGNEDSHIFTASSIGN = 118;

    int RUNSIGNEDSHIFTASSIGN = 119;

    int DEFAULT = 0;

    int IN_SINGLE_LINE_COMMENT = 1;

    int IN_FORMAL_COMMENT = 2;

    int IN_MULTI_LINE_COMMENT = 3;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"\\f\"", "\"//\"", "<token of kind 7>", "\"/*\"", "<SINGLE_LINE_COMMENT>", "\"*/\"", "\"*/\"", "<token of kind 12>", "\"abstract\"", "\"boolean\"", "\"break\"", "\"byte\"", "\"case\"", "\"catch\"", "\"char\"", "\"class\"", "\"const\"", "\"continue\"", "\"default\"", "\"do\"", "\"double\"", "\"else\"", "\"extends\"", "\"false\"", "\"final\"", "\"finally\"", "\"float\"", "\"for\"", "\"goto\"", "\"if\"", "\"implements\"", "\"import\"", "\"instanceof\"", "\"int\"", "\"interface\"", "\"long\"", "\"native\"", "\"new\"", "\"null\"", "\"package\"", "\"private\"", "\"protected\"", "\"public\"", "\"return\"", "\"short\"", "\"static\"", "\"super\"", "\"switch\"", "\"synchronized\"", "\"this\"", "\"throw\"", "\"throws\"", "\"transient\"", "\"true\"", "\"try\"", "\"void\"", "\"volatile\"", "\"while\"", "<INTEGER_LITERAL>", "<DECIMAL_LITERAL>", "<HEX_LITERAL>", "<OCTAL_LITERAL>", "<FLOATING_POINT_LITERAL>", "<EXPONENT>", "<CHARACTER_LITERAL>", "<STRING_LITERAL>", "<IDENTIFIER>", "<LETTER>", "<DIGIT>", "\"(\"", "\")\"", "\"{\"", "\"}\"", "\"[\"", "\"]\"", "\";\"", "\",\"", "\".\"", "\"=\"", "\">\"", "\"<\"", "\"!\"", "\"~\"", "\"?\"", "\":\"", "\"==\"", "\"<=\"", "\">=\"", "\"!=\"", "\"||\"", "\"&&\"", "\"++\"", "\"--\"", "\"+\"", "\"-\"", "\"*\"", "\"/\"", "\"&\"", "\"|\"", "\"^\"", "\"%\"", "\"<<\"", "\">>\"", "\">>>\"", "\"+=\"", "\"-=\"", "\"*=\"", "\"/=\"", "\"&=\"", "\"|=\"", "\"^=\"", "\"%=\"", "\"<<=\"", "\">>=\"", "\">>>=\"" };
}
