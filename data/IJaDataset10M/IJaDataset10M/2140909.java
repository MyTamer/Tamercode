package jmri.jmrix.srcp;

import jmri.InstanceManager;
import jmri.*;

/**
 * Lightweight class to denote that a system is active,
 * and provide general information.
 * <p>
 * Objects of specific subtypes are registered
 * in the instance manager to activate their
 * particular system.
 *
 * @author		Bob Jacobsen  Copyright (C) 2010
 * @version             $Revision: 1.3 $
 */
public class SRCPSystemConnectionMemo extends jmri.jmrix.SystemConnectionMemo {

    public SRCPSystemConnectionMemo(SRCPTrafficController et) {
        super("D", "SRCP");
        this.et = et;
        this.et.setSystemConnectionMemo(this);
        register();
    }

    public SRCPSystemConnectionMemo() {
        super("D", "SRCP");
        register();
        InstanceManager.store(this, SRCPSystemConnectionMemo.class);
    }

    jmri.jmrix.swing.ComponentFactory cf = null;

    /**
     * Provides access to the TrafficController for this
     * particular connection.
     */
    public SRCPTrafficController getTrafficController() {
        return et;
    }

    public void setTrafficController(SRCPTrafficController et) {
        this.et = et;
        this.et.setSystemConnectionMemo(this);
    }

    private SRCPTrafficController et;

    /**
     * Configure the common managers for Internal connections.
     * This puts the common manager config in one
     * place.  This method is static so that it can be referenced
     * from classes that don't inherit, including hexfile.HexFileFrame
     * and locormi.LnMessageClient
     */
    public void configureManagers() {
        setProgrammerManager(new SRCPProgrammerManager(new SRCPProgrammer()));
        jmri.InstanceManager.setProgrammerManager(getProgrammerManager());
        setPowerManager(new jmri.jmrix.srcp.SRCPPowerManager());
        jmri.InstanceManager.setPowerManager(getPowerManager());
        setTurnoutManager(new jmri.jmrix.srcp.SRCPTurnoutManager());
        jmri.InstanceManager.setTurnoutManager(getTurnoutManager());
        jmri.InstanceManager.setSensorManager(getSensorManager());
        setThrottleManager(new jmri.jmrix.srcp.SRCPThrottleManager());
        jmri.InstanceManager.setThrottleManager(getThrottleManager());
    }

    /**
     * Configure the programming manager and "command station" objects
     */
    public void configureCommandStation() {
        jmri.InstanceManager.setCommandStation(new jmri.jmrix.srcp.SRCPCommandStation());
        et.sendSRCPMessage(new SRCPMessage("SET PROTOCOL SRCP 0.8.3\n"), null);
        et.sendSRCPMessage(new SRCPMessage("SET CONNECTIONMODE SRCP COMMAND\n"), null);
        et.sendSRCPMessage(new SRCPMessage("GO\n"), null);
    }

    /**
     * Provides access to the Programmer for this particular connection.
     * NOTE: Programmer defaults to null
     */
    public ProgrammerManager getProgrammerManager() {
        return programmerManager;
    }

    public void setProgrammerManager(ProgrammerManager p) {
        programmerManager = p;
    }

    private ProgrammerManager programmerManager = null;

    public ThrottleManager getThrottleManager() {
        if (throttleManager == null) throttleManager = new SRCPThrottleManager();
        return throttleManager;
    }

    public void setThrottleManager(ThrottleManager t) {
        throttleManager = t;
    }

    private ThrottleManager throttleManager;

    public PowerManager getPowerManager() {
        if (powerManager == null) powerManager = new SRCPPowerManager();
        return powerManager;
    }

    public void setPowerManager(PowerManager p) {
        powerManager = p;
    }

    private PowerManager powerManager;

    public SensorManager getSensorManager() {
        if (sensorManager == null) sensorManager = new SRCPSensorManager(this);
        return sensorManager;
    }

    public void setSensorManager(SensorManager s) {
        sensorManager = s;
    }

    private SensorManager sensorManager = null;

    public TurnoutManager getTurnoutManager() {
        return turnoutManager;
    }

    public void setTurnoutManager(TurnoutManager t) {
        turnoutManager = t;
    }

    private TurnoutManager turnoutManager = null;

    public void dispose() {
        et = null;
        InstanceManager.deregister(this, SRCPSystemConnectionMemo.class);
        if (cf != null) InstanceManager.deregister(cf, jmri.jmrix.swing.ComponentFactory.class);
        super.dispose();
    }
}
