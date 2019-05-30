package com.thymeleaf.entity;

import java.util.List;

import lombok.Data;

@Data
public class PermissionEntity {

	private Integer id;
	private String name;
	private String descritpion;
	private String url;
	private Integer pid;
	private String type;
	
	private Integer roleId;
	private Integer userId;
	
	private List<PermissionEntity> subPermission;
	
}
