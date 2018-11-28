package com.synpower.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.AlertorSetting;

@Repository
public interface AlertorSettingMapper {
	public AlertorSetting getAlertorInfoByUserId(String userId);
	public int updateAlertorInfoByUserId(Map map);
	public int insertAlertorInfo(Map map);
}