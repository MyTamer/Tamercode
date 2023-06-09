package org.apache.hadoop.security;

import java.io.IOException;
import javax.security.auth.login.LoginException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

/** A {@link Writable} abstract class for storing user and groups information.
 */
public abstract class UserGroupInformation implements Writable {

    public static final Log LOG = LogFactory.getLog(UserGroupInformation.class);

    private static UserGroupInformation LOGIN_UGI = null;

    private static final ThreadLocal<UserGroupInformation> currentUGI = new ThreadLocal<UserGroupInformation>();

    /** @return the {@link UserGroupInformation} for the current thread */
    public static UserGroupInformation getCurrentUGI() {
        return currentUGI.get();
    }

    /** Set the {@link UserGroupInformation} for the current thread */
    public static void setCurrentUGI(UserGroupInformation ugi) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Thread.currentThread().getName() + ", ugi=" + ugi);
        }
        currentUGI.set(ugi);
    }

    /** Get username
   * 
   * @return the user's name
   */
    public abstract String getUserName();

    /** Get the name of the groups that the user belong to
   * 
   * @return an array of group names
   */
    public abstract String[] getGroupNames();

    /** Login and return a UserGroupInformation object. */
    public static UserGroupInformation login(Configuration conf) throws LoginException {
        if (LOGIN_UGI == null) {
            LOGIN_UGI = UnixUserGroupInformation.login(conf);
        }
        return LOGIN_UGI;
    }

    /** Read a {@link UserGroupInformation} from conf */
    public static UserGroupInformation readFrom(Configuration conf) throws IOException {
        try {
            return UnixUserGroupInformation.readFromConf(conf, UnixUserGroupInformation.UGI_PROPERTY_NAME);
        } catch (LoginException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }
}
