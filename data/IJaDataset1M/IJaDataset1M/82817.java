package townhall.data;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ThRole generated by hbm2java
 */
@Entity
@Table(name = "th_role", schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = "name"), @UniqueConstraint(columnNames = { "roleid", "name" }) })
public class ThRole implements java.io.Serializable {

    private int roleid;

    private String name;

    private String description;

    private Set<ThUser> thUsers = new HashSet<ThUser>(0);

    public ThRole() {
    }

    public ThRole(int roleid, String name) {
        this.roleid = roleid;
        this.name = name;
    }

    public ThRole(int roleid, String name, String description, Set<ThUser> thUsers) {
        this.roleid = roleid;
        this.name = name;
        this.description = description;
        this.thUsers = thUsers;
    }

    @Id
    @Column(name = "roleid", unique = true, nullable = false)
    public int getRoleid() {
        return this.roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    @Column(name = "name", unique = true, nullable = false, length = 256)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 256)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "thRoles")
    public Set<ThUser> getThUsers() {
        return this.thUsers;
    }

    public void setThUsers(Set<ThUser> thUsers) {
        this.thUsers = thUsers;
    }
}
