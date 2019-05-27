package com.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.security.service.PermissionRoleService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {
	
	@Autowired
	private PermissionRoleService service;

	@Test
	public void contextLoads() {
		System.out.println(service.findUrl(4));
	}

}
