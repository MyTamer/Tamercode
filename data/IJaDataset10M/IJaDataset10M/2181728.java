package com.google.gwt.junit;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.cfg.ConfigurationProperty;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.ModuleDefLoader;
import com.google.gwt.dev.util.collect.HashSet;
import com.google.gwt.junit.JUnitShell.Strategy;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.junit.client.impl.GWTRunner;
import com.google.gwt.junit.client.impl.JUnitHost.TestInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * An interface that specifies how modules should be compiled.
 */
public abstract class CompileStrategy {

    /**
   * The list of modules that have already been compiled. We use this to avoid
   * adding test batches that have already been added.
   */
    private Set<String> compiledModuleNames = new HashSet<String>();

    private final JUnitShell junitShell;

    /**
   * Construct a CompileStrategy.
   *
   * @param junitShell
   */
    public CompileStrategy(JUnitShell junitShell) {
        this.junitShell = junitShell;
    }

    /**
   * Maybe add a test block for the currently executed test case.
   *
   * @param testCase the test case being run
   * @param batchingStrategy the batching strategy
   */
    public void maybeAddTestBlockForCurrentTest(GWTTestCase testCase, BatchingStrategy batchingStrategy) {
        if (batchingStrategy.isSingleTestOnly()) {
            TestInfo testInfo = new TestInfo(testCase.getSyntheticModuleName(), testCase.getClass().getName(), testCase.getName());
            List<TestInfo[]> testBlocks = new ArrayList<TestInfo[]>(1);
            testBlocks.add(new TestInfo[] { testInfo });
            getMessageQueue().addTestBlocks(testBlocks, false);
        }
    }

    /**
   * Let the compile strategy compile another module. This is called while
   * {@link JUnitShell} is waiting for the current test to complete.
   *
   * @throws UnableToCompleteException if the compilation fails
   */
    public void maybeCompileAhead() throws UnableToCompleteException {
    }

    /**
   * Compile a single module using a synthetic module that adds JUnit support.
   *
   * @param moduleName the module name
   * @param syntheticModuleName the synthetic module name
   * @param strategy the strategy
   * @param batchingStrategy the batching strategy
   * @param treeLogger the logger
   * @return the {@link ModuleDef} describing the synthetic module
   * @throws UnableToCompleteException
   */
    public abstract ModuleDef maybeCompileModule(String moduleName, String syntheticModuleName, Strategy strategy, BatchingStrategy batchingStrategy, TreeLogger treeLogger) throws UnableToCompleteException;

    /**
   * Compile a single module using a synthetic module that adds JUnit support.
   *
   * @param moduleName the module name
   * @param syntheticModuleName the synthetic module name
   * @param strategy the strategy
   * @param batchingStrategy the batching strategy
   * @param treeLogger the logger
   * @return the {@link ModuleDef} describing the synthetic module
   */
    protected ModuleDef maybeCompileModuleImpl(String moduleName, String syntheticModuleName, Strategy strategy, BatchingStrategy batchingStrategy, TreeLogger treeLogger) throws UnableToCompleteException {
        ModuleDef moduleDef = maybeCompileModuleImpl2(moduleName, syntheticModuleName, strategy, treeLogger);
        if (!compiledModuleNames.contains(syntheticModuleName)) {
            compiledModuleNames.add(syntheticModuleName);
            if (!batchingStrategy.isSingleTestOnly()) {
                boolean isFinalModule = compiledModuleNames.size() >= getModuleCount();
                List<TestInfo[]> testBlocks = batchingStrategy.getTestBlocks(syntheticModuleName);
                getMessageQueue().addTestBlocks(testBlocks, isFinalModule);
            }
        }
        return moduleDef;
    }

    /**
   * Visible for testing and mocking.
   *
   * @return the {@link JUnitMessageQueue}
   */
    JUnitMessageQueue getMessageQueue() {
        return JUnitShell.getMessageQueue();
    }

    /**
   * Visible for testing and mocking.
   *
   * @return the number of modules to test
   */
    int getModuleCount() {
        return GWTTestCase.getModuleCount();
    }

    /**
   * Compile the module if needed.
   *
   * Visible for testing and mocking.
   *
   * @param moduleName the module name
   * @param syntheticModuleName the synthetic module name
   * @param strategy the strategy
   * @param treeLogger the logger
   * @return the {@link ModuleDef} describing the synthetic module
   */
    ModuleDef maybeCompileModuleImpl2(String moduleName, String syntheticModuleName, Strategy strategy, TreeLogger treeLogger) throws UnableToCompleteException {
        ModuleDef moduleDef = ModuleDefLoader.createSyntheticModule(treeLogger, syntheticModuleName, new String[] { moduleName, strategy.getModuleInherit() }, false);
        moduleDef.clearEntryPoints();
        moduleDef.addEntryPointTypeName(GWTRunner.class.getName());
        ConfigurationProperty moduleNameProp = moduleDef.getProperties().createConfiguration("junit.moduleName", false);
        moduleNameProp.setValue(syntheticModuleName);
        strategy.processModule(moduleDef);
        junitShell.maybeCompileForWebMode(moduleDef, JUnitShell.getRemoteUserAgents());
        return moduleDef;
    }
}
