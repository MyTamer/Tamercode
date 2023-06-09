package com.totsp.mavenplugin.gwt.support;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class GwtWebInfProcessor {

    private Document webXml;

    protected File destination;

    protected List servletDescriptors;

    private String moduleName;

    private File moduleFile;

    protected String webXmlPath;

    private HashSet checkedModules = new HashSet();

    protected GwtWebInfProcessor() {
        super();
    }

    /**
    * 
    * @param moduleName
    * @param targetWebXml
    * @param sourceWebXml
    * @param webXmlServletPath
    * @throws Exception
    */
    public GwtWebInfProcessor(String moduleName, String targetWebXml, String sourceWebXml, boolean webXmlServletPath) throws Exception {
        this.moduleName = moduleName;
        this.webXmlPath = sourceWebXml;
        File webXmlFile = new File(sourceWebXml);
        if (!webXmlFile.exists() || !webXmlFile.canRead()) {
            throw new Exception("Unable to locate source web.xml");
        }
        this.destination = new File(targetWebXml);
        if (GwtWebInfProcessor.class.getResource("/" + moduleName.replace('.', '/') + ".gwt.xml") == null) {
            throw new Exception("Unable to locate module definition file: " + moduleName.replace('.', '/') + ".gwt.xml");
        }
        this.servletDescriptors = this.getGwtServletDescriptors(moduleName, webXmlServletPath);
        if (this.servletDescriptors.size() == 0) {
            throw new ExitException("No servlets found.");
        }
    }

    /**
    * 
    * @param moduleName
    * @param moduleDefinition
    * @param targetWebXml
    * @param sourceWebXml
    * @param webXmlServletPath
    * @throws Exception
    */
    public GwtWebInfProcessor(String moduleName, File moduleDefinition, String targetWebXml, String sourceWebXml, boolean webXmlServletPath) throws Exception {
        this.moduleName = moduleName;
        this.webXmlPath = sourceWebXml;
        this.moduleFile = moduleDefinition;
        File webXmlFile = new File(sourceWebXml);
        if (!webXmlFile.exists() || !webXmlFile.canRead()) {
            throw new Exception("Unable to locate source web.xml");
        }
        this.destination = new File(targetWebXml);
        this.servletDescriptors = this.getGwtServletDescriptors(null, webXmlServletPath);
        if (this.servletDescriptors.size() == 0) {
            throw new ExitException("No servlets found.");
        }
    }

    /**
    * Return List of ServletDescriptor from gwt module file.
    *
    * @param gwtModFile
    * @param webXmlServletPath
    * @return
    */
    protected List<ServletDescriptor> getGwtServletDescriptors(String module, boolean webXmlServletPath) throws IOException, JDOMException {
        ArrayList<ServletDescriptor> servletElements = new ArrayList<ServletDescriptor>();
        checkedModules.add(module);
        Document document = null;
        SAXBuilder builder = null;
        try {
            builder = new SAXBuilder(false);
            builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            if (module == null && this.moduleFile != null) {
                document = builder.build(this.moduleFile);
            } else {
                document = builder.build(GwtWebInfProcessor.class.getResourceAsStream("/" + module.replace('.', '/') + ".gwt.xml"));
            }
        } catch (Exception e) {
            try {
                document = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + module.replace('.', '/') + ".gwt.xml"));
            } catch (Exception ee) {
                System.err.println("   Unable to parse module");
                ee.printStackTrace();
                return servletElements;
            }
        }
        Element element = document.getRootElement();
        List inherits = element.getChildren("inherits");
        for (int i = 0; (inherits != null) && (i < inherits.size()); i++) {
            Element inherit = (Element) inherits.get(i);
            if (!checkedModules.contains(inherit.getAttributeValue("name"))) servletElements.addAll(this.getGwtServletDescriptors(inherit.getAttributeValue("name"), webXmlServletPath));
        }
        List servlets = element.getChildren("servlet");
        if (servlets != null && servlets.size() > 0) {
            for (int i = 0; i < servlets.size(); i++) {
                Element servlet = (Element) servlets.get(i);
                String servletPath = null;
                if (webXmlServletPath) {
                    servletPath = servlet.getAttributeValue("path");
                } else {
                    servletPath = "/" + this.moduleName + servlet.getAttributeValue("path");
                }
                String servletClass = servlet.getAttributeValue("class");
                ServletDescriptor servletDesc = new ServletDescriptor(servletPath, servletClass);
                servletElements.add(servletDesc);
            }
        }
        String rename = element.getAttributeValue("rename-to");
        if (rename != null && module == null) {
            for (ServletDescriptor d : servletElements) {
                d.setPath(d.getPath().replaceAll(this.moduleName, rename));
            }
        }
        return servletElements;
    }

    private int getInsertPosition(String[] startAfter, String[] stopBefore) throws JDOMException, IOException {
        Element webapp = this.getWebXml().getRootElement();
        List children = webapp.getContent();
        Content insertAfter = new Comment("inserted by gwt-maven");
        ArrayList namesBefore = new ArrayList();
        ArrayList namesAfter = new ArrayList();
        for (int i = 0; i < startAfter.length; i++) {
            namesBefore.add(startAfter[i]);
        }
        for (int i = 0; i < stopBefore.length; i++) {
            namesAfter.add(stopBefore[i]);
        }
        if ((children == null) || (children.size() == 0)) {
            webapp.addContent(insertAfter);
        } else {
            boolean foundPoint = false;
            ;
            for (int i = 0; !foundPoint && i < children.size(); i++) {
                Object o = children.get(i);
                if (!(o instanceof Element)) {
                    continue;
                }
                Element child = (Element) o;
                if (namesAfter.contains(child.getName())) {
                    webapp.addContent(i, insertAfter);
                    foundPoint = true;
                    break;
                }
                if (!namesBefore.contains(child.getName())) {
                    webapp.addContent(i + 1, insertAfter);
                    foundPoint = true;
                    break;
                }
            }
            if (!foundPoint) {
                webapp.addContent(insertAfter);
            }
        }
        return webapp.indexOf(insertAfter);
    }

    private Document getWebXml() throws JDOMException, IOException {
        if (this.webXml == null) {
            SAXBuilder builder = new SAXBuilder(false);
            builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            return this.webXml = builder.build(new File(this.webXmlPath).toURL());
        } else {
            return this.webXml;
        }
    }

    private void insertServlets() throws JDOMException, IOException {
        Element webapp = this.getWebXml().getRootElement();
        String[] beforeServlets = { "icon", "display-name", "description", "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet" };
        String[] afterServlets = { "servlet-mapping", "session-config", "mime-mapping", "welcome-file-list", "error-page", "taglib", "resource-env-ref", "resource-ref", "security-constraint", "login-config", "security-role", "env-entry", "ejb-ref", "ejb-local-ref" };
        String[] beforeMappings = { "icon", "display-name", "description", "distributable", "context-param", "filter", "filter-mapping", "listener", "servlet", "servlet-mapping" };
        String[] afterMappings = { "session-config", "mime-mapping", "welcome-file-list", "error-page", "taglib", "resource-env-ref", "resource-ref", "security-constraint", "login-config", "security-role", "env-entry", "ejb-ref", "ejb-local-ref" };
        int insertAfter = this.getInsertPosition(beforeServlets, afterServlets);
        for (int i = 0; i < this.servletDescriptors.size(); i++) {
            insertAfter++;
            ServletDescriptor d = (ServletDescriptor) this.servletDescriptors.get(i);
            Element servlet = new Element("servlet", webapp.getNamespace());
            Element servletName = new Element("servlet-name", webapp.getNamespace());
            servletName.setText(d.getName() == null ? d.getClassName() + d.getPath() : d.getName());
            servlet.addContent(servletName);
            Element servletClass = new Element("servlet-class", webapp.getNamespace());
            servletClass.setText(d.getClassName());
            servlet.addContent(servletClass);
            webapp.addContent(insertAfter, servlet);
        }
        insertAfter = this.getInsertPosition(beforeMappings, afterMappings);
        for (int i = 0; i < this.servletDescriptors.size(); i++) {
            insertAfter++;
            ServletDescriptor d = (ServletDescriptor) this.servletDescriptors.get(i);
            Element servletMapping = new Element("servlet-mapping", webapp.getNamespace());
            Element servletName = new Element("servlet-name", webapp.getNamespace());
            servletName.setText(d.getName() == null ? d.getClassName() + d.getPath() : d.getName());
            servletMapping.addContent(servletName);
            Element urlPattern = new Element("url-pattern", webapp.getNamespace());
            urlPattern.setText(d.getPath());
            servletMapping.addContent(urlPattern);
            webapp.addContent(insertAfter, servletMapping);
        }
    }

    public void process() throws Exception {
        this.insertServlets();
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        FileWriter writer = new FileWriter(this.destination);
        out.output(this.webXml, new FileWriter(this.destination));
        writer.flush();
        writer.close();
    }
}
