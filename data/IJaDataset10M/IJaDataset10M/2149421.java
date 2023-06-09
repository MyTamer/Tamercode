package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import pr.BlueSVNListView;
import pr.BlueSVNLogView;
import pr.PreferencePane;
import svn.BlueSVNHandler;
import svn.SVNHandler;
import util.BlueSVNLogger.LEVEL;
import bluej.extensions.BClass;
import bluej.extensions.BPackage;
import bluej.extensions.BProject;
import bluej.extensions.BlueJ;
import bluej.extensions.ProjectNotOpenException;
import exception.BlueSVNException;

public class BlueSVNManager {

    private static BlueSVNManager _instance = null;

    private BlueSVNLogView _logger = new BlueSVNLogView();

    private BlueSVNLogView _debugger = new BlueSVNLogView();

    private BlueJ _bluej = null;

    private BProject _currentProject = null;

    private BlueSVNHandler _svnHandler = null;

    private BlueSVNListManager _listManager = null;

    private HashMap<String, String> _configuration = new HashMap<String, String>();

    private String _project_part = "";

    private BlueSVNManager() {
    }

    public static BlueSVNManager getInstance() {
        if (_instance == null) {
            _instance = new BlueSVNManager();
        }
        return _instance;
    }

    public BlueSVNHandler get_svnHandler() {
        if (_svnHandler == null) {
            _svnHandler = new BlueSVNHandler();
        }
        return _svnHandler;
    }

    public BlueSVNListManager get_listManager() {
        if (_listManager == null) {
            _listManager = new BlueSVNListManager();
        }
        return _listManager;
    }

    public BlueJ get_bluej() {
        return _bluej;
    }

    public void set_bluej(BlueJ _bluej) throws BlueSVNException {
        this._bluej = _bluej;
        if (_bluej != null) parseConfiguration();
    }

    public String get_projectRepPart() throws BlueSVNException {
        if (_project_part.length() == 0) {
            throw new BlueSVNException(Strings.getString("BlueSVNManager.projectPartEmpty"));
        }
        return get_project_part() + "-student-" + getStudentID();
    }

    public String get_projectRepPart(String new_project_part) {
        return new_project_part + "-student-" + getStudentID();
    }

    public String get_curProjectName() {
        try {
            return get_currentProject().getName();
        } catch (Exception e) {
            return "";
        }
    }

    public String get_curProjectPath() {
        try {
            return get_currentProject().getDir().getAbsolutePath();
        } catch (ProjectNotOpenException e) {
            return "";
        }
    }

    private void parseConfiguration() throws BlueSVNException {
        Scanner theScanner = null;
        if (!new File(_bluej.getUserConfigDir().getAbsolutePath() + "/blueSVN.properties").exists()) {
            BlueSVNLogger.getInstance().log("no user config found. using default values.");
            theScanner = new Scanner(BlueSVNManager.class.getResourceAsStream("/util/blueSVN.properties"));
        } else {
            BlueSVNLogger.getInstance().log("user config found. using user values.");
            try {
                theScanner = new Scanner(new FileReader(_bluej.getUserConfigDir().getAbsolutePath() + "/blueSVN.properties"));
            } catch (FileNotFoundException e) {
                BlueSVNLogger.getInstance().debug("unable to parse config file", e, BlueSVNLogger.LEVEL.HIGHEST);
                throw new BlueSVNException(Strings.getString("BlueSVNManager.unableToParse"));
            }
        }
        theScanner.useDelimiter("=");
        String key;
        String value;
        while (theScanner.hasNext()) {
            key = theScanner.next().replaceAll("(\\s|=)", "");
            value = theScanner.nextLine().replaceAll("(\\s|=)", "");
            _configuration.put(key, value);
            BlueSVNLogger.getInstance().log(key + " - " + value);
        }
        BlueSVNLogger.getInstance().set_level(getDebugLevel());
    }

