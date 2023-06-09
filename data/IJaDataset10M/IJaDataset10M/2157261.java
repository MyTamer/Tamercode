package org.jikesrvm.compilers.opt.ssa;

import java.util.Enumeration;
import org.jikesrvm.compilers.opt.ir.Binary;
import org.jikesrvm.compilers.opt.ir.IR;
import org.jikesrvm.compilers.opt.ir.Instruction;
import org.jikesrvm.compilers.opt.ir.operand.Operand;
import static org.jikesrvm.compilers.opt.ir.Operators.INT_ADD;
import static org.jikesrvm.compilers.opt.ir.Operators.INT_SUB;
import static org.jikesrvm.compilers.opt.ir.Operators.LONG_ADD;
import static org.jikesrvm.compilers.opt.ir.Operators.LONG_SUB;
import static org.jikesrvm.compilers.opt.ir.Operators.REF_ADD;
import static org.jikesrvm.compilers.opt.ir.Operators.REF_SUB;

/**
 * This class implements index equivalence via global value numbering
 * and 'uniformly generated expressions'.  See EURO-PAR 01 paper for
 * more details.
 */
class UniformlyGeneratedGVN {

    static final boolean DEBUG = false;

    /**
   * Compute Index Equivalence with uniformly generated global value
   * numbers.
   *
   * <p> PRECONDITIONS: SSA form, register lists computed, SSA bit
   * computed.
   *
   * <p> POSTCONDITION: ir.HIRInfo.uniformlyGeneratedValueNumbers
   * holds results of the analysis. Does not modify the IR in any other way.
   *
   * @param ir the governing IR
   */
    public static void perform(IR ir) {
        GlobalValueNumberState gvn = null;
        gvn = new GlobalValueNumberState(ir);
        for (Enumeration<Instruction> e = ir.forwardInstrEnumerator(); e.hasMoreElements(); ) {
            Instruction s = e.nextElement();
            if (s.operator == INT_ADD || s.operator == LONG_ADD || s.operator == REF_ADD || s.operator == REF_SUB || s.operator == INT_SUB || s.operator == LONG_SUB) {
                Operand val2 = Binary.getVal2(s);
                if (val2.isConstant()) {
                    Operand lhs = Binary.getResult(s);
                    Operand rhs = Binary.getVal1(s);
                    gvn.mergeClasses(gvn.valueGraph.getVertex(lhs), gvn.valueGraph.getVertex(rhs));
                }
            }
        }
        if (DEBUG) {
            System.out.println("@@@@ START OF INDEX EQUIVALENCE VALUE NUMBERS FOR " + ir.method + " @@@@");
            gvn.printValueNumbers();
            System.out.println("@@@@ END OF INDEX EQUIVALENCE VALUE NUMBERS FOR " + ir.method + " @@@@");
        }
        ir.HIRInfo.uniformlyGeneratedValueNumbers = gvn;
    }
}
