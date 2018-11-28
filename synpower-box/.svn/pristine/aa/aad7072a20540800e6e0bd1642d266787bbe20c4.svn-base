package com.synpower.service;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface RoleService {
	public MessageBean updateRightsByRid(Session session,String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean saveRoleAndRights(Session session, String jsonData, HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getAllRights(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean updateRoleValid(String str, Session session)throws ServiceException, SessionException, SessionTimeoutException;
}
