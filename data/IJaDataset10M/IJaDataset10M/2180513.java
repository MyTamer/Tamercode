package br.com.sysmap.crux.widgets.client.dynatabs;

import br.com.sysmap.crux.core.client.screen.JSWindow;
import br.com.sysmap.crux.core.client.screen.Screen;
import br.com.sysmap.crux.widgets.client.event.openclose.BeforeCloseEvent;
import br.com.sysmap.crux.widgets.client.event.openclose.BeforeCloseHandler;
import br.com.sysmap.crux.widgets.client.event.openclose.HasBeforeCloseHandlers;
import br.com.sysmap.crux.widgets.client.util.FrameStateCallback;
import br.com.sysmap.crux.widgets.client.util.FrameUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Thiago da Rosa de Bustamante -
 *
 */
public abstract class AbstractTab extends Widget implements HasBeforeCloseHandlers {

    private String id;

    private String url;

    private Frame frame;

    private TabInternalJSObjects tabObjetcs;

    private boolean canClose = false;

    /**
	 * Constructor
	 * 
	 * @param id
	 * @param label
	 * @param url
	 * @param closeable
	 */
    protected AbstractTab(String id, String url) {
        this.id = id;
        this.url = url;
        this.frame = new Frame(Screen.appendDebugParameters(url));
        this.frame.setHeight("100%");
        Element frameElement = getElement();
        frameElement.setPropertyString("frameBorder", "no");
        frameElement.setPropertyString("border", "0");
        frameElement.setPropertyString("id", id + ".window");
        frameElement.setPropertyString("name", id + ".window");
        FrameUtils.registerStateCallback(frameElement, new FrameStateCallback() {

            public void onComplete() {
                canClose = true;
            }
        }, 20000);
        tabObjetcs = GWT.create(TabInternalJSObjectsImpl.class);
    }

    protected void executeWhenLoaded(final Command call) {
        FrameUtils.registerStateCallback(getElement(), new FrameStateCallback() {

            public void onComplete() {
                canClose = true;
                call.execute();
            }
        }, 20000);
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param call
	 * @param param
	 * @throws ModuleComunicationException
	 */
    @Deprecated
    public static <T> T invokeOnSiblingTab(String tabId, String call, Object param, Class<T> resultType) throws br.com.sysmap.crux.core.client.screen.ModuleComunicationException {
        return DynaTabsControllerInvoker.invokeOnSiblingTab(tabId, call, param, resultType);
    }

    /**
	 * @param call
	 * @param param
	 * @throws ModuleComunicationException
	 */
    @Deprecated
    public static void invokeOnSiblingTab(String tabId, String call, Object param) throws br.com.sysmap.crux.core.client.screen.ModuleComunicationException {
        DynaTabsControllerInvoker.invokeOnSiblingTab(tabId, call, param);
    }

    /**
	 * @return the url
	 */
    protected String getUrl() {
        return url;
    }

    /**
	 * @param url
	 */
    protected void setUrl(String url) {
        this.url = url;
    }

    /**
	 * 
	 * @return
	 */
    protected boolean canClose() {
        return canClose;
    }

    /**
	 * @return the frame
	 */
    protected Frame getFrame() {
        return frame;
    }

    @Override
    public Element getElement() {
        return frame.getElement();
    }

    public HandlerRegistration addBeforeCloseHandler(BeforeCloseHandler handler) {
        return addHandler(handler, BeforeCloseEvent.getType());
    }

    public JSWindow getInternalWindow() {
        return tabObjetcs.getTabWindow(getFrame().getElement().<IFrameElement>cast());
    }

    public Document getInternalDocument() {
        return tabObjetcs.getTabDocument(getFrame().getElement().<IFrameElement>cast());
    }
}
