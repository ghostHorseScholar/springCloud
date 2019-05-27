package com.thymeleaf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.thymeleaf.util.AttributeConfig;
import com.thymeleaf.util.RequestUtil;
import com.thymeleaf.util.Utils;

@Controller
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value= {"/","/login","/index"})
	public String index() {
		return "login";
	}
	
	@GetMapping(value= {"/home"})
	public String home(Model model, HttpServletRequest request) {
		log.info("token==>", request.getSession().getAttribute(AttributeConfig.TOKEN_NAME));
		ResponseEntity<String> entity = restTemplate.exchange(AttributeConfig.SECURITY_URL + "/permissionRole/url", HttpMethod.GET, RequestUtil.getHttpEntity(request), String.class);
		log.info("status==>{},body==>{}", entity.getStatusCode(), entity.getBody());
		return Utils.getBody(request, entity, model , "home");
	}

	@PostMapping("login")
	public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		if (username == null || "".equals(username) || password == null || "".equals(password)) {
			return "login";
		}
		
		MultiValueMap<String,String> requestMap = new LinkedMultiValueMap<String,String>();
		requestMap.add("username", username);
		requestMap.add("password", password);
		
		try {
			ResponseEntity<String> entity = restTemplate.postForEntity(AttributeConfig.SECURITY_URL+"/login", requestMap, String.class);
			
			return Utils.getBody(request, entity, null , "redirect:/home");
		} catch (RestClientException e) {
			e.printStackTrace();
			return "login";
		}
	}

	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute(AttributeConfig.TOKEN_NAME, null);
		ResponseEntity<String> entity = restTemplate.getForEntity(AttributeConfig.SECURITY_URL+"/logout", String.class);
		return Utils.getBody(request, entity, null , "login");
	}
}
