package com.security.config.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.util.AttributeConfig;

/**
 * 没有登录的返还结果
 * @author 160013
 *
 */
@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final Logger log = LoggerFactory.getLogger(AjaxAuthenticationEntryPoint.class);


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    	
    	log.info("没有登录返还结果");
    	
    	String body = JSON.toJSONString(new ResponseBody(AttributeConfig.LOGIN_NOT,"Need Authorities!"));

    	String callback = request.getParameter("callback"); 
    	if (callback == null || callback == "") {
    		response.getWriter().write(body);
    	} else {
        	response.setContentType("text/javascript");
        	response.getWriter().write(callback + "(" + body + ")");
    	}
    }
}