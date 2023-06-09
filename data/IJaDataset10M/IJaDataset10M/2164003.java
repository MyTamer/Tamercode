package org.jumpmind.symmetric.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.apache.commons.io.IOUtils;
import org.jumpmind.symmetric.Version;

public class JarBuilder {

    private File baseDir;

    private File[] sourceFiles;

    private File outputFile;

    public JarBuilder(File baseDir, File outputFile, File[] sourceFiles) {
        this.sourceFiles = sourceFiles;
        this.outputFile = outputFile;
        this.baseDir = baseDir;
    }

    public void build() throws IOException {
        this.outputFile.delete();
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, Version.version());
        if (outputFile.getParentFile() != null) {
            outputFile.getParentFile().mkdirs();
        }
        JarOutputStream target = new JarOutputStream(new FileOutputStream(outputFile), manifest);
        for (File file : sourceFiles) {
            add(file, target);
        }
        target.close();
    }

    private String massageJarEntryName(File source) {
        String name = source.getPath();
        if (baseDir != null && name.startsWith(baseDir.getPath()) && name.length() > baseDir.getPath().length()) {
            name = name.substring(baseDir.getPath().length() + 1);
        }
        return name.replace("\\", "/");
    }

    private void add(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = massageJarEntryName(source);
                if (name.trim().length() != 0) {
                    if (!name.endsWith("/")) {
                        name += "/";
                    }
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles()) {
                    add(nestedFile, target);
                }
            } else {
                JarEntry entry = new JarEntry(massageJarEntryName(source));
                entry.setTime(source.lastModified());
                target.putNextEntry(entry);
                in = new BufferedInputStream(new FileInputStream(source));
                byte[] buffer = new byte[1024];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1) {
                        break;
                    }
                    target.write(buffer, 0, count);
                }
                target.closeEntry();
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
