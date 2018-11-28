package com.synpower.web;

import java.io.IOException;

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
import com.synpower.lang.SmsException;
import com.synpower.msg.MessageBean;
import com.synpower.service.SMSService;

@RestController
@RequestMapping(value="/sms")
public class SMSController extends ErrorHandler{
	@Autowired
	SMSService service;
	@RequestMapping(value="/sendValid",method=RequestMethod.POST)
	public MessageBean sendValid(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, ServiceException, ServiceException, SmsException, IOException, SessionException, SessionTimeoutException  {
		MessageBean msg= service.sendValid(jsonData,getSession());
		return msg;
	}
	/*@RequestMapping(value="/sendDailyReport",method=RequestMethod.POST)
	public MessageBean sendDailyReport(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, ServiceException, ServiceException, SmsException, IOException, SessionException, SessionTimeoutException  {
		MessageBean msg= service.sendDailyReport();
		return msg;
	}*/

	
}
