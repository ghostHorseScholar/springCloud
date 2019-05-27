package com.security.config.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.service.PermissionService;
import com.security.service.UserService;
import com.security.util.AttributeConfig;
import com.security.util.JwtUtil;

/**
 * 登录成功
 * @author 160013
 *
 */
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final Logger log = LoggerFactory.getLogger(AjaxAuthenticationSuccessHandler.class);
	
	@Autowired
	private PermissionService permissionService;

	@Autowired
	private UserService userService;

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	log.info("登录成功");
    	
    	String id = UUID.randomUUID().toString();
    	request.getSession().setAttribute(authentication.getName(), id);
    	
    	List<String> list = new ArrayList<String>();
    	for(GrantedAuthority permission : authentication.getAuthorities()) {
    		list.add(permission.getAuthority());
        }
    	response.setContentType("text/javascript");
    	response.setHeader(AttributeConfig.TOKEN_NAME, JwtUtil.createJWT(id, permissionService.getPermission(), list, authentication.getName(), "VOUCHER",userService.findOne(authentication.getName())));
    	response.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.SUCCESS,"Need Authorities!")));
    }
}
