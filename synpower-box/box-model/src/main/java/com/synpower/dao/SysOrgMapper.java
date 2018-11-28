package com.synpower.dao;

import java.util.List;
import java.util.Map;

import com.synpower.bean.PlantInfo;
import com.synpower.bean.SysOrg;
import com.synpower.bean.SysRights;
import com.synpower.bean.SysUser;

public interface SysOrgMapper {
	public List<SysOrg> getSimilar(String orgName);
	public List<SysOrg> getAllOrg(); 
	public SysOrg getOrgInfoByUser(String org_id);
	public int addOrgInfo(SysOrg sysOrg);
	public int updateOrg(SysOrg sysOrg);
	public int deleteOrg(Map map);
	public SysOrg getOrgAgentor(String userName, String org_id);
	public int countOrg(Map newMap);
	public SysOrg getOrgName(String id);
	public List<SysOrg> getSubsetOrg(Map map);
	public List<SysOrg> listOrgInfo(Map parameterMap);
	public SysOrg getOrgIntroduction(String id);
	public List<SysRights> getRightsByUser(int parseInt);
	public SysOrg getlogo(String orgId);
	public List<SysRights> getRightsByRole(Map<String, Object> map);
	public SysOrg getEverryNum(Map<String, Object> map);
	public SysOrg getLogoAndName(String id);
	public int updateSystemInfo(SysOrg org);
	public List<SysOrg> getContacts(List<Integer> list);
	public List<Integer> getPids(Map<String, Object> map);
	public List<Integer> getOrgids(Map<String, Object> map);
	public List<Map<String, Object>> getOrgCode();
	public List<String> getSubOrgs(Map<String, Object> parameterMap);
	public String getActualId(String plantofOrg);
	public SysOrg getOrgFatherId(String orgId);
	public SysOrg getOrgDesc(String orgId);
	public int updateOrgDesc(Map map);
	public int updateScreenLogoNull(String orgId);
	public int updateScreenLogo(Map map);
	public int updateSystemLogoNull(String orgId);
	public int updateSystemLogo(Map map);
	public int updateSystemName(Map map);
	public SysOrg getOrgById(String orgId);
	public int updateOrgPhoto(Map map);
	public int updatePropagandaPhoto(Map map);
	public int updateOrgPhotoNull(String orgId);
	public List<SysOrg> getOrgNameByIds(List<String> list);
}