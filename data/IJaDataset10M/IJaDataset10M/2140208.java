package org.apache.xalan.templates;

import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.res.XSLTErrorResources;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;

/**
 * Class to hold an Attribute Value Template.
 * @xsl.usage advanced
 */
public class AVT implements java.io.Serializable, XSLTVisitable {

    static final long serialVersionUID = 5167607155517042691L;

    /**
    *We are not going to use the object pool if USE_OBJECT_POOL == false.
  */
    private static final boolean USE_OBJECT_POOL = false;

    /**
    * INIT_BUFFER_CHUNK_BITS is used to set initial size of
    * of the char m_array in FastStringBuffer if USE_OBJECT_POOL == false. 
    * size = 2^ INIT_BUFFER_CHUNK_BITS, INIT_BUFFER_CHUNK_BITS = 7 
    * corresponds size = 256. 
  */
    private static final int INIT_BUFFER_CHUNK_BITS = 8;

    /**
   * If the AVT is not complex, just hold the simple string.
   * @serial
   */
    private String m_simpleString = null;

    /**
   * If the AVT is complex, hold a Vector of AVTParts.
   * @serial
   */
    private Vector m_parts = null;

    /**
   * The name of the attribute.
   * @serial
   */
    private String m_rawName;

    /**
   * Get the raw name of the attribute, with the prefix unprocessed.
   *
   * @return non-null reference to prefixed name.
   */
    public String getRawName() {
        return m_rawName;
    }

    /**
   * Get the raw name of the attribute, with the prefix unprocessed.
   *
   * @param rawName non-null reference to prefixed name.
   */
    public void setRawName(String rawName) {
        m_rawName = rawName;
    }

    /**
   * The name of the attribute.
   * @serial
   */
    private String m_name;

    /**
   * Get the local name of the attribute.
   *
   * @return non-null reference to name string.
   */
    public String getName() {
        return m_name;
    }

    /**
   * Set the local name of the attribute.
   *
   * @param name non-null reference to name string.
   */
    public void setName(String name) {
        m_name = name;
    }

    /**
   * The namespace URI of the owning attribute.
   * @serial
   */
    private String m_uri;

    /**
   * Get the namespace URI of the attribute.
   *
   * @return non-null reference to URI, "" if null namespace.
   */
    public String getURI() {
        return m_uri;
    }

    /**
   * Get the namespace URI of the attribute.
   *
   * @param uri non-null reference to URI, "" if null namespace.
   */
    public void setURI(String uri) {
        m_uri = uri;
    }

