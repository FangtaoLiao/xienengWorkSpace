package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.SysScreen;

@Repository
public interface SysScreenMapper {
	/** 
	  * @Title:  getScreenByOrgId 
	  * @Description:  根据组织id获取屏幕信息 
	  * @param orgId
	  * @return: SysScreen
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月30日上午9:57:27
	*/ 
	public SysScreen getScreenByOrgId(String orgId);
	public int updateLoginPhotoNull(String orgId);
	public int updateQrCodeNull(String orgId);
	public int updateQrCode(Map map);
	/** 
	  * @Title:  updateAutoCal 
	  * @Description:  修改手动输入的容量值 
	  * @param map
	  * @return: int
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午5:36:14
	*/ 
	public int updateAutoCal(Map map);
	public int insertScreenByOrgId(String orgId);
	public int deleteScreenByOrgId(List list);
}
