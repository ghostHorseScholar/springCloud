package com.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.entity.PermissionEntity;
import com.security.entity.ResponseBody;
import com.security.service.PermissionService;
import com.security.util.AttributeConfig;

@RestController
@RequestMapping("permission")
public class PermissionController {
	
	@Autowired
	private PermissionService service;

	@GetMapping
	public String find(Integer curPageNo) {
		if (curPageNo == null || curPageNo<0) {
			curPageNo = 1;
		}
		return service.findAll(curPageNo);
	}
	
	@GetMapping("/{id}")
	public String findOne(@PathVariable("id")int id) {
		PermissionEntity Permission = service.findOne(id);
		return new ResponseBody(AttributeConfig.SUCCESS,"操作成功",Permission).toString();
	}
	
	@PutMapping
	public String update(@ModelAttribute PermissionEntity entity, HttpServletRequest request) {
		return service.update(entity);
	}
	
	@PostMapping
	public String insert(@ModelAttribute PermissionEntity entity) {
		return service.insert(entity);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id")int id) {
		return service.delete(id);
	}
}
