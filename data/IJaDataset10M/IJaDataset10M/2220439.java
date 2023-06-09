package org.jsynthlib.synthdrivers.EnsoniqESQ1;

import java.util.prefs.Preferences;
import org.jsynthlib.core.Device;

/**
 *
 * @author  Gerrit Gehnen
 */
public class EnsoniqESQ1Device extends Device {

    private static final String infoText = "Whenever JSynthLib sends a Patch to the ESQ, the synthesizer will go to a screen where you must hit " + "'exit' on the front panel before you can send another patch. This is kind of annoying, so if anyone " + "knows a way around this let me know. The other ESQ librarians I checked out had the same problem, so " + "it may be impossible";

    /** Creates new EnsoniqESQ1Device */
    public EnsoniqESQ1Device() {
        super("Ensoniq", "ESQ-1", "F07E..06020F0200...........F7", infoText, "Brian Klock");
    }

    /** Constructor for for actual work. */
    public EnsoniqESQ1Device(final Preferences prefs) {
        this();
        this.prefs = prefs;
        addDriver(new EnsoniqESQ1BankDriver(this));
        addDriver(new EnsoniqESQ1SingleDriver(this));
    }
}