    private void checkAllFolders(File theFile, BProject theProject) {
        if (theFile != null && theFile.isDirectory() && !theFile.getAbsolutePath().contains(".svn") && !theFile.getAbsolutePath().contains(File.pathSeparator + "doc" + File.pathSeparator)) {
            try {
                String packageName = theFile.getAbsolutePath().substring(theProject.getDir().getAbsolutePath().length());
                String replaced = packageName.replaceAll("((\\\\)|(/))", ".");
                String tempPackage = replaced.substring(((replaced.startsWith(".")) ? 1 : 0), ((replaced.endsWith(".")) ? replaced.length() - 1 : replaced.length()));
                BlueSVNLogger.getInstance().debug("--> packageName is " + tempPackage + " - " + theProject.getPackage(tempPackage), LEVEL.MEDIUM);
                if (theProject.getPackage(tempPackage) == null) {
                    theProject.newPackage(tempPackage);
                }
            } catch (NullPointerException e) {
                BlueSVNLogger.getInstance().debug("--> null pointer while creating package", e, LEVEL.LOW);
            } catch (Exception e) {
                BlueSVNLogger.getInstance().debug("--> error creating package", e, LEVEL.MEDIUM);
            }
            for (File cur : theFile.listFiles()) {
                checkAllFolders(cur, theProject);
            }
        }
    }

    public void reloadCurrentProject() {
        BProject curProject = get_currentProject();
        if (curProject != null) {
            try {
                checkAllFolders(curProject.getDir(), curProject);
                for (BPackage bp : curProject.getPackages()) {
                    for (BClass cc : bp.getClasses()) {
                        cc.getEditor().saveFile();
                    }
                    bp.reload();
                }
            } catch (Exception e) {
                BlueSVNLogger.getInstance().debug(e, LEVEL.LOW);
            }
        }
    }

    public void refreshGUI() {
        reloadCurrentProject();
        BlueSVNListView.getInstance().refresh();
    }

    public BProject get_currentProject() {
        if (_currentProject == null && _bluej.getOpenProjects().length > 0) set_currentProject(_bluej.getOpenProjects()[_bluej.getOpenProjects().length - 1]);
        return _currentProject;
    }

    public void manage_project_part(BProject project) {
        if (BlueSVNListView.getInstance().isVisible() && !is_project_part_valid()) {
            try {
                File blueSVN_project_properties = new File(project.getDir().getAbsolutePath() + "/blueSVN_project.properties");
                if (!blueSVN_project_properties.exists()) {
                    set_project_part("");
                } else {
                    try {
                        Scanner theScanner = new Scanner(new FileReader(blueSVN_project_properties));
                        theScanner.useDelimiter("=");
                        String key;
                        String value;
                        while (theScanner.hasNext()) {
                            key = theScanner.next().replaceAll("(\\s|=)", "");
                            value = theScanner.nextLine().replaceAll("(\\s|=)", "");
                            BlueSVNLogger.getInstance().debug(key + "->" + value + " (" + key.compareTo("b l u e S V N _ p r o j e c t _ p a r t") + ")", LEVEL.MEDIUM);
                            set_project_part(value);
                        }
                    } catch (FileNotFoundException e) {
                        set_project_part("");
                        BlueSVNLogger.getInstance().debug(e, LEVEL.MEDIUM);
                    }
                    BlueSVNListView.getInstance().refresh();
                }
            } catch (ProjectNotOpenException e) {
                set_project_part("");
                BlueSVNLogger.getInstance().debug(e, LEVEL.MEDIUM);
            }
        }
    }

    public void set_currentProject(BProject project) {
        if (project == null) {
            set_project_part("");
            _currentProject = null;
        } else if (_currentProject != project || !is_project_part_valid()) {
            _currentProject = project;
            set_project_part("");
            manage_project_part(project);
            BlueSVNLogger.getInstance().debug("--> project set to " + get_curProjectName(), LEVEL.MEDIUM);
        }
    }

    public static String get_projectName_from_projectPart(String project_part) {
        return project_part.replaceAll("\\.", "-").replaceAll("/", "_");
    }

    public String getStudentID() {
        return _bluej.getExtensionPropertyString(PreferencePane.STUDID_LABEL, "");
    }

    public String getStudentPassword() {
        return _bluej.getExtensionPropertyString(PreferencePane.STUDPWD_LABEL, "");
    }

    private String getConfigValue(String key) throws BlueSVNException {
        String value = _configuration.get(key);
        if (value.contains("%project_part")) {
            value = value.replaceAll("%project_part", get_projectRepPart());
        }
        if (value.contains("%id")) {
            value = value.replaceAll("%id", getStudentID());
        }
        return value;
    }

