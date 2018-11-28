package com.synpower.service;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface WXAlarmService {

	/** 
	  * @Title:  getAlarm 
	  * @Description:  获取关注告警 列表
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午4:44:04
	*/ 
	public MessageBean getAlarm(String jsonData, Session session)throws  ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  followEvent 
	  * @Description:  关注告警 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午6:31:42
	*/ 
	public MessageBean followEvent(String jsonData, Session session)throws  ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  detail 
	  * @Description:  故障详情 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月18日上午11:33:40
	*/ 
	public MessageBean detail(String jsonData, Session session)throws  ServiceException, SessionException, SessionTimeoutException;
	
}
