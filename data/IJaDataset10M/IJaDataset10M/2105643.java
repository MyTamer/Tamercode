package net.sourceforge.pmd.lang.java.rule.optimizations;

import java.util.List;
import java.util.Map;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.AccessNode;
import net.sourceforge.pmd.lang.java.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.java.symboltable.Scope;
import net.sourceforge.pmd.lang.java.symboltable.VariableNameDeclaration;

public class MethodArgumentCouldBeFinalRule extends AbstractOptimizationRule {

    @Override
    public Object visit(ASTMethodDeclaration meth, Object data) {
        if (meth.isNative() || meth.isAbstract()) {
            return data;
        }
        this.lookForViolation(meth.getScope(), data);
        return super.visit(meth, data);
    }

    private void lookForViolation(Scope scope, Object data) {
        Map<VariableNameDeclaration, List<NameOccurrence>> decls = scope.getVariableDeclarations();
        for (Map.Entry<VariableNameDeclaration, List<NameOccurrence>> entry : decls.entrySet()) {
            VariableNameDeclaration var = entry.getKey();
            AccessNode node = var.getAccessNodeParent();
            if (!node.isFinal() && (node instanceof ASTFormalParameter) && !assigned(entry.getValue())) {
                addViolation(data, (Node) node, var.getImage());
            }
        }
    }

    @Override
    public Object visit(ASTConstructorDeclaration constructor, Object data) {
        this.lookForViolation(constructor.getScope(), data);
        return super.visit(constructor, data);
    }
}
