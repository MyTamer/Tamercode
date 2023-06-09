package com.strongauth.strongkeylite.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for batchDelete complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="batchDelete">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="did" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inputfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startin" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "batchDelete", propOrder = { "did", "username", "password", "inputfile", "startin" })
public class BatchDelete {

    protected Long did;

    protected String username;

    protected String password;

    protected String inputfile;

    protected Long startin;

    /**
     * Gets the value of the did property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDid() {
        return did;
    }

    /**
     * Sets the value of the did property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDid(Long value) {
        this.did = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the inputfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInputfile() {
        return inputfile;
    }

    /**
     * Sets the value of the inputfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInputfile(String value) {
        this.inputfile = value;
    }

    /**
     * Gets the value of the startin property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStartin() {
        return startin;
    }

    /**
     * Sets the value of the startin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStartin(Long value) {
        this.startin = value;
    }
}
