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

public interface ExportService {

	/** 
	  * @Title:  runReportExport 
	  * @Description: 导出电站运行报表 
	  * @param request
	  * @param response
	 * @param session 
	  * @return: MessageBean
	 * @throws SessionTimeoutException 
	 * @throws SessionException 
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月21日下午4:30:50
	*/ 
	public void runReportExport(HttpServletRequest request, HttpServletResponse response, Session session)throws ServiceException, ServletException, IOException, SessionException, SessionTimeoutException ;
	/** 
	  * @Title:  runReportsExport 
	  * @Description:  导出电站对比报表  
	  * @param request
	  * @param response
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月22日下午1:36:09
	*/ 
	public void runReportsExport(HttpServletRequest request, HttpServletResponse response,Session session)throws ServiceException, ServletException, IOException, SessionException, SessionTimeoutException ;
	/** 
	  * @Title:  eletriRunReportExport 
	  * @Description:  TODO 
	  * @param request
	  * @param response: void
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月29日下午5:51:52
	*/
	public void eletriRunReportExport(HttpServletRequest request, HttpServletResponse response)throws ServiceException, ServletException, IOException ;

}
