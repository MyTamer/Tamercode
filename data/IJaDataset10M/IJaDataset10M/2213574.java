package org.signserver.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.security.cert.Certificate;

/**
 * Generic sign response used by workers responding to GenericServletRequest from the GenericProcessServlet.
 * It adds a contentType value that should be set by the worker.
 *
 * @author Philip Vendil
 * @version $Id: GenericServletResponse.java 1829 2011-08-10 11:50:45Z netmackan $
 */
public class GenericServletResponse extends GenericSignResponse {

    private static final long serialVersionUID = 1L;

    private String contentType = "";

    /**
     * Default constructor used during serialization
     */
    public GenericServletResponse() {
    }

    /**
     * Creates a GenericWorkResponse, works as a simple VO.
     * 
     * @see org.signserver.common.ProcessRequest
     */
    public GenericServletResponse(int requestID, byte[] processedData, Certificate signerCertificate, String archiveId, ArchiveData archiveData, String contentType) {
        super(requestID, processedData, signerCertificate, archiveId, archiveData);
        this.contentType = contentType;
    }

    public void parse(DataInput in) throws IOException {
        super.parse(in);
        int stringSize = in.readInt();
        byte[] data = new byte[stringSize];
        in.readFully(data);
        contentType = new String(data, "UTF-8");
    }

    public void serialize(DataOutput out) throws IOException {
        super.serialize(out);
        byte[] stringData = contentType.getBytes("UTF-8");
        out.writeInt(stringData.length);
        out.write(stringData);
    }

    /**
     * @return the contentType that will be used in the response.
     */
    public String getContentType() {
        return contentType;
    }
}
