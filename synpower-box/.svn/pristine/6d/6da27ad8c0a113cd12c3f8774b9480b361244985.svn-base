package com.synpower.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface ScreenService {
	/** 
	  * @Title:  getPlantCount 
	  * @Description:  电站规模 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日上午9:50:38
	*/
	public MessageBean getPlantCount(String str,Session session) throws ServiceException , SessionException, SessionTimeoutException;
	/** 
	  * @Title:  getOrgIntroduction 
	  * @Description:  公司简介 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日下午3:14:40
	*/ 
	public MessageBean getOrgIntroduction(String str,Session session)throws ServiceException , SessionException, SessionTimeoutException;
	/** 
	  * @Title:  listPlantDistribution 
	  * @Description:  电站分布 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月8日下午3:47:30
	*/ 
	public Map<String, Double> getPowerTotal(List<Integer> list);
	public Map<String, Object> getCommonPart(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean listPlantDistribution(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getBasicStatics(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getBusiness(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getPlantDistribution(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getPowerBar(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getContribution(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException, IOException;
	/** 
	  * @Title:  dynamicStatics 
	  * @Description: 弹窗动态信息
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月16日上午11:09:21
	*/ 
	public MessageBean dynamicStatics(String jsonData, Session session)throws SessionException, SessionTimeoutException,ServiceException ;
	/** 
	  * @Title:  plantInfo 
	  * @Description:  弹窗静态信息 
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月17日下午4:43:17
	*/ 
	public MessageBean plantInfo(String jsonData)throws ServiceException;
	
	public MessageBean getAllPlant(Session session, String jsonData, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean districtManage(Session session, String jsonData, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  getCurrentPower 
	  * @Description:  单电站抛物线和实时功率曲线 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: Map<String,Object>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月23日上午10:58:58
	*/ 
	public Map<String, Object> getCurrentPower(String jsonData) throws ServiceException;
	public MessageBean getLogoAndName(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
}
