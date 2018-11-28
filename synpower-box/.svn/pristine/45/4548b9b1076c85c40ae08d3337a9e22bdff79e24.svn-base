package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysRights;

@Repository
public interface SysRightsMapper {
	public List<SysRights> getFatherRights();
	public List<SysRights> getChildRights(String fatherId);
	public List<SysRights> getRightsByRoleId(String roleId);
	public List<SysRights> getFatherRightsByRoleId(String roleId);
	public List<SysRights> getChildRightsByRoleId(Map map);
}