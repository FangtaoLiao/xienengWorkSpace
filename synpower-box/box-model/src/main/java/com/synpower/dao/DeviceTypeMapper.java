package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceType;
@Repository
public interface DeviceTypeMapper {

	public Integer getIdByName(String typeString);

	public String getNameById(String typeId);

	public List<Integer> getIdByCon(String deviceType);
	
	public List<DeviceType> getDeviceTypeInfo();
	
}