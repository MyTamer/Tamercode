package frege.imp.tree;

import org.eclipse.imp.editor.ModelTreeNode;
import org.eclipse.imp.preferences.PreferenceValueParser.AbstractVisitor;
import org.eclipse.imp.services.base.TreeModelBuilderBase;
import frege.List.TTree;
import frege.compiler.Data.TExprT;
import frege.compiler.Data.TGlobal;
import frege.compiler.BaseTypes.TPosition;
import frege.compiler.Data.TQName;
import frege.compiler.Data.TSubSt;
import frege.compiler.Data.TSymbol;
import frege.compiler.EclipseUtil;
import frege.imp.parser.FregeParseController;
import frege.prelude.PreludeBase.TList;
import frege.prelude.PreludeBase.TList.DCons;
import frege.prelude.PreludeBase.TMaybe;
import frege.prelude.PreludeBase.TTuple3;
import frege.rt.Box;

public class FregeTreeModelBuilder extends TreeModelBuilderBase {

    private TGlobal prev = null;

    @Override
    public void visitTree(Object root) {
        if (root == null || !(root instanceof TGlobal)) return;
        TGlobal global = (TGlobal) root;
        if (prev == null || FregeParseController.achievement(prev) <= FregeParseController.achievement(global)) prev = global; else global = prev;
        FregeModelVisitor visitor = new FregeModelVisitor();
        visitor.visit(global);
    }

    public static final int data = 0;

    public static final int link = 1;

    public static final int dcon = 2;

    public static final int clas = 3;

    public static final int inst = 4;

    public static final int func = 5;

    public static final int type = 6;

    public static final String[] categories = new String[] { "Data Types", "Imported Items", "Constructors", "Type Classes", "Instances", "Functions and Values", "Type Aliases" };

    public static final int[] order = new int[] { link, clas, inst, type, data, dcon, func };

    public class FregeModelVisitor {

        public boolean visit(TGlobal g, TTree env, boolean top) {
            final TList syms = (TList) EclipseUtil.symbols(env)._e();
            for (int cat : order) {
                if (!top) {
                    if (cat != func && cat != dcon) continue;
                } else if (cat == dcon) continue;
                TList.DCons elem = syms._Cons();
                boolean found = false;
                while (elem != null) {
                    final TSymbol sym = (TSymbol) elem.mem1._e();
                    elem = ((TList) elem.mem2._e())._Cons();
                    if (sym.constructor() != cat) continue;
                    if (sym.constructor() == link && TQName.M.our(TSymbol.M.alias(sym), g)) continue;
                    if (top) {
                        if (!found) {
                            pushSubItem(new CategoryItem(categories[cat], TSymbol.M.pos(sym)));
                            found = true;
                        }
                    }
                    visit(g, sym);
                }
                if (found) popSubItem();
                found = false;
            }
            return true;
        }

        public boolean visit(TGlobal g, TSymbol sym) {
            pushSubItem(new SymbolItem(g, sym));
            if (TSymbol.M.has$env(sym)) visit(g, TSymbol.M.env(sym), false); else if (TSymbol.M.has$expr(sym)) {
                final TMaybe mbex = TSymbol.M.expr(sym);
                final TMaybe.DJust just = mbex._Just();
                if (just != null) {
                    final TExprT expr = (TExprT) just.mem1._e();
                    visit(g, expr);
                }
            }
            popSubItem();
            return true;
        }

        public boolean visit(TGlobal g, TExprT expr) {
            TList symbols = (TList) FregeParseController.funStG(frege.compiler.EclipseUtil.exprSymbols(expr), g);
            TList.DCons node = symbols._Cons();
            while (node != null) {
                TSymbol sym = (TSymbol) node.mem1._e();
                visit(g, sym);
                node = ((TList) node.mem2._e())._Cons();
            }
            return true;
        }

        public boolean visit(TGlobal g) {
            final TSubSt sub = TGlobal.sub(g);
            final String pack = TSubSt.thisPack(sub).j;
            pushSubItem(new PackageItem(pack, TSubSt.thisPos(sub)));
            if (!"".equals(pack)) {
                final TList pnps = (TList) EclipseUtil.imports(g)._e();
                DCons elem = pnps._Cons();
                while (elem != null) {
                    final TTuple3 tuple = (TTuple3) elem.mem1._e();
                    elem = ((TList) elem.mem2._e())._Cons();
                    final TPosition pos = (TPosition) tuple.mem1._e();
                    final String ns = Box.<String>box(tuple.mem2._e()).j;
                    final String p = Box.<String>box(tuple.mem3._e()).j;
                    createSubItem(new ImportItem(pos, ns, p));
                }
            }
            popSubItem();
            if (!"".equals(pack)) return visit(g, EclipseUtil.thisTab(g), true);
            return true;
        }
    }
}
