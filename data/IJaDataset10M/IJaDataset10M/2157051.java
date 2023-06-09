package ch.unizh.ini.jaer.projects.stereo3D;

import net.sf.jaer.aemonitor.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Class to stream out packets of 3D reconstructed events in binary. The file format is
 <br>
 int32 x<br>
 int32 y<br>
 int32 disparity<br>
 int32 method<br>
 int32 lead_side<br>
 float value<br>
 int32 timestamp
 <br>
 repeated for the number of events in the file.
 
 * @author tobi
 */
public class AE3DOutputStream extends DataOutputStream {

    private static Logger log = Logger.getLogger("AE3DOutputStream");

    private boolean writeHeader = true;

    private boolean wrotePacket = false;

    private int type = Event3D.INDIRECT3D;

    /** 
     Creates a new instance of AEOutputStream.
     *
     @param os an output stream, e.g. from <code>new BufferedOutputStream(new FileOutputStream(File f)</code>.
     */
    public AE3DOutputStream(OutputStream os, int type) {
        super(os);
        this.type = type;
        if (writeHeader) {
            try {
                writeHeaderLine("!3D-AER-DAT1.0");
                writeHeaderLine(" This is a 3D AE data file - do not edit");
                writeHeaderLine(" Data format is short x, short y, short d, short method, short lead_side, float value, int32 timestamp 18 bytes total), repeated for each event");
                writeHeaderLine(" Timestamps tick is " + AEConstants.TICK_DEFAULT_US + " us");
                writeHeaderLine(" created " + new Date());
                writeHeaderLine(" type:" + type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes the packet out as sequence of address/timestamp's, just as they came as input from the device. The notion of a packet is discarded
     *to simplify later reading an input stream from the output stream result.
     *@param ae a raw addresse-event packet
     */
    public void writePacket(AEPacket3D ae) throws IOException {
        int n = ae.getNumEvents();
        if (type == Event3D.DIRECT3D) {
            int[] addrx = ae.getCoordinates_x();
            int[] addry = ae.getCoordinates_y();
            int[] addrz = ae.getCoordinates_z();
            float[] values = ae.getValues();
            int[] ts = ae.getTimestamps();
            for (int i = 0; i < n; i++) {
                writeShort((short) addrx[i]);
                writeShort((short) addry[i]);
                writeShort((short) addrz[i]);
                writeFloat(values[i]);
                writeInt(ts[i]);
            }
        } else {
            int[] addrx = ae.getCoordinates_x();
            int[] addry = ae.getCoordinates_y();
            int[] addrd = ae.getDisparities();
            int[] methods = ae.getMethods();
            int[] lead_sides = ae.getLead_sides();
            float[] values = ae.getValues();
            int[] ts = ae.getTimestamps();
            for (int i = 0; i < n; i++) {
                writeShort((short) addrx[i]);
                writeShort((short) addry[i]);
                writeShort((short) addrd[i]);
                writeShort((short) methods[i]);
                writeShort((short) lead_sides[i]);
                writeFloat(values[i]);
                writeInt(ts[i]);
            }
        }
        wrotePacket = true;
    }

    /**
     Writes a comment header line. Writes the string with prepended '#' and appended '\r\n'
     @param s the string
     */
    public void writeHeaderLine(String s) throws IOException {
        if (wrotePacket) {
            log.warning("already wrote a packet, not writing the header");
            return;
        }
        writeByte('#');
        writeBytes(s);
        writeByte('\r');
        writeByte('\n');
    }
}
