package com.synpower.web;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
import com.synpower.service.PlantInfoService;
import com.synpower.util.DownloadUtils;
import com.synpower.util.PropertiesUtil;
import com.synpower.util.Util;

@RestController
@RequestMapping(value="/plantInfo")
public class PlantInfoController extends ErrorHandler{
	@Autowired
	private PlantInfoService plantInfoService;
//	@ResponseBody
//	@RequestMapping(value="/updatePlantInfo")
//	public String updatePlantInfo( String unit) {
//		String i=plantInfoService.updatePlantInfo(unit);
//		return  i; 
//	}
	/** 
	  * @Title:  listPlantInfo 
	  * @Description:  电站设置模块中电站列表显示 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:41:51
	*/ 
	@RequestMapping(value="/listPlantInfo",method=RequestMethod.POST)
	public MessageBean listPlantInfo(@RequestBody String str) throws SessionException, ServiceException, SessionTimeoutException{
		return plantInfoService.listPlantInfo(str,getSession());
	}
	/** 
	  * @Title:  getPlantInfo 
	  * @Description:  电站设置模块中的电站基本信息和业主信息
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:43:41
	*/ 
	@RequestMapping(value="/getPlantInfo",method=RequestMethod.POST)
	public MessageBean getPlantInfo(@RequestBody String str)throws SessionException,SessionTimeoutException, ServiceException{
		return plantInfoService.getPlantInfo(str,getSession());
	}
	/** 
	  * @Title:  updatePlantInfo 
	  * @Description:  电站设置模块中的修改电站信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:44:37
	*/ 
	@RequestMapping(value="/updatePlantInfo",method=RequestMethod.POST)
	public MessageBean updatePlantInfo(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException, ParseException{
		return plantInfoService.updatePlantInfo(str,getSession());
	}
	@RequestMapping(value="/insertPlantInfo",method=RequestMethod.POST)
	public MessageBean insertPlantInfo(@RequestBody String str) throws SessionException,SessionTimeoutException,ServiceException, IOException, ParseException {
		return plantInfoService.insertPlantInfo(str,getSession());
	}
	/** 
	  * @Title:  deletePlantUser 
	  * @Description:  电站设置模块中的删除业主信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:45:38
	*/ 
	@RequestMapping(value="/deletePlantUser",method=RequestMethod.POST)
	public MessageBean deletePlantUser(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.deletePlantUser(str,getSession());
	}
	/** 
	  * @Title:  updatePlantUser 
	  * @Description:  电站设置模块中的修改业主信息 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月2日下午5:46:38
	*/
	@RequestMapping(value="/updatePlantUser",method=RequestMethod.POST)
	public MessageBean updatePlantUser(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.updatePlantUser(str,getSession());
	}
	/**
	 * 
	  * @Title:  updatePlantUser 
	  * @Description:  TODO 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月15日下午4:38:43
	 */
	@RequestMapping(value="/insertPlantUser",method=RequestMethod.POST)
	public MessageBean insertPlantUser(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.insertPlantUser(str,getSession());
	}
	/** 
	  * @Title:  getSubsetOrg 
	  * @Description:  获取当前用户组织下面的组织名称 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2017年11月3日下午5:39:20
	*/ 
	@RequestMapping(value="/getSubsetOrg",method=RequestMethod.POST)
	public MessageBean getSubsetOrg(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getSubsetOrg(str,getSession());
	}
	@RequestMapping(value="/uploadPlantPhoto" ,method = RequestMethod.POST)
	public MessageBean uploadPlantPhoto(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.uploadPlantPhoto(request,getSession());
	}
	/*@RequestMapping(value="/uploadPlantPhotos" ,method = RequestMethod.POST,produces = {"text/html;charset=UTF-8"})
	public MessageBean uploadPlantPhotos(HttpServletRequest request)throws SessionException,SessionTimeoutException,ServiceException{
			return plantInfoService.uploadPlantPhotos(request,getSession());
	}*/
	@RequestMapping(value="/deletePlantPhoto",method=RequestMethod.POST)
	public MessageBean deletePlantPhoto(@RequestBody String str,HttpServletRequest req)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.deletePlantPhoto(str,getSession(),req);
	}
	@RequestMapping(value="/plantAscription",method=RequestMethod.POST)
	public MessageBean getPlantAscription(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantAscription(str,getSession());
	}
	@RequestMapping(value="/insertPlantAscription",method=RequestMethod.POST)
	public MessageBean insertPlantAscription(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.insertPlantAscription(str,getSession());
	}
	/** 
	  * @Title:  getPlantTreeByOrg 
	  * @Description:  电站树按组织分 
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月5日上午10:21:56
	*/ 
	@RequestMapping(value="/getPlantTreeByOrg",method=RequestMethod.POST)
	public MessageBean getPlantTreeByOrg(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantTreeByOrg(str,getSession());
	}
	/** 
	  * @Title:  getPlantTreeByArea 
	  * @Description:  电站树按区域分
	  * @param str
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年1月5日上午10:23:39
	*/ 
	@RequestMapping(value="/getPlantTreeByArea",method=RequestMethod.POST)
	public MessageBean getPlantTreeByArea(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantTreeByArea(str,getSession());
	}
	@RequestMapping(value="/getAllPlantType",method=RequestMethod.POST)
	public MessageBean getAllPlantType(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantType(str);
	}
	@RequestMapping(value="/getPlantWeather",method=RequestMethod.POST)
	public MessageBean getPlantWeather(@RequestBody String jsonData)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantWeather(jsonData,getSession());
	}
	@RequestMapping(value="/getUserType",method=RequestMethod.POST)
	public MessageBean getUserType(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getUserType(str,getSession());
	}
	@RequestMapping(value="/insertPlantOwner",method=RequestMethod.POST)
	public MessageBean insertPlantOwner(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.insertPlantOwner(str,getSession());
	}
	@RequestMapping(value="/getCnPlantPrice",method=RequestMethod.POST)
	public MessageBean getCnPlantPrice(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getCnPlantPrice(str,getSession());
	}
	@RequestMapping(value="/getPlantPriceDetail",method=RequestMethod.POST)
	public MessageBean getPlantPriceDetail(@RequestBody String str)throws SessionException,SessionTimeoutException,ServiceException{
		return plantInfoService.getPlantPriceDetail(str,getSession());
	}
}
