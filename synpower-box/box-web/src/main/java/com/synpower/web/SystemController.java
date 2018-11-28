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
import com.synpower.service.SystemService;

@RestController
@RequestMapping(value="/system")
public class SystemController extends ErrorHandler{
	@Autowired
	private SystemService systemService;
	/** 
	  * @Title:  getSystemInfo 
	  * @Description:  获取其他设置信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午3:41:05
	*/ 
	@RequestMapping(value="/getSystemInfo",method=RequestMethod.POST)
	public MessageBean getSystemInfo(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.getSystemInfo(str,getSession());
	}
	@RequestMapping(value="/updateSystemInfo",method=RequestMethod.POST)
	public MessageBean updateSystemInfo(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateSystemInfo(str,getSession());
	}
	/** 
	  * @Title:  getSystemBasic 
	  * @Description:  获取企业个性化设置信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午3:40:37
	*/ 
	@RequestMapping(value="/getSystemBasic",method=RequestMethod.POST)
	public MessageBean getSystemBasic(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.getSystemBasic(str,getSession());
	}
	/** 
	  * @Title:  updateSystemBasic 
	  * @Description:  修改企业个性化设置的文本信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午4:53:21
	*/ 
	@RequestMapping(value="/updateSystemBasic",method=RequestMethod.POST)
	public MessageBean updateSystemBasic(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateSystemBasic(str,getSession());
	}
	/** 
	  * @Title:  updateSystemLogo 
	  * @Description:  修改企业个性化设置的系统logo 
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午4:53:53
	*/ 
	@RequestMapping(value="/updateSystemLogo",method=RequestMethod.POST)
	public MessageBean updateSystemLogo(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateSystemLogo(request,getSession());
	}
	/** 
	  * @Title:  updateScreenLogo 
	  * @Description:  修改企业个性化设置的大屏logo
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午4:54:17
	*/ 
	@RequestMapping(value="/updateScreenLogo",method=RequestMethod.POST)
	public MessageBean updateScreenLogo(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateScreenLogo(request,getSession());
	}
	@RequestMapping(value="/updateLoginPhoto",method=RequestMethod.POST)
	public MessageBean updateLoginPhoto(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateLoginPhoto(request,getSession());
	}
	@RequestMapping(value="/updateLoginLogo",method=RequestMethod.POST)
	public MessageBean updateLoginLogo(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateLoginLogo(request,getSession());
	}
	@RequestMapping(value="/updateQrCode",method=RequestMethod.POST)
	public MessageBean updateQrCode(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateQrCode(request,getSession());
	}
	/** 
	  * @Title:  getSystemContent 
	  * @Description:  获取系统设置信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月18日下午3:09:22
	*/ 
	@RequestMapping(value="/getSystemContent",method=RequestMethod.POST)
	public MessageBean getSystemContent(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.getSystemContent(str,getSession());
	}
	@RequestMapping(value="/getAutomaticCalculation",method=RequestMethod.POST)
	public MessageBean getAutomaticCalculation(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.getAutomaticCalculation(str,getSession());
	}
	/** 
	  * @Title:  updatePropagandaPhoto 
	  * @Description:  修改组织的宣传图片 
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月2日下午2:30:16
	*/ 
	@RequestMapping(value="/updatePropagandaPhoto",method=RequestMethod.POST)
	public MessageBean updatePropagandaPhoto(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updatePropagandaPhoto(request,getSession());
	}
	/** 
	  * @Title:  getPropagandaPhoto 
	  * @Description:  获取组织的宣传图片 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月2日下午2:30:48
	*/ 
	@RequestMapping(value="/getPropagandaPhoto",method=RequestMethod.POST)
	public MessageBean getPropagandaPhoto(@RequestBody String str) throws SessionException, SessionTimeoutException, ServiceException{
		return systemService.getPropagandaPhoto(str,getSession());
	}
	/** 
	  * @Title:  updateOrgPhoto 
	  * @Description:  修改组织图片（大屏公司简介上的图片） 
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月2日下午3:02:23
	*/ 
	@RequestMapping(value="/updateOrgPhoto",method=RequestMethod.POST)
	public MessageBean updateOrgPhoto(HttpServletRequest request) throws SessionException,SessionTimeoutException,ServiceException{
		return systemService.updateOrgPhoto(request,getSession());
	}
}
