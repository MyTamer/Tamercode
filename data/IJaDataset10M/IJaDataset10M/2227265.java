package org.apache.axis2.wsdl.codegen.emitter;

import org.apache.axis2.description.AxisBindingOperation;
import org.apache.axis2.description.AxisMessage;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.util.JavaUtils;
import org.apache.axis2.util.PolicyUtil;
import org.apache.axis2.util.Utils;
import org.apache.axis2.wsdl.HTTPHeaderMessage;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.wsdl.WSDLUtil;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.wsdl.SOAPHeaderMessage;
import org.apache.axis2.wsdl.codegen.CodeGenConfiguration;
import org.apache.axis2.wsdl.codegen.CodeGenerationException;
import org.apache.axis2.wsdl.codegen.writer.CBuildScriptWriter;
import org.apache.axis2.wsdl.codegen.writer.CServiceXMLWriter;
import org.apache.axis2.wsdl.codegen.writer.CSkelHeaderWriter;
import org.apache.axis2.wsdl.codegen.writer.CSkelSourceWriter;
import org.apache.axis2.wsdl.codegen.writer.CStubHeaderWriter;
import org.apache.axis2.wsdl.codegen.writer.CStubSourceWriter;
import org.apache.axis2.wsdl.codegen.writer.CSvcSkeletonWriter;
import org.apache.axis2.wsdl.codegen.writer.FileWriter;
import org.apache.axis2.wsdl.databinding.CUtils;
import org.apache.neethi.Policy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.ibm.wsdl.util.xml.DOM2Writer;

public class CEmitter extends AxisServiceBasedMultiLanguageEmitter {

    protected static final String C_STUB_PREFIX = "axis2_stub_";

    protected static final String C_SKEL_PREFIX = "axis2_skel_";

    protected static final String C_SVC_SKEL_PREFIX = "axis2_svc_skel_";

    protected static final String C_STUB_SUFFIX = "";

    protected static final String C_SKEL_SUFFIX = "";

    protected static final String C_SVC_SKEL_SUFFIX = "";

    protected static final String JAVA_DEFAULT_TYPE = "org.apache.axiom.om.OMElement";

    protected static final String C_DEFAULT_TYPE = "axiom_node_t*";

    protected static final String C_OUR_TYPE_PREFIX = "axis2_";

    protected static final String C_OUR_TYPE_SUFFIX = "_t*";

