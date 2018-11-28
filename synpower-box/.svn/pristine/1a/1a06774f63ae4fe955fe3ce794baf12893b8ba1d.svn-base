package com.synpower.service;

import javax.servlet.http.HttpServletRequest;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface SystemService {

	public MessageBean getSystemInfo(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateSystemInfo(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;

	public MessageBean getSystemBasic(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateSystemBasic(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;

	public MessageBean updateSystemLogo(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateScreenLogo(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateLoginPhoto(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;

	public MessageBean updateLoginLogo(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateQrCode(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;

	public MessageBean getSystemContent(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean getAutomaticCalculation(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean getSysInfo(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;

	public MessageBean updatePropagandaPhoto(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean getPropagandaPhoto(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean updateOrgPhoto(HttpServletRequest request,Session session) throws SessionException,SessionTimeoutException,ServiceException;
}
