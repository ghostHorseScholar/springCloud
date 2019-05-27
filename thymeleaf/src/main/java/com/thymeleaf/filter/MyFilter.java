package com.thymeleaf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.thymeleaf.util.AttributeConfig;
import com.thymeleaf.util.Utils;

@Component
public class MyFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(MyFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.info("thymeleaf 过滤器。。。。。。。。。。。。。。。。。。。。");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		String url = httpServletRequest.getRequestURI();
		
		log.info(url);
		
		if (url.contains("/login") || url.contains("/logout") || url.contains(".ico") || url.contains(".css") || url.contains(".js") || url.contains(".jpg") || url.contains(".png") || url.contains(".gif") || url.contains(".eot")) {
			chain.doFilter(request, response);
		} else {
			Object token = httpServletRequest.getSession().getAttribute(AttributeConfig.TOKEN_NAME);
			if (Utils.isEmpty(token)) {
				httpServletResponse.sendRedirect("/login");
				return;
			}
	
			chain.doFilter(request, response);
		}
	}

}
