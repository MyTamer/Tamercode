package org.signserver.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A Generic work request class implementing the minimal required functionality.
 * 
 * Could be used for TimeStamp Request.
 * 
 * @author philip
 * @version $Id: GenericSignRequest.java 1829 2011-08-10 11:50:45Z netmackan $
 */
public class GenericSignRequest extends ProcessRequest implements ISignRequest {

    private static final long serialVersionUID = 1L;

    private int requestID;

    private byte[] requestData;

    /**
     * Default constructor used during serialization
     */
    public GenericSignRequest() {
        super();
    }

    /**
     * Creates a GenericSignRequest, works as a simple VO.
     * 
     * @param requestID
     * @param requestData
     * @see org.signserver.common.ProcessRequest
     */
    public GenericSignRequest(int requestID, byte[] requestData) {
        this.requestID = requestID;
        this.requestData = requestData;
    }

    /**
     * @see org.signserver.common.ProcessRequest
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     * @see org.signserver.common.ProcessRequest
     */
    public byte[] getRequestData() {
        return requestData;
    }

    public void parse(DataInput in) throws IOException {
        in.readInt();
        this.requestID = in.readInt();
        int dataSize = in.readInt();
        this.requestData = new byte[dataSize];
        in.readFully(requestData);
    }

    public void serialize(DataOutput out) throws IOException {
        out.writeInt(RequestAndResponseManager.REQUESTTYPE_GENERICSIGNREQUEST);
        out.writeInt(requestID);
        out.writeInt(requestData.length);
        out.write(requestData);
    }
}
