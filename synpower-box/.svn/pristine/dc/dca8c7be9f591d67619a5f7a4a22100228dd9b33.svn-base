package com.synpower.web;

import java.io.IOException;
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
import com.synpower.service.WXmonitorService;

@RestController
@RequestMapping(value="/monitorwx")
public class WXMonitorController extends ErrorHandler{
	@Autowired
	private WXmonitorService wxmonitorService;
	@RequestMapping(value="/genCurve",method=RequestMethod.POST)
	public MessageBean genCurve(@RequestBody String str) throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return wxmonitorService.genCurve(str,getSession());
	}
	@RequestMapping(value="/getList",method=RequestMethod.POST)
	public MessageBean getList(@RequestBody String str,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.getWXPlantList(str,getSession());
	}
	@RequestMapping(value="/searchPlantByKeyword",method=RequestMethod.POST)
	public MessageBean searchPlantByKeyword(@RequestBody String str,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.searchPlantByKeyword(str,getSession());
	}
	@RequestMapping(value="/static",method=RequestMethod.POST)
	public MessageBean getStaticInfo(@RequestBody String str,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException, IOException{
		return wxmonitorService.getStaticInfo(str,getSession(),request);
	}
	@RequestMapping(value="/dynamic",method=RequestMethod.POST)
	public MessageBean getDynamic(@RequestBody String str,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException, IOException{
		return wxmonitorService.getDynamic(str,getSession(),request);
	}
	@RequestMapping(value="/plantMap",method=RequestMethod.POST)
	public MessageBean getPlantMap(@RequestBody String str) throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.getPlantMap(str,getSession());
	}
	@RequestMapping(value="/getMapPlant",method=RequestMethod.POST)
	public MessageBean getMapPlant(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.getMapPlant(str,getSession());
	}
	@RequestMapping(value="/powerCurve",method=RequestMethod.POST)
	public MessageBean powerCurve(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.powerCurve(str,getSession());
	}
	@RequestMapping(value="/getUserInfo",method=RequestMethod.POST)
	public MessageBean getUserInfo(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return wxmonitorService.getUserInfo(str,getSession());
	}
}
