package org.verisign.joid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Properties;

public class Utils {

    static String readFileAsString(String fileName) throws Exception {
        BufferedReader input = null;
        try {
            InputStream resourceStream = Utils.class.getResourceAsStream(fileName);
            if (resourceStream == null) {
                throw new IllegalArgumentException("No such resource file: " + fileName);
            }
            input = new BufferedReader(new InputStreamReader(resourceStream));
            String line = null;
            StringBuffer contents = new StringBuffer();
            while ((line = input.readLine()) != null) {
                int n = line.indexOf('=');
                String name = URLEncoder.encode(line.substring(0, n), "UTF-8");
                String value = URLEncoder.encode(line.substring(n + 1, line.length()), "UTF-8");
                contents.append(name + "=" + value + "&");
            }
            String s = contents.toString();
            return s.substring(0, s.length());
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    static Properties readFile(String fileName) throws Exception {
        File f = new File(fileName);
        if (!f.exists()) {
            throw new IllegalArgumentException("No such file: " + f.getCanonicalPath());
        }
        FileInputStream in = null;
        try {
            Properties prop = new Properties();
            in = new FileInputStream(f);
            prop.load(in);
            return prop;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
