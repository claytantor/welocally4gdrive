package com.welocally.gdrive.security;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.welocally.gdrive.dao.AbstractDao;


@Repository("roleDao")
public class RoleDaoDefaultImpl extends AbstractDao<Role> implements RoleDao {
    public RoleDaoDefaultImpl() {
        super(Role.class);
    }


	public Role findByName(String role) {
		return (Role) getCurrentSession()
        .createCriteria(this.getPersistentClass())
        .add(Restrictions.eq("role",role))
        .uniqueResult();
	}
    
    
    
}
