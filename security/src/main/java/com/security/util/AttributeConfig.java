package com.security.util;

/**
 * 常用属性配置
 * @author 160013
 *
 */
public class AttributeConfig {
	public static final String TOKEN_NAME = "accToken"; // 校验token
	public static final String TOKEN_CLAIMS_PERMISSION = "permissions"; // 校验token
	public static final String TOKEN_CLAIMS_PERMISSION_USER = "permissionUser"; // 校验token
	
	public static final String SUCCESS = "200"; // 操作成功
	public static final String FAIL = "400"; // 操作失败
	public static final String NOT_FOUND_DATA = "404"; // 没有找到数据
	
	public static final String PERMISSION_NOT = "403"; // 没有权限

	
	public static final String LOGIN_NOT = "300"; // 没有登录
	public static final String LOGIN_FAIL = "301"; // 登录失败
	public static final String LOGIN_OUT_TIME = "302"; // 登录超时
	
	
	public static final int PAGE_RECORD_NUMBER = 5; // 每页记录数
	

	public static final String LOGIN_USER = "loginUser"; // 用户信息
	public static final String LOGIN_USER_URL = "loginUserUrl"; // 用户所拥有的菜单权限
}
