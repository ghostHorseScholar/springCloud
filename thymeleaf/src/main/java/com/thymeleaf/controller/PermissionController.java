package com.thymeleaf.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.thymeleaf.entity.PermissionEntity;
import com.thymeleaf.util.AttributeConfig;
import com.thymeleaf.util.RequestUtil;
import com.thymeleaf.util.Utils;

@Controller
@RequestMapping("permission")
public class PermissionController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public String permission(Integer pageNum, Model model, HttpServletRequest request) {
		
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("curPageNo", pageNum);
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permission?curPageNo={curPageNo}", HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class, requestMap);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "permission");
	}

	@GetMapping("{id}")
	public String findOne(@PathVariable("id")int id, Model model, HttpServletRequest request) {
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permission/"+id, HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "permissionEdit");
	}

	@GetMapping("permissionAdd")
	public String permissionAdd(Integer pid, Model model) {
		model.addAttribute("pid", pid);
		return "permissionAdd";
	}
	
	@PostMapping("/edit")
	public String update(@ModelAttribute PermissionEntity body, Model model, HttpServletRequest request) {
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permission", HttpMethod.PUT, RequestUtil.getHttpEntity(request, getMultiValueMap(body)), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/permission");
	}
	
	@PostMapping
	public String insert(@ModelAttribute PermissionEntity body, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permission", HttpMethod.POST, RequestUtil.getHttpEntity(request, getMultiValueMap(body)), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/permission");
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id")int id, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permission/"+id, HttpMethod.DELETE, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/permission");
	}

	private MultiValueMap<String, Object> getMultiValueMap(PermissionEntity entit) {
		MultiValueMap<String,Object> requestMap = new LinkedMultiValueMap<String,Object>();

		requestMap.add("id", entit.getId());
		requestMap.add("name", entit.getName());
		requestMap.add("descritpion", entit.getDescritpion());
		requestMap.add("url", entit.getUrl());
		requestMap.add("pid", entit.getPid());
		requestMap.add("type", entit.getType());
		
		return requestMap;
	}
}
