package edu.clemson.cs.r2jt.location;

import edu.clemson.cs.r2jt.absyn.Exp;
import edu.clemson.cs.r2jt.collections.Iterator;
import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.collections.Stack;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.ModuleID;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;
import edu.clemson.cs.r2jt.entry.*;
import edu.clemson.cs.r2jt.errors.ErrorHandler;
import edu.clemson.cs.r2jt.init.Environment;
import edu.clemson.cs.r2jt.scope.*;
import edu.clemson.cs.r2jt.type.*;

public class DefinitionLocator {

    private ErrorHandler err;

    private SymbolTable table;

    private boolean showErrors = true;

    private boolean local = false;

    private TypeMatcher tm;

    public DefinitionLocator(SymbolTable table, boolean err, TypeMatcher tm, ErrorHandler eh) {
        this.table = table;
        showErrors = err;
        this.tm = tm;
        this.err = eh;
    }

    public DefinitionEntry locateDefinition(PosSymbol name) throws SymbolSearchException {
        List<DefinitionEntry> opers = locateDefinitionsInStack(name);
        if (opers.size() == 0) {
            opers = locateDefinitionsInImports(name);
        }
        if (opers.size() > 1) {
            List<Location> locs = getLocationList(opers);
            if (showErrors) {
                String msg = ambigDefRefMessage(name.toString(), locs.toString());
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        } else if (opers.size() == 0) {
            if (showErrors) {
                String msg = cantFindDefMessage(name.toString());
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        } else {
            return opers.get(0);
        }
    }

    public DefinitionEntry locateDefinition(PosSymbol name, List<Type> argtypes) throws SymbolSearchException {
        List<DefinitionEntry> defs = locateDefinitionsInStack(name);
        if (defs.size() == 0) {
            defs = locateDefinitionsInImports(name);
        }
        return getUniqueDefinition(name, argtypes, defs);
    }

    public DefinitionEntry locateDefinition(PosSymbol qual, PosSymbol name) throws SymbolSearchException {
        if (qual == null) {
            return locateDefinition(name);
        }
        QualifierLocator qualifierLocator = new QualifierLocator(table, err);
        ModuleScope scope;
        try {
            scope = qualifierLocator.locateMathModule(qual);
        } catch (SymbolSearchException sx1) {
            scope = qualifierLocator.locateProgramModule(qual);
        }
        if (scope.containsDefinition(name.getSymbol())) {
            DefinitionEntry def = scope.getDefinition(name.getSymbol());
            return def;
        } else {
            if (showErrors) {
                String msg = cantFindDefInModMessage(name.toString(), qual.toString());
                err.error(qual.getLocation(), msg);
            }
            throw new SymbolSearchException();
        }
    }

    public DefinitionEntry locateDefinition(PosSymbol qual, PosSymbol name, List<Type> argtypes) throws SymbolSearchException {
        if (qual == null) {
            return locateDefinition(name, argtypes);
        }
        QualifierLocator qlocator = new QualifierLocator(table, err);
        ModuleScope scope;
        try {
            scope = qlocator.locateMathModule(qual);
        } catch (SymbolSearchException sx1) {
            scope = qlocator.locateProgramModule(qual);
        }
        if (scope.containsDefinition(name.getSymbol())) {
            DefinitionEntry def = scope.getDefinition(name.getSymbol());
            checkDefinitionArguments(name, argtypes, def);
            return def;
        } else {
            if (showErrors) {
                String msg = cantFindDefInModMessage(name.toString(), qual.toString());
                err.error(qual.getLocation(), msg);
            }
            throw new SymbolSearchException();
        }
    }

    private List<DefinitionEntry> locateDefinitionsInStack(PosSymbol name) throws SymbolSearchException {
        List<DefinitionEntry> defs = new List<DefinitionEntry>();
        Stack<Scope> stack = table.getStack();
        Stack<Scope> hold = new Stack<Scope>();
        try {
            while (!stack.isEmpty()) {
                Scope scope = stack.pop();
                hold.push(scope);
                if (scope instanceof ProcedureScope) {
                    defs.addAll(locateDefinitionsInProc(name, (ProcedureScope) scope));
                    if (defs.size() > 0) {
                        break;
                    }
                } else if (scope instanceof ProofScope) {
                    defs.addAll(locateDefinitionsInProof(name, (ProofScope) scope));
                    if (defs.size() > 0) {
                        break;
                    }
                } else if (scope instanceof ModuleScope) {
                    ModuleScope mscope = (ModuleScope) scope;
                    if (mscope.containsDefinition(name.getSymbol())) {
                        defs.add(mscope.getDefinition(name.getSymbol()));
                    }
                } else {
                }
            }
            return defs;
        } finally {
            while (!hold.isEmpty()) {
                stack.push(hold.pop());
            }
        }
    }

    private List<DefinitionEntry> locateDefinitionsInProc(PosSymbol name, ProcedureScope scope) throws SymbolSearchException {
        List<DefinitionEntry> defs = new List<DefinitionEntry>();
        Iterator<ModuleScope> i = scope.getVisibleModules();
        while (i.hasNext()) {
            ModuleScope iscope = i.next();
            if (iscope.containsDefinition(name.getSymbol())) {
                defs.add(iscope.getDefinition(name.getSymbol()));
            }
        }
        return defs;
    }

    private List<DefinitionEntry> locateDefinitionsInProof(PosSymbol name, ProofScope scope) throws SymbolSearchException {
        List<DefinitionEntry> defs = new List<DefinitionEntry>();
        if (scope.containsDefinition(name.getSymbol())) {
            defs.add(scope.getDefinition(name.getSymbol()));
        }
        return defs;
    }

    private List<DefinitionEntry> locateDefinitionsInImports(PosSymbol name) throws SymbolSearchException {
        List<DefinitionEntry> defs = new List<DefinitionEntry>();
        Iterator<ModuleScope> i = table.getModuleScope().getMathVisibleModules();
        while (i.hasNext()) {
            ModuleScope iscope = i.next();
            if (iscope.containsDefinition(name.getSymbol())) {
                defs.add(iscope.getDefinition(name.getSymbol()));
            }
        }
        return defs;
    }

    private DefinitionEntry getUniqueDefinition(PosSymbol name, List<Type> argtypes, List<DefinitionEntry> defs) throws SymbolSearchException {
        if (defs.size() == 0) {
            if (showErrors) {
                String msg = cantFindDefMessage(name.toString());
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        } else if (defs.size() == 1) {
            checkDefinitionArguments(name, argtypes, defs.get(0));
            return defs.get(0);
        } else {
            return disambiguateDefinitions(name, argtypes, defs);
        }
    }

    private DefinitionEntry disambiguateDefinitions(PosSymbol name, List<Type> argtypes, List<DefinitionEntry> defs) throws SymbolSearchException {
        List<DefinitionEntry> newdefs = new List<DefinitionEntry>();
        Iterator<DefinitionEntry> i = defs.iterator();
        while (i.hasNext()) {
            DefinitionEntry def = i.next();
            if (argumentTypesMatch(def, argtypes)) {
                newdefs.add(def);
            }
        }
        if (newdefs.size() == 0) {
            List<Location> locs = getLocationList(defs);
            if (showErrors) {
                String sig = getSignatureString(defs.get(0).getName(), argtypes);
                String msg = cantFindDefMessage(sig, locs.toString());
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        } else if (newdefs.size() == 1) {
            return newdefs.get(0);
        } else {
            List<Location> locs = getLocationList(defs);
            if (showErrors) {
                String msg = ambigDefRefMessage(name.toString(), locs.toString());
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        }
    }

    private void checkDefinitionArguments(PosSymbol name, List<Type> argtypes, DefinitionEntry def) throws SymbolSearchException {
        if (!argumentTypesMatch(def, argtypes)) {
            if (showErrors) {
                String defsig = getSignatureString(def);
                String targsig = getSignatureString(def.getName(), argtypes);
                String msg = argTypeMismatchMessage(defsig, targsig);
                err.error(name.getLocation(), msg);
            }
            throw new SymbolSearchException();
        }
    }

    private boolean argumentTypesMatch(DefinitionEntry def, List<Type> argtypes) {
        List<Type> partypes = new List<Type>();
        Iterator<VarEntry> i = def.getParameters();
        while (i.hasNext()) {
            VarEntry var = i.next();
            partypes.add(var.getType());
        }
        if (argtypes.size() != partypes.size()) {
            return false;
        }
        Iterator<Type> j = argtypes.iterator();
        Iterator<Type> k = partypes.iterator();
        while (j.hasNext()) {
            Type argtype = j.next();
            Type partype = k.next();
            if (!tm.mathMatches(argtype, partype)) {
                return false;
            }
        }
        return true;
    }

    private String getSignatureString(DefinitionEntry def) {
        StringBuffer sb = new StringBuffer();
        sb.append(def.getName().toString());
        sb.append("(");
        Iterator<VarEntry> i = def.getParameters();
        while (i.hasNext()) {
            VarEntry entry = i.next();
            sb.append(entry.getType().getProgramName().toString());
            if (i.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private String getSignatureString(PosSymbol name, List<Type> argtypes) {
        StringBuffer sb = new StringBuffer();
        sb.append(name.toString());
        sb.append("(");
        Iterator<Type> i = argtypes.iterator();
        while (i.hasNext()) {
            Type type = i.next();
            sb.append(type.getProgramName().toString());
            if (i.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private List<Location> getLocationList(List<DefinitionEntry> entries) {
        List<Location> locs = new List<Location>();
        Iterator<DefinitionEntry> i = entries.iterator();
        while (i.hasNext()) {
            DefinitionEntry entry = i.next();
            locs.add(entry.getLocation());
        }
        return locs;
    }

    private String cantFindDefInModMessage(String name, String module) {
        return "Cannot find a definition named " + name + " in module " + module + ".";
    }

    private String ambigDefRefMessage(String name, String mods) {
        return "The definition named " + name + " is found in more than one " + "module visible from this scope: " + mods + ".";
    }

    private String cantFindDefMessage(String name) {
        return "Cannot find a definition named " + name + ".";
    }

    private String cantFindDefMessage(String sig, String mods) {
        return "Cannot find the definition with signature " + sig + ", but found definitions: " + mods + ".";
    }

    private String argTypeMismatchMessage(String opersig, String targsig) {
        return "Expected a definition with the signature " + targsig + " but found one with the signature " + opersig + ".";
    }
}
