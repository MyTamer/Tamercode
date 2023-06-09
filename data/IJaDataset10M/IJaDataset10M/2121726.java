package jmri.managers;

import jmri.Audio;
import jmri.AudioException;
import jmri.AudioManager;

/**
 * Abstract partial implementation of an AudioManager.
 *
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * <P>
 *
 * @author  Matthew Harris  copyright (c) 2009
 * @version $Revision: 1.7 $
 */
public abstract class AbstractAudioManager extends AbstractManager implements AudioManager {

    public char typeLetter() {
        return 'A';
    }

    public Audio provideAudio(String name) throws AudioException {
        Audio t = getAudio(name);
        if (t != null) return t;
        if (name.startsWith(getSystemPrefix() + typeLetter())) return newAudio(name, null); else return newAudio(makeSystemName(name), null);
    }

    public Audio getAudio(String name) {
        Audio t = getByUserName(name);
        if (t != null) return t;
        return getBySystemName(name);
    }

    public Audio getBySystemName(String key) {
        return (Audio) _tsys.get(key);
    }

    public Audio getByUserName(String key) {
        return key == null ? null : (Audio) _tuser.get(key);
    }

    public Audio newAudio(String systemName, String userName) throws AudioException {
        if (log.isDebugEnabled()) log.debug("new Audio:" + ((systemName == null) ? "null" : systemName) + ";" + ((userName == null) ? "null" : userName));
        if (systemName == null) {
            log.error("SystemName cannot be null. UserName was " + ((userName == null) ? "null" : userName));
            return null;
        }
        if ((!systemName.startsWith("" + getSystemPrefix() + typeLetter() + Audio.BUFFER)) && (!systemName.startsWith("" + getSystemPrefix() + typeLetter() + Audio.SOURCE)) && (!systemName.startsWith("" + getSystemPrefix() + typeLetter() + Audio.LISTENER))) {
            log.error("Invalid system name for Audio: " + systemName + " needed either " + getSystemPrefix() + typeLetter() + Audio.BUFFER + " or " + getSystemPrefix() + typeLetter() + Audio.SOURCE + " or " + getSystemPrefix() + typeLetter() + Audio.LISTENER);
            throw new AudioException("Invalid system name for Audio: " + systemName + " needed either " + getSystemPrefix() + typeLetter() + Audio.BUFFER + " or " + getSystemPrefix() + typeLetter() + Audio.SOURCE + " or " + getSystemPrefix() + typeLetter() + Audio.LISTENER);
        }
        Audio s;
        if ((userName != null) && ((s = getByUserName(userName)) != null)) {
            if (getBySystemName(systemName) != s) log.error("inconsistent user (" + userName + ") and system name (" + systemName + ") results; userName related to (" + s.getSystemName() + ")");
            return s;
        }
        if ((s = getBySystemName(systemName)) != null) {
            if ((s.getUserName() == null) && (userName != null)) s.setUserName(userName); else if (userName != null) log.warn("Found audio via system name (" + systemName + ") with non-null user name (" + userName + ")");
            return s;
        }
        s = createNewAudio(systemName, userName);
        if (!(s == null)) register(s);
        return s;
    }

    /**
     * Internal method to invoke the factory, after all the
     * logic for returning an existing method has been invoked.
     *
     * @param systemName Audio object system name (e.g. IAS1, IAB4)
     * @param userName Audio object user name
     * @return never null
     * @throws AudioException if error occurs during creation
     */
    protected abstract Audio createNewAudio(String systemName, String userName) throws AudioException;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractAudioManager.class.getName());
}
