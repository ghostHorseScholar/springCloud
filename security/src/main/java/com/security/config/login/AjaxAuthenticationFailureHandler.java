package com.security.config.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.util.AttributeConfig;

/**
 * 登录失败
 * @author 160013
 *
 */
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	private static final Logger log = LoggerFactory.getLogger(AjaxAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    	log.info("登录失败");
    	
    	response.setContentType("text/javascript");

    	response.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.LOGIN_FAIL,"Login Failure!")));
    }
}