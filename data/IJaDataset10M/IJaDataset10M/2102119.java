package org.exist.xquery.regex;

import java.util.ArrayList;
import java.util.List;
import org.exist.util.FastStringBuffer;
import org.exist.util.UTF16CharacterSet;
import org.exist.xquery.value.StringValue;

/**
 * This class translates XML Schema regex syntax into JDK 1.5 regex syntax. This differs from the JDK 1.4
 * translator because JDK 1.5 handles non-BMP characters (wide characters) in places where JDK 1.4 does not,
 * for example in a range such as [X-Y]. This enables much of the code from the 1.4 translator to be
 * removed.
 * Author: James Clark, Thai Open Source Software Center Ltd. See statement at end of file.
 * Modified by Michael Kay (a) to integrate the code into Saxon, and (b) to support XPath additions
 * to the XML Schema regex syntax. This version also removes most of the complexities of handling non-BMP
 * characters, since JDK 1.5 handles these natively.
 * 
 * Copied from Saxon-HE 9.2 package net.sf.saxon.regex.
 */
public class JDK15RegexTranslator extends RegexTranslator {

    int XML10 = 10;

    /**
     * Translates XML Schema and XPath regexes into <code>java.util.regex</code> regexes.
     *
     * @see java.util.regex.Pattern
     * @see <a href="http://www.w3.org/TR/xmlschema-2/#regexs">XML Schema Part 2</a>
     */
    public static final CharClass[] categoryCharClasses = new CharClass[RegexData.categories.length()];

    public static final CharClass[] subCategoryCharClasses = new CharClass[RegexData.subCategories.length() / 2];

    /**
     * CharClass for each block name in specialBlockNames.
     */
    public static final CharClass[] specialBlockCharClasses = { new CharRange(0x10300, 0x1032F), new CharRange(0x10330, 0x1034F), new CharRange(0x10400, 0x1044F), new CharRange(0x1D000, 0x1D0FF), new CharRange(0x1D100, 0x1D1FF), new CharRange(0x1D400, 0x1D7FF), new CharRange(0x20000, 0x2A6D6), new CharRange(0x2F800, 0x2FA1F), new CharRange(0xE0000, 0xE007F), new Union(new CharClass[] { new CharRange(0xE000, 0xF8FF), new CharRange(0xF0000, 0xFFFFD), new CharRange(0x100000, 0x10FFFD) }), Empty.getInstance(), Empty.getInstance(), Empty.getInstance() };

    private static final CharClass DOT_SCHEMA = new Complement(new Union(new CharClass[] { new SingleChar('\n'), new SingleChar('\r') }));

    private static final CharClass ESC_d = new Property("Nd");

    private static final CharClass ESC_D = new Complement(ESC_d);

    private static final CharClass ESC_W = new Union(new CharClass[] { computeCategoryCharClass('P'), computeCategoryCharClass('Z'), computeCategoryCharClass('C') });

    private static final CharClass ESC_w = new Complement(ESC_W);

    private static final CharClass ESC_s = new Union(new CharClass[] { new SingleChar(' '), new SingleChar('\n'), new SingleChar('\r'), new SingleChar('\t') });

    private static final CharClass ESC_S = new Complement(ESC_s);

    private static final CharClass ESC_i_10 = makeNameCharClass(XMLCharacterData.NAME_START_10_MASK);

    private static final CharClass ESC_i_11 = makeNameCharClass(XMLCharacterData.NAME_START_11_MASK);

    private static final CharClass ESC_I_10 = new Complement(ESC_i_10);

    private static final CharClass ESC_I_11 = new Complement(ESC_i_11);

    private static final CharClass ESC_c_10 = makeNameCharClass(XMLCharacterData.NAME_10_MASK);

    private static final CharClass ESC_c_11 = makeNameCharClass(XMLCharacterData.NAME_11_MASK);

    private static final CharClass ESC_C_10 = new Complement(ESC_c_10);

    private static final CharClass ESC_C_11 = new Complement(ESC_c_11);

    private JDK15RegexTranslator() {
    }

