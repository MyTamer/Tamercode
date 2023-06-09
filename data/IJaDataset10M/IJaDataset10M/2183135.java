package org.opennms.netmgt.provision.adapters.link;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.mock.EventAnticipator;
import org.opennms.netmgt.mock.MockEventIpcManager;
import org.opennms.netmgt.mock.MockEventUtil;
import org.opennms.netmgt.mock.MockNetwork;
import org.opennms.netmgt.mock.MockNode;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsLinkState;
import org.opennms.netmgt.model.OnmsLinkState.LinkState;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.provision.adapters.link.endpoint.dao.DefaultEndPointConfigurationDao;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.core.io.ClassPathResource;

public class LinkEventCorrelatorTest {

    private Event m_unmanagedEvent = new EventBuilder(EventConstants.DATA_LINK_UNMANAGED_EVENT_UEI, "Test").getEvent();

    private Event m_failedEvent = new EventBuilder(EventConstants.DATA_LINK_FAILED_EVENT_UEI, "Test").getEvent();

    private Event m_regainedEvent = new EventBuilder(EventConstants.DATA_LINK_RESTORED_EVENT_UEI, "Test").getEvent();

    EasyMockUtils m_easyMock = new EasyMockUtils();

    MockEventUtil m_eventUtil = new MockEventUtil();

    private MockNetwork m_network;

    private MockNode m_node1;

    private MockNode m_node2;

    private MockEventIpcManager m_eventIpcManager;

    private EventAnticipator m_anticipator;

    private NodeLinkService m_nodeLinkService;

    private DataLinkInterface m_dataLinkInterface;

    private DefaultEndPointConfigurationDao m_endPointConfigDao;

    @Before
    public void setUp() {
        m_eventIpcManager = new MockEventIpcManager();
        m_anticipator = m_eventIpcManager.getEventAnticipator();
        m_endPointConfigDao = new DefaultEndPointConfigurationDao();
        m_endPointConfigDao.setConfigResource(new ClassPathResource("/test-endpoint-configuration.xml"));
        m_endPointConfigDao.afterPropertiesSet();
        m_network = new MockNetwork();
        m_node1 = new MockNode(m_network, 1, "pittsboro-1");
        m_node1.addInterface("192.168.0.1");
        m_node1.getInterface("192.168.0.1").addService("EndPoint", 1);
        m_node2 = new MockNode(m_network, 2, "pittsboro-2");
        m_node2.addInterface("192.168.0.2").addService("EndPoint", 1);
        m_nodeLinkService = createMock(NodeLinkService.class);
        Collection<DataLinkInterface> dlis = new ArrayList<DataLinkInterface>();
        m_dataLinkInterface = new DataLinkInterface(2, 1, 1, 1, "U", new Date());
        dlis.add(m_dataLinkInterface);
        expect(m_nodeLinkService.getLinkContainingNodeId(1)).andStubReturn(dlis);
        expect(m_nodeLinkService.getLinkContainingNodeId(2)).andStubReturn(dlis);
        expect(m_nodeLinkService.getNodeLabel(1)).andStubReturn("pittsboro-1");
        expect(m_nodeLinkService.getNodeLabel(2)).andStubReturn("pittsboro-2");
    }

