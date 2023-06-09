package org.argouml.application;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.security.ArgoAwtExceptionHandler;
import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ui.ToDoPerspective;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.ui.NavPerspective;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.argouml.uml.cognitive.critics.ChildGenUML;
import org.argouml.util.Trash;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.util.ResourceLoader;
import org.tigris.gef.util.Util;
import ru.novosoft.uml.MFactoryImpl;

public class Main {

    private static Vector postLoadActions = new Vector();

    public static void main(String args[]) {
        Configuration.load();
        String directory = Argo.getDirectory();
        org.tigris.gef.base.Globals.setLastDirectory(directory);
        Locale.setDefault(new Locale(System.getProperty("user.language", "de"), System.getProperty("user.country", System.getProperty("user.region", "DE"))));
        org.argouml.util.Tools.logVersionInfo();
        boolean doSplash = Configuration.getBoolean(Argo.KEY_SPLASH, true);
        boolean useEDEM = Configuration.getBoolean(Argo.KEY_EDEM, true);
        boolean preload = Configuration.getBoolean(Argo.KEY_PRELOAD, true);
        boolean profileLoad = Configuration.getBoolean(Argo.KEY_PROFILE, false);
        boolean reloadRecent = Configuration.getBoolean(Argo.KEY_RELOAD_RECENT_PROJECT, false);
        File projectFile = null;
        Project p = null;
        String projectName = null;
        URL urlToOpen = null;
        SimpleTimer st = new SimpleTimer("Main.main");
        st.mark("arguments");
        System.setProperty("gef.imageLocation", "/org/argouml/Images");
        System.setProperty("com.apple.macos.useScreenMenuBar", "false");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ArgoUML");
        int themeMemory = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if ((themeMemory = ProjectBrowser.getThemeFromArg(args[i])) != 0) {
                } else if (args[i].equalsIgnoreCase("-help") || args[i].equalsIgnoreCase("-h")) {
                    System.err.println("Usage: [options] [project-file]");
                    System.err.println("Options include: ");
                    ProjectBrowser.printThemeArgs();
                    System.err.println("  -nosplash       dont display Argo/UML logo");
                    System.err.println("  -noedem         dont report usage statistics");
                    System.err.println("  -nopreload      dont preload common classes");
                    System.err.println("  -profileload    report on load times");
                    System.err.println("  -norecentfile   do not reload last saved file");
                    System.err.println("");
                    System.err.println("You can also set java settings which influence the behaviour of ArgoUML:");
                    System.err.println("  -Duser.language e.g. en");
                    System.err.println("  -Duser.region   e.g. US");
                    System.exit(0);
                } else if (args[i].equalsIgnoreCase("-nosplash")) {
                    doSplash = false;
                } else if (args[i].equalsIgnoreCase("-noedem")) {
                    useEDEM = false;
                } else if (args[i].equalsIgnoreCase("-nopreload")) {
                    preload = false;
                } else if (args[i].equalsIgnoreCase("-profileload")) {
                    profileLoad = true;
                } else if (args[i].equalsIgnoreCase("-norecentfile")) {
                    reloadRecent = false;
                } else {
                    System.err.println("Ingoring unknown option '" + args[i] + "'");
                }
            } else {
                if (projectName == null) {
                    System.out.println("Setting projectName to '" + args[i] + "'");
                    projectName = args[i];
                }
            }
        }
        if (reloadRecent && projectName == null) {
            String s = Configuration.getString(Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
            if (s != "") {
                File file = new File(s);
                if (file.exists()) {
                    Argo.log.info("Re-opening project " + s);
                    projectName = s;
                } else {
                    Argo.log.warn("Cannot re-open " + s + " because it does not exist");
                }
            }
        }
        if (projectName != null) {
            if (!projectName.endsWith(Project.COMPRESSED_FILE_EXT)) projectName += Project.COMPRESSED_FILE_EXT;
            projectFile = new File(projectName);
            if (!projectFile.exists()) {
                System.err.println("Project file '" + projectFile + "' does not exist.");
                p = null;
            } else {
                try {
                    urlToOpen = Util.fileToURL(projectFile);
                } catch (Exception e) {
                    Argo.log.error("Exception opening project in main()", e);
                }
            }
        }
        st.mark("locales");
        ResourceLoader.addResourceExtension("gif");
        ResourceLoader.addResourceLocation("/org/argouml/Images");
        ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
        Translator.init();
        st.mark("splash");
        SplashScreen splash = new SplashScreen("Loading ArgoUML...", "Splash");
        splash.getStatusBar().showStatus("Making Project Browser");
        splash.getStatusBar().showProgress(10);
        splash.setVisible(doSplash);
        st.mark("projectbrowser");
        Object dgd = org.argouml.uml.generator.GeneratorDisplay.getInstance();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        ProjectBrowser pb = new ProjectBrowser("ArgoUML", splash.getStatusBar(), themeMemory);
        JOptionPane.setRootFrame(pb);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int w = Math.min(Configuration.getInteger(Argo.KEY_SCREEN_WIDTH, (int) (0.95 * scrSize.width)), scrSize.width);
        int h = Math.min(Configuration.getInteger(Argo.KEY_SCREEN_HEIGHT, (int) (0.95 * scrSize.height)), scrSize.height);
        int x = Configuration.getInteger(Argo.KEY_SCREEN_LEFT_X, 0);
        int y = Configuration.getInteger(Argo.KEY_SCREEN_TOP_Y, 0);
        pb.setLocation(x, y);
        pb.setSize(w, h);
        if (splash != null) {
            if (urlToOpen == null) splash.getStatusBar().showStatus("Making Default Project"); else splash.getStatusBar().showStatus("Reading " + projectName);
            splash.getStatusBar().showProgress(40);
        }
        st.mark("make empty project");
        if (urlToOpen == null) p = Project.makeEmptyProject(); else {
            try {
                p = Project.loadProject(urlToOpen);
            } catch (FileNotFoundException fn) {
                JOptionPane.showMessageDialog(pb, "Could not find the project file " + urlToOpen.toString() + "\n", "Error", JOptionPane.ERROR_MESSAGE);
                Argo.log.error("Could not load most recent project file: " + urlToOpen.toString());
                Argo.log.error(fn);
                Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
                urlToOpen = null;
                p = Project.makeEmptyProject();
            } catch (IOException io) {
                JOptionPane.showMessageDialog(pb, "Could not load the project " + urlToOpen.toString() + "\n" + "Project file probably corrupted.\n" + "Please file a bug report at argouml.tigris.org including" + " the corrupted project file.", "Error", JOptionPane.ERROR_MESSAGE);
                Argo.log.error("Could not load most recent project file: " + urlToOpen.toString());
                Argo.log.error(io);
                Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
                urlToOpen = null;
                p = Project.makeEmptyProject();
            } catch (Exception ex) {
                Argo.log.error("Could not load most recent project file: " + urlToOpen.toString());
                Argo.log.error(ex);
                Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE, "");
                urlToOpen = null;
                p = Project.makeEmptyProject();
            }
        }
        st.mark("set project");
        Trash.SINGLETON.getSize();
        pb.setProject(p);
        st.mark("perspectives");
        if (urlToOpen == null) pb.setTitle("Untitled");
        if (splash != null) {
            splash.getStatusBar().showStatus("Setting Perspectives");
            splash.getStatusBar().showProgress(50);
        }
        pb.setPerspectives(NavPerspective.getRegisteredPerspectives());
        pb.setToDoPerspectives(ToDoPerspective.getRegisteredPerspectives());
        pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (splash != null) {
            splash.getStatusBar().showStatus("Loading modules");
            splash.getStatusBar().showProgress(75);
        }
        st.mark("modules");
        Argo.initializeModules();
        st.mark("open window");
        if (splash != null) {
            splash.getStatusBar().showStatus("Opening Project Browser");
            splash.getStatusBar().showProgress(95);
        }
        pb.setVisible(true);
        Object model = p.getModels().elementAt(0);
        Object diag = p.getDiagrams().elementAt(0);
        pb.getNavPane().setSelection(model, diag);
        st.mark("close splash");
        if (splash != null) {
            splash.setVisible(false);
            splash.dispose();
            splash = null;
        }
        Class c = null;
        c = org.tigris.gef.base.ModePlace.class;
        c = org.tigris.gef.base.ModeModify.class;
        c = org.tigris.gef.base.SelectionResize.class;
        c = org.tigris.gef.presentation.FigPoly.class;
        c = org.tigris.gef.base.PathConvPercentPlusConst.class;
        c = org.tigris.gef.presentation.ArrowHeadNone.class;
        c = org.tigris.gef.base.Geometry.class;
        c = org.tigris.gef.ui.ColorRenderer.class;
        c = org.tigris.gef.util.EnumerationEmpty.class;
        c = org.tigris.gef.util.EnumerationSingle.class;
        c = ru.novosoft.uml.foundation.data_types.MScopeKind.class;
        c = ru.novosoft.uml.foundation.data_types.MChangeableKind.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigClass.class;
        c = org.argouml.uml.diagram.ui.SelectionNodeClarifiers.class;
        c = org.argouml.uml.diagram.ui.SelectionWButtons.class;
        c = org.argouml.uml.diagram.static_structure.ui.SelectionClass.class;
        c = org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigPackage.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigInterface.class;
        c = java.lang.ClassNotFoundException.class;
        c = org.argouml.kernel.DelayedChangeNotify.class;
        c = org.tigris.gef.graph.GraphEvent.class;
        c = org.tigris.gef.graph.presentation.NetEdge.class;
        c = org.tigris.gef.graph.GraphEdgeHooks.class;
        c = org.argouml.uml.cognitive.critics.WizMEName.class;
        c = org.argouml.kernel.Wizard.class;
        st.mark("start critics");
        Runnable startCritics = new StartCritics();
        Main.addPostLoadAction(startCritics);
        st.mark("start preloader");
        if (preload) {
            Runnable preloader = new PreloadClasses();
            Main.addPostLoadAction(preloader);
        }
        PostLoad pl = new PostLoad(postLoadActions);
        Thread postLoadThead = new Thread(pl);
        pl.setThread(postLoadThead);
        postLoadThead.start();
        if (profileLoad) {
            Argo.log.info("");
            Argo.log.info("profile of load time ############");
            for (Enumeration i = st.result(); i.hasMoreElements(); ) {
                Argo.log.info(i.nextElement());
            }
            Argo.log.info("#################################");
            Argo.log.info("");
        }
        st = null;
        ToolTipManager.sharedInstance().setDismissDelay(50000000);
    }

    public static void addPostLoadAction(Runnable r) {
        postLoadActions.addElement(r);
    }

    /** Install our security handlers,
     *  and do basic initialization of log4j.
     *
     *  Log4j initialization must be done as
     *  part of the main class initializer, so that
     *  the log4j initialization is complete
     *  before any other static initializers.
     *
     *  Also installs a trap to "eat" certain SecurityExceptions.
     *  Refer to {@link java.awt.EventDispatchThread} for details.
     */
    static {
        System.setProperty("sun.awt.exception.handler", ArgoAwtExceptionHandler.class.getName());
        System.setSecurityManager(ArgoSecurityManager.getInstance());
        if (System.getProperty("log4j.configuration") == null) {
            BasicConfigurator.configure();
            Category.getRoot().getHierarchy().disableAll();
        }
    }
}

