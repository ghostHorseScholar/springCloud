package com.security.service;

import com.security.entity.RoleEntity;

public interface RoleService {

	RoleEntity findOne(int id);
	
	String findAll(int curPageNo);
	
	String insert(RoleEntity entity);
	
	String update(RoleEntity entity);
	
	String delete(int id);
	
}
