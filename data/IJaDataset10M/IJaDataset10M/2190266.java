package org.jdesktop.swingx.event;

import java.util.logging.Level;
import javax.swing.event.EventListenerList;

/**
 * A helper class which is an implementation of a message source and a progress
 * source. This class manages the listener list and has convenience methods to
 * fire MessageEvents and ProgressEvents.
 * 
 * @see MessageEvent
 * @see MessageSource
 * @see ProgressEvent
 * @see ProgressSource
 * @author Mark Davidson
 */
public class MessageSourceSupport implements MessageSource, ProgressSource {

    private EventListenerList listeners;

    private Object source;

    /**
     * Creates an MessageSource instance using the source object as the source
     * of events.
     */
    public MessageSourceSupport(Object source) {
        if (source == null) {
            throw new IllegalArgumentException("source Object cannot be null");
        }
        this.source = source;
        this.listeners = new EventListenerList();
    }

    public void addMessageListener(MessageListener l) {
        if (l != null) {
            listeners.add(MessageListener.class, l);
        }
    }

    public void removeMessageListener(MessageListener l) {
        if (l != null) {
            listeners.remove(MessageListener.class, l);
        }
    }

    public MessageListener[] getMessageListeners() {
        return (MessageListener[]) listeners.getListeners(MessageListener.class);
    }

    public void addProgressListener(ProgressListener l) {
        if (l != null) {
            listeners.add(ProgressListener.class, l);
        }
    }

    public void removeProgressListener(ProgressListener l) {
        if (l != null) {
            listeners.remove(ProgressListener.class, l);
        }
    }

    public ProgressListener[] getProgressListeners() {
        return (ProgressListener[]) listeners.getListeners(ProgressListener.class);
    }

    /**
     * Indicates that a long operation is staring. For a determinite progress
     * operation, the minimum value should be less than the maximum value. For
     * inderminate operations, set minimum equal to maximum.
     * 
     * @param minimum the minimum value of the progress operation
     * @param maximum the maximum value of the progress operation
     */
    public void fireProgressStarted(int minimum, int maximum) {
        fireProgressStarted(new ProgressEvent(source, minimum, maximum));
    }

    /**
     * Indicates that an increment of progress has occured.
     * 
     * @param progress total value of the progress operation. This value should
     *        be between the minimum and maximum values
     */
    public void fireProgressIncremented(int progress) {
        fireProgressIncremented(new ProgressEvent(source, progress));
    }

    /**
     * Indicates that a progress operation has completed
     */
    public void fireProgressEnded() {
        fireProgressEnded(new ProgressEvent(source));
    }

    /**
     * Create a SEVERE MessageEvent which represents an Exception.
     * <p>
     * <b>NOTE: This will create an event which is by default at Level.SEVERE</b>
     * 
     * @see java.util.logging.Level
     */
    public void fireException(Throwable t) {
        fireMessage(new MessageEvent(source, t, Level.SEVERE));
    }

    /**
     * Send a Level.INFO, non-timestamped message to the list of listeners.
     * 
     * @param message a string of text to send
     */
    public void fireMessage(String message) {
        fireMessage(message, Level.INFO);
    }

    /**
     * Send a non-timestamped message to the list of listeners.
     * 
     * @param value the contents of the message
     * @param level the level of message.
     */
    public void fireMessage(Object value, Level level) {
        fireMessage(value, level, 0L);
    }

    /**
     * Send a message to the list of listeners with a timestamp.
     * 
     * @param value the contents of the message
     * @param level the level of message
     * @param when timestamp of when this event occured
     */
    public void fireMessage(Object value, Level level, long when) {
        fireMessage(new MessageEvent(source, value, level, when));
    }

    /**
     * Send the MessageEvent to the list of listeners.
     * 
     * @param evt a non-null MessageEvent.
     */
    public void fireMessage(MessageEvent evt) {
        if (evt == null) {
            throw new IllegalArgumentException("the event should not be null");
        }
        MessageListener[] ls = getMessageListeners();
        for (int i = 0; i < ls.length; i++) {
            ls[i].message(evt);
        }
    }

    /**
     * Send the ProgessEvent to the list of listeners.
     * 
     * @param evt a non-null ProgressEvent
     */
    public void fireProgressIncremented(ProgressEvent evt) {
        if (evt == null) {
            throw new IllegalArgumentException("the event should not be null");
        }
        ProgressListener[] ls = getProgressListeners();
        for (int i = 0; i < ls.length; i++) {
            ls[i].progressIncremented(evt);
        }
    }

    /**
     * Send the ProgessEvent to the list of listeners.
     * 
     * @param evt a non-null ProgressEvent
     */
    public void fireProgressStarted(ProgressEvent evt) {
        if (evt == null) {
            throw new IllegalArgumentException("the event should not be null");
        }
        ProgressListener[] ls = getProgressListeners();
        for (int i = 0; i < ls.length; i++) {
            ls[i].progressStarted(evt);
        }
    }

    /**
     * Send the ProgessEvent to the list of listeners.
     * 
     * @param evt a non-null ProgressEvent
     */
    public void fireProgressEnded(ProgressEvent evt) {
        if (evt == null) {
            throw new IllegalArgumentException("the event should not be null");
        }
        ProgressListener[] ls = getProgressListeners();
        for (int i = 0; i < ls.length; i++) {
            ls[i].progressEnded(evt);
        }
    }
}
