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
import com.synpower.service.DeviceService;

@RestController
@RequestMapping(value="/devicewx")
public class WXDeviceController extends ErrorHandler{
	@Autowired
	private DeviceService deviceService;
	@RequestMapping(value="/getInventerNum",method=RequestMethod.POST)
	public MessageBean getInventerNum(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getInventerNum(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateDevice",method=RequestMethod.POST)
	public MessageBean updateDevice(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateDevice(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateSelfDevice",method=RequestMethod.POST)
	public MessageBean updateSelfDevice(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateDeviceNew(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/deleteDevice",method=RequestMethod.POST)
	public MessageBean deleteDevice(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.deleteDevice(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getDevType",method=RequestMethod.POST)
	public MessageBean getDevType(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getDevType(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getModel",method=RequestMethod.POST)
	public MessageBean getModel(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getModel(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updatePointTableWx",method=RequestMethod.POST)
	public MessageBean updatePointTableWx(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updatePointTableWx(jsonData,getSession());
		return msg;
	}
}
