package jmri.jmrit.operations.rollingstock;

import java.util.*;

/**
 * A group of rolling stock that is managed as one unit.
 *
 * @author Daniel Boudreau Copyright (C) 2010
 * @version	$Revision: 1.3 $
 */
public class RollingStockGroup {

    protected String _name = "";

    protected RollingStock _lead = null;

    protected List<RollingStock> _group = new ArrayList<RollingStock>();

    public RollingStockGroup(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public String toString() {
        return _name;
    }

    public void add(RollingStock rs) {
        if (_group.contains(rs)) {
            log.debug("rs " + rs.getId() + " alreay part of group " + getName());
            return;
        }
        if (_group.size() <= 0) {
            _lead = rs;
        }
        int oldSize = _group.size();
        _group.add(rs);
        firePropertyChange("listLength", Integer.toString(oldSize), Integer.valueOf(_group.size()));
    }

    public void delete(RollingStock rs) {
        if (!_group.contains(rs)) {
            log.debug("rs " + rs.getId() + " not part of group " + getName());
            return;
        }
        int oldSize = _group.size();
        _group.remove(rs);
        if (isLead(rs) && _group.size() > 0) {
            setLead(_group.get(0));
        }
        firePropertyChange("listLength", Integer.toString(oldSize), Integer.valueOf(_group.size()));
    }

    public List<RollingStock> getGroup() {
        return _group;
    }

    public int getLength() {
        int length = 0;
        for (int i = 0; i < _group.size(); i++) {
            RollingStock rs = _group.get(i);
            length = length + Integer.parseInt(rs.getLength()) + RollingStock.COUPLER;
        }
        return length;
    }

    /**
	 * Get a group's adjusted weight
	 * @return group's weight
	 */
    public int getAdjustedWeightTons() {
        int weightTons = 0;
        for (int i = 0; i < _group.size(); i++) {
            RollingStock rs = _group.get(i);
            weightTons = weightTons + rs.getAdjustedWeightTons();
        }
        return weightTons;
    }

    public boolean isLead(RollingStock rs) {
        if (rs == _lead) return true;
        return false;
    }

    /**
	 * Gets the number of rolling stock in this group
	 * @return number of elements in this group
	 */
    public int getSize() {
        return _group.size();
    }

    /**
	 * Sets the lead for this group. RollingStock must be part of the group. The
	 * rolling stock that make up this group will have the attributes of the
	 * lead. However, the length attribute is the sum of all unit lengths
	 * plus the coupler lengths.
	 * 
	 * @param rs lead for this group.
	 */
    public void setLead(RollingStock rs) {
        if (_group.contains(rs)) {
            _lead = rs;
        }
    }

    public void dispose() {
    }

    java.beans.PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(this);

    public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String p, Object old, Object n) {
        pcs.firePropertyChange(p, old, n);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RollingStockGroup.class.getName());
}
