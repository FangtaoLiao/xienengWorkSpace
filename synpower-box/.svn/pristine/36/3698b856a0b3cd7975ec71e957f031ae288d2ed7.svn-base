package com.synpower.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

import jxl.read.biff.BiffException;

public interface EquipmentService {
	
	public MessageBean getEquipmentInfo(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean listEquipmentInfo(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateStatus(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getEquipmentBasic(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateEquipmentBasic(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateEquipmentPhoto(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	
	public void inverterExport(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;
	
	public void interverExcelTemplate(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;
	
	public MessageBean readInterverExcel(HttpServletRequest request,Session session) throws ServiceException, SessionException, SessionTimeoutException,BiffException, IOException;
	
	public MessageBean insertInterver(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean listEquipmentSub(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean updateSubStatus(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getSubBasic(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean updateSubBasic(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean updateSubPhoto(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;

	public void subExport(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;

	public void subExcelTemplate(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;

	public MessageBean readSubExcel(HttpServletRequest request,Session session) throws ServiceException, SessionException, SessionTimeoutException,BiffException, IOException;

	public MessageBean insertSub(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean listCamera(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateCameraStatus(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateCambasic(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean updateCameraPhoto(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;

	public void cameraExport(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;

	public void cameraExcelTemplate(HttpServletRequest request,HttpServletResponse response,Session session)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException ;

	public MessageBean readCameraExcel(HttpServletRequest request,Session session) throws ServiceException, SessionException, SessionTimeoutException,BiffException, IOException;

	public MessageBean insertCamera(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
}
