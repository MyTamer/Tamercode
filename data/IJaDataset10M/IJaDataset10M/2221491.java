package dr.inference.model;

import dr.inference.parallel.MPIServices;
import dr.xml.*;
import java.util.*;

/**
 * @author Marc A. Suchard
 */
public class ParallelCompoundLikelihood extends CompoundLikelihood {

    public static final String PARALLEL_COMPOUND_LIKELIHOOD = "parallelCompoundLikelihood";

    public static final String LOCAL_CHECK = "doLocalCheck";

    public static final String RUN_PARALLEL = "doInParallel";

    public ParallelCompoundLikelihood(Collection<Likelihood> likelihoods, boolean doParallel, boolean checkLocal) {
        super(1, likelihoods);
        this.doParallel = doParallel;
        this.checkLocal = checkLocal;
    }

    private boolean doParallel = true;

    private boolean checkLocal = false;

    public double getLogLikelihood() {
        double logLikelihood = 0;
        if (doParallel) {
            logLikelihood = getLogLikelihoodRemote();
            if (checkLocal) {
                super.makeDirty();
                double logLikelihoodLocal = super.getLogLikelihood();
                System.err.printf("Local: %5.4f  Remote: %5.4f\n", logLikelihoodLocal, logLikelihood);
            }
        } else logLikelihood = super.getLogLikelihood();
        return logLikelihood;
    }

    private double getLogLikelihoodRemote() {
        double logLikelihood = 0.0;
        final int N = getLikelihoodCount();
        List<ParallelLikelihood> likelihoodsDistributed = new ArrayList<ParallelLikelihood>();
        List<Integer> processorList = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            ParallelLikelihood likelihood = (ParallelLikelihood) getLikelihood(i);
            if (!likelihood.getLikelihoodKnown()) {
                final int processor = i + 1;
                likelihoodsDistributed.add(likelihood);
                processorList.add(processor);
            } else {
                logLikelihood += likelihood.getLogLikelihood();
            }
        }
        final int size = likelihoodsDistributed.size();
        if (size == 1) {
            logLikelihood += likelihoodsDistributed.get(0).getLogLikelihood();
        } else if (size > 1) {
            int index = 0;
            for (ParallelLikelihood likelihood : likelihoodsDistributed) {
                int processor = processorList.get(index++);
                MPIServices.requestLikelihood(processor);
                ((AbstractModel) likelihood.getModel()).sendState(processor);
            }
            index = 0;
            for (ParallelLikelihood likelihood : likelihoodsDistributed) {
                int processor = processorList.get(index++);
                double l = MPIServices.receiveDouble(processor);
                logLikelihood += l;
                likelihood.setLikelihood(l);
            }
        }
        return logLikelihood;
    }

    public static XMLObjectParser PARSER = new AbstractXMLObjectParser() {

        public String getParserName() {
            return PARALLEL_COMPOUND_LIKELIHOOD;
        }

        public Object parseXMLObject(XMLObject xo) throws XMLParseException {
            boolean doParallel = true;
            boolean checkLocal = false;
            if (xo.hasAttribute(LOCAL_CHECK)) {
                checkLocal = xo.getBooleanAttribute(LOCAL_CHECK);
            }
            if (xo.hasAttribute(RUN_PARALLEL)) {
                doParallel = xo.getBooleanAttribute(RUN_PARALLEL);
            }
            List<Likelihood> likelihoods = new ArrayList<Likelihood>();
            for (int i = 0; i < xo.getChildCount(); i++) {
                if (xo.getChild(i) instanceof Likelihood) {
                    likelihoods.add((Likelihood) xo.getChild(i));
                } else {
                    throw new XMLParseException("An element which is not a likelihood has been added to a " + PARALLEL_COMPOUND_LIKELIHOOD + " element");
                }
            }
            ParallelCompoundLikelihood compoundLikelihood = new ParallelCompoundLikelihood(likelihoods, doParallel, checkLocal);
            return compoundLikelihood;
        }

        public String getParserDescription() {
            return "A likelihood function which is simply the product of its component likelihood functions.";
        }

        public XMLSyntaxRule[] getSyntaxRules() {
            return rules;
        }

        private XMLSyntaxRule[] rules = new XMLSyntaxRule[] { new ElementRule(Likelihood.class, 1, Integer.MAX_VALUE) };

        public Class getReturnType() {
            return ParallelCompoundLikelihood.class;
        }
    };
}