    @Test
    public void testNodeDownEvent() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);
        expect(m_nodeLinkService.nodeHasEndPointService(1)).andReturn(true);
        Event e = m_node1.createDownEvent();
        e.setService("EndPoint");
        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);
        m_anticipator.anticipateEvent(m_failedEvent);
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        replay();
        correlator.handleNodeDown(e);
        m_eventIpcManager.finishProcessingEvents();
        m_anticipator.verifyAnticipated();
        verify();
    }

    @Test
    public void testCorrelator1NodeDown() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);
        expect(m_nodeLinkService.nodeHasEndPointService(1)).andReturn(true);
        Event e = m_node1.createDownEvent();
        e.setService("EndPoint");
        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);
        m_anticipator.anticipateEvent(m_failedEvent);
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        replay();
        correlator.handleNodeDown(e);
        m_eventIpcManager.finishProcessingEvents();
        m_anticipator.verifyAnticipated();
        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParms().getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1) || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if (p.getValue().getContent().equals("pittsboro-1") || p.getValue().getContent().equals("pittsboro-2")) {
                    foundGood++;
                }
            }
        }
        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    @Test
    public void testCorrelatorNodeFlap() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);
        expect(m_nodeLinkService.nodeHasEndPointService(1)).andStubReturn(true);
        expect(m_nodeLinkService.nodeHasEndPointService(2)).andStubReturn(true);
        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);
        expect(m_nodeLinkService.getPrimaryAddress(1)).andStubReturn("192.168.0.1");
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_BOTH_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_DOWN));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP));
        replay();
        m_anticipator.anticipateEvent(m_failedEvent);
        m_anticipator.anticipateEvent(m_regainedEvent);
        correlator.handleInterfaceDown(m_node1.getInterface("192.168.0.1").createDownEvent());
        correlator.handleNodeDown(m_node1.createDownEvent());
        correlator.handleNodeDown(m_node2.createDownEvent());
        correlator.handleInterfaceUp(m_node1.getInterface("192.168.0.1").createUpEvent());
        correlator.handleNodeUp(m_node1.createUpEvent());
        correlator.handleNodeUp(m_node2.createUpEvent());
        m_eventIpcManager.finishProcessingEvents();
        m_anticipator.verifyAnticipated();
        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParms().getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1) || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if (p.getValue().getContent().equals("pittsboro-1") || p.getValue().getContent().equals("pittsboro-2")) {
                    foundGood++;
                }
            }
        }
        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    @Test
    public void testCorrelatorUnmanagedNodeFlap() {
        OnmsLinkState ls = new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_BOTH_UNMANAGED);
        expect(m_nodeLinkService.getLinkStateForInterface(m_dataLinkInterface)).andStubReturn(ls);
        LinkEventCorrelator correlator = new LinkEventCorrelator();
        correlator.setEventForwarder(m_eventIpcManager);
        correlator.setNodeLinkService(m_nodeLinkService);
        correlator.setEndPointConfigDao(m_endPointConfigDao);
        expect(m_nodeLinkService.getPrimaryAddress(1)).andStubReturn("192.168.0.1");
        expect(m_nodeLinkService.getPrimaryAddress(2)).andStubReturn("192.168.0.2");
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_NODE_UNMANAGED));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_UP));
        m_nodeLinkService.saveLinkState(new OnmsLinkState(m_dataLinkInterface, LinkState.LINK_PARENT_NODE_UNMANAGED));
        replay();
        m_anticipator.anticipateEvent(m_regainedEvent);
        m_anticipator.anticipateEvent(m_unmanagedEvent);
        correlator.handleNodeGainedService(m_node1.getInterface("192.168.0.1").getService("EndPoint").createNewEvent());
        correlator.handleNodeGainedService(m_node2.getInterface("192.168.0.2").getService("EndPoint").createNewEvent());
        correlator.handleServiceDeleted(m_node1.getInterface("192.168.0.1").getService("EndPoint").createDeleteEvent());
        m_eventIpcManager.finishProcessingEvents();
        m_anticipator.verifyAnticipated();
        List<Parm> parms = m_anticipator.getAnticipatedEventsRecieved().get(0).getParms().getParmCollection();
        assertEquals(2, parms.size());
        int foundGood = 0;
        for (Parm p : parms) {
            if (p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT1) || p.getParmName().contentEquals(EventConstants.PARM_ENDPOINT2)) {
                if (p.getValue().getContent().equals("pittsboro-1") || p.getValue().getContent().equals("pittsboro-2")) {
                    foundGood++;
                }
            }
        }
        assertEquals("expect 2 endpoint parms", 2, foundGood);
        verify();
    }

    public <T> T createMock(Class<T> clazz) {
        return m_easyMock.createMock(clazz);
    }

    public void verify() {
        m_easyMock.verifyAll();
    }

    public void replay() {
        m_easyMock.replayAll();
    }
}
