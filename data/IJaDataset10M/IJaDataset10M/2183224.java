package org.sablecc.sablecc.errormessage;

class MSemanticErrorHead {

    MSemanticErrorHead() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*** SEMANTIC ERROR ***");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
