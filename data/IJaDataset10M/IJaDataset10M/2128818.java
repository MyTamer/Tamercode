package de.uniluebeck.itm.spyglass.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import de.uniluebeck.itm.spyglass.io.PacketReader;
import de.uniluebeck.itm.spyglass.io.SpyglassPacketRecorder;
import de.uniluebeck.itm.spyglass.plugin.Plugin;
import de.uniluebeck.itm.spyglass.plugin.PluginFactory;
import de.uniluebeck.itm.spyglass.plugin.PluginManager;
import de.uniluebeck.itm.spyglass.xmlconfig.GeneralSettingsXMLConfig;
import de.uniluebeck.itm.spyglass.xmlconfig.PluginXMLConfig;
import de.uniluebeck.itm.spyglass.xmlconfig.XMLConfig;

/**
 * Spyglass configuration file, mainly used by the deserialization process of the Spyglass class.
 * The persistence framework to utilize the serialization and deserialization to/from an XML file is
 * "SimpleXML" (http://simple.sourceforge.net/).
 * 
 * @author Sebastian Ebers, Daniel Bimschas, Dariush Forouher
 */
@Root(strict = true)
public class SpyglassConfiguration extends XMLConfig {

    /**
	 * The current packetReader.
	 * 
	 * Must be declared volatile since it can be replaced at runtime.
	 */
    @Element(name = "packetReader")
    private volatile PacketReader packetReader = new SpyglassPacketRecorder();

    /**
	 * The one and only plug-in manager
	 */
    @Element(name = "instances")
    private PluginManager pluginManager = new PluginManager();

    /**
	 * some general settings
	 */
    @Element(name = "generalSettings")
    private final GeneralSettingsXMLConfig generalSettings = new GeneralSettingsXMLConfig();

    /**
	 * The default configurations for all plug-ins
	 * 
	 * Must be declared volatile since it can be replaced at runtime.
	 */
    @ElementList(name = "defaults")
    private volatile Collection<Plugin> defaults = new LinkedList<Plugin>();

    /**
	 * Constructor
	 */
    public SpyglassConfiguration() {
        final Collection<Plugin> defaultPlugins = new HashSet<Plugin>();
        for (final Class<? extends Plugin> p : PluginManager.getAvailablePluginTypes()) {
            defaultPlugins.add(PluginFactory.createDefaultInstance(p));
        }
        setDefaultPlugins(defaultPlugins);
    }

    /**
	 * @return the generalSettings
	 */
    public GeneralSettingsXMLConfig getGeneralSettings() {
        return generalSettings;
    }

    /**
	 * @param defaults
	 *            the defaults to set
	 */
    public void setDefaultPlugins(final Collection<Plugin> defaults) {
        final Collection<Plugin> oldDefaults = this.defaults;
        this.defaults = new LinkedList<Plugin>(defaults);
        firePropertyChange("defaults", oldDefaults, this.defaults);
    }

    /**
	 * Returns a read-only collection of plug-ins which are configured by default
	 * 
	 * Note that this collection is immutable, entries cannot be added or removed.
	 * 
	 * @return the defaults a collection of plug-ins which are configured by default
	 */
    public Collection<Plugin> getDefaultPlugins() {
        return Collections.unmodifiableCollection(defaults);
    }

    /**
	 * Returns the default configuration parameters of a plug-in
	 * 
	 * @param clazz
	 *            the plug-in's class
	 * @return the default configuration parameters of a plug-in
	 */
    public PluginXMLConfig getDefaultConfig(final Class<? extends Plugin> clazz) {
        final Collection<Plugin> plugins = getDefaultPlugins();
        for (final Plugin plugin : plugins) {
            if (plugin.getClass().equals(clazz)) {
                return plugin.getXMLConfig();
            }
        }
        return null;
    }

    /**
	 * Sets the instance which manages the plug-ins
	 * 
	 * @param pluginManager
	 *            the instance which manages the plug-ins
	 */
    public void setPluginManager(final PluginManager pluginManager) {
        final PluginManager oldPM = this.pluginManager;
        this.pluginManager = pluginManager;
        firePropertyChange("pluginManager", oldPM, this.pluginManager);
    }

    /**
	 * Returns the instance which manages the plug-ins
	 * 
	 * @return the instance which manages the plug-ins
	 */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
	 * Returns the PacketReader instance which currently provides the packets.
	 * 
	 * @return the PacketReader instance which currently provides the packets.
	 */
    public PacketReader getPacketReader() {
        return packetReader;
    }

    /**
	 * Sets the PacketReader instance which will provide the packets.
	 * 
	 * @param packetReader
	 *            the PacketReader instance which will provide the packets.
	 */
    public void setPacketReader(final PacketReader packetReader) {
        final PacketReader oldReader = this.packetReader;
        this.packetReader = packetReader;
        firePropertyChange("packetReader", oldReader, packetReader);
    }
}
