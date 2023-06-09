package org.sablecc.objectmacro.errormessage;

public class MLexicalError {

    private final String pLine;

    private final String pChar;

    private final String pMessage;

    private final MLexicalError mLexicalError = this;

    public MLexicalError(String pLine, String pChar, String pMessage) {
        if (pLine == null) {
            throw new NullPointerException();
        }
        this.pLine = pLine;
        if (pChar == null) {
            throw new NullPointerException();
        }
        this.pChar = pChar;
        if (pMessage == null) {
            throw new NullPointerException();
        }
        this.pMessage = pMessage;
    }

    String pLine() {
        return this.pLine;
    }

    String pChar() {
        return this.pChar;
    }

    String pMessage() {
        return this.pMessage;
    }

    private String rLine() {
        return this.mLexicalError.pLine();
    }

    private String rChar() {
        return this.mLexicalError.pChar();
    }

    private String rMessage() {
        return this.mLexicalError.pMessage();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*** LEXICAL ERROR ***");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append("Line: ");
        sb.append(rLine());
        sb.append(System.getProperty("line.separator"));
        sb.append("Char: ");
        sb.append(rChar());
        sb.append(System.getProperty("line.separator"));
        sb.append(rMessage());
        sb.append(".");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
