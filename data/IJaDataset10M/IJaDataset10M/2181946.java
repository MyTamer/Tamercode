package org.jlibrtp.demo;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.jlibrtp.DataFrame;
import org.jlibrtp.Participant;
import org.jlibrtp.RTPAppIntf;
import org.jlibrtp.RTPSession;

/**
 * @author Arne Kepp
 */
public class SoundSenderDemo implements RTPAppIntf {

    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(SoundSenderDemo.class.getName());

    public RTPSession rtpSession = null;

    static int pktCount = 0;

    static int dataCount = 0;

    private String filename;

    private final int EXTERNAL_BUFFER_SIZE = 1024;

    SourceDataLine auline;

    private Position curPosition;

    boolean local;

    enum Position {

        LEFT, RIGHT, NORMAL
    }

    ;

    public SoundSenderDemo(boolean isLocal) {
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(16386);
            rtcpSocket = new DatagramSocket(16387);
        } catch (Exception e) {
            System.out.println("RTPSession failed to obtain port");
        }
        rtpSession = new RTPSession(rtpSocket, rtcpSocket);
        rtpSession.registerRTPSession(this, null, null);
        System.out.println("CNAME: " + rtpSession.CNAME());
        this.local = isLocal;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "]" + args[i]);
        }
        if (args.length == 0) {
            args = new String[4];
            args[1] = "127.0.0.1";
            args[0] = "demo/test.wav";
            args[2] = "16384";
            args[3] = "16385";
        }
        SoundSenderDemo aDemo = new SoundSenderDemo(false);
        Participant p = new Participant(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[2]) + 1);
        aDemo.rtpSession.addParticipant(p);
        aDemo.filename = args[0];
        aDemo.run();
        System.out.println("pktCount: " + pktCount);
    }

    public void receiveData(DataFrame dummy1, Participant dummy2) {
    }

    public void userEvent(int type, Participant[] participant) {
    }

    public int frameSize(int payloadType) {
        return 1;
    }

    public void run() {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("-> Run()");
        }
        File soundFile = new File(filename);
        if (!soundFile.exists()) {
            System.err.println("Wave file not found: " + filename);
            return;
        }
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        AudioFormat.Encoding encoding = new AudioFormat.Encoding("PCM_SIGNED");
        AudioFormat format = new AudioFormat(encoding, ((float) 8000.0), 16, 1, 2, ((float) 8000.0), false);
        System.out.println(format.toString());
        if (!this.local) {
            auline = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            try {
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (auline.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
                if (this.curPosition == Position.RIGHT) pan.setValue(1.0f); else if (this.curPosition == Position.LEFT) pan.setValue(-1.0f);
            }
            auline.start();
        }
        int nBytesRead = 0;
        byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
        long start = System.currentTimeMillis();
        try {
            while (nBytesRead != -1 && pktCount < 200) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    rtpSession.sendData(abData);
                    auline.write(abData, 0, abData.length);
                    pktCount++;
                }
                if (pktCount == 100) {
                    Enumeration<Participant> iter = this.rtpSession.getParticipants();
                    Participant p = null;
                    while (iter.hasMoreElements()) {
                        p = iter.nextElement();
                        String name = "name";
                        byte[] nameBytes = name.getBytes();
                        String data = "abcd";
                        byte[] dataBytes = data.getBytes();
                        int ret = rtpSession.sendRTCPAppPacket(p.getSSRC(), 0, nameBytes, dataBytes);
                        System.out.println("!!!!!!!!!!!! ADDED APPLICATION SPECIFIC " + ret);
                        continue;
                    }
                    if (p == null) System.out.println("No participant with SSRC available :(");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Time: " + (System.currentTimeMillis() - start) / 1000 + " s");
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
        this.rtpSession.endSession();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("<- Run()");
        }
    }
}
