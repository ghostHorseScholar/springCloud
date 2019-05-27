package com.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.security.entity.PermissionEntity;

@Mapper
public interface PermissionMapper extends CoreMapper<PermissionEntity> {

	List<String> getPermission(@Param("userId")Integer userId);
}