    /**
     * Translates a regular expression in the syntax of XML Schemas Part 2 into a regular
     * expression in the syntax of <code>java.util.regex.Pattern</code>.  The translation
     * assumes that the string to be matched against the regex uses surrogate pairs correctly.
     * If the string comes from XML content, a conforming XML parser will automatically
     * check this; if the string comes from elsewhere, it may be necessary to check
     * surrogate usage before matching.
     * @param xmlVersion set to {@link net.sf.saxon.Configuration#XML10} for XML 1.0
     * or {@link net.sf.saxon.Configuration#XML11} for XML 1.1
     * @param regExp a String containing a regular expression in the syntax of XML Schemas Part 2
     * @param xpath  a boolean indicating whether the XPath 2.0 F+O extensions to the schema
     *               regex syntax are permitted
     * @param ignoreWhitespace true if whitespace is to be ignored ('x' flag)
     * @param caseBlind true if case is to be ignored ('i' flag)
     * @return a JDK 1.5 regular expression
     * @throws RegexSyntaxException if <code>regexp</code> is not a regular expression in the
     *                              syntax of XML Schemas Part 2, or XPath 2.0, as appropriate
     * @see java.util.regex.Pattern
     * @see <a href="http://www.w3.org/TR/xmlschema-2/#regexs">XML Schema Part 2</a>
     */
    public static String translate(CharSequence regExp, int xmlVersion, boolean xpath, boolean ignoreWhitespace, boolean caseBlind) throws RegexSyntaxException {
        JDK15RegexTranslator tr = new JDK15RegexTranslator();
        tr.regExp = regExp;
        tr.length = regExp.length();
        tr.xmlVersion = xmlVersion;
        tr.isXPath = xpath;
        tr.ignoreWhitespace = ignoreWhitespace;
        tr.caseBlind = caseBlind;
        tr.advance();
        tr.translateTop();
        return tr.result.toString();
    }

    abstract static class CharClass {

        protected CharClass() {
        }

        abstract void output(FastStringBuffer buf);

        abstract void outputComplement(FastStringBuffer buf);

        int getSingleChar() {
            return -1;
        }
    }

    abstract static class SimpleCharClass extends CharClass {

        SimpleCharClass() {
        }

        void output(FastStringBuffer buf) {
            buf.append('[');
            inClassOutput(buf);
            buf.append(']');
        }

        void outputComplement(FastStringBuffer buf) {
            buf.append("[^");
            inClassOutput(buf);
            buf.append(']');
        }

        abstract void inClassOutput(FastStringBuffer buf);
    }

    static class SingleChar extends SimpleCharClass {

        private final int c;

        private boolean isEscaped = false;

        SingleChar(int c) {
            this.c = c;
        }

        SingleChar(int c, boolean isEscaped) {
            this.c = c;
            this.isEscaped = isEscaped;
        }

        int getSingleChar() {
            return c;
        }

        void output(FastStringBuffer buf) {
            inClassOutput(buf);
        }

        void inClassOutput(FastStringBuffer buf) {
            if (isJavaMetaChar(c)) {
                buf.append('\\');
                buf.append((char) c);
            } else {
                switch(c) {
                    case '\r':
                        buf.append("\\r");
                        break;
                    case '\n':
                        buf.append("\\n");
                        break;
                    case '\t':
                        buf.append("\\t");
                        break;
                    case ' ':
                        buf.append("\\x20");
                        break;
                    default:
                        buf.appendWideChar(c);
                }
            }
        }
    }

    static class Empty extends SimpleCharClass {

        private static final Empty instance = new Empty();

        private Empty() {
        }

        static Empty getInstance() {
            return instance;
        }

        void output(FastStringBuffer buf) {
            buf.append("\\x00");
        }

        void outputComplement(FastStringBuffer buf) {
            buf.append("[^\\x00]");
        }

        void inClassOutput(FastStringBuffer buf) {
            throw new RuntimeException("BMP output botch");
        }
    }

    static class CharRange extends SimpleCharClass {

        private final int lower;

        private final int upper;

        CharRange(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        void inClassOutput(FastStringBuffer buf) {
            if (isJavaMetaChar(lower)) {
                buf.append('\\');
            }
            buf.appendWideChar(lower);
            buf.append('-');
            if (isJavaMetaChar(upper)) {
                buf.append('\\');
            }
            buf.appendWideChar(upper);
        }
    }

    static class Property extends SimpleCharClass {

        private final String name;

        Property(String name) {
            this.name = name;
        }

        void inClassOutput(FastStringBuffer buf) {
            buf.append("\\p{");
            buf.append(name);
            buf.append('}');
        }

        void outputComplement(FastStringBuffer buf) {
            buf.append("\\P{");
            buf.append(name);
            buf.append('}');
        }
    }

