package com.emailer4j.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

public class JFileIO {

    private String errorMessage = "";

    private String filename = "";

    private String shortfilename = "";

    private final Logger logger = Logger.getLogger(JFileIO.class);

    public String getShortFilename() {
        return shortfilename;
    }

    private void setShortFilename(String filename) {
        this.shortfilename = filename;
    }

    public String getFilename() {
        return filename;
    }

    private void setFilename(String filename) {
        this.filename = filename;
    }

    private void setErrorMessage(String errmsg) {
        errorMessage = errmsg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean move_File(String srcFile, String destFile) {
        File from = new File(srcFile);
        File to = new File(destFile);
        boolean result = true;
        try {
            deleteFile(destFile);
            org.apache.commons.io.FileUtils.moveFile(from, to);
        } catch (IOException e) {
            logger.debug("move_File error :" + e.getMessage());
            setErrorMessage(e.getMessage());
            result = false;
        }
        return result;
    }

    public boolean move_FileToDirectory(String srcFile, String destDir, boolean createDestDir) {
        File from = new File(srcFile);
        File dir = new File(destDir);
        boolean result = true;
        try {
            org.apache.commons.io.FileUtils.moveFileToDirectory(from, dir, createDestDir);
        } catch (IOException e) {
            logger.debug("move_FileToDirectory error :" + e.getMessage());
            setErrorMessage(e.getMessage());
            result = false;
        }
        return result;
    }

    public String readFiletoString(String filename) {
        String result = "";
        File file = new File(filename);
        try {
            result = FileUtils.readFileToString(file);
        } catch (IOException e) {
            logger.debug("readFiletoString error :" + e.getMessage());
            setErrorMessage(e.getMessage());
        }
        return result;
    }

    public boolean deleteFile(String filename) {
        boolean result = false;
        File file = new File(filename);
        result = file.delete();
        return result;
    }

    public Boolean writeToDisk(String path, Document document, long dbTransactionRef, String filesuffix) {
        Boolean result = true;
        try {
            NumberFormat formatter = new DecimalFormat("0000000000");
            path = path.replace("\\", java.io.File.separator);
            path = path.replace("/", java.io.File.separator);
            if (path.length() > 0) {
                if (path.substring(path.length() - 1).equals(java.io.File.separator) == false) {
                    path = path + java.io.File.separator;
                }
            }
            String filename = path + formatter.format(dbTransactionRef) + filesuffix;
            setFilename(filename);
            setShortFilename(formatter.format(dbTransactionRef) + filesuffix);
            XMLSerializer serializer = new XMLSerializer();
            java.io.FileWriter fw = new java.io.FileWriter(filename);
            serializer.setOutputCharStream(fw);
            serializer.serialize(document);
            fw.close();
        } catch (Exception ex) {
            logger.debug("writeToDisk error :" + ex.getMessage());
            setErrorMessage("Error writing message to disk. " + ex.getMessage());
            result = false;
        }
        return result;
    }

    public Boolean writeToDisk(String path, String document, long dbTransactionRef, String filesuffix) {
        Boolean result = true;
        try {
            NumberFormat formatter = new DecimalFormat("0000000000");
            path = path.replace("\\", java.io.File.separator);
            path = path.replace("/", java.io.File.separator);
            if (path.length() > 0) {
                if (path.substring(path.length() - 1).equals(java.io.File.separator) == false) {
                    path = path + java.io.File.separator;
                }
            }
            String filename = path + formatter.format(dbTransactionRef) + filesuffix;
            setFilename(filename);
            setShortFilename(formatter.format(dbTransactionRef) + filesuffix);
            java.io.FileWriter fw = new java.io.FileWriter(filename);
            fw.write(document);
            fw.close();
        } catch (Exception ex) {
            logger.debug("writeToDisk error :" + ex.getMessage());
            setErrorMessage("Error writing message to disk. " + ex.getMessage());
            result = false;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<String> readFileLines(String filename) {
        List<String> result = null;
        File from = new File(filename);
        try {
            result = FileUtils.readLines(from);
        } catch (IOException e) {
            logger.debug("readFileLines error :" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Boolean writeFileLines(String filename, List<String> data) {
        Boolean result = true;
        File from = new File(filename);
        try {
            FileUtils.writeLines(from, data);
        } catch (IOException e) {
            result = false;
            logger.debug("writeFileLines error :" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
