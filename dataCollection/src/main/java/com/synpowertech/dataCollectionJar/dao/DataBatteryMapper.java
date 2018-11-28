package com.synpowertech.dataCollectionJar.dao;

import java.util.List;

import com.synpowertech.dataCollectionJar.domain.DataBattery;
import org.apache.ibatis.annotations.Param;

public interface DataBatteryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataBattery record);

    int insertSelective(DataBattery record);

    DataBattery selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataBattery record);

    int updateByPrimaryKey(DataBattery record);

	int insertBatch(List<DataBattery> ycRecordList);

	List<DataBattery> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);
}