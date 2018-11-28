package com.synpower.web;

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
import com.synpower.service.AlarmService;
import com.synpower.service.PlantReportService;
import com.synpower.service.WXAlarmService;

@RestController
@RequestMapping(value="/alarmwx")
public class WXAlarmController extends ErrorHandler{
	@Autowired
	private WXAlarmService wxAlarmService;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private PlantReportService plantReportService;
	@RequestMapping(value="/getAlarm",method=RequestMethod.POST)
	public MessageBean getAlarm(@RequestBody String jsonData)throws  ServiceException, SessionException, SessionTimeoutException{
		return wxAlarmService.getAlarm(jsonData,getSession());
	}
	@RequestMapping(value="/clearFollow",method=RequestMethod.POST)
	public MessageBean clearFollow(@RequestBody String jsonData)throws  ServiceException, SessionException, SessionTimeoutException{
		return alarmService.delAllAlarmAttention(jsonData,getSession());
	}
	@RequestMapping(value="/searchPlant",method=RequestMethod.POST)
	public MessageBean searchPlant(@RequestBody String jsonData)throws  ServiceException, SessionException, SessionTimeoutException{
		return plantReportService.fuzzySearchPlant(jsonData,getSession());
	}
	@RequestMapping(value="/followEvent",method=RequestMethod.POST)
	public MessageBean followEvent(@RequestBody String jsonData)throws  ServiceException, SessionException, SessionTimeoutException{
		return wxAlarmService.followEvent(jsonData,getSession());
	}
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	public MessageBean detail(@RequestBody String jsonData)throws  ServiceException, SessionException, SessionTimeoutException{
		return wxAlarmService.detail(jsonData,getSession());
	}
}
