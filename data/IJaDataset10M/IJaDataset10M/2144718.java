package quickfixj.codegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Generates Message and Field related code for the various FIX versions.
 * 
 */
public class MessageCodeGenerator {

    private static final String BIGDECIMAL_TYPE_OPTION = "generator.decimal";

    private static final String ORDERED_FIELDS_OPTION = "generator.orderedFields";

    private static final String OVERWRITE_OPTION = "generator.overwrite";

    private static final long SERIAL_UID = 20050617;

    private static final String SERIAL_UID_STR = String.valueOf(SERIAL_UID);

    private static final String XSLPARAM_SERIAL_UID = "serialVersionUID";

    protected void logInfo(String msg) {
        System.out.println(msg);
    }

    protected void logDebug(String msg) {
        System.out.println(msg);
    }

    protected void logError(String msg, Throwable e) {
        System.err.println(msg);
        e.printStackTrace();
    }

    private void generateMessageBaseClass(Task task) throws TransformerConfigurationException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        logInfo(task.getName() + ": generating message base class");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(XSLPARAM_SERIAL_UID, SERIAL_UID_STR);
        generateClassCode(task, "Message", parameters);
    }

    private void generateMessageFactoryClass(Task task) throws TransformerConfigurationException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        generateClassCode(task, "MessageFactory", null);
    }

    private void generateMessageCrackerClass(Task task) throws TransformerConfigurationException, FileNotFoundException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        generateClassCode(task, "MessageCracker", null);
    }

    private void generateClassCode(Task task, String className, Map<String, String> parameters) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerConfigurationException, FileNotFoundException, TransformerException {
        logDebug("generating " + className + " for " + task.getName());
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        parameters.put("messagePackage", task.getMessagePackage());
        parameters.put("fieldPackage", task.getFieldPackage());
        Document document = getSpecification(task);
        generateCodeFile(task, document, parameters, task.getOutputBaseDirectory() + "/" + task.getMessageDirectory() + "/" + className + ".java", createTransformer(task, className + ".xsl"));
    }

    private void generateFieldClasses(Task task) throws ParserConfigurationException, SAXException, IOException {
        String outputDirectory = task.getOutputBaseDirectory() + "/" + task.getFieldDirectory() + "/";
        logInfo(task.getName() + ": generating field classes in " + outputDirectory);
        writePackageDocumentation(outputDirectory, "FIX field definitions for " + task.getName());
        Document document = getSpecification(task);
        List<String> fieldNames = getNames(document.getDocumentElement(), "fields/field");
        try {
            Transformer transformer = createTransformer(task, "Fields.xsl");
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String outputFile = outputDirectory + fieldName + ".java";
                if (!new File(outputFile).exists()) {
                    logDebug("field: " + fieldName);
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("fieldName", fieldName);
                    parameters.put("fieldPackage", task.getFieldPackage());
                    if (task.isDecimalGenerated()) {
                        parameters.put("decimalType", "java.math.BigDecimal");
                        parameters.put("decimalConverter", "Decimal");
                    }
                    parameters.put(XSLPARAM_SERIAL_UID, SERIAL_UID_STR);
                    generateCodeFile(task, document, parameters, outputFile, transformer);
                }
            }
        } catch (Exception e) {
            logError("error while generating field classes", e);
        }
    }

    private void generateMessageSubclasses(Task task) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, FileNotFoundException, TransformerFactoryConfigurationError, TransformerException {
        logInfo(task.getName() + ": generating message subclasses");
        String outputDirectory = task.getOutputBaseDirectory() + "/" + task.getMessageDirectory() + "/";
        writePackageDocumentation(outputDirectory, "Message classes");
        Document document = getSpecification(task);
        List<String> messageNames = getNames(document.getDocumentElement(), "messages/message");
        Transformer transformer = createTransformer(task, "MessageSubclass.xsl");
        for (int i = 0; i < messageNames.size(); i++) {
            String messageName = messageNames.get(i);
            logDebug("generating message class: " + messageName);
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("itemName", messageName);
            parameters.put(XSLPARAM_SERIAL_UID, SERIAL_UID_STR);
            parameters.put("orderedFields", Boolean.toString(task.isOrderedFields()));
            parameters.put("fieldPackage", task.getFieldPackage());
            parameters.put("messagePackage", task.getMessagePackage());
            generateCodeFile(task, document, parameters, outputDirectory + messageName + ".java", transformer);
        }
    }

    private void generateComponentClasses(Task task) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, FileNotFoundException, TransformerFactoryConfigurationError, TransformerException {
        logInfo(task.getName() + ": generating component classes");
        String outputDirectory = task.getOutputBaseDirectory() + "/" + task.getMessageDirectory() + "/component/";
        Document document = getSpecification(task);
        List<String> componentNames = getNames(document.getDocumentElement(), "components/component");
        if (componentNames.size() > 0) {
            writePackageDocumentation(outputDirectory, "Message component classes");
        }
        Transformer transformer = createTransformer(task, "MessageSubclass.xsl");
        for (int i = 0; i < componentNames.size(); i++) {
            String componentName = componentNames.get(i);
            logDebug("generating component class: " + componentName);
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("itemName", componentName);
            parameters.put("baseClass", "quickfix.MessageComponent");
            parameters.put("subpackage", ".component");
            parameters.put("fieldPackage", task.getFieldPackage());
            parameters.put("messagePackage", task.getMessagePackage());
            parameters.put("orderedFields", Boolean.toString(task.isOrderedFields()));
            parameters.put(XSLPARAM_SERIAL_UID, SERIAL_UID_STR);
            generateCodeFile(task, document, parameters, outputDirectory + componentName + ".java", transformer);
        }
    }

    private Transformer createTransformer(Task task, String xsltFile) throws TransformerFactoryConfigurationError, TransformerConfigurationException {
        StreamSource styleSource;
        File xslt = new File(task.getTransformDirectory() + "/" + xsltFile);
        if (xslt.exists()) {
            styleSource = new StreamSource(xslt);
        } else {
            logInfo("Loading predefined xslt file:" + xsltFile);
            styleSource = new StreamSource(this.getClass().getResourceAsStream(xsltFile));
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(styleSource);
        return transformer;
    }

    private Map<String, Document> specificationCache = new HashMap<String, Document>();

    private Document getSpecification(Task task) throws ParserConfigurationException, SAXException, IOException {
        Document document = specificationCache.get(task.getName());
        if (document == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(task.getSpecification());
            specificationCache.put(task.getName(), document);
        }
        return document;
    }

    private void writePackageDocumentation(String outputDirectory, String description) throws FileNotFoundException {
        File packageDescription = new File(outputDirectory + "package.html");
        File parentDirectory = packageDescription.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
        PrintStream out = new PrintStream(new FileOutputStream(packageDescription));
        out.println("<html>");
        out.println("<head><title/></head>");
        out.println("<body>" + description + "</body>");
        out.println("</html>");
        out.close();
    }

    private List<String> getNames(Element element, String path) {
        return getNames(element, path, new ArrayList<String>());
    }

    private List<String> getNames(Element element, String path, List<String> names) {
        int separatorOffset = path.indexOf("/");
        if (separatorOffset == -1) {
            NodeList fieldNodeList = element.getElementsByTagName(path);
            for (int i = 0; i < fieldNodeList.getLength(); i++) {
                names.add(((Element) fieldNodeList.item(i)).getAttribute("name"));
            }
        } else {
            String tag = path.substring(0, separatorOffset);
            NodeList subnodes = element.getElementsByTagName(tag);
            for (int i = 0; i < subnodes.getLength(); i++) {
                getNames((Element) subnodes.item(i), path.substring(separatorOffset + 1), names);
            }
        }
        return names;
    }

    private void generateCodeFile(Task task, Document document, Map<String, String> parameters, String outputFileName, Transformer transformer) throws TransformerFactoryConfigurationError, TransformerConfigurationException, FileNotFoundException, TransformerException {
        if (parameters != null) {
            Iterator<Map.Entry<String, String>> paramItr = parameters.entrySet().iterator();
            while (paramItr.hasNext()) {
                Map.Entry<String, String> entry = paramItr.next();
                transformer.setParameter((String) entry.getKey(), entry.getValue());
            }
        }
        File out = new File(outputFileName);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        File outputFile = new File(outputFileName);
        if (!task.isOverwrite() && outputFile.exists()) {
            return;
        }
        if (outputFile.exists() && (outputFile.lastModified() > task.getSpecificationLastModified())) {
            logDebug("Skipping file " + outputFile.getName());
            return;
        } else {
            logDebug("spec has mod " + task.getSpecificationLastModified() + " output has mod " + outputFile.lastModified());
        }
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(outputFile));
        transformer.transform(source, result);
    }

    public void generate(Task task) {
        try {
            generateFieldClasses(task);
            generateMessageBaseClass(task);
            generateMessageFactoryClass(task);
            generateMessageCrackerClass(task);
            generateComponentClasses(task);
            generateMessageSubclasses(task);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }

    public static class Task {

        private String name;

        private File specification;

        private File outputBaseDirectory;

        private String messagePackage;

        private String fieldPackage;

        private boolean overwrite = true;

        private File transformDirectory;

        private boolean orderedFields;

        private boolean useDecimal;

        private long specificationLastModified;

        public long getSpecificationLastModified() {
            return specificationLastModified;
        }

        public String getName() {
            return name;
        }

        public void setOrderedFields(boolean orderedFields) {
            this.orderedFields = orderedFields;
        }

        public boolean isOrderedFields() {
            return orderedFields;
        }

        public File getTransformDirectory() {
            return transformDirectory;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFieldPackage() {
            return fieldPackage;
        }

        public String getFieldDirectory() {
            return fieldPackage.replace('.', '/');
        }

        public void setFieldPackage(String fieldPackage) {
            this.fieldPackage = fieldPackage;
        }

        public String getMessageDirectory() {
            return messagePackage.replace('.', '/');
        }

        public String getMessagePackage() {
            return messagePackage;
        }

        public void setMessagePackage(String messagePackage) {
            this.messagePackage = messagePackage;
        }

        public File getOutputBaseDirectory() {
            return outputBaseDirectory;
        }

        public void setOutputBaseDirectory(File outputDirectory) {
            this.outputBaseDirectory = outputDirectory;
        }

        public File getSpecification() {
            return specification;
        }

        public void setSpecification(File dictFile) {
            this.specification = dictFile;
            this.specificationLastModified = dictFile.lastModified();
        }

        public boolean isOverwrite() {
            return overwrite;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }

        public void setTransformDirectory(File schemaDirectory) {
            this.transformDirectory = schemaDirectory;
        }

        public void setDecimalGenerated(boolean useDecimal) {
            this.useDecimal = useDecimal;
        }

        public boolean isDecimalGenerated() {
            return useDecimal;
        }
    }

    public static String stripSpaces(String str) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < str.length(); ++i) {
            char t = str.charAt(i);
            if (t == ' ') continue;
            if (t == '.') continue;
            b.append(t);
        }
        return b.toString();
    }

    public static void main(String[] args) {
        MessageCodeGenerator codeGenerator = new MessageCodeGenerator();
        try {
            if (args.length != 3) {
                String classname = MessageCodeGenerator.class.getName();
                System.err.println("usage: " + classname + " specDir xformDir outputBaseDir");
                return;
            }
            boolean overwrite = getOption(OVERWRITE_OPTION, true);
            boolean orderedFields = getOption(ORDERED_FIELDS_OPTION, false);
            boolean useDecimal = getOption(BIGDECIMAL_TYPE_OPTION, false);
            long start = System.currentTimeMillis();
            final String[] vers = new String[] { "FIXT 1.1", "FIX 5.0", "FIX 4.4", "FIX 4.3", "FIX 4.2", "FIX 4.1", "FIX 4.0" };
            for (int i = 0; i < vers.length; ++i) {
                Task task = new Task();
                task.setName(vers[i]);
                final String temp = stripSpaces(vers[i]);
                task.setSpecification(new File(args[0] + "/" + temp + ".xml"));
                task.setTransformDirectory(new File(args[1]));
                task.setMessagePackage("quickfix." + temp.toLowerCase());
                task.setOutputBaseDirectory(new File(args[2]));
                task.setFieldPackage("quickfix.field");
                task.setOverwrite(overwrite);
                task.setOrderedFields(orderedFields);
                task.setDecimalGenerated(useDecimal);
                codeGenerator.generate(task);
            }
            double duration = System.currentTimeMillis() - start;
            DecimalFormat durationFormat = new DecimalFormat("#.###");
            codeGenerator.logInfo("Time for generation: " + durationFormat.format(duration / 1000L) + " seconds");
        } catch (Exception e) {
            codeGenerator.logError("error during code generation", e);
            System.exit(1);
        }
    }

    private static boolean getOption(String key, boolean defaultValue) {
        return System.getProperties().containsKey(key) ? Boolean.getBoolean(key) : defaultValue;
    }
}
