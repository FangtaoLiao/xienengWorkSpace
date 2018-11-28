package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataElectricMeter;
import com.synpower.bean.ElectricStorageData;

@Repository
public interface DataElectricMeterMapper {
	public List<DataElectricMeter> getTimesPower(Map map);

	public List<DataElectricMeter> getCurrentPower(Map map);

	/**
	 * @Title: getDataElectricForStorage
	 * @Description: 获取今天电表的数据
	 * @param map
	 * @return: List<DataElectricMeter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月27日下午3:59:13
	 */
	public List<DataElectricMeter> getDataElectricForStorage(Map map);

	/**
	 * @Title: getBeforDataElectricForStorage
	 * @Description: 获取电表历史最大的数据
	 * @param map
	 * @return: List<DataElectricMeter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月27日下午3:59:27
	 */
	public List<DataElectricMeter> getBeforDataElectricForStorage(Map map);

	/**
	 * @Title: getTotalDataElectric
	 * @Description: 获取每个设备最新的发电量
	 * @param map
	 * @return: List<DataElectricMeter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月15日下午5:13:00
	 */
	public List<DataElectricMeter> getTotalDataElectric(Map map);

	/**
	 * @Title: getLastHistoryDataElectric
	 * @Description: 获取历史最大记录数
	 * @param map
	 * @return: List<DataElectricMeter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日上午10:42:51
	 */
	public List<DataElectricMeter> getLastHistoryDataElectric(Map map);

	/**
	 * @Title: getEnergyPlantDataForStorage
	 * @Description: 获取电表指定时间段的数据记录明细
	 * @param map
	 * @return: List<DataElectricMeter>
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日下午3:00:04
	 */
	public List<DataElectricMeter> getEnergyPlantDataForStorage(Map map);

	/**
	 * @Author lz
	 * @Description 能效 获取某日用电量 data_electric_meter
	 * @Date 14:44 2018/8/22
	 * @Param [paramMap] map 参数为deviceId，startTime，endTime
	 * @return java.util.HashMap
	 **/
	public Map<String, Object> getEnergyByDeviceIdAndTime(Map paramMap);

	/**
	 * @Author lz
	 * @Description //TODO
	 * @Date 14:15 2018/8/23
	 * @Param [deviceId]
	 * @return java.util.Map<java.lang.String,java.lang.Double>
	 **/
	public Map<String, Double> getNewestForFactor(Integer deviceId);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月24日上午9:30:03
	 * @Description -_-得到大于time最新的那一条数据
	 * @return
	 */
	public Map<String, Object> getElec(Map map);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月27日下午12:51:07
	 * @Description -_-根据时间和设备列表得到这些设备的 时间-功率
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPowerBydeviceList(Map map);

	/**
	 * 
	 * @author ybj
	 * @date 2018年8月30日上午10:21:33
	 * @Description -_-
	 * @param record
	 * @return
	 */
	int insert(DataElectricMeter record);

	/**
	 * 
	 * @author ybj
	 * @date 2018年9月3日下午6:20:28
	 * @Description -_- 得到某个设备在某个时间段内的最大最小功率因数
	 * @param map
	 * @return
	 */
	Map<String, Double> getMinAndMaxFactor(Map map);

}