package uk.org.toot.audio.fader;

import java.util.Arrays;
import uk.org.toot.control.ControlLaw;

/**
 * A 'fader control law'
 */
public class FaderLaw implements ControlLaw {

    /**
     * @label SEMI_LOG
     * @supplierCardinality 1
     * @link aggregationByValue 
     */
    public static final FaderLaw SEMI_LOG = new FaderLaw(1024, -10f, 10f, 0.33f);

    /**
     * @label LOG
     * @supplierCardinality 1
     * @link aggregationByValue 
     */
    public static final FaderLaw LOG = new FaderLaw(1024, -20f, 15f, 0.2f);

    /**
     * @label BROADCAST
     * @supplierCardinality 1
     * @link aggregationByValue 
     */
    public static final FaderLaw BROADCAST = new FaderLaw(1024, -30f, 15f, 0.125f);

    private int resolution;

    private float halfdB;

    private float maxdB;

    private float attenuationCutoffFactor;

    private float[] floatValues;

    public static float ATTENUATION_CUTOFF = 100f;

    protected FaderLaw(int resolution, float halfdB, float maxdB, float attenuationCutoffFactor) {
        this.resolution = resolution;
        this.halfdB = halfdB;
        this.maxdB = maxdB;
        this.attenuationCutoffFactor = attenuationCutoffFactor;
    }

    public int getResolution() {
        return resolution;
    }

    public float getMaximum() {
        return maxdB;
    }

    public float getMinimum() {
        return -ATTENUATION_CUTOFF;
    }

    public float getMaxdB() {
        return maxdB;
    }

    public String getUnits() {
        return "dB";
    }

    public int intValue(float v) {
        if (floatValues == null) {
            floatValues = createFloatValues();
        }
        if (v <= floatValues[0]) return 0;
        if (v >= floatValues[resolution - 1]) return resolution - 1;
        int ret = Arrays.binarySearch(floatValues, v);
        if (ret >= 0) return ret;
        return -(++ret);
    }

    public float userValue(int v) {
        if (floatValues == null) {
            floatValues = createFloatValues();
        }
        if (v < 0) return floatValues[0];
        if (v > resolution - 1) return floatValues[resolution - 1];
        return floatValues[v];
    }

    private float[] createFloatValues() {
        float[] vals = new float[resolution];
        for (int i = 0; i < resolution; i++) {
            vals[i] = calculateFloatValue(i);
        }
        return vals;
    }

    protected float calculateFloatValue(int v) {
        float val = (float) v / (resolution - 1);
        val = maxdB + (2 * halfdB * (1 - val));
        float linZerodB = maxdB + (2 * halfdB);
        int cutoff = (int) (attenuationCutoffFactor * (resolution - 1));
        if (v < cutoff) {
            float attenFactor = (float) (cutoff - v) / cutoff;
            attenFactor *= attenFactor;
            val -= (ATTENUATION_CUTOFF + linZerodB) * attenFactor;
        }
        return val;
    }
}
