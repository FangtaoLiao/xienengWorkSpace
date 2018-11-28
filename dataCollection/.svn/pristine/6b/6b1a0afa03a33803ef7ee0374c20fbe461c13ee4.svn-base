package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataCombinerBox;
import org.apache.ibatis.annotations.Param;

public interface DataCombinerBoxMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataCombinerBox record);

    int insertSelective(DataCombinerBox record);

    DataCombinerBox selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataCombinerBox record);

    int updateByPrimaryKey(DataCombinerBox record);

	int insertBatch(List<DataCombinerBox> ycRecordList);

	List<DataCombinerBox> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
}