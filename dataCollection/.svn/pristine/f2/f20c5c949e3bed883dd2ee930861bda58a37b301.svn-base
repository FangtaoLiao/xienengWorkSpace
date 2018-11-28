package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataPcs;
import org.apache.ibatis.annotations.Param;

public interface DataPcsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataPcs record);

    int insertSelective(DataPcs record);

    DataPcs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataPcs record);

    int updateByPrimaryKey(DataPcs record);

	int insertBatch(List<DataPcs> ycRecordList);

	List<DataPcs> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
}