package org.jikesrvm.adaptive;

import org.jikesrvm.VM;
import org.jikesrvm.VM_CompiledMethod;
import org.jikesrvm.classloader.VM_NormalMethod;

/**
 * This class encapsulates the analytic model used by the controller
 * to guide multi-level recompilation decisions.  An early version of
 * this model is described in the OOPSLA'2000 paper, but we've made
 * some improvements since then...
 *
 * @see VM_MultiLevelAdaptiveModel
 *
 * @author Mike Hind
 * @author Dave Grove
 * @author Peter Sweeney
 * @author Stephen Fink
 * @author Matthew Arnold 
 */
abstract class VM_AnalyticModel extends VM_RecompilationStrategy {

    /**
   * Initialize the set of "optimization choices" that the
   * cost-benefit model will consider when using will consider when
   * using adaptive compilation.
   */
    abstract void populateRecompilationChoices();

    /**
   * Compute the set of optimization choices that should be
   * considered by the cost-benefit model, given the previous compiler.  
   *
   * @param prevCompiler The compiler compiler that was used to 
   *                     comile cmpMethod
   * @param cmpMethod The compiled method being considered
   */
    abstract VM_RecompilationChoice[] getViableRecompilationChoices(int prevCompiler, VM_CompiledMethod cmpMethod);

    /**
   * Initialize the analytic model:
   *
   *  NOTE: The call to super.init() uses the command line options to
   *  set up the optimization plans, so this must be run after the
   *  command line options are available.  
   */
    void init() {
        super.init();
        populateRecompilationChoices();
    }

    /**
   * This method is the main decision making loop for all
   * recompilation strategies that use the analytic model.  
   * <p>
   * Given a HotMethodRecompilationEvent, this code will determine 
   * IF the method should be recompiled, and if so, HOW to perform 
   * the recompilation, i.e., what compilation plan should be used.
   * The method returns a controller plan, which contains the compilation
   * plan and other goodies.
   *
   * @param cmpMethod the compiled method of interest
   * @param hme       the VM_HotMethodRecompilationEvent
   * @return the controller plan to be used or NULL, if no 
   *                   compilation is to be performed.  */
    VM_ControllerPlan considerHotMethod(VM_CompiledMethod cmpMethod, VM_HotMethodEvent hme) {
        int prevCompiler = getPreviousCompiler(cmpMethod);
        if (prevCompiler == -1) {
            return null;
        }
        VM_ControllerPlan plan = VM_ControllerMemory.findMatchingPlan(cmpMethod);
        if (considerOSRRecompilation(cmpMethod, hme, plan)) return null;
        if (!considerForRecompilation(hme, plan)) return null;
        double futureTimeForMethod = futureTimeForMethod(hme);
        VM_RecompilationChoice bestActionChoice = null;
        double bestActionTime = futureTimeForMethod;
        double bestCost = 0.0;
        VM_AOSLogging.recordControllerEstimateCostDoNothing(cmpMethod.getMethod(), VM_CompilerDNA.getOptLevel(prevCompiler), bestActionTime);
        VM_RecompilationChoice[] recompilationChoices = getViableRecompilationChoices(prevCompiler, cmpMethod);
        VM_NormalMethod meth = (VM_NormalMethod) hme.getMethod();
        for (VM_RecompilationChoice choice : recompilationChoices) {
            double cost = choice.getCost(meth);
            double futureExecutionTime = choice.getFutureExecutionTime(prevCompiler, futureTimeForMethod);
            double curActionTime = cost + futureExecutionTime;
            VM_AOSLogging.recordControllerEstimateCostOpt(cmpMethod.getMethod(), choice.toString(), cost, curActionTime);
            if (curActionTime < bestActionTime) {
                bestActionTime = curActionTime;
                bestActionChoice = choice;
                bestCost = cost;
            }
        }
        if (bestActionChoice == null) {
            plan = null;
        } else {
            plan = bestActionChoice.makeControllerPlan(cmpMethod, prevCompiler, futureTimeForMethod, bestActionTime, bestCost);
        }
        return plan;
    }

    boolean considerOSRRecompilation(VM_CompiledMethod cmpMethod, VM_HotMethodEvent hme, VM_ControllerPlan plan) {
        boolean outdatedBaseline = false;
        if (plan == null) {
            outdatedBaseline = VM_ControllerMemory.planWithStatus(cmpMethod.getMethod(), VM_ControllerPlan.COMPLETED) && cmpMethod.getCompilerType() == VM_CompiledMethod.BASELINE;
            if (outdatedBaseline) VM_AOSLogging.debug("outdated Baseline " + cmpMethod.getMethod() + "(" + cmpMethod.getId() + ")");
        }
        if (outdatedBaseline) {
            if (!hme.getCompiledMethod().getSamplesReset()) {
                hme.getCompiledMethod().setSamplesReset();
                VM_Controller.methodSamples.reset(hme.getCMID());
                VM_AOSLogging.debug(" Resetting method samples " + hme);
                return true;
            } else {
                plan = chooseOSRRecompilation(hme);
                if (plan != null) {
                    VM_ControllerMemory.insert(plan);
                    if (VM.VerifyAssertions) {
                        VM._assert(cmpMethod.getCompilerType() == VM_CompiledMethod.BASELINE);
                    }
                    cmpMethod.setOutdated();
                }
                return true;
            }
        }
        return false;
    }

