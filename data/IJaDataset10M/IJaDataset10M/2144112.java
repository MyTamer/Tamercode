package jugglinglab.jml;

import java.util.*;
import java.io.*;
import jugglinglab.util.*;

public class JMLSymmetry {

    static ResourceBundle errorstrings;

    static {
        errorstrings = JLLocale.getBundle("ErrorStrings");
    }

    int type;

    int numjugglers;

    int numpaths;

    Permutation jugglerperm = null;

    Permutation pathperm = null;

    double delay = -1.0;

    public static final int TYPE_DELAY = 1;

    public static final int TYPE_SWITCH = 2;

    public static final int TYPE_SWITCHDELAY = 3;

    public JMLSymmetry() {
    }

    public JMLSymmetry(int type, int numjugglers, String jugperm, int numpaths, String pathperm, double delay) throws JuggleExceptionUser {
        setType(type);
        setJugglerPerm(numjugglers, jugperm);
        setPathPerm(numpaths, pathperm);
        setDelay(delay);
    }

    public int getType() {
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }

    public int getNumberOfJugglers() {
        return numjugglers;
    }

    public Permutation getJugglerPerm() {
        return jugglerperm;
    }

    protected void setJugglerPerm(int nj, String jp) throws JuggleExceptionUser {
        this.numjugglers = nj;
        try {
            if (jp == null) this.jugglerperm = new Permutation(numjugglers, true); else this.jugglerperm = new Permutation(numjugglers, jp, true);
        } catch (JuggleException je) {
            throw new JuggleExceptionUser(je.getMessage());
        }
    }

    public int getNumberOfPaths() {
        return numpaths;
    }

    public Permutation getPathPerm() {
        return pathperm;
    }

    protected void setPathPerm(int np, String pp) throws JuggleExceptionUser {
        this.numpaths = np;
        try {
            if (pp == null) this.pathperm = new Permutation(numpaths, false); else this.pathperm = new Permutation(numpaths, pp, false);
        } catch (JuggleException je) {
            throw new JuggleExceptionUser(je.getMessage());
        }
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double del) {
        this.delay = del;
    }

    public void readJML(JMLNode current, int numjug, int numpat, String version) throws JuggleExceptionUser {
        JMLAttributes at = current.getAttributes();
        String symtype, pathperm, jugglerperm, delaystring;
        int symtypenum;
        double delay = -1.0;
        symtype = at.getAttribute("type");
        jugglerperm = at.getAttribute("jperm");
        pathperm = at.getAttribute("pperm");
        delaystring = at.getAttribute("delay");
        if (delaystring != null) {
            try {
                delay = Double.valueOf(delaystring).doubleValue();
            } catch (NumberFormatException nfe) {
                throw new JuggleExceptionUser(errorstrings.getString("Error_symmetry_format"));
            }
        }
        if (symtype == null) throw new JuggleExceptionUser(errorstrings.getString("Error_symmetry_notype"));
        if (symtype.equalsIgnoreCase("delay")) symtypenum = TYPE_DELAY; else if (symtype.equalsIgnoreCase("switch")) symtypenum = TYPE_SWITCH; else if (symtype.equalsIgnoreCase("switchdelay")) symtypenum = TYPE_SWITCHDELAY; else throw new JuggleExceptionUser(errorstrings.getString("Error_symmetry_type"));
        setType(symtypenum);
        setJugglerPerm(numjug, jugglerperm);
        setPathPerm(numpat, pathperm);
        setDelay(delay);
    }

    public void writeJML(PrintWriter wr) throws IOException {
        String out = "<symmetry type=\"";
        switch(getType()) {
            case TYPE_DELAY:
                out += "delay\" pperm=\"" + pathperm.toString(true) + "\" delay=\"" + JMLPattern.toStringTruncated(getDelay(), 4) + "\"/>";
                break;
            case TYPE_SWITCH:
                out += "switch\" jperm=\"" + jugglerperm.toString(true) + "\" pperm=\"" + pathperm.toString(true) + "\"/>";
                break;
            case TYPE_SWITCHDELAY:
                out += "switchdelay\" jperm=\"" + jugglerperm.toString(true) + "\" pperm=\"" + pathperm.toString(true) + "\"/>";
                break;
        }
        wr.println(out);
    }
}
