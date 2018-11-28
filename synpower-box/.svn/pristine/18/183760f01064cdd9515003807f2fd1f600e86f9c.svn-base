package com.synpower.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.OrgService;
import com.synpower.service.RoleService;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: OrgController
 * @Description: 组织机构
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年10月25日上午10:33:03   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@RestController
@RequestMapping(value="/role")
public class RoleController extends ErrorHandler{
	@Autowired
	private RoleService service;
	@RequestMapping(value="/updateRightsByRid",method=RequestMethod.POST)
	public MessageBean updateRightsByRid(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg= service.updateRightsByRid(getSession(),jsonData,request);
		return msg;
	}
	@RequestMapping(value="/saveRoleAndRights",method=RequestMethod.POST)
	public MessageBean saveRoleAndRights(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		MessageBean msg= service.saveRoleAndRights(getSession(),jsonData,request);
		return msg;
	}
	@RequestMapping(value="/getAllRights",method=RequestMethod.POST)
	public MessageBean getAllRights(@RequestBody String str,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		return service.getAllRights(str,getSession());
	}
	@RequestMapping(value="/updateRoleValid",method=RequestMethod.POST)
	public MessageBean updateRoleValid(@RequestBody String str,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		return service.updateRoleValid(str,getSession());
	}
}
