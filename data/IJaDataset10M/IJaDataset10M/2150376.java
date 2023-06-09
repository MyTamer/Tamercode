package org.marre.sms.transport;

import java.util.Properties;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;

/**
 * Interface for an asynchronous SMS transport.
 * 
 * This interface is used to transfer an smsj message to the sms server.
 * 
 * @author Markus Eriksson
 * @version $Id: SmsAsyncTransport.java 408 2006-02-28 19:36:39Z c95men $
 */
public interface SmsAsyncTransport {

    /**
     * Initializes and starts the asynchronous transport.
     * 
     * Please see each transport for information on what properties that are available.
     *
     * @throws SmsException Indicates a sms related problem.
     */
    void start(Properties props) throws SmsException;

    /**
     * Stops the asynchronous transport.
     *
     * @throws SmsException Indicates a sms related problem.
     */
    void stop() throws SmsException;

    /**
     * Adds an listener for mobile originated messages.
     * 
     * @param listener
     */
    void addIncomingSmsListener(IncomingSmsListener listener) throws SmsException;

    /**
     * Adds a listener for delivery reports.
     * 
     * @param listener
     */
    void addDeliveryReportListener(DeliveryReportListener listener) throws SmsException;

    /**
     * Sends an SmsMessage to the given destination.
     * 
     * This method returns a local identifier for the message. This messageid can then be used to query the 
     * status, cancel or replace the sent SMS. The format of the identifier is specific to the SmsTransport 
     * implementation. The SmsTransport implementation may decide how long the message id is valid. 
     * 
     * Depending on the implementation this method is blocking or non-blocking.
     * 
     * It is possible that the returned identifier is null. This indicates that the actual transport
     * doesn't handle message ids.
     * 
     * @param msg The Message to send
     * @param dest Destination address
     * @param sender Sender address
     * @return a local identifier for the message.
     * @throws SmsException Indicates a sms related problem.
     * @throws IOException Inidicates a failure to communicate with the SMS server.
     */
    String send(SmsMessage msg, SmsAddress dest, SmsAddress sender) throws SmsException;

    /**
     * Cancel the transmission of the given message id.
     * 
     * @param messageId Message id as returned from the send operation.
     */
    void cancel(String messageId) throws SmsException;
}
