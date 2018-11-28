package com.synpower.web;

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
import com.synpower.service.EquipmentService;

import jxl.read.biff.BiffException;

@RestController
@RequestMapping(value="/equipment")
public class EquipmentController extends ErrorHandler{
	@Autowired
	private EquipmentService equipmentService;
	@RequestMapping(value="/getEquipmentInfo",method=RequestMethod.POST)
	public MessageBean getEquipmentInfo(@RequestBody String str) throws ServiceException, SessionException, SessionTimeoutException {
		return equipmentService.getEquipmentInfo(str,getSession());
	}
	/** 
	  * @Title:  listEquipmentInfo 
	  * @Description:  获取逆变器列表 
	  * @param str
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:13:45
	*/ 
	@RequestMapping(value="/listEquipmentInfo",method=RequestMethod.POST)
	public MessageBean listEquipmentInfo(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.listEquipmentInfo(str,getSession());
	}
	/** 
	  * @Title:  updateStatus 
	  * @Description:  逆变器设备的启停用 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:14:25
	*/ 
	@RequestMapping(value="/updateStatus",method=RequestMethod.POST)
	public MessageBean updateStatus(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateStatus(str,getSession());
	}
	/** 
	  * @Title:  getEquipmentBasic 
	  * @Description:  获取一个逆变器的基本信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:15:07
	*/ 
	@RequestMapping(value="/getEquipmentBasic",method=RequestMethod.POST)
	public MessageBean getEquipmentBasic(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.getEquipmentBasic(str,getSession());
	}
	/** 
	  * @Title:  updateEquipmentBasic 
	  * @Description:  修改逆变器的基本信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:16:07
	*/ 
	@RequestMapping(value="/updateEquipmentBasic",method=RequestMethod.POST)
	public MessageBean updateEquipmentBasic(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateEquipmentBasic(str,getSession());
	}
	/** 
	  * @Title:  updateEquipmentPhoto 
	  * @Description:  修改逆变器的图片 
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:16:45
	*/ 
	@RequestMapping(value="/updateEquipmentPhoto",method=RequestMethod.POST)
	public MessageBean updateEquipmentPhoto(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return equipmentService.updateEquipmentPhoto(request,getSession());
	}
	/** 
	  * @Title:  readInterverExcel 
	  * @Description:  逆变器的导入
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws BiffException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:18:42
	*/ 
	@RequestMapping(value="readInterverExcel",method=RequestMethod.POST)
	public MessageBean readInterverExcel(HttpServletRequest request)throws SessionException, SessionTimeoutException, ServiceException,BiffException, IOException{
		return equipmentService.readInterverExcel(request,getSession());
	}
	/** 
	  * @Title:  insertInterver 
	  * @Description:  新增逆变器 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年2月28日下午2:58:33
	*/ 
	@RequestMapping(value="insertInterver",method=RequestMethod.POST)
	public MessageBean insertInterver(@RequestBody String str)throws SessionException, SessionTimeoutException,ServiceException{
		return equipmentService.insertInterver(str,getSession());
	}
	/** 
	  * @Title:  listEquipmentSub 
	  * @Description:  获取组件列表 
	  * @param str
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:19:02
	*/ 
	@RequestMapping(value="/listEquipmentSub",method=RequestMethod.POST)
	public MessageBean listEquipmentSub(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.listEquipmentSub(str,getSession());
	}
	/** 
	  * @Title:  updateSubStatus 
	  * @Description:  组件的启停用 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:19:22
	*/ 
	@RequestMapping(value="/updateSubStatus",method=RequestMethod.POST)
	public MessageBean updateSubStatus(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateSubStatus(str,getSession());
	}
	/** 
	  * @Title:  getSubBasic 
	  * @Description:  获取一个组件的基本信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:21:49
	*/ 
	@RequestMapping(value="/getSubBasic",method=RequestMethod.POST)
	public MessageBean getSubBasic(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.getSubBasic(str,getSession());
	}
	/** 
	  * @Title:  updateSubBasic 
	  * @Description:  修改组件的基本信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:22:23
	*/ 
	@RequestMapping(value="/updateSubBasic",method=RequestMethod.POST)
	public MessageBean updateSubBasic(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateSubBasic(str,getSession());
	}
	/** 
	  * @Title:  updateSubPhoto 
	  * @Description:  修改组件的图片 
	  * @param request
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:22:39
	*/ 
	@RequestMapping(value="/updateSubPhoto",method=RequestMethod.POST)
	public MessageBean updateSubPhoto(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return equipmentService.updateSubPhoto(request,getSession());
	}
	/** 
	  * @Title:  readSubExcel 
	  * @Description:  组件的导入 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException
	  * @throws BiffException
	  * @throws IOException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年12月27日下午4:24:20
	*/ 
	@RequestMapping(value="/readSubExcel",method=RequestMethod.POST)
	public MessageBean readSubExcel(HttpServletRequest request)throws SessionException, SessionTimeoutException, ServiceException,BiffException, IOException{
		return equipmentService.readSubExcel(request,getSession());
	}
	@RequestMapping(value="/insertSub",method=RequestMethod.POST)
	public MessageBean insertSub(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.insertSub(str,getSession());
	}
	@RequestMapping(value="/listCamera",method=RequestMethod.POST)
	public MessageBean listCamera(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.listCamera(str,getSession());
	}
	@RequestMapping(value="/updateCameraStatus",method=RequestMethod.POST)
	public MessageBean updateCameraStatus(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateCameraStatus(str,getSession());
	}
	@RequestMapping(value="/updateCambasic",method=RequestMethod.POST)
	public MessageBean updateCambasic(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.updateCambasic(str,getSession());
	}
	@RequestMapping(value="/updateCameraPhoto",method=RequestMethod.POST)
	public MessageBean updateCameraPhoto(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return equipmentService.updateCameraPhoto(request,getSession());
	}
	@RequestMapping(value="readCameraExcel",method=RequestMethod.POST)
	public MessageBean readCameraExcel(HttpServletRequest request)throws SessionException, SessionTimeoutException, ServiceException,BiffException, IOException{
		return equipmentService.readCameraExcel(request,getSession());
	}
	@RequestMapping(value="insertCamera",method=RequestMethod.POST)
	public MessageBean insertCamera(@RequestBody String str)throws SessionException, SessionTimeoutException, ServiceException{
		return equipmentService.insertCamera(str,getSession());
	}
}
