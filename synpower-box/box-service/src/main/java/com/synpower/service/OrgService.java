package com.synpower.service;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface OrgService {
	public MessageBean getAllOrgByUid(String jsonData,Session session)throws SessionException, SessionTimeoutException, ServiceException;

	public MessageBean getOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException;

	public MessageBean addOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException;

	public MessageBean updateOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException;
	
	public MessageBean deleteOrgInfo(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException;
	
	public MessageBean getOrgAgentor(String jsonData, Session session) throws SessionException, SessionTimeoutException, ServiceException;

	public MessageBean getOrgList(String jsonData, Session session, HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException;
	
	public MessageBean getRoleList(String jsonData, Session session, HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException;

	public MessageBean showRoleAndRights(String jsonData, Session session, HttpServletRequest request) throws SessionException, SessionTimeoutException,ServiceException;

//	public MessageBean getOrgCode(String str,Session session)throws SessionException, SessionTimeoutException,ServiceException;
	
	public MessageBean selectOrgIsNull(String str,Session session)throws SessionException, SessionTimeoutException,ServiceException;

	public MessageBean getOrgTree(String str,Session session)throws SessionException, SessionTimeoutException,ServiceException;
}
