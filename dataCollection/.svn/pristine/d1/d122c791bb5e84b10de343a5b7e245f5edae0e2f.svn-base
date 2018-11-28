package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataCentralInverter;
import org.apache.ibatis.annotations.Param;

public interface DataCentralInverterMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(DataCentralInverter record);

	// 批量插入
	int insertBatch(List<DataCentralInverter> recordList);

	int insertSelective(DataCentralInverter record);

	DataCentralInverter selectByPrimaryKey(Integer id);
	
	//根据设备id和data_time
	List<DataCentralInverter> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);

	int updateByPrimaryKeySelective(DataCentralInverter record);

	int updateByPrimaryKey(DataCentralInverter record);
}