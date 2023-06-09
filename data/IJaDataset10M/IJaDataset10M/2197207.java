package org.apache.roller.business;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.TestUtils;
import org.apache.roller.model.AutoPingManager;
import org.apache.roller.model.PingTargetManager;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.pojos.AutoPingData;
import org.apache.roller.pojos.PingTargetData;
import org.apache.roller.pojos.UserData;
import org.apache.roller.pojos.WebsiteData;

/**
 * Test Pings related business operations.
 */
public class PingsTest extends TestCase {

    public static Log log = LogFactory.getLog(PingsTest.class);

    UserData testUser = null;

    WebsiteData testWeblog = null;

    PingTargetData testCommonPing = null;

    PingTargetData testCustomPing = null;

    public PingsTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PingsTest.class);
    }

    /**
     * All tests in this suite require a user and a weblog.
     */
    public void setUp() throws Exception {
        try {
            testUser = TestUtils.setupUser("wtTestUser");
            testWeblog = TestUtils.setupWeblog("wtTestWeblog", testUser);
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test setup failed", ex);
        }
        testCommonPing = new PingTargetData();
        testCommonPing.setName("testCommonPing");
        testCommonPing.setPingUrl("http://localhost/testCommonPing");
        testCustomPing = new PingTargetData();
        testCustomPing.setName("testCommonPing");
        testCustomPing.setPingUrl("http://localhost/testCommonPing");
    }

    public void tearDown() throws Exception {
        try {
            TestUtils.teardownWeblog(testWeblog.getId());
            TestUtils.teardownUser(testUser.getId());
            TestUtils.endSession(true);
        } catch (Exception ex) {
            log.error(ex);
            throw new Exception("Test teardown failed", ex);
        }
        testCommonPing = null;
        testCustomPing = null;
    }

    /**
     * Test basic persistence operations ... Create, Update, Delete
     */
    public void testPingTargetCRUD() throws Exception {
        PingTargetManager mgr = RollerFactory.getRoller().getPingTargetManager();
        PingTargetData ping = null;
        mgr.savePingTarget(testCommonPing);
        String commonId = testCommonPing.getId();
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(commonId);
        assertNotNull(ping);
        assertEquals(testCommonPing.getPingUrl(), ping.getPingUrl());
        testCustomPing.setWebsite(testWeblog);
        mgr.savePingTarget(testCustomPing);
        String customId = testCustomPing.getId();
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(customId);
        assertNotNull(ping);
        assertEquals(testCustomPing.getPingUrl(), ping.getPingUrl());
        ping = null;
        ping = mgr.getPingTarget(commonId);
        ping.setName("testtestCommon");
        mgr.savePingTarget(ping);
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(commonId);
        assertNotNull(ping);
        assertEquals("testtestCommon", ping.getName());
        ping = null;
        ping = mgr.getPingTarget(customId);
        ping.setName("testtestCustom");
        mgr.savePingTarget(ping);
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(customId);
        assertNotNull(ping);
        assertEquals("testtestCustom", ping.getName());
        ping = null;
        ping = mgr.getPingTarget(commonId);
        mgr.removePingTarget(ping);
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(commonId);
        assertNull(ping);
        ping = null;
        ping = mgr.getPingTarget(customId);
        mgr.removePingTarget(ping);
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(customId);
        assertNull(ping);
    }

    /**
     * Test lookup mechanisms ... id, all common, all custom for weblog
     */
    public void testPingTargetLookups() throws Exception {
        PingTargetManager mgr = RollerFactory.getRoller().getPingTargetManager();
        PingTargetData ping = null;
        mgr.savePingTarget(testCommonPing);
        String commonId = testCommonPing.getId();
        TestUtils.endSession(true);
        testCustomPing.setWebsite(testWeblog);
        mgr.savePingTarget(testCustomPing);
        String customId = testCustomPing.getId();
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(commonId);
        assertNotNull(ping);
        assertEquals(testCommonPing.getName(), ping.getName());
        List commonPings = mgr.getCommonPingTargets();
        assertNotNull(commonPings);
        assertEquals(1, commonPings.size());
        List customPings = mgr.getCustomPingTargets(testWeblog);
        assertNotNull(customPings);
        assertEquals(1, customPings.size());
        ping = null;
        ping = mgr.getPingTarget(commonId);
        mgr.removePingTarget(ping);
        TestUtils.endSession(true);
        ping = null;
        ping = mgr.getPingTarget(customId);
        mgr.removePingTarget(ping);
        TestUtils.endSession(true);
    }

    /**
     * Test basic persistence operations ... Create, Update, Delete
     */
    public void testAutoPingCRUD() throws Exception {
        AutoPingManager mgr = RollerFactory.getRoller().getAutopingManager();
        AutoPingData autoPing = null;
        PingTargetData pingTarget = TestUtils.setupPingTarget("fooPing", "http://foo/null");
        PingTargetData pingTarget2 = TestUtils.setupPingTarget("blahPing", "http://blah/null");
        TestUtils.endSession(true);
        autoPing = new AutoPingData(null, pingTarget, testWeblog);
        mgr.saveAutoPing(autoPing);
        String id = autoPing.getId();
        TestUtils.endSession(true);
        autoPing = null;
        autoPing = mgr.getAutoPing(id);
        assertNotNull(autoPing);
        assertEquals(pingTarget, autoPing.getPingTarget());
        autoPing.setPingTarget(pingTarget2);
        mgr.saveAutoPing(autoPing);
        TestUtils.endSession(true);
        autoPing = null;
        autoPing = mgr.getAutoPing(id);
        assertNotNull(autoPing);
        assertEquals(pingTarget2, autoPing.getPingTarget());
        mgr.removeAutoPing(autoPing);
        TestUtils.endSession(true);
        autoPing = null;
        autoPing = mgr.getAutoPing(id);
        assertNull(autoPing);
        TestUtils.teardownPingTarget(pingTarget.getId());
        TestUtils.teardownPingTarget(pingTarget2.getId());
        TestUtils.endSession(true);
    }

    /**
     * Test special ping target removal methods ... by weblog/target, collection, all
     */
    public void testPingTargetRemovals() throws Exception {
        AutoPingManager mgr = RollerFactory.getRoller().getAutopingManager();
        AutoPingData testAutoPing = null;
        PingTargetData pingTarget = TestUtils.setupPingTarget("fooPing", "http://foo/null");
        PingTargetData pingTarget2 = TestUtils.setupPingTarget("blahPing", "http://blah/null");
        PingTargetData pingTarget3 = TestUtils.setupPingTarget("gahPing", "http://gah/null");
        AutoPingData autoPing = TestUtils.setupAutoPing(pingTarget, testWeblog);
        AutoPingData autoPing2 = TestUtils.setupAutoPing(pingTarget2, testWeblog);
        AutoPingData autoPing3 = TestUtils.setupAutoPing(pingTarget3, testWeblog);
        TestUtils.endSession(true);
        mgr.removeAutoPing(pingTarget, testWeblog);
        TestUtils.endSession(true);
        testAutoPing = null;
        testAutoPing = mgr.getAutoPing(autoPing.getId());
        assertNull(testAutoPing);
        List autoPings = new ArrayList();
        autoPings.add(autoPing2);
        autoPings.add(autoPing3);
        mgr.removeAutoPings(autoPings);
        TestUtils.endSession(true);
        autoPings = mgr.getAutoPingsByWebsite(testWeblog);
        assertNotNull(autoPings);
        assertEquals(0, autoPings.size());
        autoPing = TestUtils.setupAutoPing(pingTarget, testWeblog);
        autoPing2 = TestUtils.setupAutoPing(pingTarget2, testWeblog);
        autoPing3 = TestUtils.setupAutoPing(pingTarget3, testWeblog);
        TestUtils.endSession(true);
        mgr.removeAllAutoPings();
        TestUtils.endSession(true);
        autoPings = mgr.getAutoPingsByWebsite(testWeblog);
        assertNotNull(autoPings);
        assertEquals(0, autoPings.size());
        TestUtils.teardownPingTarget(pingTarget.getId());
        TestUtils.teardownPingTarget(pingTarget2.getId());
        TestUtils.endSession(true);
    }

    /**
     * Test lookup mechanisms ... id, ping target, weblog
     */
    public void testAutoPingLookups() throws Exception {
        AutoPingManager mgr = RollerFactory.getRoller().getAutopingManager();
        AutoPingData autoPing = null;
        PingTargetData pingTarget = TestUtils.setupPingTarget("fooPing", "http://foo/null");
        TestUtils.endSession(true);
        autoPing = new AutoPingData(null, pingTarget, testWeblog);
        mgr.saveAutoPing(autoPing);
        String id = autoPing.getId();
        TestUtils.endSession(true);
        autoPing = null;
        autoPing = mgr.getAutoPing(id);
        assertNotNull(autoPing);
        assertEquals(pingTarget, autoPing.getPingTarget());
        List autoPings = mgr.getAutoPingsByTarget(pingTarget);
        assertNotNull(autoPings);
        assertEquals(1, autoPings.size());
        autoPings = null;
        autoPings = mgr.getAutoPingsByWebsite(testWeblog);
        assertNotNull(autoPing);
        assertEquals(1, autoPings.size());
        mgr.removeAutoPing(autoPing);
        TestUtils.endSession(true);
        TestUtils.teardownPingTarget(pingTarget.getId());
        TestUtils.endSession(true);
    }

    public void testApplicableAutoPings() throws Exception {
    }

    /**
     * Test that we can properly remove a ping target when it has
     * associated elements like auto pings and ping queue entries.
     */
    public void testRemoveLoadedPingTarget() throws Exception {
    }
}
