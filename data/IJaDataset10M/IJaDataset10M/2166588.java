package net.azib.ipscan.fetchers;

import java.util.MissingResourceException;
import net.azib.ipscan.config.Labels;

/**
 * Convenience base class for built-in fetchers
 *
 * @author Anton Keks
 */
public abstract class AbstractFetcher implements Fetcher {

    public String getName() {
        return Labels.getLabel(getId());
    }

    public String getFullName() {
        return getName();
    }

    public String getInfo() {
        try {
            return Labels.getLabel(getId() + ".info");
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public Class<? extends FetcherPrefs> getPreferencesClass() {
        return null;
    }

    public void init() {
    }

    public void cleanup() {
    }
}
