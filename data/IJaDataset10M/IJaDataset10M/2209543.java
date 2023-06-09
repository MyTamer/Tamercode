package org.openmobster.core.synchronizer.server.engine;

import org.openmobster.core.common.ServiceManager;
import junit.framework.TestCase;

/**
 * 
 * @author openmobster@gmail.com
 */
public class TestConflictPersistence extends TestCase {

    private ConflictEngine conflictEngine;

    /**
	 * 
	 */
    protected void setUp() throws Exception {
        ServiceManager.bootstrap();
        this.conflictEngine = (ConflictEngine) ServiceManager.locate("ConflictEngine");
    }

    /**
	 * 
	 */
    protected void tearDown() throws Exception {
        ServiceManager.shutdown();
    }

    public void testSaveLock() throws Exception {
        ConflictEntry entry = new ConflictEntry();
        entry.setDeviceId("deviceId");
        entry.setOid("oid");
        entry.setState("blahblah");
        entry.setApp("testApp");
        entry.setChannel("testChannel");
        this.conflictEngine.saveLock(entry);
        ConflictEntry stored = this.conflictEngine.readLock("deviceId", "oid", "testApp", "testChannel");
        assertEquals(stored.getState(), "blahblah");
        assertTrue(stored.getId() > 0);
        stored.setState("blahblah2");
        this.conflictEngine.saveLock(stored);
        stored = this.conflictEngine.readLock("deviceId", "oid", "testApp", "testChannel");
        assertEquals(stored.getState(), "blahblah2");
    }
}
