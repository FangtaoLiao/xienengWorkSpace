package com.synpower.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface MonitorPlantService {

	/** 
	  * @Title:  singleBasicInfo 
	  * @Description:  单电站基本信息 
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月24日上午10:26:43
	*/ 
	public MessageBean singleBasicInfo(String jsonData)throws ServiceException ;

	/** 
	  * @Title:  deviceDetail 
	  * @Description:  设备详情
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月24日上午11:52:25
	*/ 
	public MessageBean deviceDetail(String jsonData)throws ServiceException ;
	
	/** 
	  * @Title:  deviceStatus 
	  * @Description:  设备 数据 、 状态 、 远程控制 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月25日上午10:38:40
	*/ 
	public MessageBean deviceStatus(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean updateValueOfGuid(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getPlantIncome(String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public MessageBean getPowerCurrentOfDevice(String jsonData) throws ServiceException;

	/** 
	  * @Title:  getCurrentIncomeOfPlant 
	  * @Description: 电站实时收益计算
	  * @param str
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午4:33:09
	*/ 
	public MessageBean getCurrentIncomeOfPlant(String jsonData) throws ServiceException;

	/** 
	  * @Title:  fuzzySearchPlant 
	  * @Description:  电站模糊搜索 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年11月27日下午5:13:07
	*/ 
	public MessageBean fuzzySearchPlant(String jsonData,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	

	public MessageBean plantDist(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean plantOpen(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	//public MessageBean getSimplePlist(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getMultiEtGenProfit(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  getPlantPower 
	  * @Description:  获取电站功率曲线 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月29日下午2:19:25
	*/ 
	public MessageBean getPlantPower(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  plantDist 
	  * @Description:  优化改进电站列表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月5日下午4:30:20
	*/ 
	//public MessageBean plantDist(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  plantStatusPower 
	  * @Description: 单电站状态和实时功率
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月7日下午5:17:24
	*/ 
	public MessageBean plantStatusPower(String jsonData) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  singlePlantDistribution 
	  * @Description: 单电站社会贡献 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月8日下午5:46:23
	*/ 
	public MessageBean singlePlantDistribution(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  snglePlantWeather 
	  * @Description:  单电站状态 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月8日下午5:49:21
	*/ 
	public MessageBean singlePlantStatus(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getPlantName(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getCollectorList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getInverterList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getCenInverterList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getPCSList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getBMSList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getMeterList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getConfBoxList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getBoxChangeList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getEnviroDetectorList(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	public MessageBean getBatCap(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getSingleEneProfit(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getCNTopo(String jsonData)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getSingleElecPro(String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public MessageBean getWeatherInfo(String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public MessageBean getSinglePowerCurve(String jsonData)throws SessionException, ServiceException, SessionTimeoutException, ParseException;

	public MessageBean getStoList(String jsonData, Session session, HttpServletRequest request) throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean getPlantOpen(String jsonData, Session session, HttpServletRequest request)throws ServiceException, SessionException, SessionTimeoutException, FileNotFoundException, IOException, ParseException ;
	
	public MessageBean getPvTopo(String str,Session session)throws ServiceException, SessionException, SessionTimeoutException;

    public MessageBean plantDist(String jsonData, Session session) throws SessionTimeoutException, SessionException, ServiceException;
}
