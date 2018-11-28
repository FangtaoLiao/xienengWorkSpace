package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysOrg;
import com.synpower.bean.SysRole;

@Repository
public interface SysRoleMapper {
	public int deleteRightsOfRole(String roleId);
	public int saveRightsOfRole(Map map);
	public int saveNewRole(SysRole sysRole);
	public int updateRoleInfo(Map map);
	public List<SysRole> getAllRolesByOrgList(List<String>list);
	public int deleteRightOfRoles(Map map);
	public int addOrgDefault(List<SysRole> list);
	public List<SysRole> getActualOrgRoles(String org_id);
	public int updateRoleValid(String roleId);
	public List<SysRole> getRoleByOrgId(String orgId);
	public List<SysRole> getRoleAll(List<String> list);
	public List<SysRole> getRoleByOrgIds(List<String> list);
	public SysRole getRoleById(int id);
	public List<SysRole> getSameNameById(String roleId);
}