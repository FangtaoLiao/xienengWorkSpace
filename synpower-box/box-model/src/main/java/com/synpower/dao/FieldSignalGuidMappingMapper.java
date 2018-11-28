package com.synpower.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface FieldSignalGuidMappingMapper {


	/** 
	  * @Title:  getImId 
	  * @Description:  TODO 
	  * @param paraMap
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日上午11:28:08
	*/
	public String getImId(Map<String, Object> paraMap);
	
}