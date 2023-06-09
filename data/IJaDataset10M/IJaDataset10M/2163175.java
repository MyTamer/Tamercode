package edu.indiana.extreme.www.xgws.msgbox.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.log4j.Logger;
import edu.indiana.extreme.www.xgws.msgbox.util.MsgBoxCommonConstants;
import edu.indiana.extreme.www.xgws.msgbox.util.MsgBoxNameSpConsts;

public class DestroyMsgBox {

    private EndpointReference msgBoxEndPointReference = null;

    private long timeoutInMilliSeconds = 0;

    org.apache.log4j.Logger logger = Logger.getLogger(DestroyMsgBox.class);

    private String msgBoxId = null;

    private final OMFactory factory = OMAbstractFactory.getOMFactory();

    public DestroyMsgBox(EndpointReference msgBoxEpr, long timeout) throws AxisFault {
        this.msgBoxEndPointReference = msgBoxEpr;
        this.timeoutInMilliSeconds = timeout;
        String address = msgBoxEpr.getAddress();
        int biginIndex = address.indexOf("clientid");
        if (biginIndex != -1) {
            msgBoxId = address.substring(biginIndex + "clientid".length() + 1);
        } else {
            throw new AxisFault("Invalid Message Box EPR cannot find message box ID");
        }
    }

    public long getTimeoutInMilliSeconds() {
        return timeoutInMilliSeconds;
    }

    public void setTimeoutInMilliSeconds(long timeout) {
        timeoutInMilliSeconds = timeout;
    }

    public String execute() throws AxisFault {
        OMElement message = createMessageEl(this.msgBoxId);
        ServiceClient serviceClient = createServiceClient(message);
        OMElement responseMessage = null;
        try {
            responseMessage = serviceClient.sendReceive(message);
        } finally {
            serviceClient.cleanupTransport();
        }
        responseMessage = serviceClient.sendReceive(message);
        if (responseMessage == null) {
            throw AxisFault.makeFault(new RuntimeException("no response recieved for subscription message"));
        }
        return responseMessage.getFirstElement().getText();
    }

    private OMElement createMessageEl(String msgboxid) throws AxisFault {
        OMElement message = factory.createOMElement("destroyMsgBox", MsgBoxNameSpConsts.MSG_BOX);
        OMElement msgBoxId = factory.createOMElement("MsgBoxId", MsgBoxNameSpConsts.MSG_BOX);
        msgBoxId.setText(msgboxid);
        message.addChild(msgBoxId);
        message.declareNamespace(MsgBoxNameSpConsts.MSG_BOX);
        return message;
    }

    private ServiceClient createServiceClient(OMElement message) throws AxisFault {
        String uuid = UUIDGenerator.getUUID();
        Options opts = new Options();
        opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        opts.setTo(msgBoxEndPointReference);
        opts.setMessageId(uuid);
        opts.setAction(message.getNamespace().getNamespaceURI() + "/" + message.getLocalName());
        opts.setTimeOutInMilliSeconds(getTimeoutInMilliSeconds());
        ServiceClient client = new ServiceClient();
        try {
            client.engageModule(MsgBoxCommonConstants.AXIS_MODULE_NAME_ADDRESSING);
            if (logger.isDebugEnabled()) logger.debug("Addressing module engaged");
        } catch (AxisFault e) {
            if (logger.isDebugEnabled()) logger.debug("Addressing module not engaged :" + e);
        }
        client.setOptions(opts);
        return client;
    }
}
