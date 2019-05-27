package com.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.entity.RoleUserEntity;
import com.security.service.RoleUserService;
import com.security.util.AttributeConfig;

@RestController
@RequestMapping("roleUser")
public class RoleUserController {
	
	@Autowired
	private RoleUserService service;

	@GetMapping("/{userId}")
	public String findUser(@PathVariable("userId")Integer userId) {
		if (userId == null || userId<0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return service.findUser(userId);
	}

	@PostMapping
	public String save(String roleId, Integer userId) {
		if (userId == null || userId < 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"请选择用户！"));
		}
		
		if (roleId == null || "".equals(roleId)) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"请选择角色！"));
		}
		
		// 通过前台传递的参数封装对象集合
		List<RoleUserEntity> list = new ArrayList<RoleUserEntity>();
		RoleUserEntity entity = null;
		for (String id : roleId.split(",")) {
			if (id != null && !"".equals(id)) {
				entity = new RoleUserEntity();
				entity.setUserId(userId);
				entity.setRoleId(Integer.parseInt(id));
				list.add(entity);
			}
		}
		if (list.size() < 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"赋权失败！"));
		}
		return service.insertByBatch(list, userId);
	}
}
