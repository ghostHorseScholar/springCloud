package com.security.service;

import com.security.entity.UserEntity;

public interface UserService {

	UserEntity findOne(String userName);

	UserEntity findOne(int id);
	
	String findAll(int curPageNo);
	
	String insert(UserEntity entity);
	
	String update(UserEntity entity);
	
	String delete(int id);
	
}
