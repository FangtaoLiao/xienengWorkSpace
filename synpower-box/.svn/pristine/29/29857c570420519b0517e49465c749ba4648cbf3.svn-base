package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface SysPersonalSetMapper {
	/** 
	  * @Title:  getPlantForUser 
	  * @Description:  查询用户可见的电站 
	  * @param uId
	  * @return: List<String>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月6日下午2:35:53
	*/ 
	public List<String> getPlantForUser(String uId);	
	
	/** 
	  * @Title:  deleteUserRecord 
	  * @Description:  删除用户的报表个性化设置
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午2:52:20
	*/ 
	public int deleteUserRecord(Map map);
	/** 
	  * @Title:  deleteUserRecord 
	  * @Description:  删除用户的报表个性化设置
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午2:52:20
	*/ 
	public int deleteUserOneRecord(Map map);
	
	/** 
	  * @Title:  saveUserRecord 
	  * @Description:  保存用户的报表个性化设置 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午2:52:52
	*/ 
	public int saveUserRecord(Map map);
	
	/** 
	  * @Title:  getObjectsByBelong 
	  * @Description:  获取所属者的对象ID集合 
	  * @param map
	  * @return: List<String>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午3:21:09
	*/                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
	public int insertUserRecord(Map map);
	public List<String>getObjectsByBelong(Map map);
	
	public List<String>getObjectsByBelongWX(Map map);
	/** 
	  * @Title:  deleteUserRecord 
	  * @Description:  删除用户的报表个性化设置
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午2:52:20
	*/ 
	public int deleteHistoryReportSet(Map map);
}