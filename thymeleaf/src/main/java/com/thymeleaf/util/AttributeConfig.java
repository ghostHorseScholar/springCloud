package com.thymeleaf.util;

public class AttributeConfig {
	
	public static final String TOKEN_NAME = "accToken"; // 校验token

	public static final String SECURITY_URL = "http://localhost:8080";
	
	public static final String SUCCESS = "200"; // 操作成功
	public static final String FAIL = "400"; // 操作失败
	
	public static final String PERMISSION_NOT = "403"; // 没有权限
	
	public static final String LOGIN_NOT = "300"; // 没有登录
	public static final String LOGIN_FAIL = "301"; // 登录失败
	public static final String LOGIN_OUT_TIME = "302"; // 登录超时

}
