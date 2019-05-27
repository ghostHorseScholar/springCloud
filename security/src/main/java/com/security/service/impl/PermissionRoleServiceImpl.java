package com.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.security.entity.PermissionEntity;
import com.security.entity.PermissionRoleEntity;
import com.security.entity.ResponseBody;
import com.security.mapper.PermissionRoleMapper;
import com.security.service.PermissionRoleService;
import com.security.util.AttributeConfig;

@Service
public class PermissionRoleServiceImpl implements PermissionRoleService {
	
	@Autowired
	private PermissionRoleMapper mapper;

	@Override
	public String findAll(int curPageNo) {
		PageHelper.startPage(curPageNo, AttributeConfig.PAGE_RECORD_NUMBER);
		List<PermissionEntity> list = mapper.findAll();
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", new PageInfo<PermissionEntity>(list)).toString();
	}
	
	public String findRole(int roleId) {
		List<PermissionEntity> list = mapper.findRole(roleId);
		if (list == null || list.size()==0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", list).toString();
	}
	
	public String findUser(int userId) {
		List<PermissionEntity> list = mapper.findUser(userId);
		if (list == null || list.size()==0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", list).toString();
	}
	
	public List<PermissionEntity> findUrl(int userId, int pid, List<PermissionEntity> list) {
		for (PermissionEntity entity : mapper.findUrl(userId, pid)) {
			List<PermissionEntity> list2 = findUrl(userId, entity.getId(), new ArrayList<PermissionEntity>());
			if (list2 != null && list2.size()>0)
				entity.setSubPermission(list2);
			list.add(entity);
		}
		return list;
	}
	
	public String findUrl(int userId) {
		List<PermissionEntity> list = findUrl(userId, 0, new ArrayList<PermissionEntity>());
		if (list == null || list.size()==0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", list).toString();
	}
	
	public List<PermissionEntity> findPermission(int roleId, int pid, List<PermissionEntity> list) {
		for (PermissionEntity entity : mapper.findPermission(roleId, pid)) {
			List<PermissionEntity> list2 = findPermission(roleId, entity.getId(), new ArrayList<PermissionEntity>());
			if (list2 != null && list2.size()>0)
				entity.setSubPermission(list2);
			list.add(entity);
		}
		return list;
	}
	
	public String findPermission(int roleId) {
		List<PermissionEntity> list = findPermission(roleId, 0, new ArrayList<PermissionEntity>());
		if (list == null || list.size()==0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return new ResponseBody(AttributeConfig.SUCCESS, "操作成功", list).toString();
	}
	
	@Override
	public String insert(PermissionRoleEntity entity) {
		int flag = mapper.insert(entity);
		if (flag == 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"保存成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"保存失败！"));
		}
	}
	
	@Override
	public String insertByBatch(List<PermissionRoleEntity> list, int roleId) {
		int flag = mapper.delRole(roleId);
		if (flag < 0) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"更新失败！"));
		}
		flag = mapper.insertByBatch(list);
		if (flag == list.size()) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"编辑成功！"));
		} else if (flag > 0) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"部分保存失败！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"保存失败！"));
		}
	}
	
	@Override
	public String delete(int permissionId, int roleId) {
		int flag = mapper.del(permissionId, roleId);
		if (flag >= 0) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"删除成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
	}
	
	@Override
	public String deleteRole(int roleId) {
		int flag = mapper.delRole(roleId);
		if (flag >= 0) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"删除成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
	}
	
	@Override
	public String deleteUser(int userId) {
		int flag = mapper.delUser(userId);
		if (flag >= 0) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"删除成功！"));
		} else {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"删除失败！"));
		}
	}

}
