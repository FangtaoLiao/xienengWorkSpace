package com.synpower.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.NotRegisterException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.MessageBean;
import com.synpower.service.EquipmentService;
import com.synpower.service.ExportService;
import com.synpower.service.MonitorPlantService;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: ExportController
 * @Description: 导入导出
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月21日下午4:21:39   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@RestController
@RequestMapping(value="/exports")
public class ExportController extends ErrorHandler{
	@Autowired
	private ExportService exportService;
	@Autowired
	private EquipmentService equipmentService;
	@RequestMapping(value="/runReportExport")
	public void runReportExport(HttpServletRequest request,HttpServletResponse response) throws SessionException, IOException, ServiceException, NotRegisterException, WeiXinExption, ServletException,SessionTimeoutException  {
		exportService.runReportExport(request,response,getSession());
	}
	@RequestMapping(value="/eletriRunReportExport")
	public void eletriRunReportExport(HttpServletRequest request,HttpServletResponse response) throws SessionException, IOException, ServiceException, NotRegisterException, WeiXinExption, ServletException  {
		exportService.eletriRunReportExport(request,response);
	}
	@RequestMapping(value="/runReportsExport")
	public void runReportsExport(HttpServletRequest request,HttpServletResponse response) throws SessionException, IOException, ServiceException, NotRegisterException, WeiXinExption, ServletException, SessionTimeoutException  {
		exportService.runReportsExport(request,response,getSession());
	}
	/** 
	  * @Title:  inverterExport 
	  * @Description:  逆变器列表的导出 
	  * @param request
	  * @param response
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:17:08
	*/ 
	@RequestMapping(value="/inverterExport")
	public void inverterExport(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.inverterExport(request,response,getSession());
	}
	/** 
	  * @Title:  interverExcelTemplate 
	  * @Description:  下载逆变器模板 
	  * @param request
	  * @param response
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:17:25
	*/ 
	@RequestMapping(value="/interverExcelTemplate")
	public void interverExcelTemplate(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.interverExcelTemplate(request,response,getSession());
	}
	/** 
	  * @Title:  subExport 
	  * @Description:  组件列表的导出 
	  * @param request
	  * @param response
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:22:53
	*/ 
	@RequestMapping(value="/subExport")
	public void subExport(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.subExport(request,response,getSession());
	}
	/** 
	  * @Title:  subExcelTemplate 
	  * @Description:  下载组件模板 
	  * @param request
	  * @param response
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:23:18
	*/ 
	@RequestMapping(value="/subExcelTemplate")
	public void subExcelTemplate(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.subExcelTemplate(request,response,getSession());
	}
	/** 
	  * @Title:  inverterExport 
	  * @Description:  摄像头列表的导出 
	  * @param request
	  * @param response
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws ServletException
	  * @throws IOException: void
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月26日下午4:32:18
	*/ 
	@RequestMapping(value="/cameraExport")
	public void cameraExport(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.cameraExport(request,response,getSession());
	}
	@RequestMapping(value="/cameraExcelTemplate")
	public void cameraExcelTemplate(HttpServletRequest request,HttpServletResponse response)throws SessionException,SessionTimeoutException,ServiceException, ServletException, IOException {
		equipmentService.cameraExcelTemplate(request,response,getSession());
	}
}
