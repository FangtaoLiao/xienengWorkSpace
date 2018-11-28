package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceDetailCentralInverter;
import com.synpower.bean.DeviceDetailStringInverter;


@Repository
public interface DeviceDetailStringInverterMapper {

	
	public DeviceDetailStringInverter getModel(Integer deviceModelId);
	public String getIdByName(String typeString);
	public Integer updateIdentity(Map<String, Object> pMap);
	public int updateStatus(Map map);
	public int updateEquipmentBasic(Map map);
	public int updatePhotoNull(String deviceId);
	public int updatePhoto(Map map);
	public DeviceDetailStringInverter getStringInverterById(String deviceId);
	public int insertInverterByMap(DeviceDetailCentralInverter inverter);
	public int insertInverter(List list);
	public List<Map<String,Object>> getInverterInfo(Map map);
	public DeviceDetailCentralInverter getFactoryAndPower(int modelId2);
	public List<DeviceDetailStringInverter> getModelforChoice(Map<String, Object> paraMap);
	public Integer getInverterCount();
	public List<DeviceDetailStringInverter> getAllmodelInfoStr();
}