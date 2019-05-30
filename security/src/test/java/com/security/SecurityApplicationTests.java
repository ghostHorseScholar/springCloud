package com.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.security.entity.UserEntity;
import com.security.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {
	
	@Autowired
	private UserService service;

	@Test
	public void contextLoads() {
		UserEntity user = new UserEntity();
		user.setUserName("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		System.out.println(service.insert(user));
	}

}
