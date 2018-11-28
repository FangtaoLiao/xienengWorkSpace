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
import com.synpower.service.MonitorService;

@RestController
@RequestMapping(value="/monitor")
public class MonitorController extends ErrorHandler{
	@Autowired
	private MonitorService monitorService;
	@RequestMapping(value="/basicInfo",method=RequestMethod.POST)
	public MessageBean getBasicInfo(@RequestBody String str) throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getBasicInfo(str,getSession());	
	}
	@RequestMapping(value="/curveSearch",method=RequestMethod.POST)
	public MessageBean getPriceBarByTime(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorService.getPriceBarByTime(str,getSession());
	}
	@RequestMapping(value="/curveSearchDate",method=RequestMethod.POST)
	public MessageBean getCurveSearchDate(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getCurveSearchDate(str,getSession());
	}
	@RequestMapping(value="/getProSearchDate",method=RequestMethod.POST)
	public MessageBean getProSearchDate(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getProSearchDate(str,getSession());
	}
	@RequestMapping(value="/getProSearch",method=RequestMethod.POST)
	public MessageBean getProSearch(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorService.getProSearch(str);
	}
	@RequestMapping(value="/getSinglePower",method=RequestMethod.POST)
	public MessageBean getSinglePower(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorService.getSinglePower(str);
	}
	@RequestMapping(value="/getTotalInfo",method=RequestMethod.POST)
	public MessageBean getPlantStatus(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getPlantStatus(str,getSession());
	}
	@RequestMapping(value="/getTodayInfo",method=RequestMethod.POST)
	public MessageBean getTodayInfo(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getTodayInfo(str,getSession());
	}
	@RequestMapping(value="/singelDevices",method=RequestMethod.POST)
	public MessageBean getSingelDevices(@RequestBody String str,HttpServletRequest request) throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getSingelDevicess(str,getSession(),request);
	}
	/** 
	  * @Title:  getTodayChargeEle 
	  * @Description:  光储多电站今日数据及状态
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月30日下午5:31:01
	*/ 
	@RequestMapping(value="/getTodayChargeEle",method=RequestMethod.POST)
	public MessageBean getTodayChargeEle(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getTodayChargeEle(str,getSession());
	}
	@RequestMapping(value="/getTotalPhotovoltaic",method=RequestMethod.POST)
	public MessageBean getTotalPhotovoltaic(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException{
		return monitorService.getTotalPhotovoltaic(str,getSession());
	}
	@RequestMapping(value="/getElectricityProfit",method=RequestMethod.POST)
	public MessageBean getElectricityProfit(@RequestBody String str)throws SessionException, ServiceException, SessionTimeoutException, ParseException{
		return monitorService.getElectricityProfit(str,getSession());
	}
}