    /**
   * @param hme sample data for an outdated cmid 
   * @return a plan representing recompilation with OSR, null if OSR not
   * justified.
   */
    private VM_ControllerPlan chooseOSRRecompilation(VM_HotMethodEvent hme) {
        if (!VM_Controller.options.OSR_PROMOTION) return null;
        VM_AOSLogging.debug(" Consider OSR for " + hme);
        VM_ControllerPlan prev = VM_ControllerMemory.findLatestPlan(hme.getMethod());
        if (prev.getStatus() == VM_ControllerPlan.OSR_BASE_2_OPT) {
            VM_AOSLogging.debug(" Already have an OSR promotion plan for this method");
            return null;
        }
        double millis = (double) (prev.getTimeCompleted() - prev.getTimeInitiated());
        double speedup = prev.getExpectedSpeedup();
        double futureTimeForMethod = futureTimeForMethod(hme);
        double futureTimeOptimized = futureTimeForMethod / speedup;
        VM_AOSLogging.debug(" Estimated future time for method " + hme + " is " + futureTimeForMethod);
        VM_AOSLogging.debug(" Estimated future time optimized " + hme + " is " + (futureTimeOptimized + millis));
        if (futureTimeForMethod > futureTimeOptimized + millis) {
            VM_AOSLogging.recordOSRRecompilationDecision(prev);
            VM_ControllerPlan p = new VM_ControllerPlan(prev.getCompPlan(), prev.getTimeCreated(), hme.getCMID(), prev.getExpectedSpeedup(), millis, prev.getPriority());
            p.setStatus(VM_ControllerPlan.OSR_BASE_2_OPT);
            return p;
        } else {
            return null;
        }
    }

    /**
   * This function defines how the analytic model handles a
   * VM_AINewHotEdgeEvent.  The basic idea is to use the model to
   * evaluate whether it would be better to do nothing or to recompile
   * at the same opt level, assuming there would be some "boost" after
   * performing inlining.  
   */
    void considerHotCallEdge(VM_CompiledMethod cmpMethod, VM_AINewHotEdgeEvent event) {
        int prevCompiler = getPreviousCompiler(cmpMethod);
        if (prevCompiler == -1) {
            return;
        }
        VM_ControllerPlan plan = VM_ControllerMemory.findMatchingPlan(cmpMethod);
        if (!considerForRecompilation(event, plan)) return;
        double prevCompileTime = cmpMethod.getCompilationTime();
        double futureTimeForMethod = futureTimeForMethod(event);
        double futureTimeForFDOMethod = prevCompileTime + (futureTimeForMethod / event.getBoostFactor());
        int prevOptLevel = VM_CompilerDNA.getOptLevel(prevCompiler);
        VM_AOSLogging.recordControllerEstimateCostDoNothing(cmpMethod.getMethod(), prevOptLevel, futureTimeForMethod);
        VM_AOSLogging.recordControllerEstimateCostOpt(cmpMethod.getMethod(), "O" + prevOptLevel + "AI", prevCompileTime, futureTimeForFDOMethod);
        if (futureTimeForFDOMethod < futureTimeForMethod) {
            int optLevel = VM_CompilerDNA.getOptLevel(prevCompiler);
            double priority = futureTimeForMethod - futureTimeForFDOMethod;
            plan = createControllerPlan(cmpMethod.getMethod(), optLevel, null, cmpMethod.getId(), event.getBoostFactor(), futureTimeForFDOMethod, priority);
            plan.execute();
        }
    }

    /**
   * How much time do we expect to spend in the method in the future if
   * we take no recompilation action?
   * The key assumption is that we'll spend just as much time 
   * executing in the the method in the future as we have done so far
   * in the past.
   * 
   * @param hme The VM_HotMethodEvent in question
   * @return estimate of future execution time to be spent in this method
   */
    double futureTimeForMethod(VM_HotMethodEvent hme) {
        double numSamples = hme.getNumSamples();
        double timePerSample = (double) VM.interruptQuantum;
        if (!VM.UseEpilogueYieldPoints) {
            timePerSample /= 2.0;
        }
        if (VM_Controller.options.mlCBS()) {
            timePerSample /= (double) VM.CBSMethodSamplesPerTick;
        }
        double timeInMethodSoFar = numSamples * timePerSample;
        return timeInMethodSoFar;
    }
}
