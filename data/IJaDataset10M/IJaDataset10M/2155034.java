package net.sf.kosmagene.webapp.services.impl;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.Context;
import org.appfuse.Constants;
import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import net.sf.kosmagene.webapp.services.ServiceFacade;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implementation of the ServiceFacade Interface
 *
 * @author Serge Eby
 * @version $Id: ServiceFacadeImpl.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ServiceFacadeImpl implements ServiceFacade {

    private Logger logger;

    private MailEngine mailEngine;

    private UserManager userManager;

    private RoleManager roleManager;

    private SimpleMailMessage mailMessage;

    private ThreadLocale threadLocale;

    public ServiceFacadeImpl(Logger logger, Context context, MailEngine mailEngine, UserManager userMgr, RoleManager roleMgr, SimpleMailMessage mailMessage, ThreadLocale threadLocale) {
        this.logger = logger;
        this.mailEngine = mailEngine;
        this.userManager = userMgr;
        this.roleManager = roleMgr;
        this.mailMessage = mailMessage;
        this.threadLocale = threadLocale;
    }

    public MailEngine getMailEngine() {
        return mailEngine;
    }

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAvailableRoles() {
        List<Role> roles = roleManager.getAll();
        List<String> availableRoles = new ArrayList<String>();
        for (Role role : roles) {
            availableRoles.add(role.getName());
        }
        return availableRoles;
    }

    public Map<String, String> getAvailableCountries() {
        Map<String, String> countries = new HashMap<String, String>();
        Locale locale = threadLocale.getLocale();
        final String EMPTY = "";
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale l : availableLocales) {
            String name = l.getDisplayCountry(locale);
            String iso = l.getCountry();
            if (!EMPTY.equals(name) && !EMPTY.equals(iso)) {
                countries.put(iso, name);
            }
        }
        logger.debug("Number of countries added: " + countries.size());
        Map<String, String> sortedCountries = new TreeMap<String, String>(new CountryComparator(countries, locale));
        sortedCountries.putAll(countries);
        return sortedCountries;
    }

    /**
     * Class to compare LabelValues using their labels with locale-sensitive
     * behaviour.
     */
    private class CountryComparator implements Comparator<String> {

        private Collator c;

        private Map<String, String> unsortedMap;

        /**
         * Creates a new CountryComparator object.
         *
         * @param map of country and locale
         * @param locale The Locale used for localized String comparison.
         */
        public CountryComparator(Map<String, String> map, Locale locale) {
            unsortedMap = map;
            c = Collator.getInstance(locale);
        }

        /**
         * Compares the localized labels of two LabelValues.
         *
         * @param rhs The first String to compare.
         * @param lhs The second String to compare.
         * @return The value returned by comparing the localized labels.
         */
        public final int compare(String lhs, String rhs) {
            String lvalue = unsortedMap.get(lhs);
            String rvalue = unsortedMap.get(rhs);
            return c.compare(lvalue, rvalue);
        }
    }
}
