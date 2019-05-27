package com.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.entity.UserEntity;
import com.security.service.UserService;
import com.security.util.AttributeConfig;
import com.security.util.JwtUtil;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService service;

	@GetMapping
	public String find(Integer curPageNo) {
		if (curPageNo == null || curPageNo<0) {
			curPageNo = 1;
		}
		return service.findAll(curPageNo);
	}
	
	@GetMapping("/{id}")
	public String findOne(@PathVariable("id")int id) {
		UserEntity user = service.findOne(id);
		return new ResponseBody(AttributeConfig.SUCCESS,"操作成功",user).toString();
	}
	
	@PutMapping("/password")
	public String update(String newPass, String oldPass, HttpServletRequest request) {
		String userName = JwtUtil.getUserName(request);
		UserEntity user = service.findOne(userName);
		if (!new BCryptPasswordEncoder().matches(oldPass, user.getPassword())) {
			return JSON.toJSONString(new ResponseBody(AttributeConfig.FAIL,"旧密码错误！"));
		}
		user.setPassword(new BCryptPasswordEncoder().encode(newPass));
		return service.update(user);
	}
	
	@PutMapping
	public String edit(@ModelAttribute UserEntity entity, HttpServletRequest request) {
		entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
		return service.update(entity);
	}
	
	@PostMapping
	public String insert(@ModelAttribute UserEntity entity) {
		entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
		return service.insert(entity);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id")int id) {
		return service.delete(id);
	}
}
