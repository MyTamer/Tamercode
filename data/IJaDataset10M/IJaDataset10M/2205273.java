package cruise.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}Properties"/>
 *         &lt;element ref="{}GenerationUnits"/>
 *         &lt;element ref="{}Files"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UIFactory" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="OutputFolder" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UmpleFile" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "properties", "generationUnits", "files" })
@XmlRootElement(name = "UmpleProject")
public class UmpleProject {

    @XmlElement(name = "Properties", required = true)
    protected Properties properties;

    @XmlElement(name = "GenerationUnits", required = true)
    protected GenerationUnits generationUnits;

    @XmlElement(name = "Files", required = true)
    protected Files files;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "UIFactory", required = true)
    protected String uiFactory;

    @XmlAttribute(name = "OutputFolder", required = true)
    protected String outputFolder;

    @XmlAttribute(name = "UmpleFile", required = true)
    protected String umpleFile;

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link Properties }
     *     
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link Properties }
     *     
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * Gets the value of the generationUnits property.
     * 
     * @return
     *     possible object is
     *     {@link GenerationUnits }
     *     
     */
    public GenerationUnits getGenerationUnits() {
        return generationUnits;
    }

    /**
     * Sets the value of the generationUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenerationUnits }
     *     
     */
    public void setGenerationUnits(GenerationUnits value) {
        this.generationUnits = value;
    }

    /**
     * Gets the value of the files property.
     * 
     * @return
     *     possible object is
     *     {@link Files }
     *     
     */
    public Files getFiles() {
        return files;
    }

    /**
     * Sets the value of the files property.
     * 
     * @param value
     *     allowed object is
     *     {@link Files }
     *     
     */
    public void setFiles(Files value) {
        this.files = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the uiFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUIFactory() {
        return uiFactory;
    }

    /**
     * Sets the value of the uiFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUIFactory(String value) {
        this.uiFactory = value;
    }

    /**
     * Gets the value of the outputFolder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputFolder() {
        return outputFolder;
    }

    /**
     * Sets the value of the outputFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputFolder(String value) {
        this.outputFolder = value;
    }

    /**
     * Gets the value of the umpleFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUmpleFile() {
        return umpleFile;
    }

    /**
     * Sets the value of the umpleFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUmpleFile(String value) {
        this.umpleFile = value;
    }
}
