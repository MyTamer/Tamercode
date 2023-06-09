package org.xsocket.stream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.QAUtil;

/**
*
* @author grro@xsocket.org
*/
public final class NonBlockingConnectionPoolTest {

    private static final String DELIMITER = System.getProperty("line.separator");

    private static final int WORKER_COUNT = 10;

    private static final int LOOPS = 20;

    private static final String QUIT = "QUIT";

    private int running = 0;

    private final List<String> errors = new ArrayList<String>();

    @Test
    public void testSimple() throws Exception {
        errors.clear();
        final NonBlockingConnectionPool pool = new NonBlockingConnectionPool(500);
        final Server server = new Server();
        new Thread(server).start();
        for (int i = 0; i < WORKER_COUNT; i++) {
            Thread t = new Thread() {

                @Override
                public void run() {
                    running++;
                    INonBlockingConnection con = null;
                    for (int i = 0; i < LOOPS; i++) {
                        try {
                            con = pool.getNonBlockingConnection("127.0.0.1", server.getLocalPort());
                            con.setAutoflush(false);
                            con.write("test1" + DELIMITER);
                            con.write("test2" + DELIMITER);
                            con.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (con != null) {
                                try {
                                    con.close();
                                } catch (Exception ignore) {
                                }
                            }
                        }
                    }
                    running--;
                }
            };
            t.start();
        }
        do {
            QAUtil.sleep(200);
        } while (running > 0);
        for (String error : errors) {
            System.out.println("error: " + error);
        }
        Assert.assertTrue(errors.size() == 0);
        QAUtil.sleep(1000);
        Assert.assertTrue("pool size should be 0 not " + pool.getNumActive(), pool.getNumActive() == 0);
        pool.close();
        server.close();
    }

    @Test
    public void testIdleTimeout() throws Exception {
        errors.clear();
        final NonBlockingConnectionPool pool = new NonBlockingConnectionPool(500);
        final Server server = new Server();
        new Thread(server).start();
        INonBlockingConnection con = pool.getNonBlockingConnection("localhost", server.getLocalPort());
        con.setIdleTimeoutSec(1);
        Assert.assertTrue(con.isOpen());
        QAUtil.sleep(1500);
        Assert.assertTrue(!con.isOpen());
        pool.close();
        server.close();
    }

    private static class Server implements Runnable {

        private ExecutorService executorService = Executors.newCachedThreadPool();

        private boolean isRunning = true;

        private ServerSocket sso = null;

        Server() throws IOException {
            sso = new ServerSocket(0);
        }

        public void run() {
            while (isRunning) {
                try {
                    Socket s = sso.accept();
                    executorService.execute(new Worker(s));
                } catch (Exception e) {
                    if (isRunning) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public InetAddress getLocalAddress() {
            return sso.getInetAddress();
        }

        public int getLocalPort() {
            return sso.getLocalPort();
        }

        public void close() throws IOException {
            isRunning = false;
            sso.close();
        }
    }

    private static class Worker implements Runnable {

        private boolean isRunning = true;

        private LineNumberReader in = null;

        private PrintWriter out = null;

        private Socket s = null;

        Worker(Socket s) throws IOException {
            this.s = s;
            in = new LineNumberReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
        }

        public void run() {
            while (isRunning) {
                try {
                    String request = in.readLine();
                    if (request != null) {
                        if (request.equals(QUIT)) {
                            isRunning = false;
                        } else {
                            out.write("OK" + DELIMITER);
                            out.flush();
                        }
                    } else {
                        isRunning = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                in.close();
                out.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
