package fr.cnes.sitools.dictionary;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import com.thoughtworks.xstream.XStream;
import fr.cnes.sitools.common.SitoolsResource;
import fr.cnes.sitools.common.XStreamFactory;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.common.store.SitoolsStore;
import fr.cnes.sitools.dictionary.model.ConceptTemplate;
import fr.cnes.sitools.util.Property;

/**
 * Base resource for concept template management
 * 
 * @author c
 * 
 */
public abstract class AbstractConceptTemplateResource extends SitoolsResource {

    /** Store */
    private SitoolsStore<ConceptTemplate> store = null;

    /** Template id in the request */
    private String templateId = null;

    /** Property id in the request */
    private String propertyId = null;

    @Override
    public void doInit() {
        super.doInit();
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        getVariants().add(new Variant(MediaType.APPLICATION_JAVA_OBJECT));
        store = ((ConceptTemplateAdministration) getApplication()).getStore();
        templateId = (String) this.getRequest().getAttributes().get("templateId");
        propertyId = (String) this.getRequest().getAttributes().get("propertyId");
    }

    /**
   * Configure XStream mapping for xml and json serialization
   * 
   * TODO Optimisation possible au lieu de créer à chaque fois une instance de XStream conserver 4 instances d'XStream
   * correspondant aux 4 combinaisons possibles : classe retournée (ConceptTemplate, Property) et type (data / item)
   * 
   * @param xstream
   *          XStream
   * @param response
   *          Response
   */
    @Override
    public void configure(XStream xstream, Response response) {
        super.configure(xstream, response);
        xstream.alias("template", ConceptTemplate.class);
        xstream.alias("property", Property.class);
    }

    /**
   * Decodes a representation to a ConceptTemplate object.
   * 
   * @param representation
   *          Representation
   * @param variant
   *          Variant
   * @return ConceptTemplate
   */
    public final ConceptTemplate getObject(Representation representation, Variant variant) {
        ConceptTemplate templateInput = null;
        if (MediaType.APPLICATION_XML.isCompatible(representation.getMediaType())) {
            XstreamRepresentation<ConceptTemplate> repXML = new XstreamRepresentation<ConceptTemplate>(representation);
            XStream xstream = XStreamFactory.getInstance().getXStreamReader(MediaType.APPLICATION_XML);
            xstream.autodetectAnnotations(false);
            xstream.alias("template", ConceptTemplate.class);
            xstream.alias("property", Property.class);
            repXML.setXstream(xstream);
            templateInput = repXML.getObject();
        } else if (MediaType.APPLICATION_JSON.isCompatible(representation.getMediaType())) {
            templateInput = new JacksonRepresentation<ConceptTemplate>(representation, ConceptTemplate.class).getObject();
        }
        return templateInput;
    }

    /**
   * Gets the propertyId value
   * @return the propertyId
   */
    public final String getPropertyId() {
        return propertyId;
    }

    /**
   * Gets the templateId value
   * @return the templateId
   */
    public final String getConceptTemplateId() {
        return templateId;
    }

    /**
   * Gets the store value
   * @return the store
   */
    public final SitoolsStore<ConceptTemplate> getStore() {
        return store;
    }
}
