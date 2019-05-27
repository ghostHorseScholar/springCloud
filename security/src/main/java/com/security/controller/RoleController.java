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

import com.security.entity.ResponseBody;
import com.security.entity.RoleEntity;
import com.security.service.RoleService;
import com.security.util.AttributeConfig;

@RestController
@RequestMapping("role")
public class RoleController {
	
	@Autowired
	private RoleService service;

	@GetMapping
	public String find(Integer curPageNo) {
		if (curPageNo == null || curPageNo<0) {
			curPageNo = 1;
		}
		return service.findAll(curPageNo);
	}
	
	@GetMapping("/{id}")
	public String findOne(@PathVariable("id")int id) {
		RoleEntity Role = service.findOne(id);
		return new ResponseBody(AttributeConfig.SUCCESS,"操作成功",Role).toString();
	}
	
	@PutMapping
	public String update(@ModelAttribute RoleEntity entity, HttpServletRequest request) {
		return service.update(entity);
	}
	
	@PostMapping
	public String insert(@ModelAttribute RoleEntity entity) {
		return service.insert(entity);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id")int id) {
		return service.delete(id);
	}
}
