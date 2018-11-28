package com.synpower.web;

import java.text.ParseException;

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
import com.synpower.service.UserService;

@RestController
@RequestMapping(value="/userwx")
public class WXUserController extends ErrorHandler {
	@Autowired
	private UserService userService;
	@RequestMapping(value="/updateOwner",method=RequestMethod.POST)
	public MessageBean getInventerNum(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= userService.updateOwner(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/savePlantOwner",method=RequestMethod.POST)
	public MessageBean savePlantOwner(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= userService.savePlantOwner(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/deleteOwner",method=RequestMethod.POST)
	public MessageBean deleteOwner(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= userService.deleteOwner(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getUserByTel",method=RequestMethod.POST)
	public MessageBean getUserByTel(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= userService.wxgetUserByTel(jsonData);
		return msg;
	}
	/** 
	  * @Title:  updateUserTel 
	  * @Description:  微信修改用户手机号码 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月3日下午2:20:14
	*/ 
	@RequestMapping(value="/updateUserTel",method=RequestMethod.POST)
	public MessageBean updateUserTel(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserTel(str,getSession());
	}
	/** 
	  * @Title:  updateUserName 
	  * @Description:  微信修改用户姓名 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月3日下午2:56:12
	*/ 
	@RequestMapping(value="/updateUserName",method=RequestMethod.POST)
	public MessageBean updateUserName(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.updateUserName(str,getSession());
	}
	@RequestMapping(value="/saveSharedLink",method=RequestMethod.POST)
	public MessageBean saveSharedLink(@RequestBody String jsonData)throws SessionException,SessionTimeoutException,ServiceException{
		return userService.saveSharedLink(jsonData,getSession(),generateId());
	}
	@RequestMapping(value="/getInfoForLink",method=RequestMethod.POST)
	public MessageBean getInfoForLink(@RequestBody String jsonData)throws SessionException,SessionTimeoutException,ServiceException, ParseException{
		return userService.getInfoForLink(jsonData,getSession());
	}
}
