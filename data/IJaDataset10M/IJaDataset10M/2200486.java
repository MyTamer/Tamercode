package br.com.sysmap.crux.tools.compile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import br.com.sysmap.crux.core.rebind.scanner.module.Module;
import br.com.sysmap.crux.core.rebind.scanner.screen.ScreenConfigException;
import br.com.sysmap.crux.core.server.classpath.ClassPathResolver;
import br.com.sysmap.crux.core.server.classpath.ClassPathResolverImpl;
import br.com.sysmap.crux.core.server.classpath.ClassPathResolverInitializer;
import br.com.sysmap.crux.core.utils.FileUtils;
import br.com.sysmap.crux.core.utils.RegexpPatterns;
import br.com.sysmap.crux.module.CruxModule;
import br.com.sysmap.crux.module.CruxModuleBridge;
import br.com.sysmap.crux.module.CruxModuleHandler;
import br.com.sysmap.crux.module.classpath.ModuleClassPathResolver;
import br.com.sysmap.crux.tools.compile.utils.ClassPathUtils;
import br.com.sysmap.crux.tools.parameters.ConsoleParameter;
import br.com.sysmap.crux.tools.parameters.ConsoleParametersProcessor;

/**
 * Compiler to support the Module Container Application layout.
 * @author Gesse S. F. Dafe - <code>gessedafe@gmail.com</code>
 */
public class CruxModuleContainerApplicationCompiler extends CruxModuleCompiler {

    private String keepPagesUnder;

    private List<File> cruxPagesDir;

    private boolean monolithicCompilerInitialized = false;

    private MonolithicApplicationCompiler monolithicAppCompiler;

    private ClassPathResolver monolithicClassPathResolver;

    private ClassPathResolver moduleClassPathResolver;

    @Override
    protected void doCompileModule(URL url, Module module) throws Exception {
        CruxModule cruxModule = CruxModuleHandler.getCruxModule(module.getName());
        if (cruxModule != null) {
            ClassPathResolverInitializer.registerClassPathResolver(this.moduleClassPathResolver);
            super.doCompileModule(url, module);
        } else {
            ClassPathResolverInitializer.registerClassPathResolver(this.monolithicClassPathResolver);
            nonModuleCompile(url, module);
        }
    }

    /**
	 * Compiles the non-modular part of the application.
	 * @param url
	 * @param module
	 * @throws Exception 
	 */
    private void nonModuleCompile(URL url, Module module) throws Exception {
        if (!this.monolithicCompilerInitialized) {
            initializeMonolithicCompiler();
        }
        this.monolithicAppCompiler.doCompileModule(url, module);
        setModuleAsCompiled(module);
    }

    /**
	 * 
	 */
    private void initializeMonolithicCompiler() {
        this.monolithicAppCompiler = new MonolithicApplicationCompiler();
        this.monolithicAppCompiler.setIndentPages(this.isIndentPages());
        this.monolithicAppCompiler.setKeepPagesGeneratedFiles(this.isKeepPagesGeneratedFiles());
        this.monolithicAppCompiler.setKeepPagesUnder(this.keepPagesUnder);
        this.monolithicAppCompiler.setOutputCharset(this.getOutputCharset());
        this.monolithicAppCompiler.setOutputDir(this.getOutputDir());
        this.monolithicAppCompiler.setPageFileExtension(this.getPageFileExtension());
        this.monolithicAppCompiler.setPagesOutputDir(this.getPagesOutputDir());
        this.monolithicAppCompiler.setPreCompileJavaSource(this.isPreCompileJavaSource());
        this.monolithicAppCompiler.setWebDir(this.getWebDir());
        this.monolithicCompilerInitialized = true;
    }

    @Override
    protected List<URL> getURLs() throws Exception {
        List<URL> result = new ArrayList<URL>();
        Set<URL> urls = new HashSet<URL>();
        ClassPathResolverInitializer.registerClassPathResolver(this.moduleClassPathResolver = new ModuleClassPathResolver());
        urls.addAll(getAllModulesURLs());
        ClassPathResolverInitializer.registerClassPathResolver(this.monolithicClassPathResolver = new ClassPathResolverImpl());
        for (File dir : cruxPagesDir) {
            urls.addAll(MonolithicAppCompileUtils.getURLs(dir));
        }
        result.addAll(urls);
        return result;
    }

    /**
	 * @return
	 * @throws ScreenConfigException 
	 */
    private Collection<URL> getAllModulesURLs() throws ScreenConfigException {
        List<URL> urls = new ArrayList<URL>();
        Iterator<CruxModule> cruxModules = CruxModuleHandler.iterateCruxModules();
        while (cruxModules.hasNext()) {
            CruxModuleBridge.getInstance().registerCurrentModule(cruxModules.next().getName());
            urls.addAll(getURLsForRegisteredModule());
        }
        return urls;
    }

    @Override
    protected void processParameters(Collection<ConsoleParameter> parameters) {
        super.processParameters(parameters);
        for (ConsoleParameter parameter : parameters) {
            if (parameter.getName().equals("keepPagesUnder")) {
                this.keepPagesUnder = parameter.getValue();
            } else if (parameter.getName().equals("cruxPagesDir")) {
                this.cruxPagesDir = new ArrayList<File>();
                String[] pageDirs = RegexpPatterns.REGEXP_COMMA.split(parameter.getValue());
                for (String dir : pageDirs) {
                    this.cruxPagesDir.add(new File(dir.trim()));
                }
            }
        }
    }

    @Override
    protected ConsoleParametersProcessor createParametersProcessor() {
        ConsoleParametersProcessor parametersProcessor = super.createParametersProcessor();
        MonolithicAppCompileUtils.addParametersToProcessor(parametersProcessor);
        return parametersProcessor;
    }

    @Override
    protected void initializeCompilerDir() throws IOException, MalformedURLException {
        compilerWorkDir = new File(FileUtils.getTempDirFile(), "crux_compiler" + System.currentTimeMillis());
        compilerWorkDir.mkdirs();
        ClassPathUtils.addURL(compilerWorkDir.toURI().toURL());
    }
}
