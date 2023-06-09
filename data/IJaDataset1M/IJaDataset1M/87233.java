package es.caib.sistra.back.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;
import es.caib.sistra.back.Constants;

/**
 * <code>RequestProcessor</code> que a�ade funcionalidad para
 * los InitForm
 */
public class CustomRequestProcessor extends TilesRequestProcessor {

    protected static Log log = LogFactory.getLog(CustomRequestProcessor.class);

    private String defaultLang = null;

    private List supportedLangs = null;

    /**
     * Inicializa los idiomas soportados por la aplicaci�n
     */
    protected void initLangs() {
        try {
            defaultLang = Constants.DEFAULT_LANG;
            log.info("Default lang: " + defaultLang);
            getServletContext().setAttribute(Constants.DEFAULT_LANG_KEY, defaultLang);
            supportedLangs = new ArrayList();
            supportedLangs.add("ca");
            supportedLangs.add("es");
            supportedLangs.add("en");
            supportedLangs.add("de");
            log.info("Supported langs: " + supportedLangs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elije el lenguaje en el que se servira la petici�n.
     * @param request
     * @param response
     */
    protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
        if (!moduleConfig.getControllerConfig().getLocale()) {
            return;
        }
        if (defaultLang == null) initLangs();
        HttpSession session = request.getSession(true);
        String paramLang = request.getParameter("language");
        if (paramLang != null) {
            if (supportedLangs.contains(paramLang)) {
                log.info("Setting selected locale: " + paramLang);
                session.setAttribute(Globals.LOCALE_KEY, new Locale(paramLang));
                return;
            } else {
                log.info("Invalid selected locale: " + paramLang);
            }
        }
        Locale local = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (session.getAttribute(Globals.LOCALE_KEY) != null) {
            return;
        }
        Enumeration enumer = request.getLocales();
        while (enumer.hasMoreElements()) {
            Locale locale = (Locale) enumer.nextElement();
            String lang = locale.getLanguage().trim();
            if (supportedLangs.contains(lang)) {
                log.info("Setting browser locale: " + lang);
                session.setAttribute(Globals.LOCALE_KEY, new Locale(lang));
                return;
            }
        }
        log.info("Setting default locale: " + defaultLang);
        session.setAttribute(Globals.LOCALE_KEY, new Locale(defaultLang));
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("ISO-8859-15");
        }
        super.process(request, response);
    }

    protected void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        super.processPopulate(request, response, form, mapping);
    }
}
