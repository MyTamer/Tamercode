package org.tapestrycomponents.tassel.domain.auto;

import java.util.List;

/** Class _User was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _User extends org.objectstyle.cayenne.CayenneDataObject {

    public static final String EMAIL_PROPERTY = "email";

    public static final String FIRST_NAME_PROPERTY = "firstName";

    public static final String LAST_NAME_PROPERTY = "lastName";

    public static final String LAST_ON_FROM_PROPERTY = "lastOnFrom";

    public static final String LAST_ON_TIME_PROPERTY = "lastOnTime";

    public static final String LOGIN_PROPERTY = "login";

    public static final String PASSWORD_PROPERTY = "password";

    public static final String DISCUSSIONS_PROPERTY = "discussions";

    public static final String DOWNLOADS_PROPERTY = "downloads";

    public static final String RATINGS_PROPERTY = "ratings";

    public static final String SUBMITTED_COMPONENTS_PROPERTY = "submittedComponents";

    public static final String SUBMITTED_STYLESHEETS_PROPERTY = "submittedStylesheets";

    public static final String TRACKED_COMPONENTS_PROPERTY = "trackedComponents";

    public static final String USER_STATUS_PROPERTY = "userStatus";

    public static final String USER_STYLE_PROPERTY = "userStyle";

    public static final String LOGIN_PK_COLUMN = "login";

    public void setEmail(String email) {
        writeProperty("email", email);
    }

    public String getEmail() {
        return (String) readProperty("email");
    }

    public void setFirstName(String firstName) {
        writeProperty("firstName", firstName);
    }

    public String getFirstName() {
        return (String) readProperty("firstName");
    }

    public void setLastName(String lastName) {
        writeProperty("lastName", lastName);
    }

    public String getLastName() {
        return (String) readProperty("lastName");
    }

    public void setLastOnFrom(String lastOnFrom) {
        writeProperty("lastOnFrom", lastOnFrom);
    }

    public String getLastOnFrom() {
        return (String) readProperty("lastOnFrom");
    }

    public void setLastOnTime(java.sql.Date lastOnTime) {
        writeProperty("lastOnTime", lastOnTime);
    }

    public java.sql.Date getLastOnTime() {
        return (java.sql.Date) readProperty("lastOnTime");
    }

    public void setLogin(String login) {
        writeProperty("login", login);
    }

    public String getLogin() {
        return (String) readProperty("login");
    }

    public void setPassword(String password) {
        writeProperty("password", password);
    }

    public String getPassword() {
        return (String) readProperty("password");
    }

    public void addToDiscussions(org.tapestrycomponents.tassel.domain.UserDiscussion obj) {
        addToManyTarget("discussions", obj, true);
    }

    public void removeFromDiscussions(org.tapestrycomponents.tassel.domain.UserDiscussion obj) {
        removeToManyTarget("discussions", obj, true);
    }

    public List getDiscussions() {
        return (List) readProperty("discussions");
    }

    public void addToDownloads(org.tapestrycomponents.tassel.domain.ComponentDL obj) {
        addToManyTarget("downloads", obj, true);
    }

    public void removeFromDownloads(org.tapestrycomponents.tassel.domain.ComponentDL obj) {
        removeToManyTarget("downloads", obj, true);
    }

    public List getDownloads() {
        return (List) readProperty("downloads");
    }

    public void addToRatings(org.tapestrycomponents.tassel.domain.ComponentRating obj) {
        addToManyTarget("ratings", obj, true);
    }

    public void removeFromRatings(org.tapestrycomponents.tassel.domain.ComponentRating obj) {
        removeToManyTarget("ratings", obj, true);
    }

    public List getRatings() {
        return (List) readProperty("ratings");
    }

    public void addToSubmittedComponents(org.tapestrycomponents.tassel.domain.Component obj) {
        addToManyTarget("submittedComponents", obj, true);
    }

    public void removeFromSubmittedComponents(org.tapestrycomponents.tassel.domain.Component obj) {
        removeToManyTarget("submittedComponents", obj, true);
    }

    public List getSubmittedComponents() {
        return (List) readProperty("submittedComponents");
    }

    public void addToSubmittedStylesheets(org.tapestrycomponents.tassel.domain.Stylesheet obj) {
        addToManyTarget("submittedStylesheets", obj, true);
    }

    public void removeFromSubmittedStylesheets(org.tapestrycomponents.tassel.domain.Stylesheet obj) {
        removeToManyTarget("submittedStylesheets", obj, true);
    }

    public List getSubmittedStylesheets() {
        return (List) readProperty("submittedStylesheets");
    }

    public void addToTrackedComponents(org.tapestrycomponents.tassel.domain.TrackedComponent obj) {
        addToManyTarget("trackedComponents", obj, true);
    }

    public void removeFromTrackedComponents(org.tapestrycomponents.tassel.domain.TrackedComponent obj) {
        removeToManyTarget("trackedComponents", obj, true);
    }

    public List getTrackedComponents() {
        return (List) readProperty("trackedComponents");
    }

    public void setUserStatus(org.tapestrycomponents.tassel.domain.UserStatus userStatus) {
        setToOneTarget("userStatus", userStatus, true);
    }

    public org.tapestrycomponents.tassel.domain.UserStatus getUserStatus() {
        return (org.tapestrycomponents.tassel.domain.UserStatus) readProperty("userStatus");
    }

    public void setUserStyle(org.tapestrycomponents.tassel.domain.Stylesheet userStyle) {
        setToOneTarget("userStyle", userStyle, true);
    }

    public org.tapestrycomponents.tassel.domain.Stylesheet getUserStyle() {
        return (org.tapestrycomponents.tassel.domain.Stylesheet) readProperty("userStyle");
    }
}
