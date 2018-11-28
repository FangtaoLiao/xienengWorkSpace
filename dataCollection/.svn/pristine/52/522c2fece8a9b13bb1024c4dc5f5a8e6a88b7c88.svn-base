package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataStringInverter;
import com.synpowertech.dataCollectionJar.domain.DataStringInverterSimple;
import org.apache.ibatis.annotations.Param;

public interface DataStringInverterMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(DataStringInverter record);

	// 批量插入
	int insertBatch(List<DataStringInverter> recordList);

	int insertSelective(DataStringInverter record);

	DataStringInverter selectByPrimaryKey(Integer id);
	
	//根据设备id和data_time
	List<DataStringInverter> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);

	int updateByPrimaryKeySelective(DataStringInverter record);

	int updateByPrimaryKey(DataStringInverter record);
	
	//获取最近的总发电量记录
	List<DataStringInverterSimple> selectTimeResult();
}