package com.synpower.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.synpower.bean.SysUser;
import com.synpower.lang.NotRegisterException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;

public interface LoginService {

	public MessageBean loginIn(String jsonData,Long sessionId,Session session,HttpServletRequest request)  throws  Exception ;
	public MessageBean loginOut(String jsonData,Session session,HttpServletRequest request)throws SessionException, SessionTimeoutException, ServiceException;
	public User fillIn(SysUser u,Long sessionId,HttpServletRequest request) throws SessionException, SessionTimeoutException , ServiceException ;
	public MessageBean push(Map<String, Object>map,Session session,long sessionId,HttpServletRequest request)throws SessionException, SessionTimeoutException, ServiceException ;
	/** 
	  * @Title:  getAddrLocation 
	  * @Description:  根据地区名字获取经纬度 
	  * @param str
	  * @param session
	  * @param generateId
	  * @param request
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午5:18:50
	*/ 
	public MessageBean getAddrLocation(String jsonData, Session session)throws SessionException, SessionTimeoutException, ServiceException;
	/** 
	  * @Title:  getWxLogin 
	  * @Description:  微信扫码登录 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月14日下午1:35:48
	*/ 
	public MessageBean getWxLogin(String jsonData,Session session,Long sessionId,HttpServletRequest request) throws SessionException, SessionTimeoutException, ServiceException, IOException;

}
