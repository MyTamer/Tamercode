package com.bocoon.entity.cms.assist.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the jc_vote_item table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_vote_item"
 */
public abstract class BaseCmsVoteItem implements Serializable {

    public static String REF = "CmsVoteItem";

    public static String PROP_TOPIC = "topic";

    public static String PROP_PRIORITY = "priority";

    public static String PROP_TITLE = "title";

    public static String PROP_VOTE_COUNT = "voteCount";

    public static String PROP_ID = "id";

    public BaseCmsVoteItem() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseCmsVoteItem(java.lang.Integer id) {
        this.setId(id);
        initialize();
    }

    /**
	 * Constructor for required fields
	 */
    public BaseCmsVoteItem(java.lang.Integer id, com.bocoon.entity.cms.assist.CmsVoteTopic topic, java.lang.String title, java.lang.Integer voteCount, java.lang.Integer priority) {
        this.setId(id);
        this.setTopic(topic);
        this.setTitle(title);
        this.setVoteCount(voteCount);
        this.setPriority(priority);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.Integer id;

    private java.lang.String title;

    private java.lang.Integer voteCount;

    private java.lang.Integer priority;

    private com.bocoon.entity.cms.assist.CmsVoteTopic topic;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="voteitem_id"
     */
    public java.lang.Integer getId() {
        return id;
    }

    /**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
    public void setId(java.lang.Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: title
	 */
    public java.lang.String getTitle() {
        return title;
    }

    /**
	 * Set the value related to the column: title
	 * @param title the title value
	 */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    /**
	 * Return the value associated with the column: vote_count
	 */
    public java.lang.Integer getVoteCount() {
        return voteCount;
    }

    /**
	 * Set the value related to the column: vote_count
	 * @param voteCount the vote_count value
	 */
    public void setVoteCount(java.lang.Integer voteCount) {
        this.voteCount = voteCount;
    }

    /**
	 * Return the value associated with the column: priority
	 */
    public java.lang.Integer getPriority() {
        return priority;
    }

    /**
	 * Set the value related to the column: priority
	 * @param priority the priority value
	 */
    public void setPriority(java.lang.Integer priority) {
        this.priority = priority;
    }

    /**
	 * Return the value associated with the column: votetopic_id
	 */
    public com.bocoon.entity.cms.assist.CmsVoteTopic getTopic() {
        return topic;
    }

    /**
	 * Set the value related to the column: votetopic_id
	 * @param topic the votetopic_id value
	 */
    public void setTopic(com.bocoon.entity.cms.assist.CmsVoteTopic topic) {
        this.topic = topic;
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof com.bocoon.entity.cms.assist.CmsVoteItem)) return false; else {
            com.bocoon.entity.cms.assist.CmsVoteItem cmsVoteItem = (com.bocoon.entity.cms.assist.CmsVoteItem) obj;
            if (null == this.getId() || null == cmsVoteItem.getId()) return false; else return (this.getId().equals(cmsVoteItem.getId()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode(); else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }
}
