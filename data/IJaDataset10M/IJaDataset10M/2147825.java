package org.roller.model;

import org.roller.RollerException;
import org.roller.pojos.RefererData;
import org.roller.pojos.WebsiteData;
import org.roller.util.ThreadManager;
import java.util.List;

/**
 * Interface to Referer management.
 * @author David M Johnson
 */
public interface RefererManager {

    /**
     * Get all referers for specified user.
     * @param userName
     * @return List of type RefererData
     * @throws RollerException
     */
    public List getReferers(WebsiteData website) throws RollerException;

    /**
     * Get all referers for specified user that were made today.
     * @param userName Name of user.
     * @return List of type RefererData
     * @throws RollerException
     */
    public List getTodaysReferers(WebsiteData website) throws RollerException;

    /**
     * Get referers for a specified date.
     * @param userName Name of user.
     * @param date YYYYMMDD format of day's date.
     * @return List of type RefererData.
     * @throws RollerException
     */
    public List getReferersToDate(WebsiteData website, String date) throws RollerException;

    /**
     * Get most popular websites based on referer day hits.
     * @return List of WebsiteDisplayData objects.
     */
    public List getDaysPopularWebsites(int max) throws RollerException;

    /**
     * Get referers that refer to a specific weblog entry.
     * @param entryid Weblog entry ID
     * @return List of RefererData objects.
     * @throws RollerException
     */
    public List getReferersToEntry(String entryid) throws RollerException;

    /**
     * Remove all referers for the specific weblog entry.
	 * @param entryId Weblog entry ID
	 * @throws RollerException
     */
    public void removeReferersForEntry(String entryid) throws RollerException;

    /**
	 * Get referers that refer to a specific weblog entry.
	 * @param entryId Weblog entry ID
	 * @param authorized Is the current user authorized to edit these Referers.
	 * @return List of RefererData objects.
	 * @throws RollerException
	 */
    public List getEntryReferers(String entryId, boolean authorized) throws RollerException;

    /** Get user's day hits */
    public int getDayHits(WebsiteData website) throws RollerException;

    /** Get user's all-time total hits */
    public int getTotalHits(WebsiteData website) throws RollerException;

    /**
     * Process request for incoming referers.
     * @param request Request to be processed.
     */
    public void processRequest(ParsedRequest request);

    /**
     * Set thread manager to be used.
     * @param threadManager
     */
    public void setThreadManager(ThreadManager threadManager);

    public void forceTurnover(String websiteId) throws RollerException;

    public void checkForTurnover(boolean forceTurnover, String websiteId) throws RollerException;

    public RefererData retrieveReferer(String id) throws RollerException;

    public void removeReferer(String id) throws RollerException;

    public void release();
}
