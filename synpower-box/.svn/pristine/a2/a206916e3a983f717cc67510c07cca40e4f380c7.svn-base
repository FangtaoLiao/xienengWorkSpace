package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.CollYxExpand;
import com.synpower.bean.DataYx;

@Repository
public interface CollYxExpandMapper {

	public int saveYXExcel(Map<String, Object> map);

	/**
	 * @Title: getYxFaultPoint
	 * @Description: 根据设备获取故障点的GUID集合
	 * @return: List<CollYxExpand>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月24日下午2:28:26
	 */
	public List<CollYxExpand> getYxFaultPoint(String dataMode);

	/**
	 * @Title: getYxFaultPoint
	 * @Description: 根据设备获取异常点的GUID集合
	 * @return: List<CollYxExpand>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月24日下午2:28:26
	 */
	public List<CollYxExpand> getYxExceptionPoint(String dataMode);

	/**
	 * @Title: getYxFaultPoint
	 * @Description: 根据设备获取待机点的GUID集合
	 * @return: List<CollYxExpand>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年11月24日下午2:28:26
	 */
	public List<CollYxExpand> getYxStandbyPoint(String dataMode);

	/**
	 * @Title: getAlarmList
	 * @Description: 得到实时告警列表
	 * @param list
	 * @return: List<Map<String,String>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月9日下午4:57:04
	 */
	public List<Map<String, String>> getAlarmList(Map map);

	/**
	 * @Title: getAlarmListCount
	 * @Description: 得到实时告警列表 总数
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月11日下午2:18:02
	 */
	public int getAlarmListCount(Map map);

	/**
	 * @Title: getAttentionAlarmCount
	 * @Description: 得到关注告警列表 总数
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月11日下午2:18:27
	 */
	public int getAttentionAlarmCount(Map map);

	/**
	 * @Title: getAttentionAlarm
	 * @Description: 得到关注告警列表
	 * @param map
	 * @return: List<Map<String,String>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月11日下午2:18:54
	 */
	public List<Map<String, String>> getAttentionAlarm(Map map);

	/**
	 * @Title: getAttentionAlarmWX
	 * @Description: TODO
	 * @param map
	 * @return: List<Map<String,String>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月18日上午10:07:02
	 */
	public List<Map<String, Object>> getAttentionAlarmWX(Map map);

	/**
	 * @Title: getAlarmListWX
	 * @Description: TODO
	 * @param map
	 * @return: List<Map<String,String>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月18日上午10:07:13
	 */
	public List<Map<String, Object>> getAlarmListWX(Map map);

	/**
	 * @Title: getAlarmById
	 * @Description: 获取告警
	 * @param map
	 * @return: DataYx
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月18日下午1:44:45
	 */
	public Map<String, Object> getAlarmById(Map map);

	/**
	 * @Title: getAlarmCountWX
	 * @Description: 告警类型分类统计实时
	 * @param map
	 * @return: List<Map<String,Object>>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月20日下午3:59:23
	 */
	public List<Map<String, String>> getAlarmCountWX(Map map);

	/**
	 * @Title: getYxValueCount
	 * @Description: 校验YX的值是都匹配
	 * @param collYxExpand
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年1月23日下午4:56:52
	 */
	public int getYxValueCount(CollYxExpand collYxExpand);

}