package com.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.security.entity.RoleEntity;
import com.security.entity.RoleUserEntity;

@Mapper
public interface RoleUserMapper {
	
	int insert(RoleUserEntity entity);
	
	int insertByBatch(List<RoleUserEntity> entity);

	List<RoleEntity> findAll();

	List<RoleEntity> findUser(int userId);
	
	int del(int roleId, int userId);
	
	int delUser(int userId);
}
