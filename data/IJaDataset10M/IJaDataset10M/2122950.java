package org.jmlspecs.ajmlrac;

import org.jmlspecs.checker.JmlClassDeclaration;
import org.jmlspecs.checker.JmlTypeDeclaration;
import org.multijava.mjc.JMethodDeclarationType;

/**
 * A class for generating assertion check methods for class-level
 * assertions such as invariants and history constraints.
 * There are two types of class-level assertions:
 * <em>instance</em> (<em>non-static</em>) <em>assertions</em> and
 * <em>class</em> (<em>static</em>) <em>assertions</em>.
 * As thus, two types of assertion check methods are generated. 
 * An instance assertion check method checks both the instance and class 
 * assertions while a class assertion check method checks only the class 
 * assertionss. 
 * The generated assertion check methods inherit assertions of its superclass
 * interfaces by dynamically invoking the corresponding assertion methods.
 *
 * <p>
 * The class implements a variant of the <em>Template Pattern</em>
 * [GoF95], prescribed in the class {@link AssertionMethod}.
 * </p>
 *
 * @see AssertionMethod
 *
 * @author Henrique Rebelo
 * @version $Revision: 1.0 $
 */
public class InvariantMethodAdviceAsPreconditionConstructorCallSite extends AssertionMethod {

    /** Construct a new <code>InvariantLikeMethod</code> object. 
	 *
	 * @param isStatic the kind of assertion check method to generate;
	 *                 <tt>true</tt> for static and <tt>false</tt> for 
	 *                 non-static (instance) assertion check method.
	 * @param typeDecl the target type declaration whose assertion check
	 *                 methods are to be generated.
	 */
    public InvariantMethodAdviceAsPreconditionConstructorCallSite(boolean isStatic, JmlTypeDeclaration typeDecl) {
        this.isStatic = isStatic;
        this.typeDecl = typeDecl;
        this.exceptionToThrow = "JMLInvariantError";
        this.hasStatInv = !this.combineInvariantsWithNonNull(STAT_INV).equals("");
        this.hasPublicStatInv = !this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PUBLIC).equals("");
        this.hasProtectedStatInv = !this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PROTECTED).equals("");
        this.hasDefaultStatInv = !this.combineVisibilityInvariantsWithNonNull(STAT_INV, 0L).equals("");
        this.hasPrivateStatInv = !this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PRIVATE).equals("");
        this.publicStatInvPred = this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PUBLIC);
        this.protectedStatInvPred = this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PROTECTED);
        this.defaultStatInvPred = this.combineVisibilityInvariantsWithNonNull(STAT_INV, 0L);
        this.privateStatInvPred = this.combineVisibilityInvariantsWithNonNull(STAT_INV, ACC_PRIVATE);
        javadoc = "/** Generated by JML to check " + "static" + " invariants of \n" + ((this.typeDecl instanceof JmlClassDeclaration) ? " * class " : " * interface ") + this.typeDecl.ident() + ". */";
    }

    /** Generate and return a type-level assertion check method such
	 * as invariants and history constraints.  Append to the body
	 * (<code>stmt</code>): (1) code to check the inherited assertions
	 * if any, and (2) code to throw an appropriate exception to
	 * notify an assertion violation. 
	 * 
	 * @param stmt code to evaluate the assertions; the result is supposed
	 *             to be stored in the variable <code>VN_ASSERTION</code>. 
	 *             A <code>null</code> value means that no assertion is 
	 *             specified or the assertion is not executable.
	 */
    public JMethodDeclarationType generate(RacNode stmt) {
        return null;
    }

    public JMethodDeclarationType generate() {
        StringBuffer code = buildBeforeAdvice();
        code.append("\n");
        return RacParser.parseMethod(code.toString(), null);
    }

    /** Builds and returns the method header of the assertion check
	 * method as a string.
	 * 
	 */
    protected StringBuffer buildBeforeAdvice() {
        String classQualifiedName = this.typeDecl.getCClass().getJavaName();
        String packageQualifiedName = this.typeDecl.getCClass().getJavaName().replace(this.typeDecl.ident(), "");
        final StringBuffer code = new StringBuffer();
        final String HEADER = "<init>@pre <File \\\"" + this.typeDecl.ident() + ".java\\\">";
        String errorMsgForPublicStatInv = "\"";
        String errorMsgForProtectedStatInv = "\"";
        String errorMsgForDefaultStatInv = "\"";
        String errorMsgForPrivateStatInv = "\"";
        String evalErrorMsgForPublicStatInv = "\"";
        String evalErrorMsgForProtectedStatInv = "\"";
        String evalErrorMsgForDefaultStatInv = "\"";
        String evalErrorMsgForPrivateStatInv = "\"";
        if (!this.getInvariantsTokenReference(true).equals("")) {
            errorMsgForPublicStatInv = HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            errorMsgForPublicStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PUBLIC);
            errorMsgForPublicStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PUBLIC);
            errorMsgForPublicStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC) + "\"" : "");
            errorMsgForProtectedStatInv = HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            errorMsgForProtectedStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PROTECTED);
            errorMsgForProtectedStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PROTECTED);
            errorMsgForProtectedStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED) + "\"" : "");
            errorMsgForDefaultStatInv = HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            errorMsgForDefaultStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, 0L);
            errorMsgForDefaultStatInv += this.getVisibilityContextValuesForInvariant(true, 0L);
            errorMsgForDefaultStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, 0L).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, 0L) + "\"" : "");
            errorMsgForPrivateStatInv = HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            errorMsgForPrivateStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PRIVATE);
            errorMsgForPrivateStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PRIVATE);
            errorMsgForPrivateStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE) + "\"" : "");
            evalErrorMsgForPublicStatInv = SPEC_EVAL_ERROR_MSG + HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            evalErrorMsgForPublicStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PUBLIC);
            evalErrorMsgForPublicStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PUBLIC);
            evalErrorMsgForPublicStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC) : "+\"");
            evalErrorMsgForProtectedStatInv = SPEC_EVAL_ERROR_MSG + HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            evalErrorMsgForProtectedStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PROTECTED);
            evalErrorMsgForProtectedStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PROTECTED);
            evalErrorMsgForProtectedStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED) : "+\"");
            evalErrorMsgForDefaultStatInv = SPEC_EVAL_ERROR_MSG + HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            evalErrorMsgForDefaultStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, 0L);
            evalErrorMsgForDefaultStatInv += this.getVisibilityContextValuesForInvariant(true, 0L);
            evalErrorMsgForDefaultStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, 0L).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, 0L) : "+\"");
            evalErrorMsgForPrivateStatInv = SPEC_EVAL_ERROR_MSG + HEADER + SPEC_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
            evalErrorMsgForPrivateStatInv += ", " + this.getVisibilityInvariantsTokenReference(true, ACC_PRIVATE);
            evalErrorMsgForPrivateStatInv += this.getVisibilityContextValuesForInvariant(true, ACC_PRIVATE);
            evalErrorMsgForPrivateStatInv += (!this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE).equals("") ? "+\"\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE) : "+\"");
        } else {
            if (!this.getContextValuesForDefaultInvariant(true).equals("")) {
                errorMsgForPublicStatInv = HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                errorMsgForPublicStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC) + "\"";
                errorMsgForProtectedStatInv = HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                errorMsgForProtectedStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED) + "\"";
                errorMsgForDefaultStatInv = HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                errorMsgForDefaultStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, 0L) + "\"";
                errorMsgForPrivateStatInv = HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                errorMsgForPrivateStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE) + "\"";
                evalErrorMsgForPublicStatInv = SPEC_EVAL_ERROR_MSG + HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                evalErrorMsgForPublicStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PUBLIC);
                evalErrorMsgForProtectedStatInv = SPEC_EVAL_ERROR_MSG + HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                evalErrorMsgForProtectedStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PROTECTED);
                evalErrorMsgForDefaultStatInv = SPEC_EVAL_ERROR_MSG + HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                evalErrorMsgForDefaultStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, 0L);
                evalErrorMsgForPrivateStatInv = SPEC_EVAL_ERROR_MSG + HEADER + CODE_ERROR_MSG + "File \\\"" + this.typeDecl.ident() + ".java\\\"";
                evalErrorMsgForPrivateStatInv += "\\n" + this.getVisibilityContextValuesForDefaultInvariant(true, ACC_PRIVATE);
            }
        }
        if (this.typeDecl.isClass()) {
            if (this.hasStatInv) {
                boolean hasOther = false;
                String javadocStat = "";
                if (this.javadoc != null) {
                    javadocStat = javadoc;
                } else {
                    javadocStat = "/** Generated by JML to check assertions. */";
                }
                if (this.hasPublicStatInv) {
                    code.append("\n");
                    code.append(javadocStat.replace("invariants", "public invariants"));
                    code.append("\n");
                    generateBeforeAdviceForInvChecking(classQualifiedName, packageQualifiedName, code, errorMsgForPublicStatInv, evalErrorMsgForPublicStatInv, publicStatInvPred, ACC_PUBLIC);
                    hasOther = true;
                }
                if (this.hasProtectedStatInv) {
                    if (hasOther) {
                        code.append("\n");
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "protected invariants"));
                        code.append("\n");
                    } else {
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "protected invariants"));
                        code.append("\n");
                    }
                    generateBeforeAdviceForInvChecking(classQualifiedName, packageQualifiedName, code, errorMsgForProtectedStatInv, evalErrorMsgForProtectedStatInv, this.protectedStatInvPred, ACC_PROTECTED);
                    hasOther = true;
                }
                if (this.hasDefaultStatInv) {
                    if (hasOther) {
                        code.append("\n");
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "default invariants"));
                        code.append("\n");
                    } else {
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "default invariants"));
                        code.append("\n");
                    }
                    generateBeforeAdviceForInvChecking(classQualifiedName, packageQualifiedName, code, errorMsgForDefaultStatInv, evalErrorMsgForDefaultStatInv, this.defaultStatInvPred, 0L);
                    hasOther = true;
                }
                if (this.hasPrivateStatInv) {
                    if (hasOther) {
                        code.append("\n");
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "private invariants"));
                        code.append("\n");
                    } else {
                        code.append("\n");
                        code.append(javadocStat.replace("invariants", "private invariants"));
                        code.append("\n");
                    }
                    generateBeforeAdviceForInvChecking(classQualifiedName, packageQualifiedName, code, errorMsgForPrivateStatInv, evalErrorMsgForPrivateStatInv, this.privateStatInvPred, ACC_PRIVATE);
                    hasOther = true;
                }
            }
        }
        return code;
    }

    private void generateBeforeAdviceForInvChecking(String classQualifiedName, String packageQualifiedName, final StringBuffer code, String errorMsg, String evalErrorMsg, String predClause, long visibility) {
        if (visibility == ACC_PUBLIC) {
            this.exceptionToThrow = "JMLPublicInvariantError";
        } else if (visibility == ACC_PROTECTED) {
            this.exceptionToThrow = "JMLProtectedInvariantError";
        } else if (visibility == 0L) {
            this.exceptionToThrow = "JMLDefaultInvariantError";
        } else if (visibility == ACC_PRIVATE) {
            this.exceptionToThrow = "JMLPrivateInvariantError";
        }
        code.append("before (");
        code.append(")").append(" :");
        code.append("\n");
        code.append("   call(").append(classQualifiedName).append(".new(..))");
        if (visibility == ACC_PROTECTED) {
            code.append(" &&\n");
            code.append("   within(" + packageQualifiedName + "*+)");
        } else if (visibility == 0L) {
            code.append(" &&\n");
            code.append("   within(" + packageQualifiedName + "*)");
        } else if (visibility == ACC_PRIVATE) {
            code.append(" &&\n");
            code.append("   within(" + classQualifiedName + ")");
        }
        code.append(" {\n");
        code.append("     boolean rac$b = " + predClause + ";\n");
        code.append("     String invErrorMsg = \"" + errorMsg + ";\n");
        code.append("     String evalErrorMsg = " + evalErrorMsg + "\\nCaused by: \";\n");
        code.append("     JMLChecker.checkInvariant(rac$b, invErrorMsg, evalErrorMsg, " + visibility + ");\n");
        code.append("\n").append("   ").append("}");
    }

    protected boolean canInherit() {
        return false;
    }

    private static boolean STAT_INV = true;

    private boolean hasStatInv = false;

    private boolean hasPublicStatInv = false;

    private boolean hasProtectedStatInv = false;

    private boolean hasDefaultStatInv = false;

    private boolean hasPrivateStatInv = false;

    private String publicStatInvPred = "";

    private String protectedStatInvPred = "";

    private String defaultStatInvPred = "";

    private String privateStatInvPred = "";
}