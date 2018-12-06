package com.synpower.web;

import com.synpower.handler.ErrorHandler;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.service.AlarmService;
import com.synpower.util.MessageBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: AlarmController
 * @Description: 告警
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年12月9日下午2:19:05   SP0011             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@RestController
@RequestMapping(value="/alarm")
public class AlarmController extends ErrorHandler{
	@Autowired 
	AlarmService alarmService;
	@RequestMapping(value="/alarmList",method=RequestMethod.POST)
	public MessageBean alarmList(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.alarmList(jsonData,getSession());
	}
	@RequestMapping(value="/alarmAttention",method=RequestMethod.POST)
	public MessageBean alarmAttention(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.alarmAttention(jsonData,getSession());
	}
	//**一致
	@RequestMapping(value="/alarmLine",method=RequestMethod.POST)
	public MessageBean alarmLine(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.alarmLine(jsonData,getSession());
	}
	@RequestMapping(value="/getalarmLight",method=RequestMethod.POST)
	public MessageBean getalarmLight(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.getalarmLight(jsonData,getSession());
	}
	@RequestMapping(value="/setalarmLight",method=RequestMethod.POST)
	public MessageBean setalarmLight(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.setalarmLight(jsonData,getSession());
	}
	@RequestMapping(value="/getAlertorOrder",method=RequestMethod.POST)
	public MessageBean getAlertorOrder(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.getAlertorOrder(jsonData,getSession());
	}
	@RequestMapping(value="/getConfirmAlarm",method=RequestMethod.POST)
	public MessageBean getConfirmAlarm(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.getConfirmAlarm(jsonData,getSession());
	}
	@RequestMapping(value="/getAlarmData",method=RequestMethod.POST)
	public MessageBean getAlarmData(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return alarmService.getAlarmData(jsonData,getSession());
	}

	@RequestMapping("/recentAlarmList")
	public MessageBean recentAlarmList(@RequestBody String json) {
//		List<Map<String, Object>> result = alarmService.recentAlarmList(getJsonMap());
//		MessageBean messageBean = MessageBeanUtil.getOkMB(result);
//		return messageBean;
//
		return alarmService.recentAlarmList(getJsonMap());
	}

	@RequestMapping("/recentAlarmListByLevel")
	public MessageBean recentAlarmListByLevel(@RequestBody String json) {
//		List<Map<String, Object>> result = alarmService.recentAlarmList(getJsonMap());
//		MessageBean messageBean = MessageBeanUtil.getOkMB(result);
//		return messageBean;
//
		return alarmService.recentAlarmListByLevel(getJsonMap());
	}

}
