package edu.indiana.extreme.www.xgws.msgbox.client;

import javax.xml.namespace.QName;
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
import edu.indiana.extreme.www.xgws.msgbox.util.MsgBoxUtils;

public class CreateMsgBox {

    org.apache.log4j.Logger logger = Logger.getLogger(CreateMsgBox.class);

    private final OMFactory factory = OMAbstractFactory.getOMFactory();

    protected String msgBoxEndPointReference;

    protected long timeoutInMilliSeconds;

    private String msgBoxId;

    private OMElement responseEl;

    public CreateMsgBox(String msgBoxLocation, long timeout) {
        this.msgBoxEndPointReference = msgBoxLocation;
        responseEl = OMAbstractFactory.getOMFactory().createOMElement(new QName("http://www.extreme.indiana.edu/xgws/msgbox/2004/", "MsgBoxId"));
        timeoutInMilliSeconds = timeout;
    }

    public long getTimeoutInMilliSeconds() {
        return timeoutInMilliSeconds;
    }

    public void setTimeoutInMilliSeconds(long timeout) {
        timeoutInMilliSeconds = timeout;
    }

    public EndpointReference execute() throws AxisFault {
        ServiceClient serviceClient = createServiceClient();
        OMElement responseMessage = null;
        try {
            responseMessage = serviceClient.sendReceive(createMessageEl());
        } finally {
            serviceClient.cleanupTransport();
        }
        if (responseMessage == null) {
            throw AxisFault.makeFault(new RuntimeException("no response recieved for subscription message"));
        }
        String response = responseMessage.getFirstElement().getText();
        this.responseEl.setText(response);
        this.msgBoxEndPointReference = MsgBoxUtils.formatMessageBoxUrl(this.msgBoxEndPointReference, response);
        return new EndpointReference(this.msgBoxEndPointReference);
    }

    private OMElement createMessageEl() throws AxisFault {
        OMElement message = factory.createOMElement("createMsgBox", MsgBoxNameSpConsts.MSG_BOX);
        OMElement msgBoxId = factory.createOMElement("MsgBoxId", MsgBoxNameSpConsts.MSG_BOX);
        msgBoxId.setText("Create message box");
        message.addChild(msgBoxId);
        message.declareNamespace(MsgBoxNameSpConsts.MSG_BOX);
        return message;
    }

    private ServiceClient createServiceClient() throws AxisFault {
        String uuid = UUIDGenerator.getUUID();
        Options opts = new Options();
        opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        opts.setTo(new EndpointReference(this.msgBoxEndPointReference));
        opts.setMessageId(uuid);
        opts.setAction(MsgBoxNameSpConsts.MSG_BOX.getNamespaceURI() + "/" + "createMsgBox");
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
