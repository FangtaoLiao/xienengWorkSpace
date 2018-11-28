package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataBoxChange;
import org.apache.ibatis.annotations.Param;

public interface DataBoxChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataBoxChange record);

    int insertSelective(DataBoxChange record);

    DataBoxChange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataBoxChange record);

    int updateByPrimaryKey(DataBoxChange record);

    //批量插入
	int insertBatch(List<DataBoxChange> ycRecordList);

	//根据设备id和data_time
	List<DataBoxChange> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
}