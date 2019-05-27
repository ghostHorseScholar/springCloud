package com.security.config.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.security.entity.ResponseBody;
import com.security.util.AttributeConfig;

/**
 * 退出登录
 * @author 160013
 *
 */
@Component
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger log = LoggerFactory.getLogger(AjaxLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("退出登录");
		response.setContentType("text/javascript");
		response.setHeader(AttributeConfig.TOKEN_NAME, null);
        response.getWriter().write(new ResponseBody(AttributeConfig.SUCCESS,"Need Authorities!").toString());
	}

}
