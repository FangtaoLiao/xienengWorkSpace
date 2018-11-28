package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataTransformer;
import org.apache.ibatis.annotations.Param;

public interface DataTransformerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataTransformer record);
    
    //批量插入
    int insertBatch(List<DataTransformer> recordList);

    int insertSelective(DataTransformer record);

    DataTransformer selectByPrimaryKey(Integer id);
    
    DataTransformer selectByIdAndDataTime(Integer id);
    
    //根据设备id和data_time
  	List<DataTransformer> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
    
    int updateByPrimaryKeySelective(DataTransformer record);

    int updateByPrimaryKey(DataTransformer record);
}