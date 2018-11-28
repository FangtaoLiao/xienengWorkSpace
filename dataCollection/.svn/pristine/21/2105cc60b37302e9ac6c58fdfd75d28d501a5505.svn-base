package com.synpowertech.dataCollectionJar.dao;


import com.synpowertech.dataCollectionJar.domain.DataEnvMonitor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataEnvMonitorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataEnvMonitor record);

    int insertSelective(DataEnvMonitor record);

    DataEnvMonitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataEnvMonitor record);

    int updateByPrimaryKey(DataEnvMonitor record);

    List<DataEnvMonitor> selectByIdAndDataTime(@Param("deviceId") Integer deviceId , @Param("dataTime") Long dataTime);

    int insertBatch(List<DataEnvMonitor> ycRecordList);

}