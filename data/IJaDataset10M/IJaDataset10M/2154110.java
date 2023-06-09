package jmri.util;

import java.io.*;

/**
 * Small service class to read characters from a pipe
 * and post them to a JTextArea for display
 *
 * @author	Bob Jacobsen    Copyright (C) 2004
 * @version     $Revision: 1.7 $
 */
class PipeListener extends Thread {

    private PipedReader pr;

    private javax.swing.JTextArea ta;

    public PipeListener(PipedReader pr, javax.swing.JTextArea ta) {
        this.pr = pr;
        this.ta = ta;
    }

    public void run() {
        try {
            char[] c = new char[1];
            while (true) {
                try {
                    c[0] = (char) pr.read();
                    ta.append(new String(c));
                } catch (IOException ex) {
                    if (ex.getMessage().equals("Write end dead") || ex.getMessage().equals("Pipe broken")) {
                        synchronized (this) {
                            try {
                                wait(500);
                            } catch (InterruptedException exi) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    } else {
                        throw ex;
                    }
                }
            }
        } catch (IOException ex) {
            ta.append("PipeListener Exiting on IOException:" + ex);
        }
    }
}
