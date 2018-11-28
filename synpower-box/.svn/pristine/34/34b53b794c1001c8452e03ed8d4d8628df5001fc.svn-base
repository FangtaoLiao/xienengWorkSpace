package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollJsonDevSet;

@Repository
public interface CollJsonDevSetMapper {
	public int insertCollJsonDevSet(CollJsonDevSet collJsonDevSet);
	public List<CollJsonDevSet> getJsonDevSetByComId(Map map);
	public int updateDevices(List list);
	/** 
	  * @Title:  updateSlaveId 
	  * @Description:  修改设备通讯地址 
	  * @param deviceId 设备id
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月14日上午11:36:04
	*/ 
	public int updateSlaveId(Map map);
	public CollJsonDevSet getJsonDevSetById(Integer id);
	public CollJsonDevSet getUnique(Map map);
	public int updateSlaveIdAndComId(Map map);
}