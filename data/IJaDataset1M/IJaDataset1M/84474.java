package org.xsocket.bayeux.http;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.xsocket.bayeux.http.BayeuxBroker.ConnectionType;
import org.xsocket.connection.http.IHttpExchange;
import dojox.cometd.Bayeux;
import dojox.cometd.Client;
import dojox.cometd.Message;

final class BayeuxClientProxy extends AbstractBayeuxClient {

    private static final Logger LOG = Logger.getLogger(BayeuxClientProxy.class.getName());

    private static final SimpleDateFormat DF = new SimpleDateFormat("HH:mm:ss,S");

    private String id = null;

    private boolean isBatchRunning = false;

    private final Object sendLock = new Object();

    private ArrayList<Message> incomingMessages = new ArrayList<Message>();

    private ArrayList<MessageImpl> outgoingMessages = new ArrayList<MessageImpl>();

    private BayeuxBroker bayeux = null;

    private boolean isJsonCommentFiltered = false;

    private ConnectionType connectionType = ConnectionType.LongPolling;

    private boolean isConnected = false;

    private final LinkedList<IHttpExchange> exchangeStack = new LinkedList<IHttpExchange>();

    private long timeLastMessageReceived = System.currentTimeMillis();

    public BayeuxClientProxy(BayeuxBroker bayeux, String id) {
        this.bayeux = bayeux;
        this.id = id;
    }

    void incMessageReceived() {
        timeLastMessageReceived = System.currentTimeMillis();
    }

    void addExchange(IHttpExchange exchange) {
        synchronized (sendLock) {
            exchangeStack.addLast(exchange);
            synchronized (outgoingMessages) {
                if (!outgoingMessages.isEmpty()) {
                    writeMessages();
                }
            }
        }
    }

    int getOpenConnectionsS2C() {
        return exchangeStack.size();
    }

    boolean isJsonCommentFiltered() {
        return isJsonCommentFiltered;
    }

    public String getId() {
        return id;
    }

    public boolean isLocal() {
        return false;
    }

    public void subscribe(String toChannel) {
        bayeux.subscribe(toChannel, this);
    }

    public void unsubscribe(String toChannel) {
        bayeux.unsubscribe(toChannel, this);
    }

    public void publish(String toChannel, Object data, String msgId) {
        bayeux.publish(this, toChannel, data, msgId);
    }

    public void deliver(Client toClient, Message msg) {
    }

    void deliver(String channelId, Object data, String msgId) {
        MessageImpl msg = MessageImpl.newInstance(channelId);
        msg.setId(msgId);
        msg.setClientId(getId());
        synchronized (outgoingMessages) {
            outgoingMessages.add(msg);
        }
        writeMessages();
    }

    public void deliver(Client arg0, String arg1, Object arg2, String arg3) {
    }

    public boolean hasMessages() {
        synchronized (incomingMessages) {
            return !incomingMessages.isEmpty();
        }
    }

    public List<Message> takeMessages() {
        List<Message> result = null;
        synchronized (incomingMessages) {
            result = incomingMessages;
            incomingMessages = new ArrayList<Message>();
        }
        return result;
    }

    public void startBatch() {
    }

    public void endBatch() {
    }

    private void writeMessages() {
        IHttpExchange exchange = null;
        List<MessageImpl> msgToSend = null;
        synchronized (sendLock) {
            if (outgoingMessages.isEmpty()) {
                return;
            }
            synchronized (outgoingMessages) {
                msgToSend = outgoingMessages;
                outgoingMessages = new ArrayList<MessageImpl>();
            }
            if (exchangeStack.isEmpty()) {
                return;
            } else {
                exchange = exchangeStack.removeFirst();
            }
        }
        bayeux.send(msgToSend, exchange, isJsonCommentFiltered);
    }

    private void close() {
        List<IHttpExchange> openExchanges = null;
        synchronized (sendLock) {
            openExchanges = (List<IHttpExchange>) exchangeStack.clone();
            exchangeStack.clear();
        }
        for (IHttpExchange httpExchange : openExchanges) {
            httpExchange.destroy();
        }
    }

    boolean handleHandeshake(MessageImpl request, MessageImpl response) {
        if (request.getChannel().equals((Bayeux.META_HANDSHAKE))) {
            String version = request.getVersion();
            String[] supportedConnectionTypes = request.getSupportedConnectionTypes();
            String minimumVersion = request.getMinimumVersion();
            String id = request.getId();
            if (request.getExt().containsKey("json-comment-filtered")) {
                isJsonCommentFiltered = (Boolean) request.getExt().get("json-comment-filtered");
            }
            response.setVersion("1.0");
            response.setSupportedConnectionTypes(new String[] { "long-polling", "callback-polling" });
            response.setClientId(getId());
            response.setSuccessful(true);
            if (id != null) {
                response.setId(id);
            }
            return true;
        } else {
            System.out.println("UNSUPPORTED CASE");
            return false;
        }
    }

    MessageImpl handleConnect(MessageImpl request) {
        boolean isSuccessful = true;
        connectionType = BayeuxBroker.resolve(request.getConnectionType());
        String requestId = request.getId();
        if (!isConnected) {
            MessageImpl response = MessageImpl.newInstance(request.getChannel());
            response.setClientId(getId());
            response.setSuccessful(true);
            if (requestId != null) {
                response.setId(requestId);
            }
            isConnected = true;
            return response;
        } else {
            return null;
        }
    }

    MessageImpl handleDisconnect(MessageImpl request) {
        boolean isSuccessful = true;
        String requestId = request.getId();
        MessageImpl response = MessageImpl.newInstance(request.getChannel());
        response.setClientId(getId());
        response.setSuccessful(true);
        if (requestId != null) {
            response.setId(requestId);
        }
        close();
        return response;
    }

    MessageImpl handleSubscribe(MessageImpl request) {
        String subscription = request.getSubscription();
        String id = request.getId();
        MessageImpl response = MessageImpl.newInstance(request.getChannel());
        response.setSubscription(subscription);
        response.setClientId(getId());
        response.setSuccessful(true);
        if (id != null) {
            response.setId(id);
        }
        bayeux.subscribe(subscription, this);
        return response;
    }

    MessageImpl handleUnsubscribe(MessageImpl request) {
        String subscription = request.getSubscription();
        String id = request.getId();
        MessageImpl response = MessageImpl.newInstance(request.getChannel());
        response.setSubscription(subscription);
        response.setClientId(getId());
        response.setSuccessful(true);
        if (id != null) {
            response.setId(id);
        }
        bayeux.unsubscribe(subscription, this);
        return response;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id);
        sb.append(" openS2C=" + exchangeStack.size() + " timeLastMessageReceived=" + DF.format(new Date(timeLastMessageReceived)) + " isJsonCommentFiltered=" + isJsonCommentFiltered);
        return sb.toString();
    }
}
