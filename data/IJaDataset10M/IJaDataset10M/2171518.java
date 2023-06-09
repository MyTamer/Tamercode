package com.yosemity.extra.dao.ibatis;

import com.yosemity.extra.dao.RoleDao;
import com.yosemity.extra.model.Role;
import java.util.List;

/**
 * This class interacts with iBatis's SQL Maps to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class RoleDaoiBatis extends GenericDaoiBatis<Role, Long> implements RoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDaoiBatis() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getAll() {
        return getSqlMapClientTemplate().queryForList("getRoles", null);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String name) {
        return (Role) getSqlMapClientTemplate().queryForObject("getRoleByName", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role save(final Role role) {
        if (role.getId() == null) {
            getSqlMapClientTemplate().insert("addRole", role);
        } else {
            getSqlMapClientTemplate().update("updateRole", role);
        }
        return role;
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        getSqlMapClientTemplate().update("deleteRole", rolename);
    }
}
