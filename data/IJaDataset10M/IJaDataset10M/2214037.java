package edu.indiana.extreme.www.xgws.msgbox;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import edu.indiana.extreme.www.xgws.msgbox.util.MsgBoxOperations;

/**
 * MsgBoxServiceMessageReceiverInOut message receiver
 */
public class MsgBoxServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {

    private static org.apache.log4j.Logger logger = Logger.getLogger(MsgBoxServiceMessageReceiverInOut.class);

    public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext) throws org.apache.axis2.AxisFault {
        Object obj = getTheImplementationObject(msgContext);
        MsgBoxServiceSkeleton skel = (MsgBoxServiceSkeleton) obj;
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
            throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }
        String operationName = getOperationName(msgContext);
        MsgBoxOperations msgType = MsgBoxOperations.valueFrom(operationName);
        SOAPEnvelope enlp = msgContext.getEnvelope();
        String clientId = null;
        String toAddress = msgContext.getTo().getAddress();
        int biginIndex = toAddress.indexOf("clientid");
        clientId = toAddress.substring(biginIndex + "clientid".length() + 1);
        try {
            ProcessingContext procCtxt = new ProcessingContext();
            switch(msgType) {
                case STORE_MSGS:
                    {
                        if (biginIndex != -1) {
                            procCtxt.setMessage(enlp.getBody().getFirstElement());
                            procCtxt.setMsgBoxId(clientId);
                            procCtxt.setMessageId(msgContext.getMessageID());
                            procCtxt.setSoapAction(msgContext.getSoapAction());
                            OMElement response = skel.storeMessages(procCtxt);
                            envelope = toEnvelope(getSOAPFactory(msgContext), response);
                        } else {
                            throw new AxisFault("clientid cannot be found");
                        }
                    }
                    break;
                case DESTROY_MSGBOX:
                    {
                        if (biginIndex != -1) {
                            procCtxt.setMsgBoxId(clientId);
                            envelope = toEnvelope(getSOAPFactory(msgContext), skel.destroyMsgBox(procCtxt));
                        } else {
                            throw new AxisFault("clientid cannot be found");
                        }
                    }
                    break;
                case TAKE_MSGS:
                    {
                        if (biginIndex != -1) {
                            procCtxt.setMsgBoxId(clientId);
                            OMElement respEl = skel.takeMessages(procCtxt);
                            envelope = toEnvelope(getSOAPFactory(msgContext), respEl);
                        } else {
                            throw new AxisFault("clientid cannot be found");
                        }
                    }
                    break;
                case CREATE_MSGBOX:
                    {
                        OMElement response = skel.createMsgBox();
                        envelope = toEnvelope(getSOAPFactory(msgContext), response);
                    }
                    break;
            }
        } catch (Exception e) {
            logger.fatal("Exception", e);
            throw new AxisFault("Exception in Message Box ", e);
        }
        newMsgContext.setEnvelope(envelope);
        newMsgContext.getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(SOAPFactory factory, OMElement response) {
        org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
        emptyEnvelope.getBody().addChild(response);
        return emptyEnvelope;
    }

    protected String getOperationName(MessageContext inMsg) throws AxisFault {
        org.apache.axis2.description.AxisOperation op = inMsg.getOperationContext().getAxisOperation();
        if (op == null) {
            throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }
        java.lang.String operationName = null;
        if ((op.getName() == null) || ((operationName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) == null)) {
            throw new AxisFault("invalid operation found");
        }
        return operationName;
    }
}
