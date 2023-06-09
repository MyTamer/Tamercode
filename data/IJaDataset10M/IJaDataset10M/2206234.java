package org.gbif.ipt.action.manage;

import org.gbif.ipt.config.Constants;
import org.gbif.ipt.config.DataDir;
import org.gbif.ipt.config.JdbcSupport;
import org.gbif.ipt.model.Source;
import org.gbif.ipt.model.Source.FileSource;
import org.gbif.ipt.model.Source.SqlSource;
import org.gbif.ipt.service.AlreadyExistingException;
import org.gbif.ipt.service.ImportException;
import org.gbif.ipt.service.manage.SourceManager;
import org.gbif.utils.file.CompressionUtil;
import org.gbif.utils.file.CompressionUtil.UnsupportedCompressionType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class SourceAction extends ManagerBaseAction {

    @Inject
    private SourceManager sourceManager;

    @Inject
    private JdbcSupport jdbcSupport;

    @Inject
    private DataDir dataDir;

    private Source source;

    private String rdbms;

    private String problem;

    private File file;

    private String fileContentType;

    private String fileFileName;

    private boolean analyze = false;

    private List<String> columns;

    private List<String[]> peek;

    private int peekRows = 10;

    private int analyzeRows = 1000;

    public String add() throws IOException {
        boolean replace = false;
        File ftest = (File) session.get(Constants.SESSION_FILE);
        if (ftest != null) {
            file = ftest;
            fileFileName = (String) session.get(Constants.SESSION_FILE_NAME);
            fileContentType = (String) session.get(Constants.SESSION_FILE_CONTENT_TYPE);
            replace = true;
        }
        if (file != null) {
            if (StringUtils.endsWithIgnoreCase(fileContentType, "zip") || StringUtils.endsWithIgnoreCase(fileContentType, "gzip") || StringUtils.endsWithIgnoreCase(fileContentType, "compressed")) {
                try {
                    File tmpDir = dataDir.tmpDir();
                    List<File> files = CompressionUtil.decompressFile(tmpDir, file);
                    addActionMessage(getText("manage.source.compressed.files", new String[] { String.valueOf(files.size()) }));
                    if (!replace) {
                        for (File f : files) {
                            if (resource.getSource(f.getName()) != null) {
                                copyFileToOverwrite();
                                return INPUT;
                            }
                        }
                    }
                    for (File f : files) {
                        addTextFile(f, f.getName());
                    }
                    removeSessionFile();
                } catch (IOException e) {
                    log.error(e);
                    addActionError(getText("manage.source.filesystem.error", new String[] { e.getMessage() }));
                    return ERROR;
                } catch (UnsupportedCompressionType e) {
                    addActionError(getText("manage.source.unsupported.compression.format"));
                    return ERROR;
                }
            } else {
                if (!replace && resource.getSource(fileFileName) != null) {
                    copyFileToOverwrite();
                    return INPUT;
                }
                addTextFile(file, fileFileName);
                removeSessionFile();
            }
        }
        return SUCCESS;
    }

    private void addTextFile(File f, String filename) {
        boolean replaced = resource.getSource(filename) != null;
        try {
            source = sourceManager.add(resource, f, filename);
            saveResource();
            id = source.getName();
            if (replaced) {
                addActionMessage(getText("manage.source.replaced.existing", new String[] { source.getName() }));
            } else {
                addActionMessage(getText("manage.source.added.new", new String[] { source.getName() }));
            }
        } catch (ImportException e) {
            log.error("Cannot add source " + filename + ": " + e.getMessage(), e);
            addActionError(getText("manage.source.cannot.add", new String[] { filename, e.getMessage() }));
        }
    }

    public String cancelOverwrite() {
        removeSessionFile();
        return INPUT;
    }

    /**
   * Copy current file to same directory with different name and insert some temporal session variables.
   */
    private void copyFileToOverwrite() throws IOException {
        File fileNew = new File(file.getParent(), Source.normaliseName(file.getName()) + "-copied.tmp");
        FileUtils.copyFile(file, fileNew);
        session.put(Constants.SESSION_FILE, fileNew);
        session.put(Constants.SESSION_FILE_NAME, fileFileName);
        session.put(Constants.SESSION_FILE_CONTENT_TYPE, fileContentType);
    }

    @Override
    public String delete() {
        if (sourceManager.delete(resource, resource.getSource(id))) {
            addActionMessage(getText("manage.source.deleted", new String[] { id }));
            saveResource();
        } else {
            addActionMessage(getText("manage.source.deleted.couldnt", new String[] { id }));
        }
        return SUCCESS;
    }

    public List<String> getColumns() {
        return columns;
    }

    public FileSource getFileSource() {
        if (source instanceof FileSource) {
            return (FileSource) source;
        }
        return null;
    }

    public Map<String, String> getJdbcOptions() {
        return jdbcSupport.optionMap();
    }

    public boolean getLogExists() {
        return dataDir.sourceLogFile(resource.getShortname(), source.getName()).exists();
    }

    public List<String[]> getPeek() {
        return peek;
    }

    public int getPreviewSize() {
        return peekRows;
    }

    public String getProblem() {
        return problem;
    }

    public String getRdbms() {
        return rdbms;
    }

    public Source getSource() {
        return source;
    }

    public SqlSource getSqlSource() {
        if (source instanceof SqlSource) {
            return (SqlSource) source;
        }
        return null;
    }

    public String peek() {
        if (source == null) {
            return NOT_FOUND;
        }
        peek = sourceManager.peek(source, peekRows);
        columns = sourceManager.columns(source);
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
        if (id != null) {
            source = resource.getSource(id);
        } else if (file == null) {
            source = new Source.SqlSource();
            source.setResource(resource);
            ((SqlSource) source).setRdbms(jdbcSupport.get("mysql"));
        } else {
            source = new Source.FileSource();
            source.setResource(resource);
        }
    }

    /**
   * Remove any previous uploaded file in temporal directory.
   * And clean some session variables used to confirm overwrite action.
   */
    private void removeSessionFile() {
        File fileNew = (File) session.get(Constants.SESSION_FILE);
        if (fileNew != null && fileNew.exists()) {
            fileNew.delete();
        }
        session.remove(Constants.SESSION_FILE);
        session.remove(Constants.SESSION_FILE_NAME);
        session.remove(Constants.SESSION_FILE_CONTENT_TYPE);
    }

    @Override
    public String save() throws IOException {
        if (source != null && rdbms != null) {
            ((SqlSource) source).setRdbms(jdbcSupport.get(rdbms));
        }
        String result = INPUT;
        if (id != null && source != null) {
            if (this.analyze || !source.isReadable()) {
                problem = sourceManager.analyze(source);
            } else {
                result = SUCCESS;
            }
        } else {
            if (file == null) {
                try {
                    resource.addSource(source, false);
                    id = source.getName();
                    if (this.analyze || !source.isReadable()) {
                        problem = sourceManager.analyze(source);
                    }
                } catch (AlreadyExistingException e) {
                    addActionError(getText("manage.source.existing"));
                }
            } else {
                try {
                    source = sourceManager.add(resource, file, fileFileName);
                    if (resource.getSource(source.getName()) != null) {
                        addActionMessage(getText("manage.source.replaced.existing", new String[] { source.getName() }));
                    } else {
                        addActionMessage(getText("manage.source.added.new", new String[] { source.getName() }));
                    }
                } catch (ImportException e) {
                    log.error("Source error: " + e.getMessage(), e);
                    addActionError(getText("manage.source.error", new String[] { e.getMessage() }));
                }
            }
            id = source.getName();
        }
        saveResource();
        return result;
    }

    public void setAnalyze(String analyze) {
        if (StringUtils.trimToNull(analyze) != null) {
            this.analyze = true;
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setRdbms(String jdbc) {
        this.rdbms = jdbc;
        if (source != null && !source.isFileSource()) {
            ((SqlSource) source).setRdbms(jdbcSupport.get(rdbms));
        }
    }

    public void setRows(int previewSize) {
        this.peekRows = previewSize > 0 ? previewSize : 10;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String uploadLogo() {
        if (file != null) {
            for (String suffix : Constants.IMAGE_TYPES) {
                FileUtils.deleteQuietly(dataDir.resourceLogoFile(resource.getShortname(), suffix));
            }
            String type = "jpeg";
            if (fileContentType != null) {
                type = StringUtils.substringAfterLast(fileContentType, "/");
            }
            File logoFile = dataDir.resourceLogoFile(resource.getShortname(), type);
            try {
                FileUtils.copyFile(file, logoFile);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
        return INPUT;
    }

    @Override
    public void validateHttpPostOnly() {
        if (source != null) {
            if (StringUtils.trimToEmpty(source.getName()).length() == 0) {
                addFieldError("source.name", getText("validation.required", new String[] { getText("source.name") }));
            } else if (StringUtils.trimToEmpty(source.getName()).length() < 3) {
                addFieldError("source.name", getText("validation.short", new String[] { getText("source.name"), "3" }));
            } else if (id == null && resource.getSources().contains(source)) {
                addFieldError("source.name", getText("manage.source.unique"));
            }
            if (SqlSource.class.isInstance(source)) {
                SqlSource src = (SqlSource) source;
                if (StringUtils.trimToEmpty(src.getHost()).length() == 0 && rdbms != null && !rdbms.equalsIgnoreCase("odbc")) {
                    addFieldError("sqlSource.host", getText("validation.required", new String[] { getText("sqlSource.host") }));
                } else if (StringUtils.trimToEmpty(src.getHost()).length() < 2) {
                    addFieldError("sqlSource.host", getText("validation.short", new String[] { getText("sqlSource.host"), "2" }));
                }
                if (StringUtils.trimToEmpty(src.getDatabase()).length() == 0) {
                    addFieldError("sqlSource.database", getText("validation.required", new String[] { getText("sqlSource.database") }));
                } else if (StringUtils.trimToEmpty(src.getDatabase()).length() < 2) {
                    addFieldError("sqlSource.database", getText("validation.short", new String[] { getText("sqlSource.database"), "2" }));
                }
            }
        }
    }
}
