package net.sourceforge.squirrel_sql.client.update;

import static java.lang.System.currentTimeMillis;
import static net.sourceforge.squirrel_sql.client.update.UpdateUtil.RELEASE_XML_FILENAME;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.plugin.IPluginManager;
import net.sourceforge.squirrel_sql.client.plugin.PluginInfo;
import net.sourceforge.squirrel_sql.client.preferences.GlobalPreferencesActionListener;
import net.sourceforge.squirrel_sql.client.preferences.GlobalPreferencesSheet;
import net.sourceforge.squirrel_sql.client.preferences.IUpdateSettings;
import net.sourceforge.squirrel_sql.client.preferences.UpdatePreferencesPanel;
import net.sourceforge.squirrel_sql.client.update.async.ReleaseFileUpdateCheckTask;
import net.sourceforge.squirrel_sql.client.update.async.UpdateCheckRunnableCallback;
import net.sourceforge.squirrel_sql.client.update.downloader.ArtifactDownloader;
import net.sourceforge.squirrel_sql.client.update.downloader.ArtifactDownloaderFactory;
import net.sourceforge.squirrel_sql.client.update.gui.ArtifactAction;
import net.sourceforge.squirrel_sql.client.update.gui.ArtifactStatus;
import net.sourceforge.squirrel_sql.client.update.gui.CheckUpdateListener;
import net.sourceforge.squirrel_sql.client.update.gui.UpdateManagerDialog;
import net.sourceforge.squirrel_sql.client.update.gui.UpdateSummaryDialog;
import net.sourceforge.squirrel_sql.client.update.xmlbeans.ChannelXmlBean;
import net.sourceforge.squirrel_sql.fw.gui.GUIUtils;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

/**
 * This class implements the business logic needed by the view (UpdateManagerDialog), to let the user install
 * new or updated software (the model)
 * 
 * @author manningr
 */
public class UpdateControllerImpl implements UpdateController, CheckUpdateListener {

    /** This is the pattern that all translation jars (i18n) begin with */
    public static final String TRANSLATION_JAR_PREFIX = "squirrel-sql_";

    /** Logger for this class. */
    private static final ILogger s_log = LoggerController.createLogger(UpdateControllerImpl.class);

    /** I18n strings for this class */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(UpdateControllerImpl.class);

    /** the application and services it provides */
    private IApplication _app = null;

    /** utility class for low-level update routines */
    private UpdateUtil _util = null;

    /** the release that we downloaded when we last checked */
    private ChannelXmlBean _currentChannelBean = null;

    /** the release we had installed the last time we checked / updated */
    private ChannelXmlBean _installedChannelBean = null;

    /** Used to be able to bring the update dialog back up after re-config */
    private static GlobalPrefsListener listener = null;

    /** The class that we use which is responsible for downloading artifacts */
    private ArtifactDownloader _downloader = null;

    private ArtifactDownloaderFactory _downloaderFactory = null;

    static interface i18n {

        String EXCEPTION_MSG = s_stringMgr.getString("UpdateControllerImpl.exceptionMsg");

        String UPDATE_CHECK_FAILED_TITLE = s_stringMgr.getString("UpdateControllerImpl.updateCheckFailedTitle");

        String SOFTWARE_VERSION_CURRENT_MSG = s_stringMgr.getString("UpdateControllerImpl.softwareVersionCurrentMsg");

        String UPDATE_CHECK_TITLE = s_stringMgr.getString("UpdateControllerImpl.updateCheckTitle");

        String CHANGES_RECORDED_TITLE = s_stringMgr.getString("UpdateControllerImpl.changesRecordedTitle");

        String CHANGES_RECORDED_MSG = s_stringMgr.getString("UpdateControllerImpl.changesRecordedMsg");

        String RELEASE_FILE_DOWNLOAD_FAILED_MSG = s_stringMgr.getString("UpdateControllerImpl.releaseFileDownloadFailedMsg");

        String PROMPT_TO_DOWNLOAD_AVAILABLE_UPDATES_MSG = s_stringMgr.getString("UpdateControllerImpl.promptToDownloadAvailableUpdatesMsg");

        String PROMPT_TO_DOWNLOAD_AVAILABLE_UPDATES_TITLE = s_stringMgr.getString("UpdateControllerImpl.promptToDownloadAvailableUpdatesTitle");
    }