    static class Subtraction extends CharClass {

        private final CharClass cc1;

        private final CharClass cc2;

        Subtraction(CharClass cc1, CharClass cc2) {
            this.cc1 = cc1;
            this.cc2 = cc2;
        }

        void output(FastStringBuffer buf) {
            buf.append('[');
            cc1.output(buf);
            buf.append("&&");
            cc2.outputComplement(buf);
            buf.append(']');
        }

        void outputComplement(FastStringBuffer buf) {
            buf.append('[');
            cc1.outputComplement(buf);
            cc2.output(buf);
            buf.append(']');
        }
    }

    static class Union extends CharClass {

        private final List members;

        Union(CharClass[] v) {
            this(toList(v));
        }

        private static List toList(CharClass[] v) {
            List members = new ArrayList(5);
            for (int i = 0; i < v.length; i++) members.add(v[i]);
            return members;
        }

        Union(List members) {
            this.members = members;
        }

        void output(FastStringBuffer buf) {
            buf.append('[');
            for (int i = 0, len = members.size(); i < len; i++) {
                CharClass cc = (CharClass) members.get(i);
                cc.output(buf);
            }
            buf.append(']');
        }

        void outputComplement(FastStringBuffer buf) {
            boolean first = true;
            int len = members.size();
            for (int i = 0; i < len; i++) {
                CharClass cc = (CharClass) members.get(i);
                if (cc instanceof SimpleCharClass) {
                    if (first) {
                        buf.append("[^");
                        first = false;
                    }
                    ((SimpleCharClass) cc).inClassOutput(buf);
                }
            }
            for (int i = 0; i < len; i++) {
                CharClass cc = (CharClass) members.get(i);
                if (!(cc instanceof SimpleCharClass)) {
                    if (first) {
                        buf.append('[');
                        first = false;
                    } else {
                        buf.append("&&");
                    }
                    cc.outputComplement(buf);
                }
            }
            if (first) {
                buf.append("[-");
                buf.appendWideChar(UTF16CharacterSet.NONBMP_MAX);
                buf.append("]");
            } else {
                buf.append(']');
            }
        }
    }

    static class BackReference extends CharClass {

        private final int i;

        BackReference(int i) {
            this.i = i;
        }

        void output(FastStringBuffer buf) {
            inClassOutput(buf);
        }

        void outputComplement(FastStringBuffer buf) {
            inClassOutput(buf);
        }

        void inClassOutput(FastStringBuffer buf) {
            if (i != -1) {
                buf.append("(?:\\" + i + ")");
            } else {
                buf.append("(?:)");
            }
        }
    }

    static class Complement extends CharClass {

        private final CharClass cc;

        Complement(CharClass cc) {
            this.cc = cc;
        }

        void output(FastStringBuffer buf) {
            cc.outputComplement(buf);
        }

        void outputComplement(FastStringBuffer buf) {
            cc.output(buf);
        }
    }

    protected boolean translateAtom() throws RegexSyntaxException {
        switch(curChar) {
            case RegexData.EOS:
                if (!eos) break;
            case '?':
            case '*':
            case '+':
            case ')':
            case '{':
            case '}':
            case '|':
            case ']':
                return false;
            case '(':
                copyCurChar();
                int thisCapture = ++currentCapture;
                translateRegExp();
                expect(')');
                captures.add(thisCapture);
                copyCurChar();
                return true;
            case '\\':
                advance();
                parseEsc().output(result);
                return true;
            case '[':
                inCharClassExpr = true;
                advance();
                parseCharClassExpr().output(result);
                return true;
            case '.':
                if (isXPath) {
                    break;
                } else {
                    DOT_SCHEMA.output(result);
                    advance();
                    return true;
                }
            case '$':
            case '^':
                if (isXPath) {
                    copyCurChar();
                    return true;
                }
                result.append('\\');
                break;
            default:
                if (caseBlind) {
                    int thisChar = absorbSurrogatePair();
                    int[] variants = CaseVariants.getCaseVariants(thisChar);
                    if (variants.length > 0) {
                        CharClass[] chars = new CharClass[variants.length + 1];
                        chars[0] = new SingleChar(thisChar);
                        for (int i = 0; i < variants.length; i++) {
                            chars[i + 1] = new SingleChar(variants[i]);
                        }
                        Union union = new Union(chars);
                        union.output(result);
                        advance();
                        return true;
                    }
                }
        }
        copyCurChar();
        return true;
    }

