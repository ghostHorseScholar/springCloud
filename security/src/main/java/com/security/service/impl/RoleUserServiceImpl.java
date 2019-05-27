package com.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.security.entity.ResponseBody;
import com.security.entity.RoleEntity;
import com.security.entity.RoleUserEntity;
import com.security.mapper.RoleUserMapper;
import com.security.service.RoleUserService;
import com.security.util.AttributeConfig;

@Service
public class RoleUserServiceImpl implements RoleUserService {
	
	@Autowired
	private RoleUserMapper mapper;

	@Override
	public String findAll(int curPageNo) {
		PageHelper.startPage(curPageNo, AttributeConfig.PAGE_RECORD_NUMBER);
		List<RoleEntity> list = mapper.findAll();
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", new PageInfo<RoleEntity>(list)).toString();
	}
	
	public String findUser(int userId) {
		List<RoleEntity> list = mapper.findUser(userId);
		if (list == null || list.size()==0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", list).toString();
	}
	
	@Override
	public String insert(RoleUserEntity entity) {
		int flag = mapper.insert(entity);
		if (flag == 1) {
			return new ResponseBody(AttributeConfig.SUCCESS,"保存成功！").toString();
		} else {
			return new ResponseBody(AttributeConfig.FAIL,"保存失败！").toString();
		}
	}
	
	@Override
	public String insertByBatch(List<RoleUserEntity> list, int userId) {
		int flag = mapper.delUser(userId);
		if (flag < 0) {
			return new ResponseBody(AttributeConfig.FAIL,"更新失败！").toString();
		}
		flag = mapper.insertByBatch(list);
		if (flag == list.size()) {
			return new ResponseBody(AttributeConfig.SUCCESS,"编辑成功！").toString();
		} else if (flag > 0) {
			return new ResponseBody(AttributeConfig.SUCCESS,"部分保存失败！").toString();
		} else {
			return new ResponseBody(AttributeConfig.FAIL,"保存失败！").toString();
		}
	}
	
	@Override
	public String delete(int roleId, int userId) {
		int flag = mapper.del(roleId, userId);
		if (flag >= 1) {
			return new ResponseBody(AttributeConfig.SUCCESS,"删除成功！").toString();
		} else {
			return new ResponseBody(AttributeConfig.FAIL,"删除失败！").toString();
		}
	}
	
	@Override
	public String deleteUser(int userId) {
		int flag = mapper.delUser(userId);
		if (flag >= 0) {
			return new ResponseBody(AttributeConfig.SUCCESS,"删除成功！").toString();
		} else {
			return new ResponseBody(AttributeConfig.FAIL,"删除失败！").toString();
		}
	}

}
