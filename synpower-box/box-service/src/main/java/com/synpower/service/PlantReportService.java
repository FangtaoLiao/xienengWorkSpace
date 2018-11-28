package com.synpower.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface PlantReportService {

	/** 
	  * @Title:  fuzzySearchPlant 
	  * @Description:  电站模糊查询 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月6日下午2:30:34
	*/ 
	public MessageBean fuzzySearchPlant(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;
	
	/**
	 * @Title:  fuzzySearchPlantByAreaId 
	 * @Description:  电站模糊查询 
	 * @param jsonData
	 * @param session
	 * @return
	 * @throws ServiceException
	 * @throws SessionException
	 * @throws SessionTimeoutException
	 */
	public MessageBean fuzzySearchPlantByAreaId(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  runReportTable 
	  * @Description:  单电站运行报表 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月7日下午1:40:07
	*/ 
	public MessageBean runReportTable(String jsonData)throws ServiceException;
	
	/** 
	  * @Title:  runReportLine 
	  * @Description:  单电站运行折线图 
	  * @param jsonData
	  * @return
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月7日下午1:40:19
	*/ 
	public MessageBean runReportLine(String jsonData)throws ServiceException;
	
	/** 
	  * @Title:  areaDistribution 
	  * @Description:  地图上下钻取 
	  * @param map
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日上午10:56:31
	*/ 
	public MessageBean getAreaDistribution(Map<String, Object> map,Session session) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  saveUserSet 
	  * @Description:保存用户的电站报表设置 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午2:37:26
	*/ 
	public MessageBean saveUserSet(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  runReports 
	  * @Description:  电站对比 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午3:33:31
	*/ 
	public MessageBean runReportsLine(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException ;
	
	/** 
	  * @Title:  runReportsTable 
	  * @Description:  电站对比报表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月9日上午10:52:15
	*/ 
	public MessageBean runReportsTable(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException ;
	/** 
	  * @Title:  savePersonalSet 
	  * @Description:  历史报表保存用户设置 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月13日下午1:44:51
	*/ 
	public MessageBean savePersonalSet(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException ;

	/** 
	  * @Title:  getDeviceForPlant 
	  * @Description:  根据电站获取逆变器集合 
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月14日下午2:32:28
	*/ 
	public MessageBean getDeviceForPlant(String jsonData,Session session) throws ServiceException, SessionException, SessionTimeoutException ;
	
	/** 
	  * @Title:  getHistroyTime 
	  * @Description:  获取用户历史报表设置时间
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月15日上午11:15:41
	*/ 
	public MessageBean getHistoryTime(String jsonData,Session session) throws ServiceException, SessionException, SessionTimeoutException ;

	/** 
	  * @Title:  getYXGuidForInventers 
	  * @Description:  获取设备的归一的遥测信号点 
	  * @param jsonData
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月14日下午3:47:26
	*/ 
	public MessageBean getYXGuidForInventers(@RequestBody String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	/** 
	  * @Title:  getHistoryLine 
	  * @Description:  生成历史数据曲线 
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月14日下午5:46:02
	*/ 
	public MessageBean getHistoryLine(String jsonData,Session session)throws ServiceException, SessionException, SessionTimeoutException;

	public MessageBean reviewData(String jsonData, Session session)throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  getPlantListByArea 
	  * @Description:  获取地区下的电站ID集合 
	  * @param map
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: List<Integer>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月6日上午10:27:35
	*/ 
	public List<Integer> getPlantListByArea(Map<String, Object> map,Session session) throws ServiceException, SessionException, SessionTimeoutException;
	/** 
	  * @Title:  storageRunReportsTable 
	  * @Description:  储能电站运行报表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws ServiceException
	  * @throws SessionException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年3月28日下午4:18:08
	*/ 
	public MessageBean storageRunReportsTable(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException ;
	public MessageBean storageRunReportsLine(String jsonData, Session session) throws ServiceException, SessionException, SessionTimeoutException ;

	/** 
	  * @Title:  electricReportLine 
	  * @Description:  电费报表折线
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月30日上午9:55:22
	*/
	public MessageBean electricReportLine(String jsonData)throws ServiceException;

	/** 
	  * @Title:  electricReportTable 
	  * @Description:  电费报表折线表格 
	  * @param jsonData
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月30日上午11:35:10
	*/
	public MessageBean electricReportTable(String jsonData)throws ServiceException;
}
