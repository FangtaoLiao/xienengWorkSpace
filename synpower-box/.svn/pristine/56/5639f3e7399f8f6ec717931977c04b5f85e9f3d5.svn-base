package com.synpower.service;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface AlarmService {

	/** 
	  * @Title:  alarmList 
	  * @Description:  告警列表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月11日上午11:34:41
	*/ 
	public MessageBean alarmList(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  saveAlarmAttention 
	  * @Description:  告警关注 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月11日上午11:34:51
	*/ 
	public MessageBean alarmAttention(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  delAllAlarmAttention 
	  * @Description:  清空关注告警
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日下午4:54:07
	*/ 
	public MessageBean delAllAlarmAttention(String jsonData, Session session)  throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean alarmLine(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getalarmLight(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean setalarmLight(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getAlertorOrder(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getConfirmAlarm(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getAlarmData(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;
}
