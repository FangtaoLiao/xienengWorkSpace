package com.synpower.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;
import com.synpower.handle.WebSocketPushHandler;
import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.lang.SmsException;
import com.synpower.lang.WeiXinExption;
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.msg.Msg;
import com.synpower.service.LoginService;
import com.synpower.service.SystemService;
import com.synpower.service.UserService;
import com.synpower.util.Util;

/*****************************************************************************
 * @Package: com.synpower.web ClassName: LoginController
 * @Description: 登录模块
 * @version -----------------------------------------------------------------------------
 *          VERSION TIME BY CHANGE/COMMENT
 *          -----------------------------------------------------------------------------
 *          1.0 2017年10月26日上午10:13:01 SP0011 create
 *          -----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved.
 *            注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目
 ******************************************************************************/
@RestController
@RequestMapping(value = "/login")
public class LoginController extends ErrorHandler {
	private Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private WebSocketPushHandler webSocketPushHandler;
	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/loginIn", method = RequestMethod.POST)
	public MessageBean loginIn(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
		MessageBean msg = loginService.loginIn(jsonData, generateId(), getSession(), request);
		return msg;
	}

	@RequestMapping(value = "/loginOut", method = RequestMethod.POST)
	public MessageBean loginOut(@RequestBody String jsonData, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException {
		MessageBean msg = loginService.loginOut(jsonData, getSession(), request);
		return msg;
	}

	@RequestMapping(value = "/scanedCode", method = RequestMethod.POST)
	public MessageBean scanedCode(@RequestBody String jsonData, HttpServletRequest request) throws SessionException,
			SessionTimeoutException, ServiceException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = Util.parseURL(jsonData);
		String userId = String.valueOf(map.get("sn"));
		MessageBean msg = new MessageBean();
		msg.setCode(Header.STATUS_SCAN_CODE);
		msg.setMsg(Msg.STATUS_SCAN_CODE);
		Object jsonObject = JSONObject.toJSON(msg);
		String result = jsonObject.toString();
		webSocketPushHandler.sendMessageToUser(userId, new TextMessage(result));
		msg.setMsg(Msg.SUCCESS_SCAN_CODE);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	/**
	 * 没用了好像
	 */
	@RequestMapping(value = "/push", method = RequestMethod.POST)
	public MessageBean push(@RequestBody String jsonData, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException {
		Map<String, Object> map = Util.parseURL(jsonData);
		MessageBean msg = loginService.push(map, getSession(), generateId(), request);
		String userId = String.valueOf(map.get("sn"));
		Object jsonObject = JSONObject.toJSON(msg);
		String result = jsonObject.toString();
		webSocketPushHandler.sendMessageToUser(userId, new TextMessage(result));
		msg.setMsg(Msg.SUCCESS_SCAN_CODE_LOGIN);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}

	@RequestMapping(value = "/getUserByTel", method = RequestMethod.POST)
	public MessageBean getUserByTel(@RequestBody String str)
			throws SessionException, ServiceException, SessionTimeoutException {
		return userService.wxgetUserByTel(str);
	}

	@RequestMapping(value = "/getValidCode", method = RequestMethod.POST)
	public MessageBean getValidCode(@RequestBody String str, HttpServletRequest request)
			throws SessionException, SessionTimeoutException, ServiceException, SmsException, IOException {
		return userService.getValidCode(str);
	}

	/**
	 * @Title: insertUserInfo
	 * @Description: 新用户创建
	 * @param str
	 * @return: MessageBean
	 * @throws Exception
	 * @lastEditor: SP0009
	 * @lastEdit: 2017年10月26日下午5:18:58
	 */
	@RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST)
	public MessageBean insertUserInfo(@RequestBody String str, HttpServletRequest request) throws Exception {
		return userService.insertUserInfo(str, getSession(), generateId(), request);
	}

	@RequestMapping(value = "/getAddrLocation", method = RequestMethod.POST)
	public MessageBean getAddrLocation(@RequestBody String jsonData, HttpServletRequest request)
			throws SessionException, ServiceException, SmsException, IOException, WeiXinExption,
			SessionTimeoutException {
		return loginService.getAddrLocation(jsonData, getSession());
	}

	@RequestMapping(value = "/getSysInfo", method = RequestMethod.POST)
	public MessageBean getSysInfo(@RequestBody String str)
			throws SessionException, ServiceException, SessionTimeoutException {
		return systemService.getSysInfo(str, getSession());
	}

	/**
	 * 没用了好像
	 */
	@RequestMapping(value = "/getWxLogin", method = RequestMethod.POST)
	public MessageBean getWxLogin(@RequestBody String jsonData, HttpServletRequest request)
			throws SessionException, ServiceException, SessionTimeoutException, IOException {
		return loginService.getWxLogin(jsonData, getSession(), generateId(), request);
	}

	@RequestMapping(value = "/quicklyRegister", method = RequestMethod.POST)
	public MessageBean quicklyRegister(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
		return userService.quicklyRegister(jsonData, getSession(), generateId(), request);
	}

	@RequestMapping(value = "/checkRegisterUser", method = RequestMethod.POST)
	public MessageBean checkRegisterUser(@RequestBody String jsonData) throws Exception {
		return userService.checkRegisterUser(jsonData);
	}

	@RequestMapping(value = "/checkLinkCode", method = RequestMethod.POST)
	public MessageBean checkLinkCode(@RequestBody String jsonData) throws IOException, WeiXinExption, ServiceException {
		return userService.checkLinkCode(jsonData);
	}
}
