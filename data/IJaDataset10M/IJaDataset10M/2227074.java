package org.xsocket.stream;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

/**
 * Implements a handler chain. Each handler of the chain will be called (in the registering order),
 * until one handler signal by the return value true, that the event has been handled. In 
 * this case the remaing handlers will not be called.  
 *   
 * <br>
 * E.g. 
 * <pre>
 *   ...
 *   HandlerChain tcpBasedSpamfilter = new HandlerChain();
 *   tcpBasedSpamfilter.addLast(new BlackIPFilter()); 
 *   tcpBasedSpamfilter.addLast(new FirstConnectRefuseFilter());
 *   
 *   HandlerChain mainChain = new HandlerChain();
 *   mainChain.addLast(tcpBasedSpamfilter);
 *   mainChain.addLast(new SmtpProtocolHandler());
 *   
 *   IServer smtpServer = new Server(port, mainChain);
 *   StreamUtils.start(server);
 *   ...
 *   
 * </pre>

 * 
 * 
 * @author grro@xsocket.org
 */
public final class HandlerChain implements IHandler, IConnectHandler, IDisconnectHandler, IDataHandler, ITimeoutHandler, IConnectionScoped, org.xsocket.ILifeCycle {

    private static final Logger LOG = Logger.getLogger(HandlerChain.class.getName());

    private IHandler[] handlers = new IHandler[0];

    private Integer[] connectionScopedIndex = new Integer[0];

    private Integer[] connectHandlerChain = new Integer[0];

    private Integer[] disconnectHandlerChain = new Integer[0];

    private Integer[] dataHandlerChain = new Integer[0];

    private Integer[] timeoutHandlerChain = new Integer[0];

    private Integer[] lifeCycleChain = new Integer[0];

    private List<HandlerChain> enclosingChains = new ArrayList<HandlerChain>();

    @Resource
    private IServerContext ctx = null;

    /**
	 * constructor 
	 * 
	 */
    public HandlerChain() {
    }

    /**
	 * constructor 
	 * 
	 * @param handlers the initial handlers 
	 */
    public HandlerChain(List<IHandler> handlers) {
        for (IHandler hdl : handlers) {
            addLast(hdl);
        }
    }

    /**
	 * add a handler to the end og the chain
	 * 
	 * @param handler the handler to add
	 */
    public void addLast(IHandler handler) {
        int pos = handlers.length;
        handlers = incArray(handlers, handler);
        if (handler instanceof IConnectHandler) {
            connectHandlerChain = incArray(connectHandlerChain, pos);
        }
        if (handler instanceof IDisconnectHandler) {
            disconnectHandlerChain = incArray(disconnectHandlerChain, pos);
        }
        if (handler instanceof IDataHandler) {
            dataHandlerChain = incArray(dataHandlerChain, pos);
        }
        if (handler instanceof ITimeoutHandler) {
            timeoutHandlerChain = incArray(timeoutHandlerChain, pos);
        }
        if (handler instanceof org.xsocket.ILifeCycle) {
            lifeCycleChain = incArray(lifeCycleChain, pos);
        }
        if (handler instanceof HandlerChain) {
            ((HandlerChain) handler).addEnclosingChain(this);
        }
        if (handler instanceof IConnectionScoped) {
            updateScope(pos, true);
        } else {
            updateScope(pos, false);
        }
    }

    private void addEnclosingChain(HandlerChain enclosingChain) {
        if (enclosingChains == null) {
            enclosingChains = new ArrayList<HandlerChain>();
        }
        enclosingChains.add(enclosingChain);
    }

    private void updateScope(IHandler handler, boolean isConnectionScoped) {
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] == handler) {
                updateScope(i, isConnectionScoped);
                return;
            }
        }
    }

    private void updateScope(int pos, boolean isConnectionScoped) {
        if (isConnectionScoped) {
            connectionScopedIndex = incArray(connectionScopedIndex, pos);
            if (enclosingChains != null) {
                for (HandlerChain chain : enclosingChains) {
                    chain.updateScope(this, true);
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void onInit() {
        for (IHandler handler : handlers) {
            Field[] fields = handler.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Resource.class) != null) {
                    field.setAccessible(true);
                    try {
                        field.set(handler, ctx);
                    } catch (IllegalAccessException iae) {
                        LOG.warning("could not set HandlerContext for attribute " + field.getName() + ". Reason " + iae.toString());
                    }
                }
            }
        }
        for (Integer pos : lifeCycleChain) {
            ((org.xsocket.ILifeCycle) handlers[pos]).onInit();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void onDestroy() {
        for (Integer pos : lifeCycleChain) {
            ((org.xsocket.ILifeCycle) handlers[pos]).onDestroy();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean onConnect(INonBlockingConnection connection) throws IOException {
        for (Integer pos : connectHandlerChain) {
            boolean result = ((IConnectHandler) handlers[pos]).onConnect(connection);
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
        for (Integer pos : disconnectHandlerChain) {
            boolean result = ((IDisconnectHandler) handlers[pos]).onDisconnect(connection);
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean onData(INonBlockingConnection connection) throws IOException {
        for (Integer pos : dataHandlerChain) {
            boolean result = ((IDataHandler) handlers[pos]).onData(connection);
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
        for (Integer pos : timeoutHandlerChain) {
            boolean result = ((ITimeoutHandler) handlers[pos]).onConnectionTimeout(connection);
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean onIdleTimeout(INonBlockingConnection connection) throws IOException {
        for (Integer pos : timeoutHandlerChain) {
            boolean result = ((ITimeoutHandler) handlers[pos]).onIdleTimeout(connection);
            if (result == true) {
                return true;
            }
        }
        return false;
    }

    /**
	 * only for test purposes
	 * 
	 * @param pos the postion of the handler
	 * @return handler 
	 */
    IHandler getHandler(int pos) {
        return handlers[pos];
    }

    ;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object clone() throws CloneNotSupportedException {
        if (connectionScopedIndex.length > 0) {
            HandlerChain clone = (HandlerChain) super.clone();
            clone.handlers = handlers.clone();
            for (int i = 0; i < connectionScopedIndex.length; i++) {
                int position = connectionScopedIndex[i];
                clone.handlers[position] = (IHandler) ((IConnectionScoped) handlers[position]).clone();
            }
            return clone;
        } else {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest(this.getClass().getSimpleName() + " does not contain connection-specific handlers. return current instance as clone");
            }
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] incArray(T[] original, T newElement) {
        T[] newArray = (T[]) Arrays.copyOf(original, original.length + 1, original.getClass());
        newArray[original.length] = newElement;
        return newArray;
    }
}
