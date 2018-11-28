package com.synpower.web;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synpower.msg.MessageBean;
import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.service.UserService;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: UserController
 * @Description: 用户模块
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年10月25日上午10:33:26   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@RestController
@RequestMapping(value="/user")
public class UserController extends ErrorHandler{
	@Autowired
	private UserService userService;
	/** 
	  * @Title:  getSimilar 
	  * @Description:  经销商查询 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:17:51
	*/ 
	@RequestMapping(value="/getSimilar",method=RequestMethod.POST)
	public MessageBean getSimilar( @RequestBody String str)throws ServiceException{
		MessageBean msg = new MessageBean();
		msg = userService.getSimilar(str,msg);
		msg.setCode("100");
		msg.setMsg("经销商查询成功");
		List<String> list = (List<String>) msg.getBody();
		return msg;
	}
	/** 
	  * @Title:  insertUserInfo 
	  * @Description:  新用户创建 
	  * @param str
	  * @return: MessageBean
	 * @throws Exception 
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:18:58
	*/ 
	@RequestMapping(value="/insertUserInfo",method=RequestMethod.POST)
	public MessageBean insertUserInfo(@RequestBody String str,HttpServletRequest request) throws Exception{
		return userService.insertUserInfo(str, getSession(),generateId(),request);
	}
	/** 
	  * @Title:  getUserByLoginId 
	  * @Description:  查询注册账号是否存在 
	  * @param str
	  * @return: MessageBean
	 * @throws ServiceException 
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:20:33
	*/ 
	@RequestMapping(value="/getUserByLoginId",method=RequestMethod.POST)
	public MessageBean getUserByLoginId(@RequestBody String str) throws SessionException, ServiceException {
		return userService.getUserByLoginId(str);
	}
	/** 
	  * @Title:  getUserByTel 
	  * @Description:  查询电话号码是否存在  
	  * @param str
	  * @return: MessageBean
	  * @throws ServiceException 
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:21:16
	*/ 
	@RequestMapping(value="/getUserByTel",method=RequestMethod.POST)
	public MessageBean getUserByTel(@RequestBody String str) throws SessionException, ServiceException{
		return userService.getUserByTel(str);
	}
	/** 
	  * @Title:  listUserInfo 
	  * @Description:  获取账户列表 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月27日下午6:19:15
	*/ 
	@RequestMapping(value="/listUserInfo",method=RequestMethod.POST)
	public MessageBean listUserInfo(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.listUserInfo(str,getSession());
	}
	/** 
	  * @Title:  updateUserStatus 
	  * @Description:  启用/停用 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月27日下午6:20:51
	*/ 
	@RequestMapping(value="/updateUserStatus",method=RequestMethod.POST)
	public MessageBean updateUserStatus(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.updateUserStatus(str,getSession());
	}
	/** 
	  * @Title:  updateUserValid 
	  * @Description:  账户列表的删除 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	 * @throws ServiceException 
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:13:31
	*/ 
	@RequestMapping(value="/updateUserValid",method=RequestMethod.POST)
	public MessageBean updateUserValid(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.updateUserValid(str,getSession());
	}
	@RequestMapping(value="/getRoleByOrgId",method=RequestMethod.POST)
	public MessageBean getRoleByOrgId(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.getRoleByOrgId(str,getSession());
	}
	@RequestMapping(value="/getRoleAll",method=RequestMethod.POST)
	public MessageBean getRoleAll(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.getRoleAll(str,getSession());
	}
	@RequestMapping(value="/getUserInfoById",method=RequestMethod.POST)
	public MessageBean getUserInfoById(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.getUserInfoById(str,getSession());
	}
	@RequestMapping(value="/updateUserAndRole",method=RequestMethod.POST)
	public MessageBean updateUserAndRole(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserAndRole(str,getSession());
	}
	/** 
	  * @Title:  getUseInfo 
	  * @Description:  我的信息模块的基本信息展示 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:15:04
	*/ 
	@RequestMapping(value="/getUserInfo",method=RequestMethod.POST)
	public MessageBean getUseInfo(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.getUserInfo(str,getSession());
	}
	/** 
	  * @Title:  updateUserInfo 
	  * @Description:  我的信息模块的修改
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	 * @throws ServiceException 
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:16:07
	*/ 
	@RequestMapping(value="/updateUserInfo",method=RequestMethod.POST)
	public MessageBean updateUserInfo(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.updateUserInfo(str,getSession());
	}
	/** 
	  * @Title:  getUserNotMyself 
	  * @Description:  我的信息模块电话、账号、邮箱唯一性效验 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月31日下午1:46:25
	*/ 
	@RequestMapping(value="/getUserNotMyself",method=RequestMethod.POST)
	public MessageBean getUserNotMyself(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.getUserNotMyself(str,getSession());
	}
	@RequestMapping(value="getUnique",method=RequestMethod.POST)
	public MessageBean getUnique(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.getUnique(str,getSession());
	}
	@RequestMapping(value="/insertPlantUser",method=RequestMethod.POST)
	public MessageBean insertPlantUser(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return userService.insertPlantUser(str,getSession());
	}
	@RequestMapping(value="/queryUserRolSrc",method=RequestMethod.POST)
	public MessageBean queryUserRolSrc(@RequestBody String jsonData)throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg= userService.queryUserRolSrc(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateUserIcon",method = RequestMethod.POST)
	public MessageBean updateUserIcon(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserIcon(request,getSession());
	}
	@RequestMapping(value="/insertNewUser",method=RequestMethod.POST)
	public MessageBean insertNewUser(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.insertNewUser(str,getSession());
	}
	@RequestMapping(value="/uploadUserIcon",method=RequestMethod.POST)
	public MessageBean uploadUserIcon(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.uploadUserIcon(request,getSession());
	}
	@RequestMapping(value="/selectUserInfo",method=RequestMethod.POST)
	public MessageBean selectUserInfo(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.selectUserInfo(str,getSession());
	}
	@RequestMapping(value="/udpateUserById",method=RequestMethod.POST)
	public MessageBean udpateUserById(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserById(str,getSession());
	}
	/** 
	  * @Title:  updateUserPwd 
	  * @Description:  修改密码 
	  * @param str
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月5日上午11:22:32
	*/ 
	@RequestMapping(value="/updateUserPwd",method=RequestMethod.POST)
	public MessageBean updateUserPwd(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserPwd(str,getSession());
	}
	@RequestMapping(value="/updateDirectPwd",method=RequestMethod.POST)
	public MessageBean updateDirectPwd(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateDirectPwd(str,getSession());
	}
	@RequestMapping(value="/insertUserFormId",method=RequestMethod.POST)
	public MessageBean insertUserFormId(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.insertUserFormId(str,getSession());
	}
	@RequestMapping(value="/getPushInfo",method=RequestMethod.POST)
	public MessageBean getPushInfo(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException, ParseException {
		return userService.getPushInfo(str,getSession());
	}
}
