package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollSignalLabel;
@Repository
public interface CollSignalLabelMapper {
	/** 
	  * @Title:  getFieldByType 
	  * @Description:  根据设备类型获取需要对比的信号点集合 
	  * @param type
	  * @return: List<CollSignalLabel>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月15日上午10:54:20
	*/ 
	public List<CollSignalLabel> getFieldByType(String type);
	
	/** 
	  * @Title:  getFieldForHistory 
	  * @Description:  根据设备类型获取需要对比的信号点集合 
	  * @param map
	  * @return: List<CollSignalLabel>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月15日上午10:55:12
	*/ 
	public List<CollSignalLabel> getFieldForHistory(Map map);
	/** 
	  * @Title:  getFieldLabelByField 
	  * @Description:  TODO 
	  * @param map
	  * @return: List<CollSignalLabel>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月15日下午3:48:27
	*/ 
	public CollSignalLabel getFieldLabelByField(Map map);

	/** 
	  * @Title:  getImName 
	  * @Description:  TODO 
	  * @param imId
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日上午11:31:10
	*/
	public String getImName(String imId);
	/**
	 * ybj 2018/11/8
	 */
	List<Map<String,String>> getField2Guid(int deviceType);
}