    private static CharClass makeNameCharClass(byte mask) {
        List ranges = new ArrayList();
        ranges.add(new SingleChar(':'));
        IntRangeSet members = XMLCharacterData.getCategory(mask);
        int used = members.getNumberOfRanges();
        int[] startPoints = members.getStartPoints();
        int[] endPoints = members.getEndPoints();
        for (int i = 0; i < used; i++) {
            if (startPoints[i] == endPoints[i]) {
                ranges.add(new SingleChar(startPoints[i]));
            } else {
                ranges.add(new CharRange(startPoints[i], endPoints[i]));
            }
        }
        return new Union(ranges);
    }

    private CharClass parseEsc() throws RegexSyntaxException {
        switch(curChar) {
            case 'n':
                advance();
                return new SingleChar('\n', true);
            case 'r':
                advance();
                return new SingleChar('\r', true);
            case 't':
                advance();
                return new SingleChar('\t', true);
            case '\\':
            case '|':
            case '.':
            case '-':
            case '^':
            case '?':
            case '*':
            case '+':
            case '(':
            case ')':
            case '{':
            case '}':
            case '[':
            case ']':
                break;
            case 's':
                advance();
                return ESC_s;
            case 'S':
                advance();
                return ESC_S;
            case 'i':
                advance();
                return (xmlVersion == XML10 ? ESC_i_10 : ESC_i_11);
            case 'I':
                advance();
                return (xmlVersion == XML10 ? ESC_I_10 : ESC_I_11);
            case 'c':
                advance();
                return (xmlVersion == XML10 ? ESC_c_10 : ESC_c_11);
            case 'C':
                advance();
                return (xmlVersion == XML10 ? ESC_C_10 : ESC_C_11);
            case 'd':
                advance();
                return ESC_d;
            case 'D':
                advance();
                return ESC_D;
            case 'w':
                advance();
                return ESC_w;
            case 'W':
                advance();
                return ESC_W;
            case 'p':
                advance();
                return parseProp();
            case 'P':
                advance();
                return new Complement(parseProp());
            case '0':
                throw makeException("backreference cannot start with digit zero");
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                if (isXPath) {
                    if (inCharClassExpr) {
                        throw makeException("back-reference not allowed within []");
                    }
                    int backRef = (curChar - '0');
                    while (true) {
                        advance();
                        int c1 = "0123456789".indexOf(curChar);
                        if (c1 < 0) {
                            break;
                        } else {
                            int backRef2 = backRef * 10 + c1;
                            if (backRef2 > currentCapture) {
                                break;
                            } else {
                                backRef = backRef2;
                            }
                        }
                    }
                    if (!captures.contains(backRef)) {
                        String explanation = (backRef > currentCapture ? "(no such group)" : "(group not yet closed)");
                        throw makeException("invalid backreference \\" + backRef + " " + explanation);
                    }
                    return new BackReference(backRef);
                } else {
                    throw makeException("digit not allowed after \\");
                }
            case '$':
                if (isXPath) {
                    break;
                }
            default:
                throw makeException("invalid escape sequence");
        }
        CharClass tem = new SingleChar(curChar, true);
        advance();
        return tem;
    }

    private CharClass parseProp() throws RegexSyntaxException {
        expect('{');
        int start = pos;
        for (; ; ) {
            advance();
            if (curChar == '}') break;
            if (!isAsciiAlnum(curChar) && curChar != '-') expect('}');
        }
        CharSequence propertyNameCS = regExp.subSequence(start, pos - 1);
        if (ignoreWhitespace && !inCharClassExpr) {
            propertyNameCS = StringValue.collapseWhitespace(propertyNameCS);
        }
        String propertyName = propertyNameCS.toString();
        advance();
        switch(propertyName.length()) {
            case 0:
                throw makeException("empty property name");
            case 2:
                int sci = RegexData.subCategories.indexOf(propertyName);
                if (sci < 0 || sci % 2 == 1) throw makeException("unknown category");
                return getSubCategoryCharClass(sci / 2);
            case 1:
                int ci = RegexData.categories.indexOf(propertyName.charAt(0));
                if (ci < 0) throw makeException("unknown category", propertyName);
                return getCategoryCharClass(ci);
            default:
                if (!propertyName.startsWith("Is")) break;
                String blockName = propertyName.substring(2);
                for (int i = 0; i < RegexData.specialBlockNames.length; i++) if (blockName.equals(RegexData.specialBlockNames[i])) return specialBlockCharClasses[i];
                if (!isBlock(blockName)) throw makeException("invalid block name", blockName);
                return new Property("In" + blockName);
        }
        throw makeException("invalid property name", propertyName);
    }

