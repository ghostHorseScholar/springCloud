package com.security.mapper;

import java.util.List;

public interface CoreMapper<T> {

	int insert(T entity);

	int update(T entity);
	
	T findOne(int id);
	
	List<T> findAll();
	
	int del(int id);
}
