package org.apache.tools.ant.taskdefs.optional.perforce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;

/**
 * base class to manage streams around the execution of the Perforce
 * command line client
 */
public abstract class P4HandlerAdapter implements P4Handler {

    String p4input = "";

    private PumpStreamHandler myHandler = null;

    /**
     *  set any data to be written to P4's stdin
     *  @param p4Input the text to write to P4's stdin
     */
    public void setOutput(String p4Input) {
        this.p4input = p4Input;
    }

    /**
     * subclasses of P4HandlerAdapter must implement this routine
     * processing of one line of stdout or of stderr
     * @param line line of stdout or stderr to process
     */
    public abstract void process(String line);

    /**
     * this routine gets called by the execute routine of the Execute class
     * it connects the PumpStreamHandler to the input/output/error streams of the process.
     * @throws BuildException if there is a error.
     * @see org.apache.tools.ant.taskdefs.Execute#execute
     */
    public void start() throws BuildException {
        if (p4input != null && p4input.length() > 0) {
            myHandler = new PumpStreamHandler(new P4OutputStream(this), new P4OutputStream(this), new ByteArrayInputStream(p4input.getBytes()));
        } else {
            myHandler = new PumpStreamHandler(new P4OutputStream(this), new P4OutputStream(this));
        }
        myHandler.setProcessInputStream(os);
        myHandler.setProcessErrorStream(es);
        myHandler.setProcessOutputStream(is);
        myHandler.start();
    }

    /**
     * stops the processing of streams
     * called from P4Base#execP4Command(String command, P4Handler handler)
     * @see P4Base#execP4Command(String, P4Handler)
     */
    public void stop() {
        if (myHandler != null) {
            myHandler.stop();
        }
    }

    OutputStream os;

    InputStream is;

    InputStream es;

    /**
     * connects the handler to the input stream into Perforce
     * used indirectly by tasks requiring to send specific standard input
     * such as p4label, p4change
     * @param os the stream bringing input to the p4 executable
     * @throws IOException under unknown circumstances
     */
    public void setProcessInputStream(OutputStream os) throws IOException {
        this.os = os;
    }

    /**
     * connects the handler to the stderr of the Perforce process
     * @param is stderr coming from Perforce
     * @throws IOException under unknown circumstances
     */
    public void setProcessErrorStream(InputStream is) throws IOException {
        this.es = is;
    }

    /**
     * connects the handler to the stdout of the Perforce process
     * @param is stdout coming from Perforce
     * @throws IOException under unknown circumstances
     */
    public void setProcessOutputStream(InputStream is) throws IOException {
        this.is = is;
    }
}
