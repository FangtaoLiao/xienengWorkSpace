package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysUserPlant;

@Repository
public interface SysUserPlantMapper {
	/** 
	  * @Title:  getUserPlant 
	  * @Description:  获取业主用户的 电站
	  * @param uId
	  * @return: List<Integer>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月12日下午2:18:15
	*/ 
	public List<Integer>getUserPlant(String uId);
	/** 
	  * @Title:  getPlantUser 
	  * @Description:  根据电站获取业主账户 
	  * @param plantId
	  * @return: List<Integer>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月12日下午2:18:28
	*/ 
	public List<Integer>getPlantUser(String plantId);
	/** 
	  * @Title:  saveUserPlant 
	  * @Description:  TODO 
	  * @param sysUserPlant
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日上午11:11:33
	*/ 
	public int saveUserPlant(SysUserPlant sysUserPlant);
	
	
	/** 
	  * @Title:  delUserPlant 
	  * @Description:  删除业主账户的电站关联 
	  * @param sysUserPlant
	  * @return: int
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日上午11:25:43
	*/ 
	public int delUserPlant(SysUserPlant sysUserPlant);
	public List<SysUserPlant> selectPlantUser(Map map);
	public int delUserAllPlant(int userId);
}