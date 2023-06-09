package gov.nasa.worldwindx.applications.worldwindow.features;

import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwindx.applications.worldwindow.core.*;

/**
 * @author tag
 * @version $Id: Crosshair.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class Crosshair extends AbstractOnDemandLayerFeature {

    public Crosshair(Registry registry) {
        super("Crosshair", Constants.FEATURE_CROSSHAIR, null, null, registry);
    }

    @Override
    protected Layer createLayer() {
        Layer layer = this.doCreateLayer();
        layer.setPickEnabled(false);
        return layer;
    }

    protected Layer doCreateLayer() {
        return new CrosshairLayer();
    }

    @Override
    public void turnOn(boolean tf) {
        if (tf == this.on || this.layer == null) return;
        if (tf) controller.addInternalActiveLayer(this.layer); else this.controller.getActiveLayers().remove(this.layer);
        this.on = tf;
    }
}
