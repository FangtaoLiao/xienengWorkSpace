package com.synpower.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleMapper {
	public int insertUserRole(Map map);
	public int updateUserRoleId(Map map);
	/** 
	  * @Title:  delUserRole 
	  * @Description:  删除用户的角色关联
	  * @param userId
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日上午11:33:21
	*/ 
	public int delUserRole(int userId);
}