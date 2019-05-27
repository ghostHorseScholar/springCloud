package com.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.security.entity.UserEntity;

@Mapper
public interface UserMapper extends CoreMapper<UserEntity> {
	
	UserEntity findByName(String userName);
}
