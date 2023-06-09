package com.yosemity.extra.service.impl;

import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import com.yosemity.extra.dao.UserDao;
import com.yosemity.extra.model.User;
import com.yosemity.extra.service.UserExistsException;
import com.yosemity.extra.service.UserManager;
import com.yosemity.extra.service.UserService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import javax.jws.WebService;
import javax.persistence.EntityExistsException;
import java.util.List;

/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@WebService(serviceName = "UserService", endpointInterface = "com.yosemity.extra.service.UserService")
public class UserManagerImpl extends UniversalManagerImpl implements UserManager, UserService {

    private UserDao dao;

    private DaoAuthenticationProvider authenticationProvider;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao the UserDao that communicates with the database
     */
    @Required
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }

    /**
     * Set the DaoAuthenticationProvider object that will provide both the
     * PasswordEncoder and the SaltSource which will be used for password
     * encryption when necessary.
     * @param authenticationProvider the DaoAuthenticationProvider object
     */
    @Required
    public void setAuthenticationProvider(DaoAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return dao.get(new Long(userId));
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(User user) {
        return dao.getUsers();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) throws UserExistsException {
        if (user.getVersion() == null) {
            user.setUsername(user.getUsername().toLowerCase());
        }
        boolean passwordChanged = false;
        if (authenticationProvider != null) {
            PasswordEncoder passwordEncoder = authenticationProvider.getPasswordEncoder();
            if (passwordEncoder != null) {
                if (user.getVersion() == null) {
                    passwordChanged = true;
                } else {
                    String currentPassword = dao.getUserPassword(user.getUsername());
                    if (currentPassword == null) {
                        passwordChanged = true;
                    } else {
                        if (!currentPassword.equals(user.getPassword())) {
                            passwordChanged = true;
                        }
                    }
                }
                if (passwordChanged) {
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                }
            } else {
                log.warn("PasswordEncoder not set on AuthenticationProvider, skipping password encryption...");
            }
        } else {
            log.warn("AuthenticationProvider not set, skipping password encryption...");
        }
        try {
            return dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        dao.remove(new Long(userId));
    }

    /**
     * {@inheritDoc}
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) dao.loadUserByUsername(username);
    }
}
