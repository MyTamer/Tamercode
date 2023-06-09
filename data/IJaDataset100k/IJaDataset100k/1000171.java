package net.sf.istcontract.wsimport.encoding.fastinfoset;

import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.stream.buffer.XMLStreamBuffer;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;
import net.sf.istcontract.wsimport.api.SOAPVersion;
import net.sf.istcontract.wsimport.api.message.Packet;
import net.sf.istcontract.wsimport.api.pipe.Codec;
import net.sf.istcontract.wsimport.api.pipe.ContentType;
import net.sf.istcontract.wsimport.api.pipe.StreamSOAPCodec;
import net.sf.istcontract.wsimport.encoding.ContentTypeImpl;
import net.sf.istcontract.wsimport.message.stream.StreamHeader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * A stream SOAP codec for handling SOAP message infosets to fast
 * infoset documents.
 *
 * <p>
 * This implementation currently defers to {@link StreamSOAPCodec} for the decoding
 * using {@link XMLStreamReader}.
 *
 * @author Paul Sandoz
 */
public abstract class FastInfosetStreamSOAPCodec implements Codec {

    private static final FastInfosetStreamReaderFactory READER_FACTORY = FastInfosetStreamReaderFactory.getInstance();

    private StAXDocumentParser _statefulParser;

    private StAXDocumentSerializer _serializer;

    private final StreamSOAPCodec _soapCodec;

    private final boolean _retainState;

    protected final ContentType _defaultContentType;

    FastInfosetStreamSOAPCodec(StreamSOAPCodec soapCodec, SOAPVersion soapVersion, boolean retainState, String mimeType) {
        _soapCodec = soapCodec;
        _retainState = retainState;
        _defaultContentType = new ContentTypeImpl(mimeType);
    }

    FastInfosetStreamSOAPCodec(FastInfosetStreamSOAPCodec that) {
        this._soapCodec = (StreamSOAPCodec) that._soapCodec.copy();
        this._retainState = that._retainState;
        this._defaultContentType = that._defaultContentType;
    }

    public String getMimeType() {
        return _defaultContentType.getContentType();
    }

    public ContentType getStaticContentType(Packet packet) {
        return getContentType(packet.soapAction);
    }

    public ContentType encode(Packet packet, OutputStream out) {
        if (packet.getMessage() != null) {
            final XMLStreamWriter writer = getXMLStreamWriter(out);
            try {
                packet.getMessage().writeTo(writer);
                writer.flush();
            } catch (XMLStreamException e) {
                throw new WebServiceException(e);
            }
        }
        return getContentType(packet.soapAction);
    }

    public ContentType encode(Packet packet, WritableByteChannel buffer) {
        throw new UnsupportedOperationException();
    }

    public void decode(InputStream in, String contentType, Packet response) throws IOException {
        response.setMessage(_soapCodec.decode(getXMLStreamReader(in)));
    }

    public void decode(ReadableByteChannel in, String contentType, Packet response) {
        throw new UnsupportedOperationException();
    }

    protected abstract StreamHeader createHeader(XMLStreamReader reader, XMLStreamBuffer mark);

    protected abstract ContentType getContentType(String soapAction);

    private XMLStreamWriter getXMLStreamWriter(OutputStream out) {
        if (_serializer != null) {
            _serializer.setOutputStream(out);
            return _serializer;
        } else {
            return _serializer = FastInfosetCodec.createNewStreamWriter(out, _retainState);
        }
    }

    private XMLStreamReader getXMLStreamReader(InputStream in) {
        if (_retainState) {
            if (_statefulParser != null) {
                _statefulParser.setInputStream(in);
                return _statefulParser;
            } else {
                return _statefulParser = FastInfosetCodec.createNewStreamReader(in, _retainState);
            }
        }
        return READER_FACTORY.doCreate(null, in, false);
    }

    /**
     * Creates a new {@link FastInfosetStreamSOAPCodec} instance.
     *
     * @param version the SOAP version of the codec.
     * @return a new {@link FastInfosetStreamSOAPCodec} instance.
     */
    public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version) {
        return create(soapCodec, version, false);
    }

    /**
     * Creates a new {@link FastInfosetStreamSOAPCodec} instance.
     *
     * @param version the SOAP version of the codec.
     * @param retainState if true the Codec should retain the state of
     *        vocabulary tables for multiple encode/decode invocations.
     * @return a new {@link FastInfosetStreamSOAPCodec} instance.
     */
    public static FastInfosetStreamSOAPCodec create(StreamSOAPCodec soapCodec, SOAPVersion version, boolean retainState) {
        if (version == null) throw new IllegalArgumentException();
        switch(version) {
            case SOAP_11:
                return new FastInfosetStreamSOAP11Codec(soapCodec, retainState);
            case SOAP_12:
                return new FastInfosetStreamSOAP12Codec(soapCodec, retainState);
            default:
                throw new AssertionError();
        }
    }
}
