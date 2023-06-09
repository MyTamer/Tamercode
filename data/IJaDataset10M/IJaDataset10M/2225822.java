package net.sf.antcontrib.cpptasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.sf.antcontrib.cpptasks.compiler.ProcessorConfiguration;
import org.apache.tools.ant.BuildException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A history of the compiler and linker settings used to build the files in the
 * same directory as the history.
 *
 * @author Curt Arnold
 */
public final class TargetHistoryTable {

    /**
     * This class handles populates the TargetHistory hashtable in response to
     * SAX parse events
     */
    private class TargetHistoryTableHandler extends DefaultHandler {

        private final File baseDir;

        private String config;

        private final Hashtable history;

        private String output;

        private long outputLastModified;

        private final Vector sources = new Vector();

        /**
         * Constructor
         *
         * @param history
         *            hashtable of TargetHistory keyed by output name
         * @param outputFiles
         *            existing files in output directory
         */
        private TargetHistoryTableHandler(Hashtable history, File baseDir) {
            this.history = history;
            config = null;
            output = null;
            this.baseDir = baseDir;
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (qName.equals("target")) {
                if (config != null && output != null) {
                    File existingFile = new File(baseDir, output);
                    if (existingFile.exists()) {
                        long existingLastModified = existingFile.lastModified();
                        if (!CUtil.isSignificantlyBefore(existingLastModified, outputLastModified) && !CUtil.isSignificantlyAfter(existingLastModified, outputLastModified)) {
                            SourceHistory[] sourcesArray = new SourceHistory[sources.size()];
                            sources.copyInto(sourcesArray);
                            TargetHistory targetHistory = new TargetHistory(config, output, outputLastModified, sourcesArray);
                            history.put(output, targetHistory);
                        }
                    }
                }
                output = null;
                sources.setSize(0);
            } else {
                if (qName.equals("processor")) {
                    config = null;
                }
            }
        }

