package org.tanukisoftware.wrapper;

import java.util.Date;

/**
 * A WrapperUser contains information about a user account on the platform
 *  running the Wrapper.  A WrapperUser is obtained by calling
 *  WrapperManager.getUser() or WrapperManager.getInteractiveUser().
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class WrapperWin32User extends WrapperUser {

    /** The current SID of the User Account. */
    private String m_sid;

    /** The domain of the User Account. */
    private String m_domain;

    /** Time that the user logged in. */
    private long m_loginTime;

    WrapperWin32User(byte[] sid, byte[] user, byte[] domain, int loginTime) {
        super(user);
        m_sid = new String(sid);
        m_domain = new String(domain);
        m_loginTime = loginTime * 1000L;
    }

    /**
     * Returns the current Security Identifier (SID) of the user account.
     *
     * @return The SID of the user account.
     */
    public String getSID() {
        return m_sid;
    }

    /**
     * Returns the domain name of the user account.
     *
     * @return The domain name of the user account.
     */
    public String getDomain() {
        return m_domain;
    }

    /**
     * Returns the full name of the user account.
     *
     * @return The full name of the user account.
     */
    public String getAccount() {
        return m_domain + "/" + getUser();
    }

    /**
     * Returns the login time of the user account.
     *
     * @return The login time of the user account.
     */
    public long getLoginTime() {
        return m_loginTime;
    }

    void addGroup(byte[] sid, byte[] user, byte[] domain) {
        addGroup(new WrapperWin32Group(sid, user, domain));
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A string representation of the user.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("WrapperWin32User[");
        sb.append(getAccount());
        sb.append(", ");
        sb.append(new Date(m_loginTime).toString());
        sb.append(", groups {");
        WrapperGroup[] groups = getGroups();
        for (int i = 0; i < groups.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(groups[i].toString());
        }
        sb.append("}");
        sb.append("]");
        return sb.toString();
    }
}
