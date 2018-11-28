package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollModelDetailMqtt;
import com.synpower.bean.CollYxExpand;

@Repository
public interface CollModelDetailMqttMapper {

	public int saveMQTTExcel(Map<String, Object> mqttMap);

	/**
	 * @Title: getGuidForDevice
	 * @Description: 找到设备的对应GUID
	 * @param map
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月22日上午9:47:46
	 */
	public List<CollModelDetailMqtt> getGuidForDevice(Map map);

	/**
	 * @Title: getYTGuidForDevice
	 * @Description: 根据设备id 得到遥调点位
	 * @param deviceId
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月24日上午11:41:25
	 */
	public List<CollModelDetailMqtt> getYTValueOfDevice(String dataModel);

	/**
	 * @Title: getSignalValueOfInveter
	 * @Description: TODO
	 * @param deviceId
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月25日上午11:12:20
	 */
	public List<CollModelDetailMqtt> getSignalValueOfInveter(String dataModel);

	/**
	 * @Title: getSignalValueOfElectric
	 * @Description: TODO
	 * @param deviceId
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月25日下午2:01:10
	 */
	public List<CollModelDetailMqtt> getSignalValueOfElectric(Map map);

	/**
	 * @Title: getYXValueOfDevice
	 * @Description: TODO
	 * @param deviceId
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月25日下午3:10:34
	 */
	public List<CollModelDetailMqtt> getYXValueOfDevice(String dataModel);

	/**
	 * @Title: getYKValueOfDevice
	 * @Description: TODO
	 * @param deviceId
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月25日下午4:02:35
	 */
	public List<CollModelDetailMqtt> getYKValueOfDevice(String dataModel);

	/**
	 * @Title: getDetailByGuid
	 * @Description: TODO
	 * @param signalGuid
	 * @return: CollModelDetailMqtt
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月25日下午5:05:30
	 */
	public CollModelDetailMqtt getDetailByGuid(String signalGuid);

	/**
	 * @Title: insertMq
	 * @Description: TODO
	 * @param mMap
	 * @return: int
	 * @lastEditor: SP0012
	 * @lastEdit: 2017年11月27日下午4:13:35
	 */
	public int insertMq(Map<String, Object> mMap);

	/**
	 * @Title: getYCList
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日上午9:56:39
	 */
	public List<CollModelDetailMqtt> getYCList(Map<String, Object> paraMap);

	/**
	 * @Title: getYXList
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月18日下午8:36:40
	 */
	public List<Map<String, String>> getYXList(Map<String, Object> paraMap);

	/**
	 * @Title: getYKList
	 * @Description: TODO
	 * @param paraMap
	 * @return: List<CollModelDetailMqtt>
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年1月19日下午1:13:40
	 */
	public List<Map<String, Object>> getYKList(Map<String, Object> paraMap);

	/**
	 * @Title: getTotalCount
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午5:57:16
	 */
	public Integer getTotalCount(Map<String, Object> paraMap);

	/**
	 * @Title: getTotalYX
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午6:00:26
	 */
	public Integer getTotalYX(Map<String, Object> paraMap);

	/**
	 * @Title: getTotalYK
	 * @Description: TODO
	 * @param paraMap
	 * @return: Integer
	 * @lastEditor: SP0012
	 * @lastEdit: 2018年2月1日下午6:03:03
	 */
	public Integer getTotalYK(Map<String, Object> paraMap);
}