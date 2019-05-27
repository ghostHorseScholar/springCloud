package com.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.security.entity.PermissionEntity;
import com.security.entity.PermissionRoleEntity;

@Mapper
public interface PermissionRoleMapper {

	List<PermissionEntity> findAll();

	List<PermissionEntity> findRole(int roleId);

	List<PermissionEntity> findUser(int userId);

	List<PermissionEntity> findUrl(@Param("userId")int userId, @Param("pid")int pid);

	List<PermissionEntity> findPermission(@Param("roleId")int roleId, @Param("pid")int pid);
	
	int insert(PermissionRoleEntity entity);
	
	int insertByBatch(List<PermissionRoleEntity> entity);
	
	int del(int permission, int roleId);
	
	int delRole(int roleId);
	
	int delUser(int userId);
}
