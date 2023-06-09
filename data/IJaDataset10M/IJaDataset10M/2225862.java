package edu.clemson.cs.r2jt.data;

/** Provides access to type checkable variable modes. */
public class Mode {

    private String modeName;

    private Mode(String modeName) {
        this.modeName = modeName;
    }

    public static final Mode UPDATES = new Mode("updates");

    public static final Mode RESTORES = new Mode("restores");

    public static final Mode REPLACES = new Mode("replaces");

    public static final Mode PRESERVES = new Mode("preserves");

    public static final Mode EVALUATES = new Mode("evaluates");

    public static final Mode REASSIGNS = new Mode("reassigns");

    public static final Mode CLEARS = new Mode("clears");

    public static final Mode ALTERS = new Mode("alters");

    public static final Mode STATE = new Mode("State");

    public static final Mode OPER_NAME = new Mode("Oper_Name");

    public static final Mode LOCAL = new Mode("Local");

    public static final Mode FIELD = new Mode("Field");

    public static final Mode MATH = new Mode("Math");

    public static final Mode DEFINITION = new Mode("Definition");

    public static final Mode DEF_PARAM = new Mode("Def_Param");

    public static final Mode CONCEPTUAL = new Mode("Conceptual");

    public static final Mode EXEMPLAR = new Mode("Exemplar");

    public static final Mode MATH_FIELD = new Mode("Math_Field");

    public String getModeName() {
        return modeName;
    }

    public static boolean equals(Mode a, Mode b) {
        boolean result;
        if (a.modeName.equals(b.modeName)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean isMathMode() {
        if (equals(this, MATH) || equals(this, DEFINITION) || equals(this, DEF_PARAM) || equals(this, CONCEPTUAL) || equals(this, EXEMPLAR) || equals(this, MATH_FIELD)) {
            return true;
        } else return false;
    }

    public static boolean implementsCompatible(Mode actual, Mode formal) {
        boolean result;
        if (formal.equals(REASSIGNS) || actual.equals(REASSIGNS)) {
            result = true;
        } else {
            if (formal.equals(UPDATES)) {
                result = updatesImplementsCompatible(actual);
            } else if (formal.equals(ALTERS)) {
                result = altersImplementsCompatible(actual);
            } else if (formal.equals(REPLACES)) {
                result = replacesImplementsCompatible(actual);
            } else if (formal.equals(CLEARS)) {
                result = clearsImplementsCompatible(actual);
            } else if (formal.equals(RESTORES)) {
                result = restoresImplementsCompatible(actual);
            } else if (formal.equals(PRESERVES)) {
                result = preservesImplementsCompatible(actual);
            } else if (formal.equals(EVALUATES)) {
                result = evaluatesImplementsCompatible(actual);
            } else if (formal.equals(REASSIGNS)) {
                result = evaluatesImplementsCompatible(actual);
            } else {
                result = false;
            }
        }
        return result;
    }

    public static boolean callCompatible(Mode actual, Mode formal) {
        boolean result;
        if (formal.equals(UPDATES)) {
            result = updatesCallCompatible(actual);
        } else if (formal.equals(ALTERS)) {
            result = altersCallCompatible(actual);
        } else if (formal.equals(REPLACES)) {
            result = replacesCallCompatible(actual);
        } else if (formal.equals(CLEARS)) {
            result = clearsCallCompatible(actual);
        } else if (formal.equals(RESTORES)) {
            result = restoresCallCompatible(actual);
        } else if (formal.equals(PRESERVES)) {
            result = preservesCallCompatible(actual);
        } else if (formal.equals(EVALUATES)) {
            result = evaluatesCallCompatible(actual);
        } else if (formal.equals(REASSIGNS)) {
            result = evaluatesCallCompatible(actual);
        } else {
            result = false;
        }
        return result;
    }

    public String toString() {
        return modeName;
    }

    private static boolean updatesImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(PRESERVES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean altersImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(PRESERVES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean replacesImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(REPLACES) || actual.equals(CLEARS)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean clearsImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(CLEARS)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean restoresImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(RESTORES) || actual.equals(PRESERVES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean preservesImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(PRESERVES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean evaluatesImplementsCompatible(Mode actual) {
        boolean result;
        if (actual.equals(EVALUATES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean updatesCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(REPLACES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean altersCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(REPLACES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean replacesCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(REPLACES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean clearsCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(REPLACES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean restoresCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(UPDATES) || actual.equals(ALTERS) || actual.equals(REPLACES) || actual.equals(CLEARS) || actual.equals(RESTORES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean preservesCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(PRESERVES) || actual.equals(LOCAL)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private static boolean evaluatesCallCompatible(Mode actual) {
        boolean result;
        if (actual.equals(EVALUATES)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