    /**
     * Emit the stub
     *
     * @throws CodeGenerationException
     */
    public void emitStub() throws CodeGenerationException {
        try {
            writeCStub();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Emit the skeltons
     *
     * @throws CodeGenerationException
     */
    public void emitSkeleton() throws CodeGenerationException {
        try {
            writeCSkel();
            writeCServiceSkeleton();
            emitBuildScript();
            writeServiceXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Emit the build script
     *
     * @throws CodeGenerationException
     */
    public void emitBuildScript() throws CodeGenerationException {
        try {
            writeBuildScript();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the Stub.
     *
     * @throws Exception
     */
    protected void writeCStub() throws Exception {
        Document interfaceImplModel = createDOMDocumentForInterfaceImplementation();
        CStubHeaderWriter writerHStub = new CStubHeaderWriter(getOutputDirectory(codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), codeGenConfiguration.getOutputLanguage());
        writeFile(interfaceImplModel, writerHStub);
        CStubSourceWriter writerCStub = new CStubSourceWriter(getOutputDirectory(codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), codeGenConfiguration.getOutputLanguage());
        writeFile(interfaceImplModel, writerCStub);
    }

    /**
     * Writes the Skel.
     *
     * @throws Exception
     */
    protected void writeCSkel() throws Exception {
        Document skeletonModel = createDOMDocumentForSkeleton(codeGenConfiguration.isServerSideInterface());
        CSkelHeaderWriter skeletonWriter = new CSkelHeaderWriter(getOutputDirectory(this.codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), this.codeGenConfiguration.getOutputLanguage());
        writeFile(skeletonModel, skeletonWriter);
        CSkelSourceWriter skeletonWriterStub = new CSkelSourceWriter(getOutputDirectory(this.codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), this.codeGenConfiguration.getOutputLanguage());
        writeFile(skeletonModel, skeletonWriterStub);
    }

    /** @throws Exception  */
    protected void writeCServiceSkeleton() throws Exception {
        Document skeletonModel = createDOMDocumentForServiceSkeletonXML();
        CSvcSkeletonWriter writer = new CSvcSkeletonWriter(getOutputDirectory(codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), codeGenConfiguration.getOutputLanguage());
        writeFile(skeletonModel, writer);
    }

    /**
     * Write the Build Script
     *
     * @throws Exception
     */
    protected void writeBuildScript() throws Exception {
        if (this.codeGenConfiguration.isGenerateDeployementDescriptor()) {
            Document buildXMLModel = createDOMDocumentForBuildScript(this.codeGenConfiguration);
            FileWriter buildXmlWriter = new CBuildScriptWriter(getOutputDirectory(this.codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getSourceLocation()), this.codeGenConfiguration.getOutputLanguage());
            writeFile(buildXMLModel, buildXmlWriter);
        }
    }

    protected void writeServiceXml() throws Exception {
        if (this.codeGenConfiguration.isGenerateDeployementDescriptor()) {
            Document serviceXMLModel = createDOMDocumentForServiceXML();
            FileWriter serviceXmlWriter = new CServiceXMLWriter(getOutputDirectory(this.codeGenConfiguration.getOutputLocation(), codeGenConfiguration.getResourceLocation()), this.codeGenConfiguration.getOutputLanguage());
            writeFile(serviceXMLModel, serviceXmlWriter);
        }
    }

    /** Creates the DOM tree for implementations. */
    protected Document createDOMDocumentForInterfaceImplementation() throws Exception {
        String serviceName = axisService.getName();
        String serviceTns = axisService.getTargetNamespace();
        String serviceCName = makeCClassName(axisService.getName());
        String stubName = C_STUB_PREFIX + serviceCName + C_STUB_SUFFIX;
        Document doc = getEmptyDocument();
        Element rootElement = doc.createElement("class");
        addAttribute(doc, "name", stubName, rootElement);
        addAttribute(doc, "prefix", stubName, rootElement);
        addAttribute(doc, "qname", serviceName + "|" + serviceTns, rootElement);
        addAttribute(doc, "servicename", serviceCName, rootElement);
        addAttribute(doc, "package", "", rootElement);
        addAttribute(doc, "namespace", serviceTns, rootElement);
        addAttribute(doc, "interfaceName", serviceCName, rootElement);
        if (codeGenConfiguration.isPackClasses()) {
            addAttribute(doc, "wrapped", "yes", rootElement);
        }
        addSoapVersion(doc, rootElement);
        addEndpoint(doc, rootElement);
        fillSyncAttributes(doc, rootElement);
        if (mapper.isObjectMappingPresent()) {
            addAttribute(doc, "skip-write", "yes", rootElement);
            processModelObjects(mapper.getAllMappedObjects(), rootElement, doc);
        }
        loadOperations(doc, rootElement, null);
        rootElement.appendChild(createDOMElementforDatabinders(doc, false));
        Object stubMethods;
        if ((stubMethods = codeGenConfiguration.getProperty("stubMethods")) != null) {
            rootElement.appendChild(doc.importNode((Element) stubMethods, true));
        }
        rootElement.appendChild(getUniqueListofFaults(doc));
        doc.appendChild(rootElement);
        return doc;
    }

    protected Document createDOMDocumentForSkeleton(boolean isSkeletonInterface) {
        Document doc = getEmptyDocument();
        Element rootElement = doc.createElement("interface");
        String serviceCName = makeCClassName(axisService.getName());
        String skelName = C_SKEL_PREFIX + serviceCName + C_SKEL_SUFFIX;
        addAttribute(doc, "name", skelName, rootElement);
        addAttribute(doc, "package", "", rootElement);
        String serviceName = axisService.getName();
        String serviceTns = axisService.getTargetNamespace();
        addAttribute(doc, "prefix", skelName, rootElement);
        addAttribute(doc, "qname", serviceName + "|" + serviceTns, rootElement);
        fillSyncAttributes(doc, rootElement);
        loadOperations(doc, rootElement, null);
        rootElement.appendChild(getUniqueListofFaults(doc));
        doc.appendChild(rootElement);
        return doc;
    }

    protected Document createDOMDocumentForServiceSkeletonXML() {
        Document doc = getEmptyDocument();
        Element rootElement = doc.createElement("interface");
        String localPart = makeCClassName(axisService.getName());
        String svcSkelName = C_SVC_SKEL_PREFIX + localPart + C_SVC_SKEL_SUFFIX;
        String skelName = C_SKEL_PREFIX + localPart + C_SKEL_SUFFIX;
        addAttribute(doc, "name", svcSkelName, rootElement);
        addAttribute(doc, "prefix", svcSkelName, rootElement);
        String serviceName = axisService.getName();
        String serviceTns = axisService.getTargetNamespace();
        addAttribute(doc, "qname", serviceName + "|" + serviceTns, rootElement);
        addAttribute(doc, "svcname", skelName, rootElement);
        addAttribute(doc, "svcop_prefix", skelName, rootElement);
        addAttribute(doc, "package", "", rootElement);
        fillSyncAttributes(doc, rootElement);
        loadOperations(doc, rootElement, null);
        addSoapVersion(doc, rootElement);
        rootElement.appendChild(getUniqueListofFaults(doc));
        doc.appendChild(rootElement);
        return doc;
    }

    protected Document createDOMDocumentForBuildScript(CodeGenConfiguration codegen) {
        Document doc = getEmptyDocument();
        Element rootElement = doc.createElement("interface");
        String serviceCName = makeCClassName(axisService.getName());
        addAttribute(doc, "servicename", serviceCName, rootElement);
        if (codegen.isSetoutputSourceLocation()) {
            String outputLocation = codegen.getOutputLocation().getPath();
            String targetsourceLocation = codegen.getSourceLocation();
            addAttribute(doc, "option", "1", rootElement);
            addAttribute(doc, "outputlocation", outputLocation, rootElement);
            addAttribute(doc, "targetsourcelocation", targetsourceLocation, rootElement);
        } else {
            addAttribute(doc, "option", "0", rootElement);
        }
        fillSyncAttributes(doc, rootElement);
        loadOperations(doc, rootElement, null);
        addSoapVersion(doc, rootElement);
        rootElement.appendChild(getUniqueListofFaults(doc));
        doc.appendChild(rootElement);
        return doc;
    }

    /**
     * @param word
     * @return Returns character removed string.
     */
    protected String makeCClassName(String word) {
        if (CUtils.isCKeyword(word)) {
            return CUtils.makeNonCKeyword(word);
        }
        String outWord = word.replace('.', '_');
        return outWord.replace('-', '_');
    }

    /**
     * Loads the operations
     *
     * @param doc
     * @param rootElement
     * @param mep
     * @return operations found
     */
    protected boolean loadOperations(Document doc, Element rootElement, String mep) {
        Element methodElement;
        String portTypeName = makeCClassName(axisService.getName());
        Iterator bindingOperations = this.axisBinding.getChildren();
        boolean opsFound = false;
        AxisOperation axisOperation = null;
        AxisBindingOperation axisBindingOperation = null;
        while (bindingOperations.hasNext()) {
            axisBindingOperation = (AxisBindingOperation) bindingOperations.next();
            axisOperation = axisBindingOperation.getAxisOperation();
            String messageExchangePattern = axisOperation.getMessageExchangePattern();
            if (infoHolder.get(messageExchangePattern) == null) {
                infoHolder.put(messageExchangePattern, Boolean.TRUE);
            }
            if (mep == null) {
                opsFound = true;
                List soapHeaderInputParameterList = new ArrayList();
                List soapHeaderOutputParameterList = new ArrayList();
                methodElement = doc.createElement("method");
                String localPart = axisOperation.getName().getLocalPart();
                String opCName = makeCClassName(localPart);
                String opNS = axisOperation.getName().getNamespaceURI();
                addAttribute(doc, "name", opCName, methodElement);
                addAttribute(doc, "localpart", localPart, methodElement);
                addAttribute(doc, "qname", localPart + "|" + opNS, methodElement);
                addAttribute(doc, "namespace", opNS, methodElement);
                String style = axisOperation.getStyle();
                addAttribute(doc, "style", style, methodElement);
                addAttribute(doc, "dbsupportname", portTypeName + localPart + DATABINDING_SUPPORTER_NAME_SUFFIX, methodElement);
                addAttribute(doc, "mep", Utils.getAxisSpecifMEPConstant(axisOperation.getMessageExchangePattern()) + "", methodElement);
                addAttribute(doc, "mepURI", axisOperation.getMessageExchangePattern(), methodElement);
                addSOAPAction(doc, methodElement, axisBindingOperation.getName());
                addHeaderOperations(soapHeaderInputParameterList, axisBindingOperation, true);
                addHeaderOperations(soapHeaderOutputParameterList, axisBindingOperation, false);
                PolicyInclude policyInclude = axisOperation.getPolicyInclude();
                Policy policy = policyInclude.getPolicy();
                if (policy != null) {
                    try {
                        addAttribute(doc, "policy", PolicyUtil.policyComponentToString(policy), methodElement);
                    } catch (Exception ex) {
                        throw new RuntimeException("can't serialize the policy to a String ", ex);
                    }
                }
                methodElement.appendChild(getInputElement(doc, axisBindingOperation, soapHeaderInputParameterList));
                methodElement.appendChild(getOutputElement(doc, axisBindingOperation, soapHeaderOutputParameterList));
                methodElement.appendChild(getFaultElement(doc, axisOperation));
                rootElement.appendChild(methodElement);
            } else {
                if (mep.equals(axisOperation.getMessageExchangePattern())) {
                    opsFound = true;
                    List soapHeaderInputParameterList = new ArrayList();
                    List soapHeaderOutputParameterList = new ArrayList();
                    List soapHeaderFaultParameterList = new ArrayList();
                    methodElement = doc.createElement("method");
                    String localPart = axisOperation.getName().getLocalPart();
                    String opCName = makeCClassName(localPart);
                    String opNS = axisOperation.getName().getNamespaceURI();
                    addAttribute(doc, "name", opCName, methodElement);
                    addAttribute(doc, "localpart", localPart, methodElement);
                    addAttribute(doc, "qname", localPart + "|" + opNS, methodElement);
                    addAttribute(doc, "namespace", axisOperation.getName().getNamespaceURI(), methodElement);
                    addAttribute(doc, "style", axisOperation.getStyle(), methodElement);
                    addAttribute(doc, "dbsupportname", portTypeName + localPart + DATABINDING_SUPPORTER_NAME_SUFFIX, methodElement);
                    addAttribute(doc, "mep", Utils.getAxisSpecifMEPConstant(axisOperation.getMessageExchangePattern()) + "", methodElement);
                    addAttribute(doc, "mepURI", axisOperation.getMessageExchangePattern(), methodElement);
                    addSOAPAction(doc, methodElement, axisBindingOperation.getName());
                    addHeaderOperations(soapHeaderInputParameterList, axisBindingOperation, true);
                    addHeaderOperations(soapHeaderOutputParameterList, axisBindingOperation, false);
                    Policy policy = axisOperation.getPolicyInclude().getPolicy();
                    if (policy != null) {
                        try {
                            addAttribute(doc, "policy", PolicyUtil.policyComponentToString(policy), methodElement);
                        } catch (Exception ex) {
                            throw new RuntimeException("can't serialize the policy to a String", ex);
                        }
                    }
                    methodElement.appendChild(getInputElement(doc, axisBindingOperation, soapHeaderInputParameterList));
                    methodElement.appendChild(getOutputElement(doc, axisBindingOperation, soapHeaderOutputParameterList));
                    methodElement.appendChild(getFaultElement(doc, axisOperation));
                    rootElement.appendChild(methodElement);
                }
            }
        }
        return opsFound;
    }

    /**
     * A convenient method for the generating the parameter element
     *
     * @param doc
     * @param paramName
     * @param paramType
     * @param opName
     * @param paramName
     */
    protected Element generateParamComponent(Document doc, String paramName, String paramType, QName opName, String partName, boolean isPrimitive) {
        Element paramElement = doc.createElement("param");
        addAttribute(doc, "name", paramName, paramElement);
        String typeMappingStr = (paramType == null) ? "" : paramType;
        if (JAVA_DEFAULT_TYPE.equals(typeMappingStr)) {
            typeMappingStr = C_DEFAULT_TYPE;
        }
        addAttribute(doc, "type", typeMappingStr, paramElement);
        addAttribute(doc, "caps-type", typeMappingStr.toUpperCase(), paramElement);
        addShortType(paramElement, paramType);
        if (mapper.getDefaultMappingName().equals(paramType)) {
            addAttribute(doc, "default", "yes", paramElement);
        }
        addAttribute(doc, "value", getParamInitializer(paramType), paramElement);
        addAttribute(doc, "location", "body", paramElement);
        if (opName != null) {
            addAttribute(doc, "opname", opName.getLocalPart(), paramElement);
        }
        if (partName != null) {
            addAttribute(doc, "partname", JavaUtils.capitalizeFirstChar(partName), paramElement);
        }
        if (isPrimitive) {
            addAttribute(doc, "primitive", "yes", paramElement);
        }
        boolean isOurs = true;
        if (typeMappingStr.length() != 0 && !typeMappingStr.equals("void") && !typeMappingStr.equals(C_DEFAULT_TYPE)) {
            addAttribute(doc, "ours", "yes", paramElement);
            isOurs = true;
        } else {
            isOurs = false;
        }
        if (isOurs) {
            typeMappingStr = C_OUR_TYPE_PREFIX + typeMappingStr + C_OUR_TYPE_SUFFIX;
        }
        addAttribute(doc, "axis2-type", typeMappingStr, paramElement);
        addAttribute(doc, "axis2-caps-type", typeMappingStr.toUpperCase(), paramElement);
        return paramElement;
    }

    /**
     * @param doc
     * @param operation
     * @param param
     */
    protected void addCSpecifcAttributes(Document doc, AxisOperation operation, Element param, String messageType) {
        String typeMappingStr;
        AxisMessage message;
        if (messageType.equals(WSDLConstants.MESSAGE_LABEL_IN_VALUE)) message = operation.getMessage(WSDLConstants.MESSAGE_LABEL_IN_VALUE); else message = operation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
        QName typeMapping = message.getElementQName();
        String paramType = this.mapper.getTypeMappingName(message.getElementQName());
        if (doc == null || paramType == null || param == null) {
            return;
        }
        if (message != null) {
            String type = this.mapper.getTypeMappingName(message.getElementQName());
            typeMappingStr = (type == null) ? "" : type;
        } else {
            typeMappingStr = "";
        }
        addAttribute(doc, "caps-type", paramType.toUpperCase(), param);
        if (!paramType.equals("") && !paramType.equals("void") && !typeMappingStr.equals(C_DEFAULT_TYPE) && typeMappingStr.contains("adb_")) {
            addAttribute(doc, "ours", "yes", param);
        }
    }

    /**
     * @param doc
     * @param operation
     * @return Returns the parameter element.
     */
    protected Element[] getInputParamElement(Document doc, AxisOperation operation) {
        Element[] param = super.getInputParamElement(doc, operation);
        for (int i = 0; i < param.length; i++) {
            addCSpecifcAttributes(doc, operation, param[i], WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        }
        return param;
    }

    /**
     * @param doc
     * @param operation
     * @return Returns Element.
     */
    protected Element getOutputParamElement(Document doc, AxisOperation operation) {
        Element param = super.getOutputParamElement(doc, operation);
        addCSpecifcAttributes(doc, operation, param, WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
        return param;
    }

    /**
     * Gets the output directory for source files.
     *
     * @param outputDir
     * @return Returns File.
     */
    protected File getOutputDirectory(File outputDir, String dir2) {
        if (dir2 != null && !"".equals(dir2)) {
            if (outputDir.getName().equals(".")) {
                outputDir = new File(outputDir, dir2);
            }
        }
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        return outputDir;
    }

    /**
     * @param doc
     * @param parameters
     * @param location
     */
    protected List getParameterElementList(Document doc, List parameters, String location) {
        List parameterElementList = new ArrayList();
        if ((parameters != null) && !parameters.isEmpty()) {
            int count = parameters.size();
            for (int i = 0; i < count; i++) {
                Element param = doc.createElement("param");
                SOAPHeaderMessage header = (SOAPHeaderMessage) parameters.get(i);
                QName name = header.getElement();
                addAttribute(doc, "name", this.mapper.getParameterName(name), param);
                String typeMapping = this.mapper.getTypeMappingName(name);
                String typeMappingStr = (typeMapping == null) ? "" : typeMapping;
                addAttribute(doc, "type", typeMappingStr, param);
                addAttribute(doc, "location", location, param);
                if (header.isMustUnderstand()) {
                    addAttribute(doc, "mustUnderstand", "true", param);
                }
                if (name != null) {
                    Element qNameElement = doc.createElement("qname");
                    addAttribute(doc, "nsuri", name.getNamespaceURI(), qNameElement);
                    addAttribute(doc, "localname", name.getLocalPart(), qNameElement);
                    param.appendChild(qNameElement);
                }
                parameterElementList.add(param);
                boolean isOurs = true;
                if (typeMappingStr.length() != 0 && !typeMappingStr.equals("void") && !typeMappingStr.equals(C_DEFAULT_TYPE)) {
                    addAttribute(doc, "ours", "yes", param);
                    isOurs = true;
                } else {
                    isOurs = false;
                }
                if (isOurs) {
                    typeMappingStr = C_OUR_TYPE_PREFIX + typeMappingStr + C_OUR_TYPE_SUFFIX;
                }
                addAttribute(doc, "axis2-type", typeMappingStr, param);
            }
        }
        return parameterElementList;
    }

    /**
     * Finds the output element.
     *
     * @param doc
     * @param bindingOperation
     * @param headerParameterQNameList
     */
    protected Element getOutputElement(Document doc, AxisBindingOperation bindingOperation, List headerParameterQNameList) {
        AxisOperation operation = bindingOperation.getAxisOperation();
        Element outputElt = doc.createElement("output");
        String mep = operation.getMessageExchangePattern();
        if (WSDLUtil.isOutputPresentForMEP(mep)) {
            Element param = getOutputParamElement(doc, operation);
            if (param != null) {
                outputElt.appendChild(param);
            }
            List outputElementList = getParameterElementList(doc, headerParameterQNameList, WSDLConstants.SOAP_HEADER);
            outputElementList.addAll(getParameterElementListForHttpHeader(doc, (ArrayList) getBindingPropertyFromMessage(WSDL2Constants.ATTR_WHTTP_HEADER, operation.getName(), WSDLConstants.WSDL_MESSAGE_DIRECTION_OUT), WSDLConstants.HTTP_HEADER));
            for (int i = 0; i < outputElementList.size(); i++) {
                outputElt.appendChild((Element) outputElementList.get(i));
            }
            Policy policy = getBindingPolicyFromMessage(bindingOperation, WSDLConstants.WSDL_MESSAGE_DIRECTION_OUT);
            if (policy != null) {
                try {
                    addAttribute(doc, "policy", PolicyUtil.getSafeString(PolicyUtil.policyComponentToString(policy)), outputElt);
                } catch (Exception ex) {
                    throw new RuntimeException("can't serialize the policy ..");
                }
            }
        }
        return outputElt;
    }

    /**
     * Get the input element
     *
     * @param doc
     * @param bindingOperation
     * @param headerParameterQNameList
     * @return DOM element
     */
    protected Element getInputElement(Document doc, AxisBindingOperation bindingOperation, List headerParameterQNameList) {
        AxisOperation operation = bindingOperation.getAxisOperation();
        Element inputElt = doc.createElement("input");
        String mep = operation.getMessageExchangePattern();
        if (WSDLUtil.isInputPresentForMEP(mep)) {
            Element[] param = getInputParamElement(doc, operation);
            for (int i = 0; i < param.length; i++) {
                inputElt.appendChild(param[i]);
            }
            List parameterElementList = getParameterElementList(doc, headerParameterQNameList, WSDLConstants.SOAP_HEADER);
            parameterElementList.addAll(getParameterElementListForHttpHeader(doc, (ArrayList) getBindingPropertyFromMessage(WSDL2Constants.ATTR_WHTTP_HEADER, operation.getName(), WSDLConstants.WSDL_MESSAGE_DIRECTION_IN), WSDLConstants.HTTP_HEADER));
            parameterElementList.addAll(getParameterElementListForSOAPModules(doc, (ArrayList) getBindingPropertyFromMessage(WSDL2Constants.ATTR_WSOAP_MODULE, operation.getName(), WSDLConstants.WSDL_MESSAGE_DIRECTION_IN)));
            for (int i = 0; i < parameterElementList.size(); i++) {
                inputElt.appendChild((Element) parameterElementList.get(i));
            }
            Policy policy = getBindingPolicyFromMessage(bindingOperation, WSDLConstants.WSDL_MESSAGE_DIRECTION_IN);
            if (policy != null) {
                try {
                    addAttribute(doc, "policy", PolicyUtil.getSafeString(PolicyUtil.policyComponentToString(policy)), inputElt);
                } catch (Exception ex) {
                    throw new RuntimeException("can't serialize the policy ..");
                }
            }
        }
        return inputElt;
    }
}
