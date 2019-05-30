package com.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.security.entity.ResponseBody;
import com.security.entity.RoleEntity;
import com.security.mapper.PermissionRoleMapper;
import com.security.mapper.RoleMapper;
import com.security.mapper.RoleUserMapper;
import com.security.service.RoleService;
import com.security.util.AttributeConfig;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper mapper;
	
	@Autowired
	private RoleUserMapper roleUserMapper;
	
	@Autowired
	private PermissionRoleMapper permissionRoleMapper;

	@Override
	public RoleEntity findOne(int id) {
		return mapper.findOne(id);
	}

	@Override
	public String findAll(int curPageNo) {
		PageHelper.startPage(curPageNo, AttributeConfig.PAGE_RECORD_NUMBER);
		List<RoleEntity> list = mapper.findAll();
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", new PageInfo<RoleEntity>(list)).toString();
	}
	
	@Override
	public String insert(RoleEntity entity) {
		int flag = mapper.insert(entity);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"保存成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"保存失败！"));
		}
	}
	
	@Override
	public String update(RoleEntity entity) {
		int flag = mapper.update(entity);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"更新成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"更新失败！"));
		}
	}
	
	@Override
	public String delete(int id) {
		// 删除角色表信息
		int flag = mapper.del(id);
		if (flag< 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
		
		// 删除角色用户关系表信息
		roleUserMapper.delRole(id);
		if (flag< 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
		
		// 删除资源角色表信息
		permissionRoleMapper.delRole(id);
		if (flag >= 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"删除成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
	}

}