class StartCritics implements Runnable {

    public void run() {
        Designer dsgr = Designer.theDesigner();
        org.argouml.uml.cognitive.critics.Init.init();
        org.argouml.uml.cognitive.checklist.Init.init(Locale.getDefault());
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = pb.getProject();
        dsgr.spawnCritiquer(p);
        dsgr.setChildGenerator(new ChildGenUML());
        java.util.Enumeration models = (p.getModels()).elements();
        while (models.hasMoreElements()) {
            ((ru.novosoft.uml.model_management.MModel) models.nextElement()).addMElementListener(dsgr);
        }
        Argo.log.info("spawned critiquing thread");
        dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decINHERITANCE);
        dsgr.startConsidering(org.argouml.uml.cognitive.critics.CrUML.decCONTAINMENT);
        Designer._userWorking = true;
    }
}

class PostLoad implements Runnable {

    Vector postLoadActions = null;

    Thread myThread = null;

    public PostLoad(Vector v) {
        postLoadActions = v;
    }

    public void setThread(Thread t) {
        myThread = t;
    }

    public void run() {
        try {
            myThread.sleep(1000);
        } catch (Exception ex) {
            Argo.log.error("post load no sleep", ex);
        }
        int size = postLoadActions.size();
        for (int i = 0; i < size; i++) {
            Runnable r = (Runnable) postLoadActions.elementAt(i);
            r.run();
            try {
                myThread.sleep(100);
            } catch (Exception ex) {
                Argo.log.error("post load no sleep2", ex);
            }
        }
    }
}

