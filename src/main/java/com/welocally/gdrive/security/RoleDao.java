package com.welocally.gdrive.security;

import com.welocally.gdrive.dao.BaseDao;


public interface RoleDao extends BaseDao<Role> {
	public Role findByName(String roleName);
	
}
