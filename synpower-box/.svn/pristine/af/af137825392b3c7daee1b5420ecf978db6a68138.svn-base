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
import com.synpower.service.PovertyService;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: PovertyController
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2018年5月29日下午4:54:26       SP0009             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/
@RestController
@RequestMapping(value="/poverty")
public class PovertyController extends ErrorHandler{
	@Autowired 
	PovertyService povertyService;
	
	/** 
	  * @Title:  getIncreaseIncome 
	  * @Description:  贫困户增收 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月30日上午9:56:11
	*/ 
	@RequestMapping(value="/IncreaseIncome",method=RequestMethod.POST)
	public MessageBean getIncreaseIncome(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return povertyService.getIncreaseIncome(jsonData,getSession());
	}
	/** 
	  * @Title:  getPoorHouseholdsInfo 
	  * @Description:  扶贫户信息和贫困户覆盖 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月30日上午9:56:31
	*/ 
	@RequestMapping(value="/getPoorHouseholdsInfo",method=RequestMethod.POST)
	public MessageBean getPoorHouseholdsInfo(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return povertyService.getPoorHouseholdsInfo(jsonData,getSession());
	}
	/** 
	  * @Title:  getNewsList 
	  * @Description:  获取扶贫大事件列表
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月30日下午4:37:39
	*/ 
	@RequestMapping(value="/getNewsList",method=RequestMethod.POST)
	public MessageBean getNewsList(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return povertyService.getNewsList(jsonData,getSession());
	}
	/** 
	  * @Title:  getNewsDetail 
	  * @Description:  获取扶贫大事件的详细信息 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月30日下午4:38:07
	*/ 
	@RequestMapping(value="/getNewsDetail",method=RequestMethod.POST)
	public MessageBean getNewsDetail(@RequestBody String jsonData)throws ServiceException, SessionException, SessionTimeoutException{
		return povertyService.getNewsDetail(jsonData,getSession());
	}
}
