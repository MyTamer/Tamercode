package org.gudy.azureus2.pluginsimpl.local.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gudy.azureus2.core3.internat.MessageText;
import org.gudy.azureus2.core3.util.AEMonitor;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.pluginsimpl.local.deprecate.PluginDeprecation;
import org.gudy.azureus2.pluginsimpl.local.ui.SWT.SWTManagerImpl;
import org.gudy.azureus2.pluginsimpl.local.ui.model.BasicPluginConfigModelImpl;
import org.gudy.azureus2.pluginsimpl.local.ui.model.BasicPluginViewModelImpl;
import org.gudy.azureus2.pluginsimpl.local.ui.menus.MenuManagerImpl;
import org.gudy.azureus2.pluginsimpl.local.ui.tables.TableManagerImpl;
import org.gudy.azureus2.plugins.PluginConfig;
import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.PluginView;
import org.gudy.azureus2.plugins.logging.LoggerChannel;
import org.gudy.azureus2.plugins.torrent.Torrent;
import org.gudy.azureus2.plugins.ui.*;
import org.gudy.azureus2.plugins.ui.menus.MenuManager;
import org.gudy.azureus2.plugins.ui.SWT.SWTManager;
import org.gudy.azureus2.plugins.ui.config.ConfigSection;
import org.gudy.azureus2.plugins.ui.model.BasicPluginConfigModel;
import org.gudy.azureus2.plugins.ui.model.BasicPluginViewModel;
import org.gudy.azureus2.plugins.ui.model.PluginConfigModel;
import org.gudy.azureus2.plugins.ui.model.PluginViewModel;
import org.gudy.azureus2.plugins.ui.tables.TableManager;
import com.aelitis.azureus.core.util.CopyOnWriteList;
import com.aelitis.azureus.ui.IUIIntializer;

/**
 * @author parg
 *
 */
public class UIManagerImpl implements UIManager {

    protected static AEMonitor class_mon = new AEMonitor("UIManager:class");

    protected static boolean initialisation_complete;

    protected static CopyOnWriteList ui_listeners = new CopyOnWriteList();

    protected static CopyOnWriteList ui_event_listeners = new CopyOnWriteList();

    protected static List<UIInstanceFactory> ui_factories = new ArrayList<UIInstanceFactory>();

    protected static List<UIManagerEventAdapter> ui_event_history = new ArrayList<UIManagerEventAdapter>();

    protected static List<BasicPluginConfigModel> configModels = new ArrayList<BasicPluginConfigModel>();

    protected PluginInterface pi;

    protected PluginConfig plugin_config;

    protected String key_prefix;

    protected TableManager table_manager;

    protected MenuManager menu_manager;

    public UIManagerImpl(PluginInterface _pi) {
        pi = _pi;
        plugin_config = pi.getPluginconfig();
        key_prefix = plugin_config.getPluginConfigKeyPrefix();
        table_manager = new TableManagerImpl(this);
        menu_manager = new MenuManagerImpl(this);
    }

    public PluginInterface getPluginInterface() {
        return (pi);
    }

    public BasicPluginViewModel getBasicPluginViewModel(String name) {
        return (createBasicPluginViewModel(name));
    }

    public PluginView createPluginView(PluginViewModel model) {
        try {
            return (SWTManagerImpl.getSingleton().createPluginView(model));
        } catch (Throwable e) {
            e.printStackTrace();
            return (null);
        }
    }

    public BasicPluginViewModel createBasicPluginViewModel(String name) {
        final BasicPluginViewModel model = new BasicPluginViewModelImpl(this, name);
        fireEvent(pi, UIManagerEvent.ET_PLUGIN_VIEW_MODEL_CREATED, model);
        return (model);
    }

    public void destroy(final BasicPluginViewModel model) {
        fireEvent(pi, UIManagerEvent.ET_PLUGIN_VIEW_MODEL_DESTROYED, model);
    }

    public BasicPluginConfigModel createBasicPluginConfigModel(String section_name) {
        return (createBasicPluginConfigModel(ConfigSection.SECTION_PLUGINS, section_name));
    }

    public BasicPluginConfigModel createBasicPluginConfigModel(String parent_section, String section_name) {
        final BasicPluginConfigModel model = new BasicPluginConfigModelImpl(this, parent_section, section_name);
        try {
            class_mon.enter();
            configModels.add(model);
        } finally {
            class_mon.exit();
        }
        fireEvent(pi, UIManagerEvent.ET_PLUGIN_CONFIG_MODEL_CREATED, model);
        return (model);
    }

    public void destroy(final BasicPluginConfigModel model) {
        try {
            class_mon.enter();
            configModels.remove(model);
        } finally {
            class_mon.exit();
        }
        fireEvent(pi, UIManagerEvent.ET_PLUGIN_CONFIG_MODEL_DESTROYED, model);
    }

