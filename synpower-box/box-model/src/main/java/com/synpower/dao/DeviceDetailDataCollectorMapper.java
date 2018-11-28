/**
 * 
 */
package com.synpower.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DeviceDetailDataCollector;

@Repository
public interface DeviceDetailDataCollectorMapper {

	/** 
	  * @Title:  getModel 
	  * @Description:  TODO 
	  * @param deviceModelId
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月16日上午11:43:01
	*/
	public String getModel(Integer deviceModelId);

	/** 
	  * @Title:  getCollModelNames 
	  * @Description:  TODO 
	  * @return: List<DeviceDetailDataCollector>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月19日下午1:54:08
	*/
	public List<DeviceDetailDataCollector> getCollModelNames();

}
