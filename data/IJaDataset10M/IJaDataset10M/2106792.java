package org.sablecc.objectmacro.codegeneration.java.macro;

public class MSelfRefMacro {

    private final MMacro mMacro;

    MSelfRefMacro(MMacro mMacro) {
        if (mMacro == null) {
            throw new NullPointerException();
        }
        this.mMacro = mMacro;
    }

    private String rName() {
        return this.mMacro.pName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  private final M");
        sb.append(rName());
        sb.append(" m");
        sb.append(rName());
        sb.append(" = this;");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
