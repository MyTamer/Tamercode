package com.mekya.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import android.util.Log;
import com.mekya.interfaces.IAppManager;
import com.mekya.interfaces.ISocketOperator;

public class SocketOperator implements ISocketOperator {

    private static final String AUTHENTICATION_SERVER_ADDRESS = "http://localhost/android_im/";

    private int listeningPort = 0;

    private static final String HTTP_REQUEST_FAILED = null;

    private HashMap<InetAddress, Socket> sockets = new HashMap<InetAddress, Socket>();

    private ServerSocket serverSocket = null;

    private boolean listening;

    private IAppManager appManager;

    private class ReceiveConnection extends Thread {

        Socket clientSocket = null;

        public ReceiveConnection(Socket socket) {
            this.clientSocket = socket;
            SocketOperator.this.sockets.put(socket.getInetAddress(), socket);
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("exit") == false) {
                        appManager.messageReceived(inputLine);
                    } else {
                        clientSocket.shutdownInput();
                        clientSocket.shutdownOutput();
                        clientSocket.close();
                        SocketOperator.this.sockets.remove(clientSocket.getInetAddress());
                    }
                }
            } catch (IOException e) {
                Log.e("ReceiveConnection.run: when receiving connection ", "");
            }
        }
    }

    public SocketOperator(IAppManager appManager) {
        this.appManager = appManager;
    }

    public String sendHttpRequest(String params) {
        URL url;
        String result = new String();
        try {
            url = new URL(AUTHENTICATION_SERVER_ADDRESS);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.println(params);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result = result.concat(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.length() == 0) {
            result = HTTP_REQUEST_FAILED;
        }
        return result;
    }

    public boolean sendMessage(String message, String ip, int port) {
        try {
            String[] str = ip.split("\\.");
            byte[] IP = new byte[str.length];
            for (int i = 0; i < str.length; i++) {
                IP[i] = (byte) Integer.parseInt(str[i]);
            }
            Socket socket = getSocket(InetAddress.getByAddress(IP), port);
            if (socket == null) {
                return false;
            }
            PrintWriter out = null;
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public int startListening(int portNo) {
        listening = true;
        try {
            serverSocket = new ServerSocket(portNo);
            this.listeningPort = portNo;
        } catch (IOException e) {
            this.listeningPort = 0;
            return 0;
        }
        while (listening) {
            try {
                new ReceiveConnection(serverSocket.accept()).start();
            } catch (IOException e) {
                return 2;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e("Exception server socket", "Exception when closing server socket");
            return 3;
        }
        return 1;
    }

    public void stopListening() {
        this.listening = false;
    }

    private Socket getSocket(InetAddress addr, int portNo) {
        Socket socket = null;
        if (sockets.containsKey(addr) == true) {
            socket = sockets.get(addr);
            if (socket.isConnected() == false || socket.isInputShutdown() == true || socket.isOutputShutdown() == true || socket.getPort() != portNo) {
                sockets.remove(addr);
                try {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                    socket = new Socket(addr, portNo);
                    sockets.put(addr, socket);
                } catch (IOException e) {
                    Log.e("getSocket: when closing and removing", "");
                }
            }
        } else {
            try {
                socket = new Socket(addr, portNo);
                sockets.put(addr, socket);
            } catch (IOException e) {
                Log.e("getSocket: when creating", "");
            }
        }
        return socket;
    }

    public void exit() {
        for (Iterator<Socket> iterator = sockets.values().iterator(); iterator.hasNext(); ) {
            Socket socket = (Socket) iterator.next();
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) {
            }
        }
        sockets.clear();
        this.stopListening();
        appManager = null;
    }

    public int getListeningPort() {
        return this.listeningPort;
    }
}