    /**
	 * Constructor
	 * 
	 * @param app
	 *           the application and services it provides
	 */
    public UpdateControllerImpl(IApplication app) {
        _app = app;
        if (listener == null) {
            listener = new GlobalPrefsListener();
            GlobalPreferencesSheet.addGlobalPreferencesActionListener(listener);
        }
    }

    /**
	 * @param factory
	 */
    public void setArtifactDownloaderFactory(ArtifactDownloaderFactory factory) {
        this._downloaderFactory = factory;
    }

    /**
	 * Sets the utility class for low-level update routines
	 * 
	 * @param util
	 *           the Update utility class to use.
	 */
    public void setUpdateUtil(UpdateUtil util) {
        this._util = util;
        _util.setPluginManager(_app.getPluginManager());
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#showUpdateDialog()
	 */
    public void showUpdateDialog() {
        JFrame parent = _app.getMainFrame();
        IUpdateSettings settings = getUpdateSettings();
        boolean isRemoteUpdateSite = settings.isRemoteUpdateSite();
        UpdateManagerDialog dialog = new UpdateManagerDialog(parent, isRemoteUpdateSite);
        if (isRemoteUpdateSite) {
            dialog.setUpdateServerName(settings.getUpdateServer());
            dialog.setUpdateServerPort(settings.getUpdateServerPort());
            dialog.setUpdateServerPath(settings.getUpdateServerPath());
            dialog.setUpdateServerChannel(settings.getUpdateServerChannel());
        } else {
            dialog.setLocalUpdatePath(settings.getFileSystemUpdatePath());
        }
        dialog.addCheckUpdateListener(this);
        dialog.setVisible(true);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#isUpToDate()
	 */
    public boolean isUpToDate() throws Exception {
        IUpdateSettings settings = getUpdateSettings();
        String releaseFilename = _util.getLocalReleaseFile().getAbsolutePath();
        _installedChannelBean = _util.getLocalReleaseInfo(releaseFilename);
        String channelName = getDesiredChannel(settings);
        StringBuilder releasePath = new StringBuilder("/");
        releasePath.append(getUpdateServerPath());
        releasePath.append("/");
        releasePath.append(channelName);
        releasePath.append("/");
        if (settings.isRemoteUpdateSite()) {
            _currentChannelBean = _util.downloadCurrentRelease(getUpdateServerName(), getUpdateServerPortAsInt(), releasePath.toString(), RELEASE_XML_FILENAME, _app.getSquirrelPreferences().getProxySettings());
        } else {
            _currentChannelBean = _util.loadUpdateFromFileSystem(settings.getFileSystemUpdatePath());
        }
        settings.setLastUpdateCheckTimeMillis("" + currentTimeMillis());
        saveUpdateSettings(settings);
        return _currentChannelBean.equals(_installedChannelBean);
    }

    /**
	 * This method takes a look at preference for channel and the channel that the user currently has installed
	 * and logs an info if switching from one to channel to another.
	 * 
	 * @return the name of the channel that the user wants.
	 */
    private String getDesiredChannel(final IUpdateSettings settings) {
        String desiredChannel = settings.getUpdateServerChannel().toLowerCase();
        String currentChannelName = _installedChannelBean.getName();
        if (!currentChannelName.equals(desiredChannel)) {
            if (s_log.isInfoEnabled()) {
                s_log.info("getDesiredChannel: User is switching distribution channel from " + "installed channel (" + currentChannelName + ") to new channel (" + desiredChannel + ")");
            }
        }
        return desiredChannel;
    }

    /**
	 * Returns a set of plugins (internal names) of plugins that are currently installed (regardless of whether
	 * or not they are enabled).
	 * 
	 * @return a set of plugin internal names
	 */
    public Set<String> getInstalledPlugins() {
        Set<String> result = new HashSet<String>();
        IPluginManager pmgr = _app.getPluginManager();
        PluginInfo[] infos = pmgr.getPluginInformation();
        for (PluginInfo info : infos) {
            result.add(info.getInternalName());
        }
        return result;
    }

    /**
	 * Go get the files that need to be updated. The specified list could have new files to get (INSTALL),
	 * existing files to remove (REMOVE). This method's only concern is with fetching the new artifacts to be
	 * installed.
	 */
    public void pullDownUpdateFiles(List<ArtifactStatus> artifactStatusList, DownloadStatusEventHandler handler, boolean releaseVersionWillChange) {
        List<ArtifactStatus> newartifactsList = new ArrayList<ArtifactStatus>();
        for (ArtifactStatus status : artifactStatusList) {
            if (status.getArtifactAction() == ArtifactAction.INSTALL) {
                newartifactsList.add(status);
            }
        }
        if (newartifactsList.size() > 0) {
            _downloader = _downloaderFactory.create(newartifactsList);
            _downloader.setUtil(_util);
            _downloader.setProxySettings(_app.getSquirrelPreferences().getProxySettings());
            _downloader.setIsRemoteUpdateSite(isRemoteUpdateSite());
            _downloader.setHost(getUpdateServerName());
            _downloader.setPort(Integer.parseInt(getUpdateServerPort()));
            _downloader.setPath(getUpdateServerPath());
            _downloader.setFileSystemUpdatePath(getUpdateSettings().getFileSystemUpdatePath());
            _downloader.addDownloadStatusListener(handler);
            _downloader.setReleaseVersionWillChange(releaseVersionWillChange);
            handler.setDownloader(_downloader);
            _downloader.setChannelName(getUpdateServerChannel().toLowerCase());
            _downloader.start();
        } else {
            showMessage(i18n.CHANGES_RECORDED_TITLE, i18n.CHANGES_RECORDED_MSG);
        }
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#getUpdateServerChannel()
	 */
    public String getUpdateServerChannel() {
        return getUpdateSettings().getUpdateServerChannel();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#getUpdateServerName()
	 */
    public String getUpdateServerName() {
        return getUpdateSettings().getUpdateServer();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#isRemoteUpdateSite()
	 */
    public boolean isRemoteUpdateSite() {
        return getUpdateSettings().isRemoteUpdateSite();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#getUpdateServerPath()
	 */
    public String getUpdateServerPath() {
        return getUpdateSettings().getUpdateServerPath();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#getUpdateServerPort()
	 */
    public String getUpdateServerPort() {
        return getUpdateSettings().getUpdateServerPort();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#getUpdateServerPortAsInt()
	 */
    public int getUpdateServerPortAsInt() {
        return Integer.parseInt(getUpdateServerPort());
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController# showConfirmMessage(java.lang.String,
	 *      java.lang.String)
	 */
    public boolean showConfirmMessage(String title, String msg) {
        int result = JOptionPane.showConfirmDialog(_app.getMainFrame(), msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == JOptionPane.YES_OPTION);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#showMessage(java.lang.String,
	 *      java.lang.String)
	 */
    public void showMessage(String title, String msg) {
        JOptionPane.showMessageDialog(_app.getMainFrame(), msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#showErrorMessage(java.lang.String,
	 *      java.lang.String)
	 */
    public void showErrorMessage(String title, String msg, Exception e) {
        s_log.error(msg, e);
        JOptionPane.showMessageDialog(_app.getMainFrame(), msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#showErrorMessage(java.lang.String,
	 *      java.lang.String)
	 */
    public void showErrorMessage(String title, String msg) {
        showErrorMessage(title, msg, null);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#isTimeToCheckForUpdates()
	 */
    public boolean isTimeToCheckForUpdates() {
        IUpdateSettings settings = getUpdateSettings();
        if (!settings.isEnableAutomaticUpdates()) {
            return false;
        }
        long lastCheckTime = Long.parseLong(settings.getLastUpdateCheckTimeMillis());
        long delta = currentTimeMillis() - lastCheckTime;
        UpdateCheckFrequency updateCheckFrequency = _util.getUpdateCheckFrequency(settings);
        return updateCheckFrequency.isTimeForUpdateCheck(delta);
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#promptUserToDownloadAvailableUpdates()
	 */
    public void promptUserToDownloadAvailableUpdates() {
        boolean userSaidYes = showConfirmMessage(i18n.PROMPT_TO_DOWNLOAD_AVAILABLE_UPDATES_TITLE, i18n.PROMPT_TO_DOWNLOAD_AVAILABLE_UPDATES_MSG);
        if (userSaidYes) {
            showUpdateDialog();
        } else {
            s_log.info("promptUserToDownloadAvailableUpdates: user decided not to download updates at " + "this time (currentTimeMillis=" + System.currentTimeMillis() + ")");
        }
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#checkUpToDate()
	 */
    public void checkUpToDate() {
        UpdateCheckRunnableCallback callback = new UpdateCheckRunnableCallback() {

            public void updateCheckComplete(final boolean isUpdateToDate, final ChannelXmlBean installedChannelXmlBean, final ChannelXmlBean currentChannelXmlBean) {
                GUIUtils.processOnSwingEventThread(new Runnable() {

                    public void run() {
                        if (isUpdateToDate) {
                            showMessage(i18n.UPDATE_CHECK_TITLE, i18n.SOFTWARE_VERSION_CURRENT_MSG);
                        }
                        _currentChannelBean = currentChannelXmlBean;
                        _installedChannelBean = installedChannelXmlBean;
                        List<ArtifactStatus> artifactStatusItems = _util.getArtifactStatus(_currentChannelBean);
                        String installedVersion = _installedChannelBean.getCurrentRelease().getVersion();
                        String currentVersion = _currentChannelBean.getCurrentRelease().getVersion();
                        showUpdateSummaryDialog(artifactStatusItems, installedVersion, currentVersion);
                    }
                });
            }

            public void updateCheckFailed(final Exception e) {
                if (e == null || e instanceof FileNotFoundException) {
                    showErrorMessage(i18n.UPDATE_CHECK_FAILED_TITLE, i18n.RELEASE_FILE_DOWNLOAD_FAILED_MSG);
                } else {
                    showErrorMessage(i18n.UPDATE_CHECK_FAILED_TITLE, i18n.EXCEPTION_MSG + e.getClass().getName() + ":" + e.getMessage(), e);
                }
            }
        };
        ReleaseFileUpdateCheckTask runnable = new ReleaseFileUpdateCheckTask(callback, getUpdateSettings(), _util, _app);
        runnable.start();
    }

    private void showUpdateSummaryDialog(final List<ArtifactStatus> artifactStatusItems, final String installedVersion, final String currentVersion) {
        GUIUtils.processOnSwingEventThread(new Runnable() {

            public void run() {
                UpdateSummaryDialog dialog = new UpdateSummaryDialog(_app.getMainFrame(), artifactStatusItems, UpdateControllerImpl.this);
                dialog.setInstalledVersion(installedVersion);
                dialog.setAvailableVersion(currentVersion);
                GUIUtils.centerWithinParent(_app.getMainFrame());
                dialog.setVisible(true);
            }
        });
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.UpdateController#applyChanges(java.util.List, boolean)
	 */
    public void applyChanges(List<ArtifactStatus> artifactStatusList, boolean releaseVersionWillChange) {
        try {
            _util.saveChangeList(artifactStatusList);
            pullDownUpdateFiles(artifactStatusList, new DownloadStatusEventHandler(this), releaseVersionWillChange);
        } catch (Exception e) {
            showErrorMessage(i18n.UPDATE_CHECK_FAILED_TITLE, i18n.EXCEPTION_MSG + e.getClass().getName() + ":" + e.getMessage(), e);
        }
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.gui.CheckUpdateListener#showPreferences()
	 */
    public void showPreferences() {
        listener.setWaitingForOk(true);
        GlobalPreferencesSheet.showSheet(_app, UpdatePreferencesPanel.class);
    }

    public JFrame getMainFrame() {
        return _app.getMainFrame();
    }

    /**
	 * Returns the UpdateSettings from preferences.
	 * 
	 * @return
	 */
    private IUpdateSettings getUpdateSettings() {
        return _app.getSquirrelPreferences().getUpdateSettings();
    }

    /**
	 * @param settings
	 */
    private void saveUpdateSettings(final IUpdateSettings settings) {
        _app.getSquirrelPreferences().setUpdateSettings(settings);
    }

    private class GlobalPrefsListener implements GlobalPreferencesActionListener {

        private boolean waitingForOk = false;

        public void onDisplayGlobalPreferences() {
        }

        public void onPerformClose() {
            showDialog();
        }

        public void onPerformOk() {
            showDialog();
        }

        /**
		 * Re-show the dialog if we were waiting for Ok/Close.
		 */
        private void showDialog() {
            if (waitingForOk) {
                waitingForOk = false;
                showUpdateDialog();
            }
        }

        /**
		 * @param waitingForOk
		 *           the waitingForOk to set
		 */
        public void setWaitingForOk(boolean waitingForOk) {
            this.waitingForOk = waitingForOk;
        }
    }
}