    private CharClass parseCharClassExpr() throws RegexSyntaxException {
        boolean compl;
        if (curChar == '^') {
            advance();
            compl = true;
        } else {
            compl = false;
        }
        List members = new ArrayList(10);
        do {
            CharClass lower = parseCharClassEscOrXmlChar();
            members.add(lower);
            if (curChar == ']' || eos) {
                addCaseVariant(lower, members);
                break;
            }
            if (curChar == '-') {
                char next = regExp.charAt(pos);
                if (next == '[') {
                    addCaseVariant(lower, members);
                    advance();
                    break;
                } else if (next == ']') {
                    addCaseVariant(lower, members);
                } else {
                    advance();
                    CharClass upper = parseCharClassEscOrXmlChar();
                    if (lower.getSingleChar() < 0 || upper.getSingleChar() < 0) {
                        throw makeException("the ends of a range must be single characters");
                    }
                    if (lower.getSingleChar() > upper.getSingleChar()) {
                        throw makeException("invalid range (start > end)");
                    }
                    if (lower instanceof SingleChar && lower.getSingleChar() == '-' && !((SingleChar) lower).isEscaped) {
                        throw makeException("range cannot start with unescaped hyphen");
                    }
                    if (upper instanceof SingleChar && upper.getSingleChar() == '-' && !((SingleChar) upper).isEscaped) {
                        throw makeException("range cannot end with unescaped hyphen");
                    }
                    members.set(members.size() - 1, new CharRange(lower.getSingleChar(), upper.getSingleChar()));
                    if (caseBlind) {
                        if (lower.getSingleChar() == 'a' && upper.getSingleChar() == 'z') {
                            members.add(new CharRange('A', 'Z'));
                            for (int v = 0; v < CaseVariants.ROMAN_VARIANTS.length; v++) {
                                members.add(new SingleChar(CaseVariants.ROMAN_VARIANTS[v]));
                            }
                        } else if (lower.getSingleChar() == 'A' && upper.getSingleChar() == 'Z') {
                            members.add(new CharRange('a', 'z'));
                            for (int v = 0; v < CaseVariants.ROMAN_VARIANTS.length; v++) {
                                members.add(new SingleChar(CaseVariants.ROMAN_VARIANTS[v]));
                            }
                        } else {
                            for (int k = lower.getSingleChar(); k <= upper.getSingleChar(); k++) {
                                int[] variants = CaseVariants.getCaseVariants(k);
                                for (int v = 0; v < variants.length; v++) {
                                    members.add(new SingleChar(variants[v]));
                                }
                            }
                        }
                    }
                    if (curChar == '-' && regExp.charAt(pos) == '[') {
                        advance();
                        break;
                    }
                }
            } else {
                addCaseVariant(lower, members);
            }
        } while (curChar != ']');
        if (eos) {
            expect(']');
        }
        CharClass result;
        if (members.size() == 1) result = (CharClass) members.get(0); else result = new Union(members);
        if (compl) result = new Complement(result);
        if (curChar == '[') {
            advance();
            result = new Subtraction(result, parseCharClassExpr());
            expect(']');
        }
        inCharClassExpr = false;
        advance();
        return result;
    }

    private void addCaseVariant(CharClass lower, List members) {
        if (caseBlind) {
            int[] variants = CaseVariants.getCaseVariants(lower.getSingleChar());
            for (int v = 0; v < variants.length; v++) {
                members.add(new SingleChar(variants[v]));
            }
        }
    }

    private CharClass parseCharClassEscOrXmlChar() throws RegexSyntaxException {
        switch(curChar) {
            case RegexData.EOS:
                if (eos) expect(']');
                break;
            case '\\':
                advance();
                return parseEsc();
            case '[':
            case ']':
                throw makeException("character must be escaped", new String(new char[] { curChar }));
            case '-':
                break;
        }
        CharClass tem = new SingleChar(absorbSurrogatePair());
        advance();
        return tem;
    }

    private static synchronized CharClass getCategoryCharClass(int ci) {
        if (categoryCharClasses[ci] == null) categoryCharClasses[ci] = computeCategoryCharClass(RegexData.categories.charAt(ci));
        return categoryCharClasses[ci];
    }

