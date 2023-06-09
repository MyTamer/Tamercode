package org.sablecc.sablecc.codegeneration.java.macro;

public class MOpenRightInterval {

    private final String pLowerBound;

    private final String pName;

    private final MOpenRightInterval mOpenRightInterval = this;

    MOpenRightInterval(String pLowerBound, String pName) {
        if (pLowerBound == null) {
            throw new NullPointerException();
        }
        this.pLowerBound = pLowerBound;
        if (pName == null) {
            throw new NullPointerException();
        }
        this.pName = pName;
    }

    String pLowerBound() {
        return this.pLowerBound;
    }

    String pName() {
        return this.pName;
    }

    private String rLowerBound() {
        return this.mOpenRightInterval.pLowerBound();
    }

    private String rName() {
        return this.mOpenRightInterval.pName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("      if(c >= ");
        sb.append(rLowerBound());
        sb.append(") {");
        sb.append(System.getProperty("line.separator"));
        sb.append("        return Symbol_");
        sb.append(rName());
        sb.append(";");
        sb.append(System.getProperty("line.separator"));
        sb.append("      }");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
