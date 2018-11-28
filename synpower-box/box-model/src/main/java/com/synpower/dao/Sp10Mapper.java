package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.SysOrg;
import com.synpower.bean.SysUser;

public interface Sp10Mapper {
	int insertUserInfo(SysUser user);
	List<SysUser> getUserByLoginId(String loginId);
	List<SysUser> getUserByTel(String tel);
	public int countUser(Map map);
	public List<SysUser> listUserInfo(Map map);
	public int updateUserStatus(Map map);
	public int updateUserValid(Map map);
	public SysUser getUserInfo(String id);
	public int updateUserInfo(SysUser user);
}
