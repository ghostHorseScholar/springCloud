package com.security.filter;

import java.io.IOException;
import java.util.List;

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

import com.alibaba.fastjson.JSON;
import com.security.entity.ResponseBody;
import com.security.util.AttributeConfig;
import com.security.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

@Component
public class PermissionFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(PermissionFilter.class);

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		log.info("过滤器。。。。。。。。。。。。。。。。。。。。。。。。");
		try {
			// 请求中获取token
			String token = req.getHeader(AttributeConfig.TOKEN_NAME);
			if (token==null || "".equals(token.trim())) {
				rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.PERMISSION_NOT,"No token was found")));
				return;
			}
			
			
			// 刷新token
			token = JwtUtil.refresh(token);
			if (token == null) {
				rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.LOGIN_OUT_TIME,"login timeout")));
				return ;
			}
			rep.setHeader(AttributeConfig.TOKEN_NAME, token);
			
			String code = req.getRequestURI().split("/")[1] + req.getMethod();
			
			Claims claims = JwtUtil.parseJWT(token);
			
			//校验用户token是否有效
			if (claims.getId().equals(req.getSession().getAttribute(claims.getIssuer()))) {
				rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.LOGIN_NOT,"Need Authorities!")));
				return ;
			}
			
			// 判断用户是否拥有访问资源的权限
			List<String> permission = claims.get(AttributeConfig.TOKEN_CLAIMS_PERMISSION, List.class); // 所有资源权限
			List<String> userPermission = claims.get(AttributeConfig.TOKEN_CLAIMS_PERMISSION_USER, List.class); // 用户所拥有的资源权限
			
			log.info("code==>{}", code);
			
			if (permission.contains(req.getRequestURI()) && !userPermission.contains(req.getRequestURI())) { // 菜单判断
				rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.PERMISSION_NOT,"Need Authorities!")));
				return ;
			}
			if (permission.contains(code) && !userPermission.contains(code)) { // 按钮判断
				rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.PERMISSION_NOT,"Need Authorities!")));
				return ;
			}
			chain.doFilter(request, response);
			
		} catch (ExpiredJwtException e) {
			log.info("登录超时");
			rep.getWriter().write(JSON.toJSONString(new ResponseBody(AttributeConfig.LOGIN_OUT_TIME,"login timeout")));
		}
	}

}
