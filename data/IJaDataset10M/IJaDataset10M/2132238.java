package lejos.robotics;

/**
 * A platform independent implementation for sensors that can detect white light levels.
 * @author BB
 *
 */
public interface LightDetector {

    /**
	 * Returns the calibrated and normalized brightness of the white light detected.
	 * @return A brightness value between 0 and 100%, with 0 = darkness and 100 = intense sunlight
	 */
    public int getLightValue();

    /**
	 * Returns the normalized value of the brightness of the white light detected, such that
	 * the lowest value is darkness and the highest value is intense bright light.
	 * @return A raw value, between getLow() and getHigh(). Usually 
	 * between 0 and 1023 but can be anything depending on hardware.
	 */
    public int getNormalizedLightValue();

    /**
	 * The highest raw light value this sensor can return from intense bright light.
	 * @return
	 */
    public int getHigh();

    /**
	 * The lowest raw light value this sensor can return in pitch black darkness.
	 * @return
	 */
    public int getLow();
}
