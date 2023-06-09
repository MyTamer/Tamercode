package org.sablecc.objectmacro.codegeneration.c.macro;

public class MTextInsertAncestor {

    private final String pName;

    private final MTextInsertAncestor mTextInsertAncestor = this;

    private final MFile mFile;

    MTextInsertAncestor(String pName, MFile mFile) {
        if (pName == null) {
            throw new NullPointerException();
        }
        this.pName = pName;
        if (mFile == null) {
            throw new NullPointerException();
        }
        this.mFile = mFile;
    }

    String pName() {
        return this.pName;
    }

    private String rFileName() {
        return this.mFile.pFileName();
    }

    private String rName() {
        return this.mTextInsertAncestor.pName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("M");
        sb.append(rFileName());
        sb.append("->_m");
        sb.append(rName());
        sb.append("_");
        return sb.toString();
    }
}
