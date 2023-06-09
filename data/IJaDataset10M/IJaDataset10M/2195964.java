package com.aelitis.azureus.core.networkmanager;

import java.io.IOException;
import com.aelitis.azureus.core.peermanager.messaging.*;

/**
 * Inbound peer message queue.
 */
public interface IncomingMessageQueue {

    /**
   * Set the message stream decoder that will be used to decode incoming messages.
   * @param new_stream_decoder to use
   */
    public void setDecoder(MessageStreamDecoder new_stream_decoder);

    public MessageStreamDecoder getDecoder();

    /**
   * Get the percentage of the current message that has already been received.
   * @return percentage complete (0-99), or -1 if no message is currently being received
   */
    public int getPercentDoneOfCurrentMessage();

    /**
   * Receive (read) message(s) data from the underlying transport.
   * @param max_bytes to read
   * @return number of bytes received
   * @throws IOException on receive error
   */
    public int receiveFromTransport(int max_bytes) throws IOException;

    /**
   * Notifty the queue (and its listeners) of a message received externally on the queue's behalf.
   * @param message received externally
   */
    public void notifyOfExternallyReceivedMessage(Message message);

    /**
   * Manually resume processing (reading) incoming messages.
   * NOTE: Allows us to resume docoding externally, in case it was auto-paused internally.
   */
    public void resumeQueueProcessing();

    /**
   * Add a listener to be notified of queue events.
   * @param listener
   */
    public void registerQueueListener(MessageQueueListener listener);

    /**
   * Cancel queue event notification listener.
   * @param listener
   */
    public void cancelQueueListener(MessageQueueListener listener);

    /**
   * Destroy this queue.
   */
    public void destroy();

    /**
   * For notification of queue events.
   */
    public interface MessageQueueListener {

        /**
     * A message has been read from the connection.
     * @param message recevied
     * @return true if this message was accepted, false if not handled
     */
        public boolean messageReceived(Message message);

        /**
     * The given number of protocol (overhead) bytes read from the connection.
     * @param byte_count number of protocol bytes
     */
        public void protocolBytesReceived(int byte_count);

        /**
     * The given number of (piece) data bytes read from the connection.
     * @param byte_count number of data bytes
     */
        public void dataBytesReceived(int byte_count);

        public boolean isPriority();
    }
}