        /**
         * startElement handler
         */
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            if (qName.equals("source")) {
                String sourceFile = atts.getValue("file");
                long sourceLastModified = Long.parseLong(atts.getValue("lastModified"), 16);
                sources.addElement(new SourceHistory(sourceFile, sourceLastModified));
            } else {
                if (qName.equals("target")) {
                    sources.setSize(0);
                    output = atts.getValue("file");
                    outputLastModified = Long.parseLong(atts.getValue("lastModified"), 16);
                } else {
                    if (qName.equals("processor")) {
                        config = atts.getValue("signature");
                    }
                }
            }
        }
    }

    /** Flag indicating whether the cache should be written back to file. */
    private boolean dirty;

    /**
     * a hashtable of TargetHistory's keyed by output file name
     */
    private final Hashtable history = new Hashtable();

    /** The file the cache was loaded from. */
    private File historyFile;

    private File outputDir;

    private String outputDirPath;

    /**
     * Creates a target history table from history.xml in the output directory,
     * if it exists. Otherwise, initializes the history table empty.
     *
     * @param task
     *            task used for logging history load errors
     * @param outputDir
     *            output directory for task
     */
    public TargetHistoryTable(CCTask task, File outputDir) throws BuildException {
        if (outputDir == null) {
            throw new NullPointerException("outputDir");
        }
        if (!outputDir.isDirectory()) {
            throw new BuildException("Output directory is not a directory");
        }
        if (!outputDir.exists()) {
            throw new BuildException("Output directory does not exist");
        }
        this.outputDir = outputDir;
        try {
            outputDirPath = outputDir.getCanonicalPath();
        } catch (IOException ex) {
            outputDirPath = outputDir.toString();
        }
        historyFile = new File(outputDir, "history.xml");
        if (historyFile.exists()) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            try {
                SAXParser parser = factory.newSAXParser();
                parser.parse(historyFile, new TargetHistoryTableHandler(history, outputDir));
            } catch (Exception ex) {
                task.log("Error reading history.xml: " + ex.toString());
            }
        } else {
            try {
                FileOutputStream outputStream = new FileOutputStream(historyFile);
                byte[] historyElement = new byte[] { 0x3C, 0x68, 0x69, 0x73, 0x74, 0x6F, 0x72, 0x79, 0x2F, 0x3E };
                outputStream.write(historyElement);
                outputStream.close();
            } catch (IOException ex) {
                throw new BuildException("Can't create history file", ex);
            }
        }
    }

    public void commit() throws IOException {
        if (dirty) {
            Hashtable configs = new Hashtable(20);
            Enumeration elements = history.elements();
            while (elements.hasMoreElements()) {
                TargetHistory targetHistory = (TargetHistory) elements.nextElement();
                String configId = targetHistory.getProcessorConfiguration();
                if (configs.get(configId) == null) {
                    configs.put(configId, configId);
                }
            }
            FileOutputStream outStream = new FileOutputStream(historyFile);
            OutputStreamWriter outWriter;
            String encodingName = "UTF-8";
            try {
                outWriter = new OutputStreamWriter(outStream, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                outWriter = new OutputStreamWriter(outStream);
                encodingName = outWriter.getEncoding();
            }
            BufferedWriter writer = new BufferedWriter(outWriter);
            writer.write("<?xml version='1.0' encoding='");
            writer.write(encodingName);
            writer.write("'?>\n");
            writer.write("<history>\n");
            StringBuffer buf = new StringBuffer(200);
            Enumeration configEnum = configs.elements();
            while (configEnum.hasMoreElements()) {
                String configId = (String) configEnum.nextElement();
                buf.setLength(0);
                buf.append("   <processor signature=\"");
                buf.append(CUtil.xmlAttribEncode(configId));
                buf.append("\">\n");
                writer.write(buf.toString());
                elements = history.elements();
                while (elements.hasMoreElements()) {
                    TargetHistory targetHistory = (TargetHistory) elements.nextElement();
                    if (targetHistory.getProcessorConfiguration().equals(configId)) {
                        buf.setLength(0);
                        buf.append("      <target file=\"");
                        buf.append(CUtil.xmlAttribEncode(targetHistory.getOutput()));
                        buf.append("\" lastModified=\"");
                        buf.append(Long.toHexString(targetHistory.getOutputLastModified()));
                        buf.append("\">\n");
                        writer.write(buf.toString());
                        SourceHistory[] sourceHistories = targetHistory.getSources();
                        for (int i = 0; i < sourceHistories.length; i++) {
                            buf.setLength(0);
                            buf.append("         <source file=\"");
                            buf.append(CUtil.xmlAttribEncode(sourceHistories[i].getRelativePath()));
                            buf.append("\" lastModified=\"");
                            buf.append(Long.toHexString(sourceHistories[i].getLastModified()));
                            buf.append("\"/>\n");
                            writer.write(buf.toString());
                        }
                        writer.write("      </target>\n");
                    }
                }
                writer.write("   </processor>\n");
            }
            writer.write("</history>\n");
            writer.close();
            dirty = false;
        }
    }

    public TargetHistory get(String configId, String outputName) {
        TargetHistory targetHistory = (TargetHistory) history.get(outputName);
        if (targetHistory != null) {
            if (!targetHistory.getProcessorConfiguration().equals(configId)) {
                targetHistory = null;
            }
        }
        return targetHistory;
    }

    public void markForRebuild(Hashtable targetInfos) {
        Enumeration targetInfoEnum = targetInfos.elements();
        while (targetInfoEnum.hasMoreElements()) {
            markForRebuild((TargetInfo) targetInfoEnum.nextElement());
        }
    }

    public void markForRebuild(TargetInfo targetInfo) {
        if (!targetInfo.getRebuild()) {
            TargetHistory history = get(targetInfo.getConfiguration().toString(), targetInfo.getOutput().getName());
            if (history == null) {
                targetInfo.mustRebuild();
            } else {
                SourceHistory[] sourceHistories = history.getSources();
                File[] sources = targetInfo.getSources();
                if (sourceHistories.length != sources.length) {
                    targetInfo.mustRebuild();
                } else {
                    Hashtable sourceMap = new Hashtable(sources.length);
                    for (int i = 0; i < sources.length; i++) {
                        try {
                            sourceMap.put(sources[i].getCanonicalPath(), sources[i]);
                        } catch (IOException ex) {
                            sourceMap.put(sources[i].getAbsolutePath(), sources[i]);
                        }
                    }
                    for (int i = 0; i < sourceHistories.length; i++) {
                        String absPath = sourceHistories[i].getAbsolutePath(outputDir);
                        File match = (File) sourceMap.get(absPath);
                        if (match != null) {
                            try {
                                match = (File) sourceMap.get(new File(absPath).getCanonicalPath());
                            } catch (IOException ex) {
                                targetInfo.mustRebuild();
                                break;
                            }
                        }
                        if (match == null || match.lastModified() != sourceHistories[i].getLastModified()) {
                            targetInfo.mustRebuild();
                            break;
                        }
                    }
                }
            }
        }
    }

    public void update(ProcessorConfiguration config, String[] sources, VersionInfo versionInfo) {
        String configId = config.getIdentifier();
        String[] onesource = new String[1];
        String[] outputNames;
        for (int i = 0; i < sources.length; i++) {
            onesource[0] = sources[i];
            outputNames = config.getOutputFileNames(sources[i], versionInfo);
            for (int j = 0; j < outputNames.length; j++) {
                update(configId, outputNames[j], onesource);
            }
        }
    }

    private void update(String configId, String outputName, String[] sources) {
        File outputFile = new File(outputDir, outputName);
        if (outputFile.exists() && !CUtil.isSignificantlyBefore(outputFile.lastModified(), historyFile.lastModified())) {
            dirty = true;
            history.remove(outputName);
            SourceHistory[] sourceHistories = new SourceHistory[sources.length];
            for (int i = 0; i < sources.length; i++) {
                File sourceFile = new File(sources[i]);
                long lastModified = sourceFile.lastModified();
                String relativePath = CUtil.getRelativePath(outputDirPath, sourceFile);
                sourceHistories[i] = new SourceHistory(relativePath, lastModified);
            }
            TargetHistory newHistory = new TargetHistory(configId, outputName, outputFile.lastModified(), sourceHistories);
            history.put(outputName, newHistory);
        }
    }

    public void update(TargetInfo linkTarget) {
        File outputFile = linkTarget.getOutput();
        String outputName = outputFile.getName();
        if (outputFile.exists() && !CUtil.isSignificantlyBefore(outputFile.lastModified(), historyFile.lastModified())) {
            dirty = true;
            history.remove(outputName);
            SourceHistory[] sourceHistories = linkTarget.getSourceHistories(outputDirPath);
            TargetHistory newHistory = new TargetHistory(linkTarget.getConfiguration().getIdentifier(), outputName, outputFile.lastModified(), sourceHistories);
            history.put(outputName, newHistory);
        }
    }
}
