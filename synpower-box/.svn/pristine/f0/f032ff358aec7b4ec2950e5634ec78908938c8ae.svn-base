package com.synpower.service;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.bridge.Message;

import com.synpower.bean.SysUser;
import com.synpower.lang.NotRegisterException;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface UserService {
	/** 
	  * @Title:  getSimilar 
	  * @Description:  经销商查询 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:17:51
	*/
	public MessageBean getSimilar(String str,MessageBean msg) throws ServiceException;
	/** 
	  * @Title:  insertUserInfo 
	  * @Description:  新用户创建 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:18:58
	*/ 
	public MessageBean insertUserInfo(String str,Session session,long sessionId,HttpServletRequest request)throws Exception ;
	/** 
	  * @Title:  getUserByLoginId 
	  * @Description:  查询登录账号是否存在 
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:20:33
	*/ 
	public MessageBean getUserByLoginId(String str) throws SessionException, ServiceException, ServiceException;
	/** 
	  * @Title:  getUserByTel 
	  * @Description:  查询电话号码是否存在  
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月26日下午5:21:16
	*/
	public MessageBean getUserByTel(String str)throws SessionException, ServiceException, ServiceException;
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
	public MessageBean listUserInfo(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
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
	public MessageBean updateUserStatus(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException ;
	/** 
	  * @Title:  updateUserValid 
	  * @Description:  账户列表的删除 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:13:31
	*/
	public MessageBean updateUserValid(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	public MessageBean getRoleByOrgId(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	public MessageBean getRoleAll(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	public MessageBean getUserInfoById(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	public MessageBean updateUserAndRole(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
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
	public MessageBean getUserInfo(String str, Session session)throws SessionException, SessionTimeoutException, ServiceException;
	/** 
	  * @Title:  updateUserInfo 
	  * @Description:  我的信息模块的修改
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月30日下午5:16:07
	*/
	public MessageBean updateUserInfo(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	/** 
	  * @Title:  getUserNotMyself 
	  * @Description:  我的信息模块电话唯一性效验 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年10月31日下午1:46:25
	*/ 
	public MessageBean getUserNotMyself(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean getUnique(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean insertPlantUser(String str,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	public MessageBean queryUserRolSrc(String jsonData,Session session)throws  ServiceException, SessionException, SessionTimeoutException ;
	public MessageBean updateUserIcon(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean insertNewUser(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean uploadUserIcon(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean selectUserInfo(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean updateUserById(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean updateUserPwd(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean updateDirectPwd(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean insertUserFormId(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean getPushInfo(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException, ParseException ;
	/** 
	  * @Title:  getValidCode 
	  * @Description:  发验证码 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月18日下午2:31:20
	*/ 
	public MessageBean getValidCode(String str)throws SessionException, ServiceException, SmsException, IOException;
	public MessageBean updateUserTel(String str,Session session) throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean updateUserName(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	/** 
	  * @Title:  updateOwner 
	  * @Description:  更改业主信息 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月9日下午5:22:51
	*/ 
	public MessageBean updateOwner(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  savePlantOwner 
	  * @Description:  添加业主
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月10日上午10:19:12
	*/ 
	public MessageBean savePlantOwner(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  deleteOwner 
	  * @Description:  删除业主账户 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日上午11:16:35
	*/ 
	public MessageBean deleteOwner(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  wxgetUserByTel 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月18日上午10:53:32
	*/ 
	public MessageBean wxgetUserByTel(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;
	
	/** 
	  * @Title:  saveSharedLink 
	  * @Description:  创建分享链接信息 
	  * @param str
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年4月16日下午2:20:47
	*/ 
	public MessageBean saveSharedLink(String jsonData, Session session, long linkCode)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  quicklyRegister 
	  * @Description:  快速注册通道 
	  * @param jsonData
	  * @param session
	  * @param generateId
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年4月16日下午2:44:50
	*/ 
	public MessageBean quicklyRegister(String jsonData,Session session,long sessionId,HttpServletRequest request)
			throws Exception;
	public MessageBean getInfoForLink(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException, ParseException;
	
	public MessageBean checkLinkCode(String jsonData) throws ServiceException, IOException, WeiXinExption;
	public MessageBean checkRegisterUser(String jsonData) throws Exception ;
	
}
