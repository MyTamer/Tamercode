package libsidplay.sidtune;

import java.io.File;
import libsidplay.sidtune.SidTune.Clock;
import libsidplay.sidtune.SidTune.Compatibility;
import libsidplay.sidtune.SidTune.Model;

/**
 * An instance of this structure is used to transport values to and from SidTune
 * objects.<BR>
 * You must read (i.e. activate) sub-song specific information via:
 * 
 * <pre>
 * final SidTuneInfo tuneInfo = SidTune[songNumber];
 * final SidTuneInfo tuneInfo = SidTune.getInfo();
 * void SidTune.getInfo(tuneInfo);
 * </pre>
 * 
 * Consider the following fields as read-only, because the SidTune class does
 * not provide an implementation of:
 * 
 * <pre>
 *  boolean setInfo(final SidTuneInfo)
 * </pre>
 * 
 * Currently, the only way to get the class to accept values which are written
 * to these fields is by creating a derived class.
 * 
 * @author Ken H�ndel
 * 
 */
public class SidTuneInfo {

    public int loadAddr;

    public int initAddr;

    public int playAddr;

    public int songs;

    public int startSong = 1;

    /**
	 * The SID chip base address used by the sidtune.
	 */
    public int sidChipBase1 = 0xd400;

    /**
	 * The SID chip base address used by the sidtune.
	 * 
	 * 0xD??0 (2nd SID) or 0 (no 2nd SID)
	 */
    public int sidChipBase2;

    /**
	 * the one that has been initialized
	 */
    public int currentSong;

    /**
	 * intended speed, see top
	 */
    public Clock clockSpeed = Clock.UNKNOWN;

    /**
	 * First available page for relocation
	 */
    public short relocStartPage;

    /**
	 * Number of pages available for relocation
	 */
    public short relocPages;

    /**
	 * Sid Model required for this sid
	 */
    public Model sid1Model = Model.UNKNOWN;

    /**
	 * Sid Model required for this sid
	 */
    public Model sid2Model = Model.UNKNOWN;

    /**
	 * compatibility requirements
	 */
    public Compatibility compatibility = Compatibility.RSID_BASIC;

    /**
	 * Song title, credits, ... 0 = Title, 1 = Author, 2 = Copyright/Publisher
	 * 
	 * the number of available text info lines
	 */
    public short numberOfInfoStrings;

    /**
	 * holds text info from the format headers etc.
	 */
    public String infoString[] = new String[SidTune.SIDTUNE_MAX_CREDIT_STRINGS];

    /**
	 * Number of MUS comments (2 when STR also has comment)
	 */
    public int numberOfCommentStrings;

    /**
	 * Used to stash the MUS comment somewhere. Ignored by everything.
	 */
    public String[] commentString = new String[2];

    /**
	 * length of single-file sidtune file
	 */
    public int dataFileLen;

    /**
	 * length of raw C64 data without load address
	 */
    public int c64dataLen;

    /**
	 * path to sidtune files
	 */
    public File file;

    /**
	 * Calculated driver address for PSID driver (0 if none).
	 */
    public int determinedDriverAddr;

    /**
	 * Length of driver.
	 */
    public int determinedDriverLength;
}
