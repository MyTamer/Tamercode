package com.googlecode.sardine.impl.methods;

import java.net.URI;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.protocol.HTTP;

/**
 * @version $Id: HttpLock.java 290 2011-07-04 17:22:05Z latchkey $
 */
public class HttpLock extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "LOCK";

    public HttpLock(String url) {
        this(URI.create(url));
    }

    public HttpLock(URI url) {
        this.setURI(url);
        this.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml" + HTTP.CHARSET_PARAM + HTTP.UTF_8.toLowerCase());
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    /**
	 * The Depth header may be used with the <code>LOCK</code> method. Values other than <code>0</code> or <code>infinity</code> must not
	 * be used with the Depth header on a <code>LOCK</code> method. All resources that support the <code>LOCK</code>
	 * method must support the Depth header.
	 * <p/>
	 * If no Depth header is submitted on a <code>LOCK</code> request then the request must act as if
	 * a <code>Depth:infinity</code> had been submitted.
	 *
	 * @param depth <code>"0"</code> or <code>"infinity"</code>.
	 */
    public void setDepth(String depth) {
        this.setHeader(HttpHeaders.DEPTH, depth);
    }

    /**
	 * Clients may include Timeout headers in their LOCK requests. However, the server is not required to honor
	 * or even consider these requests.
	 */
    public void setTimeout(int seconds) {
        this.setHeader(HttpHeaders.TIMEOUT, "Second-" + seconds);
    }

    /**
	 * Desires an infinite length lock.
	 */
    public void setInfinite() {
        this.setHeader(HttpHeaders.TIMEOUT, "Infinite");
    }
}
