package org.sablecc.objectmacro.errormessage;

class MCommandLineErrorHead {

    MCommandLineErrorHead() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*** COMMAND ERROR ***");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
