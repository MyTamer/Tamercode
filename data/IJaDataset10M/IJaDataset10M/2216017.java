package util;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.*;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * 
 * @author reza
 */
public class InternetExamples implements Runnable {

    private Thread thisthread;

    private String word;

    private DefaultListModel examplemodel;

    boolean stopped;

    JButton QueryBT;

    /** Creates a new instance of InternetExamples */
    public InternetExamples() {
    }

    /**
     *This method is used to gather examples for a word from the web.
     */
    public void init(String word, DefaultListModel examplemodel, JButton button) {
        this.word = word;
        this.examplemodel = examplemodel;
        QueryBT = button;
    }

    public void run() {
        String s;
        s = "";
        BufferedReader in = null;
        try {
            URL url = new URL("http://sara.natcorp.ox.ac.uk/cgi-bin/saraWeb?qy=" + word);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while (((str = in.readLine()) != null) && (!stopped)) {
                s = s + str;
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
            QueryBT.setEnabled(true);
            try {
                if (in != null) in.close();
            } catch (Exception e1) {
            }
            return;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e.getMessage());
            QueryBT.setEnabled(true);
            try {
                if (in != null) in.close();
            } catch (Exception e1) {
            }
            return;
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e1) {
            }
        }
        Pattern pattern = Pattern.compile("/b>(.+?) <", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        ArrayList exmpls = new ArrayList();
        while ((matcher.find()) && (!stopped)) {
            String stemp = matcher.group(1);
            stemp = stemp.replaceAll(word, "<b>" + word + "</b>");
            exmpls.add("<html>" + stemp + "</html>");
        }
        final String[] exexmpls = ((String[]) exmpls.toArray(new String[0]));
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < exexmpls.length; i++) {
                    examplemodel.addElement(exexmpls[i]);
                }
            }
        });
        QueryBT.setEnabled(true);
    }

    public void start() {
        QueryBT.setEnabled(false);
        stopped = false;
        thisthread = new Thread(this);
        thisthread.start();
    }

    public void stop() {
        stopped = true;
        thisthread = null;
    }
}
