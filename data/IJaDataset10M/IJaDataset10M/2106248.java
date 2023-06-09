package com.google.gdata.data.youtube;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * A tag containing statistics about the user
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = YouTubeNamespace.PREFIX, nsUri = YouTubeNamespace.URI, localName = "statistics")
public class YtUserProfileStatistics extends AbstractExtension {

    /**
   * How many time the users profile has been viewed? On the web site this
   * field is called "channel views".
   */
    private long viewCount;

    private long videoWatchCount;

    private long subscriberCount;

    private DateTime lastWebAccess;

    /**
   * Returns how many times this channel/profile has been viewed.
   * @return number of views
   */
    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getVideoWatchCount() {
        return videoWatchCount;
    }

    public void setVideoWatchCount(long vwc) {
        videoWatchCount = vwc;
    }

    public long getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(long sc) {
        subscriberCount = sc;
    }

    public DateTime getLastWebAccess() {
        return lastWebAccess;
    }

    public void setLastWebAccess(DateTime lastWebAccess) {
        this.lastWebAccess = lastWebAccess;
    }

    @Override
    protected void putAttributes(AttributeGenerator generator) {
        putAttributeIfGreaterZero(generator, "viewCount", viewCount);
        putAttributeIfGreaterZero(generator, "videoWatchCount", videoWatchCount);
        putAttributeIfGreaterZero(generator, "subscriberCount", subscriberCount);
        if (lastWebAccess != null) {
            generator.put("lastWebAccess", lastWebAccess);
        }
    }

    /**
   * Put the attribute only to the generator if its value is greater than
   * zero.
   */
    private void putAttributeIfGreaterZero(AttributeGenerator generator, String key, long value) {
        if (value > 0) {
            generator.put(key, value);
        }
    }

    @Override
    protected void consumeAttributes(AttributeHelper helper) throws ParseException {
        viewCount = helper.consumeLong("viewCount", false, 0L);
        videoWatchCount = helper.consumeLong("videoWatchCount", false, 0L);
        subscriberCount = helper.consumeLong("subscriberCount", false, 0L);
        lastWebAccess = helper.consumeDateTime("lastWebAccess", false);
    }
}
