package net.java.sip.communicator.service.protocol.event;

import net.java.sip.communicator.service.protocol.*;

/**
 * The <tt>CallParticipantSecurityOnEvent</tt> is triggered whenever a
 * communication with a given participant is going secure.
 * 
 * @author Werner Dittmann
 * @author Yana Stamcheva
 */
public class CallParticipantSecurityOnEvent extends CallParticipantSecurityStatusEvent {

    private final String securityString;

    private final boolean isVerified;

    private final String cipher;

    /**
     * The event constructor
     * 
     * @param callParticipant
     *            the call participant associated with this event
     * @param sessionType
     *            the type of the session, either AUDIO_SESSION or VIDEO_SESSION
     * @param cipher
     *            the cipher used for the encryption
     * @param securityString
     *            the security string (SAS)
     * @param isVerified
     *            indicates if the security string has already been verified
     */
    public CallParticipantSecurityOnEvent(CallParticipant callParticipant, int sessionType, String cipher, String securityString, boolean isVerified) {
        super(callParticipant, sessionType);
        this.cipher = cipher;
        this.securityString = securityString;
        this.isVerified = isVerified;
    }

    /**
     * Returns the <tt>CallParticipant</tt> for which this event occurred.
     * 
     * @return the <tt>CallParticipant</tt> for which this event occurred.
     */
    public CallParticipant getCallParticipant() {
        return (CallParticipant) getSource();
    }

    /**
     * Returns the cipher used for the encryption.
     * 
     * @return the cipher used for the encryption.
     */
    public String getCipher() {
        return cipher;
    }

    /**
     * Returns the security string.
     * 
     * @return the security string.
     */
    public String getSecurityString() {
        return securityString;
    }

    /**
     * Returns <code>true</code> if the security string was already verified
     * and <code>false</code> - otherwise.
     * 
     * @return <code>true</code> if the security string was already verified
     * and <code>false</code> - otherwise.
     */
    public boolean isSecurityVerified() {
        return isVerified;
    }
}
