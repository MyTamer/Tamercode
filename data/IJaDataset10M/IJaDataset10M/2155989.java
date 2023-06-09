package cz.softinel.sis.user;

import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.role.Role;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class User {

    private Long pk;

    private ContactInfo contactInfo;

    private Role personalRole;

    private Login login;

    /**
	 * @return Returns the contactInfo.
	 */
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
	 * @param contactInfo The contactInfo to set.
	 */
    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
	 * @return Returns the personalRole.
	 */
    public Role getPersonalRole() {
        return personalRole;
    }

    /**
	 * @param personalRole The personalRole to set.
	 */
    public void setPersonalRole(Role personalRole) {
        this.personalRole = personalRole;
    }

    /**
	 * @return Returns the pk.
	 */
    public Long getPk() {
        return pk;
    }

    /**
	 * @param pk The pk to set.
	 */
    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
