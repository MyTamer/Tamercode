package ru.tapublog.lib.gsm0348.api.model;

import java.util.Arrays;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ResponsePacket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponsePacket">
 *   &lt;complexContent>
 *     &lt;extension base="{ru.tapublog.lib.gsm0348}Packet">
 *       &lt;sequence>
 *         &lt;element name="Header" type="{ru.tapublog.lib.gsm0348}ResponsePacketHeader"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponsePacket", propOrder = { "header" })
public class ResponsePacket extends Packet {

    @XmlElement(name = "Header", required = true)
    protected ResponsePacketHeader header;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link ResponsePacketHeader }
     *     
     */
    public ResponsePacketHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponsePacketHeader }
     *     
     */
    public void setHeader(ResponsePacketHeader value) {
        this.header = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((header == null) ? 0 : header.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof ResponsePacket)) return false;
        ResponsePacket other = (ResponsePacket) obj;
        if (header == null) {
            if (other.header != null) return false;
        } else if (!header.equals(other.header)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponsePacket [header=");
        builder.append(header);
        builder.append(", data=");
        builder.append(Arrays.toString(data));
        builder.append("]");
        return builder.toString();
    }
}
