package edu.clemson.cs.r2jt.proving;

import java.util.LinkedList;
import java.util.List;
import edu.clemson.cs.r2jt.analysis.MathExpTypeResolver;
import edu.clemson.cs.r2jt.proving.absyn.PExp;
import edu.clemson.cs.r2jt.proving.absyn.PSymbol;

/**
 * <p>The base class for <code>RuleNormalizer</code>s that operate only on those
 * rules of the form <code>x = y</code>.
 * 
 * <p>Because other sorts of rules are generally mistakes as of this writing,
 * this class can be put into <em>noisy mode</em>, in which it will print
 * a warning when it filters out a rule.</p>
 */
public abstract class AbstractEqualityRuleNormalizer implements RuleNormalizer {

    private static final List<VCTransformer> DUMMY_SET = new LinkedList<VCTransformer>();

    protected final MathExpTypeResolver myTyper;

    private final boolean myNoisyFlag;

    public AbstractEqualityRuleNormalizer(MathExpTypeResolver r, boolean noisy) {
        myTyper = r;
        myNoisyFlag = noisy;
    }

    @Override
    public final Iterable<VCTransformer> normalize(PExp e) {
        List<VCTransformer> retval = DUMMY_SET;
        if (e instanceof PSymbol) {
            PSymbol sE = (PSymbol) e;
            if (sE.isEquality()) {
                retval = doNormalize(sE.arguments.get(0), sE.arguments.get(1));
            }
        }
        if (myNoisyFlag && retval == DUMMY_SET) {
            System.out.println("WARNING: " + this.getClass() + " is " + "filtering out this non-equality rule: \n" + e.toString() + "(" + e.getClass() + ")");
        }
        return retval;
    }

    protected abstract List<VCTransformer> doNormalize(PExp left, PExp right);

    public final Iterable<VCTransformer> normalizeAll(Iterable<PExp> es) {
        return normalizeAll(this, es);
    }

    public static final Iterable<VCTransformer> normalizeAll(RuleNormalizer n, Iterable<PExp> es) {
        ChainingIterable<VCTransformer> transformers = new ChainingIterable<VCTransformer>();
        for (PExp e : es) {
            transformers.add(n.normalize(e));
        }
        return transformers;
    }
}
