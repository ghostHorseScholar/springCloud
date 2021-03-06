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

import com.thymeleaf.entity.RoleEntity;
import com.thymeleaf.util.AttributeConfig;
import com.thymeleaf.util.RequestUtil;
import com.thymeleaf.util.Utils;

@Controller
@RequestMapping("role")
public class RoleController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public String role(Integer pageNum, Model model, HttpServletRequest request) {
		
		Map<String,Object> requestMap = new HashMap<String,Object>();
		requestMap.put("curPageNo", pageNum);
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/role?curPageNo={curPageNo}", HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class, requestMap);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "role");
	}

	@GetMapping("{id}")
	public String findOne(@PathVariable("id")int id, Model model, HttpServletRequest request) {
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/role/"+id, HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "roleEdit");
	}

	@GetMapping("roleAdd")
	public String roleAdd() {
		return "roleAdd";
	}
	
	@PostMapping("edit")
	public String update(@ModelAttribute RoleEntity body, Model model, HttpServletRequest request) {
		
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/role", HttpMethod.PUT, RequestUtil.getHttpEntity(request, getMultiValueMap(body)), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/role");
	}
	
	@PostMapping
	public String insert(@ModelAttribute RoleEntity body, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/role", HttpMethod.POST, RequestUtil.getHttpEntity(request, getMultiValueMap(body)), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/role");
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id")int id, Model model, HttpServletRequest request) {
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/role/"+id, HttpMethod.DELETE, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		
		return Utils.getBody(request, entity, model, "redirect:/role");
	}

	private MultiValueMap<String, Object> getMultiValueMap(RoleEntity entit) {
		MultiValueMap<String,Object> requestMap = new LinkedMultiValueMap<String,Object>();
		
		requestMap.add("id", entit.getId());
		requestMap.add("name", entit.getName());
		
		return requestMap;
	}

}
