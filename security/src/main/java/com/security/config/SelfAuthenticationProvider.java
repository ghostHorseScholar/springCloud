package com.security.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.security.entity.UserEntity;
import com.security.service.PermissionService;
import com.security.service.UserService;


/**
 * 
 * @author 160013
 *
 */
@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
	
	private Logger logger = LoggerFactory.getLogger(SelfAuthenticationProvider.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	logger.info("进入自定义校验方法,只有登录是才会校验");
    	String userName = authentication.getName();
        String password = (String) authentication.getCredentials(); // 这个是表单中输入的密码；
        
        UserEntity entity = userService.findOne(userName);

        if (entity == null || !new BCryptPasswordEncoder().matches(password, entity.getPassword())) {
        	logger.error("用户名密码不正确，请重新登陆！");
            throw new BadCredentialsException("用户名密码不正确，请重新登陆！");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (String role : permissionService.getPermission(entity.getId())) {
        	if (role == null || "".equals(role)) {
        		role = "#";
        	}
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
        return new UsernamePasswordAuthenticationToken(userName, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