    /**
   * Construct an AVT by parsing the string, and either
   * constructing a vector of AVTParts, or simply hold
   * on to the string if the AVT is simple.
   *
   * @param handler non-null reference to StylesheetHandler that is constructing.
   * @param uri non-null reference to URI, "" if null namespace.
   * @param name  non-null reference to name string.
   * @param rawName prefixed name.
   * @param stringedValue non-null raw string value.
   *
   * @throws javax.xml.transform.TransformerException
   */
    public AVT(StylesheetHandler handler, String uri, String name, String rawName, String stringedValue, ElemTemplateElement owner) throws javax.xml.transform.TransformerException {
        m_uri = uri;
        m_name = name;
        m_rawName = rawName;
        StringTokenizer tokenizer = new StringTokenizer(stringedValue, "{}\"\'", true);
        int nTokens = tokenizer.countTokens();
        if (nTokens < 2) {
            m_simpleString = stringedValue;
        } else {
            FastStringBuffer buffer = null;
            FastStringBuffer exprBuffer = null;
            if (USE_OBJECT_POOL) {
                buffer = StringBufferPool.get();
                exprBuffer = StringBufferPool.get();
            } else {
                buffer = new FastStringBuffer(6);
                exprBuffer = new FastStringBuffer(6);
            }
            try {
                m_parts = new Vector(nTokens + 1);
                String t = null;
                String lookahead = null;
                String error = null;
                while (tokenizer.hasMoreTokens()) {
                    if (lookahead != null) {
                        t = lookahead;
                        lookahead = null;
                    } else t = tokenizer.nextToken();
                    if (t.length() == 1) {
                        switch(t.charAt(0)) {
                            case ('\"'):
                            case ('\''):
                                {
                                    buffer.append(t);
                                    break;
                                }
                            case ('{'):
                                {
                                    try {
                                        lookahead = tokenizer.nextToken();
                                        if (lookahead.equals("{")) {
                                            buffer.append(lookahead);
                                            lookahead = null;
                                            break;
                                        } else {
                                            if (buffer.length() > 0) {
                                                m_parts.addElement(new AVTPartSimple(buffer.toString()));
                                                buffer.setLength(0);
                                            }
                                            exprBuffer.setLength(0);
                                            while (null != lookahead) {
                                                if (lookahead.length() == 1) {
                                                    switch(lookahead.charAt(0)) {
                                                        case '\'':
                                                        case '\"':
                                                            {
                                                                exprBuffer.append(lookahead);
                                                                String quote = lookahead;
                                                                lookahead = tokenizer.nextToken();
                                                                while (!lookahead.equals(quote)) {
                                                                    exprBuffer.append(lookahead);
                                                                    lookahead = tokenizer.nextToken();
                                                                }
                                                                exprBuffer.append(lookahead);
                                                                lookahead = tokenizer.nextToken();
                                                                break;
                                                            }
                                                        case '{':
                                                            {
                                                                error = XSLMessages.createMessage(XSLTErrorResources.ER_NO_CURLYBRACE, null);
                                                                lookahead = null;
                                                                break;
                                                            }
                                                        case '}':
                                                            {
                                                                buffer.setLength(0);
                                                                XPath xpath = handler.createXPath(exprBuffer.toString(), owner);
                                                                m_parts.addElement(new AVTPartXPath(xpath));
                                                                lookahead = null;
                                                                break;
                                                            }
                                                        default:
                                                            {
                                                                exprBuffer.append(lookahead);
                                                                lookahead = tokenizer.nextToken();
                                                            }
                                                    }
                                                } else {
                                                    exprBuffer.append(lookahead);
                                                    lookahead = tokenizer.nextToken();
                                                }
                                            }
                                            if (error != null) {
                                                break;
                                            }
                                        }
                                        break;
                                    } catch (java.util.NoSuchElementException ex) {
                                        error = XSLMessages.createMessage(XSLTErrorResources.ER_ILLEGAL_ATTRIBUTE_VALUE, new Object[] { name, stringedValue });
                                        break;
                                    }
                                }
                            case ('}'):
                                {
                                    lookahead = tokenizer.nextToken();
                                    if (lookahead.equals("}")) {
                                        buffer.append(lookahead);
                                        lookahead = null;
                                    } else {
                                        try {
                                            handler.warn(XSLTErrorResources.WG_FOUND_CURLYBRACE, null);
                                        } catch (org.xml.sax.SAXException se) {
                                            throw new TransformerException(se);
                                        }
                                        buffer.append("}");
                                    }
                                    break;
                                }
                            default:
                                {
                                    buffer.append(t);
                                }
                        }
                    } else {
                        buffer.append(t);
                    }
                    if (null != error) {
                        try {
                            handler.warn(XSLTErrorResources.WG_ATTR_TEMPLATE, new Object[] { error });
                        } catch (org.xml.sax.SAXException se) {
                            throw new TransformerException(se);
                        }
                        break;
                    }
                }
                if (buffer.length() > 0) {
                    m_parts.addElement(new AVTPartSimple(buffer.toString()));
                    buffer.setLength(0);
                }
            } finally {
                if (USE_OBJECT_POOL) {
                    StringBufferPool.free(buffer);
                    StringBufferPool.free(exprBuffer);
                } else {
                    buffer = null;
                    exprBuffer = null;
                }
                ;
            }
        }
        if (null == m_parts && (null == m_simpleString)) {
            m_simpleString = "";
        }
    }

