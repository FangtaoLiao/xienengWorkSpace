package com.synpower.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
@RequestMapping(value="/device")
public class DeviceController extends ErrorHandler{
	@Autowired
	private DeviceService deviceService;
	@RequestMapping(value="/getDeviceListByPid",method=RequestMethod.POST)
	public MessageBean deviceService(@RequestBody Map<String, Object> map,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.deviceService(map,getSession());
		return msg;
	}
	@RequestMapping(value="/getCollectors",method=RequestMethod.POST)
	public MessageBean getCollectors(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getCollectors(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/deleteCollectors",method=RequestMethod.POST)
	public MessageBean deleteCollectors(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.deleteCollectors(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateCollector",method=RequestMethod.POST)
	public MessageBean updateCollector(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateCollector(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateCollAndSubDev",method=RequestMethod.POST)
	public MessageBean updateCollAndSubDev(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateCollAndSubDev(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updatePointTable",method=RequestMethod.POST)
	public MessageBean updatePointTable(@RequestBody String jsonData,HttpServletRequest request)throws SessionTimeoutException, SessionException, ServiceException{
		return deviceService.updatePointTable(jsonData,getSession());
	}
	@RequestMapping(value="/getDevicesUnderColl",method=RequestMethod.POST)
	public MessageBean getDevicesUnderColl(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getDevicesUnderColl(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/get2ConnDevices",method=RequestMethod.POST)
	public MessageBean get2ConnDevices(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.get2ConnDevices(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/addCollector",method=RequestMethod.POST)
	public MessageBean addCollector(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.addCollector(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getDeviceChoiceTree",method=RequestMethod.POST)
	public MessageBean getDeviceChoiceTree(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getDeviceChoiceTree(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getInverterList",method=RequestMethod.POST)
	public MessageBean getInverterList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getInverterList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getInverterModelforChoice",method=RequestMethod.POST)
	public MessageBean getInverterModelforChoice(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getInverterModelforChoice(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getModuleInfo",method=RequestMethod.POST)
	public MessageBean getModuleInfo(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getModuleInfo(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getModuleInfoforChoice",method=RequestMethod.POST)
	public MessageBean getModuleInfoforChoice(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getModuleInfoforChoice(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateInEtModue",method=RequestMethod.POST)
	public MessageBean updateInEtModue(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateInEtModue(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getYCList",method=RequestMethod.POST)
	public MessageBean getYCList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getYCList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getYXList",method=RequestMethod.POST)
	public MessageBean getYXList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getYXList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getYKList",method=RequestMethod.POST)
	public MessageBean getYKList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getYKList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getAllCollNames",method=RequestMethod.POST)
	public MessageBean getAllCollNames(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getAllCollNames(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/addCamera",method=RequestMethod.POST)
	public MessageBean addCamera(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.addCamera(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getCameraList",method=RequestMethod.POST)
	public MessageBean getCameraList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getCameraList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getCaModelList",method=RequestMethod.POST)
	public MessageBean getCaModelList(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getCaModelList(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/delCamera",method=RequestMethod.POST)
	public MessageBean delCamera(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.delCamera(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/updateCamera",method=RequestMethod.POST)
	public MessageBean updateCamera(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.updateCamera(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getCameraListYS",method=RequestMethod.POST)
	public MessageBean getCameraListYS(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getCameraListYS(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getLiveHLS",method=RequestMethod.POST)
	public MessageBean getLiveHLS(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getLiveHLS(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/plantIdofOrg4Device",method=RequestMethod.POST)
	public MessageBean plantIdofOrg4Device(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.plantIdofOrg4Device(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/plantIdofOrg4Camera",method=RequestMethod.POST)
	public MessageBean plantIdofOrg4Camera(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.plantIdofOrg4Camera(jsonData,getSession());
		return msg;
	}
	@RequestMapping(value="/getVedioByTime",method=RequestMethod.POST)
	public MessageBean getVedioByTime(@RequestBody String jsonData,HttpServletRequest request)throws  SessionTimeoutException, SessionException, ServiceException, ServiceException {
		MessageBean msg= deviceService.getVedioByTime(jsonData,getSession());
		return msg;
	}
}
