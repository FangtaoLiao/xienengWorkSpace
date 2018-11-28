package com.synpower.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synpower.bean.EnergyStorageData;

public interface EnergyStorageDataMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(EnergyStorageData record);

	int insertSelective(EnergyStorageData record);

	EnergyStorageData selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(EnergyStorageData record);

	int updateByPrimaryKey(EnergyStorageData record);

	/**
	 * @Title: getStoragePlantRunReport
	 * @Description: 储能运行报表数据
	 * @param map
	 * @return: List<Map<String,Object>>
	 * @lastEditor: SP0009
	 * @lastEdit: 2018年3月29日上午9:54:46
	 */
	public List<Map<String, Object>> getStoragePlantRunReport(Map map);

	/**
	 * @Title: getPlantLastRecord
	 * @Description: 得到电站最新的入库记录
	 * @param plantId
	 * @return: EnergyStorageData
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月28日下午5:52:06
	 */
	public EnergyStorageData getPlantLastRecord(Map map);

	/**
	 * @Title: insertDataForStotage
	 * @Description: 批量入库
	 * @param list
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月29日上午9:42:21
	 */
	public int insertDataForStotage(List<EnergyStorageData> list);

	/**
	 * @Title: deteleDataForStotage
	 * @Description: 删除指定电站在指定时间段的入库数据
	 * @param map
	 * @return: int
	 * @lastEditor: SP0011
	 * @lastEdit: 2018年3月29日上午9:43:06
	 */
	public int deteleDataForStotage(Map map);

	public EnergyStorageData getPlantLastRecord(int plantId);

	public List<Map<String, Object>> getInOutEnergyAndIncome(Map<String, String> powerPriceMap);

	public List<Map<String, Object>> getElectriRunReportLine(Map<String, Object> map);

	public Map<String, Object> getInOutEnergyForMulti(Map<String, Object> eleMap);

	public List<Map<String, Object>> getPowerPriceByDay(Map map);

	public List<Map<String, Object>> getStorageNegPosPrice(Map map);

	public List<Map<String, Object>> getPlantRunReportLine(Map map);

	
	public HashMap getEnergyByDeviceIdAndTime(HashMap paramMap);
	
}