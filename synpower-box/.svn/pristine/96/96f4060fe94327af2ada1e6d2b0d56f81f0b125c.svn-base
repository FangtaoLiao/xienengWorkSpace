package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataCentralInverter;

@Repository
public interface DataCentralInverterMapper {
	public List<DataCentralInverter> getTimesPower(Map map);

	public List<DataCentralInverter> getCurrentPowerOfCentralInvnter(Map map);

	public List<DataCentralInverter> getCurrentPowerOfStringInvnter(Map map);

	public List<DataCentralInverter> getEnergyPower(String deviceId, String startTime);

	/**
	 * @Title: getPowerOfCentralInvnter
	 * @Description: 集中式逆变器某个时间段直流交流功率
	 * @param map
	 * @return: List<DataCentralInverter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月11日下午6:42:17
	 */
	public List<DataCentralInverter> getPowerOfCentralInvnter(Map map);

	/**
	 * @Title: getPowerOfCentralInvnter
	 * @Description: 组串式逆变器某个时间段直流交流功率
	 * @param map
	 * @return: List<DataCentralInverter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月11日下午6:42:17
	 */
	public List<DataCentralInverter> getPowerOfStringInvnter(Map map);

	/**
	 * @Title: getDataForHistory
	 * @Description: 获取设备指定字段
	 * @param map
	 * @return: List<DataCentralInverter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2017年12月15日下午1:38:47
	 */
	public List<Map<String, Object>> getDataForHistory(Map map);

	/**
	 * @Title: getCurrPowerForDevices
	 * @Description: 获取多个设备的实时功率
	 * @param map
	 * @return: List<Map<String,Object>>
	 * @lastEditor: SP0007
	 * @lastEdit: 2017年12月20日下午8:15:39
	 */
	public List<Map<String, Object>> getCurrPowerForDevices(Map map);

}