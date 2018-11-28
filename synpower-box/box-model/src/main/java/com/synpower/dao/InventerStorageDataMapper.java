package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.InventerStorageData;

@Repository
public interface InventerStorageDataMapper {
	public List<Map<String,Object>> getPowerPriceByDay(Map map);
	public InventerStorageData getDataCentralForStorage(Map map);
	public InventerStorageData getDataStringForStorage(Map map);
	public int  deteleDataForStotage(Map map);
	public int  insertDataForStotage(Map map);
	public List<Map<String, Object>> getEnergy(String plantId);
	public List<InventerStorageData> getEnergyPrice(String plantId);
	public List<Map<String, Object>> getEnergyPriceByMon(Map map);
	public List<Map<String, Object>> getEnergyPriceByYear(Map map);
	public List<Map<String, Object>> getSinglePowerByMon(Map map);
	public int getPowerByYear(Map map);
	public int getPowerByMonth(Map map);
	public Map getHistoryIncome(String pId);
	public String getPowerByYear2(Map map);
	public List<Map<String,Object>> getSinglePowerByMon2(Map map);
	/** 
	  * @Title:  getPlantRunReport 
	  * @Description: 电站运行报表折线图 
	  * @param map
	  * @return: List<Map<String,String>>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月7日上午10:03:36
	*/ 
	public List<Map<String, Object>>getPlantRunReportLine(Map map);
	
	/** 
	  * @Title:  getPlantRunReports 
	  * @Description:  电站对比报表 
	  * @param map
	  * @return: List<ElectricStorageData>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2017年12月8日下午4:35:56
	*/ 
	public List<Map<String, Object>>getPlantRunReports(Map map);
}