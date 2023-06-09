package gov.nasa.worldwind.view.firstperson;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.view.BasicViewPropertyLimits;

/**
 * @author jym
 * @version $Id: FlyViewLimits.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class FlyViewLimits extends BasicViewPropertyLimits {

    public double limitEyeElevation(Position position, Globe globe) {
        double newElevation = position.getElevation();
        double terrainElevation = globe.getElevation(position.getLatitude(), position.getLongitude());
        double[] elevLimits = this.getEyeElevationLimits();
        if (position.getElevation() < (elevLimits[0] + terrainElevation)) {
            newElevation = elevLimits[0] + terrainElevation;
        } else if (position.getElevation() > elevLimits[1] + terrainElevation) {
            newElevation = elevLimits[1] + terrainElevation;
        }
        return (newElevation);
    }

    public double limitEyeElevation(double elevation) {
        double[] elevLimits = this.getEyeElevationLimits();
        if (elevation < elevLimits[0]) {
            return elevLimits[0];
        } else if (elevation > elevLimits[1]) {
            return elevLimits[1];
        }
        return (elevation);
    }
}
