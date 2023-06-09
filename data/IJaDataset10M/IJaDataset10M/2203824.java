package org.sablecc.objectmacro.codegeneration.c.macro;

public class MInlineString {

    private final String pString;

    private final MInlineString mInlineString = this;

    MInlineString(String pString) {
        if (pString == null) {
            throw new NullPointerException();
        }
        this.pString = pString;
    }

    String pString() {
        return this.pString;
    }

    private String rString() {
        return this.mInlineString.pString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(rString());
        sb.append("\"");
        return sb.toString();
    }
}
