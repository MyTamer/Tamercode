package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;

public class MathTypeDec extends Dec {

    /** The name member. */
    private PosSymbol name;

    /** The ty member. */
    private Ty ty;

    public MathTypeDec() {
    }

    ;

    public MathTypeDec(PosSymbol name, Ty ty) {
        this.name = name;
        this.ty = ty;
    }

    /** Returns the value of the name variable. */
    public PosSymbol getName() {
        return name;
    }

    /** Returns the value of the ty variable. */
    public Ty getTy() {
        return ty;
    }

    /** Sets the name variable to the specified value. */
    public void setName(PosSymbol name) {
        this.name = name;
    }

    /** Sets the ty variable to the specified value. */
    public void setTy(Ty ty) {
        this.ty = ty;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitMathTypeDec(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("MathTypeDec\n");
        if (name != null) {
            sb.append(name.asString(indent + increment, increment));
        }
        if (ty != null) {
            sb.append(ty.asString(indent + increment, increment));
        }
        return sb.toString();
    }
}