    private String getConfigValue(String key, String new_project_part) {
        String value = _configuration.get(key).replaceAll("%id", getStudentID()).replaceAll("%project_part", get_projectRepPart(new_project_part));
        return value;
    }

    public String getSVNHost() {
        return _configuration.get("bluesvn.prog2.repository_url");
    }

    public String getSVNStudentPath() throws BlueSVNException {
        return getConfigValue("bluesvn.prog2.repository_path_to_projects");
    }

    public String getSVNProjectSpecificSuffix() throws BlueSVNException {
        return getConfigValue("bluesvn.prog2.project_specific_suffix");
    }

    public String getSVNProjectSpecificSuffix(String project_part) {
        return getConfigValue("bluesvn.prog2.project_specific_suffix", project_part);
    }

    public String getSVNProjectSpecificPath() throws BlueSVNException {
        return getSVNStudentPath() + getSVNProjectSpecificSuffix();
    }

    public String getSVNProjectSpecificPath(String projectname) throws BlueSVNException {
        return getSVNStudentPath() + getSVNProjectSpecificSuffix(projectname);
    }

    public String getSupportMailAdress() throws BlueSVNException {
        return getConfigValue("bluesvn.prog2.support_mail");
    }

    public LEVEL getDebugLevel() throws BlueSVNException {
        String val = getConfigValue("bluesvn.prog2.debug_level").toLowerCase();
        if (val.compareTo("low") == 0) {
            return LEVEL.LOW;
        } else if (val.compareTo("medium") == 0) {
            return LEVEL.MEDIUM;
        } else if (val.compareTo("high") == 0) {
            return LEVEL.HIGH;
        } else if (val.compareTo("highest") == 0) {
            return LEVEL.HIGHEST;
        } else {
            return LEVEL.MEDIUM;
        }
    }

    public boolean is_project_part_valid() {
        return is_project_part_valid(_project_part);
    }

    public boolean connected() {
        InetAddress inetAddress = null;
        int port = 80;
        try {
            inetAddress = InetAddress.getByName("prog2.cs.uni-sb.de");
            long startTime = System.currentTimeMillis();
            Socket socket = new Socket(inetAddress, port);
            socket.close();
            long endTime = System.currentTimeMillis() - startTime;
            BlueSVNLogger.getInstance().debug("checked connection to server ... ok (port " + port + ", Time = " + endTime + " ms). \n" + "Host Address = " + inetAddress.getHostAddress() + "\n" + "Host Name = " + inetAddress.getHostName(), LEVEL.MEDIUM);
        } catch (UnknownHostException e) {
            BlueSVNLogger.getInstance().debug("Unbekannter Host. " + e.getMessage(), BlueSVNLogger.LEVEL.MEDIUM);
            return false;
        } catch (IOException e) {
            BlueSVNLogger.getInstance().debug("Cannot open Socket to course server. " + e.getMessage(), BlueSVNLogger.LEVEL.MEDIUM);
            return false;
        }
        return true;
    }

    public boolean is_project_part_valid(String new_project_part) {
        boolean isvalid = false;
        if (new_project_part != null && new_project_part.length() > 0) {
            try {
                SVNHandler handler = new SVNHandler();
                String path = getSVNProjectSpecificSuffix(new_project_part);
                BlueSVNLogger.getInstance().debug("looking at: " + path, BlueSVNLogger.LEVEL.LOW);
                handler.getFolderList(path);
                isvalid = true;
            } catch (BlueSVNException e) {
                BlueSVNLogger.getInstance().debug("Project part not valid: " + e.get_message(), e, BlueSVNLogger.LEVEL.LOW);
            }
        }
        return (isvalid);
    }

    public String getSVNUser() {
        return getStudentID();
    }

    public String getSVNPassword() {
        return getStudentPassword();
    }

    public BlueSVNLogView get_debugView() {
        return _debugger;
    }

    public BlueSVNLogView get_logView() {
        return _logger;
    }

    public String get_project_part() {
        return _project_part;
    }

    public void set_project_part(String _project_part) {
        this._project_part = _project_part;
        BlueSVNLogger.getInstance().debug("--> artifact id (project part/id) set to " + get_project_part(), LEVEL.MEDIUM);
    }
}
