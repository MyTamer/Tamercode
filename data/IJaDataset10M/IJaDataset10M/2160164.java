package net.sf.istcontract.wsimport.api.model.soap;

import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebMethod;
import net.sf.istcontract.wsimport.api.SOAPVersion;
import net.sf.istcontract.wsimport.api.model.JavaMethod;

/**
 * Models soap:binding in a WSDL document or a {@link javax.jws.soap.SOAPBinding} annotation. This
 * can be the return of {@link JavaMethod#getBinding()}.
 *
 * @author Vivek Pandey
 */
public abstract class SOAPBinding {

    protected Use use = Use.LITERAL;

    protected Style style = Style.DOCUMENT;

    protected SOAPVersion soapVersion = SOAPVersion.SOAP_11;

    protected String soapAction = "";

    /**
     * Get {@link Use} such as <code>literal</code> or <code>encoded</code>.
     */
    public Use getUse() {
        return use;
    }

    /**
     * Get {@link Style} - such as <code>document</code> or <code>rpc</code>.
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Get the {@link SOAPVersion}
     */
    public SOAPVersion getSOAPVersion() {
        return soapVersion;
    }

    /**
     * Returns true if its document/literal
     */
    public boolean isDocLit() {
        return style == Style.DOCUMENT && use == Use.LITERAL;
    }

    /**
     * Returns true if this is a rpc/literal binding
     */
    public boolean isRpcLit() {
        return style == Style.RPC && use == Use.LITERAL;
    }

    /**
     * Value of <code>wsdl:binding/wsdl:operation/soap:operation@soapAction</code> attribute or
     * {@link WebMethod#action()} annotation.
     * <pre>
     * For example:
     * &lt;wsdl:binding name="HelloBinding" type="tns:Hello">
     *   &lt;soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
     *   &lt;wsdl:operation name="echoData">
     *       &lt;soap12:operation soapAction=""/>
     * ...
     * </pre>
     * It's always non-null. soap message serializer needs to generated SOAPAction HTTP header with
     * the return of this method enclosed in quotes("").
     *
     * @see net.sf.istcontract.wsimport.api.message.Packet#soapAction
     */
    public String getSOAPAction() {
        return soapAction;
    }
}
