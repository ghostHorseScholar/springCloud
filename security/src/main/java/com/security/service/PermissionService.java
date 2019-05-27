package com.security.service;

import java.util.List;

import com.security.entity.PermissionEntity;

public interface PermissionService {
	
	List<String> getPermission();
	
	List<String> getPermission(int userId);

	PermissionEntity findOne(int id);
	
	String findAll(int curPageNo);
	
	String insert(PermissionEntity entity);
	
	String update(PermissionEntity entity);
	
	String delete(int id);
	
}
