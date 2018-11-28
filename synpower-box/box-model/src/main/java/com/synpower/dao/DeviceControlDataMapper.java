package com.synpower.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceControlData;
@Repository
public interface DeviceControlDataMapper {
	public DeviceControlData getValueOfGuid(String signalGuid);
	public int saveControl(DeviceControlData deviceControlData);
	public DeviceControlData getValueOfYKGuid(Map map);
}