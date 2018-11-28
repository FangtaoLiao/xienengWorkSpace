package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceDetailCentralInverter;

@Repository
public interface DeviceDetailCentralInverterMapper {
	public List<Map> listInverterInfo(Map map);
	public int countInverterInfo(Map map);
	public Map getEquipmentBasic(String deviceId);
	public int updateStatus(Map map);
	public int updateEquipmentBasic(Map map);
	public int updatePhotoNull(String deviceId);
	public int updatePhoto(Map map);
	public DeviceDetailCentralInverter getInverterById(String deviceId);
	public List<Map<String,Object>> getInverterInfo(Map map);
	public int insertInverter(List list);
	public int insertInverterByMap(DeviceDetailCentralInverter inverter);
	public DeviceDetailCentralInverter getModel(Integer deviceModelId);
	public Integer updateIdentity(Map<String, Object> pMap);
	public DeviceDetailCentralInverter getFactoryAndPower(int modelId2);
	public List<DeviceDetailCentralInverter> getModelforChoice();
	public List<DeviceDetailCentralInverter> getAllModelInfoCen();
}