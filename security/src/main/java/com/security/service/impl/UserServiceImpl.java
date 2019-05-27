package com.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.security.entity.ResponseBody;
import com.security.entity.UserEntity;
import com.security.mapper.UserMapper;
import com.security.service.UserService;
import com.security.util.AttributeConfig;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper mapper;

	@Override
	public UserEntity findOne(String username) {
		return mapper.findByName(username);
	}

	@Override
	public UserEntity findOne(int id) {
		return mapper.findOne(id);
	}

	@Override
	public String findAll(int curPageNo) {
		PageHelper.startPage(curPageNo, AttributeConfig.PAGE_RECORD_NUMBER);
		List<UserEntity> list = mapper.findAll();
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", new PageInfo<UserEntity>(list)).toString();
	}
	
	@Override
	public String insert(UserEntity entity) {
		int flag = mapper.insert(entity);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"保存成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"保存失败！"));
		}
	}
	
	@Override
	public String update(UserEntity entity) {
		int flag = mapper.update(entity);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"更新成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"更新失败！"));
		}
	}
	
	@Override
	public String delete(int id) {
		int flag = mapper.del(id);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"删除成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
	}

}
