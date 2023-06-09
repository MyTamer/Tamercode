package edu.clemson.cs.r2jt.treewalk;

import java.util.ArrayList;
import java.util.HashMap;
import edu.clemson.cs.r2jt.absyn.*;

public class VisitorGenModuleDecDot extends TreeWalkerStackVisitor {

    private int nodeNum = 0;

    private ArrayList<ResolveConceptualElement> parentList = new ArrayList<ResolveConceptualElement>();

    private StringBuffer nodeList = new StringBuffer();

    private StringBuffer arrowList = new StringBuffer();

    public VisitorGenModuleDecDot() {
    }

    @Override
    public void preAnyStack(ResolveConceptualElement data) {
        ResolveConceptualElement parent = getParent();
        String className = data.getClass().getSimpleName();
        if (parent != null) {
            parentList.add(nodeNum, parent);
            int p = parentList.indexOf(parent) - 1;
            arrowList.append("n" + p + " -> n" + nodeNum + " //" + className + "\n");
        } else {
            parentList.add(0, null);
        }
        nodeList.append("n" + (nodeNum++) + " [label=\"" + className);
        if (data instanceof VarExp) {
            nodeList.append("\\n(" + ((VarExp) data).getName().toString() + ")");
        } else if (data instanceof NameTy) {
            nodeList.append("\\n(" + ((NameTy) data).getName().toString() + ")");
        } else if (data instanceof Object) {
            try {
                if ((data.getClass()).getMethod("toString").getDeclaringClass() == data.getClass()) {
                    nodeList.append("\\n(" + ((Object) data).toString().toString() + ")");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        nodeList.append("\"]; //" + className + "\n");
    }

    @Override
    public void postAnyStack(ResolveConceptualElement data) {
    }

    public StringBuffer getNodeList() {
        return nodeList;
    }

    public StringBuffer getArrowList() {
        return arrowList;
    }
}
