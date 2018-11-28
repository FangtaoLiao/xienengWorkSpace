package com.synpower.service;


import java.util.Map;

import com.synpower.lang.ServiceException;
import com.synpower.lang.SessionException;
import com.synpower.lang.SessionTimeoutException;
import com.synpower.msg.MessageBean;
import com.synpower.msg.session.Session;

public interface DeviceService {
	public MessageBean deviceService(Map<String, Object>map,Session session)throws SessionException, ServiceException;

	/** 
	  * @Title:  getCollectors 
	  * @Description:  获取数采集合  
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月5日下午5:03:58
	*/ 
	public MessageBean getCollectors(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException ;

	/** 
	  * @Title:  getInventerNum 
	  * @Description:  根据数采获取逆变器数量 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月9日下午3:32:41
	*/ 
	public MessageBean getInventerNum(String jsonData, Session session) throws ServiceException;

	/** 
	  * @Title:  updateDevice 
	  * @Description:  绑定数采 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午1:35:43
	*/ 
	public MessageBean updateDevice(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException ;
	
	public MessageBean updateDeviceNew(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException ;

	/** 
	  * @Title:  deleteDevice 
	  * @Description:  删除数采 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年1月15日下午3:18:49
	*/ 
	public MessageBean deleteDevice(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException;
	 /** * @Title:  deleteCollectors 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	 * @throws SessionTimeoutException 
	 * @throws ServiceException 
	 * @throws SessionException 
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月16日下午2:51:14
	*/
	public MessageBean deleteCollectors(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  updateCollector 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月16日下午5:07:59
	*/
	public MessageBean updateCollector(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getDevicesUnderColl 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月16日下午6:03:22
	*/
	public MessageBean getDevicesUnderColl(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  addCollector 
	  * @Description:  新建数采 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月17日上午10:22:11
	*/
	public MessageBean addCollector(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  get2ConnDevices 
	  * @Description:  获得将挂载设备 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月17日上午10:23:32
	*/
	public MessageBean get2ConnDevices(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getInverterList 
	  * @Description:  逆变器集合 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月17日上午11:26:02
	*/
	public MessageBean getInverterList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getDeviceInfo 
	  * @Description:  组串信息 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月17日下午3:07:22
	*/
	public MessageBean getModuleInfo(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getYCList 
	  * @Description:  遥测列表 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日上午9:47:16
	*/
	public MessageBean getYCList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getAllDeviceType 
	  * @Description:  设备型号类型点表层级选择 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日下午1:38:18
	*/
	public MessageBean getDeviceChoiceTree(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getYXList 
	  * @Description:  遥信列表 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月18日下午8:26:37
	*/
	public MessageBean getYXList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getYKList 
	  * @Description:  遥控列表 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月19日下午1:12:11
	*/
	public MessageBean getYKList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getAllCollNames 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月19日下午1:51:59
	*/
	public MessageBean getAllCollNames(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  updateCollAndSubDev 
	  * @Description: 更新数采及所属设备 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月20日上午10:56:42
	*/
	public MessageBean updateCollAndSubDev(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  updatePointTable 
	  * @Description:  保存设备信息并下发点表 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws ServiceException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月23日下午2:30:39
	*/ 
	public MessageBean updatePointTable(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;
	/** 
	  * @Title:  updateInEtModue 
	  * @Description:  编辑逆变器及组串
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月21日下午6:43:12
	*/
	public MessageBean updateInEtModue(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  addCamera 
	  * @Description:    新增摄像头
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日下午3:06:07
	*/
	public MessageBean addCamera(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getCameraList 
	  * @Description:  摄像头列表
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月23日下午3:46:41
	*/
	public MessageBean getCameraList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  delCamera 
	  * @Description:  删除摄像头 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月24日上午11:32:22
	*/
	public MessageBean delCamera(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  updateCamera 
	  * @Description: 更新摄像头
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月24日下午1:38:02
	*/
	public MessageBean updateCamera(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getCaModelList 
	  * @Description:  更新摄像头
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月25日上午11:04:48
	*/
	public MessageBean getCaModelList(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getLiveHLS 
	  * @Description:  高清播放地址
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日上午9:32:45
	*/
	public MessageBean getLiveHLS(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getCameraList4YS 
	  * @Description:  获取萤石摄像头列表
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日上午10:06:15
	*/
	public MessageBean getCameraListYS(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  plantIdofOrg 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日下午2:45:21
	*/
	public MessageBean plantIdofOrg4Device(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  plantIdofOrg4Camera 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年1月29日下午3:14:34
	*/
	public MessageBean plantIdofOrg4Camera(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getInverterModelforChoice 
	  * @Description:  获取所有逆变器信息供选择 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月1日上午10:35:53
	*/
	public MessageBean getInverterModelforChoice(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getModuleInfoforChoice 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月1日上午11:12:18
	*/
	public MessageBean getModuleInfoforChoice(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getVedioByTime 
	  * @Description:  TODO 
	  * @param jsonData
	  * @param session
	  * @return: MessageBean
	  * @lastEditor:  SP0012
	  * @lastEdit:  2018年3月2日上午10:34:03
	*/
	public MessageBean getVedioByTime(String jsonData, Session session) throws SessionException, ServiceException, SessionTimeoutException;

	/** 
	  * @Title:  getDevType 
	  * @Description:  获取设备类型 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月8日上午10:37:28
	*/ 
	public MessageBean getDevType(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException;
	
	/** 
	  * @Title:  getModel 
	  * @Description:  获取设备型号 
	  * @param jsonData
	  * @param session
	  * @return
	  * @throws SessionException
	  * @throws ServiceException
	  * @throws SessionTimeoutException: MessageBean
	  * @lastEditor:  SP0009
	  * @lastEdit:  2018年5月8日上午11:06:54
	*/ 
	public MessageBean getModel(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException;
	
	public MessageBean updatePointTableWx(String jsonData, Session session)throws SessionException, ServiceException, SessionTimeoutException;
}
