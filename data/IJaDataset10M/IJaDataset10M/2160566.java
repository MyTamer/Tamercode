package tudu.service;

import tudu.domain.model.Property;

/**
 * Manage the application configuration. 
 * 
 * @author Julien Dubois
 */
public interface ConfigurationManager {

    /**
     * Find a property by key.
     * 
     * @param key The property key
     * @return The property
     */
    Property getProperty(String key);

    /**
     * Update email properties.
     * 
     * @param smtpHost SMTP host
     * @param smtpPort SMTP port
     * @param smtpUser SMTP user
     * @param smtpPassword SMTP password
     * @param smtpFrom From address of the emails sent by the application
     */
    void updateEmailProperties(String smtpHost, String smtpPort, String smtpUser, String smtpPassword, String smtpFrom);
}