    private static synchronized CharClass getSubCategoryCharClass(int sci) {
        if (subCategoryCharClasses[sci] == null) subCategoryCharClasses[sci] = computeSubCategoryCharClass(RegexData.subCategories.substring(sci * 2, (sci + 1) * 2));
        return subCategoryCharClasses[sci];
    }

    private static CharClass computeCategoryCharClass(char code) {
        List classes = new ArrayList(5);
        classes.add(new Property(new String(new char[] { code })));
        for (int ci = RegexData.CATEGORY_NAMES.indexOf(code); ci >= 0; ci = RegexData.CATEGORY_NAMES.indexOf(code, ci + 1)) {
            int[] addRanges = RegexData.CATEGORY_RANGES[ci / 2];
            for (int i = 0; i < addRanges.length; i += 2) classes.add(new CharRange(addRanges[i], addRanges[i + 1]));
        }
        if (code == 'P') classes.add(makeCharClass(RegexData.CATEGORY_Pi + RegexData.CATEGORY_Pf));
        if (code == 'L') {
            classes.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Ll));
            classes.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Lu));
        }
        if (code == 'C') {
            classes.add(new Subtraction(new Property("Cn"), new Union(new CharClass[] { new SingleChar(RegexData.UNICODE_3_1_ADD_Lu), new SingleChar(RegexData.UNICODE_3_1_ADD_Ll) })));
            List assignedRanges = new ArrayList(5);
            for (int i = 0; i < RegexData.CATEGORY_RANGES.length; i++) for (int j = 0; j < RegexData.CATEGORY_RANGES[i].length; j += 2) assignedRanges.add(new CharRange(RegexData.CATEGORY_RANGES[i][j], RegexData.CATEGORY_RANGES[i][j + 1]));
            classes.add(new Subtraction(new CharRange(UTF16CharacterSet.NONBMP_MIN, UTF16CharacterSet.NONBMP_MAX), new Union(assignedRanges)));
        }
        if (classes.size() == 1) return (CharClass) classes.get(0);
        return new Union(classes);
    }

    private static CharClass computeSubCategoryCharClass(String name) {
        CharClass base = new Property(name);
        int sci = RegexData.CATEGORY_NAMES.indexOf(name);
        if (sci < 0) {
            if (name.equals("Cn")) {
                List assignedRanges = new ArrayList(5);
                assignedRanges.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Lu));
                assignedRanges.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Ll));
                for (int i = 0; i < RegexData.CATEGORY_RANGES.length; i++) for (int j = 0; j < RegexData.CATEGORY_RANGES[i].length; j += 2) assignedRanges.add(new CharRange(RegexData.CATEGORY_RANGES[i][j], RegexData.CATEGORY_RANGES[i][j + 1]));
                return new Subtraction(new Union(new CharClass[] { base, new CharRange(UTF16CharacterSet.NONBMP_MIN, UTF16CharacterSet.NONBMP_MAX) }), new Union(assignedRanges));
            }
            if (name.equals("Pi")) return makeCharClass(RegexData.CATEGORY_Pi);
            if (name.equals("Pf")) return makeCharClass(RegexData.CATEGORY_Pf);
            return base;
        }
        List classes = new ArrayList(5);
        classes.add(base);
        int[] addRanges = RegexData.CATEGORY_RANGES[sci / 2];
        for (int i = 0; i < addRanges.length; i += 2) classes.add(new CharRange(addRanges[i], addRanges[i + 1]));
        if (name.equals("Lu")) classes.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Lu)); else if (name.equals("Ll")) classes.add(new SingleChar(RegexData.UNICODE_3_1_ADD_Ll)); else if (name.equals("Nl")) classes.add(new CharRange(RegexData.UNICODE_3_1_CHANGE_No_to_Nl_MIN, RegexData.UNICODE_3_1_CHANGE_No_to_Nl_MAX)); else if (name.equals("No")) return new Subtraction(new Union(classes), new CharRange(RegexData.UNICODE_3_1_CHANGE_No_to_Nl_MIN, RegexData.UNICODE_3_1_CHANGE_No_to_Nl_MAX));
        return new Union(classes);
    }

    private static CharClass makeCharClass(String members) {
        List list = new ArrayList(5);
        for (int i = 0, len = members.length(); i < len; i++) list.add(new SingleChar(members.charAt(i)));
        return new Union(list);
    }
}
