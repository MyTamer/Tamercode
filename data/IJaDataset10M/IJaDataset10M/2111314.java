package org.jmlspecs.jml6.core.javacontract.translator;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * Internal AST visitor for serializing an AST in a quick and dirty fashion.
 * For various reasons the resulting string is not necessarily legal
 * Java code; and even if it is legal Java code, it is not necessarily the string
 * that corresponds to the given AST. Although useless for most purposes, it's
 * fine for generating debug print strings.
 * <p>
 * Example usage:
 * <code>
 * <pre>
 *    NaiveASTFlattener p = new NaiveASTFlattener();
 *    node.accept(p);
 *    String result = p.getResult();
 * </pre>
 * </code>
 * Call the <code>reset</code> method to clear the previous result before reusing an
 * existing instance.
 * </p>
 * 
 * @since 2.0
 */
public class NaiveASTFlattener extends ASTVisitor {

    /**
   * Internal synonym for {@link AST#JLS2}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private static final int JLS2 = AST.JLS2;

    /**
   * The string buffer into which the serialized representation of the AST is
   * written.
   */
    protected StringBuffer buffer;

    private int indent = 0;

    /**
   * Creates a new AST printer.
   */
    public NaiveASTFlattener() {
        this.buffer = new StringBuffer();
    }

    /**
   * Internal synonym for {@link ClassInstanceCreation#getName()}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private Name getName(ClassInstanceCreation node) {
        return node.getName();
    }

    /**
   * Returns the string accumulated in the visit.
   *
   * @return the serialized 
   */
    public String getResult() {
        return this.buffer.toString();
    }

    /**
   * Internal synonym for {@link MethodDeclaration#getReturnType()}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private Type getReturnType(MethodDeclaration node) {
        return node.getReturnType();
    }

    /**
   * Internal synonym for {@link TypeDeclaration#getSuperclass()}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private Name getSuperclass(TypeDeclaration node) {
        return node.getSuperclass();
    }

    /**
   * Internal synonym for {@link TypeDeclarationStatement#getTypeDeclaration()}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private TypeDeclaration getTypeDeclaration(TypeDeclarationStatement node) {
        return node.getTypeDeclaration();
    }

    void printIndent() {
        for (int i = 0; i < this.indent; i++) this.buffer.append("  ");
    }

    /**
   * Appends the text representation of the given modifier flags, followed by a single space.
   * Used for JLS2 modifiers.
   * 
   * @param modifiers the modifier flags
   */
    void printModifiers(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            this.buffer.append("public ");
        }
        if (Modifier.isProtected(modifiers)) {
            this.buffer.append("protected ");
        }
        if (Modifier.isPrivate(modifiers)) {
            this.buffer.append("private ");
        }
        if (Modifier.isStatic(modifiers)) {
            this.buffer.append("static ");
        }
        if (Modifier.isAbstract(modifiers)) {
            this.buffer.append("abstract ");
        }
        if (Modifier.isFinal(modifiers)) {
            this.buffer.append("final ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            this.buffer.append("synchronized ");
        }
        if (Modifier.isVolatile(modifiers)) {
            this.buffer.append("volatile ");
        }
        if (Modifier.isNative(modifiers)) {
            this.buffer.append("native ");
        }
        if (Modifier.isStrictfp(modifiers)) {
            this.buffer.append("strictfp ");
        }
        if (Modifier.isTransient(modifiers)) {
            this.buffer.append("transient ");
        }
    }

    /**
   * Appends the text representation of the given modifier flags, followed by a single space.
   * Used for 3.0 modifiers and annotations.
   * 
   * @param ext the list of modifier and annotation nodes
   * (element type: <code>IExtendedModifiers</code>)
   */
    void printModifiers(List ext) {
        for (Iterator it = ext.iterator(); it.hasNext(); ) {
            ASTNode p = (ASTNode) it.next();
            p.accept(this);
            this.buffer.append(" ");
        }
    }

    /**
   * Resets this printer so that it can be used again.
   */
    public void reset() {
        this.buffer.setLength(0);
    }

