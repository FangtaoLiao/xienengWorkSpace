package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceDetailModule;

@Repository
public interface DeviceDetailModuleMapper {
	public int countSubassembly(Map map);
	public List<Map> listSubassembly(Map map);
	public int updateSubStatus(Map map);
	public Map getSubBasic(String deviceId);
	public int updateSubBasic(Map map);
	public int updatePhotoNull(String deviceId);
	public int updatePhoto(Map map);
	public DeviceDetailModule getSubById(String deviceId);
	public List<Map<String,Object>> getSubInfo(Map map);
	public int insertSub(List list);
	public int insertSubByBean(DeviceDetailModule sub);
	public List<DeviceDetailModule> getModeleInfoSearch(Map<String, Object> paraMap);
	public Integer getModeleInfoSearchPage(Map<String, Object> paraMap);
}