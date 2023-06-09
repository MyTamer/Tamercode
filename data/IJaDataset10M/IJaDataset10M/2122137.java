package net.jini.event;

/**
 * An exception thrown when an event iterator becomes invalid. An event iterator
 * becomes invalid when:
 * <UL>
 * <LI>A subsequent call to {@link MailboxPullRegistration#getRemoteEvents
 * MailboxPullRegistration.getRemoteEvents} creates another iterator for the
 * same registration.
 * <LI>A subsequent call to
 * {@link MailboxRegistration#enableDelivery(net.jini.core.event.RemoteEventListener)
 * MailboxRegistration.enableDelivery} for the same registration switches from
 * iterator functionality to event listener functionality.
 * <LI>A subsequent call to {@link RemoteEventIterator#close()
 * RemoteEventIterator.close} occurs.
 * </UL>
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @since 2.1
 */
public class InvalidIteratorException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs an InvalidIteratorException with the specified detail message.
	 * 
	 * @param reason
	 *            a <code>String</code> containing a detail message
	 */
    public InvalidIteratorException(String reason) {
        super(reason);
    }

    /**
	 * Constructs an InvalidIteratorException with no detail message.
	 */
    public InvalidIteratorException() {
        super();
    }
}
