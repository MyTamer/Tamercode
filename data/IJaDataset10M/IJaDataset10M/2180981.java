package net.sourceforge.processdash.tool.export.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalResourceAutoLocator implements ExternalResourceMappingLoader {

    private Set importedDirs = new HashSet();

    public void addImportedPath(String importedDirectory) {
        String path = normalize(importedDirectory);
        if (getDataDirPortion(path) != null) importedDirs.add(path);
    }

    public Map load(File dir) {
        Map result = new HashMap();
        scanForDirectories(result, dir, "");
        scanForDirectories(result, dir.getParentFile(), "");
        return result;
    }

    private void scanForDirectories(Map result, File dir, String prefix) {
        if (dir == null) return;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) return;
        for (int i = 0; i < files.length; i++) {
            if (importedDirs.isEmpty()) return;
            if (!files[i].isDirectory()) continue;
            String thisPath = prefix + "/" + files[i].getName();
            if (searchForPath(result, files[i], thisPath)) {
                scanForDirectories(result, files[i], thisPath);
            }
        }
    }

    /** Check to see if the given directory might be relevant for remapping
     * purposes.
     * 
     * If the given directory IS one of the directories that needs remapping,
     * it will be added to the <code>result</code> Map.
     * 
     * Otherwise, if it looks like the given directory could possibly be the
     * parent of a directory that needs remapping, this will return true.
     */
    private boolean searchForPath(Map result, File dir, String onePath) {
        for (Iterator i = importedDirs.iterator(); i.hasNext(); ) {
            String importedPath = (String) i.next();
            if (importedPath.endsWith(onePath)) {
                result.put(importedPath, dir.getAbsolutePath());
                i.remove();
                return false;
            } else if (importedPath.indexOf(onePath) != -1) {
                return true;
            }
        }
        return false;
    }

    public static Map getGeneralizedMappings(Map mappings) {
        if (mappings == null || mappings.isEmpty()) return Collections.EMPTY_MAP;
        Map result = new HashMap();
        for (Iterator i = mappings.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            String origPath = normalize((String) e.getKey());
            String generalizedOrigPath = getDataDirPortion(origPath);
            if (generalizedOrigPath != null) result.put(generalizedOrigPath, e.getValue());
        }
        return result;
    }

    private static String getDataDirPortion(String path) {
        Matcher m = DATA_DIR_PATTERN.matcher(path);
        while (m.find()) if (mightBeDataDir(m.group(1))) {
            return path.substring(m.start());
        }
        return null;
    }

    private static String normalize(String path) {
        return path.replace('\\', '/');
    }

    public static boolean mightBeDataDir(String dirName) {
        try {
            long l = Long.parseLong(dirName, Character.MAX_RADIX);
            return (l > 1009868401000L && l < System.currentTimeMillis());
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static Pattern DATA_DIR_PATTERN = Pattern.compile("/data/([0-9a-z]+)($|/)", Pattern.CASE_INSENSITIVE);
}
