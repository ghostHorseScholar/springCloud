package com.thymeleaf.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.thymeleaf.util.AttributeConfig;
import com.thymeleaf.util.RequestUtil;
import com.thymeleaf.util.Utils;

@Controller
public class AuthorizationController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/roleUser/{userId}")
	public String roleUser(@PathVariable("userId")Integer userId, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/roleUser/" + userId, HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		model.addAttribute("userId", userId);
		
		return Utils.getBody(request, entity, model, "roleAuthorization");
	}
	
	@PostMapping("/roleUser")
	public String roleUser(Integer[] roleId, Integer userId, Model model, HttpServletRequest request) {
		
		String ids = "";
		for (int id : roleId) {
			ids += id + ",";
		}
		
		MultiValueMap<String,Object> requestMap = new LinkedMultiValueMap<String,Object>();
		requestMap.add("roleId", ids);
		requestMap.add("userId", userId);

		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/roleUser", HttpMethod.POST, RequestUtil.getHttpEntity(request, requestMap), String.class);
		
		restTemplate.postForEntity(AttributeConfig.SECURITY_URL+"/permissionRole", requestMap, String.class);
		
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		return Utils.getBody(request, entity, model, "redirect:/roleUser/" + userId);
	}
	
	@GetMapping("/permissionRole/{roleId}")
	public String permissionRole(@PathVariable("roleId")Integer roleId, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permissionRole/" + roleId, HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		model.addAttribute("roleId", roleId);
		
		return Utils.getBody(request, entity, model, "permissionAuthorization");
	}
	
	@PostMapping("/permissionRole")
	public String permissionRole(String permissionId, Integer roleId, Model model, HttpServletRequest request) {
		
		MultiValueMap<String,Object> requestMap = new LinkedMultiValueMap<String,Object>();
		requestMap.add("permissionId", permissionId);
		requestMap.add("roleId", roleId);

		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permissionRole", HttpMethod.POST, RequestUtil.getHttpEntity(request, requestMap), String.class);
		
		restTemplate.postForEntity(AttributeConfig.SECURITY_URL+"/permissionRole", requestMap, String.class);
		
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		return Utils.getBody(request, entity, model, "redirect:/permissionRole/" + roleId);
	}
}
