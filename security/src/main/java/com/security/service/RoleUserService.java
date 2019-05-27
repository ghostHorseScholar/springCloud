package com.security.service;

import java.util.List;

import com.security.entity.RoleUserEntity;

public interface RoleUserService {

	String findAll(int curPageNo);
	
	String findUser(int userId);
	
	String insert(RoleUserEntity entity);
	
	String insertByBatch(List<RoleUserEntity> entity, int userId);
	
	String delete(int roleId, int userId);
	
	String deleteUser(int userId);
	
}
