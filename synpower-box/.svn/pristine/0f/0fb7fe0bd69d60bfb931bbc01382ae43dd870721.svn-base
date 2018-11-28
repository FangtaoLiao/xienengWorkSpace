package com.synpower.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
import com.synpower.msg.Header;
import com.synpower.msg.MessageBean;
import com.synpower.service.WXMonitorPlantService;

@RestController
@RequestMapping(value="/monitorwx")
public class WXMonitorPlantController extends ErrorHandler{
	@Autowired
	private WXMonitorPlantService  wxMonitorService;
	/** 
	  * @Title:  singlePowerCurve 
	  * @Description:  小程序实时功率刷新 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月16日上午9:11:21
	*/ 
	@RequestMapping(value="/singlePowerCurve",method=RequestMethod.POST)
	public MessageBean singlePowerCurve(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		Map<String,Object> resultMap=wxMonitorService.getCurrentPowerWX(jsonData);
		MessageBean msg=new MessageBean();
		msg.setBody(resultMap);
		msg.setCode(Header.STATUS_SUCESS);
		return msg;
	}
	@RequestMapping(value="/singlePlant",method=RequestMethod.POST)
	public MessageBean singlePlant(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		return wxMonitorService.singlePlant(jsonData);
	}
	@RequestMapping(value="/singleDynamic",method=RequestMethod.POST)
	public MessageBean singleDynamic(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException {
		return wxMonitorService.singleDynamic(jsonData,getSession());
	}
	@RequestMapping(value="/singleGenCurve",method=RequestMethod.POST)
	public MessageBean singleGenCurve(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, ParseException{
		return wxMonitorService.singleGenCurve(jsonData,getSession());
	}
	@RequestMapping(value="/device",method=RequestMethod.POST)
	public MessageBean device(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, ParseException{
		return wxMonitorService.deviceWX(jsonData,getSession());
	}
	@RequestMapping(value="/deviceWX",method=RequestMethod.POST)
	public MessageBean deviceWX(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, ParseException{
		return wxMonitorService.deviceWX(jsonData,getSession());
	}
	@RequestMapping(value="/deviceDetail",method=RequestMethod.POST)
	public MessageBean deviceDetail(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, ParseException{
		return wxMonitorService.deviceDetail(jsonData,getSession());
	}
	@RequestMapping(value="/updateValueOfGuid",method=RequestMethod.POST)
	public MessageBean updateValueOfGuid(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, ParseException{
		return wxMonitorService.updateValueOfGuid(jsonData,getSession());
	}
}
