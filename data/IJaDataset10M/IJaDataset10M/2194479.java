package org.netbeans.cubeon.gcode.persistence;

/**
 * Original @author is Chris Hyzer ,Credit is gos to Chris Hyzer.
 * 
 */
public class JsonIndenter {

    /** chars to process */
    private String json;

    /** current start tag */
    private int startTagIndex;

    /** current end tag */
    private int endTagIndex;

    /** current number of indents (times to is the indent */
    private int currentNumberOfIndents;

    /** result */
    private StringBuilder result;

    /**
     * get the result
     * @return the result
     */
    public String result() {
        try {
            this.indent();
        } catch (RuntimeException re) {
            throw new RuntimeException("Problem here: " + this, re);
        }
        if (this.json == null) {
            return null;
        }
        return this.result.toString().trim();
    }

    /**
     * indent the string
     */
    private void indent() {
        if (this.json == null) {
            return;
        }
        this.result = new StringBuilder();
        this.startTagIndex = -1;
        this.endTagIndex = -1;
        this.currentNumberOfIndents = 0;
        while (true) {
            this.startTagIndex = findStartTagIndex();
            if (this.startTagIndex == -1) {
                if (this.endTagIndex != this.json.length() - 1) {
                    this.result.append(this.json, this.endTagIndex + 1, this.json.length());
                }
                break;
            }
            if (instantIndent(this.json, this.startTagIndex)) {
                this.currentNumberOfIndents++;
                this.printNewlineIndent(this.startTagIndex, this.startTagIndex + 1);
                this.endTagIndex = this.startTagIndex;
                continue;
            }
            if (instantUnindentTwoChars(this.json, this.startTagIndex)) {
                this.currentNumberOfIndents--;
                this.newlineIndent();
                this.printNewlineIndent(this.startTagIndex, this.startTagIndex + 2);
                this.endTagIndex = this.startTagIndex + 1;
                continue;
            }
            if (instantUnindent(this.json, this.startTagIndex)) {
                this.currentNumberOfIndents--;
                if (onNewline()) {
                    this.unindent();
                } else {
                    this.newlineIndent();
                }
                this.printNewlineIndent(this.startTagIndex, this.startTagIndex + 1);
                this.endTagIndex = this.startTagIndex;
                continue;
            }
            if (instantNewline(this.json, this.startTagIndex)) {
                this.printNewlineIndent(this.startTagIndex, this.startTagIndex + 1);
                this.endTagIndex = this.startTagIndex;
                continue;
            }
            this.endTagIndex = findEndTagIndex();
            this.result.append(this.json, this.startTagIndex, this.endTagIndex + 1);
            if (this.endTagIndex >= this.json.length() - 1) {
                continue;
            }
            char nextChar = this.json.charAt(this.endTagIndex + 1);
            if (nextChar == ':') {
                this.result.append(':');
                this.endTagIndex++;
            }
        }
    }

    /**
     * see if current pos is on newline
     * @return true if on new line
     */
    private boolean onNewline() {
        for (int i = this.result.length() - 1; i >= 0; i--) {
            char curChar = this.result.charAt(i);
            if (curChar == '\n') {
                return true;
            }
            if (Character.isWhitespace(curChar)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * see if instant indent
     * @param json
     * @param index
     * @return if it is an instant indent
     */
    static boolean instantIndent(String json, int index) {
        char curChar = json.charAt(index);
        if (curChar == '{' || curChar == '[') {
            return true;
        }
        return false;
    }

    /**
     * see if instant indent
     * @param json
     * @param index
     * @return if it is an instant indent
     */
    static boolean instantNewline(String json, int index) {
        char curChar = json.charAt(index);
        if (curChar == ',') {
            return true;
        }
        return false;
    }

    /**
     * see if instant unindent
     * @param json
     * @param index
     * @return if it is an instant unindent
     */
    static boolean instantUnindent(String json, int index) {
        char curChar = json.charAt(index);
        if (curChar == '}' || curChar == ']') {
            return true;
        }
        return false;
    }

    /**
     * see if instant indent
     * @param json
     * @param index
     * @return if it is an instant indent
     */
    static boolean instantUnindentTwoChars(String json, int index) {
        char curChar = json.charAt(index);
        if (index == json.length() - 1) {
            return false;
        }
        char nextchar = json.charAt(index + 1);
        if (curChar == '}' && nextchar == ',') {
            return true;
        }
        return false;
    }

    /**
     * put a newline and indent
     * @param start
     * @param end
     */
    private void printNewlineIndent(int start, int end) {
        this.result.append(this.json, start, end);
        this.newlineIndent();
    }

    /**
     * put a newline and indent
     */
    private void newlineIndent() {
        this.result.append("\n");
        for (int i = 0; i < this.currentNumberOfIndents; i++) {
            this.result.append("  ");
        }
    }

    /**
     * unindent a previous indent if it is there
     */
    private void unindent() {
        for (int i = 0; i < 2; i++) {
            if (this.result.charAt(this.result.length() - 1) == ' ') {
                this.result.deleteCharAt(this.result.length() - 1);
            }
        }
    }

    /**
     * after the last end tag, find the next start tag
     * @return the next start tag
     */
    private int findStartTagIndex() {
        return findNextStartTagIndex(this.json, this.endTagIndex + 1);
    }

    /**
     * after the last start tag, find the next end start tag
     * @return the next start tag
     */
    private int findEndTagIndex() {
        return findNextEndTagIndex(this.json, this.startTagIndex + 1);
    }

    /**
     * find the start tag from json and a start from index
     * either look for a quote, {, [ or scalar.  generally not whitespace
     * @param json
     * @param startFrom
     * @return the start tag index of -1 if not found another
     */
    static int findNextStartTagIndex(String json, int startFrom) {
        int length = json.length();
        for (int i = startFrom; i < length; i++) {
            char curChar = json.charAt(i);
            if (Character.isWhitespace(curChar)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    /**
     * find the end tag from json and a start from index
     * @param json
     * @param startFrom is the char after the start of tag
     * @return the start tag index of -1 if not found another
     */
    static int findNextEndTagIndex(String json, int startFrom) {
        int length = json.length();
        boolean quotedString = json.charAt(startFrom - 1) == '\"';
        int ignoreSlashInIndex = -1;
        boolean afterSlash = false;
        for (int i = startFrom; i < length; i++) {
            afterSlash = i != ignoreSlashInIndex && i != startFrom && json.charAt(i - 1) == '\\';
            char curChar = json.charAt(i);
            if (!afterSlash && curChar == '\\') {
                ignoreSlashInIndex = i + 2;
            }
            if (!quotedString) {
                if (curChar == ':' || Character.isWhitespace(curChar) || curChar == ']' || curChar == '}' || curChar == ',') {
                    return i - 1;
                }
            } else {
                if (!afterSlash && curChar == '\"') {
                    return i;
                }
            }
        }
        return json.length() - 1;
    }

    /**
     * @param theJson is the json to format
     * indenter
     */
    public JsonIndenter(String theJson) {
        if (theJson != null) {
            this.json = theJson.trim();
        }
    }
}
