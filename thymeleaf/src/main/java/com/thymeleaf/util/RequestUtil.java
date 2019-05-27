package com.thymeleaf.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public class RequestUtil {

	// 设置请求头
	public static HttpEntity<MultiValueMap<String, Object>> getHttpEntity(HttpServletRequest request, MultiValueMap<String, Object> paramMap) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AttributeConfig.TOKEN_NAME, getToken(request));
		return new HttpEntity<MultiValueMap<String, Object>>(paramMap,headers);
	}
	
	// 设置请求头
	public static HttpEntity<MultiValueMap<String, Object>> getHttpEntity(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(AttributeConfig.TOKEN_NAME, getToken(request));
		return new HttpEntity<MultiValueMap<String, Object>>(null,headers);
	}
	
	//  获取请求中的token
	public static String getToken(HttpServletRequest request) {
		if (request.getSession().getAttribute(AttributeConfig.TOKEN_NAME) == null) {
			return null;
		}
		return request.getSession().getAttribute(AttributeConfig.TOKEN_NAME).toString();
	}
}
