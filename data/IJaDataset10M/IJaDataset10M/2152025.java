package es.caib.xml.movilidad.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}TELEFONOS"/>
 *         &lt;element ref="{}TEXTO"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "telefonos", "texto" })
@XmlRootElement(name = "MENSAJE_SMS")
public class MENSAJESMS {

    @XmlElement(name = "TELEFONOS", required = true)
    protected String telefonos;

    @XmlElement(name = "TEXTO", required = true)
    protected String texto;

    /**
     * Gets the value of the telefonos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTELEFONOS() {
        return telefonos;
    }

    /**
     * Sets the value of the telefonos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTELEFONOS(String value) {
        this.telefonos = value;
    }

    /**
     * Gets the value of the texto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEXTO() {
        return texto;
    }

    /**
     * Sets the value of the texto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEXTO(String value) {
        this.texto = value;
    }
}
