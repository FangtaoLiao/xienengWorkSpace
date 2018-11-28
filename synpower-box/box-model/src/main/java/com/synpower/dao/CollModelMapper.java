package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollModel;

@Repository
public interface CollModelMapper {

	/** 
	  * @Title:  getModelName 
	  * @Description:  TODO 
	  * @param dataMode
	  * @return: String
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月16日下午6:15:24
	*/
	public CollModel getModelName(Integer dataMode);

	/** 
	  * @Title:  getModel4Choice 
	  * @Description:  TODO 
	  * @param paraMap
	  * @return: List<String>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日下午3:56:10
	*/
	public List<CollModel> getModel4Choice(Map<String, Object> paraMap);

	/** 
	  * @Title:  getModeByModelId 
	  * @Description:  TODO 
	  * @param pMap
	  * @return: List<CollModel>
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月8日下午5:44:39
	*/
	public List<CollModel> getModeByModelId(Map<String, Object> pMap);
	
	/** 
	  * @Title:  getDateModelId 
	  * @Description:  根据型号和类型获取dataModel 
	  * @param map
	  * @return: String
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月14日下午2:41:34
	*/ 
	public int getDateModelId(Map map);
   
}