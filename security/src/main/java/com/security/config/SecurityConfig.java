/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.security.config.login.AjaxAuthenticationEntryPoint;
import com.security.config.login.AjaxAuthenticationFailureHandler;
import com.security.config.login.AjaxAuthenticationSuccessHandler;
import com.security.config.login.AjaxLogoutSuccessHandler;

/**
 * @author Joe Grandja
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;  //  未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler;  // 登录成功返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler;  //  登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxLogoutSuccessHandler  logoutSuccessHandler;  // 注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）

    @Autowired
    SelfAuthenticationProvider provider; // 自定义安全认证
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		
		// 开启跨域
		.cors()

		// 通过浏览器直接访问资源,httpBasic如果没有登录,会跳转到登录页面，exceptionHandling则会返还authenticationEntryPoint的响应
//       .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)
//		.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)

//		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		
        .and()
        .authorizeRequests().antMatchers("/").permitAll()
//        .anyRequest().authenticated()// 其他 url 需要身份认证

        .and()
        .formLogin()  //开启登录
        .successHandler(authenticationSuccessHandler) // 登录成功
        .failureHandler(authenticationFailureHandler) // 登录失败
        .permitAll()
        
//        .and().addFilter(filter)
        
        .and()
        .logout()
        .logoutSuccessHandler(logoutSuccessHandler)
        .permitAll();
		
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(provider);
    }

	/*
	 * @Bean UserDetailsService customUserService() { return new
	 * MyUserDetailsService(); }
	 */
    
    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
