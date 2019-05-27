package com.thymeleaf.util;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtils {
	
	private static RestTemplate restTemplate;
	
	public static RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(15000);
			factory.setReadTimeout(5000);
			return new RestTemplate(factory);
		}
		return restTemplate;
	}

}