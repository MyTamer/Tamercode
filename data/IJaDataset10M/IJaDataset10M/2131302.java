package es.caib.xml.movilidad.modelo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.caib.xml.movilidad.modelo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _FECHACADUCIDAD_QNAME = new QName("", "FECHA_CADUCIDAD");

    private static final QName _NOMBRE_QNAME = new QName("", "NOMBRE");

    private static final QName _INMEDIATO_QNAME = new QName("", "INMEDIATO");

    private static final QName _EMAILS_QNAME = new QName("", "EMAILS");

    private static final QName _TELEFONOS_QNAME = new QName("", "TELEFONOS");

    private static final QName _FECHAPROGRAMACION_QNAME = new QName("", "FECHA_PROGRAMACION");

    private static final QName _TITULO_QNAME = new QName("", "TITULO");

    private static final QName _TEXTO_QNAME = new QName("", "TEXTO");

    private static final QName _CUENTA_QNAME = new QName("", "CUENTA");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.caib.xml.movilidad.modelo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MENSAJEEMAIL }
     * 
     */
    public MENSAJEEMAIL createMENSAJEEMAIL() {
        return new MENSAJEEMAIL();
    }

    /**
     * Create an instance of {@link MENSAJESMS }
     * 
     */
    public MENSAJESMS createMENSAJESMS() {
        return new MENSAJESMS();
    }

    /**
     * Create an instance of {@link MENSAJES }
     * 
     */
    public MENSAJES createMENSAJES() {
        return new MENSAJES();
    }

    /**
     * Create an instance of {@link ENVIO }
     * 
     */
    public ENVIO createENVIO() {
        return new ENVIO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FECHA_CADUCIDAD")
    public JAXBElement<String> createFECHACADUCIDAD(String value) {
        return new JAXBElement<String>(_FECHACADUCIDAD_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "NOMBRE")
    public JAXBElement<String> createNOMBRE(String value) {
        return new JAXBElement<String>(_NOMBRE_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INMEDIATO")
    public JAXBElement<String> createINMEDIATO(String value) {
        return new JAXBElement<String>(_INMEDIATO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "EMAILS")
    public JAXBElement<String> createEMAILS(String value) {
        return new JAXBElement<String>(_EMAILS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TELEFONOS")
    public JAXBElement<String> createTELEFONOS(String value) {
        return new JAXBElement<String>(_TELEFONOS_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FECHA_PROGRAMACION")
    public JAXBElement<String> createFECHAPROGRAMACION(String value) {
        return new JAXBElement<String>(_FECHAPROGRAMACION_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TITULO")
    public JAXBElement<String> createTITULO(String value) {
        return new JAXBElement<String>(_TITULO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "TEXTO")
    public JAXBElement<String> createTEXTO(String value) {
        return new JAXBElement<String>(_TEXTO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CUENTA")
    public JAXBElement<String> createCUENTA(String value) {
        return new JAXBElement<String>(_CUENTA_QNAME, String.class, null, value);
    }
}
