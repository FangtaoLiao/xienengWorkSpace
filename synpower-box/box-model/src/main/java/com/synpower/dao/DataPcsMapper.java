package com.synpower.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.synpower.bean.DataElectricMeter;
import com.synpower.bean.DataCentralInverter;
import com.synpower.bean.DataPcs;

@Repository
public interface DataPcsMapper {
	public List<DataPcs> getVoltagePower(Map map);

	public Map<String, Object> getBatAndGridPower(String pId);
	/** 
	  * @Title:  getEnergyPlantDataForStorage 
	  * @Description:  入库PCS指定设备在指定时间段的记录明细
	  * 			        为了方便处理将DataPcs参数装载到DataElectricMeter中
	  * @return: List<DataPcs>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月30日下午1:55:30
	*/ 
	public List<DataElectricMeter>getEnergyPlantDataForStorage(Map map);
	/** 
	  * @Title:  getLastHistoryDataPcs 
	  * @Description:  获取指定时间段各pcs设备的最大值 
	  * 				为了方便处理将DataPcs参数装载到DataElectricMeter中
	  * @param map
	  * @return: List<DataElectricMeter>
	  * @lastEditor:  SP0011
	  * @lastEdit:  2018年3月30日下午2:08:45
	*/ 
	public List<DataElectricMeter>getLastHistoryDataPcs(Map map);
	

	public List<DataPcs> getTimesPower(Map<String, Object> paramMap);

	public List<DataPcs> getTimesPowerForMulti(Map<String, Object> paramMap);
}