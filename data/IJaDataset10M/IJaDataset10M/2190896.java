package br.com.sysmap.crux.tools.compile.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import br.com.sysmap.crux.core.client.screen.InterfaceConfigException;
import br.com.sysmap.crux.core.declarativeui.CruxToHtmlTransformer;
import br.com.sysmap.crux.core.i18n.MessagesFactory;
import br.com.sysmap.crux.core.rebind.GeneratorMessages;
import br.com.sysmap.crux.core.rebind.scanner.module.Module;
import br.com.sysmap.crux.core.rebind.scanner.module.Modules;
import br.com.sysmap.crux.core.rebind.scanner.module.ModulesScanner;
import br.com.sysmap.crux.core.server.scan.ScannerURLS;
import br.com.sysmap.crux.core.utils.XMLUtils;
import br.com.sysmap.crux.core.utils.XMLUtils.XMLException;
import br.com.sysmap.crux.scannotation.URLStreamManager;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public class ModuleUtils {

    private static final String MODULE_IMPORT_SUFFIX = ".nocache.js";

    private static GeneratorMessages messages = (GeneratorMessages) MessagesFactory.getMessages(GeneratorMessages.class);

    public static void initializeScannerURLs(URL[] urls) {
        ScannerURLS.setURLsForSearch(urls);
        List<String> classesDir = new ArrayList<String>();
        for (URL url : urls) {
            if (!url.toString().endsWith(".jar")) {
                classesDir.add(url.toString());
            }
        }
        if (classesDir.size() > 0) {
            ModulesScanner.getInstance().setClassesDir(classesDir.toArray(new String[classesDir.size()]));
        }
    }

    /**
	 * 
	 * @param pageFile
	 * @return
	 * @throws IOException
	 * @throws InterfaceConfigException
	 */
    public static String findModuleNameFromHostedPage(URL pageFile) throws IOException, InterfaceConfigException {
        Module module = findModuleFromPageUrl(pageFile);
        if (module != null) {
            return module.getFullName();
        }
        return null;
    }

    /**
	 * 
	 * @param pageFile
	 * @return
	 * @throws IOException
	 * @throws InterfaceConfigException
	 */
    public static Module findModuleFromPageUrl(URL pageFile) throws IOException, InterfaceConfigException {
        String result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        URLStreamManager manager = new URLStreamManager(pageFile);
        CruxToHtmlTransformer.generateHTML(manager.open(), out);
        manager.close();
        ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
        Document source = null;
        try {
            source = XMLUtils.createNSUnawareDocument(input);
        } catch (XMLException e) {
            throw new InterfaceConfigException(messages.screenFactoryErrorParsingScreen(pageFile.toString(), e.getMessage()));
        }
        NodeList elementList = source.getElementsByTagName("script");
        int length = elementList.getLength();
        for (int i = 0; i < length; i++) {
            Element element = (Element) elementList.item(i);
            String src = element.getAttribute("src");
            if (src != null && src.endsWith(MODULE_IMPORT_SUFFIX)) {
                if (result != null) {
                    throw new InterfaceConfigException("Multiple modules in the same html page is not allowed in CRUX.");
                }
                int lastSlash = src.lastIndexOf("/");
                if (lastSlash >= 0) {
                    int indexOfModuleSuffix = src.indexOf(MODULE_IMPORT_SUFFIX, lastSlash);
                    result = src.substring(lastSlash + 1, indexOfModuleSuffix);
                } else {
                    int indexOfModuleSuffix = src.indexOf(MODULE_IMPORT_SUFFIX);
                    result = src.substring(0, indexOfModuleSuffix);
                }
            }
        }
        return Modules.getInstance().getModule(result);
    }
}
