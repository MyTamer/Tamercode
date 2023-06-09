package org.apache.axis2.rmi.databind;

import junit.framework.TestCase;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axis2.rmi.Configurator;
import org.apache.axis2.rmi.exception.MetaDataPopulateException;
import org.apache.axis2.rmi.exception.SchemaGenerationException;
import org.apache.axis2.rmi.exception.XmlParsingException;
import org.apache.axis2.rmi.exception.XmlSerializingException;
import org.apache.axis2.rmi.metadata.Parameter;
import org.apache.axis2.rmi.util.NamespacePrefix;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class DataBindTest extends TestCase {

    protected JavaObjectSerializer javaObjectSerializer;

    protected XmlStreamParser xmlStreamParser;

    protected Configurator configurator;

    protected Map processedMap;

    protected Map schemaMap;

    protected void setUp() throws Exception {
        this.configurator = new Configurator();
        this.processedMap = new HashMap();
        this.schemaMap = new HashMap();
        this.javaObjectSerializer = new JavaObjectSerializer(this.processedMap, this.configurator, this.schemaMap);
        this.xmlStreamParser = new XmlStreamParser(this.processedMap, this.configurator, this.schemaMap);
    }

    protected Object getReturnObject(Parameter parameter, Object inputObject) {
        Object reutnObject = null;
        try {
            parameter.populateMetaData(configurator, processedMap);
            parameter.generateSchema(configurator, schemaMap);
            StringWriter stringWriter = new StringWriter();
            XMLStreamWriter xmlStreamWriter = StAXUtils.createXMLStreamWriter(stringWriter);
            javaObjectSerializer.serializeParameter(inputObject, parameter, xmlStreamWriter, new NamespacePrefix());
            xmlStreamWriter.flush();
            String xmlString = stringWriter.toString();
            System.out.println("Xml String ==> " + xmlString);
            XMLStreamReader reader = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(xmlString.getBytes()));
            reutnObject = xmlStreamParser.getObjectForParameter(reader, parameter);
        } catch (MetaDataPopulateException e) {
            fail();
        } catch (SchemaGenerationException e) {
            fail();
        } catch (XMLStreamException e) {
            fail();
        } catch (XmlSerializingException e) {
            fail();
        } catch (XmlParsingException e) {
            e.printStackTrace();
            fail();
        }
        return reutnObject;
    }
}
