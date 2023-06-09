package de.uniluebeck.itm.spyglass.plugin;

import java.util.List;
import de.uniluebeck.itm.spyglass.core.Spyglass;
import de.uniluebeck.itm.spyglass.drawing.DrawingObject;
import de.uniluebeck.itm.spyglass.gui.configuration.AbstractPluginTypePreferencePage;
import de.uniluebeck.itm.spyglass.gui.configuration.PluginPreferenceDialog;
import de.uniluebeck.itm.spyglass.gui.configuration.PluginPreferencePage;
import de.uniluebeck.itm.spyglass.gui.view.DrawingArea;
import de.uniluebeck.itm.spyglass.layer.Layer;
import de.uniluebeck.itm.spyglass.packet.SpyglassPacket;
import de.uniluebeck.itm.spyglass.xmlconfig.PluginXMLConfig;

/**
 * Temporary class which all old Plugins inherit. It adds a compatibillity layer to the Plugin
 * interface so that the old Plugins can run without much change.
 * 
 * @author dariush
 * @deprecated
 */
@Deprecated
public class HistoricalPlugin extends Plugin implements Drawable {

    private final Layer layer = new QuadTree();

    public HistoricalPlugin() {
        super(true);
    }

    @Override
    public PluginPreferencePage<? extends Plugin, ? extends PluginXMLConfig> createPreferencePage(final PluginPreferenceDialog dialog, final Spyglass spyglass) {
        return new AbstractPluginTypePreferencePage(dialog, spyglass, HistoricalPlugin.getHumanReadableName());
    }

    public static PluginPreferencePage<? extends Plugin, ? extends PluginXMLConfig> createTypePreferencePage(final PluginPreferenceDialog dialog, final Spyglass spyglass) {
        return new AbstractPluginTypePreferencePage(dialog, spyglass, HistoricalPlugin.getHumanReadableName());
    }

    public List<DrawingObject> getDrawingObjects(final DrawingArea drawingArea) {
        return this.layer.getDrawingObjects();
    }

    public static String getHumanReadableName() {
        return "HistoricalPlugin";
    }

    @Override
    public PluginXMLConfig getXMLConfig() {
        return new PluginXMLConfig() {

            @Override
            public String getName() {
                return "unnamedHP";
            }

            @Override
            public boolean getVisible() {
                return false;
            }

            @Override
            public boolean equals(final PluginXMLConfig other) {
                return false;
            }
        };
    }

    @Override
    protected void processPacket(final SpyglassPacket packet) {
    }

    @Override
    public void reset() {
    }

    @Override
    protected void updateQuadTree() {
    }

    protected Layer getSubLayer() {
        return layer;
    }

    public int getTimeout() {
        return getXMLConfig().getTimeout();
    }

    @Override
    public String toString() {
        return HistoricalPlugin.getHumanReadableName() + "." + this.getInstanceName();
    }

    @Override
    public List<DrawingObject> getAutoZoomDrawingObjects() {
        return this.layer.getDrawingObjects();
    }
}
