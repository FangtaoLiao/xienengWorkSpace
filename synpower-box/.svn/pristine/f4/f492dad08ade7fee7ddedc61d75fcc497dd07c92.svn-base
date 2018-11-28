package com.synpower.web;

import java.io.FileNotFoundException;
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
import com.synpower.msg.MessageBean;
import com.synpower.service.ScreenService;

/*****************************************************************************
 * @Package: com.synpower.web
 * ClassName: ScreenController
 * @Description: TODO
 * @version
 *-----------------------------------------------------------------------------
 *     VERSION           TIME                BY           CHANGE/COMMENT
 *-----------------------------------------------------------------------------
 *      1.0    2017年11月6日下午2:46:22      SP0009             create
 *-----------------------------------------------------------------------------
 * @Copyright 2017 www.synpowertech.com Inc. All rights reserved. 
 * 注意：本内容仅限于成都协能共创科技内部传阅，禁止外泄以及用于其他的商业目的
 ******************************************************************************/

@RestController
@RequestMapping(value="/screen")
public class ScreenController extends ErrorHandler{
	@Autowired
	private ScreenService screenService;
	/** 
	  * @Title:  getPlantCount 
	  * @Description:  电站规模 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日上午9:50:38
	*/ 
	@RequestMapping(value="/getPlantCount",method=RequestMethod.POST)
	public MessageBean getPlantCount(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException {
		return screenService.getPlantCount(str,getSession());
	}
	/** 
	  * @Title:  getOrgIntroduction 
	  * @Description:  公司简介 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月7日下午3:14:40
	*/ 
	@RequestMapping(value="/introduction",method=RequestMethod.POST)
	public MessageBean getOrgIntroduction(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException {
		return screenService.getOrgIntroduction(str,getSession());
	}
	/** 
	  * @Title:  listPlantDistribution 
	  * @Description:  电站分布 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月8日下午3:47:30
	*/ 
	@RequestMapping(value="/distribution",method=RequestMethod.POST)
	public MessageBean listPlantDistribution(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException{
		return screenService.listPlantDistribution(str,getSession());
	}
	
	/** 
	  * @Title:  listPlantDistribution 
	  * @Description:  大屏获取电站基本信息 
	  * @param String(jsonData)
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月8日下午3:47:30
	*/
	@RequestMapping(value="/basicStatic",method=RequestMethod.POST)
	public MessageBean basicStatic(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		return screenService.getBasicStatics(jsonData,getSession(), request);
	}
	
	/** 
	  * @Title:  listPlantDistribution 
	  * @Description:  大屏获取电站运营信息
	  * @param String(jsonData)
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月8日下午3:47:30
	*/
	@RequestMapping(value="/business",method=RequestMethod.POST)
	public MessageBean business(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		return screenService.getBusiness(jsonData,getSession(), request);
	}
	
	/** 
	  * @Title:  listPlantDistribution 
	  * @Description:  大屏获取电站分布信息
	  * @param String(jsonData)
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月8日下午3:47:30
	*/
	@RequestMapping(value="/plantDistribution",method=RequestMethod.POST)
	public MessageBean plantDistribution(@RequestBody String jsonData,HttpServletRequest request)throws SessionException, SessionTimeoutException,ServiceException{
		return screenService.getPlantDistribution(jsonData,getSession(), request);
	}
	
	/**
	 * 
	  * @Title:  getpowerBar 
	  * @Description:  发电量统计 
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月18日上午10:23:23
	 */
	@RequestMapping(value="/generationStatics")
	public MessageBean getpowerBar(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException{
		return screenService.getPowerBar(str,getSession());
	}
	
	/**
	 * 
	  * @Title:  getContribution 
	  * @Description:  社会贡献
	  * @param str
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月18日上午10:22:20
	 */
	@RequestMapping(value="/contribution")
	public MessageBean getContribution(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException, IOException{
		return screenService.getContribution(str,getSession());
	}
	
	/**
	 * 
	  * @Title:  dynamicStatics 
	  * @Description:  弹窗动态信息 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月18日上午10:21:58
	 */
	@RequestMapping(value="/dynamicStatics",method=RequestMethod.POST)
	public MessageBean dynamicStatics(@RequestBody String jsonData)throws SessionException, SessionTimeoutException,ServiceException {
		return screenService.dynamicStatics(jsonData,getSession());
	}
	
	/**
	 * 
	  * @Title:  plantInfo 
	  * @Description:   弹窗静态信息 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月18日上午10:21:38
	 */
	@RequestMapping(value="/plantInfo",method=RequestMethod.POST)
	public MessageBean plantInfo(@RequestBody String jsonData)throws SessionException, SessionTimeoutException,ServiceException {
		return screenService.plantInfo(jsonData);
	}
	
	/**
	 * 
	  * @Title:  getAllPlant 
	  * @Description:  所有电站信息 
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月18日上午10:21:23
	 */
	@RequestMapping(value="/getAllPlant",method=RequestMethod.POST)
	public MessageBean getAllPlant(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		MessageBean msg= screenService.getAllPlant(getSession(),jsonData,request);
		return msg;
	}
	
	/**
	 * 
	  * @Title:  districtManage 
	  * @Description:  区域信息管理
	  * @param jsonData
	  * @param request
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws FileNotFoundException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2017年11月18日上午10:21:08
	 */
	@RequestMapping(value="/districtManage",method=RequestMethod.POST)
	public MessageBean districtManage(@RequestBody String jsonData,HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException {
		MessageBean msg= screenService.districtManage(getSession(),jsonData,request);
		return msg;
	}
	@RequestMapping(value="/getLogoAndName",method=RequestMethod.POST)
	public MessageBean getLogoAndName(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException{
		return screenService.getLogoAndName(str,getSession());
	}
}