    /**
   * Get the AVT as the original string.
   *
   * @return The AVT as the original string
   */
    public String getSimpleString() {
        if (null != m_simpleString) {
            return m_simpleString;
        } else if (null != m_parts) {
            final FastStringBuffer buf = getBuffer();
            String out = null;
            int n = m_parts.size();
            try {
                for (int i = 0; i < n; i++) {
                    AVTPart part = (AVTPart) m_parts.elementAt(i);
                    buf.append(part.getSimpleString());
                }
                out = buf.toString();
            } finally {
                if (USE_OBJECT_POOL) {
                    StringBufferPool.free(buf);
                } else {
                    buf.setLength(0);
                }
                ;
            }
            return out;
        } else {
            return "";
        }
    }

    /**
   * Evaluate the AVT and return a String.
   *
   * @param xctxt Te XPathContext to use to evaluate this.
   * @param context The current source tree context.
   * @param nsNode The current namespace context (stylesheet tree context).
   *
   * @return The AVT evaluated as a string
   *
   * @throws javax.xml.transform.TransformerException
   */
    public String evaluate(XPathContext xctxt, int context, org.apache.xml.utils.PrefixResolver nsNode) throws javax.xml.transform.TransformerException {
        if (null != m_simpleString) {
            return m_simpleString;
        } else if (null != m_parts) {
            final FastStringBuffer buf = getBuffer();
            String out = null;
            int n = m_parts.size();
            try {
                for (int i = 0; i < n; i++) {
                    AVTPart part = (AVTPart) m_parts.elementAt(i);
                    part.evaluate(xctxt, buf, context, nsNode);
                }
                out = buf.toString();
            } finally {
                if (USE_OBJECT_POOL) {
                    StringBufferPool.free(buf);
                } else {
                    buf.setLength(0);
                }
            }
            return out;
        } else {
            return "";
        }
    }

    /**
   * Test whether the AVT is insensitive to the context in which
   *  it is being evaluated. This is intended to facilitate
   *  compilation of templates, by allowing simple AVTs to be
   *  converted back into strings.
   *
   *  Currently the only case we recognize is simple strings.
   * ADDED 9/5/2000 to support compilation experiment
   *
   * @return True if the m_simpleString member of this AVT is not null
   */
    public boolean isContextInsensitive() {
        return null != m_simpleString;
    }

    /**
   * Tell if this expression or it's subexpressions can traverse outside
   * the current subtree.
   *
   * @return true if traversal outside the context node's subtree can occur.
   */
    public boolean canTraverseOutsideSubtree() {
        if (null != m_parts) {
            int n = m_parts.size();
            for (int i = 0; i < n; i++) {
                AVTPart part = (AVTPart) m_parts.elementAt(i);
                if (part.canTraverseOutsideSubtree()) return true;
            }
        }
        return false;
    }

    /**
   * This function is used to fixup variables from QNames to stack frame 
   * indexes at stylesheet build time.
   * @param vars List of QNames that correspond to variables.  This list 
   * should be searched backwards for the first qualified name that 
   * corresponds to the variable reference qname.  The position of the 
   * QName in the vector from the start of the vector will be its position 
   * in the stack frame (but variables above the globalsTop value will need 
   * to be offset to the current stack frame).
   */
    public void fixupVariables(java.util.Vector vars, int globalsSize) {
        if (null != m_parts) {
            int n = m_parts.size();
            for (int i = 0; i < n; i++) {
                AVTPart part = (AVTPart) m_parts.elementAt(i);
                part.fixupVariables(vars, globalsSize);
            }
        }
    }

    /**
   * @see XSLTVisitable#callVisitors(XSLTVisitor)
   */
    public void callVisitors(XSLTVisitor visitor) {
        if (visitor.visitAVT(this) && (null != m_parts)) {
            int n = m_parts.size();
            for (int i = 0; i < n; i++) {
                AVTPart part = (AVTPart) m_parts.elementAt(i);
                part.callVisitors(visitor);
            }
        }
    }

    /**
   * Returns true if this AVT is simple
   */
    public boolean isSimple() {
        return m_simpleString != null;
    }

    private final FastStringBuffer getBuffer() {
        if (USE_OBJECT_POOL) {
            return StringBufferPool.get();
        } else {
            return new FastStringBuffer(INIT_BUFFER_CHUNK_BITS);
        }
    }
}
