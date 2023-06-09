package de.erdesignerng.visual;

import de.erdesignerng.ERDesignerBundle;
import de.erdesignerng.model.Model;
import de.erdesignerng.modificationtracker.HistoryModificationTracker;
import de.erdesignerng.util.ApplicationPreferences;
import de.erdesignerng.util.MavenPropertiesLocator;
import de.erdesignerng.visual.common.*;
import de.erdesignerng.visual.editor.DialogConstants;
import de.erdesignerng.visual.editor.exception.ExceptionEditor;
import de.erdesignerng.visual.editor.usagedata.UsageDataEditor;
import de.mogwai.common.client.looks.UIInitializer;
import de.mogwai.common.client.looks.components.DefaultFrame;
import de.mogwai.common.client.looks.components.DefaultToolbar;
import de.mogwai.common.i18n.ResourceHelper;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import org.apache.log4j.Logger;

/**
 * @author $Author: mirkosertic $
 * @version $Date: 2008-11-06 22:01:08 $
 */
public class ERDesignerMainFrame extends DefaultFrame implements ERDesignerWorldConnector {

    private static final Logger LOGGER = Logger.getLogger(ERDesignerMainFrame.class);

    private static final String WINDOW_ALIAS = "ERDesignerMainFrame";

    private ERDesignerComponent component;

    private DockingHelper dockingHelper;

    public ERDesignerMainFrame() {
        super(ERDesignerBundle.TITLE);
        initialize();
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(IconFactory.getERDesignerIcon().getImage());
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        UIInitializer.getInstance().initialize(this);
        initTitle();
    }

    @Override
    public ResourceHelper getResourceHelper() {
        return ResourceHelper.getResourceHelper(ERDesignerBundle.BUNDLE_NAME);
    }

    private void initialize() {
        component = ERDesignerComponent.initializeComponent(this);
        OutlineComponent.initializeComponent();
        SQLComponent.initializeComponent();
        dockingHelper = new DockingHelper();
        try {
            dockingHelper.initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getDefaultFrameContent().setDetailComponent(dockingHelper.getRootWindow());
    }

    @Override
    public final void initTitle() {
        initTitle(null);
    }

    @Override
    public DefaultToolbar getToolBar() {
        return getDefaultFrameContent().getToolbar();
    }

    @Override
    public void initTitle(String aFile) {
        StringBuffer theTitle = new StringBuffer();
        if (aFile != null) {
            theTitle.append(" - ").append(aFile);
        }
        String theVersion = MavenPropertiesLocator.getERDesignerVersionInfo();
        setTitle(getResourceHelper().getText(getResourceBundleID()) + " " + theVersion + " " + theTitle);
    }

    @Override
    public void setStatusText(String aMessage) {
        getDefaultFrameContent().getStatusBar().setText(aMessage);
    }

    public void setModel(Model aModel) {
        component.setModel(aModel);
    }

    @Override
    public boolean supportsClasspathEditor() {
        return true;
    }

    @Override
    public boolean supportsConnectionEditor() {
        return true;
    }

    @Override
    public boolean supportsExitApplication() {
        return true;
    }

    @Override
    public Model createNewModel() {
        Model theModel = new Model();
        theModel.setModificationTracker(new HistoryModificationTracker(theModel));
        return theModel;
    }

    @Override
    public boolean supportsPreferences() {
        return true;
    }

    @Override
    public void initializeLoadedModel(Model aModel) {
        aModel.setModificationTracker(new HistoryModificationTracker(aModel));
    }

    @Override
    public void notifyAboutException(Exception aException) {
        ExceptionEditor theEditor = new ExceptionEditor(this, aException);
        theEditor.showModal();
    }

    @Override
    public void exitApplication() {
        ApplicationPreferences.getInstance().updateWindowDefinition(WINDOW_ALIAS, this);
        dockingHelper.saveLayoutToPreferences();
        component.savePreferences();
        setVisible(false);
    }

    @Override
    public void setVisible(boolean aVisible) {
        ApplicationPreferences thePreferences = ApplicationPreferences.getInstance();
        if (aVisible == false) {
            if (thePreferences.isUsageDataCollector()) {
                if (thePreferences.isUsageDataCollectorAlways()) {
                    UsageDataCollector.getInstance().flush();
                } else {
                    if (!thePreferences.isUsageDataCollectorNoThisTime()) {
                        UsageDataEditor theEditor = new UsageDataEditor(this, false);
                        if (theEditor.showModal() == DialogConstants.MODAL_RESULT_OK) {
                            try {
                                theEditor.applyValues();
                            } catch (Exception e1) {
                                LOGGER.error(e1.getMessage(), e1);
                            }
                        }
                    }
                }
                component.savePreferences();
            }
        }
        super.setVisible(aVisible);
        if (aVisible && !thePreferences.isAskedForUsageDataCollection()) {
            UsageDataEditor theEditor = new UsageDataEditor(this, true);
            if (theEditor.showModal() == DialogConstants.MODAL_RESULT_OK) {
                try {
                    theEditor.applyValues();
                } catch (Exception e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            }
            thePreferences.setAskedForUsageDataCollection(true);
        }
        if (aVisible) {
            ApplicationPreferences.getInstance().setWindowState(WINDOW_ALIAS, this);
        } else {
            component.savePreferences();
            System.exit(0);
        }
    }

    @Override
    public boolean supportsRepositories() {
        return true;
    }

    @Override
    public boolean supportsHelp() {
        return true;
    }

    @Override
    public boolean supportsReporting() {
        return true;
    }

    /**
     * Open a specific file in the editor.
     *
     * @param aFile
     */
    public void commandOpenFile(File aFile) {
        component.commandOpenFile(aFile);
    }
}
