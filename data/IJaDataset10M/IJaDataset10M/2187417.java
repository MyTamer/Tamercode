package etch.examples.chat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import etch.bindings.java.support.ServerFactory;
import etch.util.Log;
import etch.util.core.io.Transport;

/**
 * Main program for ChatServer. This program makes a listener to accept
 * connections from MainChatClient.
 */
public class MainChatListener implements ChatHelper.ChatServerFactory {

    /**
	 * Main program for ChatServer.
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        Log.addSink(null);
        Log.report("MainChatListener");
        String uri = "tcp://0.0.0.0:4005";
        if (args.length > 0) {
            uri = args[0];
        } else if (args.length != 0) {
            System.out.println("usage: MainChatListener [uri]");
            System.exit(1);
            return;
        }
        MainChatListener implFactory = new MainChatListener();
        Transport<ServerFactory> listener = ChatHelper.newListener(uri, null, implFactory);
        listener.transportControl(Transport.START_AND_WAIT_UP, 4000);
    }

    private final Map<String, ImplChatServer> whoIsOnline = Collections.synchronizedMap(new HashMap<String, ImplChatServer>());

    public ChatServer newChatServer(RemoteChatClient client) {
        return new ImplChatServer(client, whoIsOnline);
    }
}