class PreloadClasses implements Runnable {

    public void run() {
        Class c = null;
        Argo.log.info("preloading...");
        c = org.tigris.gef.base.CmdSetMode.class;
        c = org.tigris.gef.base.ModePlace.class;
        c = org.tigris.gef.base.ModeModify.class;
        c = org.tigris.gef.base.SelectionResize.class;
        c = org.tigris.gef.ui.ColorRenderer.class;
        c = org.tigris.gef.ui.Swatch.class;
        c = org.tigris.gef.util.EnumerationEmpty.class;
        c = org.tigris.gef.util.EnumerationSingle.class;
        c = org.argouml.uml.GenCompositeClasses.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigClass.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigPackage.class;
        c = org.argouml.uml.diagram.static_structure.ui.FigInterface.class;
        c = org.argouml.uml.diagram.ui.FigAssociation.class;
        c = org.argouml.uml.diagram.ui.FigGeneralization.class;
        c = org.argouml.uml.diagram.ui.FigRealization.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelClass.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelInterface.class;
        c = org.argouml.uml.ui.foundation.core.PropPanelAssociation.class;
        c = org.argouml.ui.StylePanelFig.class;
        c = org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass.class;
        c = org.argouml.uml.diagram.static_structure.ui.StylePanelFigInterface.class;
        c = org.argouml.uml.diagram.ui.SPFigEdgeModelElement.class;
        c = java.lang.ClassNotFoundException.class;
        c = org.argouml.ui.UpdateTreeHack.class;
        c = org.argouml.kernel.DelayedChangeNotify.class;
        c = org.tigris.gef.graph.GraphEvent.class;
        c = org.tigris.gef.graph.presentation.NetEdge.class;
        c = org.tigris.gef.graph.GraphEdgeHooks.class;
        c = org.argouml.uml.cognitive.critics.WizMEName.class;
        c = org.argouml.kernel.Wizard.class;
        c = java.beans.Introspector.class;
        c = java.beans.BeanInfo.class;
        c = java.beans.BeanDescriptor.class;
        c = java.beans.FeatureDescriptor.class;
        c = java.lang.InterruptedException.class;
        c = java.lang.CloneNotSupportedException.class;
        c = java.lang.reflect.Modifier.class;
        c = java.beans.EventSetDescriptor.class;
        c = java.beans.PropertyDescriptor.class;
        c = java.lang.Void.class;
        c = java.beans.MethodDescriptor.class;
        c = java.beans.SimpleBeanInfo.class;
        c = java.util.TooManyListenersException.class;
        c = java.beans.PropertyVetoException.class;
        c = java.beans.IndexedPropertyDescriptor.class;
        c = org.argouml.kernel.HistoryItemResolve.class;
        c = org.tigris.gef.event.ModeChangeEvent.class;
        c = org.argouml.uml.cognitive.critics.ClClassName.class;
        c = org.argouml.ui.Clarifier.class;
        c = org.argouml.uml.cognitive.critics.ClAttributeCompartment.class;
        c = org.argouml.uml.cognitive.critics.ClOperationCompartment.class;
        c = java.lang.SecurityException.class;
        c = java.lang.NullPointerException.class;
        Argo.log.info(" done preloading");
    }
}
