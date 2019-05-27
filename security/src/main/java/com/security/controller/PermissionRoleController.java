package com.security.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.security.entity.PermissionRoleEntity;
import com.security.entity.ResponseBody;
import com.security.service.PermissionRoleService;
import com.security.util.AttributeConfig;
import com.security.util.JwtUtil;

@RestController
@RequestMapping("permissionRole")
public class PermissionRoleController {
	
	@Autowired
	private PermissionRoleService service;

	@GetMapping("/{roleId}")
	public String findRole(@PathVariable("roleId")Integer roleId) {
		if (roleId == null || roleId<0) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return service.findRole(roleId);
	}

	@GetMapping("/url")
	public String findUrl(HttpServletRequest request) {
		JSONObject user = JwtUtil.getUser(request);
		if (user == null) {
			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
		}
		return service.findUrl((int)user.get("id"));
	}

//	@GetMapping("/permission")
//	public String findPermission(HttpServletRequest request) {
//		UserEntity user = (UserEntity) request.getSession().getAttribute(AttributeConfig.LOGIN_USER);
//		if (user == null) {
//			return new ResponseBody(AttributeConfig.NOT_FOUND_DATA, "No records were found").toString();
//		}
//		return service.findRole(user.getId());
//	}

	@PostMapping
	public String save(String permissionId, Integer roleId) {
		if (roleId == null || roleId < 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"请选择角色！"));
		}
		
		if (permissionId == null || "" .equals(permissionId)) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"请选择资源！"));
		}
		List<PermissionRoleEntity> list = new ArrayList<PermissionRoleEntity>();
		PermissionRoleEntity entity = null;
		for (String id : permissionId.split(",")) {
			if (id != null && !"".equals(id)) {
				entity = new PermissionRoleEntity();
				entity.setRoleId(roleId);
				entity.setPermissionId(Integer.parseInt(id));
				list.add(entity);
			}
		}
		if (list.size() < 1) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"赋权失败！"));
		}
		return service.insertByBatch(list, roleId);
	}
}
