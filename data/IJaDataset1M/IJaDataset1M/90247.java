package jmri;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a particular set of 
 * NamedBean (usually turnout) settings to put a path through 
 * trackwork to a Block.
 * <P>
 * Directions are defined for traffic along this path "to" the block, and
 * "from" the block. 
 * Being more specific:
 *<UL>
 *<LI>The "to" direction is the direction that a train is going 
 * when it traverses this path "to" the final block.
 *<LI>The "from" direction is the direction that a train is going 
 * when it traverses this path "from" the final block.
 *</UL>
 * Although useful constants are defined, you don't 
 * have to restrict to those, and there's no assumption that they have
 * to be opposites; NORTH for "to" does not imply SOUTH for "from".
 * This allows you to e.g. handle a piece of curved track where you
 * can be going LEFT at one point and UP at another. The constants
 * are defined as bits, so you can use more than one at a time, for
 * example a direction can simultanously be EAST and RIGHT if desired.
 * What that means needs to be defined by whatever object is using this Path.
 * <P>
 * This implementation handles paths with a list of bean settings. This has
 * been extended from the initial implementation.
 *
 * @author	Bob Jacobsen  Copyright (C) 2006, 2008
 * @version	$Revision: 1.10 $
 */
public class Path {

    /**
     * Create an object with default directions of NONE, and
     * no setting element.
     */
    public Path() {
    }

    /**
     * Convenience constructor to set the destination/source block
     * and directions in one call.
     */
    public Path(Block dest, int toBlockDirection, int fromBlockDirection) {
        this();
        _toBlockDirection = toBlockDirection;
        _fromBlockDirection = fromBlockDirection;
        setBlock(dest);
    }

    /**
     * Convenience constructor to set the destination/source block,
     * directions and a single setting element in one call.
     */
    public Path(Block dest, int toBlockDirection, int fromBlockDirection, BeanSetting setting) {
        this(dest, toBlockDirection, fromBlockDirection);
        addSetting(setting);
    }

    public void addSetting(BeanSetting t) {
        _beans.add(t);
    }

    public List<BeanSetting> getSettings() {
        return _beans;
    }

    public void removeSetting(BeanSetting t) {
        _beans.remove(t);
    }

    public void clearSettings() {
        for (int i = _beans.size(); i > 0; i--) {
            _beans.remove(i - 1);
        }
    }

    public void setBlock(Block b) {
        _block = b;
    }

    public Block getBlock() {
        return _block;
    }

    public int getToBlockDirection() {
        return _toBlockDirection;
    }

    public void setToBlockDirection(int d) {
        _toBlockDirection = d;
    }

    public int getFromBlockDirection() {
        return _fromBlockDirection;
    }

    public void setFromBlockDirection(int d) {
        _fromBlockDirection = d;
    }

    /**
     * Check that the Path can be traversed.
     * This means that any path elements are set to the 
     * proper state, e.g. that the Turnouts on this path
     * are set to the proper CLOSED or OPEN status.
     * @return true if the path can be traversed; always true
     * if no path elements (BeanSettings) are defined.
     */
    public boolean checkPathSet() {
        if (_beans.size() == 0) return true;
        for (int i = 0; i < _beans.size(); i++) {
            if (!(_beans.get(i)).check()) return false;
        }
        return true;
    }

    /**
     * Direction not known or not specified. May also
     * represent "stopped", in the sense of not moving in any direction.
     */
    public static final int NONE = 0x00000;

    /**
     * Northward
     */
    public static final int NORTH = 0x00010;

    /**
     * Southward
     */
    public static final int SOUTH = 0x00020;

    /**
     * Eastward
     */
    public static final int EAST = 0x00040;

    /**
     * Westward
     */
    public static final int WEST = 0x00080;

    /**
     * Clockwise
     */
    public static final int CW = 0x00100;

    /**
     * Counter-clockwise
     */
    public static final int CCW = 0x00200;

    /**
     * Leftward, e.g. on a schematic diagram or CTC panel
     */
    public static final int LEFT = 0x00400;

    /**
     * Rightward, e.g. on a schematic diagram or CTC panel
     */
    public static final int RIGHT = 0x00800;

    /**
     * Upward, e.g. on a schematic diagram or CTC panel
     */
    public static final int UP = 0x01000;

    /**
     * Downward, e.g. on a schematic diagram or CTC panel
     */
    public static final int DOWN = 0x02000;

    /**
     * Decode the direction constants into a human-readable
     * form.
     * This should eventually be internationalized.
     */
    public static String decodeDirection(int d) {
        if (d == NONE) return "None";
        StringBuffer b = new StringBuffer();
        if ((d & NORTH) != 0) appendOne(b, "North");
        if ((d & SOUTH) != 0) appendOne(b, "South");
        if ((d & EAST) != 0) appendOne(b, "East");
        if ((d & WEST) != 0) appendOne(b, "West");
        if ((d & CW) != 0) appendOne(b, "CW");
        if ((d & CCW) != 0) appendOne(b, "CCW");
        if ((d & LEFT) != 0) appendOne(b, "Left");
        if ((d & RIGHT) != 0) appendOne(b, "Right");
        if ((d & UP) != 0) appendOne(b, "Up");
        if ((d & DOWN) != 0) appendOne(b, "Down");
        final int mask = NORTH | SOUTH | EAST | WEST | CW | CCW | LEFT | RIGHT | UP | DOWN;
        if ((d & ~mask) != 0) appendOne(b, "Unknown: 0x" + Integer.toHexString(d & ~mask));
        return b.toString();
    }

    private static void appendOne(StringBuffer b, String t) {
        if (b.length() != 0) b.append(", ");
        b.append(t);
    }

    private ArrayList<BeanSetting> _beans = new ArrayList<BeanSetting>();

    private Block _block;

    private int _toBlockDirection;

    private int _fromBlockDirection;

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Path.class.getName());
}
