package com.security.service;

import java.util.List;

import com.security.entity.PermissionEntity;
import com.security.entity.PermissionRoleEntity;

public interface PermissionRoleService {

	String findAll(int curPageNo);

	String findRole(int roleId);

	String findUser(int userId);
	
	List<PermissionEntity> findUrl(int userId, int pid, List<PermissionEntity> list);
	
	String findUrl(int userId);
	
	List<PermissionEntity> findPermission(int userId, int pid, List<PermissionEntity> list);
	
	String findPermission(int userId);
	
	String insert(PermissionRoleEntity entity);
	
	String insertByBatch(List<PermissionRoleEntity> entity, int roleId);
	
	String delete(int permissionId, int roleId);
	
	String deleteRole(int roleId);
	
	String deleteUser(int userId);
	
}