    /**
   * Internal synonym for {@link TypeDeclaration#superInterfaces()}. Use to alleviate
   * deprecation warnings.
   * @deprecated
   * @since 3.4
   */
    private List superInterfaces(TypeDeclaration node) {
        return node.superInterfaces();
    }

    public boolean visit(AnnotationTypeDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        printModifiers(node.modifiers());
        this.buffer.append("@interface ");
        node.getName().accept(this);
        this.buffer.append(" {");
        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext(); ) {
            BodyDeclaration d = (BodyDeclaration) it.next();
            d.accept(this);
        }
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(AnnotationTypeMemberDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        printModifiers(node.modifiers());
        node.getType().accept(this);
        this.buffer.append(" ");
        node.getName().accept(this);
        this.buffer.append("()");
        if (node.getDefault() != null) {
            this.buffer.append(" default ");
            node.getDefault().accept(this);
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(AnonymousClassDeclaration node) {
        this.buffer.append("{\n");
        this.indent++;
        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext(); ) {
            BodyDeclaration b = (BodyDeclaration) it.next();
            b.accept(this);
        }
        this.indent--;
        printIndent();
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(ArrayAccess node) {
        node.getArray().accept(this);
        this.buffer.append("[");
        node.getIndex().accept(this);
        this.buffer.append("]");
        return false;
    }

    public boolean visit(ArrayCreation node) {
        this.buffer.append("new ");
        ArrayType at = node.getType();
        int dims = at.getDimensions();
        Type elementType = at.getElementType();
        elementType.accept(this);
        for (Iterator it = node.dimensions().iterator(); it.hasNext(); ) {
            this.buffer.append("[");
            Expression e = (Expression) it.next();
            e.accept(this);
            this.buffer.append("]");
            dims--;
        }
        for (int i = 0; i < dims; i++) {
            this.buffer.append("[]");
        }
        if (node.getInitializer() != null) {
            node.getInitializer().accept(this);
        }
        return false;
    }

    public boolean visit(ArrayInitializer node) {
        this.buffer.append("{");
        for (Iterator it = node.expressions().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append("}");
        return false;
    }

    public boolean visit(ArrayType node) {
        node.getComponentType().accept(this);
        this.buffer.append("[]");
        return false;
    }

    public boolean visit(AssertStatement node) {
        printIndent();
        this.buffer.append("assert ");
        node.getExpression().accept(this);
        if (node.getMessage() != null) {
            this.buffer.append(" : ");
            node.getMessage().accept(this);
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(Assignment node) {
        node.getLeftHandSide().accept(this);
        this.buffer.append(node.getOperator().toString());
        node.getRightHandSide().accept(this);
        return false;
    }

    public boolean visit(Block node) {
        this.buffer.append("{\n");
        this.indent++;
        for (Iterator it = node.statements().iterator(); it.hasNext(); ) {
            Statement s = (Statement) it.next();
            s.accept(this);
        }
        this.indent--;
        printIndent();
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(BlockComment node) {
        printIndent();
        this.buffer.append("/* */");
        return false;
    }

    public boolean visit(BooleanLiteral node) {
        if (node.booleanValue() == true) {
            this.buffer.append("true");
        } else {
            this.buffer.append("false");
        }
        return false;
    }

    public boolean visit(BreakStatement node) {
        printIndent();
        this.buffer.append("break");
        if (node.getLabel() != null) {
            this.buffer.append(" ");
            node.getLabel().accept(this);
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(CastExpression node) {
        this.buffer.append("(");
        node.getType().accept(this);
        this.buffer.append(")");
        node.getExpression().accept(this);
        return false;
    }

    public boolean visit(CatchClause node) {
        this.buffer.append("catch (");
        node.getException().accept(this);
        this.buffer.append(") ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(CharacterLiteral node) {
        this.buffer.append(node.getEscapedValue());
        return false;
    }

    public boolean visit(ClassInstanceCreation node) {
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
            this.buffer.append(".");
        }
        this.buffer.append("new ");
        if (node.getAST().apiLevel() == JLS2) {
            this.getName(node).accept(this);
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
            node.getType().accept(this);
        }
        this.buffer.append("(");
        for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        if (node.getAnonymousClassDeclaration() != null) {
            node.getAnonymousClassDeclaration().accept(this);
        }
        return false;
    }

    public boolean visit(CompilationUnit node) {
        if (node.getPackage() != null) {
            node.getPackage().accept(this);
        }
        for (Iterator it = node.imports().iterator(); it.hasNext(); ) {
            ImportDeclaration d = (ImportDeclaration) it.next();
            d.accept(this);
        }
        for (Iterator it = node.types().iterator(); it.hasNext(); ) {
            AbstractTypeDeclaration d = (AbstractTypeDeclaration) it.next();
            d.accept(this);
        }
        return false;
    }

    public boolean visit(ConditionalExpression node) {
        node.getExpression().accept(this);
        this.buffer.append(" ? ");
        node.getThenExpression().accept(this);
        this.buffer.append(" : ");
        node.getElseExpression().accept(this);
        return false;
    }

    public boolean visit(ConstructorInvocation node) {
        printIndent();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        this.buffer.append("this(");
        for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(");\n");
        return false;
    }

    public boolean visit(ContinueStatement node) {
        printIndent();
        this.buffer.append("continue");
        if (node.getLabel() != null) {
            this.buffer.append(" ");
            node.getLabel().accept(this);
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(DoStatement node) {
        printIndent();
        this.buffer.append("do ");
        node.getBody().accept(this);
        this.buffer.append(" while (");
        node.getExpression().accept(this);
        this.buffer.append(");\n");
        return false;
    }

    public boolean visit(EmptyStatement node) {
        printIndent();
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(EnhancedForStatement node) {
        printIndent();
        this.buffer.append("for (");
        node.getParameter().accept(this);
        this.buffer.append(" : ");
        node.getExpression().accept(this);
        this.buffer.append(") ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(EnumConstantDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        printModifiers(node.modifiers());
        node.getName().accept(this);
        if (!node.arguments().isEmpty()) {
            this.buffer.append("(");
            for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
                Expression e = (Expression) it.next();
                e.accept(this);
                if (it.hasNext()) {
                    this.buffer.append(",");
                }
            }
            this.buffer.append(")");
        }
        if (node.getAnonymousClassDeclaration() != null) {
            node.getAnonymousClassDeclaration().accept(this);
        }
        return false;
    }

    public boolean visit(EnumDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        printModifiers(node.modifiers());
        this.buffer.append("enum ");
        node.getName().accept(this);
        this.buffer.append(" ");
        if (!node.superInterfaceTypes().isEmpty()) {
            this.buffer.append("implements ");
            for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext(); ) {
                Type t = (Type) it.next();
                t.accept(this);
                if (it.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(" ");
        }
        this.buffer.append("{");
        for (Iterator it = node.enumConstants().iterator(); it.hasNext(); ) {
            EnumConstantDeclaration d = (EnumConstantDeclaration) it.next();
            d.accept(this);
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
        if (!node.bodyDeclarations().isEmpty()) {
            this.buffer.append("; ");
            for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext(); ) {
                BodyDeclaration d = (BodyDeclaration) it.next();
                d.accept(this);
            }
        }
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(ExpressionStatement node) {
        printIndent();
        node.getExpression().accept(this);
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(FieldAccess node) {
        node.getExpression().accept(this);
        this.buffer.append(".");
        node.getName().accept(this);
        return false;
    }

    public boolean visit(FieldDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        node.getType().accept(this);
        this.buffer.append(" ");
        for (Iterator it = node.fragments().iterator(); it.hasNext(); ) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            f.accept(this);
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(ForStatement node) {
        printIndent();
        this.buffer.append("for (");
        for (Iterator it = node.initializers().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) buffer.append(", ");
        }
        this.buffer.append("; ");
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        this.buffer.append("; ");
        for (Iterator it = node.updaters().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) buffer.append(", ");
        }
        this.buffer.append(") ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(IfStatement node) {
        printIndent();
        this.buffer.append("if (");
        node.getExpression().accept(this);
        this.buffer.append(") ");
        node.getThenStatement().accept(this);
        if (node.getElseStatement() != null) {
            this.buffer.append(" else ");
            node.getElseStatement().accept(this);
        }
        return false;
    }

    public boolean visit(ImportDeclaration node) {
        printIndent();
        this.buffer.append("import ");
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.isStatic()) {
                this.buffer.append("static ");
            }
        }
        node.getName().accept(this);
        if (node.isOnDemand()) {
            this.buffer.append(".*");
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(InfixExpression node) {
        node.getLeftOperand().accept(this);
        this.buffer.append(' ');
        this.buffer.append(node.getOperator().toString());
        this.buffer.append(' ');
        node.getRightOperand().accept(this);
        final List extendedOperands = node.extendedOperands();
        if (extendedOperands.size() != 0) {
            this.buffer.append(' ');
            for (Iterator it = extendedOperands.iterator(); it.hasNext(); ) {
                this.buffer.append(node.getOperator().toString()).append(' ');
                Expression e = (Expression) it.next();
                e.accept(this);
            }
        }
        return false;
    }

    public boolean visit(Initializer node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(InstanceofExpression node) {
        node.getLeftOperand().accept(this);
        this.buffer.append(" instanceof ");
        node.getRightOperand().accept(this);
        return false;
    }

    public boolean visit(Javadoc node) {
        printIndent();
        this.buffer.append("/** ");
        for (Iterator it = node.tags().iterator(); it.hasNext(); ) {
            ASTNode e = (ASTNode) it.next();
            e.accept(this);
        }
        this.buffer.append("\n */\n");
        return false;
    }

    public boolean visit(LabeledStatement node) {
        printIndent();
        node.getLabel().accept(this);
        this.buffer.append(": ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(LineComment node) {
        this.buffer.append("//\n");
        return false;
    }

    public boolean visit(MarkerAnnotation node) {
        this.buffer.append("@");
        node.getTypeName().accept(this);
        return false;
    }

    public boolean visit(MemberRef node) {
        if (node.getQualifier() != null) {
            node.getQualifier().accept(this);
        }
        this.buffer.append("#");
        node.getName().accept(this);
        return false;
    }

    public boolean visit(MemberValuePair node) {
        node.getName().accept(this);
        this.buffer.append("=");
        node.getValue().accept(this);
        return false;
    }

    public boolean visit(MethodDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        printIndent();
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
            if (!node.typeParameters().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeParameters().iterator(); it.hasNext(); ) {
                    TypeParameter t = (TypeParameter) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        if (!node.isConstructor()) {
            if (node.getAST().apiLevel() == JLS2) {
                this.getReturnType(node).accept(this);
            } else {
                if (node.getReturnType2() != null) {
                    node.getReturnType2().accept(this);
                } else {
                    this.buffer.append("void");
                }
            }
            this.buffer.append(" ");
        }
        node.getName().accept(this);
        this.buffer.append("(");
        for (Iterator it = node.parameters().iterator(); it.hasNext(); ) {
            SingleVariableDeclaration v = (SingleVariableDeclaration) it.next();
            v.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        for (int i = 0; i < node.getExtraDimensions(); i++) {
            this.buffer.append("[]");
        }
        if (!node.thrownExceptions().isEmpty()) {
            this.buffer.append(" throws ");
            for (Iterator it = node.thrownExceptions().iterator(); it.hasNext(); ) {
                Name n = (Name) it.next();
                n.accept(this);
                if (it.hasNext()) {
                    this.buffer.append(", ");
                }
            }
            this.buffer.append(" ");
        }
        if (node.getBody() == null) {
            this.buffer.append(";\n");
        } else {
            node.getBody().accept(this);
        }
        return false;
    }

    public boolean visit(MethodInvocation node) {
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
            this.buffer.append(".");
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        node.getName().accept(this);
        this.buffer.append("(");
        for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        return false;
    }

    public boolean visit(MethodRef node) {
        if (node.getQualifier() != null) {
            node.getQualifier().accept(this);
        }
        this.buffer.append("#");
        node.getName().accept(this);
        this.buffer.append("(");
        for (Iterator it = node.parameters().iterator(); it.hasNext(); ) {
            MethodRefParameter e = (MethodRefParameter) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        return false;
    }

    public boolean visit(MethodRefParameter node) {
        node.getType().accept(this);
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.isVarargs()) {
                this.buffer.append("...");
            }
        }
        if (node.getName() != null) {
            this.buffer.append(" ");
            node.getName().accept(this);
        }
        return false;
    }

    public boolean visit(Modifier node) {
        this.buffer.append(node.getKeyword().toString());
        return false;
    }

    public boolean visit(NormalAnnotation node) {
        this.buffer.append("@");
        node.getTypeName().accept(this);
        this.buffer.append("(");
        for (Iterator it = node.values().iterator(); it.hasNext(); ) {
            MemberValuePair p = (MemberValuePair) it.next();
            p.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        return false;
    }

    public boolean visit(NullLiteral node) {
        this.buffer.append("null");
        return false;
    }

    public boolean visit(NumberLiteral node) {
        this.buffer.append(node.getToken());
        return false;
    }

    public boolean visit(PackageDeclaration node) {
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.getJavadoc() != null) {
                node.getJavadoc().accept(this);
            }
            for (Iterator it = node.annotations().iterator(); it.hasNext(); ) {
                Annotation p = (Annotation) it.next();
                p.accept(this);
                this.buffer.append(" ");
            }
        }
        printIndent();
        this.buffer.append("package ");
        node.getName().accept(this);
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(ParameterizedType node) {
        node.getType().accept(this);
        this.buffer.append("<");
        for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
            Type t = (Type) it.next();
            t.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(">");
        return false;
    }

    public boolean visit(ParenthesizedExpression node) {
        this.buffer.append("(");
        node.getExpression().accept(this);
        this.buffer.append(")");
        return false;
    }

    public boolean visit(PostfixExpression node) {
        node.getOperand().accept(this);
        this.buffer.append(node.getOperator().toString());
        return false;
    }

    public boolean visit(PrefixExpression node) {
        this.buffer.append(node.getOperator().toString());
        node.getOperand().accept(this);
        return false;
    }

    public boolean visit(PrimitiveType node) {
        this.buffer.append(node.getPrimitiveTypeCode().toString());
        return false;
    }

    public boolean visit(QualifiedName node) {
        node.getQualifier().accept(this);
        this.buffer.append(".");
        node.getName().accept(this);
        return false;
    }

    public boolean visit(QualifiedType node) {
        node.getQualifier().accept(this);
        this.buffer.append(".");
        node.getName().accept(this);
        return false;
    }

    public boolean visit(ReturnStatement node) {
        printIndent();
        this.buffer.append("return");
        if (node.getExpression() != null) {
            this.buffer.append(" ");
            node.getExpression().accept(this);
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(SimpleName node) {
        this.buffer.append(node.getIdentifier());
        return false;
    }

    public boolean visit(SimpleType node) {
        return true;
    }

    public boolean visit(SingleMemberAnnotation node) {
        this.buffer.append("@");
        node.getTypeName().accept(this);
        this.buffer.append("(");
        node.getValue().accept(this);
        this.buffer.append(")");
        return false;
    }

    public boolean visit(SingleVariableDeclaration node) {
        printIndent();
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        node.getType().accept(this);
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.isVarargs()) {
                this.buffer.append("...");
            }
        }
        this.buffer.append(" ");
        node.getName().accept(this);
        for (int i = 0; i < node.getExtraDimensions(); i++) {
            this.buffer.append("[]");
        }
        if (node.getInitializer() != null) {
            this.buffer.append("=");
            node.getInitializer().accept(this);
        }
        return false;
    }

    public boolean visit(StringLiteral node) {
        this.buffer.append(node.getEscapedValue());
        return false;
    }

    public boolean visit(SuperConstructorInvocation node) {
        printIndent();
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
            this.buffer.append(".");
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        this.buffer.append("super(");
        for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(");\n");
        return false;
    }

    public boolean visit(SuperFieldAccess node) {
        if (node.getQualifier() != null) {
            node.getQualifier().accept(this);
            this.buffer.append(".");
        }
        this.buffer.append("super.");
        node.getName().accept(this);
        return false;
    }

    public boolean visit(SuperMethodInvocation node) {
        if (node.getQualifier() != null) {
            node.getQualifier().accept(this);
            this.buffer.append(".");
        }
        this.buffer.append("super.");
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeArguments().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        node.getName().accept(this);
        this.buffer.append("(");
        for (Iterator it = node.arguments().iterator(); it.hasNext(); ) {
            Expression e = (Expression) it.next();
            e.accept(this);
            if (it.hasNext()) {
                this.buffer.append(",");
            }
        }
        this.buffer.append(")");
        return false;
    }

    public boolean visit(SwitchCase node) {
        if (node.isDefault()) {
            this.buffer.append("default :\n");
        } else {
            this.buffer.append("case ");
            node.getExpression().accept(this);
            this.buffer.append(":\n");
        }
        this.indent++;
        return false;
    }

    public boolean visit(SwitchStatement node) {
        this.buffer.append("switch (");
        node.getExpression().accept(this);
        this.buffer.append(") ");
        this.buffer.append("{\n");
        this.indent++;
        for (Iterator it = node.statements().iterator(); it.hasNext(); ) {
            Statement s = (Statement) it.next();
            s.accept(this);
            this.indent--;
        }
        this.indent--;
        printIndent();
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(SynchronizedStatement node) {
        this.buffer.append("synchronized (");
        node.getExpression().accept(this);
        this.buffer.append(") ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(TagElement node) {
        if (node.isNested()) {
            this.buffer.append("{");
        } else {
            this.buffer.append("\n * ");
        }
        boolean previousRequiresWhiteSpace = false;
        if (node.getTagName() != null) {
            this.buffer.append(node.getTagName());
            previousRequiresWhiteSpace = true;
        }
        boolean previousRequiresNewLine = false;
        for (Iterator it = node.fragments().iterator(); it.hasNext(); ) {
            ASTNode e = (ASTNode) it.next();
            boolean currentIncludesWhiteSpace = (e instanceof TextElement);
            if (previousRequiresNewLine && currentIncludesWhiteSpace) {
                this.buffer.append("\n * ");
            }
            previousRequiresNewLine = currentIncludesWhiteSpace;
            if (previousRequiresWhiteSpace && !currentIncludesWhiteSpace) {
                this.buffer.append(" ");
            }
            e.accept(this);
            previousRequiresWhiteSpace = !currentIncludesWhiteSpace && !(e instanceof TagElement);
        }
        if (node.isNested()) {
            this.buffer.append("}");
        }
        return false;
    }

    public boolean visit(TextElement node) {
        this.buffer.append(node.getText());
        return false;
    }

    public boolean visit(ThisExpression node) {
        if (node.getQualifier() != null) {
            node.getQualifier().accept(this);
            this.buffer.append(".");
        }
        this.buffer.append("this");
        return false;
    }

    public boolean visit(ThrowStatement node) {
        printIndent();
        this.buffer.append("throw ");
        node.getExpression().accept(this);
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(TryStatement node) {
        printIndent();
        this.buffer.append("try ");
        node.getBody().accept(this);
        this.buffer.append(" ");
        for (Iterator it = node.catchClauses().iterator(); it.hasNext(); ) {
            CatchClause cc = (CatchClause) it.next();
            cc.accept(this);
        }
        if (node.getFinally() != null) {
            this.buffer.append(" finally ");
            node.getFinally().accept(this);
        }
        return false;
    }

    public boolean visit(TypeDeclaration node) {
        if (node.getJavadoc() != null) {
            node.getJavadoc().accept(this);
        }
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        this.buffer.append(node.isInterface() ? "interface " : "class ");
        node.getName().accept(this);
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeParameters().isEmpty()) {
                this.buffer.append("<");
                for (Iterator it = node.typeParameters().iterator(); it.hasNext(); ) {
                    TypeParameter t = (TypeParameter) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(",");
                    }
                }
                this.buffer.append(">");
            }
        }
        this.buffer.append(" ");
        if (node.getAST().apiLevel() == JLS2) {
            if (this.getSuperclass(node) != null) {
                this.buffer.append("extends ");
                this.getSuperclass(node).accept(this);
                this.buffer.append(" ");
            }
            if (!this.superInterfaces(node).isEmpty()) {
                this.buffer.append(node.isInterface() ? "extends " : "implements ");
                for (Iterator it = this.superInterfaces(node).iterator(); it.hasNext(); ) {
                    Name n = (Name) it.next();
                    n.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(", ");
                    }
                }
                this.buffer.append(" ");
            }
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.getSuperclassType() != null) {
                this.buffer.append("extends ");
                node.getSuperclassType().accept(this);
                this.buffer.append(" ");
            }
            if (!node.superInterfaceTypes().isEmpty()) {
                this.buffer.append(node.isInterface() ? "extends " : "implements ");
                for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext(); ) {
                    Type t = (Type) it.next();
                    t.accept(this);
                    if (it.hasNext()) {
                        this.buffer.append(", ");
                    }
                }
                this.buffer.append(" ");
            }
        }
        this.buffer.append("{\n");
        this.indent++;
        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext(); ) {
            BodyDeclaration d = (BodyDeclaration) it.next();
            d.accept(this);
        }
        this.indent--;
        printIndent();
        this.buffer.append("}\n");
        return false;
    }

    public boolean visit(TypeDeclarationStatement node) {
        if (node.getAST().apiLevel() == JLS2) {
            this.getTypeDeclaration(node).accept(this);
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            node.getDeclaration().accept(this);
        }
        return false;
    }

    public boolean visit(TypeLiteral node) {
        node.getType().accept(this);
        this.buffer.append(".class");
        return false;
    }

    public boolean visit(TypeParameter node) {
        node.getName().accept(this);
        if (!node.typeBounds().isEmpty()) {
            this.buffer.append(" extends ");
            for (Iterator it = node.typeBounds().iterator(); it.hasNext(); ) {
                Type t = (Type) it.next();
                t.accept(this);
                if (it.hasNext()) {
                    this.buffer.append(" & ");
                }
            }
        }
        return false;
    }

    public boolean visit(VariableDeclarationExpression node) {
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        node.getType().accept(this);
        this.buffer.append(" ");
        for (Iterator it = node.fragments().iterator(); it.hasNext(); ) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            f.accept(this);
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
        return false;
    }

    public boolean visit(VariableDeclarationFragment node) {
        node.getName().accept(this);
        for (int i = 0; i < node.getExtraDimensions(); i++) {
            this.buffer.append("[]");
        }
        if (node.getInitializer() != null) {
            this.buffer.append("=");
            node.getInitializer().accept(this);
        }
        return false;
    }

    public boolean visit(VariableDeclarationStatement node) {
        printIndent();
        if (node.getAST().apiLevel() == JLS2) {
            printModifiers(node.getModifiers());
        }
        if (node.getAST().apiLevel() >= AST.JLS3) {
            printModifiers(node.modifiers());
        }
        node.getType().accept(this);
        this.buffer.append(" ");
        for (Iterator it = node.fragments().iterator(); it.hasNext(); ) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            f.accept(this);
            if (it.hasNext()) {
                this.buffer.append(", ");
            }
        }
        this.buffer.append(";\n");
        return false;
    }

    public boolean visit(WhileStatement node) {
        printIndent();
        this.buffer.append("while (");
        node.getExpression().accept(this);
        this.buffer.append(") ");
        node.getBody().accept(this);
        return false;
    }

    public boolean visit(WildcardType node) {
        this.buffer.append("?");
        Type bound = node.getBound();
        if (bound != null) {
            if (node.isUpperBound()) {
                this.buffer.append(" extends ");
            } else {
                this.buffer.append(" super ");
            }
            bound.accept(this);
        }
        return false;
    }
}
