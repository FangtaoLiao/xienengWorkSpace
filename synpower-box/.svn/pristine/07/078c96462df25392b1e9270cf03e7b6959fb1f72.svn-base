package com.synpower.service;

import java.text.ParseException;
import java.util.Map;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface WXMonitorPlantService {
	/** 
	  * @Title:  getCurrentPowerWX 
	  * @Description:  微信单电站实时功率接口 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日上午10:28:49
	*/ 
	public Map<String, Object> getCurrentPowerWX(String jsonData) throws ServiceException;

	/** 
	  * @Title:  singlePlant 
	  * @Description:  单电站实时收益
	  * @param jsonData
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午1:50:11
	*/ 
	public MessageBean singlePlant(String jsonData)throws ServiceException;

	/** 
	  * @Title:  singleDynamic 
	  * @Description:  单电站静态信息
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午1:49:52
	*/ 
	public MessageBean singleDynamic(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException ;
	
	/** 
	  * @Title:  singleGenCurve 
	  * @Description: 发电量柱状图
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午1:49:48
	*/ 
	public MessageBean singleGenCurve(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException,ParseException ;

	/** 
	  * @Title:  device 
	  * @Description:  设备列表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午3:04:03
	*/ 
	public MessageBean device(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  deviceWX 
	  * @Description:  微信获取设备列表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月29日下午6:29:57
	*/ 
	public MessageBean deviceWX(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  deviceDetail 
	  * @Description:  设备详情
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午3:39:16
	*/ 
	public MessageBean deviceDetail(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	/** 
	  * @Title:  updateValueOfGuid 
	  * @Description:  遥控遥调指令下发 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午4:20:39
	*/ 
	public MessageBean updateValueOfGuid(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;

}
