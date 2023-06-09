package ring.nrapi.business;

import java.io.StringWriter;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import ring.nrapi.data.DataStoreFactory;
import ring.nrapi.data.Persistable;
import ring.nrapi.data.RingConstants;
import ring.nrapi.xml.JAXBAnnotationReader;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
@XmlType(namespace = RingConstants.RING_NAMESPACE, propOrder = { "ID" })
public abstract class AbstractBusinessObject implements BusinessObject {

    private AbstractBusinessObject parent;

    private String docID;

    private String id;

    private boolean storeAsUpdate;

    private boolean referential;

    private boolean isUnique = false;

    public void save() {
        DataStoreFactory.getDefaultStore().storePersistable(this);
    }

    /**
	 * Creates the parent relationships for all child business
	 * objects of this AbstractBusinessObject.
	 */
    public abstract void createChildRelationships();

    @Override
    public Persistable getRoot() {
        if (getParent() == null) {
            return this;
        } else {
            Persistable root = getParent();
            while (root.getParent() != null) {
                root = root.getParent();
            }
            return root;
        }
    }

    @Override
    @XmlTransient
    public Persistable getParent() {
        return parent;
    }

    public void setDocumentID(String id) {
        docID = id;
    }

    @Override
    @XmlTransient
    public String getDocumentID() {
        return docID;
    }

    public void setParent(AbstractBusinessObject obj) {
        parent = obj;
    }

    @XmlAttribute(name = "ref")
    public boolean isReferential() {
        return referential;
    }

    public void setReferential(boolean val) {
        referential = val;
    }

    /**
	 * Returns the objects unique's ID.
	 * @return the id
	 */
    @XmlAttribute(name = "id", required = true)
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    @Override
    public void setStoreAsUpdate(boolean val) {
        storeAsUpdate = val;
    }

    @Override
    public boolean storeAsUpdate() {
        return storeAsUpdate;
    }

    @Override
    public void makeUnique() {
        if (!isUnique) {
            id += UUID.randomUUID().toString();
            isUnique = true;
        }
    }

    public String toXMLDocument() {
        String xml = marshalledXMLDocument();
        String body = xml.substring(xml.indexOf("?>") + 2);
        body = "<ring>" + body + "</ring>";
        String xmlHeader = xml.substring(0, xml.indexOf("?>") + 2);
        return xmlHeader + body;
    }

    @Override
    public String toXML() {
        if (isReferential()) {
            JAXBAnnotationReader reader = new JAXBAnnotationReader(this.getClass());
            String element = reader.rootElementName();
            String xml = "<" + element + " reference=\"true\">";
            xml += "<id>" + getID() + "</id>";
            xml += "</" + element + ">";
            return xml;
        } else {
            return marshalledXMLFragment();
        }
    }

    private String marshalledXMLFragment() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(this.getClass());
            Marshaller m = ctx.createMarshaller();
            StringWriter writer = new StringWriter();
            m.marshal(this, writer);
            String xml = writer.toString();
            return xml.substring(xml.indexOf("?>") + 2);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String marshalledXMLDocument() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(this.getClass());
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            m.marshal(this, writer);
            String xml = writer.toString();
            return xml;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
