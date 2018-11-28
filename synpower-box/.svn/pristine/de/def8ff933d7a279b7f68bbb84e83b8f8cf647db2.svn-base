package com.synpower.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.synpower.bean.SysWeather;
import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;
import com.synpower.msg.session.User;

public interface PlantInfoService {

//	String updatePlantInfo(String str);
	 
	public MessageBean listPlantInfo(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getPlantInfo(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException;
	
	public MessageBean updatePlantInfo(String str,Session session)throws SessionException, SessionTimeoutException, ServiceException, ParseException;
	
	public MessageBean insertPlantInfo(String str,Session session) throws SessionException, SessionTimeoutException, ServiceException, IOException, ParseException ;
	
	public MessageBean deletePlantUser(String str,Session session)throws SessionException, SessionTimeoutException,ServiceException;
	
	public MessageBean updatePlantUser(String str,Session session)throws SessionException, SessionTimeoutException,ServiceException;
	
	public MessageBean getSubsetOrg(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean uploadPlantPhoto(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean uploadPlantPhotos(HttpServletRequest request,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	
	public MessageBean deletePlantPhoto(String str,Session session,HttpServletRequest req)throws SessionException,SessionTimeoutException,ServiceException;
	
	public List<Integer> getPlantsForUser(String tokenId,Session session,int type)throws SessionException,SessionTimeoutException;
	public MessageBean getPlantAscription(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean insertPlantAscription(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean getPlantTreeByOrg(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean getPlantTreeByArea(String str,Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public List<Integer> getPlantsForUser(User u,int type) throws SessionException, SessionTimeoutException ;

	/** 
	  * @Title:  getPlantType 
	  * @Description:  获取电站类型 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月10日下午5:34:43
	*/ 
	public MessageBean getPlantType(String jsonData)throws SessionException,SessionTimeoutException,ServiceException;

	/** 
	  * @Title:  deleteWXPlantPhoto 
	  * @Description:  删除电站图片 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月11日下午5:32:50
	*/ 
	public MessageBean deleteWXPlantPhoto(String jsonData,Session session)throws SessionException,SessionTimeoutException,ServiceException;

	/** 
	  * @Title:  wxplantInfo 
	  * @Description:  获取电站信息微信 
	  * @param jsonData
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月12日上午11:32:44
	*/ 
	public MessageBean wxplantInfo(String jsonData)throws SessionException,SessionTimeoutException,ServiceException;

	/** 
	  * @Title:  insertPlantUser 
	  * @Description:  TODO 
	  * @param str
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月15日下午4:39:06
	*/
	public MessageBean insertPlantUser(String str, Session session) throws SessionException,SessionTimeoutException,ServiceException;
	/**   
	* @Title:  wxupdatePlantInfo 
	  * @Description:  更改电站信息 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月16日下午5:37:00
	*/ 
	public MessageBean wxupdatePlantInfo(String jsonData, Session session)throws SessionException,SessionTimeoutException,ServiceException;

	/** 
	  * @Title:  wxinsertPlantInfo 
	  * @Description:  创建电站微信接口 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月17日下午3:04:41
	*/ 
	public MessageBean wxinsertPlantInfo(String jsonData, Session session)throws SessionException,SessionTimeoutException,ServiceException;
	/** 
	  * @Title:  getPlantTodayWeather 
	  * @Description:  获取电站当日信息 
	  * @param plantId
	  * @return
	  * @throws ServiceException: SysWeather
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月23日上午10:32:15
	*/ 
	public SysWeather getPlantTodayWeather(int plantId) throws ServiceException;
	/** 
	  * @Title:  getPlantThreeWeather 
	  * @Description:  TODO 
	  * @param plantId
	  * @return
	  * @throws ServiceException: SysWeather
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月23日上午10:36:17
	*/ 
	public List<SysWeather> getPlantThreeWeather(int plantId) throws ServiceException;

	/** 
	  * @Title:  getPlantWeather 
	  * @Description:  获取电站天气
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月23日上午10:53:08
	*/ 
	public MessageBean getPlantWeather(String jsonData, Session session)throws ServiceException;

	/** 
	  * @Title:  getDefaultPowerPrice 
	  * @Description:  获取默认电价 
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年2月11日下午2:10:14
	*/ 
	public MessageBean getDefaultPowerPrice();
	public MessageBean getUserType(String str, Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean insertPlantOwner(String str, Session session)throws SessionException,SessionTimeoutException,ServiceException;
	/** 
	  * @Title:  getPlantsTypeForUser 
	  * @Description:  获取用户的电站列表并按电站类型分类 
	  * @param tokenId
	  * @param session
	  * @param type
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: Map<String,List<Integer>>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月29日下午5:22:28
	*/ 
	public Map<String, List<Integer>>getPlantsTypeForUser(String tokenId,Session session,int type)throws SessionException,SessionTimeoutException,ServiceException;
	/** 
	  * @Title:  getPlantTypeForUser 
	  * @Description:  获取用户的电站类型最后分类 
	  * @param tokenId
	  * @param session
	  * @param type
	  * @return
	  * @throws SessionException
	  * @throws SessionTimeoutException
	  * @throws ServiceException: String
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月29日下午5:23:23
	*/ 
	public String getPlantTypeForUser(String tokenId,Session session,int type)throws SessionException,SessionTimeoutException,ServiceException;
	public String getPlantTypeForUser(User u, int type,int serviceType)
			throws SessionException, SessionTimeoutException, ServiceException ;
	public String getPlantTypeForUserWX(User u, int type)
			throws SessionException, SessionTimeoutException, ServiceException ;
	public MessageBean getCnPlantPrice(String str, Session session)throws SessionException,SessionTimeoutException,ServiceException;
	public MessageBean getPlantPriceDetail(String str, Session session)throws SessionException,SessionTimeoutException,ServiceException;
}
