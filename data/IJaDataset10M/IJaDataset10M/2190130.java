package org.sablecc.sablecc.errormessage;

public class MGrammarNotFile {

    private final String pFileName;

    private final MGrammarNotFile mGrammarNotFile = this;

    public MGrammarNotFile(String pFileName) {
        if (pFileName == null) {
            throw new NullPointerException();
        }
        this.pFileName = pFileName;
    }

    String pFileName() {
        return this.pFileName;
    }

    private String rFileName() {
        return this.mGrammarNotFile.pFileName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(new MCommandLineErrorHead().toString());
        sb.append(System.getProperty("line.separator"));
        sb.append("The grammar argument, \"");
        sb.append(rFileName());
        sb.append("\", is not a file.");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append(new MCommandLineErrorTail().toString());
        return sb.toString();
    }
}