    public PluginConfigModel[] getPluginConfigModels() {
        try {
            class_mon.enter();
            return (PluginConfigModel[]) configModels.toArray(new PluginConfigModel[0]);
        } finally {
            class_mon.exit();
        }
    }

    public void copyToClipBoard(final String data) throws UIException {
        boolean ok = fireEvent(pi, UIManagerEvent.ET_COPY_TO_CLIPBOARD, data);
        if (!ok) {
            throw (new UIException("Failed to deliver request to UI"));
        }
    }

    public void openURL(final URL url) throws UIException {
        boolean ok = fireEvent(pi, UIManagerEvent.ET_OPEN_URL, url);
        if (!ok) {
            throw (new UIException("Failed to deliver request to UI"));
        }
    }

    public TableManager getTableManager() {
        return (table_manager);
    }

    public MenuManager getMenuManager() {
        return menu_manager;
    }

    public SWTManager getSWTManager() {
        PluginDeprecation.call("getSWTManager", pi.getPluginID());
        return SWTManagerImpl.getSingleton();
    }

    public static void initialisationComplete() {
        try {
            class_mon.enter();
            initialisation_complete = true;
            for (int j = 0; j < ui_factories.size(); j++) {
                UIInstanceFactory instance = (UIInstanceFactory) ui_factories.get(j);
                Iterator it = ui_listeners.iterator();
                while (it.hasNext()) {
                    Object[] entry = (Object[]) it.next();
                    try {
                        ((UIManagerListener) entry[0]).UIAttached(instance.getInstance((PluginInterface) entry[1]));
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            class_mon.exit();
        }
    }

    public void attachUI(UIInstanceFactory factory) throws UIException {
        attachUI(factory, null);
    }

    public void attachUI(UIInstanceFactory factory, IUIIntializer init) {
        try {
            class_mon.enter();
            ui_factories.add(factory);
            if (initialisation_complete) {
                Iterator it = ui_listeners.iterator();
                while (it.hasNext()) {
                    Object[] entry = (Object[]) it.next();
                    PluginInterface pi = (PluginInterface) entry[1];
                    String name = pi.getPluginName();
                    if (init != null) {
                        init.reportCurrentTask(MessageText.getString("splash.plugin.UIinit", new String[] { name }));
                        init.increaseProgress();
                    }
                    try {
                        ((UIManagerListener) entry[0]).UIAttached(factory.getInstance(pi));
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            class_mon.exit();
        }
    }

    public void detachUI(UIInstanceFactory instance) throws UIException {
        try {
            class_mon.enter();
            instance.detach();
            ui_factories.remove(instance);
            if (initialisation_complete) {
                Iterator it = ui_listeners.iterator();
                while (it.hasNext()) {
                    Object[] entry = (Object[]) it.next();
                    try {
                        ((UIManagerListener) entry[0]).UIDetached(instance.getInstance((PluginInterface) entry[1]));
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            class_mon.exit();
        }
    }

    public void addUIListener(UIManagerListener listener) {
        try {
            class_mon.enter();
            ui_listeners.add(new Object[] { listener, pi });
            if (initialisation_complete) {
                for (int i = 0; i < ui_factories.size(); i++) {
                    UIInstanceFactory instance = (UIInstanceFactory) ui_factories.get(i);
                    try {
                        listener.UIAttached(instance.getInstance(pi));
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            class_mon.exit();
        }
    }

    public void removeUIListener(UIManagerListener listener) {
        try {
            class_mon.enter();
            Iterator it = ui_listeners.iterator();
            while (it.hasNext()) {
                Object[] entry = (Object[]) it.next();
                if (entry[0] == listener) {
                    it.remove();
                }
            }
        } finally {
            class_mon.exit();
        }
    }

    public void addUIEventListener(UIManagerEventListener listener) {
        List ui_event_history_copy;
        try {
            class_mon.enter();
            ui_event_listeners.add(listener);
            ui_event_history_copy = new ArrayList(ui_event_history);
        } finally {
            class_mon.exit();
        }
        for (int i = 0; i < ui_event_history_copy.size(); i++) {
            try {
                listener.eventOccurred((UIManagerEvent) ui_event_history_copy.get(i));
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
        }
    }

    public void removeUIEventListener(UIManagerEventListener listener) {
        try {
            class_mon.enter();
            ui_event_listeners.remove(listener);
        } finally {
            class_mon.exit();
        }
    }

    public boolean hasUIInstances() {
        return !ui_factories.isEmpty();
    }

    public UIInstance[] getUIInstances() {
        try {
            class_mon.enter();
            ArrayList result = new ArrayList(ui_factories.size());
            for (int i = 0; i < ui_factories.size(); i++) {
                UIInstanceFactory instance = (UIInstanceFactory) ui_factories.get(i);
                result.add(instance.getInstance(pi));
            }
            return (UIInstance[]) result.toArray(new UIInstance[result.size()]);
        } finally {
            class_mon.exit();
        }
    }

    public static boolean fireEvent(PluginInterface pi, int type, Object data) {
        return (fireEvent(new UIManagerEventAdapter(pi, type, data)));
    }

    public static boolean fireEvent(UIManagerEventAdapter event) {
        boolean delivered = false;
        Iterator event_it = ui_event_listeners.iterator();
        while (event_it.hasNext()) {
            try {
                if (((UIManagerEventListener) event_it.next()).eventOccurred(event)) {
                    delivered = true;
                    break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        int type = event.getType();
        if (type == UIManagerEvent.ET_PLUGIN_VIEW_MODEL_CREATED || type == UIManagerEvent.ET_PLUGIN_CONFIG_MODEL_CREATED || type == UIManagerEvent.ET_ADD_TABLE_CONTEXT_MENU_ITEM || type == UIManagerEvent.ET_ADD_MENU_ITEM || type == UIManagerEvent.ET_REMOVE_TABLE_CONTEXT_MENU_ITEM || type == UIManagerEvent.ET_REMOVE_MENU_ITEM) {
            delivered = true;
            try {
                class_mon.enter();
                ui_event_history.add(event);
            } finally {
                class_mon.exit();
            }
        } else if (type == UIManagerEvent.ET_PLUGIN_VIEW_MODEL_DESTROYED || type == UIManagerEvent.ET_PLUGIN_CONFIG_MODEL_DESTROYED) {
            delivered = true;
            try {
                class_mon.enter();
                Iterator history_it = ui_event_history.iterator();
                while (history_it.hasNext()) {
                    UIManagerEvent e = (UIManagerEvent) history_it.next();
                    int e_type = e.getType();
                    if (e_type == UIManagerEvent.ET_PLUGIN_VIEW_MODEL_CREATED || e_type == UIManagerEvent.ET_PLUGIN_CONFIG_MODEL_CREATED) {
                        if (e.getData() == event.getData()) {
                            history_it.remove();
                            break;
                        }
                    }
                }
            } finally {
                class_mon.exit();
            }
        }
        return (delivered);
    }

    public void showTextMessage(final String title_resource, final String message_resource, final String contents) {
        fireEvent(pi, UIManagerEvent.ET_SHOW_TEXT_MESSAGE, new String[] { title_resource, message_resource, contents });
    }

    public long showMessageBox(String title_resource, String message_resource, long message_map) {
        UIManagerEventAdapter event = new UIManagerEventAdapter(pi, UIManagerEvent.ET_SHOW_MSG_BOX, new Object[] { title_resource, message_resource, new Long(message_map) });
        if (!fireEvent(event)) {
            return (UIManagerEvent.MT_NONE);
        }
        return (((Long) event.getResult()).longValue());
    }

    public void openTorrent(Torrent torrent) {
        fireEvent(pi, UIManagerEvent.ET_OPEN_TORRENT_VIA_TORRENT, torrent);
    }

    public void openFile(File file) {
        fireEvent(pi, UIManagerEvent.ET_FILE_OPEN, file);
    }

    public void showFile(File file) {
        fireEvent(pi, UIManagerEvent.ET_FILE_SHOW, file);
    }

    public boolean showConfigSection(String sectionID) {
        UIManagerEventAdapter event = new UIManagerEventAdapter(pi, UIManagerEvent.ET_SHOW_CONFIG_SECTION, sectionID);
        if (!fireEvent(event)) return false;
        if (event.getResult() instanceof Boolean) return false;
        return ((Boolean) event.getResult()).booleanValue();
    }

    public UIInputReceiver getInputReceiver() {
        UIInstance[] instances = this.getUIInstances();
        UIInputReceiver r = null;
        for (int i = 0; i < instances.length; i++) {
            r = instances[i].getInputReceiver();
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public UIMessage createMessage() {
        UIInstance[] instances = this.getUIInstances();
        UIMessage r = null;
        for (int i = 0; i < instances.length; i++) {
            r = instances[i].createMessage();
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public BasicPluginViewModel createLoggingViewModel(LoggerChannel channel, boolean use_plugin_name) {
        String log_view_name = (use_plugin_name) ? pi.getPluginName() : channel.getName();
        BasicPluginViewModel model = createBasicPluginViewModel(log_view_name);
        model.getActivity().setVisible(false);
        model.getProgress().setVisible(false);
        model.getStatus().setVisible(false);
        model.attachLoggerChannel(channel);
        return model;
    }

    public static void unload(PluginInterface pi) {
        try {
            class_mon.enter();
            Iterator it = ui_listeners.iterator();
            while (it.hasNext()) {
                Object[] entry = (Object[]) it.next();
                if (pi == (PluginInterface) entry[1]) {
                    it.remove();
                }
            }
            Iterator<UIManagerEventAdapter> ev_it = ui_event_history.iterator();
            while (ev_it.hasNext()) {
                UIManagerEventAdapter event = ev_it.next();
                if (event.getPluginInterface() == pi) {
                    ev_it.remove();
                }
            }
        } finally {
            class_mon.exit();
        }
    }
}
