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
@RequestMapping(value="/org")
public class OrgController extends ErrorHandler{
	@Autowired
	private OrgService service;
	@RequestMapping(value="/getAllOrgByUid", method = RequestMethod.POST)
	public MessageBean getAllOrgByUid(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		return service.getAllOrgByUid(jsonData,getSession());
	}
	
	/**
	 * 
	  * @Title:  getOrgInfo
	  * @Description:  根据用户查询组织信息 
	  * @param jsonData
	  * @return messageBean
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/getOrgInfo",method=RequestMethod.POST)
	public MessageBean getOrgInfo(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		MessageBean messageBean = service.getOrgInfo(jsonData,getSession());
		//System.out.println(messageBean);
		return messageBean;
		
	}
	
	/**
	 * 
	  * @Title:  addOrgInfoByUser 
	  * @Description:  根据增加用户组织信息 
	  * @param jsonData
	  * @return messageBean
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/addOrgInfoByUser",method=RequestMethod.POST)
	public MessageBean addOrgInfoByUser(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		MessageBean messageBean = service.addOrgInfo(jsonData,getSession());
		return messageBean;
		
	}
	
	/**
	 * 
	  * @Title:  updateOrgInfoByUser 
	  * @Description:  根据用户修改组织信息 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/updateOrgInfoByUser")
	public MessageBean updateOrgInfoByUser(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		MessageBean messageBean = service.updateOrgInfo(jsonData,getSession());
		return messageBean;
		
	}
	
	/**
	 * 
	  * @Title:  deleteOrgInfoByUser 
	  * @Description:  根据用户删除组织信息 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/deleteOrgInfoByUser")
	public MessageBean deleteOrgInfoByUser(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		MessageBean messageBean = service.deleteOrgInfo(jsonData,getSession());
		return messageBean;
		
	}
	
	/**
	 * 
	  * @Title:  getOrgAgentor 
	  * @Description:  根据用户模糊查询组织代表 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/getOrgAgentor")
	public MessageBean getOrgAgentor(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException{
		MessageBean messageBean = service.getOrgAgentor(jsonData,getSession());
		return messageBean;		
	}
	
	/**
	 * 
	  * @Title:  getOrgList 
	  * @Description:  获取组织信息列表 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	 */
	@RequestMapping(value="/getOrgList")
	public MessageBean getOrgList(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		MessageBean messageBean = service.getOrgList(jsonData,getSession(), request);
		return messageBean;	 
	}
	
	/**
	 * 
	  * @Title:  getRoleList 
	  * @Description:  获取角色列表 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月2日上午11:09:23
	 */
	@RequestMapping(value="/getRoleList")
	public MessageBean getRoleList(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		MessageBean messageBean = service.getRoleList(jsonData,getSession(), request);
		return messageBean;	 
	}
	
	/**
	 * 
	  * @Title:  showRoleAndRights 
	  * @Description:  显示当前组织角色及权限 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月10日下午5:36:16
	 */
	@RequestMapping(value="/showRoleAndRights")
	public MessageBean showRoleAndRights(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		MessageBean messageBean = service.showRoleAndRights(jsonData,getSession(), request);
		return messageBean;	 
	}
	/** 
	  * @Title:  getOrgCode 
	  * @Description:  生成组织机构代码，并且不重复 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月8日下午2:16:01
	*/ 
//	@RequestMapping(value="/getOrgCode")
//	public MessageBean getOrgCode(@RequestBody String str)throws SessionException, SessionTimeoutException,ServiceException{
//		return service.getOrgCode(str,getSession());
//	}
	@RequestMapping(value="/selectOrgIsNull")
	public MessageBean selectOrgIsNull(@RequestBody String str)throws SessionException, SessionTimeoutException,ServiceException{
		return service.selectOrgIsNull(str,getSession());
	}
	/** 
	  * @Title:  getOrgTree 
	  * @Description:  组织设置的组织树 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月2日下午2:45:06
	*/ 
	@RequestMapping(value="/getOrgTree", method = RequestMethod.POST)
	public MessageBean getOrgTree(@RequestBody String str)throws SessionException, SessionTimeoutException,ServiceException{
		return service.getOrgTree(str,getSession());
	}
}
