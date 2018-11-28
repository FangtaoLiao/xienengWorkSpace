package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataElectricMeter;
import org.apache.ibatis.annotations.Param;

public interface DataElectricMeterMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(DataElectricMeter record);

	// 批量插入
	int insertBatch(List<DataElectricMeter> recordList);

	int insertSelective(DataElectricMeter record);

	DataElectricMeter selectByPrimaryKey(Integer id);

	 //根据设备id和data_time
	List<DataElectricMeter> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);

	int updateByPrimaryKeySelective(DataElectricMeter record);

	int updateByPrimaryKey(DataElectricMeter record);
}